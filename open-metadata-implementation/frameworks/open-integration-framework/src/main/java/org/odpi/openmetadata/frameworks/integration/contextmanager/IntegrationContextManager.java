/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.contextmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.client.ConnectedAssetClient;
import org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentFormatter;
import org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentListener;
import org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentManager;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolDocument;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContract;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProduct;
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
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementOriginCategory;
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
public abstract class IntegrationContextManager implements OpenLineageListenerManager,
                                                           BitolDocumentManager
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
    private final        List<BitolDocumentListener>    registeredBitolListeners = new ArrayList<>();

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
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
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


    /* ======================================================================================
     * Bitol documents (ODCS data contracts and ODPS data products)
     */

    /**
     * The listener is implemented by the integration connector.  Once it is registered with the context, its processDataContract()
     * and processDataProduct() methods are called each time a Bitol document is published to the integration daemon.
     *
     * @param listener listener to call
     */
    @Override
    public synchronized void registerListener(BitolDocumentListener listener)
    {
        registeredBitolListeners.add(listener);
    }


    /**
     * Publish a Bitol document of either kind.  The document is parsed and routed to the listeners according to its "kind" property.
     * If the document can not be parsed into Egeria's beans but its kind can be determined, the raw document is still passed to the
     * listeners (with a null bean) so that it can be stored or forwarded.
     *
     * @param rawDocument document in YAML or JSON format
     */
    @Override
    public synchronized void publishBitolDocument(String rawDocument)
    {
        final String methodName = "publishBitolDocument";

        publishRawBitolDocument(rawDocument, null, methodName);
    }


    /**
     * Publish an Open Data Contract Standard (ODCS) data contract.
     *
     * @param rawDocument document in YAML or JSON format
     */
    @Override
    public synchronized void publishDataContract(String rawDocument)
    {
        final String methodName = "publishDataContract";

        publishRawBitolDocument(rawDocument, BitolDocument.DATA_CONTRACT_KIND, methodName);
    }


    /**
     * Publish an Open Data Contract Standard (ODCS) data contract.
     *
     * @param dataContract bean for the document
     */
    @Override
    public synchronized void publishDataContract(DataContract dataContract)
    {
        final String methodName = "publishDataContract(bean)";

        if (dataContract != null)
        {
            String rawDocument = null;

            try
            {
                rawDocument = BitolDocumentFormatter.toYAML(dataContract);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      OIFAuditCode.BITOL_FORMAT_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                           error.getMessage(),
                                                                                           dataContract.toString()),
                                      dataContract.toString(),
                                      error);
            }

            publishToBitolListeners(dataContract, null, rawDocument, methodName);
        }
    }


    /**
     * Publish an Open Data Product Standard (ODPS) data product.
     *
     * @param rawDocument document in YAML or JSON format
     */
    @Override
    public synchronized void publishDataProduct(String rawDocument)
    {
        final String methodName = "publishDataProduct";

        publishRawBitolDocument(rawDocument, BitolDocument.DATA_PRODUCT_KIND, methodName);
    }


    /**
     * Publish an Open Data Product Standard (ODPS) data product.
     *
     * @param dataProduct bean for the document
     */
    @Override
    public synchronized void publishDataProduct(DataProduct dataProduct)
    {
        final String methodName = "publishDataProduct(bean)";

        if (dataProduct != null)
        {
            String rawDocument = null;

            try
            {
                rawDocument = BitolDocumentFormatter.toYAML(dataProduct);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      OIFAuditCode.BITOL_FORMAT_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                           error.getMessage(),
                                                                                           dataProduct.toString()),
                                      dataProduct.toString(),
                                      error);
            }

            publishToBitolListeners(null, dataProduct, rawDocument, methodName);
        }
    }


    /**
     * Parse a raw Bitol document, validate its kind and version, and pass it to the listeners.
     *
     * @param rawDocument document in YAML or JSON format
     * @param expectedKind the kind of document expected by the calling method, or null if either kind is acceptable
     * @param methodName calling method
     */
    private void publishRawBitolDocument(String rawDocument,
                                         String expectedKind,
                                         String methodName)
    {
        if (rawDocument == null)
        {
            return;
        }

        String        kind     = null;
        BitolDocument document = null;

        try
        {
            kind = BitolDocumentFormatter.getKind(rawDocument);

            if ((kind != null) && ((expectedKind == null) || (expectedKind.equals(kind))))
            {
                document = BitolDocumentFormatter.parseDocument(rawDocument);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  OIFAuditCode.BITOL_FORMAT_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                       error.getMessage(),
                                                                                       rawDocument),
                                  rawDocument,
                                  error);
        }

        if ((expectedKind != null) && (! expectedKind.equals(kind)))
        {
            auditLog.logMessage(methodName,
                                OIFAuditCode.BITOL_UNEXPECTED_KIND.getMessageDefinition(kind,
                                                                                        methodName,
                                                                                        expectedKind,
                                                                                        getDocumentStart(rawDocument)));
            return;
        }

        if ((document != null) && (! document.hasSupportedApiVersion()))
        {
            auditLog.logMessage(methodName,
                                OIFAuditCode.BITOL_UNSUPPORTED_VERSION.getMessageDefinition(kind,
                                                                                            document.getId(),
                                                                                            document.getApiVersion()));
            document = null;
        }

        if (BitolDocument.DATA_CONTRACT_KIND.equals(kind))
        {
            publishToBitolListeners((DataContract) document, null, rawDocument, methodName);
        }
        else if (BitolDocument.DATA_PRODUCT_KIND.equals(kind))
        {
            publishToBitolListeners(null, (DataProduct) document, rawDocument, methodName);
        }
        else
        {
            auditLog.logMessage(methodName,
                                OIFAuditCode.BITOL_UNEXPECTED_KIND.getMessageDefinition(kind,
                                                                                        methodName,
                                                                                        BitolDocument.DATA_CONTRACT_KIND + " or " + BitolDocument.DATA_PRODUCT_KIND,
                                                                                        getDocumentStart(rawDocument)));
        }
    }


    /**
     * Return the start of a document for use in an audit log message.
     *
     * @param rawDocument document
     * @return first few hundred characters
     */
    private String getDocumentStart(String rawDocument)
    {
        final int maxLength = 300;

        if (rawDocument.length() > maxLength)
        {
            return rawDocument.substring(0, maxLength) + " ...";
        }

        return rawDocument;
    }


    /**
     * Loop through the Bitol listeners sending the document to each.  If a connector throws an exception, it is logged and the
     * publishing process continues with the other listeners.
     *
     * @param dataContract bean for a data contract (null if the document is a data product or could not be parsed)
     * @param dataProduct bean for a data product (null if the document is a data contract or could not be parsed)
     * @param rawDocument the document as received or serialized
     * @param methodName calling method
     */
    private void publishToBitolListeners(DataContract dataContract,
                                         DataProduct  dataProduct,
                                         String       rawDocument,
                                         String       methodName)
    {
        for (BitolDocumentListener listener : registeredBitolListeners)
        {
            if (listener != null)
            {
                try
                {
                    if (dataProduct != null)
                    {
                        listener.processDataProduct(dataProduct, rawDocument);
                    }
                    else if (dataContract != null)
                    {
                        listener.processDataContract(dataContract, rawDocument);
                    }
                    else if (BitolDocument.DATA_PRODUCT_KIND.equals(BitolDocumentFormatter.getKind(rawDocument)))
                    {
                        listener.processDataProduct(null, rawDocument);
                    }
                    else
                    {
                        listener.processDataContract(null, rawDocument);
                    }
                }
                catch (Exception error)
                {
                    auditLog.logException(methodName,
                                          OIFAuditCode.BITOL_PUBLISH_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                                error.getMessage()),
                                          rawDocument,
                                          error);
                }
            }
        }
    }
}
