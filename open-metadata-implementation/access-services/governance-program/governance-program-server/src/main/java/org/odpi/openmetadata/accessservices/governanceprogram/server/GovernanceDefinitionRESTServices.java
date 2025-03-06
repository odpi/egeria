/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.server;


import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GovernanceDefinitionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceDefinitionElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PeerDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * GovernanceDefinitionManager is the java client for managing the definitions for the governance drivers, policies and controls
 * that define the motivation, goals and implementation approach for the governance program.
 * Governance drivers document of the business strategy and regulations that provide the motivation behind the governance program. They feed
 * into the governance program's policymaking phase ensuring the governance program is focused on activity that delivers value to the organization.
 * A governance driver could be a governance strategy statement, a business imperative, a regulation or a regulation's article.
 * Governance policies define the goals and best practices for the governance program.  There are three types of governance policies:
 * <ul>
 *     <li>
 *         Governance Principles define the invariants that the organization tries to maintain.
 *     </li>
 *     <li>
 *         Governance Obligations define the requirements coming from regulations and policy makers of the organization.
 *     </li>
 *     <li>
 *         Governance Approaches describe preferred approaches and methods to follow
 *     </li>
 * </ul>
 * Within the definition of each governance policy is a description of what the policy is trying to achieve
 * along with the implications to the organization's operation when they adopt this.
 * These implications help to estimate the cost of the policy's implementation and the activities that need to happen.
 * The governance definitions that define how the governance program is to be implemented.
 * There are two types of governance definitions:
 * <ul>
 *     <li>
 *         Technical Controls define the use of technology to implement governance definitions.  They consist of either:
 *         <ul>
 *             <li>
 *                 GovernanceRule - a rule that need to be enforced to support a requirement of the governance program.
 *             </li>
 *             <li>
 *                 GovernanceProcess - a series of automated steps that need to run to support a requirement of the governance program.
 *             </li>
 *         </ul>
 *     </li>
 *     <li>
 *         Organizational controls define roles, teams and manual procedures that implement an aspect of governance. They consist of either:
 *         <ul>
 *             <li>
 *                 GovernanceResponsibility - a set of responsibilities that can be associated with a governance role
 *             </li>
 *             <li>
 *                 GovernanceProcedure - an manual procedure
 *             </li>
 *         </ul>
 *     </li>
 * </ul>
 * Within the definition of each governance definition is a description of what the control is trying to achieve
 * along with the implications to the organization's operation when they adopt this.
 * These implications help to estimate the cost of the control's implementation and the activities that need to happen.
 */
