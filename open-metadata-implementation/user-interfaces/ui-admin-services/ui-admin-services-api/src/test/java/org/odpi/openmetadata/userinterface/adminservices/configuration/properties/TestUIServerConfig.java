package org.odpi.openmetadata.userinterface.adminservices.configuration.properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertTrue;

/* Copyright Contributors to the ODPi Egeria project. */
public class TestUIServerConfig {
    @Test
    void testdeser() {
        UIServerConfig uiServerConfig = new UIServerConfig();
        uiServerConfig.setLocalServerName("aaa");
        uiServerConfig.setLocalServerPassword("bbb");
        uiServerConfig.setMetadataServerName("Server1");
        uiServerConfig.setMetadataServerURL("http://localhost:8080");
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        List<GovernanceServerEndpoint>  gsendpoints= new ArrayList<>();

        GovernanceServerEndpoint openLinenbdpoint = new GovernanceServerEndpoint();
        openLinenbdpoint.setGovernanceServiceName("open-lineage");
        openLinenbdpoint.setServerName("lin1");
        openLinenbdpoint.setServerRootURL("http://localhost:8081");
        gsendpoints.add(openLinenbdpoint);

        uiServerConfig.setGovernanceServerEndpoints(gsendpoints);

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
