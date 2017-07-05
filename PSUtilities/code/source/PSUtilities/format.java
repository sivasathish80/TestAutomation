package PSUtilities;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2005-04-20 18:31:05 PDT
// -----( ON-HOST: D600-Loaner1.mckesson.com

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.math.BigDecimal;

// --- <<IS-END-IMPORTS>> ---

public final class format

{
  // ---( internal utility methods )---

  final static format _instance = new format();

  static format _newInstance()
  {
    return new format();
  }

  static format _cast(Object o)
  {
    return (format) o;
  }

  // ---( server methods )---

  public static final void convertPackedDecimalToString(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(convertPackedDecimalToString)>> ---
    // @sigtype java 3.5
    // [i] object:0:required bytes
    // [o] field:0:required integer

    IDataCursor idc = pipeline.getCursor();
    byte packedBytes[] = null;
    int answer = 0;

    if (idc.first("bytes"))
    {
      packedBytes = (byte[]) idc.getValue();
    }
    if (packedBytes != null)
    {
      if (!(packedBytes instanceof byte[]))
      {
        idc.destroy(); // error case
      }
      else
      {
        // Packed Decimal into an int
        int i0 = (int) packedBytes[packedBytes.length - 1] >>> 4;
        int i1 = (int) packedBytes[packedBytes.length - 1] & (15);
        boolean plus = (i1 == 12) | (i1 == 15); // determine
        // sign digit
        answer = i0;
        int digit = 10; // Power of 10 - each digit's
        // place value

        for (int i = packedBytes.length - 2; i > -1; i--)
        {
          byte x = packedBytes[i];
          i0 = (int) x >>> 4; // shift right 4 to get high
          // order digit
          i1 = (int) x & (15); // AND with X'0F' to get low
          // order digit
          answer += i1 * digit;
          digit *= 10;
          answer += i0 * digit;
          digit *= 10;
        }
        answer = (plus ? answer : -1 * answer); // return signed result
      }
    }

    Integer result = new Integer(answer);

    // pipeline
    idc = pipeline.getCursor();
    idc.last();
    idc.insertAfter("integer", result.toString());
    idc.destroy();

    // --- <<IS-END>> ---

  }

  public static final void convertStringToPackedDecimal(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(convertStringToPackedDecimal)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] field:0:required Integer
    // [i] field:0:required Sign
    // [o] object:0:required ByteStreamPackedDecimalValue

    IDataCursor idc = pipeline.getCursor();
    int number = 0;
    String sign = "";
    String errorText = "";

    // get input number
    number = IDataUtil.getInt(idc, "Integer", -1);
    // get sign:
    // + = positive
    // - = negative
    // blank = unsigned
    sign = IDataUtil.getString(idc, "Sign");
    if (sign == null)
    {
      sign = "";
    }

