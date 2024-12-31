/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector;

import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mocks.MockConnection;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.readops.GetEntity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.HistorySequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.*;

/**
 * Tests the metadata collection interface of the connector.
 */
public class XTDBOMRSMetadataCollectionTest
{

    private static final XTDBOMRSRepositoryConnector connector = MockConnection.getMockConnector();
    private static final OMRSRepositoryHelper        helper    = connector.getRepositoryHelper();
    private static final String username = MockConnection.USERNAME;
    private static final String source   = MockConnection.SOURCE_NAME;

    private static XTDBOMRSMetadataCollection mc = null;

    private static final String qualifiedNameProperty = "qualifiedName";
    private static final String displayNameProperty   = "displayName";
    private static final String descriptionProperty   = "description";
    private static final String userDefinedProperty   = "userDefined";
    private static final String partialMatchProperty  = "partialMatch";

    private static final String referenceableGuid = "a32316b8-dc8c-48c5-b12b-71c1b2a080bf";
    private static final String glossaryTypeGuid = "36f66863-9726-4b41-97ee-714fd0dc6fe4";
    private static final String categoryTypeGuid = "e507485b-9b5a-44c9-8a28-6967f7ff3672";
    private static final String normTermTypeGuid = "0db3e6ec-f5ef-4d75-ae38-b7ee6fd6ec0a";
    private static final String ctrlTermTypeGuid = "c04e29b2-2d66-48fc-a20d-e59895de6040";
    private static final String dataClassTypeGuid = "6bc727dc-e855-4979-8736-78ac3cfcd32f";

    private static final String categoryAnchorTypeGuid = "c628938e-815e-47db-8d1c-59bb2e84e028";
    private static final String termAnchorTypeGuid     = "1d43d661-bdc7-4a91-a996-3239b8f82e56";
    private static final String termCategorizationTypeGuid = "696a81f5-ac60-46c7-b9fd-6979a1e7ad27";
    private static final String categoryLinkTypeGuid   = "71e4b6fb-3412-4193-aff3-a16eccd87e8e";
    private static final String dataClassAssignmentTypeGuid = "4df37335-7f0c-4ced-82df-3b2fd07be1bd";
    private static final String softwareServerPlatformDeploymentTypeGuid = "b909eb3b-5205-4180-9f63-122a65b30738";

    private static final String glossaryQN = "omrs-mc-glossary";
    private static final String categoryQN = "omrs-mc-category";
    private static final String ctrlTermQN = "omrs-mc-term";
    private static final String dataClassQN = "omrs-mc-data-class";

    private static final String glossaryDN = "glossary";
    private static final String categoryDN = "category";
    private static final String ctrlTermDN = "term";

    private static final String externalMetadataCollectionId = "987";
    private static final String externalMetadataCollectionName = "Some imaginary metadata collection";

    // Entities
    private String glossaryGuid = "";
    private String categoryGuid = "";
    private String ctrlTermGuid = "";
    private String dataClassGuid = "";
    private String categoryGuidEX = "";
    private String ctrlTermGuidRC = "";
    private String hostGuid = "";
    private String softwareServerPlatformGuid = "";

    // Relationships
    private String categoryAnchorGuid     = "";
    private String termAnchorGuid         = "";
    private String termCategorizationGuid = "";
    private String dataClassAssignmentGuid = "";
    private String categoryLinkGuidEX     = "";
    private String termCategorizationGuidRC = "";
    private String softwareServerPlatformDeploymentGuid = "";

    private Date now = null;

