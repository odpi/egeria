/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.solutionarchitect.server;


import org.odpi.openmetadata.accessservices.digitalarchitecture.client.SolutionManager;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The SolutionArchitectRESTServices provides the server-side implementation of the Solution Architect Open Metadata
 * View Service (OMVS).  This interface provides access to information supply chains, solution blueprints,
 * solution components and solution roles.
 */
public class SolutionArchitectRESTServices extends TokenController
{
    private static final SolutionArchitectInstanceHandler instanceHandler = new SolutionArchitectInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(SolutionArchitectRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public SolutionArchitectRESTServices()
    {
    }


    /**
     * Retrieve the list of information supply chain metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param addImplementation should details of the implementation of the information supply chain be extracted too?
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public InformationSupplyChainsResponse findInformationSupplyChains(String            serverName,
                                                                       boolean           addImplementation,
                                                                       boolean           startsWith,
                                                                       boolean           endsWith,
                                                                       boolean           ignoreCase,
                                                                       int               startFrom,
                                                                       int               pageSize,
                                                                       FilterRequestBody requestBody)
    {
        final String methodName = "findInformationSupplyChains";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        InformationSupplyChainsResponse response = new InformationSupplyChainsResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findInformationSupplyChains(userId,
                                                                         instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                                         addImplementation,
                                                                         null,
                                                                         null,
                                                                         SequencingOrder.CREATION_DATE_RECENT,
                                                                         null,
                                                                         startFrom,
                                                                         pageSize,
                                                                         new Date()));
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
     * Retrieve the list of solution blueprint metadata elements that contain the search string.  The returned blueprints include a list of the components that are associated with it.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SolutionBlueprintsResponse findSolutionBlueprints(String            serverName,
                                                             boolean           startsWith,
                                                             boolean           endsWith,
                                                             boolean           ignoreCase,
                                                             int               startFrom,
                                                             int               pageSize,
                                                             FilterRequestBody requestBody)
    {
        final String methodName = "findSolutionBlueprints";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SolutionBlueprintsResponse response = new SolutionBlueprintsResponse();
        AuditLog                   auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findSolutionBlueprints(userId,
                                                                    instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                                    null,
                                                                    null,
                                                                    SequencingOrder.CREATION_DATE_RECENT,
                                                                    null,
                                                                    startFrom,
                                                                    pageSize,
                                                                    new Date()));
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
     * Retrieve the list of actor roles metadata elements that contain the search string and show which solution components (if any) are attached to it.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SolutionRolesResponse findSolutionRoles(String            serverName,
                                                   boolean           startsWith,
                                                   boolean           endsWith,
                                                   boolean           ignoreCase,
                                                   int               startFrom,
                                                   int               pageSize,
                                                   FilterRequestBody requestBody)
    {
        final String methodName = "findSolutionRoles";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SolutionRolesResponse response = new SolutionRolesResponse();
        AuditLog              auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findSolutionRoles(userId,
                                                               instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                               null,
                                                               null,
                                                               SequencingOrder.CREATION_DATE_RECENT,
                                                               null,
                                                               startFrom,
                                                               pageSize,
                                                               new Date()));
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
     * Retrieve the list of solution component metadata elements that contain the search string.  The solutions components returned include information about the consumers, actors and other solution components that are associated with them.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SolutionComponentsResponse findSolutionComponents(String            serverName,
                                                             boolean           startsWith,
                                                             boolean           endsWith,
                                                             boolean           ignoreCase,
                                                             int               startFrom,
                                                             int               pageSize,
                                                             FilterRequestBody requestBody)
    {
        final String methodName = "findSolutionComponents";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SolutionComponentsResponse response = new SolutionComponentsResponse();
        AuditLog                   auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findSolutionComponents(userId,
                                                                    instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                                    null,
                                                                    null,
                                                                    SequencingOrder.CREATION_DATE_RECENT,
                                                                    null,
                                                                    startFrom,
                                                                    pageSize,
                                                                    new Date()));
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
     * Retrieve the list of  metadata elements that are associated with the solution component via the ImplementedBy relationship.
     *
     * @param serverName name of the service to route the request to
     * @param solutionComponentGUID unique identifier of the solution component to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementsResponse getSolutionComponentImplementations(String             serverName,
                                                                               String             solutionComponentGUID,
                                                                               int                startFrom,
                                                                               int                pageSize,
                                                                               ResultsRequestBody requestBody)
    {
        final String methodName = "getSolutionComponentImplementations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RelatedMetadataElementsResponse response = new RelatedMetadataElementsResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody == null)
            {
                response.setElementList(handler.getSolutionComponentImplementations(userId,
                                                                                    solutionComponentGUID,
                                                                                    null,
                                                                                    null,
                                                                                    SequencingOrder.CREATION_DATE_RECENT,
                                                                                    null,
                                                                                    startFrom,
                                                                                    pageSize,
                                                                                    new Date()));
            }
            else
            {
                response.setElementList(handler.getSolutionComponentImplementations(userId,
                                                                                    solutionComponentGUID,
                                                                                    requestBody.getLimitResultsByStatus(),
                                                                                    requestBody.getAsOfTime(),
                                                                                    requestBody.getSequencingOrder(),
                                                                                    requestBody.getSequencingProperty(),
                                                                                    startFrom,
                                                                                    pageSize,
                                                                                    requestBody.getEffectiveTime()));
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
