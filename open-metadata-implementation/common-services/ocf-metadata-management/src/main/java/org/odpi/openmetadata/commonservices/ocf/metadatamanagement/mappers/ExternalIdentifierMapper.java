/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers;

/**
 * ExternalIdentifierMapper provides property name mapping for ExternalIdentifiers and their relationships.
 */
public class ExternalIdentifierMapper
{
    public static final String EXTERNAL_IDENTIFIER_TYPE_GUID             = "7c8f8c2c-cc48-429e-8a21-a1f1851ccdb0";
    public static final String EXTERNAL_IDENTIFIER_TYPE_NAME             = "ExternalId";              /* from Area 0 */

    public static final String IDENTIFIER_PROPERTY_NAME                  = "identifier";              /* from ExternalId entity */
    public static final String KEY_PATTERN_PROPERTY_NAME                 = "keyPattern";              /* from External entity */
    /* Enum type KeyPattern */

    public static final String REFERENCEABLE_TO_EXTERNAL_ID_TYPE_GUID    = "28ab0381-c662-4b6d-b787-5d77208de126";
    public static final String REFERENCEABLE_TO_EXTERNAL_ID_TYPE_NAME    = "ExternalIdLink";
    /* End1 = Referenceable; End 2 = ExternalId */

    public static final String EXTERNAL_ID_SCOPE_TYPE_GUID               = "8c5b1415-2d1f-4190-ba6c-1fdd47f03269";
    public static final String EXTERNAL_ID_SCOPE_TYPE_NAME               = "ExternalIdScope";
    /* End1 = Referenceable; End 2 = ExternalId */

    public static final String DESCRIPTION_PROPERTY_NAME                 = "description";              /* from ExternalIdLink and ExternalIdScope relationships */
    public static final String USAGE_PROPERTY_NAME                       = "usage";                    /* from ExternalIdLink relationship */
    public static final String SOURCE_PROPERTY_NAME                      = "source";                   /* from ExternalIdLink relationship */
}
