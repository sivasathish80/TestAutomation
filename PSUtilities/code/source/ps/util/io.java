package ps.util;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2008-02-05 12:46:32 EST
// -----( ON-HOST: MCRSINGH.AME.ad.sag

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
// --- <<IS-END-IMPORTS>> ---

public final class io

{
  // ---( internal utility methods )---

  final static io _instance = new io();

  static io _newInstance()
  {
    return new io();
  }

  static io _cast(Object o)
  {
    return (io) o;
  }

  // ---( server methods )---

  public static final void close(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(close)>> ---
    // @sigtype java 3.5
    // [i] object:0:required stream
    IDataCursor cursor = pipeline.getCursor();

    Object stream;

    try
    {
      if (cursor.first("stream"))
      {
        // If the input object is of type java.io.InputStream
        stream = cursor.getValue();
        if (stream instanceof InputStream)
        {
          ((InputStream) stream).close();
        }
        // If the input object is of type java.io.OutputStream
        else if (stream instanceof OutputStream)
        {
          OutputStream os = (OutputStream) stream;
          os.flush();
          os.close();
        }
        // If the input object is of type java.io.Writer
        else if (stream instanceof Writer)
        {
          Writer writer = (Writer) stream;
          writer.flush();
          writer.close();
        }
        // If the input object is of type java.io.Reader
        else if (stream instanceof Reader)
        {
          ((Reader) stream).close();
        }
        else
        {
          throw new ServiceException("Incorrect object type 'stream'");
        }
      }
    }
    catch (IOException ioe)
    {
      throw new ServiceException(ioe);
    }
    finally
    {
      cursor.destroy();
    }
    // --- <<IS-END>> ---

  }

  public static final void createTempFile(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(createTempFile)>> ---
    // @sigtype java 3.5
    // [i] field:0:optional prefix
    // [i] field:0:optional suffix
    // [i] field:0:optional folder
    // [i] field:0:optional asStream {"true","false"}
    // [o] field:0:required tempFilename
    // [o] object:0:required tempWriter
    // [o] object:0:required tempStream
    IDataCursor cursor = pipeline.getCursor();

    String prefix = null;
    String suffix = null;
    File folder = null;
    boolean asStream = false;

    if (cursor.first("prefix"))
    {
      prefix = (String) cursor.getValue();
    }

    if (prefix == null || prefix.length() < 3)
    {
      throw new ServiceException("The field 'prefix' should be atleast 3 characters long");
    }

    if (cursor.first("suffix"))
    {
      suffix = (String) cursor.getValue();
    }

    if (cursor.first("folder"))
    {
      folder = new File((String) cursor.getValue());
    }

    if (cursor.first("asStream"))
    {
      asStream = Boolean.valueOf((String) cursor.getValue()).booleanValue();
    }
    try
    {
      // Create the file from the input parameters
      File tempFile = File.createTempFile(prefix, suffix, folder);
      // Save the full file path to output parameters.
      IDataUtil.put(cursor, "tempFilename", tempFile.getCanonicalPath());
      if (!asStream)
      {
        IDataUtil.put(cursor, "tempWriter", new BufferedWriter(new FileWriter(tempFile)));
      }
      else
      {
        IDataUtil.put(cursor, "tempStream", new BufferedOutputStream(new FileOutputStream(tempFile)));
      }
    }
    catch (IOException ioe)
    {
      throw new ServiceException(ioe);
    }
    finally
    {
      cursor.destroy();
    }
    // --- <<IS-END>> ---

  }

