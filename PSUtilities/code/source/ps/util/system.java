package ps.util;

//-----( IS Java Code Template v1.2
//-----( CREATED: 2008-01-08 21:00:00 EDT
//-----( ON-HOST: MCRSINGH.AME.ad.sag

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.b2b.server.*;
import java.io.*;
import com.wm.lang.ns.NSService;
import com.wm.ps.util.system.StreamCopier;
import com.wm.ps.util.system.UUID;
import java.util.Stack;

// --- <<IS-END-IMPORTS>> ---

public final class system

{
  // ---( internal utility methods )---

  final static system _instance = new system();

  static system _newInstance()
  {
    return new system();
  }

  static system _cast(Object o)
  {
    return (system) o;
  }

  // ---( server methods )---

  public static final void executeOSCommand(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(executeOSCommand)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] field:0:required command
    // [i] field:1:optional arguments
    // [i] field:1:optional environment
    // [i] field:0:optional workingDirectory
    // [i] object:0:required inputRedirect
    // [i] object:0:optional outputRedirect
    // [i] object:0:optional errorRedirect
    // [o] field:0:required exitValue
    // [o] field:0:optional outputMessage
    // [o] field:0:optional errorMessage
    IDataCursor cursor = pipeline.getCursor();

    String command = null;

    Reader inputReader = null;
    Writer outputWriter = null;
    Writer errorWriter = null;

    if (cursor.first("command"))
    {
      command = (String) cursor.getValue();
    }

    if (command == null || command.length() == 0)
    {
      throw new ServiceException("Missing input 'command'");
    }

    inputReader = getReader(cursor, "inputRedirect");

    outputWriter = getWriter(cursor, "outputRedirect");
    errorWriter = getWriter(cursor, "errorRedirect");

    String[] args = cursor.first("arguments") ? (String[]) cursor.getValue() : null;
    String[] cmdArgs = null;

    if (args != null && args.length > 0)
    {
      cmdArgs = new String[args.length + 1];
      cmdArgs[0] = command;
      System.arraycopy(args, 0, cmdArgs, 1, args.length);
    }

    String[] env = cursor.first("environment") ? (String[]) cursor.getValue() : null;
    File folder = cursor.first("workingDirectory") ? new File((String) cursor.getValue()) : null;

