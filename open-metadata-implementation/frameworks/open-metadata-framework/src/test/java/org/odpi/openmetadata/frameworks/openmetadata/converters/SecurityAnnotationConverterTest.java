/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataElementBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementVersions;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.PenetrationTestFindingAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.SecurityFindingAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.SecurityPostureAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.VulnerabilityAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Verify that the properties of the security annotations survive the round trip from bean to stored properties and
 * back, and that each comes back as the right bean.
 */
public class SecurityAnnotationConverterTest
{
    private final PropertyHelper propertyHelper = new PropertyHelper();

    private final OpenMetadataRootConverter<OpenMetadataRootElement> converter = new OpenMetadataRootConverter<>(propertyHelper, "test", "testServer");

    private static final List<String> ANNOTATION_SUPER_TYPES = List.of(OpenMetadataType.ANNOTATION.typeName,
                                                                       OpenMetadataType.AUTHORED_REFERENCEABLE.typeName,
                                                                       OpenMetadataType.REFERENCEABLE.typeName,
                                                                       OpenMetadataType.OPEN_METADATA_ROOT.typeName);


    @Test
    public void testVulnerabilityAnnotationRoundTrip() throws Exception
    {
        VulnerabilityAnnotationProperties properties = new VulnerabilityAnnotationProperties();

        properties.setQualifiedName("VulnerabilityAnnotation::log4shell");
        properties.setIdentifier("TRIVY-0001");
        properties.setSeverity("Critical");
        properties.setCvssScore(10.0f);
        properties.setCvssVector("CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:C/C:H/I:H/A:H");
        properties.setVulnerabilityIdentifiers(List.of("CVE-2021-44228"));
        properties.setWeaknessIdentifiers(List.of("CWE-502", "CWE-917"));
        properties.setAffectedLocation("pkg:maven/org.apache.logging.log4j/log4j-core@2.14.1");
        properties.setRemediation("Upgrade log4j-core to 2.17.1 or later.");
        properties.setExploitAvailable(true);
        properties.setPackageURL("pkg:maven/org.apache.logging.log4j/log4j-core@2.14.1");
        properties.setInstalledVersion("2.14.1");
        properties.setFixedVersion("2.17.1");
        properties.setPatchAvailable(true);

        VulnerabilityAnnotationProperties retrieved = (VulnerabilityAnnotationProperties) roundTrip(properties,
                                                                                                      OpenMetadataType.VULNERABILITY_ANNOTATION.typeName,
                                                                                                      OpenMetadataType.SECURITY_FINDING_ANNOTATION.typeName);

        assertEquals(retrieved.getIdentifier(), "TRIVY-0001");
        assertEquals(retrieved.getSeverity(), "Critical");
        assertEquals(retrieved.getCvssScore(), 10.0f);
        assertEquals(retrieved.getCvssVector(), properties.getCvssVector());
        assertEquals(retrieved.getVulnerabilityIdentifiers(), List.of("CVE-2021-44228"));
        assertEquals(retrieved.getWeaknessIdentifiers(), List.of("CWE-502", "CWE-917"));
        assertEquals(retrieved.getAffectedLocation(), properties.getAffectedLocation());
        assertEquals(retrieved.getRemediation(), properties.getRemediation());
        assertTrue(retrieved.getExploitAvailable());
        assertEquals(retrieved.getPackageURL(), properties.getPackageURL());
        assertEquals(retrieved.getInstalledVersion(), "2.14.1");
        assertEquals(retrieved.getFixedVersion(), "2.17.1");
        assertTrue(retrieved.getPatchAvailable());
    }


    @Test
    public void testPenetrationTestFindingAnnotationRoundTrip() throws Exception
    {
        PenetrationTestFindingAnnotationProperties properties = new PenetrationTestFindingAnnotationProperties();

        properties.setQualifiedName("PenetrationTestFindingAnnotation::sql-injection");
        properties.setSeverity("High");
        properties.setCvssScore(8.6f);
        properties.setEvidence("Payload ' OR 1=1 -- returned every supplier record.");
        properties.setAttackVector("Network");
        properties.setAttackTechniques(List.of("T1190"));
        properties.setPrivilegesGained("Read access to the supplier database");
        properties.setCompromisedResources(List.of("Supplier Master Register"));
        properties.setReproductionSteps("1. Open the supplier search page.\n2. Enter the payload in the name field.");

        PenetrationTestFindingAnnotationProperties retrieved = (PenetrationTestFindingAnnotationProperties) roundTrip(properties,
                                                                                                                        OpenMetadataType.PENETRATION_TEST_FINDING_ANNOTATION.typeName,
                                                                                                                        OpenMetadataType.SECURITY_FINDING_ANNOTATION.typeName);

        assertEquals(retrieved.getSeverity(), "High");
        assertEquals(retrieved.getCvssScore(), 8.6f);
        assertEquals(retrieved.getEvidence(), properties.getEvidence());
        assertEquals(retrieved.getAttackVector(), "Network");
        assertEquals(retrieved.getAttackTechniques(), List.of("T1190"));
        assertEquals(retrieved.getPrivilegesGained(), properties.getPrivilegesGained());
        assertEquals(retrieved.getCompromisedResources(), List.of("Supplier Master Register"));
        assertEquals(retrieved.getReproductionSteps(), properties.getReproductionSteps());
    }


