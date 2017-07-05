package ps.util;

//-----( IS Java Code Template v1.2
//-----( CREATED: 2008-01-08 21:00:00 EDT
//-----( ON-HOST: MCRSINGH.AME.ad.sag

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.math.*;

// --- <<IS-END-IMPORTS>> ---

public final class dataTypes

{
  // ---( internal utility methods )---

  final static dataTypes _instance = new dataTypes();

  static dataTypes _newInstance()
  {
    return new dataTypes();
  }

  static dataTypes _cast(Object o)
  {
    return (dataTypes) o;
  }

  // ---( server methods )---

  public static final void convertFromString(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(convertFromString)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] field:0:required string
    // [i] field:0:required type
    // {"String","java.lang.String","Integer","java.lang.Integer","MInteger","com.wm.data.MInteger","Short","java.lang.Short","MShort","com.wm.data.MShort","Long","java.lang.Long","MLong","com.wm.data.MLong","Float","java.lang.Float","MFloat","com.wm.data.MFloat","Double","java.lang.Double","MDouble","com.wm.data.MDouble","Boolean","java.lang.Boolean","MBoolean","com.wm.data.MBoolean","BigDecimal","java.math.BigDecimal","BigInteger","java.math.BigInteger"}
    // [o] object:0:required object
    IDataCursor cursor = pipeline.getCursor();

    String string = null;
    String type = null;

    if (cursor.first("string"))
    {
      string = (String) cursor.getValue();
    }
    else
    {
      throw new ServiceException("Missing input 'string'");
    }

    if (cursor.first("type"))
    {
      type = (String) cursor.getValue();
    }
    else
    {
      throw new ServiceException("Missing input 'type'");
    }

    if (string == null || string.length() == 0)
    {
      IDataUtil.put(cursor, "object", null);
    }
    else if (type.equals("String") || type.equals("java.lang.String"))
    {
      IDataUtil.put(cursor, "object", string);
    }
    else if (type.equals("Integer") || type.equals("java.lang.Integer"))
    {
      IDataUtil.put(cursor, "object", new Integer(string));
    }
    else if (type.equals("MInteger") || type.equals("com.wm.data.MInteger"))
    {
      IDataUtil.put(cursor, "object", new MInteger(string));
    }
    else if (type.equals("Short") || type.equals("java.lang.Short"))
    {
      IDataUtil.put(cursor, "object", new Short(string));
    }
    else if (type.equals("MShort") || type.equals("com.wm.data.MShort"))
    {
      IDataUtil.put(cursor, "object", new MShort(string));
    }
    else if (type.equals("Long") || type.equals("java.lang.Long"))
    {
      IDataUtil.put(cursor, "object", new Long(string));
    }
    else if (type.equals("MLong") || type.equals("com.wm.data.MLong"))
    {
      IDataUtil.put(cursor, "object", new MLong(string));
    }
    else if (type.equals("Float") || type.equals("java.lang.Float"))
    {
      IDataUtil.put(cursor, "object", new Float(string));
    }
    else if (type.equals("MFloat") || type.equals("com.wm.data.MFloat"))
    {
      IDataUtil.put(cursor, "object", new MFloat(string));
    }
    else if (type.equals("Double") || type.equals("java.lang.Double"))
    {
      IDataUtil.put(cursor, "object", new Double(string));
    }
    else if (type.equals("MDouble") || type.equals("com.wm.data.MDouble"))
    {
      IDataUtil.put(cursor, "object", new MDouble(string));
    }
    else if (type.equals("Boolean") || type.equals("java.lang.Boolean"))
    {
      IDataUtil.put(cursor, "object", new Boolean(string));
    }
    else if (type.equals("MBoolean") || type.equals("com.wm.data.MBoolean"))
    {
      IDataUtil.put(cursor, "object", new MBoolean(string));
    }
    else if (type.equals("BigDecimal") || type.equals("java.math.BigDecimal"))
    {
      IDataUtil.put(cursor, "object", new BigDecimal(string));
    }
    else if (type.equals("BigInteger") || type.equals("java.math.BigInteger"))
    {
      IDataUtil.put(cursor, "object", new BigInteger(string));
    }
    else
    {
      throw new ServiceException("Dont know how to convert string to '" + type + "'");
    }
    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void convertToString(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(convertToString)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] object:0:required object
    // [o] field:0:required value
    IDataCursor cursor = pipeline.getCursor();

    Object object = null;

    if (cursor.first("object"))
    {
      object = cursor.getValue();
    }
    else
    {
      throw new ServiceException("Missing input 'object'");
    }

    if (object == null || object instanceof java.lang.String)
    {
      IDataUtil.put(cursor, "value", object);
    }
    else
    {
      IDataUtil.put(cursor, "value", object.toString());
    }

    cursor.destroy();
    // --- <<IS-END>> ---

  }
}
