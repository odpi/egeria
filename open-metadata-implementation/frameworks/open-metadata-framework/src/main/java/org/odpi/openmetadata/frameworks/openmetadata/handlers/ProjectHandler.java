/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.OpenMetadataRootHierarchyMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.VisualStyle;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootHierarchy;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectDependencyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;

/**
 * ProjectManagerClient supports the APIs to maintain projects and their related objects.
 */
public class ProjectHandler extends OpenMetadataHandlerBase
{
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
    }

    /* =====================================================================================================================
     * A Project describes a targeted set of activities
     */


    /**
     * Create a new project.
     *
     * @param userId                 userId of user making request.
     * @param newElementOptions details of the element to create
     * @param optionalClassification classification of the projects - eg Campaign, Task or PersonalProjectProperties
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
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public boolean updateProject(String            userId,
                                 String            projectGUID,
                                 UpdateOptions     updateOptions,
                                 ProjectProperties projectProperties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName        = "updateProject";
        final String guidParameterName = "projectGUID";

        return super.updateElement(userId,
                                   projectGUID,
                                   guidParameterName,
                                   updateOptions,
                                   projectProperties,
                                   methodName);
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
                                       MakeAnchorOptions           metadataSourceOptions,
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
     * @param makeAnchorOptions  options to control access to open metadata
     * @param properties  properties for the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupProjectHierarchy(String                     userId,
                                      String                     projectGUID,
                                      String                     managedProjectGUID,
                                      MakeAnchorOptions          makeAnchorOptions,
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
                                                        makeAnchorOptions,
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
    public void setupProjectTeam(String                    userId,
                                 String                    projectGUID,
                                 String                    actorGUID,
                                 MakeAnchorOptions     metadataSourceOptions,
                                 AssignmentScopeProperties properties) throws InvalidParameterException,
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
                                                        OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                        actorGUID,
                                                        projectGUID,
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
                                                        OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                        actorGUID,
                                                        projectGUID,
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
    public void deleteProject(String        userId,
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
    public List<OpenMetadataRootElement> getLinkedProjects(String       userId,
                                                           String       parentGUID,
                                                           String       projectStatus,
                                                           QueryOptions queryOptions) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "getLinkedProjects";
        final String parentGUIDParameterName = "parentGUID";

        List<OpenMetadataRootElement> linkedProjects = super.getRelatedRootElements(userId,
                                                                                    parentGUID,
                                                                                    parentGUIDParameterName,
                                                                                    1,
                                                                                    null,
                                                                                    OpenMetadataType.PROJECT.typeName,
                                                                                    queryOptions,
                                                                                    methodName);

        return this.filterProjects(linkedProjects, projectStatus);
    }


    /**
     * Filter process objects by activity status.
     *
     * @param openMetadataRootElements retrieved elements
     * @param projectStatus           optional  status
     * @return list of process elements
     */
    private List<OpenMetadataRootElement> filterProjects(List<OpenMetadataRootElement> openMetadataRootElements,
                                                         String                        projectStatus)
    {
        if (openMetadataRootElements != null)
        {
            List<OpenMetadataRootElement> projects = new ArrayList<>();

            for (OpenMetadataRootElement openMetadataRootElement : openMetadataRootElements)
            {
                if ((openMetadataRootElement != null) &&
                        (openMetadataRootElement.getProperties() instanceof ProjectProperties projectProperties))
                {
                    if ((projectStatus == null) || (projectStatus.isBlank()) || (projectStatus.equals(projectProperties.getProjectStatus())))
                    {
                        projects.add(openMetadataRootElement);
                    }
                }
            }

            return projects;
        }

        return null;
    }


    /**
     * Retrieve the project metadata element and all linked projects.
     *
     * @param userId calling user
     * @param projectGUID unique identifier of the requested metadata element
     * @param queryOptions           multiple options to control the query
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootHierarchy getProjectInContext(String       userId,
                                                         String       projectGUID,
                                                         QueryOptions queryOptions) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "getProjectsInContext";

        OpenMetadataRootElement rootElement = this.getRootElementByGUID(userId, projectGUID, queryOptions, methodName);

        if (rootElement != null)
        {
            OpenMetadataRootHierarchy openMetadataRootHierarchy = new OpenMetadataRootHierarchy(rootElement);

            Set<String>  processedProjects = new HashSet<>(Collections.singletonList(projectGUID));

            openMetadataRootHierarchy.setOpenMetadataRootHierarchies(this.getRelatedProjects(userId,
                                                                                             new OpenMetadataRootHierarchy(rootElement),
                                                                                             queryOptions,
                                                                                             processedProjects));

            /*
             * Replaces the graph added by addMermaidToRootElement().
             */
            OpenMetadataRootHierarchyMermaidGraphBuilder mermaidGraphBuilder = new OpenMetadataRootHierarchyMermaidGraphBuilder(openMetadataRootHierarchy,
                                                                                                                                "Related Projects",
                                                                                                                                VisualStyle.PROJECT);

            openMetadataRootHierarchy.setMermaidGraph(mermaidGraphBuilder.getMermaidGraph());

            return openMetadataRootHierarchy;
        }

        return null;
    }


    /**
     * Retrieve the list of related projects not currently in the list.
     *
     * @param userId calling user
     * @param startingProject place to start
     * @param queryOptions type of query
     * @param processedProjects list of guids of governance definitions already processed.
     * @return list of connected governance definitions not yet processed
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private List<OpenMetadataRootHierarchy> getRelatedProjects(String                      userId,
                                                               OpenMetadataRootHierarchy   startingProject,
                                                               QueryOptions                queryOptions,
                                                               Set<String>                 processedProjects) throws InvalidParameterException,
                                                                                                                     PropertyServerException,
                                                                                                                     UserNotAuthorizedException
    {
        List<OpenMetadataRootHierarchy> relatedElements = new ArrayList<>();

        if (! processedProjects.contains(startingProject.getElementHeader().getGUID()))
        {
            if (startingProject.getManagedProjects() != null)
            {
                for (RelatedMetadataElementSummary relatedDefinition : startingProject.getManagedProjects())
                {
                    if ((relatedDefinition != null) && (! processedProjects.contains(relatedDefinition.getRelatedElement().getElementHeader().getGUID())))
                    {
                        OpenMetadataRootHierarchy relatedElement = new OpenMetadataRootHierarchy(
                                this.getProjectByGUID(userId,
                                                      relatedDefinition.getRelatedElement().getElementHeader().getGUID(),
                                                      queryOptions));

                        relatedElements.add(relatedElement);
                        processedProjects.add(relatedElement.getElementHeader().getGUID());

                        getRelatedProjects(userId, relatedElement, queryOptions, processedProjects);
                    }
                }
            }

            if (startingProject.getManagingProjects() != null)
            {
                for (RelatedMetadataElementSummary relatedDefinition : startingProject.getManagingProjects())
                {
                    if ((relatedDefinition != null) && (! processedProjects.contains(relatedDefinition.getRelatedElement().getElementHeader().getGUID())))
                    {
                        OpenMetadataRootHierarchy relatedElement = new OpenMetadataRootHierarchy(
                                this.getProjectByGUID(userId,
                                                      relatedDefinition.getRelatedElement().getElementHeader().getGUID(),
                                                      queryOptions));

                        relatedElements.add(relatedElement);
                        processedProjects.add(relatedElement.getElementHeader().getGUID());

                        getRelatedProjects(userId, relatedElement, queryOptions, processedProjects);
                    }
                }
            }

            if (startingProject.getDependentProjects() != null)
            {
                for (RelatedMetadataElementSummary relatedDefinition : startingProject.getDependentProjects())
                {
                    if ((relatedDefinition != null) && (! processedProjects.contains(relatedDefinition.getRelatedElement().getElementHeader().getGUID())))
                    {
                        OpenMetadataRootHierarchy relatedElement = new OpenMetadataRootHierarchy(
                                this.getProjectByGUID(userId,
                                                      relatedDefinition.getRelatedElement().getElementHeader().getGUID(),
                                                      queryOptions));

                        relatedElements.add(relatedElement);
                        processedProjects.add(relatedElement.getElementHeader().getGUID());

                        getRelatedProjects(userId, relatedElement, queryOptions, processedProjects);
                    }
                }
            }

            if (startingProject.getDependsOnProjects() != null)
            {
                for (RelatedMetadataElementSummary relatedDefinition : startingProject.getDependsOnProjects())
                {
                    if ((relatedDefinition != null) && (! processedProjects.contains(relatedDefinition.getRelatedElement().getElementHeader().getGUID())))
                    {
                        OpenMetadataRootHierarchy relatedElement = new OpenMetadataRootHierarchy(
                                this.getProjectByGUID(userId,
                                                      relatedDefinition.getRelatedElement().getElementHeader().getGUID(),
                                                      queryOptions));

                        relatedElements.add(relatedElement);
                        processedProjects.add(relatedElement.getElementHeader().getGUID());

                        getRelatedProjects(userId, relatedElement, queryOptions, processedProjects);
                    }
                }
            }
        }

        return relatedElements;
    }


    /**
     * Retrieve the list of subprojects not currently in the list.
     *
     * @param userId calling user
     * @param startingProject place to start
     * @param queryOptions type of query
     * @param processedProjects list of guids of governance definitions already processed.
     * @return list of connected governance definitions not yet processed
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private List<OpenMetadataRootHierarchy> getSubProjects(String                      userId,
                                                           OpenMetadataRootHierarchy   startingProject,
                                                           QueryOptions                queryOptions,
                                                           Set<String>                 processedProjects) throws InvalidParameterException,
                                                                                                                 PropertyServerException,
                                                                                                                 UserNotAuthorizedException
    {
        List<OpenMetadataRootHierarchy> relatedElements = new ArrayList<>();

        if (! processedProjects.contains(startingProject.getElementHeader().getGUID()))
        {
            if (startingProject.getManagedProjects() != null)
            {
                for (RelatedMetadataElementSummary relatedDefinition : startingProject.getManagedProjects())
                {
                    if ((relatedDefinition != null) && (! processedProjects.contains(relatedDefinition.getRelatedElement().getElementHeader().getGUID())))
                    {
                        OpenMetadataRootHierarchy relatedElement = new OpenMetadataRootHierarchy(
                                this.getProjectByGUID(userId,
                                                      relatedDefinition.getRelatedElement().getElementHeader().getGUID(),
                                                      queryOptions));

                        relatedElements.add(relatedElement);
                        processedProjects.add(relatedElement.getElementHeader().getGUID());

                        getRelatedProjects(userId, relatedElement, queryOptions, processedProjects);
                    }
                }
            }
        }

        return relatedElements;
    }




    /**
     * Retrieve the project hierarchy for a project.
     *
     * @param userId calling user
     * @param projectGUID starting project
     * @param queryOptions multiple options to control the query
     * @return project hierarchy for the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootHierarchy getProjectHierarchy(String       userId,
                                                         String       projectGUID,
                                                         QueryOptions queryOptions) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName  = "getProjectHierarchy";

        OpenMetadataRootElement rootElement = this.getRootElementByGUID(userId, projectGUID, queryOptions, methodName);

        if (rootElement != null)
        {
            OpenMetadataRootHierarchy openMetadataRootHierarchy = new OpenMetadataRootHierarchy(rootElement);

            Set<String>  processedProjects = new HashSet<>(Collections.singletonList(projectGUID));

            openMetadataRootHierarchy.setOpenMetadataRootHierarchies(this.getSubProjects(userId,
                                                                                         new OpenMetadataRootHierarchy(rootElement),
                                                                                         queryOptions,
                                                                                         processedProjects));

            /*
             * Replaces the graph added by addMermaidToRootElement().
             */
            OpenMetadataRootHierarchyMermaidGraphBuilder mermaidGraphBuilder = new OpenMetadataRootHierarchyMermaidGraphBuilder(openMetadataRootHierarchy,
                                                                                                                                "Managed Projects",
                                                                                                                                VisualStyle.PROJECT);

            openMetadataRootHierarchy.setMermaidGraph(mermaidGraphBuilder.getMermaidGraph());

            return openMetadataRootHierarchy;
        }

        return null;
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
    public List<OpenMetadataRootElement> getClassifiedProjects(String       userId,
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
        return convertRootElements(userId, openMetadataElements, queryOptions, methodName);
    }


    /**
     * Return a list of actors that are members of a project.
     *
     * @param userId             userId of user making request
     * @param projectGUID unique identifier of the project
     * @param teamRole optional team role value found in AssignmentScope's assignmentType attribute.  Used to filter the results.
     * @param queryOptions multiple options to control the query
     *
     * @return list of team members
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getProjectMembers(String       userId,
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

        List<OpenMetadataRootElement> linkedActors = super.getRelatedRootElements(userId,
                                                                                  projectGUID,
                                                                                  parentGUIDParameterName,
                                                                                  2,
                                                                                  OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                                  OpenMetadataType.ACTOR.typeName,
                                                                                  queryOptions,
                                                                                  methodName);

        if (linkedActors != null)
        {
            List<OpenMetadataRootElement> teamMembers = new ArrayList<>();

            /*
             * Optional filtering on assignmentType attribute
             */
            for (OpenMetadataRootElement linkedActor : linkedActors)
            {
                if ((teamRole == null) || (teamRole.isBlank()) || (linkedActor == null))
                {
                    teamMembers.add(linkedActor);
                }
                else
                {
                    if ((linkedActor.getRelatedBy() != null) &&
                            (linkedActor.getRelatedBy().getRelationshipProperties() instanceof AssignmentScopeProperties assignmentScopeProperties) &&
                            (teamRole.equals(assignmentScopeProperties.getAssignmentType())))

                    {
                        teamMembers.add(linkedActor);
                    }
                }
            }

            return teamMembers;
        }

        return null;
    }


    /**
     * Retrieve the list of metadata elements that contain the search string.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param searchOptions options for query
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findProjects(String        userId,
                                                      String        searchString,
                                                      SearchOptions searchOptions) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName = "findProjects";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }


    /**
     * Retrieve the list of metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param queryOptions multiple options to control the query
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> getProjectsByName(String       userId,
                                                           String       name,
                                                           QueryOptions queryOptions) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "getProjectByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
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
    public OpenMetadataRootElement getProjectByGUID(String     userId,
                                                    String     projectGUID,
                                                    GetOptions getOptions) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "getProjectByGUID";
        final String guidParameterName = "projectGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(projectGUID, guidParameterName, methodName);

        return super.getRootElementByGUID(userId, projectGUID, getOptions, methodName);
    }
}
