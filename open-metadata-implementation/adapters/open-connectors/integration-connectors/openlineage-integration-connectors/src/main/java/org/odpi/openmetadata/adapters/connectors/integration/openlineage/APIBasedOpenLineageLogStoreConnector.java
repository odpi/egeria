/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
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
    private static final String defaultLogStoreURL = "http://localhost:5000/api/v1/lineage";

    private String         logStoreURL = null;
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

        EndpointProperties endpoint = connectionProperties.getEndpoint();

        if (endpoint != null)
        {
            logStoreURL = endpoint.getAddress();
        }

        if (logStoreURL == null)
        {
            logStoreURL = defaultLogStoreURL;
        }

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
     * Store the open lineage event in the open lineage log store.  If the raw event is null, a json version of the open lineage event is
     * generated using the Egeria beans.
     *
     * @param openLineageEvent event formatted using Egeria beans
     * @param rawEvent event in Json form from the originator - may have facets that are not known to Egeria
     *
     * @throws InvalidParameterException indicates that the openLineageEvent parameter is invalid
     * @throws UserNotAuthorizedException indicates that the caller is not authorized to issue this rest call
     * @throws PropertyServerException  indicates that the  log store is not available or has an error
     */
    @Override
    public void storeEvent(OpenLineageRunEvent openLineageEvent,
                           String              rawEvent) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String methodName = "storeEvent";

        if (rawEvent != null)
        {
            HttpEntity<?> request = new HttpEntity<>((Object)rawEvent, header);

            restTemplate.exchange(logStoreURL, HttpMethod.POST, request, Void.class);
        }
        else
        {
            super.logNoRawEvent(openLineageEvent, methodName);
        }
    }
}
