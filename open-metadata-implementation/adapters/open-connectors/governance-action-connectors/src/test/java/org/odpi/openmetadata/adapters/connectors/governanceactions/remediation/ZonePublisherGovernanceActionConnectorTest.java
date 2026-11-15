/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.mockito.ArgumentCaptor;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.GuardType;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

/**
 * ZonePublisherGovernanceActionConnectorTest runs the Zone Publisher Governance Action Service over a
 * mocked governance context and checks the zones it sets and the guards it produces.
 */
public class ZonePublisherGovernanceActionConnectorTest
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
     * The zones from the request parameter are set on the new asset.
     *
     * @throws Exception not expected
     */
    @Test public void testZonesFromRequestParameter() throws Exception
    {
        when(governanceContext.getRequestParameters()).thenReturn(Map.of(ZonePublisherRequestParameter.PUBLISH_ZONES.getName(), "data-lake, clinical-trials"));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.newAsset(ASSET_GUID)));

        this.runService();

        assertEquals(this.zoneMembership(), List.of("data-lake", "clinical-trials"));
        this.verifyCompletion(CompletionStatus.ACTIONED, ZonePublisherGuard.ZONE_ASSIGNED.getName());
    }


    /**
     * The zones in the configuration properties may be a comma separated list.
     *
     * @throws Exception not expected
     */
    @Test public void testZonesFromConfigurationPropertyString() throws Exception
    {
        configurationProperties.put(ZonePublisherRequestParameter.PUBLISH_ZONES.getName(), "zone1,zone2");

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.newAsset(ASSET_GUID)));

        this.runService();

        assertEquals(this.zoneMembership(), List.of("zone1", "zone2"));
        this.verifyCompletion(CompletionStatus.ACTIONED, ZonePublisherGuard.ZONE_ASSIGNED.getName());
    }


    /**
     * The zones in the configuration properties may be a list, which is how a JSON array arrives.
     *
     * @throws Exception not expected
     */
    @Test public void testZonesFromConfigurationPropertyList() throws Exception
    {
        configurationProperties.put(ZonePublisherRequestParameter.PUBLISH_ZONES.getName(), List.of("zone1", "zone2"));

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.newAsset(ASSET_GUID)));

        this.runService();

        assertEquals(this.zoneMembership(), List.of("zone1", "zone2"));
        this.verifyCompletion(CompletionStatus.ACTIONED, ZonePublisherGuard.ZONE_ASSIGNED.getName());
    }


    /**
     * The request parameter overrides the configuration property.
     *
     * @throws Exception not expected
     */
    @Test public void testRequestParameterOverridesConfigurationProperty() throws Exception
    {
        configurationProperties.put(ZonePublisherRequestParameter.PUBLISH_ZONES.getName(), List.of("zone1", "zone2"));

        when(governanceContext.getRequestParameters()).thenReturn(Map.of(ZonePublisherRequestParameter.PUBLISH_ZONES.getName(), "zone3"));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.newAsset(ASSET_GUID)));

        this.runService();

        assertEquals(this.zoneMembership(), List.of("zone3"));
        this.verifyCompletion(CompletionStatus.ACTIONED, ZonePublisherGuard.ZONE_ASSIGNED.getName());
    }


    /**
     * Only elements supplied as newAsset or "*" action targets are updated.
     *
     * @throws Exception not expected
     */
    @Test public void testOnlyNewAssetsUpdated() throws Exception
    {
        when(governanceContext.getRequestParameters()).thenReturn(Map.of(ZonePublisherRequestParameter.PUBLISH_ZONES.getName(), "zone1"));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.newAsset(ASSET_GUID),
                                                                             this.actionTarget("*", "any-guid"),
                                                                             this.actionTarget("sourceFile", "source-guid"),
                                                                             this.newAssetWithoutElement()));

        this.runService();

        verify(openMetadataStore).classifyMetadataElementInStore(eq(ASSET_GUID), eq(OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName), any(), any(NewElementProperties.class));
        verify(openMetadataStore).classifyMetadataElementInStore(eq("any-guid"), eq(OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName), any(), any(NewElementProperties.class));
        verify(openMetadataStore, times(2)).classifyMetadataElementInStore(anyString(), anyString(), any(), any(NewElementProperties.class));

        this.verifyCompletion(CompletionStatus.ACTIONED, ZonePublisherGuard.ZONE_ASSIGNED.getName());
    }


    /**
     * Without zones, the zone membership is removed from the new assets only.
     *
     * @throws Exception not expected
     */
    @Test public void testNoZones() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.newAsset(ASSET_GUID),
                                                                             this.actionTarget("sourceFile", "source-guid")));

        this.runService();

        verify(openMetadataStore).declassifyMetadataElementInStore(eq(ASSET_GUID), eq(OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName), any());
        verify(openMetadataStore, times(1)).declassifyMetadataElementInStore(anyString(), anyString(), any());
        verify(openMetadataStore, never()).classifyMetadataElementInStore(anyString(), anyString(), any(), any(NewElementProperties.class));

        this.verifyCompletion(CompletionStatus.ACTIONED, ZonePublisherGuard.NO_ZONES_DETECTED.getName());
    }


    /**
     * An empty request parameter overrides the configured zones, so the zone membership is removed.
     *
     * @throws Exception not expected
     */
    @Test public void testEmptyRequestParameterRemovesZones() throws Exception
    {
        configurationProperties.put(ZonePublisherRequestParameter.PUBLISH_ZONES.getName(), "zone1");

        when(governanceContext.getRequestParameters()).thenReturn(Map.of(ZonePublisherRequestParameter.PUBLISH_ZONES.getName(), " , "));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.newAsset(ASSET_GUID)));

        this.runService();

        verify(openMetadataStore).declassifyMetadataElementInStore(eq(ASSET_GUID), eq(OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName), any());
        verify(openMetadataStore, never()).classifyMetadataElementInStore(anyString(), anyString(), any(), any(NewElementProperties.class));

        this.verifyCompletion(CompletionStatus.ACTIONED, ZonePublisherGuard.NO_ZONES_DETECTED.getName());
    }


    /**
     * Without action targets there is nothing to update.
     *
     * @throws Exception not expected
     */
    @Test public void testNoTargets() throws Exception
    {
        when(governanceContext.getRequestParameters()).thenReturn(Map.of(ZonePublisherRequestParameter.PUBLISH_ZONES.getName(), "zone1"));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of());

        this.runService();

        this.verifyNothingChanged();
        this.verifyCompletion(CompletionStatus.INVALID, ZonePublisherGuard.NO_TARGETS.getName());
    }


    /**
     * Action targets that are not new assets do not mean that zones were assigned, or removed.
     *
     * @throws Exception not expected
     */
    @Test public void testNoNewAssets() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget("sourceFile", "source-guid")));

        this.runService();

        this.verifyNothingChanged();
        this.verifyCompletion(CompletionStatus.INVALID, ZonePublisherGuard.NO_TARGETS.getName());
    }


    /**
     * A failure to update an asset is reported with the zone-publishing-failed guard.
     *
     * @throws Exception not expected
     */
    @Test public void testPublishingFailure() throws Exception
    {
        when(governanceContext.getRequestParameters()).thenReturn(Map.of(ZonePublisherRequestParameter.PUBLISH_ZONES.getName(), "zone1"));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.newAsset(ASSET_GUID)));

        doThrow(new PropertyServerException(GovernanceActionConnectorsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition("test", "test", "repository unavailable"),
                                            "test",
                                            "test"))
                .when(openMetadataStore).classifyMetadataElementInStore(anyString(), anyString(), any(), any(NewElementProperties.class));

        assertThrows(ConnectorCheckedException.class, this::runService);

        verify(governanceContext).recordCompletionStatus(eq(CompletionStatus.FAILED),
                                                         eq(List.of(ZonePublisherGuard.ZONE_PUBLISHING_FAILED.getName())),
                                                         isNull(),
                                                         isNull(),
                                                         anyString());
    }


    /**
     * The guards that the service produces are declared by its provider.
     */
    @Test public void testProducedGuardsDeclared()
    {
        List<String> declaredGuards = new ZonePublisherGovernanceActionProvider().getProducedGuards()
                                                                                .stream()
                                                                                .map(GuardType::getName)
                                                                                .toList();

        for (ZonePublisherGuard guard : ZonePublisherGuard.values())
        {
            assertTrue(declaredGuards.contains(guard.getName()), guard.getName());
        }
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

        ZonePublisherGovernanceActionConnector connector = new ZonePublisherGovernanceActionConnector();

        connector.initialize("test-instance", connection);
        connector.setGovernanceContext(governanceContext);
        connector.start();
    }


    /**
     * Return the zone membership set on the new asset.
     *
     * @return zone names
     * @throws Exception not expected
     */
    private List<String> zoneMembership() throws Exception
    {
        ArgumentCaptor<NewElementProperties> captor = ArgumentCaptor.forClass(NewElementProperties.class);

        verify(openMetadataStore).classifyMetadataElementInStore(eq(ASSET_GUID),
                                                                 eq(OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName),
                                                                 any(),
                                                                 captor.capture());

        return propertyHelper.getStringArrayProperty("test", OpenMetadataProperty.ZONE_MEMBERSHIP.name, captor.getValue(), "test");
    }


    /**
     * Check that no element was classified or declassified.
     *
     * @throws Exception not expected
     */
    private void verifyNothingChanged() throws Exception
    {
        verify(openMetadataStore, never()).classifyMetadataElementInStore(anyString(), anyString(), any(), any(NewElementProperties.class));
        verify(openMetadataStore, never()).declassifyMetadataElementInStore(anyString(), anyString(), any());
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
     * Return a newAsset action target for an asset.
     *
     * @param guid unique identifier of the asset
     * @return action target
     */
    private ActionTargetElement newAsset(String guid)
    {
        return this.actionTarget("newAsset", guid);
    }


    /**
     * Return a newAsset action target whose element could not be retrieved.
     *
     * @return action target
     */
    private ActionTargetElement newAssetWithoutElement()
    {
        ActionTargetElement actionTarget = new ActionTargetElement();
        actionTarget.setActionTargetName("newAsset");
        actionTarget.setActionTargetGUID("missing-guid");

        return actionTarget;
    }


    /**
     * Return an action target for an asset.
     *
     * @param actionTargetName name of the action target
     * @param guid unique identifier of the asset
     * @return action target
     */
    private ActionTargetElement actionTarget(String actionTargetName,
                                             String guid)
    {
        ElementType elementType = new ElementType();
        elementType.setTypeName(OpenMetadataType.ASSET.typeName);
        elementType.setSuperTypeNames(List.of(OpenMetadataType.REFERENCEABLE.typeName, OpenMetadataType.OPEN_METADATA_ROOT.typeName));

        OpenMetadataElement element = new OpenMetadataElement();
        element.setElementGUID(guid);
        element.setType(elementType);

        ActionTargetElement actionTarget = new ActionTargetElement();
        actionTarget.setActionTargetName(actionTargetName);
        actionTarget.setActionTargetGUID(guid);
        actionTarget.setTargetElement(element);

        return actionTarget;
    }
}
