/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.builders;

import org.odpi.openmetadata.accessservices.communityprofile.mappers.ContributionRecordMapper;
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
 * ContributionRecordBuilder creates repository entities and relationships from properties for a person's contribution.
 */
public class ContributionRecordBuilder extends ReferenceableBuilder
{
    private static final Logger log = LoggerFactory.getLogger(ContributionRecordBuilder.class);

    /**
     * Simple constructor.
     *
     * @param qualifiedName unique name of this object
     * @param repositoryHelper helper class for formatting instances
     * @param serviceName name of this service
     * @param serverName name of this server
     */
    public ContributionRecordBuilder(String               qualifiedName,
                                     OMRSRepositoryHelper repositoryHelper,
                                     String               serviceName,
                                     String               serverName)
    {
        super(qualifiedName, repositoryHelper, serviceName, serverName);


    }


    /**
     * Create an instance properties object for a Person entity for an individual.
     *
     * @param karmaPoints number of karma points awarded so far to the individual.
     * @param extendedProperties  properties about the individual for a new type that is the subclass of Person.
     * @param additionalProperties  additional properties about the individual.
     *
     * @return InstanceProperties object
     * @throws InvalidParameterException bad property
     */
    public  InstanceProperties getContributionRecordProperties(int                 karmaPoints,
                                                               Map<String, Object> extendedProperties,
                                                               Map<String, String> additionalProperties) throws InvalidParameterException
    {
        final String  methodName = "getContributionRecordProperties";

        InstanceProperties properties = super.getQualifiedNameInstanceProperties(methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               ContributionRecordMapper.KARMA_POINTS_PROPERTY_NAME,
                                                               karmaPoints,
                                                               methodName);



        if (extendedProperties != null)
        {
            try
            {
                properties = repositoryHelper.addPropertyMapToInstance(serviceName,
                                                                       null,
                                                                       extendedProperties,
                                                                       methodName);
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException error)
            {
                final String  propertyName = "extendedProperties";

                errorHandler.handleUnsupportedProperty(error, methodName, propertyName);
            }
        }


        if ((additionalProperties != null) && (! additionalProperties.isEmpty()))
        {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         PersonalProfileMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                         additionalProperties,
                                                                         methodName);
        }

        log.debug("Instance properties: " + properties.toString());

        return properties;
    }
}

