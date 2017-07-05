package ps.util;

//-----( IS Java Code Template v1.2
//-----( CREATED: 2008-01-08 21:00:00 EDT
//-----( ON-HOST: MCRSINGH.AME.ad.sag

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.text.*;
import java.util.*;
import java.util.regex.*;

// --- <<IS-END-IMPORTS>> ---

public final class date

{
  // ---( internal utility methods )---

  final static date _instance = new date();

  static date _newInstance()
  {
    return new date();
  }

  static date _cast(Object o)
  {
    return (date) o;
  }

  // ---( server methods )---

  public static final void getCurrentDateString(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(getCurrentDateString)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] field:0:required pattern
    // [i] field:0:optional timezone
    // [i] field:0:optional locale
    // [o] field:0:required value
    IDataCursor idcPipeline = pipeline.getCursor();
    try
    {
      String inPattern = "";
      if (idcPipeline.first("pattern"))
      {
        inPattern = (String) idcPipeline.getValue();
      }
      else
      {
        throw new ServiceException("Input parameter 'pattern' was not found.");
      }

      IData output = Service.doInvoke("pub.date", "getCurrentDateString", pipeline);
      IDataCursor outCur = output.getCursor();

      String stDate = IDataUtil.getString(outCur, "value");
      IDataUtil.remove(outCur, "value");

      outCur.destroy();

      if (inPattern.endsWith("Z"))
      {
        Matcher matcher = specialTimezonePattern.matcher(stDate);
        if (matcher.find())
        {
          stDate = matcher.replaceAll(":" + matcher.group(1));
        }
      }

      idcPipeline.insertAfter("value", stDate);
    }
    catch (Exception e)
    {
      throw new ServiceException(e.getMessage());
    }

    idcPipeline.destroy();
    // --- <<IS-END>> ---

  }

  // --- <<IS-START-SHARED>> ---
  private static Pattern specialTimezonePattern = Pattern.compile(":?(\\d{2})$");

  private static boolean compareDateStrings(String value1, String value2, String operator, String pattern)
  {
    if (value1 == null || value2 == null)
    {
      return false;
    }

    String datePattern = pattern;

    if (pattern == null)
    {
      datePattern = "yyyy-MM-dd HH:mm:ss.S";
    }
    long time1;
    long time2;

    DateFormat dateFormat = new SimpleDateFormat(datePattern);
    try
    {
      Date date1 = dateFormat.parse(value1);
      Date date2 = dateFormat.parse(value2);
      time1 = date1.getTime();
      time2 = date2.getTime();
    }
    catch (ParseException pe)
    {
      return false;
    }

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
  // --- <<IS-END-SHARED>> ---
}
