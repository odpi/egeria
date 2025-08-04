/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.converters.ProjectConverter;
import org.odpi.openmetadata.frameworks.openmetadata.converters.ProjectHierarchyConverter;
import org.odpi.openmetadata.frameworks.openmetadata.converters.TeamMemberConverter;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.ProjectGraphMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.ProjectMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;

/**
 * ProjectManagerClient supports the APIs to maintain projects and their related objects.
 */
public class ProjectHandler extends OpenMetadataHandlerBase
{


    final private ProjectConverter<ProjectElement>       projectConverter;
    final private Class<ProjectElement>                  projectBeanClass       = ProjectElement.class;
    final private TeamMemberConverter<ProjectTeamMember> teamMemberConverter;

    final private Class<ProjectTeamMember> projectMemberBeanClass = ProjectTeamMember.class;

    final private ActorRoleHandler actorRoleHandler;

    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public ProjectHandler(String             localServerName,
                          AuditLog           auditLog,
                          String             serviceName,
                          OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              serviceName,
              openMetadataClient,
              OpenMetadataType.PROJECT.typeName);

        projectConverter = new ProjectConverter<>(propertyHelper,
                                                  serviceName,
                                                  localServerName);

        teamMemberConverter = new TeamMemberConverter<>(propertyHelper,
                                                        serviceName,
                                                        localServerName);

