/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OpenMetadataTypesArchive builds an open metadata archive containing all the standard open metadata types.
 * These types have hardcoded dates and guids so that however many times this archive is rebuilt, it will
 * produce the same content.
 * <p>
 * Details of the open metadata types are documented on the wiki:
 * <a href="https://egeria-project.org/types/">The Open Metadata Type System</a>
 * </p>
 * <p>
 * There are 8 areas, each covering a different topic area of metadata.  The module breaks down the process of creating
 * the models into the areas and then the individual models to simplify the maintenance of this class
 * </p>
 */
public class OpenMetadataTypesArchive
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "6.2-SNAPSHOT";
    private static final String                  originatorName     = "Egeria";
    private static final String                  originatorLicense  = "Apache-2.0";
    private static final Date                    creationDate       = new Date(1769277597779L);

    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "6.2-SNAPSHOT";


    private final OMRSArchiveBuilder archiveBuilder;
    private final OMRSArchiveHelper  archiveHelper;

    /**
     * Default constructor sets up the archive builder.  This in turn sets up the header for the archive.
     */
    public OpenMetadataTypesArchive()
    {
        this.archiveBuilder = new OMRSArchiveBuilder(archiveGUID,
                                                     archiveName,
                                                     archiveDescription,
                                                     archiveType,
                                                     archiveVersion,
                                                     originatorName,
                                                     originatorLicense,
                                                     creationDate,
                                                     null);

        this.archiveHelper = new OMRSArchiveHelper(archiveBuilder,
                                                   archiveGUID,
                                                   originatorName,
                                                   creationDate,
                                                   versionNumber,
                                                   versionName);
    }


    /**
     * Chained constructor sets up the archive builder.  This in turn sets up the header for the archive.
     *
     * @param archiveBuilder accumulator for types
     */
    public OpenMetadataTypesArchive(OMRSArchiveBuilder archiveBuilder)
    {
        this.archiveBuilder = archiveBuilder;

        this.archiveHelper = new OMRSArchiveHelper(archiveBuilder,
                                                   archiveGUID,
                                                   originatorName,
                                                   creationDate,
                                                   versionNumber,
                                                   versionName);
    }


    /**
     * Return the unique identifier for this archive.
     *
     * @return String guid
     */
    public String getArchiveGUID()
    {
        return archiveGUID;
    }


    /**
     * Returns the open metadata type archive containing all the standard open metadata types.
     *
     * @return populated open metadata archive object
     */
    public OpenMetadataArchive getOpenMetadataArchive()
    {
        final String methodName = "getOpenMetadataArchive";

        if (this.archiveBuilder != null)
        {
            /*
             * Build the type archive.
             */
            this.getOriginalTypes();

            /*
             * The completed archive is ready to be packaged up and returned
             */
            return this.archiveBuilder.getOpenMetadataArchive();
        }
        else
        {
            /*
             * This is a logic error since it means the creation of the archive builder threw an exception
             * in the constructor and so this object should not be used.
             */
            throw new OMRSLogicErrorException(OMRSErrorCode.ARCHIVE_UNAVAILABLE.getMessageDefinition(),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Add the types from this archive to the archive builder supplied in the
     * constructor.
     */
    public void getOriginalTypes()
    {
        OpenMetadataTypesArchive6_1 previousTypes = new OpenMetadataTypesArchive6_1(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * New types for this release
         */
        update0010BaseModel();
        update0025Locations();
        update0112People();
        update0205ConnectionLinkage();
        update0221DocumentStores();
        update0423SecurityDefinitions();
        update0505SchemaAttributes();
        add0280SoftwareDevelopmentAssets();
        add0281SoftwareModules();
        add0282ReleasedSoftwareComponents();
        add0462GovernanceActionProcesses();
        add0680CodeAnalysis();
        update0610Annotations();
        update0710DigitalServices();
        update0735SolutionPortsAndWires();
        update0770LineageMapping();
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0010BaseModel()
    {
        this.archiveBuilder.addTypeDefPatch(updateLineageRelationship());
    }


    /**
     * LineageRelationship defines iscQualifiedName, which identifies the information supply chain that the
     * relationship belongs to.  Where multiple information supply chains make use of the same dependency, there
     * needs to be a separate relationship for each of them, so multiple relationships between the same two
     * entities are permitted.
     *
     * @return patch
     */
    private TypeDefPatch updateLineageRelationship()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.LINEAGE_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setUpdateMultiLink(true);
        typeDefPatch.setMultiLink(true);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0205ConnectionLinkage()
    {
        this.archiveBuilder.addRelationshipDef(getResourceConnectionRelationship());
        this.archiveBuilder.addTypeDefPatch(updateAssetConnectionRelationship());
    }


    /**
     * ResourceConnection links any referenceable to the connection that describes how to create a connector
     * that can access its digital resource.  It replaces AssetConnection so that elements that are not assets -
     * such as software capabilities - can also be linked to a connection.
     *
     * @return relationship def
     */
    private RelationshipDef getResourceConnectionRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP,
                                                                               this.archiveBuilder.getRelationshipDef(OpenMetadataType.LABELED_RELATIONSHIP.typeName),
                                                                               ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "connectedResources";
        final String                     end1AttributeDescription     = "Elements that describe the digital resource.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "resourceConnections";
        final String                     end2AttributeDescription     = "Connections to the digital resource.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.CONNECTION.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    /**
     * AssetConnection becomes a subtype of ResourceConnection so that existing relationships continue to
     * work.  It is deprecated in favour of ResourceConnection.
     *
     * @return patch
     */
    private TypeDefPatch updateAssetConnectionRelationship()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.ASSET_CONNECTION_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getRelationshipDef(OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeName));
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0221DocumentStores()
    {
        this.archiveBuilder.addTypeDefPatch(updateDocumentStoreEntity());
        this.archiveBuilder.addTypeDefPatch(updateMediaCollectionEntity());
    }


    /**
     * DocumentStoreProperties and MediaCollectionProperties both carry embeddedMetadata, and the builder and
     * converter have always read and written it, but neither type declared the attribute.  MediaFile, the third
     * type using it, did.  Add it to the two that were missing it.
     *
     * @return patch
     */
    private TypeDefPatch updateDocumentStoreEntity()
    {
        return getEmbeddedMetadataPatch(OpenMetadataType.DOCUMENT_STORE.typeName);
    }


    /**
     * Add embeddedMetadata to MediaCollection - see updateDocumentStoreEntity().
     *
     * @return patch
     */
    private TypeDefPatch updateMediaCollectionEntity()
    {
        return getEmbeddedMetadataPatch(OpenMetadataType.MEDIA_COLLECTION.typeName);
    }


    /**
     * Build a patch that adds the embeddedMetadata attribute to the named type.
     *
     * @param typeName type to patch
     * @return patch
     */
    private TypeDefPatch getEmbeddedMetadataPatch(String typeName)
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.EMBEDDED_METADATA));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0423SecurityDefinitions()
    {
        this.archiveBuilder.addTypeDefPatch(updateSecurityListMembershipClassification());
    }


    /**
     * SecurityListMembership lists both the security groups and the security roles that the element belongs to.
     * SecurityListMembershipProperties has always carried both, and the diagram shows both, but the type only
     * declared securityGroups.
     *
     * @return patch
     */
    private TypeDefPatch updateSecurityListMembershipClassification()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.SECURITY_LIST_MEMBERSHIP_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SECURITY_ROLES));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0505SchemaAttributes()
    {
        this.archiveBuilder.addTypeDefPatch(updateTypeEmbeddedAttributeClassification());
    }


    /**
     * TypeEmbeddedAttributeProperties carries thirteen properties and the builder and converter have always
     * read and written all of them, but the type declared only ten.  category, namespacePath and
     * versionIdentifier were missing.  TypeEmbeddedAttribute has no super type, so it inherits nothing and has
     * to declare them itself.
     *
     * @return patch
     */
    private TypeDefPatch updateTypeEmbeddedAttributeClassification()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CATEGORY));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NAMESPACE_PATH));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.VERSION_IDENTIFIER));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void add0680CodeAnalysis()
    {
        this.archiveBuilder.addEntityDef(getContributorAnalysisAnnotationEntity());
        this.archiveBuilder.addEntityDef(getCodeAnalysisAnnotationEntity());
    }


    /**
     * ContributorAnalysisAnnotation captures the level of activity around a code repository - who is contributing, and how much.
     *
     * @return entity definition
     */
    private EntityDef getContributorAnalysisAnnotationEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.CONTRIBUTOR_ANALYSIS_ANNOTATION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.ANNOTATION.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.BUS_FACTOR));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TOTAL_CONTRIBUTOR_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ACTIVE_CONTRIBUTOR_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.COMMIT_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ACTIVE_COMMIT_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ISSUE_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ACTIVE_ISSUE_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONTRIBUTION_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ACTIVE_CONTRIBUTION_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.COPY_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ACTIVE_COPY_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STARGAZER_COUNT));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /**
     * CodeAnalysisAnnotation captures measures of the content of the code held in a code repository, including its size, shape and complexity.
     *
     * @return entity definition
     */
    private EntityDef getCodeAnalysisAnnotationEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.CODE_ANALYSIS_ANNOTATION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.ANNOTATION.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FILE_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LINE_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CODE_LINE_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.COMMENT_LINE_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PRIMARY_LANGUAGE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LANGUAGE_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PUBLIC_SYMBOL_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ENTRY_POINT_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DATA_READ_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DATA_CREATE_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DATA_UPDATE_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DATA_DELETE_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DATA_CHECKS_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DATA_STORE_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.EXTERNAL_CALL_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FUNCTION_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CYCLOMATIC_COMPLEXITY_TOTAL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CYCLOMATIC_COMPLEXITY_MAX));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MAX_NESTING_DEPTH));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TEST_FILE_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DOCUMENTED_SYMBOL_COUNT));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0025Locations()
    {
        this.archiveBuilder.addTypeDefPatch(updateFixedLocationClassification());
    }


    /**
     * FixedLocation declared the timezone attribute, but FixedLocationProperties calls it timeZone and so does
     * the published type documentation.  Rename it, leaving the old spelling in place as a renamed attribute so
     * that repositories holding existing instances know where the value moved to.
     *
     * @return patch
     */
    private TypeDefPatch updateFixedLocationClassification()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.FIXED_LOCATION_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setPropertyDefinitions(getRenamedTimeZoneAttributes());

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0112People()
    {
        this.archiveBuilder.addTypeDefPatch(updatePersonEntity());
    }


    /**
     * Person declared the timezone attribute, but PersonProperties calls it timeZone and so does the published
     * type documentation.  Rename it in the same way as FixedLocation.
     *
     * @return patch
     */
    private TypeDefPatch updatePersonEntity()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.PERSON.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setPropertyDefinitions(getRenamedTimeZoneAttributes());

        return typeDefPatch;
    }


    /**
     * Build the attribute pair that renames timezone to timeZone: the new attribute, plus the old spelling
     * marked as renamed so that a repository can follow the value across the change.
     *
     * @return list of attributes
     */
    private List<TypeDefAttribute> getRenamedTimeZoneAttributes()
    {
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TIME_ZONE));

        TypeDefAttribute renamedAttribute = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TIME_ZONE_DEPRECATED);
        renamedAttribute.setAttributeStatus(TypeDefAttributeStatus.RENAMED_ATTRIBUTE);
        renamedAttribute.setReplacedByAttribute(OpenMetadataProperty.TIME_ZONE.name);
        properties.add(renamedAttribute);

        return properties;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0610Annotations()
    {
        this.archiveBuilder.addTypeDefPatch(updateReportedAnnotationRelationship());
    }


    /**
     * ReportedAnnotation had no super type, even though ReportedAnnotationProperties extends
     * LabeledRelationshipProperties and so expects label and description to be present.  Its sibling
     * AnnotationExtension already inherits from LabeledRelationship; this brings ReportedAnnotation into line
     * so that the two properties the bean reads and writes actually exist in the type system.
     *
     * @return patch
     */
    private TypeDefPatch updateReportedAnnotationRelationship()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getRelationshipDef(OpenMetadataType.LABELED_RELATIONSHIP.typeName));

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0710DigitalServices()
    {
        this.archiveBuilder.addTypeDefPatch(updateDigitalProductDependencyRelationship());
    }


    /**
     * DigitalProductDependency is a subtype of LineageRelationship and so inherits iscQualifiedName.  The same
     * dependency between two digital products may be used by more than one information supply chain, and each
     * needs its own relationship.  multiLink is not inherited from the super type, so it is set explicitly here.
     *
     * @return patch
     */
    private TypeDefPatch updateDigitalProductDependencyRelationship()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setUpdateMultiLink(true);
        typeDefPatch.setMultiLink(true);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0735SolutionPortsAndWires()
    {
        this.archiveBuilder.addTypeDefPatch(updateSolutionLinkingWireRelationship());
    }


    /**
     * SolutionLinkingWire defines iscQualifiedNames, which lists the information supply chains that the wire
     * belongs to.  The 6.1 patch that introduced this intended the relationship to be multiLink, but set
     * multiLink without setting updateMultiLink, so the change was never applied.  That patch has already taken
     * the type to version 2 in any repository that loaded the 6.1 types, so the correction is made here as a
     * further patch rather than by amending the 6.1 patch in place.
     *
     * @return patch
     */
    private TypeDefPatch updateSolutionLinkingWireRelationship()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setUpdateMultiLink(true);
        typeDefPatch.setMultiLink(true);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0770LineageMapping()
    {
        this.archiveBuilder.addTypeDefPatch(updateDataMappingRelationship());
    }


    /**
     * DataMapping is a subtype of LineageRelationship and so inherits iscQualifiedName.  The same mapping between
     * two schema elements may be used by more than one information supply chain, and each needs its own
     * relationship.  multiLink is not inherited from the super type, so it is set explicitly here.
     *
     * @return patch
     */
    private TypeDefPatch updateDataMappingRelationship()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setUpdateMultiLink(true);
        typeDefPatch.setMultiLink(true);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void add0280SoftwareDevelopmentAssets()
    {
        this.archiveBuilder.addClassificationDef(getGeneratedTargetClassification());
        this.archiveBuilder.addClassificationDef(getReusableTechniqueClassification());
        this.archiveBuilder.addRelationshipDef(getReusableTechniqueUseRelationship());
    }


    /**
     * GeneratedTarget identifies an element that is the output of a build program or script.  It can be
     * reproduced at will and so does not need backing up.
     *
     * @return classification def
     */
    private ClassificationDef getGeneratedTargetClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.GENERATED_TARGET_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PURPOSE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.BUILD_ID));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.BUILD_TOOL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.BUILD_TOOL_VERSION));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /**
     * ReusableTechnique identifies a process, script or technique that can be reused in multiple contexts.
     *
     * @return classification def
     */
    private ClassificationDef getReusableTechniqueClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.REUSABLE_TECHNIQUE_CLASSIFICATION,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                  false);
    }


    /**
     * ReusableTechniqueUse identifies where a reusable technique has been used.  Both ends are Referenceable
     * because the technique is identified by a classification rather than a type of its own.
     *
     * @return relationship def
     */
    private RelationshipDef getReusableTechniqueUseRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.REUSABLE_TECHNIQUE_USE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "techniqueInUseBy";
        final String                     end1AttributeDescription     = "The elements that are making use of the reusable technique.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "reusedTechnique";
        final String                     end2AttributeDescription     = "The reusable technique that is being used.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void add0281SoftwareModules()
    {
        this.archiveBuilder.addEntityDef(getSoftwareComponentEntity());
        this.archiveBuilder.addEntityDef(getSoftwareModuleEntity());
    }


    /**
     * SoftwareComponent is a collection of the artifacts that together build a runnable software component.
     *
     * @return entity def
     */
    private EntityDef getSoftwareComponentEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.SOFTWARE_COMPONENT,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.COLLECTION.typeName));
    }


    /**
     * SoftwareModule is a collection of software components, which is why it inherits from SoftwareComponent.
     *
     * @return entity def
     */
    private EntityDef getSoftwareModuleEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.SOFTWARE_MODULE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_COMPONENT.typeName));
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void add0282ReleasedSoftwareComponents()
    {
        this.archiveBuilder.addEntityDef(getRunnableSoftwareComponentEntity());
        this.archiveBuilder.addRelationshipDef(getDependentSoftwareComponentRelationship());
        this.archiveBuilder.addRelationshipDef(getSoftwareSourceRelationship());
    }


    /**
     * RunnableSoftwareComponent is a released software component that is executable.  The version of the
     * release is recorded in versionIdentifier, which it inherits from Referenceable.
     *
     * @return entity def
     */
    private EntityDef getRunnableSoftwareComponentEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.RUNNABLE_SOFTWARE_COMPONENT,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.RUNTIME_ENVIRONMENT_TYPE));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /**
     * DependentSoftwareComponent describes the dependency between runnable software components.
     *
     * @return relationship def
     */
    private RelationshipDef getDependentSoftwareComponentRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.DEPENDENT_SOFTWARE_COMPONENT_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "dependentOnForExecution";
        final String                     end1AttributeDescription     = "The runnable software components that need this component in order to execute.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.RUNNABLE_SOFTWARE_COMPONENT.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "dependsOnForExecution";
        final String                     end2AttributeDescription     = "The runnable software components that this component needs in order to execute.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.RUNNABLE_SOFTWARE_COMPONENT.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    /**
     * SoftwareSource links a software component to the software asset that derives from it.
     *
     * @return relationship def
     */
    private RelationshipDef getSoftwareSourceRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.SOFTWARE_SOURCE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "softwareComponentUsedBy";
        final String                     end1AttributeDescription     = "The software assets that derive from this software component.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "derivedFromSoftwareComponent";
        final String                     end2AttributeDescription     = "The software component that this asset is built from.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_COMPONENT.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void add0462GovernanceActionProcesses()
    {
        this.archiveBuilder.addEntityDef(getAnalyticalActionProcessEntity());
        this.archiveBuilder.addEntityDef(getCataloguingActionProcessEntity());
        this.archiveBuilder.addEntityDef(getExploringActionProcessEntity());
        this.archiveBuilder.addEntityDef(getSurveyingActionProcessEntity());
        this.archiveBuilder.addEntityDef(getProvisioningActionProcessEntity());
        this.archiveBuilder.addEntityDef(getDeletingActionProcessEntity());
        this.archiveBuilder.addEntityDef(getSubscribingActionProcessEntity());
    }


    /**
     * AnalyticalActionProcess drives an analytical engine to produce a report or other output.
     *
     * @return entity def
     */
    private EntityDef getAnalyticalActionProcessEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.ANALYTICAL_ACTION_PROCESS,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName));
    }


    /**
     * CataloguingActionProcess extracts metadata from an external source and catalogs it.
     *
     * @return entity def
     */
    private EntityDef getCataloguingActionProcessEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.CATALOGUING_ACTION_PROCESS,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName));
    }


    /**
     * ExploringActionProcess explores a digital resource to understand an overview of its content and context.
     *
     * @return entity def
     */
    private EntityDef getExploringActionProcessEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.EXPLORING_ACTION_PROCESS,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName));
    }


    /**
     * SurveyingActionProcess surveys a digital resource to deeply understand its content and context.
     *
     * @return entity def
     */
    private EntityDef getSurveyingActionProcessEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.SURVEYING_ACTION_PROCESS,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName));
    }


    /**
     * ProvisioningActionProcess provisions a digital resource to a target environment.
     *
     * @return entity def
     */
    private EntityDef getProvisioningActionProcessEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.PROVISIONING_ACTION_PROCESS,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName));
    }


    /**
     * DeletingActionProcess deletes requested metadata elements.
     *
     * @return entity def
     */
    private EntityDef getDeletingActionProcessEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.DELETING_ACTION_PROCESS,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName));
    }


    /**
     * SubscribingActionProcess creates a subscription to a digital resource.
     *
     * @return entity def
     */
    private EntityDef getSubscribingActionProcessEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.SUBSCRIBING_ACTION_PROCESS,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName));
    }

}

