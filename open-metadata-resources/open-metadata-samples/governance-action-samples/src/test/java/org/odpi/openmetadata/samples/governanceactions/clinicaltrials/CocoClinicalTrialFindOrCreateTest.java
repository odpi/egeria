/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionContext;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * CocoClinicalTrialFindOrCreateTest covers the find-or-create methods that let the clinical trial services be run
 * again after a failure, or for a clinical trial that is already set up, without creating any element or
 * relationship a second time.  The governance context and open metadata store are mocked.
 */
public class CocoClinicalTrialFindOrCreateTest
{
    private static final String PROCESS_GUID = "process-guid";
    private static final String PERSON_GUID  = "person-guid";

    private final PropertyHelper propertyHelper = new PropertyHelper();

    private OpenMetadataStore           openMetadataStore;
    private CocoClinicalTrialBaseService service;


    /**
     * A base service over a mocked governance context.
     *
     * @throws Exception not expected
     */
    @BeforeMethod public void setUpService() throws Exception
    {
        GovernanceActionContext governanceContext = mock(GovernanceActionContext.class);

        openMetadataStore = mock(OpenMetadataStore.class);

        when(governanceContext.getOpenMetadataStore()).thenReturn(openMetadataStore);
        when(governanceContext.getMaxPageSize()).thenReturn(100);

        service = new CocoClinicalTrialBaseService();
        service.setGovernanceContext(governanceContext);
    }


    /**
     * With no relationship in place, the relationship is created.
     *
     * @throws Exception not expected
     */
    @Test public void testRelationshipCreatedWhenAbsent() throws Exception
    {
        ElementProperties properties = this.actionTargetName("clinicalTrialManager");

        when(openMetadataStore.createRelatedElementsInStore(anyString(), anyString(), anyString(), any(), any(), any(ElementProperties.class))).thenReturn("new-relationship-guid");

        assertEquals(service.createRelationshipIfAbsent(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName, PROCESS_GUID, PERSON_GUID, properties),
                     "new-relationship-guid");
    }


    /**
     * A relationship with the same ends and properties is reused, not created again.
     *
     * @throws Exception not expected
     */
    @Test public void testIdenticalRelationshipReused() throws Exception
    {
        this.relatedPeople(this.related("existing-relationship-guid", PERSON_GUID, this.actionTargetName("clinicalTrialManager")));

        assertEquals(service.createRelationshipIfAbsent(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                        PROCESS_GUID,
                                                        PERSON_GUID,
                                                        this.actionTargetName("clinicalTrialManager")),
                     "existing-relationship-guid");

        verify(openMetadataStore, never()).createRelatedElementsInStore(anyString(), anyString(), anyString(), any(), any(), any(ElementProperties.class));
    }


    /**
     * The same two elements may be linked more than once with different properties - here the same person is an
     * action target under two names - so a relationship with different properties does not count as a match.
     *
     * @throws Exception not expected
     */
    @Test public void testRelationshipWithDifferentPropertiesCreated() throws Exception
    {
        this.relatedPeople(this.related("existing-relationship-guid", PERSON_GUID, this.actionTargetName("clinicalTrialManager")));

        when(openMetadataStore.createRelatedElementsInStore(anyString(), anyString(), anyString(), any(), any(), any(ElementProperties.class))).thenReturn("new-relationship-guid");

        assertEquals(service.createRelationshipIfAbsent(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                        PROCESS_GUID,
                                                        PERSON_GUID,
                                                        this.actionTargetName("clinicalTrialOwner")),
                     "new-relationship-guid");
    }


    /**
     * A relationship to a different element does not count as a match.
     *
     * @throws Exception not expected
     */
    @Test public void testRelationshipToOtherElementIgnored() throws Exception
    {
        this.relatedPeople(this.related("existing-relationship-guid", "other-person-guid", this.actionTargetName("clinicalTrialManager")));

        assertNull(service.getExistingRelationshipGUID(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                       PROCESS_GUID,
                                                       PERSON_GUID,
                                                       this.actionTargetName("clinicalTrialManager")));
    }


