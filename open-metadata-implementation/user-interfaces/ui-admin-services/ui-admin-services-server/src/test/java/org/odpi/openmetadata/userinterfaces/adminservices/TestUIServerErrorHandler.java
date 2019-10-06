package org.odpi.openmetadata.userinterfaces.adminservices;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.GovernanceServerEndpoint;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.UIServerConfig;
import org.odpi.openmetadata.userinterface.adminservices.ffdc.exception.UIConfigurationErrorException;
import org.odpi.openmetadata.userinterface.adminservices.ffdc.exception.UIInvalidParameterException;
import org.odpi.openmetadata.userinterface.adminservices.ffdc.exception.UINotAuthorizedException;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.fail;

/* Copyright Contributors to the ODPi Egeria project. */
public class TestUIServerErrorHandler {
    @Test
    void testValidateUserId()
    {
        UIServerErrorHandler errorHandler = new UIServerErrorHandler();

        try {
            errorHandler.validateUserId("1","2","3");

        } catch (UINotAuthorizedException e) {
            fail();
        }
        try {
            errorHandler.validateUserId(null,"2","3");
            fail();
        } catch (UINotAuthorizedException e) {
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
        } catch (UIInvalidParameterException e) {
            fail();
        }

        try {
            errorHandler.validateServerName(null,"2");
            fail();
        } catch (UIInvalidParameterException e) {
            checkInsertsInserted(e.getErrorMessage());
        }
    }
    @Test
    void testValidateConfigServerName()
    {
        UIServerErrorHandler errorHandler = new UIServerErrorHandler();


        try {
            errorHandler.validateConfigServerName("1","1","2");
        } catch (UIConfigurationErrorException e) {
            fail();
        }

        try {
            errorHandler.validateConfigServerName("1","2","3");
            fail();
        } catch (UIConfigurationErrorException e) {
            checkInsertsInserted(e.getErrorMessage());
        }
    }

    @Test
    void testValidateConnection()
    {

        UIServerErrorHandler errorHandler = new UIServerErrorHandler();

        try {
            errorHandler.validateConnection(new Connection(),"1","2");
        } catch (UIInvalidParameterException e) {
            fail();
        }

        try {
            errorHandler.validateConnection(null,"1","2");
            fail();
        } catch (UIInvalidParameterException e) {
            checkInsertsInserted(e.getErrorMessage());
        }

        try {
            errorHandler.validateConnection(new Connection(),"1");
        } catch (UIInvalidParameterException e) {
            fail();
        }

        try {
            errorHandler.validateConnection(null,"1");
            fail();
        } catch (UIInvalidParameterException e) {
            checkInsertsInserted(e.getErrorMessage());
        }
    }
    @Test
    void testValidateMetadataServerName() {

        UIServerErrorHandler errorHandler = new UIServerErrorHandler();

        try {
            errorHandler.validateMetadataServerName("serv1", "meth2", "3");
        } catch (UIInvalidParameterException e) {
            fail();
        }
        try {
            errorHandler.validateMetadataServerName("serv11", "meth2", null);
            fail();
        } catch (UIInvalidParameterException e) {
            checkInsertsInserted(e.getErrorMessage());

        }
    }
    @Test
    void testValidateMetadataServerURL() {

        UIServerErrorHandler errorHandler = new UIServerErrorHandler();

        try {
            errorHandler.validateMetadataServerURL("serv1", "meth2", "http://aaa.bbb");
        } catch (UIInvalidParameterException e) {
            fail();
        }
        try {
            errorHandler.validateMetadataServerURL("serv11", "meth2", null);
            fail();
        } catch (UIInvalidParameterException e) {
            checkInsertsInserted(e.getErrorMessage());
        }
        try {
            errorHandler.validateMetadataServerURL("serv11", "meth2", "aaa");
            fail();
        } catch (UIInvalidParameterException e) {
            checkInsertsInserted(e.getErrorMessage());
        }
    }
    @Test
    void testValidateGovernanceServerName() {

        UIServerErrorHandler errorHandler = new UIServerErrorHandler();

        try {
            errorHandler.validateGovernanceServerName("serv2", "serv1", "meth");
        } catch (UIInvalidParameterException e) {
            fail();
        }
        try {
            errorHandler.validateGovernanceServerName(null, "serv1", "meth");
            fail();
        } catch (UIInvalidParameterException e) {
            checkInsertsInserted(e.getErrorMessage());
        }
    }
    @Test
    void testValidateGovernanceServerURL() {

        UIServerErrorHandler errorHandler = new UIServerErrorHandler();

        try {
            errorHandler.validateGovernanceServerURL( "http://aaa.bbb","serv1", "meth2");
        } catch (UIInvalidParameterException e) {
            fail();
        }
        try {
            errorHandler.validateGovernanceServerURL(null, "serv1", "meth");
            fail();
        } catch (UIInvalidParameterException e) {
            checkInsertsInserted(e.getErrorMessage());
        }
        try {
            errorHandler.validateGovernanceServerURL("bb","serv1", "meth2");
            fail();
        } catch (UIInvalidParameterException e) {
            checkInsertsInserted(e.getErrorMessage());
        }
    }
    @Test
    void testValidateGovernanceServiceName() {

        UIServerErrorHandler errorHandler = new UIServerErrorHandler();

        try {
            errorHandler.validateGovernanceServiceName("open-lineage","serv1", "meth2");
        } catch (UIInvalidParameterException e) {
            fail();
        }
        // check invalid service name fails
        try {
            errorHandler.validateGovernanceServiceName("ooooo","serv1", "meth2");
            fail();
        } catch (UIInvalidParameterException e) {
            checkInsertsInserted(e.getErrorMessage());

        }
        try {
            errorHandler.validateGovernanceServiceName(null,"serv1", "meth2");
            fail();
        } catch (UIInvalidParameterException e) {
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
        } catch (UIInvalidParameterException e) {
            fail();
        }
        GovernanceServerEndpoint governanceServer1 = new GovernanceServerEndpoint();
        governanceServer1.setServerName("AAA");
        governanceServer1.setServerURL("http://aaa.aaa");
        governanceServer1.setGovernanceServiceName("open-lineage");
        govEnds.add(governanceServer1);
        try {
            errorHandler.validateUIconfiguration("serv1", uiServerConfig, "meth2");
        } catch (UIInvalidParameterException e) {
            fail();
        }

        GovernanceServerEndpoint governanceServer2 = new GovernanceServerEndpoint();
        governanceServer2.setServerName("BBB");
        governanceServer2.setServerURL("http://bbb.bbb");
        governanceServer2.setGovernanceServiceName("open-lineage");
        govEnds.add(governanceServer2);
        try {
            errorHandler.validateUIconfiguration("serv1", uiServerConfig, "meth2");
            fail();
        } catch (UIInvalidParameterException e) {
            checkInsertsInserted(e.getErrorMessage());
        }
    }
}
