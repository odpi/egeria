/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.mappers;

/**
 * EndpointMapper provides property name mapping for an Endpoint object.
 * The Endpoint object is a Referenceable.
 */
public class EndpointMapper
{
    public static final String ENDPOINT_TYPE_GUID                        = "dbc20663-d705-4ff0-8424-80c262c6b8e7";
    public static final String ENDPOINT_TYPE_NAME                        = "Endpoint";

    public static final String DISPLAY_NAME_PROPERTY_NAME                = "name";                                 /* from Endpoint entity */
    public static final String DESCRIPTION_PROPERTY_NAME                 = "description";                          /* from Endpoint entity */
    public static final String NETWORK_ADDRESS_PROPERTY_NAME             = "networkAddress";                       /* from Endpoint entity */
    public static final String PROTOCOL_PROPERTY_NAME                    = "protocol";                             /* from Endpoint entity */
    public static final String ENCRYPTION_METHOD_PROPERTY_NAME           = "encryptionMethod";                     /* from Endpoint entity */
}
