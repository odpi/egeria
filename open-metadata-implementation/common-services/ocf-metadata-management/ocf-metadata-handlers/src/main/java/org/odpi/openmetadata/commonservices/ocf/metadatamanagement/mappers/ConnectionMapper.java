/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers;

/**
 * ConnectionMapper provides property name mapping for a Connection object.
 *
 * The ConnectorType object is a Referenceable.
 * It contains an Endpoint and a ConnectorType.  It may also be a VirtualConnection with embedded
 * Connection Objects.
 */
public class ConnectionMapper
{
    public static final String CONNECTION_TYPE_GUID                      = "114e9f8f-5ff3-4c32-bd37-a7eb42712253";
    public static final String CONNECTION_TYPE_NAME                      = "Connection";
    /* Referenceable */

    public static final String DISPLAY_NAME_PROPERTY_NAME                = "displayName";                          /* from Connection entity */
    public static final String DESCRIPTION_PROPERTY_NAME                 = "description";                          /* from Connection entity */
    public static final String SECURED_PROPERTIES_PROPERTY_NAME          = "securedProperties";                    /* from Connection entity */
    public static final String CONFIGURATION_PROPERTIES_PROPERTY_NAME    = "configurationProperties";              /* from Connection entity */
    public static final String USER_ID_PROPERTY_NAME                     = "userId";                               /* from Connection entity */
    public static final String CLEAR_PASSWORD_PROPERTY_NAME              = "clearPassword";                        /* from Connection entity */
    public static final String ENCRYPTED_PASSWORD_PROPERTY_NAME          = "encryptedPassword";                    /* from Connection entity */

    public static final String CONNECTION_ENDPOINT_TYPE_GUID             = "887a7132-d6bc-4b92-a483-e80b60c86fb2";
    public static final String CONNECTION_ENDPOINT_TYPE_NAME             = "ConnectionEndpoint";
    /* End1 = Endpoint; End 2 = Connection */

    public static final String CONNECTION_CONNECTOR_TYPE_TYPE_GUID       = "e542cfc1-0b4b-42b9-9921-f0a5a88aaf96";
    public static final String CONNECTION_CONNECTOR_TYPE_TYPE_NAME       = "ConnectionConnectorType";
    /* End1 = Connection; End 2 = ConnectorType */

    public static final String VIRTUAL_CONNECTION_TYPE_GUID              = "82f9c664-e59d-484c-a8f3-17088c23a2f3";
    public static final String VIRTUAL_CONNECTION_TYPE_NAME              = "VirtualConnection";

    public static final String EMBEDDED_CONNECTION_TYPE_GUID             = "eb6dfdd2-8c6f-4f0d-a17d-f6ce4799f64f";
    public static final String EMBEDDED_CONNECTION_TYPE_NAME             = "EmbeddedConnection";
    /* End1 = VirtualConnection; End 2 = Connection */

    public static final String POSITION_PROPERTY_NAME                    = "position";              /* from EmbeddedConnection relationship */
    public static final String ARGUMENTS_PROPERTY_NAME                   = "arguments";             /* from EmbeddedConnection relationship */
}
