package wx.restServices.impl;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2015-03-25 20:00:37 CET
// -----( ON-HOST: sagbase.eur.ad.sag

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.File;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import com.wm.app.b2b.server.ServerAPI;
import com.wm.data.IData;
// --- <<IS-END-IMPORTS>> ---

public final class startup

{
	// ---( internal utility methods )---

	final static startup _instance = new startup();

	static startup _newInstance() { return new startup(); }

	static startup _cast(Object o) { return (startup)o; }

	// ---( server methods )---




	public static final void setupLogger (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(setupLogger)>> ---
		// @sigtype java 3.5
		File configFile = new File(ServerAPI.getPackageConfigDir("WxRestServices"),
				"log4j.xml");
		if(configFile.exists() && configFile.canRead()) {
		    DOMConfigurator.configureAndWatch(configFile.getAbsolutePath());
		} else {
			throw new ServiceException("Cannot load log4j.xml config from WxInterceptor config dir");
		}
		// --- <<IS-END>> ---

                
	}
}

