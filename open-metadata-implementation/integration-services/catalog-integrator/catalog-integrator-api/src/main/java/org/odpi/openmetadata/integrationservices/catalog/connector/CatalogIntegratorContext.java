/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalog.connector;

import org.odpi.openmetadata.accessservices.assetmanager.api.AssetManagerEventListener;
import org.odpi.openmetadata.accessservices.assetmanager.client.*;
import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.CollaborationExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.ConnectionExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.DataAssetExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.ExternalAssetManagerClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.ExternalReferenceExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.GlossaryExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.GovernanceExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.InfrastructureExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.LineageExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.StewardshipExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.ValidValuesExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.CorrelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.client.ActionControlInterface;
import org.odpi.openmetadata.frameworks.governanceaction.client.GovernanceConfiguration;
import org.odpi.openmetadata.frameworks.governanceaction.properties.MetadataCorrelationHeader;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.integration.client.OpenIntegrationClient;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorAuditCode;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorErrorCode;

import java.util.List;

/**
 * CatalogIntegratorContext provides a wrapper around the Asset Manager OMAS client.
 * It provides the simplified interface to open metadata needed by the CatalogIntegratorConnector.
 */
public class CatalogIntegratorContext extends IntegrationContext
{
    /**
     * Service for creating connector instances.
     */
    public final static String connectorFactoryServiceName          = "ConnectorFactoryExchangeService";

    /**
     * Service for working with feedback elements.
     */
    public final static String collaborationExchangeServiceName     = "CollaborationExchangeService";

    /**
     * Service for building and maintaining connections, connector types and endpoints.
     */
    public final static String connectionExchangeServiceName        = "ConnectionExchangeService";

    /**
     * Service for working with data stores, data sets, data feeds and APIs.
     */
    public final static String dataAssetExchangeServiceName         = "DataAssetExchangeService";

    /**
     * Services for maintaining links to external references.
     */
    public final static String externalReferenceExchangeServiceName = "ExternalReferenceExchangeService";

    /**
     * Service for maintaining glossaries and their contents
     */
    public final static String glossaryExchangeServiceName          = "GlossaryExchangeService";

    /**
     * Service for maintaining governance definitions such as policies and rules.
     */
    public final static String governanceExchangeServiceName        = "GovernanceExchangeService";

    /**
     * Service for maintaining models of infrastructure.
     */
    public final static String infrastructureExchangeServiceName    = "InfrastructureExchangeService";

    /**
     * Service for maintaining lineage relationships.
     */
    public final static String lineageExchangeServiceName           = "LineageExchangeService";

    /**
     * Service for handing actions for stewards.
     */
    public final static String stewardshipExchangeServiceName       = "StewardshipExchangeService";

    /**
     * Service for maintaining reference data (valid values).
     */
    public final static String validValuesExchangeServiceName       = "ValidValuesExchangeService";


    private final ExternalAssetManagerClient       assetManagerClient;
    private final AssetManagerEventClient          eventClient;
    private final ConnectorFactoryService          connectorFactoryService;
    private final CollaborationExchangeService     collaborationExchangeService;
    private final ConnectionExchangeService        connectionExchangeService;
    private final DataAssetExchangeService         dataAssetExchangeService;
    private final ExternalReferenceExchangeService externalReferenceExchangeService;
    private final GlossaryExchangeService          glossaryExchangeService;
    private final GovernanceExchangeService        governanceExchangeService;
    private final InfrastructureExchangeService    infrastructureExchangeService;
    private final LineageExchangeService           lineageExchangeService;
    private final StewardshipExchangeService       stewardshipExchangeService;
    private final ValidValuesExchangeService       validValuesExchangeService;

    private final String integrationServiceName;

    private boolean connectorFactoryActive          = true;
    private boolean glossaryExchangeActive          = true;
    private boolean externalReferenceExchangeActive = true;
    private boolean dataAssetExchangeActive         = true;
    private boolean collaborationExchangeActive     = true;
    private boolean connectionExchangeActive        = true;
    private boolean infrastructureExchangeActive    = true;
    private boolean lineageExchangeActive           = true;
    private boolean stewardshipExchangeActive       = true;
    private boolean governanceExchangeActive        = true;
    private boolean validValuesExchangeActive       = true;

    private boolean listenerRegistered = false;

