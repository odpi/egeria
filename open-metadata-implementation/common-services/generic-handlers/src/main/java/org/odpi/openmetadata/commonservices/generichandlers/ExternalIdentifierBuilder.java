/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
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
    private String identifier                     = null;
    private int    keyPattern                     = 0;
    private String externalInstanceTypeName       = null;
    private String externalInstanceCreatedBy      = null;
    private Date   externalInstanceCreationTime   = null;
    private String externalInstanceLastUpdatedBy  = null;
    private Date   externalInstanceLastUpdateTime = null;
    private long   externalInstanceVersion        = 0L;


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
        super(OpenMetadataType.EXTERNAL_ID.typeGUID,
              OpenMetadataType.EXTERNAL_ID.typeName,
              repositoryHelper,
              serviceName,
              serverName);
    }


    /**
     * Constructor supporting all properties for the ExternalId entity.
     *
     * @param identifier the identifier from the external technology
     * @param keyPattern identifier from the external party
     * @param externalInstanceTypeName the type name of the instance in the external system
     * @param externalInstanceCreatedBy the username of the person or process that created the instance in the external system
     * @param externalInstanceCreationTime the date/time when the instance in the external system was created
     * @param externalInstanceLastUpdatedBy the username of the person or process that last updated the instance in the external system
     * @param externalInstanceLastUpdateTime the date/time that the instance in the external system was last updated
     * @param externalInstanceVersion the latest version of the element in the external system
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ExternalIdentifierBuilder(String               identifier,
                              int                  keyPattern,
                              String               externalInstanceTypeName,
                              String               externalInstanceCreatedBy,
                              Date                 externalInstanceCreationTime,
                              String               externalInstanceLastUpdatedBy,
                              Date                 externalInstanceLastUpdateTime,
                              long                 externalInstanceVersion,
                              OMRSRepositoryHelper repositoryHelper,
                              String               serviceName,
                              String               serverName)
    {
        super(identifier + ":" + UUID.randomUUID(),
              OpenMetadataType.EXTERNAL_ID.typeGUID,
              OpenMetadataType.EXTERNAL_ID.typeName,
              repositoryHelper,
              serviceName,
              serverName);

        this.identifier = identifier;
        this.keyPattern = keyPattern;
        this.externalInstanceTypeName = externalInstanceTypeName;
        this.externalInstanceCreatedBy = externalInstanceCreatedBy;
        this.externalInstanceCreationTime = externalInstanceCreationTime;
        this.externalInstanceLastUpdatedBy = externalInstanceLastUpdatedBy;
        this.externalInstanceLastUpdateTime = externalInstanceLastUpdateTime;
        this.externalInstanceVersion = externalInstanceVersion;
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
                                                                  OpenMetadataProperty.IDENTIFIER.name,
                                                                  identifier,
                                                                  methodName);

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataProperty.KEY_PATTERN.name,
                                                                    OpenMetadataType.KEY_PATTERN_ENUM_TYPE_GUID,
                                                                    OpenMetadataType.KEY_PATTERN_ENUM_TYPE_NAME,
                                                                    keyPattern,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataProperty.KEY_PATTERN.name);
        }

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.EXT_INSTANCE_TYPE_NAME.name,
                                                                  externalInstanceTypeName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.EXT_INSTANCE_CREATED_BY.name,
                                                                  externalInstanceCreatedBy,
                                                                  methodName);

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataProperty.EXT_INSTANCE_CREATION_TIME.name,
                                                                externalInstanceCreationTime,
                                                                methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.EXT_INSTANCE_LAST_UPDATED_BY.name,
                                                                  externalInstanceLastUpdatedBy,
                                                                  methodName);

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataProperty.EXT_INSTANCE_LAST_UPDATE_TIME.name,
                                                                externalInstanceLastUpdateTime,
                                                                methodName);

        properties = repositoryHelper.addLongPropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataProperty.EXT_INSTANCE_VERSION.name,
                                                                externalInstanceVersion,
                                                                methodName);

        return properties;
    }


    /**
     * Return the properties that are to be stored in the ExternalIdLink relationship.
     *
     * @param description description of the linkage between the open metadata referenceable (resource) and the external id
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
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.USAGE.name,
                                                                  usage,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.SOURCE.name,
                                                                  source,
                                                                  methodName);

        properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                     properties,
                                                                     OpenMetadataProperty.MAPPING_PROPERTIES.name,
                                                                     mappingProperties,
                                                                     methodName);

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataProperty.LAST_SYNCHRONIZED.name,
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
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataProperty.PERMITTED_SYNCHRONIZATION.name,
                                                                    OpenMetadataType.PERMITTED_SYNC_ENUM_TYPE_GUID,
                                                                    OpenMetadataType.PERMITTED_SYNC_ENUM_TYPE_NAME,
                                                                    permittedSynchronization,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataProperty.PERMITTED_SYNCHRONIZATION.name);
        }

        setEffectivityDates(properties);

        return properties;
    }
}
