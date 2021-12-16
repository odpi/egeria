/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersErrorCode;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * PersonRoleHandler provides the exchange of metadata about roles between the repository and the OMAS.
 * The PersonRole entity does not support effectivity dates - but appointments - ie links between person roles and actors
 * do need effectivity dates
 *
 * @param <B> class that represents the role
 */
public class PersonRoleHandler<B> extends ReferenceableHandler<B>
{
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
    public PersonRoleHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Create the description of a role.  This is typically a subtype of PersonRole.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software server capability representing the caller
     * @param externalSourceName     unique name of software server capability representing the caller
     * @param qualifiedName unique name for the role - used in other configuration
     * @param name short display name for the role
     * @param description description of the role
     * @param scope the scope of the role
     * @param headCount number of individuals that can be appointed to this role
     * @param headCountLimitSet should the headcount be added to the entity?
     * @param additionalProperties additional properties for a role
     * @param suppliedTypeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for a governance role subtype
     * @param methodName calling method
     *
     * @return unique identifier of the new role object
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createPersonRole(String              userId,
                                   String              externalSourceGUID,
                                   String              externalSourceName,
                                   String              qualifiedName,
                                   String              name,
                                   String              description,
                                   String              scope,
                                   int                 headCount,
                                   boolean             headCountLimitSet,
                                   Map<String, String> additionalProperties,
                                   String              suppliedTypeName,
                                   Map<String, Object> extendedProperties,
                                   Date                effectiveFrom,
                                   Date                effectiveTo,
                                   String              methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataAPIMapper.PERSON_ROLE_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.PERSON_ROLE_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        PersonRoleBuilder roleBuilder = new PersonRoleBuilder(qualifiedName,
                                                              name,
                                                              description,
                                                              scope,
                                                              headCount,
                                                              headCountLimitSet,
                                                              additionalProperties,
                                                              typeGUID,
                                                              typeName,
                                                              extendedProperties,
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);

        roleBuilder.setEffectivityDates(effectiveFrom, effectiveTo);

        return this.createBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           typeGUID,
                                           typeName,
                                           qualifiedName,
                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                           roleBuilder,
                                           methodName);
    }


    /**
     * Create a new metadata element to represent a role using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new role.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software server capability representing the caller
     * @param externalSourceName     unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param qualifiedName unique name for the role - used in other configuration
     * @param name short display name for the role
     * @param description description of the governance role
     * @param headCount number of individuals that can be appointed to this role
     * @param headCountLimitSet should the headcount value be set in the entity?
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createPersonRoleFromTemplate(String  userId,
                                               String  externalSourceGUID,
                                               String  externalSourceName,
                                               String  templateGUID,
                                               String  qualifiedName,
                                               String  name,
                                               String  description,
                                               int     headCount,
                                               boolean headCountLimitSet,
                                               String  methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String templateGUIDParameterName   = "templateGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        PersonRoleBuilder roleBuilder = new PersonRoleBuilder(qualifiedName,
                                                              name,
                                                              description,
                                                              headCount,
                                                              headCountLimitSet,
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);

        return this.createBeanFromTemplate(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           templateGUID,
                                           templateGUIDParameterName,
                                           OpenMetadataAPIMapper.PERSON_ROLE_TYPE_GUID,
                                           OpenMetadataAPIMapper.PERSON_ROLE_TYPE_NAME,
                                           qualifiedName,
                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                           roleBuilder,
                                           methodName);
    }


    /**
     * Link a person role with a Person profile to show that the person has been appointed to role.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software server capability representing the caller
     * @param externalSourceName     unique name of software server capability representing the caller
     * @param profileGUID unique identifier of actor profile
     * @param profileGUIDParameterName parameter name supplying profileGUID
     * @param roleGUID  unique identifier of the person role
     * @param roleGUIDParameterName parameter name supplying roleGUID
     * @param isPublic is this appointment visible to others
     * @param effectiveFrom the official start date of the appointment - null means effective immediately
     * @param effectiveFrom the official end date of the appointment - null means unknown
     * @param methodName calling method
     *
     * @return unique identifier of the appointment relationship
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String appointPersonToRole(String  userId,
                                      String  externalSourceGUID,
                                      String  externalSourceName,
                                      String  profileGUID,
                                      String  profileGUIDParameterName,
                                      String  roleGUID,
                                      String  roleGUIDParameterName,
                                      boolean isPublic,
                                      Date    effectiveFrom,
                                      Date    effectiveTo,
                                      String  methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        PersonRoleBuilder builder = new PersonRoleBuilder(repositoryHelper, serviceName, serverName);

        return this.linkElementToElement(userId,
                                         externalSourceGUID,
                                         externalSourceName,
                                         profileGUID,
                                         profileGUIDParameterName,
                                         OpenMetadataAPIMapper.PERSON_TYPE_NAME,
                                         roleGUID,
                                         roleGUIDParameterName,
                                         OpenMetadataAPIMapper.PERSON_ROLE_TYPE_NAME,
                                         false,
                                         false,
                                         supportedZones,
                                         OpenMetadataAPIMapper.PERSON_ROLE_APPOINTMENT_RELATIONSHIP_TYPE_GUID,
                                         OpenMetadataAPIMapper.PERSON_ROLE_APPOINTMENT_RELATIONSHIP_TYPE_NAME,
                                         builder.getAppointmentProperties(isPublic, effectiveFrom, effectiveTo, methodName),
                                         methodName);
    }


    /**
     * Update the properties in an appointment relationship.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software server capability representing the caller
     * @param externalSourceName     unique name of software server capability representing the caller
     * @param appointmentGUID        relationship GUID
     * @param appointmentGUIDParameterName property for appointmentGUID
     * @param isPublic is this appointment visible to others
     * @param effectiveFrom the official start date of the appointment - null means effective immediately
     * @param effectiveFrom the official end date of the appointment - null means unknown
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateAppointment(String  userId,
                                  String  externalSourceGUID,
                                  String  externalSourceName,
                                  String  appointmentGUID,
                                  String  appointmentGUIDParameterName,
                                  boolean isPublic,
                                  Date    effectiveFrom,
                                  Date    effectiveTo,
                                  boolean isMergeUpdate,
                                  String  methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        PersonRoleBuilder builder = new PersonRoleBuilder(repositoryHelper, serviceName, serverName);

        this.updateRelationshipProperties(userId,
                                             externalSourceGUID,
                                             externalSourceName,
                                             appointmentGUID,
                                             appointmentGUIDParameterName,
                                             OpenMetadataAPIMapper.PERSON_ROLE_APPOINTMENT_RELATIONSHIP_TYPE_NAME,
                                             isMergeUpdate,
                                             builder.getAppointmentProperties(isPublic, effectiveFrom, effectiveTo, methodName),
                                             methodName);
    }


    /**
     * Set an end date on a specific appointment.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software server capability representing the caller
     * @param externalSourceName     unique name of software server capability representing the caller
     * @param profileGUID unique identifier of person profile
     * @param profileGUIDParameterName parameter name supplying profileGUID
     * @param roleGUID  unique identifier of the person role
     * @param roleGUIDParameterName parameter name supplying roleGUID
     * @param appointmentGUID unique identifier (guid) of the appointment relationship
     * @param appointmentGUIDParameterName parameter name supplying appointmentGUID
     * @param endDate the official end of the appointment - null means effective immediately
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void relievePersonFromRole(String userId,
                                      String externalSourceGUID,
                                      String externalSourceName,
                                      String profileGUID,
                                      String profileGUIDParameterName,
                                      String roleGUID,
                                      String roleGUIDParameterName,
                                      String appointmentGUID,
                                      String appointmentGUIDParameterName,
                                      Date   endDate,
                                      String methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        invalidParameterHandler.validateGUID(appointmentGUID, appointmentGUIDParameterName, methodName);

        Relationship relationship = repositoryHandler.getRelationshipByGUID(userId,
                                                                            appointmentGUID,
                                                                            appointmentGUIDParameterName,
                                                                            OpenMetadataAPIMapper.PERSON_ROLE_APPOINTMENT_RELATIONSHIP_TYPE_NAME,
                                                                            null,
                                                                            methodName);


        if ((relationship != null) && (relationship.getEntityOneProxy() != null) && (relationship.getEntityTwoProxy() != null))
        {
            if (roleGUID != null)
            {
                if (! roleGUID.equals(relationship.getEntityTwoProxy().getGUID()))
                {
                    String relationshipTypeName = "<Unknown>";
                    String proxyTypeName        = "<Unknown>";

                    if (relationship.getType() != null)
                    {
                        relationshipTypeName = relationship.getType().getTypeDefName();
                    }
                    if (relationship.getEntityTwoProxy().getType() != null)
                    {
                        proxyTypeName = relationship.getEntityTwoProxy().getType().getTypeDefName();
                    }

                    throw new InvalidParameterException(GenericHandlersErrorCode.WRONG_END_GUID.getMessageDefinition(roleGUIDParameterName,
                                                                                                                     roleGUID,
                                                                                                                     proxyTypeName,
                                                                                                                     relationship.getEntityTwoProxy().getGUID(),
                                                                                                                     Integer.toString(2),
                                                                                                                     relationshipTypeName,
                                                                                                                     appointmentGUIDParameterName,
                                                                                                                     appointmentGUID),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        roleGUIDParameterName);
                }
            }

            if (profileGUID != null)
            {
                if (! profileGUID.equals(relationship.getEntityOneProxy().getGUID()))
                {
                    String relationshipTypeName = "<Unknown>";
                    String proxyTypeName        = "<Unknown>";

                    if (relationship.getType() != null)
                    {
                        relationshipTypeName = relationship.getType().getTypeDefName();
                    }
                    if (relationship.getEntityOneProxy().getType() != null)
                    {
                        proxyTypeName = relationship.getEntityOneProxy().getType().getTypeDefName();
                    }

                    throw new InvalidParameterException(GenericHandlersErrorCode.WRONG_END_GUID.getMessageDefinition(profileGUIDParameterName,
                                                                                                                     profileGUID,
                                                                                                                     proxyTypeName,
                                                                                                                     relationship.getEntityOneProxy().getGUID(),
                                                                                                                     Integer.toString(1),
                                                                                                                     relationshipTypeName,
                                                                                                                     appointmentGUIDParameterName,
                                                                                                                     appointmentGUID),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        roleGUIDParameterName);
                }
            }

            InstanceProperties properties = new InstanceProperties(relationship.getProperties());

            if (endDate == null)
            {
                properties.setEffectiveToTime(new Date());
            }
            else
            {
                properties.setEffectiveToTime(endDate);
            }

            repositoryHandler.updateRelationshipProperties(userId,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           relationship,
                                                           properties,
                                                           methodName);

        }
    }


    /**
     * Link a team leader role to a team profile.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software server capability representing the caller
     * @param externalSourceName     unique name of software server capability representing the caller
     * @param teamLeaderRoleGUID unique identifier of TeamLeader role
     * @param teamLeaderRoleGUIDParameterName parameter name supplying teamLeaderRoleGUID
     * @param teamGUID  unique identifier of the user identity
     * @param teamGUIDParameterName parameter name supplying teamGUID
     * @param position optional name of position
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void addTeamLeader(String userId,
                              String externalSourceGUID,
                              String externalSourceName,
                              String teamLeaderRoleGUID,
                              String teamLeaderRoleGUIDParameterName,
                              String teamGUID,
                              String teamGUIDParameterName,
                              String position,
                              Date   effectiveFrom,
                              Date   effectiveTo,
                              String methodName) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {
        PersonRoleBuilder builder = new PersonRoleBuilder(repositoryHelper, serviceName, serverName);

        InstanceProperties relationshipProperties = builder.getTeamLeadershipProperties(position, methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  teamLeaderRoleGUID,
                                  teamLeaderRoleGUIDParameterName,
                                  OpenMetadataAPIMapper.PERSON_ROLE_TYPE_NAME,
                                  teamGUID,
                                  teamGUIDParameterName,
                                  OpenMetadataAPIMapper.TEAM_TYPE_NAME,
                                  false,
                                  false,
                                  supportedZones,
                                  OpenMetadataAPIMapper.TEAM_LEADERSHIP_RELATIONSHIP_TYPE_GUID,
                                  OpenMetadataAPIMapper.TEAM_LEADERSHIP_RELATIONSHIP_TYPE_NAME,
                                  setUpEffectiveDates(relationshipProperties, effectiveFrom, effectiveTo),
                                  methodName);
    }


    /**
     * Unlink a team leader role from a team profile.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software server capability representing the caller
     * @param externalSourceName     unique name of software server capability representing the caller
     * @param teamLeaderRoleGUID unique identifier of TeamLeader role
     * @param teamLeaderRoleGUIDParameterName parameter name supplying teamLeaderRoleGUID
     * @param teamGUID  unique identifier of the user identity
     * @param teamGUIDParameterName parameter name supplying teamGUID
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void removeTeamLeader(String userId,
                                 String externalSourceGUID,
                                 String externalSourceName,
                                 String teamLeaderRoleGUID,
                                 String teamLeaderRoleGUIDParameterName,
                                 String teamGUID,
                                 String teamGUIDParameterName,
                                 Date   effectiveTime,
                                 String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      teamLeaderRoleGUID,
                                      teamLeaderRoleGUIDParameterName,
                                      OpenMetadataAPIMapper.PERSON_ROLE_TYPE_NAME,
                                      teamGUID,
                                      teamGUIDParameterName,
                                      OpenMetadataAPIMapper.TEAM_TYPE_GUID,
                                      OpenMetadataAPIMapper.TEAM_TYPE_NAME,
                                      false,
                                      false,
                                      supportedZones,
                                      OpenMetadataAPIMapper.TEAM_LEADERSHIP_RELATIONSHIP_TYPE_GUID,
                                      OpenMetadataAPIMapper.TEAM_LEADERSHIP_RELATIONSHIP_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }



    /**
     * Link a team member role to a team profile.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software server capability representing the caller
     * @param externalSourceName     unique name of software server capability representing the caller
     * @param teamMemberRoleGUID unique identifier of TeamMember role
     * @param teamMemberRoleGUIDParameterName parameter name supplying teamMemberRoleGUID
     * @param teamGUID  unique identifier of the user identity
     * @param teamGUIDParameterName parameter name supplying teamGUID
     * @param position optional name of position
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addTeamMember(String userId,
                               String externalSourceGUID,
                               String externalSourceName,
                               String teamMemberRoleGUID,
                               String teamMemberRoleGUIDParameterName,
                               String teamGUID,
                               String teamGUIDParameterName,
                               String position,
                               Date   effectiveFrom,
                               Date   effectiveTo,
                               String methodName) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        PersonRoleBuilder builder = new PersonRoleBuilder(repositoryHelper, serviceName, serverName);

        InstanceProperties relationshipProperties = builder.getTeamMembershipProperties(position, methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  teamMemberRoleGUID,
                                  teamMemberRoleGUIDParameterName,
                                  OpenMetadataAPIMapper.PERSON_ROLE_TYPE_NAME,
                                  teamGUID,
                                  teamGUIDParameterName,
                                  OpenMetadataAPIMapper.TEAM_TYPE_NAME,
                                  false,
                                  false,
                                  supportedZones,
                                  OpenMetadataAPIMapper.TEAM_MEMBERSHIP_RELATIONSHIP_TYPE_GUID,
                                  OpenMetadataAPIMapper.TEAM_MEMBERSHIP_RELATIONSHIP_TYPE_NAME,
                                  setUpEffectiveDates(relationshipProperties, effectiveFrom, effectiveTo),
                                  methodName);
    }


    /**
     * Unlink a team member role from a team profile.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software server capability representing the caller
     * @param externalSourceName     unique name of software server capability representing the caller
     * @param teamMemberRoleGUID unique identifier of TeamMember role
     * @param teamMemberRoleGUIDParameterName parameter name supplying teamMemberRoleGUID
     * @param teamGUID  unique identifier of the user identity
     * @param teamGUIDParameterName parameter name supplying teamGUID
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void removeTeamMember(String userId,
                                 String externalSourceGUID,
                                 String externalSourceName,
                                 String teamMemberRoleGUID,
                                 String teamMemberRoleGUIDParameterName,
                                 String teamGUID,
                                 String teamGUIDParameterName,
                                 Date   effectiveTime,
                                 String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      teamMemberRoleGUID,
                                      teamMemberRoleGUIDParameterName,
                                      OpenMetadataAPIMapper.PERSON_ROLE_TYPE_NAME,
                                      teamGUID,
                                      teamGUIDParameterName,
                                      OpenMetadataAPIMapper.TEAM_TYPE_GUID,
                                      OpenMetadataAPIMapper.TEAM_TYPE_NAME,
                                      false,
                                      false,
                                      supportedZones,
                                      OpenMetadataAPIMapper.TEAM_MEMBERSHIP_RELATIONSHIP_TYPE_GUID,
                                      OpenMetadataAPIMapper.TEAM_MEMBERSHIP_RELATIONSHIP_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }



    /**
     * Update the person role object.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software server capability representing the caller
     * @param externalSourceName     unique name of software server capability representing the caller
     * @param roleGUID unique identifier of the role to update
     * @param roleGUIDParameterName parameter passing the roleGUID
     * @param qualifiedName unique name for the role - used in other configuration
     * @param qualifiedNameParameterName  parameter providing qualified name
     * @param name short display name for the role
     * @param nameParameterName  parameter providing name
     * @param description description of the role
     * @param scope the scope of the role
     * @param headCountLimitSet should the head count be set in the entity?
     * @param headCount number of individuals that can be appointed to this role
     * @param additionalProperties additional properties for a governance role
     * @param typeName type of role
     * @param extendedProperties  properties for a governance role subtype
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param effectiveFrom starting time for this element (null for all time)
     * @param effectiveTo ending time for this element (null for all time)
     * @param methodName calling method
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updatePersonRole(String              userId,
                                 String              externalSourceGUID,
                                 String              externalSourceName,
                                 String              roleGUID,
                                 String              roleGUIDParameterName,
                                 String              qualifiedName,
                                 String              qualifiedNameParameterName,
                                 String              name,
                                 String              nameParameterName,
                                 String              description,
                                 String              scope,
                                 int                 headCount,
                                 boolean             headCountLimitSet,
                                 Map<String, String> additionalProperties,
                                 String              typeName,
                                 Map<String, Object> extendedProperties,
                                 boolean             isMergeUpdate,
                                 Date                effectiveFrom,
                                 Date                effectiveTo,
                                 String              methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(roleGUID, roleGUIDParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
            invalidParameterHandler.validateName(name, nameParameterName, methodName);
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.PERSON_ROLE_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        PersonRoleBuilder roleBuilder = new PersonRoleBuilder(qualifiedName,
                                                              name,
                                                              description,
                                                              scope,
                                                              headCount,
                                                              headCountLimitSet,
                                                              additionalProperties,
                                                              typeGUID,
                                                              typeName,
                                                              extendedProperties,
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);

        roleBuilder.setEffectivityDates(effectiveFrom, effectiveTo);

        Date effectiveTime = this.getEffectiveTime(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    roleGUID,
                                    roleGUIDParameterName,
                                    typeGUID,
                                    typeName,
                                    false,
                                    false,
                                    supportedZones,
                                    roleBuilder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Remove the metadata element representing a role.  This will delete the role and all categories and terms because
     * the Anchors classifications are set up in these elements.
     *
     * @param userId calling user
     * @param roleGUID unique identifier of the metadata element to remove
     * @param roleGUIDParameterName parameter supplying the roleGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removePersonRole(String userId,
                                 String roleGUID,
                                 String roleGUIDParameterName,
                                 String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        this.deleteBeanInRepository(userId,
                                    null,
                                    null,
                                    roleGUID,
                                    roleGUIDParameterName,
                                    OpenMetadataAPIMapper.PERSON_ROLE_TYPE_GUID,
                                    OpenMetadataAPIMapper.PERSON_ROLE_TYPE_NAME,
                                    null,
                                    null,
                                    false,
                                    false,
                                    new Date(),
                                    methodName);
    }


    /**
     * Retrieve the list of role metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param searchStringParameterName name of parameter supplying the search string
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> findPersonRoles(String userId,
                                   String searchString,
                                   String searchStringParameterName,
                                   int    startFrom,
                                   int    pageSize,
                                   Date   effectiveTime,
                                   String methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        return this.findBeans(userId,
                              searchString,
                              searchStringParameterName,
                              OpenMetadataAPIMapper.PERSON_ROLE_TYPE_GUID,
                              OpenMetadataAPIMapper.PERSON_ROLE_TYPE_NAME,
                              null,
                              startFrom,
                              pageSize,
                              effectiveTime,
                              methodName);
    }


    /**
     * Retrieve the list of role metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param nameParameterName parameter supplying name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>  getPersonRolesByName(String userId,
                                         String name,
                                         String nameParameterName,
                                         int    startFrom,
                                         int    pageSize,
                                         Date   effectiveTime,
                                         String methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.NAME_PROPERTY_NAME);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    OpenMetadataAPIMapper.PERSON_ROLE_TYPE_GUID,
                                    OpenMetadataAPIMapper.PERSON_ROLE_TYPE_NAME,
                                    specificMatchPropertyNames,
                                    true,
                                    null,
                                    null,
                                    false,
                                    false,
                                    supportedZones,
                                    null,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Return the bean that represents a person role.
     *
     * @param userId calling user
     * @param personRoleGUID unique identifier of the role
     * @param personRoleGUIDParameterName name of the parameter that supplied the GUID
     * @param effectiveTime when should this bean be retrieved from
     * @param methodName calling method
     *
     * @return bean
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getPersonRoleByGUID(String userId,
                                 String personRoleGUID,
                                 String personRoleGUIDParameterName,
                                 Date   effectiveTime,
                                 String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        return getBeanFromRepository(userId,
                                     personRoleGUID,
                                     personRoleGUIDParameterName,
                                     OpenMetadataAPIMapper.PERSON_TYPE_NAME,
                                     false,
                                     false,
                                     effectiveTime,
                                     methodName);
    }


    /**
     * Retrieve the list of role metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param nameParameterName parameter supplying name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> getPersonRolesForRoleId(String userId,
                                           String name,
                                           String nameParameterName,
                                           int    startFrom,
                                           int    pageSize,
                                           Date   effectiveTime,
                                           String methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    OpenMetadataAPIMapper.PERSON_ROLE_TYPE_GUID,
                                    OpenMetadataAPIMapper.PERSON_ROLE_TYPE_NAME,
                                    specificMatchPropertyNames,
                                    true,
                                    null,
                                    null,
                                    false,
                                    false,
                                    supportedZones,
                                    null,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the list of role metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param nameParameterName parameter supplying name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> getPersonRolesForTitle(String userId,
                                          String name,
                                          String nameParameterName,
                                          int    startFrom,
                                          int    pageSize,
                                          Date   effectiveTime,
                                          String methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.NAME_PROPERTY_NAME);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    OpenMetadataAPIMapper.PERSON_ROLE_TYPE_GUID,
                                    OpenMetadataAPIMapper.PERSON_ROLE_TYPE_NAME,
                                    specificMatchPropertyNames,
                                    true,
                                    null,
                                    null,
                                    false,
                                    false,
                                    supportedZones,
                                    null,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the list of role metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param domainIdentifier domain of interest - 0 means all domains
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> getPersonRolesForDomainId(String userId,
                                             int    domainIdentifier,
                                             int    startFrom,
                                             int    pageSize,
                                             Date   effectiveTime,
                                             String methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        /*
         * A governance domain identifier of 0 is ignored in the retrieval because it means return for all domains.
         */
        if (domainIdentifier == 0)
        {
            return this.getBeansByType(userId,
                                       OpenMetadataAPIMapper.PERSON_ROLE_TYPE_GUID,
                                       OpenMetadataAPIMapper.PERSON_ROLE_TYPE_NAME,
                                       null,
                                       false,
                                       false,
                                       supportedZones,
                                       startFrom,
                                       pageSize,
                                       effectiveTime,
                                       methodName);
        }

        return this.getBeansByIntValue(userId,
                                       domainIdentifier,
                                       OpenMetadataAPIMapper.PERSON_ROLE_TYPE_GUID,
                                       OpenMetadataAPIMapper.PERSON_ROLE_TYPE_NAME,
                                       OpenMetadataAPIMapper.DOMAIN_IDENTIFIER_PROPERTY_NAME,
                                       null,
                                       null,
                                       false,
                                       false,
                                       supportedZones,
                                       null,
                                       startFrom,
                                       pageSize,
                                       effectiveTime,
                                       methodName);
    }
}
