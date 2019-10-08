/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterfaces.adminservices;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.GovernanceServerEndpoint;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.UIServerConfig;
import org.testng.annotations.Test;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.fail;

public class TestUIServerErrorHandler {
    @Test
    void testValidateUserId()
    {
        UIServerErrorHandler errorHandler = new UIServerErrorHandler();

        try {
            errorHandler.validateUserId("1","2","3");

        } catch (OMAGNotAuthorizedException e) {
            fail();
        }
        try {
            errorHandler.validateUserId(null,"2","3");
            fail();
        } catch (OMAGNotAuthorizedException e) {
            checkInsertsInserted(e.getErrorMessage());
        }
    }
    void checkInsertsInserted(String errMsg) {
        if ( errMsg.contains("{") || errMsg.contains("}")) {
            fail("Error message inserts not filled - " + errMsg);
        }
        System.err.println("Error message is : " +errMsg);
    }
    @Test
    void testValidateServerName()
    {
        UIServerErrorHandler errorHandler = new UIServerErrorHandler();


        try {
            errorHandler.validateServerName("1","2");
        } catch (InvalidParameterException e) {
            fail();
        }

        try {
            errorHandler.validateServerName(null,"2");
            fail();
        } catch (InvalidParameterException e) {
            checkInsertsInserted(e.getErrorMessage());
        }
    }
    @Test
    void testValidateConfigServerName()
    {
        UIServerErrorHandler errorHandler = new UIServerErrorHandler();


        try {
            errorHandler.validateConfigServerName("1","1","2");
        } catch (OMAGConfigurationErrorException e) {
            fail();
        }

        try {
            errorHandler.validateConfigServerName("1","2","3");
            fail();
        } catch (OMAGConfigurationErrorException e) {
            checkInsertsInserted(e.getErrorMessage());
        }
    }

    @Test
    void testValidateConnection()
    {

        UIServerErrorHandler errorHandler = new UIServerErrorHandler();

        try {
            errorHandler.validateConnection(new Connection(),"1","2");
        } catch (InvalidParameterException e) {
            fail();
        }

        try {
            errorHandler.validateConnection(null,"1","2");
            fail();
        } catch (InvalidParameterException e) {
            checkInsertsInserted(e.getErrorMessage());
        }

        try {
            errorHandler.validateConnection(new Connection(),"1");
        } catch (InvalidParameterException e) {
            fail();
        }

        try {
            errorHandler.validateConnection(null,"1");
            fail();
        } catch (InvalidParameterException e) {
            checkInsertsInserted(e.getErrorMessage());
        }
    }
    @Test
    void testValidateMetadataServerName() {

        UIServerErrorHandler errorHandler = new UIServerErrorHandler();

        try {
            errorHandler.validateMetadataServerName("serv1", "meth2", "3");
        } catch (InvalidParameterException e) {
            fail();
        }
        try {
            errorHandler.validateMetadataServerName("serv11", "meth2", null);
            fail();
        } catch (InvalidParameterException e) {
            checkInsertsInserted(e.getErrorMessage());

        }
    }
    @Test
    void testValidateMetadataServerURL() {

        UIServerErrorHandler errorHandler = new UIServerErrorHandler();

        try {
            errorHandler.validateMetadataServerURL("serv1", "meth2", "http://aaa.bbb");
        } catch (InvalidParameterException e) {
            fail();
        }
        try {
            errorHandler.validateMetadataServerURL("serv11", "meth2", null);
            fail();
        } catch (InvalidParameterException e) {
            checkInsertsInserted(e.getErrorMessage());
        }
        try {
            errorHandler.validateMetadataServerURL("serv11", "meth2", "aaa");
            fail();
        } catch (InvalidParameterException e) {
            checkInsertsInserted(e.getErrorMessage());
        }
    }
    @Test
    void testValidateGovernanceServerName() {

        UIServerErrorHandler errorHandler = new UIServerErrorHandler();

        try {
            errorHandler.validateGovernanceServerName("serv2", "serv1", "meth");
        } catch (InvalidParameterException e) {
            fail();
        }
        try {
            errorHandler.validateGovernanceServerName(null, "serv1", "meth");
            fail();
        } catch (InvalidParameterException e) {
            checkInsertsInserted(e.getErrorMessage());
        }
    }
    @Test
    void testValidateGovernanceServerURL() {

        UIServerErrorHandler errorHandler = new UIServerErrorHandler();

        try {
            errorHandler.validateGovernanceServerURL( "http://aaa.bbb","serv1", "meth2");
        } catch (InvalidParameterException e) {
            fail();
        }
        try {
            errorHandler.validateGovernanceServerURL(null, "serv1", "meth");
            fail();
        } catch (InvalidParameterException e) {
            checkInsertsInserted(e.getErrorMessage());
        }
        try {
            errorHandler.validateGovernanceServerURL("bb","serv1", "meth2");
            fail();
        } catch (InvalidParameterException e) {
            checkInsertsInserted(e.getErrorMessage());
        }
    }
    @Test
    void testValidateGovernanceServiceName() {

        UIServerErrorHandler errorHandler = new UIServerErrorHandler();

        try {
            errorHandler.validateGovernanceServiceName("open-lineage","serv1", "meth2");
        } catch (InvalidParameterException e) {
            fail();
        }
        // check invalid service name fails
        try {
            errorHandler.validateGovernanceServiceName("ooooo","serv1", "meth2");
            fail();
        } catch (InvalidParameterException e) {
            checkInsertsInserted(e.getErrorMessage());

        }
        try {
            errorHandler.validateGovernanceServiceName(null,"serv1", "meth2");
            fail();
        } catch (InvalidParameterException e) {
            checkInsertsInserted(e.getErrorMessage());
        }

    }
    @Test
    void testValidateUIconfiguration() {

        UIServerErrorHandler errorHandler = new UIServerErrorHandler();
        UIServerConfig uiServerConfig = new UIServerConfig();
        uiServerConfig.setMetadataServerURL("http://aaa.bbb");
        uiServerConfig.setMetadataServerName("AAA");
        uiServerConfig.setOrganizationName("Coco");
        uiServerConfig.setLocalServerName("UIServer1");
        uiServerConfig.setLocalServerPassword("pwd");
        uiServerConfig.setLocalServerType("dsfgdsf");
        List<GovernanceServerEndpoint> govEnds = new ArrayList<>();
        uiServerConfig.setGovernanceServerEndpoints(govEnds);
        try {
            errorHandler.validateUIconfiguration("serv1", uiServerConfig, "meth2");
        } catch (InvalidParameterException e) {
            fail();
        }
        GovernanceServerEndpoint governanceServer1 = new GovernanceServerEndpoint();
        governanceServer1.setServerName("AAA");
        governanceServer1.setServerRootURL("http://aaa.aaa");
        governanceServer1.setGovernanceServiceName("open-lineage");
        govEnds.add(governanceServer1);
        try {
            errorHandler.validateUIconfiguration("serv1", uiServerConfig, "meth2");
        } catch (InvalidParameterException e) {
            fail();
        }

        GovernanceServerEndpoint governanceServer2 = new GovernanceServerEndpoint();
        governanceServer2.setServerName("BBB");
        governanceServer2.setServerRootURL("http://bbb.bbb");
        governanceServer2.setGovernanceServiceName("open-lineage");
        govEnds.add(governanceServer2);
        try {
            errorHandler.validateUIconfiguration("serv1", uiServerConfig, "meth2");
            fail();
        } catch (InvalidParameterException e) {
            checkInsertsInserted(e.getErrorMessage());
        }
    }
}
