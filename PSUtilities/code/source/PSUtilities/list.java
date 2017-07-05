package PSUtilities;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2005-03-21 17:34:52 PST
// -----( ON-HOST: 172.20.6.126

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.util.List;

// --- <<IS-END-IMPORTS>> ---

public final class list

{
  // ---( internal utility methods )---

  final static list _instance = new list();

  static list _newInstance()
  {
    return new list();
  }

  static list _cast(Object o)
  {
    return (list) o;
  }

  // ---( server methods )---

  public static final void addToList(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(addToList)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] object:0:required list
    // [i] record:0:required Document
    // [o] object:0:required list
    List l = (List) ValuesEmulator.get(pipeline, "list");
    Object o = ValuesEmulator.get(pipeline, "Document");
    l.addElement(o);
    // --- <<IS-END>> ---

  }

  public static final void createList(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(createList)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] field:0:optional initialListSize
    // [o] object:0:required list
    String initialListSize = (String) ValuesEmulator.get(pipeline, "initialListSize");
    List l = null;

    if (initialListSize == null)
    {
      l = new List(100); // default list size
    }
    else
    {
      l = new List(Integer.parseInt(initialListSize));
    }

    ValuesEmulator.put(pipeline, "list", l);
    // --- <<IS-END>> ---

  }

  public static final void getListSize(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(getListSize)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] object:0:required list
    // [o] field:0:required suze
    List l = (List) ValuesEmulator.get(pipeline, "list");
    int size = l.size();
    ValuesEmulator.put(pipeline, "size", Integer.toString(size));
    // --- <<IS-END>> ---

  }

  public static final void listToArray(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(listToArray)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] object:0:required list
    // [o] record:1:required DocumentList
    List l = (List) ValuesEmulator.get(pipeline, "list");
    int size = l.size();
    IData[] output = new IData[size];
    for (int i = 0; i < size; i++)
    {
      output[i] = (IData) l.elementAt(i);
    }
    ValuesEmulator.put(pipeline, "DocumentList", output);
    // --- <<IS-END>> ---

  }
}