        actorRoleHandler = new ActorRoleHandler(localServerName, auditLog, serviceName, openMetadataClient);
    }

    /* =====================================================================================================================
     * A Project describes a targeted set of activities
     */


    /**
     * Create a new generic project.
     *
     * @param userId                 userId of user making request.
     * @param newElementOptions details of the element to create
     * @param optionalClassification classification of the projects - eg Campaign, Task or PersonalProject
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties             properties for the project.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the newly created Project
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createProject(String                                userId,
                                NewElementOptions                     newElementOptions,
                                String                                optionalClassification,
                                Map<String, ClassificationProperties> initialClassifications,
                                ProjectProperties                     properties,
                                RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        final String methodName = "createProject";

        Map<String, ClassificationProperties> requestedClassifications = initialClassifications;

        if (requestedClassifications == null)
        {
            requestedClassifications = new HashMap<>();
        }

        requestedClassifications.put(optionalClassification, null);

        return super.createNewElement(userId,
                                      newElementOptions,
                                      requestedClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a project using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new project.
     *
     * @param userId             calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createProjectFromTemplate(String                 userId,
                                            TemplateOptions        templateOptions,
                                            String                 templateGUID,
                                            ElementProperties      replacementProperties,
                                            Map<String, String>    placeholderProperties,
                                            RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        return super.createElementFromTemplate(userId,
                                               templateOptions,
                                               templateGUID,
                                               replacementProperties,
                                               placeholderProperties,
                                               parentRelationshipProperties);
    }


    /**
     * Update the metadata element representing a project.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param userId calling user
     * @param projectGUID unique identifier of the metadata element to update
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param projectProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateProject(String            userId,
                              String            projectGUID,
                              UpdateOptions     updateOptions,
                              ProjectProperties projectProperties) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName        = "updateProject";
        final String guidParameterName = "projectGUID";

        super.updateElement(userId,
                            projectGUID,
                            guidParameterName,
                            updateOptions,
                            projectProperties,
                            methodName);
    }


    /**
     * Create a project management relationship between a project and a person role to show that someone has been appointed to the project management role.
     *
     * @param userId calling user
     * @param projectGUID unique identifier of the project
     * @param personRoleGUID unique identifier of the person role
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties  properties for the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupProjectManagementRole(String                      userId,
                                           String                      projectGUID,
                                           String                      personRoleGUID,
                                           MetadataSourceOptions       metadataSourceOptions,
                                           ProjectManagementProperties properties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName                  = "setupProjectManagementRole";
        final String projectGUIDParameterName    = "projectGUID";
        final String personRoleGUIDParameterName = "personRoleGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(projectGUID, projectGUIDParameterName, methodName);
        propertyHelper.validateGUID(personRoleGUID, personRoleGUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.PROJECT_MANAGEMENT_RELATIONSHIP.typeName,
                                                        projectGUID,
                                                        personRoleGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove a project management relationship between a project and a person role.
     *
     * @param userId calling user
     * @param projectGUID unique identifier of the project
     * @param personRoleGUID unique identifier of the person role
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProjectManagementRole(String        userId,
                                           String        projectGUID,
                                           String        personRoleGUID,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName                  = "clearProjectManagementRole";
        final String projectGUIDParameterName    = "projectGUID";
        final String personRoleGUIDParameterName = "personRoleGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(projectGUID, projectGUIDParameterName, methodName);
        propertyHelper.validateGUID(personRoleGUID, personRoleGUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.PROJECT_MANAGEMENT_RELATIONSHIP.typeName,
                                                        projectGUID,
                                                        personRoleGUID,
                                                        deleteOptions);
    }


    /**
     * Create a project dependency relationship between two projects.
     *
     * @param userId calling user
     * @param projectGUID unique identifier of the project
     * @param dependsOnProjectGUID unique identifier of the project it depends on
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties  properties for the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupProjectDependency(String                      userId,
                                       String                      projectGUID,
                                       String                      dependsOnProjectGUID,
                                       MetadataSourceOptions       metadataSourceOptions,
                                       ProjectDependencyProperties properties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName                  = "setupProjectDependency";
        final String projectGUIDParameterName    = "projectGUID";
        final String personRoleGUIDParameterName = "dependsOnProjectGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(projectGUID, projectGUIDParameterName, methodName);
        propertyHelper.validateGUID(dependsOnProjectGUID, personRoleGUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.PROJECT_DEPENDENCY_RELATIONSHIP.typeName,
                                                        projectGUID,
                                                        dependsOnProjectGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove a project dependency relationship between two projects.
     *
     * @param userId calling user
     * @param projectGUID unique identifier of the project
     * @param dependsOnProjectGUID unique identifier of the project it depends on
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProjectDependency(String        userId,
                                       String        projectGUID,
                                       String        dependsOnProjectGUID,
                                       DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName                  = "clearProjectDependency";
        final String projectGUIDParameterName    = "projectGUID";
        final String personRoleGUIDParameterName = "dependsOnProjectGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(projectGUID, projectGUIDParameterName, methodName);
        propertyHelper.validateGUID(dependsOnProjectGUID, personRoleGUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.PROJECT_DEPENDENCY_RELATIONSHIP.typeName,
                                                        projectGUID,
                                                        dependsOnProjectGUID,
                                                        deleteOptions);
    }


    /**
     * Create a project hierarchy relationship between two projects.
     *
     * @param userId calling user
     * @param projectGUID unique identifier of the project
     * @param managedProjectGUID unique identifier of the project it manages
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties  properties for the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupProjectHierarchy(String                     userId,
                                      String                     projectGUID,
                                      String                     managedProjectGUID,
                                      MetadataSourceOptions      metadataSourceOptions,
                                      ProjectHierarchyProperties properties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName                  = "setupProjectHierarchy";
        final String projectGUIDParameterName    = "projectGUID";
        final String managedProjectGUIDParameterName = "managedProjectGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(projectGUID, projectGUIDParameterName, methodName);
        propertyHelper.validateGUID(managedProjectGUID, managedProjectGUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName,
                                                        projectGUID,
                                                        managedProjectGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove a project hierarchy relationship between two projects.
     *
     * @param userId calling user
     * @param projectGUID unique identifier of the project
     * @param managedProjectGUID unique identifier of the project it manages
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProjectHierarchy(String        userId,
                                      String        projectGUID,
                                      String        managedProjectGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName                  = "clearProjectHierarchy";
        final String projectGUIDParameterName    = "projectGUID";
        final String managedProjectGUIDParameterName = "managedProjectGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(projectGUID, projectGUIDParameterName, methodName);
        propertyHelper.validateGUID(managedProjectGUID, managedProjectGUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName,
                                                        projectGUID,
                                                        managedProjectGUID,
                                                        deleteOptions);
    }


    /**
     * Create a ProjectTeam relationship between a project and an actor to show that they are member of the project.
     *
     * @param userId calling user
     * @param projectGUID unique identifier of the project
     * @param actorGUID unique identifier of the person role
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties describes the permissions that the role has in the project
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupProjectTeam(String                userId,
                                 String                projectGUID,
                                 String                actorGUID,
                                 MetadataSourceOptions metadataSourceOptions,
                                 ProjectTeamProperties properties) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName                  = "setupProjectTeam";
        final String projectGUIDParameterName    = "projectGUID";
        final String personRoleGUIDParameterName = "actorGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(projectGUID, projectGUIDParameterName, methodName);
        propertyHelper.validateGUID(actorGUID, personRoleGUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.PROJECT_TEAM_RELATIONSHIP.typeName,
                                                        projectGUID,
                                                        actorGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove a ProjectTeam relationship between a project and an actor.
     *
     * @param userId calling user
     * @param projectGUID unique identifier of the project
     * @param actorGUID unique identifier of the person role
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProjectTeam(String        userId,
                                 String        projectGUID,
                                 String        actorGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String methodName                  = "clearProjectTeam";
        final String projectGUIDParameterName    = "projectGUID";
        final String personRoleGUIDParameterName = "actorGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(projectGUID, projectGUIDParameterName, methodName);
        propertyHelper.validateGUID(actorGUID, personRoleGUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.PROJECT_TEAM_RELATIONSHIP.typeName,
                                                        projectGUID,
                                                        actorGUID,
                                                        deleteOptions);
    }


    /**
     * Remove the metadata element representing a project.
     *
     * @param userId calling user
     * @param projectGUID unique identifier of the metadata element to remove
     * @param deleteOptions options for a delete request
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeProject(String        userId,
                              String        projectGUID,
                              DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName               = "removeProject";
        final String elementGUIDParameterName = "projectGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(projectGUID, elementGUIDParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, projectGUID, deleteOptions);
    }


    /**
     * Returns the list of projects that are linked off of the supplied element.
     *
     * @param userId         userId of user making request
     * @param parentGUID     unique identifier of referenceable object (typically a personal profile, project or
     *                       community) that the projects hang off of
     * @param projectStatus filter response by project type - if null, any value will do
     *
     * @return a list of projects
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<ProjectElement> getLinkedProjects(String       userId,
                                                  String       parentGUID,
                                                  String       projectStatus,
                                                  QueryOptions queryOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "getLinkedProjects";
        final String parentGUIDParameterName = "parentGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentGUID, parentGUIDParameterName, methodName);
        propertyHelper.validatePaging(queryOptions, openMetadataClient.getMaxPagingSize(), methodName);

        RelatedMetadataElementList linkedProjects = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                  parentGUID,
                                                                                                  1,
                                                                                                  null,
                                                                                                  queryOptions);

        if ((linkedProjects != null) && (linkedProjects.getElementList() != null))
        {
            List<ProjectElement> filteredProjects = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : linkedProjects.getElementList())
            {
                if (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), OpenMetadataType.PROJECT.typeName))
                {
                    ProjectElement projectElement = projectConverter.getNewComplexBean(projectBeanClass,
                                                                                       relatedMetadataElement,
                                                                                       super.getElementRelatedElements(userId, relatedMetadataElement.getElement(), queryOptions),
                                                                                       methodName);

                    if ((projectStatus == null) || (projectStatus.isBlank()) || (projectStatus.equals(projectElement.getProperties().getProjectStatus())))
                    {
                        ProjectMermaidGraphBuilder mermaidGraphBuilder = new ProjectMermaidGraphBuilder(projectElement);

                        projectElement.setMermaidGraph(mermaidGraphBuilder.getMermaidGraph());
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
     * Returns the graph of related projects and resources starting with a supplied project guid..
     *
     * @param userId         userId of user making request
     * @param projectGUID     unique identifier of the starting project
     * @param queryOptions multiple options to control the query
     *
     * @return a graph of projects
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public ProjectGraph getProjectGraph(String       userId,
                                        String       projectGUID,
                                        QueryOptions queryOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        OpenMetadataElement startingProject = openMetadataClient.getMetadataElementByGUID(userId, projectGUID, queryOptions);

        if (startingProject != null)
        {
            ProjectGraph projectGraph = new ProjectGraph(this.getProjectHierarchy(userId, startingProject, queryOptions, new ArrayList<>()));

            ProjectGraphMermaidGraphBuilder projectGraphBuilder = new ProjectGraphMermaidGraphBuilder(projectGraph);

            projectGraph.setMermaidGraph(projectGraphBuilder.getMermaidGraph());

            return projectGraph;
        }

        return null;
    }


    /**
     * Retrieve the project hierarchy for a project.
     *
     * @param userId calling user
     * @param project starting project
     * @param queryOptions multiple options to control the query
     * @param coveredProjects unique identifiers of projects already processed
     * @return project hierarchy for the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private ProjectHierarchy getProjectHierarchy(String              userId,
                                                 OpenMetadataElement project,
                                                 QueryOptions        queryOptions,
                                                 List<String>        coveredProjects) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "getProjectHierarchy";

        List<RelatedMetadataElement> relatedElements  = super.getElementRelatedElements(userId, project, queryOptions);
        List<RelatedMetadataElement> projectResources = new ArrayList<>();

        List<ProjectHierarchy>       projectHierarchy = null;

        if (relatedElements != null)
        {
            List<RelatedMetadataElement> children = new ArrayList<>();

            for (RelatedMetadataElement relatedProjectElement : relatedElements)
            {
                /*
                 * Separate details of the project dependency elements
                 */
                if ((relatedProjectElement != null) &&
                        (propertyHelper.isTypeOf(relatedProjectElement, OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName)))
                {
                    if (! relatedProjectElement.getElementAtEnd1())
                    {
                        children.add(relatedProjectElement);
                    }
                    else if (! coveredProjects.contains(relatedProjectElement.getElement().getElementGUID()))
                    {
                        /*
                         * Only keeping projects not already in the graph
                         */
                        projectResources.add(relatedProjectElement);
                    }
                }
                else
                {
                    projectResources.add(relatedProjectElement);
                }
            }

            if (! children.isEmpty())
            {
                projectHierarchy = new ArrayList<>();

                for (RelatedMetadataElement child : children)
                {
                    if ((child != null) && (! coveredProjects.contains(child.getElement().getElementGUID())))
                    {
                        coveredProjects.add(child.getElement().getElementGUID());

                        ProjectHierarchy childProjectHierarchy = this.getProjectHierarchy(userId, child.getElement(), queryOptions, coveredProjects);

                        childProjectHierarchy.setRelatedBy(projectConverter.getRelatedBy(ProjectElement.class,
                                                                                         child,
                                                                                         methodName));

                        projectHierarchy.add(childProjectHierarchy);
                    }
                }
            }
        }

        ProjectHierarchyConverter<ProjectHierarchy> hierarchyConverter = new ProjectHierarchyConverter<>(propertyHelper,
                                                                                                         localServiceName,
                                                                                                         localServerName,
                                                                                                         projectHierarchy);

        return hierarchyConverter.getNewComplexBean(ProjectHierarchy.class,
                                                    project,
                                                    projectResources,
                                                    methodName);
    }


    /**
     * Returns the list of projects with a particular classification.
     *
     * @param userId             userId of user making request
     * @param classificationName name of the classification - if null, all projects are returned
     * @param suppliedQueryOptions multiple options to control the query
     *
     * @return a list of projects
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<ProjectElement> getClassifiedProjects(String       userId,
                                                      String       classificationName,
                                                      QueryOptions suppliedQueryOptions) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName = "getClassifiedProjects";
        final String parameterName = "classificationName";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validatePaging(suppliedQueryOptions, openMetadataClient.getMaxPagingSize(), methodName);
        propertyHelper.validateMandatoryName(classificationName, parameterName, methodName);

        QueryOptions queryOptions = suppliedQueryOptions;

        if (queryOptions == null)
        {
            queryOptions = new QueryOptions();
        }

        if (queryOptions.getMetadataElementTypeName() == null)
        {
            queryOptions.setMetadataElementTypeName(OpenMetadataType.PROJECT.typeName);
        }

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.getMetadataElementsByClassification(userId,
                                                                                                                classificationName,
                                                                                                                queryOptions);
        return convertProjects(userId, openMetadataElements, queryOptions);
    }


    /**
     * Return a list of actors that are members of a project.
     *
     * @param userId             userId of user making request
     * @param projectGUID unique identifier of the project
     * @param teamRole optional team role
     * @param queryOptions multiple options to control the query
     *
     * @return list of team members
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<ProjectTeamMember> getProjectMembers(String       userId,
                                                     String       projectGUID,
                                                     String       teamRole,
                                                     QueryOptions queryOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName = "getProjectMembers";
        final String parentGUIDParameterName = "projectGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(projectGUID, parentGUIDParameterName, methodName);
        propertyHelper.validatePaging(queryOptions, openMetadataClient.getMaxPagingSize(), methodName);

        RelatedMetadataElementList linkedActors = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                projectGUID,
                                                                                                1,
                                                                                                null,
                                                                                                queryOptions);

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
     * @param userId calling user
     * @param openMetadataElements retrieved elements
     * @param queryOptions multiple options to control the query
     *
     * @return list of project elements
     * @throws PropertyServerException error in retrieved values
     */
    private List<ProjectElement> convertProjects(String                     userId,
                                                 List<OpenMetadataElement>  openMetadataElements,
                                                 QueryOptions               queryOptions) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException
    {
        final String methodName = "convertProjects";

        if (openMetadataElements != null)
        {
            List<ProjectElement> projectElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    ProjectElement projectElement = projectConverter.getNewComplexBean(projectBeanClass,
                                                                                       openMetadataElement,
                                                                                       super.getElementRelatedElements(userId, openMetadataElement, queryOptions),
                                                                                       methodName);

                    ProjectMermaidGraphBuilder mermaidGraphBuilder = new ProjectMermaidGraphBuilder(projectElement);

                    projectElement.setMermaidGraph(mermaidGraphBuilder.getMermaidGraph());

                    projectElements.add(projectElement);
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
     * @param suppliedSearchOptions options for query
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProjectElement> findProjects(String        userId,
                                             String        searchString,
                                             SearchOptions suppliedSearchOptions) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName                = "findProject";
        final String searchStringParameterName = "searchString";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateSearchString(searchString, searchStringParameterName, methodName);

        SearchOptions searchOptions = suppliedSearchOptions;

        if (searchOptions == null)
        {
            searchOptions = new SearchOptions();
        }

        if (searchOptions.getMetadataElementTypeName() == null)
        {
            searchOptions.setMetadataElementTypeName(OpenMetadataType.PROJECT.typeName);
        }

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElementsWithString(userId,
                                                                                                           searchString,
                                                                                                           searchOptions);

        return this.convertProjects(userId, openMetadataElements, searchOptions);
    }


    /**
     * Retrieve the list of metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param suppliedQueryOptions multiple options to control the query
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProjectElement> getProjectsByName(String       userId,
                                                  String       name,
                                                  QueryOptions suppliedQueryOptions) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName        = "getProjectByName";
        final String nameParameterName = "name";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateMandatoryName(name, nameParameterName, methodName);
        propertyHelper.validatePaging(suppliedQueryOptions, openMetadataClient.getMaxPagingSize(), methodName);

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name);

        QueryOptions queryOptions = suppliedQueryOptions;

        if (queryOptions == null)
        {
            queryOptions = new QueryOptions();
        }

        if (queryOptions.getMetadataElementTypeName() == null)
        {
            queryOptions.setMetadataElementTypeName(OpenMetadataType.PROJECT.typeName);
        }

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.getMetadataElementsByPropertyValue(userId,
                                                                                                               propertyNames,
                                                                                                               name,
                                                                                                               queryOptions);

        if (openMetadataElements != null)
        {
            return this.convertProjects(userId, openMetadataElements, queryOptions);
        }

        return null;
    }


    /**
     * Retrieve the list of projects.
     *
     * @param userId calling user
     * @param suppliedQueryOptions multiple options to control the query
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProjectElement> getProjects(String       userId,
                                            QueryOptions suppliedQueryOptions) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "getProjects";

        propertyHelper.validateUserId(userId, methodName);

        QueryOptions queryOptions = suppliedQueryOptions;

        if (queryOptions == null)
        {
            queryOptions = new QueryOptions();
        }

        if (queryOptions.getMetadataElementTypeName() == null)
        {
            queryOptions.setMetadataElementTypeName(OpenMetadataType.PROJECT.typeName);
        }

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElementsWithString(userId,
                                                                                                           null,
                                                                                                           new SearchOptions(queryOptions));

        if (openMetadataElements != null)
        {
            return this.convertProjects(userId, openMetadataElements, queryOptions);
        }

        return null;
    }


    /**
     * Return information about the person roles linked to a project.
     *
     * @param userId calling user
     * @param projectGUID unique identifier for the project
     * @param queryOptions multiple options to control the query
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<OpenMetadataRootElement> getProjectManagementRoles(String       userId,
                                                                   String       projectGUID,
                                                                   QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName        = "getProjectManagementRoles";
        final String guidPropertyName  = "projectGUID";

        return actorRoleHandler.getRelatedRootElements(userId,
                                                       projectGUID,
                                                       guidPropertyName,
                                                       1,
                                                       OpenMetadataType.PROJECT_MANAGEMENT_RELATIONSHIP.typeName,
                                                       queryOptions,
                                                       methodName);
    }


    /**
     * Return information about the actors linked to a project.
     *
     * @param userId calling user
     * @param projectGUID unique identifier for the project
     * @param queryOptions multiple options to control the query
     *
     * @return list of matching actor profiles
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<OpenMetadataRootElement> getProjectActors(String       userId,
                                                          String       projectGUID,
                                                          QueryOptions queryOptions) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName        = "getProjectActors";
        final String guidPropertyName  = "projectGUID";

        return actorRoleHandler.getRelatedRootElements(userId,
                                                       projectGUID,
                                                       guidPropertyName,
                                                       1,
                                                       OpenMetadataType.PROJECT_TEAM_RELATIONSHIP.typeName,
                                                       queryOptions,
                                                       methodName);
    }


    /**
     * Retrieve the metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param projectGUID unique identifier of the requested metadata element
     * @param getOptions multiple options to control the query
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProjectElement getProjectByGUID(String     userId,
                                           String     projectGUID,
                                           GetOptions getOptions) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "getProjectByGUID";
        final String guidParameterName = "projectGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(projectGUID, guidParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataClient.getMetadataElementByGUID(userId, projectGUID, getOptions);

        if (openMetadataElement != null)
        {
            ProjectElement projectElement = projectConverter.getNewComplexBean(projectBeanClass,
                                                                               openMetadataElement,
                                                                               super.getElementRelatedElements(userId, openMetadataElement, new QueryOptions(getOptions)),
                                                                               methodName);

            ProjectMermaidGraphBuilder mermaidGraphBuilder = new ProjectMermaidGraphBuilder(projectElement);

            projectElement.setMermaidGraph(mermaidGraphBuilder.getMermaidGraph());

            return projectElement;
        }

        return null;
    }
}
