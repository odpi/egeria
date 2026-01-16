/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.contextmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.client.ConnectedAssetClient;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnector;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFAuditCode;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageEventListener;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageListenerManager;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRunEvent;
import org.odpi.openmetadata.frameworks.opengovernance.client.GovernanceConfiguration;
import org.odpi.openmetadata.frameworks.opengovernance.client.OpenGovernanceClient;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ElementOriginCategory;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.AssetHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * IntegrationContextManager is the base class for the context manager that is implemented by each integration service.
 */
public abstract class IntegrationContextManager implements OpenLineageListenerManager
{
    protected String                  partnerOMASPlatformRootURL = null;
    protected String                  partnerOMASServerName      = null;
    protected GovernanceConfiguration governanceConfiguration    = null;
    protected ConnectedAssetClient    connectedAssetClient       = null;
    protected OpenMetadataClient      openMetadataClient         = null;
    protected AssetHandler            assetHandler               = null;
    protected OpenGovernanceClient    openGovernanceClient       = null;
    protected String                  localServerName            = null;
    protected String                  localServiceName           = null;
    protected String                  localServerUserId          = null;
    protected String                  secretsStoreProvider       = null;
    protected String                  secretsStoreLocation       = null;
    protected String                  secretsStoreCollection     = null;
    protected int                     maxPageSize                = 0;
    protected AuditLog                auditLog                   = null;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ObjectWriter                   OBJECT_WRITER            = OBJECT_MAPPER.writer();
    private static final ObjectReader                   OBJECT_READER            = OBJECT_MAPPER.reader();
    private final        List<OpenLineageEventListener> registeredEventListeners = new ArrayList<>();

    /**
     * Default constructor
     */
    protected IntegrationContextManager()
    {
    }


    /**
     * Initialize server properties for the context manager.
     *
     * @param localServerName name of this integration daemon
     * @param localServiceName name of calling service
     * @param partnerOMASServerName name of the server to connect to
     * @param partnerOMASPlatformRootURL the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param secretsStoreProvider secrets store connector for bearer token
     * @param secretsStoreLocation secrets store location for bearer token
     * @param secretsStoreCollection secrets store collection for bearer token
     * @param maxPageSize maximum number of results that can be returned on a single REST call
     * @param auditLog logging destination
     */
    public void initializeContextManager(String   localServerName,
                                         String   localServiceName,
                                         String   partnerOMASServerName,
                                         String   partnerOMASPlatformRootURL,
                                         String   userId,
                                         String   secretsStoreProvider,
                                         String   secretsStoreLocation,
                                         String   secretsStoreCollection,
                                         int      maxPageSize,
                                         AuditLog auditLog)
    {
        this.localServerName            = localServerName;
        this.localServiceName           = localServiceName;
        this.partnerOMASPlatformRootURL = partnerOMASPlatformRootURL;
        this.partnerOMASServerName      = partnerOMASServerName;
        this.localServerUserId          = userId;
        this.secretsStoreProvider       = secretsStoreProvider;
        this.secretsStoreLocation       = secretsStoreLocation;
        this.secretsStoreCollection     = secretsStoreCollection;
        this.maxPageSize                = maxPageSize;
        this.auditLog                   = auditLog;

        final String methodName = "initializeContextManager";

        auditLog.logMessage(methodName,
                            OIFAuditCode.CONTEXT_INITIALIZING.getMessageDefinition(partnerOMASServerName, partnerOMASPlatformRootURL));
    }


    /**
     * Suggestion for subclass to create client(s) to partner OMAS.
     *
     * @throws InvalidParameterException the subclass is not able to create one of its clients
     */
    public abstract void createClients() throws InvalidParameterException;


    /**
     * Suggestion for subclass to create client(s) to partner OMAS.
     *
     * @param connectorId used as the caller Id
     * @throws InvalidParameterException the subclass is not able to create one of its clients
     */
    public abstract OpenMetadataEventClient createEventClient(String connectorId) throws InvalidParameterException;


    /**
     * Retrieve the metadata source's unique identifier (GUID) or if it is not defined, create the software server capability
     * for this service.
     *
     * @param metadataSourceQualifiedName unique name of the software capability that represents this integration service
     * @param connectorId unique identifier of the connector (used to configure the event listener)
     * @param connectorName name of connector from config
     * @param connectorUserId userId for the connector
     *
     * @return unique identifier of the metadata source
     *
     * @throws InvalidParameterException one of the parameters passed (probably on initialize) is invalid
     * @throws UserNotAuthorizedException the integration daemon's userId does not have access to the partner OMAS
     * @throws PropertyServerException a problem in the remote server running the partner OMAS
     */
    protected String setUpMetadataSource(String metadataSourceQualifiedName,
                                         String connectorId,
                                         String connectorName,
                                         String connectorUserId) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        if ((assetHandler != null) && (metadataSourceQualifiedName != null))
        {
            return assetHandler.setUpMetadataSource(localServerUserId,
                                                    metadataSourceQualifiedName,
                                                    connectorId,
                                                    connectorName,
                                                    connectorUserId,
                                                    ElementOriginCategory.EXTERNAL_SOURCE);
        }

