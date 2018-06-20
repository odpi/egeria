package org.odpi.openmetadata.accessservices.governanceengine.client;


import org.junit.Assert;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.MetadataServerException;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.RootClassificationNotFoundException;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.GovernanceClassificationDefinition;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.GovernedAssetComponent;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.SuiteDisplayName;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuiteDisplayName("Governance Engine Client Implementation")

// Mockito configuration
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)

public class GovernanceEngineImplTest {

    // Dependency used for REST calls in Governance Engine Client - we want a mocked verion of this
    // to avoid calling the back end!
    @Mock
    private RestTemplate restTemplate;

    // Mock for the constructor parameter (alternative - add setter to implementation)
    // @Mock (name="newServerURL")
    // private String serverURLmock;

    // If only that works -- but it doesn't as we can't mock a non-default constructor that takes string
    // as an argument (we can with other types) so

    static final String defaultOMASServerURL = "http://localhost:12345";
    static final String defaultUserId = "zebra91";
    //private class GovernanceEngineImplWithDefaultConstructor extends GovernanceEngineImpl
    //{
     //   public GovernanceEngineImplWithDefaultConstructor()
    //    {
    //        super(defaultOMASServerURL);
    //    }
    //}

    // Now inject into our test class (note we don't use this in our constructor tests below)
    @InjectMocks
    private GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl(defaultOMASServerURL);

    // For exception test cases
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
    @DisplayName("Check null userid")
    void testGetGovernedAssetComponentListNullParmsUser() {

        GovernanceEngineImpl governanceEngineImplalt = new GovernanceEngineImpl(defaultOMASServerURL);

        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            List<GovernedAssetComponent> result = governanceEngineImplalt.getGovernedAssetComponentList("", "rootClassificationType", "rootType");
            // exception!
        });
    }


    // -- Beginning of tests that need mocking

    @Test
    @DisplayName("Check client REST exception")
    void testGetGovernedAssetComponentListClientAPIException() {



        // Whatever we call this with throw a RTE (simulates a client REST API errr)
        // ResourceAccessException (spring) is thrown if, for example, the client cannot
        // connect to the endpoint

        // The argument matchers types need to match exactly - any is used for varargs...
        when (restTemplate.getForObject(ArgumentMatchers.anyString(),ArgumentMatchers.any(Class.class),ArgumentMatchers.<Object>any())).thenThrow(new ResourceAccessException("error"));

        // test we get the exception expected
        thrown = assertThrows(MetadataServerException.class, () ->
        {

            List<GovernedAssetComponent> result = governanceEngineImpl.getGovernedAssetComponentList(defaultUserId, "rootClassificationType", "rootType");
        });

        // verify we actually used the mocked rest template once
        verify(restTemplate,times(1)).getForObject(ArgumentMatchers.anyString(),ArgumentMatchers.any(Class.class),ArgumentMatchers.<Object>any());
        // assert (in addition to exception assertion!)

        // Checking we at least get a sensible GE OMAS Errorcode (we won't go
        // too specific
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-503"));
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
