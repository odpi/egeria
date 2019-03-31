/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.builders;

import org.odpi.openmetadata.accessservices.communityprofile.mappers.PersonalProfileMapper;
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
public class PersonalProfileBuilder
{
    private static final Logger log = LoggerFactory.getLogger(PersonalProfileBuilder.class);

    private OMRSRepositoryHelper    repositoryHelper;
    private String                  serviceName;


    /**
     * Simple constructor.
     *
     * @param repositoryHelper helper class for formatting instances
     * @param serviceName name of this service
     */
    public PersonalProfileBuilder(OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName)
    {
        this.repositoryHelper = repositoryHelper;
        this.serviceName = serviceName;
    }


    /**
     * Create an instance properties object for a Person entity for an individual.
     *
     * @param qualifiedName personnel/serial/unique employee number of the individual.
     * @param fullName full name of the person.
     * @param name known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param description job description of the individual.
     * @param additionalProperties  additional properties about the individual.
     *
     * @return InstanceProperties object
     */
    private InstanceProperties getPersonEntityProperties(String              qualifiedName,
                                                         String              name,
                                                         String              fullName,
                                                         String              jobTitle,
                                                         String              description,
                                                         Map<String, String> additionalProperties)
    {
        final String  methodName = "getPersonEntityProperties";

        InstanceProperties properties = null;

        if (qualifiedName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      PersonalProfileMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                      qualifiedName,
                                                                      methodName);
        }

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


        if ((additionalProperties != null) && (! additionalProperties.isEmpty()))
        {
            properties = repositoryHelper.addMapPropertyToInstance(serviceName,
                                                                   properties,
                                                                   PersonalProfileMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                   additionalProperties,
                                                                   methodName);
        }

        log.debug("Instance properties: " + properties.toString());

        return properties;
    }


}

