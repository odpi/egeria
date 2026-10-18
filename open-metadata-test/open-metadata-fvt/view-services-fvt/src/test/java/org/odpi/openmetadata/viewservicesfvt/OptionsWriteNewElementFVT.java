/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ConfidentialityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.assetMaker;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.classification;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.collectionManager;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.dataFile;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.named;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.propertyHelper;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.raw;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.rawOrNull;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.record;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.relationships;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.string;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.strings;

/**
 * OptionsWriteNewElementFVT checks the options for creating an element and linking it into the graph:
 * <ul>
 *     <li>AnchorOptions: anchorGUID, isOwnAnchor, anchorScopeGUIDs</li>
 *     <li>NewLinkedElementOptions / NewElementRequestBody: parentGUID, parentRelationshipTypeName, parentAtEnd1,
 *     parentRelationshipProperties, initialClassifications</li>
 *     <li>MakeAnchorOptions (on a link request): makeAnchor, anchorScopeGUIDs</li>
 * </ul>
 * The anchor of an element is recorded in its Anchors classification (anchorGUID, anchorTypeName,
 * anchorScopeGUIDs).
 */
@ExtendWith(OMAGPlatformExtension.class)
public class OptionsWriteNewElementFVT
{
    private static final List<String> created = new ArrayList<>();


    /**
     * isOwnAnchor=true gives the new element an Anchors classification that makes it the anchor of its own graph.
     * It takes precedence over anchorGUID: an element created with isOwnAnchor=true and an anchorGUID is still its
     * own anchor, while the same request with isOwnAnchor=false anchors it to the anchorGUID element (see also
     * anchorGUIDAndAnchorScopeGUIDs).
     * <br>
     * (An element created with isOwnAnchor=false and no anchorGUID is not left without an Anchors classification
     * for long: the first read derives its anchor from its relationships - OpenMetadataAPIAnchorHandler
     * .getAnchorGUIDForEntity - and, finding none, records it as its own anchor.  So that case is not asserted.)
     */
    @Test
    void isOwnAnchor()
    {
        String otherElement = createCollection("not-the-anchor", null);
        String ownAnchor    = create(dataFile("own-anchor"), true, otherElement, null);

        AttachedClassification anchors = anchors(ownAnchor);

        assertNotNull(anchors, "isOwnAnchor=true did not add an Anchors classification");

        String anchorGUID = string(anchors.getClassificationProperties(), OpenMetadataProperty.ANCHOR_GUID.name);

        assertTrue((anchorGUID == null) || ownAnchor.equals(anchorGUID),
                   "an element created as its own anchor is anchored to another element: " + anchorGUID);
        assertEquals(OpenMetadataType.DATA_FILE.typeName, string(anchors.getClassificationProperties(), OpenMetadataProperty.ANCHOR_TYPE_NAME.name));

        String anchored = create(dataFile("not-own-anchor"), false, otherElement, null);

        assertEquals(otherElement, string(anchors(anchored).getClassificationProperties(), OpenMetadataProperty.ANCHOR_GUID.name),
                     "isOwnAnchor=false did not let anchorGUID anchor the element");
    }


    /**
     * anchorGUID (with isOwnAnchor=false) anchors the new element to another element, with the anchorScopeGUIDs
     * supplied.  The anchored element is part of its anchor's graph, so deleting the anchor deletes it too.
     */
    @Test
    void anchorGUIDAndAnchorScopeGUIDs()
    {
        String scope  = createCollection("anchor-scope", null);
        String anchor = createCollection("anchor", null);
        String member = create(dataFile("anchored"), false, anchor, List.of(scope));

        AttachedClassification anchors = anchors(member);

        assertNotNull(anchors, "anchorGUID did not add an Anchors classification");
        assertEquals(anchor, string(anchors.getClassificationProperties(), OpenMetadataProperty.ANCHOR_GUID.name));
        assertEquals(OpenMetadataType.COLLECTION.typeName, string(anchors.getClassificationProperties(), OpenMetadataProperty.ANCHOR_TYPE_NAME.name));
        assertEquals(List.of(scope), strings(anchors.getClassificationProperties(), OpenMetadataProperty.ANCHOR_SCOPE_GUIDS.name));

        assetMaker.call("/assets/" + anchor + "/delete", new DeleteElementRequestBody());

        assertNull(rawOrNull(member, new GetRequestBody()), "deleting the anchor did not delete the element anchored to it");
    }


