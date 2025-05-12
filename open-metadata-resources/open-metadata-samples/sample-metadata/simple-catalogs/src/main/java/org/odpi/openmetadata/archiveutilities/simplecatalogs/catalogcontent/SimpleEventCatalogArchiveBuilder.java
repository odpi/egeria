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
 * SimpleEventCatalogArchiveBuilder provides event and topic metadata.
 */
public class SimpleEventCatalogArchiveBuilder
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "e5114849-4341-4eab-b1b7-5a4b037363c4";
    private static final String                  archiveName        = "SimpleEventCatalog";
    private static final String                  archiveLicense     = "Apache-2.0";
    private static final String                  archiveDescription = "Sample metadata showing topic assets and event types.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.REPOSITORY_BACKUP;
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

    private final OMRSArchiveBuilder         archiveBuilder;
    private final SimpleCatalogArchiveHelper archiveHelper;

    /**
     * Constructor pushes all archive header values to the superclass
     *
     * @param archiveName name of the open metadata archive metadata collection.
     * @param archiveRootName non-spaced root name of the open metadata GUID map.
     */
    public SimpleEventCatalogArchiveBuilder(String archiveName,
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
    public SimpleEventCatalogArchiveBuilder(OMRSArchiveBuilder archiveBuilder, SimpleCatalogArchiveHelper archiveHelper)
    {
        this.archiveBuilder = archiveBuilder;
        this.archiveHelper = archiveHelper;
    }


    /**
     * Fills the archive builder with all the elements for this catalog.
     */
    public void fillBuilder()
    {
        String assetGUID = archiveHelper.addAsset(topicAssetTypeName,
                                                  customerChangeQualifiedName,
                                                  customerChangeDisplayName,
                                                  customerChangeDescription,
                                                  null,
                                                  null);

        String eventTypeListGUID = archiveHelper.addTopLevelSchemaType(assetGUID,
                                                                       topicAssetTypeName,
                                                                       eventTypeListTypeName,
                                                                       customerChangeQualifiedName + "_event_type_list",
                                                                       customerChangeDisplayName + " Event Type List",
                                                                       null,
                                                                       null);

        String eventTypeGUID  = archiveHelper.addSchemaType(assetGUID,
                                                            topicAssetTypeName,
                                                            eventTypeTypeName,
                                                            newCustomerStatusQualifiedName,
                                                            newCustomerStatusDisplayName,
                                                            newCustomerStatusDescription,
                                                            null);

        archiveHelper.addSchemaTypeOption(eventTypeListGUID, eventTypeGUID);

        String eventAttributeGUID = archiveHelper.addSchemaAttribute(assetGUID,
                                                                     topicAssetTypeName,
                                                                     eventAttributeTypeName,
                                                                     null,
                                                                     customerIdQualifiedName,
                                                                     customerIdDisplayName,
                                                                     customerIdDescription,
                                                                     customerIdDataType,
                                                                     customerIdLength,
                                                                     null,
                                                                     null);

        archiveHelper.addAttributeForSchemaType(eventTypeGUID, 1, 1, 1, eventAttributeGUID);

        eventAttributeGUID = archiveHelper.addSchemaAttribute(assetGUID,
                                                              topicAssetTypeName,
                                                              eventAttributeTypeName,
                                                              null,
                                                              customerNameQualifiedName,
                                                              customerNameDisplayName,
                                                              customerNameDescription,
                                                              customerNameDataType,
                                                              customerNameLength,
                                                              null,
                                                              null);

        archiveHelper.addAttributeForSchemaType(eventTypeGUID, 2, 1, 1, eventAttributeGUID);

        eventAttributeGUID = archiveHelper.addSchemaAttribute(assetGUID,
                                                              topicAssetTypeName,
                                                              eventAttributeTypeName,
                                                              null,
                                                              customerStatusQualifiedName,
                                                              customerStatusDisplayName,
                                                              customerStatusDescription,
                                                              customerStatusDataType,
                                                              customerStatusLength,
                                                              null,
                                                              null);

        archiveHelper.addAttributeForSchemaType(eventTypeGUID, 3, 1, 1, eventAttributeGUID);

        archiveHelper.saveGUIDs();
    }


    /**
     * Returns the open metadata type archive containing all the elements extracted from the connector
     * providers of the featured open connectors.
     *
     * @return populated open metadata archive object
     */
    public OpenMetadataArchive getOpenMetadataArchive()
    {
        fillBuilder();
        return archiveBuilder.getOpenMetadataArchive();
    }
}
