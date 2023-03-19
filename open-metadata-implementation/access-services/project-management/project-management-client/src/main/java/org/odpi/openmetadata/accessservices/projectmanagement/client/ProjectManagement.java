/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.projectmanagement.client;

import org.odpi.openmetadata.accessservices.projectmanagement.api.ProjectsInterface;
import org.odpi.openmetadata.accessservices.projectmanagement.client.rest.ProjectManagementRESTClient;
import org.odpi.openmetadata.accessservices.projectmanagement.metadataelements.ActorProfileElement;
import org.odpi.openmetadata.accessservices.projectmanagement.metadataelements.ProjectElement;
import org.odpi.openmetadata.accessservices.projectmanagement.metadataelements.PersonRoleElement;
import org.odpi.openmetadata.accessservices.projectmanagement.properties.ProjectProperties;
import org.odpi.openmetadata.accessservices.projectmanagement.properties.ProjectTeamProperties;
import org.odpi.openmetadata.accessservices.projectmanagement.properties.TemplateProperties;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.ActorProfileListResponse;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.ProjectResponse;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.ProjectListResponse;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.PersonRoleListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * ProjectManagerClient supports the APIs to maintain projects and their related objects.
 */
public class ProjectManagement extends ProjectManagementBaseClient implements ProjectsInterface
{
    private static final String projectURLTemplatePrefix = "/servers/{0}/open-metadata/access-services/project-profile/users/{1}/projects";


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ProjectManagement(String   serverName,
                             String   serverPlatformURLRoot,
                             AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ProjectManagement(String serverName,
                             String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ProjectManagement(String serverName,
                             String serverPlatformURLRoot,
                             String userId,
                             String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ProjectManagement(String   serverName,
                             String   serverPlatformURLRoot,
                             String   userId,
                             String   password,
                             AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ProjectManagement(String                      serverName,
                             String                      serverPlatformURLRoot,
                             ProjectManagementRESTClient restClient,
                             int                         maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }


    /* =====================================================================================================================
     * A Project is the top level object for working with connectors
     */

    /**
     * Create a new metadata element to represent a project.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param projectProperties properties about the project to store
     *
     * @return unique identifier of the new project
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createProject(String              userId,
                                String              externalSourceGUID,
                                String              externalSourceName,
                                ProjectProperties projectProperties) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName                  = "createProject";
        final String propertiesParameterName     = "projectProperties";
        final String urlTemplate = serverPlatformURLRoot + projectURLTemplatePrefix;

        return super.createReferenceable(userId, externalSourceGUID, externalSourceName, projectProperties, propertiesParameterName, urlTemplate, methodName);
    }


    /**
     * Create a new metadata element to represent a project using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new project
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createProjectFromTemplate(String             userId,
                                            String             externalSourceGUID,
                                            String             externalSourceName,
                                            String             templateGUID,
                                            TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName  = "createProjectFromTemplate";
        final String urlTemplate = serverPlatformURLRoot + projectURLTemplatePrefix + "/from-template/{2}";

        return super.createReferenceableFromTemplate(userId, externalSourceGUID, externalSourceName, templateGUID, templateProperties, urlTemplate, methodName);
    }


    /**
     * Update the metadata element representing a project.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param projectGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param projectProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateProject(String              userId,
                              String              externalSourceGUID,
                              String              externalSourceName,
                              String              projectGUID,
                              boolean             isMergeUpdate,
                              ProjectProperties projectProperties) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName                  = "updateProject";
        final String elementGUIDParameterName    = "projectGUID";
        final String propertiesParameterName     = "projectProperties";
        final String urlTemplate = serverPlatformURLRoot + projectURLTemplatePrefix + "/{2}?isMergeUpdate={3}";

        super.updateReferenceable(userId, externalSourceGUID, externalSourceName, projectGUID, elementGUIDParameterName, isMergeUpdate, projectProperties, propertiesParameterName, urlTemplate, methodName);
    }


    /**
     * Create a membership relationship between a project and a person role to show that anyone appointed to the role is a member of the project.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param projectGUID unique identifier of the project in the external data manager
     * @param personRoleGUID unique identifier of the person role in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupProjectManagementRole(String                        userId,
                                           String                        externalSourceGUID,
                                           String                        externalSourceName,
                                           String                        projectGUID,
                                           String                        personRoleGUID) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName                  = "setupProjectRole";
        final String projectGUIDParameterName  = "projectGUID";
        final String personRoleGUIDParameterName = "personRoleGUID";

        final String urlTemplate = serverPlatformURLRoot + projectURLTemplatePrefix + "/{2}/project-management-roles/{3}";

        super.setupRelationship(userId, externalSourceGUID, externalSourceName, projectGUID, projectGUIDParameterName, null, personRoleGUID, personRoleGUIDParameterName, urlTemplate, methodName);
    }


    /**
     * Remove a membership relationship between a project and a person role.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param projectGUID unique identifier of the project in the external data manager
     * @param personRoleGUID unique identifier of the person role in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearProjectManagementRole(String userId,
                                           String externalSourceGUID,
                                           String externalSourceName,
                                           String projectGUID,
                                           String personRoleGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName                  = "clearProjectRole";
        final String projectGUIDParameterName  = "projectGUID";
        final String personRoleGUIDParameterName = "personRoleGUID";
        final String urlTemplate                 = serverPlatformURLRoot + projectURLTemplatePrefix + "/{2}/project-management-roles/{3}/delete";

        super.clearRelationship(userId,
                                externalSourceGUID,
                                externalSourceName,
                                projectGUID,
                                projectGUIDParameterName,
                                personRoleGUID,
                                personRoleGUIDParameterName,
                                urlTemplate,
                                methodName);
    }



    /**
     * Create a project team relationship between a project and a person role to show that anyone appointed to the role is a member of the project.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param projectGUID unique identifier of the project
     * @param properties describes the permissions that the role has in the project
     * @param actorProfileGUID unique identifier of the person role
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupProjectTeam(String                        userId,
                                 String                        externalSourceGUID,
                                 String                        externalSourceName,
                                 String                        projectGUID,
                                 ProjectTeamProperties properties,
                                 String actorProfileGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName                  = "setupProjectRole";
        final String projectGUIDParameterName  = "projectGUID";
        final String personRoleGUIDParameterName = "personRoleGUID";

        final String urlTemplate = serverPlatformURLRoot + projectURLTemplatePrefix + "/{2}/project-teams/{3}";

        super.setupRelationship(userId, externalSourceGUID, externalSourceName, projectGUID, projectGUIDParameterName, properties, actorProfileGUID, personRoleGUIDParameterName, urlTemplate, methodName);
    }


    /**
     * Remove a membership relationship between a project and a person role.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param projectGUID unique identifier of the project in the external data manager
     * @param actorProfileGUID unique identifier of the person role in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearProjectTeam(String userId,
                                 String externalSourceGUID,
                                 String externalSourceName,
                                 String projectGUID,
                                 String actorProfileGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName                  = "clearProjectRole";
        final String projectGUIDParameterName  = "projectGUID";
        final String personRoleGUIDParameterName = "personRoleGUID";
        final String urlTemplate                 = serverPlatformURLRoot + projectURLTemplatePrefix + "/{2}/project-teams/{3}/delete";

        super.clearRelationship(userId,
                                externalSourceGUID,
                                externalSourceName,
                                projectGUID,
                                projectGUIDParameterName,
                                actorProfileGUID,
                                personRoleGUIDParameterName,
                                urlTemplate,
                                methodName);
    }


    /**
     * Remove the metadata element representing a project.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param projectGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeProject(String userId,
                              String externalSourceGUID,
                              String externalSourceName,
                              String projectGUID) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String methodName               = "removeProject";
        final String elementGUIDParameterName = "projectGUID";
        final String urlTemplate              = serverPlatformURLRoot + projectURLTemplatePrefix + "/{2}/delete";

        super.removeReferenceable(userId, externalSourceGUID, externalSourceName, projectGUID, elementGUIDParameterName, urlTemplate, methodName);
    }


    /**
     * Retrieve the list of metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ProjectElement> findProjects(String userId,
                                             String searchString,
                                             int    startFrom,
                                             int    pageSize) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String methodName                = "findProject";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + projectURLTemplatePrefix + "/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        ProjectListResponse restResult = restClient.callProjectListPostRESTCall(methodName,
                                                                                urlTemplate,
                                                                                requestBody,
                                                                                serverName,
                                                                                userId,
                                                                                startFrom,
                                                                                validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ProjectElement> getProjectsByName(String userId,
                                                  String name,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName        = "getProjectByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + projectURLTemplatePrefix + "/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        ProjectListResponse restResult = restClient.callProjectListPostRESTCall(methodName,
                                                                                urlTemplate,
                                                                                requestBody,
                                                                                serverName,
                                                                                userId,
                                                                                startFrom,
                                                                                validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of projects.
     *
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ProjectElement> getProjects(String userId,
                                            int    startFrom,
                                            int    pageSize) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "getProjects";

        invalidParameterHandler.validateUserId(userId, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + projectURLTemplatePrefix + "?startFrom={2}&pageSize={3}";

        ProjectListResponse restResult = restClient.callProjectListGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               startFrom,
                                                                               validatedPageSize);

        return restResult.getElements();
    }



    /**
     * Return information about the person roles linked to a project.
     *
     * @param userId calling user
     * @param projectGUID unique identifier for the project
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<PersonRoleElement> getProjectManagementRoles(String userId,
                                                             String projectGUID,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName        = "getProjectManagementRoles";
        final String guidPropertyName  = "projectGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(projectGUID, guidPropertyName, methodName);

        final String urlTemplate = serverPlatformURLRoot + projectURLTemplatePrefix + "/project-managers/by-project/{2}?startFrom={3}&pageSize={4}";

        PersonRoleListResponse restResult = restClient.callPersonRoleListGetRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     serverName,
                                                                                     userId,
                                                                                     projectGUID,
                                                                                     Integer.toString(startFrom),
                                                                                     Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Return information about the actors linked to a project.
     *
     * @param userId calling user
     * @param projectGUID unique identifier for the project
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ActorProfileElement> getProjectActors(String userId,
                                                      String projectGUID,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName        = "getProjectActors";
        final String guidPropertyName  = "projectGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(projectGUID, guidPropertyName, methodName);

        final String urlTemplate = serverPlatformURLRoot + projectURLTemplatePrefix + "/project-actors/by-project/{2}?startFrom={3}&pageSize={4}";

        ActorProfileListResponse restResult = restClient.callActorProfileListGetRESTCall(methodName,
                                                                                       urlTemplate,
                                                                                       serverName,
                                                                                       userId,
                                                                                       projectGUID,
                                                                                       Integer.toString(startFrom),
                                                                                       Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Retrieve the metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param projectGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public ProjectElement getProjectByGUID(String userId,
                                           String projectGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "getProjectByGUID";
        final String guidParameterName = "projectGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(projectGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + projectURLTemplatePrefix + "/{2}";

        ProjectResponse restResult = restClient.callProjectGetRESTCall(methodName,
                                                                       urlTemplate,
                                                                       serverName,
                                                                       userId,
                                                                       projectGUID);

        return restResult.getElement();
    }
}
