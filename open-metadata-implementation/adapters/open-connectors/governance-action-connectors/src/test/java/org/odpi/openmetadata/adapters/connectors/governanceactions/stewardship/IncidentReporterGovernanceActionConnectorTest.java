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
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
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
 * IncidentReporterGovernanceActionConnectorTest runs the Incident Reporter Governance Action Service over a mocked
 * governance context and checks the incident report it creates and the guards it produces.
 */
public class IncidentReporterGovernanceActionConnectorTest
{
    private static final String TEMPLATE_GUID          = "template-guid";
    private static final String REPORT_GUID            = "report-guid";
    private static final String SERVICE_GUID           = "service-guid";
    private static final String ENGINE_ACTION_GUID     = "engine-action-guid";
    private static final String TEMPLATE_GUID_PROPERTY = IncidentReporterRequestParameter.TEMPLATE_GUID.getName();

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
        when(openMetadataStore.createMetadataElementFromTemplate(any(), any(), anyBoolean(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean())).thenReturn(REPORT_GUID);

        configurationProperties = new HashMap<>();
    }


    /**
     * The report is created from the template with the request parameters as placeholder values.  It has a generated
     * unique name and the service as its originator.  Action targets are not needed to report an incident.
     *
     * @throws Exception not expected
     */
    @Test public void testReportCreatedFromTemplate() throws Exception
    {
        Map<String, String> requestParameters = Map.of(TEMPLATE_GUID_PROPERTY, TEMPLATE_GUID, "background", "Missing file");

        when(governanceContext.getRequestParameters()).thenReturn(requestParameters);

        this.runService();

        ArgumentCaptor<ElementProperties> replacementProperties = ArgumentCaptor.forClass(ElementProperties.class);

        verify(openMetadataStore).createMetadataElementFromTemplate(eq(OpenMetadataType.INCIDENT_REPORT.typeName),
                                                                    isNull(),
                                                                    eq(true),
                                                                    isNull(),
                                                                    isNull(),
                                                                    isNull(),
                                                                    eq(TEMPLATE_GUID),
                                                                    replacementProperties.capture(),
                                                                    isNull(),
                                                                    eq(requestParameters),
                                                                    eq(SERVICE_GUID),
                                                                    eq(OpenMetadataType.REPORT_ORIGINATOR_RELATIONSHIP.typeName),
                                                                    isNull(),
                                                                    eq(true));

        assertEquals(propertyHelper.getStringProperty("test", OpenMetadataProperty.QUALIFIED_NAME.name, replacementProperties.getValue(), "test"),
                     "<Unknown>::" + ENGINE_ACTION_GUID);

        this.verifyReportReturned();
    }


    /**
     * The template can be configured in the configuration properties.
     *
     * @throws Exception not expected
     */
    @Test public void testTemplateFromConfigurationProperties() throws Exception
    {
        configurationProperties.put(TEMPLATE_GUID_PROPERTY, TEMPLATE_GUID);

        this.runService();

        verify(openMetadataStore).createMetadataElementFromTemplate(any(), any(), anyBoolean(), any(), any(), any(), eq(TEMPLATE_GUID), any(), any(), any(), any(), any(), any(), anyBoolean());

        this.verifyReportReturned();
    }


    /**
     * Each Referenceable action target is an impacted resource.  Any other element can not be linked to the report.
     *
     * @throws Exception not expected
     */
    @Test public void testImpactedResources() throws Exception
    {
        configurationProperties.put(TEMPLATE_GUID_PROPERTY, TEMPLATE_GUID);

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget("asset-guid", OpenMetadataType.ASSET.typeName, OpenMetadataType.REFERENCEABLE.typeName),
                                                                             this.actionTarget("term-guid", OpenMetadataType.REFERENCEABLE.typeName),
                                                                             this.actionTarget("other-guid", OpenMetadataType.OPEN_METADATA_ROOT.typeName)));

        this.runService();

        verify(openMetadataStore).createRelatedElementsInStore(OpenMetadataType.IMPACTED_RESOURCE_RELATIONSHIP.typeName, "asset-guid", REPORT_GUID, null, null, null);
        verify(openMetadataStore).createRelatedElementsInStore(OpenMetadataType.IMPACTED_RESOURCE_RELATIONSHIP.typeName, "term-guid", REPORT_GUID, null, null, null);
        verify(openMetadataStore, never()).createRelatedElementsInStore(OpenMetadataType.IMPACTED_RESOURCE_RELATIONSHIP.typeName, "other-guid", REPORT_GUID, null, null, null);

        this.verifyReportReturned();
    }


    /**
     * The report can not be created without a template.  A blank value counts as missing.
     *
     * @throws Exception not expected
     */
    @Test public void testMissingTemplate() throws Exception
    {
        when(governanceContext.getRequestParameters()).thenReturn(Map.of(TEMPLATE_GUID_PROPERTY, " "));

        this.runService();

        verify(openMetadataStore, never()).createMetadataElementFromTemplate(any(), any(), anyBoolean(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean());
        verify(governanceContext).recordCompletionStatus(eq(IncidentReporterGuard.MISSING_TEMPLATE.getCompletionStatus()),
                                                         eq(List.of(IncidentReporterGuard.MISSING_TEMPLATE.getName())),
                                                         isNull(),
                                                         isNull(),
                                                         nullable(AuditLogMessageDefinition.class));
    }


    /**
     * A problem with the template, such as one that is not an incident report, is reported as an exception.
     *
     * @throws Exception not expected
     */
    @Test public void testInvalidTemplate() throws Exception
    {
        configurationProperties.put(TEMPLATE_GUID_PROPERTY, TEMPLATE_GUID);

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

        IncidentReporterGovernanceActionConnector connector = new IncidentReporterGovernanceActionConnector();

        connector.initialize("test-instance", connection);
        connector.setGovernanceContext(governanceContext);
        connector.start();
    }


    /**
     * Check that the service completed successfully and passed the report on as an action target.
     *
     * @throws Exception not expected
     */
    private void verifyReportReturned() throws Exception
    {
        ArgumentCaptor<List<NewActionTarget>> newActionTargets = ArgumentCaptor.captor();

        verify(governanceContext).recordCompletionStatus(eq(IncidentReporterGuard.INCIDENT_REPORTED.getCompletionStatus()),
                                                         eq(List.of(IncidentReporterGuard.INCIDENT_REPORTED.getName())),
                                                         isNull(),
                                                         newActionTargets.capture(),
                                                         nullable(AuditLogMessageDefinition.class));

        assertEquals(newActionTargets.getValue().size(), 1);
        assertEquals(newActionTargets.getValue().get(0).getActionTargetGUID(), REPORT_GUID);
        assertEquals(newActionTargets.getValue().get(0).getActionTargetName(), ActionTarget.NEW_ASSET.getName());
    }


    /**
     * Return an action target for an element of the requested type.
     *
     * @param guid unique identifier of the element
     * @param typeName type of the element
     * @param superTypeNames names of the super types of the element
     * @return action target
     */
    private ActionTargetElement actionTarget(String    guid,
                                             String    typeName,
                                             String... superTypeNames)
    {
        ElementType elementType = new ElementType();
        elementType.setTypeName(typeName);
        elementType.setSuperTypeNames(List.of(superTypeNames));

        OpenMetadataElement element = new OpenMetadataElement();
        element.setElementGUID(guid);
        element.setType(elementType);

        ActionTargetElement actionTarget = new ActionTargetElement();
        actionTarget.setActionTargetName("impactedResource");
        actionTarget.setActionTargetGUID(guid);
        actionTarget.setTargetElement(element);

        return actionTarget;
    }
}
