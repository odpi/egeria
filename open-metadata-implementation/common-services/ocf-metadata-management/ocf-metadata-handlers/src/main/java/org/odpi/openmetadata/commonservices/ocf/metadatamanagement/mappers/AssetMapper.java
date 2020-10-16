/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers;

/**
 * AssetMapper provides property name mapping for assets.
 */
public class AssetMapper
{
    public static final String ASSET_TYPE_GUID                           = "896d14c2-7522-4f6c-8519-757711943fe6";
    public static final String ASSET_TYPE_NAME                           = "Asset";
    /* Referenceable */

    public static final String DISPLAY_NAME_PROPERTY_NAME                = "name";                                 /* from Asset entity */
    public static final String DESCRIPTION_PROPERTY_NAME                 = "description";                          /* from Asset entity */
    public static final String LATEST_CHANGE_PROPERTY_NAME               = "latestChange";                         /* from Asset entity */

    public static final String ASSET_TO_CONNECTION_TYPE_GUID             = "e777d660-8dbe-453e-8b83-903771f054c0";
    public static final String ASSET_TO_CONNECTION_TYPE_NAME             = "ConnectionToAsset";
    /* End1 = Connection; End 2 = Asset */

    public static final String SHORT_DESCRIPTION_PROPERTY_NAME           = "assetSummary";                         /* from ConnectionToAsset relationship */

    public static final String ASSET_LOCATION_TYPE_GUID                  = "bc236b62-d0e6-4c5c-93a1-3a35c3dba7b1";  /* from Area 0 */
    public static final String ASSET_LOCATION_TYPE_NAME                  = "AssetLocation";
    /* End1 = Location; End 2 = Asset */

    public static final String ASSET_TO_SCHEMA_TYPE_TYPE_GUID            = "815b004d-73c6-4728-9dd9-536f4fe803cd";  /* from Area 5 */
    public static final String ASSET_TO_SCHEMA_TYPE_TYPE_NAME            = "AssetSchemaType";
    /* End1 = Asset; End 2 = SchemaType */


    public static final String DATA_STORE_TYPE_GUID                      = "10752b4a-4b5d-4519-9eae-fdd6d162122f";  /* from Area 2 */
    public static final String DATA_STORE_TYPE_NAME                      = "DataStore";
    /* Asset */

    public static final String CREATE_TIME_PROPERTY_NAME                 = "createTime";                            /* from DataStore entity */
    public static final String MODIFIED_TIME_PROPERTY_NAME               = "modifiedTime";                          /* from DataStore entity */

    public static final String DATA_STORE_ENCODING_CLASSIFICATION_GUID   = "f08e48b5-6b66-40f5-8ff6-c2bfe527330b";
    public static final String DATA_STORE_ENCODING_CLASSIFICATION_NAME   = "DataStoreEncoding";

    public static final String ENCODING_TYPE_PROPERTY_NAME               = "encoding";      /* from DataStoreEncoding classification */
    public static final String ENCODING_LANGUAGE_PROPERTY_NAME           = "language";      /* from DataStoreEncoding classification */
    public static final String ENCODING_DESCRIPTION_PROPERTY_NAME        = "description";   /* from DataStoreEncoding classification */
    public static final String ENCODING_PROPERTIES_PROPERTY_NAME         = "properties";    /* from DataStoreEncoding classification */

    public static final String DATABASE_TYPE_GUID                        = "0921c83f-b2db-4086-a52c-0d10e52ca078";  /* from Area 2 */
    public static final String DATABASE_TYPE_NAME                        = "Database";
    /* DataStore */

    public static final String DATABASE_TYPE_PROPERTY_NAME               = "type";          /* from Database entity */
    public static final String DATABASE_VERSION_PROPERTY_NAME            = "version";       /* from Database entity */
    public static final String DATABASE_INSTANCE_PROPERTY_NAME           = "instance";      /* from Database entity */
    public static final String DATABASE_IMPORTED_FROM_PROPERTY_NAME      = "importedFrom";  /* from Database entity */

    public static final String FILE_FOLDER_TYPE_GUID                     = "229ed5cc-de31-45fc-beb4-9919fd247398";  /* from Area 2 */
    public static final String FILE_FOLDER_TYPE_NAME                     = "FileFolder";
    /* DataStore */

    public static final String DATA_FOLDER_TYPE_GUID                     = "9f1fb984-db15-43ee-85fb-f8b0353bfb8b";  /* from Area 2 */
    public static final String DATA_FOLDER_TYPE_NAME                     = "DataFolder";
    /* FileFolder */

