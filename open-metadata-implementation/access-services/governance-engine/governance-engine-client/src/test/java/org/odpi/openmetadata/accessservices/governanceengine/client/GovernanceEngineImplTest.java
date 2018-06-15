package org.odpi.openmetadata.accessservices.governanceengine.client;


import org.junit.Assert;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.MetadataServerException;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.RootClassificationNotFoundException;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.GovernanceClassificationDefinition;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.GovernedAssetComponent;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.SuiteDisplayName;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SuiteDisplayName("Governance Engine Client Implementation")

// Mockito configuration
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)

public class GovernanceEngineImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GovernanceEngineImpl governanceEngineImpl;

    private Throwable thrown;


    // Do not initialize implementation class here as we want to check with different constructors

    // These first two tests use an alternative constructor - so we can test some exceptions
    // they will cause in future invocations. The other tests use one with Mocking Support

    @Test
    @DisplayName("Check empty server URL")
     void testGetGovernedAssetComponentListBadConstructorEmpty() {

        GovernanceEngineImpl governanceEngineImplalt = new GovernanceEngineImpl("");

        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            List<GovernedAssetComponent> result = governanceEngineImplalt.getGovernedAssetComponentList("", "rootClassificationType", "rootType");
            // exception!
        });

    }

    @Test
    @DisplayName("Check null server URL")
    void testGetGovernedAssetComponentListBadConstructorNull() {
        GovernanceEngineImpl governanceEngineImplalt = new GovernanceEngineImpl(null);

        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            List<GovernedAssetComponent> result = governanceEngineImplalt.getGovernedAssetComponentList("", "rootClassificationType", "rootType");
            // exception!
        });
    }


    @Test
    @DisplayName("Check client REST exception")
    void testGetGovernedAssetComponentListClientAPIException() {


        // Setup - Build the mocked return object
        GovernedAssetComponent mockedResult = new GovernedAssetComponent();

        // The client raises an exception if there's an issue with making the call - so this should be a generic rest api error
        when (restTemplate.getForObject(ArgumentMatchers.anyString(),ArgumentMatchers.any(Class.class),ArgumentMatchers.anyString())).thenThrow(new RuntimeException());

        // when
        thrown = assertThrows(InvalidParameterException.class, () ->
        {

            List<GovernedAssetComponent> result = governanceEngineImpl.getGovernedAssetComponentList("", "rootClassificationType", "rootType");
        });

        // then
        //assertEquals(mockedResult,result.get(0))
    }

    @Test
    @DisplayName("Check null userid")
    void testGetGovernedAssetComponentListNullParmsUser() {


        // Setup - Build the mocked return object
        GovernedAssetComponent mockedResult = new GovernedAssetComponent();
        thrown = assertThrows(InvalidParameterException.class, () ->
        {

        // when
            List<GovernedAssetComponent> result = governanceEngineImpl.getGovernedAssetComponentList("", "rootClassificationType", "rootType");
    });
        // then
            //assertEquals(mockedResult,result.get(0))
    }

    @Test
    void testGetGovernedAssetComponentListNullParmsRootClassification() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl("http://localhost:12345");

            List<GovernedAssetComponent> result = governanceEngineImpl.getGovernedAssetComponentList("userId", "", "rootType");
        });
    }

    @Test
    void testGetGovernedAssetComponentListNullParmsRootType() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl("http://localhost:12345");

            List<GovernedAssetComponent> result = governanceEngineImpl.getGovernedAssetComponentList("userId", "", "");
        });
    }


    // - End of tests on GetGovernedAssetComponent

    // Tests on GetGovernanceClassificationDefinitionList

    @Test
    void testGetGovernanceClassificationDefinitionListNullParmsUserid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl("http://localhost:12345");

            List<GovernanceClassificationDefinition> result = governanceEngineImpl.getGovernanceClassificationDefinitionList("", "rootClassificationType");
        });
    }

    @Test
    void testGetGovernanceClassificationDefinitionListNullParmsRoot() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl("http://localhost:12345");

            List<GovernanceClassificationDefinition> result = governanceEngineImpl.getGovernanceClassificationDefinitionList("userid", "");
        });
    }

    // End

    // Tests on GetGoverenedAssetComponent


    @Test
    void testGetGovernedAssetComponentNullParmsUserid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl("http://localhost:12345");

            GovernedAssetComponent result = governanceEngineImpl.getGovernedAssetComponent("", "assetGuid");
        });
    }

    @Test
    void testGetGovernedAssetComponentNullParmsGuid() {
        thrown = assertThrows(InvalidParameterException.class, () ->

        {
            GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl("http://localhost:12345");

            GovernedAssetComponent result = governanceEngineImpl.getGovernedAssetComponent("userId", "");
        });
    }


    @Test
    void testGetGovernanceClassificationDefinitionNullParmsUserid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl("http://localhost:12345");

            GovernanceClassificationDefinition result = governanceEngineImpl.getGovernanceClassificationDefinition("", "tagGuid");
        });
    }

    @Test
    void testGetGovernanceClassificationDefinitionNullParmsGuid() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl("http://localhost:12345");

            GovernanceClassificationDefinition result = governanceEngineImpl.getGovernanceClassificationDefinition("userId", "");
        });
    }


}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme