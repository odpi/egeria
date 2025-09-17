/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.securityofficer.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneHierarchyProperties;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;



/**
 * The SecurityOfficerRESTServices provides the server-side implementation of the Security Officer Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class SecurityOfficerRESTServices extends TokenController
{
    private static final SecurityOfficerInstanceHandler instanceHandler = new SecurityOfficerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(SecurityOfficerRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public SecurityOfficerRESTServices()
    {
    }



    /**
     * Attach governance zones in a hierarchy.
     *
     * @param serverName         name of called server
     * @param governanceZoneGUID    unique identifier of the parent governance zone.
     * @param nestedGovernanceZoneGUID    unique identifier of the nested governance zone.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkGovernanceZones(String                     serverName,
                                            String                     governanceZoneGUID,
                                            String                     nestedGovernanceZoneGUID,
                                            NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkGovernanceZones";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ZoneHierarchyProperties zoneHierarchyProperties)
                {
                    handler.linkGovernanceZones(userId,
                                                governanceZoneGUID,
                                                nestedGovernanceZoneGUID,
                                                requestBody,
                                                zoneHierarchyProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkGovernanceZones(userId,
                                                governanceZoneGUID,
                                                nestedGovernanceZoneGUID,
                                                requestBody,
                                                null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ZoneHierarchyProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkGovernanceZones(userId,
                                         governanceZoneGUID,
                                         nestedGovernanceZoneGUID,
                                         null,
                                         null);
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
     * Detach governance zone from a hierarchical relationship.
     *
     * @param serverName         name of called server
     * @param governanceZoneGUID    unique identifier of the parent governance zone.
     * @param nestedGovernanceZoneGUID    unique identifier of the nested governance zone.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachGovernanceZones(String                        serverName,
                                              String                        governanceZoneGUID,
                                              String                        nestedGovernanceZoneGUID,
                                              DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachGovernanceZones";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            handler.detachGovernanceZones(userId, governanceZoneGUID, nestedGovernanceZoneGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

}
