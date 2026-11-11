/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.mockito.ArgumentCaptor;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationExplorerClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.PeerDuplicateLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.PrimitiveTypePropertyValue;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * QualifiedNamePeerDuplicateGovernanceActionConnectorTest runs the Qualified Name De-duplicator Governance Action Service
 * over a mocked governance context and checks the peer duplicates it links, the KnownDuplicate classifications it sets
 * and the guards it produces.
 */
public class QualifiedNamePeerDuplicateGovernanceActionConnectorTest
{
    private static final String SERVICE_NAME     = "qualified-name-dedup";
    private static final String TARGET_GUID      = "target-guid";
    private static final String DUPLICATE1_GUID  = "duplicate1-guid";
    private static final String DUPLICATE2_GUID  = "duplicate2-guid";
    private static final String QUALIFIED_NAME   = "Asset::SalesData";
    private static final String TYPE_NAME        = OpenMetadataType.ASSET.typeName;
    private static final String TYPE_GUID        = "asset-type-guid";

    private final PropertyHelper propertyHelper = new PropertyHelper();

    private GovernanceActionContext      governanceContext;
    private OpenMetadataStore            openMetadataStore;
    private ClassificationExplorerClient classificationExplorerClient;


    /**
     * A mocked governance context with no action targets.
     *
     * @throws Exception not expected
     */
    @BeforeMethod public void setUpContext() throws Exception
    {
        governanceContext            = mock(GovernanceActionContext.class);
        openMetadataStore            = mock(OpenMetadataStore.class);
        classificationExplorerClient = mock(ClassificationExplorerClient.class);

        when(governanceContext.getOpenMetadataStore()).thenReturn(openMetadataStore);
        when(governanceContext.getClassificationExplorerClient()).thenReturn(classificationExplorerClient);
    }