    /**
     * Create a new context for a connector.
     *
     * @param connectorId unique identifier of the connector (used to configure the event listener)
     * @param connectorName name of connector from config
     * @param connectorUserId userId for the connector
     * @param serverName name of the integration daemon
     * @param openIntegrationClient client for calling the metadata server
     * @param governanceConfiguration client for managing catalog targets
     * @param openMetadataStoreClient client for calling the metadata server
     * @param actionControlInterface client for initiating governance actions
     * @param assetManagerClient common client to map requests to
     * @param eventClient client to register for events
     * @param connectedAssetClient client for connectors
     * @param collaborationExchangeClient client for collaboration requests
     * @param connectionExchangeClient client for connection requests
     * @param dataAssetExchangeClient client for asset requests
     * @param externalReferenceExchangeClient client for data asset requests
     * @param glossaryExchangeClient client for glossary requests
     * @param governanceExchangeClient client for governance requests
     * @param infrastructureExchangeClient client for infrastructure requests
     * @param lineageExchangeClient client for lineage requests
     * @param stewardshipExchangeClient client for stewardship requests
     * @param validValuesExchangeClient client for valid values requests
     * @param assetManagerGUID unique identifier of the software server capability for the asset manager
     * @param assetManagerName unique name of the software server capability for the asset manager
     * @param generateIntegrationReport should the connector generate an integration reports?
     * @param permittedSynchronization the direction of integration permitted by the integration connector
     * @param integrationConnectorGUID unique identifier for the integration connector if it is started via an integration group (otherwise it is
     *                                 null).
     * @param disabledExchangeServices option from the integration service's configuration
     * @param integrationServiceName name of this service
     * @param maxPageSize max number of elements that can be returned on a query
     * @param auditLog logging destination
     */
    public CatalogIntegratorContext(String                          connectorId,
                                    String                          connectorName,
                                    String                          connectorUserId,
                                    String                          serverName,
                                    OpenIntegrationClient           openIntegrationClient,
                                    GovernanceConfiguration         governanceConfiguration,
                                    OpenMetadataClient              openMetadataStoreClient,
                                    ActionControlInterface          actionControlInterface,
                                    ExternalAssetManagerClient      assetManagerClient,
                                    AssetManagerEventClient         eventClient,
                                    ConnectedAssetClient            connectedAssetClient,
                                    CollaborationExchangeClient     collaborationExchangeClient,
                                    ConnectionExchangeClient        connectionExchangeClient,
                                    DataAssetExchangeClient         dataAssetExchangeClient,
                                    ExternalReferenceExchangeClient externalReferenceExchangeClient,
                                    GlossaryExchangeClient          glossaryExchangeClient,
                                    GovernanceExchangeClient        governanceExchangeClient,
                                    InfrastructureExchangeClient    infrastructureExchangeClient,
                                    LineageExchangeClient           lineageExchangeClient,
                                    StewardshipExchangeClient       stewardshipExchangeClient,
                                    ValidValuesExchangeClient       validValuesExchangeClient,
                                    boolean                         generateIntegrationReport,
                                    PermittedSynchronization        permittedSynchronization,
                                    String                          integrationConnectorGUID,
                                    String                          assetManagerGUID,
                                    String                          assetManagerName,
                                    String                          integrationServiceName,
                                    List<String>                    disabledExchangeServices,
                                    int                             maxPageSize,
                                    AuditLog                        auditLog)
    {
        super(connectorId,
              connectorName,
              connectorUserId,
              serverName,
              openIntegrationClient,
              governanceConfiguration,
              openMetadataStoreClient,
              actionControlInterface,
              generateIntegrationReport,
              permittedSynchronization,
              assetManagerGUID,
              assetManagerName,
              integrationConnectorGUID,
              auditLog,
              maxPageSize);

        final String methodName = "CatalogIntegratorContext";

        PermittedSynchronization synchronizationDirection =   getSynchronizationDirection(permittedSynchronization);

        this.assetManagerClient       = assetManagerClient;
        this.eventClient              = eventClient;

        this.connectorFactoryService       = new ConnectorFactoryService(connectedAssetClient, userId);
        this.collaborationExchangeService  = new CollaborationExchangeService(collaborationExchangeClient,
                                                                              synchronizationDirection,
                                                                              userId,
                                                                              assetManagerGUID,
                                                                              assetManagerName,
                                                                              connectorName,
                                                                              auditLog);
        this.connectionExchangeService  = new ConnectionExchangeService(connectionExchangeClient,
                                                                        synchronizationDirection,
                                                                        userId,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        connectorName,
                                                                        auditLog);
        this.dataAssetExchangeService  = new DataAssetExchangeService(dataAssetExchangeClient,
                                                                      synchronizationDirection,
                                                                      userId,
                                                                      assetManagerGUID,
                                                                      assetManagerName,
                                                                      connectorName,
                                                                      auditLog);
        this.externalReferenceExchangeService  = new ExternalReferenceExchangeService(externalReferenceExchangeClient,
                                                                                      synchronizationDirection,
                                                                                      userId,
                                                                                      assetManagerGUID,
                                                                                      assetManagerName,
                                                                                      connectorName,
                                                                                      auditLog);
        this.glossaryExchangeService  = new GlossaryExchangeService(glossaryExchangeClient,
                                                                    synchronizationDirection,
                                                                    userId,
                                                                    assetManagerGUID,
                                                                    assetManagerName,
                                                                    connectorName,
                                                                    auditLog);
        this.governanceExchangeService  = new GovernanceExchangeService(governanceExchangeClient,
                                                                        synchronizationDirection,
                                                                        userId,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        connectorName,
                                                                        auditLog);
        this.infrastructureExchangeService  = new InfrastructureExchangeService(infrastructureExchangeClient,
                                                                                synchronizationDirection,
                                                                                userId,
                                                                                assetManagerGUID,
                                                                                assetManagerName,
                                                                                connectorName,
                                                                                auditLog);
        this.lineageExchangeService  = new LineageExchangeService(lineageExchangeClient,
                                                                  synchronizationDirection,
                                                                  userId,
                                                                  assetManagerGUID,
                                                                  assetManagerName,
                                                                  connectorName,
                                                                  auditLog);
        this.stewardshipExchangeService  = new StewardshipExchangeService(stewardshipExchangeClient,
                                                                          synchronizationDirection,
                                                                          userId,
                                                                          assetManagerGUID,
                                                                          assetManagerName,
                                                                          connectorName,
                                                                          auditLog);
        this.validValuesExchangeService = new ValidValuesExchangeService(validValuesExchangeClient,
                                                                          synchronizationDirection,
                                                                          userId,
                                                                          assetManagerGUID,
                                                                          assetManagerName,
                                                                          connectorName,
                                                                          auditLog);
        this.externalSourceGUID     = assetManagerGUID;
        this.externalSourceName     = assetManagerName;
        this.integrationServiceName = integrationServiceName;

        auditLog.logMessage(methodName,
                            CatalogIntegratorAuditCode.PERMITTED_SYNCHRONIZATION.getMessageDefinition(connectorName,
                                                                                                      permittedSynchronization.getName()));

        if (disabledExchangeServices != null)
        {
            for (String exchangeServiceName : disabledExchangeServices)
            {
                if (connectorFactoryServiceName.equals(exchangeServiceName))
                {
                    connectorFactoryActive = false;
                }
                else if (collaborationExchangeServiceName.equals(exchangeServiceName))
                {
                    collaborationExchangeActive = false;
                }
                else if (connectionExchangeServiceName.equals(exchangeServiceName))
                {
                    connectionExchangeActive = false;
                }
                else if (glossaryExchangeServiceName.equals(exchangeServiceName))
                {
                    glossaryExchangeActive = false;
                }
                else if (dataAssetExchangeServiceName.equals(exchangeServiceName))
                {
                    dataAssetExchangeActive = false;
                }
                else if (externalReferenceExchangeServiceName.equals(exchangeServiceName))
                {
                    externalReferenceExchangeActive = false;
                }
                else if (infrastructureExchangeServiceName.equals(exchangeServiceName))
                {
                    infrastructureExchangeActive = false;
                }
                else if (lineageExchangeServiceName.equals(exchangeServiceName))
                {
                    lineageExchangeActive = false;
                }
                else if (stewardshipExchangeServiceName.equals(exchangeServiceName))
                {
                    stewardshipExchangeActive = false;
                }
                else if (governanceExchangeServiceName.equals(exchangeServiceName))
                {
                    governanceExchangeActive = false;
                }
                else if (validValuesExchangeServiceName.equals(exchangeServiceName))
                {
                    validValuesExchangeActive = false;
                }
            }
        }
    }


