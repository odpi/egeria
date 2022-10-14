/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * SubjectAreaBuilder creates the parts for an entity that represents a subject area definition.
 */
public class SubjectAreaBuilder extends ReferenceableBuilder
{
    private final String subjectAreaName;
    private final String displayName;
    private final String description;
    private final String usage;
    private final String scope;
    private final int    domainIdentifier;


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the subject area entity
     * @param subjectAreaName unique name for the subject area - used in other configuration
     * @param displayName short display name for the subject area
     * @param description description of the subject area
     * @param usage the usage for inclusion in a subject area
     * @param scope scope of the organization that this definition applies to
     * @param domainIdentifier the identifier of the governance domain where the subject area is managed
     * @param additionalProperties additional properties for a subject area
     * @param typeGUID unique identifier of the type for the subject area
     * @param typeName unique name for the type for the subject area
     * @param extendedProperties  properties for a subject area subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    SubjectAreaBuilder(String               qualifiedName,
                       String               subjectAreaName,
                       String               displayName,
                       String               description,
                       String               usage,
                       String               scope,
                       int                  domainIdentifier,
                       Map<String, String>  additionalProperties,
                       String               typeGUID,
                       String               typeName,
                       Map<String, Object>  extendedProperties,
                       OMRSRepositoryHelper repositoryHelper,
                       String               serviceName,
                       String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              typeGUID,
              typeName,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.subjectAreaName = subjectAreaName;
        this.displayName = displayName;
        this.description = description;
        this.usage = usage;
        this.scope = scope;
        this.domainIdentifier = domainIdentifier;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    @Override
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.SUBJECT_AREA_NAME_PROPERTY_NAME,
                                                                  subjectAreaName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                  displayName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.USAGE_PROPERTY_NAME,
                                                                  usage,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.SCOPE_PROPERTY_NAME,
                                                                  scope,
                                                                  methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataAPIMapper.DOMAIN_IDENTIFIER_PROPERTY_NAME,
                                                               domainIdentifier,
                                                               methodName);

        return properties;
    }
}
