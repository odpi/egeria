/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * ExternalIdentifierBuilder is able to build the properties for an ExternalId entity and the relationships that connect it
 * to the element it represents and the source of the external id.
 */
class ExternalIdentifierBuilder extends ReferenceableBuilder
{
    private String              identifier        = null;
    private int                 keyPattern        = 0;



    /**
     * Constructor used for working with relationship properties.
     *
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ExternalIdentifierBuilder(OMRSRepositoryHelper repositoryHelper,
                              String               serviceName,
                              String               serverName)
    {
        super(OpenMetadataAPIMapper.EXTERNAL_IDENTIFIER_TYPE_GUID,
              OpenMetadataAPIMapper.EXTERNAL_IDENTIFIER_TYPE_NAME,
              repositoryHelper,
              serviceName,
              serverName);
    }


    /**
     * Constructor supporting all properties for the ExternalId entity.
     *
     * @param identifier the identifier from the external technology
     * @param keyPattern identifier from the external party
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ExternalIdentifierBuilder(String               identifier,
                              int                  keyPattern,
                              OMRSRepositoryHelper repositoryHelper,
                              String               serviceName,
                              String               serverName)
    {
        super(identifier + UUID.randomUUID(),
              OpenMetadataAPIMapper.EXTERNAL_IDENTIFIER_TYPE_GUID,
              OpenMetadataAPIMapper.EXTERNAL_IDENTIFIER_TYPE_NAME,
              repositoryHelper,
              serviceName,
              serverName);

        this.identifier = identifier;
        this.keyPattern = keyPattern;
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
                                                                  OpenMetadataAPIMapper.IDENTIFIER_PROPERTY_NAME,
                                                                  identifier,
                                                                  methodName);

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataAPIMapper.KEY_PATTERN_PROPERTY_NAME,
                                                                    OpenMetadataAPIMapper.KEY_PATTERN_ENUM_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.KEY_PATTERN_ENUM_TYPE_NAME,
                                                                    keyPattern,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataAPIMapper.KEY_PATTERN_PROPERTY_NAME);
        }


        return properties;
    }


    /**
     * Return the properties that are to be stored in the ExternalIdLink relationship.
     *
     * @param description description of the linkage between the open metadata referenceable (resource) and the external id)
     * @param usage the way that the external identifier should be used.
     * @param source the description of the source (typically the integration connector name)
     * @param mappingProperties additional properties to help with the mapping to the external metadata
     * @param methodName calling method
     * @return these properties packed into an OMRS instance properties object (or null if all the properties are null)
     */
    InstanceProperties getExternalIdResourceLinkProperties(String              description,
                                                           String              usage,
                                                           String              source,
                                                           Map<String, String> mappingProperties,
                                                           String              methodName)
    {
        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  null,
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
                                                                  OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME,
                                                                  source,
                                                                  methodName);

        properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                     properties,
                                                                     OpenMetadataAPIMapper.MAPPING_PROPERTIES_PROPERTY_NAME,
                                                                     mappingProperties,
                                                                     methodName);

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataAPIMapper.LAST_SYNCHRONIZED_PROPERTY_NAME,
                                                                new Date(),
                                                                methodName);

        setEffectivityDates(properties);

        return properties;
    }


    /**
     * Return the properties that are stored in the ExternalIdScope relationship.  This is between the externalId and
     * the software capability that represents the external metadata source.
     *
     * @param description description of the linkage between the external identifier and its owner
     * @param permittedSynchronization direction of synchronization
     * @param methodName calling method
     * @return description packed into an OMRS instance properties object (or null if description is null)
     */
    InstanceProperties getExternalIdScopeProperties(String description,
                                                    int    permittedSynchronization,
                                                    String methodName) throws InvalidParameterException
    {
        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  null,
                                                                  OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                  description,
                                                                  methodName);

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataAPIMapper.PERMITTED_SYNC_PROPERTY_NAME,
                                                                    OpenMetadataAPIMapper.PERMITTED_SYNC_ENUM_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.PERMITTED_SYNC_ENUM_TYPE_NAME,
                                                                    permittedSynchronization,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataAPIMapper.KEY_PATTERN_PROPERTY_NAME);
        }

        setEffectivityDates(properties);

        return properties;
    }
}
