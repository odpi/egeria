/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.handlers;

import org.odpi.openmetadata.accessservices.governanceprogram.properties.PersonalProfile;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * PersonalProfileHandler manages Person objects from the property server.  It runs server-side in the GovernanceProgram
 * OMAS and retrieves entities and relationships through the OMRSRepositoryConnector.
 */
public class PersonalProfileHandler
{
    private static final String personalDetailsTypeGUID     = "ac406bf8-e53e-49f1-9088-2af28bbbd285";
    private static final String personalDetailsTypeName     = "Person";
    private static final String employeeNumberPropertyName  = "qualifiedName";
    private static final String fullNamePropertyName        = "fullName";
    private static final String knownNamePropertyName       = "name";
    private static final String jobTitlePropertyName        = "jobTitle";
    private static final String jobDescriptionPropertyName  = "description";
    private static final String additionalPropertiesName    = "additionalProperties";

    private static final String profileGUIDParameterName    = "profileGUID";
    private static final String employeeNumberParameterName = "employeeNumber";
    private static final String knownNameParameterName      = "knownName";

    private static final Logger log = LoggerFactory.getLogger(PersonalProfileHandler.class);

    private String                  serviceName;
    private String                  serverName;
    private RepositoryHandler       repositoryHandler;
    private OMRSRepositoryHelper    repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;


    /**
     * Construct the handler caching the objects
     * needed to operate within a single server instance.
     *
     * @param serviceName name of the consuming service
     * @param serverName name of this server instance
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper helper used by the converters
     * @param repositoryHandler handler for calling the repository services
     */
    public PersonalProfileHandler(String                  serviceName,
                                  String                  serverName,
                                  InvalidParameterHandler invalidParameterHandler,
                                  OMRSRepositoryHelper    repositoryHelper,
                                  RepositoryHandler repositoryHandler)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
    }


    /**
     * Retrieve a personal profile by guid.
     *
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @return personal profile object.
     * @throws InvalidParameterException the unique identifier of the personal profile is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    PersonalProfile getPersonalProfileByGUID(String        userId,
                                             String        profileGUID) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String   methodName = "getPersonalProfileByGUID";
        final String   guidParameter = "profileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(profileGUID, guidParameter, methodName);

        PersonalProfile personalProfile = null;

        EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                profileGUID,
                                                                guidParameter,
                                                                personalDetailsTypeName,
                                                                methodName);
        if (entity != null)
        {
            personalProfile = new PersonalProfile();

            personalProfile.setGUID(entity.getGUID());

            InstanceType   instanceType = entity.getType();
            if (instanceType != null)
            {
                personalProfile.setType(instanceType.getTypeDefName());
            }
            else
            {
                personalProfile.setType(personalDetailsTypeName);
            }

            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                personalProfile.setEmployeeNumber(repositoryHelper.getStringProperty(serviceName, employeeNumberPropertyName, instanceProperties, methodName));
                personalProfile.setFullName(repositoryHelper.getStringProperty(serviceName, fullNamePropertyName, instanceProperties, methodName));
                personalProfile.setKnownName(repositoryHelper.getStringProperty(serviceName, knownNamePropertyName, instanceProperties, methodName));
                personalProfile.setJobTitle(repositoryHelper.getStringProperty(serviceName, jobTitlePropertyName, instanceProperties, methodName));
                personalProfile.setJobRoleDescription(repositoryHelper.getStringProperty(serviceName, jobDescriptionPropertyName, instanceProperties, methodName));
                personalProfile.setAdditionalProperties(repositoryHelper.getStringMapFromProperty(serviceName, additionalPropertiesName, instanceProperties, methodName));
            }

            log.debug("Personal details: " + personalProfile.toString());
        }
        else
        {
            log.debug("Personal details: <null>");
        }

        return personalProfile;
    }
}
