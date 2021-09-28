/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.simplecatalogs.catalogcontent;


import org.odpi.openmetadata.archiveutilities.catalogbuilder.CatalogTypesArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;

import java.util.Date;

/**
 * SimpleEventCatalogArchiveBuilder provides event and topic metadata.
 */
public class SimpleEventCatalogArchiveBuilder extends CatalogTypesArchiveBuilder
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "e5114849-4341-4eab-b1b7-5a4b037363c4";
    private static final String                  archiveRootName    = "SimpleEventCatalog";
    private static final String                  archiveName        = "Simple Event Catalog";
    private static final String                  archiveLicense     = "Apache 2.0";
    private static final String                  archiveDescription = "Sample metadata showing topic assets and event types.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  originatorName     = "Egeria";
    private static final Date                    creationDate       = new Date(1632046251579L);

    /*
     * Names for event definitions
     */
    private static final String customerChangeQualifiedName = "global-event-bus/CustomerDomain/Topics/CustomerChange";
    private static final String customerChangeDisplayName   = "CustomerChangeTopic";
    private static final String customerChangeDescription   = "Topic for distributing updates when a customer status changes.";

    private static final String newCustomerStatusQualifiedName  = "global-event-bus/CustomerDomain/EventTypes/NewCustomerStatus";
    private static final String newCustomerStatusDisplayName    = "New Customer Status Event Type";
    private static final String newCustomerStatusDescription    = "Event payload to notify subscribers that a customer status has changed.";

    private static final String customerIdQualifiedName = "global-event-bus/CustomerDomain/EventAttributes/CustomerIdentifiers/customerId";
    private static final String customerIdDisplayName   = "customerId";
    private static final String customerIdDescription   = "The unique identifier assigned internally for a customer.";
    private static final String customerIdDataType      = "string";
    private static final int    customerIdLength        = 12;

    private static final String customerNameQualifiedName = "global-event-bus/CustomerDomain/EventAttributes/CustomerIdentifiers/customerName";
    private static final String customerNameDisplayName   = "customerName";
    private static final String customerNameDescription   = "The name for a customer - as supplied by the customer.";
    private static final String customerNameDataType      = "string";
    private static final int    customerNameLength        = 40;

    private static final String customerStatusQualifiedName = "global-event-bus/CustomerDomain/EventAttributes/CustomerIdentifiers/customerStatus";
    private static final String customerStatusDisplayName   = "customerStatus";
    private static final String customerStatusDescription   = "The calculated status for a customer indicating their value to the organization.";
    private static final String customerStatusDataType      = "string";
    private static final int    customerStatusLength        = 10;

    /*
     * Additional AssetTypes for basic file connector
     */
    private static final String topicAssetTypeName     = "KafkaTopic";
    private static final String eventTypeTypeName      = "EventType";
    private static final String eventAttributeTypeName = "EventSchemaAttribute";
    private static final String eventTypeListTypeName  = "EventTypeList";
    private static final String eventSetTypeName       = "EventSet";


    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "1.0";


    /**
     * Constructor pushes all archive header values to the superclass
     */
    public SimpleEventCatalogArchiveBuilder()
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
        String assetGUID = super.addAsset(topicAssetTypeName,
                                          customerChangeQualifiedName,
                                          customerChangeDisplayName,
                                          customerChangeDescription,
                                          null);

        String eventTypeListGUID = super.addTopLevelSchemaType(assetGUID,
                                                               eventTypeListTypeName,
                                                               customerChangeQualifiedName + "_event_type_list",
                                                               customerChangeDisplayName + " Event Type List",
                                                               null,
                                                               null);

        String eventTypeGUID  = super.addTopLevelSchemaType(null,
                                                            eventTypeTypeName,
                                                            newCustomerStatusQualifiedName,
                                                            newCustomerStatusDisplayName,
                                                            newCustomerStatusDescription,
                                                            null);

        super.addSchemaTypeOption(eventTypeListGUID, eventTypeGUID);

        String eventAttributeGUID = super.addSchemaAttribute(eventAttributeTypeName,
                                                             null,
                                                             customerIdQualifiedName,
                                                             customerIdDisplayName,
                                                             customerIdDescription,
                                                             customerIdDataType,
                                                             customerIdLength,
                                                             0,
                                                             null);

        super.addAttributeForSchemaType(eventTypeGUID, eventAttributeGUID);

        eventAttributeGUID = super.addSchemaAttribute(eventAttributeTypeName,
                                                      null,
                                                      customerNameQualifiedName,
                                                      customerNameDisplayName,
                                                      customerNameDescription,
                                                      customerNameDataType,
                                                      customerNameLength,
                                                      1,
                                                      null);

        super.addAttributeForSchemaType(eventTypeGUID, eventAttributeGUID);

        eventAttributeGUID = super.addSchemaAttribute(eventAttributeTypeName,
                                                      null,
                                                      customerStatusQualifiedName,
                                                      customerStatusDisplayName,
                                                      customerStatusDescription,
                                                      customerStatusDataType,
                                                      customerStatusLength,
                                                      2,
                                                      null);

        super.addAttributeForSchemaType(eventTypeGUID, eventAttributeGUID);

        return super.getOpenMetadataArchive();
    }
}
