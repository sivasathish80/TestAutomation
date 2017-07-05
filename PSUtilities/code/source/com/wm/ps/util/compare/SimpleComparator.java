package com.wm.ps.util.compare;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataUtil;

public class SimpleComparator implements Comparator
{

  private String key;
  private String type;
  private boolean reverse;
  private SimpleDateFormat currentFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
   */
  public int compare(Object o1, Object o2)
  {
    IDataCursor cursor1 = null;
    IDataCursor cursor2 = null;
    try
    {
      if (reverse)
      {
        cursor1 = ((IData) o2).getCursor();
        cursor2 = ((IData) o1).getCursor();
      }
      else
      {
        cursor1 = ((IData) o1).getCursor();
        cursor2 = ((IData) o2).getCursor();
      }

      if (this.type.equals("Numeric"))
      {
        double key1 = Double.valueOf(IDataUtil.getString(cursor1, key)).doubleValue();
        double key2 = Double.valueOf(IDataUtil.getString(cursor2, key)).doubleValue();
        cursor1.destroy();
        cursor2.destroy();
        if (key1 == key2)
        {
          return 0;
        }

        return key1 - key2 > 0 ? 1 : -1;
      }
      else if (this.type.equals("Date"))
      {
        Date d = currentFormat.parse(IDataUtil.getString(cursor1, key));
        Date d1 = currentFormat.parse(IDataUtil.getString(cursor2, key));
        cursor1.destroy();
        cursor2.destroy();
        return d.compareTo(d1);
      }
      else if (this.type.equals("String"))
      {
        String key1 = IDataUtil.getString(cursor1, key);
        String key2 = IDataUtil.getString(cursor2, key);
        cursor1.destroy();
        cursor2.destroy();
        return key1.compareTo(key2);
      }
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }

    return 0;
  }

  public SimpleComparator(String key, String type, String pattern, boolean reverse)
  {

    this.type = type;
    this.key = key;
    this.reverse = reverse;
    if (pattern != null)
    {
      currentFormat = new SimpleDateFormat(pattern);
    }
  }
}