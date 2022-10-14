/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.projectmanagement.server;

import org.odpi.openmetadata.accessservices.projectmanagement.metadataelements.ActorProfileElement;
import org.odpi.openmetadata.accessservices.projectmanagement.metadataelements.PersonRoleElement;
import org.odpi.openmetadata.accessservices.projectmanagement.metadataelements.ProjectElement;
import org.odpi.openmetadata.accessservices.projectmanagement.metadataelements.RelatedElement;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.ActorProfileHandler;
import org.odpi.openmetadata.commonservices.generichandlers.CommunityHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ContributionRecordHandler;
import org.odpi.openmetadata.commonservices.generichandlers.PersonRoleHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ProjectHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SoftwareCapabilityHandler;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;


/**
 * ProjectManagementInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the ProjectManagementAdmin class.
 */
class ProjectManagementInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    ProjectManagementInstanceHandler()
    {
        super(AccessServiceDescription.PROJECT_MANAGEMENT_OMAS.getAccessServiceFullName());

        ProjectManagementRegistration.registerAccessService();
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    ReferenceableHandler<RelatedElement> getRelatedElementHandler(String userId,
                                                                  String serverName,
                                                                  String serviceOperationName) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        ProjectManagementServicesInstance instance = (ProjectManagementServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                       serverName,
                                                                                                                       serviceOperationName);

        if (instance != null)
        {
            return instance.getRelatedElementHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    ActorProfileHandler<ActorProfileElement> getActorProfileHandler(String userId,
                                                                    String serverName,
                                                                    String serviceOperationName) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        ProjectManagementServicesInstance instance = (ProjectManagementServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                       serverName,
                                                                                                                       serviceOperationName);

        if (instance != null)
        {
            return instance.getActorProfileHandler();
        }

        return null;
    }



    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    PersonRoleHandler<PersonRoleElement> getPersonRoleHandler(String userId,
                                                              String serverName,
                                                              String serviceOperationName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        ProjectManagementServicesInstance instance = (ProjectManagementServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                       serverName,
                                                                                                                       serviceOperationName);

        if (instance != null)
        {
            return instance.getPersonRoleHandler();
        }

        return null;
    }



    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    ProjectHandler<ProjectElement> getProjectHandler(String userId,
                                                     String serverName,
                                                     String serviceOperationName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        ProjectManagementServicesInstance instance = (ProjectManagementServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                       serverName,
                                                                                                                       serviceOperationName);

        if (instance != null)
        {
            return instance.getProjectHandler();
        }

        return null;
    }


}
