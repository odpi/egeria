/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ProjectHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectDependencyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with project elements.
 */
public class ProjectClient extends ConnectorContextClientBase
{
    private final ProjectHandler projectHandler;


    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param connectorGUID the unique identifier that represents this connector in open metadata
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openMetadataClient client to access the open metadata store
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public ProjectClient(ConnectorContextBase     parentContext,
                         String                   localServerName,
                         String                   localServiceName,
                         String                   connectorUserId,
                         String                   connectorGUID,
                         String                   externalSourceGUID,
                         String                   externalSourceName,
                         OpenMetadataClient       openMetadataClient,
                         AuditLog                 auditLog,
                         int                      maxPageSize)
    {
        super(parentContext, localServerName, localServiceName, connectorUserId, connectorGUID, externalSourceGUID, externalSourceName, auditLog, maxPageSize);

        this.projectHandler = new ProjectHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a new project.
     *
     * @param newElementOptions details of the element to create
     * @param optionalClassification classification of the projects - eg Campaign, Task or PersonalProjectProperties
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createProject(NewElementOptions                     newElementOptions,
                                String                                optionalClassification,
                                Map<String, ClassificationProperties> initialClassifications,
                                ProjectProperties                    properties,
                                RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        String elementGUID = projectHandler.createProject(connectorUserId, newElementOptions, optionalClassification, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent a project using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new project.
     *
     * @param templateOptions              details of the element to create
     * @param templateGUID                 the unique identifier of the existing project to copy (this will copy all the attachments such as nested content, schema
     *                                     connection etc)
     * @param replacementProperties        properties of the new metadata element.  These override the template values
     * @param placeholderProperties        property name-to-property value map to replace any placeholder values in the
     *                                     template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createProjectFromTemplate(TemplateOptions templateOptions,
                                             String templateGUID,
                                             ElementProperties replacementProperties,
                                             Map<String, String> placeholderProperties,
                                             RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        String elementGUID = projectHandler.createProjectFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of a project.
     *
     * @param projectGUID       unique identifier of the project (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateProject(String            projectGUID,
                                 UpdateOptions     updateOptions,
                                 ProjectProperties properties) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        boolean updateOccurred = projectHandler.updateProject(connectorUserId, projectGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getIntegrationReportWriter() != null))
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(projectGUID);
        }

        return updateOccurred;
    }


    /**
     * Create a project dependency relationship between two projects.
     *
     * @param projectGUID unique identifier of the project
     * @param dependsOnProjectGUID unique identifier of the project it depends on
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties  properties for the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupProjectDependency(String                      projectGUID,
                                       String                      dependsOnProjectGUID,
                                       MetadataSourceOptions       metadataSourceOptions,
                                       ProjectDependencyProperties properties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        projectHandler.setupProjectDependency(connectorUserId, projectGUID, dependsOnProjectGUID, metadataSourceOptions, properties);
    }


    /**
     * Remove a project dependency relationship between two projects.
     *
     * @param projectGUID unique identifier of the project
     * @param dependsOnProjectGUID unique identifier of the project it depends on
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProjectDependency(String        projectGUID,
                                       String        dependsOnProjectGUID,
                                       DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        projectHandler.clearProjectDependency(connectorUserId, projectGUID, dependsOnProjectGUID, deleteOptions);
    }


    /**
     * Create a project hierarchy relationship between two projects.
     *
     * @param projectGUID unique identifier of the project
     * @param managedProjectGUID unique identifier of the project it manages
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties  properties for the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupProjectHierarchy(String                     projectGUID,
                                      String                     managedProjectGUID,
                                      MetadataSourceOptions      metadataSourceOptions,
                                      ProjectHierarchyProperties properties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        projectHandler.setupProjectHierarchy(connectorUserId, projectGUID, managedProjectGUID, metadataSourceOptions, properties);
    }


    /**
     * Remove a project hierarchy relationship between two projects.
     *
     * @param projectGUID unique identifier of the project
     * @param managedProjectGUID unique identifier of the project it manages
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProjectHierarchy(String        projectGUID,
                                      String        managedProjectGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        projectHandler.clearProjectHierarchy(connectorUserId, projectGUID, managedProjectGUID, deleteOptions);
    }


    /**
     * Create a ProjectTeam relationship between a project and an actor to show that they are member of the project.
     *
     * @param projectGUID unique identifier of the project
     * @param actorGUID unique identifier of the person role
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties describes the permissions that the role has in the project
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupProjectTeam(String                    projectGUID,
                                 String                    actorGUID,
                                 MetadataSourceOptions     metadataSourceOptions,
                                 AssignmentScopeProperties properties) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        projectHandler.setupProjectTeam(connectorUserId, projectGUID, actorGUID, metadataSourceOptions, properties);
    }


    /**
     * Remove a ProjectTeam relationship between a project and an actor.
     *
     * @param projectGUID unique identifier of the project
     * @param actorGUID unique identifier of the person role
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProjectTeam(String        projectGUID,
                                 String        actorGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        projectHandler.clearProjectTeam(connectorUserId, projectGUID, actorGUID, deleteOptions);
    }


    /**
     * Delete a project.
     *
     * @param projectGUID       unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteProject(String        projectGUID,
                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        projectHandler.deleteProject(connectorUserId, projectGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(projectGUID);
        }
    }


    /**
     * Returns the list of projects with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, identifier or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getProjectsByName(String       name,
                                                            QueryOptions queryOptions) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        return projectHandler.getProjectsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific project.
     *
     * @param projectGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getProjectByGUID(String     projectGUID,
                                                     GetOptions getOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        return projectHandler.getProjectByGUID(connectorUserId, projectGUID, getOptions);
    }


    /**
     * Retrieve the list of projects metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned projects include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findProjects(String        searchString,
                                                       SearchOptions searchOptions) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        return projectHandler.findProjects(connectorUserId, searchString, searchOptions);
    }
}
