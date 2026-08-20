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
        add0680CodeAnalysis();
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

}

