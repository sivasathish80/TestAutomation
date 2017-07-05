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
import java.util.Random;
import java.math.BigDecimal;

// --- <<IS-END-IMPORTS>> ---

public final class math

{
  // ---( internal utility methods )---

  final static math _instance = new math();

  static math _newInstance()
  {
    return new math();
  }

  static math _cast(Object o)
  {
    return (math) o;
  }

  // ---( server methods )---

  public static final void getAbsVal(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(getAbsVal)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] field:0:required inputVal
    // [o] field:0:required absVal
    IDataCursor cursor = pipeline.getCursor();

    String strVal = IDataUtil.getString(cursor, "inputVal");

    double absDblVal = Math.abs(Double.parseDouble(strVal));
    Double dblObj = new Double(absDblVal);

    NumberFormat nf = NumberFormat.getInstance();
    nf.setGroupingUsed(false);
    nf.setMaximumFractionDigits(2);
    nf.setMinimumFractionDigits(2);

    IDataUtil.put(cursor, "absVal", nf.format(dblObj.doubleValue()));
    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void getRandom(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(getRandom)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] object:0:optional randomizer
    // [i] field:0:optional seed
    // [i] field:0:optional minRange
    // [i] field:0:optional maxRange
    // [i] field:0:optional wholeNumbers {"false","true"}
    // [o] field:0:required random
    IDataCursor cursor = pipeline.getCursor();
    Random random;
    double minRange = 0.0;
    double maxRange = 1.0;
    boolean wholeNumbers = false;

    if (cursor.first("randomizer"))
    {
      random = (Random) cursor.getValue();
    }
    else
    {
      random = new Random();
      if (cursor.first("seed"))
      {
        random.setSeed(Long.parseLong((String) cursor.getValue()));
      }
    }

    if (cursor.first("minRange"))
    {
      minRange = Double.parseDouble((String) cursor.getValue());
    }
    if (cursor.first("maxRange"))
    {
      maxRange = Double.parseDouble((String) cursor.getValue());
    }

    if (cursor.first("wholeNumbers"))
    {
      wholeNumbers = new Boolean((String) cursor.getValue()).booleanValue();
      if (wholeNumbers == true)
      {
        maxRange += 1;
      }
    }

    double randomDouble = random.nextDouble() * (maxRange - minRange) + minRange;
    if (wholeNumbers == true)
    {
      IDataUtil.put(cursor, "random", String.valueOf((long) randomDouble));
    }
    else
    {
      IDataUtil.put(cursor, "random", String.valueOf(randomDouble));
    }

    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void getRandomizer(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(getRandomizer)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] field:0:optional seed
    // [o] object:0:required randomizer
    IDataCursor cursor = pipeline.getCursor();
    Random random = new Random();

    if (cursor.first("seed"))
    {
      random.setSeed(Long.parseLong((String) cursor.getValue()));
    }

    IDataUtil.put(cursor, "randomizer", random);
    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void modDivideInts(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(modDivideInts)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] field:0:required numerator
    // [i] field:0:required denominator
    // [o] field:0:required quotient
    // [o] field:0:required remainder
    IDataCursor cursor = pipeline.getCursor();

    int numerator;
    int denominator;

    if (cursor.first("numerator"))
    {
      numerator = Integer.parseInt((String) cursor.getValue());
    }
    else
    {
      throw new ServiceException("Missing input 'numerator'");
    }

    if (cursor.first("denominator"))
    {
      denominator = Integer.parseInt((String) cursor.getValue());
    }
    else
    {
      throw new ServiceException("Missing input 'denominator'");
    }

    IDataUtil.put(cursor, "quotient", String.valueOf(numerator / denominator));
    IDataUtil.put(cursor, "remainder", String.valueOf(numerator % denominator));
    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void round(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(round)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] field:0:required number
    // [i] field:0:optional precision
    // [i] field:0:optional method {"ROUND_HALF_UP","ROUND_UP","ROUND_DOWN","ROUND_CEILING","ROUND_FLOOR","ROUND_HALF_DOWN","ROUND_HALF_EVEN"}
    // [o] field:0:required newNumber
    IDataCursor cursor = pipeline.getCursor();

    double number = 0;
    int precision = 2;

    if (IDataUtil.getString(cursor, "number") == null)
    {
      cursor.destroy();
      return;
    }

    if (cursor.first("number"))
    {
      number = Double.parseDouble((String) cursor.getValue());
    }
    else
    {
      throw new ServiceException("Missing input 'number'");
    }

    if (cursor.first("precision"))
    {
      precision = Integer.parseInt((String) cursor.getValue());
    }

    IDataUtil.put(cursor, "newNumber", new BigDecimal(number).setScale(precision, getRoundingMethod(IDataUtil.getString(cursor, "roundingMethod"))).toString());
    IDataUtil.put(cursor, "method", String.valueOf(getRoundingMethod(IDataUtil.getString(cursor, "roundingMethod"))));
    cursor.destroy();
    // --- <<IS-END>> ---

  }

  // --- <<IS-START-SHARED>> ---

  private static int getRoundingMethod(String roundingMethod)
  {
    if ("ROUND_UP".equals(roundingMethod))
    {
      return BigDecimal.ROUND_UP;
    }

    if ("ROUND_DOWN".equals(roundingMethod))
    {
      return BigDecimal.ROUND_DOWN;
    }

    if ("ROUND_CEILING".equals(roundingMethod))
    {
      return BigDecimal.ROUND_CEILING;
    }

    if ("ROUND_FLOOR".equals(roundingMethod))
    {
      return BigDecimal.ROUND_FLOOR;
    }

    if ("ROUND_HALF_DOWN".equals(roundingMethod))
    {
      return BigDecimal.ROUND_HALF_DOWN;
    }

    if ("ROUND_HALF_EVEN".equals(roundingMethod))
    {
      return BigDecimal.ROUND_HALF_EVEN;
    }

    return BigDecimal.ROUND_HALF_UP;
  }
  // --- <<IS-END-SHARED>> ---
}
