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
    public static final String OWNER_PROPERTY_NAME                       = "owner";                                /* from Asset entity */
    public static final String OWNER_TYPE_PROPERTY_NAME                  = "ownerType";                            /* from Asset entity */
    public static final String ZONE_MEMBERSHIP_PROPERTY_NAME             = "zoneMembership";                       /* from Asset entity */
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

    public static final String FILE_FOLDER_TYPE_GUID                     = "229ed5cc-de31-45fc-beb4-9919fd247398";  /* from Area 2 */
    public static final String FILE_FOLDER_TYPE_NAME                     = "FileFolder";
    /* DataStore */

    public static final String DATA_FILE_TYPE_GUID                       = "10752b4a-4b5d-4519-9eae-fdd6d162122f";  /* from Area 2 */
    public static final String DATA_FILE_TYPE_NAME                       = "DataFile";
    /* DataStore */

    public static final String MEDIA_FILE_TYPE_GUID                      = "c5ce5499-9582-42ea-936c-9771fbd475f8";  /* from Area 2 */
    public static final String MEDIA_FILE_TYPE_NAME                      = "MediaFile";
    /* DataFile */

    public static final String AVRO_FILE_TYPE_GUID                       = "75293260-3373-4777-af7d-7274d5c0b9a5";  /* from Area 2 */
    public static final String AVRO_FILE_TYPE_NAME                       = "AvroFile";
    /* DataFile */

    public static final String CSV_FILE_TYPE_GUID                        = "2ccb2117-9cee-47ca-8150-9b3a543adcec";  /* from Area 2 */
    public static final String CSV_FILE_TYPE_NAME                        = "CSVFile";
    /* DataFile */

    public static final String DELIMITER_CHARACTER_PROPERTY_NAME         = "delimiterCharacter";                   /* from CSVFile entity */
    public static final String QUOTE_CHARACTER_PROPERTY_NAME             = "quoteCharacter";                       /* from CSVFile entity */

}