        return null;
    }


    /**
     * Set up the context in the supplied connector. This is called between initialize() and start() on the connector.
     *
     * @param connectorId unique identifier of the connector (used to configure the event listener)
     * @param connectorName name of connector from config
     * @param connectorUserId userId for the connector
     * @param integrationConnector connector created from connection integration service configuration
     * @param integrationConnectorGUID unique identifier of the integration connector entity (only set if working with integration groups)
     * @param permittedSynchronization controls the direction(s) that metadata is allowed to flow
     * @param generateIntegrationReport should the connector generate an integration reports?
     * @param metadataSourceQualifiedName unique name of the software server capability that represents the metadata source.
     *
     * @return the new integration context
     * @throws InvalidParameterException the connector is not of the correct type
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public IntegrationContext setContext(String                   connectorId,
                                         String                   connectorName,
                                         String                   connectorUserId,
                                         IntegrationConnector     integrationConnector,
                                         String                   integrationConnectorGUID,
                                         PermittedSynchronization permittedSynchronization,
                                         boolean                  generateIntegrationReport,
                                         String                   metadataSourceQualifiedName,
                                         DeleteMethod             deleteMethod) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        IntegrationContext integrationContext = null;

        String externalSourceGUID = this.setUpMetadataSource(metadataSourceQualifiedName,
                                                             connectorId,
                                                             connectorName,
                                                             connectorUserId);

        String externalSourceName = metadataSourceQualifiedName;

        if (externalSourceGUID == null)
        {
            externalSourceName = null;
        }

        if (openMetadataClient != null)
        {
            integrationContext = new IntegrationContext(localServerName,
                                                        localServiceName,
                                                        externalSourceGUID,
                                                        externalSourceName,
                                                        connectorId,
                                                        connectorName,
                                                        connectorUserId,
                                                        integrationConnectorGUID,
                                                        generateIntegrationReport,
                                                        permittedSynchronization,
                                                        openMetadataClient,
                                                        this.createEventClient(connectorId),
                                                        connectedAssetClient,
                                                        this,
                                                        governanceConfiguration,
                                                        openGovernanceClient,
                                                        auditLog,
                                                        maxPageSize,
                                                        deleteMethod);
        }

        integrationConnector.setContext(integrationContext);
        integrationConnector.setConnectorName(connectorName);

        return integrationContext;
    }

    /**
     * The listener is implemented by the integration connector.  Once it is registered with the context, its processOpenLineageRunEvent()
     * method is called each time an open lineage event is published to the integration daemon.
     *
     * @param listener listener to call
     */
    @Override
    public synchronized  void registerListener(OpenLineageEventListener listener)
    {
        registeredEventListeners.add(listener);
    }

    /**
     * Called each time an open lineage run event is published to the integration daemon.  The integration connector is able to
     * work with the formatted event using the Egeria beans or reformat the open lineage run event using the supplied open lineage backend beans
     * or another set of beans.
     *
     * @param rawEvent json payload received for the event
     */
    @Override
    public synchronized void publishOpenLineageRunEvent(String rawEvent)
    {
        final String methodName = "publishOpenLineageRunEvent(rawEvent)";

        OpenLineageRunEvent event = null;

        if (rawEvent != null)
        {
            try
            {
                event = OBJECT_READER.readValue(rawEvent, OpenLineageRunEvent.class);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      OIFAuditCode.OPEN_LINEAGE_FORMAT_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                                  error.getMessage(),
                                                                                                  rawEvent),
                                      rawEvent,
                                      error);
            }
        }

        publishToListeners(event, rawEvent, methodName);
    }

    /**
     * Called each time an open lineage run event is published to the integration demon.  The integration connector is able to
     * work with the formatted event using the Egeria beans or reformat the open lineage run event using the supplied open lineage backend beans
     * or another set of beans.
     *
     * @param event bean for the event
     */
    @Override
    public synchronized void publishOpenLineageRunEvent(OpenLineageRunEvent event)
    {
        final String methodName = "publishOpenLineageRunEvent(event)";

        String rawEvent = null;

        if (event != null)
        {
            try
            {
                rawEvent = OBJECT_WRITER.writeValueAsString(event);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      OIFAuditCode.OPEN_LINEAGE_FORMAT_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                                  error.getMessage(),
                                                                                                  event.toString()),
                                      event.toString(),
                                      error);
            }
        }

        publishToListeners(event, rawEvent, methodName);
    }


    /**
     * Loop through the listeners and sending an event to each.  If a connector throws an exception, it is logged and the publishing process
     * continues with the other listeners.
     *
     * @param event event bean
     * @param rawEvent string event
     * @param methodName calling method
     */
    private void publishToListeners(OpenLineageRunEvent event,
                                    String              rawEvent,
                                    String              methodName)
    {
        for (OpenLineageEventListener listener : registeredEventListeners)
        {
            if (listener != null)
            {
                try
                {
                    listener.processOpenLineageRunEvent(event, rawEvent);
                }
                catch (Exception error)
                {
                    auditLog.logException(methodName,
                                          OIFAuditCode.OPEN_LINEAGE_PUBLISH_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                                                     error.getMessage()),
                                          rawEvent,
                                          error);
                }
            }
        }
    }
}
