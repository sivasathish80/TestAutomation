package ps.util;

//-----( IS Java Code Template v1.2
//-----( CREATED: 2008-01-08 21:00:00 EDT
//-----( ON-HOST: MCRSINGH.AME.ad.sag
//-----( Author : Rupinder Singh


import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

// --- <<IS-END-IMPORTS>> ---

public final class config

{
  // ---( internal utility methods )---

  final static config _instance = new config();

  static config _newInstance()
  {
    return new config();
  }

  static config _cast(Object o)
  {
    return (config) o;
  }

  // ---( server methods )---

  public static final void clear(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(clear)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] field:0:optional packageName
    // [i] field:0:optional configFilename
    IDataCursor cursor = pipeline.getCursor();
    String packageName = IDataUtil.getString(cursor, "packageName");
    String configFilename = IDataUtil.getString(cursor, "configFilename");
    if (packageName == null)
    {
      configs.clear();
    }
    else if (configFilename == null)
    {
      Set keySet = configs.keySet();
      ArrayList keysToRemove = new ArrayList();
      for (Iterator keyIterator = keySet.iterator(); keyIterator.hasNext();)
      {
        String key = (String) keyIterator.next();
        if (key.startsWith(packageName + "."))
        {
          keysToRemove.add(key);
        }
      }
      for (int i = 0; i < keysToRemove.size(); i++)
      {
        configs.remove((String) keysToRemove.get(i));
      }
    }
    else
    {
      configs.remove(packageName + "." + configFilename);
    }
    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void getConfig(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(getConfig)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] field:0:required packageName
    // [i] field:0:required configFilename
    // [i] field:0:optional documentTypeName
    // [o] record:0:required config
    IDataCursor cursor = pipeline.getCursor();
    String packageName = IDataUtil.getString(cursor, "packageName");
    String configFilename = IDataUtil.getString(cursor, "configFilename");
    String documentTypeName = IDataUtil.getString(cursor, "documentTypeName");

    IData config = getConfiguration(packageName, configFilename);
    if (config == null)
    {

      config = loadConfiguration(packageName, configFilename, documentTypeName);
    }
    IDataUtil.put(cursor, "config", config);
    cursor.destroy();

    // --- <<IS-END>> ---

  }

  public static final void loadConfig(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(loadConfig)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] field:0:required packageName
    // [i] field:0:required configFilename
    // [i] field:0:optional documentTypeName
    // [o] record:0:required config
    IDataCursor cursor = pipeline.getCursor();
    String packageName = IDataUtil.getString(cursor, "packageName");
    String configFilename = IDataUtil.getString(cursor, "configFilename");
    String documentTypeName = IDataUtil.getString(cursor, "documentTypeName");
    IDataUtil.put(cursor, "config", loadConfiguration(packageName, configFilename, documentTypeName));
    cursor.destroy();
    // --- <<IS-END>> ---

  }

  // --- <<IS-START-SHARED>> ---
  static private Hashtable configs = new Hashtable();

  static synchronized void setConfiguration(String packageName, String filename, IData config)
  {
    configs.put(packageName + "." + filename, config);
  }

  static synchronized IData getConfiguration(String packageName, String filename)
  {
    return (IData) configs.get(packageName + "." + filename);
  }

  static IData loadConfiguration(String packageName, String configFilename, String documentTypeName) throws ServiceException
  {
    IData output = null;
    try
    {
      output = Service.doInvoke("ps.util.config", "readConfigFile", IDataFactory.create(new Object[][] { { "packageName", packageName }, { "configFilename", configFilename },
          { "documentTypeName", documentTypeName } }));
      IDataCursor outCursor = output.getCursor();

      if (outCursor.first("config"))
      {
        IData configDoc = (IData) outCursor.getValue();
        IDataCursor configDocCursor = configDoc.getCursor();

        while (configDocCursor.next())
        {
          Object object = configDocCursor.getValue();
          if (object instanceof IData)
          {
            IData config = (IData) object;
            setConfiguration(packageName, configFilename, config);
            return config;
          }
        }
        configDocCursor.destroy();
      }
    }
    catch (Exception e)
    {
      throw new ServiceException(e);
    }
    return null;
  }
  // --- <<IS-END-SHARED>> ---
}
