/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
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
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.ClassificationNotFoundException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.GuidNotFoundException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.MetadataServerException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.TypeNotFoundException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAsset;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAssetAPIResponse;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAssetListAPIResponse;
import org.slf4j.Logger;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuiteDisplayName("Governance Engine Client Implementation")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class GovernanceEngineImplTest {

    static final String defaultOMASServerURL = "http://localhost:12345";
    static final String defaultServerName = "TestServer";
    static final String defaultUserId = "zebra91";
    static final String defaultClassificationType = "interestingClassificationType";
    static final String defaultRootType = "interestingType";
    static final String defaultGUID = "c7184523-7ca5-4876-9210-fe1bb1b55cd7";
    @Mock
    private RestTemplate restTemplate; // from Spring, used for REST API calls to handlers in implementation under test
    @Mock
    private Logger log;
    /*
     * Class under test requiring mock injection - note - not used in tests of constructor
     */
    @InjectMocks
    private GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl(defaultServerName, defaultOMASServerURL);

    private Throwable thrown = assertThrows(InvalidParameterException.class, () ->
    {
        GovernedAsset result = governanceEngineImpl.getGovernedAsset("", defaultGUID);
    }); // for testing exceptions

    @Test
    @DisplayName("GovernanceEngineImpl Constructor - Check empty handlers URL")
    void testGetGovernedAssetComponentListBadConstructorEmpty() {
        GovernanceEngineImpl governanceEngineImplalt = new GovernanceEngineImpl(defaultServerName, ""); // local

        // Mockito assertion for exception that would be thrown after using a poor constructor parameter - actual method is arbitrary
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

        GovernanceEngineImpl governanceEngineImplalt = new GovernanceEngineImpl(defaultServerName, null);
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
        } catch (InvalidParameterException | TypeNotFoundException | ClassificationNotFoundException | MetadataServerException | UserNotAuthorizedException e) {
            log.error("Unable to fetch the governed assets list");
        }

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
        } catch (InvalidParameterException | TypeNotFoundException | ClassificationNotFoundException | MetadataServerException | UserNotAuthorizedException e) {
            log.error("Unable to fetch the governed assets list");
        }

        // verify we actually used the mocked rest template once
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
        } catch (InvalidParameterException | GuidNotFoundException | MetadataServerException | UserNotAuthorizedException e) {
            log.debug("unable to fetch the governed asset");
        }

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
        } catch (InvalidParameterException | GuidNotFoundException | MetadataServerException | UserNotAuthorizedException e) {
            log.debug("unable to fetch the governed asset");
        }

        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
        //TODO: Verify the content in the list. tbd.
    }
}

