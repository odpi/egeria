/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.productmanager.server;


import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.IntegrationDaemonConnector;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.client.ConnectedAssetClient;
import org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentFormatter;
import org.odpi.openmetadata.frameworks.integration.bitol.mapping.BitolMappingResult;
import org.odpi.openmetadata.frameworks.integration.bitol.mapping.DataContractGenerator;
import org.odpi.openmetadata.frameworks.integration.bitol.mapping.DataContractMapper;
import org.odpi.openmetadata.frameworks.integration.bitol.mapping.DataProductGenerator;
import org.odpi.openmetadata.frameworks.integration.bitol.mapping.DataProductMapper;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContract;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProduct;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.viewservices.productmanager.rest.DataContractResponse;
import org.odpi.openmetadata.viewservices.productmanager.rest.DataProductResponse;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.CollectionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductDependencyProperties;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;



/**
 * The ProductManagerRESTServices provides the server-side implementation of the Product Manager Open Metadata
 * View Service (OMVS).  This interface provides access to digital products and digital product families.
 */
public class ProductManagerRESTServices extends TokenController
{
    private static final ProductManagerInstanceHandler instanceHandler = new ProductManagerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(ProductManagerRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public ProductManagerRESTServices()
    {
    }


    /**
     * Link two dependent products.
     *
     * @param serverName         name of called server
     * @param consumerDigitalProductGUID    unique identifier of the digital product that has the dependency.
     * @param consumedDigitalProductGUID    unique identifier of the digital product that it is using.
     * @param requestBody  description of the relationship.
     *
     * @return unique identifier of the new relationship or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse linkDigitalProductDependency(String                     serverName,
                                                     String                     consumerDigitalProductGUID,
                                                     String                     consumedDigitalProductGUID,
                                                     NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkDigitalProductDependency";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DigitalProductDependencyProperties properties)
                {
                    response.setGUID(handler.linkDigitalProductDependency(userId,
                                                         consumerDigitalProductGUID,
                                                         consumedDigitalProductGUID,
                                                         requestBody,
                                                         properties));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setGUID(handler.linkDigitalProductDependency(userId,
                                                         consumerDigitalProductGUID,
                                                         consumedDigitalProductGUID,
                                                         requestBody,
                                                         null));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DigitalProductDependencyProperties.class.getName(), methodName);
                }
            }
            else
            {
                response.setGUID(handler.linkDigitalProductDependency(userId,
                                                     consumerDigitalProductGUID,
                                                     consumedDigitalProductGUID,
                                                     null,
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
     * Update the properties of a digital product dependency relationship.
     *
     * @param serverName name of the server to route the request to
     * @param digitalProductDependencyRelationshipGUID unique identifier of the relationship
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse updateDigitalProductDependency(String                        serverName,
                                                       String                        digitalProductDependencyRelationshipGUID,
                                                       UpdateRelationshipRequestBody requestBody)
    {
        final String methodName = "updateDigitalProductDependency";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DigitalProductDependencyProperties properties)
                {
                    handler.updateDigitalProductDependency(userId, digitalProductDependencyRelationshipGUID, requestBody, properties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DigitalProductDependencyProperties.class.getName(), methodName);
                }
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
     * Remove a digital product dependency relationship.
     *
     * @param serverName name of the server to route the request to
     * @param digitalProductDependencyRelationshipGUID unique identifier of the relationship
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachDigitalProductDependency(String                        serverName,
                                                       String                        digitalProductDependencyRelationshipGUID,
                                                       DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachDigitalProductDependency";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            handler.detachDigitalProductDependency(userId, digitalProductDependencyRelationshipGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Attach a product manager to a digital product.
     *
     * @param serverName         name of called server
     * @param digitalProductGUID  unique identifier of the digital product
     * @param digitalProductManagerRoleGUID      unique identifier of the product manager role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkProductManager(String                     serverName,
                                           String                     digitalProductGUID,
                                           String                     digitalProductManagerRoleGUID,
                                           NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkProductManager";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof AssignmentScopeProperties properties)
                {
                    handler.linkProductManager(userId,
                                               digitalProductGUID,
                                               digitalProductManagerRoleGUID,
                                               requestBody,
                                               properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkProductManager(userId,
                                               digitalProductGUID,
                                               digitalProductManagerRoleGUID,
                                               requestBody,
                                               null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(AssignmentScopeProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkProductManager(userId,
                                           digitalProductGUID,
                                           digitalProductManagerRoleGUID,
                                           requestBody,
                                           null);
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
     * Detach a product manager from a digital product.
     *
     * @param serverName         name of called server
     * @param digitalProductGUID  unique identifier of the digital product
     * @param digitalProductManagerRoleGUID      unique identifier of the product manager role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachProductManager(String                        serverName,
                                             String                        digitalProductGUID,
                                             String                        digitalProductManagerRoleGUID,
                                             DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachProductManager";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            handler.detachProductManager(userId,
                                         digitalProductGUID,
                                         digitalProductManagerRoleGUID,
                                         requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }

    /**
     * Unlink dependent products.
     *
     * @param serverName         name of called server
     * @param consumerDigitalProductGUID    unique identifier of the digital product that has the dependency.
     * @param consumedDigitalProductGUID    unique identifier of the digital product that it is using.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     *
     * This is a multi-link relationship, so this request removes every digital product dependency relationship
     * between the two elements.  Use the request that takes the relationship's own unique identifier to
     * remove just one of them.
     */
    public VoidResponse detachDigitalProductDependency(String                        serverName,
                                                       String                        consumerDigitalProductGUID,
                                                       String                        consumedDigitalProductGUID,
                                                       DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachDigitalProductDependency";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            handler.detachDigitalProductDependency(userId,
                                                   consumerDigitalProductGUID,
                                                   consumedDigitalProductGUID,
                                                   requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /* =====================================================================================================================
     * Bitol documents: Open Data Contract Standard (ODCS) data contracts and Open Data Product Standard (ODPS) data products
     * are published to an integration daemon which passes them on to the integration connectors that have registered a
     * listener for Bitol documents.
     */

    /**
     * The action to perform once connected to the integration daemon.
     */
    @FunctionalInterface
    private interface IntegrationDaemonAction
    {
        /**
         * Publish a document through the connector.
         *
         * @param connector connector to the integration daemon
         * @throws InvalidParameterException one of the parameters is null or invalid
         * @throws UserNotAuthorizedException the caller is not authorized to call the service
         * @throws PropertyServerException a problem processing the request
         */
        void publish(IntegrationDaemonConnector connector) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;
    }


    /**
     * Connect to the integration daemon identified by its server GUID and perform the requested action.
     *
     * @param serverName name of called server
     * @param serverGUID unique identifier of the integration daemon's software server asset
     * @param methodName calling method
     * @param action publishing action
     * @return void or exception
     */
    private VoidResponse publishToIntegrationDaemon(String                  serverName,
                                                    String                  serverGUID,
                                                    String                  methodName,
                                                    IntegrationDaemonAction action)
    {
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

                action.publish(integrationDaemonConnector);

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
     * Pass an Open Data Contract Standard (ODCS) data contract, as a YAML or JSON string, to an integration daemon.
     *
     * @param serverName name of called server
     * @param serverGUID unique identifier of the integration daemon's software server asset
     * @param document data contract to publish
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse publishDataContract(String serverName,
                                            String serverGUID,
                                            String document)
    {
        final String methodName = "publishDataContract";

        return publishToIntegrationDaemon(serverName, serverGUID, methodName, connector -> connector.publishDataContract(document));
    }


    /**
     * Pass an Open Data Contract Standard (ODCS) data contract bean to an integration daemon.
     *
     * @param serverName name of called server
     * @param serverGUID unique identifier of the integration daemon's software server asset
     * @param dataContract data contract to publish
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse publishDataContract(String       serverName,
                                            String       serverGUID,
                                            DataContract dataContract)
    {
        final String methodName = "publishDataContract(bean)";

        return publishToIntegrationDaemon(serverName, serverGUID, methodName, connector -> connector.publishDataContract(dataContract));
    }


    /**
     * Pass an Open Data Product Standard (ODPS) data product, as a YAML or JSON string, to an integration daemon.
     *
     * @param serverName name of called server
     * @param serverGUID unique identifier of the integration daemon's software server asset
     * @param document data product to publish
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse publishDataProduct(String serverName,
                                           String serverGUID,
                                           String document)
    {
        final String methodName = "publishDataProduct";

        return publishToIntegrationDaemon(serverName, serverGUID, methodName, connector -> connector.publishDataProduct(document));
    }


    /**
     * Pass an Open Data Product Standard (ODPS) data product bean to an integration daemon.
     *
     * @param serverName name of called server
     * @param serverGUID unique identifier of the integration daemon's software server asset
     * @param dataProduct data product to publish
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse publishDataProduct(String      serverName,
                                           String      serverGUID,
                                           DataProduct dataProduct)
    {
        final String methodName = "publishDataProduct(bean)";

        return publishToIntegrationDaemon(serverName, serverGUID, methodName, connector -> connector.publishDataProduct(dataProduct));
    }


    /**
     * Catalog an Open Data Contract Standard (ODCS) data contract, supplied as a YAML or JSON string, directly in open
     * metadata as an Agreement classified as a DataSharingAgreement.
     *
     * @param serverName name of called server
     * @param document data contract to catalog
     * @return unique identifier of the agreement (null if the document was skipped) or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse importDataContract(String serverName,
                                           String document)
    {
        final String methodName = "importDataContract";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataContract dataContract = BitolDocumentFormatter.parseDataContract(document);

            response.setGUID(importDataContract(userId, serverName, methodName, dataContract));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Catalog an Open Data Contract Standard (ODCS) data contract bean directly in open metadata as an Agreement
     * classified as a DataSharingAgreement.
     *
     * @param serverName name of called server
     * @param dataContract data contract to catalog
     * @return unique identifier of the agreement (null if the document was skipped) or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse importDataContract(String       serverName,
                                           DataContract dataContract)
    {
        final String methodName = "importDataContract(bean)";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setGUID(importDataContract(userId, serverName, methodName, dataContract));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Run the data contract mapper for the calling user.
     *
     * @param userId calling user
     * @param serverName name of called server
     * @param methodName calling method
     * @param dataContract document to catalog
     * @return unique identifier of the resulting agreement, or null if the document was skipped
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String importDataContract(String       userId,
                                      String       serverName,
                                      String       methodName,
                                      DataContract dataContract) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        ConnectorContextBase context = instanceHandler.getConnectorContext(userId, serverName, methodName);

        BitolMappingResult result = new DataContractMapper(context).catalogueDataContract(dataContract, instanceHandler.getServiceName());

        return result.getElementGUID();
    }


    /**
     * Catalog an Open Data Product Standard (ODPS) data product, supplied as a YAML or JSON string, directly in open
     * metadata as a DigitalProduct.
     *
     * @param serverName name of called server
     * @param document data product to catalog
     * @return unique identifier of the digital product (null if the document was skipped) or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse importDataProduct(String serverName,
                                          String document)
    {
        final String methodName = "importDataProduct";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataProduct dataProduct = BitolDocumentFormatter.parseDataProduct(document);

            response.setGUID(importDataProduct(userId, serverName, methodName, dataProduct));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Catalog an Open Data Product Standard (ODPS) data product bean directly in open metadata as a DigitalProduct.
     *
     * @param serverName name of called server
     * @param dataProduct data product to catalog
     * @return unique identifier of the digital product (null if the document was skipped) or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse importDataProduct(String      serverName,
                                          DataProduct dataProduct)
    {
        final String methodName = "importDataProduct(bean)";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setGUID(importDataProduct(userId, serverName, methodName, dataProduct));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Run the data product mapper for the calling user.
     *
     * @param userId calling user
     * @param serverName name of called server
     * @param methodName calling method
     * @param dataProduct document to catalog
     * @return unique identifier of the resulting digital product, or null if the document was skipped
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String importDataProduct(String      userId,
                                     String      serverName,
                                     String      methodName,
                                     DataProduct dataProduct) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        ConnectorContextBase context = instanceHandler.getConnectorContext(userId, serverName, methodName);

        BitolMappingResult result = new DataProductMapper(context).catalogueDataProduct(dataProduct, instanceHandler.getServiceName());

        return result.getElementGUID();
    }


    /**
     * Generate the Open Data Contract Standard (ODCS) document for an agreement.
     *
     * @param serverName name of called server
     * @param agreementGUID unique identifier of the agreement
     * @return data contract or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public DataContractResponse generateDataContract(String serverName,
                                                     String agreementGUID)
    {
        final String methodName = "generateDataContract";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        DataContractResponse response = new DataContractResponse();
        AuditLog             auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectorContextBase context = instanceHandler.getConnectorContext(userId, serverName, methodName);

            response.setDataContract(new DataContractGenerator(context).generateDataContract(agreementGUID));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Generate the Open Data Product Standard (ODPS) document for a digital product.
     *
     * @param serverName name of called server
     * @param digitalProductGUID unique identifier of the digital product
     * @return data product or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public DataProductResponse generateDataProduct(String serverName,
                                                   String digitalProductGUID)
    {
        final String methodName = "generateDataProduct";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        DataProductResponse response = new DataProductResponse();
        AuditLog            auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectorContextBase context = instanceHandler.getConnectorContext(userId, serverName, methodName);

            response.setDataProduct(new DataProductGenerator(context).generateDataProduct(digitalProductGUID));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
