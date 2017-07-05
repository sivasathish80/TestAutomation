package zzDefault.Sathish.Interface2.services;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2017-07-01 01:25:49 IST
// -----( ON-HOST: MCSSIV02.eur.ad.sag

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
// --- <<IS-END-IMPORTS>> ---

public final class javaServices

{
	// ---( internal utility methods )---

	final static javaServices _instance = new javaServices();

	static javaServices _newInstance() { return new javaServices(); }

	static javaServices _cast(Object o) { return (javaServices)o; }

	// ---( server methods )---




	public static final void proxyConnectionCheck (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(proxyConnectionCheck)>> ---
		// @sigtype java 3.5
		// [i] field:0:required proxyHost
		// [i] field:0:required proxyPort
		// [i] field:0:required targetHTTPHost
		// [i] field:0:required targetHTTPPort
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	proxyHost = IDataUtil.getString( pipelineCursor, "proxyHost" );
			String	proxyPort = IDataUtil.getString( pipelineCursor, "proxyPort" );
			String	targetHTTPHost = IDataUtil.getString( pipelineCursor, "targetHTTPHost" );
			String	targetHTTPPort = IDataUtil.getString( pipelineCursor, "targetHTTPPort" );
			
			CloseableHttpClient httpclient = HttpClients.createDefault();
		    try {
		        HttpHost target = new HttpHost(targetHTTPHost,Integer.parseInt(targetHTTPPort), "http");
		        HttpHost proxy = new HttpHost(proxyHost,Integer.parseInt(proxyPort), "http");
		
		        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
		        HttpGet request = new HttpGet("/ipnet/as2");
		        request.setConfig(config);
		
		
		        CloseableHttpResponse response = httpclient.execute(target, request);
		        try {
		            pipelineCursor.insertAfter("OutputStatus", response.getStatusLine().toString());
		            pipelineCursor.insertAfter("OutputResponse",EntityUtils.toString(response.getEntity()));
		//		            String\u00A0responseString\u00A0=\u00A0EntityUtils.toString(response.getEntity(),\u00A0"UTF-8");
		//		            pipelineCursor.insertAfter("OutputResponse",EntityUtils.toString(response.getEntity()));
		        } finally {
		            response.close();
		        }
		    } catch(Exception e){
		    	pipelineCursor.insertAfter("ExceptionMessage",e.getMessage());
		    }finally {
		        try {
					httpclient.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
		    } 
		
		pipelineCursor.destroy();
		
		// pipeline
			
		// --- <<IS-END>> ---

                
	}
}

