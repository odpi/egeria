/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageEventListener;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRunEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


import java.util.*;


/**
 * OpenLineageLogStoreConnectorBase is the base class for connectors that support the OMRSAuditLog.
 * It has implementations of the query methods that throw "function not supported".  This means that
 * log destinations that do not support queries can ignore these methods.
 * It also supports the start and stop method for the connector which only need to be
 * overridden if the connector has work to do at these times
 */
public abstract class OpenLineageLogStoreConnectorBase extends IntegrationConnectorBase implements OpenLineageLogStore,
                                                                                                   OpenLineageEventListener
{

    private static final ObjectWriter  OBJECT_WRITER   = new ObjectMapper().writer();
    protected       String             distributorName      = "<Unknown>";
    protected       IntegrationContext myContext            = null;
    protected final List<String>       destinationAddresses = new ArrayList<>();


    /**
     * Default constructor
     */
    public OpenLineageLogStoreConnectorBase()
    {
    }


    /**
     * Store the open lineage event in the open lineage log store.  If the raw event is null, a json version of the open lineage event is
     * generated using the Egeria beans.
     *
     * @param openLineageEvent event formatted using Egeria beans
     * @param rawEvent event in Json form from the originator - may have facets that are not known to Egeria
     * @param logDestinationAddress address to send the event to
     *                 
     * @throws InvalidParameterException indicates that the openLineageEvent parameter is invalid.
     * @throws UserNotAuthorizedException indicates that the caller is not authorized to access the log store.
     * @throws PropertyServerException  indicates that the  log store is not available or has an error.
     */
    @Override
    public abstract void storeEvent(OpenLineageRunEvent openLineageEvent,
                                    String              rawEvent,
                                    String              logDestinationAddress) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException;


    /**
     * Informs the subclasses that there is a new destination - in case they need to do special setup.
     *
     * @param destinationAddress new destination
     * @throws ConnectorCheckedException new destination not valid
     */
    protected abstract void newDestinationIdentified(String destinationAddress) throws ConnectorCheckedException;


    /**
     * Create JSON version of the openLineage event.
     *
     * @param openLineageEvent event formatted using Egeria beans
     * @return JSON string
     * @throws InvalidParameterException unable to convert the openLineage event.
     */
    private String getJSONOpenLineageEvent(OpenLineageRunEvent openLineageEvent) throws InvalidParameterException
    {
        final String methodName = "getJSONOpenLineageEvent";
        final String parameterName = "openLineageEvent";

        if (openLineageEvent != null)
        {

            try
            {
                return OBJECT_WRITER.writeValueAsString(openLineageEvent);
            }
            catch (Exception error)
            {
                throw new InvalidParameterException(
                        OpenLineageIntegrationConnectorErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                           error.getClass().getName(),
                                                                                                           methodName,
                                                                                                           error.getMessage()),
                        this.getClass().getName(),
                        methodName,
                        error,
                        parameterName);
            }
        }

        return null;
    }


    /**
     * Call made by the ConnectorProvider to initialize the Connector with the base services.
     *
     * @param connectorInstanceId   unique id for the connector instance   useful for messages etc
     * @param connectionDetails   POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String     connectorInstanceId,
                           Connection connectionDetails)
    {
        super.initialize(connectorInstanceId, connectionDetails);

        if (connectionDetails != null)
        {
            if (connectionDetails.getDisplayName() != null)
            {
                distributorName = connectionDetails.getDisplayName();
            }
            else if (connectionDetails.getConnectorType() != null)
            {
                ConnectorType connectorType = connectionDetails.getConnectorType();

                if (connectorType.getDisplayName() != null)
                {
                    distributorName = connectorType.getDisplayName();
                }
            }
        }
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        Endpoint endpoint = connectionBean.getEndpoint();

        if ((endpoint != null) && (endpoint.getAddress() != null))
        {
            destinationAddresses.add(endpoint.getAddress());
            this.newDestinationIdentified(endpoint.getAddress());
        }

        myContext = integrationContext;

        if (myContext != null)
        {
            myContext.registerListener(this);
        }
    }


    /**
     * Maintains the list of catalog targets.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the catalog targets.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        try
        {
            int                startFrom = 0;
            List<CatalogTarget> catalogTargets = myContext.getConnectorConfigClient().getCatalogTargets(integrationContext.getIntegrationConnectorGUID(),
                                                                                                        startFrom,
                                                                                                        myContext.getMaxPageSize());

            while (catalogTargets != null)
            {
                for (CatalogTarget catalogTarget : catalogTargets)
                {
                    if (catalogTarget != null)
                    {
                        String endpointGUID = null;
                        if (propertyHelper.isTypeOf(catalogTarget.getCatalogTargetElement(), OpenMetadataType.ENDPOINT.typeName))
                        {
                            endpointGUID = catalogTarget.getCatalogTargetElement().getGUID();
                        }
                        else if (propertyHelper.isTypeOf(catalogTarget.getCatalogTargetElement(), OpenMetadataType.CONNECTION.typeName))
                        {
                            endpointGUID = getEndpointGUID(catalogTarget.getCatalogTargetElement().getGUID());
                        }
                        else if (propertyHelper.isTypeOf(catalogTarget.getCatalogTargetElement(), OpenMetadataType.ASSET.typeName))
                        {
                            RelatedMetadataElement connectionElement = integrationContext.getOpenMetadataStore().getRelatedMetadataElement(catalogTarget.getCatalogTargetElement().getGUID(),
                                                                                                                                                                     1,
                                                                                                                                                                     OpenMetadataType.ASSET_CONNECTION_RELATIONSHIP.typeName);

                            if (connectionElement != null)
                            {
                                endpointGUID = getEndpointGUID(connectionElement.getElement().getElementGUID());
                            }
                        }

                        if (endpointGUID != null)
                        {
                            OpenMetadataElement endpoint = integrationContext.getOpenMetadataStore().getMetadataElementByGUID(endpointGUID);

                            if (endpoint != null)
                            {
                                String networkAddress = propertyHelper.getStringProperty(connectorName,
                                                                                         OpenMetadataProperty.NETWORK_ADDRESS.name,
                                                                                         endpoint.getElementProperties(),
                                                                                         methodName);

                                if ((networkAddress != null) && (! destinationAddresses.contains(networkAddress)))
                                {
                                    destinationAddresses.add(networkAddress);
                                    newDestinationIdentified(networkAddress);
                                }
                            }
                        }
                    }
                }

                startFrom = startFrom + myContext.getMaxPageSize();

                catalogTargets = myContext.getConnectorConfigClient().getCatalogTargets(integrationContext.getIntegrationConnectorGUID(),
                                                                                        startFrom,
                                                                                        myContext.getMaxPageSize());
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  OpenLineageIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                     error.getClass().getName(),
                                                                                                                     methodName,
                                                                                                                     error.getMessage()),
                                  error);
        }
    }


    /**
     * Retrieve the endpoint for the connection.
     *
     * @param connectionGUID unique identifier of the connection
     * @return unique identifier of the endpoint (or null)
     * @throws InvalidParameterException bad parameter
     * @throws PropertyServerException repository not working correctly
     * @throws UserNotAuthorizedException security problem
     */
    private String getEndpointGUID(String connectionGUID) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        String endpointGUID = null;

        RelatedMetadataElement endpointElement = integrationContext.getOpenMetadataStore().getRelatedMetadataElement(connectionGUID,
                                                                                                                     1,
                                                                                                                     OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName);

        if (endpointElement != null)
        {
            endpointGUID = endpointElement.getElement().getElementGUID();
        }

        return endpointGUID;
    }


    /**
     * Called each time an open lineage run event is published to the integration daemon.  The integration connector is able to
     * work with the formatted event using the Egeria beans or reformat the open lineage run event using the supplied open lineage backend beans
     * or another set of beans.
     *
     * @param event run event formatted using Egeria supplied beans (null if egeria can not format the event)
     * @param rawEvent json payload received for the event
     */
    @Override
    public void processOpenLineageRunEvent(OpenLineageRunEvent event,
                                           String              rawEvent)
    {
        final String methodName = "processOpenLineageRunEvent";

        String jsonEvent = rawEvent;

        try
        {
            if (jsonEvent == null)
            {
                jsonEvent = this.getJSONOpenLineageEvent(event);
            }

            for (String destinationAddress : destinationAddresses)
            {
                storeEvent(event, rawEvent, destinationAddress);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  OpenLineageIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                     error.getClass().getName(),
                                                                                                                     methodName,
                                                                                                                     error.getMessage()),
                                  jsonEvent,
                                  error);
        }
    }


    /**
     * Throws an invalid parameter exception.  Used by the subclasses when this class has failed to pass a raw event.
     *
     * @param openLineageEvent supplied open lineage event - may also be null
     * @throws InvalidParameterException resulting exception
     */
    protected void logNoRawEvent(OpenLineageRunEvent openLineageEvent) throws InvalidParameterException
    {
        final String methodName = "logNoRawEvent";
        final String parameterName = "rawEvent";

        Map<String, Object> additionalProperties = new HashMap<>();

        if (openLineageEvent != null)
        {
            additionalProperties.put(parameterName, openLineageEvent.toString());
        }

        throw new InvalidParameterException(OpenLineageIntegrationConnectorErrorCode.NO_RAW_EVENT.getMessageDefinition(connectorName, methodName),
                                            this.getClass().getName(),
                                            methodName,
                                            parameterName,
                                            additionalProperties);
    }
}
