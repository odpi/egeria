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
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewExternalIdRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateRelationshipRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.enums.KeyPattern;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.CitedDocumentLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.CitedDocumentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.MediaReferenceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.RelatedMediaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.LocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ExternalLinksFVT covers every request of the External Links OMVS: external references (plain, media and cited
 * documents) with their three kinds of link to the elements they describe, and external identifiers with
 * their links.  The elements the references and identifiers are attached to are locations created through the
 * Location Arena.  Each link is attached, updated and detached by relationship GUID, then attached again and
 * detached by the GUIDs of its two ends, so both forms of each detach request are checked.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExternalLinksFVT
{
    private static final ViewServiceClient externalLinks = new ViewServiceClient(ViewServiceDescription.EXTERNAL_LINKS);
    private static final ViewServiceClient locationArena = new ViewServiceClient(ViewServiceDescription.LOCATION_ARENA);

    private static String subjectGUID      = null;
    private static String otherSubjectGUID = null;

    private static String referenceQualifiedName = null;
    private static String referenceGUID          = null;
    private static String copyGUID               = null;
    private static String mediaGUID              = null;
    private static String citedDocumentGUID      = null;

    private static String externalIdQualifiedName = null;
    private static String externalIdGUID          = null;


    /**
     * Create the subjects, an external reference, a media reference and a cited document, and read them back.
     */
    @Test
    @Order(1)
    void createAndRetrieveExternalReferences()
    {
        subjectGUID      = createLocation("subject");
        otherSubjectGUID = createLocation("other-subject");

        referenceQualifiedName = ViewServicesFvtTestSupport.qualifiedName("ExternalReference", "standard");

        ExternalReferenceProperties referenceProperties = new ExternalReferenceProperties();

        referenceProperties.setQualifiedName(referenceQualifiedName);
        referenceProperties.setDisplayName("Data standard");
        referenceProperties.setReferenceTitle("The data standard");
        referenceProperties.setOrganization("Standards body");
        referenceProperties.setURL("https://example.com/standard");

        referenceGUID = createExternalReference(referenceProperties);

        RelatedMediaProperties mediaProperties = new RelatedMediaProperties();

        mediaProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("RelatedMedia", "diagram"));
        mediaProperties.setDisplayName("Site diagram");
        mediaProperties.setURL("https://example.com/diagram.png");

        mediaGUID = createExternalReference(mediaProperties);

        CitedDocumentProperties citedDocumentProperties = new CitedDocumentProperties();

        citedDocumentProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("CitedDocument", "paper"));
        citedDocumentProperties.setDisplayName("Research paper");
        citedDocumentProperties.setNumberOfPages(12);
        citedDocumentProperties.setPublisher("Journal of Metadata");

        citedDocumentGUID = createExternalReference(citedDocumentProperties);

        ExternalReferenceProperties reference = (ExternalReferenceProperties) retrieveReference(referenceGUID, ExternalReferenceProperties.class);

        assertEquals(referenceQualifiedName, reference.getQualifiedName());
        assertEquals("The data standard", reference.getReferenceTitle());
        assertEquals("Standards body", reference.getOrganization());

        assertEquals(OpenMetadataType.RELATED_MEDIA.typeName, retrieve(mediaGUID).getElementHeader().getType().getTypeName());

        CitedDocumentProperties citedDocument = (CitedDocumentProperties) retrieveReference(citedDocumentGUID, CitedDocumentProperties.class);

        assertEquals(12, citedDocument.getNumberOfPages());
        assertEquals("Journal of Metadata", citedDocument.getPublisher());
    }


    /**
     * A merge update changes only the properties it supplies.
     */
    @Test
    @Order(2)
    void updateExternalReference()
    {
        ExternalReferenceProperties properties = new ExternalReferenceProperties();

        properties.setReferenceAbstract("What the standard covers");

        UpdateElementRequestBody requestBody = new UpdateElementRequestBody();

        requestBody.setMergeUpdate(true);
        requestBody.setProperties(properties);

        externalLinks.post("/external-references/" + referenceGUID + "/update", requestBody, BooleanResponse.class);

        ExternalReferenceProperties updated = (ExternalReferenceProperties) retrieveReference(referenceGUID, ExternalReferenceProperties.class);

        assertEquals("What the standard covers", updated.getReferenceAbstract());
        assertEquals("The data standard", updated.getReferenceTitle(), "a merge update lost the reference title");
    }


    /**
     * An external reference created from a template copies the template's properties.
     */
    @Test
    @Order(3)
    void createExternalReferenceFromTemplate()
    {
        String copyQualifiedName = ViewServicesFvtTestSupport.qualifiedName("ExternalReference", "copy");

        ExternalReferenceProperties replacementProperties = new ExternalReferenceProperties();

        replacementProperties.setQualifiedName(copyQualifiedName);

        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setTemplateGUID(referenceGUID);
        requestBody.setReplacementProperties(replacementProperties);

        copyGUID = externalLinks.create("/external-references/from-template", requestBody);

        assertNotEquals(referenceGUID, copyGUID);

        ExternalReferenceProperties copy = (ExternalReferenceProperties) retrieveReference(copyGUID, ExternalReferenceProperties.class);

        assertEquals(copyQualifiedName, copy.getQualifiedName());
        assertEquals("The data standard", copy.getReferenceTitle(), "the template's reference title was not copied");
    }


    /**
     * Retrieve by name finds exactly the standard; a search finds all of this run's references.
     */
    @Test
    @Order(4)
    void findExternalReferences()
    {
        FilterRequestBody filterRequestBody = new FilterRequestBody();

        filterRequestBody.setFilter(referenceQualifiedName);

        List<OpenMetadataRootElement> byName = externalLinks.elements("/external-references/by-name", filterRequestBody);

        assertEquals(1, byName.size(), "by-name should return just the standard");
        assertEquals(referenceGUID, byName.get(0).getElementHeader().getGUID());

        SearchStringRequestBody searchRequestBody = new SearchStringRequestBody();

        searchRequestBody.setSearchString(ViewServicesFvtTestSupport.runSearchString());

        List<String> found = externalLinks.elements("/external-references/by-search-string", searchRequestBody)
                                          .stream().map(element -> element.getElementHeader().getGUID()).toList();

        assertTrue(found.containsAll(List.of(referenceGUID, copyGUID, mediaGUID, citedDocumentGUID)),
                   "by-search-string missed some of this run's external references: " + found);
        assertFalse(found.contains(subjectGUID), "by-search-string returned a location");
    }


    /**
     * The external reference link: attach, update and detach by relationship GUID, then attach and detach by the
     * GUIDs of its ends.
     */
    @Test
    @Order(5)
    void externalReferenceLinks()
    {
        ExternalReferenceLinkProperties properties = new ExternalReferenceLinkProperties();
        properties.setLabel("conforms to");

        ExternalReferenceLinkProperties updatedProperties = new ExternalReferenceLinkProperties();
        updatedProperties.setLabel("must conform to");

        linkLifecycle("/external-references/", referenceGUID,
                      "/external-reference-links/",
                      properties, updatedProperties,
                      "label", "conforms to", "must conform to",
                      OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName);
    }


    /**
     * The media reference link, as for the external reference link.
     */
    @Test
    @Order(6)
    void mediaReferenceLinks()
    {
        MediaReferenceProperties properties = new MediaReferenceProperties();
        properties.setMediaId("figure-1");

        MediaReferenceProperties updatedProperties = new MediaReferenceProperties();
        updatedProperties.setMediaId("figure-2");

        linkLifecycle("/media-references/", mediaGUID,
                      "/media-reference-links/",
                      properties, updatedProperties,
                      "mediaId", "figure-1", "figure-2",
                      OpenMetadataType.MEDIA_REFERENCE_RELATIONSHIP.typeName);
    }


    /**
     * The cited document link, as for the external reference link.
     */
    @Test
    @Order(7)
    void citedDocumentLinks()
    {
        CitedDocumentLinkProperties properties = new CitedDocumentLinkProperties();
        properties.setPages("3-4");

        CitedDocumentLinkProperties updatedProperties = new CitedDocumentLinkProperties();
        updatedProperties.setPages("5-9");

        linkLifecycle("/cited-document-references/", citedDocumentGUID,
                      "/cited-document-references/",
                      properties, updatedProperties,
                      "pages", "3-4", "5-9",
                      OpenMetadataType.CITED_DOCUMENT_LINK_RELATIONSHIP.typeName);
    }


    /**
     * An external identifier is added to an element, found by name and search, updated, its synchronization
     * confirmed, linked to a second element and detached from it.
     */
    @Test
    @Order(8)
    void externalIdentifiers()
    {
        externalIdQualifiedName = ViewServicesFvtTestSupport.qualifiedName("ExternalId", "site-42");

        ExternalIdProperties properties = new ExternalIdProperties();

        properties.setQualifiedName(externalIdQualifiedName);
        properties.setKey("site-42");
        properties.setKeyPattern(KeyPattern.LOCAL_KEY);
        properties.setExternalInstanceTypeName("Site");

        ExternalIdLinkProperties linkProperties = new ExternalIdLinkProperties();

        linkProperties.setUsage("Site register key");
        linkProperties.setPermittedSynchronization(PermittedSynchronization.BOTH_DIRECTIONS);

        NewExternalIdRequestBody requestBody = new NewExternalIdRequestBody();

        requestBody.setProperties(properties);
        requestBody.setParentRelationshipProperties(linkProperties);

        /*
         * addExternalIdentifier returns no GUID, so the new identifier is found by name.
         */
        externalLinks.call("/elements/" + subjectGUID + "/external-identifiers/add", requestBody);

        FilterRequestBody filterRequestBody = new FilterRequestBody();

        filterRequestBody.setFilter(externalIdQualifiedName);

        List<OpenMetadataRootElement> byName = externalLinks.elements("/external-identifiers/by-name", filterRequestBody);

        assertEquals(1, byName.size(), "by-name should return the new external identifier");

        externalIdGUID = byName.get(0).getElementHeader().getGUID();

        assertEquals("site-42", externalId().getKey());

        String externalIdLink = OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeName;

        assertEquals(List.of(subjectGUID), MetadataExpertProbe.linkedGUIDs(externalIdGUID, externalIdLink),
                     "the new external identifier should be linked to its element");

        SearchStringRequestBody searchRequestBody = new SearchStringRequestBody();

        searchRequestBody.setSearchString(ViewServicesFvtTestSupport.runSearchString() + "::ExternalId::");

        assertTrue(externalLinks.elements("/external-identifiers/by-search-string", searchRequestBody)
                                .stream().anyMatch(element -> externalIdGUID.equals(element.getElementHeader().getGUID())),
                   "by-search-string did not find the external identifier");

        ExternalIdProperties updateProperties = new ExternalIdProperties();

        updateProperties.setExternalInstanceLastUpdatedBy("site-registrar");

        UpdateElementRequestBody updateRequestBody = new UpdateElementRequestBody();

        updateRequestBody.setMergeUpdate(true);
        updateRequestBody.setProperties(updateProperties);

        externalLinks.post("/external-identifiers/" + externalIdGUID + "/update", updateRequestBody, BooleanResponse.class);

        assertEquals("site-registrar", externalId().getExternalInstanceLastUpdatedBy());
        assertEquals("site-42", externalId().getKey(), "a merge update lost the key");

        /*
         * confirmSynchronization updates the ExternalIdLink relationship.
         */
        List<String> linkGUIDs = MetadataExpertProbe.relationshipGUIDs(externalIdGUID, externalIdLink);

        assertEquals(1, linkGUIDs.size());

        ExternalIdLinkProperties synchronizedProperties = new ExternalIdLinkProperties();

        synchronizedProperties.setLastSynchronized(new Date());
        synchronizedProperties.setSource("nightly sync");

        UpdateRelationshipRequestBody synchronizedRequestBody = new UpdateRelationshipRequestBody();

        synchronizedRequestBody.setMergeUpdate(true);
        synchronizedRequestBody.setProperties(synchronizedProperties);

        externalLinks.call("/external-identifiers/links/" + linkGUIDs.get(0) + "/synchronized", synchronizedRequestBody);

        assertEquals("nightly sync", MetadataExpertProbe.relationshipStringProperty(linkGUIDs.get(0), "source"));

        /*
         * The same identifier can describe a second element.
         */
        NewRelationshipRequestBody linkRequestBody = new NewRelationshipRequestBody();

        linkRequestBody.setProperties(new ExternalIdLinkProperties());

        externalLinks.call("/elements/" + otherSubjectGUID + "/external-ids/" + externalIdGUID + "/attach", linkRequestBody);

        assertTrue(MetadataExpertProbe.linkedGUIDs(externalIdGUID, externalIdLink).containsAll(List.of(subjectGUID, otherSubjectGUID)),
                   "the external identifier should now describe both elements");

        externalLinks.call("/elements/" + otherSubjectGUID + "/external-ids/" + externalIdGUID + "/detach", new DeleteRelationshipRequestBody());

        assertEquals(List.of(subjectGUID), MetadataExpertProbe.linkedGUIDs(externalIdGUID, externalIdLink),
                     "detach did not remove the link to the second element");
    }


    /**
     * Delete everything this test created.
     */
    @Test
    @Order(9)
    void deleteExternalReferencesAndIdentifiers()
    {
        externalLinks.call("/external-identifiers/" + externalIdGUID + "/delete", new DeleteElementRequestBody());
        ViewServicesFvtTestSupport.assertGone(externalLinks, "/external-identifiers/" + externalIdGUID + "/retrieve");

        for (String guid : List.of(copyGUID, mediaGUID, citedDocumentGUID, referenceGUID))
        {
            externalLinks.call("/external-references/" + guid + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(externalLinks, "/external-references/" + guid + "/retrieve");
        }

        for (String guid : List.of(subjectGUID, otherSubjectGUID))
        {
            locationArena.call("/locations/" + guid + "/delete", new DeleteElementRequestBody());
        }
    }


    private static String createLocation(String name)
    {
        LocationProperties properties = new LocationProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Location", name));
        properties.setDisplayName(name);

        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        return locationArena.create("/locations", requestBody);
    }


    private static String createExternalReference(ExternalReferenceProperties properties)
    {
        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        return externalLinks.create("/external-references", requestBody);
    }


    private static OpenMetadataRootElement retrieve(String externalReferenceGUID)
    {
        OpenMetadataRootElement element = externalLinks.element("/external-references/" + externalReferenceGUID + "/retrieve", new GetRequestBody());

        assertNotNull(element, "external reference " + externalReferenceGUID + " is missing");
        assertEquals(externalReferenceGUID, element.getElementHeader().getGUID());

        return element;
    }


    private static OpenMetadataRootProperties retrieveReference(String   externalReferenceGUID,
                                                                Class<?> propertiesClass)
    {
        OpenMetadataRootProperties properties = retrieve(externalReferenceGUID).getProperties();

        assertTrue(propertiesClass.isInstance(properties), "retrieve returned " + properties);

        return properties;
    }


    private static ExternalIdProperties externalId()
    {
        OpenMetadataRootElement element = externalLinks.element("/external-identifiers/" + externalIdGUID + "/retrieve", new GetRequestBody());

        assertNotNull(element, "external identifier " + externalIdGUID + " is missing");
        assertTrue(element.getProperties() instanceof ExternalIdProperties, "retrieve returned " + element.getProperties());

        return (ExternalIdProperties) element.getProperties();
    }


    /**
     * Attach a link, update it and detach it by relationship GUID, then attach it again and detach it by the
     * GUIDs of its two ends.
     */
    private static void linkLifecycle(String                 attachSegment,
                                      String                 externalReferenceGUID,
                                      String                 relationshipSegment,
                                      RelationshipProperties properties,
                                      RelationshipProperties updatedProperties,
                                      String                 propertyName,
                                      String                 value,
                                      String                 updatedValue,
                                      String                 relationshipTypeName)
    {
        String attachPath = "/elements/" + subjectGUID + attachSegment + externalReferenceGUID;

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        String relationshipGUID = externalLinks.create(attachPath + "/attach", requestBody);

        assertEquals(List.of(relationshipGUID), MetadataExpertProbe.relationshipGUIDs(externalReferenceGUID, relationshipTypeName),
                     attachPath + "/attach did not return the GUID of the " + relationshipTypeName + " relationship it created");
        assertTrue(MetadataExpertProbe.linkedGUIDs(subjectGUID, relationshipTypeName).contains(externalReferenceGUID));
        assertEquals(value, MetadataExpertProbe.relationshipStringProperty(relationshipGUID, propertyName));

        UpdateRelationshipRequestBody updateRequestBody = new UpdateRelationshipRequestBody();

        updateRequestBody.setMergeUpdate(true);
        updateRequestBody.setProperties(updatedProperties);

        externalLinks.call(relationshipSegment + relationshipGUID + "/update", updateRequestBody);

        assertEquals(updatedValue, MetadataExpertProbe.relationshipStringProperty(relationshipGUID, propertyName),
                     relationshipSegment + "{guid}/update did not change " + propertyName);

        externalLinks.call(relationshipSegment + relationshipGUID + "/detach", new DeleteRelationshipRequestBody());

        assertTrue(MetadataExpertProbe.relationshipGUIDs(externalReferenceGUID, relationshipTypeName).isEmpty(),
                   relationshipSegment + "{guid}/detach did not remove the " + relationshipTypeName + " relationship");

        externalLinks.create(attachPath + "/attach", requestBody);

        assertTrue(MetadataExpertProbe.linkedGUIDs(subjectGUID, relationshipTypeName).contains(externalReferenceGUID));

        externalLinks.call(attachPath + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(MetadataExpertProbe.linkedGUIDs(subjectGUID, relationshipTypeName).contains(externalReferenceGUID),
                    attachPath + "/detach did not remove the " + relationshipTypeName + " relationship");
    }
}
