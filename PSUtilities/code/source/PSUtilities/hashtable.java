package PSUtilities;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2005-06-10 15:24:27 PDT
// -----( ON-HOST: 172.20.6.126

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.util.*;

// --- <<IS-END-IMPORTS>> ---

public final class hashtable

{
  // ---( internal utility methods )---

  final static hashtable _instance = new hashtable();

  static hashtable _newInstance()
  {
    return new hashtable();
  }

  static hashtable _cast(Object o)
  {
    return (hashtable) o;
  }

  // ---( server methods )---

  public static final void containsKey(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(containsKey)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] object:0:required Hashtable
    // [i] field:0:required key
    // [o] field:0:required containsKey {"true","false"}

    IDataCursor idcPipeline = pipeline.getCursor();

    Hashtable ht;
    if (idcPipeline.first("Hashtable"))
    {
      ht = (Hashtable) idcPipeline.getValue();
    }
    else
    {
      throw new ServiceException("Hashtable is null!");
    }

    idcPipeline.first("key");
    String strKey = (String) idcPipeline.getValue();

    idcPipeline.insertAfter("containsKey", String.valueOf(ht.containsKey(strKey)));

    idcPipeline.destroy();
    // --- <<IS-END>> ---

  }

  public static final void createHashtable(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(createHashtable)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [o] object:0:required Hashtable

    IDataCursor idcPipeline = pipeline.getCursor();
    idcPipeline.insertAfter("Hashtable", new Hashtable());
    idcPipeline.destroy();
    // --- <<IS-END>> ---

  }

  public static final void get(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(get)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] object:0:required Hashtable
    // [i] field:0:required key
    // [o] object:0:required value

    IDataCursor idcPipeline = pipeline.getCursor();

    Hashtable ht;
    if (idcPipeline.first("Hashtable"))
    {
      ht = (Hashtable) idcPipeline.getValue();
    }
    else
    {
      throw new ServiceException("Hashtable is null!");
    }

    idcPipeline.first("key");
    String strKey = (String) idcPipeline.getValue();

    idcPipeline.insertAfter("value", ht.get(strKey));

    idcPipeline.destroy();
    // --- <<IS-END>> ---

  }

  public static final void keys(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(keys)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] object:0:required Hashtable
    // [o] field:1:required keys

    IDataCursor idcPipeline = pipeline.getCursor();

    Hashtable ht;
    if (idcPipeline.first("Hashtable"))
    {
      ht = (Hashtable) idcPipeline.getValue();
    }
    else
    {
      throw new ServiceException("Hashtable is null!");
    }

    idcPipeline.insertAfter("keys", ht.keySet().toArray());

    idcPipeline.destroy();
    // --- <<IS-END>> ---

  }

  public static final void put(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(put)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] object:0:required Hashtable
    // [i] field:0:required key
    // [i] object:0:required value
    // [o] object:0:required Hashtable

    IDataCursor idcPipeline = pipeline.getCursor();

    Hashtable ht;
    if (idcPipeline.first("Hashtable"))
    {
      ht = (Hashtable) idcPipeline.getValue();
      idcPipeline.delete();
    }
    else
    {
      ht = new Hashtable();
    }

    String strKey = null;
    Object objValue = new Object();
    if (idcPipeline.first("key"))
    {
      strKey = (String) idcPipeline.getValue();
    }
    if (idcPipeline.first("value"))
    {
      objValue = idcPipeline.getValue();
    }

    ht.put(strKey, objValue);

    idcPipeline.insertAfter("Hashtable", ht);

    idcPipeline.destroy();
    // --- <<IS-END>> ---

  }

  public static final void remove(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(remove)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] object:0:required Hashtable
    // [i] field:0:required key
    // [o] object:0:required Hashtable
    // [o] object:0:required value

    IDataCursor idcPipeline = pipeline.getCursor();

    Hashtable ht;
    if (idcPipeline.first("Hashtable"))
    {
      ht = (Hashtable) idcPipeline.getValue();
      idcPipeline.delete();
    }
    else
    {
      throw new ServiceException("Hashtable is null!");
    }

    idcPipeline.first("key");
    String strKey = (String) idcPipeline.getValue();

    Object value = ht.remove(strKey);

    idcPipeline.insertAfter("Hashtable", ht);
    idcPipeline.insertAfter("value", value);

    idcPipeline.destroy();
    // --- <<IS-END>> ---

  }

  public static final void size(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(size)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] object:0:required Hashtable
    // [o] field:0:required size

    IDataCursor idcPipeline = pipeline.getCursor();

    Hashtable ht;
    if (idcPipeline.first("Hashtable"))
    {
      ht = (Hashtable) idcPipeline.getValue();
    }
    else
    {
      throw new ServiceException("Hashtable is null!");
    }

    idcPipeline.insertAfter("size", String.valueOf(ht.size()));

    idcPipeline.destroy();
    // --- <<IS-END>> ---

  }
}
