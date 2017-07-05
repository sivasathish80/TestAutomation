package com.wm.ps.util.system;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class StreamCopier extends Thread
{
  Reader reader = null;
  Writer writer = null;

  public StreamCopier(Reader reader, Writer writer)
  {
    this.reader = reader;
    this.writer = writer;
  }

  public void run()
  {
    try
    {
      int numRead;
      char[] buff = new char[1024];
      if (reader != null)
      {
        while ((numRead = reader.read(buff, 0, buff.length)) >= 0)
        {
          if (writer != null)
          {
            writer.write(buff, 0, numRead);
          }
        }
      }
    }
    catch (IOException ioe)
    {
    }
    finally
    {
      try
      {
        if (writer != null)
        {
          writer.flush();
          writer.close();
        }
        if (reader != null)
        {
          reader.close();
        }
      }
      catch (Exception e)
      {
      }
    }
  }
}