    int exitValue = 999;
    StringBuffer output = new StringBuffer();
    StringBuffer error = new StringBuffer();
    try
    {
      Process process;
      if (args == null || args.length == 0)
      {
        process = Runtime.getRuntime().exec(command, env, folder);
      }
      else
      {
        process = Runtime.getRuntime().exec(cmdArgs, env, folder);
      }

      StreamCopier o = new StreamCopier(new BufferedReader(new InputStreamReader(process.getInputStream())), outputWriter);
      StreamCopier e = new StreamCopier(new BufferedReader(new InputStreamReader(process.getErrorStream())), errorWriter);
      StreamCopier i = new StreamCopier(inputReader, new BufferedWriter(new OutputStreamWriter(process.getOutputStream())));
      o.start();
      e.start();
      i.start();

      exitValue = process.waitFor();

    }
    catch (Exception e)
    {
      throw new ServiceException(e);
    }
    finally
    {
      IDataUtil.put(cursor, "exitValue", String.valueOf(exitValue));
      if (outputWriter instanceof StringWriter)
      {
        IDataUtil.put(cursor, "outputMessage", ((StringWriter) outputWriter).toString());
      }
      if (errorWriter instanceof StringWriter)
      {
        IDataUtil.put(cursor, "errorMessage", ((StringWriter) errorWriter).toString());
      }
      cursor.destroy();
    }
    // --- <<IS-END>> ---

  }

  public static final void generateUUID(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(generateUUID)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [o] field:0:required uuid
    IDataCursor cursor = pipeline.getCursor();
    IDataUtil.put(cursor, "uuid", UUID.generate());
    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void getCallingServiceName(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(getCallingServiceName)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [o] field:0:required callingServiceName
    IDataCursor cursor = pipeline.getCursor();
    NSService currentSvc = Service.getServiceEntry();
    Stack callStack = InvokeState.getCurrentState().getCallStack();
    int index = callStack.indexOf(currentSvc);
    if (index > 1)
    {
      IDataUtil.put(cursor, "callingServiceName", ((NSService) callStack.elementAt(index - 2)).toString());
    }
    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void getExceptionType(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(getExceptionType)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] field:0:required errorType
    // [o] field:0:required exceptionForRetry
    IDataCursor cursor = pipeline.getCursor();

    String errorType = IDataUtil.getString(cursor, "errorType");
    String exceptionForRetry = "false";

    if (errorType.equals("com.wm.pkg.art.error.DetailedSystemException") || errorType.equals("com.wm.app.b2b.server.ISRuntimeException"))
    {
      exceptionForRetry = "true";
    }
    else
    {
      Class clazz = null;
      try
      {
        clazz = Class.forName(errorType);
        Object obj = clazz.newInstance();
        if (obj instanceof ISRuntimeException)
        {
          exceptionForRetry = "true";
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    IDataUtil.put(cursor, "exceptionForRetry", exceptionForRetry);
    cursor.destroy();
    // --- <<IS-END>> ---
  }

  public static final void getProperty(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(getProperty)>> ---
    // @sigtype java 3.5
    // [i] field:0:required property
    // [o] field:0:required propertyValue
    IDataCursor cursor = pipeline.getCursor();

    if (cursor.first("property"))
    {
      String property = (String) cursor.getValue();
      String propertyValue = System.getProperty(property);
      if (propertyValue != null && propertyValue.length() > 0)
      {
        IDataUtil.put(cursor, "propertyValue", propertyValue);
      }
    }

    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void getServiceName(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(getServiceName)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [o] field:0:required serviceName
    IDataCursor cursor = pipeline.getCursor();
    NSService nsService = Service.getCallingService();
    if (nsService != null)
    {
      IDataUtil.put(cursor, "serviceName", nsService.toString());
    }
    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void getTimeMillis(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(getTimeMillis)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] field:0:optional startTime
    // [o] field:0:required timeMillis
    long timeMillis = System.currentTimeMillis();

    IDataCursor cursor = pipeline.getCursor();
    if (cursor.first("startTime"))
    {
      long startTimeMillis = Long.parseLong((String) cursor.getValue());
      timeMillis -= startTimeMillis;
    }

    IDataUtil.put(cursor, "timeMillis", String.valueOf(timeMillis));
    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void setProperty(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(setProperty)>> ---
    // @sigtype java 3.5
    // [i] field:0:required property
    // [i] field:0:required value
    IDataCursor cursor = pipeline.getCursor();

    String property;

    if (cursor.first("property"))
    {
      property = (String) cursor.getValue();
      if (cursor.first("value"))
      {
        System.setProperty(property, (String) cursor.getValue());
      }
    }

    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void sleep(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(sleep)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] field:0:required sleepTime
    IDataCursor cursor = pipeline.getCursor();

    int numSecs = 0;
    if (cursor.first("sleepTime"))
    {
      numSecs = Integer.parseInt((String) cursor.getValue());
    }

    try
    {
      Thread.currentThread().sleep(numSecs * 1000);
    }
    catch (InterruptedException ie)
    {
      throw new ServiceException(ie);
    }
    finally
    {
      cursor.destroy();
    }
    // --- <<IS-END>> ---

  }

  public static final void spawnService(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(spawnService)>> ---
    // @sigtype java 3.5
    // [i] field:0:required folder
    // [i] field:0:required service
    // [i] record:0:required inputs
    // [o] object:0:required serviceThread
    IDataCursor cursor = pipeline.getCursor();

    String service = null;
    String folder = null;
    IData inputs = null;

    if (cursor.first("service"))
    {
      service = (String) cursor.getValue();
    }
    else
    {
      throw new ServiceException("Missing input 'service'");
    }

    if (cursor.first("folder"))
    {
      folder = (String) cursor.getValue();
    }
    else
    {
      throw new ServiceException("Missing input 'interface'");
    }

    if (cursor.first("inputs"))
    {
      inputs = (IData) cursor.getValue();
    }

    try
    {
      ServiceThread serviceThread = Service.doThreadInvoke(folder, service, inputs);

      if (cursor.first("serviceThread"))
      {
        cursor.setValue(serviceThread);
      }
      else
      {
        cursor.insertAfter("serviceThread", serviceThread);
      }
    }
    catch (Exception e)
    {
      throw new ServiceException(e);
    }
    finally
    {
      cursor.destroy();
    }
    // --- <<IS-END>> ---

  }

  public static final void throwError(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(throwError)>> ---
    // @sigtype java 3.5
    // [i] field:0:required errorMessage

    IDataCursor idcPipeline = pipeline.getCursor();

    String strErrorMessage = null;
    if (idcPipeline.first("errorMessage"))
    {
      strErrorMessage = (String) idcPipeline.getValue();
    }

    idcPipeline.destroy();

    throw new ServiceException(strErrorMessage);
    // --- <<IS-END>> ---

  }

  // --- <<IS-START-SHARED>> ---
  static Reader getReader(IDataCursor cursor, String key)
  {
    Reader reader = null;
    if (cursor.first(key))
    {
      Object o = cursor.getValue();
      if (o == null)
      {
        reader = new StringReader("");
      }
      else if (o instanceof BufferedReader)
      {
        reader = (BufferedReader) o;
      }
      else if (o instanceof Reader)
      {
        reader = new BufferedReader((Reader) o);
      }
      else if (o instanceof InputStream)
      {
        reader = new BufferedReader(new InputStreamReader((InputStream) o));
      }
      else if (o instanceof String)
      {
        reader = new BufferedReader(new StringReader((String) o));
      }
      else if (o instanceof byte[])
      {
        reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream((byte[]) o)));
      }
    }
    return reader;

  }

  static Writer getWriter(IDataCursor cursor, String key)
  {
    Writer writer = new StringWriter();
    if (cursor.first(key))
    {
      Object o = cursor.getValue();
      if (o == null)
      {
        return writer;
      }
      else if (o instanceof BufferedWriter)
      {
        writer = (BufferedWriter) o;
      }
      else if (o instanceof Writer)
      {
        writer = new BufferedWriter((Writer) o);
      }
      else if (o instanceof OutputStream)
      {
        writer = new BufferedWriter(new OutputStreamWriter((OutputStream) o));
      }
    }
    return writer;

  }
  // --- <<IS-END-SHARED>> ---
}