    if (number > 0)
    {
      int byteLength = 0;
      String strNumber = "" + number;
      int inputLength = strNumber.length();

      // if number of digits is even, need an extra byte, because of sign
      if (inputLength % 2 == 0)
      {
        byteLength = ((inputLength + 1) / 2) + 1;
      }
      else
      // need a nibble for the sign
      {
        byteLength = (inputLength / 2) + 1;
      }
      byte[] output = new byte[byteLength];

      int leftNibbleValue = 0;
      int rightNibbleValue = 0;
      int byteValue = 0;
      int powerOfTen = 0; // for finding place of digits within the number
                          // (10^0, 10^1, etc.)

      // set position in byte array
      int bytePosition = byteLength - 1;
      int iterations = 0;

      // iterate through positions in input number, from right to left
      for (int i = inputLength; i > 0; i--)
      {
        // decremement the bytePosition every 2nd time through the loop (2
        // digits per byte)
        // and every other time through, set a flag to set the left nibble value
        // (1st time through, both nibbles, a whole byte, get set;
        // 2nd time through, 2nd byte's right nibble gets set;
        // 3rd time, 2nd byte's left nibble gets set; etc.)
        iterations++;
        boolean setLeftNibble = false;
        if (iterations % 2 == 0)
        {
          bytePosition--;
        }
        else
        {
          setLeftNibble = true;
        }

        int digit = 0;

        // handle the right-most byte
        if (i == inputLength)
        {
          if (sign.equals("+")) // last nibble is C
          {
            rightNibbleValue = 12;
          }
          else if (sign.equals("-")) // last nibble is D
          {
            rightNibbleValue = 13;
          }
          else
          // unsigned -- last nibble is F
          {
            rightNibbleValue = 15;
          }

          // get the right-most digit
          digit = number % 10;
          leftNibbleValue = digit << 4;
          byteValue = leftNibbleValue | rightNibbleValue;
          output[bytePosition] = (byte) byteValue;
          rightNibbleValue = 0;
          leftNibbleValue = 0;
        }
        else
        {
          // get the next digit
          powerOfTen++;
          int place = (int) Math.pow(10, powerOfTen);
          digit = (number / place) % 10;

          // set leftNibble and byte
          if (setLeftNibble)
          {
            leftNibbleValue = digit << 4;
            byteValue = leftNibbleValue | rightNibbleValue;
            output[bytePosition] = (byte) byteValue;
            rightNibbleValue = 0;
            leftNibbleValue = 0;
          }
          // set rightNibble and, if i = 1 (first digit of number), set byte
          else
          {
            if (i > 1)
            {
              rightNibbleValue = digit;
            }
            else
            // if output has odd number of nibbles
            {
              output[bytePosition] = (byte) digit;
            }
          }
        }
      }

      // pipeline out
      idc.last();
      idc.insertAfter("ByteStreamPackedDecimalValue", output);
      idc.destroy();
    }
    else
    {
      throw new ServiceException("Number isn't in integer format");
    }
    // --- <<IS-END>> ---

  }

  public static final void convertStringToZonedDecimal(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(convertStringToZonedDecimal)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] field:0:required string
    // [i] field:0:required length
    // [i] field:0:required decimals
    // [i] field:0:optional signed {"false","true"}
    // [o] field:0:required zonedDecimal
    // [o] field:0:optional sign {"+","-"}

    IDataCursor idc = pipeline.getCursor();
    StringBuffer sbZonedDecimal = null;
    String strZonedDecimal = null;
    int length = 0;
    int decimals = 0;
    boolean signed = false;
    String sign = "+";

    if (idc.first("string"))
    {
      strZonedDecimal = (String) idc.getValue();
      sbZonedDecimal = new StringBuffer(strZonedDecimal);
    }
    else
    {
      idc.insertAfter("zonedDecimal", null);
      idc.destroy();
      return;
    }
    if (idc.first("length"))
    {
      length = Integer.parseInt((String) idc.getValue());
    }
    if (idc.first("decimals"))
    {
      decimals = Integer.parseInt((String) idc.getValue());
    }
    if (idc.first("signed"))
    {
      String strSigned = (String) idc.getValue();
      if (strSigned.equals("true"))
      {
        signed = true;
      }
    }

    // Remove decimal
    int i = sbZonedDecimal.indexOf(".");
    if (i >= 0)
    {
      sbZonedDecimal.deleteCharAt(i);
    }
    BigDecimal bd = new BigDecimal(0);
    if (!strZonedDecimal.trim().equals(""))
    {
      bd = new BigDecimal(sbZonedDecimal.toString());
    }
    else
    {
      idc.insertAfter("zonedDecimal", "");
      idc.destroy();
      return;
    }

    int len = sbZonedDecimal.length();
    char c = sbZonedDecimal.charAt(len - 1);
    char r = c;
    BigDecimal zero = new BigDecimal(0);

    if (signed)
    {
      if (bd.compareTo(zero) < 0)
      {
        switch (c)
        {
          case '0':
            r = '}';
            break;
          case '1':
            r = 'J';
            break;
          case '2':
            r = 'K';
            break;
          case '3':
            r = 'L';
            break;
          case '4':
            r = 'M';
            break;
          case '5':
            r = 'N';
            break;
          case '6':
            r = 'O';
            break;
          case '7':
            r = 'P';
            break;
          case '8':
            r = 'Q';
            break;
          case '9':
            r = 'R';
            break;
        }
      }
      else
      {
        switch (c)
        {
          case '0':
            r = '{';
            break;
          case '1':
            r = 'A';
            break;
          case '2':
            r = 'B';
            break;
          case '3':
            r = 'C';
            break;
          case '4':
            r = 'D';
            break;
          case '5':
            r = 'E';
            break;
          case '6':
            r = 'F';
            break;
          case '7':
            r = 'G';
            break;
          case '8':
            r = 'H';
            break;
          case '9':
            r = 'I';
            break;
        }
      }
      sbZonedDecimal.setCharAt(len - 1, r);
    }

    i = sbZonedDecimal.indexOf("-");
    if (i >= 0)
    {
      sbZonedDecimal.deleteCharAt(i);
      sign = "-";
    }

    else
    {
      i = sbZonedDecimal.indexOf("+");
      if (i >= 0)
      {
        sbZonedDecimal.deleteCharAt(i);
      }
    }
    len = sbZonedDecimal.length();
    while (len < length)
    {
      sbZonedDecimal.insert(0, '0');
      len++;
    }

    idc.insertAfter("zonedDecimal", sbZonedDecimal.toString());
    if (!signed)
    {
      idc.insertAfter("sign", sign);
    }
    idc.destroy();
    // --- <<IS-END>> ---

  }
}
