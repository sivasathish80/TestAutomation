package com.wm.ps.util.cache;

import java.util.Hashtable;

public class LookupCacheTable extends Hashtable
{
  public static final long defaultMaxAge = 86400000L; // 24 hours
  private long timeCreated;
  private long maxAge = defaultMaxAge;
  private String dataRetrievalService;
  private String[] lookupKeys;
  private boolean setToExpire = true;

  public LookupCacheTable(boolean setToExpire)
  {
    setSetToExpire(setToExpire);
    refreshAge();
  }

  public LookupCacheTable(String[] lookupKeys, String dataRetrievalService)
  {
    this(dataRetrievalService != null);
    setLookupKeys(lookupKeys);
    setDataRetrievalService(dataRetrievalService);
  }

  public LookupCacheTable(String lookupKeys, String dataRetrievalService)
  {
    this(dataRetrievalService != null);
    setLookupKeys(lookupKeys);
    setDataRetrievalService(dataRetrievalService);
  }

  public String[] getLookupKeys()
  {
    return lookupKeys;
  }

  public void setLookupKeys(String[] lookupKeys)
  {
    this.lookupKeys = lookupKeys;
  }

  public void setLookupKeys(String lookupKeys)
  {
    setLookupKeys(new String[] { lookupKeys });
  }

  public void setDataRetrievalService(String dataRetrievalService)
  {
    this.dataRetrievalService = dataRetrievalService;
  }

  public String getDataRetrievalService()
  {
    return dataRetrievalService;
  }

  public long getMaxAge()
  {
    return maxAge;
  }

  public void setMaxAge(long maxAge)
  {
    this.maxAge = maxAge;
  }

  public boolean isExpired()
  {
    return isSetToExpire() && getAge() > maxAge;
  }

  public long getAge()
  {
    return System.currentTimeMillis() - timeCreated;
  }

  public boolean isSetToExpire()
  {
    return setToExpire;
  }

  public void setSetToExpire(boolean setToExpire)
  {
    this.setToExpire = setToExpire;
  }

  public void refreshAge()
  {
    timeCreated = System.currentTimeMillis();
  }
}