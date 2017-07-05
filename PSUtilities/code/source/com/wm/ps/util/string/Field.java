package com.wm.ps.util.string;

import com.wm.data.IDataCursor;

public class Field
{
  private String name;
  private int startIndex = 0;
  private int length = 0;

  public Field(IDataCursor cursor) throws BadFieldException
  {
    if (cursor.first("name"))
    {
      setName((String) cursor.getValue());
    }
    else
    {
      throw new BadFieldException("Field does not have a 'name' attribute");
    }

    try
    {
      if (cursor.first("startIndex"))
      {
        setStartIndex(Integer.parseInt((String) cursor.getValue()));
      }
      else
      {
        throw new BadFieldException("Field '" + name + "' does not have a 'startIndex' attribute");
      }
    }
    catch (NumberFormatException nfe)
    {
      throw new BadFieldException("Field '" + name + "' must have a numeric 'startIndex' attribute");
    }

    try
    {

      if (cursor.first("length"))
      {
        setLength(Integer.parseInt((String) cursor.getValue()));
      }
      else
      {
        throw new BadFieldException("Field '" + name + "' does not have a 'length' attribute");
      }
    }
    catch (NumberFormatException nfe)
    {
      throw new BadFieldException("Field '" + name + "' must have a numeric 'length' attribute");
    }
  }

  Field(String name, int startIndex, int length)
  {
    this.name = name;
    this.startIndex = startIndex;
    this.length = length;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setStartIndex(int startIndex)
  {
    this.startIndex = startIndex;
  }

  public void setLength(int length)
  {
    this.length = length;
  }

  public String getName()
  {
    return this.name;
  }

  public int getStartIndex()
  {
    return this.startIndex;
  }

  public int getLength()
  {
    return this.length;
  }
}