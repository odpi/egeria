package org.odpi.openmetadata.accessservices.governanceengine.client;


import org.junit.Assert;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.MetadataServerException;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.RootClassificationNotFoundException;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.*;

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
import static org.mockito.Mockito.*;

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
    static final String defaultClassificationType = "interestingClassificationType";
    static final String defaultRootType = "interestingType";
    static final String defaultGUID = "c7184523-7ca5-4876-9210-fe1bb1b55cd7";
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

    // --> Constructor tests (we pick one method only)
    @Test
    @DisplayName("GovernanceEngineImpl Constructor - Check empty server URL")
     void testGetGovernedAssetComponentListBadConstructorEmpty() {

        GovernanceEngineImpl governanceEngineImplalt = new GovernanceEngineImpl("");

        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            List<GovernedAssetComponent> result = governanceEngineImplalt.getGovernedAssetComponentList("", "rootClassificationType", "rootType");
            // exception!
        });

    }

    @Test
    @DisplayName("GovernanceEngineImp Constructor - Check null server URL")
    void testGetGovernedAssetComponentListBadConstructorNull() {
        GovernanceEngineImpl governanceEngineImplalt = new GovernanceEngineImpl(null);

        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            List<GovernedAssetComponent> result = governanceEngineImplalt.getGovernedAssetComponentList("", "rootClassificationType", "rootType");
            // exception!
        });
    }

    // -> getGovernedAssetComponentList
    @Test
    @DisplayName("getGovernedAssetComponentList - null userid")
    void testGetGovernedAssetComponentListNullUserid() {

        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            List<GovernedAssetComponent> result = governanceEngineImpl.getGovernedAssetComponentList(null, defaultClassificationType, defaultRootType);
            // exception!
        });

        // Check we do not call the backend server - should be checked locally
        verify(restTemplate,never()).getForObject(ArgumentMatchers.anyString(),ArgumentMatchers.any(Class.class),ArgumentMatchers.<Object>any());
    }

    @Test
    @DisplayName("getGovernedAssetComponentList - empty userid")
    void testGetGovernedAssetComponentListEmptyUserid() {

        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            List<GovernedAssetComponent> result = governanceEngineImpl.getGovernedAssetComponentList("", defaultClassificationType, defaultRootType);
            // exception!
        });
        // Check we do not call the backend server - should be checked locally
        verify(restTemplate,never()).getForObject(ArgumentMatchers.anyString(),ArgumentMatchers.any(Class.class),ArgumentMatchers.<Object>any());
        // TODO - verify we get the correct error code. Currently hitting error with lambda expression
        //assertEquals(result.get);
    }






    // -- Beginning of tests that need mocking

    @Test
    @DisplayName("getGovernedAssetComponentList - client REST exception")
    void testGetGovernedAssetComponentListClientAPIException() {



        // Whatever we call this with throw a RTE (simulates a client REST API errr)
        // ResourceAccessException (spring) is thrown if, for example, the client cannot
        // connect to the endpoint

        // The argument matchers types need to match exactly - any is used for varargs...

        // For this generic API issue we won't be specific on the parms
        when (restTemplate.getForObject(ArgumentMatchers.anyString(),ArgumentMatchers.any(Class.class),ArgumentMatchers.<Object>any())).thenThrow(new ResourceAccessException("Test Exception from REST client error"));

        // test we get the exception expected
        thrown = assertThrows(MetadataServerException.class, () ->
        {

            List<GovernedAssetComponent> result = governanceEngineImpl.getGovernedAssetComponentList(defaultUserId, defaultClassificationType, defaultRootType);
        });

        // verify we actually used the mocked rest template once
        verify(restTemplate,times(1)).getForObject(ArgumentMatchers.anyString(),ArgumentMatchers.any(Class.class),ArgumentMatchers.<Object>any());
        // assert (in addition to exception assertion!)

        // Checking we at least get a sensible GE OMAS Errorcode (we won't go
        // too specific
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-503"));
    }



    // This is an initial test of a server generated exception -- but testing the exact list is more a server testing
    // concern. However we will check for one other than a client rest api error, as the behaviour could be different in the client layer
    @Test
    @DisplayName("getGovernedAssetComponentList - server side exception")
    void testGetGovernedAssetComponentListRemoteInvalidParmException() {

        // Construct the response we want to emulate in the mocked library
        //GovernedAssetComponentAPIResponse expectedResponse = new GovernedAssetComponentAPIResponse();
        //expectedResponse.setRelatedHTTPCode();
        // For now rely on InvalidArgumentException when this occurs
        // The argument matchers types need to match exactly - any is used for varargs...
        GovernedAssetComponentListAPIResponse expectedResponse = new GovernedAssetComponentListAPIResponse();
        expectedResponse.setRelatedHTTPCode(404);
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);

        // test we get the exception expected
