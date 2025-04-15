/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

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
     * @param projectPhase lifecycle phase of project
     * @param projectHealth how well is the project tracking to plan
     * @param projectStatus status of the project
     * @param priority priority of project
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
                                String              projectPhase,
                                String              projectHealth,
                                String              projectStatus,
                                int                 priority,
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

        String typeName = OpenMetadataType.PROJECT.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.PROJECT.typeName,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        ProjectBuilder builder = new ProjectBuilder(qualifiedName,
                                                    identifier,
                                                    name,
                                                    description,
                                                    startDate,
                                                    plannedEndDate,
                                                    projectPhase,
                                                    projectHealth,
                                                    projectStatus,
                                                    priority,
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
     * @param projectPhase lifecycle phase of project
     * @param projectHealth how well is the project tracking to plan
     * @param projectStatus status of the project
     * @param priority priority of project
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
                                String              projectPhase,
                                String              projectHealth,
                                String              projectStatus,
                                int                 priority,
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

        if (!isMergeUpdate)
        {
            invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        }

        String typeName = OpenMetadataType.PROJECT.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.PROJECT.typeName,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        ProjectBuilder builder = new ProjectBuilder(qualifiedName,
                                                    identifier,
                                                    name,
                                                    description,
                                                    startDate,
                                                    plannedEndDate,
                                                    projectPhase,
                                                    projectHealth,
                                                    projectStatus,
                                                    priority,
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
                                                                                     OpenMetadataProperty.TEAM_ROLE.name,
                                                                                     teamRole,
                                                                                     methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  projectGUID,
                                  projectGUIDParameterName,
                                  OpenMetadataType.PROJECT.typeName,
                                  actorProfileGUID,
                                  actorProfileGUIDParameterName,
                                  OpenMetadataType.ACTOR.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.PROJECT_TEAM_RELATIONSHIP.typeGUID,
                                  OpenMetadataType.PROJECT_TEAM_RELATIONSHIP.typeName,
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
                                      OpenMetadataType.PROJECT.typeName,
                                      actorProfileGUID,
                                      actorProfileGUIDParameterName,
                                      OpenMetadataType.ACTOR.typeGUID,
                                      OpenMetadataType.ACTOR.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.PROJECT_TEAM_RELATIONSHIP.typeGUID,
                                      OpenMetadataType.PROJECT_TEAM_RELATIONSHIP.typeName,
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
                                  OpenMetadataType.PROJECT.typeName,
                                  personRoleGUID,
                                  personRoleGUIDParameterName,
                                  OpenMetadataType.PERSON_ROLE.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.PROJECT_MANAGEMENT_RELATIONSHIP.typeGUID,
                                  OpenMetadataType.PROJECT_MANAGEMENT_RELATIONSHIP.typeName,
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
                                      OpenMetadataType.PROJECT.typeName,
                                      personRoleGUID,
                                      personRoleGUIDParameterName,
                                      OpenMetadataType.PERSON_ROLE.typeGUID,
                                      OpenMetadataType.PERSON_ROLE.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.PROJECT_MANAGEMENT_RELATIONSHIP.typeGUID,
                                      OpenMetadataType.PROJECT_MANAGEMENT_RELATIONSHIP.typeName,
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
     * @param cascadedDelete     boolean indicating whether the delete request can cascade to dependent elements
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
                              boolean cascadedDelete,
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
                                    OpenMetadataType.PROJECT.typeGUID,
                                    OpenMetadataType.PROJECT.typeName,
                                    cascadedDelete,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }
}
