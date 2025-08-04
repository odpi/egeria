/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.assetcatalog.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.AssetHandler;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.AssetCatalogSupportedTypes;
import org.slf4j.LoggerFactory;


/**
 * The AssetCatalogRESTServices provides the implementation of the Asset Catalog Open Metadata View Service (OMVS).
 * This interface provides view interfaces for glossary UIs.
 */

public class AssetCatalogRESTServices extends TokenController
{
    private static final AssetCatalogInstanceHandler instanceHandler = new AssetCatalogInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(AssetCatalogRESTServices.class),
                                                                            instanceHandler.getServiceName());



    /**
     * Default constructor
     */
    public AssetCatalogRESTServices()
    {
    }


    /**
     * Return all the elements that are anchored to an asset plus relationships between these elements and to other elements.
     *
     * @param serverName name of the server instances for this request
     * @param assetGUID  unique name for the connection.
     * @param queryOptions options to control the query
     *
     * @return graph of elements or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem retrieving the connected asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public AssetGraphResponse getAssetGraph(String       serverName,
                                            String       assetGUID,
                                            QueryOptions queryOptions)
    {
        final String methodName    = "getAssetGraph";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AssetGraphResponse response = new AssetGraphResponse();
        AuditLog           auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            response.setAssetGraph(handler.getAssetGraph(userId, assetGUID, queryOptions));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return all the elements that are linked to an asset using lineage relationships.  The relationships are
     * retrieved both from the asset, and the anchored schema elements
     *
     * @param serverName name of the server instances for this request
     * @param assetGUID  unique identifier for the asset
     * @param requestBody list of relationship type names to use in the search

     *
     * @return graph of elements or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem retrieving the connected asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public AssetLineageGraphResponse getAssetLineageGraph(String                       serverName,
                                                          String                       assetGUID,
                                                          AssetLineageGraphRequestBody requestBody)
    {
        final String methodName    = "getAssetLineageGraph";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AssetLineageGraphResponse response = new AssetLineageGraphResponse();
        AuditLog           auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setAssetLineageGraph(handler.getAssetLineageGraph(userId,
                                                                           assetGUID,
                                                                           requestBody.getLimitToISCQualifiedName(),
                                                                           requestBody.getHighlightISCQualifiedName(),
                                                                           requestBody));
            }
            else
            {
                response.setAssetLineageGraph(handler.getAssetLineageGraph(userId,
                                                                           assetGUID,
                                                                           null,
                                                                           null,
                                                                           null));
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
     * Return a list of assets with the requested search string in their name, qualified name
     * or description.  The search string is interpreted as a regular expression (RegEx).
     *
     * @param serverName name of the server instances for this request
     * @param requestBody string to search for in text
     *
     * @return list of results for assets that match the search string or
     * InvalidParameterException the searchString is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    public AssetSearchMatchesListResponse findInAssetDomain(String                  serverName,
                                                            SearchStringRequestBody requestBody)
    {
        final String methodName = "findInAssetDomain";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AssetSearchMatchesListResponse response = new AssetSearchMatchesListResponse();
        AuditLog                       auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setSearchMatches(handler.findAssetsInDomain(userId,
                                                                     requestBody.getSearchString(),
                                                                     requestBody));
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
     * Return a list of assets that come from the requested metadata collection.
     *
     * @param serverName name of the server instances for this request
     * @param metadataCollectionId guid to search for
     * @param requestBody optional type name to restrict search by
     *
     * @return list of unique identifiers for Assets with the requested name or
     * InvalidParameterException the name is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    public OpenMetadataRootElementsResponse getAssetsByMetadataCollectionId(String            serverName,
                                                                            String            metadataCollectionId,
                                                                            FilterRequestBody requestBody)
    {
        final String methodName = "getAssetsByMetadataCollectionId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getAssetsByMetadataCollectionId(userId,
                                                                           metadataCollectionId,
                                                                           requestBody));
            }
            else
            {
                response.setElements(handler.getAssetsByMetadataCollectionId(userId,
                                                                             metadataCollectionId,
                                                                             null));
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
     * Returns the list with supported types for search, including the subtypes supported.
     * The list is deduplicated.
     *
     * @param serverName name of the server to route the request to
     * @return the supported types from Asset Consumer OMAS or
     * PropertyServerException if a configuration on the backend
     * InvalidParameterException if parameter validation fails
     * UserNotAuthorizedException security access problem
     */
    public AssetCatalogSupportedTypes getSupportedTypes(String serverName)
    {
        final String methodName = "getSupportedTypes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AssetCatalogSupportedTypes response = new AssetCatalogSupportedTypes();
        AuditLog                   auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setTypes(instanceHandler.getSupportedAssetTypes(userId, serverName, methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

}
