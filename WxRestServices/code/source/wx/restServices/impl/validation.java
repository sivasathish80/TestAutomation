package wx.restServices.impl;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2015-02-11 08:23:51 CET
// -----( ON-HOST: sagbase.eur.ad.sag

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.b2b.server.InvokeState;
import java.util.ArrayList;
import java.util.List;
import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataUtil;
import com.wm.lang.ns.NSField;
import com.wm.lang.schema.ContentType;
// --- <<IS-END-IMPORTS>> ---

public final class validation

{
	// ---( internal utility methods )---

	final static validation _instance = new validation();

	static validation _newInstance() { return new validation(); }

	static validation _cast(Object o) { return (validation)o; }

	// ---( server methods )---




	public static final void validateInput (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(validateInput)>> ---
		// @sigtype java 3.5
		// [i] field:0:required serviceName
		// [i] field:0:required resource
		// [o] field:0:required isValid {"true","false"}
		// [o] recref:1:optional validationErrors wx.restServices.pub.documents.error:validationError
		IDataCursor pipelineC = pipeline.getCursor();
		String serviceName = IDataUtil.getString(pipelineC, "serviceName");
		String resource = IDataUtil.getString(pipelineC, "resource");
		List<NSField> fieldList = getInputFields(serviceName);
		String isValid = "true";
		List<IData> validationErrors = new ArrayList<IData>();
		
		for( NSField f : fieldList ) {
			int type = f.getType();
			boolean isOptional = f.isOptional();
			if( type == NSField.FIELD_STRING ) {
				String value = IDataUtil.getString(pipelineC, f.getName());
				if( value == null ) {
					if( isOptional ) {
						// all fine, the optional element is empty, continue
						continue;
					} else {
						// validation error, mandatory field is missing
						isValid = "false";
						validationErrors.add(createMandatoryFieldValidationErrorDoc(resource, f.getName()));
						continue;
					}
				}
				// value is filled, get content Type
				ContentType contentType = f.getContentType();
				if( contentType != null ) {
					int ctType = contentType.getType();
					if( ctType == ContentType.SIMPLE ) {
						com.wm.lang.schema.dummyWorkSpace validationWorkspace = new com.wm.lang.schema.dummyWorkSpace();
						boolean valid = contentType.validate(value, validationWorkspace);
						if( !valid ) {
							validationErrors.add(createContentTypeFieldValidationErrorDoc(resource, f.getName()));
							isValid = "false";
						}
					}
				} else {
					// no content type given, just continue
					continue;
				}
			}
		}
		IDataUtil.put(pipelineC, "isValid", isValid);
		IDataUtil.put(pipelineC, "validationErrors", validationErrors.toArray(new IData[0]));
		pipelineC.destroy();
			
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	private static List<NSField> getInputFields(String serviceName) {
		com.wm.lang.ns.NSService callingService = (com.wm.lang.ns.NSService)com.wm.app.b2b.server.ns.Namespace.current().getNode(serviceName);
		List<NSField> fieldList = new ArrayList<NSField>();
		// If there is a calling service...
		//Map calling Service Output SigVals to Variable Saved
		if (callingService.getSignature() != null 	&& 
				callingService.getSignature().getInput() != null && 
				callingService.getSignature().getInput().getFields() != null) {
		    for (NSField f : callingService.getSignature().getInput().getFields()) {
		    	fieldList.add(f);
		    }
		}
		return fieldList;
	}
	
	private static IData createMandatoryFieldValidationErrorDoc(String resource, String fieldName) {
		return createValidationErrorDoc(resource, fieldName, "missing_field");
	}
	
	private static IData createContentTypeFieldValidationErrorDoc(String resource, String fieldName) {
		return createValidationErrorDoc(resource, fieldName, "invalid");
	}
	
	private static IData createValidationErrorDoc(String resource, String fieldName, String code) {
		IData vDoc = IDataFactory.create();
		IDataCursor idc = vDoc.getCursor();
		IDataUtil.put(idc, "field", fieldName);
		IDataUtil.put(idc, "resource", resource);
		IDataUtil.put(idc, "code", code);
		idc.destroy();
		return vDoc;
	}
	// --- <<IS-END-SHARED>> ---
}

