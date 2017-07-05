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
import java.lang.reflect.Array;

// --- <<IS-END-IMPORTS>> ---

public final class vector

{
  // ---( internal utility methods )---

  final static vector _instance = new vector();

  static vector _newInstance()
  {
    return new vector();
  }

  static vector _cast(Object o)
  {
    return (vector) o;
  }

  // ---( server methods )---

  public static final void addElement(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(addElement)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] object:0:optional vector
    // [i] object:0:optional element
    // [i] object:1:optional elementList
    // [i] field:0:optional addNulls {"false","true"}
    // [o] object:0:required vector
    IDataCursor cursor = pipeline.getCursor();

    Vector vector = null;
    Object object = null;
    boolean added = false;

    boolean list = false;
    boolean addNulls = false;

    if (cursor.first("vector"))
    {
      vector = (Vector) cursor.getValue();
    }
    else
    {
      vector = new Vector();
      IDataUtil.put(cursor, "vector", vector);
    }

    if (cursor.first("element"))
    {
      object = cursor.getValue();
    }
    else if (cursor.first("elementList"))

    {
      object = cursor.getValue();
      list = true;
    }

    if (cursor.first("addNulls"))
    {
      addNulls = new Boolean((String) cursor.getValue()).booleanValue();
    }

    if (list)
    {
      int length = Array.getLength(object);
      for (int i = 0; i < length; i++)
      {
        Object item = Array.get(object, i);
        if (item != null || (item == null && addNulls))
        {
          vector.add(Array.get(object, i));
        }
      }
    }
    else
    {
      if (object != null || (object == null && addNulls))
      {
        vector.add(object);
      }
    }

    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void toArray(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(toArray)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] object:0:required vector
    // [i] field:0:optional stronglyType {"false","true"}
    // [o] object:1:required array
    IDataCursor cursor = pipeline.getCursor();
    boolean stronglyType = false;

    if (cursor.first("vector"))
    {
      Vector vector = (Vector) cursor.getValue();
      int length = vector.size();
      if (length == 0)
      {
        return;
      }

      if (cursor.first("stronglyType"))
      {
        stronglyType = new Boolean((String) cursor.getValue()).booleanValue();
      }

      Object arr = null;
      if (stronglyType)
      {
        arr = Array.newInstance(vector.elementAt(0).getClass(), length);
        for (int i = 0; i < length; i++)
        {
          Array.set(arr, i, vector.elementAt(i));
        }
      }
      else
      {
        arr = vector.toArray();
      }

      IDataUtil.put(cursor, "array", arr);

    }

    cursor.destroy();
    // --- <<IS-END>> ---

  }
}
