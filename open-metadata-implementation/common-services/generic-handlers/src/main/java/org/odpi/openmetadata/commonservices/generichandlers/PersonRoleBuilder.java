/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
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
    private String  identifier          = null;
    private String  name                = null;
    private String  description         = null;
    private String  scope               = null;
    private int     headCount           = 1;
    private boolean headCountLimitSet   = false;
    private int     domainIdentifier    = 0; /* only supported on roles that inherit from GovernanceRole. */
    private boolean domainIdentifierSet = false;


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the role
     * @param identifier unique identifier for the role - typically from external system
     * @param name short display name for the role
     * @param description description of the role
     * @param scope the scope of the role
     * @param headCount number of individuals that can be appointed to this role
     * @param headCountLimitSet should the headcount attribute be set in the role?
     * @param domainIdentifier governance domain identifier
     * @param domainIdentifierSet should the domainIdentifier attribute be set in the role?
     * @param additionalProperties additional properties for a role
     * @param typeGUID unique identifier of this element's type
     * @param typeName unique name of this element's type
     * @param extendedProperties  properties for a role subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    PersonRoleBuilder(String               qualifiedName,
                      String               identifier,
                      String               name,
                      String               description,
                      String               scope,
                      int                  headCount,
                      boolean              headCountLimitSet,
                      int                  domainIdentifier,
                      boolean              domainIdentifierSet,
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
        this.name = name;
        this.description = description;
        this.scope = scope;
        this.headCount = headCount;
        this.headCountLimitSet = headCountLimitSet;
        this.domainIdentifier = domainIdentifier;
        this.domainIdentifierSet = domainIdentifierSet;
    }


    /**
     * Create constructor for templated roles.
     *
     * @param qualifiedName unique name for the role
     * @param identifier unique identifier for the role - typically from external system
     * @param name short display name for the role
     * @param description description of the role
     * @param headCount number of individuals that can be appointed to this role
     * @param headCountLimitSet should the headcount attribute be set in the role?
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    PersonRoleBuilder(String               qualifiedName,
                      String               identifier,
                      String               name,
                      String               description,
                      int                  headCount,
                      boolean              headCountLimitSet,
                      OMRSRepositoryHelper repositoryHelper,
                      String               serviceName,
                      String               serverName)
    {
        super(qualifiedName,
              repositoryHelper,
              serviceName,
              serverName);

        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.headCount = headCount;
        this.headCountLimitSet = headCountLimitSet;
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
        super(OpenMetadataType.PERSON_ROLE.typeGUID,
              OpenMetadataType.PERSON_ROLE.typeName,
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
                                                                  OpenMetadataProperty.IDENTIFIER.name,
                                                                  identifier,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.NAME.name,
                                                                  name,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.SCOPE.name,
                                                                  scope,
                                                                  methodName);

        if (headCountLimitSet)
        {
            properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataProperty.HEAD_COUNT.name,
                                                                   headCount,
                                                                   methodName);
        }

        if (domainIdentifierSet)
        {
            /*
             * The domain identifier may have been set in the extended properties which are populated by the super class.
             * This ensures the value fromm extended properties is not overridden.
             */
            if ((properties == null) || (properties.getPropertyValue(OpenMetadataProperty.DOMAIN_IDENTIFIER.name) == null))
            {
                properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataProperty.DOMAIN_IDENTIFIER.name,
                                                                       domainIdentifier,
                                                                       methodName);
            }
        }

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
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataProperty.POSITION_NAME.name,
                                                                                     position,
                                                                                     methodName);

        setEffectivityDates(properties);

        return properties;
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
        InstanceProperties properties =  repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                      null,
                                                                                      OpenMetadataProperty.POSITION_NAME.name,
                                                                                      position,
                                                                                      methodName);

        setEffectivityDates(properties);

        return properties;
    }


    /**
     * Return the bean properties for the PersonRoleAppointment relationship in an InstanceProperties object.
     *
     * @param isPublic is this appointment visible to others
     * @param effectiveFrom the official start date of the appointment - null means effective immediately
     * @param effectiveTo the official end date of the appointment - null means unknown
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getAppointmentProperties(boolean isPublic,
                                                Date    effectiveFrom,
                                                Date    effectiveTo,
                                                String  methodName)
    {
        InstanceProperties properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                                      null,
                                                                                      OpenMetadataProperty.IS_PUBLIC.name,
                                                                                      isPublic,
                                                                                      methodName);

        return this.setEffectivityDates(properties, effectiveFrom, effectiveTo);
    }

}
