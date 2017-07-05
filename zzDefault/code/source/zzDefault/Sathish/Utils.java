package zzDefault.Sathish;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2017-06-18 16:58:46 IST
// -----( ON-HOST: MCSSIV02.eur.ad.sag

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.wm.app.b2b.server.InvokeState;
import com.wm.app.b2b.server.ServerAPI;
import com.wm.app.b2b.server.User;
import com.wm.lang.ns.NSService;
import java.sql.Blob;
// --- <<IS-END-IMPORTS>> ---

public final class Utils

{
	// ---( internal utility methods )---

	final static Utils _instance = new Utils();

	static Utils _newInstance() { return new Utils(); }

	static Utils _cast(Object o) { return (Utils)o; }

	// ---( server methods )---




	public static final void convertDateToTimeStamp (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(convertDateToTimeStamp)>> ---
		// @sigtype java 3.5
		// [i] field:0:required strDate
		// [o] object:0:required sqlTimeStamp
		try
		{		
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	strDate = IDataUtil.getString( pipelineCursor, "strDate" );
		pipelineCursor.destroy();
		
		  DateFormat formatter;
		  //formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		  formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  //formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
		  Date date = (Date) formatter.parse(strDate);
		  Timestamp timeStampDate = new Timestamp(date.getTime());
		
		IDataCursor pipelineCursor_1 = pipeline.getCursor();
		IDataUtil.put( pipelineCursor_1, "sqlTimeStamp", timeStampDate );
		pipelineCursor_1.destroy();
		} catch (Exception e) {
		throw new ServiceException(e.getMessage());
		}
			
		// --- <<IS-END>> ---

                
	}



	public static final void convertIntToString (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(convertIntToString)>> ---
		// @sigtype java 3.5
		// [i] object:0:required intIns
		// [o] field:0:required strOpt
		// pipeline
		try
		{
		IDataCursor pipelineCursor = pipeline.getCursor();
			Object	intIns = IDataUtil.get( pipelineCursor, "intIns" );
		pipelineCursor.destroy();
		
		String strOpt="";
		if(intIns != null)
		{
		strOpt = String.valueOf(intIns);
		}
		else
		{
			throw new ServiceException("Input variable is null");
		}
		
		// pipeline
		IDataCursor pipelineCursor_1 = pipeline.getCursor();
		IDataUtil.put( pipelineCursor_1, "strOpt", strOpt );
			pipelineCursor_1.destroy();
		} catch(Exception ex){			
			throw new ServiceException(ex.toString());
			}
				
		// --- <<IS-END>> ---

                
	}



	public static final void convertObjectToBLOB (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(convertObjectToBLOB)>> ---
		// @sigtype java 3.5
		// [i] object:0:required inObj
		// [o] object:0:required outObj
		try
		{
		IDataCursor pipelineCursor = pipeline.getCursor();
			Object	inObj = IDataUtil.get( pipelineCursor, "inObj" );
		pipelineCursor.destroy();
		
		Blob blob = new javax.sql.rowset.serial.SerialBlob((byte[]) inObj);
		
		
		IDataCursor pipelineCursor_1 = pipeline.getCursor();
		IDataUtil.put( pipelineCursor_1, "outObj", blob );
			pipelineCursor_1.destroy();
		} catch(Exception ex){			
		throw new ServiceException(ex.toString());
		}
				
		// --- <<IS-END>> ---

                
	}



	public static final void convertToBigDecimal (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(convertToBigDecimal)>> ---
		// @sigtype java 3.5
		// [i] field:0:required strId
		// [o] object:0:required bdId
		try
		{
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	strId = IDataUtil.getString( pipelineCursor, "strId" );
		pipelineCursor.destroy();
		
		
		BigDecimal bc = new BigDecimal(strId);
		
		IDataCursor pipelineCursor_1 = pipeline.getCursor();
		IDataUtil.put( pipelineCursor_1, "bdId", bc );
		pipelineCursor_1.destroy();
		} catch (Exception e) {
		throw new ServiceException(e.getMessage());
		}
		
			
		// --- <<IS-END>> ---

                
	}



	public static final void getConfigFilePath (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getConfigFilePath)>> ---
		// @sigtype java 3.5
		// [i] field:0:required packageName
		// [o] field:0:required configFilePath
		try {
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String	packageName = IDataUtil.getString( pipelineCursor, "packageName" );
		pipelineCursor.destroy();
		
		File file = ServerAPI.getPackageConfigDir(packageName);
		String configFilePath=file.getAbsolutePath();
		
		IDataCursor pipelineCursor_1 = pipeline.getCursor();
		IDataUtil.put( pipelineCursor_1, "configFilePath", configFilePath);
		pipelineCursor_1.destroy();
			
		} catch(Exception ex){			
		throw new ServiceException(ex.toString());
		}
		// --- <<IS-END>> ---

                
	}



	public static final void getFlowServiceName (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getFlowServiceName)>> ---
		// @sigtype java 3.5
		// [o] field:0:required flowServiceName
		try{
		IDataCursor pipelineCursor = pipeline.getCursor();		
		String outputValue = new String();			
			NSService service = Service.getCallingService();			
			outputValue = service.toString();		
		IDataUtil.put( pipelineCursor,"flowServiceName",outputValue);
		pipelineCursor.destroy();
		} catch(Exception ex){			
			throw new ServiceException(ex.toString());
		}
			
		// --- <<IS-END>> ---

                
	}



	public static final void getPackageName (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getPackageName)>> ---
		// @sigtype java 3.5
		// [o] field:0:required getPackageName
		try{
		IDataCursor pipelineCursor = pipeline.getCursor();
		String outputValue = new String();	
		outputValue = Service.getCallingService().getPackage().getName();
		IDataUtil.put( pipelineCursor,"getPackageName",outputValue);
		pipelineCursor.destroy();
		}catch(Exception ex){			
			throw new ServiceException(ex.toString());
		}			
		// --- <<IS-END>> ---

                
	}



	public static final void getServerDetails (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getServerDetails)>> ---
		// @sigtype java 3.5
		// [o] field:0:required serverName
		// [o] field:0:required port
		try {
			String strServerName = ServerAPI.getServerName();
			int port = ServerAPI.getCurrentPort();
			String outport = String.valueOf(port);		
			IDataCursor descriptionCursor = pipeline.getCursor();
			IDataUtil.put( descriptionCursor, "serverName", strServerName );
			IDataUtil.put( descriptionCursor, "port", outport );		
			descriptionCursor.destroy();		
		} catch(Exception e) {
			throw new ServiceException(e.toString());
		}
		// --- <<IS-END>> ---

                
	}



	public static final void getUserId (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getUserId)>> ---
		// @sigtype java 3.5
		// [o] field:0:required userId
		try {
		InvokeState is = InvokeState.getCurrentState();
		User user = is.getCurrentUser();
		String strUsername = user.getName();		
		IDataCursor idcPipeline = pipeline.getCursor();
		idcPipeline.insertAfter("userId", strUsername);
		idcPipeline.destroy();		
		} catch(Exception e) {
			throw new ServiceException(e.toString());
		}
		// --- <<IS-END>> ---

                
	}



	public static final void throwException (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(throwException)>> ---
		// @sigtype java 3.5
		// [i] field:0:required errorMessage
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	errorMessage = IDataUtil.getString( pipelineCursor, "errorMessage" );
		pipelineCursor.destroy();
		
		throw new ServiceException(errorMessage);		
			
		// --- <<IS-END>> ---

                
	}
}

