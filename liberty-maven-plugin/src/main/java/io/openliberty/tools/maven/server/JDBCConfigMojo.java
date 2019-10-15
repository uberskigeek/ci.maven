package io.openliberty.tools.maven.server;

import java.io.File;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import io.openliberty.tools.common.plugins.config.JDBCConfigDropinXmlDocument;
import io.openliberty.tools.common.plugins.util.PluginExecutionException;
import io.openliberty.tools.maven.BasicSupport;


@Mojo(name = "install-JDBC")
public class JDBCConfigMojo extends BasicSupport {
	// extends BasicSupport {

	
    public static String DERBY = "derby";
    public static String DB2 = "db2";
    public static String MYSQL = "mysql";

    public static String DERBY_DRIVER_CLASS_NAME = "org.apache.derby.jdbc.EmbeddedDriver";
    public static String DB2_DRIVER_CLASS_NAME = "com.ibm.db2.jcc.DB2Driver";
    public static String MYSQL_DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

    public static String DERBY_DEPENDENCY = "org.apache.derby:derby";
    public static String DB2_DEPENDENCY = "com.ibm.db2.jcc:db2jcc";
    public static String MYSQL_DEPENDENCY = "mysql:mysql-connector-java";
    public static String JDBC_CONFIG_FILE = "jdbc-config.xml";

    private static String DERBY_DEFAULT = "org.apache.derby:derby:10.14.2.0";
    
    public static final String LIBRARY_REF = "libraryRef";
    public static final String LIBRARY = "library";
    public static final String FILESET = "fileset";
    public static final String JDBC_DRIVER_1 = "JdbcDriver1";
    public static final String JDBC_LIBRARY_1 = "Library1";
    


	@Parameter(property = "dbtype")
	private String dbType ;
	
	private JDBCConfigDropinXmlDocument jdbcDoc;
	
	 @Override
	 protected void doExecute() throws Exception {
		
		 if( dbType != null ) {			 
		     jdbcDoc = JDBCConfigDropinXmlDocument.newInstance();
		     if(dbType == DERBY) {
		    	    addLibrary(DERBY_DRIVER_CLASS_NAME);
		     } else if(dbType == MYSQL) {
		    	    addLibrary(MYSQL_DRIVER_CLASS_NAME);
		     } else if(dbType == DB2) {
		    		addLibrary(DB2);		    	 
		     } else {
		    	    throw new PluginExecutionException("Invalid DB type " + dbType + " specified" );
		     }	
		     
		     jdbcDoc.writeXMLDocument(new File(serverDirectory + "configDropins/overrides/" + JDBC_CONFIG_FILE));
		 } else {
			 throw new PluginExecutionException("JDBC Config called but no JDBC specified" );
		 }
		 
		 
	 }
	
	
	private void addLibrary(String driverJar) {
		// Add library
        Element lib = jdbcDoc.createElement(LIBRARY);
	    jdbcDoc.createVariableWithValue(lib, "id", JDBC_LIBRARY_1, true);
        Element fileLoc = jdbcDoc.createElement(FILESET);
        fileLoc.setAttribute("dir", serverDirectory + "/resources");
        fileLoc.setAttribute("includes", driverJar);
        lib.appendChild(fileLoc);
        jdbcDoc.appendChild(lib);
	}
   
}
