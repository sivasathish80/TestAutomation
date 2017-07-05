package ps.util;

//-----( IS Java Code Template v1.2
//-----( CREATED: 2008-01-08 21:00:00 EDT
//-----( ON-HOST: MCRSINGH.AME.ad.sag

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.lang.reflect.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import com.wm.ps.util.compare.SimpleComparator;

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

  public static final void filterDocumentList(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(filterDocumentList)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] record:1:required originalList
    // [i] record:1:required filter
    // [i] - field:0:required columnName
    // [i] - field:1:required columnValues
    // [i] - field:0:optional operator {"equals","equalsIgnoreCase","=",">","<",">=","<=","max","min"}
    // [i] record:1:optional columnTypes
    // [i] - field:0:required name
    // [i] - field:0:required type {"String","Numeric","Date"}
    // [i] - field:0:optional pattern
    // [o] record:1:required filteredList
    IDataCursor cursor = pipeline.getCursor();
    IData[] originalList = IDataUtil.getIDataArray(cursor, "originalList");
    IData[] filter = IDataUtil.getIDataArray(cursor, "filter");
    IData[] columnTypes = IDataUtil.getIDataArray(cursor, "columnTypes");
    if (originalList == null || filter == null)
    {
      return;
    }

    ArrayList list = new ArrayList();

    outer: for (int i = 0; i < originalList.length; i++)
    {
      IDataCursor listCursor = originalList[i].getCursor();
      boolean matched = true;
      for (int j = 0; j < filter.length; j++)
      {
        IDataCursor filterCursor = filter[j].getCursor();
        String columnName = IDataUtil.getString(filterCursor, "columnName");
        String operator = IDataUtil.getString(filterCursor, "operator");
        if (operator == null)
        {
          operator = "equals";
        }

        String[] columnValues = IDataUtil.getStringArray(filterCursor, "columnValues");
        String itemValue = IDataUtil.getString(listCursor, columnName);
        String[] columnInfo = getColumnInfo(columnTypes, columnName);

        try
        {
          if (operator.equals("max") || operator.equals("min"))
          {
            int[] indices = getMaxMinValuesIndices(originalList, columnName, columnInfo[0], columnInfo[1], operator);
            for (int k = 0; k < indices.length; k++)
            {
              list.add(originalList[indices[k]]);
            }
            break outer;
          }

          if (!matchAny(itemValue, columnValues, operator, columnInfo[0], columnInfo[1]))
          {
            matched = false;
            break;
          }
        }
        catch (Exception e)
        {
          throw new ServiceException(e);
        }
      }

      if (matched)
      {
        list.add(originalList[i]);
      }
      listCursor.destroy();
    }

    IData[] filteredList = null;

    if (list.size() > 0)
    {
      filteredList = new IData[list.size()];
      list.toArray(filteredList);
    }

    IDataUtil.put(cursor, "filteredList", filteredList);
    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void getItem(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(getItem)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] object:1:required list
    // [i] field:0:required itemIndex
    // [i] field:0:required zeroBased {"true","false"}
    // [o] object:0:required item
    IDataCursor cursor = pipeline.getCursor();

    int itemIndex = 0;
    boolean zeroBased = true;

    if (cursor.first("list"))
    {
      Object[] list = (Object[]) cursor.getValue();
      if (cursor.first("itemIndex"))
      {
        itemIndex = Integer.parseInt((String) cursor.getValue());
      }
      if (cursor.first("zeroBased"))
      {
        zeroBased = Boolean.valueOf((String) cursor.getValue()).booleanValue();
      }
      if (!zeroBased)
      {
        itemIndex--;
      }

      IDataUtil.put(cursor, "item", list[itemIndex]);
    }

    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void getSubArray(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(getSubArray)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] object:1:required array
    // [i] field:0:optional startAt
    // [i] field:0:required length
    // [o] object:1:required subArray
    IDataCursor cursor = pipeline.getCursor();

    int startAt = 0;
    int arrayLength;

    if (cursor.first("array"))
    {
      Object object = cursor.getValue();
      int length = 0;
      arrayLength = Array.getLength(object);

      if (arrayLength > 0)
      {
        if (cursor.first("startAt"))
        {
          startAt = Integer.parseInt((String) cursor.getValue());
        }

        if (cursor.first("length"))
        {
          length = Integer.parseInt((String) cursor.getValue());
        }

        if (length == 0)
        {
          throw new ServiceException("Missing or invalid paramtere value for 'length'");
        }

        Object subArray = Array.newInstance(Array.get(object, 0).getClass(), length < arrayLength ? length : arrayLength);
        System.arraycopy(object, startAt, subArray, 0, length < arrayLength ? length : arrayLength);

        IDataUtil.put(cursor, "subArray", subArray);

      }
    }

    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void sortDocumentListByKey(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(sortDocumentListByKey)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] record:1:required documentList
    // [i] field:0:required key
    // [i] field:0:optional type {"String","Date","Numeric"}
    // [i] field:0:optional pattern
    // [i] field:0:optional reverse {"false","true"}
    // [o] record:1:required sortedDocumentList
    IDataCursor cursor = pipeline.getCursor();

    IData[] documentList = IDataUtil.getIDataArray(cursor, "documentList");
    if (documentList != null && documentList.length > 0)
    {
      String key = IDataUtil.getString(cursor, "key");
      String type = IDataUtil.getString(cursor, "type");
      if (type == null || type.length() == 0)
      {
        type = "String";
      }
      String pattern = IDataUtil.getString(cursor, "pattern");
      boolean reverse = IDataUtil.getBoolean(cursor, "reverse");
      Arrays.sort(documentList, new SimpleComparator(key, type, pattern, reverse));
    }
    IDataUtil.put(cursor, "sortedDocumentList", documentList);
    cursor.destroy();
    // --- <<IS-END>> ---

  }

  // --- <<IS-START-SHARED>> ---
  private static boolean matchAny(String value, String[] values, String operator, String columnType, String pattern) throws ParseException
  {
    if (value == null || values == null)
    {
      return false;
    }
    String dataType = columnType;
    if (columnType == null)
    {
      dataType = "String";
    }
    for (int i = 0; i < values.length; i++)
    {
      if (operator.equals("equals"))
      {
        if (value.equals(values[i]))
        {
          return true;
        }
      }
      else if (operator.equals("equalsIgnoreCase"))
      {
        if (value.equalsIgnoreCase(values[i]))
        {
          return true;
        }
      }
      else
      {
        if (dataType.equalsIgnoreCase("Numeric"))
        {
          return compareNumerics(value, values[i], operator);
        }
        else if (dataType.equalsIgnoreCase("Date"))
        {
          return compareDates(value, values[i], operator, pattern);
        }
        else
        {
          return compareStrings(value, values[i], operator);
        }
      }
    }
    return false;
  }

  private static boolean compareNumerics(String value1, String value2, String operator)
  {
    if (value1 == null || value2 == null)
    {
      return false;
    }
    double doubleValue1;
    double doubleValue2;
    try
    {
      doubleValue1 = Double.parseDouble(value1);
      doubleValue2 = Double.parseDouble(value2);
    }
    catch (NumberFormatException nfe)
    {
      return false;
    }
    if ("=".equals(operator))
    {
      return doubleValue1 == doubleValue2;
    }
    else if (">".equals(operator))
    {
      return doubleValue1 > doubleValue2;
    }
    else if ("<".equals(operator))
    {
      return doubleValue1 < doubleValue2;
    }
    if (">=".equals(operator))
    {
      return doubleValue1 >= doubleValue2;
    }
    if ("<=".equals(operator))
    {
      return doubleValue1 <= doubleValue2;
    }
    return false;
  }

  private static boolean compareStrings(String value1, String value2, String operator)
  {
    if (value1 == null || value2 == null)
    {
      return false;
    }
    if ("=".equals(operator) || "equals".equals(operator))
    {
      return value1.equals(value2);
    }
    else if ("equalsIgnoreCase".equals(operator))
    {
      return value1.equalsIgnoreCase(value2);
    }
    else if (">".equals(operator))
    {
      return value1.compareTo(value2) > 0;
    }
    else if ("<".equals(operator))
    {
      return value1.compareTo(value2) < 0;
    }
    else if (">=".equals(operator))
    {
      return value1.compareTo(value2) >= 0;
    }
    if ("<=".equals(operator))
    {
      return value1.compareTo(value2) <= 0;
    }
    return false;
  }

  private static boolean compareDates(String value1, String value2, String operator, String pattern) throws ParseException
  {
    if (value1 == null || value2 == null)
    {
      return false;
    }
    String datePattern = pattern;
    if (pattern == null)
    {
      datePattern = "MM/dd/yyyy hh:mm:ss a";
    }
    long time1;
    long time2;
    DateFormat dateFormat = new SimpleDateFormat(datePattern);
    Date date1 = dateFormat.parse(value1);
    Date date2 = dateFormat.parse(value2);
    time1 = date1.getTime();
    time2 = date2.getTime();

    if ("=".equals(operator))
    {
      return time1 == time2;
    }
    else if (">".equals(operator))
    {
      return time1 > time2;
    }
    else if ("<".equals(operator))
    {
      return time1 < time2;
    }
    else if (">=".equals(operator))
    {
      return time1 >= time2;
    }
    else if ("<=".equals(operator))
    {
      return time1 <= time2;
    }
    return false;
  }

  private static int[] getMaxMinValuesIndices(IData[] data, String columnName, String columnType, String pattern, String comparisonOperator) throws ParseException
  {
    String selectedValue = null;
    String operator = ">";
    ArrayList selectedIndicesList = new ArrayList();
    if ("min".equalsIgnoreCase(comparisonOperator))
    {
      operator = "<";
    }
    for (int i = 0; i < data.length; i++)
    {
      IDataCursor cursor = data[i].getCursor();
      String value = IDataUtil.getString(cursor, columnName);
      if (selectedValue == null)
      {
        selectedValue = value;
        selectedIndicesList.add(new Integer(i));
        continue;
      }

      if ("Numeric".equalsIgnoreCase(columnType))
      {
        if (compareNumerics(value, selectedValue, operator))
        {
          selectedIndicesList.clear();
          selectedIndicesList.add(new Integer(i));
          selectedValue = value;
        }
        else if (compareNumerics(value, selectedValue, "="))
        {
          selectedIndicesList.add(new Integer(i));
          selectedValue = value;
        }
      }
      else if ("Date".equalsIgnoreCase(columnType))
      {
        if (compareDates(value, selectedValue, operator, pattern))
        {
          selectedIndicesList.clear();
          selectedIndicesList.add(new Integer(i));
          selectedValue = value;
        }
        else if (compareDates(value, selectedValue, "=", pattern))
        {
          selectedIndicesList.add(new Integer(i));
          selectedValue = value;
        }
      }
      else
      {
        if (compareStrings(value, selectedValue, operator))
        {
          selectedIndicesList.clear();
          selectedIndicesList.add(new Integer(i));
          selectedValue = value;
        }
        else if (compareStrings(value, selectedValue, "="))
        {
          selectedIndicesList.add(new Integer(i));
          selectedValue = value;
        }
      }
      cursor.destroy();
    }

    int[] selectedIndices = new int[selectedIndicesList.size()];
    for (int i = 0; i < selectedIndices.length; i++)
    {
      selectedIndices[i] = ((Integer) selectedIndicesList.get(i)).intValue();
    }
    return selectedIndices;
  }

  private static String[] getColumnInfo(IData[] columnTypes, String columnName)
  {
    for (int i = 0; columnTypes != null && i < columnTypes.length; i++)
    {
      IDataCursor cursor = columnTypes[i].getCursor();
      String name = IDataUtil.getString(cursor, "name");
      if (name.equals(columnName))
      {
        String columnType = IDataUtil.getString(cursor, "type");
        String pattern = IDataUtil.getString(cursor, "pattern");
        cursor.destroy();
        return new String[] { columnType, pattern };
      }
      cursor.destroy();
    }
    return new String[] { null, null };
  }
  // --- <<IS-END-SHARED>> ---
}
