/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.server;

import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceZoneElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceZoneProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.ZoneListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.ZoneResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceZoneHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;

/**
 * GovernanceZoneRESTServices provides the server side logic for the Governance Zone Manager.
 * It manages the definitions of governance zones and their linkage to the rest of the
 * governance program.
 */
public class GovernanceZoneRESTServices
{
    private static GovernanceProgramInstanceHandler instanceHandler = new GovernanceProgramInstanceHandler();

    private static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(GovernanceZoneRESTServices.class),
                                                                      instanceHandler.getServiceName());

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public GovernanceZoneRESTServices()
    {
    }
    

    /**
     * Create a definition of a governance zone.  The qualified name of these governance zones can be added
     * to the supportedZones and defaultZones properties of an OMAS to control which assets are processed
     * and how they are set up.  In addition the qualified names of zones can be added to Asset definitions
     * to indicate which zone(s) they belong to.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody other properties for a governance zone
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse createGovernanceZone(String                  serverName,
                                             String                   userId,
                                             GovernanceZoneProperties requestBody)
    {
        final String   methodName = "createGovernanceZone";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                GovernanceZoneHandler<GovernanceZoneElement> handler = instanceHandler.getGovernanceZoneHandler(userId, serverName, methodName);

                handler.createGovernanceZone(userId,
                                             null,
                                             null,
                                             requestBody.getQualifiedName(),
                                             requestBody.getDisplayName(),
                                             requestBody.getDescription(),
                                             requestBody.getCriteria(),
                                             requestBody.getScope(),
                                             requestBody.getDomainIdentifier(),
                                             requestBody.getAdditionalProperties(),
                                             requestBody.getExtendedProperties(),
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
     * Return information about all of the governance zones.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param startingFrom position in the list (used when there are so many reports that paging is needed
     * @param maximumResults maximum number of elements to return an this call
     *
     * @return properties of the governance zone or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public ZoneListResponse getGovernanceZones(String   serverName,
                                               String   userId,
                                               int      startingFrom,
                                               int      maximumResults)
    {
        final String   methodName = "getGovernanceZones";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ZoneListResponse response = new ZoneListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceZoneHandler<GovernanceZoneElement> handler = instanceHandler.getGovernanceZoneHandler(userId, serverName, methodName);

            response.setGovernanceZone(handler.getGovernanceZones(userId,
                                                                  startingFrom,
                                                                  maximumResults,
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
     * Return information about a specific governance zone.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param qualifiedName unique name for the zone
     *
     * @return properties of the governance zone or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public ZoneResponse getGovernanceZone(String   serverName,
                                          String   userId,
                                          String   qualifiedName)
    {
        final String   qualifiedNameParameterName = "qualifiedName";
        final String   methodName = "getGovernanceZone";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ZoneResponse response = new ZoneResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceZoneHandler<GovernanceZoneElement> handler = instanceHandler.getGovernanceZoneHandler(userId, serverName, methodName);

            response.setGovernanceZone(handler.getGovernanceZone(userId,
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
}
