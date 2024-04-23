/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.oif.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.SoftwareCapabilityHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.integration.properties.IntegrationReportProperties;
import org.odpi.openmetadata.frameworkservices.oif.handlers.OpenIntegrationHandler;
import org.odpi.openmetadata.frameworkservices.oif.rest.CatalogTargetsResponse;
import org.odpi.openmetadata.frameworkservices.oif.rest.IntegrationReportResponse;
import org.odpi.openmetadata.frameworkservices.oif.rest.IntegrationReportsResponse;
import org.odpi.openmetadata.frameworkservices.oif.rest.MetadataSourceRequestBody;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The OpenIntegrationRESTServices provides the server-side implementation of the services used by the governance
 * engine as it is managing requests to execute open governance services in the governance server.
 * These services align with the interface definitions from the Open Integration Framework (OIF).
 */
public class OpenIntegrationRESTServices
{
    private final static OpenIntegrationInstanceHandler instanceHandler = new OpenIntegrationInstanceHandler();

    private final        RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
    private final static RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(OpenIntegrationRESTServices.class),
                                                                                        instanceHandler.getServiceName());



    /**
     * Default constructor
     */
    public OpenIntegrationRESTServices()
    {
    }


    /**
     * Retrieve the unique identifier of the external metadata source.
     *
     * @param serverName name of the server to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param requestBody unique name of the software capability
     *
     * @return unique identifier of the integration daemon's software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    public GUIDResponse getMetadataSourceGUID(String          serverName,
                                              String          serviceURLMarker,
                                              String          userId,
                                              NameRequestBody requestBody)
    {
        final String methodName = "getMetadataSourceGUID";
        final String parameterName = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SoftwareCapabilityHandler<Object> handler = instanceHandler.getMetadataSourceHandler(userId, serverName, methodName);

                response.setGUID(handler.getBeanGUIDByQualifiedName(userId,
                                                                    OpenMetadataType.SOFTWARE_CAPABILITY.typeGUID,
                                                                    OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                    requestBody.getName(),
                                                                    parameterName,
                                                                    false,
                                                                    false,
                                                                    new Date(),
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


    /**
     * Create a new metadata element to represent a software capability.  This describes the metadata source.
     *
     * @param serverName name of the server to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param requestBody properties of the software capability
     *
     * @return unique identifier of the integration daemon's software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    @SuppressWarnings(value = "unused")
    public GUIDResponse createMetadataSource(String                    serverName,
                                             String                    serviceURLMarker,
                                             String                    userId,
                                             MetadataSourceRequestBody requestBody)
    {
        final String methodName = "createMetadataSource";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SoftwareCapabilityHandler<Object> handler = instanceHandler.getMetadataSourceHandler(userId, serverName, methodName);

                response.setGUID(handler.createSoftwareCapability(userId,
                                                                  null,
                                                                  null,
                                                                  requestBody.getTypeName(),
                                                                  requestBody.getClassificationName(),
                                                                  requestBody.getQualifiedName(),
                                                                  null,
                                                                  null,
                                                                  requestBody.getDeployedImplementationType(),
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  false,
                                                                  false,
                                                                  new Date(),
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


    /**
     * Retrieve the identifiers of the metadata elements identified as catalog targets with an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the integration connector definition.
     */
    public CatalogTargetsResponse  getCatalogTargets(String  serverName,
                                                     String  serviceURLMarker,
                                                     String  userId,
                                                     String  integrationConnectorGUID,
                                                     int     startingFrom,
                                                     int     maximumResults)
    {
        final String methodName = "getCatalogTargets";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        CatalogTargetsResponse response = new CatalogTargetsResponse();
        AuditLog                             auditLog = null;

        try
        {
            OpenIntegrationHandler handler = instanceHandler.getOpenIntegrationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName,methodName);
            response.setElements(handler.getCatalogTargets(userId, integrationConnectorGUID, startingFrom, maximumResults));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new integration report for an element (identified by anchorGUID).
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param anchorGUID element to attach the integration report to
     * @param anchorTypeName typeName of the anchor for the integration report
     * @param properties properties of the report
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid,
     *  UserNotAuthorizedException user not authorized to issue this request,
     *  PropertyServerException problem with the metadata server.
     */
    public VoidResponse publishIntegrationReport(String                      serverName,
                                                 String                      serviceURLMarker,
                                                 String                      userId,
                                                 String                      anchorGUID,
                                                 String                      anchorTypeName,
                                                 IntegrationReportProperties properties)
    {
        final String methodName = "publishIntegrationReport";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            OpenIntegrationHandler handler = instanceHandler.getOpenIntegrationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName,methodName);
            handler.publishIntegrationReport(userId, anchorGUID, anchorTypeName, properties);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve a specific integration report by unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param reportGUID unique identifier of the integration report
     *
     * @return report or null or
     *  InvalidParameterException one of the parameters is null or invalid,
     *  UserNotAuthorizedException user not authorized to issue this request,
     *  PropertyServerException problem with the metadata server.
     */
    public IntegrationReportResponse getIntegrationReport(String serverName,
                                                          String serviceURLMarker,
                                                          String userId,
                                                          String reportGUID)
    {
        final String methodName = "getIntegrationReport";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        IntegrationReportResponse response = new IntegrationReportResponse();
        AuditLog                  auditLog = null;

        try
        {
            OpenIntegrationHandler handler = instanceHandler.getOpenIntegrationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName,methodName);
            response.setElement(handler.getIntegrationReport(userId, reportGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the integration reports attached to an element.  The list can be filtered by start and completion date of the report along with the
     * paging options if there are many integration reports.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param elementGUID calling user
     * @param afterCompletionDate restrict reports to those that completed after the requested time (null for any completion time).
     * @param beforeStartDate restrict reports to those that started before the requested time (null for any start time).
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of qualifying reports or
     *  InvalidParameterException one of the parameters is null or invalid,
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException problem with the metadata server.
     */
    public IntegrationReportsResponse getIntegrationReportsForElement(String  serverName,
                                                                      String  serviceURLMarker,
                                                                      String  userId,
                                                                      String  elementGUID,
                                                                      Date    afterCompletionDate,
                                                                      Date    beforeStartDate,
                                                                      int     startingFrom,
                                                                      int     maximumResults)
    {
        final String methodName = "getIntegrationReportsForElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        IntegrationReportsResponse response = new IntegrationReportsResponse();
        AuditLog                   auditLog = null;

        try
        {
            OpenIntegrationHandler handler = instanceHandler.getOpenIntegrationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName,methodName);
            response.setElements(handler.getIntegrationReportsForElement(userId, elementGUID, afterCompletionDate, beforeStartDate, startingFrom, maximumResults));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the published integration reports.  The list can be filtered by start and completion date of the report along with the
     * paging options if there are many integration reports.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param afterCompletionDate restrict reports to those that completed after the requested time (null for any completion time).
     * @param beforeStartDate restrict reports to those that started before the requested time (null for any start time).
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of qualifying reports or
     *  InvalidParameterException one of the parameters is null or invalid,
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException problem with the metadata server.
     */
    public IntegrationReportsResponse getIntegrationReports(String  serverName,
                                                            String  serviceURLMarker,
                                                            String  userId,
                                                            Date    afterCompletionDate,
                                                            Date    beforeStartDate,
                                                            int     startingFrom,
                                                            int     maximumResults)
    {
        final String methodName = "getIntegrationReports";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        IntegrationReportsResponse response = new IntegrationReportsResponse();
        AuditLog                   auditLog = null;

        try
        {
            OpenIntegrationHandler handler = instanceHandler.getOpenIntegrationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName,methodName);
            response.setElements(handler.getIntegrationReports(userId, afterCompletionDate, beforeStartDate, startingFrom, maximumResults));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
