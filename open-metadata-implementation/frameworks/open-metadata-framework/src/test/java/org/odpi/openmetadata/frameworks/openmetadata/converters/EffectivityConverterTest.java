/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataClassificationBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataElementBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataRelationshipBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ConfidentialityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * Effectivity dates are held on the entity, relationship or classification itself, not in its properties.
 * The builders put them there and the converter must copy them back onto the properties bean.
 */
public class EffectivityConverterTest
{
    private static final Date FROM = new Date(1_700_000_000_000L);
    private static final Date TO   = new Date(1_800_000_000_000L);

    private final OpenMetadataElementBuilder        elementBuilder        = new OpenMetadataElementBuilder();
    private final OpenMetadataRelationshipBuilder   relationshipBuilder   = new OpenMetadataRelationshipBuilder();
    private final OpenMetadataClassificationBuilder classificationBuilder = new OpenMetadataClassificationBuilder();
    private final OpenMetadataPropertyConverterBase converter             = new OpenMetadataPropertyConverterBase(new PropertyHelper(),
                                                                                                                  this.getClass().getName());


    private ElementType getElementType(OpenMetadataType type, OpenMetadataType... superTypes)
    {
        ElementType elementType = new ElementType();

        elementType.setTypeName(type.typeName);

        if (superTypes.length > 0)
        {
            elementType.setSuperTypeNames(java.util.Arrays.stream(superTypes).map(superType -> superType.typeName).toList());
        }

        return elementType;
    }


    private OpenMetadataElement getElement(NewElementProperties newElementProperties)
    {
        OpenMetadataElement element = new OpenMetadataElement();

        element.setType(getElementType(OpenMetadataType.EXTERNAL_REFERENCE,
                                       OpenMetadataType.AUTHORED_REFERENCEABLE,
                                       OpenMetadataType.REFERENCEABLE,
                                       OpenMetadataType.OPEN_METADATA_ROOT));
        element.setElementProperties(newElementProperties);
        element.setEffectiveFromTime(newElementProperties.getEffectiveFrom());
        element.setEffectiveToTime(newElementProperties.getEffectiveTo());

        return element;
    }


    /**
     * Entity effectivity dates and authors both come back after a create and read.
     */
    @Test public void testEntityEffectivityAndAuthorsReturned()
    {
        ExternalReferenceProperties properties = new ExternalReferenceProperties();

        properties.setQualifiedName("ExternalReference::test");
        properties.setAuthors(List.of("Ivor Padlock", "Erin Overview"));
        properties.setEffectiveFrom(FROM);
        properties.setEffectiveTo(TO);

        OpenMetadataRootProperties returned = converter.getBeanProperties(getElement(elementBuilder.getNewElementProperties(properties)));

        assertEquals(returned.getEffectiveFrom(), FROM);
        assertEquals(returned.getEffectiveTo(), TO);
        assertEquals(((ExternalReferenceProperties)returned).getAuthors(), List.of("Ivor Padlock", "Erin Overview"));
        assertNull(returned.getExtendedProperties());
    }


    /**
     * An entity with no effectivity dates comes back with none.
     */
    @Test public void testNoEntityEffectivity()
    {
        ExternalReferenceProperties properties = new ExternalReferenceProperties();

        properties.setQualifiedName("ExternalReference::test");

        OpenMetadataRootProperties returned = converter.getBeanProperties(getElement(elementBuilder.getNewElementProperties(properties)));

        assertNull(returned.getEffectiveFrom());
        assertNull(returned.getEffectiveTo());
    }


    /**
     * Relationship effectivity dates come back with the relationship's properties.
     */
    @Test public void testRelationshipEffectivityReturned()
    {
        ExternalReferenceLinkProperties properties = new ExternalReferenceLinkProperties();

        properties.setLabel("label");
        properties.setEffectiveFrom(FROM);
        properties.setEffectiveTo(TO);

        ElementType              relationshipType = getElementType(OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP);
        OpenMetadataRelationship relationship     = new OpenMetadataRelationship();

        relationship.setType(relationshipType);
        relationship.setRelationshipType(relationshipType);
        relationship.setRelationshipProperties(relationshipBuilder.getElementProperties(properties));
        relationship.setEffectiveFromTime(properties.getEffectiveFrom());
        relationship.setEffectiveToTime(properties.getEffectiveTo());

        RelationshipBeanProperties returned = converter.getRelationshipProperties(relationship);

        assertEquals(returned.getEffectiveFrom(), FROM);
        assertEquals(returned.getEffectiveTo(), TO);
        assertEquals(((ExternalReferenceLinkProperties)returned).getLabel(), "label");
    }


    /**
     * Classification effectivity dates come back with the classification's properties.
     */
    @Test public void testClassificationEffectivityReturned()
    {
        ConfidentialityProperties properties = new ConfidentialityProperties();

        properties.setConfidentialityLevel(3);
        properties.setEffectiveFrom(FROM);
        properties.setEffectiveTo(TO);

        NewElementProperties   newElementProperties = classificationBuilder.getNewElementProperties(properties);
        AttachedClassification classification       = new AttachedClassification();

        classification.setType(getElementType(OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION));
        classification.setClassificationName(OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName);
        classification.setClassificationProperties(newElementProperties);
        classification.setEffectiveFromTime(newElementProperties.getEffectiveFrom());
        classification.setEffectiveToTime(newElementProperties.getEffectiveTo());

        ClassificationBeanProperties returned = converter.getClassificationProperties(classification);

        assertEquals(returned.getEffectiveFrom(), FROM);
        assertEquals(returned.getEffectiveTo(), TO);
        assertEquals(((ConfidentialityProperties)returned).getConfidentialityLevel(), Integer.valueOf(3));
    }
}
