package org.odpi.openmetadata.userinterface.adminservices.configuration.properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/* Copyright Contributors to the ODPi Egeria project. */
public class TestUIServerConfig {
    @Test
    void testdeser() {
        UIServerConfig uiServerConfig = new UIServerConfig();
        uiServerConfig.setServerName("aaa");


        ServerEndpointConfig serverendpoint = new ServerEndpointConfig();
        serverendpoint.setServerName("Server1");
        serverendpoint.setServerUrl("localhost:8080");
        uiServerConfig.setMetadataServerEndpointConfig(serverendpoint);
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        GovernanceServerEndpoints governanceServerEPs = new GovernanceServerEndpoints();
        ServerEndpointConfig lineageConfig = new ServerEndpointConfig();
        lineageConfig.setServerName("lin1");
        lineageConfig.setServerUrl("localhost:8081");

        governanceServerEPs.setOpenLineage(lineageConfig);
        uiServerConfig.setGovernanceServerEndpoints(governanceServerEPs);

        try
        {
            jsonString = objectMapper.writeValueAsString(uiServerConfig);
            assertTrue(jsonString.contains("Server1"));
            assertTrue(jsonString.contains("8080"));
            assertTrue(jsonString.contains("8081"));
            System.err.println(jsonString);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

    }
}
