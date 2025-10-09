/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.simplecatalogs.catalogcontent;


import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
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
    private static final String branchQualifiedName = "V37B8752.FH567.sys/BRANCH";
    private static final String branchDisplayName   = "BRANCH Database";
    private static final String branchDescription   = "Main branch system database.";

    private static final String retailSchemaQualifiedName = "V37B8752.FH567.sys/BRANCH.RETAILSCHEMA";
    private static final String retailSchemaDisplayName   = "RETAILSCHEMA";
    private static final String retailSchemaDescription   = "Retail banking schema.";

    private static final String customerTableQualifiedName = "V37B8752.FH567.sys/BRANCH.RETAILSCHEMA.CUSTOMER";
    private static final String customerTableDisplayName   = "CUSTOMER";
    private static final String customerTableDescription   = "Branch customer table for retail banking.";

    private static final String customerIdQualifiedName = "V37B8752.FH567.sys/BRANCH.RETAILSCHEMA.CUSTOMER.CUSTID";
    private static final String customerIdDisplayName   = "CUSTID";
    private static final String customerIdDescription   = "The unique identifier assigned internally for a customer.";
    private static final String customerIdDataType      = "CHAR";
    private static final int    customerIdLength        = 12;

    private static final String customerNameQualifiedName = "V37B8752.FH567.sys/BRANCH.RETAILSCHEMA.CUSTOMER.CUSTNAME";
    private static final String customerNameDisplayName   = "CUSTNAME";
    private static final String customerNameDescription   = "The name for a customer - as supplied by the customer.";
    private static final String customerNameDataType      = "CHAR";
    private static final int    customerNameLength        = 40;

    private static final String customerStatusQualifiedName = "V37B8752.FH567.sys/BRANCH.RETAILSCHEMA.CUSTOMER.CUSTSTATUS";
    private static final String customerStatusDisplayName   = "CUSTSTATUS";
    private static final String customerStatusDescription   = "The calculated status for a customer indicating their value to the organization.";
    private static final String customerStatusDataType      = "CHAR";
    private static final int    customerStatusLength        = 10;

    private static final String customerCardIdQualifiedName = "V37B8752.FH567.sys/BRANCH.RETAILSCHEMA.CUSTOMER.CUSTCARD";
    private static final String customerCardIdDisplayName   = "CUSTCARD";
    private static final String customerCardIdDescription   = "The store card number for the customer.  Null if not issued.";
    private static final String customerCardIdDataType      = "CHAR";
    private static final int    customerCardIdLength        = 10;

    /*
     * Additional AssetTypes for data stores
     */
    private static final String databaseAssetTypeName            = "Database";
    private static final String databaseSchemaAssetTypeName      = "DeployedDatabaseSchema";
    private static final String relationalTopLevelSchemaTypeName = "RelationalDBSchemaType";
    private static final String relationalTableTypeName          = "RelationalTable";
    private static final String relationalTableSchemaTypeName    = "RelationalTableType";
    private static final String relationalColumnTypeName         = "RelationalColumn";


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
        String databaseGUID = archiveHelper.addAsset(databaseAssetTypeName,
                                                     branchQualifiedName,
                                                     branchDisplayName,
                                                     branchDescription,
                                                     null,
                                                     null);

        String databaseSchemaGUID = archiveHelper.addAsset(databaseSchemaAssetTypeName,
                                                           retailSchemaQualifiedName,
                                                           retailSchemaDisplayName,
                                                           retailSchemaDescription,
                                                           null,
                                                           null);

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
