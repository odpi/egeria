/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.simplecatalogs.catalogcontent;


import org.odpi.openmetadata.frameworks.openmetadata.enums.ContentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeploymentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.samples.archiveutilities.SimpleCatalogArchiveHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SimpleDataCatalogArchiveBuilder provides data source metadata.
 */
public class SimpleDataCatalogArchiveBuilder
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "2216ab62-176a-46c0-b889-9aa081754b54";
    private static final String                  archiveName        = "SimpleDataCatalog";
    private static final String                  archiveLicense     = "Apache-2.0";
    private static final String                  archiveDescription = "Sample metadata showing data sources and their schemas.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.REPOSITORY_BACKUP;
    private static final String                  originatorName     = "Egeria";
    private static final Date                    creationDate       = new Date(1632046251579L);

    /*
     * Names for data definitions
     */

    private static final String categoryName = "Customer Domain";

    private static final String machineQualifiedName = OpenMetadataType.BARE_METAL_COMPUTER.typeName + "::" + "V37B8752.FH567.sys";
    private static final String machineResourceName  = "V37B8752.FH567.sys";
    private static final String machineDisplayName   = "V37B8752.FH567.sys";

    private static final String branchQualifiedName = OpenMetadataType.DATABASE.typeName + "::" + "V37B8752.FH567.sys/BRANCH";
    private static final String branchResourceName  = "V37B8752.FH567.sys/BRANCH";
    private static final String branchPathName      = "V37B8752.FH567.sys";
    private static final String branchDisplayName   = "BRANCH Database";
    private static final String branchDescription   = "Main branch system database.";

    static final String retailSchemaQualifiedName = OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName + "::" + "V37B8752.FH567.sys/BRANCH.RETAILSCHEMA";
    private static final String retailSchemaResourceName = "V37B8752.FH567.sys/BRANCH.RETAILSCHEMA";
    private static final String retailSchemaPathName = "V37B8752.FH567.sys/BRANCH";
    private static final String retailSchemaDisplayName   = "RETAILSCHEMA";
    private static final String retailSchemaDescription   = "Retail banking schema.";

    static final String customerTableQualifiedName = OpenMetadataType.RELATIONAL_TABLE.typeName + "::" + "V37B8752.FH567.sys/BRANCH.RETAILSCHEMA.CUSTOMER";
    private static final String customerTablePathName = "V37B8752.FH567.sys/BRANCH.RETAILSCHEMA";
    private static final String customerTableDisplayName   = "CUSTOMER";
    private static final String customerTableDescription   = "Branch customer table for retail banking.";

    static final String customerIdQualifiedName = OpenMetadataType.RELATIONAL_COLUMN.typeName + "::" + "V37B8752.FH567.sys/BRANCH.RETAILSCHEMA.CUSTOMER.CUSTID";
    private static final String customerIdPathName = "V37B8752.FH567.sys/BRANCH.RETAILSCHEMA.CUSTOMER";
    private static final String customerIdDisplayName   = "CUSTID";
    private static final String customerIdDescription   = "The unique identifier assigned internally for a customer.";
    private static final String customerIdDataType      = "CHAR";
    private static final int    customerIdLength        = 12;

    static final String customerNameQualifiedName = OpenMetadataType.RELATIONAL_COLUMN.typeName + "::" + "V37B8752.FH567.sys/BRANCH.RETAILSCHEMA.CUSTOMER.CUSTNAME";
    private static final String customerNamePathName = "V37B8752.FH567.sys/BRANCH.RETAILSCHEMA.CUSTOMER";
    private static final String customerNameDisplayName   = "CUSTNAME";
    private static final String customerNameDescription   = "The name for a customer - as supplied by the customer.";
    private static final String customerNameDataType      = "CHAR";
    private static final int    customerNameLength        = 40;

    static final String customerStatusQualifiedName = OpenMetadataType.RELATIONAL_COLUMN.typeName + "::" + "V37B8752.FH567.sys/BRANCH.RETAILSCHEMA.CUSTOMER.CUSTSTATUS";
    private static final String customerStatusPathName = "V37B8752.FH567.sys/BRANCH.RETAILSCHEMA.CUSTOMER";
    private static final String customerStatusDisplayName   = "CUSTSTATUS";
    private static final String customerStatusDescription   = "The calculated status for a customer indicating their value to the organization.";
    private static final String customerStatusDataType      = "CHAR";
    private static final int    customerStatusLength        = 10;

    static final String customerCardIdQualifiedName = OpenMetadataType.RELATIONAL_COLUMN.typeName + "::" + "V37B8752.FH567.sys/BRANCH.RETAILSCHEMA.CUSTOMER.CUSTCARD";
    private static final String customerCardIdPathName = "V37B8752.FH567.sys/BRANCH.RETAILSCHEMA.CUSTOMER";
    private static final String customerCardIdDisplayName   = "CUSTCARD";
    private static final String customerCardIdDescription   = "The store card number for the customer.  Null if not issued.";
    private static final String customerCardIdDataType      = "CHAR";
    private static final int    customerCardIdLength        = 10;

    /*
     * Additional AssetTypes for data stores
     */
    private static final String databaseAssetTypeName            = OpenMetadataType.DATABASE.typeName;
    private static final String databaseSchemaAssetTypeName      = OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName;
    private static final String relationalTopLevelSchemaTypeName = OpenMetadataType.RELATIONAL_DB_SCHEMA_TYPE.typeName;
    private static final String relationalTableTypeName          = OpenMetadataType.RELATIONAL_TABLE.typeName;
    private static final String relationalTableSchemaTypeName    = OpenMetadataType.RELATIONAL_TABLE_TYPE.typeName;
    private static final String relationalColumnTypeName         = OpenMetadataType.RELATIONAL_COLUMN.typeName;


    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "1.0";

    private final OMRSArchiveBuilder         archiveBuilder;
    private final SimpleCatalogArchiveHelper archiveHelper;

    /**
     * Constructor pushes all archive header values to the superclass
     *
     * @param archiveName name of the open metadata archive metadata collection.
     * @param archiveRootName non-spaced root name of the open metadata GUID map.
     */
    public SimpleDataCatalogArchiveBuilder(String archiveName,
                                           String archiveRootName)
    {
        List<OpenMetadataArchive> dependentOpenMetadataArchives = new ArrayList<>();

        /*
         * This value allows the archive to be based on the existing open metadata types
         */
        dependentOpenMetadataArchives.add(new OpenMetadataTypesArchive().getOpenMetadataArchive());

        this.archiveBuilder = new OMRSArchiveBuilder(archiveGUID,
                                                     archiveName,
                                                     archiveDescription,
                                                     archiveType,
                                                     originatorName,
                                                     archiveLicense,
                                                     creationDate,
                                                     dependentOpenMetadataArchives);

        this.archiveHelper = new SimpleCatalogArchiveHelper(archiveBuilder,
                                                            archiveGUID,
                                                            archiveName,
                                                            archiveDescription,
                                                            archiveRootName,
                                                            originatorName,
                                                            creationDate,
                                                            versionNumber,
                                                            versionName,
                                                            InstanceProvenanceType.LOCAL_COHORT,
                                                            null);
    }


    /**
     * Construct the builder using a shared archive builder and helper.  Used to create a
     * combination archive.
     *
     * @param archiveBuilder archive builder
     * @param archiveHelper archive helper
     */
    public SimpleDataCatalogArchiveBuilder(OMRSArchiveBuilder archiveBuilder, SimpleCatalogArchiveHelper archiveHelper)
    {
        this.archiveBuilder = archiveBuilder;
        this.archiveHelper = archiveHelper;
    }


    /**
     * Fills the archive builder with all the elements for this catalog.
     */
    public void fillBuilder()
    {
        final String methodName = "fillBuilder";

        List<Classification> classifications = new ArrayList<>();

        classifications.add(archiveHelper.getAnchorClassification(null, OpenMetadataType.BARE_METAL_COMPUTER.typeName, OpenMetadataType.ASSET.typeName, null, methodName));

        String machineGUID = archiveHelper.addInfrastructureAsset(OpenMetadataType.BARE_METAL_COMPUTER.typeName,
                                                                  machineQualifiedName,
                                                                  machineDisplayName,
                                                                  machineResourceName,
                                                                  null,
                                                                  categoryName,
                                                                  DeployedImplementationType.BARE_METAL_COMPUTER.getDeployedImplementationType(),
                                                                  versionName,
                                                                  null,
                                                                  DeploymentStatus.ACTIVE,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  classifications);
        classifications = new ArrayList<>();

        classifications.add(archiveHelper.getAnchorClassification(machineGUID, OpenMetadataType.BARE_METAL_COMPUTER.typeName, OpenMetadataType.ASSET.typeName, null, methodName));


        String databaseGUID = archiveHelper.addDataAsset(databaseAssetTypeName,
                                                         branchQualifiedName,
                                                         branchDisplayName,
                                                         branchResourceName,
                                                         branchPathName,
                                                         categoryName,
                                                         DeployedImplementationType.JDBC_RELATIONAL_DATABASE.getDeployedImplementationType(),
                                                         versionName,
                                                         branchDescription,
                                                         ContentStatus.ACTIVE,
                                                         null,
                                                         null,
                                                         classifications);

        String databaseSchemaGUID = archiveHelper.addDataAsset(databaseSchemaAssetTypeName,
                                                               retailSchemaQualifiedName,
                                                               retailSchemaDisplayName,
                                                               retailSchemaResourceName,
                                                               retailSchemaPathName,
                                                               categoryName,
                                                               DeployedImplementationType.JDBC_RELATIONAL_DATABASE_SCHEMA.getDeployedImplementationType(),
                                                               versionName,
                                                               retailSchemaDescription,
                                                               ContentStatus.ACTIVE,
                                                               null,
                                                               null,
                                                               classifications);

        archiveHelper.addDataSetContent(databaseSchemaGUID, databaseGUID, null, null, null, null);

        String topLevelSchemaTypeGUID = archiveHelper.addTopLevelSchemaType(databaseSchemaGUID,
                                                                            databaseSchemaAssetTypeName,
                                                                            relationalTopLevelSchemaTypeName,
                                                                            retailSchemaQualifiedName + "_schema_detail",
                                                                            retailSchemaDisplayName + " Schema Detail",
                                                                            null,
                                                                            null);

        String relationalTableGUID = archiveHelper.addSchemaAttribute(databaseSchemaGUID,
                                                                      databaseSchemaAssetTypeName,
                                                                      relationalTableTypeName,
                                                                      relationalTableSchemaTypeName,
                                                                      customerTableQualifiedName,
                                                                      customerTableDisplayName,
                                                                      customerTablePathName,
                                                                      customerTableDescription,
                                                                      null,
                                                                      0,
                                                                      null,
                                                                      null);

        archiveHelper.addAttributeForSchemaType(topLevelSchemaTypeGUID, 0, 0, 0, relationalTableGUID);

        String relationalColumnGUID = archiveHelper.addSchemaAttribute(databaseSchemaGUID,
                                                                       databaseSchemaAssetTypeName,
                                                                       relationalColumnTypeName,
                                                                       null,
                                                                       customerIdQualifiedName,
                                                                       customerIdDisplayName,
                                                                       customerIdPathName,
                                                                       customerIdDescription,
                                                                       customerIdDataType,
                                                                       customerIdLength,
                                                                       null,
                                                                       null);

        archiveHelper.addNestedSchemaAttribute(relationalTableGUID, 1, 0, 0, relationalColumnGUID);

        relationalColumnGUID = archiveHelper.addSchemaAttribute(databaseSchemaGUID,
                                                                databaseSchemaAssetTypeName,
                                                                relationalColumnTypeName,
                                                                null,
                                                                customerNameQualifiedName,
                                                                customerNameDisplayName,
                                                                customerNamePathName,
                                                                customerNameDescription,
                                                                customerNameDataType,
                                                                customerNameLength,
                                                                null,
                                                                null);

        archiveHelper.addNestedSchemaAttribute(relationalTableGUID, 2, 1, 1, relationalColumnGUID);

        relationalColumnGUID = archiveHelper.addSchemaAttribute(databaseSchemaGUID,
                                                                databaseSchemaAssetTypeName,
                                                                relationalColumnTypeName,
                                                                null,
                                                                customerStatusQualifiedName,
                                                                customerStatusDisplayName,
                                                                customerStatusPathName,
                                                                customerStatusDescription,
                                                                customerStatusDataType,
                                                                customerStatusLength,
                                                                null,
                                                                null);

        archiveHelper.addNestedSchemaAttribute(relationalTableGUID, 3, 1, 1, relationalColumnGUID);

        relationalColumnGUID = archiveHelper.addSchemaAttribute(databaseSchemaGUID,
                                                                databaseSchemaAssetTypeName,
                                                                relationalColumnTypeName,
                                                                null,
                                                                customerCardIdQualifiedName,
                                                                customerCardIdDisplayName,
                                                                customerCardIdPathName,
                                                                customerCardIdDescription,
                                                                customerCardIdDataType,
                                                                customerCardIdLength,
                                                                null,
                                                                null);

        archiveHelper.addNestedSchemaAttribute(relationalTableGUID, 4, 1, 1, relationalColumnGUID);

        archiveHelper.saveGUIDs();
    }


    /**
     * Returns the open metadata type archive containing all the elements for this catalog.
     *
     * @return populated open metadata archive object
     */
    public OpenMetadataArchive getOpenMetadataArchive()
    {
        fillBuilder();
        return archiveBuilder.getOpenMetadataArchive();
    }
}
