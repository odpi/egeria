/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers;

/**
 * RelatedMediaMapper provides property name mapping for RelatedMedia and their relationships.
 */
public class RelatedMediaMapper
{
    public static final String RELATED_MEDIA_TYPE_GUID                   = "747f8b86-fe7c-4c9b-ba75-979e093cc307";
    public static final String RELATED_MEDIA_TYPE_NAME                   = "RelatedMedia";         /* from Area 0 */
    /* ExternalReference */

    public static final String MEDIA_USAGE_PROPERTY_NAME                 = "mediaUsage";           /* from RelatedMedia entity */
    public static final String MEDIA_TYPE_PROPERTY_NAME                  = "mediaType";            /* from RelatedMedia entity */
    /* MediaType enum */

    public static final String REFERENCEABLE_TO_RELATED_MEDIA_TYPE_GUID  = "1353400f-b0ab-4ab9-ab09-3045dd8a7140";
    public static final String REFERENCEABLE_TO_RELATED_MEDIA_TYPE_NAME  = "MediaReference";
    /* End1 = Referenceable; End 2 = RelatedMedia */

    public static final String MEDIA_ID_PROPERTY_NAME                    = "mediaId";              /* from MediaReference relationship */
    public static final String DESCRIPTION_PROPERTY_NAME                 = "description";          /* from MediaReference relationship */
}
