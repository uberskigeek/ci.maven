package io.openliberty.tools.maven.server;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import io.openliberty.tools.common.plugins.config.JDBCConfigDropinXmlDocument;
import io.openliberty.tools.common.plugins.util.PluginExecutionException;
import io.openliberty.tools.maven.BasicSupport;


@Mojo(name = "install-JDBC", requiresDependencyCollection = ResolutionScope.COMPILE_PLUS_RUNTIME,
      requiresProject = true,
      requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class JDBCConfigMojo extends BasicSupport {

	
    public static String DERBY = "derby";
    public static String DB2 = "db2";
    public static String MYSQL = "mysql";

    public static String DERBY_DRIVER_CLASS_NAME = "org.apache.derby.jdbc.EmbeddedDriver";
    public static String DB2_DRIVER_CLASS_NAME = "com.ibm.db2.jcc.DB2Driver";
    public static String MYSQL_DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

    public static String DERBY_DEPENDENCY = "org.apache.derby:derby";
    public static String DB2_DEPENDENCY = "com.ibm.db2.jcc:db2jcc";
    public static String MYSQL_DEPENDENCY = "mysql:mysql-connector-java";
    
    public static String DERBY_ARTIFACT_ID = "derby";
    public static String DB2_ARTIFACT_ID = "db2jcc";
    public static String MYSQL_ARTIFACT_ID = "mysql-connector-java";
    
    public static String DERBY_DRIVER_NAME = "derby";
    public static String DB2_DRIVER_NAME = "db2";
    public static String MYSQL_DRIVER_NAME = "mysql";

    public static String JDBC_CONFIG_FILE = "jdbc-config.xml";

    private static String DERBY_DEFAULT = "org.apache.derby:derby:10.14.2.0";
    
    public static final String LIBRARY_REF = "libraryRef";
    public static final String LIBRARY = "library";
    public static final String FILESET = "fileset";
    public static final String JDBC_DRIVER_1 = "JdbcDriver1";
    public static final String JDBC_LIBRARY_1 = "Library1";
    
    public static String DRIVER_CLASS_NAME = "driverClassName";
    public static String DRIVER_NAME = "driverName";
    public static String DRIVER_JAR = "driverJar";
    public static String JDBC_DRIVER = "jdbcDriver";
    public static String JDBC_DRIVER_ID = "JdbcDriver1";

	@Parameter(property = "dbtype")
	private String dbType ;
	
	private JDBCConfigDropinXmlDocument jdbcDoc;
	
	private HashMap<String, String> driverInfo;
	
	 @Override
	 protected void doExecute() throws Exception {
		
		 log.info("Running install-JDBC goal");
		 String dbType = addDriverInfo();
		 if( dbType != null) {
		     jdbcDoc = JDBCConfigDropinXmlDocument.newInstance();
		     if(dbType.equals(DERBY)) {
		    	    jdbcDoc.addLibrary(driverInfo);
		     } else if(dbType.equals(MYSQL)) {
		    	    jdbcDoc.addLibrary(driverInfo);
		     } else if(dbType.equals(DB2)) {
		    		jdbcDoc.addLibrary(driverInfo);		    	 
		     } 	
		     jdbcDoc.writeXMLDocument(new File(serverDirectory + "/configDropins/overrides/" + JDBC_CONFIG_FILE));
		 }	else {
			 log.info("No JDBC dependencies defined");
		 }
		 
	 }
	
	private String addDriverInfo() throws Exception {
		String dbType = null;
		if(driverInfo!= null ) {
			driverInfo.clear();
		} else {
			driverInfo = new HashMap<String, String>();
		}
		Set<Artifact> artifacts = project.getArtifacts();
		if(artifacts.size() == 0) {
			log.info("No artifacts returned for this project");
		} else {
		  for(Artifact artifact: project.getArtifacts()) {
		  	  log.info("Checking artifact " + artifact.getGroupId() + ":" + artifact.getArtifactId());
			  String artifactCompare = artifact.getGroupId() +":"  + artifact.getArtifactId();
			  if(artifactCompare.equals(DERBY_DEPENDENCY)) {
			  	  driverInfo.put(DRIVER_NAME, DERBY);
				  driverInfo.put(DRIVER_CLASS_NAME, DERBY_DRIVER_CLASS_NAME);
		          driverInfo.put(DRIVER_JAR, artifact.getArtifactId() + "-" + artifact.getVersion() + ".jar");	
		          dbType = DERBY;
			  } else if(artifactCompare.equals(DB2_DEPENDENCY)) {
				  driverInfo.put(DRIVER_NAME, DB2_DRIVER_NAME);
	              driverInfo.put(DRIVER_CLASS_NAME, DB2_DRIVER_CLASS_NAME);
	              driverInfo.put(DRIVER_JAR, artifact.getArtifactId() + "-" + artifact.getVersion() + ".jar");
	              dbType = DB2;
			  } else if(artifactCompare.equals(MYSQL_DEPENDENCY)) {
				  driverInfo.put(DRIVER_NAME, MYSQL_DRIVER_NAME);
	              driverInfo.put(DRIVER_CLASS_NAME, MYSQL_DRIVER_CLASS_NAME);
	              driverInfo.put(DRIVER_JAR, artifact.getArtifactId() + "-" + artifact.getVersion()  + ".jar");
	              dbType = MYSQL;
			  }
		  }
		}
		return dbType;
		
	}
   
}