    public static final String FOLDER_HIERARCHY_TYPE_GUID                = "48ac9028-45dd-495d-b3e1-622685b54a01";  /* from Area 2 */
    public static final String FOLDER_HIERARCHY_TYPE_NAME                = "FolderHierarchy";

    public static final String NESTED_FILE_TYPE_GUID                     = "4cb88900-1446-4eb6-acea-29cd9da45e63";  /* from Area 2 */
    public static final String NESTED_FILE_TYPE_NAME                     = "NestedFile";

    public static final String LINKED_FILE_TYPE_GUID                     = "970a3405-fde1-4039-8249-9aa5f56d5151";  /* from Area 2 */
    public static final String LINKED_FILE_TYPE_NAME                     = "LinkedFile";

    public static final String DATA_FILE_TYPE_GUID                       = "10752b4a-4b5d-4519-9eae-fdd6d162122f";  /* from Area 2 */
    public static final String DATA_FILE_TYPE_NAME                       = "DataFile";
    /* DataStore */

    public static final String FILE_TYPE_PROPERTY_NAME                   = "fileType";                   /* from DataFile entity */

    public static final String MEDIA_FILE_TYPE_GUID                      = "c5ce5499-9582-42ea-936c-9771fbd475f8";  /* from Area 2 */
    public static final String MEDIA_FILE_TYPE_NAME                      = "MediaFile";
    /* DataFile */
    public static final String DOCUMENT_TYPE_GUID                        = "b463827c-c0a0-4cfb-a2b2-ddc63746ded4";  /* from Area 2 */
    public static final String DOCUMENT_TYPE_NAME                        = "Document";
    /* MediaFile */
    public static final String DOCUMENT_STORE_TYPE_GUID                  = "37156790-feac-4e1a-a42e-88858ae6f8e1";  /* from Area 2 */
    public static final String DOCUMENT_STORE_TYPE_NAME                  = "DocumentStore";
    /* DataStore */
    public static final String MEDIA_COLLECTION_TYPE_GUID                = "0075d603-1627-41c5-8cae-f5458d1247fe";  /* from Area 2 */
    public static final String MEDIA_COLLECTION_TYPE_NAME                = "MediaCollection";
    /* DataSet */

    public static final String LINKED_MEDIA_TYPE_GUID                    = "cee3a190-fc8d-4e53-908a-f1b9689581e0";  /* from Area 0 */
    public static final String LINKED_MEDIA_TYPE_NAME                    = "LinkedMedia";
    /* End1 = MediaFile; End 2 = MediaFile */
    public static final String GROUPED_MEDIA_TYPE_GUID                   = "7d881574-461d-475c-ab44-077451528cb8";  /* from Area 0 */
    public static final String GROUPED_MEDIA_TYPE_NAME                   = "GroupedMedia";
    /* End1 = MediaCollection; End 2 = MediaFile */

    public static final String EMBEDDED_METADATA_PROPERTY_NAME           = "embeddedMetadata";                      /* from MediaFile entity */

    public static final String AVRO_FILE_TYPE_GUID                       = "75293260-3373-4777-af7d-7274d5c0b9a5";  /* from Area 2 */
    public static final String AVRO_FILE_TYPE_NAME                       = "AvroFile";
    /* DataFile */

    public static final String CSV_FILE_TYPE_GUID                        = "2ccb2117-9cee-47ca-8150-9b3a543adcec";  /* from Area 2 */
    public static final String CSV_FILE_TYPE_NAME                        = "CSVFile";
    /* DataFile */

    public static final String DELIMITER_CHARACTER_PROPERTY_NAME         = "delimiterCharacter";                   /* from CSVFile entity */
    public static final String QUOTE_CHARACTER_PROPERTY_NAME             = "quoteCharacter";                       /* from CSVFile entity */


    public static final String JSON_FILE_TYPE_GUID                       = "baa608fa-510e-42d7-95cd-7c12fa37bb35";  /* from Area 2 */
    public static final String JSON_FILE_TYPE_NAME                       = "JSONFile";
    /* DataFile */

    public static final String DATA_SET_TYPE_GUID                        = "1449911c-4f44-4c22-abc0-7540154feefb";  /* from Area 0 */
    public static final String DATA_SET_TYPE_NAME                        = "DataSet";
    /* Asset */

    public static final String DEPLOYED_DATABASE_SCHEMA_TYPE_GUID        = "eab811ec-556a-45f1-9091-bc7ac8face0f";  /* from Area 2 */
    public static final String DEPLOYED_DATABASE_SCHEMA_TYPE_NAME        = "DeployedDatabaseSchema";
    /* DataSet */

