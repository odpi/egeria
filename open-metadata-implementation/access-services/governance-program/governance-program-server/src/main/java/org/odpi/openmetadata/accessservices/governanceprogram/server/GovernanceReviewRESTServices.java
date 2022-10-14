/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.server;

import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceDefinitionElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceDefinitionGraph;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceMetricImplementation;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.ElementStubListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceDefinitionGraphResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceDefinitionListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceDefinitionResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceMetricImplementationListResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceMetricHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The GovernanceReviewRESTServices supports the periodic review of the governance program.
 * This includes looking at the metrics and the governance zones.
 */
public class GovernanceReviewRESTServices
{
    private static final GovernanceProgramInstanceHandler instanceHandler = new GovernanceProgramInstanceHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(GovernanceReviewRESTServices.class),
                                                                            instanceHandler.getServiceName());

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public GovernanceReviewRESTServices()
    {
    }


    /**
     * Retrieve the certification type by the unique identifier assigned by this service when it was created.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param definitionGUID identifier of the governance definition to retrieve
     *
     * @return properties of the certification type or
     *  InvalidParameterException guid or userId is null; guid is not recognized
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public GovernanceDefinitionResponse getGovernanceDefinitionByGUID(String serverName,
                                                                      String userId,
                                                                      String definitionGUID)
    {
        final String methodName = "getGovernanceDefinitionByGUID";
        final String guidParameterName = "definitionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceDefinitionResponse response = new GovernanceDefinitionResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler<GovernanceDefinitionElement> handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            response.setElement(handler.getGovernanceDefinitionByGUID(userId,
                                                                      definitionGUID,
                                                                      guidParameterName,
                                                                      false,
                                                                      false,
                                                                      new Date(),
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
     * Return the list of governance definitions associated with a particular governance domain.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param typeName option type name to restrict retrieval to a specific type
     * @param domainIdentifier identifier of the governance domain - 0 = all domains
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of governance definitions or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the caller is not authorized to issue the request
     *  PropertyServerException the metadata service has problems
     */
    public GovernanceDefinitionListResponse getGovernanceDefinitionsForDomain(String serverName,
                                                                              String userId,
                                                                              String typeName,
                                                                              int    domainIdentifier,
                                                                              int    startFrom,
                                                                              int    pageSize)
    {
        final String   methodName = "getGovernanceDefinitionsForDomain";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceDefinitionListResponse response = new GovernanceDefinitionListResponse();
        AuditLog                         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler<GovernanceDefinitionElement> handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            response.setElements(handler.getGovernanceDefinitionsByDomain(userId,
                                                                          typeName,
                                                                          domainIdentifier,
                                                                          startFrom,
                                                                          pageSize,
                                                                          false,
                                                                          false,
                                                                          new Date(),
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
     * Return the list of governance definitions associated with a unique docId.  In an ideal world, there should be only one.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param typeName option types name to restrict retrieval to a specific type
     * @param docId unique name of the governance definition
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of governance definitions or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the caller is not authorized to issue the request
     *  PropertyServerException the metadata service has problems
     */
    public GovernanceDefinitionListResponse getGovernanceDefinitionsForDocId(String serverName,
                                                                             String userId,
                                                                             String typeName,
                                                                             String docId,
                                                                             int    startFrom,
                                                                             int    pageSize)
    {
        final String   methodName = "getGovernanceDefinitionsForDocId";
        final String   docIdParameterName = "docId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceDefinitionListResponse response = new GovernanceDefinitionListResponse();
        AuditLog                         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler<GovernanceDefinitionElement> handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            response.setElements(handler.getGovernanceDefinitionsByName(userId,
                                                                        typeName,
                                                                        docId,
                                                                        docIdParameterName,
                                                                        startFrom,
                                                                        pageSize,
                                                                        false,
                                                                        false,
                                                                        new Date(),
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
     * Return the governance definition associated with a unique identifier and the other governance definitions linked to it.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param governanceDefinitionGUID unique identifier of the governance definition
     *
     * @return governance definition and its linked elements or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the caller is not authorized to issue the request
     *  PropertyServerException the metadata service has problems
     */
    public GovernanceDefinitionGraphResponse getGovernanceDefinitionInContext(String serverName,
                                                                              String userId,
                                                                              String governanceDefinitionGUID)
    {
        final String methodName = "getGovernanceDefinitionInContext";
        final String guidParameterName = "governanceDefinitionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceDefinitionGraphResponse response = new GovernanceDefinitionGraphResponse();
        AuditLog                          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler<GovernanceDefinitionGraph> handler = instanceHandler.getGovernanceDefinitionGraphHandler(userId, serverName, methodName);

            response.setElement(handler.getGovernanceDefinitionInContext(userId,
                                                                         governanceDefinitionGUID,
                                                                         guidParameterName,
                                                                         false,
                                                                         false,
                                                                         new Date(),
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
     * Return the list of governance definitions that match the search string - this can be a regular expression.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param typeName option types name to restrict retrieval to a specific type
     * @param requestBody value to search for
     * @param startFrom where to start from in the list of definition results
     * @param pageSize max number of results to return in one call
     *
     * @return list of governance definitions or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the caller is not authorized to issue the request
     *  PropertyServerException the metadata service has problems
     */
    public GovernanceDefinitionListResponse findGovernanceDefinitions(String                  serverName,
                                                                      String                  userId,
                                                                      String                  typeName,
                                                                      int                     startFrom,
                                                                      int                     pageSize,
                                                                      SearchStringRequestBody requestBody)
    {
        final String methodName = "findGovernanceDefinitions";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceDefinitionListResponse response = new GovernanceDefinitionListResponse();
        AuditLog                         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler<GovernanceDefinitionElement> handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findGovernanceDefinitions(userId,
                                                                       typeName,
                                                                       requestBody.getSearchString(),
                                                                       searchStringParameterName,
                                                                       startFrom,
                                                                       pageSize,
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
     * Return details of the metrics for a governance definition along with details of where the measurements are stored
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param governanceDefinitionGUID unique name of the governance definition
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of associated metrics and links for retrieving the captured measurements or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the caller is not authorized to issue the request
     *  PropertyServerException the metadata service has problems
     */
    public GovernanceMetricImplementationListResponse getGovernanceDefinitionMetrics(String serverName,
                                                                                     String userId,
                                                                                     String governanceDefinitionGUID,
                                                                                     int    startFrom,
                                                                                     int    pageSize)
    {
        final String methodName = "getGovernanceDefinitionMetrics";
        final String guidParameterName = "governanceDefinitionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceMetricImplementationListResponse response = new GovernanceMetricImplementationListResponse();
        AuditLog                         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceMetricHandler<GovernanceMetricImplementation> handler = instanceHandler.getGovernanceMetricImplementationHandler(userId, serverName, methodName);

            response.setElements(handler.getAttachedGovernanceMetricImplementations(userId,
                                                                                    governanceDefinitionGUID,
                                                                                    guidParameterName,
                                                                                    startFrom,
                                                                                    pageSize,
                                                                                    false,
                                                                                    false,
                                                                                    new Date(),
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
     * Return the list of assets that are members of a particular zone.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param zoneName unique identifier of the zone to search for
     * @param subTypeName optional asset subtypeName to limit the results
     * @param startFrom where to start from in the list of assets
     * @param pageSize max number of results to return in one call
     *
     * @return list of headers for assets in the requested zone or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the caller is not authorized to issue the request
     *  PropertyServerException the metadata service has problems
     */
    public ElementStubListResponse getGovernanceZoneMembers(String serverName,
                                                            String userId,
                                                            String zoneName,
                                                            String subTypeName,
                                                            int    startFrom,
                                                            int    pageSize)
    {
        final String methodName = "getGovernanceZoneMembers";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementStubListResponse response = new ElementStubListResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<ElementStub> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            response.setElements(handler.assetZoneScan(userId,
                                                       zoneName,
                                                       subTypeName,
                                                       startFrom,
                                                       pageSize,
                                                       false,
                                                       false,
                                                       new Date(),
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
