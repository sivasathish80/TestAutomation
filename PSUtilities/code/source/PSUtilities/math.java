package PSUtilities;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2005-04-20 16:25:24 PDT
// -----( ON-HOST: D600-Loaner1.mckesson.com

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.text.DecimalFormat;
import java.math.*;

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

  public static final void addDoubles(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(addDoubles)>> ---
    // @sigtype java 3.5
    // [i] field:0:required number1
    // [i] field:0:required number2
    // [i] field:0:optional scale
    // [o] field:0:required result
    /**
     * Service replaces pub.math:addFloats, as that service suffers from issues
     * with the core JVM's handling of the addition of floating-point numbers.
     * Service takes three inputs: number1: The first number to be added. Must
     * be a double. number2: The second number to be added. Must be a double.
     * scale (optional): The scale of the result. Defaults to 5.
     * 
     * The "result" will be a String that contains a double with the specified
     * scale.
     * 
     * Any errors encountered will cause a
     * com.wm.app.b2b.server.ServiceException to be thrown.
     * 
     * **NOTE**: Empirical evidence shows that results may begin to lose their
     * reliability at larger scales. Scales lower than 10 seem to work fine in
     * limited testing.
     * 
     */

    // Instantiate a cursor for access to the pipeline
    IDataCursor pipelineCursor = pipeline.getCursor();

    // Create all output & working variables
    String number1String = new String();
    double number1;
    String number2String = new String();
    double number2;
    String resultString = new String();
    double result;
    String scaleString = new String();
    int scale;
    int workingScale;
    BigDecimal bDNumber1;
    BigDecimal bDNumber2;
    BigDecimal bDResult;

    // Input Variables
    if (pipelineCursor.first("number1") == false)
    {
      throw new ServiceException("No value was specified for \"number1\".");
    }
    number1String = (String) pipelineCursor.getValue();
    try
    {
      number1 = (Double.valueOf(number1String)).doubleValue();
    }
    catch (NumberFormatException nfEx)
    {
      throw new ServiceException("The input variable \"number1\" must be a float or " + "a double.");
    }

    if (pipelineCursor.first("number2") == false)
    {
      throw new ServiceException("No value was specified for \"number2\".");
    }
    number2String = (String) pipelineCursor.getValue();
    try
    {
      number2 = (Double.valueOf(number2String)).doubleValue();
    }
    catch (NumberFormatException nfEx)
    {
      throw new ServiceException("The input variable \"number2\" must be a float or " + "a double.");
    }

    if (pipelineCursor.first("scale") == false)
    {
      // scale is not mandatory. Default to 5.
      scale = 5;
    }
    else
    {
      scaleString = (String) pipelineCursor.getValue();
      try
      {
        scale = (Integer.valueOf(scaleString)).intValue();
      }
      catch (NumberFormatException nfEx)
      {
        throw new ServiceException("The input variable \"scale\" must be an " + "integer.");
      }
    }

    // Create the BigDecimal objects representing the numbers that were
    // provided.
    bDNumber1 = new BigDecimal(number1);
    bDNumber2 = new BigDecimal(number2);

    // Service body
    /*
     * Set scale. This is critical, and it is this statement that defeats the
     * JVM bug where floating-point results are unpredictable.
     */
    bDNumber1 = bDNumber1.setScale(scale, BigDecimal.ROUND_HALF_UP);

    // Compute the "result".
    bDResult = bDNumber1.add(bDNumber2);

    /*
     * Set scale. This is critical, and it is this statement that defeats the
     * JVM bug where floating-point results are unpredictable.
     */
    bDResult = bDResult.setScale(scale, BigDecimal.ROUND_HALF_UP);

    result = bDResult.doubleValue();
    resultString = String.valueOf(result);

    // Populate service output
    if (pipelineCursor.first("result"))
      pipelineCursor.delete();
    pipelineCursor.insertAfter("result", resultString);
    pipelineCursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void compareDoubles(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(compareDoubles)>> ---
    // @sigtype java 3.5
    // [i] field:0:required firstNumber
    // [i] field:0:required secondNumber
    // [o] field:0:required output

    String s1 = "";
    String s2 = "";
    IDataCursor pipelineCursor = pipeline.getCursor();
    if (pipelineCursor.first("firstNumber"))
    {
      s1 = (String) pipelineCursor.getValue();
    }
    else
    {
      throw new ServiceException("Input firstNumber cannot be null!");
    }

    if (pipelineCursor.first("secondNumber"))
    {
      s2 = (String) pipelineCursor.getValue();
    }
    else
    {
      throw new ServiceException("Input secondNumber cannot be null!");
    }

    float firstNumber = Float.valueOf(s1).floatValue();
    float secondNumber = Float.valueOf(s2).floatValue();

    if (firstNumber == secondNumber)
    {
      pipelineCursor.insertAfter("output", "0");
    }
    else
    {
      if (firstNumber > secondNumber)
        pipelineCursor.insertAfter("output", "1");
      else
        pipelineCursor.insertAfter("output", "-1");
    }
    // --- <<IS-END>> ---

  }

  public static final void compareFloats(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(compareFloats)>> ---
    // @sigtype java 3.5
    // [i] field:0:required firstNumber
    // [i] field:0:required secondNumber
    // [o] field:0:required output

    String s1 = "";
    String s2 = "";
    IDataCursor pipelineCursor = pipeline.getCursor();
    if (pipelineCursor.first("firstNumber"))
    {
      s1 = (String) pipelineCursor.getValue();
    }
    else
    {
      throw new ServiceException("Input firstNumber cannot be null!");
    }

    if (pipelineCursor.first("secondNumber"))
    {
      s2 = (String) pipelineCursor.getValue();
    }
    else
    {
      throw new ServiceException("Input secondNumber cannot be null!");
    }

    double firstNumber = Double.valueOf(s1).doubleValue();
    double secondNumber = Double.valueOf(s2).doubleValue();

    if (firstNumber == secondNumber)
    {
      pipelineCursor.insertAfter("output", "0");
    }
    else
    {
      if (firstNumber > secondNumber)
        pipelineCursor.insertAfter("output", "1");
      else
        pipelineCursor.insertAfter("output", "-1");
    }
    // --- <<IS-END>> ---

  }

  public static final void compareInts(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(compareInts)>> ---
    // @sigtype java 3.5
    // [i] field:0:required firstNumber
    // [i] field:0:required secondNumber
    // [o] field:0:required output

    IDataCursor idcPipeline = pipeline.getCursor();
    int firstNumber = 0;
    int secondNumber = 0;

    if (idcPipeline.first("firstNumber"))
    {
      String strFirstNumber = (String) idcPipeline.getValue(); // MCK
      if (!strFirstNumber.trim().equals("")) // MCK
      { // MCK
        firstNumber = Integer.parseInt(strFirstNumber);
      } // MCK
    }
    else
    {
      throw new ServiceException("Input firstNumber cannot be null!");
    }

    if (idcPipeline.first("secondNumber"))
    {
      String strSecondNumber = (String) idcPipeline.getValue(); // MCK
      if (!strSecondNumber.trim().equals("")) // MCK
      { // MCK
        secondNumber = Integer.parseInt(strSecondNumber);
      } // MCK
    }
    else
    {
      throw new ServiceException("Input secondNumber cannot be null!");
    }

    if (firstNumber == secondNumber)
    {
      idcPipeline.insertAfter("output", "0");
    }
    else
    {
      if (firstNumber > secondNumber)
        idcPipeline.insertAfter("output", "1");
      else
        idcPipeline.insertAfter("output", "-1");
    }

    // Clean up IData cursors
    idcPipeline.destroy();
    // --- <<IS-END>> ---

  }

  public static final void divideDoubles(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(divideDoubles)>> ---
    // @sigtype java 3.5
    // [i] field:0:required number1
    // [i] field:0:required number2
    // [i] field:0:optional scale
    // [o] field:0:required result
    /**
     * Service replaces pub.math:divideFloats, as that service suffers from
     * issues with the core JVM's handling of the division of floating-point
     * numbers. Service takes three inputs: number1: The dividend. Must be a
     * double. number2: The divisor. Must be a double. (Note that
     * dividend/divisor = quotient.) scale (optional): The scale of the result.
     * Defaults to 5.
     * 
     * The "result" will be the quotient, represented as a String, that contains
     * a double with the specified scale.
     * 
     * Any errors encountered will cause a
     * com.wm.app.b2b.server.ServiceException to be thrown.
     * 
     * **NOTE**: Empirical evidence shows that results may begin to lose their
     * reliability at larger scales. Scales lower than 10 seem to work fine in
     * limited testing.
     * 
     */

    // Instantiate a cursor for access to the pipeline
    IDataCursor pipelineCursor = pipeline.getCursor();

    // Create all output & working variables
    String number1String = new String();
    double number1;
    String number2String = new String();
    double number2;
    String resultString = new String();
    double result;
    String scaleString = new String();
    int scale;
    int workingScale;
    BigDecimal bDNumber1;
    BigDecimal bDNumber2;
    BigDecimal bDResult;

    // Input Variables
    // Input Variables
    if (pipelineCursor.first("number1") == false)
    {
      throw new ServiceException("No value was specified for \"number1\".");
    }
    number1String = (String) pipelineCursor.getValue();
    try
    {
      number1 = (Double.valueOf(number1String)).doubleValue();
    }
    catch (NumberFormatException nfEx)
    {
      throw new ServiceException("The input variable \"number1\" must be a float or " + "a double.");
    }

    if (pipelineCursor.first("number2") == false)
    {
      throw new ServiceException("No value was specified for \"number2\".");
    }
    number2String = (String) pipelineCursor.getValue();
    try
    {
      number2 = (Double.valueOf(number2String)).doubleValue();
    }
    catch (NumberFormatException nfEx)
    {
      throw new ServiceException("The input variable \"number2\" must be a float or " + "a double.");
    }

    if (pipelineCursor.first("scale") == false)
    {
      // scale is not mandatory. Default to 5.
      scale = 5;
    }
    else
    {
      scaleString = (String) pipelineCursor.getValue();
      try
      {
        scale = (Integer.valueOf(scaleString)).intValue();
      }
      catch (NumberFormatException nfEx)
      {
        throw new ServiceException("The input variable \"scale\" must be an " + "integer.");
      }
    }

    // Create the BigDecimal objects representing the numbers that were
    // provided.
    bDNumber1 = new BigDecimal(number1);
    bDNumber2 = new BigDecimal(number2);

    // Service body
    /*
     * Set scale. This is critical, and it is this statement that defeats the
     * JVM bug where floating-point results are unpredictable.
     */
    bDNumber1 = bDNumber1.setScale(scale, BigDecimal.ROUND_HALF_UP);

    // Compute the "result".
    bDResult = bDNumber1.divide(bDNumber2, BigDecimal.ROUND_HALF_UP);

    /*
     * Set scale. This is critical, and it is this statement that defeats the
     * JVM bug where floating-point results are unpredictable.
     */
    bDResult = bDResult.setScale(scale, BigDecimal.ROUND_HALF_UP);

    result = bDResult.doubleValue();
    resultString = String.valueOf(result);

    // Populate service output
    if (pipelineCursor.first("result"))
      pipelineCursor.delete();
    pipelineCursor.insertAfter("result", resultString);
    pipelineCursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void getAbsoluteValue(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(getAbsoluteValue)>> ---
    // @sigtype java 3.5
    // [i] field:0:required number
    // [o] field:0:required positiveNumber

    IDataCursor idcPipeline = pipeline.getCursor();
    if (!idcPipeline.first("number"))
    {
      throw new ServiceException("input number was null!");
    }
    String strNumber = (String) idcPipeline.getValue();

    double dblPositiveNumber = Math.abs(Double.parseDouble(strNumber));

    idcPipeline.insertAfter("positiveNumber", Double.toString(dblPositiveNumber));
    idcPipeline.destroy();
    // --- <<IS-END>> ---

  }

  public static final void multiplyDoubles(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(multiplyDoubles)>> ---
    // @sigtype java 3.5
    // [i] field:0:required number1
    // [i] field:0:required number2
    // [i] field:0:optional scale
    // [o] field:0:required result
    /**
     * Service replaces pub.math:multiplyFloats, as that service suffers from
     * issues with the core JVM's handling of the multiplication of
     * floating-point numbers. Service takes three inputs: number1: The first
     * number to be multiplied. Must be a double. number2: The second number to
     * be multiplied. Must be a double. scale (optional): The scale of the
     * result. Defaults to 5.
     * 
     * The "result" will be a String that contains a double with the specified
     * scale.
     * 
     * Any errors encountered will cause a
     * com.wm.app.b2b.server.ServiceException to be thrown.
     * 
     * **NOTE**: Empirical evidence shows that results may begin to lose their
     * reliability at larger scales. Scales lower than 10 seem to work fine in
     * limited testing.
     *
     */

    // Instantiate a cursor for access to the pipeline
    IDataCursor pipelineCursor = pipeline.getCursor();

    // Create all output & working variables
    String number1String = new String();
    double number1;
    String number2String = new String();
    double number2;
    String resultString = new String();
    double result;
    String scaleString = new String();
    int scale;
    int workingScale;
    BigDecimal bDNumber1;
    BigDecimal bDNumber2;
    BigDecimal bDResult;

    // Input Variables
    if (pipelineCursor.first("number1") == false)
    {
      throw new ServiceException("No value was specified for \"number1\".");
    }
    number1String = (String) pipelineCursor.getValue();
    try
    {
      number1 = (Double.valueOf(number1String)).doubleValue();
    }
    catch (NumberFormatException nfEx)
    {
      throw new ServiceException("The input variable \"number1\" must be a float or " + "a double.");
    }

    if (pipelineCursor.first("number2") == false)
    {
      throw new ServiceException("No value was specified for \"number2\".");
    }
    number2String = (String) pipelineCursor.getValue();
    try
    {
      number2 = (Double.valueOf(number2String)).doubleValue();
    }
    catch (NumberFormatException nfEx)
    {
      throw new ServiceException("The input variable \"number2\" must be a float or " + "a double.");
    }

    if (pipelineCursor.first("scale") == false)
    {
      // scale is not mandatory. Default to 5.
      scale = 5;
    }
    else
    {
      scaleString = (String) pipelineCursor.getValue();
      try
      {
        scale = (Integer.valueOf(scaleString)).intValue();
      }
      catch (NumberFormatException nfEx)
      {
        throw new ServiceException("The input variable \"scale\" must be an " + "integer.");
      }
    }

    // Create the BigDecimal objects representing the numbers that were
    // provided.
    bDNumber1 = new BigDecimal(number1);
    bDNumber2 = new BigDecimal(number2);

    // Service body
    /*
     * Set scale. This is critical, and it is this statement that defeats the
     * JVM bug where floating-point results are unpredictable.
     */
    bDNumber1 = bDNumber1.setScale(scale, BigDecimal.ROUND_HALF_UP);

    // Compute the "result".
    bDResult = bDNumber1.multiply(bDNumber2);

    /*
     * Set scale. This is critical, and it is this statement that defeats the
     * JVM bug where floating-point results are unpredictable.
     */
    bDResult = bDResult.setScale(scale, BigDecimal.ROUND_HALF_UP);

    result = bDResult.doubleValue();
    resultString = String.valueOf(result);

    // Populate service output
    if (pipelineCursor.first("result"))
      pipelineCursor.delete();
    pipelineCursor.insertAfter("result", resultString);
    pipelineCursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void roundNumber(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(roundNumber)>> ---
    // @sigtype java 3.5
    // [i] field:0:required number
    // [i] field:0:required precision
    // [o] field:0:required roundedNumber

    IDataCursor idcPipeline = pipeline.getCursor();
    if (!idcPipeline.first("number"))
    {
      throw new ServiceException("input number was null!");
    }

    String strNumber = (String) idcPipeline.getValue();
    double dblNumber = Double.parseDouble(strNumber);

    if (idcPipeline.first("precision"))
    {
      String strPrecision = (String) idcPipeline.getValue();
      double dblPrecision = Double.parseDouble(strPrecision);

      double dblMulDivFactor = java.lang.Math.pow(10, dblPrecision);
      double dblRoundedNumber = java.lang.Math.round(dblNumber * dblMulDivFactor) / dblMulDivFactor;

      idcPipeline.insertAfter("roundedNumber", Double.toString(dblRoundedNumber));

    }
    else
    {
      idcPipeline.insertAfter("roundedNumber", strNumber);

    }

    idcPipeline.destroy();
    // --- <<IS-END>> ---

  }

  public static final void subtractDoubles(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(subtractDoubles)>> ---
    // @sigtype java 3.5
    // [i] field:0:required number1
    // [i] field:0:required number2
    // [i] field:0:optional scale
    // [o] field:0:required result
    /**
     * Service replaces pub.math:subtractFloats, as that service suffers from
     * issues with the core JVM's handling of the subtraction of floating-point
     * numbers. Service takes three inputs: number1: The number from which the
     * second is to be subtracted. Must be a double. number2: The number to be
     * subtracted from the first. Must be a double. scale (optional): The scale
     * of the result. Defaults to 5.
     * 
     * The "result" will be a String that contains a double with the specified
     * scale.
     * 
     * Any errors encountered will cause a
     * com.wm.app.b2b.server.ServiceException to be thrown.
     * 
     * **NOTE**: Empirical evidence shows that results may begin to lose their
     * reliability at larger scales. Scales lower than 10 seem to work fine in
     * limited testing.
     * 
     */

    // Instantiate a cursor for access to the pipeline
    IDataCursor pipelineCursor = pipeline.getCursor();

    // Create all output & working variables
    String number1String = new String();
    double number1;
    String number2String = new String();
    double number2;
    String resultString = new String();
    double result;
    String scaleString = new String();
    int scale;
    int workingScale;
    BigDecimal bDNumber1;
    BigDecimal bDNumber2;
    BigDecimal bDResult;

    // Input Variables
    if (pipelineCursor.first("number1") == false)
    {
      throw new ServiceException("No value was specified for \"number1\".");
    }
    number1String = (String) pipelineCursor.getValue();
    try
    {
      number1 = (Double.valueOf(number1String)).doubleValue();
    }
    catch (NumberFormatException nfEx)
    {
      throw new ServiceException("The input variable \"number1\" must be a float or " + "a double.");
    }

    if (pipelineCursor.first("number2") == false)
    {
      throw new ServiceException("No value was specified for \"number2\".");
    }
    number2String = (String) pipelineCursor.getValue();
    try
    {
      number2 = (Double.valueOf(number2String)).doubleValue();
    }
    catch (NumberFormatException nfEx)
    {
      throw new ServiceException("The input variable \"number2\" must be a float or " + "a double.");
    }

    if (pipelineCursor.first("scale") == false)
    {
      // scale is not mandatory. Default to 5.
      scale = 5;
    }
    else
    {
      scaleString = (String) pipelineCursor.getValue();
      try
      {
        scale = (Integer.valueOf(scaleString)).intValue();
      }
      catch (NumberFormatException nfEx)
      {
        throw new ServiceException("The input variable \"scale\" must be an " + "integer.");
      }
    }

    // Create the BigDecimal objects representing the numbers that were
    // provided.
    bDNumber1 = new BigDecimal(number1);
    bDNumber2 = new BigDecimal(number2);

    // Service body
    /*
     * Set scale. This is critical, and it is this statement that defeats the
     * JVM bug where floating-point results are unpredictable.
     */
    bDNumber1 = bDNumber1.setScale(scale, BigDecimal.ROUND_HALF_UP);

    // Compute the "result".
    bDResult = bDNumber1.subtract(bDNumber2);

    /*
     * Set scale. This is critical, and it is this statement that defeats the
     * JVM bug where floating-point results are unpredictable.
     */
    bDResult = bDResult.setScale(scale, BigDecimal.ROUND_HALF_UP);

    result = bDResult.doubleValue();
    resultString = String.valueOf(result);

    // Populate service output
    if (pipelineCursor.first("result"))
      pipelineCursor.delete();
    pipelineCursor.insertAfter("result", resultString);
    pipelineCursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void trimZeroes(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(trimZeroes)>> ---
    // @sigtype java 3.5
    // [i] field:0:required number
    // [o] field:0:required trimmedNumber

    IDataCursor idcPipeline = pipeline.getCursor();
    if (!idcPipeline.first("number"))
    {
      throw new ServiceException("input number was null!");
    }

    String strNumber = (String) idcPipeline.getValue();
    double dblNumber = Double.parseDouble(strNumber);

    DecimalFormat df = new DecimalFormat("#");

    // idcPipeline.insertAfter("trimmedNumber", Double.toString(dblNumber));
    idcPipeline.insertAfter("trimmedNumber", df.format(dblNumber));

    idcPipeline.destroy();
    // --- <<IS-END>> ---

  }

  public static final void truncateNumber(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(truncateNumber)>> ---
    // @sigtype java 3.5
    // [i] field:0:required number
    // [i] field:0:required precision
    // [o] field:0:required truncatedNumber

    IDataCursor idcPipeline = pipeline.getCursor();
    if (!idcPipeline.first("number"))
    {
      throw new ServiceException("input number was null!");
    }

    String strNumber = (String) idcPipeline.getValue();
    double dblNumber = Double.parseDouble(strNumber);

    if (idcPipeline.first("precision"))
    {
      String strPrecision = (String) idcPipeline.getValue();
      double dblPrecision = Double.parseDouble(strPrecision);

      double dblMulDivFactor = java.lang.Math.pow(10, dblPrecision);
      double dblTruncatedNumber = java.lang.Math.floor(dblNumber * dblMulDivFactor) / dblMulDivFactor;

      if (strPrecision.equals("0")) // Remove decimal if precision = 0
      {
        Double d = new Double(dblTruncatedNumber);
        idcPipeline.insertAfter("truncatedNumber", Long.toString(d.longValue()));
      }
      else
      {
        idcPipeline.insertAfter("truncatedNumber", Double.toString(dblTruncatedNumber));
      }

    }
    else
    {
      idcPipeline.insertAfter("truncatedNumber", strNumber);

    }

    idcPipeline.destroy();
    // --- <<IS-END>> ---

  }
}
