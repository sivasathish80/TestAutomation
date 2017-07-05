package com.wm.ps.util.cache;

public class LookupCacheKey
{
  private String[] keyValues;

  public LookupCacheKey(String[] keys)
  {
    setKeyValues(keys);
  }

  public LookupCacheKey(String key)
  {
    setKeys(key);
  }

  public void setKeys(String key)
  {
    this.keyValues = new String[] { key };
  }

  public void setKeyValues(String[] keys)
  {
    this.keyValues = keys;
  }

  public String[] getKeyValues()
  {
    return keyValues;
  }

  public boolean equals(Object obj)
  {
    if (!(obj instanceof LookupCacheKey))
    {
      return false;
    }

    if (obj == this)
    {
      return true;
    }

    String[] anotherObjectKeys = ((LookupCacheKey) obj).getKeyValues();
    String[] theseKeys = getKeyValues();

    int thisLength = (theseKeys == null ? 0 : theseKeys.length);
    int anotherLength = (anotherObjectKeys == null ? 0 : anotherObjectKeys.length);

    if (thisLength != anotherLength)
    {
      return false;
    }

    if (thisLength == 0)
    {
      return true;
    }

    for (int i = 0; i < keyValues.length; i++)
    {
      if (!areKeyValuesEqual(theseKeys[i], anotherObjectKeys[i]))
      {
        return false;
      }
    }
    return true;
  }

  private boolean areKeyValuesEqual(String keyValue, String otherKeyValue)
  {
    if ((isNull(keyValue) && isNull(otherKeyValue)) || 
        (keyValue != null && keyValue.equals(otherKeyValue)))
    {
      return true;
    }
    return false;
  }
  
  private boolean isNull(String value)
  {
    return value == null || value.length() == 0;
  }
  
  public int hashCode()
  {
    int hashCode = 1;

    if (keyValues != null)
    {
      for (int i = 0; i < keyValues.length; i++)
      {
        hashCode = 31 * hashCode + (keyValues[i] == null ? 0 : keyValues[i].hashCode());
      }
    }
    return hashCode;
  }
}