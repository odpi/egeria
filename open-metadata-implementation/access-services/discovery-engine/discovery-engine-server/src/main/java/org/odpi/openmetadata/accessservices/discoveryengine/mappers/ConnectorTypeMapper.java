/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.mappers;

/**
 * ConnectorTypeMapper provides property name mapping for a ConnectorType object.
 * The ConnectorType object is a Referenceable.
 */
public class ConnectorTypeMapper
{
    public static final String CONNECTOR_TYPE_GUID                       = "954421eb-33a6-462d-a8ca-b5709a1bd0d4";
    public static final String CONNECTOR_TYPE_NAME                       = "ConnectorType";

    public static final String DISPLAY_NAME_PROPERTY_NAME                = "displayName";                          /* from ConnectorType entity */
    public static final String DESCRIPTION_PROPERTY_NAME                 = "description";                          /* from ConnectorType entity */
    public static final String CONNECTOR_PROVIDER_PROPERTY_NAME          = "connectorProviderClassName";           /* from ConnectorType entity */
    public static final String RECOGNIZED_ADD_PROPS_PROPERTY_NAME        = "recognizedAdditionalProperties";       /* from ConnectorType entity */
    public static final String RECOGNIZED_SEC_PROPS_PROPERTY_NAME        = "recognizedSecuredProperties";          /* from ConnectorType entity */
    public static final String RECOGNIZED_CONFIG_PROPS_PROPERTY_NAME     = "recognizedConfigurationProperties";    /* from ConnectorType entity */
}
