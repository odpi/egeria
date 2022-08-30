/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * ActorProfileBuilder creates the parts for an entity that represents a actor profile.
 * It is designed to be used to create its main subtype: Person, Team and ITProfile.
 */
public class ActorProfileBuilder extends ReferenceableBuilder
{
    private final String name;
    private final String description;


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the actor profile
     * @param name short display name for the actor profile
     * @param description description of the actor profile
     * @param additionalProperties additional properties for a actor profile
     * @param typeGUID unique identifier of this element's type
     * @param typeName unique name of this element's type
     * @param extendedProperties  properties for a actor profile subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ActorProfileBuilder(String               qualifiedName,
                        String               name,
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

        this.name = name;
        this.description = description;
    }


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the actor profile
     * @param name short display name for the actor profile
     * @param description description of the actor profile
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ActorProfileBuilder(String               qualifiedName,
                        String               name,
                        String               description,
                        OMRSRepositoryHelper repositoryHelper,
                        String               serviceName,
                        String               serverName)
    {
        super(qualifiedName,
              repositoryHelper,
              serviceName,
              serverName);

        this.name = name;
        this.description = description;
    }


    /**
     * Create constructor
     *
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ActorProfileBuilder(OMRSRepositoryHelper repositoryHelper,
                        String               serviceName,
                        String               serverName)
    {
        super(repositoryHelper,
              serviceName,
              serverName);

        this.name = null;
        this.description = null;
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
                                                                  OpenMetadataAPIMapper.NAME_PROPERTY_NAME,
                                                                  name,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                  description,
                                                                  methodName);

        return properties;
    }


    /**
     * Return the bean properties describing a TeamStructure relationship in an InstanceProperties object.
     *
     * @param delegationEscalationAuthority the factor used to organize the category hierarchy that forms the taxonomy
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getTeamStructureProperties(boolean delegationEscalationAuthority,
                                                  String  methodName)
    {
        InstanceProperties properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                                      null,
                                                                                      OpenMetadataAPIMapper.DELEGATION_ESCALATION_PROPERTY_NAME,
                                                                                      delegationEscalationAuthority,
                                                                                      methodName);

        setEffectivityDates(properties);

        return properties;
    }
}
