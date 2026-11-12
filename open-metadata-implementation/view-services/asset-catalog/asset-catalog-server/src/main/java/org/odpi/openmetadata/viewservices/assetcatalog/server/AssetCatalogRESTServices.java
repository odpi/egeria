/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.assetcatalog.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.IntegrationDaemonConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.client.ConnectedAssetClient;
import org.odpi.openmetadata.frameworks.integration.dcat.DcatDocumentFormatter;
import org.odpi.openmetadata.frameworks.integration.dcat.mapping.DcatGenerator;
import org.odpi.openmetadata.frameworks.integration.dcat.mapping.DcatMapper;
import org.odpi.openmetadata.frameworks.integration.dcat.mapping.DcatMappingResult;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDocument;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.AssetHandler;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.assetcatalog.ffdc.AssetCatalogAuditCode;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFErrorCode;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.AssetCatalogSupportedTypes;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.DcatDocumentResponse;
import org.slf4j.LoggerFactory;

import java.io.IOException;


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
     * @param requestBody options to control the query
     *
     * @return graph of elements or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - a problem retrieving the connected asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementResponse getAssetGraph(String             serverName,
                                                         String             assetGUID,
                                                         ResultsRequestBody requestBody)
    {
        final String methodName    = "getAssetGraph";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            response.setElement(handler.getAssetGraph(userId, assetGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
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
     * PropertyServerException - a problem retrieving the connected asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementResponse getAssetLineageGraph(String                       serverName,
                                                                String                       assetGUID,
                                                                AssetLineageGraphRequestBody requestBody)
    {
        final String methodName = "getAssetLineageGraph";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getAssetLineageGraph(userId,
                                                                 assetGUID,
                                                                 requestBody.getLimitToISCQualifiedName(),
                                                                 requestBody.getHighlightISCQualifiedName(),
                                                                 requestBody.getAllAnchors(),
                                                                 requestBody.getMaxHops(),
                                                                 requestBody));
            }
            else
            {
                response.setElement(handler.getAssetLineageGraph(userId,
                                                                 assetGUID,
                                                                 null,
                                                                 null,
                                                                 false,
                                                                 AssetLineageGraphRequestBody.DEFAULT_MAX_HOPS,
                                                                 null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Locate string value in elements that are anchored to assets.  The results are organized by asset.
     * Paging (startFrom and pageSize) applies to the matching elements, before they are grouped by asset.
     * A page may therefore hold fewer assets than pageSize, and an asset whose matches fall on more than
     * one page is returned on each of those pages, with the matches from that page.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody string to search for in text
     *
     * @return list of results for assets that match the search string or
     * InvalidParameterException the searchString is invalid or
     * PropertyServerException a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    public OpenMetadataRootElementsResponse findInAssetDomain(String                  serverName,
                                                              SearchStringRequestBody requestBody)
    {
        final String methodName = "findInAssetDomain";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                response.setElements(handler.findAssetsInDomain(userId,
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

        restCallLogger.logRESTCallReturn(token, response);
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
     * PropertyServerException a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    public OpenMetadataRootElementsResponse getAssetsByMetadataCollectionId(String             serverName,
                                                                            String             metadataCollectionId,
                                                                            ResultsRequestBody requestBody)
    {
        final String methodName = "getAssetsByMetadataCollectionId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Returns the list with supported types for search, including the subtypes supported.
     * The list is deduplicated.
     *
     * @param serverName name of the server to route the request to
     * @return the supported types from open metadata or
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

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /* =====================================================================================================================
     * Data Catalog Vocabulary (DCAT) documents describe catalogs, datasets, dataset series and data services.
     */

    /**
     * Pass a Data Catalog Vocabulary (DCAT) document to an integration daemon.  The integration daemon passes it on to
     * the integration connectors that have registered a listener for DCAT documents, such as the DCAT Cataloguer.
     *
     * @param serverName name of called server
     * @param serverGUID unique identifier of the integration daemon's software server asset
     * @param document DCAT document in JSON-LD format
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse publishDcatDocument(String serverName,
                                            String serverGUID,
                                            String document)
    {
        final String methodName = "publishDcatDocument";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient handler = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = handler.getConnectorForAsset(userId, serverGUID, auditLog);

            if (connector instanceof IntegrationDaemonConnector integrationDaemonConnector)
            {
                integrationDaemonConnector.setDelegatingUserId(userId);
                integrationDaemonConnector.start();

                integrationDaemonConnector.publishDcatDocument(document);

                integrationDaemonConnector.disconnect();
            }
            else
            {
                restExceptionHandler.handleInvalidCallToServer(IntegrationDaemonConnector.class.getName(),
                                                               methodName,
                                                               serverGUID,
                                                               connector.getClass().getName());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Catalog a Data Catalog Vocabulary (DCAT) document directly in open metadata.  Each dataset becomes a data set
     * asset with an asset for each of its distributions, each data service becomes a deployed API and each catalog
     * becomes a collection folder.  Sending the same description again updates the elements.
     *
     * @param serverName name of called server
     * @param document DCAT document in JSON-LD format
     * @return unique identifiers of the elements that represent the catalogs, datasets, dataset series and data
     * services described in the document or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDListResponse importDcatDocument(String serverName,
                                               String document)
    {
        final String methodName        = "importDcatDocument";
        final String documentParameter = "document";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DcatDocument dcatDocument;

            try
            {
                dcatDocument = DcatDocumentFormatter.parseDocument(document);
            }
            catch (IOException parsingError)
            {
                throw new InvalidParameterException(OIFErrorCode.INVALID_DCAT_DOCUMENT.getMessageDefinition(parsingError.getMessage()),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    parsingError,
                                                    documentParameter);
            }

            if (! DcatDocumentFormatter.hasContent(dcatDocument))
            {
                throw new InvalidParameterException(OIFErrorCode.EMPTY_DCAT_DOCUMENT.getMessageDefinition(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    documentParameter);
            }

            ConnectorContextBase context = instanceHandler.getConnectorContext(userId, serverName, methodName);
            DcatMappingResult    result  = new DcatMapper(context).catalogueDocument(dcatDocument, instanceHandler.getServiceName());

            response.setGUIDs(result.getElementGUIDs());

            if (auditLog != null)
            {
                for (String warning : result.getWarnings())
                {
                    auditLog.logMessage(methodName, AssetCatalogAuditCode.DCAT_MAPPING_WARNING.getMessageDefinition(userId, warning));
                }
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
     * Return the description of an asset in the Data Catalog Vocabulary (DCAT).  A deployed API is described as a
     * data service; any other type of asset is described as a dataset.  The description is a JSON-LD document.
     *
     * @param serverName name of called server
     * @param assetGUID unique identifier of the asset
     * @return DCAT description or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public DcatDocumentResponse getAssetAsDcat(String serverName,
                                               String assetGUID)
    {
        final String methodName = "getAssetAsDcat";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        DcatDocumentResponse response = new DcatDocumentResponse();
        AuditLog             auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectorContextBase context = instanceHandler.getConnectorContext(userId, serverName, methodName);

            response.setDocument(DcatDocumentFormatter.toJSONLDStructure(new DcatGenerator(context).generateAssetDocument(assetGUID)));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
