package net.wasdev.wlp.maven.test.app;

import java.io.File;

import org.junit.Test;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import org.junit.Before;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import static junit.framework.Assert.*;

// Check that a JDBC library is generated in the configDropins/overrides 
public class LibertyJDBCLibraryConfigIT {
    
	private String dbtype = null;
	private static String DB2_DRIVER_JAR = "db2jcc-db2jcc4.jar";
	private static String MYSQL_DRIVER_JAR = "mysql-connector-java-8.0.15.jar";
	private static String DERBY_DRIVER_JAR =  "derby-10.11.1.1.jar";
	
    @Test 
    public void testLibertyJDBCLibraryConfigDropinsInstalled() throws Exception {

        String dbtype = System.getProperty("dbtype");
        assertNotNull("dbtype is null",dbtype);
    	
    	    File f = new File("./liberty/wlp/usr/servers/defaultServer/configDropins/overrides/jdbc-config.xml");
        assertTrue(f.getCanonicalFile() + " doesn't exist.", f.exists());
        if(dbtype.equals("db2")) {
           	assertTrue("Driver jar "+ DB2_DRIVER_JAR + " not found", validateDriverJar(DB2_DRIVER_JAR, f));
        } else if(dbtype.equals("mysql")) {
        	    assertTrue("Driver jar "+ MYSQL_DRIVER_JAR + " not found", validateDriverJar(MYSQL_DRIVER_JAR, f));
        } else if(dbtype.equals("derby")) {
    	        assertTrue("Driver jar "+ DERBY_DRIVER_JAR + " not found", validateDriverJar(DERBY_DRIVER_JAR, f));
        } else {
        	  throw new Exception("invalid dbtype " + dbtype + " passed");
        }
        	
        /*TODO
         * add code here to check that the correct diver jar is defined 
         * based on the value of dbtype.... So far unable to get dbtype set, always comes up null.
         */
        
    }
    
    private static boolean validateDriverJar(String driverJarName, File jdbc_config) throws Exception {
        boolean valid = false;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(jdbc_config);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("library");
        for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        NodeList fileElementList = eElement.getElementsByTagName("fileset");
                        for(int libcount = 0; libcount < fileElementList.getLength(); libcount++) {
                           Node fNode = fileElementList.item(libcount);
                           if(fNode.getNodeType() ==  Node.ELEMENT_NODE) {
                              Element fileElement = (Element)fNode;
                              String jarName = fileElement.getAttribute("includes");
                              if(jarName.equals(driverJarName)) {
                                   valid = true;
                                       break;
                              }
                           }
                        }
                }
                if(valid == true)
                  break;
        }
            return valid;

     }

    
}
