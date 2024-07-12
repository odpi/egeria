/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.projectmanagement.api;

import org.odpi.openmetadata.accessservices.projectmanagement.metadataelements.ActorProfileElement;
import org.odpi.openmetadata.accessservices.projectmanagement.metadataelements.ProjectElement;
import org.odpi.openmetadata.accessservices.projectmanagement.metadataelements.PersonRoleElement;
import org.odpi.openmetadata.accessservices.projectmanagement.metadataelements.ProjectTeamMember;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectTeamProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * The ProjectManagementInterface provides methods for managing projects, their membership and content.
 * Projects allow groups of subject-matter experts to work together and share content and ideas.
 */
public interface ProjectsInterface
{
    /**
     * Create a new metadata element to represent the project.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param projectProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createProject(String            userId,
                         String            externalSourceGUID,
                         String            externalSourceName,
                         ProjectProperties projectProperties) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;


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
    String createProject(String               userId,
                         String               anchorGUID,
                         boolean              isOwnAnchor,
                         String               optionalClassification,
                         ProjectProperties    properties,
                         String               parentGUID,
                         String               parentRelationshipTypeName,
                         ElementProperties    parentRelationshipProperties,
                         boolean              parentAtEnd1) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException;


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
    String createProjectFromTemplate(String                         userId,
                                     String                         anchorGUID,
                                     boolean                        isOwnAnchor,
                                     Date effectiveFrom,
                                     Date                           effectiveTo,
                                     String                         templateGUID,
                                     ElementProperties              replacementProperties,
                                     Map<String, String> placeholderProperties,
                                     String                         parentGUID,
                                     String                         parentRelationshipTypeName,
                                     ElementProperties              parentRelationshipProperties,
                                     boolean                        parentAtEnd1) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException;


    /**
     * Update the metadata element representing a project.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param projectGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param projectProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateProject(String            userId,
                       String            externalSourceGUID,
                       String            externalSourceName,
                       String            projectGUID,
                       boolean           isMergeUpdate,
                       ProjectProperties projectProperties) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException;


    /**
     * Create a ProjectManagement relationship between a project and a person role to show that anyone appointed to the role is a manager of the project.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param projectGUID unique identifier of the project
     * @param personRoleGUID unique identifier of the role
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupProjectManagementRole(String userId,
                                    String externalSourceGUID,
                                    String externalSourceName,
                                    String projectGUID,
                                    String personRoleGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Remove a membership relationship between a project and a person role.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param projectGUID unique identifier of the project
     * @param personRoleGUID unique identifier of the role
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearProjectManagementRole(String userId,
                                    String externalSourceGUID,
                                    String externalSourceName,
                                    String projectGUID,
                                    String personRoleGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;



    /**
     * Create a ProjectTeam relationship between a project and  an actor profile (typically a team).
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param projectGUID unique identifier of the project
     * @param properties describes the permissions that the role has in the project
     * @param actorProfileGUID unique identifier of the actor
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupProjectTeam(String                userId,
                          String                externalSourceGUID,
                          String                externalSourceName,
                          String                projectGUID,
                          ProjectTeamProperties properties,
                          String                actorProfileGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Remove a ProjectTeam relationship between a project and an actor profile (typically a team).
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param projectGUID unique identifier of the project
     * @param actorProfileGUID unique identifier of the actor
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearProjectTeam(String userId,
                          String externalSourceGUID,
                          String externalSourceName,
                          String projectGUID,
                          String actorProfileGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException;


    /**
     * Remove the metadata element representing a project.  This will delete all anchored
     * elements such as comments.
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
    void removeProject(String userId,
                       String externalSourceGUID,
                       String externalSourceName,
                       String projectGUID) throws InvalidParameterException,
                                                  UserNotAuthorizedException,
                                                  PropertyServerException;


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
    List<ProjectElement> getLinkedProjects(String userId,
                                           String parentGUID,
                                           String projectStatus,
                                           int    startFrom,
                                           int    pageSize) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException;


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
    List<ProjectElement> getClassifiedProjects(String userId,
                                               String classificationName,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException;


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
    List<ProjectTeamMember> getProjectMembers(String userId,
                                              String projectGUID,
                                              String teamRole,
                                              int    startFrom,
                                              int    pageSize) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException;


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
    List<ProjectElement> findProjects(String userId,
                                      String searchString,
                                      int    startFrom,
                                      int    pageSize) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException;


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
    List<ProjectElement> getProjectsByName(String userId,
                                           String name,
                                           int    startFrom,
                                           int    pageSize) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException;


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
    List<ProjectElement> getProjects(String userId,
                                     int    startFrom,
                                     int    pageSize) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException;


    /**
     * Return information about the project management roles linked to a project.
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
    List<PersonRoleElement> getProjectManagementRoles(String userId,
                                                      String projectGUID,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;



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
    List<ActorProfileElement> getProjectActors(String userId,
                                               String projectGUID,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;


    /**
     * Retrieve the project metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param projectGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    ProjectElement getProjectByGUID(String userId,
                                    String projectGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException;
}
