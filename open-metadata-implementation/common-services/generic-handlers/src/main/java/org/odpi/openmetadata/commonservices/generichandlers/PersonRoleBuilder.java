/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.Map;

/**
 * PersonRoleBuilder creates the parts for an entity that represents a person role plus manages the properties for the
 * relationships.
 */
public class PersonRoleBuilder extends ReferenceableBuilder
{
    private String name        = null;
    private String description = null;
    private String scope       = null;
    private int    headCount   = 1;


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the role
     * @param name short display name for the role
     * @param description description of the role
     * @param scope the scope of the role
     * @param headCount number of individuals that can be appointed to this role
     * @param additionalProperties additional properties for a role
     * @param typeGUID unique identifier of this element's type
     * @param typeName unique name of this element's type
     * @param extendedProperties  properties for a role subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    PersonRoleBuilder(String               qualifiedName,
                      String               name,
                      String               description,
                      String               scope,
                      int                  headCount,
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
        this.scope = scope;
        this.headCount = headCount;
    }


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the role
     * @param name short display name for the role
     * @param description description of the role
     * @param headCount number of individuals that can be appointed to this role
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    PersonRoleBuilder(String               qualifiedName,
                      String               name,
                      String               description,
                      int                  headCount,
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
        this.headCount = headCount;
    }


    /**
     * Relationship constructor
     *
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    PersonRoleBuilder(OMRSRepositoryHelper repositoryHelper,
                      String               serviceName,
                      String               serverName)
    {
        super(OpenMetadataAPIMapper.PERSON_ROLE_TYPE_GUID,
              OpenMetadataAPIMapper.PERSON_ROLE_TYPE_NAME,
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
                                                                  OpenMetadataAPIMapper.NAME_PROPERTY_NAME,
                                                                  name,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                  description,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.SCOPE_PROPERTY_NAME,
                                                                  scope,
                                                                  methodName);
        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataAPIMapper.HEAD_COUNT_PROPERTY_NAME,
                                                               headCount,
                                                               methodName);

        return properties;
    }

    /**
     * Return the bean properties for the TeamLeadership relationship in an InstanceProperties object.
     *
     * @param position description of a special leadership position in the team eg team leader
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getTeamLeadershipProperties(String position,
                                                   String methodName)
    {
        return repositoryHelper.addStringPropertyToInstance(serviceName,
                                                            null,
                                                            OpenMetadataAPIMapper.POSITION_PROPERTY_NAME,
                                                            position,
                                                            methodName);
    }


    /**
     * Return the bean properties for the TeamMembership relationship in an InstanceProperties object.
     *
     * @param position description of a special position in the team eg Milk Monitor
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getTeamMembershipProperties(String position,
                                                   String methodName)
    {
        return repositoryHelper.addStringPropertyToInstance(serviceName,
                                                            null,
                                                            OpenMetadataAPIMapper.POSITION_PROPERTY_NAME,
                                                            position,
                                                            methodName);
    }


    /**
     * Return the bean properties for the PersonRoleAppointment relationship in an InstanceProperties object.
     *
     * @param isPublic is this appointment visible to others
     * @param startDate the official start date of the appointment - null means effective immediately
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getAppointmentProperties(boolean isPublic,
                                                Date    startDate,
                                                String  methodName)
    {
        InstanceProperties properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                                      null,
                                                                                      OpenMetadataAPIMapper.IS_PUBLIC_PROPERTY_NAME,
                                                                                      isPublic,
                                                                                      methodName);

        properties.setEffectiveFromTime(startDate);

        return properties;
    }

}
