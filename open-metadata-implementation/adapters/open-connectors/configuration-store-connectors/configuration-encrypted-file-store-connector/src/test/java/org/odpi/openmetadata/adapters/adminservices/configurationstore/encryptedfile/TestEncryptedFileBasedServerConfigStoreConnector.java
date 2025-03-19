/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.adminservices.configurationstore.encryptedfile;

import org.junit.jupiter.api.Test;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFRuntimeException;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestEncryptedFileBasedServerConfigStoreConnector {
    @Test
    void testGetConfigsPathName() {

        EncryptedFileBasedServerConfigStoreConnector connector = new EncryptedFileBasedServerConfigStoreConnector();
        String templateString = "src/test/resources/test1/data/servers/{0}/config/{0}.config";
        Set<String> fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(1, fileNames.size());
        String fileName1 = (String) fileNames.toArray()[0];
        assertTrue(fileName1.contains("server1"));
        templateString = "src/test/resources/test2/data/servers/{0}/config/{0}.config";
        fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(5, fileNames.size());
        templateString = "src/test/resources/test3/data/{0}.config";
        fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(1, fileNames.size());
        templateString = "src/test/resources/test4/{0}/my.config";
        fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(1, fileNames.size());
        templateString = "src/test/resources/test5/data/PRE_{0}_POST.config";
        fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(1, fileNames.size());
        templateString = "src/test/resources/test6/data/{0}_POST.config";
        fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(1, fileNames.size());
        templateString = "src/test/resources/test7/data/PRE_{0}.config";
        fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(1, fileNames.size());
        templateString = "src/test/resources/test8/data/PRE_{0}_POST/PRE_{0}_POST.config";
        fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(1, fileNames.size());
        templateString = "src/test/resources/test9/data/{0}_POST/{0}_POST.config";
        fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(1, fileNames.size());
        templateString = "src/test/resources/test10/PRE_{0}/PRE_{0}.config";
        fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(1, fileNames.size());
        templateString = "src/test/resources/test11/PRE_{0}/{0}_POST.config";
        fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(1, fileNames.size());
        templateString = "src/test/resources/test12/{0}_POST/PRE_{0}.config";
        fileNames = connector.getFileNames(templateString, "testMethod");
        assertEquals(1, fileNames.size());

        // check for invalid templates
        try {
            templateString = "src/test/resources/test10/data/{0}/{0}/{0}.config";
            connector.getFileNames(templateString, "testMethod");
            assertFalse(true, "Expected an error");
        } catch (OCFRuntimeException e) {

        }
        try {
            templateString = "src/test/resources/test10/data/test/my.config";
            connector.getFileNames(templateString, "testMethod");
            assertFalse(true, "Expected an error");
        } catch (OCFRuntimeException e) {

        }


    }

}
