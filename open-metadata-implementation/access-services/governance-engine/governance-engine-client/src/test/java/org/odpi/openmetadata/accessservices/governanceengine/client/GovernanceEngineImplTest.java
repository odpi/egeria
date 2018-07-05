/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceengine.client;

/*
 * Unit tests for GovernanceEngineImpl
 *
 * Requires Mockito 2.x & JUnit 5
 */
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.MetadataServerException;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/*
 * Mockito requires static imports
 */
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/*
 * Test suite metadata & configuration
 */
@SuiteDisplayName("Governance Engine Client Implementation")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)

public class GovernanceEngineImplTest {

    @Mock
    private RestTemplate restTemplate; // from Spring, used for REST API calls to server in implementation under test

    static final String defaultOMASServerURL = "http://localhost:12345";
    static final String defaultUserId = "zebra91";
    static final String defaultClassificationType = "interestingClassificationType";
    static final String defaultRootType = "interestingType";
    static final String defaultGUID = "c7184523-7ca5-4876-9210-fe1bb1b55cd7";

    /*
     * Class under test requiring mock injection - note - not used in tests of constructor
     */
    @InjectMocks
    private GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl(defaultOMASServerURL);

    private Throwable thrown; // for testing exceptions

    @Test
    @DisplayName("GovernanceEngineImpl Constructor - Check empty server URL")
    void testGetGovernedAssetComponentListBadConstructorEmpty() {
        GovernanceEngineImpl governanceEngineImplalt = new GovernanceEngineImpl(""); // local

        // Mockito assertion for exception that would be thrown after using a poor constructor parameter - actual method is arbitary
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            List<GovernedAssetComponent> result = governanceEngineImplalt.getGovernedAssetComponentList("", "rootClassificationType", "rootType");
        });

        // Must not call backend rest server
        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        // validate appropriate exception message
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-001"));
    }

    @Test
    @DisplayName("GovernanceEngineImp Constructor - Check null server URL")
    void testGetGovernedAssetComponentListBadConstructorNull() {

        GovernanceEngineImpl governanceEngineImplalt = new GovernanceEngineImpl(null);
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            List<GovernedAssetComponent> result = governanceEngineImplalt.getGovernedAssetComponentList("", "rootClassificationType", "rootType");
        });

        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-001"));
    }

    @Test
    @DisplayName("getGovernedAssetComponentList - null userid")
    void testGetGovernedAssetComponentListNullUserid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            List<GovernedAssetComponent> result = governanceEngineImpl.getGovernedAssetComponentList(null, defaultClassificationType, defaultRootType);
        });

        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-003"));
    }

    @Test
    @DisplayName("getGovernedAssetComponentList - empty userid")
    void testGetGovernedAssetComponentListEmptyUserid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            List<GovernedAssetComponent> result = governanceEngineImpl.getGovernedAssetComponentList("", defaultClassificationType, defaultRootType);
        });

        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-003"));
    }

    @Test
    @DisplayName("getGovernedAssetComponentList - client REST exception")
    void testGetGovernedAssetComponentListClientAPIException() {
        // configure mock to just return exception. Common one for client rest api call issues (network etc) - could be different to backend issue
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenThrow(new ResourceAccessException("Test Exception from REST client error"));

        thrown = assertThrows(MetadataServerException.class, () ->
        {
            List<GovernedAssetComponent> result = governanceEngineImpl.getGovernedAssetComponentList(defaultUserId, defaultClassificationType, defaultRootType);
        });

        // verify we actually used the mocked rest template once
        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
         assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-503-002"));
    }

    @Test
    @DisplayName("getGovernedAssetComponentList - server side exception")
    void testGetGovernedAssetComponentListRemoteInvalidParmException() {
        // Not testing backend, so let's just check one error case that should go to server & back (but is mocked)
        GovernedAssetComponentListAPIResponse expectedResponse = new GovernedAssetComponentListAPIResponse();
        expectedResponse.setRelatedHTTPCode(404);
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);

        try {
            List<GovernedAssetComponent> result = governanceEngineImpl.getGovernedAssetComponentList(defaultUserId, "rootClassificationType", "rootType");
        } catch (Exception e) { } // shouldn't get the exception!

        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        // TODO Need to validate the response here
    }


    // Now check a good return
    @Test
    @DisplayName("getGovernedAssetComponentList - good result")
    void testGetGovernedAssetComponentListGoodData() {
        GovernedAssetComponentListAPIResponse expectedResponse = new GovernedAssetComponentListAPIResponse();
        expectedResponse.setRelatedHTTPCode(200);
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);

        try {
            List<GovernedAssetComponent> result = governanceEngineImpl.getGovernedAssetComponentList(defaultUserId, defaultClassificationType, defaultRootType);
        } catch (Exception e) { }

        // verify we actually used the mocked rest template once
        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        //TODO: Verify the content in the list. tbd.
           }

    @Test
    @DisplayName("getGovernanceClassificationDefinitionList - null userid")
    void testGetGovernanceClassificationDefinitionListNullUserid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            List<GovernanceClassificationDefinition> result = governanceEngineImpl.getGovernanceClassificationDefinitionList(null, defaultClassificationType);
        });

        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-003"));
    }

    @Test
    @DisplayName("getGovernanceClassificationDefinitionList - empty userid")
    void testGetGovernanceClassificationDefinitionListEmptyUserid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            List<GovernanceClassificationDefinition> result = governanceEngineImpl.getGovernanceClassificationDefinitionList("", defaultClassificationType);
            // exception!
        });

        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-003"));
    }

    @Test
    @DisplayName("getGovernanceClassificationDefinitionList - client REST exception")
    void testGetGovernanceClassificationDefinitionListClientAPIException() {
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenThrow(new ResourceAccessException("Test Exception from REST client error"));

        thrown = assertThrows(MetadataServerException.class, () ->
        {
            List<GovernanceClassificationDefinition> result = governanceEngineImpl.getGovernanceClassificationDefinitionList(defaultUserId, defaultClassificationType);
        });
       verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
               assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-503-002"));
    }


    @Test
    @DisplayName("getGovernanceClassificationDefinitionList - server side exception")
    void testGetGovernanceClassificationDefinitionList() {
        GovernanceClassificationDefinitionListAPIResponse expectedResponse = new GovernanceClassificationDefinitionListAPIResponse();
        expectedResponse.setRelatedHTTPCode(404);
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);

        try {
            List<GovernanceClassificationDefinition> result = governanceEngineImpl.getGovernanceClassificationDefinitionList(defaultUserId, "rootClassificationType");
        } catch (Exception e) { };

        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
          // TODO Need to validate the response here
    }


    // Now check a good return
    @Test
    @DisplayName("getGovernedAssetComponentList - good result")
    void testGetGovernanceClassificationDefinitionListGoodData() {
        GovernanceClassificationDefinitionListAPIResponse expectedResponse = new GovernanceClassificationDefinitionListAPIResponse();
        expectedResponse.setRelatedHTTPCode(200);
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);

        try {
            List<GovernanceClassificationDefinition> result = governanceEngineImpl.getGovernanceClassificationDefinitionList(defaultUserId, defaultClassificationType);
        } catch (Exception e) { };

        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());

        //TODO: Verify the content in the list. tbd.
      }

    @Test
    @DisplayName("getGovernedAssetComponent - null userid")
    void testGetGovernedAssetComponentNullUserid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            GovernedAssetComponent result = governanceEngineImpl.getGovernedAssetComponent(null, defaultGUID);
        });

        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-003"));
    }

    @Test
    @DisplayName("getGovernedAssetComponent - empty userid")
    void testGetGovernedAssetComponentEmptyUserid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            GovernedAssetComponent result = governanceEngineImpl.getGovernedAssetComponent("", defaultGUID);
        });

        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-003"));

    }

    @Test
    @DisplayName("getGovernedAssetComponent - null guid")
    void testGetGovernedAssetComponentNullGuid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            GovernedAssetComponent result = governanceEngineImpl.getGovernedAssetComponent(defaultUserId, null);
        });

        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-004"));

    }

    @Test
    @DisplayName("getGovernedAssetComponent - empty guid")
    void testGetGovernedAssetComponentEmptyGuid() {

        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            GovernedAssetComponent result = governanceEngineImpl.getGovernedAssetComponent(defaultUserId, "");
            // exception!
        });

        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-004"));
    }

    @Test
    @DisplayName("getGovernedAssetComponent - client REST exception")
    void testGetGovernedAssetComponentClientAPIException() {
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenThrow(new ResourceAccessException("Test Exception from REST client error"));

        thrown = assertThrows(MetadataServerException.class, () ->
        {
            GovernedAssetComponent result = governanceEngineImpl.getGovernedAssetComponent(defaultUserId, defaultGUID);
        });

        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-503-002"));
    }

    @Test
    @DisplayName("getGovernedAssetComponent - server side exception")
    void testGetGovernedAssetComponentRemoteInvalidParmException() {
        GovernedAssetComponentAPIResponse expectedResponse = new GovernedAssetComponentAPIResponse();
        expectedResponse.setRelatedHTTPCode(404);
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);

        try {
            GovernedAssetComponent result = governanceEngineImpl.getGovernedAssetComponent(defaultUserId, defaultGUID);
        } catch (Exception e) { };

        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
         // TODO Need to validate the response here
    }

    @Test
    @DisplayName("getGovernedAssetComponent - good result")
    void testGetGovernedAssetComponentGoodData() {
        GovernedAssetComponentAPIResponse expectedResponse = new GovernedAssetComponentAPIResponse();
        expectedResponse.setRelatedHTTPCode(200);
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);

        try {
            GovernedAssetComponent result = governanceEngineImpl.getGovernedAssetComponent(defaultUserId, defaultGUID);
        } catch (Exception e) { };

        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        //TODO: Verify the content in the list. tbd.
    }

    @Test
    @DisplayName("getGovernanceClassificationDefinition - null userid")
    void testGetGovernanceClassificationDefinitionNullUserid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            GovernanceClassificationDefinition result = governanceEngineImpl.getGovernanceClassificationDefinition(null, defaultGUID);
        });

        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-003"));
    }

    @Test
    @DisplayName("getGovernanceClassificationDefinition - empty userid")
    void testGetGovernanceClassificationDefinitionEmptyUserid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            GovernanceClassificationDefinition result = governanceEngineImpl.getGovernanceClassificationDefinition("", defaultGUID);
        });

        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-003"));

    }

    @Test
    @DisplayName("getGovernanceClassificationDefinition - null guid")
    void testGetGovernanceClassificationDefinitionNullGuid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            GovernanceClassificationDefinition result = governanceEngineImpl.getGovernanceClassificationDefinition(defaultUserId, null);
        });

        // Check we do not call the backend server - should be checked locally
        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-004"));
    }

    @Test
    @DisplayName("getGovernanceClassificationDefinition - empty guid")
    void testGetGovernanceClassificationDefinitionEmptyGuid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            GovernanceClassificationDefinition result = governanceEngineImpl.getGovernanceClassificationDefinition(defaultUserId, "");
        });
        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-004"));
    }

    @Test
    @DisplayName("getGovernanceClassificationDefinition - client REST exception")
    void testGetGovernanceClassificationDefinitionClientAPIException() {
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenThrow(new ResourceAccessException("Test Exception from REST client error"));

        thrown = assertThrows(MetadataServerException.class, () ->
        {
            GovernanceClassificationDefinition result = governanceEngineImpl.getGovernanceClassificationDefinition(defaultUserId, defaultGUID);
        });

        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
          assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-503-002"));
    }

    @Test
    @DisplayName("getGovernanceClassificationDefinition - server side exception")
    void testGetGovernanceClassificationDefinitionServerException() {
        GovernanceClassificationDefinitionAPIResponse expectedResponse = new GovernanceClassificationDefinitionAPIResponse();
        expectedResponse.setRelatedHTTPCode(404);
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);

        try {
            GovernanceClassificationDefinition result = governanceEngineImpl.getGovernanceClassificationDefinition(defaultUserId, defaultGUID);
        } catch (Exception e) { };

        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
         // TODO Need to validate the response here
    }


    // Now check a good return
    @Test
    @DisplayName("getGovernedAssetComponent - good result")
    void testGetGovernanceClassificationDefinitionGoodData() {
        GovernanceClassificationDefinitionAPIResponse expectedResponse = new GovernanceClassificationDefinitionAPIResponse();
        expectedResponse.setRelatedHTTPCode(200);
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);

        try {
            GovernanceClassificationDefinition result = governanceEngineImpl.getGovernanceClassificationDefinition(defaultUserId, defaultGUID);
        } catch (Exception e) { };

        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        //TODO: Verify the content in the list. tbd.
    }

    //TODO Add Tests for other backend exceptions (rootclassificationnotfound, rootassettypenotfound,usernotauthorized)
}

