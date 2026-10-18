/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateClassificationRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeploymentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.IncompleteProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.InstanceMetadataProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.MemberPopularityProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.MetamodelInstanceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.PromiseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.UsageActivityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.referencedata.ReferenceDataProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.ObjectIdentifierProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AccountingCodesProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ConfidenceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ConfidentialityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ConsolidatedDuplicateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.CriticalityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.DigitalResourceOriginProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceExpectationsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceMeasurementsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ImpactProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.KnownDuplicateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.OwnershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.RetentionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.MobileResourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityTagsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.FindDigitalResourceOriginProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.FindPropertyNamesProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.LevelIdentifierQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.SecurityTagQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ClassificationExplorerClassificationsFVT covers the Classification Explorer's classification requests: each
 * classification is added to a data set (and updated where the service allows), read back from the element
 * header with its properties, found by the matching by-* query and by the generic by-classification queries,
 * and then removed.  A second data set carries a different value of the queried classifications so the
 * queries are shown to select, not just to return everything.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClassificationExplorerClassificationsFVT
{
    private static final ViewServiceClient explorer = ClassificationExplorerFvtFixtures.classificationExplorer;

    private static final String OWNER         = "owner-" + OMAGPlatformExtension.RUN_ID;
    private static final String SECURITY_TAG  = "label-" + OMAGPlatformExtension.RUN_ID;
    private static final String CAPABILITY    = "capability-" + OMAGPlatformExtension.RUN_ID;
    private static final String ZONE          = "zone-" + OMAGPlatformExtension.RUN_ID;

    private static String elementGUID = null;
    private static String otherGUID   = null;


    @Test
    @Order(1)
    void createElements()
    {
        elementGUID = ClassificationExplorerFvtFixtures.createDataSet("classified", "Classified data set");
        otherGUID   = ClassificationExplorerFvtFixtures.createDataSet("classified-other", "Other classified data set");
    }


    /**
     * Impact, Confidence, Criticality and Confidentiality: set, read back with its level, found by
     * by-classification, then cleared.
     */
    @Test
    @Order(2)
    void governedDataClassifications()
    {
        ImpactProperties impact = new ImpactProperties();
        impact.setSeverityLevel(3);
        impact.setSteward("impact-steward");

        ConfidenceProperties confidence = new ConfidenceProperties();
        confidence.setConfidenceLevel(2);
        confidence.setNotes("confidence notes");

        CriticalityProperties criticality = new CriticalityProperties();
        criticality.setCriticalityLevel(4);
        criticality.setSource("criticality-source");

        ConfidentialityProperties confidentiality = new ConfidentialityProperties();
        confidentiality.setConfidentialityLevel(1);
        confidentiality.setConfidence(80);

        classify(elementGUID, "/impact", impact);
        classify(elementGUID, "/confidence", confidence);
        classify(elementGUID, "/criticality", criticality);
        classify(elementGUID, "/confidentiality", confidentiality);

        assertEquals(3, assertInstanceOf(ImpactProperties.class, classificationProperties(OpenMetadataType.IMPACT_CLASSIFICATION.typeName)).getSeverityLevel());
        assertEquals("impact-steward", ((ImpactProperties) classificationProperties(OpenMetadataType.IMPACT_CLASSIFICATION.typeName)).getSteward());
        assertEquals(2, assertInstanceOf(ConfidenceProperties.class, classificationProperties(OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName)).getConfidenceLevel());
        assertEquals(4, assertInstanceOf(CriticalityProperties.class, classificationProperties(OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName)).getCriticalityLevel());
        assertEquals(1, assertInstanceOf(ConfidentialityProperties.class, classificationProperties(OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName)).getConfidentialityLevel());

        for (String classificationName : List.of(OpenMetadataType.IMPACT_CLASSIFICATION.typeName,
                                                 OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName,
                                                 OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName,
                                                 OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName))
        {
            assertTrue(byClassification(classificationName).contains(elementGUID), "by-classification/" + classificationName + " does not return the element");
            assertFalse(byClassification(classificationName).contains(otherGUID), "by-classification/" + classificationName + " returns an unclassified element");
        }

        declassify(elementGUID, "/impact");
        declassify(elementGUID, "/confidence");
        declassify(elementGUID, "/criticality");
        declassify(elementGUID, "/confidentiality");

        for (String classificationName : List.of(OpenMetadataType.IMPACT_CLASSIFICATION.typeName,
                                                 OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName,
                                                 OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName,
                                                 OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName))
        {
            assertNull(classification(elementGUID, classificationName), classificationName + " still present after remove");
        }
    }


    /**
     * by-impact: the elements with an Impact classification, optionally at a specific severity level.
     */
    @Test
    @Order(3)
    void byImpact()
    {
        ImpactProperties properties = new ImpactProperties();
        properties.setSeverityLevel(3);

        ImpactProperties otherProperties = new ImpactProperties();
        otherProperties.setSeverityLevel(1);

        assertLevelQuery("/impact", properties, otherProperties, "/elements/by-impact", 3);
    }


    /**
     * by-confidence: the elements with a Confidence classification, optionally at a specific level.
     */
    @Test
    @Order(4)
    void byConfidence()
    {
        ConfidenceProperties properties = new ConfidenceProperties();
        properties.setConfidenceLevel(3);

        ConfidenceProperties otherProperties = new ConfidenceProperties();
        otherProperties.setConfidenceLevel(1);

        assertLevelQuery("/confidence", properties, otherProperties, "/elements/by-confidence", 3);
    }


    /**
     * by-criticality: the elements with a Criticality classification, optionally at a specific level.
     */
    @Test
    @Order(5)
    void byCriticality()
    {
        CriticalityProperties properties = new CriticalityProperties();
        properties.setCriticalityLevel(3);

        CriticalityProperties otherProperties = new CriticalityProperties();
        otherProperties.setCriticalityLevel(1);

        assertLevelQuery("/criticality", properties, otherProperties, "/elements/by-criticality", 3);
    }


    /**
     * by-confidentiality: the elements with a Confidentiality classification, optionally at a specific level.
     */
    @Test
    @Order(6)
    void byConfidentiality()
    {
        ConfidentialityProperties properties = new ConfidentialityProperties();
        properties.setConfidentialityLevel(3);

        ConfidentialityProperties otherProperties = new ConfidentialityProperties();
        otherProperties.setConfidentialityLevel(1);

        assertLevelQuery("/confidentiality", properties, otherProperties, "/elements/by-confidentiality", 3);
    }


    /**
     * by-retention: Retention is the one level query whose property name (retentionBasis) is right.
     */
    @Test
    @Order(7)
    void retention()
    {
        RetentionProperties properties = new RetentionProperties();
        properties.setRetentionBasis(3);
        properties.setAssociatedGUID(otherGUID);

        RetentionProperties otherProperties = new RetentionProperties();
        otherProperties.setRetentionBasis(1);

        assertLevelQuery("/retention", properties, otherProperties, "/elements/by-retention", 3);
    }


    /**
     * GovernanceExpectations and GovernanceMeasurements: add, update (merge), remove.
     */
    @Test
    @Order(8)
    void governanceExpectationsAndMeasurements()
    {
        GovernanceExpectationsProperties expectations = new GovernanceExpectationsProperties();
        expectations.setCounts(Map.of("rows", 100));
        expectations.setValues(Map.of("format", "csv"));

        classify(elementGUID, "/governance-expectations", expectations);

        GovernanceExpectationsProperties stored = assertInstanceOf(GovernanceExpectationsProperties.class,
                                                                   classificationProperties(OpenMetadataType.GOVERNANCE_EXPECTATIONS_CLASSIFICATION.typeName));
        assertEquals(Map.of("rows", 100), stored.getCounts());

        GovernanceExpectationsProperties newExpectations = new GovernanceExpectationsProperties();
        newExpectations.setCounts(Map.of("rows", 200));

        update(elementGUID, "/governance-expectations", newExpectations);

        stored = assertInstanceOf(GovernanceExpectationsProperties.class,
                                  classificationProperties(OpenMetadataType.GOVERNANCE_EXPECTATIONS_CLASSIFICATION.typeName));
        assertEquals(Map.of("rows", 200), stored.getCounts());
        assertEquals(Map.of("format", "csv"), stored.getValues(), "a merge update should keep the values");

        GovernanceMeasurementsProperties measurements = new GovernanceMeasurementsProperties();
        measurements.setCounts(Map.of("rows", 98));
        measurements.setFlags(Map.of("valid", true));

        classify(elementGUID, "/governance-measurements", measurements);

        assertEquals(Map.of("rows", 98), assertInstanceOf(GovernanceMeasurementsProperties.class,
                                                          classificationProperties(OpenMetadataType.GOVERNANCE_MEASUREMENTS_CLASSIFICATION.typeName)).getCounts());

        GovernanceMeasurementsProperties newMeasurements = new GovernanceMeasurementsProperties();
        newMeasurements.setCounts(Map.of("rows", 99));

        update(elementGUID, "/governance-measurements", newMeasurements);

        GovernanceMeasurementsProperties storedMeasurements = assertInstanceOf(GovernanceMeasurementsProperties.class,
                                                                               classificationProperties(OpenMetadataType.GOVERNANCE_MEASUREMENTS_CLASSIFICATION.typeName));
        assertEquals(Map.of("rows", 99), storedMeasurements.getCounts());
        assertEquals(Map.of("valid", true), storedMeasurements.getFlags());

        declassify(elementGUID, "/governance-expectations");
        declassify(elementGUID, "/governance-measurements");

        assertNull(classification(elementGUID, OpenMetadataType.GOVERNANCE_EXPECTATIONS_CLASSIFICATION.typeName));
        assertNull(classification(elementGUID, OpenMetadataType.GOVERNANCE_MEASUREMENTS_CLASSIFICATION.typeName));
    }


    /**
     * DataScope, UsageActivity and MemberPopularityProfile: add, update (merge), remove.
     */
    @Test
    @Order(9)
    void dataScopeAndActivity()
    {
        DataScopeProperties dataScope = new DataScopeProperties();
        dataScope.setMinLatitude(51.0F);
        dataScope.setMaxLatitude(52.0F);
        dataScope.setScopeElements(Map.of("region", "south-east"));

        classify(elementGUID, "/data-scope", dataScope);

        DataScopeProperties newDataScope = new DataScopeProperties();
        newDataScope.setMaxLatitude(53.0F);

        update(elementGUID, "/data-scope", newDataScope);

        DataScopeProperties storedDataScope = assertInstanceOf(DataScopeProperties.class,
                                                               classificationProperties(OpenMetadataType.DATA_SCOPE_CLASSIFICATION.typeName));
        assertEquals(53.0F, storedDataScope.getMaxLatitude());
        assertEquals(Map.of("region", "south-east"), storedDataScope.getScopeElements());

        UsageActivityProperties usageActivity = new UsageActivityProperties();
        usageActivity.setExplicitGetCount(5);
        usageActivity.setFindCount(7);

        classify(elementGUID, "/usage-activity", usageActivity);

        assertEquals(5L, assertInstanceOf(UsageActivityProperties.class,
                                          classificationProperties(OpenMetadataType.USAGE_ACTIVITY_CLASSIFICATION.typeName)).getExplicitGetCount());

        UsageActivityProperties newUsageActivity = new UsageActivityProperties();
        newUsageActivity.setExplicitGetCount(6);
        newUsageActivity.setFindCount(7);

        update(elementGUID, "/usage-activity", newUsageActivity);

        assertEquals(6L, assertInstanceOf(UsageActivityProperties.class,
                                          classificationProperties(OpenMetadataType.USAGE_ACTIVITY_CLASSIFICATION.typeName)).getExplicitGetCount());

        MemberPopularityProfileProperties popularity = new MemberPopularityProfileProperties();
        popularity.setExplicitGetCountTotal(10);
        popularity.setExplicitGetCountProfile(Map.of("DataSet", 10L));

        classify(elementGUID, "/member-popularity-profile", popularity);

        MemberPopularityProfileProperties newPopularity = new MemberPopularityProfileProperties();
        newPopularity.setExplicitGetCountTotal(12);
        newPopularity.setExplicitGetCountProfile(Map.of("DataSet", 12L));

        update(elementGUID, "/member-popularity-profile", newPopularity);

        MemberPopularityProfileProperties storedPopularity = assertInstanceOf(MemberPopularityProfileProperties.class,
                                                                              classificationProperties(OpenMetadataType.MEMBER_POPULARITY_PROFILE_CLASSIFICATION.typeName));
        assertEquals(12L, storedPopularity.getExplicitGetCountTotal());
        assertEquals(Map.of("DataSet", 12L), storedPopularity.getExplicitGetCountProfile());

        declassify(elementGUID, "/data-scope");
        declassify(elementGUID, "/usage-activity");
        declassify(elementGUID, "/member-popularity-profile");

        assertNull(classification(elementGUID, OpenMetadataType.DATA_SCOPE_CLASSIFICATION.typeName));
        assertNull(classification(elementGUID, OpenMetadataType.USAGE_ACTIVITY_CLASSIFICATION.typeName));
        assertNull(classification(elementGUID, OpenMetadataType.MEMBER_POPULARITY_PROFILE_CLASSIFICATION.typeName));
    }


    /**
     * SecurityTags: added, found by its label (and not for the other element's label), removed.
     */
    @Test
    @Order(10)
    void securityTags()
    {
        SecurityTagsProperties tags = new SecurityTagsProperties();
        tags.setSecurityLabels(List.of(SECURITY_TAG));
        tags.setSecurityProperties(Map.of("classification", "internal"));

        SecurityTagsProperties otherTags = new SecurityTagsProperties();
        otherTags.setSecurityLabels(List.of(SECURITY_TAG + "-other"));

        classify(elementGUID, "/security-tags", tags);
        classify(otherGUID, "/security-tags", otherTags);

        assertEquals(List.of(SECURITY_TAG), assertInstanceOf(SecurityTagsProperties.class,
                                                             classificationProperties(OpenMetadataType.SECURITY_TAGS_CLASSIFICATION.typeName)).getSecurityLabels());

        SecurityTagQueryProperties query = new SecurityTagQueryProperties();
        query.setSecurityLabels(List.of(SECURITY_TAG));

        List<String> found = ClassificationExplorerFvtFixtures.guids(explorer.elements("/elements/by-security-tags", query));

        assertTrue(found.contains(elementGUID), "by-security-tags does not return the tagged element");
        assertFalse(found.contains(otherGUID), "by-security-tags returns an element with a different label");

        declassify(elementGUID, "/security-tags");
        declassify(otherGUID, "/security-tags");

        assertNull(classification(elementGUID, OpenMetadataType.SECURITY_TAGS_CLASSIFICATION.typeName));
    }


    /**
     * AccountingCodes: added and removed.
     */
    @Test
    @Order(11)
    void accountingCodes()
    {
        AccountingCodesProperties codes = new AccountingCodesProperties();
        codes.setAccountingCode("CC-1234");
        codes.setDescription("Customer analytics cost centre");
        codes.setAccountingCodeList(List.of("CC-1234", "CC-5678"));

        classify(elementGUID, "/accounting-codes", codes);

        AccountingCodesProperties stored = assertInstanceOf(AccountingCodesProperties.class,
                                                            classificationProperties(OpenMetadataType.ACCOUNTING_CODES_CLASSIFICATION.typeName));
        assertEquals("CC-1234", stored.getAccountingCode());
        assertEquals(List.of("CC-1234", "CC-5678"), stored.getAccountingCodeList());

        declassify(elementGUID, "/accounting-codes");

        assertNull(classification(elementGUID, OpenMetadataType.ACCOUNTING_CODES_CLASSIFICATION.typeName));
    }


    /**
     * Ownership: added; found by by-ownership and by the generic by-classification property queries; removed.
     */
    @Test
    @Order(12)
    void ownership()
    {
        OwnershipProperties ownership = new OwnershipProperties();
        ownership.setOwner(OWNER);
        ownership.setOwnerTypeName("UserIdentity");
        ownership.setOwnerPropertyName("userId");

        OwnershipProperties otherOwnership = new OwnershipProperties();
        otherOwnership.setOwner(OWNER + "-other");

        classify(elementGUID, "/ownership", ownership);
        classify(otherGUID, "/ownership", otherOwnership);

        assertEquals(OWNER, assertInstanceOf(OwnershipProperties.class,
                                             classificationProperties(OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName)).getOwner());

        FilterRequestBody byOwner = new FilterRequestBody();
        byOwner.setFilter(OWNER);

        List<String> found = ClassificationExplorerFvtFixtures.guids(explorer.elements("/elements/by-ownership", byOwner));

        assertTrue(found.contains(elementGUID), "by-ownership does not return the owned element");
        assertFalse(found.contains(otherGUID), "by-ownership returns an element with a different owner");

        String classificationPath = "/elements/by-classification/" + OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName;

        FindPropertyNamesProperties exact = new FindPropertyNamesProperties();
        exact.setPropertyValue(OWNER);
        exact.setPropertyNames(List.of("owner"));

        found = ClassificationExplorerFvtFixtures.guids(explorer.elements(classificationPath + "/with-exact-property-value", exact));

        assertTrue(found.contains(elementGUID), "with-exact-property-value does not return the owned element");
        assertFalse(found.contains(otherGUID), "with-exact-property-value returns an element with a different owner");

        FindPropertyNamesProperties search = new FindPropertyNamesProperties();
        search.setPropertyValue(OWNER);
        search.setPropertyNames(List.of("owner"));

        found = ClassificationExplorerFvtFixtures.guids(explorer.elements(classificationPath + "/with-property-value-search", search));

        assertTrue(found.contains(elementGUID), "with-property-value-search does not return the owned element");
        assertTrue(found.contains(otherGUID), "with-property-value-search should find both owners that contain " + OWNER);

        declassify(elementGUID, "/ownership");
        declassify(otherGUID, "/ownership");

        assertNull(classification(elementGUID, OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName));
        assertFalse(ClassificationExplorerFvtFixtures.guids(explorer.elements("/elements/by-ownership", byOwner)).contains(elementGUID),
                    "by-ownership still returns the element after the classification is removed");
    }


    /**
     * DigitalResourceOrigin: added; found by business capability; removed.
     */
    @Test
    @Order(13)
    void digitalResourceOrigin()
    {
        classify(elementGUID, "/digital-resource-origin", origin("Org-A", CAPABILITY));
        classify(otherGUID, "/digital-resource-origin", origin("Org-B", CAPABILITY + "-other"));

        assertEquals(CAPABILITY, assertInstanceOf(DigitalResourceOriginProperties.class,
                                                  classificationProperties(OpenMetadataType.DIGITAL_RESOURCE_ORIGIN_CLASSIFICATION.typeName)).getBusinessCapability());

        List<String> found = byOrigin(null, CAPABILITY);

        assertTrue(found.contains(elementGUID), "by-digital-resource-origin does not return the element for its business capability");
        assertFalse(found.contains(otherGUID), "by-digital-resource-origin returns an element with a different business capability");

        declassify(elementGUID, "/digital-resource-origin");
        declassify(otherGUID, "/digital-resource-origin");

        assertNull(classification(elementGUID, OpenMetadataType.DIGITAL_RESOURCE_ORIGIN_CLASSIFICATION.typeName));
    }


    /**
     * by-digital-resource-origin with only an organization should return the elements from that organization.
     */
    @Test
    @Order(14)
    void digitalResourceOriginByOrganization()
    {
        String organization = "Org-" + OMAGPlatformExtension.RUN_ID;

        classify(elementGUID, "/digital-resource-origin", origin(organization, null));
        classify(otherGUID, "/digital-resource-origin", origin(organization + "-other", null));

        try
        {
            List<String> found = byOrigin(organization, null);

            assertTrue(found.contains(elementGUID), "by-digital-resource-origin does not return the element for its organization");
            assertFalse(found.contains(otherGUID), "by-digital-resource-origin returns an element from a different organization");
        }
        finally
        {
            declassify(elementGUID, "/digital-resource-origin");
            declassify(otherGUID, "/digital-resource-origin");
        }
    }


    /**
     * ZoneMembership: added, found by zone with the generic classification query, removed.
     */
    @Test
    @Order(15)
    void zoneMembership()
    {
        ZoneMembershipProperties zones = new ZoneMembershipProperties();
        zones.setZoneMembership(List.of(ZONE));

        classify(elementGUID, "/zone-membership", zones);

        assertEquals(List.of(ZONE), assertInstanceOf(ZoneMembershipProperties.class,
                                                     classificationProperties(OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName)).getZoneMembership());
        assertTrue(byClassification(OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName).contains(elementGUID));

        declassify(elementGUID, "/zone-membership");

        assertNull(classification(elementGUID, OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName));
    }


    /**
     * KnownDuplicate and ConsolidatedDuplicate: set and cleared.
     */
    @Test
    @Order(16)
    void duplicateClassifications()
    {
        classify(elementGUID, "/known-duplicate", new KnownDuplicateProperties());

        assertNotNull(classification(elementGUID, OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION.typeName), "KnownDuplicate not set");

        ConsolidatedDuplicateProperties consolidated = new ConsolidatedDuplicateProperties();
        consolidated.setStatusIdentifier(1);
        consolidated.setSteward("duplicate-steward");

        classify(otherGUID, "/consolidated-duplicate", consolidated);

        assertEquals("duplicate-steward", assertInstanceOf(ConsolidatedDuplicateProperties.class,
                                                           classification(otherGUID, OpenMetadataType.CONSOLIDATED_DUPLICATE_CLASSIFICATION.typeName).getClassificationProperties()).getSteward());

        declassify(elementGUID, "/known-duplicate");
        declassify(otherGUID, "/consolidated-duplicate");

        assertNull(classification(elementGUID, OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION.typeName));
        assertNull(classification(otherGUID, OpenMetadataType.CONSOLIDATED_DUPLICATE_CLASSIFICATION.typeName));
    }


    /**
     * Promise: an element whose real-world counterpart has not been delivered is only visible to lineage
     * requests, so it is read and cleared with forLineage set.
     */
    @Test
    @Order(17)
    void promise()
    {
        PromiseProperties promise = new PromiseProperties();
        promise.setDeploymentStatus(DeploymentStatus.PROPOSED);
        promise.setAdditionalProperties(Map.of("plan", "Q4"));

        classify(otherGUID, "/promise", promise);

        ViewServicesFvtTestSupport.assertGone(explorer, "/elements/" + otherGUID);

        GetRequestBody forLineage = new GetRequestBody();
        forLineage.setForLineage(true);

        ElementClassification classification = ClassificationExplorerFvtFixtures.classification(explorer.element("/elements/" + otherGUID, forLineage),
                                                                                                OpenMetadataType.PROMISE_CLASSIFICATION.typeName);

        assertNotNull(classification, "Promise not set");
        assertEquals(DeploymentStatus.PROPOSED, assertInstanceOf(PromiseProperties.class, classification.getClassificationProperties()).getDeploymentStatus());

        DeleteClassificationRequestBody remove = new DeleteClassificationRequestBody();
        remove.setForLineage(true);

        explorer.call("/elements/" + otherGUID + "/promise/remove", remove);

        assertNull(classification(otherGUID, OpenMetadataType.PROMISE_CLASSIFICATION.typeName), "Promise still present after remove");
    }


    /**
     * Incomplete, ObjectIdentifier, ReferenceData, MobileResource, InstanceMetadata and MetamodelInstance: each
     * set, read back and cleared.
     */
    @Test
    @Order(18)
    void elementStatusClassifications()
    {
        InstanceMetadataProperties instanceMetadata = new InstanceMetadataProperties();
        instanceMetadata.setInstanceMetadataTypeName("DataSet");
        instanceMetadata.setDescription("Describes a data set instance");

        MetamodelInstanceProperties metamodelInstance = new MetamodelInstanceProperties();
        metamodelInstance.setMetamodelElementGUID(otherGUID);

        Map<String, ClassificationBeanProperties> classifications = Map.of("/incomplete", new IncompleteProperties(),
                                                                            "/object-identifier", new ObjectIdentifierProperties(),
                                                                            "/reference-data", new ReferenceDataProperties(),
                                                                            "/mobile-resource", new MobileResourceProperties(),
                                                                            "/instance-metadata", instanceMetadata,
                                                                            "/metamodel-instance", metamodelInstance);
        Map<String, String> classificationNames = Map.of("/incomplete", OpenMetadataType.INCOMPLETE_CLASSIFICATION.typeName,
                                                         "/object-identifier", OpenMetadataType.OBJECT_IDENTIFIER_CLASSIFICATION.typeName,
                                                         "/reference-data", OpenMetadataType.REFERENCE_DATA_CLASSIFICATION.typeName,
                                                         "/mobile-resource", OpenMetadataType.MOBILE_RESOURCE_CLASSIFICATION.typeName,
                                                         "/instance-metadata", OpenMetadataType.INSTANCE_METADATA_CLASSIFICATION.typeName,
                                                         "/metamodel-instance", OpenMetadataType.METAMODEL_INSTANCE_CLASSIFICATION.typeName);

        for (Map.Entry<String, ClassificationBeanProperties> entry : classifications.entrySet())
        {
            classify(elementGUID, entry.getKey(), entry.getValue());

            assertNotNull(classification(elementGUID, classificationNames.get(entry.getKey())), entry.getKey() + " did not classify the element");
        }

        assertEquals("DataSet", assertInstanceOf(InstanceMetadataProperties.class,
                                                 classificationProperties(OpenMetadataType.INSTANCE_METADATA_CLASSIFICATION.typeName)).getInstanceMetadataTypeName());
        assertEquals(otherGUID, assertInstanceOf(MetamodelInstanceProperties.class,
                                                 classificationProperties(OpenMetadataType.METAMODEL_INSTANCE_CLASSIFICATION.typeName)).getMetamodelElementGUID());

        for (String path : classifications.keySet())
        {
            declassify(elementGUID, path);

            assertNull(classification(elementGUID, classificationNames.get(path)), path + "/remove did not remove the classification");
        }
    }


    @AfterAll
    static void deleteElements()
    {
        ClassificationExplorerFvtFixtures.deleteAsset(elementGUID);
        ClassificationExplorerFvtFixtures.deleteAsset(otherGUID);
    }


    /**
     * Classify the two elements at different levels, check the specific-level query returns only the right one
     * and the any-level query returns both (and not an element with a different classification), then clear.
     */
    private static void assertLevelQuery(String                       classificationPath,
                                         ClassificationBeanProperties properties,
                                         ClassificationBeanProperties otherProperties,
                                         String                       queryPath,
                                         int                          level)
    {
        classify(elementGUID, classificationPath, properties);
        classify(otherGUID, classificationPath, otherProperties);

        try
        {
            LevelIdentifierQueryProperties specificLevel = new LevelIdentifierQueryProperties();
            specificLevel.setReturnSpecificLevel(true);
            specificLevel.setLevelIdentifier(level);

            List<String> found = ClassificationExplorerFvtFixtures.guids(explorer.elements(queryPath, specificLevel));

            assertTrue(found.contains(elementGUID), queryPath + " does not return the element at level " + level);
            assertFalse(found.contains(otherGUID), queryPath + " returns an element at a different level");

            LevelIdentifierQueryProperties anyLevel = new LevelIdentifierQueryProperties();
            anyLevel.setReturnSpecificLevel(false);

            found = ClassificationExplorerFvtFixtures.guids(explorer.elements(queryPath, anyLevel));

            assertTrue(found.contains(elementGUID), queryPath + " (any level) does not return the element");
            assertTrue(found.contains(otherGUID), queryPath + " (any level) does not return the other element");
        }
        finally
        {
            declassify(elementGUID, classificationPath);
            declassify(otherGUID, classificationPath);
        }
    }


    private static DigitalResourceOriginProperties origin(String organization,
                                                          String businessCapability)
    {
        DigitalResourceOriginProperties properties = new DigitalResourceOriginProperties();

        properties.setOrganization(organization);
        properties.setBusinessCapability(businessCapability);

        return properties;
    }


    private static List<String> byOrigin(String organization,
                                         String businessCapability)
    {
        FindDigitalResourceOriginProperties query = new FindDigitalResourceOriginProperties();

        query.setProperties(origin(organization, businessCapability));

        return ClassificationExplorerFvtFixtures.guids(explorer.elements("/elements/by-digital-resource-origin", query));
    }


    private static List<String> byClassification(String classificationName)
    {
        return ClassificationExplorerFvtFixtures.guids(explorer.elements("/elements/by-classification/" + classificationName, new ResultsRequestBody()));
    }


    private static void classify(String                       guid,
                                 String                       path,
                                 ClassificationBeanProperties properties)
    {
        NewClassificationRequestBody requestBody = new NewClassificationRequestBody();

        requestBody.setProperties(properties);

        explorer.call("/elements/" + guid + path, requestBody);
    }


    private static void update(String                       guid,
                               String                       path,
                               ClassificationBeanProperties properties)
    {
        UpdateClassificationRequestBody requestBody = new UpdateClassificationRequestBody();

        requestBody.setMergeUpdate(true);
        requestBody.setProperties(properties);

        explorer.call("/elements/" + guid + path + "/update", requestBody);
    }


    private static void declassify(String guid,
                                   String path)
    {
        explorer.call("/elements/" + guid + path + "/remove", new DeleteClassificationRequestBody());
    }


    private static ElementClassification classification(String guid,
                                                        String classificationName)
    {
        return ClassificationExplorerFvtFixtures.classification(ClassificationExplorerFvtFixtures.readElement(guid), classificationName);
    }


    private static ClassificationBeanProperties classificationProperties(String classificationName)
    {
        ElementClassification classification = classification(elementGUID, classificationName);

        assertNotNull(classification, classificationName + " is not on the element");

        return classification.getClassificationProperties();
    }
}
