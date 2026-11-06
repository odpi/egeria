/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.mockito.ArgumentCaptor;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionContext;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.AssignmentType;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

/**
 * StewardInformerGovernanceActionConnectorTest runs the Steward Informer Governance Action Service over a mocked
 * governance context and checks the to do it creates and the guards it produces.
 */
public class StewardInformerGovernanceActionConnectorTest
{
    private static final String TEMPLATE_GUID          = "template-guid";
    private static final String STEWARD_GUID           = "steward-guid";
    private static final String SPONSOR_GUID           = "sponsor-guid";
    private static final String ASSET_GUID             = "asset-guid";
    private static final String TO_DO_GUID             = "to-do-guid";
    private static final String SERVICE_GUID           = "service-guid";
    private static final String ENGINE_ACTION_GUID     = "engine-action-guid";
    private static final String TEMPLATE_GUID_PROPERTY = StewardInformerRequestParameter.TEMPLATE_GUID.getName();

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
        when(governanceContext.getConnectorGUID()).thenReturn(SERVICE_GUID);
        when(governanceContext.getEngineActionGUID()).thenReturn(ENGINE_ACTION_GUID);
        when(openMetadataStore.createMetadataElementFromTemplate(any(), any(), anyBoolean(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean())).thenReturn(TO_DO_GUID);

        configurationProperties = new HashMap<>();
    }


