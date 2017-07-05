package ps.util;

//-----( IS Java Code Template v1.2
//-----( CREATED: 2008-01-08 21:00:00 EDT
//-----( ON-HOST: MCRSINGH.AME.ad.sag

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.util.*;
import java.io.*;
import com.wm.lang.ns.*;

// --- <<IS-END-IMPORTS>> ---

public final class idata

{
  // ---( internal utility methods )---

  final static idata _instance = new idata();

  static idata _newInstance()
  {
    return new idata();
  }

  static idata _cast(Object o)
  {
    return (idata) o;
  }

  // ---( server methods )---

  public static final void createClone(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(createClone)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] record:0:required document
    // [i] field:0:optional depth {"shallow","deep"}
    // [o] record:0:required clone
    IDataCursor cursor = pipeline.getCursor();
    IData document = IDataUtil.getIData(cursor, "document");
    String depth = IDataUtil.getString(cursor, "depth");
    try
    {
      if (document != null)
      {
        IData clonedData = null;
        if ("deep".equalsIgnoreCase(depth))
        {
          clonedData = IDataUtil.deepClone(document);
        }
        else
        {
          clonedData = IDataUtil.clone(document);
        }
        IDataUtil.put(cursor, "clone", clonedData);
      }
    }
    catch (IOException ioe)
    {
      throw new ServiceException(ioe);
    }
    finally
    {
      cursor.destroy();
    }
    // --- <<IS-END>> ---

  }

  public static final void createFromIniFile(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(createFromIniFile)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] field:0:required iniFileName
    // [o] record:0:required document
    IDataCursor cursor = pipeline.getCursor();
    String iniFilename = IDataUtil.getString(cursor, "iniFileName");

    IData autoConfigDoc = IDataFactory.create();
    IDataCursor autoConfigDocCursor = autoConfigDoc.getCursor();

    try
    {
      FileReader source = new FileReader(iniFilename);
      BufferedReader dataIn = new BufferedReader(source);
      String section = null;
      IData idata = null;
      IDataCursor idataCursor = null;
      for (;;)
      {
        String textline = dataIn.readLine();
        if (textline == null)
        {
          break; // File end
        }
        if (textline.length() == 0)
        {
          continue; // ignore Empty line
        }
        else if (textline.charAt(0) == ';')
        {
          continue; // ignore comments
        }
        if (textline.charAt(0) == '[')
        {
          // section
          if (idataCursor != null)
          {
            idataCursor.destroy();
            Object temp = IDataUtil.get(autoConfigDocCursor, section);
            if (temp != null)
            {
              // duplicated sections are changed to document list
              if (temp instanceof IData)
              {
                // only 1 entry of same section name exists
                IData[] tempIDataArray = new IData[2];
                tempIDataArray[0] = IDataUtil.clone((IData) temp);
                tempIDataArray[1] = IDataUtil.clone(idata);
                IDataUtil.remove(autoConfigDocCursor, section);
                IDataUtil.put(autoConfigDocCursor, section, tempIDataArray);
              }
              else if (temp instanceof IData[])
              {
                // multiple entries of same section name exist
                IData[] tempIDataArray = new IData[1 + ((IData[]) temp).length];
                int i;
                for (i = 0; i < ((IData[]) temp).length; i++)
                {
                  tempIDataArray[i] = IDataUtil.clone(((IData[]) temp)[i]);
                }
                tempIDataArray[i] = IDataUtil.clone(idata);
                IDataUtil.remove(autoConfigDocCursor, section);
                IDataUtil.put(autoConfigDocCursor, section, tempIDataArray);
              }
              else
              {
                // this case will never happen.
                IDataUtil.remove(autoConfigDocCursor, section);
                IDataUtil.put(autoConfigDocCursor, section, idata);
              }
            }
            else
            {
              IDataUtil.put(autoConfigDocCursor, section, idata);
            }
          }
          section = textline.substring(1, textline.length() - 1);
          section = section.trim();
          // temporary cursor
          idata = IDataFactory.create();
          idataCursor = idata.getCursor();
        }
        else
        {
          // then keys
          int offset = textline.indexOf("=");
          if (offset > 0)
          {
            // property keys
            String parseleft = textline.substring(0, offset);
            parseleft = parseleft.trim();
            String parseright = textline.substring(offset + 1);
            parseright = parseright.trim();
            IDataUtil.put(idataCursor, parseleft, parseright);
          }
        }
      }
      if (idataCursor != null)
      {
        idataCursor.destroy();
        IDataUtil.put(autoConfigDocCursor, section, idata);
      }
      autoConfigDocCursor.destroy();
      IDataUtil.put(cursor, "document", autoConfigDoc);
    }
    catch (IOException e)
    {
      throw new ServiceException(e);
    }
    finally
    {
      cursor.destroy();
    }
    // --- <<IS-END>> ---

  }

  public static final void getNestedRecord(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(getNestedRecord)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] record:0:required inputDoc
    // [i] field:0:required nestedRecordName
    // [o] record:0:required nestedRecord
    IDataCursor cursor = pipeline.getCursor();

    IData document = IDataUtil.getIData(cursor, "inputDoc");
    String documentName = IDataUtil.getString(cursor, "nestedRecordName");

    if (document == null || documentName == null)
    {
      cursor.destroy();
      return;
    }

    IDataCursor documentCursor = document.getCursor();
    IDataUtil.put(cursor, "nestedRecord", IDataUtil.get(documentCursor, documentName));

    documentCursor.destroy();
    cursor.destroy();

    // --- <<IS-END>> ---

  }

  public static final void removeNullFields(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(removeNullFields)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] record:0:required inputDocument
    // [i] field:0:optional removeEmpty {"false","true"}
    // [i] field:0:optional trimFields {"false","true"}
    // [o] record:0:required outputDocument
    IDataCursor cursor = pipeline.getCursor();
    IData input = IDataUtil.getIData(cursor, "inputDocument");
    boolean trimFields = IDataUtil.getBoolean(cursor, "trimFields", false);
    boolean removeEmpty = IDataUtil.getBoolean(cursor, "removeEmpty", false);
    IDataUtil.put(cursor, "outputDocument", removeNullFields(input, removeEmpty, trimFields));
    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void transformAll(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(transformAll)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] field:0:required serviceName
    // [i] record:0:required inputs
    // [i] record:0:optional otherInputs
    // [i] field:0:required inputVariableName
    // [i] field:0:optional outputVariableName
    // [i] field:0:optional removeNulls {"false","true"}
    // [o] record:0:required outputs
    IDataCursor cursor = pipeline.getCursor();
    IData inputs = IDataUtil.getIData(cursor, "inputs");
    IData otherInputs = IDataUtil.getIData(cursor, "otherInputs");
    String inputVariableName = IDataUtil.getString(cursor, "inputVariableName");
    boolean removeNulls = IDataUtil.getBoolean(cursor, "removeNulls", false);
    if (inputVariableName == null || inputVariableName.length() == 0)
    {
      throw new ServiceException("Missing input 'inputVariableName'");
    }
    String outputVariableName = IDataUtil.getString(cursor, "outputVariableName");
    IDataCursor inputsCursor = inputs.getCursor();
    String serviceName = IDataUtil.getString(cursor, "serviceName");
    IData outputs = IDataFactory.create();
    IDataCursor outputsCursor = outputs.getCursor();
    try
    {
      while (inputsCursor.next())
      {
        IData serviceInputs = IDataFactory.create();
        IDataUtil.copy(otherInputs, serviceInputs);
        IDataCursor serviceInputsCursor = serviceInputs.getCursor();
        IDataUtil.put(serviceInputsCursor, inputVariableName, inputsCursor.getValue());
        serviceInputsCursor.destroy();
        IData out = Service.doInvoke(NSName.create(serviceName), serviceInputs);
        IDataCursor outCursor = out.getCursor();
        Object object = IDataUtil.get(outCursor, outputVariableName);
        if (object != null || removeNulls == false)
        {
          outputsCursor.insertAfter(inputsCursor.getKey(), object);
        }
        outCursor.destroy();
      }
    }
    catch (Exception e)
    {
      throw new ServiceException(e);
    }
    finally
    {
      inputsCursor.destroy();
      outputsCursor.destroy();
      IDataUtil.put(cursor, "outputs", outputs);
      cursor.destroy();
    }
    // --- <<IS-END>> ---

  }

  public static final void trimFields(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(trimFields)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] record:0:required inputDocument
    // [i] field:0:optional removeEmptyFields {"false","true"}
    // [o] record:0:required outputDocument
    IDataCursor cursor = pipeline.getCursor();
    boolean removeEmptyFields = false;

    IData inputDocument = IDataUtil.getIData(cursor, "inputDocument");

    if (inputDocument == null)
    {
      throw new ServiceException("Missing input 'inputDocument'");
    }

    if (cursor.first("removeEmptyFields"))
    {
      removeEmptyFields = Boolean.valueOf((String) cursor.getValue()).booleanValue();
    }

    IData outIData = trimValues(inputDocument, removeEmptyFields);
    IDataUtil.put(cursor, "outputDocument", outIData);
    cursor.destroy();

    // --- <<IS-END>> ---

  }

  // --- <<IS-START-SHARED>> ---
  private static IData removeNullFields(IData inIData, boolean removeEmpty, boolean trimFields)
  {
    IData outIData = IDataFactory.create();
    IDataCursor outCursor = outIData.getCursor();
    IDataCursor inCursor = inIData.getCursor();

    while (inCursor.next())
    {
      Object obj = inCursor.getValue();
      if (obj == null)
      {
        continue;
      }

      if (obj instanceof String)
      {
        String temp = (String) obj;
        if (trimFields)
        {
          temp = temp.trim();
        }

        if (temp.length() > 0 || !removeEmpty)
        {
          IDataUtil.put(outCursor, inCursor.getKey(), temp);
        }
      }
      else if (obj instanceof IData)
      {
        IData out = removeNullFields((IData) obj, removeEmpty, trimFields);
        if (out != null && out.getCursor().hasMoreData())
        {
          IDataUtil.put(outCursor, inCursor.getKey(), out);
        }
      }
      else if (obj instanceof IData[])
      {
        IData[] objArray = (IData[]) obj;
        ArrayList outArrayList = new ArrayList();

        for (int i = 0; i < objArray.length; i++)
        {
          IData out = removeNullFields(objArray[i], removeEmpty, trimFields);
          if (out != null && out.getCursor().hasMoreData())
          {
            outArrayList.add(out);
          }
        }

        IData[] outArray = null;
        if (outArrayList.size() > 0)
        {
          outArray = new IData[outArrayList.size()];
          outArrayList.toArray(outArray);
          IDataUtil.put(outCursor, inCursor.getKey(), outArray);
        }
      }
      else
      {
        IDataUtil.put(outCursor, inCursor.getKey(), obj);
      }
    }
    outCursor.destroy();
    inCursor.destroy();
    return outIData;
  }

  private static IData trimValues(IData inIData, boolean removeEmptyFields)
  {
    IData outIData = IDataFactory.create();
    IDataCursor outCursor = outIData.getCursor();
    IDataCursor inCursor = inIData.getCursor();

    while (inCursor.next())
    {
      Object obj = inCursor.getValue();
      if (obj instanceof String)
      {
        if (obj != null)
        {
          String temp = ((String) obj).trim();
          if (temp.length() > 0 || removeEmptyFields == false)
          {
            IDataUtil.put(outCursor, inCursor.getKey(), temp);
          }
          else
          {
            IDataUtil.put(outCursor, inCursor.getKey(), null);
          }
        }
      }
      else if (obj instanceof IData)
      {
        IDataUtil.put(outCursor, inCursor.getKey(), trimValues((IData) obj, removeEmptyFields));
      }
      else if (obj instanceof IData[])
      {
        IData[] objArray = (IData[]) obj;
        IData[] outArray = new IData[objArray.length];
        for (int i = 0; i < objArray.length; i++)
        {
          outArray[i] = trimValues(objArray[i], removeEmptyFields);
        }
        IDataUtil.put(outCursor, inCursor.getKey(), outArray);
      }
      else
      {
        IDataUtil.put(outCursor, inCursor.getKey(), obj);
      }
    }
    outCursor.destroy();
    inCursor.destroy();
    return outIData;
  }
  // --- <<IS-END-SHARED>> ---
}
