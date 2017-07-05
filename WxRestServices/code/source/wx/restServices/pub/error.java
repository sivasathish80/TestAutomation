package wx.restServices.pub;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2015-03-16 08:18:45 CET
// -----( ON-HOST: sagbase.eur.ad.sag

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.util.HashMap;
import java.util.Map;
import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataUtil;
import com.wm.lang.ns.NSName;
import com.wm.lang.ns.NSNode;
import com.wm.lang.ns.NSService;
// --- <<IS-END-IMPORTS>> ---

public final class error

{
	// ---( internal utility methods )---

	final static error _instance = new error();

	static error _newInstance() { return new error(); }

	static error _cast(Object o) { return (error)o; }

	// ---( server methods )---




	public static final void registerErrorHandler (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(registerErrorHandler)>> ---
		// @sigtype java 3.5
		// [i] field:0:required packageName
		// [i] field:0:required errorHandlerFqn
		// [o] field:0:required success {"true","false"}
		// [o] field:0:required msg
		
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
		String	packageName = IDataUtil.getString( pipelineCursor, "packageName" );
		String	errorHandlerFqn = IDataUtil.getString( pipelineCursor, "errorHandlerFqn" );
		pipelineCursor.destroy();
		
		String msg = null;
		String success = "false";
		
		if( packageName == null || errorHandlerFqn == null ) {
			msg = "Neither packageName nor errorHandlerFqn must be empty in order to register an error handler";
			success = "false";
		} else {
			try {
				com.wm.lang.ns.NSService nsService = 
						(com.wm.lang.ns.NSService)com.wm.app.b2b.server.ns.Namespace.current().getNode(errorHandlerFqn);
				packageErrorHandlerMap.put(packageName, nsService);
				success = "true";
				msg = "Successfully registered service " + errorHandlerFqn + " as the error handler for package " + packageName;
				if( nsService == null ) {
					success = "false";
					msg = "Service '" + errorHandlerFqn + "' does not exist";
				}
			} catch( Exception e) {
				success = "false";
				msg = "Could not register Service '" + errorHandlerFqn + "' as error handler: " + e;
			}
		}
		
		// pipeline
		IDataCursor pipelineCursor_1 = pipeline.getCursor();
		IDataUtil.put( pipelineCursor_1, "success", success );
		IDataUtil.put( pipelineCursor_1, "msg", msg );
		pipelineCursor_1.destroy();
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	
	private static Map<String, NSService> packageErrorHandlerMap = new HashMap<String, NSService>();
	
	private static NSService lookupErrorHandler(NSName nsName) {
		if( nsName == null ) {
			return null;
		}
		NSName errorHandlerSvc = NSName.create(nsName.getFullName(), "errorHandler");
		NSNode node = com.wm.app.b2b.server.ns.Namespace.current().getNode(errorHandlerSvc);
		if( node == null ) {
			return lookupErrorHandler(nsName.getParent());	
		} else if( node.getNodeType() == NSNode.NODE_SERVICE ) {
			return (com.wm.lang.ns.NSService)node;
		}
		return null;
	}
	// --- <<IS-END-SHARED>> ---
}