    /**
     * The to do is created from the template with the request parameters as placeholder values.  It has a generated
     * unique name and is assigned to the steward.
     *
     * @throws Exception not expected
     */
    @Test public void testToDoCreatedFromTemplate() throws Exception
    {
        Map<String, String> requestParameters = Map.of(TEMPLATE_GUID_PROPERTY, TEMPLATE_GUID, "reason", "No owner");

        when(governanceContext.getRequestParameters()).thenReturn(requestParameters);
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget(ActionTarget.STEWARD.getName(), STEWARD_GUID)));

        this.runService();

        ArgumentCaptor<ElementProperties> replacementProperties = ArgumentCaptor.forClass(ElementProperties.class);

        verify(openMetadataStore).createMetadataElementFromTemplate(eq(OpenMetadataType.TO_DO.typeName),
                                                                    isNull(),
                                                                    eq(true),
                                                                    isNull(),
                                                                    isNull(),
                                                                    isNull(),
                                                                    eq(TEMPLATE_GUID),
                                                                    replacementProperties.capture(),
                                                                    isNull(),
                                                                    eq(requestParameters),
                                                                    eq(STEWARD_GUID),
                                                                    eq(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName),
                                                                    isNull(),
                                                                    eq(true));

        assertEquals(propertyHelper.getStringProperty("test", OpenMetadataProperty.QUALIFIED_NAME.name, replacementProperties.getValue(), "test"),
                     "<Unknown>::" + ENGINE_ACTION_GUID);

        verify(openMetadataStore).createRelatedElementsInStore(OpenMetadataType.ACTION_REQUESTER_RELATIONSHIP.typeName, SERVICE_GUID, TO_DO_GUID, null, null, null);

        this.verifyToDoReturned();
    }


    /**
     * The template can be configured in the configuration properties.
     *
     * @throws Exception not expected
     */
    @Test public void testTemplateFromConfigurationProperties() throws Exception
    {
        configurationProperties.put(TEMPLATE_GUID_PROPERTY, TEMPLATE_GUID);

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget(ActionTarget.STEWARD.getName(), STEWARD_GUID)));

        this.runService();

        verify(openMetadataStore).createMetadataElementFromTemplate(any(), any(), anyBoolean(), any(), any(), any(), eq(TEMPLATE_GUID), any(), any(), any(), any(), any(), any(), anyBoolean());

        this.verifyToDoReturned();
    }


    /**
     * The action targets guide the steward, keeping their names.  The steward and sponsor are not action targets of the to do.
     *
     * @throws Exception not expected
     */
    @Test public void testActionTargetsAttached() throws Exception
    {
        configurationProperties.put(TEMPLATE_GUID_PROPERTY, TEMPLATE_GUID);

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget(ActionTarget.STEWARD.getName(), STEWARD_GUID),
                                                                             this.actionTarget(ActionTarget.SPONSOR.getName(), SPONSOR_GUID),
                                                                             this.actionTarget("assetToFix", ASSET_GUID)));

        this.runService();

        ElementProperties actionTargetProperties = propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, "assetToFix");
        ElementProperties sponsorProperties      = propertyHelper.addStringProperty(null, OpenMetadataProperty.ASSIGNMENT_TYPE.name, AssignmentType.SPONSOR.getDisplayName());

        verify(openMetadataStore).createRelatedElementsInStore(OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName, TO_DO_GUID, ASSET_GUID, null, null, actionTargetProperties);
        verify(openMetadataStore).createRelatedElementsInStore(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName, SPONSOR_GUID, TO_DO_GUID, null, null, sponsorProperties);
        verify(openMetadataStore, never()).createRelatedElementsInStore(eq(OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName), any(), eq(STEWARD_GUID), any(), any(), any(ElementProperties.class));
        verify(openMetadataStore, never()).createRelatedElementsInStore(eq(OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName), any(), eq(SPONSOR_GUID), any(), any(), any(ElementProperties.class));

        this.verifyToDoReturned();
    }


    /**
     * The to do can not be created without a template.  A blank value counts as missing.
     *
     * @throws Exception not expected
     */
    @Test public void testMissingTemplate() throws Exception
    {
        when(governanceContext.getRequestParameters()).thenReturn(Map.of(TEMPLATE_GUID_PROPERTY, " "));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget(ActionTarget.STEWARD.getName(), STEWARD_GUID)));

        this.runService();

        this.verifyNothingCreated(StewardInformerGuard.MISSING_TEMPLATE);
    }


    /**
     * There is nobody to inform without a steward.
     *
     * @throws Exception not expected
     */
    @Test public void testMissingSteward() throws Exception
    {
        configurationProperties.put(TEMPLATE_GUID_PROPERTY, TEMPLATE_GUID);

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget("assetToFix", ASSET_GUID)));

        this.runService();

        this.verifyNothingCreated(StewardInformerGuard.MISSING_STEWARD);
    }


    /**
     * A problem with the template, such as one that is not a to do, is reported as an exception.
     *
     * @throws Exception not expected
     */
    @Test public void testInvalidTemplate() throws Exception
    {
        configurationProperties.put(TEMPLATE_GUID_PROPERTY, TEMPLATE_GUID);

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget(ActionTarget.STEWARD.getName(), STEWARD_GUID)));

        doThrow(mock(InvalidParameterException.class)).when(openMetadataStore).createMetadataElementFromTemplate(any(), any(), anyBoolean(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean());

        assertThrows(ConnectorCheckedException.class, this::runService);
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

        StewardInformerGovernanceActionConnector connector = new StewardInformerGovernanceActionConnector();

        connector.initialize("test-instance", connection);
        connector.setGovernanceContext(governanceContext);
        connector.start();
    }


    /**
     * Check that the service completed successfully and passed the to do on as an action target.
     *
     * @throws Exception not expected
     */
    private void verifyToDoReturned() throws Exception
    {
        ArgumentCaptor<List<NewActionTarget>> newActionTargets = ArgumentCaptor.captor();

        verify(governanceContext).recordCompletionStatus(eq(StewardInformerGuard.STEWARD_INFORMED.getCompletionStatus()),
                                                         eq(List.of(StewardInformerGuard.STEWARD_INFORMED.getName())),
                                                         isNull(),
                                                         newActionTargets.capture(),
                                                         nullable(AuditLogMessageDefinition.class));

        assertEquals(newActionTargets.getValue().size(), 1);
        assertEquals(newActionTargets.getValue().get(0).getActionTargetGUID(), TO_DO_GUID);
        assertEquals(newActionTargets.getValue().get(0).getActionTargetName(), ActionTarget.NEW_ASSET.getName());
    }


    /**
     * Check that the service stopped with the requested guard before creating anything.
     *
     * @param guard expected guard
     * @throws Exception not expected
     */
    private void verifyNothingCreated(StewardInformerGuard guard) throws Exception
    {
        verify(openMetadataStore, never()).createMetadataElementFromTemplate(any(), any(), anyBoolean(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean());
        verify(governanceContext).recordCompletionStatus(eq(guard.getCompletionStatus()),
                                                         eq(List.of(guard.getName())),
                                                         isNull(),
                                                         isNull(),
                                                         nullable(AuditLogMessageDefinition.class));
    }


    /**
     * Return an action target.
     *
     * @param name name of the action target
     * @param guid unique identifier of the element
     * @return action target
     */
    private ActionTargetElement actionTarget(String name,
                                             String guid)
    {
        ActionTargetElement actionTarget = new ActionTargetElement();

        actionTarget.setActionTargetName(name);
        actionTarget.setActionTargetGUID(guid);

        return actionTarget;
    }
}
