/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.assetcatalog.server;

import org.odpi.openmetadata.accessservices.assetconsumer.client.AssetConsumer;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest.AssetsResponse;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.AssetCatalogSupportedTypes;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


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

    private final PropertyHelper propertyHelper = new PropertyHelper();

    private final String sourceName = ViewServiceDescription.ASSET_CATALOG.getViewServiceFullName();

    private final List<ElementStatus>  activeElementsOnly = new ArrayList<>(Collections.singleton(ElementStatus.ACTIVE));

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
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return graph of elements or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem retrieving the connected asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public AssetGraphResponse getAssetGraph(String   serverName,
                                            String   assetGUID,
                                            int      startFrom,
                                            int      pageSize)
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

            AssetConsumer handler = instanceHandler.getAssetConsumerClient(userId, serverName, methodName);

            response.setAssetGraph(handler.getAssetGraph(userId, assetGUID, startFrom, pageSize));
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
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return graph of elements or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem retrieving the connected asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public AssetLineageGraphResponse getAssetLineageGraph(String                       serverName,
                                                          String                       assetGUID,
                                                          AssetLineageGraphRequestBody requestBody,
                                                          int                          startFrom,
                                                          int                          pageSize)
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

            AssetConsumer handler = instanceHandler.getAssetConsumerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setAssetLineageGraph(handler.getAssetLineageGraph(userId,
                                                                           assetGUID,
                                                                           requestBody.getRelationshipTypes(),
                                                                           requestBody.getLimitToISCQualifiedName(),
                                                                           requestBody.getHighlightISCQualifiedName(),
                                                                           requestBody.getAsOfTime(),
                                                                           requestBody.getEffectiveTime(),
                                                                           startFrom,
                                                                           pageSize));
            }
            else
            {
                response.setAssetLineageGraph(handler.getAssetLineageGraph(userId,
                                                                           assetGUID,
                                                                           null,
                                                                           null,
                                                                           null,
                                                                           null,
                                                                           new Date(),
                                                                           startFrom,
                                                                           pageSize));
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
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of results for assets that match the search string or
     * InvalidParameterException the searchString is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    public AssetSearchMatchesListResponse findInAssetDomain(String            serverName,
                                                            FilterRequestBody requestBody,
                                                            boolean           startsWith,
                                                            boolean           endsWith,
                                                            boolean           ignoreCase,
                                                            int               startFrom,
                                                            int               pageSize)
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

            AssetConsumer handler = instanceHandler.getAssetConsumerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setSearchMatches(handler.findAssetsInDomain(userId,
                                                                     instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                                     startFrom,
                                                                     pageSize));
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
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param requestBody optional type name to restrict search by
     *
     * @return list of unique identifiers for Assets with the requested name or
     * InvalidParameterException the name is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    public AssetsResponse getAssetsByMetadataCollectionId(String            serverName,
                                                          String            metadataCollectionId,
                                                          int               startFrom,
                                                          int               pageSize,
                                                          FilterRequestBody requestBody)
    {
        final String methodName = "getAssetsByMetadataCollectionId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AssetsResponse response = new AssetsResponse();
        AuditLog       auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetConsumer handler = instanceHandler.getAssetConsumerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setAssets(handler.getAssetsByMetadataCollectionId(userId,
                                                                           metadataCollectionId,
                                                                           requestBody.getFilter(),
                                                                           startFrom,
                                                                           pageSize));
            }
            else
            {
                response.setAssets(handler.getAssetsByMetadataCollectionId(userId,
                                                                           metadataCollectionId,
                                                                           null,
                                                                           startFrom,
                                                                           pageSize));
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
