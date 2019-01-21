/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.mappers;

/**
 * ExternalReferenceMapper provides the property name mapping for ExternalReference beans.
 * The mapping begins from the entity where the external reference is attached to and includes the properties
 * from the relationship as well as the external reference entity itself.
 */
public class ExternalReferenceMapper
{
    public static final String LINK_ID_PROPERTY_NAME               = "referenceId";          /* from ExternalReferenceLink relationship */
    public static final String LINK_DESCRIPTION_PROPERTY_NAME      = "description";          /* from ExternalReferenceLink relationship */
    public static final String RESOURCE_ID_PROPERTY_NAME           = "qualifiedName";        /* from Referenceable entity */
    public static final String RESOURCE_DISPLAY_NAME_PROPERTY_NAME = "displayName";          /* from ExternalReference entity */
    public static final String RESOURCE_DESCRIPTION_PROPERTY_NAME  = "description";          /* from ExternalReference entity */
    public static final String RESOURCE_URL_PROPERTY_NAME          = "url";                  /* from ExternalReference entity */
    public static final String RESOURCE_VERSION_PROPERTY_NAME      = "version";              /* from ExternalReference entity */
    public static final String OWNING_ORGANIZATION_PROPERTY_NAME   = "organization";         /* from ExternalReference entity */
    public static final String ADDITIONAL_PROPERTIES_PROPERTY_NAME = "additionalProperties"; /* from Referenceable entity */
}
