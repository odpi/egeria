/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.simplecatalogs.catalogcontent;


import org.odpi.openmetadata.archiveutilities.catalogbuilder.CatalogTypesArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;

import java.util.Date;

/**
 * SimpleAPICatalogArchiveBuilder provides API metadata.
 */
public class SimpleAPICatalogArchiveBuilder extends CatalogTypesArchiveBuilder
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "9e594f24-2494-4000-ac20-59f374eaa0e6";
    private static final String                  archiveRootName    = "SimpleAPICatalog";
    private static final String                  archiveName        = "Simple API Catalog";
    private static final String                  archiveLicense     = "Apache 2.0";
    private static final String                  archiveDescription = "Sample metadata showing API assets and their payloads.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  originatorName     = "Egeria";
    private static final Date                    creationDate       = new Date(1632046251579L);

    /*
     * Names for API definitions
     */
    private static final String customerQualifiedName = "global-api-gateway/CustomerDomain/APIs/Customer";
    private static final String customerDisplayName   = "Customer API";
    private static final String customerDescription   = "API for interacting with customer master.";

    private static final String getCustomerQualifiedName  = "global-api-gateway/CustomerDomain/APIs/Customer/getCustomer";
    private static final String getCustomerDisplayName    = "Get Customer Operation";
    private static final String getCustomerDescription    = "API operation to retrieved details about the customer.";
    private static final String getCustomerPath           = "global-api-gateway/CustomerDomain/APIs/Customer/getCustomer";
    private static final String getCustomerCommand        = "GET";

    private static final String customerNoRequestQualifiedName  = "global-api-gateway/CustomerDomain/APIs/Customer/getCustomer/request/customerNo";
    private static final String customerNoResponseQualifiedName = "global-api-gateway/CustomerDomain/APIs/Customer/getCustomer/response/customerNo";
    private static final String customerNoDisplayName           = "customerNo";
    private static final String customerNoDescription           = "The unique identifier assigned internally for a customer.";
    private static final String customerNoDataType              = "string";
    private static final int    customerNoLength                = 12;

    private static final String customerNameQualifiedName = "global-api-gateway/CustomerDomain/APIs/Customer/getCustomer/response/customerName";
    private static final String customerNameDisplayName   = "customerName";
    private static final String customerNameDescription   = "The name for a customer - as supplied by the customer.";
    private static final String customerNameDataType      = "string";
    private static final int    customerNameLength        = 40;

    private static final String customerCardIdQualifiedName = "global-api-gateway/CustomerDomain/APIs/Customer/getCustomer/responses/customerCardId";
    private static final String customerCardIdDisplayName   = "customerCardId";
    private static final String customerCardIdDescription   = "The store card number for the customer.  Null if not issued.";
    private static final String customerCardIdDataType      = "string";
    private static final int    customerCardIdLength        = 10;

    /*
     * Additional AssetTypes for basic file connector
     */
    private static final String apiAssetTypeName       = "DeployedAPI";
    private static final String apiSchemaTypeTypeName  = "APISchemaType";
    private static final String apiSchemaAttributeTypeName  = "APIParameter";


    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "1.0";


    /**
     * Constructor pushes all archive header values to the superclass
     */
    public SimpleAPICatalogArchiveBuilder()
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
              null);
    }


    /**
     * Returns the open metadata type archive containing all of the elements extracted from the connector
     * providers of the featured open connectors.
     *
     * @return populated open metadata archive object
     */
    public OpenMetadataArchive getOpenMetadataArchive()
    {
        String assetGUID = super.addAsset(apiAssetTypeName,
                                          customerQualifiedName,
                                          customerDisplayName,
                                          customerDescription,
                                          null);

        String apiSchemaTypeGUID = super.addTopLevelSchemaType(assetGUID,
                                                               apiSchemaTypeTypeName,
                                                               customerQualifiedName + "_api_schema_type",
                                                               customerDisplayName + " API Schema Type",
                                                               null,
                                                               null);

        String apiOperationGUID = super.addAPIOperation(apiSchemaTypeGUID,
                                                        getCustomerQualifiedName,
                                                        getCustomerDisplayName,
                                                        getCustomerDescription,
                                                        getCustomerPath,
                                                        getCustomerCommand,
                                                        null);

        String requestGUID = super.addAPIParameterList(apiOperationGUID,
                                                       API_REQUEST_TYPE_NAME,
                                                       getCustomerQualifiedName + "_request",
                                                       getCustomerDisplayName + " Request Parameter List",
                                                       null,
                                                       true,
                                                       null);


        String parameterGUID = super.addSchemaAttribute(apiSchemaAttributeTypeName,
                                                        null,
                                                        customerNoRequestQualifiedName,
                                                        customerNoDisplayName,
                                                        customerNoDescription,
                                                        customerNoDataType,
                                                        customerNoLength,
                                                        0,
                                                        null);

        super.addAttributeForSchemaType(requestGUID, parameterGUID);

        String responseGUID = super.addAPIParameterList(apiOperationGUID,
                                                        API_RESPONSE_TYPE_NAME,
                                                        getCustomerQualifiedName + "_response",
                                                        getCustomerDisplayName + " Response Parameter List",
                                                        null,
                                                        true,
                                                        null);

        parameterGUID = super.addSchemaAttribute(apiSchemaAttributeTypeName,
                                                 null,
                                                 customerNoResponseQualifiedName,
                                                 customerNoDisplayName,
                                                 customerNoDescription,
                                                 customerNoDataType,
                                                 customerNoLength,
                                                 0,
                                                 null);

        super.addAttributeForSchemaType(responseGUID, parameterGUID);

        parameterGUID = super.addSchemaAttribute(apiSchemaAttributeTypeName,
                                                 null,
                                                 customerNameQualifiedName,
                                                 customerNameDisplayName,
                                                 customerNameDescription,
                                                 customerNameDataType,
                                                 customerNameLength,
                                                 1,
                                                 null);

        super.addAttributeForSchemaType(responseGUID, parameterGUID);

        parameterGUID = super.addSchemaAttribute(apiSchemaAttributeTypeName,
                                                 null,
                                                 customerCardIdQualifiedName,
                                                 customerCardIdDisplayName,
                                                 customerCardIdDescription,
                                                 customerCardIdDataType,
                                                 customerCardIdLength,
                                                 2,
                                                 null);

        super.addAttributeForSchemaType(responseGUID, parameterGUID);

        return super.getOpenMetadataArchive();
    }
}
