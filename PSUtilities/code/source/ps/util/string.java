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
import java.text.NumberFormat;
import com.wm.ps.util.string.BadFieldException;
import com.wm.ps.util.string.Field;

// --- <<IS-END-IMPORTS>> ---

public final class string

{
  // ---( internal utility methods )---

  final static string _instance = new string();

  static string _newInstance()
  {
    return new string();
  }

  static string _cast(Object o)
  {
    return (string) o;
  }

  // ---( server methods )---

  public static final void concatFields(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(concatFields)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] record:0:required inRecord
    // [i] field:1:optional sortOrder
    // [i] field:0:optional separator
    // [i] field:0:optional skipNulls {"false","true"}
    // [o] field:0:required value
    IDataCursor cursor = pipeline.getCursor();

    StringBuffer buffer = new StringBuffer();
    String separator = null;
    String[] sortOrder = null;
    boolean skipNulls = IDataUtil.getBoolean(cursor, "skipNulls", false);

    if (cursor.first("separator"))
    {
      separator = (String) cursor.getValue();
    }

    if (cursor.first("sortOrder"))
    {
      sortOrder = (String[]) cursor.getValue();
    }

    IData inRecord = IDataUtil.getIData(cursor, "inRecord");
    if (inRecord == null)
    {
      inRecord = IDataFactory.create();
    }

    IDataCursor inRecordCursor = inRecord.getCursor();
    // If no sortOrder is specified, just concatenate the fields based on the
    // default order
    if (sortOrder == null || sortOrder.length == 0)
    {
      boolean firstField = true;
      for (int i = 0; inRecordCursor.next(); i++)
      {
        String value = (String) inRecordCursor.getValue();
        if (value == null)
        {
          value = "";
        }

        if (skipNulls && (value == null || value.length() == 0))
        {
          continue;
        }

        if (!firstField && separator != null)
        {
          buffer.append(separator);
        }

        buffer.append(value);
        firstField = false;
      }
    }
    else
    {
      // When sortOrder is specified, use that to retrieve and concatenate
      // fields.
      boolean firstField = true;
      for (int i = 0; i < sortOrder.length; i++)
      {
        String value = "";
        if (inRecordCursor.first(sortOrder[i]))
        {
          value = (String) inRecordCursor.getValue();
          if (value == null)
          {
            value = "";
          }
        }

        if (skipNulls && (value == null || value.length() == 0))
        {
          continue;
        }

        if (!firstField && separator != null)
        {
          buffer.append(separator);
        }

        buffer.append(value);
        firstField = false;
      }
    }

    if (buffer.length() > 0)
    {
      IDataUtil.put(cursor, "value", buffer.toString());
    }

