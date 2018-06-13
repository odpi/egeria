package org.odpi.openmetadata.accessservices.governanceengine.client;

import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.GovernanceClassificationDefinition;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.GovernedAssetComponent;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class GovernanceEngineImplTest {

    // Do not initialize implementation class here as we want to check with different constructors

    // Test with a bad constructor - empty URL - just do this for one method
    @Test (expectedExceptions = InvalidParameterException.class)
    public void testGetGovernedAssetComponentListBadConstructorEmpty() throws Exception {
        GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl("");

        List<GovernedAssetComponent> result = governanceEngineImpl.getGovernedAssetComponentList("", "rootClassificationType", "rootType");
        // exception!
    }

    @Test (expectedExceptions = InvalidParameterException.class)
    public void testGetGovernedAssetComponentListBadConstructorNull() throws Exception {
        GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl(null);

        List<GovernedAssetComponent> result = governanceEngineImpl.getGovernedAssetComponentList("", "rootClassificationType", "rootType");
        // exception!
    }


    // Tests for GetGoverenedAssetComponents
    // TODO: Can we test for messages in the exception?
    @Test (expectedExceptions = InvalidParameterException.class)
    public void testGetGovernedAssetComponentListNullParmsUser() throws Exception {
        
        GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl("http://localhost:12345");

        List<GovernedAssetComponent> result = governanceEngineImpl.getGovernedAssetComponentList("", "rootClassificationType", "rootType");
        // exception!
    }

    @Test (expectedExceptions = InvalidParameterException.class)
    public void testGetGovernedAssetComponentListNullParmsRootClassification() throws Exception {
        GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl("http://localhost:12345");

        List<GovernedAssetComponent> result = governanceEngineImpl.getGovernedAssetComponentList("userId", "", "rootType");
        Assert.assertEquals(result, Arrays.<GovernedAssetComponent>asList(new GovernedAssetComponent()));
    }

    @Test (expectedExceptions = InvalidParameterException.class)
    public void testGetGovernedAssetComponentListNullParmsRootType() throws Exception {
        GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl("http://localhost:12345");

        List<GovernedAssetComponent> result = governanceEngineImpl.getGovernedAssetComponentList("userId", "", "");
        Assert.assertEquals(result, Arrays.<GovernedAssetComponent>asList(new GovernedAssetComponent()));
    }


    // - End of tests on GetGovernedAssetComponent

    // Tests on GetGovernanceClassificationDefinitionList

    @Test (expectedExceptions = InvalidParameterException.class)
    public void testGetGovernanceClassificationDefinitionListNullParmsUserid() throws Exception {
        GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl("http://localhost:12345");

        List<GovernanceClassificationDefinition> result = governanceEngineImpl.getGovernanceClassificationDefinitionList("", "rootClassificationType");
        Assert.assertEquals(result, Arrays.<GovernanceClassificationDefinition>asList(new GovernanceClassificationDefinition()));
    }

    @Test (expectedExceptions = InvalidParameterException.class)
    public void testGetGovernanceClassificationDefinitionListNullParmsRoot() throws Exception {
        GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl("http://localhost:12345");

        List<GovernanceClassificationDefinition> result = governanceEngineImpl.getGovernanceClassificationDefinitionList("userid", "");
        Assert.assertEquals(result, Arrays.<GovernanceClassificationDefinition>asList(new GovernanceClassificationDefinition()));
    }

    // End

    // Tests on GetGoverenedAssetComponent


    @Test (expectedExceptions = InvalidParameterException.class)
    public void testGetGovernedAssetComponentNullParmsUserid() throws Exception{
        GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl("http://localhost:12345");

        GovernedAssetComponent result = governanceEngineImpl.getGovernedAssetComponent("", "assetGuid");
        Assert.assertEquals(result, new GovernedAssetComponent());
    }

    @Test (expectedExceptions = InvalidParameterException.class)
    public void testGetGovernedAssetComponentNullParmsGuid() throws Exception{
        GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl("http://localhost:12345");

        GovernedAssetComponent result = governanceEngineImpl.getGovernedAssetComponent("userId", "");
        Assert.assertEquals(result, new GovernedAssetComponent());
    }


    @Test (expectedExceptions = InvalidParameterException.class)
    public void testGetGovernanceClassificationDefinitionNullParmsUserid() throws Exception {
        GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl("http://localhost:12345");

        GovernanceClassificationDefinition result = governanceEngineImpl.getGovernanceClassificationDefinition("", "tagGuid");
        Assert.assertEquals(result, new GovernanceClassificationDefinition());
    }

    @Test (expectedExceptions = InvalidParameterException.class)
    public void testGetGovernanceClassificationDefinitionNullParmsGuid() throws Exception {
        GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl("http://localhost:12345");

        GovernanceClassificationDefinition result = governanceEngineImpl.getGovernanceClassificationDefinition("userId", "");
        Assert.assertEquals(result, new GovernanceClassificationDefinition());
    }



}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme