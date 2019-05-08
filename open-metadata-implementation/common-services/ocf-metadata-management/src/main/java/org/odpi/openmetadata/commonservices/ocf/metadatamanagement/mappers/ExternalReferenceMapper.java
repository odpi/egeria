/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers;

/**
 * ExternalReferenceMapper provides property name mapping for ExternalReferences and their relationships.
 */
public class ExternalReferenceMapper
{
    public static final String EXTERNAL_RELATIONSHIP_TYPE_GUID           = "af536f20-062b-48ef-9c31-1ddd05b04c56";
    public static final String EXTERNAL_RELATIONSHIP_TYPE_NAME           = "ExternalReference";    /* from Area 0 */
    /* Referenceable */

    public static final String DISPLAY_NAME_PROPERTY_NAME                = "displayName";         /* from ExternalReference entity */
    public static final String URL_PROPERTY_NAME                         = "url";                 /* from ExternalReference entity */
    public static final String VERSION_PROPERTY_NAME                     = "version";             /* from ExternalReference entity */
    public static final String DESCRIPTION_PROPERTY_NAME                 = "description";         /* from ExternalReference entity and ExternalReferenceLink relationship*/
    public static final String ORGANIZATION_PROPERTY_NAME                = "organization";        /* from ExternalReference entity */

    public static final String REFERENCEABLE_TO_EXT_REF_TYPE_GUID        = "7d818a67-ab45-481c-bc28-f6b1caf12f06";
    public static final String REFERENCEABLE_TO_EXT_REF_TYPE_NAME        = "ExternalReferenceLink";
    /* End1 = Referenceable; End 2 = ExternalReference */

    public static final String REFERENCE_ID_PROPERTY_NAME                = "referenceId";          /* from ExternalReferenceLink relationship */
    /* also description property */
}
