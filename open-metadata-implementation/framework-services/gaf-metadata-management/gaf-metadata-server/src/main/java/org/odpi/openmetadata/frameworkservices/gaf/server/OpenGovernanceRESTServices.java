/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceActionHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworkservices.gaf.handlers.MetadataElementHandler;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The OpenGovernanceRESTServices provides the server-side implementation of the services used by the governance
 * engine as it is managing requests to execute open governance services in the governance server.
 * These services align with the interface definitions from the Governance Action Framework (GAF).
 */
public class OpenGovernanceRESTServices
{
    private final static GAFMetadataManagementInstanceHandler instanceHandler = new GAFMetadataManagementInstanceHandler();

    private final        RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
    private final static RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(OpenGovernanceRESTServices.class),
                                                                                        instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public OpenGovernanceRESTServices()
    {
    }


    /**
     * Link elements as peer duplicates. Create a simple relationship between two elements.
     * If the relationship already exists, the properties are updated.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody parameters for the relationship
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid, or the elements are of different types
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse linkElementsAsDuplicates(String                    serverName,
                                                 String                    userId,
                                                 PeerDuplicatesRequestBody requestBody)
    {
        final String methodName = "linkElementsAsDuplicates";

        final String element1GUIDParameterName = "element1GUID";
        final String element2GUIDParameterName = "element2GUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.linkElementsAsPeerDuplicates(userId,
                                                     requestBody.getMetadataElement1GUID(),
                                                     element1GUIDParameterName,
                                                     requestBody.getMetadataElement2GUID(),
                                                     element2GUIDParameterName,
                                                     requestBody.getSetKnownDuplicate(),
                                                     requestBody.getStatusIdentifier(),
                                                     requestBody.getSteward(),
                                                     requestBody.getStewardTypeName(),
                                                     requestBody.getStewardPropertyName(),
                                                     requestBody.getSource(),
                                                     requestBody.getNotes(),
                                                     instanceHandler.getSupportedZones(userId, serverName, methodName),
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
     * Identify an element that acts as a consolidated version for a set of duplicate elements.
     * (The consolidated element is created using createMetadataElement.)
     * Creates a simple relationship between the elements. If the ConsolidatedDuplicate
     * classification already exists, the properties are updated.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody parameters for the relationship
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid, or the elements are of different types
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse linkConsolidatedDuplicate(String                            serverName,
                                                  String                            userId,
                                                  ConsolidatedDuplicatesRequestBody requestBody)
    {
        final String methodName = "linkConsolidatedDuplicate";

        final String elementGUIDParameterName = "consolidatedElementGUID";
        final String sourceElementGUIDsParameterName = "sourceElementGUIDs";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.linkConsolidatedDuplicate(userId,
                                                  requestBody.getConsolidatedElementGUID(),
                                                  elementGUIDParameterName,
                                                  requestBody.getStatusIdentifier(),
                                                  requestBody.getSteward(),
                                                  requestBody.getStewardTypeName(),
                                                  requestBody.getStewardPropertyName(),
                                                  requestBody.getSource(),
                                                  requestBody.getNotes(),
                                                  requestBody.getSourceElementGUIDs(),
                                                  sourceElementGUIDsParameterName,
                                                  instanceHandler.getSupportedZones(userId, serverName,methodName),
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
     * Update the status of a specific action target. By default, these values are derived from
     * the values for the governance action service.  However, if the governance action service has to process name
     * target elements, then setting the status on each individual target will show the progress of the
     * governance action service.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param requestBody relationship properties
     *
     * @return void or
     *
     *  InvalidParameterException the action target GUID is not recognized
     *  UserNotAuthorizedException the governance action service is not authorized to update the action target properties
     *  PropertyServerException there is a problem connecting to the metadata store
     */
    public VoidResponse updateActionTargetStatus(String                        serverName,
                                                 String                        userId,
                                                 ActionTargetStatusRequestBody requestBody)
    {
        final String methodName = "updateActionTargetStatus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GovernanceActionHandler<Object> handler = instanceHandler.getGovernanceActionHandler(userId, serverName, methodName);

                int statusOrdinal = GovernanceActionStatus.ACTIONED.getOpenTypeOrdinal();

                if (requestBody.getStatus() != null)
                {
                    statusOrdinal = requestBody.getStatus().getOpenTypeOrdinal();
                }
                handler.updateActionTargetStatus(userId,
                                                 requestBody.getActionTargetGUID(),
                                                 statusOrdinal,
                                                 requestBody.getStartDate(),
                                                 requestBody.getCompletionDate(),
                                                 requestBody.getCompletionMessage(),
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
     * Declare that all the processing for the governance action service is finished and the status of the work.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param governanceActionGUID unique identifier of the governance action to update
     * @param requestBody completion status enum value, optional guard strings for triggering subsequent action(s) plus
     *                    a list of additional elements to add to the action targets for the next phase
     *
     * @return void or
     *
     *  InvalidParameterException the completion status is null
     *  UserNotAuthorizedException the governance action service is not authorized to update the governance action service status
     *  PropertyServerException there is a problem connecting to the metadata store
     */
    public VoidResponse recordCompletionStatus(String                      serverName,
                                               String                      userId,
                                               String                      governanceActionGUID,
                                               CompletionStatusRequestBody requestBody)
    {
        final String methodName = "recordCompletionStatus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GovernanceActionHandler<Object> handler = instanceHandler.getGovernanceActionHandler(userId, serverName, methodName);

                int statusOrdinal = GovernanceActionStatus.ACTIONED.getOpenTypeOrdinal();

                if (requestBody.getStatus() != null)
                {
                    statusOrdinal = requestBody.getStatus().getOpenTypeOrdinal();
                }

                handler.recordCompletionStatus(userId,
                                               governanceActionGUID,
                                               statusOrdinal,
                                               requestBody.getRequestParameters(),
                                               requestBody.getOutputGuards(),
                                               requestBody.getNewActionTargets(),
                                               requestBody.getCompletionMessage(),
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
     * Create a governance action in the metadata store which will trigger the governance action service
     * associated with the supplied request type.  The governance action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestBody properties for the governance action and to pass to the governance action service
     *
     * @return unique identifier of the governance action or
     *
     *  InvalidParameterException null qualified name
     *  UserNotAuthorizedException this governance action service is not authorized to create a governance action
     *  PropertyServerException there is a problem with the metadata store
     */
    public GUIDResponse initiateGovernanceAction(String                      serverName,
                                                 String                      userId,
                                                 String                      governanceEngineName,
                                                 GovernanceActionRequestBody requestBody)
    {
        final String methodName = "initiateGovernanceAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GovernanceActionHandler<Object> handler = instanceHandler.getGovernanceActionHandler(userId, serverName, methodName);

                String governanceActionGUID = handler.createGovernanceAction(userId,
                                                                             requestBody.getQualifiedName(),
                                                                             requestBody.getDomainIdentifier(),
                                                                             requestBody.getDisplayName(),
                                                                             requestBody.getDescription(),
                                                                             requestBody.getRequestSourceGUIDs(),
                                                                             requestBody.getActionTargets(),
                                                                             null,
                                                                             requestBody.getReceivedGuards(),
                                                                             requestBody.getStartTime(),
                                                                             governanceEngineName,
                                                                             requestBody.getRequestType(),
                                                                             requestBody.getRequestParameters(),
                                                                             null,
                                                                             null,
                                                                             null,
                                                                             requestBody.getProcessName(),
                                                                             requestBody.getRequestSourceName(),
                                                                             requestBody.getOriginatorServiceName(),
                                                                             requestBody.getOriginatorEngineName(),
                                                                             methodName);

                if (governanceActionGUID != null)
                {
                    /*
                     * Since there is no process control, the governance action moves immediately into APPROVED
                     * status, and it is picked up by the listening engine hosts.
                     */
                    handler.approveGovernanceAction(userId,
                                                    governanceActionGUID,
                                                    requestBody.getQualifiedName(),
                                                    null,
                                                    requestBody.getReceivedGuards(),
                                                    requestBody.getStartTime(),
                                                    governanceEngineName,
                                                    requestBody.getRequestType(),
                                                    requestBody.getRequestParameters(),
                                                    null,
                                                    requestBody.getProcessName(),
                                                    methodName);

                    response.setGUID(governanceActionGUID);
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
     * Using the named governance action process as a template, initiate a chain of governance actions.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param requestBody properties to initiate the new instance of the process
     *
     * @return unique identifier of the first governance action of the process or
     *
     *  InvalidParameterException null or unrecognized qualified name of the process
     *  UserNotAuthorizedException this governance action service is not authorized to create a governance action process
     *  PropertyServerException there is a problem with the metadata store
     */
    public GUIDResponse initiateGovernanceActionProcess(String                             serverName,
                                                        String                             userId,
                                                        GovernanceActionProcessRequestBody requestBody)
    {
        final String methodName = "initiateGovernanceActionProcess";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GovernanceActionHandler<Object> handler = instanceHandler.getGovernanceActionHandler(userId, serverName, methodName);

                response.setGUID(handler.initiateGovernanceActionProcess(userId,
                                                                         requestBody.getProcessQualifiedName(),
                                                                         requestBody.getRequestSourceGUIDs(),
                                                                         requestBody.getActionTargets(),
                                                                         requestBody.getRequestParameters(),
                                                                         requestBody.getStartTime(),
                                                                         requestBody.getOriginatorServiceName(),
                                                                         requestBody.getOriginatorEngineName(),
                                                                         methodName));
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
