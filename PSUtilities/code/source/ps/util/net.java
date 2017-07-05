package ps.util;

//-----( IS Java Code Template v1.2
//-----( CREATED: 2008-01-08 21:00:00 EDT
//-----( ON-HOST: MCRSINGH.AME.ad.sag

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.net.*;

// --- <<IS-END-IMPORTS>> ---

public final class net

{
  // ---( internal utility methods )---

  final static net _instance = new net();

  static net _newInstance()
  {
    return new net();
  }

  static net _cast(Object o)
  {
    return (net) o;
  }

  // ---( server methods )---

  public static final void getServerInfo(IData pipeline) throws ServiceException
  {
    // --- <<IS-START(getServerInfo)>> ---
    // @subtype unknown
    // @sigtype java 3.5
    // [o] field:0:required hostname
    // [o] field:0:required ipAddress
    // [o] field:1:optional otherAddresses
    IDataCursor cursor = pipeline.getCursor();
    try
    {
      String hostname = InetAddress.getLocalHost().getHostName();
      InetAddress[] hosts = InetAddress.getAllByName(hostname);
      if (hosts != null && hosts.length > 0)
      {
        IDataUtil.put(cursor, "ipAddress", hosts[0].getHostAddress());
        if (hosts.length > 1)
        {
          String[] ips = new String[hosts.length - 1];
          for (int i = 1; i <= hosts.length; i++)
          {
            ips[i - 1] = hosts[i].getHostAddress();
          }
          IDataUtil.put(cursor, "otherAddresses", ips);
        }
      }
      IDataUtil.put(cursor, "hostname", hostname);
    }
    catch (UnknownHostException uhe)
    {
      throw new ServiceException(uhe);
    }
    finally
    {
      cursor.destroy();
    }
    // --- <<IS-END>> ---

  }
}