    public static final String DATA_CONTENT_FOR_DATA_SET_TYPE_GUID       = "b827683c-2924-4df3-a92d-7be1888e23c0";  /* from Area 2 */
    public static final String DATA_CONTENT_FOR_DATA_SET_TYPE_NAME       = "DataContentForDataSet";
    /* End1 = Asset; End 2 = DataSet */

    public static final String SERVER_ASSET_USE_TYPE_GUID                = "92b75926-8e9a-46c7-9d98-89009f622397";  /* from Area 0 */
    public static final String SERVER_ASSET_USE_TYPE_NAME                = "ServerAssetUse";
    /* End1 = SoftwareServerCapability; End 2 = Asset */


    public static final String USE_TYPE_PROPERTY_NAME                    = "useType";                         /* from ServerAssetUse relationship */
    /* public static final String DESCRIPTION_PROPERTY_NAME              = "description";                        from ServerAssetUse relationship */

    public static final String ASSET_ORIGIN_CLASSIFICATION_GUID          = "e530c566-03d2-470a-be69-6f52bfbd5fb7";
    public static final String ASSET_ORIGIN_CLASSIFICATION_NAME          = "AssetOrigin";

    public static final String ORGANIZATION_GUID_PROPERTY_NAME           = "organization";                         /* from AssetOrigin classification */
    public static final String BUSINESS_CAPABILITY_GUID_PROPERTY_NAME    = "businessCapability";                   /* from AssetOrigin classification */
    public static final String OTHER_ORIGIN_VALUES_PROPERTY_NAME         = "otherOriginValues";                    /* from AssetOrigin classification */

    public static final String ASSET_ZONES_CLASSIFICATION_GUID           = "a1c17a86-9fd3-40ca-bb9b-fe83c6981deb";
    public static final String ASSET_ZONES_CLASSIFICATION_NAME           = "AssetZoneMembership";

    public static final String ZONE_MEMBERSHIP_PROPERTY_NAME             = "zoneMembership";                       /* from Asset entity */

    public static final String ASSET_OWNERSHIP_CLASSIFICATION_GUID       = "d531c566-03d2-470a-be69-6f52cabd5fb9";
    public static final String ASSET_OWNERSHIP_CLASSIFICATION_NAME       = "AssetOwnership";

    public static final String OWNER_PROPERTY_NAME                       = "owner";                                /* from Asset entity */
    public static final String OWNER_TYPE_PROPERTY_NAME                  = "ownerType";                            /* from Asset entity */

    public static final String REFERENCE_DATA_CLASSIFICATION_GUID        = "55e5ae33-39c6-4834-9d05-ef0ae4e0163b";
    public static final String REFERENCE_DATA_CLASSIFICATION_NAME        = "ReferenceData";

    public static final String DEPLOYED_API_TYPE_GUID                    = "7dbb3e63-138f-49f1-97b4-66313871fc14";  /* from Area 2 */
    public static final String DEPLOYED_API_TYPE_NAME                    = "DeployedAPI";
    /* Asset */
    public static final String API_ENDPOINT_TYPE_GUID                    = "de5b9501-3ad4-4803-a8b2-e311c72a4336";  /* from Area 0 */
    public static final String API_ENDPOINT_TYPE_NAME                    = "APIEndpoint";
    /* End1 = DeployedAPI; End 2 = Endpoint */

    public static final String LOG_FILE_TYPE_GUID                        = "ff4c8484-9127-464a-97fc-99579d5bc429";  /* from Area 2 */
    public static final String LOG_FILE_TYPE_NAME                        = "LogFile";
    /* DataFile */
    public static final String LOG_FILE_TYPE_PROPERTY_NAME               = "type";                                  /* from LogFile entity */

    public static final String TOPIC_TYPE_GUID                           = "29100f49-338e-4361-b05d-7e4e8e818325";  /* from Area 2 */
    public static final String TOPIC_TYPE_NAME                           = "Topic";
    /* DataSet */
    public static final String TOPIC_TYPE_PROPERTY_NAME                  = "topicType";                             /* from Topic entity */

    public static final String SUBSCRIBER_LIST_TYPE_GUID                 = "69751093-35f9-42b1-944b-ba6251ff513d";  /* from Area 0 */
    public static final String SUBSCRIBER_LIST_TYPE_NAME                 = "SubscriberList";
    /* DataSet */
    public static final String TOPIC_SUBSCRIBERS_TYPE_GUID               = "bc91a28c-afb9-41a7-8eb2-fc8b5271fe9e";  /* from Area 0 */
    public static final String TOPIC_SUBSCRIBERS_TYPE_NAME               = "TopicSubscribers";
    /* End1 = SubscriberList; End 2 = Topic */

}
