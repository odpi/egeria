/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.projectmanagement.client;

import org.odpi.openmetadata.accessservices.projectmanagement.api.ProjectsInterface;
import org.odpi.openmetadata.frameworks.governanceaction.converters.ProjectConverter;
import org.odpi.openmetadata.frameworks.governanceaction.converters.TeamMemberConverter;
import org.odpi.openmetadata.accessservices.projectmanagement.client.rest.ProjectManagementRESTClient;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ActorProfileElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.PersonRoleElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ProjectElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ProjectTeamMember;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectTeamProperties;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.*;

import java.util.*;

/**
 * ProjectManagerClient supports the APIs to maintain projects and their related objects.
 */
public class ProjectManagement extends ProjectManagementBaseClient implements ProjectsInterface
{
    private static final String projectURLTemplatePrefix = "/servers/{0}/open-metadata/access-services/project-management/users/{1}/projects";


    final private ProjectConverter<ProjectElement>       projectConverter;
    final private Class<ProjectElement>                  projectBeanClass       = ProjectElement.class;
    final private TeamMemberConverter<ProjectTeamMember> teamMemberConverter;

    final private Class<ProjectTeamMember> projectMemberBeanClass = ProjectTeamMember.class;

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize maximum value allowed for page size
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ProjectManagement(String   serverName,
                             String   serverPlatformURLRoot,
                             int      maxPageSize,
                             AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize, auditLog);

        projectConverter = new ProjectConverter<>(propertyHelper,
                                                  AccessServiceDescription.PROJECT_MANAGEMENT_OMAS.getAccessServiceName(),
                                                  serverName);

        teamMemberConverter = new TeamMemberConverter<>(propertyHelper,
                                                        AccessServiceDescription.PROJECT_MANAGEMENT_OMAS.getAccessServiceName(),
                                                        serverName);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ProjectManagement(String serverName,
                             String serverPlatformURLRoot,
                             int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize);

        projectConverter = new ProjectConverter<>(propertyHelper,
                                                  AccessServiceDescription.PROJECT_MANAGEMENT_OMAS.getAccessServiceName(),
                                                  serverName);

        teamMemberConverter = new TeamMemberConverter<>(propertyHelper,
                                                        AccessServiceDescription.PROJECT_MANAGEMENT_OMAS.getAccessServiceName(),
                                                        serverName);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ProjectManagement(String serverName,
                             String serverPlatformURLRoot,
                             String userId,
                             String password,
                             int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize);

        projectConverter = new ProjectConverter<>(propertyHelper,
                                                  AccessServiceDescription.PROJECT_MANAGEMENT_OMAS.getAccessServiceName(),
                                                  serverName);

        teamMemberConverter = new TeamMemberConverter<>(propertyHelper,
                                                        AccessServiceDescription.PROJECT_MANAGEMENT_OMAS.getAccessServiceName(),
                                                        serverName);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param maxPageSize maximum value allowed for page size
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ProjectManagement(String   serverName,
                             String   serverPlatformURLRoot,
                             String   userId,
                             String   password,
                             int      maxPageSize,
                             AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize, auditLog);

        projectConverter = new ProjectConverter<>(propertyHelper,
                                                  AccessServiceDescription.PROJECT_MANAGEMENT_OMAS.getAccessServiceName(),
                                                  serverName);

        teamMemberConverter = new TeamMemberConverter<>(propertyHelper,
                                                        AccessServiceDescription.PROJECT_MANAGEMENT_OMAS.getAccessServiceName(),
                                                        serverName);
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

        projectConverter = new ProjectConverter<>(propertyHelper,
                                                  AccessServiceDescription.PROJECT_MANAGEMENT_OMAS.getAccessServiceName(),
                                                  serverName);