    @Test
    public void testSecurityFindingAnnotationRoundTrip() throws Exception
    {
        SecurityFindingAnnotationProperties properties = new SecurityFindingAnnotationProperties();

        properties.setQualifiedName("SecurityFindingAnnotation::open-port");
        properties.setSeverity("Medium");
        properties.setAffectedLocation("10.0.4.12:23");

        SecurityFindingAnnotationProperties retrieved = (SecurityFindingAnnotationProperties) roundTrip(properties,
                                                                                                          OpenMetadataType.SECURITY_FINDING_ANNOTATION.typeName);

        assertEquals(retrieved.getClass(), SecurityFindingAnnotationProperties.class);
        assertEquals(retrieved.getSeverity(), "Medium");
        assertEquals(retrieved.getAffectedLocation(), "10.0.4.12:23");
    }


    @Test
    public void testSecurityPostureAnnotationRoundTrip() throws Exception
    {
        SecurityPostureAnnotationProperties properties = new SecurityPostureAnnotationProperties();

        properties.setQualifiedName("SecurityPostureAnnotation::weekly-scan");
        properties.setFindingCountsBySeverity(Map.of("Critical", 1L, "High", 4L, "Medium", 12L));
        properties.setResourcesInScopeCount(120);
        properties.setResourcesTestedCount(118);
        properties.setNewFindingCount(4);
        properties.setResolvedFindingCount(7);
        properties.setResourceProperties(Map.of("scanner", "Trivy"));

        SecurityPostureAnnotationProperties retrieved = (SecurityPostureAnnotationProperties) roundTrip(properties,
                                                                                                          OpenMetadataType.SECURITY_POSTURE_ANNOTATION.typeName,
                                                                                                          OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName);

        assertEquals(retrieved.getFindingCountsBySeverity(), properties.getFindingCountsBySeverity());
        assertEquals(retrieved.getResourcesInScopeCount(), 120);
        assertEquals(retrieved.getResourcesTestedCount(), 118);
        assertEquals(retrieved.getNewFindingCount(), 4);
        assertEquals(retrieved.getResolvedFindingCount(), 7);
        assertEquals(retrieved.getResourceProperties(), Map.of("scanner", "Trivy"));
    }


    /**
     * Build the stored properties from the bean, then convert them back into a bean.
     *
     * @param properties bean to store
     * @param typeName type of the element
     * @param extraSuperTypes super types between the element's type and Annotation
     * @return retrieved bean
     * @throws Exception problem in the conversion
     */
    private EntityProperties roundTrip(EntityProperties properties,
                                       String           typeName,
                                       String...        extraSuperTypes) throws Exception
    {
        List<String> superTypes = new ArrayList<>(List.of(extraSuperTypes));

        superTypes.addAll(ANNOTATION_SUPER_TYPES);

        ElementType type = new ElementType();

        type.setTypeName(typeName);
        type.setSuperTypeNames(superTypes);

        ElementVersions versions = new ElementVersions();

        versions.setCreatedBy("test");
        versions.setCreateTime(new Date());

        OpenMetadataElement element = new OpenMetadataElement();

        element.setElementGUID("annotation-guid");
        element.setType(type);
        element.setVersions(versions);
        element.setStatus(ElementStatus.ACTIVE);
        element.setElementProperties(new OpenMetadataElementBuilder().getElementProperties(properties));

        OpenMetadataRootElement bean = converter.getNewComplexBean(OpenMetadataRootElement.class, element, (List<RelatedMetadataElement>) null, "test");

        assertEquals(bean.getProperties().getClass(), properties.getClass(), "Wrong properties class: " + bean.getProperties());

        return (EntityProperties) bean.getProperties();
    }
}
