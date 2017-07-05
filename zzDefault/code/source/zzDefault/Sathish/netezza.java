package zzDefault.Sathish;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2017-06-01 15:04:03 IST
// -----( ON-HOST: MCSSIV02.eur.ad.sag

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
// --- <<IS-END-IMPORTS>> ---

public final class netezza

{
	// ---( internal utility methods )---

	final static netezza _instance = new netezza();

	static netezza _newInstance() { return new netezza(); }

	static netezza _cast(Object o) { return (netezza)o; }

	// ---( server methods )---




	public static final void netezzaConn (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(netezzaConn)>> ---
		// @sigtype java 3.5
  Connection conn1 = null;
  Statement statement = null;
  
    try {
		Class.forName("org.netezza.Driver");
		conn1 = DriverManager.getConnection("jdbc:netezza://t1xdbia501:5480/stg_whse", "esb","d6ln9262h");
        if (conn1 != null) {
            System.out.println("Connected with connection #1");
        }
     // Create the statement to be used to get the results.
        statement = conn1.createStatement();
        // Create a query to use.
        String query = "select VENDOR.vendor_id,VENDOR.PMT_TERMS_CODE as terms_code from edw..VENDOR where VENDOR.vendor_id='176807'";
        ResultSet resultSet = statement.executeQuery(query);

        System.out.println("Printing result...");
        while (resultSet.next()) {

         String tn = resultSet.getString("vendor_id");
         String opg = resultSet.getString("terms_code");
         System.out.println("\t vendor_id: " + tn + ",  PMT_TERMS_CODE: " + opg );
     }
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

    catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    finally {
        try {
            if (conn1 != null && !conn1.isClosed()) {
                conn1.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
		// --- <<IS-END>> ---

                
	}
}

