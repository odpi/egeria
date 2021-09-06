/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.server;

import org.odpi.openmetadata.accessservices.governanceprogram.converters.ElementStubConverter;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.ElementStub;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceDefinitionElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.SubjectAreaDefinition;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.SubjectAreaElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.SubjectAreaProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.SubjectAreaDefinitionResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.SubjectAreaListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.SubjectAreaResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SubjectAreaHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
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
    private static GovernanceProgramInstanceHandler instanceHandler = new GovernanceProgramInstanceHandler();

    private static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(SubjectAreaRESTServices.class),
                                                                      instanceHandler.getServiceName());

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

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
     * @param requestBody other properties for a subject area
     *
     * @return unique identifier of the new subjectArea or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public GUIDResponse createSubjectArea(String                serverName,
                                          String                userId,
                                          SubjectAreaProperties requestBody)
    {
        final String methodName = "createSubjectArea";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                SubjectAreaHandler<SubjectAreaElement> handler = instanceHandler.getSubjectAreaHandler(userId, serverName, methodName);

                String subjectAreaGUID = handler.createSubjectArea(userId,
                                                                   null,
                                                                   null,
                                                                   requestBody.getQualifiedName(),
                                                                   requestBody.getDisplayName(),
                                                                   requestBody.getDescription(),
                                                                   requestBody.getUsage(),
                                                                   requestBody.getScope(),
                                                                   requestBody.getDomainIdentifier(),
                                                                   requestBody.getAdditionalProperties(),
                                                                   requestBody.getTypeName(),
                                                                   requestBody.getExtendedProperties(),
                                                                   methodName);

                response.setGUID(subjectAreaGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
                                          SubjectAreaProperties requestBody)
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
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                SubjectAreaHandler<SubjectAreaElement> handler = instanceHandler.getSubjectAreaHandler(userId, serverName, methodName);

                handler.updateSubjectArea(userId,
                                          null,
                                          null,
                                          subjectAreaGUID,
                                          guidParameter,
                                          requestBody.getQualifiedName(),
                                          requestBody.getDisplayName(),
                                          requestBody.getDescription(),
                                          requestBody.getUsage(),
                                          requestBody.getScope(),
                                          requestBody.getDomainIdentifier(),
                                          requestBody.getAdditionalProperties(),
                                          requestBody.getTypeName(),
                                          requestBody.getExtendedProperties(),
                                          isMergeUpdate,
                                          methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
     *
     * @return void or
     *  InvalidParameterException guid or userId is null; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse deleteSubjectArea(String serverName,
                                          String          userId,
                                          String          subjectAreaGUID,
                                          NullRequestBody requestBody)
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

            handler.deleteBeanInRepository(userId,
                                           null,
                                           null,
                                           subjectAreaGUID,
                                           guidParameter,
                                           OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_GUID,
                                           OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_NAME,
                                           null,
                                           null,
                                           methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
     * @param requestBody null requestBody
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse linkSubjectAreasInHierarchy(String          serverName,
                                                    String          userId,
                                                    String          parentSubjectAreaGUID,
                                                    String          childSubjectAreaGUID,
                                                    NullRequestBody requestBody)
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

            handler.linkElementToElement(userId,
                                         null,
                                         null,
                                         parentSubjectAreaGUID,
                                         parentSubjectAreaGUIDParameterName,
                                         OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_NAME,
                                         childSubjectAreaGUID,
                                         childSubjectAreaGUIDParameterName,
                                         OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_NAME,
                                         OpenMetadataAPIMapper.SUBJECT_AREA_HIERARCHY_TYPE_GUID,
                                         OpenMetadataAPIMapper.SUBJECT_AREA_HIERARCHY_TYPE_NAME,
                                         null,
                                         methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
     * @param requestBody null requestBody
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse unlinkSubjectAreasInHierarchy(String          serverName,
                                                      String          userId,
                                                      String          parentSubjectAreaGUID,
                                                      String          childSubjectAreaGUID,
                                                      NullRequestBody requestBody)
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

            handler.unlinkElementFromElement(userId,
                                             false,
                                             null,
                                             null,
                                             parentSubjectAreaGUID,
                                             parentSubjectAreaGUIDParameterName,
                                             OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_NAME,
                                             childSubjectAreaGUID,
                                             childSubjectAreaGUIDParameterName,
                                             OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_GUID,
                                             OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_NAME,
                                             OpenMetadataAPIMapper.SUBJECT_AREA_HIERARCHY_TYPE_GUID,
                                             OpenMetadataAPIMapper.SUBJECT_AREA_HIERARCHY_TYPE_NAME,
                                             null,
                                             methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
     * @param requestBody null requestBody
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse linkSubjectAreaToGovernanceDefinition(String          serverName,
                                                              String          userId,
                                                              String          subjectAreaGUID,
                                                              String          definitionGUID,
                                                              NullRequestBody requestBody)
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

            handler.linkElementToElement(userId,
                                         null,
                                         null,
                                         definitionGUID,
                                         definitionGUIDParameterName,
                                         OpenMetadataAPIMapper.GOVERNANCE_DEFINITION_TYPE_NAME,
                                         subjectAreaGUID,
                                         subjectAreaGUIDParameterName,
                                         OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_NAME,
                                         OpenMetadataAPIMapper.GOVERNED_BY_TYPE_GUID,
                                         OpenMetadataAPIMapper.GOVERNED_BY_TYPE_NAME,
                                         null,
                                         methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
     * @param requestBody null requestBody
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse unlinkSubjectAreaFromGovernanceDefinition(String          serverName,
                                                                  String          userId,
                                                                  String          subjectAreaGUID,
                                                                  String          definitionGUID,
                                                                  NullRequestBody requestBody)
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

            handler.unlinkElementFromElement(userId,
                                             false,
                                             null,
                                             null,
                                             definitionGUID,
                                             definitionGUIDParameterName,
                                             OpenMetadataAPIMapper.GOVERNANCE_DEFINITION_TYPE_NAME,
                                             subjectAreaGUID,
                                             subjectAreaGUIDParameterName,
                                             OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_GUID,
                                             OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_NAME,
                                             OpenMetadataAPIMapper.GOVERNED_BY_TYPE_GUID,
                                             OpenMetadataAPIMapper.GOVERNED_BY_TYPE_NAME,
                                             null,
                                             methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
                                                              OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_NAME,
                                                              methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
                                                       methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
    public SubjectAreaListResponse getSubjectAreasForDomain(String serverName,
                                                            String userId,
                                                            int    domainIdentifier,
                                                            int    startFrom,
                                                            int    pageSize)
    {
        final String methodName = "getSubjectAreasForDomain";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SubjectAreaListResponse response = new SubjectAreaListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaHandler<SubjectAreaElement> handler = instanceHandler.getSubjectAreaHandler(userId, serverName, methodName);

            List<SubjectAreaElement> subjectAreas = handler.getSubjectAreasByDomain(userId,
                                                                                    domainIdentifier,
                                                                                    startFrom,
                                                                                    pageSize,
                                                                                    methodName);

            response.setElementList(subjectAreas);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
                                                                                  OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_NAME,
                                                                                  methodName);
            if (element != null)
            {
                SubjectAreaDefinition subjectAreaDefinition = new SubjectAreaDefinition(element);

                subjectAreaDefinition.setParentSubjectAreaGUID(subjectAreaHandler.getSubjectAreaParentGUID(userId,
                                                                                                           subjectAreaGUID,
                                                                                                           subjectAreaGUIDParameterName,
                                                                                                           methodName));

                subjectAreaDefinition.setNestedSubjectAreaGUIDs(subjectAreaHandler.getSubjectAreaChildrenGUIDs(userId,
                                                                                                               subjectAreaGUID,
                                                                                                               subjectAreaGUIDParameterName,
                                                                                                               new Date(),
                                                                                                               methodName));

                GovernanceDefinitionHandler<GovernanceDefinitionElement> definitionHandler    = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);
                ElementStubConverter<ElementStub>                        elementStubConverter = instanceHandler.getElementStubConverter(userId, serverName, methodName);
                List<Relationship>                                       relationships        = definitionHandler.getGoverningDefinitionLinks(userId,
                                                                                                                                              subjectAreaGUID,
                                                                                                                                              subjectAreaGUIDParameterName,
                                                                                                                                              OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_NAME,
                                                                                                                                              null,
                                                                                                                                              0,
                                                                                                                                              0,
                                                                                                                                              methodName);
                List<ElementStub> definitions = elementStubConverter.getNewBeans(ElementStub.class, relationships, true, methodName);
                subjectAreaDefinition.setAssociatedGovernanceDefinitions(definitions);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