    /**
     * Convert permitted synchronization from the configuration into the PermittedSynchronization enum.
     * The default is BOTH_DIRECTIONS which effectively enforces no restriction.
     *
     * @param permittedSynchronization value from the configuration
     * @return synchronization direction enum
     */
    private PermittedSynchronization getSynchronizationDirection(PermittedSynchronization permittedSynchronization)
    {
        if (permittedSynchronization != null)
        {
            return switch (permittedSynchronization)
            {
                case TO_THIRD_PARTY -> PermittedSynchronization.TO_THIRD_PARTY;
                case FROM_THIRD_PARTY -> PermittedSynchronization.FROM_THIRD_PARTY;
                case BOTH_DIRECTIONS -> PermittedSynchronization.BOTH_DIRECTIONS;
                case OTHER -> PermittedSynchronization.OTHER;
            };
        }

        return PermittedSynchronization.BOTH_DIRECTIONS;
    }



    /* ========================================================
     * Returning the asset manager from the configuration.  This identifies the origin of the elements created by this
     * connector (or other connectors using the same metadataSourceQualifiedName).
     */


    /**
     * Return the unique identifier for the asset manager element with a qualifiedName matching the assetManagerName.
     *
     * @return string guid
     */
    public String getAssetManagerGUID()
    {
        return externalSourceGUID;
    }


