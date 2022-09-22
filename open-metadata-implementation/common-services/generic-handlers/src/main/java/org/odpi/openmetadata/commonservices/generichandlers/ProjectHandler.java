/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ProjectHandler provides the exchange of metadata about projects between the repository and the OMAS.
 *
 * @param <B> class that represents the project
 */
public class ProjectHandler<B> extends ReferenceableHandler<B>
{
    private static final String qualifiedNameParameterName = "qualifiedName";

    /**
     * Construct the handler with information needed to work with B objects.
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName name of this service
     * @param serverName name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve B instances from
     * @param defaultZones list of zones that the access service should set in all new B instances
     * @param publishZones list of zones that the access service sets up in published B instances
     * @param auditLog destination for audit log events
     */
    public ProjectHandler(OpenMetadataAPIGenericConverter<B> converter,
                          Class<B>                           beanClass,
                          String                             serviceName,
                          String                             serverName,
                          InvalidParameterHandler            invalidParameterHandler,
                          RepositoryHandler                  repositoryHandler,
                          OMRSRepositoryHelper               repositoryHelper,
                          String                             localServerUserId,
                          OpenMetadataServerSecurityVerifier securityVerifier,
                          List<String>                       supportedZones,
                          List<String>                       defaultZones,
                          List<String>                       publishZones,
                          AuditLog                           auditLog)
    {
        super(converter,
              beanClass,
              serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog);
    }


