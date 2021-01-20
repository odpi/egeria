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


}