    /**
     * When anchorScopeGUIDs is not supplied, the value set in the anchor's own Anchors classification is used.
     */
    @Test
    void anchorScopeGUIDsDefaultsToTheAnchorsScope()
    {
        String scope  = createCollection("inherited-scope", null);
        String anchor = createCollection("scoped-anchor", List.of(scope));

        assertEquals(List.of(scope), strings(anchors(anchor).getClassificationProperties(), OpenMetadataProperty.ANCHOR_SCOPE_GUIDS.name),
                     "anchorScopeGUIDs on an own-anchor create was not recorded");

        String member = create(dataFile("inherits-scope"), false, anchor, null);

        assertEquals(List.of(scope), strings(anchors(member).getClassificationProperties(), OpenMetadataProperty.ANCHOR_SCOPE_GUIDS.name),
                     "the new element did not take its anchor's scope");
    }


    /**
     * parentGUID, parentRelationshipTypeName and parentRelationshipProperties with parentAtEnd1=true: the new
     * element is linked to the parent with the parent at end 1 and the relationship carries the properties.
     */
    @Test
    void parentAtEnd1True()
    {
        String parent = createCollection("parent-at-end1", null);

        CollectionMembershipProperties relationshipProperties = new CollectionMembershipProperties();

        relationshipProperties.setMembershipRationale("Linked by parentGUID");

        NewElementRequestBody requestBody = newElement(dataFile("child-at-end2"), true);

        requestBody.setParentGUID(parent);
        requestBody.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);
        requestBody.setParentAtEnd1(true);
        requestBody.setParentRelationshipProperties(relationshipProperties);

        String child = record(assetMaker.create("/assets", requestBody), created);

        List<OpenMetadataRelationship> links = relationships(parent, OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName, child);

