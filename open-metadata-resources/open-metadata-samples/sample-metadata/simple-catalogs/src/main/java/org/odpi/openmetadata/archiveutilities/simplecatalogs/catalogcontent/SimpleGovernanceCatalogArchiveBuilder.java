/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.simplecatalogs.catalogcontent;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.samples.archiveutilities.SimpleCatalogArchiveHelper;

import java.util.Date;
import java.util.List;

/**
 * SimpleEventCatalogArchiveBuilder provides governance metadata.
 */
public class SimpleGovernanceCatalogArchiveBuilder
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "e915f2fa-aaac-4396-8bde-bcd65e642b1d";
    private static final String                  archiveName        = "SimpleGovernanceCatalog";
    private static final String                  archiveLicense     = "Apache-2.0";
    private static final String                  archiveDescription = "Sample metadata showing governance definitions and linking relationships.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.REPOSITORY_BACKUP;
    private static final String                  originatorName     = "Egeria";
    private static final Date                    creationDate       = new Date(1632046251579L);

    /*
     * Names for governance definitions
     */
    private static final String glossaryQualifiedName = "SimpleCatalogGlossary";
    private static final String glossaryDisplayName   = "Simple Catalog Glossary";
    private static final String glossaryDescription   = "Canonical glossary for the simple data catalog.";
    private static final String glossaryLanguage      = "English";
    private static final String glossaryUsage         = "For simple demos";
    private static final String glossaryScope         = "Minimal for simple demos";

    private static final String iscQualifiedName = OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName + "::" + "SimpleCatalogInformationSupplyChain";
    private static final String iscDisplayName   = "Simple Catalog Information Supply Chain";
    private static final String iscDescription   = "Supply of customer data and notifications of customer details change.";


    private static final String uniqueCustomerIdentifierQualifiedName  = "MainGlossary/Unique_Customer_Identifier";
    private static final String uniqueCustomerIdentifierDisplayName    = "UniqueCustomerIdentifier";
    private static final String uniqueCustomerIdentifierDescription    = "Unique identifier for a customer generated on first contact.";

    private static final String eventCustomerIdQualifiedName       = "global-event-bus/CustomerDomain/EventAttributes/CustomerIdentifiers/customerId";
    private static final String apiCustomerNoRequestQualifiedName  = "global-api-gateway/CustomerDomain/APIs/Customer/getCustomer/request/customerNo";
    private static final String apiCustomerNoResponseQualifiedName = "global-api-gateway/CustomerDomain/APIs/Customer/getCustomer/response/customerNo";
    private static final String dbCustIdQualifiedName = "V37B8752.FH567.sys/BRANCH.RETAILSCHEMA.CUSTOMER.CUSTID";

    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "6.1-SNAPSHOT";

    private final OMRSArchiveBuilder         archiveBuilder;
    private final SimpleCatalogArchiveHelper archiveHelper;

    /**
     * Constructor pushes all archive header values to the superclass
     *
     * @param archiveName name of the open metadata archive metadata collection.
     * @param archiveRootName non-spaced root name of the open metadata GUID map.
     * @param dependentArchives previously created archives that are needed for reference.
     */
    public SimpleGovernanceCatalogArchiveBuilder(String                    archiveName,
                                                 String                    archiveRootName,
                                                 List<OpenMetadataArchive> dependentArchives)
    {
        this.archiveBuilder = new OMRSArchiveBuilder(archiveGUID,
                                                     archiveName,
                                                     archiveDescription,
                                                     archiveType,
                                                     originatorName,
                                                     archiveLicense,
                                                     creationDate,
                                                     dependentArchives);

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
    public SimpleGovernanceCatalogArchiveBuilder(OMRSArchiveBuilder archiveBuilder, SimpleCatalogArchiveHelper archiveHelper)
    {
        this.archiveBuilder = archiveBuilder;
        this.archiveHelper = archiveHelper;
    }


    /**
     * Fills the archive builder with all the elements for this catalog.
     */
    public void fillBuilder()
    {
        String glossaryGUID = archiveHelper.addGlossary(OpenMetadataType.GLOSSARY.typeName + "::" + glossaryQualifiedName,
                                                        glossaryDisplayName,
                                                        glossaryDescription,
                                                        glossaryLanguage,
                                                        glossaryUsage,
                                                        null,
                                                        glossaryScope);

        String glossaryTermGUID = archiveHelper.addTerm(glossaryGUID,
                                                        null,
                                                        OpenMetadataType.GLOSSARY_TERM.typeName + "::" + uniqueCustomerIdentifierQualifiedName,
                                                        uniqueCustomerIdentifierDisplayName,
                                                        uniqueCustomerIdentifierDescription);

        String elementGUID = archiveHelper.getGUID(eventCustomerIdQualifiedName);

        archiveHelper.linkTermToReferenceable(glossaryTermGUID, elementGUID);

        elementGUID = archiveHelper.getGUID(apiCustomerNoRequestQualifiedName);

        archiveHelper.linkTermToReferenceable(glossaryTermGUID, elementGUID);

        elementGUID = archiveHelper.getGUID(apiCustomerNoResponseQualifiedName);

        archiveHelper.linkTermToReferenceable(glossaryTermGUID, elementGUID);

        elementGUID = archiveHelper.getGUID(dbCustIdQualifiedName);

        archiveHelper.linkTermToReferenceable(glossaryTermGUID, elementGUID);

        archiveHelper.addInformationSupplyChain(null,
                                                false,
                                                null,
                                                OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName,
                                                iscQualifiedName,
                                                iscDisplayName,
                                                iscDescription,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null);

        String sourceGUID = archiveHelper.getGUID(SimpleAPICatalogArchiveBuilder.customerQualifiedName);
        String destinationGUID = archiveHelper.getGUID(SimpleDataCatalogArchiveBuilder.retailSchemaQualifiedName);
        archiveHelper.addLineageRelationship(sourceGUID,
                                             destinationGUID,
                                             OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                             "calls",
                                             iscQualifiedName);

        sourceGUID = archiveHelper.getGUID(SimpleAPICatalogArchiveBuilder.getCustomerQualifiedName);
        destinationGUID = archiveHelper.getGUID(SimpleDataCatalogArchiveBuilder.customerTableQualifiedName);
        archiveHelper.addLineageRelationship(sourceGUID,
                                             destinationGUID,
                                             OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                             "retrieve customer",
                                             iscQualifiedName);

        sourceGUID = archiveHelper.getGUID(SimpleDataCatalogArchiveBuilder.customerTableQualifiedName);
        destinationGUID = archiveHelper.getGUID(SimpleEventCatalogArchiveBuilder.customerChangeQualifiedName);
        archiveHelper.addLineageRelationship(sourceGUID,
                                             destinationGUID,
                                             OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                             "notify customer change",
                                             iscQualifiedName);

        sourceGUID = archiveHelper.getGUID(SimpleAPICatalogArchiveBuilder.customerNoRequestQualifiedName);
        destinationGUID = archiveHelper.getGUID(SimpleDataCatalogArchiveBuilder.customerIdQualifiedName);
        archiveHelper.addLineageRelationship(sourceGUID,
                                             destinationGUID,
                                             OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName,
                                             "maps",
                                             null);

        sourceGUID = archiveHelper.getGUID(SimpleDataCatalogArchiveBuilder.customerIdQualifiedName);
        destinationGUID = archiveHelper.getGUID(SimpleAPICatalogArchiveBuilder.customerNoResponseQualifiedName);
        archiveHelper.addLineageRelationship(sourceGUID,
                                             destinationGUID,
                                             OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName,
                                             "maps",
                                             null);

        sourceGUID = archiveHelper.getGUID(SimpleDataCatalogArchiveBuilder.customerIdQualifiedName);
        destinationGUID = archiveHelper.getGUID(SimpleEventCatalogArchiveBuilder.customerIdQualifiedName);
        archiveHelper.addLineageRelationship(sourceGUID,
                                             destinationGUID,
                                             OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName,
                                             "maps",
                                             null);

        sourceGUID = archiveHelper.getGUID(SimpleAPICatalogArchiveBuilder.customerNameQualifiedName);
        destinationGUID = archiveHelper.getGUID(SimpleDataCatalogArchiveBuilder.customerNameQualifiedName);
        archiveHelper.addLineageRelationship(sourceGUID,
                                             destinationGUID,
                                             OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName,
                                             "maps",
                                             null);


        sourceGUID = archiveHelper.getGUID(SimpleDataCatalogArchiveBuilder.customerNameQualifiedName);
        destinationGUID = archiveHelper.getGUID(SimpleEventCatalogArchiveBuilder.customerNameQualifiedName);
        archiveHelper.addLineageRelationship(sourceGUID,
                                             destinationGUID,
                                             OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName,
                                             "maps",
                                             null);

        sourceGUID = archiveHelper.getGUID(SimpleAPICatalogArchiveBuilder.customerCardIdQualifiedName);
        destinationGUID = archiveHelper.getGUID(SimpleDataCatalogArchiveBuilder.customerCardIdQualifiedName);
        archiveHelper.addLineageRelationship(sourceGUID,
                                             destinationGUID,
                                             OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName,
                                             "maps",
                                             null);


        sourceGUID = archiveHelper.getGUID(SimpleDataCatalogArchiveBuilder.customerStatusQualifiedName);
        destinationGUID = archiveHelper.getGUID(SimpleEventCatalogArchiveBuilder.customerStatusQualifiedName);
        archiveHelper.addLineageRelationship(sourceGUID,
                                             destinationGUID,
                                             OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName,
                                             "maps",
                                             null);

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
