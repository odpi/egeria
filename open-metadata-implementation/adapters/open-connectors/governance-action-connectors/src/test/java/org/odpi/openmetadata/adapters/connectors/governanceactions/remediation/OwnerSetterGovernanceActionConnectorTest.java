/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.mockito.ArgumentCaptor;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * OwnerSetterGovernanceActionConnectorTest runs the Owner Setter Governance Action Service over a mocked governance
 * context and checks the Ownership classification it sets and the guards it produces.
 */
public class OwnerSetterGovernanceActionConnectorTest
{
    private static final String ASSET_GUID = "asset-guid";

    private final PropertyHelper propertyHelper = new PropertyHelper();

    private GovernanceActionContext governanceContext;
    private OpenMetadataStore       openMetadataStore;
    private Map<String, Object>     configurationProperties;


    /**
     * A mocked governance context with no request parameters or action targets.
     *
     * @throws Exception not expected
     */
    @BeforeMethod public void setUpContext() throws Exception
    {
        governanceContext = mock(GovernanceActionContext.class);
        openMetadataStore = mock(OpenMetadataStore.class);

        when(governanceContext.getOpenMetadataStore()).thenReturn(openMetadataStore);

        configurationProperties = new HashMap<>();
    }


    /**
     * The owner from the request parameters is set on a Referenceable action target.
     *
     * @throws Exception not expected
     */
    @Test public void testOwnerFromRequestParameters() throws Exception
    {
        when(governanceContext.getRequestParameters()).thenReturn(Map.of(OwnerSetterRequestParameter.OWNER.getName(), "erinoverview",
                                                                         OwnerSetterRequestParameter.OWNER_TYPE_NAME.getName(), "UserIdentity",
                                                                         OwnerSetterRequestParameter.OWNER_PROPERTY_NAME.getName(), "userId",
                                                                         OwnerSetterRequestParameter.USER_IDS.getName(), "erinoverview, peterprofile,"));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget(ASSET_GUID, OpenMetadataType.ASSET.typeName)));

        this.runService();

        NewElementProperties properties = this.classificationProperties();

        assertEquals(this.stringProperty(OpenMetadataProperty.OWNER.name, properties), "erinoverview");
        assertEquals(this.stringProperty(OpenMetadataProperty.OWNER_TYPE_NAME.name, properties), "UserIdentity");
        assertEquals(this.stringProperty(OpenMetadataProperty.OWNER_PROPERTY_NAME.name, properties), "userId");
        assertEquals(propertyHelper.getStringArrayProperty("test", OpenMetadataProperty.USER_IDS.name, properties, "test"),
                     List.of("erinoverview", "peterprofile"));

        this.verifyCompletion(OwnerSetterGuard.OWNER_ASSIGNED.getCompletionStatus(), OwnerSetterGuard.OWNER_ASSIGNED.getName());
    }


    /**
     * The configuration properties supply the values that the request parameters do not override.
     *
     * @throws Exception not expected
     */
    @Test public void testRequestParametersOverrideConfigurationProperties() throws Exception
    {
        configurationProperties.put(OwnerSetterRequestParameter.OWNER.getName(), "garygeeke");
        configurationProperties.put(OwnerSetterRequestParameter.OWNER_TYPE_NAME.getName(), "UserIdentity");
        configurationProperties.put(OwnerSetterRequestParameter.USER_IDS.getName(), List.of("garygeeke", "erinoverview"));

        when(governanceContext.getRequestParameters()).thenReturn(Map.of(OwnerSetterRequestParameter.OWNER.getName(), "peterprofile"));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget(ASSET_GUID, OpenMetadataType.ASSET.typeName)));

        this.runService();

        NewElementProperties properties = this.classificationProperties();

        assertEquals(this.stringProperty(OpenMetadataProperty.OWNER.name, properties), "peterprofile");
        assertEquals(this.stringProperty(OpenMetadataProperty.OWNER_TYPE_NAME.name, properties), "UserIdentity");
        assertNull(this.stringProperty(OpenMetadataProperty.OWNER_PROPERTY_NAME.name, properties));
        assertEquals(propertyHelper.getStringArrayProperty("test", OpenMetadataProperty.USER_IDS.name, properties, "test"),
                     List.of("garygeeke", "erinoverview"));

        this.verifyCompletion(OwnerSetterGuard.OWNER_ASSIGNED.getCompletionStatus(), OwnerSetterGuard.OWNER_ASSIGNED.getName());
    }


    /**
     * Every Referenceable action target is classified, and any other element is skipped.
     *
     * @throws Exception not expected
     */
    @Test public void testOnlyReferenceablesClassified() throws Exception
    {
        when(governanceContext.getRequestParameters()).thenReturn(Map.of(OwnerSetterRequestParameter.OWNER.getName(), "erinoverview"));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget(ASSET_GUID, OpenMetadataType.ASSET.typeName),
                                                                             this.actionTarget("term-guid", OpenMetadataType.GLOSSARY_TERM.typeName),
                                                                             this.actionTarget("other-guid", null)));

        this.runService();

        verify(openMetadataStore).classifyMetadataElementInStore(eq(ASSET_GUID), eq(OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName), any(), any(NewElementProperties.class));
        verify(openMetadataStore).classifyMetadataElementInStore(eq("term-guid"), eq(OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName), any(), any(NewElementProperties.class));
        verify(openMetadataStore, times(2)).classifyMetadataElementInStore(anyString(), anyString(), any(), any(NewElementProperties.class));

        this.verifyCompletion(OwnerSetterGuard.OWNER_ASSIGNED.getCompletionStatus(), OwnerSetterGuard.OWNER_ASSIGNED.getName());
    }


    /**
     * Without an owner nothing is changed and the service ends as invalid.
     *
     * @throws Exception not expected
     */
    @Test public void testMissingOwner() throws Exception
    {
        when(governanceContext.getRequestParameters()).thenReturn(Map.of(OwnerSetterRequestParameter.OWNER.getName(), "  ",
                                                                         OwnerSetterRequestParameter.OWNER_TYPE_NAME.getName(), "UserIdentity"));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget(ASSET_GUID, OpenMetadataType.ASSET.typeName)));

        this.runService();

        verify(openMetadataStore, never()).classifyMetadataElementInStore(anyString(), anyString(), any(), any(NewElementProperties.class));
        this.verifyCompletion(CompletionStatus.INVALID, OwnerSetterGuard.MISSING_OWNER.getName());
    }


    /**
     * Without action targets there is nothing to classify.
     *
     * @throws Exception not expected
     */
    @Test public void testNoTargets() throws Exception
    {
        configurationProperties.put(OwnerSetterRequestParameter.OWNER.getName(), "erinoverview");

        when(governanceContext.getActionTargetElements()).thenReturn(List.of());

        this.runService();

        verify(openMetadataStore, never()).classifyMetadataElementInStore(anyString(), anyString(), any(), any(NewElementProperties.class));
        this.verifyCompletion(CompletionStatus.INVALID, OwnerSetterGuard.NO_TARGETS.getName());
    }


    /**
     * Action targets that cannot carry the Ownership classification are reported.
     *
     * @throws Exception not expected
     */
    @Test public void testNoReferenceableTargets() throws Exception
    {
        configurationProperties.put(OwnerSetterRequestParameter.OWNER.getName(), "erinoverview");

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget("other-guid", OpenMetadataType.OPEN_METADATA_ROOT.typeName)));

        this.runService();

        verify(openMetadataStore, never()).classifyMetadataElementInStore(anyString(), anyString(), any(), any(NewElementProperties.class));
        this.verifyCompletion(CompletionStatus.INVALID, OwnerSetterGuard.INVALID_ACTION_TARGET_TYPE.getName());
    }


    /**
     * Run the service with the test's configuration properties.
     *
     * @throws Exception not expected
     */
    private void runService() throws Exception
    {
        Connection connection = new Connection();
        connection.setConfigurationProperties(configurationProperties);

        OwnerSetterGovernanceActionConnector connector = new OwnerSetterGovernanceActionConnector();

        connector.initialize("test-instance", connection);
        connector.setGovernanceContext(governanceContext);
        connector.start();
    }


    /**
     * Return the properties of the single Ownership classification that was set.
     *
     * @return classification properties
     * @throws Exception not expected
     */
    private NewElementProperties classificationProperties() throws Exception
    {
        ArgumentCaptor<NewElementProperties> captor = ArgumentCaptor.forClass(NewElementProperties.class);

        verify(openMetadataStore).classifyMetadataElementInStore(eq(ASSET_GUID),
                                                                 eq(OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName),
                                                                 any(),
                                                                 captor.capture());
        return captor.getValue();
    }


    /**
     * Check the completion status and guard that the service recorded.
     *
     * @param completionStatus expected completion status
     * @param guard expected guard
     * @throws Exception not expected
     */
    private void verifyCompletion(CompletionStatus completionStatus,
                                  String           guard) throws Exception
    {
        verify(governanceContext).recordCompletionStatus(eq(completionStatus),
                                                         eq(List.of(guard)),
                                                         isNull(),
                                                         isNull(),
                                                         nullable(AuditLogMessageDefinition.class));
    }


    /**
     * Return a string property.
     *
     * @param propertyName name of the property
     * @param properties properties to look in
     * @return value or null
     */
    private String stringProperty(String               propertyName,
                                  NewElementProperties properties)
    {
        return propertyHelper.getStringProperty("test", propertyName, properties, "test");
    }


    /**
     * Return an action target for an element of the requested type.
     *
     * @param guid unique identifier of the element
     * @param typeName type of the element - Asset and GlossaryTerm elements are given their supertypes
     * @return action target
     */
    private ActionTargetElement actionTarget(String guid,
                                             String typeName)
    {
        ElementType elementType = new ElementType();
        elementType.setTypeName(typeName);

        if ((OpenMetadataType.ASSET.typeName.equals(typeName)) || (OpenMetadataType.GLOSSARY_TERM.typeName.equals(typeName)))
        {
            elementType.setSuperTypeNames(List.of(OpenMetadataType.REFERENCEABLE.typeName, OpenMetadataType.OPEN_METADATA_ROOT.typeName));
        }

        OpenMetadataElement element = new OpenMetadataElement();
        element.setElementGUID(guid);
        element.setType(elementType);

        ActionTargetElement actionTarget = new ActionTargetElement();
        actionTarget.setActionTargetName("newAsset");
        actionTarget.setActionTargetGUID(guid);
        actionTarget.setTargetElement(element);

        return actionTarget;
    }
}
