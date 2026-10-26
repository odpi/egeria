/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataElementBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataRelationshipBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ControlImplementationStatus;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementVersions;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ControlApplicabilityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.StatementOfApplicabilityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Verify that the properties of a statement of applicability, and of the ControlApplicability relationships that
 * link controls to it, survive the round trip from bean to stored properties and back.
 */
public class StatementOfApplicabilityConverterTest
{
    private final PropertyHelper propertyHelper = new PropertyHelper();

    private final OpenMetadataRootConverter<OpenMetadataRootElement> converter = new OpenMetadataRootConverter<>(propertyHelper, "test", "testServer");


    @Test
    public void testStatementOfApplicabilityRoundTrip() throws Exception
    {
        Date reviewDate = new Date();

        StatementOfApplicabilityProperties properties = new StatementOfApplicabilityProperties();

        properties.setQualifiedName("StatementOfApplicability::Coco Pharmaceuticals ISMS");
        properties.setDisplayName("Coco Pharmaceuticals ISMS Statement of Applicability");
        properties.setVersionIdentifier("1.0");
        properties.setScope("Research, manufacturing and corporate IT");
        properties.setReviewDate(reviewDate);
        properties.setAuthors(List.of("Simon Burr"));
        properties.setContentStatus(ContentStatus.APPROVED);

        OpenMetadataElement element = new OpenMetadataElement();

        element.setElementGUID("soa-guid");
        element.setType(newType(OpenMetadataType.STATEMENT_OF_APPLICABILITY.typeName,
                                List.of(OpenMetadataType.AUTHORED_REFERENCEABLE.typeName,
                                        OpenMetadataType.REFERENCEABLE.typeName,
                                        OpenMetadataType.OPEN_METADATA_ROOT.typeName)));
        element.setVersions(newVersions());
        element.setStatus(ElementStatus.ACTIVE);
        element.setElementProperties(new OpenMetadataElementBuilder().getElementProperties(properties));

        OpenMetadataRootElement bean = converter.getNewComplexBean(OpenMetadataRootElement.class, element, (List<RelatedMetadataElement>) null, "test");

        assertTrue(bean.getProperties() instanceof StatementOfApplicabilityProperties, "Wrong properties class: " + bean.getProperties());

        StatementOfApplicabilityProperties retrieved = (StatementOfApplicabilityProperties) bean.getProperties();

        assertEquals(retrieved.getQualifiedName(), properties.getQualifiedName());
        assertEquals(retrieved.getScope(), properties.getScope());
        assertEquals(retrieved.getReviewDate(), reviewDate);
        assertEquals(retrieved.getAuthors(), List.of("Simon Burr"));
        assertEquals(retrieved.getContentStatus(), ContentStatus.APPROVED);
    }


    @Test
    public void testControlApplicabilityRoundTrip()
    {
        Date reviewDate = new Date();

        ControlApplicabilityProperties properties = new ControlApplicabilityProperties();

        properties.setIsApplicable(false);
        properties.setJustification("No software is developed in house.");
        properties.setInclusionReasons(List.of("Business Requirement"));
        properties.setImplementationStatus(ControlImplementationStatus.PARTIALLY_IMPLEMENTED);
        properties.setImplementationDescription("Covered by the supplier's secure development policy.");
        properties.setReviewDate(reviewDate);
        properties.setLabel("8.25");
        properties.setDescription("Secure development life cycle");

        RelationshipBeanProperties retrieved = convertRelationship(new OpenMetadataRelationshipBuilder().getElementProperties(properties));

        assertTrue(retrieved instanceof ControlApplicabilityProperties, "Wrong properties class: " + retrieved);

        ControlApplicabilityProperties controlApplicability = (ControlApplicabilityProperties) retrieved;

        assertFalse(controlApplicability.getIsApplicable());
        assertEquals(controlApplicability.getJustification(), properties.getJustification());
        assertEquals(controlApplicability.getInclusionReasons(), properties.getInclusionReasons());
        assertEquals(controlApplicability.getImplementationStatus(), ControlImplementationStatus.PARTIALLY_IMPLEMENTED);
        assertEquals(controlApplicability.getImplementationDescription(), properties.getImplementationDescription());
        assertEquals(controlApplicability.getReviewDate(), reviewDate);
        assertEquals(controlApplicability.getLabel(), "8.25");
        assertEquals(controlApplicability.getDescription(), "Secure development life cycle");
    }


    @Test
    public void testControlIsApplicableUnlessStatedOtherwise()
    {
        RelationshipBeanProperties retrieved = convertRelationship(null);

        assertTrue(retrieved instanceof ControlApplicabilityProperties, "Wrong properties class: " + retrieved);
        assertTrue(((ControlApplicabilityProperties) retrieved).getIsApplicable());
    }


    private RelationshipBeanProperties convertRelationship(org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties elementProperties)
    {
        RelatedMetadataElement relationship = new RelatedMetadataElement();

        relationship.setRelationshipGUID("relationship-guid");
        relationship.setType(newType(OpenMetadataType.CONTROL_APPLICABILITY_RELATIONSHIP.typeName, null));
        relationship.setVersions(newVersions());
        relationship.setStatus(ElementStatus.ACTIVE);
        relationship.setRelationshipProperties(elementProperties);

        return converter.getRelationshipProperties(relationship);
    }


    private ElementType newType(String typeName, List<String> superTypeNames)
    {
        ElementType type = new ElementType();

        type.setTypeName(typeName);
        type.setSuperTypeNames(superTypeNames);

        return type;
    }


    private ElementVersions newVersions()
    {
        ElementVersions versions = new ElementVersions();

        versions.setCreatedBy("test");
        versions.setCreateTime(new Date());

        return versions;
    }
}