        teamMemberConverter = new TeamMemberConverter<>(propertyHelper,
                                                        AccessServiceDescription.PROJECT_MANAGEMENT_OMAS.getAccessServiceName(),
                                                        serverName);
    }


    /* =====================================================================================================================
     * A Project describes a targeted set of activities
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
    public String createProject(String            userId,
                                String            externalSourceGUID,
                                String            externalSourceName,
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
     * Create a new generic project.
     *
     * @param userId                 userId of user making request.
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param optionalClassification classification of the projects - eg Campaign, Task or PersonalProject
     * @param properties             properties for the project.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     *
     * @return unique identifier of the newly created Project
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public String createProject(String               userId,
                                String               anchorGUID,
                                boolean              isOwnAnchor,
                                String               optionalClassification,
                                ProjectProperties    properties,
                                String               parentGUID,
                                String               parentRelationshipTypeName,
                                ElementProperties    parentRelationshipProperties,
                                boolean              parentAtEnd1) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "createProject";
        final String projectPropertiesName = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, projectPropertiesName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String projectTypeName = OpenMetadataType.PROJECT.typeName;

        if (properties.getTypeName() != null)
        {
            projectTypeName = properties.getTypeName();
        }

        Map<String, ElementProperties> initialClassifications = null;

        if (optionalClassification != null)
        {
            initialClassifications = new HashMap<>();

            initialClassifications.put(optionalClassification, null);
        }

        return openMetadataStoreClient.createMetadataElementInStore(userId,
                                                                    null,
                                                                    null,
                                                                    projectTypeName,
                                                                    ElementStatus.ACTIVE,
                                                                    initialClassifications,
                                                                    anchorGUID,
                                                                    isOwnAnchor,
                                                                    properties.getEffectiveFrom(),
                                                                    properties.getEffectiveTo(),
                                                                    this.getElementProperties(properties),
                                                                    parentGUID,
                                                                    parentRelationshipTypeName,
                                                                    parentRelationshipProperties,
                                                                    parentAtEnd1);
    }


    /**
     * Convert the project properties into a set of element properties for the open metadata client.
     *
     * @param projectProperties supplied project properties
     * @return element properties
     */
    private ElementProperties getElementProperties(ProjectProperties projectProperties)
    {
        if (projectProperties != null)
        {
            ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                                   OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                   projectProperties.getQualifiedName());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.NAME.name,
                                                                 projectProperties.getName());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.DESCRIPTION.name,
                                                                 projectProperties.getDescription());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.IDENTIFIER.name,
                                                                 projectProperties.getIdentifier());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.PROJECT_PHASE.name,
                                                                 projectProperties.getProjectStatus());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.PROJECT_HEALTH.name,
                                                                 projectProperties.getProjectStatus());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.PROJECT_STATUS.name,
                                                                 projectProperties.getProjectStatus());

            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                               OpenMetadataProperty.START_DATE.name,
                                                               projectProperties.getStartDate());

            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                               OpenMetadataProperty.PLANNED_END_DATE.name,
                                                               projectProperties.getPlannedEndDate());

            elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                    OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                    projectProperties.getAdditionalProperties());

            elementProperties = propertyHelper.addPropertyMap(elementProperties,
                                                              projectProperties.getExtendedProperties());

            return elementProperties;
        }

        return null;
    }


    /**
     * Create a new metadata element to represent a project using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new project.
     *
     * @param userId             calling user
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createProjectFromTemplate(String                         userId,
                                            String                         anchorGUID,
                                            boolean                        isOwnAnchor,
                                            Date                           effectiveFrom,
                                            Date                           effectiveTo,
                                            String                         templateGUID,
                                            ElementProperties              replacementProperties,
                                            Map<String, String>            placeholderProperties,
                                            String                         parentGUID,
                                            String                         parentRelationshipTypeName,
                                            ElementProperties              parentRelationshipProperties,
                                            boolean                        parentAtEnd1) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        return openMetadataStoreClient.createMetadataElementFromTemplate(userId,
                                                                         OpenMetadataType.PROJECT.typeName,
                                                                         anchorGUID,
                                                                         isOwnAnchor,
                                                                         effectiveFrom,
                                                                         effectiveTo,
                                                                         templateGUID,
                                                                         replacementProperties,
                                                                         placeholderProperties,
                                                                         parentGUID,
                                                                         parentRelationshipTypeName,
                                                                         parentRelationshipProperties,
                                                                         parentAtEnd1);
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
    public void updateProject(String            userId,
                              String            externalSourceGUID,
                              String            externalSourceName,
                              String            projectGUID,
                              boolean           isMergeUpdate,
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
     * Create a project management relationship between a project and a person role to show that someone has been appointed to the project management role.
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
    public void setupProjectManagementRole(String userId,
                                           String externalSourceGUID,
                                           String externalSourceName,
                                           String projectGUID,
                                           String personRoleGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName                  = "setupProjectRole";
        final String projectGUIDParameterName    = "projectGUID";
        final String personRoleGUIDParameterName = "personRoleGUID";

        final String urlTemplate = serverPlatformURLRoot + projectURLTemplatePrefix + "/{2}/project-management-roles/{3}";

        super.setupRelationship(userId, externalSourceGUID, externalSourceName, projectGUID, projectGUIDParameterName, null, personRoleGUID, personRoleGUIDParameterName, urlTemplate, methodName);
    }


    /**
     * Remove a project management relationship between a project and a person role.
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
        final String projectGUIDParameterName    = "projectGUID";
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
     * Create a ProjectTeam relationship between a project and an actor to show that they are member of the project.
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
    public void setupProjectTeam(String                userId,
                                 String                externalSourceGUID,
                                 String                externalSourceName,
                                 String                projectGUID,
                                 ProjectTeamProperties properties,
                                 String                actorProfileGUID) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName                  = "setupProjectTeam";
        final String projectGUIDParameterName    = "projectGUID";
        final String personRoleGUIDParameterName = "actorProfileGUID";

        final String urlTemplate = serverPlatformURLRoot + projectURLTemplatePrefix + "/{2}/project-teams/{3}";

        super.setupRelationship(userId, externalSourceGUID, externalSourceName, projectGUID, projectGUIDParameterName, properties, actorProfileGUID, personRoleGUIDParameterName, urlTemplate, methodName);
    }


    /**
     * Remove a ProjectTeam relationship between a project and an actor.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param projectGUID unique identifier of the project in the external data manager
     * @param actorGUID unique identifier of the person role in the external data manager
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
                                 String actorGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        final String methodName                  = "clearProjectTeam";
        final String projectGUIDParameterName    = "projectGUID";
        final String personRoleGUIDParameterName = "actorGUID";
        final String urlTemplate                 = serverPlatformURLRoot + projectURLTemplatePrefix + "/{2}/project-teams/{3}/delete";

        super.clearRelationship(userId,
                                externalSourceGUID,
                                externalSourceName,
                                projectGUID,
                                projectGUIDParameterName,
                                actorGUID,
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
     * Returns the list of projects that are linked off of the supplied element.
     *
     * @param userId         userId of user making request
     * @param parentGUID     unique identifier of referenceable object (typically a personal profile, project or
     *                       community) that the projects hang off of
     * @param projectStatus filter response by project type - if null, any value will do
     * @param startFrom      index of the list to start from (0 for start)
     * @param pageSize       maximum number of elements to return
     *
     * @return a list of projects
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public List<ProjectElement> getLinkedProjects(String userId,
                                                  String parentGUID,
                                                  String projectStatus,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "getLinkedProjects";
        final String parentGUIDParameterName = "parentGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentGUID, parentGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        RelatedMetadataElementList linkedProjects = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                       parentGUID,
                                                                                                       1,
                                                                                                       null,
                                                                                                       null,
                                                                                                       null,
                                                                                                       null,
                                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                                       false,
                                                                                                       false,
                                                                                                       new Date(),
                                                                                                       startFrom,
                                                                                                       pageSize);

        if ((linkedProjects != null) && (linkedProjects.getElementList() != null))
        {
            List<ProjectElement> filteredProjects = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : linkedProjects.getElementList())
            {
                if (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), OpenMetadataType.PROJECT.typeName))
                {
                    ProjectElement projectElement = projectConverter.getNewBean(projectBeanClass, relatedMetadataElement, methodName);

                    if ((projectStatus == null) || (projectStatus.isBlank()) || (projectStatus.equals(projectElement.getProperties().getProjectStatus())))
                    {
                        filteredProjects.add(projectElement);
                    }
                }
            }

            if (! filteredProjects.isEmpty())
            {
                return filteredProjects;
            }
        }

        return null;
    }


    /**
     * Returns the list of projects with a particular classification.
     *
     * @param userId             userId of user making request
     * @param classificationName name of the classification - if null, all projects are returned
     * @param startFrom          index of the list to start from (0 for start)
     * @param pageSize           maximum number of elements to return
     *
     * @return a list of projects
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public List<ProjectElement> getClassifiedProjects(String userId,
                                                      String classificationName,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String methodName = "getClassifiedProjects";
        final String parameterName = "classificationName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);
        invalidParameterHandler.validateName(classificationName, parameterName, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.getMetadataElementsByClassification(userId,
                                                                                                                     OpenMetadataType.PROJECT.typeName,
                                                                                                                     null,
                                                                                                                     classificationName,
                                                                                                                     null,
                                                                                                                     null,
                                                                                                                     OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                                     SequencingOrder.PROPERTY_ASCENDING,
                                                                                                                     false,
                                                                                                                     false,
                                                                                                                     new Date(),
                                                                                                                     startFrom,
                                                                                                                     pageSize);
        return convertProjects(openMetadataElements);
    }


    /**
     * Return a list of actors that are members of a project.
     *
     * @param userId             userId of user making request
     * @param projectGUID unique identifier of the project
     * @param teamRole optional team role
     * @param startFrom      index of the list to start from (0 for start)
     * @param pageSize       maximum number of elements to return.
     *
     * @return list of team members
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public List<ProjectTeamMember> getProjectMembers(String userId,
                                                     String projectGUID,
                                                     String teamRole,
                                                     int    startFrom,
                                                     int    pageSize) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName = "getProjectMembers";
        final String parentGUIDParameterName = "projectGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(projectGUID, parentGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        RelatedMetadataElementList linkedActors = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                       projectGUID,
                                                                                                       1,
                                                                                                       null,
                                                                                                       null,
                                                                                                       null,
                                                                                                       null,
                                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                                       false,
                                                                                                       false,
                                                                                                       new Date(),
                                                                                                       startFrom,
                                                                                                       pageSize);

        if ((linkedActors != null) && (linkedActors.getElementList() != null))
        {
            List<ProjectTeamMember> teamMembers = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : linkedActors.getElementList())
            {
                if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.PROJECT_MANAGEMENT_RELATIONSHIP.typeName))
                {
                    if ((teamRole == null) || (teamRole.isBlank()) || (OpenMetadataType.PROJECT_MANAGEMENT_RELATIONSHIP.typeName.equals(teamRole)))
                    {
                        ProjectTeamMember projectTeamMember = teamMemberConverter.getNewBean(projectMemberBeanClass, relatedMetadataElement, methodName);

                        ProjectTeamProperties projectTeamProperties = new ProjectTeamProperties();

                        projectTeamProperties.setTeamRole(OpenMetadataType.PROJECT_MANAGEMENT_RELATIONSHIP.typeName);

                        projectTeamMember.setProjectTeamProperties(projectTeamProperties);

                        teamMembers.add(projectTeamMember);
                    }
                }
                else if  (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.PROJECT_TEAM_RELATIONSHIP.typeName))
                {
                    ProjectTeamMember projectTeamMember = teamMemberConverter.getNewBean(projectMemberBeanClass, relatedMetadataElement, methodName);

                    if ((teamRole == null) || ((projectTeamMember.getProjectTeamProperties() != null) && (teamRole.equals(projectTeamMember.getProjectTeamProperties().getTeamRole()))))
                    {
                        teamMembers.add(projectTeamMember);
                    }
                }
            }

            if (! teamMembers.isEmpty())
            {
                return teamMembers;
            }
        }

        return null;
    }


    /**
     * Convert project objects from the OpenMetadataClient to local beans.
     *
     * @param openMetadataElements retrieved elements
     * @return list of project elements
     * @throws PropertyServerException error in retrieved values
     */
    private List<ProjectElement> convertProjects(List<OpenMetadataElement>  openMetadataElements) throws PropertyServerException
    {
        final String methodName = "convertProjects";

        if (openMetadataElements != null)
        {
            List<ProjectElement> projectElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    projectElements.add(projectConverter.getNewBean(projectBeanClass, openMetadataElement, methodName));
                }
            }

            return projectElements;
        }

        return null;
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

        ProjectsResponse restResult = restClient.callProjectsPostRESTCall(methodName,
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

        ProjectsResponse restResult = restClient.callProjectsPostRESTCall(methodName,
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

        ProjectsResponse restResult = restClient.callProjectsGetRESTCall(methodName,
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

        PersonRolesResponse restResult = restClient.callPersonRolesGetRESTCall(methodName,
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

        ActorProfilesResponse restResult = restClient.callActorProfilesGetRESTCall(methodName,
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