    XTDBOMRSMetadataCollectionTest() {
        try {
            mc = (XTDBOMRSMetadataCollection) connector.getMetadataCollection();
        } catch (RepositoryErrorException e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "create" })
    void addEntity() {
        try {

            InstanceProperties ip = helper.addStringPropertyToInstance(source,
                    null,
                    qualifiedNameProperty, glossaryQN,
                    this.getClass().getName());
            EntityDetail result = mc.addEntity(username, glossaryTypeGuid, ip, null, null);
            assertNotNull(result, "Expected a Glossary to be created.");
            glossaryGuid = result.getGUID();
            assertNotNull(glossaryGuid, "Expected created Glossary to have a GUID assigned.");
            validatePropertyValue(result.getProperties(), qualifiedNameProperty, glossaryQN);
            assertEquals(result.getVersion(), 1L, "Expected this to be the initial version of the Glossary.");

            ip = helper.addStringPropertyToInstance(source,
                    null,
                    qualifiedNameProperty, categoryQN,
                    this.getClass().getName());
            result = mc.addEntity(username, categoryTypeGuid, ip, null, null);
            assertNotNull(result, "Expected a GlossaryCategory to be created.");
            categoryGuid = result.getGUID();
            assertNotNull(categoryGuid, "Expected created GlossaryCategory to have a GUID assigned.");
            validatePropertyValue(result.getProperties(), qualifiedNameProperty, categoryQN);
            assertEquals(result.getVersion(), 1L, "Expected this to be the initial version of the GlossaryCategory.");

            ip = helper.addStringPropertyToInstance(source,
                    null,
                    qualifiedNameProperty, ctrlTermQN,
                    this.getClass().getName());
            result = mc.addEntity(username, normTermTypeGuid, ip, null, null);
            assertNotNull(result, "Expected a GlossaryTerm to be created.");
            ctrlTermGuid = result.getGUID();
            assertNotNull(ctrlTermGuid, "Expected created GlossaryTerm to have a GUID assigned.");
            validatePropertyValue(result.getProperties(), qualifiedNameProperty, ctrlTermQN);
            assertEquals(result.getVersion(), 1L, "Expected this to be the initial version of the GlossaryTerm.");

            ip = helper.addStringPropertyToInstance(source,
                    null,
                    qualifiedNameProperty, dataClassQN,
                    this.getClass().getName());
            ip = helper.addBooleanPropertyToInstance(source,
                    ip,
                    userDefinedProperty, false,
                    this.getClass().getName());
            result = mc.addEntity(username, dataClassTypeGuid, ip, null, null);
            assertNotNull(result, "Expected a DataClass to be created.");
            dataClassGuid = result.getGUID();
            assertNotNull(dataClassGuid, "Expected created DataClass to have a GUID assigned.");
            validatePropertyValue(result.getProperties(), userDefinedProperty, false);
            assertEquals(result.getVersion(), 1L, "Expected this to be the initial version of the DataClass.");

            ip = helper.addStringPropertyToInstance(source,
                    null,
                    qualifiedNameProperty, "HostQN",
                    this.getClass().getName());
            result = mc.addEntity(username, "1abd16db-5b8a-4fd9-aee5-205db3febe99", ip, null, null);
            assertNotNull(result, "Expected a Host to be created.");
            hostGuid = result.getGUID();
            assertNotNull(hostGuid, "Expected created Host to have a GUID assigned.");

            ip = helper.addStringPropertyToInstance(source,
                    null,
                    qualifiedNameProperty, "SoftwareServerPlatformQN",
                    this.getClass().getName());
            result = mc.addEntity(username, "ba7c7884-32ce-4991-9c41-9778f1fec6aa", ip, null, null);
            assertNotNull(result, "Expected a SoftwareServerPlatform to be created.");
            softwareServerPlatformGuid = result.getGUID();
            assertNotNull(softwareServerPlatformGuid, "Expected created SoftwareServerPlatform to have a GUID assigned.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "create" })
    void addExternalEntity() {
        try {

            String externalCategoryQN = "external-" + categoryQN;

            InstanceProperties ip = helper.addStringPropertyToInstance(source,
                    null,
                    qualifiedNameProperty, externalCategoryQN,
                    this.getClass().getName());
            EntityDetail result = mc.addExternalEntity(username,
                    categoryTypeGuid,
                    externalMetadataCollectionId, externalMetadataCollectionName,
                    ip, null, null);
            assertNotNull(result, "Expected a GlossaryCategory to be created.");
            categoryGuidEX = result.getGUID();
            assertNotNull(categoryGuidEX, "Expected created GlossaryCategory to have a GUID assigned.");
            validatePropertyValue(result.getProperties(), qualifiedNameProperty, externalCategoryQN);
            assertEquals(result.getVersion(), 1L, "Expected this to be the initial version of the GlossaryCategory.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "create" })
    void saveEntityReferenceCopy() {
        try {

            ctrlTermGuidRC = UUID.randomUUID().toString();
            String externalTermQN = "rc-" + ctrlTermQN;

            InstanceProperties ip = helper.addStringPropertyToInstance(source,
                    null,
                    qualifiedNameProperty, externalTermQN,
                    this.getClass().getName());

            EntityDetail entity = helper.getSkeletonEntity(source,
                    externalMetadataCollectionId,
                    externalMetadataCollectionName,
                    InstanceProvenanceType.LOCAL_COHORT,
                    username,
                    "ControlledGlossaryTerm");
            entity.setGUID(ctrlTermGuidRC);
            entity.setProperties(ip);
            entity.setVersion(10L);

            mc.saveEntityReferenceCopy(username, entity);
            EntityDetail retrieved = mc.getEntityDetail(username, ctrlTermGuidRC);
            assertNotNull(retrieved, "Expected reference copy ControlledGlossaryTerm to exist after being created.");
            assertEquals(retrieved.getVersion(), 10L, "Expected version to match precisely what was sent.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "create" }, dependsOnMethods = { "addEntity" })
    void addRelationship() {
        try {

            Relationship result = mc.addRelationship(username,
                    categoryAnchorTypeGuid,
                    null,
                    glossaryGuid,
                    categoryGuid,
                    null);
            assertNotNull(result, "Expected a CategoryAnchor to be created.");
            categoryAnchorGuid = result.getGUID();
            assertNotNull(categoryAnchorGuid, "Expected created CategoryAnchor to have a GUID assigned.");
            assertEquals(result.getVersion(), 1L, "Expected this to be the initial version of the CategoryAnchor.");

            result = mc.addRelationship(username,
                    termAnchorTypeGuid,
                    null,
                    glossaryGuid,
                    ctrlTermGuid,
                    null);
            assertNotNull(result, "Expected a TermAnchor to be created.");
            termAnchorGuid = result.getGUID();
            assertNotNull(termAnchorGuid, "Expected created TermAnchor to have a GUID assigned.");
            assertEquals(result.getVersion(), 1L, "Expected this to be the initial version of the TermAnchor.");

            InstanceProperties ip = helper.addStringPropertyToInstance(source,
                    null,
                    descriptionProperty, "omrs-mc-term-categorization",
                    this.getClass().getName());
            ip = helper.addEnumPropertyToInstance(source,
                    ip,
                    "status",
                    null,
                    null,
                    1,
                    "Active",
                    "The term relationship is approved and in use.",
                    this.getClass().getName());
            result = mc.addRelationship(username,
                    termCategorizationTypeGuid,
                    ip,
                    categoryGuid,
                    ctrlTermGuid,
                    null);
            assertNotNull(result, "Expected a TermCategorization to be created.");
            termCategorizationGuid = result.getGUID();
            assertNotNull(termCategorizationGuid, "Expected created TermCategorization to have a GUID assigned.");
            validatePropertyValue(result.getProperties(), descriptionProperty, "omrs-mc-term-categorization");
            assertEquals(result.getVersion(), 1L, "Expected this to be the initial version of the TermCategorization.");

            ip = helper.addBooleanPropertyToInstance(source,
                    null,
                    partialMatchProperty, false,
                    this.getClass().getName());
            result = mc.addRelationship(username,
                    dataClassAssignmentTypeGuid,
                    ip,
                    ctrlTermGuid,
                    dataClassGuid,
                    null);
            assertNotNull(result, "Expected a DataClassAssignment to be created.");
            dataClassAssignmentGuid = result.getGUID();
            assertNotNull(dataClassAssignmentGuid, "Expected created DataClassAssignment to have a GUID assigned.");
            validatePropertyValue(result.getProperties(), partialMatchProperty, false);
            assertEquals(result.getVersion(), 1L, "Expected this to be the initial version of the DataClassAssignment.");

            now = new Date();
            ip = helper.addDatePropertyToInstance(source,
                    null,
                    "deploymentTime", now,
                    this.getClass().getName());
            result = mc.addRelationship(username,
                    softwareServerPlatformDeploymentTypeGuid,
                    ip,
                    hostGuid,
                    softwareServerPlatformGuid,
                    null);
            assertNotNull(result, "Expected a SoftwareServerPlatformDeployment to be created.");
            softwareServerPlatformDeploymentGuid = result.getGUID();
            assertNotNull(softwareServerPlatformDeploymentGuid, "Expected created SoftwareServerPlatformDeployment to have a GUID assigned.");
            validatePropertyValue(result.getProperties(), "deploymentTime", ip.getPropertyValue("deploymentTime").valueAsObject());
            assertEquals(result.getVersion(), 1L, "Expected this to be the initial version of the SoftwareServerPlatformDeployment.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "create" }, dependsOnMethods = { "addExternalEntity" })
    void addExternalRelationship() {
        try {

            Relationship result = mc.addExternalRelationship(username,
                    categoryLinkTypeGuid, externalMetadataCollectionId, externalMetadataCollectionName,
                    null,
                    categoryGuid, categoryGuidEX,
                    null);
            assertNotNull(result, "Expected a CategoryHierarchyLink to be created.");
            categoryLinkGuidEX = result.getGUID();
            assertNotNull(categoryLinkGuidEX, "Expected created CategoryHierarchyLink to have a GUID assigned.");
            assertEquals(result.getVersion(), 1L, "Expected this to be the initial version of the CategoryHierarchyLink.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "create" }, dependsOnMethods = { "saveEntityReferenceCopy" })
    void saveRelationshipReferenceCopy() {
        try {

            termCategorizationGuidRC = UUID.randomUUID().toString();

            Relationship relationship = helper.getSkeletonRelationship(source,
                    externalMetadataCollectionId,
                    externalMetadataCollectionName,
                    InstanceProvenanceType.LOCAL_COHORT,
                    username,
                    "TermCategorization");
            relationship.setGUID(termCategorizationGuidRC);
            relationship.setVersion(10L);

            EntityProxy one = new GetEntity(connector, categoryGuidEX, null).asProxy();
            assertNotNull(one, "Expected proxy for the externally-sourced GlossaryCategory to exist.");
            EntityProxy two = new GetEntity(connector, ctrlTermGuidRC, null).asProxy();
            assertNotNull(two, "Expected proxy for the reference copy ControlledGlossaryTerm to exist.");

            relationship.setEntityOneProxy(one);
            relationship.setEntityTwoProxy(two);

            mc.saveRelationshipReferenceCopy(username, relationship);
            Relationship retrieved = mc.getRelationship(username, termCategorizationGuidRC);
            assertNotNull(retrieved, "Expected reference copy TermCategorization to exist after being created.");
            assertEquals(retrieved.getVersion(), 10L, "Expected version to match precisely what was sent.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "create" }, dependsOnMethods = { "addEntity" })
    void classifyEntity() {
        try {

            InstanceProperties ip = helper.addStringPropertyToInstance(source,
                    null,
                    "scope", "some-scope",
                    this.getClass().getName());
            EntityDetail result = mc.classifyEntity(username, glossaryGuid, "CanonicalVocabulary", ip);
            assertNotNull(result, "Expected classification of Glossary to return the glossary.");
            assertEquals(result.getGUID(), glossaryGuid, "Expected returned Glossary's GUID to match.");
            assertNotNull(result.getClassifications(), "Expected some classifications to exist on returned entity.");
            assertEquals(result.getClassifications().size(), 1, "Expected precisely one classification to exist on returned entity.");
            Classification one = result.getClassifications().get(0);
            assertNotNull(one, "Expected the singular classification to be non-null.");
            assertEquals(one.getName(), "CanonicalVocabulary", "Expected the classification name to be CanonicalVocabulary.");
            validatePropertyValue(one.getProperties(), "scope", "some-scope");
            assertEquals(one.getVersion(), 1L, "Expected this to be the initial version of the classification.");

            Date toArchive = new Date();
            ip = helper.addDatePropertyToInstance(source,
                    null,
                    "archiveAfter", toArchive,
                    this.getClass().getName());
            result = mc.classifyEntity(username,
                    categoryGuidEX,
                    "Retention",
                    externalMetadataCollectionId, externalMetadataCollectionName,
                    ClassificationOrigin.ASSIGNED, null,
                    ip);
            assertNotNull(result, "Expected classification of GlossaryCategory to return the category.");
            assertEquals(result.getGUID(), categoryGuidEX, "Expected returned GlossaryCategory's GUID to match.");
            assertNotNull(result.getClassifications(), "Expected some classifications to exist on returned entity.");
            assertEquals(result.getClassifications().size(), 1, "Expected precisely one classification to exist on returned entity.");
            one = result.getClassifications().get(0);
            assertNotNull(one, "Expected the singular classification to be non-null.");
            assertEquals(one.getName(), "Retention", "Expected the classification name to be Retention.");
            validatePropertyValue(one.getProperties(), "archiveAfter", toArchive.getTime());
            assertEquals(one.getVersion(), 1L, "Expected this to be the initial version of the classification.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "create" }, dependsOnMethods = { "addEntity" })
    void saveClassificationReferenceCopy() {
        try {

            InstanceProperties ip = helper.addStringPropertyToInstance(source,
                    null,
                    "name", "some-subject-area",
                    this.getClass().getName());

            EntityDetail entity = mc.getEntityDetail(username, categoryGuid);
            Classification classification = helper.getNewClassification(source,
                    externalMetadataCollectionId,
                    externalMetadataCollectionName,
                    InstanceProvenanceType.LOCAL_COHORT,
                    username,
                    "SubjectArea",
                    "GlossaryCategory",
                    ClassificationOrigin.ASSIGNED,
                    null,
                    ip);

            mc.saveClassificationReferenceCopy(username, entity, classification);

            EntityDetail retrieved = mc.getEntityDetail(username, categoryGuid);
            assertNotNull(retrieved, "Expected classification of GlossaryCategory to return the category.");
            assertEquals(retrieved.getGUID(), categoryGuid, "Expected returned GlossaryCategory's GUID to match.");
            assertNotNull(retrieved.getClassifications(), "Expected some classifications to exist on returned entity.");
            assertEquals(retrieved.getClassifications().size(), 1, "Expected precisely one classification to exist on returned entity.");
            Classification one = retrieved.getClassifications().get(0);
            assertNotNull(one, "Expected the singular classification to be non-null.");
            assertEquals(one.getName(), "SubjectArea", "Expected the classification name to be SubjectArea.");
            validatePropertyValue(one.getProperties(), "name", "some-subject-area");
            assertEquals(one.getVersion(), 1L, "Expected this to be the initial version of the classification.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "update" }, dependsOnGroups = { "create" })
    void reTypeEntity() {
        try {

            TypeDefSummary currentTypeDef = helper.getTypeDefByName(source, "GlossaryTerm");
            TypeDefSummary newTypeDef     = helper.getTypeDefByName(source, "ControlledGlossaryTerm");

            EntityDetail result = mc.reTypeEntity(username, ctrlTermGuid, currentTypeDef, newTypeDef);
            assertNotNull(result, "Expected re-typing of ControlledGlossaryTerm to return the term.");
            assertEquals(result.getGUID(), ctrlTermGuid, "Expected returned ControlledGlossaryTerm's GUID to match.");
            assertEquals(result.getType().getTypeDefGUID(), ctrlTermTypeGuid, "Expected the GUID of the type of the ControlledGlossaryTerm to match.");
            assertEquals(result.getVersion(), 2L, "Expected the version to be incremented by the type change.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "update" }, dependsOnGroups = { "create" }, dependsOnMethods = { "reTypeEntity" })
    void updateEntityStatus() {
        try {

            EntityDetail result = mc.updateEntityStatus(username + "_b", ctrlTermGuid, InstanceStatus.APPROVED);
            assertNotNull(result, "Expected status update of ControlledGlossaryTerm to return the term.");
            assertEquals(result.getGUID(), ctrlTermGuid, "Expected returned ControlledGlossaryTerm's GUID to match.");
            assertEquals(result.getStatus(), InstanceStatus.APPROVED, "Expected status to be set to the updated value.");
            assertEquals(result.getVersion(), 3L, "Expected the version to be incremented by the status update.");
            assertNotNull(result.getMaintainedBy());
            assertEquals(result.getMaintainedBy().size(), 2);
            assertEquals(result.getMaintainedBy().get(0), username);
            assertEquals(result.getMaintainedBy().get(1), username + "_b");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "update" }, dependsOnGroups = { "create" }, dependsOnMethods = { "updateEntityStatus" })
    void updateEntityProperties() {
        try {

            InstanceProperties ip = helper.addStringPropertyToInstance(source,
                    null,
                    qualifiedNameProperty, glossaryQN,
                    this.getClass().getName());
            ip = helper.addStringPropertyToInstance(source,
                    ip,
                    displayNameProperty, glossaryDN,
                    this.getClass().getName());
            EntityDetail result = mc.updateEntityProperties(username, glossaryGuid, ip);
            assertNotNull(result, "Expected the Glossary to be updated.");
            assertEquals(result.getGUID(), glossaryGuid, "Expected updated Glossary to have matching GUID.");
            validatePropertyValue(result.getProperties(), qualifiedNameProperty, glossaryQN);
            validatePropertyValue(result.getProperties(), displayNameProperty, glossaryDN);
            assertEquals(result.getVersion(), 2L, "Expected the version to be incremented by the properties update.");
            assertNotNull(result.getMaintainedBy());
            assertEquals(result.getMaintainedBy().size(), 1);
            assertEquals(result.getMaintainedBy().get(0), username);

            ip = helper.addStringPropertyToInstance(source,
                    null,
                    qualifiedNameProperty, categoryQN,
                    this.getClass().getName());
            ip = helper.addStringPropertyToInstance(source,
                    ip,
                    displayNameProperty, categoryDN,
                    this.getClass().getName());
            result = mc.updateEntityProperties(username, categoryGuid, ip);
            assertNotNull(result, "Expected the GlossaryCategory to be updated.");
            assertEquals(result.getGUID(), categoryGuid, "Expected updated GlossaryCategory to have matching GUID.");
            validatePropertyValue(result.getProperties(), qualifiedNameProperty, categoryQN);
            validatePropertyValue(result.getProperties(), displayNameProperty, categoryDN);
            assertEquals(result.getVersion(), 2L, "Expected the version to be incremented by the properties update.");
            assertNotNull(result.getMaintainedBy());
            assertEquals(result.getMaintainedBy().size(), 1);
            assertEquals(result.getMaintainedBy().get(0), username);

            ip = helper.addStringPropertyToInstance(source,
                    null,
                    qualifiedNameProperty, ctrlTermQN,
                    this.getClass().getName());
            ip = helper.addStringPropertyToInstance(source,
                    ip,
                    displayNameProperty, ctrlTermDN,
                    this.getClass().getName());
            result = mc.updateEntityProperties(username + "_c", ctrlTermGuid, ip);
            assertNotNull(result, "Expected the ControlledGlossaryTerm to be updated.");
            assertEquals(result.getGUID(), ctrlTermGuid, "Expected updated ControlledGlossaryTerm to have matching GUID.");
            validatePropertyValue(result.getProperties(), qualifiedNameProperty, ctrlTermQN);
            validatePropertyValue(result.getProperties(), displayNameProperty, ctrlTermDN);
            assertEquals(result.getVersion(), 4L, "Expected the version to be incremented by the properties update.");
            assertNotNull(result.getMaintainedBy());
            assertEquals(result.getMaintainedBy().size(), 3);
            assertEquals(result.getMaintainedBy().get(0), username);
            assertEquals(result.getMaintainedBy().get(1), username + "_b");
            assertEquals(result.getMaintainedBy().get(2), username + "_c");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "update" }, dependsOnGroups = { "create" }, dependsOnMethods = { "updateEntityProperties" })
    void updateEntityClassification() {
        try {

            InstanceProperties ip = helper.addStringPropertyToInstance(source,
                    null,
                    "scope", "revised-scope",
                    this.getClass().getName());
            EntityDetail result = mc.updateEntityClassification(username, glossaryGuid, "CanonicalVocabulary", ip);
            assertNotNull(result, "Expected classification of Glossary to return the glossary.");
            assertEquals(result.getGUID(), glossaryGuid, "Expected returned Glossary's GUID to match.");
            assertNotNull(result.getClassifications(), "Expected some classifications to exist on returned entity.");
            assertEquals(result.getClassifications().size(), 1, "Expected precisely one classification to exist on returned entity.");
            Classification one = result.getClassifications().get(0);
            assertNotNull(one, "Expected the singular classification to be non-null.");
            assertEquals(one.getName(), "CanonicalVocabulary", "Expected the classification name to be CanonicalVocabulary.");
            validatePropertyValue(one.getProperties(), "scope", "revised-scope");
            assertEquals(one.getVersion(), 2L, "Expected the version of the classification to be incremented by the update.");
            assertEquals(result.getVersion(), 2L, "Expected the version of the entity itself to be unchanged by the update.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "update" }, dependsOnGroups = { "create" })
    void reTypeRelationship() {
        // TODO: no relationships exist today with any super- or sub-types
    }

    @Test(groups = { "update" }, dependsOnGroups = { "create" })
    void updateRelationshipStatus() {
        // TODO: no relationships exist today with valid statuses other than the defaults
    }

    @Test(groups = { "update" }, dependsOnGroups = { "create" })
    void updateRelationshipProperties() {
        try {

            InstanceProperties ip = helper.addStringPropertyToInstance(source,
                    null,
                    descriptionProperty, "omrs-mc-term-categorization-revised",
                    this.getClass().getName());
            Relationship result = mc.updateRelationshipProperties(username, termCategorizationGuid, ip);
            assertNotNull(result, "Expected the TermCategorization to be updated.");
            assertEquals(result.getGUID(), termCategorizationGuid, "Expected updated TermCategorization to have matching GUID.");
            validatePropertyValue(result.getProperties(), descriptionProperty, "omrs-mc-term-categorization-revised");
            assertEquals(result.getVersion(), 2L, "Expected the version to be incremented by the properties update.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "undo" }, dependsOnGroups = { "update" })
    void undoEntityUpdate() {
        try {

            EntityDetail original = mc.getEntityDetail(username, glossaryGuid);
            EntityDetail result = mc.undoEntityUpdate(username, glossaryGuid);
            assertNotNull(result, "Expected the Glossary to be updated.");
            assertEquals(result.getGUID(), glossaryGuid, "Expected updated Glossary to have matching GUID.");
            assertEquals(result.getVersion(), 3L, "Expected the version to be further incremented by the undo operation.");
            assertEquals(original.getClassifications(), result.getClassifications(), "Classifications should match after undo, as they are managed independently.");
            assertNull(result.getProperties().getInstanceProperties().get(displayNameProperty), "Expected undone Glossary update should no longer have a display name.");

            original = mc.getEntityDetail(username, categoryGuid);
            result = mc.undoEntityUpdate(username, categoryGuid);
            assertNotNull(result, "Expected the GlossaryCategory to be updated.");
            assertEquals(result.getGUID(), categoryGuid, "Expected updated GlossaryCategory to have matching GUID.");
            assertEquals(result.getVersion(), 3L, "Expected the version to be further incremented by the undo operation.");
            assertEquals(original.getClassifications(), result.getClassifications(), "Classifications should match after undo, as they are managed independently.");

            original = mc.getEntityDetail(username, ctrlTermGuid);
            result = mc.undoEntityUpdate(username + "_d", ctrlTermGuid);
            assertNotNull(result, "Expected the ControlledGlossaryTerm to be updated.");
            assertEquals(result.getGUID(), ctrlTermGuid, "Expected updated ControlledGlossaryTerm to have matching GUID.");
            assertEquals(result.getVersion(), 5L, "Expected the version to be further incremented by the undo operation.");
            assertEquals(original.getClassifications(), result.getClassifications(), "Classifications should match after undo, as they are managed independently.");
            assertNotNull(result.getMaintainedBy());
            assertEquals(result.getMaintainedBy().size(), 3);
            assertEquals(result.getMaintainedBy().get(0), username);
            assertEquals(result.getMaintainedBy().get(1), username + "_b");
            // Note that there is no username_c because that users' changes have now been undone
            assertEquals(result.getMaintainedBy().get(2), username + "_d");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "undo" }, dependsOnGroups = { "update" })
    void undoRelationshipUpdate() {
        try {

            Relationship result = mc.undoRelationshipUpdate(username, termCategorizationGuid);
            assertNotNull(result, "Expected the TermCategorization to be updated.");
            assertEquals(result.getGUID(), termCategorizationGuid, "Expected updated TermCategorization to have matching GUID.");
            assertEquals(result.getVersion(), 3L, "Expected the version to be further incremented by the undo operation.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "read" }, dependsOnGroups = { "undo" })
    void getEntityDetail() {
        try {

            EntityDetail result = mc.getEntityDetail(username, glossaryGuid);
            assertNotNull(result, "Expected the Glossary to be retrieved.");
            assertEquals(result.getGUID(), glossaryGuid, "Expected retrieved Glossary to have matching GUID.");
            assertEquals(result.getVersion(), 3L, "Expected the latest version of the Glossary.");

            result = mc.getEntityDetail(username, categoryGuid);
            assertNotNull(result, "Expected the GlossaryCategory to be retrieved.");
            assertEquals(result.getGUID(), categoryGuid, "Expected retrieved GlossaryCategory to have matching GUID.");
            assertEquals(result.getVersion(), 3L, "Expected the latest version of the GlossaryCategory.");

            result = mc.getEntityDetail(username, ctrlTermGuid);
            assertNotNull(result, "Expected the ControlledGlossaryTerm to be retrieved.");
            assertEquals(result.getGUID(), ctrlTermGuid, "Expected retrieved ControlledGlossaryTerm to have matching GUID.");
            assertEquals(result.getVersion(), 5L, "Expected the latest version of the ControlledGlossaryTerm.");
            assertEquals(result.getStatus(), InstanceStatus.APPROVED, "Expected the latest status to still be approved.");

            result = mc.getEntityDetail(username, dataClassGuid);
            assertNotNull(result, "Expected the DataClass to be retrieved.");
            assertEquals(result.getGUID(), dataClassGuid, "Expected retrieved DataClass to have matching GUID.");
            assertEquals(result.getVersion(), 1L, "Expected the latest version of the DataClass.");
            validatePropertyValue(result.getProperties(), userDefinedProperty, false);

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "read" }, dependsOnGroups = { "undo" })
    void isEntityKnown() {
        try {

            EntityDetail result = mc.isEntityKnown(username, glossaryGuid);
            assertNotNull(result, "Expected the Glossary to be retrieved.");
            assertEquals(result.getGUID(), glossaryGuid, "Expected retrieved Glossary to have matching GUID.");
            assertEquals(result.getVersion(), 3L, "Expected the latest version of the Glossary.");

            result = mc.isEntityKnown(username, categoryGuid);
            assertNotNull(result, "Expected the GlossaryCategory to be retrieved.");
            assertEquals(result.getGUID(), categoryGuid, "Expected retrieved GlossaryCategory to have matching GUID.");
            assertEquals(result.getVersion(), 3L, "Expected the latest version of the GlossaryCategory.");

            result = mc.isEntityKnown(username, ctrlTermGuid);
            assertNotNull(result, "Expected the ControlledGlossaryTerm to be retrieved.");
            assertEquals(result.getGUID(), ctrlTermGuid, "Expected retrieved ControlledGlossaryTerm to have matching GUID.");
            assertEquals(result.getVersion(), 5L, "Expected the latest version of the ControlledGlossaryTerm.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "read" }, dependsOnGroups = { "undo" })
    void getEntitySummary() {
        try {

            EntitySummary result = mc.getEntitySummary(username, glossaryGuid);
            assertNotNull(result, "Expected the Glossary to be retrieved.");
            assertEquals(result.getGUID(), glossaryGuid, "Expected retrieved Glossary to have matching GUID.");
            assertEquals(result.getVersion(), 3L, "Expected the latest version of the Glossary.");

            result = mc.getEntitySummary(username, categoryGuid);
            assertNotNull(result, "Expected the GlossaryCategory to be retrieved.");
            assertEquals(result.getGUID(), categoryGuid, "Expected retrieved GlossaryCategory to have matching GUID.");
            assertEquals(result.getVersion(), 3L, "Expected the latest version of the GlossaryCategory.");

            result = mc.getEntitySummary(username, ctrlTermGuid);
            assertNotNull(result, "Expected the ControlledGlossaryTerm to be retrieved.");
            assertEquals(result.getGUID(), ctrlTermGuid, "Expected retrieved ControlledGlossaryTerm to have matching GUID.");
            assertEquals(result.getVersion(), 5L, "Expected the latest version of the ControlledGlossaryTerm.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "read" }, dependsOnGroups = { "undo" })
    void getEntityDetailHistory() {
        try {

            List<EntityDetail> results = mc.getEntityDetailHistory(username, glossaryGuid, null, null, 0, 100, HistorySequencingOrder.BACKWARDS);
            assertTrue(results != null && !results.isEmpty(), "Expected the Glossary's history to be retrieved.");
            // Note that we expect 5 here rather than 3, as even though the Glossary itself has
            // only 3 enumerated versions, the fact that its classification has 2 enumerated versions
            // (both applicable to the same Glossary version), means there will be a total of 5 visible versions
            assertEquals(results.size(), 5, "Expected 5 historical versions of the Glossary.");
            assertTrue(results.get(0).getVersion() > results.get(1).getVersion(), "Expected results to be sorted in reverse-chronological order.");

            results = mc.getEntityDetailHistory(username, categoryGuid, null, null, 0, 100, HistorySequencingOrder.FORWARDS);
            assertTrue(results != null && !results.isEmpty(), "Expected the GlossaryCategory's history to be retrieved.");
            assertEquals(results.size(), 4, "Expected 4 historical versions of the GlossaryCategory.");
            assertTrue(results.get(0).getVersion() < results.get(3).getVersion(), "Expected results to be sorted in chronological order.");

            results = mc.getEntityDetailHistory(username, ctrlTermGuid, null, null, 0, 100, HistorySequencingOrder.BACKWARDS);
            assertTrue(results != null && !results.isEmpty(), "Expected the ControlledGlossaryTerm's history to be retrieved.");
            assertEquals(results.size(), 5, "Expected 5 historical versions of the ControlledGlossaryTerm.");
            assertTrue(results.get(0).getVersion() > results.get(1).getVersion(), "Expected results to be sorted in reverse-chronological order.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "read" }, dependsOnGroups = { "undo" })
    void getRelationship() {
        try {

            Relationship result = mc.getRelationship(username, categoryAnchorGuid);
            assertNotNull(result, "Expected the CategoryAnchor to be retrieved.");
            assertEquals(result.getGUID(), categoryAnchorGuid, "Expected retrieved CategoryAnchor to have matching GUID.");
            assertEquals(result.getVersion(), 1L, "Expected the latest version of the CategoryAnchor.");

            result = mc.getRelationship(username, termAnchorGuid);
            assertNotNull(result, "Expected the TermAnchor to be retrieved.");
            assertEquals(result.getGUID(), termAnchorGuid, "Expected retrieved TermAnchor to have matching GUID.");
            assertEquals(result.getVersion(), 1L, "Expected the latest version of the TermAnchor.");

            result = mc.getRelationship(username, termCategorizationGuid);
            assertNotNull(result, "Expected the TermCategorization to be retrieved.");
            assertEquals(result.getGUID(), termCategorizationGuid, "Expected retrieved TermCategorization to have matching GUID.");
            assertEquals(result.getVersion(), 3L, "Expected the latest version of the TermCategorization.");

            result = mc.getRelationship(username, dataClassAssignmentGuid);
            assertNotNull(result, "Expected the DataClassAssignment to be retrieved.");
            assertEquals(result.getGUID(), dataClassAssignmentGuid, "Expected retrieved DataClassAssignment to have matching GUID.");
            assertEquals(result.getVersion(), 1L, "Expected the latest version of the DataClassAssignment.");
            validatePropertyValue(result.getProperties(), partialMatchProperty, false);

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "read" }, dependsOnGroups = { "undo" })
    void isRelationshipKnown() {
        try {

            Relationship result = mc.isRelationshipKnown(username, categoryAnchorGuid);
            assertNotNull(result, "Expected the CategoryAnchor to be retrieved.");
            assertEquals(result.getGUID(), categoryAnchorGuid, "Expected retrieved CategoryAnchor to have matching GUID.");
            assertEquals(result.getVersion(), 1L, "Expected the latest version of the CategoryAnchor.");

            result = mc.isRelationshipKnown(username, termAnchorGuid);
            assertNotNull(result, "Expected the TermAnchor to be retrieved.");
            assertEquals(result.getGUID(), termAnchorGuid, "Expected retrieved TermAnchor to have matching GUID.");
            assertEquals(result.getVersion(), 1L, "Expected the latest version of the TermAnchor.");

            result = mc.isRelationshipKnown(username, termCategorizationGuid);
            assertNotNull(result, "Expected the TermCategorization to be retrieved.");
            assertEquals(result.getGUID(), termCategorizationGuid, "Expected retrieved TermCategorization to have matching GUID.");
            assertEquals(result.getVersion(), 3L, "Expected the latest version of the TermCategorization.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "read" }, dependsOnGroups = { "undo" })
    void getRelationshipHistory() {
        try {

            List<Relationship> results = mc.getRelationshipHistory(username, termCategorizationGuid, null, null, 0, 100, HistorySequencingOrder.BACKWARDS);
            assertTrue(results != null && !results.isEmpty(), "Expected the TermCategorization's history to be retrieved.");
            assertEquals(results.size(), 3, "Expected 3 historical versions of the TermCategorization.");
            assertTrue(results.get(0).getVersion() > results.get(1).getVersion(), "Expected results to be sorted in reverse-chronological order.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "read" }, dependsOnGroups = { "undo" })
    void getHomeClassifications() {
        try {

            List<Classification> results = mc.getHomeClassifications(username, glossaryGuid);
            assertTrue(results != null && !results.isEmpty(), "Expected the Glossary's homed classifications to be non-empty.");
            assertEquals(results.size(), 1, "Expected precisely one homed classification.");
            assertEquals(results.get(0).getName(), "CanonicalVocabulary", "Expected the singular classification to be a CanonicalVocabulary classification.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "search" }, dependsOnGroups = { "read" })
    void findEntities() {
        try {

            String version = "version";

            InstanceProperties ip = helper.addIntPropertyToInstance(source,
                    null,
                    version,
                    4,
                    this.getClass().getName());
            InstancePropertyValue four = ip.getPropertyValue(version);
            ip = helper.addIntPropertyToInstance(source,
                    null,
                    version,
                    10,
                    this.getClass().getName());
            InstancePropertyValue ten = ip.getPropertyValue(version);

            PropertyCondition gt4 = new PropertyCondition();
            gt4.setProperty(version);
            gt4.setOperator(PropertyComparisonOperator.GT);
            gt4.setValue(four);
            PropertyCondition lt10 = new PropertyCondition();
            lt10.setProperty(version);
            lt10.setOperator(PropertyComparisonOperator.LT);
            lt10.setValue(ten);
            List<PropertyCondition> conditions = new ArrayList<>();
            conditions.add(gt4);
            conditions.add(lt10);
            SearchProperties matchProperties = new SearchProperties();
            matchProperties.setMatchCriteria(MatchCriteria.ALL);
            matchProperties.setConditions(conditions);

            List<EntityDetail> results = mc.findEntities(username,
                    null,
                    null,
                    matchProperties,
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    100);
            assertTrue(results != null && !results.isEmpty(), "Expected non-empty search results.");
            assertEquals(results.size(), 1, "Expected precisely one search result.");
            assertEquals(results.get(0).getGUID(), ctrlTermGuid, "Expected the singular search result to be the ControlledGlossaryTerm.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "search" }, dependsOnGroups = { "read" })
    void findEntitiesByProperty() {
        try {

            List<InstanceStatus> limitByStatus = new ArrayList<>();
            limitByStatus.add(InstanceStatus.APPROVED);

            List<EntityDetail> results = mc.findEntitiesByProperty(username,
                    null,
                    null,
                    null,
                    0,
                    limitByStatus,
                    null,
                    null,
                    null,
                    null,
                    100);
            assertTrue(results != null && !results.isEmpty(), "Expected non-empty search results.");
            assertEquals(results.size(), 1, "Expected precisely one search result.");
            assertEquals(results.get(0).getGUID(), ctrlTermGuid, "Expected the singular search result to be the ControlledGlossaryTerm.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "search" }, dependsOnGroups = { "read" })
    void findEntitiesByPropertyNone() {
        try {

            InstanceProperties matchProperties = helper.addStringPropertyToInstance(source,
                    null,
                    displayNameProperty,
                    helper.getExactMatchRegex("glossary"),
                    this.getClass().getName());

            // Search for Referenceables using an overlapping attribute (like 'displayName') that can appear on many
            // subtypes but not on the super-type itself, and use the NONE match criteria
            List<EntityDetail> results = mc.findEntitiesByProperty(username,
                    "a32316b8-dc8c-48c5-b12b-71c1b2a080bf",
                    matchProperties,
                    MatchCriteria.NONE,
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    100);
            assertTrue(results != null && !results.isEmpty(), "Expected non-empty search results.");
            assertEquals(results.size(), 14, "Expected precisely fourteen search results.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "search" }, dependsOnGroups = { "read" })
    void findEntitiesByPropertyValue() {
        try {

            List<EntityDetail> results = mc.findEntitiesByPropertyValue(username,
                    referenceableGuid,
                    helper.getStartsWithRegex("omrs-mc-"),
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    100);
            assertTrue(results != null && !results.isEmpty(), "Expected non-empty search results.");
            assertEquals(results.size(), 4, "Expected precisely four search results.");

            results = mc.findEntitiesByPropertyValue(username,
                    referenceableGuid,
                    helper.getContainsRegex("-mc-"),
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    100);
            assertTrue(results != null && !results.isEmpty(), "Expected non-empty search results.");
            assertEquals(results.size(), 6, "Expected precisely six search results.");

            results = mc.findEntitiesByPropertyValue(username,
                    referenceableGuid,
                    helper.getStartsWithRegex("oMrS-mc-", true),
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    100);
            assertTrue(results != null && !results.isEmpty(), "Expected non-empty search results.");
            assertEquals(results.size(), 4, "Expected precisely four search results.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "search" }, dependsOnGroups = { "read" })
    void findEntitiesByClassification() {
        try {

            InstanceProperties matchProperties = helper.addStringPropertyToInstance(source,
                    null,
                    "scope",
                    helper.getExactMatchRegex("revised-scope"),
                    this.getClass().getName());

            List<EntityDetail> results = mc.findEntitiesByClassification(username,
                    null,
                    "CanonicalVocabulary",
                    matchProperties,
                    MatchCriteria.ALL,
                    0,
                    null,
                    null,
                    null,
                    null,
                    100);
            assertTrue(results != null && !results.isEmpty(), "Expected non-empty search results.");
            assertEquals(results.size(), 1, "Expected precisely one search result.");
            assertEquals(results.get(0).getGUID(), glossaryGuid, "Expected the singular search result to be the Glossary.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "search" }, dependsOnGroups = { "read" })
    void findRelationships() {
        try {

            PropertyCondition condition = new PropertyCondition();
            condition.setProperty(descriptionProperty);
            condition.setOperator(PropertyComparisonOperator.NOT_NULL);
            List<PropertyCondition> conditions = new ArrayList<>();
            conditions.add(condition);
            SearchProperties matchProperties = new SearchProperties();
            matchProperties.setMatchCriteria(MatchCriteria.ALL);
            matchProperties.setConditions(conditions);

            List<Relationship> results = mc.findRelationships(username,
                    null,
                    null,
                    matchProperties,
                    0,
                    null,
                    null,
                    null,
                    null,
                    100);
            assertTrue(results != null && !results.isEmpty(), "Expected non-empty search results.");
            assertEquals(results.size(), 1, "Expected precisely one search result.");
            assertEquals(results.get(0).getGUID(), termCategorizationGuid, "Expected the singular search result to be the TermCategorization.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "search" }, dependsOnGroups = { "read" })
    void findRelationshipsByProperty() {
        try {

            InstanceProperties matchProperties = helper.addEnumPropertyToInstance(source,
                    null,
                    "status",
                    null,
                    null,
                    1,
                    "Active",
                    "The term relationship is approved and in use.",
                    this.getClass().getName());

            List<Relationship> results = mc.findRelationshipsByProperty(username,
                    null,
                    matchProperties,
                    MatchCriteria.ALL,
                    0,
                    null,
                    null,
                    null,
                    null,
                    100);
            assertTrue(results != null && !results.isEmpty(), "Expected non-empty search results.");
            assertEquals(results.size(), 1, "Expected precisely one search result.");
            assertEquals(results.get(0).getGUID(), termCategorizationGuid, "Expected the singular search result to be the TermCategorization.");

            matchProperties = helper.addBooleanPropertyToInstance(source,
                    null,
                    partialMatchProperty,
                    false,
                    this.getClass().getName());
            results = mc.findRelationshipsByProperty(username,
                    null,
                    matchProperties,
                    MatchCriteria.ALL,
                    0,
                    null,
                    null,
                    null,
                    null,
                    100);
            assertTrue(results != null && !results.isEmpty(), "Expected non-empty search results.");
            assertEquals(results.size(), 1, "Expected precisely one search result.");
            assertEquals(results.get(0).getGUID(), dataClassAssignmentGuid, "Expected the singular search result to be the DataClassAssignment.");

            matchProperties = helper.addDatePropertyToInstance(source,
                    null,
                    "deploymentTime",
                    now,
                    this.getClass().getName());
            results = mc.findRelationshipsByProperty(username,
                    null,
                    matchProperties,
                    MatchCriteria.ALL,
                    0,
                    null,
                    null,
                    null,
                    null,
                    100);
            assertTrue(results != null && !results.isEmpty(), "Expected non-empty search results.");
            assertEquals(results.size(), 1, "Expected precisely one search result.");
            assertEquals(results.get(0).getType().getTypeDefName(), "SoftwareServerPlatformDeployment");
            assertEquals(results.get(0).getType().getTypeDefGUID(), softwareServerPlatformDeploymentTypeGuid);
            assertEquals(results.get(0).getGUID(), softwareServerPlatformDeploymentGuid, "Expected the singular search result to be the SoftwareServerPlatformDeployment.");

            results = mc.findRelationshipsByProperty(username,
                    softwareServerPlatformDeploymentTypeGuid,
                    matchProperties,
                    MatchCriteria.ALL,
                    0,
                    null,
                    null,
                    null,
                    null,
                    100);
            assertTrue(results != null && !results.isEmpty(), "Expected non-empty search results.");
            assertEquals(results.size(), 1, "Expected precisely one search result.");
            assertEquals(results.get(0).getType().getTypeDefName(), "SoftwareServerPlatformDeployment");
            assertEquals(results.get(0).getType().getTypeDefGUID(), softwareServerPlatformDeploymentTypeGuid);
            assertEquals(results.get(0).getGUID(), softwareServerPlatformDeploymentGuid, "Expected the singular search result to be the SoftwareServerPlatformDeployment.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "search" }, dependsOnGroups = { "read" })
    void findRelationshipsByPropertyValue() {
        try {

            List<Relationship> results = mc.findRelationshipsByPropertyValue(username,
                    termCategorizationTypeGuid,
                    helper.getStartsWithRegex("omrs-mc-"),
                    0,
                    null,
                    null,
                    null,
                    null,
                    100);
            assertTrue(results != null && !results.isEmpty(), "Expected non-empty search results.");
            assertEquals(results.size(), 1, "Expected precisely one search result.");
            assertEquals(results.get(0).getGUID(), termCategorizationGuid, "Expected the singular search result to be the TermCategorization.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "search" }, dependsOnGroups = { "read" })
    void getRelationshipsForEntity() {
        try {

            List<Relationship> results = mc.getRelationshipsForEntity(username,
                    categoryGuid,
                    null,
                    0,
                    null,
                    null,
                    null,
                    null,
                    100);
            assertTrue(results != null && !results.isEmpty(), "Expected non-empty results.");
            assertEquals(results.size(), 3, "Expected precisely three results.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "search" }, dependsOnGroups = { "read" })
    void getLinkingEntities() {
        try {

            InstanceGraph results = mc.getLinkingEntities(username,
                    glossaryGuid,
                    ctrlTermGuid,
                    null,
                    null);
            assertNotNull(results, "Expected non-empty results.");
            List<EntityDetail> entities = results.getEntities();
            assertNotNull(entities, "Expected non-empty list of entities.");
            assertEquals(entities.size(), 3, "Expected the list of linking entities to be precisely 3.");
            List<Relationship> relationships = results.getRelationships();
            assertNotNull(relationships, "Expected non-empty list of relationships.");
            assertEquals(relationships.size(), 3, "Expected the list of linking relationships to be precisely 3.");

            results = mc.getLinkingEntities(username,
                    glossaryGuid,
                    glossaryGuid,
                    null,
                    null);
            assertNotNull(results, "Expected non-empty results when the starting and ending GUIDs are identical.");
            entities = results.getEntities();
            assertNotNull(entities, "Expected non-empty list of entities when the starting and ending GUIDs are identical.");
            assertEquals(entities.size(), 1, "Expected the list of linking entities to be precisely 1 when the starting and ending GUIDs are identical.");
            assertNull(results.getRelationships(), "Expected empty list of relationships when the starting and ending GUIDs are identical.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "search" }, dependsOnGroups = { "read" })
    void getEntityNeighborhood() {
        try {

            InstanceGraph results = mc.getEntityNeighborhood(username,
                    categoryGuid,
                    null,
                    null,
                    null,
                    null,
                    null,
                    -1);
            assertNotNull(results, "Expected non-empty results.");
            List<EntityDetail> entities = results.getEntities();
            assertNotNull(entities, "Expected non-empty list of entities.");
            assertEquals(entities.size(), 6, "Expected the list of neighboring entities to be precisely 6.");
            List<Relationship> relationships = results.getRelationships();
            assertNotNull(relationships, "Expected non-empty list of relationships.");
            assertEquals(relationships.size(), 6, "Expected the list of neighboring relationships to be precisely 6.");

            List<String> entityTypes = new ArrayList<>();
            entityTypes.add(glossaryTypeGuid);
            List<String> relationshipTypes = new ArrayList<>();
            relationshipTypes.add(categoryAnchorTypeGuid);
            List<String> classifications = new ArrayList<>();
            classifications.add("CanonicalVocabulary");

            results = mc.getEntityNeighborhood(username,
                    categoryGuid,
                    entityTypes,
                    relationshipTypes,
                    null,
                    classifications,
                    null,
                    -1);
            assertNotNull(results, "Expected non-empty results when narrowed.");
            entities = results.getEntities();
            assertNotNull(entities, "Expected non-empty list of entities when narrowed.");
            assertEquals(entities.size(), 2, "Expected the list of neighboring entities to be precisely 2 when narrowed.");
            relationships = results.getRelationships();
            assertNotNull(relationships, "Expected non-empty list of relationships when narrowed.");
            assertEquals(relationships.size(), 1, "Expected the list of neighboring relationships to be precisely 1 when narrowed.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "search" }, dependsOnGroups = { "read" })
    void getRelatedEntities() {
        try {

            List<EntityDetail> results = mc.getRelatedEntities(username,
                    glossaryGuid,
                    null,
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    100);
            assertNotNull(results, "Expected non-empty results.");
            assertEquals(results.size(), 6, "Expected the list of related entities to be precisely 6.");

            List<String> classifications = new ArrayList<>();
            classifications.add("SubjectArea");

            results = mc.getRelatedEntities(username,
                    glossaryGuid,
                    null,
                    0,
                    null,
                    classifications,
                    null,
                    null,
                    null,
                    100);
            assertNotNull(results, "Expected non-empty results.");
            assertEquals(results.size(), 2, "Expected the list of related entities to be precisely 2 when narrowed.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "declassify" }, dependsOnGroups = { "search" })
    void declassifyEntity() {
        try {

            EntityDetail result = mc.declassifyEntity(username, glossaryGuid, "CanonicalVocabulary");
            assertNotNull(result, "Expected declassification of Glossary to return the glossary.");
            assertEquals(result.getGUID(), glossaryGuid, "Expected returned Glossary's GUID to match.");
            assertNull(result.getClassifications(), "Expected no classifications to exist on returned entity.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "rehome" }, dependsOnGroups = { "declassify" })
    void reHomeEntity() {
        try {

            EntityDetail result = mc.reHomeEntity(username,
                    ctrlTermGuidRC,
                    ctrlTermTypeGuid,
                    "ControlledGlossaryTerm",
                    externalMetadataCollectionId,
                    MockConnection.METADATA_COLLECTION_ID,
                    MockConnection.METADATA_COLLECTION_NAME);
            assertNotNull(result, "Expected re-homing of ControlledGlossaryTerm to return the term.");
            assertEquals(result.getGUID(), ctrlTermGuidRC, "Expected returned ControlledGlossaryTerm's GUID to match.");
            assertEquals(result.getMetadataCollectionId(), MockConnection.METADATA_COLLECTION_ID, "Expected ControlledGlossaryTerm's metadataCollectionId to now match the internal one.");
            assertEquals(result.getMetadataCollectionName(), MockConnection.METADATA_COLLECTION_NAME, "Expected ControlledGlossaryTerm's metadataCollectionName to now match the internal one.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "rehome" }, dependsOnGroups = { "declassify" })
    void reHomeRelationship() {
        try {

            Relationship result = mc.reHomeRelationship(username,
                    termCategorizationGuidRC,
                    termCategorizationTypeGuid,
                    "TermCategorization",
                    externalMetadataCollectionId,
                    MockConnection.METADATA_COLLECTION_ID,
                    MockConnection.METADATA_COLLECTION_NAME);
            assertNotNull(result, "Expected re-homing of TermCategorization to return the relationship.");
            assertEquals(result.getGUID(), termCategorizationGuidRC, "Expected returned TermCategorization's GUID to match.");
            assertEquals(result.getMetadataCollectionId(), MockConnection.METADATA_COLLECTION_ID, "Expected TermCategorization's metadataCollectionId to now match the internal one.");
            assertEquals(result.getMetadataCollectionName(), MockConnection.METADATA_COLLECTION_NAME, "Expected TermCategorization's metadataCollectionName to now match the internal one.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "reidentify" }, dependsOnGroups = { "rehome" })
    void reIdentifyRelationship() {
        try {

            String guid = UUID.randomUUID().toString();
            Relationship result = mc.reIdentifyRelationship(username,
                    categoryAnchorTypeGuid,
                    "CategoryAnchor",
                    categoryAnchorGuid,
                    guid);
            assertNotNull(result, "Expected re-identification of CategoryAnchor to return the relationship.");
            assertEquals(result.getGUID(), guid, "Expected returned CategoryAnchor's GUID to match.");
            categoryAnchorGuid = guid;
            assertEquals(result.getVersion(), 2L, "Expected version to be incremented by the re-identification operation.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "reidentify" }, dependsOnGroups = { "rehome" })
    void reIdentifyEntity() {
        try {

            String guid = UUID.randomUUID().toString();
            EntityDetail result = mc.reIdentifyEntity(username,
                    glossaryTypeGuid,
                    "Glossary",
                    glossaryGuid,
                    guid);
            assertNotNull(result, "Expected re-identification of Glossary to return the glossary.");
            assertEquals(result.getGUID(), guid, "Expected returned Glossary's GUID to match.");
            glossaryGuid = guid;
            assertEquals(result.getVersion(), 4L, "Expected version to be incremented by the re-identification operation.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "deleteR" }, dependsOnGroups = { "reidentify" })
    void deleteRelationship() {
        try {

            Relationship result = mc.deleteRelationship(username,
                    categoryAnchorTypeGuid,
                    "CategoryAnchor",
                    categoryAnchorGuid);
            assertNotNull(result, "Expected deletion of CategoryAnchor to return the relationship.");
            assertEquals(result.getGUID(), categoryAnchorGuid, "Expected returned CategoryAnchor's GUID to match.");
            assertEquals(result.getStatus(), InstanceStatus.DELETED, "Expected returned CategoryAnchor's status to be deleted.");
            assertEquals(result.getVersion(), 3L, "Expected version to be incremented by the deletion operation.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "restoreR" }, dependsOnGroups = { "deleteR" })
    void restoreRelationship() {
        try {

            Relationship result = mc.restoreRelationship(username, categoryAnchorGuid);
            assertNotNull(result, "Expected restore of CategoryAnchor to return the relationship.");
            assertEquals(result.getGUID(), categoryAnchorGuid, "Expected restored CategoryAnchor's GUID to match.");
            assertNotEquals(result.getStatus(), InstanceStatus.DELETED, "Expected restored CategoryAnchor's status to not be deleted.");
            assertEquals(result.getVersion(), 4L, "Expected version to be incremented by the restore operation.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "purgeR" }, dependsOnGroups = { "restoreR" })
    void purgeRelationshipReferenceCopy() {
        try {

            mc.purgeRelationshipReferenceCopy(username, termCategorizationGuidRC, termCategorizationTypeGuid, "TermCategorization", externalMetadataCollectionId);
            Relationship result = mc.isRelationshipKnown(username, termCategorizationGuidRC);
            assertNull(result, "Expected the TermCategorization to no longer exist after being purged.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "purgeR" }, dependsOnGroups = { "restoreR" })
    void purgeRelationship() {
        try {

            mc.deleteRelationship(username, categoryAnchorTypeGuid, "CategoryAnchor", categoryAnchorGuid);
            mc.purgeRelationship(username, categoryAnchorTypeGuid, "CategoryAnchor", categoryAnchorGuid);
            Relationship result = mc.isRelationshipKnown(username, categoryAnchorGuid);
            assertNull(result, "Expected the CategoryAnchor to no longer exist after being purged.");

            mc.deleteRelationship(username, termAnchorTypeGuid, "TermAnchor", termAnchorGuid);
            mc.purgeRelationship(username, termAnchorTypeGuid, "TermAnchor", termAnchorGuid);
            result = mc.isRelationshipKnown(username, termAnchorGuid);
            assertNull(result, "Expected the TermAnchor to no longer exist after being purged.");

            mc.deleteRelationship(username, termCategorizationTypeGuid, "TermCategorization", termCategorizationGuid);
            mc.purgeRelationship(username, termCategorizationTypeGuid, "TermCategorization", termCategorizationGuid);
            result = mc.isRelationshipKnown(username, termCategorizationGuid);
            assertNull(result, "Expected the TermCategorization to no longer exist after being purged.");

            mc.deleteRelationship(username, categoryLinkTypeGuid, "CategoryHierarchyLink", categoryLinkGuidEX);
            mc.purgeRelationship(username, categoryLinkTypeGuid, "CategoryHierarchyLink", categoryLinkGuidEX);
            result = mc.isRelationshipKnown(username, categoryLinkGuidEX);
            assertNull(result, "Expected the CategoryHierarchyLink to no longer exist after being purged.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "deleteE" }, dependsOnGroups = { "purgeR" })
    void deleteEntity() {
        try {

            EntityDetail result = mc.deleteEntity(username,
                    glossaryTypeGuid,
                    "Glossary",
                    glossaryGuid);
            assertNotNull(result, "Expected deletion of Glossary to return the glossary.");
            assertEquals(result.getGUID(), glossaryGuid, "Expected returned Glossary's GUID to match.");
            assertEquals(result.getStatus(), InstanceStatus.DELETED, "Expected returned Glossary's status to be deleted.");
            assertEquals(result.getVersion(), 5L, "Expected version to be incremented by the deletion operation.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "restoreE" }, dependsOnGroups = { "deleteE" })
    void restoreEntity() {
        try {

            EntityDetail result = mc.restoreEntity(username, glossaryGuid);
            assertNotNull(result, "Expected restore of Glossary to return the glossary.");
            assertEquals(result.getGUID(), glossaryGuid, "Expected restored Glossary's GUID to match.");
            assertNotEquals(result.getStatus(), InstanceStatus.DELETED, "Expected restored Glossary's status to not be deleted.");
            assertEquals(result.getVersion(), 6L, "Expected version to be incremented by the restore operation.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "purgeE" }, dependsOnGroups = { "restoreE" })
    void purgeClassificationReferenceCopy() {
        try {

            EntityDetail entity = mc.getEntityDetail(username, categoryGuid);
            List<Classification> classifications = entity.getClassifications();
            assertNotNull(classifications, "Expected some classifications on the homed GlossaryCategory.");
            assertEquals(classifications.size(), 1, "Expected precisely one classification on the homed GlossaryCategory.");

            mc.purgeClassificationReferenceCopy(username, entity, classifications.get(0));

            EntityDetail retrieved = mc.getEntityDetail(username, categoryGuid);
            assertNotNull(retrieved, "Expected retrieval of GlossaryCategory to return the category.");
            assertEquals(retrieved.getGUID(), categoryGuid, "Expected returned GlossaryCategory's GUID to match.");
            assertNull(retrieved.getClassifications(), "Expected no classifications to exist on returned entity.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "purgeE" }, dependsOnGroups = { "restoreE" })
    void purgeEntityReferenceCopy() {
        try {

            mc.purgeEntityReferenceCopy(username, ctrlTermGuidRC, ctrlTermTypeGuid, "ControlledGlossaryTerm", externalMetadataCollectionId);
            EntityDetail result = mc.isEntityKnown(username, ctrlTermGuidRC);
            assertNull(result, "Expected the ControlledGlossaryTerm to no longer exist after being purged.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test(groups = { "purgeE" }, dependsOnGroups = { "restoreE" })
    void purgeEntity() {
        try {

            mc.deleteEntity(username, glossaryTypeGuid, "Glossary", glossaryGuid);
            mc.purgeEntity(username, glossaryTypeGuid, "Glossary", glossaryGuid);
            EntityDetail result = mc.isEntityKnown(username, glossaryGuid);
            assertNull(result, "Expected the Glossary to no longer exist after being purged.");

            mc.deleteEntity(username, categoryTypeGuid, "GlossaryCategory", categoryGuid);
            mc.purgeEntity(username, categoryTypeGuid, "GlossaryCategory", categoryGuid);
            result = mc.isEntityKnown(username, categoryGuid);
            assertNull(result, "Expected the GlossaryCategory to no longer exist after being purged.");

            mc.deleteEntity(username, ctrlTermTypeGuid, "ControlledGlossaryTerm", ctrlTermGuid);
            mc.purgeEntity(username, ctrlTermTypeGuid, "ControlledGlossaryTerm", ctrlTermGuid);
            result = mc.isEntityKnown(username, ctrlTermGuid);
            assertNull(result, "Expected the ControlledGlossaryTerm to no longer exist after being purged.");

            mc.deleteEntity(username, categoryTypeGuid, "GlossaryCategory", categoryGuidEX);
            mc.purgeEntity(username, categoryTypeGuid, "GlossaryCategory", categoryGuidEX);
            result = mc.isEntityKnown(username, categoryGuidEX);
            assertNull(result, "Expected the GlossaryCategory to no longer exist after being purged.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    private void validatePropertyValue(InstanceProperties properties, String propertyName, Object expectedValue) {
        assertNotNull(properties, "Expected some properties to be defined.");
        Map<String, InstancePropertyValue> map = properties.getInstanceProperties();
        assertTrue(map != null && !map.isEmpty(), "Expected the map of properties to be non-empty.");
        InstancePropertyValue actualValue = map.get(propertyName);
        if (expectedValue == null) {
            assertTrue(actualValue == null || actualValue.valueAsObject() == null, "Expected value to be null, but found: " + (actualValue == null ? "<null>" : actualValue.valueAsObject()));
        } else {
            assertNotNull(actualValue, "Expected actual value to be non-null.");
            assertEquals(actualValue.valueAsObject(), expectedValue, "Expected values for '" + propertyName + "' to be: " + expectedValue);
        }
    }

}