  public static final void readLine(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(readLine)>> ---
    // @sigtype java 3.5
    // [i] object:0:required reader
    // [o] field:0:required line
    IDataCursor cursor = pipeline.getCursor();

    if (cursor.first("reader"))
    {
      // Get the reader from the pipeline
      BufferedReader reader = (BufferedReader) cursor.getValue();
      String line = null;
      try
      {
        // Read a line of data from the reader.
        line = reader.readLine();
      }
      catch (IOException ioe)
      {
        throw new ServiceException(ioe);
      }

      IDataUtil.put(cursor, "line", line);
    }

    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void deleteFile(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(deleteFile)>> ---
    // @sigtype java 3.5
    // [i] field:0:required filename
    // [o] field:0:required deleted {"true","false"}
    IDataCursor cursor = pipeline.getCursor();

    if (cursor.first("filename"))
    {
      File file = new File((String) cursor.getValue());
      IDataUtil.put(cursor, "deleted", String.valueOf(file.delete()));
    }
    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void getReader(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(getReader)>> ---
    // @sigtype java 3.5
    // [i] object:0:required inputStream
    // [i] field:0:optional charsetName
    // [o] object:0:required reader
    IDataCursor cursor = pipeline.getCursor();

    try
    {
      if (cursor.first("inputStream"))
      {
        InputStream inputStream = (InputStream) cursor.getValue();
        String charsetName = IDataUtil.getString(cursor, "charsetName");
        // Create a BufferedReader from the input stream
        BufferedReader reader = null;
        if (charsetName != null)
        {
          reader = new BufferedReader(new InputStreamReader(inputStream, charsetName));
        }
        else
        {
          reader = new BufferedReader(new InputStreamReader(inputStream));
        }
        IDataUtil.put(cursor, "reader", reader);
      }
    }
    catch (UnsupportedEncodingException use)
    {
      throw new ServiceException(use);
    }
    finally
    {
      cursor.destroy();
    }
    // --- <<IS-END>> ---
  }

  public static final void openFile(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(openFile)>> ---
    // @sigtype java 3.5
    // [i] field:0:required filename
    // [i] field:0:optional openFor {"input","output"}
    // [i] field:0:optional asStream {"false","true"}
    // [i] field:0:optional append {"true","false"}
    // [i] field:0:optional createPath {"true","false"}
    // [o] object:0:optional stream
    // [o] object:0:optional reader
    // [o] object:0:optional writer
    IDataCursor cursor = pipeline.getCursor();

    String openFor = "input";
    boolean asStream = false;
    boolean append = false;
    boolean createPath = false;
    String filename = null;

    if (cursor.first("filename"))
    {
      filename = (String) cursor.getValue();
      if (cursor.first("openFor"))
      {
        openFor = (String) cursor.getValue();
      }

      if (cursor.first("asStream"))
      {
        asStream = Boolean.valueOf((String) cursor.getValue()).booleanValue();
      }

      if (cursor.first("append"))
      {
        append = Boolean.valueOf((String) cursor.getValue()).booleanValue();
      }

      if (cursor.first("createPath"))
      {
        createPath = Boolean.valueOf((String) cursor.getValue()).booleanValue();
      }

      try
      {
        if ("output".equalsIgnoreCase(openFor) && createPath)
        {
          new File(filename).getParentFile().mkdirs();
        }

        if (asStream)
        {
          if ("input".equalsIgnoreCase(openFor))
          {
            IDataUtil.put(cursor, "stream", new FileInputStream(filename));
          }
          else
          {
            IDataUtil.put(cursor, "stream", new FileOutputStream(filename, append));
          }
        }
        else if ("input".equalsIgnoreCase(openFor))
        {
          IDataUtil.put(cursor, "reader", new FileReader(filename));
        }
        else
        {
          IDataUtil.put(cursor, "writer", new FileWriter(filename, append));
        }
      }
      catch (IOException ioe)
      {
        throw new ServiceException(ioe);
      }
    }
    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void write(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(write)>> ---
    // @sigtype java 3.5
    // [i] object:0:required stream
    // [i] object:0:required content
    // [i] field:0:optional newline {"true","false"}
    // [i] field:0:optional encoding
    // [i] field:0:optional charsetName
    IDataCursor cursor = pipeline.getCursor();

    boolean newline = false;
    Object stream = null;
    Object content = null;

    if (cursor.first("stream"))
    {
      stream = cursor.getValue();
    }

    if (stream == null)
    {
      throw new ServiceException("Missing input 'stream'");
    }

    // Get the content to be written
    if (cursor.first("content"))
    {
      content = cursor.getValue();
    }

    if (content == null)
    {
      throw new ServiceException("Missing input 'content'");
    }

    String encoding = IDataUtil.getString(cursor, "encoding");
    String charsetName = IDataUtil.getString(cursor, "charsetName");

    if (cursor.first("newline"))
    {
      newline = Boolean.valueOf((String) cursor.getValue()).booleanValue();
    }

    try
    {
      writeContent(stream, content, newline, encoding, charsetName);
    }
    catch (IOException ioe)
    {
      throw new ServiceException(ioe.getMessage());
    }

    cursor.destroy();
    // --- <<IS-END>> ---

  }

  // --- <<IS-START-SHARED>> ---
  private static String lineSeparator = System.getProperty("line.separator");

  // Method that creates a formatted string from an object.
  private static void writeContent(Object stream, Object obj, boolean newline, String charsetName, String encoding) throws IOException
  {
    if (stream instanceof Writer)
    {
      writeToWriter(stream, obj, newline, charsetName);
    }
    else if (stream instanceof BufferedOutputStream)
    {
      writeToStream(obj, newline, encoding, (BufferedOutputStream) stream);
    }
    else if (stream instanceof OutputStream)
    {
      writeToStream(obj, newline, encoding, new BufferedOutputStream((OutputStream) stream));
    }
  }

  private static void writeToStream(Object obj, boolean newline, String encoding, BufferedOutputStream bstream) throws IOException, UnsupportedEncodingException
  {
    if (obj instanceof String)
    {
      bstream.write(getBytes((String) obj, encoding));
    }

    // If the object is a byte array, dump it as it is
    else if (obj instanceof byte[])
    {
      bstream.write((byte[]) obj);
    }
    // If the object is a string list, put each string in its own line.
    else if (obj instanceof String[])
    {
      String[] strArray = (String[]) obj;
      for (int i = 0; i < strArray.length; i++)
      {
        bstream.write(getBytes(strArray[i], encoding));
        bstream.write(getBytes(lineSeparator, encoding));
      }
    }
    // If the object is a recordlist, put each record on its own line
    else if (obj instanceof IData[])
    {
      IData[] idataArray = (IData[]) obj;
      for (int i = 0; i < idataArray.length; i++)
      {
        bstream.write(getBytes(idataArray[i].toString(), encoding));
        bstream.write(getBytes(lineSeparator, encoding));
      }
    }
    // If the object is an inputstream, read from it and return the content
    else if (obj instanceof InputStream)
    {
      InputStream is = (InputStream) obj;
      byte[] bytes = new byte[1024];
      int numRead = 0;
      while ((numRead = is.read(bytes)) >= 0)
      {
        bstream.write(bytes, 0, numRead);
      }
      is.close();
    }
    // assume that the object is a record and use its toString method.
    else
    {
      bstream.write(getBytes(((IData) obj).toString(), encoding));
    }

    if (newline)
    {
      bstream.write(getBytes(lineSeparator, encoding));
    }

    bstream.flush();
  }

  private static void writeToWriter(Object stream, Object obj, boolean newline, String charsetName) throws IOException, UnsupportedEncodingException
  {
    Writer writer = (Writer) stream;
    if (obj instanceof String)
    {
      writer.write((String) obj);
    }
    // If the object is a byte array, create a string from it
    else if (obj instanceof byte[])
    {
      writer.write(createString((byte[]) obj, charsetName));
    }
    // If the object is a string list, put each string in its own line.
    else if (obj instanceof String[])
    {
      String[] strArray = (String[]) obj;
      for (int i = 0; i < strArray.length; i++)
      {
        writer.write(strArray[i]);
        writer.write(lineSeparator);
      }
    }
    // If the object is a recordlist, put each record on its own line
    else if (obj instanceof IData[])
    {
      IData[] idataArray = (IData[]) obj;
      for (int i = 0; i < idataArray.length; i++)
      {
        writer.write(idataArray[i].toString());
        writer.write(lineSeparator);
      }
    }
    // If the object is an inputstream, read from it and return the content
    else if (obj instanceof InputStream)
    {
      InputStream is = (InputStream) obj;
      byte[] bytes = new byte[1024];
      int numRead = 0;
      while ((numRead = is.read(bytes)) >= 0)
      {
        writer.write(createString(bytes, 0, numRead, charsetName));
      }
      is.close();
    }
    // assume that the object is a record and use its toString method.
    else
    {
      writer.write(((IData) obj).toString());
    }
    if (newline)
    {
      writer.write(lineSeparator);
    }
    writer.flush();
  }

  private static byte[] getBytes(String str, String encoding) throws UnsupportedEncodingException
  {
    if (encoding != null)
    {
      return str.getBytes(encoding);
    }
    return str.getBytes();
  }

  private static String createString(byte[] bytes, String charsetName) throws UnsupportedEncodingException
  {
    if (charsetName != null)
    {
      return new String(bytes, charsetName);
    }
    return new String(bytes);
  }

  private static String createString(byte[] bytes, int offset, int length, String charsetName) throws UnsupportedEncodingException
  {
    if (charsetName != null)
    {
      return new String(bytes, offset, length, charsetName);
    }
    return new String(bytes, offset, length);
  }
  // --- <<IS-END-SHARED>> ---
}