    /**
     * Create the project object.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param qualifiedName qualified name of project
     * @param identifier unique identifier of project - typically allocated externally
     * @param name display name
     * @param description description
     * @param startDate date the project started
     * @param plannedEndDate date the project is expected to end
     * @param projectStatus status of the project
     * @param additionalProperties additional properties for a project
     * @param suppliedTypeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for a governance project subtype
     * @param setCampaignClassification should the Campaign classification be set?
     * @param setTaskClassification should the Task classification be set?
     * @param projectTypeClassification add special classification that defines the type of project - eg GlossaryProject or GovernanceProject
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new project object
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createProject(String              userId,
                                String              externalSourceGUID,
                                String              externalSourceName,
                                String              qualifiedName,
                                String              identifier,
                                String              name,
                                String              description,
                                Date                startDate,
                                Date                plannedEndDate,
                                String              projectStatus,
                                Map<String, String> additionalProperties,
                                String              suppliedTypeName,
                                Map<String, Object> extendedProperties,
                                boolean             setCampaignClassification,
                                boolean             setTaskClassification,
                                String              projectTypeClassification,
                                Date                effectiveFrom,
                                Date                effectiveTo,
                                Date                effectiveTime,
                                String              methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataAPIMapper.PROJECT_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        ProjectBuilder builder = new ProjectBuilder(qualifiedName,
                                                    identifier,
                                                    name,
                                                    description,
                                                    startDate,
                                                    plannedEndDate,
                                                    projectStatus,
                                                    additionalProperties,
                                                    typeGUID,
                                                    typeName,
                                                    extendedProperties,
                                                    repositoryHelper,
                                                    serviceName,
                                                    serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        if (setCampaignClassification)
        {
            builder.setCampaignClassification(userId, methodName);
        }
        else if (setTaskClassification)
        {
            builder.setTaskClassification(userId, methodName);
        }

        if (projectTypeClassification != null)
        {
            builder.setProjectTypeClassification(userId, projectTypeClassification, methodName);
        }

        return this.createBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           typeGUID,
                                           typeName,
                                           builder,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Create a new metadata element to represent a project using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new project.
     *
     * All categories and terms are linked to a single project.  They are owned by this project and if the
     * project is deleted, any linked terms and categories are deleted as well.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param templateGUID unique identifier of the metadata element to copy
     * @param qualifiedName unique name for the project - used in other configuration
     * @param identifier unique identifier of project - typically allocated externally
     * @param displayName short display name for the project
     * @param description description of the governance project
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createProjectFromTemplate(String userId,
                                            String externalSourceGUID,
                                            String externalSourceName,
                                            String templateGUID,
                                            String qualifiedName,
                                            String identifier,
                                            String displayName,
                                            String description,
                                            String methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String templateGUIDParameterName   = "templateGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        ProjectBuilder builder = new ProjectBuilder(qualifiedName,
                                                    identifier,
                                                    displayName,
                                                    description,
                                                    repositoryHelper,
                                                    serviceName,
                                                    serverName);

        return this.createBeanFromTemplate(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           templateGUID,
                                           templateGUIDParameterName,
                                           OpenMetadataAPIMapper.PROJECT_TYPE_GUID,
                                           OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                           qualifiedName,
                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                           builder,
                                           supportedZones,
                                           methodName);
    }


    /**
     * Update the anchor object that all elements in a project (terms and categories) are linked to.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param projectGUID unique identifier of the project to update
     * @param projectGUIDParameterName parameter passing the projectGUID
     * @param qualifiedName qualified name of project
     * @param identifier unique identifier of project - typically allocated externally
     * @param name display name
     * @param description description
     * @param startDate date the project started
     * @param plannedEndDate date the project is expected to end
     * @param projectStatus status of the project
     * @param additionalProperties additional properties for a governance project
     * @param suppliedTypeName type of project
     * @param extendedProperties  properties for a governance project subtype
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void   updateProject(String              userId,
                                String              externalSourceGUID,
                                String              externalSourceName,
                                String              projectGUID,
                                String              projectGUIDParameterName,
                                String              qualifiedName,
                                String              identifier,
                                String              name,
                                String              description,
                                Date                startDate,
                                Date                plannedEndDate,
                                String              projectStatus,
                                Map<String, String> additionalProperties,
                                String              suppliedTypeName,
                                Map<String, Object> extendedProperties,
                                Date                effectiveFrom,
                                Date                effectiveTo,
                                boolean             isMergeUpdate,
                                boolean             forLineage,
                                boolean             forDuplicateProcessing,
                                Date                effectiveTime,
                                String              methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(projectGUID, projectGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataAPIMapper.PROJECT_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        ProjectBuilder builder = new ProjectBuilder(qualifiedName,
                                                    identifier,
                                                    name,
                                                    description,
                                                    startDate,
                                                    plannedEndDate,
                                                    projectStatus,
                                                    additionalProperties,
                                                    typeGUID,
                                                    typeName,
                                                    extendedProperties,
                                                    repositoryHelper,
                                                    serviceName,
                                                    serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    projectGUID,
                                    projectGUIDParameterName,
                                    typeGUID,
                                    typeName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Add a project team (ActorProfile) to a project.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param projectGUID unique identifier of the project
     * @param projectGUIDParameterName parameter supplying the projectGUID
     * @param actorProfileGUID unique identifier of the element that is being added to the project
     * @param actorProfileGUIDParameterName parameter supplying the actorProfileGUID
     * @param teamRole why is the team attached to the project? (optional)
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addActorToProject(String  userId,
                                  String  externalSourceGUID,
                                  String  externalSourceName,
                                  String  projectGUID,
                                  String  projectGUIDParameterName,
                                  String  actorProfileGUID,
                                  String  actorProfileGUIDParameterName,
                                  String  teamRole,
                                  Date    effectiveFrom,
                                  Date    effectiveTo,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing,
                                  Date    effectiveTime,
                                  String  methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataAPIMapper.TEAM_ROLE_PROPERTY_NAME,
                                                                                     teamRole,
                                                                                     methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  projectGUID,
                                  projectGUIDParameterName,
                                  OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                  actorProfileGUID,
                                  actorProfileGUIDParameterName,
                                  OpenMetadataAPIMapper.ACTOR_PROFILE_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataAPIMapper.PROJECT_TEAM_RELATIONSHIP_TYPE_GUID,
                                  OpenMetadataAPIMapper.PROJECT_TEAM_RELATIONSHIP_TYPE_NAME,
                                  properties,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a project team (ActorProfile) from a project.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param projectGUID unique identifier of the project
     * @param projectGUIDParameterName parameter supplying the projectGUID
     * @param actorProfileGUID unique identifier of the element that is being added to the project
     * @param actorProfileGUIDParameterName parameter supplying the actorProfileGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeActorFromProject(String  userId,
                                       String  externalSourceGUID,
                                       String  externalSourceName,
                                       String  projectGUID,
                                       String  projectGUIDParameterName,
                                       String  actorProfileGUID,
                                       String  actorProfileGUIDParameterName,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime,
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      projectGUID,
                                      projectGUIDParameterName,
                                      OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                      actorProfileGUID,
                                      actorProfileGUIDParameterName,
                                      OpenMetadataAPIMapper.ACTOR_PROFILE_TYPE_GUID,
                                      OpenMetadataAPIMapper.ACTOR_PROFILE_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataAPIMapper.PROJECT_TEAM_RELATIONSHIP_TYPE_GUID,
                                      OpenMetadataAPIMapper.PROJECT_TEAM_RELATIONSHIP_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }



    /**
     * Add a subproject.  This creates a hierarchical relationship between the projects.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param projectGUID unique identifier of the project
     * @param projectGUIDParameterName parameter supplying the projectGUID
     * @param subprojectGUID unique identifier of the subproject that is being added to the project
     * @param subprojectGUIDParameterName parameter supplying the subprojectGUID
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addSubProject(String  userId,
                              String  externalSourceGUID,
                              String  externalSourceName,
                              String  projectGUID,
                              String  projectGUIDParameterName,
                              String  subprojectGUID,
                              String  subprojectGUIDParameterName,
                              Date    effectiveFrom,
                              Date    effectiveTo,
                              boolean forLineage,
                              boolean forDuplicateProcessing,
                              Date    effectiveTime,
                              String  methodName) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  projectGUID,
                                  projectGUIDParameterName,
                                  OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                  subprojectGUID,
                                  subprojectGUIDParameterName,
                                  OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataAPIMapper.PROJECT_HIERARCHY_RELATIONSHIP_TYPE_GUID,
                                  OpenMetadataAPIMapper.PROJECT_HIERARCHY_RELATIONSHIP_TYPE_NAME,
                                  null,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a subproject.  This deletes a hierarchical relationship between the projects.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param projectGUID unique identifier of the project
     * @param projectGUIDParameterName parameter supplying the projectGUID
     * @param subprojectProfileGUID unique identifier of the subproject that is being added to the project
     * @param subprojectGUIDParameterName parameter supplying the subprojectProfileGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSubProject(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  projectGUID,
                                 String  projectGUIDParameterName,
                                 String  subprojectProfileGUID,
                                 String  subprojectGUIDParameterName,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime,
                                 String  methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      projectGUID,
                                      projectGUIDParameterName,
                                      OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                      subprojectProfileGUID,
                                      subprojectGUIDParameterName,
                                      OpenMetadataAPIMapper.PROJECT_TYPE_GUID,
                                      OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataAPIMapper.PROJECT_HIERARCHY_RELATIONSHIP_TYPE_GUID,
                                      OpenMetadataAPIMapper.PROJECT_HIERARCHY_RELATIONSHIP_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Add a project dependency.  This adds a ProjectDependency relationship between the projects.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param projectGUID unique identifier of the project
     * @param projectGUIDParameterName parameter supplying the projectGUID
     * @param dependsOnProjectGUID unique identifier of the project that is being added to the project as a dependency
     * @param dependsOnProjectGUIDParameterName parameter supplying the dependsOnProjectGUID
     * @param dependencySummary why is the team attached to the project? (optional)
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addProjectDependency(String  userId,
                                     String  externalSourceGUID,
                                     String  externalSourceName,
                                     String  projectGUID,
                                     String  projectGUIDParameterName,
                                     String  dependsOnProjectGUID,
                                     String  dependsOnProjectGUIDParameterName,
                                     String  dependencySummary,
                                     Date    effectiveFrom,
                                     Date    effectiveTo,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime,
                                     String  methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataAPIMapper.DEPENDENCY_SUMMARY_PROPERTY_NAME,
                                                                                     dependencySummary,
                                                                                     methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  projectGUID,
                                  projectGUIDParameterName,
                                  OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                  dependsOnProjectGUID,
                                  dependsOnProjectGUIDParameterName,
                                  OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataAPIMapper.PROJECT_DEPENDENCY_RELATIONSHIP_TYPE_GUID,
                                  OpenMetadataAPIMapper.PROJECT_DEPENDENCY_RELATIONSHIP_TYPE_NAME,
                                  properties,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a project dependency.  This removes a ProjectDependency relationship between the projects.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param projectGUID unique identifier of the project
     * @param projectGUIDParameterName parameter supplying the projectGUID
     * @param dependsOnProjectGUID unique identifier of the project that is being added to the project as a dependency
     * @param dependsOnProjectGUIDParameterName parameter supplying the dependsOnProjectGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeProjectDependency(String  userId,
                                        String  externalSourceGUID,
                                        String  externalSourceName,
                                        String  projectGUID,
                                        String  projectGUIDParameterName,
                                        String  dependsOnProjectGUID,
                                        String  dependsOnProjectGUIDParameterName,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing,
                                        Date    effectiveTime,
                                        String  methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      projectGUID,
                                      projectGUIDParameterName,
                                      OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                      dependsOnProjectGUID,
                                      dependsOnProjectGUIDParameterName,
                                      OpenMetadataAPIMapper.PROJECT_TYPE_GUID,
                                      OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataAPIMapper.PROJECT_DEPENDENCY_RELATIONSHIP_TYPE_GUID,
                                      OpenMetadataAPIMapper.PROJECT_DEPENDENCY_RELATIONSHIP_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Add a project manager (PersonRole) to a project.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param projectGUID unique identifier of the project
     * @param projectGUIDParameterName parameter supplying the projectGUID
     * @param personRoleGUID unique identifier of the element that is being added to the project
     * @param personRoleGUIDParameterName parameter supplying the personRoleGUID
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addProjectManager(String  userId,
                                  String  externalSourceGUID,
                                  String  externalSourceName,
                                  String  projectGUID,
                                  String  projectGUIDParameterName,
                                  String  personRoleGUID,
                                  String  personRoleGUIDParameterName,
                                  Date    effectiveFrom,
                                  Date    effectiveTo,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing,
                                  Date    effectiveTime,
                                  String  methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  projectGUID,
                                  projectGUIDParameterName,
                                  OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                  personRoleGUID,
                                  personRoleGUIDParameterName,
                                  OpenMetadataAPIMapper.PERSON_ROLE_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataAPIMapper.PROJECT_MANAGEMENT_RELATIONSHIP_TYPE_GUID,
                                  OpenMetadataAPIMapper.PROJECT_MANAGEMENT_RELATIONSHIP_TYPE_NAME,
                                  null,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a project manager (PersonRole) from a project.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param projectGUID unique identifier of the project
     * @param projectGUIDParameterName parameter supplying the projectGUID
     * @param personRoleGUID unique identifier of the element that is being added to the project
     * @param personRoleGUIDParameterName parameter supplying the personRoleGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeProjectManager(String  userId,
                                     String  externalSourceGUID,
                                     String  externalSourceName,
                                     String  projectGUID,
                                     String  projectGUIDParameterName,
                                     String  personRoleGUID,
                                     String  personRoleGUIDParameterName,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime,
                                     String  methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      projectGUID,
                                      projectGUIDParameterName,
                                      OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                      personRoleGUID,
                                      personRoleGUIDParameterName,
                                      OpenMetadataAPIMapper.PERSON_ROLE_TYPE_GUID,
                                      OpenMetadataAPIMapper.PERSON_ROLE_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataAPIMapper.PROJECT_MANAGEMENT_RELATIONSHIP_TYPE_GUID,
                                      OpenMetadataAPIMapper.PROJECT_MANAGEMENT_RELATIONSHIP_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Remove the metadata element representing a project.  This will delete the project and all categories and terms because
     * the Anchors classifications are set up in these elements.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param projectGUID unique identifier of the metadata element to remove
     * @param projectGUIDParameterName parameter supplying the projectGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeProject(String  userId,
                              String  externalSourceGUID,
                              String  externalSourceName,
                              String  projectGUID,
                              String  projectGUIDParameterName,
                              boolean forLineage,
                              boolean forDuplicateProcessing,
                              Date    effectiveTime,
                              String  methodName) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    projectGUID,
                                    projectGUIDParameterName,
                                    OpenMetadataAPIMapper.PROJECT_TYPE_GUID,
                                    OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the list of project metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param searchStringParameterName name of parameter supplying the search string
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> findProjects(String  userId,
                                String  searchString,
                                String  searchStringParameterName,
                                int     startFrom,
                                int     pageSize,
                                boolean forLineage,
                                boolean forDuplicateProcessing,
                                Date    effectiveTime,
                                String  methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        return this.findBeans(userId,
                              searchString,
                              searchStringParameterName,
                              OpenMetadataAPIMapper.PROJECT_TYPE_GUID,
                              OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                              null,
                              startFrom,
                              pageSize,
                              forLineage,
                              forDuplicateProcessing,
                              effectiveTime,
                              methodName);
    }


    /**
     * Retrieve the list of project metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param nameParameterName parameter supplying name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)

     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>   getProjectsByName(String  userId,
                                       String  name,
                                       String  nameParameterName,
                                       int     startFrom,
                                       int     pageSize,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime,
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.IDENTIFIER_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.NAME_PROPERTY_NAME);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    OpenMetadataAPIMapper.PROJECT_TYPE_GUID,
                                    OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                    specificMatchPropertyNames,
                                    true,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    null,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the list of project metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)

     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>   getProjects(String  userId,
                                 int     startFrom,
                                 int     pageSize,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime,
                                 String  methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        return this.getBeansByType(userId,
                                   OpenMetadataAPIMapper.PROJECT_TYPE_GUID,
                                   OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                   null,
                                   forLineage,
                                   forDuplicateProcessing,
                                   supportedZones,
                                   startFrom,
                                   pageSize,
                                   effectiveTime,
                                   methodName);
    }


    /**
     * Return the projects attached to a supplied actor profile via the project team relationship.
     *
     * @param userId     calling user
     * @param profileGUID identifier for the entity that the projects are attached to
     * @param profileGUIDParameterName name of parameter supplying the GUID
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of objects or null if none found
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getActorProjects(String              userId,
                                     String              profileGUID,
                                     String              profileGUIDParameterName,
                                     int                 startingFrom,
                                     int                 pageSize,
                                     boolean             forLineage,
                                     boolean             forDuplicateProcessing,
                                     Date                effectiveTime,
                                     String              methodName) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        profileGUID,
                                        profileGUIDParameterName,
                                        OpenMetadataAPIMapper.ACTOR_PROFILE_TYPE_NAME,
                                        OpenMetadataAPIMapper.PROJECT_TEAM_RELATIONSHIP_TYPE_GUID,
                                        OpenMetadataAPIMapper.PROJECT_TEAM_RELATIONSHIP_TYPE_NAME,
                                        OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                        null,
                                        null,
                                        1,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Return the projects attached to a supplied person role via the project management relationship.
     *
     * @param userId     calling user
     * @param personRoleGUID identifier for the entity that the projects are attached to
     * @param personRoleGUIDParameterName name of parameter supplying the GUID
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of objects or null if none found
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getRoleProjects(String              userId,
                                     String              personRoleGUID,
                                     String              personRoleGUIDParameterName,
                                     int                 startingFrom,
                                     int                 pageSize,
                                     boolean             forLineage,
                                     boolean             forDuplicateProcessing,
                                     Date                effectiveTime,
                                     String              methodName) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        personRoleGUID,
                                        personRoleGUIDParameterName,
                                        OpenMetadataAPIMapper.PERSON_ROLE_TYPE_NAME,
                                        OpenMetadataAPIMapper.PROJECT_MANAGEMENT_RELATIONSHIP_TYPE_GUID,
                                        OpenMetadataAPIMapper.PROJECT_MANAGEMENT_RELATIONSHIP_TYPE_NAME,
                                        OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                        null,
                                        null,
                                        1,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Return the projects that are dependent on this project.
     *
     * @param userId     calling user
     * @param projectGUID identifier for the entity that the contact details are attached to
     * @param projectGUIDParameterName name of parameter supplying the GUID
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of objects or null if none found
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getDependentProjects(String              userId,
                                         String              projectGUID,
                                         String              projectGUIDParameterName,
                                         int                 startingFrom,
                                         int                 pageSize,
                                         boolean             forLineage,
                                         boolean             forDuplicateProcessing,
                                         Date                effectiveTime,
                                         String              methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        projectGUID,
                                        projectGUIDParameterName,
                                        OpenMetadataAPIMapper.PERSON_ROLE_TYPE_NAME,
                                        OpenMetadataAPIMapper.PROJECT_MANAGEMENT_RELATIONSHIP_TYPE_GUID,
                                        OpenMetadataAPIMapper.PROJECT_MANAGEMENT_RELATIONSHIP_TYPE_NAME,
                                        OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                        null,
                                        null,
                                        1,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Return the projects that this project depends on.
     *
     * @param userId     calling user
     * @param projectGUID identifier for the entity that the contact details are attached to
     * @param projectGUIDParameterName name of parameter supplying the GUID
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of objects or null if none found
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getDependsOnProjects(String              userId,
                                         String              projectGUID,
                                         String              projectGUIDParameterName,
                                         int                 startingFrom,
                                         int                 pageSize,
                                         boolean             forLineage,
                                         boolean             forDuplicateProcessing,
                                         Date                effectiveTime,
                                         String              methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        projectGUID,
                                        projectGUIDParameterName,
                                        OpenMetadataAPIMapper.PERSON_ROLE_TYPE_NAME,
                                        OpenMetadataAPIMapper.PROJECT_MANAGEMENT_RELATIONSHIP_TYPE_GUID,
                                        OpenMetadataAPIMapper.PROJECT_MANAGEMENT_RELATIONSHIP_TYPE_NAME,
                                        OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                        null,
                                        null,
                                        2,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Return the projects that manage this project via the ProjectHierarchy relationship.
     *
     * @param userId     calling user
     * @param projectGUID identifier for the entity that the contact details are attached to
     * @param projectGUIDParameterName name of parameter supplying the GUID
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of objects or null if none found
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getSuperProjects(String              userId,
                                     String              projectGUID,
                                     String              projectGUIDParameterName,
                                     int                 startingFrom,
                                     int                 pageSize,
                                     boolean             forLineage,
                                     boolean             forDuplicateProcessing,
                                     Date                effectiveTime,
                                     String              methodName) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        projectGUID,
                                        projectGUIDParameterName,
                                        OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                        OpenMetadataAPIMapper.PROJECT_HIERARCHY_RELATIONSHIP_TYPE_GUID,
                                        OpenMetadataAPIMapper.PROJECT_HIERARCHY_RELATIONSHIP_TYPE_NAME,
                                        OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                        null,
                                        null,
                                        1,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Return the projects that this project is managing via the ProjectHierarchy relationship.
     *
     * @param userId     calling user
     * @param projectGUID identifier for the entity that the contact details are attached to
     * @param projectGUIDParameterName name of parameter supplying the GUID
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of objects or null if none found
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getSubProjects(String              userId,
                                     String              projectGUID,
                                     String              projectGUIDParameterName,
                                     int                 startingFrom,
                                     int                 pageSize,
                                     boolean             forLineage,
                                     boolean             forDuplicateProcessing,
                                     Date                effectiveTime,
                                     String              methodName) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        projectGUID,
                                        projectGUIDParameterName,
                                        OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                        OpenMetadataAPIMapper.PROJECT_HIERARCHY_RELATIONSHIP_TYPE_GUID,
                                        OpenMetadataAPIMapper.PROJECT_HIERARCHY_RELATIONSHIP_TYPE_NAME,
                                        OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                        null,
                                        null,
                                        2,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Retrieve the project metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param guidParameterName parameter name of guid
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getProjectByGUID(String  userId,
                              String  guid,
                              String  guidParameterName,
                              boolean forLineage,
                              boolean forDuplicateProcessing,
                              Date    effectiveTime,
                              String  methodName) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        return this.getBeanFromRepository(userId,
                                          guid,
                                          guidParameterName,
                                          OpenMetadataAPIMapper.PROJECT_TYPE_NAME,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          effectiveTime,
                                          methodName);
    }
}
