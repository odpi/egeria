/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * LocationBuilder creates the parts for an entity that represents a location.
 */
public class LocationBuilder extends ReferenceableBuilder
{
    private String identifier = null;
    private String displayName = null;
    private String description = null;
   

    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the location
     * @param identifier code value or symbol used to identify the location - typically unique.
     * @param displayName short display name for the location
     * @param description description of the location
     * @param additionalProperties additional properties for a location
     * @param typeGUID unique identifier of this element's type
     * @param typeName unique name of this element's type
     * @param extendedProperties  properties for a location subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    LocationBuilder(String               qualifiedName,
                    String               identifier,
                    String               displayName,
                    String               description,
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

        this.identifier = identifier;
        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the location
     * @param identifier code value or symbol used to identify the location - typically unique.
     * @param displayName short display name for the location
     * @param description description of the location
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    LocationBuilder(String               qualifiedName,
                    String               identifier,
                    String               displayName,
                    String               description,
                    OMRSRepositoryHelper repositoryHelper,
                    String               serviceName,
                    String               serverName)
    {
        super(qualifiedName,
              null,
              OpenMetadataAPIMapper.LOCATION_TYPE_GUID,
              OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
              null,
              repositoryHelper,
              serviceName,
              serverName);

        this.identifier = identifier;
        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Classification constructor
     *
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    LocationBuilder(OMRSRepositoryHelper repositoryHelper,
                    String               serviceName,
                    String               serverName)
    {
        super(OpenMetadataAPIMapper.LOCATION_TYPE_GUID,
              OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
              repositoryHelper,
              serviceName,
              serverName);
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

        return properties;
    }


    /**
     * Return the bean properties describing a fixed location in an InstanceProperties object.
     *
     * @param coordinates coordinate location
     * @param mapProjection scheme used for the coordinates
     * @param postalAddress postal address of the location
     * @param timeZone time zone of the location
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getFixedLocationProperties(String coordinates,
                                                  String mapProjection,
                                                  String postalAddress,
                                                  String timeZone,
                                                  String methodName)
    {
        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  null,
                                                                  OpenMetadataAPIMapper.COORDINATES_PROPERTY_NAME,
                                                                  coordinates,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.MAP_PROJECTION_PROPERTY_NAME,
                                                                  mapProjection,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.POSTAL_ADDRESS_PROPERTY_NAME,
                                                                  postalAddress,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.TIME_ZONE_PROPERTY_NAME,
                                                                  timeZone,
                                                                  methodName);

        setEffectivityDates(properties);

        return properties;
    }


    /**
     * Return the bean properties describing a secure location in an InstanceProperties object.
     *
     * @param description description of security
     * @param level level of security
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getSecureLocationProperties(String description,
                                                   String level,
                                                   String methodName)
    {
        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  null,
                                                                  OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.LEVEL_PROPERTY_NAME,
                                                                  level,
                                                                  methodName);

        setEffectivityDates(properties);

        return properties;
    }


    /**
     * Return the bean properties describing a secure location in an InstanceProperties object.
     *
     * @param networkAddress network address of the location
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getCyberLocationProperties(String networkAddress,
                                                  String methodName)
    {
        return repositoryHelper.addStringPropertyToInstance(serviceName,
                                                            null,
                                                            OpenMetadataAPIMapper.NETWORK_ADDRESS_PROPERTY_NAME,
                                                            networkAddress,
                                                            methodName);
    }
}
