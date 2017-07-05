package ps.util;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2008-01-08 21:00:00 EDT
// -----( ON-HOST: MCRSINGH.AME.ad.sag
// -----( Author : Rupinder Singh

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.util.Enumeration;
import java.util.HashMap;
import com.wm.ps.util.cache.LookupCacheKey;
import com.wm.ps.util.cache.LookupCacheTable;
import com.wm.app.b2b.server.ISRuntimeException;
// --- <<IS-END-IMPORTS>> ---

public final class cache

{
  // ---( internal utility methods )---

  final static cache _instance = new cache();

  static cache _newInstance()
  {
    return new cache();
  }

  static cache _cast(Object o)
  {
    return (cache) o;
  }

  // ---( server methods )---

  public static final void clear(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(clear)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] field:0:optional lookupName
    IDataCursor cursor = pipeline.getCursor();
    String lookupName = IDataUtil.getString(cursor, "lookupName");
    if (lookupName == null || lookupName.length() == 0)
    {
      lookups.clear();
    }
    else
    {
      lookups.remove(lookupName);
    }

    cursor.destroy();
    // --- <<IS-END>> ---
  }

  public static final void cacheLookup(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(cacheLookup)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] field:0:required lookupName
    // [i] record:1:optional lookupData
    // [i] field:1:optional lookupKeyNames
    // [i] field:0:optional dataRetrievalService
    // [i] field:0:optional allowDuplicates {"false", "true"}
    // [i] field:0:optional maxAge
    IDataCursor cursor = pipeline.getCursor();
    String lookupName = IDataUtil.getString(cursor, "lookupName");
    if (lookupName == null || lookupName.length() == 0)
    {
      cursor.destroy();
      throw new ServiceException("Missing input 'lookupName'");
    }

    String[] lookupKeyNames = IDataUtil.getStringArray(cursor, "lookupKeyNames");
    String dataRetrievalService = IDataUtil.getString(cursor, "dataRetrievalService");
    IData[] lookupData = IDataUtil.getIDataArray(cursor, "lookupData");
    String maxAgeStr = IDataUtil.getString(cursor, "maxAge");
    boolean allowDuplicates = IDataUtil.getBoolean(cursor, "allowDuplicates", false);
    long maxAge = LookupCacheTable.defaultMaxAge;
    if (maxAgeStr != null)
    {
      maxAge = Long.parseLong(maxAgeStr);
    }

    LookupCacheTable lookupTable = new LookupCacheTable(lookupKeyNames, dataRetrievalService);

    if ((lookupData == null || lookupData.length == 0) && dataRetrievalService != null && dataRetrievalService.length() > 0)
    {
      readTable(lookupName, lookupTable, allowDuplicates);
      if (maxAge > 0)
      {
        lookupTable.setSetToExpire(true);
        lookupTable.setMaxAge(maxAge);
      }
    }
    else
    {
      addDataToTable(lookupData, lookupTable, allowDuplicates);
      lookupTable.setSetToExpire(false);
    }

    lookups.put(lookupName, lookupTable);

    cursor.destroy();
    // --- <<IS-END>> ---
  }

  public static final void getTable(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(getTable)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] field:0:required lookupName
    // [i] field:1:optional lookupKeyNames
    // [i] field:0:optional dataRetrievalService
    // [i] field:0:optional allowDuplicates {"false", "true"}
    // [i] field:0:optional maxAge
    // [o] record:1:required lookupData
    IDataCursor cursor = pipeline.getCursor();
    String lookupName = IDataUtil.getString(cursor, "lookupName");

    String[] lookupKeyNames = IDataUtil.getStringArray(cursor, "lookupKeyNames");
    String dataRetrievalService = IDataUtil.getString(cursor, "dataRetrievalService");
    boolean allowDuplicates = IDataUtil.getBoolean(cursor, "allowDuplicates", false);
    String maxAgeStr = IDataUtil.getString(cursor, "maxAge");
    long maxAge = LookupCacheTable.defaultMaxAge;
    if (maxAgeStr != null)
    {
      maxAge = Long.parseLong(maxAgeStr);
    }

    if (lookupName == null || lookupName.length() == 0)
    {
      cursor.destroy();
      throw new ServiceException("Missing input 'lookupName'");
    }

    LookupCacheTable lookupTable = (LookupCacheTable) lookups.get(lookupName);

    if ((lookupTable == null || lookupTable.size() == 0) && dataRetrievalService != null && dataRetrievalService.length() > 0)
    {
      lookupTable = new LookupCacheTable(lookupKeyNames, dataRetrievalService);
      if (dataRetrievalService != null && dataRetrievalService.length() > 0)
      {
        readTable(lookupName, lookupTable, allowDuplicates);
        if (maxAge > 0)
        {
          lookupTable.setSetToExpire(true);
          lookupTable.setMaxAge(maxAge);
        }
      }
      lookups.put(lookupName, lookupTable);
    }

    if (lookupTable != null)
    {
      if (lookupTable.isExpired() || lookupTable.size() == 0)
      {
        readTable(lookupName, lookupTable, allowDuplicates);
      }

      IDataUtil.put(cursor, "lookupData", getLookupData(lookupTable));
    }
    else
    {
      throw new ServiceException("Lookup '" + lookupName + "' does not exist in lookup cache.");
    }
    cursor.destroy();
    // --- <<IS-END>> ---
  }

  public static final void listTables(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(listTables)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [o] field:1:required tables
    IDataCursor cursor = pipeline.getCursor();
    String[] tables = new String[lookups.size()];
    lookups.keySet().toArray(tables);
    IDataUtil.put(cursor, "tables", tables);
    cursor.destroy();
    // --- <<IS-END>> ---
  }

  public static final void lookupExists(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(lookupExists)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] field:0:required lookupName
    // [o] field:0:required exists
    IDataCursor cursor = pipeline.getCursor();
    String lookupName = IDataUtil.getString(cursor, "lookupName");
    IDataUtil.put(cursor, "exists", String.valueOf(lookups.containsKey(lookupName)));
    cursor.destroy();
    // --- <<IS-END>> ---

  }

  public static final void lookupValue(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(lookupValue)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [i] field:0:required lookupName
    // [i] field:1:optional lookupKeyNames
    // [i] field:1:required lookupKeyValues
    // [i] field:0:optional lookupValueKeyName
    // [i] field:0:optional returnAllValues {"false","true"}
    // [i] field:0:optional dataRetrievalService
    // [i] field:0:optional allowDuplicates {"false", "true"}
    // [i] field:0:optional maxAge
    // [o] object:0:required lookupValue
    IDataCursor cursor = pipeline.getCursor();
    String lookupName = IDataUtil.getString(cursor, "lookupName");
    String[] lookupKeyValues = IDataUtil.getStringArray(cursor, "lookupKeyValues");
    String lookupValueKeyName = IDataUtil.getString(cursor, "lookupValueKeyName");
    boolean returnAllValues = IDataUtil.getBoolean(cursor, "returnAllValues", false);

    String[] lookupKeyNames = IDataUtil.getStringArray(cursor, "lookupKeyNames");
    String dataRetrievalService = IDataUtil.getString(cursor, "dataRetrievalService");
    boolean allowDuplicates = IDataUtil.getBoolean(cursor, "allowDuplicates", false);
    String maxAgeStr = IDataUtil.getString(cursor, "maxAge");
    long maxAge = LookupCacheTable.defaultMaxAge;
    if (maxAgeStr != null)
    {
      maxAge = Long.parseLong(maxAgeStr);
    }

    if (lookupName == null || lookupName.length() == 0)
    {
      cursor.destroy();
      throw new ServiceException("Missing input 'lookupName'");
    }

    if (lookupKeyValues == null || lookupKeyValues.length == 0)
    {
      cursor.destroy();
      return;
    }

    LookupCacheTable lookupTable = (LookupCacheTable) lookups.get(lookupName);

    if (lookupTable == null && dataRetrievalService != null && dataRetrievalService.length() > 0)
    {
      lookupTable = new LookupCacheTable(lookupKeyNames, dataRetrievalService);
      if (dataRetrievalService != null && dataRetrievalService.length() > 0)
      {
        readTable(lookupName, lookupTable, allowDuplicates);
        if (maxAge > 0)
        {
          lookupTable.setSetToExpire(true);
          lookupTable.setMaxAge(maxAge);
        }
      }
      lookups.put(lookupName, lookupTable);
    }

    if (lookupTable != null)
    {
      if (lookupTable.isExpired())
      {
        readTable(lookupName, lookupTable, allowDuplicates);
      }

      IData lookupData = (IData) lookupTable.get(new LookupCacheKey(lookupKeyValues));
      if (lookupData != null)
      {
        if (returnAllValues == true)
        {
          IDataUtil.put(cursor, "lookupValue", lookupData);
        }
        else
        {
          String lookupValue = getValueFromTable(lookupValueKeyName, lookupData);
          if (lookupValue != null)
          {
            IDataUtil.put(cursor, "lookupValue", lookupValue);
          }
        }
      }
    }
    else
    {
      throw new ServiceException("Lookup '" + lookupName + "' does not exist in lookup cache.");
    }
    cursor.destroy();
    // --- <<IS-END>> ---

  }

  // --- <<IS-START-SHARED>> ---
  static HashMap lookups = new HashMap();

  public synchronized static void readTable(String lookupName, LookupCacheTable lookupTable, boolean allowDuplicates) throws ServiceException
  {
    String dataRetrievalService = lookupTable.getDataRetrievalService();
    if (dataRetrievalService == null || dataRetrievalService.length() == 0)
    {
      return;
    }
    int index = dataRetrievalService.indexOf(":");
    String folder = dataRetrievalService.substring(0, index);
    String service = dataRetrievalService.substring(index + 1);

    IData input = IDataFactory.create();
    IDataCursor cursor = input.getCursor();
    cursor.insertAfter("lookupName", lookupName);
    cursor.destroy();
    IDataCursor outCursor = null;
    IData[] lookupData = null;
    try
    {
      IData out = Service.doInvoke(folder, service, input);

      outCursor = out.getCursor();
      lookupData = IDataUtil.getIDataArray(outCursor, "lookupData");
      lookupTable.refreshAge();
      addDataToTable(lookupData, lookupTable, allowDuplicates);
    }
    catch (ISRuntimeException isre)
    {
      throw new ISRuntimeException(isre);
    }
    catch (Exception e)
    {
      throw new ServiceException("Lookup table '" + lookupName + "' could not be loaded :" + e.getMessage());
    }
    finally
    {
      if (outCursor != null)
      {
        outCursor.destroy();
      }
    }
  }

  private static String[] getKeyValues(IData row, String[] keyNames)
  {
    IDataCursor rowCursor = row.getCursor();

    if (keyNames == null || keyNames.length == 0)
    {
      String value = IDataUtil.getString(rowCursor);
      rowCursor.destroy();
      return new String[] { value };
    }

    String[] keyValues = new String[keyNames.length];
    for (int i = 0; i < keyValues.length; i++)
    {
      keyValues[i] = IDataUtil.getString(rowCursor, keyNames[i]);
    }
    rowCursor.destroy();
    return keyValues;
  }

  private synchronized static void addDataToTable(IData[] lookupData, LookupCacheTable lookupTable, boolean allowDuplicates) throws ServiceException
  {
    if (lookupData == null)
    {
      return;
    }
    
    lookupTable.clear();

    for (int i = 0; i < lookupData.length; i++)
    {
      String[] lookupKeyNames = lookupTable.getLookupKeys();
      String[] keyValues = getKeyValues(lookupData[i], lookupKeyNames);
      LookupCacheKey key = new LookupCacheKey(keyValues);
      if (!allowDuplicates && lookupTable.containsKey(key))
      {
        throw new ServiceException("Key field value(s) are not unique. Cannot be used for lookups");
      }
      lookupTable.put(key, lookupData[i]);
    }
  }

  private static IData[] getLookupData(LookupCacheTable lookupTable)
  {
    IData[] lookupData = new IData[lookupTable.size()];
    Enumeration enumeration = lookupTable.elements();
    for (int i = 0; enumeration.hasMoreElements(); i++)
    {
      lookupData[i] = (IData) enumeration.nextElement();
    }
    return lookupData;
  }

  private static String getValueFromTable(String lookupValueKeyName, IData lookupData)
  {
    String value = null;

    if (lookupData != null)
    {
      IDataCursor lookupDataCursor = lookupData.getCursor();

      if (lookupValueKeyName != null && lookupValueKeyName.length() > 0)
      {
        value = IDataUtil.getString(lookupDataCursor, lookupValueKeyName);
      }
      else
      {
        lookupDataCursor.first();
        lookupDataCursor.next();
        value = IDataUtil.getString(lookupDataCursor); // Pick the second field
        // if key not specified
      }
      lookupDataCursor.destroy();

    }
    return value;
  }
  // --- <<IS-END-SHARED>> ---
}