public class GovernanceDefinitionRESTServices
{
    private static final GovernanceProgramInstanceHandler instanceHandler = new GovernanceProgramInstanceHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(GovernanceDefinitionRESTServices.class),
                                                                            instanceHandler.getServiceName());

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();


    /**
     * Default constructor
     */
    public GovernanceDefinitionRESTServices()
    {
    }


    /* ========================================
     * Governance Definitions
     */

    /**
     * Create a new governance definition.  The type of the definition is located in the properties.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody properties of the definition and initial status
     *
     * @return unique identifier of the definition or
     *  InvalidParameterException typeName, documentIdentifier or userId is null; documentIdentifier is not unique; typeName is not valid
     *  PropertyServerException problem accessing the metadata service
     *  UserNotAuthorizedException security access problem
     */
    public GUIDResponse createGovernanceDefinition(String                          serverName,
                                                   String                          userId,
                                                   GovernanceDefinitionRequestBody requestBody)
    {
        final String   methodName = "createGovernanceDefinition";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                if (requestBody.getProperties() != null)
                {
                    auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                    GovernanceDefinitionHandler<GovernanceDefinitionElement> handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

                    GovernanceDefinitionProperties properties =  requestBody.getProperties();
                    String setGUID = handler.createGovernanceDefinition(userId,
                                                                        properties.getDocumentIdentifier(),
                                                                        properties.getTitle(),
                                                                        properties.getSummary(),
                                                                        properties.getDescription(),
                                                                        properties.getScope(),
                                                                        properties.getDomainIdentifier(),
                                                                        properties.getImportance(),
                                                                        properties.getImplications(),
                                                                        properties.getOutcomes(),
                                                                        properties.getResults(),
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        properties.getAdditionalProperties(),
                                                                        properties.getTypeName(),
                                                                        properties.getExtendedProperties(),
                                                                        null,
                                                                        null,
                                                                        new Date(),
                                                                        methodName);

                    response.setGUID(setGUID);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceDefinitionProperties.class.getName(), methodName);
                }
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
     * Update an existing governance definition.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param definitionGUID unique identifier of the definition to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param requestBody properties to update
     *
     * @return void or
     *  InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse  updateGovernanceDefinition(String                          serverName,
                                                    String                          userId,
                                                    String                          definitionGUID,
                                                    boolean                         isMergeUpdate,
                                                    GovernanceDefinitionRequestBody requestBody)
    {
        final String methodName = "updateGovernanceDefinition";
        final String guidParameterName = "definitionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                if (requestBody.getProperties() != null)
                {
                    auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                    GovernanceDefinitionHandler<GovernanceDefinitionElement> handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

                    GovernanceDefinitionProperties properties = requestBody.getProperties();

                    handler.updateGovernanceDefinition(userId,
                                                       definitionGUID,
                                                       guidParameterName,
                                                       properties.getDocumentIdentifier(),
                                                       properties.getTitle(),
                                                       properties.getSummary(),
                                                       properties.getDescription(),
                                                       properties.getScope(),
                                                       properties.getDomainIdentifier(),
                                                       properties.getImportance(),
                                                       properties.getImplications(),
                                                       properties.getOutcomes(),
                                                       properties.getResults(),
                                                       null,
                                                       null,
                                                       null,
                                                       null,
                                                       null,
                                                       null,
                                                       properties.getAdditionalProperties(),
                                                       properties.getTypeName(),
                                                       properties.getExtendedProperties(),
                                                       isMergeUpdate,
                                                       null,
                                                       null,
                                                       false,
                                                       false,
                                                       new Date(),
                                                       methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceDefinitionProperties.class.getName(), methodName);
                }
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
     * Update the status of a governance definition
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param definitionGUID unique identifier
     * @param requestBody new status
     * @return void or
     *  InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse setGovernanceDefinitionStatus(String                      serverName,
                                                      String                      userId,
                                                      String                      definitionGUID,
                                                      GovernanceStatusRequestBody requestBody)
    {
        final String methodName = "setGovernanceDefinitionStatus";
        final String guidParameterName = "definitionGUID";
        final String propertiesParameterName = "newStatus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                GovernanceDefinitionHandler<GovernanceDefinitionElement> handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

                handler.updateBeanStatusInRepository(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     definitionGUID,
                                                     guidParameterName,
                                                     OpenMetadataType.GOVERNANCE_DEFINITION.typeGUID,
                                                     OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                     false,
                                                     false,
                                                     getInstanceStatus(requestBody.getStatus()),
                                                     propertiesParameterName,
                                                     new Date(),
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
     * Convert the governance status into an instance status understood by the repository services.
     *
     * @param governanceDefinitionStatus governance status
     * @return instance status
     */
    private InstanceStatus getInstanceStatus(GovernanceDefinitionStatus governanceDefinitionStatus)
    {
        if (governanceDefinitionStatus != null)
        {
            return switch (governanceDefinitionStatus)
            {
                case DRAFT -> InstanceStatus.DRAFT;
                case ACTIVE -> InstanceStatus.ACTIVE;
                case PROPOSED -> InstanceStatus.PROPOSED;
                case DEPRECATED -> InstanceStatus.DEPRECATED;
                case OTHER -> InstanceStatus.OTHER;
            };
        }

        return null;
    }


    /**
     * Delete a specific governance definition.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param definitionGUID unique identifier of the definition to remove
     * @param requestBody external source request body
     *
     * @return void or
     *  InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse  deleteGovernanceDefinition(String                    serverName,
                                                    String                    userId,
                                                    String                    definitionGUID,
                                                    ExternalSourceRequestBody requestBody)
    {
        final String methodName = "deleteGovernanceDefinition";
        final String guidParameterName = "definitionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler<GovernanceDefinitionElement> handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            handler.removeGovernanceDefinition(userId,
                                               definitionGUID,
                                               guidParameterName,
                                               false,
                                               false,
                                               new Date(),
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
     * Link two related governance definitions together.  The governance definitions are of the same type as follows:
     * <ul>
     *     <li>A relationship of type GovernanceDriverLink is between two GovernanceDriver definitions</li>
     *     <li>A relationship of type GovernancePolicyLink is between two GovernancePolicy definitions</li>
     *     <li>A relationship of type GovernanceControl is between two GovernanceControl definitions</li>
     * </ul>
     * If the link already exists the description is updated.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param definitionOneGUID unique identifier of the first definition
     * @param definitionTwoGUID unique identifier of the second definition
     * @param requestBody description of their relationship
     *
     * @return void or
     *  InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse linkPeerDefinitions(String                  serverName,
                                            String                  userId,
                                            String                  definitionOneGUID,
                                            String                  definitionTwoGUID,
                                            RelationshipRequestBody requestBody)
    {
        final String methodName = "linkPeerDefinitions";
        final String definitionOneGUIDParameterName = "definitionOneGUID";
        final String definitionTwoGUIDParameterName = "definitionTwoGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                GovernanceDefinitionHandler<GovernanceDefinitionElement> handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof PeerDefinitionProperties properties)
                {
                    handler.setupPeerRelationship(userId,
                                                  definitionOneGUID,
                                                  definitionOneGUIDParameterName,
                                                  OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                  definitionTwoGUID,
                                                  definitionTwoGUIDParameterName,
                                                  OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                  requestBody.getRelationshipName(),
                                                  properties.getDescription(),
                                                  properties.getEffectiveFrom(),
                                                  properties.getEffectiveTo(),
                                                  false,
                                                  false,
                                                  new Date(),
                                                  methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.setupPeerRelationship(userId,
                                                  definitionOneGUID,
                                                  definitionOneGUIDParameterName,
                                                  OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                  definitionTwoGUID,
                                                  definitionTwoGUIDParameterName,
                                                  OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                  requestBody.getRelationshipName(),
                                                  null,
                                                  null,
                                                  null,
                                                  false,
                                                  false,
                                                  new Date(),
                                                  methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(PeerDefinitionProperties.class.getName(), methodName);
                }
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
     * Remove the link between two definitions.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param definitionOneGUID unique identifier of the first definition
     * @param definitionTwoGUID unique identifier of the second definition
     * @param requestBody the name of the relationship to delete
     *
     * @return void or
     *  InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse unlinkPeerDefinitions(String                  serverName,
                                              String                  userId,
                                              String                  definitionOneGUID,
                                              String                  definitionTwoGUID,
                                              RelationshipRequestBody requestBody)
    {
        final String methodName = "unlinkPeerDefinitions";

        final String definitionOneGUIDParameterName = "definitionOneGUID";
        final String definitionTwoGUIDParameterName = "definitionTwoGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                GovernanceDefinitionHandler<GovernanceDefinitionElement> handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

                handler.clearDefinitionRelationship(userId,
                                                    definitionOneGUID,
                                                    definitionOneGUIDParameterName,
                                                    OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                    definitionTwoGUID,
                                                    definitionTwoGUIDParameterName,
                                                    OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                    requestBody.getRelationshipName(),
                                                    false,
                                                    false,
                                                    new Date(),
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
     * Create a link to show that a governance definition supports the requirements of another governance definition.
     * This supporting relationship is between definitions of different types as follows:
     * <ul>
     *     <li>A relationship of type GovernanceResponse is between a GovernanceDriver and a GovernancePolicy</li>
     *     <li>A relationship of type GovernanceImplementation is between a GovernancePolicy and a GovernanceControl</li>
     * </ul>
     * If the link already exists the rationale is updated.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param definitionGUID unique identifier of the governance definition
     * @param supportingDefinitionGUID unique identifier of the supporting governance definition
     * @param requestBody description of how the supporting definition provides support
     *
     * @return void or
     *  InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse setupSupportingDefinition(String                  serverName,
                                                  String                  userId,
                                                  String                  definitionGUID,
                                                  String                  supportingDefinitionGUID,
                                                  RelationshipRequestBody requestBody)
    {
        final String methodName = "setupSupportingDefinition";

        final String definitionOneGUIDParameterName = "definitionGUID";
        final String definitionTwoGUIDParameterName = "supportingDefinitionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                GovernanceDefinitionHandler<GovernanceDefinitionElement> handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof SupportingDefinitionProperties properties)
                {
                    handler.setupDelegationRelationship(userId,
                                                        definitionGUID,
                                                        definitionOneGUIDParameterName,
                                                        OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                        supportingDefinitionGUID,
                                                        definitionTwoGUIDParameterName,
                                                        OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                        requestBody.getRelationshipName(),
                                                        properties.getRationale(),
                                                        properties.getEffectiveFrom(),
                                                        properties.getEffectiveTo(),
                                                        false,
                                                        false,
                                                        new Date(),
                                                        methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.setupDelegationRelationship(userId,
                                                        definitionGUID,
                                                        definitionOneGUIDParameterName,
                                                        OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                        supportingDefinitionGUID,
                                                        definitionTwoGUIDParameterName,
                                                        OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                        requestBody.getRelationshipName(),
                                                        null,
                                                        null,
                                                        null,
                                                        false,
                                                        false,
                                                        new Date(),
                                                        methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SupportingDefinitionProperties.class.getName(), methodName);
                }
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
     * Remove the supporting link between two governance definitions.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param definitionGUID unique identifier of the governance definition
     * @param supportingDefinitionGUID unique identifier of the supporting governance definition
     * @param requestBody the name of the relationship to delete
     *
     * @return void or
     *  InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse clearSupportingDefinition(String                  serverName,
                                                  String                  userId,
                                                  String                  definitionGUID,
                                                  String                  supportingDefinitionGUID,
                                                  RelationshipRequestBody requestBody)
    {
        final String methodName = "clearSupportingDefinition";

        final String definitionOneGUIDParameterName = "definitionGUID";
        final String definitionTwoGUIDParameterName = "supportingDefinitionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                GovernanceDefinitionHandler<GovernanceDefinitionElement> handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

                handler.clearDefinitionRelationship(userId,
                                                    definitionGUID,
                                                    definitionOneGUIDParameterName,
                                                    OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                    supportingDefinitionGUID,
                                                    definitionTwoGUIDParameterName,
                                                    OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                    requestBody.getRelationshipName(),
                                                    false,
                                                    false,
                                                    new Date(),
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
}
