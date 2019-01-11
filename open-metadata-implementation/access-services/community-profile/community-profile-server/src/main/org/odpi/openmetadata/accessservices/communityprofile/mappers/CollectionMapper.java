/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.mappers;

/**
 * CollectionMapper provides property name mapping for collections.
 */
public class CollectionMapper
{
    public static final String QUALIFIED_NAME_PROPERTY_NAME             = "qualifiedName";        /* from Referenceable entity */
    public static final String NAME_PROPERTY_NAME                       = "name";                 /* from Collection entity */
    public static final String DESCRIPTION_PROPERTY_NAME                = "description";          /* from Collection entity */
    public static final String COLLECTION_ORDERING_ENUM_PROPERTY_NAME   = "orderBy";              /* from Folder classification */
    public static final String COLLECTION_ORDERING_OTHER_PROPERTY_NAME  = "orderPropertyName";    /* from Folder classification */
    public static final String ADDITIONAL_PROPERTIES_PROPERTY_NAME      = "additionalProperties"; /* from Referenceable entity */

    public static final String FOLDER_TYPE_NAME                         = "Folder";               /* from Folder classification */

    public static final String ORDER_BY_NAME_VALUE                      = "Name";                 /* from OrderBy enum */
    public static final String ORDER_BY_OWNER_VALUE                     = "Owner";                /* from OrderBy enum */
    public static final String ORDER_BY_DATE_ADDED_VALUE                = "DateAdded";            /* from OrderBy enum */
    public static final String ORDER_BY_DATE_UPDATED_VALUE              = "DateUpdated";          /* from OrderBy enum */
    public static final String ORDER_BY_DATE_CREATED_VALUE              = "DateCreated";          /* from OrderBy enum */
    public static final String ORDER_BY_OTHER_VALUE                     = "Other";                /* from OrderBy enum */
}