    cursor.destroy();
    // --- <<IS-END>> ---
  }

  public static final void formatNumber(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(formatNumber)>> ---
    // @sigtype java 3.5
    // [i] field:0:required number
    // [i] field:0:optional integerDigits
    // [i] field:0:optional minFractionDigits
    // [i] field:0:optional maxFractionDigits
    // [o] field:0:required formattedNumber
    IDataCursor cursor = pipeline.getCursor();

    double number = 0;
    int integerDigits = 0;
    int minFractionDigits = 0;
    int maxFractionDigits = 0;

    if (cursor.first("number"))
    {
      String temp = (String) cursor.getValue();
      if (temp != null && temp.length() > 0)
      {
        number = Double.parseDouble(temp);
      }
    }

    int integerLength = String.valueOf((long) number).length();
    if (cursor.first("integerDigits"))
    {
      String temp = (String) cursor.getValue();
      if (temp != null && temp.length() > 0)
      {
        integerDigits = Integer.parseInt(temp);
      }
    }

    if (cursor.first("minFractionDigits"))
    {
      String temp = (String) cursor.getValue();
      if (temp != null && temp.length() > 0)
      {
        minFractionDigits = Integer.parseInt(temp);
      }
    }

    if (cursor.first("maxFractionDigits"))
    {
      String temp = (String) cursor.getValue();
      if (temp != null && temp.length() > 0)
      {
        maxFractionDigits = Integer.parseInt(temp);
      }
    }

    if (maxFractionDigits < minFractionDigits)
    {
      maxFractionDigits = minFractionDigits;
    }

    NumberFormat nfmt = NumberFormat.getInstance();
    nfmt.setMinimumIntegerDigits(integerDigits);
    nfmt.setMaximumIntegerDigits(integerDigits > integerLength ? integerDigits : integerLength);
    nfmt.setMinimumFractionDigits(minFractionDigits);
    nfmt.setMaximumFractionDigits(maxFractionDigits);
    nfmt.setGroupingUsed(false);

    if (cursor.first("formattedNumber"))
    {
      cursor.setValue(nfmt.format(number));
    }
    else
    {
      cursor.insertAfter("formattedNumber", nfmt.format(number));
    }

    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void formatString(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(formatString)>> ---
    // @sigtype java 3.5
    // [i] field:0:required inString
    // [i] field:0:optional minLength
    // [i] field:0:optional maxLength
    // [i] field:0:optional padCharacter
    // [i] field:0:optional justification {"Right","Left"}
    // [i] field:0:optional quoted {"true","false"}
    // [i] field:0:optional quoteCharacter {"\"","'"}
    // [i] field:0:optional default
    // [o] field:0:required formattedString
    IDataCursor cursor = pipeline.getCursor();

    StringBuffer buffer = new StringBuffer();
    String justification = "Left";
    boolean quoted = true;
    String padding = " ";
    int minLength = 0;
    int maxLength = 0;
    String inString = "";
    String def = " ";
    String quoteCharacter = "\"";

    if (cursor.first("inString"))
    {
      inString = (String) cursor.getValue();
    }
    else
    {
      if (cursor.first("default"))
      {
        def = (String) cursor.getValue();
      }
    }

    if (inString == null || inString.length() == 0)
    {
      inString = def;
    }

    if (cursor.first("maxLength"))
    {
      maxLength = Integer.parseInt((String) cursor.getValue());
      if (maxLength > 0 && inString.length() > maxLength)
      {
        inString = inString.substring(0, maxLength);
      }
    }

    if (cursor.first("minLength"))
    {
      minLength = Integer.parseInt((String) cursor.getValue());
    }

    if (maxLength > 0 && minLength > maxLength)
    {
      minLength = maxLength;
    }

    if (cursor.first("padCharacter"))
    {
      padding = (String) cursor.getValue();
    }

    if (cursor.first("justification"))
    {
      justification = (String) cursor.getValue();
    }

    if (cursor.first("quoted"))
    {
      quoted = Boolean.valueOf((String) cursor.getValue()).booleanValue();
    }
    
    if (cursor.first("quoteCharacter"))
    {
      quoteCharacter = (String)cursor.getValue();
    }
    
    if (inString.length() < minLength && justification.equalsIgnoreCase("right"))
    {
      for (int i = 0; i < minLength - inString.length(); i++)
      {
        buffer.append(padding);
      }
    }

    buffer.append(inString);

    if (inString.length() < minLength && justification.equalsIgnoreCase("left"))
    {
      for (int i = 0; i < minLength - inString.length(); i++)
      {
        buffer.append(padding);
      }
    }

    if (buffer.length() > 0)
    {
      if (quoted == true)
      {
        buffer.insert(0, quoteCharacter).append(quoteCharacter);
      }

      if (cursor.first("formattedString"))
      {
        cursor.setValue(buffer.toString());
      }
      else
      {
        cursor.insertAfter("formattedString", buffer.toString());
      }
    }

    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void parse(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(parse)>> ---
    // @sigtype java 3.5
    // [i] field:0:required inString
    // [i] field:0:optional outputRecord {"true","false"}
    // [i] field:0:optional maxLength
    // [i] field:0:optional delimitor
    // [i] record:1:optional fields
    // [i] - field:0:required name
    // [i] - field:0:optional startIndex
    // [i] - field:0:optional length
    // [i] field:0:optional ignoreWhitespace {"true","false"}
    // [o] record:0:optional record
    // [o] field:1:optional list
    IDataCursor cursor = pipeline.getCursor();

    String line = "";
    boolean outputRecord = true;
    boolean ignoreWhitespace = true;
    String delimitor = null;
    String[] tokens = null;

    if (cursor.first("inString"))
    {
      line = (String) cursor.getValue();
      if (cursor.first("ignoreWhitespace"))
      {
        ignoreWhitespace = Boolean.valueOf((String) cursor.getValue()).booleanValue();
      }

      if (cursor.first("outputRecord"))
      {
        outputRecord = Boolean.valueOf((String) cursor.getValue()).booleanValue();
      }

      // Create an array of tokens from the String using the delimitor
      if (cursor.first("delimitor"))
      {
        delimitor = (String) cursor.getValue();
        if (delimitor != null && delimitor.length() > 0)
        {
          tokens = tokenize(line, delimitor);
        }
      }

      // If the output is to be in a record format
      if (outputRecord)
      {
        // Create the output record to hold the fields
        IData outRecord = IDataFactory.create();
        IDataCursor outRecordCursor = outRecord.getCursor();

        if (cursor.first("fields"))
        {
          // Get the fields list
          IData[] fields = (IData[]) cursor.getValue();
          for (int i = 0; i < fields.length; i++)
          {
            // Extract the name of the current field
            IDataCursor fieldCursor = fields[i].getCursor();
            try
            {
              if (delimitor != null && delimitor.length() > 0)
              {
                String name = null;
                if (fieldCursor.first("name"))
                {
                  name = (String) fieldCursor.getValue();
                }
                else
                {
                  throw new BadFieldException("Field does not have a 'name' attribute");
                }

                String token = tokens[i];
                outRecordCursor.insertAfter(name, ignoreWhitespace ? token.trim().length() > 0 ? token.trim() : null : token);
              }
              else
              {
                // Create a field object from the input values
                Field field = new Field(fieldCursor);
                // Calculate the fixed length positions of this field
                int startIndex = field.getStartIndex();
                int endIndex = field.getStartIndex() + field.getLength();

                if (startIndex > line.length())
                {
                  outRecordCursor.insertAfter(field.getName(), null);
                }
                else if (endIndex >= line.length())
                {
                  String value = line.substring(startIndex);
                  outRecordCursor.insertAfter(field.getName(), ignoreWhitespace ? value.trim().length() > 0 ? value.trim() : null : value);
                }
                else
                {
                  String value = line.substring(startIndex, endIndex);
                  outRecordCursor.insertAfter(field.getName(), ignoreWhitespace ? value.trim().length() > 0 ? value.trim() : null : value);
                }
              }
            }
            catch (BadFieldException bfe)
            {
              throw new ServiceException(bfe.getMessage());
            }
            fieldCursor.destroy();
          }
        }
        else
        {
          throw new ServiceException("Input 'fields' must exist when outputRecord is set to 'true'");
        }

        outRecordCursor.destroy();
        IDataUtil.put(cursor, "record", outRecord);
      }
      else
      {
        // Simple String List needed
        String[] array;

        if (delimitor != null && delimitor.length() > 0)
        {
          array = new String[tokens.length];
          for (int i = 0; i < array.length; i++)
          {
            array[i] = ignoreWhitespace ? tokens[i].trim().length() > 0 ? tokens[i].trim() : null : tokens[i];
          }
        }
        else
        {
          int maxLength = 0;

          if (cursor.first("maxLength"))
          {
            maxLength = Integer.parseInt((String) cursor.getValue());
          }

          if (maxLength <= 0)
          {
            throw new ServiceException("Input 'maxLength' or 'delimitor' must exist when outputRecord is set to 'false'");
          }

          array = new String[line.length() / maxLength + 1];
          for (int i = 0; i < array.length; i++)
          {
            if ((i + 1) * maxLength > line.length())
            {
              String value = line.substring(i * maxLength);
              array[i] = ignoreWhitespace ? value.trim().length() > 0 ? value.trim() : null : value;
            }
            else
            {
              String value = line.substring(i * maxLength, (i + 1) * maxLength);
              array[i] = ignoreWhitespace ? value.trim().length() > 0 ? value.trim() : null : value;
            }
          }
        }
        IDataUtil.put(cursor, "list", array);
      }
    }
    else
    {
      throw new ServiceException("Missing input 'inString'");
    }

    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void substituteVariables(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(substituteVariables)>> ---
    // @sigtype java 3.5
    // [i] field:0:required inputString
    // [o] field:0:required outputString

    IDataCursor cursor = pipeline.getCursor();

    String strInputString = null;
    if (cursor.first("inputString"))
    {
      strInputString = (String) cursor.getValue();
    }
    else
    {
      return; // Input string is null
    }

    // pipeline
    String variableNotFoundString = IDataUtil.getString(cursor, "variableNotFoundString");

    String strOutputString = "";

    int intCurrentIndex = 0;
    int intStartIndex = 0;
    int intEndIndex = 0;
    while ((intStartIndex = strInputString.indexOf('%', intCurrentIndex)) != -1)
    {
      // If \%, then ignore...don't perform variable substitution
      if ((intStartIndex != 0) && (strInputString.charAt(intStartIndex - 1) == '\\'))
      {
        strOutputString = strOutputString + strInputString.substring(intCurrentIndex, intStartIndex - 1) + '%';
        intCurrentIndex = intStartIndex + 1;
        continue;
      }

      // Find the ending %
      intEndIndex = strInputString.indexOf('%', intStartIndex + 1);
      if (intEndIndex == -1)
      {
        break;
      }

      // Print out everything before the %
      strOutputString = strOutputString + strInputString.substring(intCurrentIndex, intStartIndex);
      // Find the string to substitute
      String strStringToSubstitute = null;
      String strVariableName = strInputString.substring(intStartIndex + 1, intEndIndex);

      if (strVariableName.equals(""))
      {
        // We had occurence of "%%"
        intCurrentIndex = intEndIndex + 1;
        continue;
      }

      // Perform variable substitution
      StringTokenizer tokenizedString = new StringTokenizer(strVariableName, "/", false);
      int maxTokens = tokenizedString.countTokens();
      IData currentRecord = null;
      int intTokenIndex = 1;
      while (tokenizedString.hasMoreTokens())
      {
        String strCurrentToken = tokenizedString.nextToken();
        // System.out.println("strCurrentToken = " + strCurrentToken);

        IDataCursor idc = null;
        if (currentRecord == null)
        {
          // New search - look in pipeline for record/string
          idc = cursor;
        }
        else
        {
          idc = currentRecord.getCursor();
        }

        if (idc.first(strCurrentToken))
        {
          Object o = idc.getValue();
          if ((intTokenIndex == maxTokens) && (o instanceof String))
          {
            // This is the last token. Look for a string
            // Variable found in pipeline
            strStringToSubstitute = (String) o;
            strOutputString = strOutputString + strStringToSubstitute;
            intCurrentIndex = intEndIndex + 1;
          }
          else if ((intTokenIndex != maxTokens) && (o instanceof IData))
          {
            // Look for a IData (record)
            currentRecord = (IData) o;
          }
          else
          {
            // Type mismatch - variable not found in pipeline
            // strOutputString = strOutputString +
            // strInputString.substring(intStartIndex, intEndIndex + 1);
            intCurrentIndex = intEndIndex + 1;
            break; // Ignore other tokens
          }
        }
        else
        {
          // Variable not found in pipeline
          // strOutputString = strOutputString +
          // strInputString.substring(intStartIndex, intEndIndex + 1);
          strOutputString = strOutputString + variableNotFoundString;
          intCurrentIndex = intEndIndex + 1;
          break; // Ignore other tokens
        }

        intTokenIndex++;
      }

    }

    strOutputString = strOutputString + strInputString.substring(intCurrentIndex);

    cursor.insertAfter("outputString", strOutputString);
    cursor.destroy();
    // --- <<IS-END>> ---

  }

  // --- <<IS-START-SHARED>> ---
  private static String[] tokenize(String string, String delimitor)
  {
    Vector vector = new Vector();
    int index = 0;
    int prevIndex = 0;

    while ((index = string.indexOf(delimitor, prevIndex)) >= 0)
    {
      vector.addElement(string.substring(prevIndex, index));
      prevIndex = index + 1;
    }
    vector.addElement(string.substring(prevIndex));
    String[] array = new String[vector.size()];
    for (int i = 0; i < array.length; i++)
    {
      array[i] = (String) vector.elementAt(i);
    }

    return array;
  }
  // --- <<IS-END-SHARED>> ---
}
