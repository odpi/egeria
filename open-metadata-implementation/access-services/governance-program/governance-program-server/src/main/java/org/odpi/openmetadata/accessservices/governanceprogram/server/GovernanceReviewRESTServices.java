/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.server;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceDefinitionElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceDefinitionGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceMetricImplementation;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceMetricHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementStub;
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
    public GovernanceDefinitionsResponse getGovernanceDefinitionsForDomain(String serverName,
                                                                           String userId,
                                                                           String typeName,
                                                                           int    domainIdentifier,
                                                                           int    startFrom,
                                                                           int    pageSize)
    {
        final String   methodName = "getGovernanceDefinitionsForDomain";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceDefinitionsResponse response = new GovernanceDefinitionsResponse();
        AuditLog                      auditLog = null;

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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
    public GovernanceDefinitionsResponse getGovernanceDefinitionsForDocId(String serverName,
                                                                          String userId,
                                                                          String typeName,
                                                                          String docId,
                                                                          int    startFrom,
                                                                          int    pageSize)
    {
        final String   methodName = "getGovernanceDefinitionsForDocId";
        final String   docIdParameterName = "docId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceDefinitionsResponse response = new GovernanceDefinitionsResponse();
        AuditLog                      auditLog = null;

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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
    public GovernanceDefinitionsResponse findGovernanceDefinitions(String                  serverName,
                                                                   String                  userId,
                                                                   String                  typeName,
                                                                   int                     startFrom,
                                                                   int                     pageSize,
                                                                   SearchStringRequestBody requestBody)
    {
        final String methodName = "findGovernanceDefinitions";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceDefinitionsResponse response = new GovernanceDefinitionsResponse();
        AuditLog                      auditLog = null;

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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
    public GovernanceMetricImplementationsResponse getGovernanceDefinitionMetrics(String serverName,
                                                                                  String userId,
                                                                                  String governanceDefinitionGUID,
                                                                                  int    startFrom,
                                                                                  int    pageSize)
    {
        final String methodName = "getGovernanceDefinitionMetrics";
        final String guidParameterName = "governanceDefinitionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceMetricImplementationsResponse response = new GovernanceMetricImplementationsResponse();
        AuditLog                                auditLog = null;

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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
    public ElementStubsResponse getGovernanceZoneMembers(String serverName,
                                                         String userId,
                                                         String zoneName,
                                                         String subTypeName,
                                                         int    startFrom,
                                                         int    pageSize)
    {
        final String methodName = "getGovernanceZoneMembers";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementStubsResponse response = new ElementStubsResponse();
        AuditLog             auditLog = null;

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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;

    }
}
