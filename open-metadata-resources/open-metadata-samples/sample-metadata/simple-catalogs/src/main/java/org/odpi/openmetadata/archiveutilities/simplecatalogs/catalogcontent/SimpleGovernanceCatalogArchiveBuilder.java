/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.simplecatalogs.catalogcontent;


import org.odpi.openmetadata.archiveutilities.catalogbuilder.CatalogTypesArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;

import java.util.Date;
import java.util.List;

/**
 * SimpleEventCatalogArchiveBuilder provides event and topic metadata.
 */
public class SimpleGovernanceCatalogArchiveBuilder extends CatalogTypesArchiveBuilder
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "e915f2fa-aaac-4396-8bde-bcd65e642b1d";
    private static final String                  archiveRootName    = "SimpleGovernanceCatalog";
    private static final String                  archiveName        = "Simple Governance Catalog";
    private static final String                  archiveLicense     = "Apache 2.0";
    private static final String                  archiveDescription = "Sample metadata showing governance definitions and linking relationships.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  originatorName     = "Egeria";
    private static final Date                    creationDate       = new Date(1632046251579L);

    /*
     * Names for governance definitions
     */
    private static final String glossaryQualifiedName = "MainGlossary";
    private static final String glossaryDisplayName   = "Main Glossary";
    private static final String glossaryDescription   = "Canonical glossary for the organization.";
    private static final String glossaryLanguage      = "English";
    private static final String glossaryUsage         = "For simple demos";
    private static final String glossaryScope         = "Minimal for simple demos";

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
    private static final String versionName   = "1.0";


    /**
     * Constructor pushes all archive header values to the superclass
     *
     * @param dependentArchives previously created archives that are needed for reference.
     */
    public SimpleGovernanceCatalogArchiveBuilder(List<OpenMetadataArchive> dependentArchives)
    {
        super(archiveGUID,
              archiveName,
              archiveDescription,
              archiveType,
              archiveRootName,
              originatorName,
              archiveLicense,
              creationDate,
              versionNumber,
              versionName,
              dependentArchives);
    }


    /**
     * Returns the open metadata type archive containing all of the elements extracted from the connector
     * providers of the featured open connectors.
     *
     * @return populated open metadata archive object
     */
    public OpenMetadataArchive getOpenMetadataArchive()
    {
        String glossaryGUID = super.addGlossary(glossaryQualifiedName,
                                                glossaryDisplayName,
                                                glossaryDescription,
                                                glossaryLanguage,
                                                glossaryUsage,
                                                null,
                                                glossaryScope);

        String glossaryTermGUID = super.addTerm(glossaryGUID,
                                                null,
                                                uniqueCustomerIdentifierQualifiedName,
                                                uniqueCustomerIdentifierDisplayName,
                                                uniqueCustomerIdentifierDescription);

        String elementGUID = idToGUIDMap.getGUID(eventCustomerIdQualifiedName);

        super.linkTermToReferenceable(glossaryTermGUID, elementGUID);

        elementGUID = idToGUIDMap.getGUID(apiCustomerNoRequestQualifiedName);

        super.linkTermToReferenceable(glossaryTermGUID, elementGUID);

        elementGUID = idToGUIDMap.getGUID(apiCustomerNoResponseQualifiedName);

        super.linkTermToReferenceable(glossaryTermGUID, elementGUID);

        elementGUID = idToGUIDMap.getGUID(dbCustIdQualifiedName);

        super.linkTermToReferenceable(glossaryTermGUID, elementGUID);

        return super.getOpenMetadataArchive();
    }
}
