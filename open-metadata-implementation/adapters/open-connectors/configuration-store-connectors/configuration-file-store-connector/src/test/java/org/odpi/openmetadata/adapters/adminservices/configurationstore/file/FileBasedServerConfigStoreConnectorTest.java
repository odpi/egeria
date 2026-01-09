/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.adminservices.configurationstore.file;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFRuntimeException;
import org.testng.annotations.Test;

import java.util.Set;

import static org.testng.Assert.*;


public class FileBasedServerConfigStoreConnectorTest
{
    @Test
    void testGetConfigsPathName()
    {
        FileBasedServerConfigStoreConnector connector = new FileBasedServerConfigStoreConnector();
        String templateString = "src/test/resources/test1/data/servers/{0}/config/{0}.config";
        Set<String> fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(fileNames.size(), 1);
        String fileName1 = (String) fileNames.toArray()[0];
        assertTrue(fileName1.contains("server1"));
        templateString = "src/test/resources/test2/data/servers/{0}/config/{0}.config";
        fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(fileNames.size(), 5);
        templateString = "src/test/resources/test3/data/{0}.config";
        fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(fileNames.size(), 1);
        templateString = "src/test/resources/test4/{0}/my.config";
        fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(fileNames.size(), 1);
        templateString = "src/test/resources/test5/data/PRE_{0}_POST.config";
        fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(fileNames.size(), 1);
        templateString = "src/test/resources/test6/data/{0}_POST.config";
        fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(fileNames.size(), 1);
        templateString = "src/test/resources/test7/data/PRE_{0}.config";
        fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(fileNames.size(), 1);
        templateString = "src/test/resources/test8/data/PRE_{0}_POST/PRE_{0}_POST.config";
        fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(fileNames.size(), 1);
        templateString = "src/test/resources/test9/data/{0}_POST/{0}_POST.config";
        fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(fileNames.size(), 1);
        templateString = "src/test/resources/test10/PRE_{0}/PRE_{0}.config";
        fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(fileNames.size(), 1);
        templateString = "src/test/resources/test11/PRE_{0}/{0}_POST.config";
        fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(fileNames.size(), 1);
        templateString = "src/test/resources/test12/{0}_POST/PRE_{0}.config";
        fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(fileNames.size(), 1);

        // check for invalid templates
        try
        {
            templateString = "src/test/resources/test10/data/{0}/{0}/{0}.config";
            connector.getFileNames(templateString, "testMethod");
            fail("Expected an error");
        }
        catch (OMFRuntimeException e)
        {

        }
        try
        {
            templateString = "src/test/resources/test10/data/test/my.config";
            connector.getFileNames(templateString, "testMethod");
            assertFalse(true, "Expected an error");
        }
        catch (OMFRuntimeException e)
        {

        }


    }

}
