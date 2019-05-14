/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * ComplexSchemaTypeBuilder creates repository entities and relationships from properties for a primitive schema type.
 */
public class ComplexSchemaTypeBuilder extends SchemaTypeBuilder
{
    /**
     * Minimal constructor
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public ComplexSchemaTypeBuilder(String               qualifiedName,
                                    String               displayName,
                                    OMRSRepositoryHelper repositoryHelper,
                                    String               serviceName,
                                    String               serverName)
    {
        super(qualifiedName, displayName, repositoryHelper, serviceName, serverName);
    }


    /**
     * Constructor supporting all properties.
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param versionNumber version of the schema type.
     * @param author name of the author
     * @param usage guidance on how the schema should be used.
     * @param encodingStandard format of the schema.
     * @param additionalProperties additional properties
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public ComplexSchemaTypeBuilder(String               qualifiedName,
                                    String               displayName,
                                    String               versionNumber,
                                    String               author,
                                    String               usage,
                                    String               encodingStandard,
                                    Map<String, String>  additionalProperties,
                                    Map<String, Object>  extendedProperties,
                                    OMRSRepositoryHelper repositoryHelper,
                                    String               serviceName,
                                    String               serverName)
    {
        super(qualifiedName,
              displayName,
              versionNumber,
              author,
              usage,
              encodingStandard,
              additionalProperties,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);
    }
}
