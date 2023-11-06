/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.myprofile.server;


import org.odpi.openmetadata.accessservices.communityprofile.client.OrganizationManagement;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.ActorProfileElement;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ActorProfileProperties;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.myprofile.metadataelements.PersonalProfileUniverse;
import org.odpi.openmetadata.viewservices.myprofile.properties.PersonalProfileProperties;
import org.odpi.openmetadata.viewservices.myprofile.rest.PersonalProfileResponse;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * The MyProfileRESTServices provides the server-side implementation of the My Profile Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class MyProfileRESTServices extends TokenController
{
    private static final MyProfileInstanceHandler instanceHandler = new MyProfileInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(MyProfileRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public MyProfileRESTServices()
    {
    }


    /**
     * Return the profile for this user.
     *
     * @param serverName name of the server instances for this request
     *
     * @return profile response object or or null or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public PersonalProfileResponse getMyProfile(String serverName)
    {
        final String methodName = "getMyProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        PersonalProfileResponse response = new PersonalProfileResponse();
        AuditLog                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            token.setUserId(userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OrganizationManagement client = instanceHandler.getOrganizationManagementClient(userId, serverName, methodName);

            ActorProfileElement actorProfileElement = client.getActorProfileByUserId(userId, userId);

            if (actorProfileElement != null)
            {
                PersonalProfileUniverse personalProfileUniverse = new PersonalProfileUniverse();
                PersonalProfileProperties profileProperties = new PersonalProfileProperties();
                ActorProfileProperties actorProfileProperties = actorProfileElement.getProfileProperties();
                Map<String, Object> extendedProperties = actorProfileProperties.getExtendedProperties();

                profileProperties.setQualifiedName(actorProfileProperties.getQualifiedName());
                profileProperties.setKnownName(actorProfileProperties.getKnownName());
                profileProperties.setDescription(actorProfileProperties.getDescription());
                profileProperties.setPronouns(getExtendedProperty(extendedProperties,"pronouns"));
                profileProperties.setTitle(getExtendedProperty(extendedProperties,"title"));
                profileProperties.setInitials(getExtendedProperty(extendedProperties,"initials"));
                profileProperties.setGivenNames(getExtendedProperty(extendedProperties,"givenNames"));
                profileProperties.setSurname(getExtendedProperty(extendedProperties,"surname"));
                profileProperties.setFullName(getExtendedProperty(extendedProperties,"fullName"));
                profileProperties.setPreferredLanguage(getExtendedProperty(extendedProperties,"preferredLanguage"));
                profileProperties.setJobTitle(getExtendedProperty(extendedProperties,"jobTitle"));
                profileProperties.setEmployeeNumber(getExtendedProperty(extendedProperties,"employeeNumber"));
                profileProperties.setEmployeeType(getExtendedProperty(extendedProperties,"employeeType"));
                profileProperties.setIsPublic(getExtendedBooleanProperty(extendedProperties,"isPublic"));
                profileProperties.setAdditionalProperties(actorProfileProperties.getAdditionalProperties());
                profileProperties.setEffectiveFrom(actorProfileProperties.getEffectiveFrom());
                profileProperties.setEffectiveTo(actorProfileProperties.getEffectiveTo());

                personalProfileUniverse.setElementHeader(actorProfileElement.getElementHeader());
                personalProfileUniverse.setProfileProperties(profileProperties);
                personalProfileUniverse.setContributionRecord(actorProfileElement.getContributionRecord());
                personalProfileUniverse.setContactMethods(actorProfileElement.getContactMethods());
                personalProfileUniverse.setPeers(actorProfileElement.getPeers());
                personalProfileUniverse.setRoles(actorProfileElement.getPersonRoles());
                personalProfileUniverse.setUserIdentities(actorProfileElement.getUserIdentities());

                response.setPersonalProfile(personalProfileUniverse);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Extract the named property from the extended properties.
     *
     * @param extendedProperties extended properties from the repositories
     * @param propertyName name of property
     * @return property
     */
    private String getExtendedProperty(Map<String, Object> extendedProperties,
                                       String              propertyName)
    {
        if (extendedProperties != null)
        {
            Object propertyValue = extendedProperties.get(propertyName);

            if (propertyValue != null)
            {
                return propertyValue.toString();
            }
        }

        return null;
    }


    /**
     * Extract the named property from the extended properties.
     *
     * @param extendedProperties extended properties from the repositories
     * @param propertyName name of property
     * @return property
     */
    private boolean getExtendedBooleanProperty(Map<String, Object> extendedProperties,
                                               String              propertyName)
    {
        if (extendedProperties != null)
        {
            Object propertyValue = extendedProperties.get(propertyName);

            if (propertyValue instanceof Boolean booleanValue)
            {
                return booleanValue;
            }
        }

        return true;
    }
}