try {
        List<GovernedAssetComponent> result = governanceEngineImpl.getGovernedAssetComponentList(defaultUserId, "rootClassificationType", "rootType");
    } catch (Exception e) {};

        // verify we actually used the mocked rest template once
        verify(restTemplate,times(1)).getForObject(ArgumentMatchers.anyString(),ArgumentMatchers.any(Class.class),ArgumentMatchers.<Object>any());
        // assert (in addition to exception assertion!)

        // Checking we at least get a sensible GE OMAS Errorcode (we won't go
        // too specific
        // TODO Need to validate the response here
    }


    // Now check a good return
    @Test
    @DisplayName("getGovernedAssetComponentList - good result")
    void testGetGovernedAssetComponentListGoodData() {

        // Construct the response we want to emulate in the mocked library
        //GovernedAssetComponentAPIResponse expectedResponse = new GovernedAssetComponentAPIResponse();
        //expectedResponse.setRelatedHTTPCode();
        // For now rely on InvalidArgumentException when this occurs
        // The argument matchers types need to match exactly - any is used for varargs

        GovernedAssetComponentListAPIResponse expectedResponse = new GovernedAssetComponentListAPIResponse();
        expectedResponse.setRelatedHTTPCode(200);
        when (restTemplate.getForObject(ArgumentMatchers.anyString(),ArgumentMatchers.any(Class.class),ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);


try {
    List<GovernedAssetComponent> result = governanceEngineImpl.getGovernedAssetComponentList(defaultUserId, defaultClassificationType, defaultRootType);
} catch (Exception e) {};

        // verify we actually used the mocked rest template once
        verify(restTemplate,times(1)).getForObject(ArgumentMatchers.anyString(),ArgumentMatchers.any(Class.class),ArgumentMatchers.<Object>any());

        //TODO: Verify the content in the list. tbd.
        // assert (in addition to exception assertion!)

        // Checking we at least get a sensible GE OMAS Errorcode (we won't go
        // too specific
    }

    // -> getGovernanceClassificationDefinitionList
    @Test
    @DisplayName("getGovernanceClassificationDefinitionList - null userid")
    void testGetGovernanceClassificationDefinitionListNullUserid() {

        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            List<GovernanceClassificationDefinition> result = governanceEngineImpl.getGovernanceClassificationDefinitionList(null, defaultClassificationType);
            // exception!
        });

        // Check we do not call the backend server - should be checked locally
        verify(restTemplate,never()).getForObject(ArgumentMatchers.anyString(),ArgumentMatchers.any(Class.class),ArgumentMatchers.<Object>any());
    }

    @Test
    @DisplayName("getGovernanceClassificationDefinitionList - empty userid")
    void testGetGovernanceClassificationDefinitionListEmptyUserid() {

        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            List<GovernanceClassificationDefinition> result = governanceEngineImpl.getGovernanceClassificationDefinitionList("", defaultClassificationType);
            // exception!
        });
        // Check we do not call the backend server - should be checked locally
        verify(restTemplate,never()).getForObject(ArgumentMatchers.anyString(),ArgumentMatchers.any(Class.class),ArgumentMatchers.<Object>any());
        // TODO - verify we get the correct error code. Currently hitting error with lambda expression
        //assertEquals(result.get);
    }






    // -- Beginning of tests that need mocking

    @Test
    @DisplayName("getGovernanceClassificationDefinitionList - client REST exception")
    void testGetGovernanceClassificationDefinitionListClientAPIException() {



        // Whatever we call this with throw a RTE (simulates a client REST API errr)
        // ResourceAccessException (spring) is thrown if, for example, the client cannot
        // connect to the endpoint

        // The argument matchers types need to match exactly - any is used for varargs...

        // For this generic API issue we won't be specific on the parms
        when (restTemplate.getForObject(ArgumentMatchers.anyString(),ArgumentMatchers.any(Class.class),ArgumentMatchers.<Object>any())).thenThrow(new ResourceAccessException("Test Exception from REST client error"));

        // test we get the exception expected
        thrown = assertThrows(MetadataServerException.class, () ->
        {

            List<GovernanceClassificationDefinition> result = governanceEngineImpl.getGovernanceClassificationDefinitionList(defaultUserId, defaultClassificationType);
        });

        // verify we actually used the mocked rest template once
        verify(restTemplate,times(1)).getForObject(ArgumentMatchers.anyString(),ArgumentMatchers.any(Class.class),ArgumentMatchers.<Object>any());
        // assert (in addition to exception assertion!)

        // Checking we at least get a sensible GE OMAS Errorcode (we won't go
        // too specific
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-503"));
    }



    // This is an initial test of a server generated exception -- but testing the exact list is more a server testing
    // concern. However we will check for one other than a client rest api error, as the behaviour could be different in the client layer
    @Test
    @DisplayName("getGovernanceClassificationDefinitionList - server side exception")
    void testGetGovernanceClassificationDefinitionList() {

        // Construct the response we want to emulate in the mocked library
        //GovernedAssetComponentAPIResponse expectedResponse = new GovernedAssetComponentAPIResponse();
        //expectedResponse.setRelatedHTTPCode();
        // For now rely on InvalidArgumentException when this occurs
        // The argument matchers types need to match exactly - any is used for varargs...
        GovernanceClassificationDefinitionListAPIResponse expectedResponse = new GovernanceClassificationDefinitionListAPIResponse();
        expectedResponse.setRelatedHTTPCode(404);
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);

        // test we get the exception expected
        try {
            List<GovernanceClassificationDefinition> result = governanceEngineImpl.getGovernanceClassificationDefinitionList(defaultUserId, "rootClassificationType");
        } catch (Exception e) {};

        // verify we actually used the mocked rest template once
        verify(restTemplate,times(1)).getForObject(ArgumentMatchers.anyString(),ArgumentMatchers.any(Class.class),ArgumentMatchers.<Object>any());
        // assert (in addition to exception assertion!)

        // Checking we at least get a sensible GE OMAS Errorcode (we won't go
        // too specific
        // TODO Need to validate the response here
    }


    // Now check a good return
    @Test
    @DisplayName("getGovernedAssetComponentList - good result")
    void testGetGovernanceClassificationDefinitionListGoodData() {

        // Construct the response we want to emulate in the mocked library
        //GovernedAssetComponentAPIResponse expectedResponse = new GovernedAssetComponentAPIResponse();
        //expectedResponse.setRelatedHTTPCode();
        // For now rely on InvalidArgumentException when this occurs
        // The argument matchers types need to match exactly - any is used for varargs

        GovernanceClassificationDefinitionListAPIResponse expectedResponse = new GovernanceClassificationDefinitionListAPIResponse();
        expectedResponse.setRelatedHTTPCode(200);
        when (restTemplate.getForObject(ArgumentMatchers.anyString(),ArgumentMatchers.any(Class.class),ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);


        try {
            List<GovernanceClassificationDefinition> result = governanceEngineImpl.getGovernanceClassificationDefinitionList(defaultUserId, defaultClassificationType);
        } catch (Exception e) {};

        // verify we actually used the mocked rest template once
        verify(restTemplate,times(1)).getForObject(ArgumentMatchers.anyString(),ArgumentMatchers.any(Class.class),ArgumentMatchers.<Object>any());

        //TODO: Verify the content in the list. tbd.
        // assert (in addition to exception assertion!)

        // Checking we at least get a sensible GE OMAS Errorcode (we won't go
        // too specific
    }

}

