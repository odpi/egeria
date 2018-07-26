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
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.MetadataServerException;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.*;
import org.slf4j.Logger;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/*
 * Mockito requires static imports
 */

/*
 * Test suite metadata & configuration
 */
@SuiteDisplayName("Governance Engine Client Implementation")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)

public class GovernanceEngineImplTest {

    @Mock
    private RestTemplate restTemplate; // from Spring, used for REST API calls to handlers in implementation under test

    @Mock
    private Logger log;
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
    @DisplayName("GovernanceEngineImpl Constructor - Check empty handlers URL")
    void testGetGovernedAssetComponentListBadConstructorEmpty() {
        GovernanceEngineImpl governanceEngineImplalt = new GovernanceEngineImpl(""); // local

        // Mockito assertion for exception that would be thrown after using a poor constructor parameter - actual method is arbitary
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            List<GovernedAsset> result = governanceEngineImplalt.getGovernedAssetList("", "rootClassificationType", "rootType");
        });

        // Must not call backend rest handlers
        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        // validate appropriate exception message
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-001"));
    }

    @Test
    @DisplayName("GovernanceEngineImp Constructor - Check null handlers URL")
    void testGetGovernedAssetComponentListBadConstructorNull() {

        GovernanceEngineImpl governanceEngineImplalt = new GovernanceEngineImpl(null);
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            List<GovernedAsset> result = governanceEngineImplalt.getGovernedAssetList("", "rootClassificationType", "rootType");
        });

        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-001"));
    }

    @Test
    @DisplayName("getGovernedAssetList - null userid")
    void testGetGovernedAssetComponentListNullUserid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            List<GovernedAsset> result = governanceEngineImpl.getGovernedAssetList(null, defaultClassificationType, defaultRootType);
        });

        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-003"));
    }

    @Test
    @DisplayName("getGovernedAssetList - empty userid")
    void testGetGovernedAssetComponentListEmptyUserid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            List<GovernedAsset> result = governanceEngineImpl.getGovernedAssetList("", defaultClassificationType, defaultRootType);
        });

        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-003"));
    }

    @Test
    @DisplayName("getGovernedAssetList - client REST exception")
    void testGetGovernedAssetComponentListClientAPIException() {
        // configure mock to just return exception. Common one for client rest client call issues (network etc) - could be different to backend issue
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenThrow(new ResourceAccessException("Test Exception from REST client error"));

        thrown = assertThrows(MetadataServerException.class, () ->
        {
            List<GovernedAsset> result = governanceEngineImpl.getGovernedAssetList(defaultUserId, defaultClassificationType, defaultRootType);
        });

        // verify we actually used the mocked rest template once
        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
         assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-503-002"));
    }

    @Test
    @DisplayName("getGovernedAssetList - handlers side exception")
    void testGetGovernedAssetComponentListRemoteInvalidParmException() {
        // Not testing backend, so let's just check one error case that should go to handlers & back (but is mocked)
        GovernedAssetListAPIResponse expectedResponse = new GovernedAssetListAPIResponse();
        expectedResponse.setRelatedHTTPCode(404);
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);

        try {
            List<GovernedAsset> result = governanceEngineImpl.getGovernedAssetList(defaultUserId, "rootClassificationType", "rootType");
        } catch (Exception e) { } // shouldn't get the exception!

        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        // TODO Need to validate the response here
    }


    // Now check a good return
    @Test
    @DisplayName("getGovernedAssetList - good result")
    void testGetGovernedAssetComponentListGoodData() {
        GovernedAssetListAPIResponse expectedResponse = new GovernedAssetListAPIResponse();
        expectedResponse.setRelatedHTTPCode(200);
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);

        try {
            List<GovernedAsset> result = governanceEngineImpl.getGovernedAssetList(defaultUserId, defaultClassificationType, defaultRootType);
        } catch (Exception e) { }

        // verify we actually used the mocked rest template once
        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        //TODO: Verify the content in the list. tbd.
           }

    @Test
    @DisplayName("getGovernanceClassificationDefList - null userid")
    void testGetGovernanceClassificationDefinitionListNullUserid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            List<GovernanceClassificationDef> result = governanceEngineImpl.getGovernanceClassificationDefList(null, defaultClassificationType);
        });

        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-003"));
    }

    @Test
    @DisplayName("getGovernanceClassificationDefList - empty userid")
    void testGetGovernanceClassificationDefinitionListEmptyUserid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            List<GovernanceClassificationDef> result = governanceEngineImpl.getGovernanceClassificationDefList("", defaultClassificationType);
            // exception!
        });

        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-003"));
    }

    @Test
    @DisplayName("getGovernanceClassificationDefList - client REST exception")
    void testGetGovernanceClassificationDefinitionListClientAPIException() {
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenThrow(new ResourceAccessException("Test Exception from REST client error"));

        thrown = assertThrows(MetadataServerException.class, () ->
        {
            List<GovernanceClassificationDef> result = governanceEngineImpl.getGovernanceClassificationDefList(defaultUserId, defaultClassificationType);
        });
       verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
               assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-503-002"));
    }


    @Test
    @DisplayName("getGovernanceClassificationDefList - handlers side exception")
    void testGetGovernanceClassificationDefinitionList() {
        GovernanceClassificationDefListAPIResponse expectedResponse = new GovernanceClassificationDefListAPIResponse();
        expectedResponse.setRelatedHTTPCode(404);
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);

        try {
            List<GovernanceClassificationDef> result = governanceEngineImpl.getGovernanceClassificationDefList(defaultUserId, "rootClassificationType");
        } catch (Exception e) { };

        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
          // TODO Need to validate the response here
    }


    // Now check a good return
    @Test
    @DisplayName("getGovernedAssetList - good result")
    void testGetGovernanceClassificationDefinitionListGoodData() {
        GovernanceClassificationDefListAPIResponse expectedResponse = new GovernanceClassificationDefListAPIResponse();
        expectedResponse.setRelatedHTTPCode(200);
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);

        try {
            List<GovernanceClassificationDef> result = governanceEngineImpl.getGovernanceClassificationDefList(defaultUserId, defaultClassificationType);
        } catch (Exception e) { };

        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());

        //TODO: Verify the content in the list. tbd.
      }

    @Test
    @DisplayName("getGovernedAsset - null userid")
    void testGetGovernedAssetComponentNullUserid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            GovernedAsset result = governanceEngineImpl.getGovernedAsset(null, defaultGUID);
        });

        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-003"));
    }

    @Test
    @DisplayName("getGovernedAsset - empty userid")
    void testGetGovernedAssetComponentEmptyUserid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            GovernedAsset result = governanceEngineImpl.getGovernedAsset("", defaultGUID);
        });

        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-003"));

    }

    @Test
    @DisplayName("getGovernedAsset - null guid")
    void testGetGovernedAssetComponentNullGuid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            GovernedAsset result = governanceEngineImpl.getGovernedAsset(defaultUserId, null);
        });

        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-004"));

    }

    @Test
    @DisplayName("getGovernedAsset - empty guid")
    void testGetGovernedAssetComponentEmptyGuid() {

        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            GovernedAsset result = governanceEngineImpl.getGovernedAsset(defaultUserId, "");
            // exception!
        });

        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-004"));
    }

    @Test
    @DisplayName("getGovernedAsset - client REST exception")
    void testGetGovernedAssetComponentClientAPIException() {
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenThrow(new ResourceAccessException("Test Exception from REST client error"));

        thrown = assertThrows(MetadataServerException.class, () ->
        {
            GovernedAsset result = governanceEngineImpl.getGovernedAsset(defaultUserId, defaultGUID);
        });

        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-503-002"));
    }

    @Test
    @DisplayName("getGovernedAsset - handlers side exception")
    void testGetGovernedAssetComponentRemoteInvalidParmException() {
        GovernedAssetAPIResponse expectedResponse = new GovernedAssetAPIResponse();
        expectedResponse.setRelatedHTTPCode(404);
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);

        try {
            GovernedAsset result = governanceEngineImpl.getGovernedAsset(defaultUserId, defaultGUID);
        } catch (Exception e) { };

        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
         // TODO Need to validate the response here
    }

    @Test
    @DisplayName("getGovernedAsset - good result")
    void testGetGovernedAssetComponentGoodData() {
        GovernedAssetAPIResponse expectedResponse = new GovernedAssetAPIResponse();
        expectedResponse.setRelatedHTTPCode(200);
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);

        try {
            GovernedAsset result = governanceEngineImpl.getGovernedAsset(defaultUserId, defaultGUID);
        } catch (Exception e) { };

        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        //TODO: Verify the content in the list. tbd.
    }

    @Test
    @DisplayName("getGovernanceClassificationDef - null userid")
    void testGetGovernanceClassificationDefinitionNullUserid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            GovernanceClassificationDef result = governanceEngineImpl.getGovernanceClassificationDef(null, defaultGUID);
        });

        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-003"));
    }

    @Test
    @DisplayName("getGovernanceClassificationDef - empty userid")
    void testGetGovernanceClassificationDefinitionEmptyUserid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            GovernanceClassificationDef result = governanceEngineImpl.getGovernanceClassificationDef("", defaultGUID);
        });

        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-003"));

    }

    @Test
    @DisplayName("getGovernanceClassificationDef - null guid")
    void testGetGovernanceClassificationDefinitionNullGuid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            GovernanceClassificationDef result = governanceEngineImpl.getGovernanceClassificationDef(defaultUserId, null);
        });

        // Check we do not call the backend handlers - should be checked locally
        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-004"));
    }

    @Test
    @DisplayName("getGovernanceClassificationDef - empty guid")
    void testGetGovernanceClassificationDefinitionEmptyGuid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            GovernanceClassificationDef result = governanceEngineImpl.getGovernanceClassificationDef(defaultUserId, "");
        });
        verify(restTemplate, never()).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-004"));
    }

    @Test
    @DisplayName("getGovernanceClassificationDef - client REST exception")
    void testGetGovernanceClassificationDefinitionClientAPIException() {
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenThrow(new ResourceAccessException("Test Exception from REST client error"));

        thrown = assertThrows(MetadataServerException.class, () ->
        {
            GovernanceClassificationDef result = governanceEngineImpl.getGovernanceClassificationDef(defaultUserId, defaultGUID);
        });

        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
          assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-503-002"));
    }

    @Test
    @DisplayName("getGovernanceClassificationDef - handlers side exception")
    void testGetGovernanceClassificationDefinitionServerException() {
        GovernanceClassificationDefAPIResponse expectedResponse = new GovernanceClassificationDefAPIResponse();
        expectedResponse.setRelatedHTTPCode(404);
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);

        try {
            GovernanceClassificationDef result = governanceEngineImpl.getGovernanceClassificationDef(defaultUserId, defaultGUID);
        } catch (Exception e) { };

        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
         // TODO Need to validate the response here
    }


    // Now check a good return
    @Test
    @DisplayName("getGovernedAsset - good result")
    void testGetGovernanceClassificationDefinitionGoodData() {
        GovernanceClassificationDefAPIResponse expectedResponse = new GovernanceClassificationDefAPIResponse();
        expectedResponse.setRelatedHTTPCode(200);
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);

        try {
            GovernanceClassificationDef result = governanceEngineImpl.getGovernanceClassificationDef(defaultUserId, defaultGUID);
        } catch (Exception e) { };

        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        //TODO: Verify the content in the list. tbd.
    }

    //TODO Add Tests for other backend exceptions (rootclassificationnotfound, rootassettypenotfound,usernotauthorized)
}

