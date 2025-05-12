/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.integrationservices.lineage.properties.OpenLineageRunEvent;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * APIBasedOpenLineageLogStoreConnector provides a connector implementation for an API based open lineage log.
 * Each lineage record is passed to the supplied lineage API.
 */
public class APIBasedOpenLineageLogStoreConnector extends OpenLineageLogStoreConnectorBase
{
    private RestTemplate   restTemplate = null;
    private HttpHeaders    header = null;


    /**
     * Default constructor used by the connector provider.
     */
    public APIBasedOpenLineageLogStoreConnector()
    {
        super();
    }


    /**
     * Set up the name of the file store
     *
     * @throws ConnectorCheckedException something went wrong
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        try
        {
            restTemplate = new RestTemplate();

            /* Ensure that the REST template always uses UTF-8 */
            List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
            converters.removeIf(httpMessageConverter -> httpMessageConverter instanceof StringHttpMessageConverter);
            converters.add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

            header = new HttpHeaders();

            header.setContentType(MediaType.APPLICATION_JSON);
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(
                    OpenLineageIntegrationConnectorErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                       error.getClass().getName(),
                                                                                                       methodName,
                                                                                                       error.getMessage()),
                    this.getClass().getName(),
                    methodName,
                    error);
        }
    }


    /**
     * Informs the subclasses that there is a new destination - in case they need to do special setup.
     *
     * @param destinationAddress new destination
     */
    @Override
    protected void newDestinationIdentified(String destinationAddress)
    {
        // nothing to do
    }


    /**
     * Store the open lineage event in the open lineage log store.  If the raw event is null, a json version of the open lineage event is
     * generated using the Egeria beans.
     *
     * @param openLineageEvent event formatted using Egeria beans
     * @param rawEvent event in Json form from the originator - may have facets that are not known to Egeria
     * @param logStoreURL address to send the event
     *
     * @throws InvalidParameterException indicates that the openLineageEvent parameter is invalid
     */
    @Override
    public void storeEvent(OpenLineageRunEvent openLineageEvent,
                           String              rawEvent,
                           String              logStoreURL) throws InvalidParameterException
    {
        final String methodName = "storeEvent";

        if (rawEvent != null)
        {
            try
            {
                HttpEntity<?> request = new HttpEntity<>((Object) rawEvent, header);

                restTemplate.exchange(logStoreURL, HttpMethod.POST, request, Void.class);
            }
            catch (Exception error)
            {
                auditLog.logMessage(methodName, OpenLineageIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                                   error.getClass().getName(),
                                                                                                                                   methodName,
                                                                                                                                   error.getMessage()),
                                    logStoreURL);
            }
        }
        else
        {
            super.logNoRawEvent(openLineageEvent);
        }
    }
}
