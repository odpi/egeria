/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateElementRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.CohortMemberProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.MetadataCohortPeerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.MetadataRepositoryCohortProperties;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.RuntimeManagerFvtSupport.assetMaker;
import static org.odpi.openmetadata.viewservicesfvt.RuntimeManagerFvtSupport.runtimeManager;

/**
 * RuntimeManagerCohortsFVT covers the Runtime Manager requests that maintain the catalogue entries for open
 * metadata repository cohorts and their members: create (directly and from a template), retrieve, update,
 * find, attach and detach a member, and delete.  These only read and write metadata, so they all succeed
 * in this suite's deployment.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RuntimeManagerCohortsFVT
{
    private static final ViewServiceClient metadataExpert = new ViewServiceClient(ViewServiceDescription.METADATA_EXPERT);

    private static final String COHORT_NAME   = "View services FVT cohort " + OMAGPlatformExtension.RUN_ID;
    private static final String TEMPLATED_NAME = "View services FVT templated cohort " + OMAGPlatformExtension.RUN_ID;
    private static final String TOPIC          = "egeria.omag.openMetadataTopic.viewServicesFvt";

    private static String cohortGUID          = null;
    private static String templatedCohortGUID = null;
    private static String cohortMemberGUID    = null;


    /**
     * A cohort is created and read back with its properties.
     */
    @Test
    @Order(1)
    void createAndRetrieveCohort()
    {
        MetadataRepositoryCohortProperties properties = new MetadataRepositoryCohortProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("MetadataRepositoryCohort", "cohort"));
        properties.setDisplayName(COHORT_NAME);
        properties.setDescription("Created by the Runtime Manager FVT.");
        properties.setCohortTopics(List.of(TOPIC));

        cohortGUID = runtimeManager.create("/metadata-repository-cohorts", RuntimeManagerFvtSupport.newElement(properties));

        MetadataRepositoryCohortProperties retrieved = cohortProperties(cohortGUID);

        assertEquals(COHORT_NAME, retrieved.getDisplayName());
        assertEquals(properties.getQualifiedName(), retrieved.getQualifiedName());
        assertEquals(List.of(TOPIC), retrieved.getCohortTopics());
    }


    /**
     * A merge update changes the description and leaves the other properties alone.
     */
    @Test
    @Order(2)
    void updateCohort()
    {
        MetadataRepositoryCohortProperties properties = new MetadataRepositoryCohortProperties();

        properties.setDescription("Updated by the Runtime Manager FVT.");

        UpdateElementRequestBody requestBody = new UpdateElementRequestBody();

        requestBody.setMergeUpdate(true);
        requestBody.setProperties(properties);

        runtimeManager.post("/metadata-repository-cohorts/" + cohortGUID + "/update", requestBody, BooleanResponse.class);

        MetadataRepositoryCohortProperties retrieved = cohortProperties(cohortGUID);

        assertEquals("Updated by the Runtime Manager FVT.", retrieved.getDescription());
        assertEquals(COHORT_NAME, retrieved.getDisplayName(), "a merge update lost the display name");
        assertEquals(List.of(TOPIC), retrieved.getCohortTopics(), "a merge update lost the cohort topics");
    }


    /**
     * The cohort is found by name and by search string.
     */
    @Test
    @Order(3)
    void findCohort()
    {
        FilterRequestBody byName = new FilterRequestBody();

        byName.setFilter(COHORT_NAME);

        assertTrue(guids(runtimeManager.elements("/metadata-repository-cohorts/by-name", byName)).contains(cohortGUID),
                   "getMetadataRepositoryCohortsByName did not return the cohort");

        SearchStringRequestBody bySearch = new SearchStringRequestBody();

        bySearch.setSearchString(ViewServicesFvtTestSupport.runSearchString());

        assertTrue(guids(runtimeManager.elements("/metadata-repository-cohorts/by-search-string", bySearch)).contains(cohortGUID),
                   "findMetadataRepositoryCohorts did not return the cohort");
    }


    /**
     * A cohort created from the first as a template has its own names and the template's other properties.
     */
    @Test
    @Order(4)
    void createCohortFromTemplate()
    {
        MetadataRepositoryCohortProperties replacementProperties = new MetadataRepositoryCohortProperties();

        replacementProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("MetadataRepositoryCohort", "templated"));
        replacementProperties.setDisplayName(TEMPLATED_NAME);

        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setTemplateGUID(cohortGUID);
        requestBody.setReplacementProperties(replacementProperties);

        templatedCohortGUID = runtimeManager.create("/metadata-repository-cohorts/from-template", requestBody);

        assertNotEquals(cohortGUID, templatedCohortGUID);

        MetadataRepositoryCohortProperties retrieved = cohortProperties(templatedCohortGUID);

        assertEquals(replacementProperties.getQualifiedName(), retrieved.getQualifiedName());
        assertEquals(TEMPLATED_NAME, retrieved.getDisplayName());
        assertEquals("Updated by the Runtime Manager FVT.", retrieved.getDescription(), "the description was not copied from the template");
        assertEquals(List.of(TOPIC), retrieved.getCohortTopics(), "the cohort topics were not copied from the template");
    }


    /**
     * A cohort member attached to the cohort is visible from both ends, and disappears when detached.
     */
    @Test
    @Order(5)
    void attachAndDetachCohortMember()
    {
        CohortMemberProperties memberProperties = new CohortMemberProperties();

        memberProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("CohortMember", "member"));
        memberProperties.setDisplayName("View services FVT cohort member");

        cohortMemberGUID = assetMaker.create("/software-capabilities", RuntimeManagerFvtSupport.newElement(memberProperties));

        MetadataCohortPeerProperties peerProperties = new MetadataCohortPeerProperties();

        peerProperties.setRegistrationDate(new Date());

        NewRelationshipRequestBody attachBody = new NewRelationshipRequestBody();

        attachBody.setProperties(peerProperties);

        runtimeManager.call("/metadata-repository-cohorts/" + cohortGUID + "/cohort-members/" + cohortMemberGUID + "/attach", attachBody);

        assertTrue(guids(cohort(cohortGUID).getCohortMembership()).contains(cohortMemberGUID),
                   "the cohort does not list the attached member");
        assertTrue(guids(member().getRegisteredWithCohorts()).contains(cohortGUID),
                   "the member does not list the cohort it was attached to");

        runtimeManager.call("/metadata-repository-cohorts/" + cohortGUID + "/cohort-members/" + cohortMemberGUID + "/detach",
                            new DeleteRelationshipRequestBody());

        assertFalse(guids(cohort(cohortGUID).getCohortMembership()).contains(cohortMemberGUID),
                    "the cohort still lists the detached member");
        assertFalse(guids(member().getRegisteredWithCohorts()).contains(cohortGUID),
                    "the member still lists the cohort it was detached from");
    }


    /**
     * Both cohorts and the member are deleted.
     */
    @Test
    @Order(99)
    void deleteCohorts()
    {
        for (String guid : new String[]{ templatedCohortGUID, cohortGUID })
        {
            if (guid != null)
            {
                runtimeManager.call("/metadata-repository-cohorts/" + guid + "/delete", new DeleteElementRequestBody());
                ViewServicesFvtTestSupport.assertGone(runtimeManager, "/metadata-repository-cohorts/" + guid + "/retrieve");
            }
        }

        if (cohortMemberGUID != null)
        {
            metadataExpert.call("/metadata-elements/" + cohortMemberGUID + "/delete", Map.of("class", "OpenMetadataDeleteRequestBody"));
            ViewServicesFvtTestSupport.assertGone(assetMaker, "/software-capabilities/" + cohortMemberGUID);
        }
    }


    private static OpenMetadataRootElement cohort(String guid)
    {
        OpenMetadataRootElement element = runtimeManager.element("/metadata-repository-cohorts/" + guid + "/retrieve", new GetRequestBody());

        assertNotNull(element, "cohort " + guid + " is missing");
        assertEquals(guid, element.getElementHeader().getGUID());

        return element;
    }


    private static MetadataRepositoryCohortProperties cohortProperties(String guid)
    {
        return assertInstanceOf(MetadataRepositoryCohortProperties.class, cohort(guid).getProperties());
    }


    private static OpenMetadataRootElement member()
    {
        OpenMetadataRootElement element = assetMaker.element("/software-capabilities/" + cohortMemberGUID, new GetRequestBody());

        assertNotNull(element, "cohort member " + cohortMemberGUID + " is missing");

        return element;
    }


    private static List<String> guids(List<?> elements)
    {
        if (elements == null)
        {
            return List.of();
        }

        return elements.stream().map(element -> (element instanceof RelatedMetadataElementSummary related)
                ? related.getRelatedElement().getElementHeader().getGUID()
                : ((OpenMetadataRootElement) element).getElementHeader().getGUID()).toList();
    }
}
