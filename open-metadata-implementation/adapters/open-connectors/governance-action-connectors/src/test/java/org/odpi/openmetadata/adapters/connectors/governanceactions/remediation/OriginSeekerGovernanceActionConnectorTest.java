/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.mockito.ArgumentCaptor;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyValue;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.GuardType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

/**
 * OriginSeekerGovernanceActionConnectorTest runs the Origin Seeker Governance Action Service over a mocked
 * governance context and lineage graph, and checks the origin it assigns and the guards it produces.
 */
public class OriginSeekerGovernanceActionConnectorTest
{
    private static final String ASSET_GUID = "asset-guid";

    private final PropertyHelper propertyHelper = new PropertyHelper();

    private GovernanceActionContext governanceContext;
    private OpenMetadataStore       openMetadataStore;


    /**
     * A mocked governance context with no action targets and an empty lineage graph.
     *
     * @throws Exception not expected
     */
    @BeforeMethod public void setUpContext() throws Exception
    {
        governanceContext = mock(GovernanceActionContext.class);
        openMetadataStore = mock(OpenMetadataStore.class);

        when(governanceContext.getOpenMetadataStore()).thenReturn(openMetadataStore);
    }


    /**
     * The origin of an upstream asset is assigned to the new asset.
     *
     * @throws Exception not expected
     */
    @Test public void testOriginAssigned() throws Exception
    {
        OpenMetadataElement asset    = this.asset(ASSET_GUID, null);
        OpenMetadataElement process  = this.asset("process-guid", null);
        OpenMetadataElement source   = this.asset("source-guid", this.origin("Hampton Hospital"));

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.newAsset(asset)));
        this.addUpstream(ASSET_GUID, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, process);
        this.addUpstream("process-guid", OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName, source);

        this.runService();

        assertEquals(this.assignedOrigin(), this.origin("Hampton Hospital").getPropertyValueMap());
        this.verifyCompletion(CompletionStatus.ACTIONED, OriginSeekerGuard.ORIGIN_ASSIGNED.getName());
    }


    /**
     * Every page of lineage relationships is read, so an origin beyond the first page is found.
     *
     * @throws Exception not expected
     */
    @Test public void testOriginBeyondFirstPage() throws Exception
    {
        OpenMetadataElement asset = this.asset(ASSET_GUID, null);

        when(governanceContext.getMaxPageSize()).thenReturn(2);
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.newAsset(asset)));
        when(openMetadataStore.getRelatedMetadataElements(ASSET_GUID, 2, OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName, 0, 2))
                .thenReturn(this.relatedElements(this.asset("first-guid", null), this.asset("second-guid", null)));
        when(openMetadataStore.getRelatedMetadataElements(ASSET_GUID, 2, OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName, 2, 2))
                .thenReturn(this.relatedElements(this.asset("third-guid", this.origin("Coco Pharmaceuticals"))));

        this.runService();

        assertEquals(this.assignedOrigin(), this.origin("Coco Pharmaceuticals").getPropertyValueMap());
        this.verifyCompletion(CompletionStatus.ACTIONED, OriginSeekerGuard.ORIGIN_ASSIGNED.getName());
    }


    /**
     * The same origin found down different branches of the lineage graph, including a circular one, is assigned once.
     *
     * @throws Exception not expected
     */
    @Test public void testSameOriginFromSeveralBranches() throws Exception
    {
        OpenMetadataElement asset   = this.asset(ASSET_GUID, null);
        OpenMetadataElement left    = this.asset("left-guid", null);
        OpenMetadataElement right   = this.asset("right-guid", this.origin("Hampton Hospital"));
        OpenMetadataElement source  = this.asset("source-guid", this.origin("Hampton Hospital"));

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.newAsset(asset)));
        this.addUpstream(ASSET_GUID, OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName, left, right);
        this.addUpstream("left-guid", OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName, source, asset);

        this.runService();

        assertEquals(this.assignedOrigin(), this.origin("Hampton Hospital").getPropertyValueMap());
        this.verifyCompletion(CompletionStatus.ACTIONED, OriginSeekerGuard.ORIGIN_ASSIGNED.getName());
    }


    /**
     * Different origins are passed on in the detectedOrigins request parameter for a steward to choose from.
     *
     * @throws Exception not expected
     */
    @Test public void testMultipleOrigins() throws Exception
    {
        OpenMetadataElement asset = this.asset(ASSET_GUID, null);

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.newAsset(asset)));
        this.addUpstream(ASSET_GUID,
                         OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                         this.asset("first-guid", this.origin("Hampton Hospital")),
                         this.asset("second-guid", this.origin("Oak Dene Hospital")));

        this.runService();

        verify(openMetadataStore, never()).classifyMetadataElementInStore(anyString(), anyString(), any(), any(NewElementProperties.class));

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);

        verify(governanceContext).recordCompletionStatus(eq(CompletionStatus.INVALID),
                                                         eq(List.of(OriginSeekerGuard.MULTIPLE_ORIGINS_DETECTED.getName())),
                                                         captor.capture(),
                                                         isNull());

        String detectedOrigins = captor.getValue().get("detectedOrigins");

        assertNotNull(detectedOrigins);
        assertTrue(detectedOrigins.contains("Hampton Hospital"), detectedOrigins);
        assertTrue(detectedOrigins.contains("Oak Dene Hospital"), detectedOrigins);
    }


    /**
     * Without an origin in the lineage graph, nothing is assigned.
     *
     * @throws Exception not expected
     */
    @Test public void testNoOrigins() throws Exception
    {
        OpenMetadataElement asset = this.asset(ASSET_GUID, null);

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.newAsset(asset)));
        this.addUpstream(ASSET_GUID, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, this.asset("source-guid", null));

        this.runService();

        verify(openMetadataStore, never()).classifyMetadataElementInStore(anyString(), anyString(), any(), any(NewElementProperties.class));
        this.verifyCompletion(CompletionStatus.INVALID, OriginSeekerGuard.NO_ORIGINS_DETECTED.getName());
    }


    /**
     * An asset that already has an origin is left alone.
     *
     * @throws Exception not expected
     */
    @Test public void testOriginAlreadyAssigned() throws Exception
    {
        OpenMetadataElement asset = this.asset(ASSET_GUID, this.origin("Coco Pharmaceuticals"));

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.newAsset(asset)));

        this.runService();

        verify(openMetadataStore, never()).getRelatedMetadataElements(anyString(), anyInt(), any(), anyInt(), anyInt());
        verify(openMetadataStore, never()).classifyMetadataElementInStore(anyString(), anyString(), any(), any(NewElementProperties.class));
        this.verifyCompletion(CompletionStatus.ACTIONED, OriginSeekerGuard.ORIGIN_ALREADY_ASSIGNED.getName());
    }


    /**
     * No action targets, or no newAsset action target, means there is no asset to work on.
     *
     * @throws Exception not expected
     */
    @Test public void testNoTargets() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(null);

        this.runService();

        when(governanceContext.getActionTargetElements()).thenReturn(List.of());

        this.runService();

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget("sourceFile", this.asset("source-guid", null))));

        this.runService();

        verify(openMetadataStore, never()).classifyMetadataElementInStore(anyString(), anyString(), any(), any(NewElementProperties.class));
        verify(governanceContext, times(3)).recordCompletionStatus(eq(CompletionStatus.INVALID),
                                                                   eq(List.of(OriginSeekerGuard.NO_TARGETS.getName())),
                                                                   isNull(),
                                                                   isNull());
    }


    /**
     * More than one newAsset action target is not supported.
     *
     * @throws Exception not expected
     */
    @Test public void testMultipleTargets() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.newAsset(this.asset(ASSET_GUID, null)),
                                                                             this.newAsset(this.asset("second-asset-guid", null))));

        this.runService();

        verify(openMetadataStore, never()).classifyMetadataElementInStore(anyString(), anyString(), any(), any(NewElementProperties.class));
        this.verifyCompletion(CompletionStatus.INVALID, OriginSeekerGuard.MULTIPLE_TARGETS.getName());
    }


    /**
     * A newAsset action target that is not an asset is rejected.
     *
     * @throws Exception not expected
     */
    @Test public void testTargetNotAsset() throws Exception
    {
        OpenMetadataElement term = this.element("term-guid", OpenMetadataType.GLOSSARY_TERM.typeName, null);

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.newAsset(term)));

        this.runService();

        verify(openMetadataStore, never()).classifyMetadataElementInStore(anyString(), anyString(), any(), any(NewElementProperties.class));
        this.verifyCompletion(CompletionStatus.INVALID, OriginSeekerGuard.TARGET_NOT_ASSET.getName());
    }


    /**
     * A failure to read the lineage graph is reported with the origin-seeking-failed guard.
     *
     * @throws Exception not expected
     */
    @Test public void testOriginSeekingFailed() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.newAsset(this.asset(ASSET_GUID, null))));
        when(openMetadataStore.getRelatedMetadataElements(anyString(), anyInt(), anyString(), anyInt(), anyInt()))
                .thenThrow(new PropertyServerException(GovernanceActionConnectorsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition("test", "test", "repository unavailable"),
                                                       "test",
                                                       "test"));

        assertThrows(ConnectorCheckedException.class, this::runService);

        verify(governanceContext).recordCompletionStatus(eq(CompletionStatus.FAILED),
                                                         eq(List.of(OriginSeekerGuard.ORIGIN_SEEKING_FAILED.getName())),
                                                         isNull(),
                                                         isNull(),
                                                         anyString());
    }


    /**
     * The provider lists every guard that the service can produce.
     */
    @Test public void testProducedGuards()
    {
        List<String> producedGuards = new ArrayList<>();

        for (GuardType guardType : new OriginSeekerGovernanceActionProvider().getProducedGuards())
        {
            producedGuards.add(guardType.getName());
        }

        for (OriginSeekerGuard guard : OriginSeekerGuard.values())
        {
            assertTrue(producedGuards.contains(guard.getName()), guard.getName());
        }

        assertTrue(producedGuards.contains("no-targets-detected"));
        assertTrue(producedGuards.contains("multiple-targets-detected"));
        assertTrue(producedGuards.contains("target-not-asset"));
    }


    /**
     * Run the service.
     *
     * @throws Exception not expected
     */
    private void runService() throws Exception
    {
        OriginSeekerGovernanceActionConnector connector = new OriginSeekerGovernanceActionConnector();

        connector.initialize("test-instance", new Connection());
        connector.setGovernanceContext(governanceContext);
        connector.start();
    }


    /**
     * Return the properties of the single DigitalResourceOrigin classification that was set on the new asset.
     *
     * @return map of classification property values
     * @throws Exception not expected
     */
    private Map<String, PropertyValue> assignedOrigin() throws Exception
    {
        ArgumentCaptor<NewElementProperties> captor = ArgumentCaptor.forClass(NewElementProperties.class);

        verify(openMetadataStore).classifyMetadataElementInStore(eq(ASSET_GUID),
                                                                 eq(OpenMetadataType.DIGITAL_RESOURCE_ORIGIN_CLASSIFICATION.typeName),
                                                                 any(),
                                                                 captor.capture());
        return captor.getValue().getPropertyValueMap();
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
                                                         isNull());
    }


    /**
     * Make the supplied elements the upstream end of lineage relationships of the requested type to an element.
     *
     * @param elementGUID unique identifier of the downstream element
     * @param relationshipTypeName type of lineage relationship
     * @param upstreamElements elements at the upstream end
     * @throws Exception not expected
     */
    private void addUpstream(String                 elementGUID,
                             String                 relationshipTypeName,
                             OpenMetadataElement... upstreamElements) throws Exception
    {
        when(openMetadataStore.getRelatedMetadataElements(eq(elementGUID), eq(2), eq(relationshipTypeName), anyInt(), anyInt()))
                .thenReturn(this.relatedElements(upstreamElements));
    }


    /**
     * Return a page of related elements.
     *
     * @param elements related elements
     * @return page
     */
    private RelatedMetadataElementList relatedElements(OpenMetadataElement... elements)
    {
        List<RelatedMetadataElement> elementList = new ArrayList<>();

        for (OpenMetadataElement element : elements)
        {
            RelatedMetadataElement relatedMetadataElement = new RelatedMetadataElement();
            relatedMetadataElement.setElement(element);

            elementList.add(relatedMetadataElement);
        }

        RelatedMetadataElementList relatedMetadataElementList = new RelatedMetadataElementList();
        relatedMetadataElementList.setElementList(elementList);

        return relatedMetadataElementList;
    }


    /**
     * Return the properties of an origin classification.
     *
     * @param organization name of the organization where the data came from
     * @return classification properties
     */
    private ElementProperties origin(String organization)
    {
        return propertyHelper.addStringProperty(null, OpenMetadataProperty.ORGANIZATION.name, organization);
    }


    /**
     * Return an asset.
     *
     * @param guid unique identifier of the asset
     * @param origin properties of its origin classification, or null for no origin
     * @return asset
     */
    private OpenMetadataElement asset(String            guid,
                                      ElementProperties origin)
    {
        return this.element(guid, OpenMetadataType.ASSET.typeName, origin);
    }


    /**
     * Return an element of the requested type.
     *
     * @param guid unique identifier of the element
     * @param typeName type of the element
     * @param origin properties of its origin classification, or null for no origin
     * @return element
     */
    private OpenMetadataElement element(String            guid,
                                        String            typeName,
                                        ElementProperties origin)
    {
        ElementType elementType = new ElementType();
        elementType.setTypeName(typeName);
        elementType.setSuperTypeNames(List.of(OpenMetadataType.REFERENCEABLE.typeName, OpenMetadataType.OPEN_METADATA_ROOT.typeName));

        OpenMetadataElement element = new OpenMetadataElement();
        element.setElementGUID(guid);
        element.setType(elementType);

        if (origin != null)
        {
            AttachedClassification classification = new AttachedClassification();
            classification.setClassificationName(OpenMetadataType.DIGITAL_RESOURCE_ORIGIN_CLASSIFICATION.typeName);
            classification.setClassificationProperties(origin);

            element.setClassifications(List.of(classification));
        }

        return element;
    }


    /**
     * Return a newAsset action target.
     *
     * @param element element to work on
     * @return action target
     */
    private ActionTargetElement newAsset(OpenMetadataElement element)
    {
        return this.actionTarget("newAsset", element);
    }


    /**
     * Return an action target.
     *
     * @param actionTargetName name of the action target
     * @param element element to work on
     * @return action target
     */
    private ActionTargetElement actionTarget(String              actionTargetName,
                                             OpenMetadataElement element)
    {
        ActionTargetElement actionTarget = new ActionTargetElement();
        actionTarget.setActionTargetName(actionTargetName);
        actionTarget.setActionTargetGUID(element.getElementGUID());
        actionTarget.setTargetElement(element);

        return actionTarget;
    }
}
