/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.builders;

import org.odpi.openmetadata.accessservices.communityprofile.mappers.PersonalProfileMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.ReferenceableBuilder;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * PersonalProfileBuilder creates repository entities and relationships from properties for a personal
 * profile.  Specifically, a single PersonalProfile is represented as a Person entity with an optional ContributionRecord
 * entity linked to the Person using a PersonalContribution relationship.  Each Person entity must be linked to
 * at least one UserIdentity via a ProfileIdentity relationship.  In addition, Person is optionally linked to
 * multiple ContactDetails entities via the ContactThrough relationship.
 */
public class PersonalProfileBuilder extends ReferenceableBuilder
{
    private static final Logger log = LoggerFactory.getLogger(PersonalProfileBuilder.class);

    /**
     * Simple constructor.
     *
     * @param qualifiedName unique name of this object
     * @param repositoryHelper helper class for formatting instances
     * @param serviceName name of this service
     * @param serverName name of this server
     */
    public PersonalProfileBuilder(String               qualifiedName,
                                  OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName,
                                  String               serverName)
    {
        super(qualifiedName, repositoryHelper, serviceName, serverName);

        this.repositoryHelper = repositoryHelper;
        this.serviceName = serviceName;

        this.errorHandler = new RepositoryErrorHandler(repositoryHelper, serviceName, serverName);
    }


    /**
     * Create/update constructor.
     *
     * @param qualifiedName unique name of this object
     * @param additionalProperties additional properties
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper class for formatting instances
     * @param serviceName name of this service
     * @param serverName name of this server
     */
    public PersonalProfileBuilder(String               qualifiedName,
                                  Map<String, String>  additionalProperties,
                                  Map<String, Object>  extendedProperties,
                                  OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName,
                                  String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              PersonalProfileMapper.PERSONAL_PROFILE_TYPE_NAME,
              PersonalProfileMapper.PERSONAL_PROFILE_TYPE_GUID,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.repositoryHelper = repositoryHelper;
        this.serviceName = serviceName;

        this.errorHandler = new RepositoryErrorHandler(repositoryHelper, serviceName, serverName);
    }


    /**
     * Create an instance properties object for a Person entity for an individual.
     *
     * @param fullName full name of the person.
     * @param name known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param description job description of the individual.
     *
     * @return InstanceProperties object
     * @throws InvalidParameterException bad property
     */
    public  InstanceProperties getPersonEntityProperties(String              name,
                                                         String              fullName,
                                                         String              jobTitle,
                                                         String              description) throws InvalidParameterException
    {
        final String  methodName = "getPersonEntityProperties";

        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (fullName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      PersonalProfileMapper.FULL_NAME_PROPERTY_NAME,
                                                                      fullName,
                                                                      methodName);
        }

        if (name != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      PersonalProfileMapper.NAME_PROPERTY_NAME,
                                                                      name,
                                                                      methodName);
        }

        if (jobTitle != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      PersonalProfileMapper.JOB_TITLE_PROPERTY_NAME,
                                                                      jobTitle,
                                                                      methodName);
        }


        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      PersonalProfileMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }


        log.debug("Instance properties: " + properties.toString());

        return properties;
    }
}