        assertEquals(1, links.size(), "the new element is not linked to its parent");
        assertEquals(parent, links.get(0).getElementGUIDAtEnd1(), "parentAtEnd1=true did not put the parent at end 1");
        assertEquals(child, links.get(0).getElementGUIDAtEnd2());
        assertEquals("Linked by parentGUID", string(links.get(0).getRelationshipProperties(), OpenMetadataProperty.MEMBERSHIP_RATIONALE.name),
                     "the parent relationship does not carry parentRelationshipProperties");
    }


    /**
     * parentAtEnd1=false puts the parent at end 2 and the new element at end 1.
     */
    @Test
    void parentAtEnd1False()
    {
        String parent = assetCreate(dataFile("parent-at-end2"));

        ResourceListProperties relationshipProperties = new ResourceListProperties();

        relationshipProperties.setResourceUse("Linked with the parent at end 2");

        NewElementRequestBody requestBody = newElement(dataFile("child-at-end1"), true);

        requestBody.setParentGUID(parent);
        requestBody.setParentRelationshipTypeName(OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName);
        requestBody.setParentAtEnd1(false);
        requestBody.setParentRelationshipProperties(relationshipProperties);

        String child = record(assetMaker.create("/assets", requestBody), created);

        List<OpenMetadataRelationship> links = relationships(child, OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName, parent);

        assertEquals(1, links.size(), "the new element is not linked to its parent");
        assertEquals(child, links.get(0).getElementGUIDAtEnd1(), "parentAtEnd1=false did not put the new element at end 1");
        assertEquals(parent, links.get(0).getElementGUIDAtEnd2(), "parentAtEnd1=false did not put the parent at end 2");
        assertEquals("Linked with the parent at end 2", string(links.get(0).getRelationshipProperties(), OpenMetadataProperty.RESOURCE_USE.name));
    }


    /**
     * initialClassifications are attached, with their properties, when the element is created.
     */
    @Test
    void initialClassifications()
    {
        ConfidentialityProperties confidentiality = new ConfidentialityProperties();

        confidentiality.setConfidentialityLevel(3);

        NewElementRequestBody requestBody = newElement(dataFile("initially-classified"), true);

        requestBody.setInitialClassifications(Map.<String, ClassificationProperties>of(OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName, confidentiality));

        String guid = record(assetMaker.create("/assets", requestBody), created);

        AttachedClassification attached = classification(raw(guid), OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName);

        assertNotNull(attached, "the initial classification was not attached");
        assertEquals(3, propertyHelper.getIntProperty("OptionsWriteNewElementFVT",
                                                      OpenMetadataProperty.CONFIDENTIALITY_LEVEL.name,
                                                      attached.getClassificationProperties(),
                                                      "initialClassifications"));

        assertNull(classification(raw(create(dataFile("not-classified"), true, null, null)), OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName),
                   "an element created without initialClassifications is classified");
    }


    /**
     * MakeAnchorOptions on a link request: makeAnchor=true anchors the element at end 2 to the element at end 1,
     * with the anchorScopeGUIDs supplied; makeAnchor=false (the default) leaves the end 2 element's anchor alone.
     */
    @Test
    void makeAnchorAndItsAnchorScopeGUIDs()
    {
        String scope      = createCollection("make-anchor-scope", null);
        String collection = createCollection("make-anchor", null);
        String anchored   = create(dataFile("made-anchored"), false, null, null);
        String linkedOnly = create(dataFile("linked-only"), false, null, null);

        NewRelationshipRequestBody makeAnchor = new NewRelationshipRequestBody();

        makeAnchor.setMakeAnchor(true);
        makeAnchor.setAnchorScopeGUIDs(List.of(scope));

        collectionManager.call("/collections/" + collection + "/members/" + anchored + "/attach", makeAnchor);
        collectionManager.call("/collections/" + collection + "/members/" + linkedOnly + "/attach", new NewRelationshipRequestBody());

        AttachedClassification anchors = anchors(anchored);

        assertNotNull(anchors, "makeAnchor=true did not anchor the member");
        assertEquals(collection, string(anchors.getClassificationProperties(), OpenMetadataProperty.ANCHOR_GUID.name));
        assertEquals(List.of(scope), strings(anchors.getClassificationProperties(), OpenMetadataProperty.ANCHOR_SCOPE_GUIDS.name),
                     "makeAnchor did not record the anchorScopeGUIDs");

        AttachedClassification linkedOnlyAnchors = anchors(linkedOnly);

        assertTrue((linkedOnlyAnchors == null) ||
                           (! collection.equals(string(linkedOnlyAnchors.getClassificationProperties(), OpenMetadataProperty.ANCHOR_GUID.name))),
                   "makeAnchor=false anchored the member to the collection");
    }


    private static AttachedClassification anchors(String guid)
    {
        OpenMetadataElement element = raw(guid);

        return classification(element, OpenMetadataType.ANCHORS_CLASSIFICATION.typeName);
    }


    private static NewElementRequestBody newElement(org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties properties,
                                                    boolean                                                                     isOwnAnchor)
    {
        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(isOwnAnchor);
        requestBody.setProperties(properties);

        return requestBody;
    }


    private static String assetCreate(org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties properties)
    {
        return create(properties, true, null, null);
    }


    private static String create(org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties properties,
                                 boolean                                                                     isOwnAnchor,
                                 String                                                                      anchorGUID,
                                 List<String>                                                                anchorScopeGUIDs)
    {
        NewElementRequestBody requestBody = newElement(properties, isOwnAnchor);

        requestBody.setAnchorGUID(anchorGUID);
        requestBody.setAnchorScopeGUIDs(anchorScopeGUIDs);

        return record(assetMaker.create("/assets", requestBody), created);
    }


    private static String createCollection(String       name,
                                           List<String> anchorScopeGUIDs)
    {
        NewElementRequestBody requestBody = newElement(named(new CollectionProperties(), "Collection", name), true);

        requestBody.setAnchorScopeGUIDs(anchorScopeGUIDs);

        return record(collectionManager.create("/collections", requestBody), created);
    }


    @AfterAll
    static void cleanUp()
    {
        OptionsWriteSupport.purgeAll(created);
    }
}
