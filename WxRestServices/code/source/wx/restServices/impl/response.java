package wx.restServices.impl;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2015-03-17 19:02:52 CET
// -----( ON-HOST: sagbase.eur.ad.sag

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;
import com.wm.app.b2b.server.InvokeState;
import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataUtil;
import com.wm.lang.ns.NSName;
import com.wm.lang.ns.NSBrokerLocalTrigger;
import com.wm.lang.ns.NSField;
import java.util.*;
// --- <<IS-END-IMPORTS>> ---

public final class response

{
	// ---( internal utility methods )---

	final static response _instance = new response();

	static response _newInstance() { return new response(); }

	static response _cast(Object o) { return (response)o; }

	// ---( server methods )---




	public static final void extractAcceptHdr (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(extractAcceptHdr)>> ---
		// @sigtype java 3.5
		// [i] record:0:required requestHdrs
		// [o] field:0:required accept
		IDataCursor pipelineC = pipeline.getCursor();
		IData requestHdrs = IDataUtil.getIData(pipelineC, "requestHdrs");
		String accept = IDataUtil.getString(requestHdrs.getCursor(), "Accept");
		IDataUtil.put(pipelineC, "accept", accept);
		pipelineC.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void extractOutputSignature (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(extractOutputSignature)>> ---
		// @sigtype java 3.5
		// [i] field:0:optional svcName
		// [i] field:0:optional pkgName
		// [o] field:0:required responseDocType
		// [o] record:0:required responseDoc
		//		com.wm.lang.ns.NSService callingService = Service.getCallingService();
		//		String packageName = callingService.getPackage().getName();
		IDataCursor pipelineC = pipeline.getCursor();
		String svcName = IDataUtil.getString(pipelineC, "svcName");
		
		com.wm.lang.ns.NSService callingService = 
				(com.wm.lang.ns.NSService)com.wm.app.b2b.server.ns.Namespace.current().getNode(svcName);
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		
		// If there is a calling service...
		//Map calling Service Output SigVals to Variable Saved
		if (callingService.getSignature() != null 	&& 
				callingService.getSignature().getOutput() != null && 
				callingService.getSignature().getOutput().getFields() != null) {
			// get the wsError document from the pipeline.
			// init the responseDoc and the responseDocType
			IData responseDoc = null;
			String responseDocType = null;
			// note that wsError will be null if the document is not part of the pipeline
			IData wsError = IDataUtil.getIData(pipelineCursor, "wsError");
			if( wsError != null ) {
				// if there is a wsError document we have to return it
				// wrap the wsError document in the wx.restServices.pub.documents.error:WsErrorResponse document
				IData wsErrorResponse = IDataFactory.create();
				IDataUtil.put(wsErrorResponse.getCursor(), "wsError", wsError);
				responseDoc = wsErrorResponse;
				responseDocType = "wx.restServices.pub.documents.error:WsErrorResponse";
			}
			// iterate over output signature in order to determine the business document
		    for (NSField f : callingService.getSignature().getOutput().getFields()) {
			  String responseDocName = f.getName();
			  if( responseDocName.equals("wsError") ) {
				  continue;
			  } else {
				  // get the document
				  Object o = IDataUtil.get(pipelineCursor,responseDocName);
				  if( !(o instanceof IData) ) {
					  // ignore any document which is not an IntegrationServer document
					  continue;
				  }
				  /*
				   * Check if there is an instance of the wsError document
				   */
				  if( wsError == null ) {
					  // there is no wsError document, just return the biz doc
					  // put the biz document in the output pipeline with the name "responseDoc"
					  responseDoc =  (IData)o;
					  // get the doctype fqn
					  // we expect f to be a doctype reference
					  if (f instanceof com.wm.lang.ns.NSRecordRef) {  
						  responseDocType = ((com.wm.lang.ns.NSRecordRef) f).getTargetName().getFullName();
					  }
				  } else {
					  // put the business doc in the data field of the wsError document
					  IDataUtil.put(wsError.getCursor(), "data", o);
				  }
				  // stop after the first biz document, we assume that there is only one in the Output signature
				  break;
			  }
			}
		  // put the document in the output pipeline with the name "responseDoc"
		  IDataUtil.put(pipelineCursor, "responseDoc", responseDoc);
		  IDataUtil.put(pipelineCursor, "responseDocType", responseDocType);
		}
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void extractOutputSignature_1 (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(extractOutputSignature_1)>> ---
		// @sigtype java 3.5
		// [i] field:0:optional svcName
		// [i] field:0:optional pkgName
		// [o] field:0:required responseDocType
		// [o] record:0:required responseDoc
		//		com.wm.lang.ns.NSService callingService = Service.getCallingService();
		//		String packageName = callingService.getPackage().getName();
		IDataCursor pipelineC = pipeline.getCursor();
		String svcName = IDataUtil.getString(pipelineC, "svcName");
		
		com.wm.lang.ns.NSService callingService = 
				(com.wm.lang.ns.NSService)com.wm.app.b2b.server.ns.Namespace.current().getNode(svcName);
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		
		// If there is a calling service...
		//Map calling Service Output SigVals to Variable Saved
		if (callingService.getSignature() != null 	&& 
				callingService.getSignature().getOutput() != null && 
				callingService.getSignature().getOutput().getFields() != null) {
		    for (NSField f : callingService.getSignature().getOutput().getFields()) {
			  String responseDocName = f.getName();
			  if( responseDocName == "wsError" ) {
				  // ignore, wsError is not the standard output
				  continue;
			  } else {
				  // get the document
				  Object o = IDataUtil.get(pipelineCursor,responseDocName);
				  if( !(o instanceof IData) ) {
					  // ignore any document which is not an IntegrationServer document
					  continue;
					  
				  }
				  // put the document in the output pipeline with the name "responseDoc"
				  IDataUtil.put(pipelineCursor, "responseDoc", o);
				  // get the doctype fqn
				  String responseDocType = null;
				  // we expect f to be a doctype reference
				  if (f instanceof com.wm.lang.ns.NSRecordRef) {  
					  responseDocType = ((com.wm.lang.ns.NSRecordRef) f).getTargetName().getFullName();
				  }
				  IDataUtil.put(pipelineCursor, "responseDocType", responseDocType);
				  // stop after the first document, we assume that there is only one in the Output signature
				  break;
			  }
			  
			}
		}
		    pipelineCursor.destroy();
			
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	
	
	
	public static void cleanup(IData doc, boolean int2String) {
	if (doc == null) {
		return;
	}
	IDataCursor c = doc.getCursor();
	c.first();
	while (true) {
		try {
			boolean delete = false;
			String key = c.getKey();
			if (key == null) {
				break;
			} 
			Object value = c.getValue();
			
			if (value instanceof IData) {
				cleanup((IData)value, int2String);
			} else if (value instanceof IData[]) {
				IData[] arr = (IData[]) value;
				for (int a = 0; a < arr.length; a++) {
					cleanup((IData)arr[a], int2String);
				}
			} else if (value instanceof String) {
				if( value == null || value.equals("")) {
					delete = true;
				}
			} else if( value instanceof Number ) {
				IDataUtil.put(c, key, value.toString());
			} else if( value == null ) {
				delete = true;
			}
			if( delete ) {
				c.delete();
			}
			if (c.hasMoreData()) {
				if( !delete )
					c.next();
			} else {
				return;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	return;
	}
		
		
	// --- <<IS-END-SHARED>> ---
}

