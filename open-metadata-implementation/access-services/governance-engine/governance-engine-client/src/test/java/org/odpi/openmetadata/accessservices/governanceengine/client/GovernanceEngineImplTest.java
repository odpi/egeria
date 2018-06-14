package org.odpi.openmetadata.accessservices.governanceengine.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.RunWith;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.GovernanceClassificationDefinition;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.GovernedAssetComponent;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SuiteDisplayName("Governance Engine Client Implementation")
public class GovernanceEngineImplTest {

    private Throwable thrown;


    // Do not initialize implementation class here as we want to check with different constructors

    // Test with a bad constructor - empty URL - just do this for one method
    @Test
    @DisplayName("Check empty server URL")
     void testGetGovernedAssetComponentListBadConstructorEmpty() {

        GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl("");

        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            List<GovernedAssetComponent> result = governanceEngineImpl.getGovernedAssetComponentList("", "rootClassificationType", "rootType");
            // exception!
        });

    }

    @Test
    @DisplayName("Check null server URL")
    void testGetGovernedAssetComponentListBadConstructorNull() {
        GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl(null);

        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            List<GovernedAssetComponent> result = governanceEngineImpl.getGovernedAssetComponentList("", "rootClassificationType", "rootType");
            // exception!
        });
    }


    // Tests for GetGovernedAssetComponents
    // TODO: Can we test for messages in the exception?
    @Test
    @DisplayName("Check null userid")
    void testGetGovernedAssetComponentListNullParmsUser() {
        GovernanceEngineImpl governanceEngineImpl = new GovernanceEngineImpl("http://localhost:12345");

        thrown = assertThrows(InvalidParameterException.class, () -> //TODO check exception
        {
            List<GovernedAssetComponent> result = governanceEngineImpl.getGovernedAssetComponentList("", "rootClassificationType", "rootType");
            // exception!
        });
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