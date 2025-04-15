/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.generichandlers.ElementStubConverter;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SubjectAreaHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * SubjectAreaRESTServices provides the server side logic for the Governance SubjectArea Manager.
 * It manages the definitions of subject areas and their linkage to the rest of the
 * governance program.
 */
public class SubjectAreaRESTServices
{
    private static final GovernanceProgramInstanceHandler instanceHandler = new GovernanceProgramInstanceHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(SubjectAreaRESTServices.class),
                                                                            instanceHandler.getServiceName());

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public SubjectAreaRESTServices()
    {
    }


    /**
     * Create a definition of a subject area.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody  properties to store
     *
     * @return unique identifier of the new subjectArea or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public GUIDResponse createSubjectArea(String                   serverName,
                                          String                   userId,
                                          ReferenceableRequestBody requestBody)
    {
        final String methodName = "createSubjectArea";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof SubjectAreaProperties properties)
                {
                    auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                    SubjectAreaHandler<SubjectAreaElement> handler = instanceHandler.getSubjectAreaHandler(userId, serverName, methodName);

                    String subjectAreaGUID = handler.createSubjectArea(userId,
                                                                       requestBody.getExternalSourceGUID(),
                                                                       requestBody.getExternalSourceName(),
                                                                       properties.getQualifiedName(),
                                                                       properties.getSubjectAreaName(),
                                                                       properties.getDisplayName(),
                                                                       properties.getDescription(),
                                                                       properties.getUsage(),
                                                                       properties.getScope(),
                                                                       properties.getDomainIdentifier(),
                                                                       properties.getAdditionalProperties(),
                                                                       properties.getTypeName(),
                                                                       properties.getExtendedProperties(),
                                                                       new Date(),
                                                                       methodName);

                    response.setGUID(subjectAreaGUID);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SubjectAreaProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update the definition of a subjectArea.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param subjectAreaGUID unique identifier of subjectArea
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param requestBody properties to change
     *
     * @return void or
     *  InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse updateSubjectArea(String                serverName,
                                          String                userId,
                                          String                subjectAreaGUID,
                                          boolean               isMergeUpdate,
                                          ReferenceableRequestBody requestBody)
    {
        final String methodName = "updateSubjectArea";
        final String guidParameter = "subjectAreaGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof SubjectAreaProperties)
                {
                    auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                    SubjectAreaHandler<SubjectAreaElement> handler = instanceHandler.getSubjectAreaHandler(userId, serverName, methodName);

                    SubjectAreaProperties properties = (SubjectAreaProperties)requestBody.getProperties();

                    handler.updateSubjectArea(userId,
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              subjectAreaGUID,
                                              guidParameter,
                                              properties.getQualifiedName(),
                                              properties.getSubjectAreaName(),
                                              properties.getDisplayName(),
                                              properties.getDescription(),
                                              properties.getUsage(),
                                              properties.getScope(),
                                              properties.getDomainIdentifier(),
                                              properties.getAdditionalProperties(),
                                              properties.getTypeName(),
                                              properties.getExtendedProperties(),
                                              isMergeUpdate,
                                              methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SubjectAreaProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the definition of a subjectArea.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param subjectAreaGUID unique identifier of subjectArea
     * @param requestBody external source request body
     *
     * @return void or
     *  InvalidParameterException guid or userId is null; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse deleteSubjectArea(String                    serverName,
                                          String                    userId,
                                          String                    subjectAreaGUID,
                                          ExternalSourceRequestBody requestBody)
    {
        final String methodName = "deleteSubjectArea";
        final String guidParameter = "subjectAreaGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaHandler<SubjectAreaElement> handler = instanceHandler.getSubjectAreaHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteBeanInRepository(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               subjectAreaGUID,
                                               guidParameter,
                                               OpenMetadataType.SUBJECT_AREA_DEFINITION.typeGUID,
                                               OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
                                               false,
                                               null,
                                               null,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
            }
            else
            {
                handler.deleteBeanInRepository(userId,
                                               null,
                                               null,
                                               subjectAreaGUID,
                                               guidParameter,
                                               OpenMetadataType.SUBJECT_AREA_DEFINITION.descriptionGUID,
                                               OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
                                               false,
                                               null,
                                               null,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Link two related subject areas together as part of a hierarchy.
     * A subjectArea can only have one parent but many child subjectAreas.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param parentSubjectAreaGUID unique identifier of the parent subjectArea
     * @param childSubjectAreaGUID unique identifier of the child subjectArea
     * @param requestBody relationship requestBody
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse linkSubjectAreasInHierarchy(String                  serverName,
                                                    String                  userId,
                                                    String                  parentSubjectAreaGUID,
                                                    String                  childSubjectAreaGUID,
                                                    RelationshipRequestBody requestBody)
    {
        final String methodName = "linkSubjectAreasInHierarchy";

        final String parentSubjectAreaGUIDParameterName = "parentSubjectAreaGUID";
        final String childSubjectAreaGUIDParameterName = "childSubjectAreaGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaHandler<SubjectAreaElement> handler = instanceHandler.getSubjectAreaHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() != null)
                {
                    handler.linkElementToElement(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 parentSubjectAreaGUID,
                                                 parentSubjectAreaGUIDParameterName,
                                                 OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
                                                 childSubjectAreaGUID,
                                                 childSubjectAreaGUIDParameterName,
                                                 OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
                                                 false,
                                                 false,
                                                 OpenMetadataType.SUBJECT_AREA_HIERARCHY_RELATIONSHIP.typeGUID,
                                                 OpenMetadataType.SUBJECT_AREA_HIERARCHY_RELATIONSHIP.typeName,
                                                 null,
                                                 requestBody.getProperties().getEffectiveFrom(),
                                                 requestBody.getProperties().getEffectiveTo(),
                                                 new Date(),
                                                 methodName);
                }
                else
                {
                    handler.linkElementToElement(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 parentSubjectAreaGUID,
                                                 parentSubjectAreaGUIDParameterName,
                                                 OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
                                                 childSubjectAreaGUID,
                                                 childSubjectAreaGUIDParameterName,
                                                 OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
                                                 false,
                                                 false,
                                                 OpenMetadataType.SUBJECT_AREA_HIERARCHY_RELATIONSHIP.typeGUID,
                                                 OpenMetadataType.SUBJECT_AREA_HIERARCHY_RELATIONSHIP.typeName,
                                                 (InstanceProperties) null,
                                                 null,
                                                 null,
                                                 new Date(),
                                                 methodName);
                }
            }
            else
            {
                handler.linkElementToElement(userId,
                                             null,
                                             null,
                                             parentSubjectAreaGUID,
                                             parentSubjectAreaGUIDParameterName,
                                             OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
                                             childSubjectAreaGUID,
                                             childSubjectAreaGUIDParameterName,
                                             OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
                                             false,
                                             false,
                                             OpenMetadataType.SUBJECT_AREA_HIERARCHY_RELATIONSHIP.typeGUID,
                                             OpenMetadataType.SUBJECT_AREA_HIERARCHY_RELATIONSHIP.typeName,
                                             (InstanceProperties) null,
                                             null,
                                             null,
                                             new Date(),
                                             methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the link between two subjectAreas in the subjectArea hierarchy.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param parentSubjectAreaGUID unique identifier of the parent subjectArea
     * @param childSubjectAreaGUID unique identifier of the child subjectArea
     * @param requestBody relationship requestBody
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse unlinkSubjectAreasInHierarchy(String                  serverName,
                                                      String                  userId,
                                                      String                  parentSubjectAreaGUID,
                                                      String                  childSubjectAreaGUID,
                                                      RelationshipRequestBody requestBody)
    {
        final String methodName = "unlinkSubjectAreasInHierarchy";

        final String parentSubjectAreaGUIDParameterName = "parentSubjectAreaGUID";
        final String childSubjectAreaGUIDParameterName = "childSubjectAreaGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaHandler<SubjectAreaElement> handler = instanceHandler.getSubjectAreaHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.unlinkElementFromElement(userId,
                                                 false,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 parentSubjectAreaGUID,
                                                 parentSubjectAreaGUIDParameterName,
                                                 OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
                                                 childSubjectAreaGUID,
                                                 childSubjectAreaGUIDParameterName,
                                                 OpenMetadataType.SUBJECT_AREA_DEFINITION.typeGUID,
                                                 OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
                                                 false,
                                                 false,
                                                 OpenMetadataType.SUBJECT_AREA_HIERARCHY_RELATIONSHIP.typeGUID,
                                                 OpenMetadataType.SUBJECT_AREA_HIERARCHY_RELATIONSHIP.typeName,
                                                 null,
                                                 methodName);
            }
            else
            {
                handler.unlinkElementFromElement(userId,
                                                 false,
                                                 null,
                                                 null,
                                                 parentSubjectAreaGUID,
                                                 parentSubjectAreaGUIDParameterName,
                                                 OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
                                                 childSubjectAreaGUID,
                                                 childSubjectAreaGUIDParameterName,
                                                 OpenMetadataType.SUBJECT_AREA_DEFINITION.typeGUID,
                                                 OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
                                                 false,
                                                 false,
                                                 OpenMetadataType.SUBJECT_AREA_HIERARCHY_RELATIONSHIP.typeGUID,
                                                 OpenMetadataType.SUBJECT_AREA_HIERARCHY_RELATIONSHIP.typeName,
                                                 null,
                                                 methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Link a subject area to a governance definition that controls how the assets in the subjectArea should be governed.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param subjectAreaGUID unique identifier of the subjectArea
     * @param definitionGUID unique identifier of the governance definition
     * @param requestBody relationship requestBody
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse linkSubjectAreaToGovernanceDefinition(String                  serverName,
                                                              String                  userId,
                                                              String                  subjectAreaGUID,
                                                              String                  definitionGUID,
                                                              RelationshipRequestBody requestBody)
    {
        final String methodName = "linkSubjectAreaToGovernanceDefinition";

        final String subjectAreaGUIDParameterName = "subjectAreaGUID";
        final String definitionGUIDParameterName = "definitionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaHandler<SubjectAreaElement> handler = instanceHandler.getSubjectAreaHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() != null)
                {
                    handler.linkElementToElement(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 subjectAreaGUID,
                                                 subjectAreaGUIDParameterName,
                                                 OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
                                                 definitionGUID,
                                                 definitionGUIDParameterName,
                                                 OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                 false,
                                                 false,
                                                 OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeGUID,
                                                 OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
                                                 null,
                                                 requestBody.getProperties().getEffectiveFrom(),
                                                 requestBody.getProperties().getEffectiveTo(),
                                                 new Date(),
                                                 methodName);
                }
                else
                {
                    handler.linkElementToElement(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 subjectAreaGUID,
                                                 subjectAreaGUIDParameterName,
                                                 OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
                                                 definitionGUID,
                                                 definitionGUIDParameterName,
                                                 OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                 false,
                                                 false,
                                                 OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeGUID,
                                                 OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
                                                 (InstanceProperties) null,
                                                 null,
                                                 null,
                                                 new Date(),
                                                 methodName);
                }
            }
            else
            {
                handler.linkElementToElement(userId,
                                             null,
                                             null,
                                             subjectAreaGUID,
                                             subjectAreaGUIDParameterName,
                                             OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
                                             definitionGUID,
                                             definitionGUIDParameterName,
                                             OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                             false,
                                             false,
                                             OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeGUID,
                                             OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
                                             (InstanceProperties) null,
                                             null,
                                             null,
                                             new Date(),
                                             methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the link between a subjectArea and a governance definition.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param subjectAreaGUID unique identifier of the subjectArea
     * @param definitionGUID unique identifier of the governance definition
     * @param requestBody relationship requestBody
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse unlinkSubjectAreaFromGovernanceDefinition(String                  serverName,
                                                                  String                  userId,
                                                                  String                  subjectAreaGUID,
                                                                  String                  definitionGUID,
                                                                  RelationshipRequestBody requestBody)
    {
        final String methodName = "unlinkSubjectAreaToGovernanceDefinition";

        final String subjectAreaGUIDParameterName = "subjectAreaGUID";
        final String definitionGUIDParameterName = "definitionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaHandler<SubjectAreaElement> handler = instanceHandler.getSubjectAreaHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.unlinkElementFromElement(userId,
                                                 false,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 subjectAreaGUID,
                                                 subjectAreaGUIDParameterName,
                                                 OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
                                                 definitionGUID,
                                                 definitionGUIDParameterName,
                                                 OpenMetadataType.GOVERNANCE_DEFINITION.typeGUID,
                                                 OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                 false,
                                                 false,
                                                 OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeGUID,
                                                 OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
                                                 null,
                                                 methodName);
            }
            else
            {
                handler.unlinkElementFromElement(userId,
                                                 false,
                                                 null,
                                                 null,
                                                 subjectAreaGUID,
                                                 subjectAreaGUIDParameterName,
                                                 OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
                                                 definitionGUID,
                                                 definitionGUIDParameterName,
                                                 OpenMetadataType.GOVERNANCE_DEFINITION.typeGUID,
                                                 OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                 false,
                                                 false,
                                                 OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeGUID,
                                                 OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
                                                 null,
                                                 methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return information about a specific subject area.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param subjectAreaGUID unique identifier for the subjectArea
     *
     * @return properties of the subject area or
     *  InvalidParameterException subjectAreaGUID or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public SubjectAreaResponse getSubjectAreaByGUID(String serverName,
                                                    String userId,
                                                    String subjectAreaGUID)
    {
        final String methodName = "getSubjectAreaByGUID";
        final String subjectAreaGUIDParameterName = "subjectAreaGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SubjectAreaResponse response = new SubjectAreaResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaHandler<SubjectAreaElement> handler = instanceHandler.getSubjectAreaHandler(userId, serverName, methodName);

            response.setElement(handler.getBeanFromRepository(userId,
                                                              subjectAreaGUID,
                                                              subjectAreaGUIDParameterName,
                                                              OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
                                                              false,
                                                              false,
                                                              new Date(),
                                                              methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return information about a specific subject area.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param qualifiedName unique name for the subjectArea
     *
     * @return properties of the subject area or
     *  InvalidParameterException qualifiedName or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public SubjectAreaResponse getSubjectAreaByName(String serverName,
                                                    String userId,
                                                    String qualifiedName)
    {
        final String methodName = "getSubjectAreaByName";
        final String qualifiedNameParameterName = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SubjectAreaResponse response = new SubjectAreaResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaHandler<SubjectAreaElement> handler = instanceHandler.getSubjectAreaHandler(userId, serverName, methodName);

            response.setElement(handler.getSubjectArea(userId,
                                                       qualifiedName,
                                                       qualifiedNameParameterName,
                                                       false,
                                                       false,
                                                       new Date(),
                                                       methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return information about the defined subject areas.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param domainIdentifier identifier for the desired governance domain - 0 for all domains
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the subject area or
     *  InvalidParameterException qualifiedName or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public SubjectAreasResponse getSubjectAreasForDomain(String serverName,
                                                         String userId,
                                                         int    domainIdentifier,
                                                         int    startFrom,
                                                         int    pageSize)
    {
        final String methodName = "getSubjectAreasForDomain";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SubjectAreasResponse response = new SubjectAreasResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaHandler<SubjectAreaElement> handler = instanceHandler.getSubjectAreaHandler(userId, serverName, methodName);

            List<SubjectAreaElement> subjectAreas = handler.getSubjectAreasByDomain(userId,
                                                                                    domainIdentifier,
                                                                                    startFrom,
                                                                                    pageSize,
                                                                                    false,
                                                                                    false,
                                                                                    new Date(),
                                                                                    methodName);

            response.setElements(subjectAreas);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return information about a specific subject area and its linked governance definitions.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param subjectAreaGUID unique identifier for the subjectArea
     *
     * @return properties of the subject area linked to the associated governance definitions or
     *  InvalidParameterException subjectAreaGUID or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public SubjectAreaDefinitionResponse getSubjectAreaDefinitionByGUID(String serverName,
                                                                        String userId,
                                                                        String subjectAreaGUID)
    {
        final String methodName = "getSubjectAreaDefinitionByGUID";
        final String subjectAreaGUIDParameterName = "subjectAreaGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SubjectAreaDefinitionResponse response = new SubjectAreaDefinitionResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaHandler<SubjectAreaElement> subjectAreaHandler = instanceHandler.getSubjectAreaHandler(userId, serverName, methodName);

            SubjectAreaElement element = subjectAreaHandler.getBeanFromRepository(userId,
                                                                                  subjectAreaGUID,
                                                                                  subjectAreaGUIDParameterName,
                                                                                  OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
                                                                                  false,
                                                                                  false,
                                                                                  new Date(),
                                                                                  methodName);
            if (element != null)
            {
                SubjectAreaDefinition subjectAreaDefinition = new SubjectAreaDefinition(element);

                subjectAreaDefinition.setParentSubjectAreaGUID(subjectAreaHandler.getSubjectAreaParentGUID(userId,
                                                                                                           subjectAreaGUID,
                                                                                                           subjectAreaGUIDParameterName,
                                                                                                           false,
                                                                                                           false,
                                                                                                           new Date(),
                                                                                                           methodName));

                subjectAreaDefinition.setNestedSubjectAreaGUIDs(subjectAreaHandler.getSubjectAreaChildrenGUIDs(userId,
                                                                                                               subjectAreaGUID,
                                                                                                               subjectAreaGUIDParameterName,
                                                                                                               false,
                                                                                                               false,
                                                                                                               new Date(),
                                                                                                               methodName));

                GovernanceDefinitionHandler<GovernanceDefinitionElement> definitionHandler    = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);
                ElementStubConverter<ElementStub>                        elementStubConverter = instanceHandler.getElementStubConverter(userId, serverName, methodName);
                List<Relationship>                                       relationships        = definitionHandler.getGoverningDefinitionLinks(userId,
                                                                                                                                              subjectAreaGUID,
                                                                                                                                              subjectAreaGUIDParameterName,
                                                                                                                                              OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
                                                                                                                                              null,
                                                                                                                                              0,
                                                                                                                                              0,
                                                                                                                                              false,
                                                                                                                                              false,
                                                                                                                                              new Date(),
                                                                                                                                              methodName);
                List<ElementStub> definitions = elementStubConverter.getNewBeans(ElementStub.class, relationships, true, methodName);
                subjectAreaDefinition.setAssociatedGovernanceDefinitions(definitions);

                response.setProperties(subjectAreaDefinition);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Add a subject area classification to a referenceable element.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier for the element
     * @param requestBody identifier for a subject area
     *
     * @return void or
     *  InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse addSubjectAreaMemberClassification(String                    serverName,
                                                           String                    userId,
                                                           String                    elementGUID,
                                                           ClassificationRequestBody requestBody)
    {
        final String methodName = "addSubjectAreaMemberClassification";
        final String elementGUIDParameterName = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof SubjectAreaClassificationProperties)
                {
                    SubjectAreaClassificationProperties properties = (SubjectAreaClassificationProperties)requestBody.getProperties();

                    handler.addSubjectAreaClassification(userId,
                                                         requestBody.getExternalSourceGUID(),
                                                         requestBody.getExternalSourceName(),
                                                         elementGUID,
                                                         elementGUIDParameterName,
                                                         OpenMetadataType.REFERENCEABLE.typeName,
                                                         properties.getSubjectAreaName(),
                                                         false,
                                                         false,
                                                         properties.getEffectiveFrom(),
                                                         properties.getEffectiveTo(),
                                                         null,
                                                         methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SubjectAreaClassificationProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove a subject area classification from a referenceable.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier for the element
     * @param requestBody external source request body
     *
     * @return void or
     *  InvalidParameterException guid or userId is null; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse deleteSubjectAreaMemberClassification(String                    serverName,
                                                              String                    userId,
                                                              String                    elementGUID,
                                                              ExternalSourceRequestBody requestBody)
    {
        final String methodName = "deleteSubjectAreaMemberClassification";
        final String elementGUIDParameterName = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeSubjectAreaClassification(userId,
                                                        requestBody.getExternalSourceGUID(),
                                                        requestBody.getExternalSourceName(),
                                                        elementGUID,
                                                        elementGUIDParameterName,
                                                        OpenMetadataType.REFERENCEABLE.typeName,
                                                        false,
                                                        false,
                                                        null,
                                                        methodName);
            }
            else
            {
                handler.removeSubjectAreaClassification(userId,
                                                        null,
                                                        null,
                                                        elementGUID,
                                                        elementGUIDParameterName,
                                                        OpenMetadataType.REFERENCEABLE.typeName,
                                                        false,
                                                        false,
                                                        null,
                                                        methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return information about the contents of a subject area such as the glossaries, reference data sets and quality definitions.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param subjectAreaName unique identifier for the subject area
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of subject area members or
     *  InvalidParameterException qualifiedName or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public ElementStubsResponse getMembersOfSubjectArea(String serverName,
                                                        String userId,
                                                        String subjectAreaName,
                                                        int    startFrom,
                                                        int    pageSize)
    {
        final String methodName = "getMembersOfSubjectArea";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementStubsResponse response = new ElementStubsResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<ElementStub> handler = instanceHandler.getElementStubHandler(userId, serverName, methodName);

            response.setElements(handler.getSubjectAreaMembers(userId,
                                                               subjectAreaName,
                                                               startFrom,
                                                               pageSize,
                                                               false,
                                                               false,
                                                               null,
                                                               methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