    /**
     * Every element with the same type and qualified name is linked to the action target, and they are all classified
     * as known duplicates.  The search uses the action target's type name rather than its type GUID.
     *
     * @throws Exception not expected
     */
    @Test public void testEveryDuplicateLinked() throws Exception
    {
        OpenMetadataElement target = this.element(TARGET_GUID, QUALIFIED_NAME, false);

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget(target)));
        this.setUpSearch(List.of(target,
                                 this.element(DUPLICATE1_GUID, QUALIFIED_NAME, false),
                                 this.element(DUPLICATE2_GUID, QUALIFIED_NAME, false)));

        this.runService();

        ArgumentCaptor<SearchProperties> searchCaptor = ArgumentCaptor.forClass(SearchProperties.class);
        verify(openMetadataStore).findMetadataElements(eq(TYPE_NAME), isNull(), searchCaptor.capture(), isNull(), eq(0), eq(0));
        assertEquals(((PrimitiveTypePropertyValue) searchCaptor.getValue().getConditions().get(0).getValue()).getPrimitiveValue(), QUALIFIED_NAME);

        ArgumentCaptor<PeerDuplicateLinkProperties> linkCaptor = ArgumentCaptor.forClass(PeerDuplicateLinkProperties.class);
        verify(classificationExplorerClient).linkElementsAsPeerDuplicates(eq(TARGET_GUID), eq(DUPLICATE1_GUID), linkCaptor.capture(), any(MakeAnchorOptions.class));
        verify(classificationExplorerClient).linkElementsAsPeerDuplicates(eq(TARGET_GUID), eq(DUPLICATE2_GUID), any(PeerDuplicateLinkProperties.class), any(MakeAnchorOptions.class));
        verify(classificationExplorerClient, never()).linkElementsAsPeerDuplicates(eq(TARGET_GUID), eq(TARGET_GUID), any(), any());
        assertEquals(linkCaptor.getValue().getStatusIdentifier(), Integer.valueOf(1));
        assertEquals(linkCaptor.getValue().getSource(), SERVICE_NAME);

        verify(classificationExplorerClient).setKnownDuplicateClassification(eq(TARGET_GUID), isNull(), any(MakeAnchorOptions.class));
        verify(classificationExplorerClient).setKnownDuplicateClassification(eq(DUPLICATE1_GUID), isNull(), any(MakeAnchorOptions.class));
        verify(classificationExplorerClient).setKnownDuplicateClassification(eq(DUPLICATE2_GUID), isNull(), any(MakeAnchorOptions.class));

        this.verifyCompletion(CompletionStatus.ACTIONED, QualifiedNamePeerDuplicateGuard.DUPLICATE_ASSIGNED.getName());
    }


    /**
     * A duplicate that is already linked is not linked again, and an element that already has the KnownDuplicate
     * classification is not classified again.
     *
     * @throws Exception not expected
     */
    @Test public void testOnlyNewDuplicatesLinked() throws Exception
    {
        OpenMetadataElement target     = this.element(TARGET_GUID, QUALIFIED_NAME, true);
        OpenMetadataElement duplicate1 = this.element(DUPLICATE1_GUID, QUALIFIED_NAME, true);

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget(target)));
        this.setUpSearch(List.of(target, duplicate1, this.element(DUPLICATE2_GUID, QUALIFIED_NAME, false)));
        this.setUpPeerDuplicates(List.of(duplicate1));

        this.runService();

        verify(classificationExplorerClient, never()).linkElementsAsPeerDuplicates(anyString(), eq(DUPLICATE1_GUID), any(), any());
        verify(classificationExplorerClient).linkElementsAsPeerDuplicates(eq(TARGET_GUID), eq(DUPLICATE2_GUID), any(PeerDuplicateLinkProperties.class), any(MakeAnchorOptions.class));

        verify(classificationExplorerClient, never()).setKnownDuplicateClassification(eq(TARGET_GUID), any(), any());
        verify(classificationExplorerClient, never()).setKnownDuplicateClassification(eq(DUPLICATE1_GUID), any(), any());
        verify(classificationExplorerClient).setKnownDuplicateClassification(eq(DUPLICATE2_GUID), isNull(), any(MakeAnchorOptions.class));

        this.verifyCompletion(CompletionStatus.ACTIONED, QualifiedNamePeerDuplicateGuard.DUPLICATE_ASSIGNED.getName());
    }


    /**
     * Rerunning the service once all the duplicates are linked changes nothing.
     *
     * @throws Exception not expected
     */
    @Test public void testDuplicatesAlreadyLinked() throws Exception
    {
        OpenMetadataElement target     = this.element(TARGET_GUID, QUALIFIED_NAME, true);
        OpenMetadataElement duplicate1 = this.element(DUPLICATE1_GUID, QUALIFIED_NAME, true);
        OpenMetadataElement duplicate2 = this.element(DUPLICATE2_GUID, QUALIFIED_NAME, true);

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget(target)));
        this.setUpSearch(List.of(target, duplicate1, duplicate2));
        this.setUpPeerDuplicates(List.of(duplicate2, duplicate1));

        this.runService();

        verify(classificationExplorerClient, never()).linkElementsAsPeerDuplicates(anyString(), anyString(), any(), any());
        verify(classificationExplorerClient, never()).setKnownDuplicateClassification(anyString(), any(), any());
        this.verifyCompletion(CompletionStatus.ACTIONED, QualifiedNamePeerDuplicateGuard.DUPLICATE_ALREADY_ASSIGNED.getName());
    }


    /**
     * When the action target is the only element with its qualified name, nothing is changed.
     *
     * @throws Exception not expected
     */
    @Test public void testNoDuplicates() throws Exception
    {
        OpenMetadataElement target = this.element(TARGET_GUID, QUALIFIED_NAME, false);

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget(target)));
        this.setUpSearch(List.of(target));

        this.runService();

        verifyNoInteractions(classificationExplorerClient);
        this.verifyCompletion(CompletionStatus.ACTIONED, QualifiedNamePeerDuplicateGuard.NO_DUPLICATION_DETECTED.getName());
    }


    /**
     * An action target without a qualified name can not have duplicates detected.
     *
     * @throws Exception not expected
     */
    @Test public void testNoQualifiedName() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget(this.element(TARGET_GUID, null, false))));

        this.runService();

        verifyNoInteractions(openMetadataStore, classificationExplorerClient);
        this.verifyCompletion(CompletionStatus.INVALID, QualifiedNamePeerDuplicateGuard.INVALID_ACTION_TARGET_TYPE.getName());
    }


    /**
     * Without action targets there is nothing to check.
     *
     * @throws Exception not expected
     */
    @Test public void testNoTargets() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(new ArrayList<>());

        this.runService();

        verifyNoInteractions(openMetadataStore, classificationExplorerClient);
        this.verifyCompletion(CompletionStatus.INVALID, QualifiedNamePeerDuplicateGuard.NO_TARGETS_DETECTED.getName());
    }


    /**
     * Only one action target is supported.
     *
     * @throws Exception not expected
     */
    @Test public void testMultipleTargets() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget(this.element(TARGET_GUID, QUALIFIED_NAME, false)),
                                                                             this.actionTarget(this.element(DUPLICATE1_GUID, QUALIFIED_NAME, false))));

        this.runService();

        verifyNoInteractions(openMetadataStore, classificationExplorerClient);
        this.verifyCompletion(CompletionStatus.INVALID, QualifiedNamePeerDuplicateGuard.MULTIPLE_TARGETS_DETECTED.getName());
    }


    /**
     * Run the service.
     *
     * @throws Exception not expected
     */
    private void runService() throws Exception
    {
        QualifiedNamePeerDuplicateGovernanceActionConnector connector = new QualifiedNamePeerDuplicateGovernanceActionConnector();

        connector.initialize("test-instance", new Connection());
        connector.setGovernanceServiceName(SERVICE_NAME);
        connector.setGovernanceContext(governanceContext);
        connector.start();
    }


    /**
     * Return the elements from the search for the qualified name.
     *
     * @param elements elements with the qualified name
     * @throws Exception not expected
     */
    private void setUpSearch(List<OpenMetadataElement> elements) throws Exception
    {
        when(openMetadataStore.findMetadataElements(eq(TYPE_NAME), isNull(), any(SearchProperties.class), isNull(), anyInt(), anyInt())).thenReturn(elements);
    }


    /**
     * Return the elements that are already linked to the action target as peer duplicates.
     *
     * @param peerDuplicates linked elements
     * @throws Exception not expected
     */
    private void setUpPeerDuplicates(List<OpenMetadataElement> peerDuplicates) throws Exception
    {
        List<RelatedMetadataElement> relatedElements = new ArrayList<>();

        for (OpenMetadataElement peerDuplicate : peerDuplicates)
        {
            RelatedMetadataElement relatedElement = new RelatedMetadataElement();
            relatedElement.setElement(peerDuplicate);
            relatedElements.add(relatedElement);
        }

        RelatedMetadataElementList relatedElementList = new RelatedMetadataElementList();
        relatedElementList.setElementList(relatedElements);

        when(openMetadataStore.getRelatedMetadataElements(eq(TARGET_GUID), eq(0), eq(OpenMetadataType.PEER_DUPLICATE_LINK.typeName), anyInt(), anyInt()))
                .thenReturn(relatedElementList);
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
     * Return an action target for an element.
     *
     * @param element element to wrap
     * @return action target
     */
    private ActionTargetElement actionTarget(OpenMetadataElement element)
    {
        ActionTargetElement actionTarget = new ActionTargetElement();
        actionTarget.setActionTargetName("elementGUID");
        actionTarget.setActionTargetGUID(element.getElementGUID());
        actionTarget.setTargetElement(element);

        return actionTarget;
    }


    /**
     * Return an asset with the requested qualified name.
     *
     * @param guid unique identifier of the element
     * @param qualifiedName qualified name of the element - null for no properties
     * @param knownDuplicate should the element have the KnownDuplicate classification?
     * @return element
     */
    private OpenMetadataElement element(String  guid,
                                        String  qualifiedName,
                                        boolean knownDuplicate)
    {
        ElementType elementType = new ElementType();
        elementType.setTypeId(TYPE_GUID);
        elementType.setTypeName(TYPE_NAME);
        elementType.setSuperTypeNames(List.of(OpenMetadataType.REFERENCEABLE.typeName, OpenMetadataType.OPEN_METADATA_ROOT.typeName));

        OpenMetadataElement element = new OpenMetadataElement();
        element.setElementGUID(guid);
        element.setType(elementType);

        if (qualifiedName != null)
        {
            element.setElementProperties(propertyHelper.addStringProperty(null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName));
        }

        if (knownDuplicate)
        {
            AttachedClassification classification = new AttachedClassification();
            classification.setClassificationName(OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION.typeName);
            element.setClassifications(List.of(classification));
        }

        return element;
    }
}