    /**
     * The qualified name an element would get from a template is worked out from the template's qualified name and
     * the placeholder values, and an element with that name is found.
     *
     * @throws Exception not expected
     */
    @Test public void testTemplatedElementFound() throws Exception
    {
        this.template("InformationSupplyChain::Clinical Trial Treatment Validation Information Supply Chain: ~{clinicalTrialId}~");

        when(openMetadataStore.getMetadataElementGUIDByUniqueName(eq("InformationSupplyChain::Clinical Trial Treatment Validation Information Supply Chain: PROJ-CT-TBDF"),
                                                                  eq(OpenMetadataProperty.QUALIFIED_NAME.name),
                                                                  any())).thenReturn("existing-isc-guid");

        assertEquals(service.getExistingTemplatedElementGUID("template-guid", null, Map.of("clinicalTrialId", "PROJ-CT-TBDF")), "existing-isc-guid");
    }


    /**
     * If a placeholder in the template's qualified name has no value, the new element's name cannot be predicted,
     * so nothing is looked up and the element is created.
     *
     * @throws Exception not expected
     */
    @Test public void testUnresolvedPlaceholderNotLookedUp() throws Exception
    {
        this.template("CertificationType::~{clinicalTrialId}~::~{hospitalName}~");

        assertNull(service.getExistingTemplatedElementGUID("template-guid", null, Map.of("clinicalTrialId", "PROJ-CT-TBDF")));

        verify(openMetadataStore, never()).getMetadataElementGUIDByUniqueName(anyString(), anyString(), any());
    }


    /**
     * A governance action process that an earlier run created is reused.
     *
     * @throws Exception not expected
     */
    @Test public void testExistingProcessReused() throws Exception
    {
        when(openMetadataStore.getMetadataElementGUIDByUniqueName(eq("ClinicalTrials::PROJ-CT-TBDF::certify-hospital"),
                                                                  eq(OpenMetadataProperty.QUALIFIED_NAME.name),
                                                                  any())).thenReturn("existing-process-guid");

        assertEquals(service.createProcessFromGovernanceActionTypeIfAbsent(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                           "ClinicalTrials::PROJ-CT-TBDF::certify-hospital",
                                                                           "Certify Hospital (PROJ-CT-TBDF)",
                                                                           null,
                                                                           0,
                                                                           "governance-action-type-guid",
                                                                           null,
                                                                           null,
                                                                           null),
                     "existing-process-guid");
    }


    /**
     * Return action target properties.
     *
     * @param actionTargetName name of the action target
     * @return properties
     */
    private ElementProperties actionTargetName(String actionTargetName)
    {
        return propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, actionTargetName);
    }


    /**
     * Return a related element as the store returns it.
     *
     * @param relationshipGUID unique identifier of the relationship
     * @param elementGUID unique identifier of the element at the other end
     * @param properties relationship properties
     * @return related element
     */
    private RelatedMetadataElement related(String            relationshipGUID,
                                           String            elementGUID,
                                           ElementProperties properties)
    {
        OpenMetadataElement element = new OpenMetadataElement();

        element.setElementGUID(elementGUID);

        RelatedMetadataElement relatedElement = new RelatedMetadataElement();

        relatedElement.setRelationshipGUID(relationshipGUID);
        relatedElement.setElement(element);
        relatedElement.setRelationshipProperties(properties);

        return relatedElement;
    }


    /**
     * Have the store return these elements as related to the process, on a single page.
     *
     * @param relatedElements related elements
     * @throws Exception not expected
     */
    private void relatedPeople(RelatedMetadataElement... relatedElements) throws Exception
    {
        RelatedMetadataElementList relatedList = new RelatedMetadataElementList();

        relatedList.setElementList(List.of(relatedElements));

        when(openMetadataStore.getRelatedMetadataElements(eq(PROCESS_GUID), eq(1), anyString(), eq(0), anyInt())).thenReturn(relatedList);
    }


    /**
     * Have the store return a template with this qualified name.
     *
     * @param qualifiedName template's qualified name
     * @throws Exception not expected
     */
    private void template(String qualifiedName) throws Exception
    {
        OpenMetadataElement template = new OpenMetadataElement();

        template.setElementGUID("template-guid");
        template.setElementProperties(propertyHelper.addStringProperty(null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName));

        when(openMetadataStore.getMetadataElementByGUID("template-guid")).thenReturn(template);
    }
}
