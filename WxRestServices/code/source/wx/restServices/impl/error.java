package wx.restServices.impl;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2015-03-16 08:18:43 CET
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




	public static final void invokeErrorHandler (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(invokeErrorHandler)>> ---
		// @sigtype java 3.5
		// [i] field:0:required callingService
		// [i] recref:0:required wsError wx.restServices.pub.documents.error:WsError
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
		String	callingService = IDataUtil.getString( pipelineCursor, "callingService" );
		// wsError
		IData	wsError = IDataUtil.getIData( pipelineCursor, "wsError" );
		pipelineCursor.destroy();
		
		if( wsError == null || callingService == null )  {
			return;
		}
		
		com.wm.lang.ns.NSService nsService = 
				(com.wm.lang.ns.NSService)com.wm.app.b2b.server.ns.Namespace.current().getNode(callingService);
		NSName parentFolderNsName = nsService.getNSName().getInterfaceNSName();
		if( parentFolderNsName == null ) {
			return;
		}
		NSService errorHandlerService = lookupErrorHandler(parentFolderNsName);
		
		if( errorHandlerService == null ) {
			errorHandlerService = packageErrorHandlerMap.get(nsService.getPackage().getName());
			if( errorHandlerService == null ) {
				return;
			}
		}
		
		try {
			errorHandlerService.invoke(pipeline);
		} catch (Exception e) {
			System.out.println("Could not inovke error handling service " + errorHandlerService + ": "  + e);
			e.printStackTrace();
		}
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