    /**
     * Return the qualified name of the asset manager that is supplied in the configuration as metadataSourceQualifiedName.
     *
     * @return string name
     */
    public String getAssetManagerName()
    {
        return externalSourceName;
    }



    /* ========================================================
     * Register for inbound events from the Asset Manager OMAS OutTopic
     */


    /**
     * Register a listener object that will be passed each of the events published by the Asset Manager OMAS.
     *
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void registerListener(AssetManagerEventListener listener) throws InvalidParameterException,
                                                                            ConnectionCheckedException,
                                                                            ConnectorCheckedException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        eventClient.registerListener(userId, listener);
        this.listenerRegistered = true;
    }


    /**
     * Return a flag indicating whether a listener has been registered or not.
     *
     * @return true means the listener has been successfully registered
     */
    public boolean isListenerRegistered()
    {
        return listenerRegistered;
    }



    /* ========================================================
     * Support external identifiers
     */


    /**
     * Retrieve the correlation header from the retrieved element that matches this asset manager.
     *
     * @param retrievedElement element retrieved from the metadata repository
     * @return metadata correlation header or null if the element does not have a matching correlation header
     * @throws InvalidParameterException the element passed is null
     */
    public MetadataCorrelationHeader getMetadataCorrelationHeader(CorrelatedMetadataElement retrievedElement) throws InvalidParameterException
    {
        final String methodName = "getMetadataCorrelationHeader";
        final String parameterName = "retrievedElement";

        if (retrievedElement == null)
        {
            throw new InvalidParameterException(CatalogIntegratorErrorCode.NULL_ELEMENT_PASSED.getMessageDefinition(connectorName,
                                                                                                                    methodName,
                                                                                                                    parameterName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else if ((retrievedElement.getCorrelationHeaders() == null) || (externalSourceName == null))
        {
            return null;
        }
        else
        {
            for (MetadataCorrelationHeader metadataCorrelationHeader : retrievedElement.getCorrelationHeaders())
            {
                if (externalSourceName.equals(metadataCorrelationHeader.getExternalScopeName()))
                {
                    return metadataCorrelationHeader;
                }
            }

            /*
             * None of the correlation headers match this asset manager.
             */
            return null;
        }
    }


    /**
     * Add a new external identifier to an existing open metadata element.
     *
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifierProperties optional properties used to define an external identifier
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void addExternalIdentifier(String                       openMetadataElementGUID,
                                      String                       openMetadataElementTypeName,
                                      ExternalIdentifierProperties externalIdentifierProperties) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        openMetadataStoreClient.addExternalIdentifier(userId,
                                                      externalSourceGUID,
                                                      externalSourceName,
                                                      openMetadataElementGUID,
                                                      openMetadataElementTypeName,
                                                      externalIdentifierProperties);
    }


    /**
     * Add a new external identifier to an existing open metadata element.
     *
     * @param externalScopeGUID      unique identifier of the software capability that owns this collection
     * @param externalScopeName      unique name of the software capability that owns this collection
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifierProperties optional properties used to define an external identifier
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void addExternalIdentifier(String                       externalScopeGUID,
                                      String                       externalScopeName,
                                      String                       openMetadataElementGUID,
                                      String                       openMetadataElementTypeName,
                                      ExternalIdentifierProperties externalIdentifierProperties) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        openMetadataStoreClient.addExternalIdentifier(userId,
                                                      externalScopeGUID,
                                                      externalScopeName,
                                                      openMetadataElementGUID,
                                                      openMetadataElementTypeName,
                                                      externalIdentifierProperties);
    }


    /**
     * Update the description of a specific external identifier.
     *
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifierProperties optional properties used to define an external identifier
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void updateExternalIdentifier(String                       openMetadataElementGUID,
                                         String                       openMetadataElementTypeName,
                                         ExternalIdentifierProperties externalIdentifierProperties) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        openMetadataStoreClient.updateExternalIdentifier(userId,
                                                         externalSourceGUID,
                                                         externalSourceName,
                                                         openMetadataElementGUID,
                                                         openMetadataElementTypeName,
                                                         externalIdentifierProperties);
    }


    /**
     * Remove a new external identifier from an existing open metadata element.  The open metadata element is not
     * affected.
     *
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifier unique identifier of this element in the third party asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void removeExternalIdentifier(String openMetadataElementGUID,
                                         String openMetadataElementTypeName,
                                         String externalIdentifier) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        openMetadataStoreClient.removeExternalIdentifier(userId,
                                                         externalSourceGUID,
                                                         externalSourceName,
                                                         openMetadataElementGUID,
                                                         openMetadataElementTypeName,
                                                         externalIdentifier);
    }


    /**
     * Confirm that the values of a particular metadata element have been synchronized.  This is important
     * from an audit point of view, and to allow bidirectional updates of metadata using optimistic locking.
     *
     * @param openMetadataGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifier unique identifier of this element in the external asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void confirmSynchronization(String openMetadataGUID,
                                       String openMetadataElementTypeName,
                                       String externalIdentifier) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        openMetadataStoreClient.confirmSynchronization(userId,
                                                       externalSourceGUID,
                                                       externalSourceName,
                                                       openMetadataGUID,
                                                       openMetadataElementTypeName,
                                                       externalIdentifier);
    }


    /**
     * Confirm that the values of a particular metadata element have been synchronized.  This is important
     * from an audit point of view, and to allow bidirectional updates of metadata using optimistic locking.
     *
     * @param externalScopeGUID unique identifier for the owning scope (typically a catalog)
     * @param externalScopeName unique name for the owning scope (typically a catalog)
     * @param openMetadataGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifier unique identifier of this element in the external asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void confirmSynchronization(String externalScopeGUID,
                                       String externalScopeName,
                                       String openMetadataGUID,
                                       String openMetadataElementTypeName,
                                       String externalIdentifier) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        openMetadataStoreClient.confirmSynchronization(userId,
                                                       externalScopeGUID,
                                                       externalScopeName,
                                                       openMetadataGUID,
                                                       openMetadataElementTypeName,
                                                       externalIdentifier);
    }


    /**
     * Return the interface for working with connectors to digital resources.
     *
     * @return collaboration exchange service
     * @throws UserNotAuthorizedException this option is not enabled in the configuration
     */
    public ConnectorFactoryService getConnectorFactoryService() throws UserNotAuthorizedException
    {
        final String methodName = "getConnectorFactoryService";

        if (connectorFactoryActive)
        {
            return connectorFactoryService;
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.DISABLED_EXCHANGE_SERVICE.getMessageDefinition(connectorFactoryServiceName,
                                                                                              integrationServiceName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Return the interface for exchanging collaboration information (comments, likes, reviews, tags).
     *
     * @return collaboration exchange service
     * @throws UserNotAuthorizedException this option is not enabled in the configuration
     */
    public CollaborationExchangeService getCollaborationExchangeService() throws UserNotAuthorizedException
    {
        final String methodName = "getCollaborationExchangeService";

        if (collaborationExchangeActive)
        {
            return collaborationExchangeService;
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.DISABLED_EXCHANGE_SERVICE.getMessageDefinition(collaborationExchangeServiceName,
                                                                                              integrationServiceName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Return the interface for exchanging connection information (connections, connector types, endpoints).
     *
     * @return connection exchange service
     * @throws UserNotAuthorizedException this option is not enabled in the configuration
     */
    public ConnectionExchangeService getConnectionExchangeService() throws UserNotAuthorizedException
    {
        final String methodName = "getConnectionExchangeService";

        if (connectionExchangeActive)
        {
            return connectionExchangeService;
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.DISABLED_EXCHANGE_SERVICE.getMessageDefinition(connectionExchangeServiceName,
                                                                                              integrationServiceName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Return the interface for exchanging data asset information (assets, schemas, connections).
     *
     * @return data asset exchange service
     * @throws UserNotAuthorizedException this option is not enabled in the configuration
     */
    public DataAssetExchangeService getDataAssetExchangeService() throws UserNotAuthorizedException
    {
        final String methodName = "getDataAssetExchangeService";

        if (dataAssetExchangeActive)
        {
            return dataAssetExchangeService;
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.DISABLED_EXCHANGE_SERVICE.getMessageDefinition(dataAssetExchangeServiceName,
                                                                                              integrationServiceName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Return the interface for exchanging data asset information (assets, schemas, connections).
     *
     * @return data asset exchange service
     * @throws UserNotAuthorizedException this option is not enabled in the configuration
     */
    public ExternalReferenceExchangeService getExternalReferenceService() throws UserNotAuthorizedException
    {
        final String methodName = "getExternalReferenceService";

        if (externalReferenceExchangeActive)
        {
            return externalReferenceExchangeService;
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.DISABLED_EXCHANGE_SERVICE.getMessageDefinition(externalReferenceExchangeServiceName,
                                                                                              integrationServiceName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Return the interface for exchanging glossary information (glossaries, with categories, terms and relationships)
     *
     * @return glossary exchange service
     * @throws UserNotAuthorizedException this option is not enabled in the configuration
     */
    public GlossaryExchangeService getGlossaryExchangeService() throws UserNotAuthorizedException
    {
        final String methodName = "getGlossaryExchangeService";

        if (glossaryExchangeActive)
        {
            return glossaryExchangeService;
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.DISABLED_EXCHANGE_SERVICE.getMessageDefinition(glossaryExchangeServiceName,
                                                                                              integrationServiceName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Return the interface for exchanging governance information (policies, rules, ...)
     *
     * @return governance exchange service
     * @throws UserNotAuthorizedException this option is not enabled in the configuration
     */
    public GovernanceExchangeService getGovernanceExchangeService() throws UserNotAuthorizedException
    {
        final String methodName = "getGovernanceExchangeService";

        if (governanceExchangeActive)
        {
            return governanceExchangeService;
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.DISABLED_EXCHANGE_SERVICE.getMessageDefinition(governanceExchangeServiceName,
                                                                                              integrationServiceName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Return the interface for exchanging infrastructure information (servers, applications, hosts etc).
     *
     * @return infrastructure exchange service
     * @throws UserNotAuthorizedException this option is not enabled in the configuration
     */
    public InfrastructureExchangeService getInfrastructureExchangeService() throws UserNotAuthorizedException
    {
        final String methodName = "getInfrastructureExchangeService";

        if (infrastructureExchangeActive)
        {
            return infrastructureExchangeService;
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.DISABLED_EXCHANGE_SERVICE.getMessageDefinition(infrastructureExchangeServiceName,
                                                                                              integrationServiceName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Return the interface for exchanging lineage information (processes, ports, schemas, lineage linkage etc).
     *
     * @return lineage exchange service
     * @throws UserNotAuthorizedException this option is not enabled in the configuration
     */
    public LineageExchangeService getLineageExchangeService() throws UserNotAuthorizedException
    {
        final String methodName = "getLineageExchangeService";

        if (lineageExchangeActive)
        {
            return lineageExchangeService;
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.DISABLED_EXCHANGE_SERVICE.getMessageDefinition(lineageExchangeServiceName,
                                                                                              integrationServiceName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Return the interface for exchanging stewardship information.
     *
     * @return stewardship exchange service
     * @throws UserNotAuthorizedException this option is not enabled in the configuration
     */
    public StewardshipExchangeService getStewardshipExchangeService() throws UserNotAuthorizedException
    {
        final String methodName = "getStewardshipExchangeService";

        if (stewardshipExchangeActive)
        {
            return stewardshipExchangeService;
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.DISABLED_EXCHANGE_SERVICE.getMessageDefinition(stewardshipExchangeServiceName,
                                                                                              integrationServiceName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Return the interface for exchanging valid values and reference data information.
     *
     * @return valid values exchange service
     * @throws UserNotAuthorizedException this option is not enabled in the configuration
     */
    public ValidValuesExchangeService getValidValuesExchangeService() throws UserNotAuthorizedException
    {
        final String methodName = "getValidValuesExchangeService";

        if (validValuesExchangeActive)
        {
            return validValuesExchangeService;
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.DISABLED_EXCHANGE_SERVICE.getMessageDefinition(validValuesExchangeServiceName,
                                                                                              integrationServiceName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }
}
