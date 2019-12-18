package org.odpi.openmetadata.userinterface.adminservices.configuration.properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertTrue;

/* Copyright Contributors to the ODPi Egeria project. */
public class TestUIServerConfig {
    @Test
    void testdeser() throws JsonProcessingException {
        UIServerConfig uiServerConfig = new UIServerConfig();
        uiServerConfig.setLocalServerName("cocoUIS1");
        uiServerConfig.setLocalServerPassword("bbb");
        uiServerConfig.setMetadataServerName("cocoMDS1");
        uiServerConfig.setMetadataServerURL("http://localhost:8080");
        ObjectMapper objectMapper = new ObjectMapper();
        List<ViewServiceConfig> viewList = new ArrayList<>();

        ViewServiceConfig subjectAreaViewServiceConfig = createViewServiceConfig(ViewServiceDescription.SUBJECT_AREA);
        subjectAreaViewServiceConfig.setViewServiceAdminClass("");

        viewList.add(subjectAreaViewServiceConfig);
        ViewServiceConfig assetSearchViewServiceConfig = createViewServiceConfig(ViewServiceDescription.ASSET_SEARCH);
        assetSearchViewServiceConfig.setViewServiceAdminClass("");

        viewList.add(assetSearchViewServiceConfig);
        ViewServiceConfig typeExplorerServiceConfig = createViewServiceConfig(ViewServiceDescription.TYPE_EXPLORER);
        typeExplorerServiceConfig.setViewServiceAdminClass("");
        viewList.add(typeExplorerServiceConfig);
        ViewServiceConfig openLineageViewServiceConfig = createViewServiceConfig(ViewServiceDescription.OPEN_LINEAGE);

        Map<String, Object> openLineageOptions = new HashMap<>();
        openLineageOptions.put("openLineageServerName","cocoLINS1");
        openLineageOptions.put("openLineageServerURL","http://localhost:8081");

        openLineageViewServiceConfig.setViewServiceOptions(openLineageOptions);
        viewList.add(openLineageViewServiceConfig);

        uiServerConfig.setViewServicesConfig(viewList);

        String jsonString = objectMapper.writeValueAsString(uiServerConfig);
        assertTrue(jsonString.contains("cocoMDS1"));
        assertTrue(jsonString.contains("8080"));
        assertTrue(jsonString.contains("8081"));
        System.err.println(jsonString);

    }
    private ViewServiceConfig createViewServiceConfig(ViewServiceDescription viewServiceDescription) {
        ViewServiceConfig viewServiceConfig = new ViewServiceConfig();

        viewServiceConfig.setServiceURLMarker(viewServiceDescription.getViewServiceURLMarker());
        viewServiceConfig.setViewServiceDescription(viewServiceDescription.getViewServiceDescription());
        viewServiceConfig.setViewServiceId(viewServiceDescription.getViewServiceCode());
        viewServiceConfig.setViewServiceName(viewServiceDescription.getViewServiceName());
        viewServiceConfig.setViewServiceWiki(viewServiceDescription.getViewServiceWiki());
        return viewServiceConfig;
    }
}
