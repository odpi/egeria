/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectorTypeProperties;
import org.odpi.openmetadata.integrationservices.lineage.connector.LineageIntegratorConnector;
import org.odpi.openmetadata.integrationservices.lineage.connector.LineageIntegratorContext;
import org.odpi.openmetadata.integrationservices.lineage.connector.OpenLineageEventListener;
import org.odpi.openmetadata.integrationservices.lineage.properties.OpenLineageRunEvent;

import java.util.HashMap;
import java.util.Map;


/**
 * OpenLineageLogStoreConnectorBase is the base class for connectors that support the OMRSAuditLog.
 * It has implementations of the query methods that throw "function not supported".  This means that
 * log destinations that do not support queries can ignore these methods.
 * It also supports the start and stop method for the connector which only need to be
 * overridden if the connector has work to do at these times
 */
public abstract class OpenLineageLogStoreConnectorBase extends LineageIntegratorConnector implements OpenLineageLogStore,
                                                                                                     OpenLineageEventListener
{

    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer();
    protected String                   destinationName = "<Unknown";
    protected LineageIntegratorContext myContext       = null;


    /**
     * Default constructor
     */
    public OpenLineageLogStoreConnectorBase()
    {
    }


    /**
     * Return the name of this open lineage log destination.
     *
     * @return string display name suitable for messages.
     */
    public String  getDestinationName()
    {
        return destinationName;
    }
    

    /**
     * Store the open lineage event in the open lineage log store.  If the raw event is null, a json version of the open lineage event is
     * generated using the Egeria beans.
     *
     * @param openLineageEvent event formatted using Egeria beans
     * @param rawEvent event in Json form from the originator - may have facets that are not known to Egeria
     *                 
     * @throws InvalidParameterException indicates that the openLineageEvent parameter is invalid.
     * @throws UserNotAuthorizedException indicates that the caller is not authorized to access the log store.
     * @throws PropertyServerException  indicates that the  log store is not available or has an error.
     */
    public abstract void storeEvent(OpenLineageRunEvent openLineageEvent,
                                    String              rawEvent) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;
    


    /**
     * Create JSON version of the openLineage event.
     *
     * @param openLineageEvent event formatted using Egeria beans
     * @param methodName calling method
     * @return JSON string
     * @throws InvalidParameterException unable to convert the openLineage event.
     */
    private String getJSONOpenLineageEvent(OpenLineageRunEvent openLineageEvent,
                                           String              methodName) throws InvalidParameterException
    {
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
     * @param connectionProperties   POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String               connectorInstanceId,
                           ConnectionProperties connectionProperties)
    {
        super.initialize(connectorInstanceId, connectionProperties);

        if (connectionProperties != null)
        {
            if (connectionProperties.getDisplayName() != null)
            {
                destinationName = connectionProperties.getDisplayName();
            }
            else if (connectionProperties.getConnectorType() != null)
            {
                ConnectorTypeProperties connectorType = connectionProperties.getConnectorType();

                if (connectorType.getDisplayName() != null)
                {
                    destinationName = connectorType.getDisplayName();
                }
            }
        }
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public synchronized void start() throws ConnectorCheckedException
    {
        super.start();

        myContext = super.getContext();

        if (myContext != null)
        {
            myContext.registerListener(this);
        }
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    public void refresh() throws ConnectorCheckedException
    {
        // nothing to do
    }


    /**
     * Called each time an open lineage run event is published to the Lineage Integrator OMIS.  The integration connector is able to
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
                jsonEvent = this.getJSONOpenLineageEvent(event, methodName);
            }

            storeEvent(event, rawEvent);
        }
        catch (Exception error)
        {
            if (auditLog != null)
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
    }


    /**
     * Throws an invalid parameter exception.  Used by the subclasses when this class has failed to pass a raw event.
     *
     * @param openLineageEvent supplied open lineage event - may also be null
     * @param methodName calling method
     * @throws InvalidParameterException resulting exception
     */
    protected void logNoRawEvent(OpenLineageRunEvent openLineageEvent,
                                 String              methodName) throws InvalidParameterException
    {
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
