/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.mendel;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementVersions;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Verify how MendelDuplicateInstanceRemover decides which instances are exact duplicates and which one is kept.
 */
public class MendelDuplicateInstanceRemoverTest
{
    private final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * Default constructor
     */
    public MendelDuplicateInstanceRemoverTest()
    {
    }


    /**
     * Build a relationship.
     *
     * @param guid relationship GUID
     * @param typeName relationship type
     * @param end1GUID element at end 1
     * @param end2GUID element at end 2
     * @param createTime creation time in milliseconds
     * @param propertyValue value of a string property (or null for no properties)
     * @return relationship
     */
    private OpenMetadataRelationship relationship(String guid,
                                                  String typeName,
                                                  String end1GUID,
                                                  String end2GUID,
                                                  long   createTime,
                                                  String propertyValue)
    {
        OpenMetadataRelationship relationship = new OpenMetadataRelationship();
        ElementType              type         = new ElementType();
        ElementVersions          versions     = new ElementVersions();
        OpenMetadataElementStub  end1         = new OpenMetadataElementStub();
        OpenMetadataElementStub  end2         = new OpenMetadataElementStub();

        type.setTypeName(typeName);
        versions.setCreateTime(new Date(createTime));
        end1.setGUID(end1GUID);
        end2.setGUID(end2GUID);

        relationship.setRelationshipGUID(guid);
        relationship.setRelationshipType(type);
        relationship.setVersions(versions);
        relationship.setElementAtEnd1(end1);
        relationship.setElementAtEnd2(end2);

        if (propertyValue != null)
        {
            relationship.setRelationshipProperties(propertyHelper.addStringProperty(null, "agreementItemId", propertyValue));
        }

        return relationship;
    }


    /**
     * Build an attached element.
     *
     * @param guid element GUID
     * @param keyword keyword display name
     * @param createTime creation time in milliseconds
     * @return attached element with the relationship that attaches it
     */
    private RelatedMetadataElement keyword(String guid,
                                           String keyword,
                                           long   createTime)
    {
        RelatedMetadataElement attachment       = new RelatedMetadataElement();
        ElementType            relationshipType = new ElementType();
        OpenMetadataElement    element          = new OpenMetadataElement();
        ElementType            elementType      = new ElementType();
        ElementVersions        versions         = new ElementVersions();

        relationshipType.setTypeName("SearchKeywordLink");
        attachment.setType(relationshipType);
        attachment.setRelationshipGUID("link-" + guid);

        elementType.setTypeName("SearchKeyword");
        versions.setCreateTime(new Date(createTime));
        element.setType(elementType);
        element.setVersions(versions);
        element.setElementGUID(guid);

        ElementProperties properties = propertyHelper.addStringProperty(null, "displayName", keyword);

        element.setElementProperties(properties);
        attachment.setElement(element);

        return attachment;
    }


    /**
     * Relationships with the same type, ends and properties are one group; the oldest is kept.
     */
    @Test public void testIdenticalRelationshipsKeepOldest()
    {
        List<OpenMetadataRelationship> relationships = List.of(relationship("r2", "AgreementItem", "a", "b", 2000, "item1"),
                                                               relationship("r1", "AgreementItem", "a", "b", 1000, "item1"),
                                                               relationship("r3", "AgreementItem", "a", "b", 3000, "item1"));

        List<MendelDuplicateInstanceRemover.DuplicateGroup<OpenMetadataRelationship>> groups =
                MendelDuplicateInstanceRemover.findDuplicates(relationships, MendelDuplicateInstanceRemover::getRelationshipKey);

        assertEquals(groups.size(), 1);
        assertEquals(groups.get(0).kept().getRelationshipGUID(), "r1");
        assertEquals(groups.get(0).duplicates().stream().map(OpenMetadataRelationship::getRelationshipGUID).toList(), List.of("r2", "r3"));
        assertEquals(MendelDuplicateInstanceRemover.countDuplicates(groups), 2);
    }


    /**
     * Relationships that differ in their properties, ends, direction or type are not duplicates.
     */
    @Test public void testDifferentRelationshipsAreKept()
    {
        List<OpenMetadataRelationship> relationships = List.of(relationship("r1", "AgreementItem", "a", "b", 1000, "item1"),
                                                               relationship("r2", "AgreementItem", "a", "b", 1000, "item2"),
                                                               relationship("r3", "AgreementItem", "a", "c", 1000, "item1"),
                                                               relationship("r4", "AgreementItem", "b", "a", 1000, "item1"),
                                                               relationship("r5", "ExternalReferenceLink", "a", "b", 1000, "item1"),
                                                               relationship("r6", "AgreementItem", "a", "b", 1000, null));

        assertTrue(MendelDuplicateInstanceRemover.findDuplicates(relationships, MendelDuplicateInstanceRemover::getRelationshipKey).isEmpty());
    }


    /**
     * When the creation times are the same, the instance kept is decided by its unique identifier, so a repeat
     * run keeps the same one whatever order the repository returns them in.
     */
    @Test public void testTiesAreStable()
    {
        List<OpenMetadataRelationship> forwards  = List.of(relationship("rA", "AgreementItem", "a", "b", 1000, null),
                                                           relationship("rB", "AgreementItem", "a", "b", 1000, null));
        List<OpenMetadataRelationship> backwards = new ArrayList<>(forwards).reversed();

        assertEquals(MendelDuplicateInstanceRemover.findDuplicates(forwards, MendelDuplicateInstanceRemover::getRelationshipKey).get(0).kept().getRelationshipGUID(), "rA");
        assertEquals(MendelDuplicateInstanceRemover.findDuplicates(backwards, MendelDuplicateInstanceRemover::getRelationshipKey).get(0).kept().getRelationshipGUID(), "rA");
    }


    /**
     * Keywords with the same name attached to one element form one group per name; the oldest keyword is kept.
     */
    @Test public void testDuplicateKeywords()
    {
        List<RelatedMetadataElement> keywords = List.of(keyword("k1", "Reference Data", 1000),
                                                        keyword("k2", "Reference Data", 2000),
                                                        keyword("k3", "Reference Data", 3000),
                                                        keyword("k4", "Security", 4000),
                                                        keyword("k5", "Security", 500),
                                                        keyword("k6", "Governance", 600));

        List<MendelDuplicateInstanceRemover.DuplicateGroup<RelatedMetadataElement>> groups =
                MendelDuplicateInstanceRemover.findDuplicates(keywords, MendelDuplicateInstanceRemover::getAttachmentKey);

        assertEquals(groups.size(), 2);
        assertEquals(groups.get(0).kept().getElement().getElementGUID(), "k1");
        assertEquals(groups.get(0).duplicates().size(), 2);
        assertEquals(groups.get(1).kept().getElement().getElementGUID(), "k5", "the older Security keyword is kept");
        assertEquals(groups.get(1).duplicates().get(0).getElement().getElementGUID(), "k4");
        assertEquals(MendelDuplicateInstanceRemover.countDuplicates(groups), 3);
    }
}
