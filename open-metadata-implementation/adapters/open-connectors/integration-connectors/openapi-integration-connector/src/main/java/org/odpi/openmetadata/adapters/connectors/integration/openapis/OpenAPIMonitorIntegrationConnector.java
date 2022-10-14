/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.openapis;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.datamanager.api.DataManagerEventListener;
import org.odpi.openmetadata.accessservices.datamanager.events.DataManagerOutboundEvent;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.APIElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.APIOperationElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.EndpointElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.APIOperationProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.APIProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.EndpointProperties;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.ffdc.OpenAPIIntegrationConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.ffdc.OpenAPIIntegrationConnectorErrorCode;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.properties.OpenAPIOperation;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.properties.OpenAPIPathDescription;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.properties.OpenAPISpecification;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.properties.OpenAPITag;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.integrationservices.api.connector.APIIntegratorConnector;
import org.odpi.openmetadata.integrationservices.api.connector.APIIntegratorContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * OpenAPIMonitorIntegrationConnector provides common methods for the connectors in this module.
 */
public class OpenAPIMonitorIntegrationConnector extends APIIntegratorConnector implements DataManagerEventListener
{
    private static final String urlMarker = "http";

    private String templateQualifiedName = null;
    private String targetRootURL = null;

    private Map<String, RESTClient> restClients = new HashMap<>();

    private APIIntegratorContext myContext = null;

    /**
     * Initialize the connector.
     *
     * @param connectorInstanceId - unique id for the connector instance - useful for messages etc
     * @param connectionProperties - POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String connectorInstanceId, ConnectionProperties connectionProperties)
    {
        super.initialize(connectorInstanceId, connectionProperties);


    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        myContext = super.getContext();

        /*
         * Retrieve information from the supplied connection.
         */
        org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties  endpoint = connectionProperties.getEndpoint();

        if (endpoint != null)
        {
            targetRootURL = endpoint.getAddress();
        }

        Map<String, Object> configurationProperties = connectionProperties.getConfigurationProperties();

        if (configurationProperties != null)
        {
            templateQualifiedName = configurationProperties.get(OpenAPIMonitorIntegrationProvider.TEMPLATE_QUALIFIED_NAME_CONFIGURATION_PROPERTY).toString();
        }

        try
        {
            if (targetRootURL != null)
            {
                RESTClient restClient = new RESTClient(connectorName, targetRootURL, auditLog);

                restClients.put(targetRootURL, restClient);
            }
            else
            {
                /*
                 * Retrieve all the rest endpoint already catalogued.
                 */
                getRESTClients();
            }
        }
        catch (OCFCheckedExceptionBase error)
        {
            throw new ConnectorCheckedException(methodName, error);
        }

        /*
         * Record the configuration
         */
        if (auditLog != null)
        {
            if (targetRootURL != null)
            {
                auditLog.logMessage(methodName,
                                    OpenAPIIntegrationConnectorAuditCode.CONNECTOR_CONFIGURATION_WITH_ENDPOINT.getMessageDefinition(connectorName,
                                                                                                                                    targetRootURL,
                                                                                                                                    templateQualifiedName));
            }
            else
            {
                auditLog.logMessage(methodName,
                                    OpenAPIIntegrationConnectorAuditCode.CONNECTOR_CONFIGURATION_NO_ENDPOINT.getMessageDefinition(connectorName,
                                                                                                                                  templateQualifiedName));
            }
        }
    }


    /**
     * Process an event that was published by the Data Manager OMAS.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    public void processEvent(DataManagerOutboundEvent event)
    {
        /*
         * Only interested in Endpoint events.
         */
        final String elementType = "Endpoint";
        final String methodName = "processEvent";

        if (elementType.equals(event.getPrincipleElement().getType().getTypeName()) ||
            ((event.getPrincipleElement().getType().getSuperTypeNames() != null)) &&
             (event.getPrincipleElement().getType().getSuperTypeNames().contains(elementType)))
        {
            try
            {
                EndpointElement endpointElement = myContext.getEndpointByGUID(event.getPrincipleElement().getGUID());

                if (endpointElement != null)
                {
                    registerEndpoint(endpointElement, methodName);
                }

            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logMessage(methodName,
                                        OpenAPIIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                       error.getClass().getName(),
                                                                                                                       methodName,
                                                                                                                       error.getMessage()));


                }
            }
        }
    }


    /**
     * Retrieve all the Endpoints
     */
    private void getRESTClients()
    {
        final String methodName = "getRESTClients";

        try
        {
            List<EndpointElement> endpointElements = myContext.findEndpoints(urlMarker, 0, 0);

            if (endpointElements != null)
            {
                for (EndpointElement endpointElement : endpointElements)
                {
                    if (endpointElement != null)
                    {
                        registerEndpoint(endpointElement, methodName);
                    }
                }
            }
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    OpenAPIIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                   error.getClass().getName(),
                                                                                                                   methodName,
                                                                                                                   error.getMessage()));


            }
        }
    }


    /**
     * Retrieve an endpoint element and if it is for a new URL, create a rest client and add it to map.
     *
     * @param endpointElement endpoint element
     * @param methodName calling method
     */
    private void registerEndpoint(EndpointElement endpointElement,
                                  String          methodName)
    {

        if ((endpointElement.getElementHeader() != null) &&
            (endpointElement.getElementHeader().getGUID() != null) &&
            (endpointElement.getEndpointProperties() != null) &&
            (endpointElement.getEndpointProperties().getAddress() != null))
        {
            if (endpointElement.getEndpointProperties().getAddress().startsWith(urlMarker))
            {
                try
                {
                    /*
                     * If this is new endpoint then add it to the restClients being monitored.
                     */
                    if (restClients.get(endpointElement.getEndpointProperties().getAddress()) == null)
                    {
                        if (auditLog != null)
                        {
                            auditLog.logMessage(methodName,
                                                OpenAPIIntegrationConnectorAuditCode.NEW_ENDPOINT.getMessageDefinition(connectorName,
                                                                                                                       endpointElement.getEndpointProperties().getDisplayName(),
                                                                                                                       endpointElement.getEndpointProperties().getAddress()));
                        }

                        RESTClient restClient = new RESTClient(connectorName, endpointElement.getEndpointProperties().getAddress(), auditLog);

                        restClients.put(endpointElement.getEndpointProperties().getAddress(), restClient);
                    }
                }
                catch (Exception error)
                {
                    if (auditLog != null)
                    {
                        auditLog.logMessage(methodName,
                                            OpenAPIIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                           error.getClass().getName(),
                                                                                                                           methodName,
                                                                                                                           error.getMessage()));


                    }
                }
            }
        }
        else
        {
            if (auditLog != null)
            {
                final String elementType = "Endpoint";

                auditLog.logMessage(methodName,
                                    OpenAPIIntegrationConnectorAuditCode.BAD_ELEMENT.getMessageDefinition(connectorName,
                                                                                                          elementType,
                                                                                                          methodName,
                                                                                                          endpointElement.toString()));


            }
        }
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * This method ...
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        if (targetRootURL != null)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    OpenAPIIntegrationConnectorAuditCode.CONNECTOR_REFRESH_WITH_ENDPOINT.getMessageDefinition(connectorName,
                                                                                                                              targetRootURL));


            }
        }
        else
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    OpenAPIIntegrationConnectorAuditCode.CONNECTOR_REFRESH_ALL_ENDPOINTS.getMessageDefinition(connectorName,
                                                                                                                              Integer.toString(restClients.size())));


            }
        }

        for (String url : restClients.keySet())
        {
            if (url != null)
            {
                try
                {
                    RESTClient restClient = restClients.get(url);

                    String openAPIJSON = restClient.callGetRESTCallNoParams(methodName, String.class, url + "/v3/api-docs");

                    if (openAPIJSON != null)
                    {
                        ObjectMapper         objectMapper = new ObjectMapper();
                        OpenAPISpecification openAPISpecification = objectMapper.readValue(openAPIJSON, OpenAPISpecification.class);
                        String               title = "<Untitled>";

                        if (openAPISpecification != null)
                        {
                            if (auditLog != null)
                            {
                                if (openAPISpecification.getInfo() != null)
                                {
                                    title = openAPISpecification.getInfo().getTitle();
                                }

                                auditLog.logMessage(methodName,
                                                    OpenAPIIntegrationConnectorAuditCode.RETRIEVED_OPEN_API_SPEC.getMessageDefinition(connectorName,
                                                                                                                                      url,
                                                                                                                                      title));
                            }

                            /*
                             * all the discovered APIs are added to the server's endpoint.
                             */
                            String endpointGUID = this.getEndpointGUID(url, openAPISpecification);

                            /*
                             * Each API/Operation discovered is added to the map as it is added to the catalog.
                             * This is used to create the summary audit log message - and as
                             * lookup for the apiGUID/apiOperationGUID when adding detail elements.
                             */
                            Map<String, String> apiGUIDMap = new HashMap<>(); /* map from API (Tag) name to GUID */
                            Map<String, String> apiOperationGUIDMap = new HashMap<>(); /* map from API (Path/Operation) name to GUID */

                            if (openAPISpecification.getTags() != null)
                            {
                                for (OpenAPITag tag : openAPISpecification.getTags())
                                {
                                    String apiGUID = getAPIGUID(url, endpointGUID, tag);

                                    if (apiGUID != null)
                                    {
                                        apiGUIDMap.put(tag.getName(), apiGUID);
                                    }
                                }
                            }


                            if (openAPISpecification.getPaths() != null)
                            {
                                Map<String, OpenAPIPathDescription> paths = openAPISpecification.getPaths();

                                for (String pathName : paths.keySet())
                                {
                                    String apiOperationGUID;
                                    String apiOperationQualifiedName;

                                    OpenAPIPathDescription pathDescription = paths.get(pathName);

                                    if (pathDescription.getGet() != null)
                                    {
                                        apiOperationQualifiedName = "GET " + pathName;

                                        apiOperationGUID = getAPIOperationGUID(apiGUIDMap, apiOperationQualifiedName, pathDescription.getGet());

                                        if (apiOperationGUID != null)
                                        {
                                            apiOperationGUIDMap.put(apiOperationQualifiedName, apiOperationGUID);
                                        }
                                    }

                                    if (pathDescription.getPost() != null)
                                    {
                                        apiOperationQualifiedName = "POST " + pathName;

                                        apiOperationGUID = getAPIOperationGUID(apiGUIDMap, apiOperationQualifiedName, pathDescription.getPost());

                                        if (apiOperationGUID != null)
                                        {
                                            apiOperationGUIDMap.put(apiOperationQualifiedName, apiOperationGUID);
                                        }
                                    }


                                    if (pathDescription.getPut() != null)
                                    {
                                        apiOperationQualifiedName = "PUT " + pathName;

                                        apiOperationGUID = getAPIOperationGUID(apiGUIDMap, apiOperationQualifiedName, pathDescription.getPut());

                                        if (apiOperationGUID != null)
                                        {
                                            apiOperationGUIDMap.put(apiOperationQualifiedName, apiOperationGUID);
                                        }
                                    }


                                    if (pathDescription.getDelete() != null)
                                    {
                                        apiOperationQualifiedName = "DELETE " + pathName;

                                        apiOperationGUID = getAPIOperationGUID(apiGUIDMap, apiOperationQualifiedName, pathDescription.getDelete());

                                        if (apiOperationGUID != null)
                                        {
                                            apiOperationGUIDMap.put(apiOperationQualifiedName, apiOperationGUID);
                                        }
                                    }
                                }
                            }

                            if (auditLog != null)
                            {
                                auditLog.logMessage(methodName,
                                                    OpenAPIIntegrationConnectorAuditCode.CATALOGUED_OPEN_API_SPEC.getMessageDefinition(connectorName,
                                                                                                                                       url,
                                                                                                                                       title,
                                                                                                                                       endpointGUID,
                                                                                                                                       Integer.toString(apiGUIDMap.size()),
                                                                                                                                       Integer.toString(apiOperationGUIDMap.size())));
                            }
                        }
                    }
                }
                catch (Exception error)
                {
                    if (auditLog != null)
                    {
                        auditLog.logMessage(methodName,
                                            OpenAPIIntegrationConnectorAuditCode.UNABLE_TO_RETRIEVE_OPEN_API_SPEC.getMessageDefinition(error.getClass().getName(),
                                                                                                                                       connectorName,
                                                                                                                                       methodName,
                                                                                                                                       url,
                                                                                                                                       error.getMessage()));

                    }

                    throw new ConnectorCheckedException(OpenAPIIntegrationConnectorErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                                       error.getClass().getName(),
                                                                                                                                       error.getMessage()),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        error);
                }
            }
        }
    }


    /**
     * Return the endpoint's GUID - it may create a catalog entry if it does not exist.
     *
     * @param url URL of the API
     * @param openAPISpecification descriptive information from the open API spec
     * @return unique identifier of the metadata element for the Endpoint element
     * @throws InvalidParameterException one of the parameters is not correct
     * @throws UserNotAuthorizedException the connector's userId is not able to work with open metadata
     * @throws PropertyServerException there is an issue with one of the open metadata repositories
     */
    private String getEndpointGUID(String               url,
                                   OpenAPISpecification openAPISpecification) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        List<EndpointElement> endpointElements = myContext.findEndpoints(".*.", 0, 0);
        String                endpointGUID = null;
        String                endpointQualifiedName = "ServerEndpoint:" + url;


        if (endpointElements != null)
        {
            for (EndpointElement endpointElement : endpointElements)
            {
                if (endpointQualifiedName.equals(endpointElement.getEndpointProperties().getQualifiedName()))
                {
                    endpointGUID = endpointElement.getElementHeader().getGUID();
                }
            }
        }

        if (endpointGUID == null)
        {
            EndpointProperties properties = new EndpointProperties();

            properties.setQualifiedName(endpointQualifiedName);

            if (openAPISpecification.getInfo() != null)
            {
                properties.setDisplayName(openAPISpecification.getInfo().getTitle());
                properties.setDescription(openAPISpecification.getInfo().getDescription());
            }

            endpointGUID = myContext.createEndpoint(properties);
        }

        /*
         * The connector could be improved here by updating the Endpoint catalog element with the latest values from the openAPI spec.
         */

        return endpointGUID;
    }


    /**
     * Create a new API element if one does not already exist in the open metadata catalog.  The GUID of the API element is
     * returned but it is the responsibility of the caller to add it to the apiGUIDMap if needed.
     *
     * @param url URL of the API
     * @param endpointGUID unique identifier for endpoint element that the API should be connected to
     * @param tag API identifiers from the openAPI spec
     * @return unique identifier of the metadata element for the API element
     * @throws InvalidParameterException one of the parameters is not correct
     * @throws UserNotAuthorizedException the connector's userId is not able to work with open metadata
     * @throws PropertyServerException there is an issue with one of the open metadata repositories
     */
    private String getAPIGUID(String     url,
                              String     endpointGUID,
                              OpenAPITag tag) throws InvalidParameterException,
                                                     UserNotAuthorizedException,
                                                     PropertyServerException
    {
        String apiGUID          = null;

        if (tag.getName() != null)
        {
            String apiQualifiedName = "API:" + tag.getName() + "(" + url + ")";

            List<APIElement> apiElements = myContext.getAPIsByName(apiQualifiedName, 0, 0);

            if (apiElements != null)
            {
                for (APIElement apiElement : apiElements)
                {
                    if (apiQualifiedName.equals(apiElement.getAPIProperties().getQualifiedName()))
                    {
                        apiGUID = apiElement.getElementHeader().getGUID();
                    }
                }
            }

            if (apiGUID == null)
            {
                APIProperties properties = new APIProperties();

                properties.setQualifiedName(apiQualifiedName);
                properties.setName(tag.getName());
                properties.setDescription(tag.getDescription());

                apiGUID = myContext.createAPI(endpointGUID, properties);
            }

            /*
             * The connector could be enhanced here to update the API element in the values differ between the API specification and the
             * catalog.
             */
        }

        return apiGUID;
    }


    /**
     * Catalog an API operation if not already catalogued.  The GUID is returned but the caller is responsible for adding the new GUID to
     * the apiGUIDMap.
     *
     * @param apiGUIDMap map of known guids
     * @param apiOperationQualifiedName unique name for the API operation in the open metadata catalog.
     * @param operation operation extracted from the open metadata specification.
     * @return unique identifier of the API operation in the open metadata catalog
     * @throws InvalidParameterException one of the parameters is not correct
     * @throws UserNotAuthorizedException the connector's userId is not able to work with open metadata
     * @throws PropertyServerException there is an issue with one of the open metadata repositories
     */
    private String getAPIOperationGUID(Map<String, String> apiGUIDMap,
                                       String              apiOperationQualifiedName,
                                       OpenAPIOperation    operation) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        String apiOperationGUID = null;
        String apiGUID          = null;

        List<String> tags = operation.getTags();

        if (tags != null)
        {
            for (String tag : tags)
            {
                /*
                 * The API operation has already been retrieved from the catalog and its GUID is known.
                 */
                apiGUID = apiGUIDMap.get(tag);
            }
        }

        if (apiGUID != null)
        {
            /*
             * The GUID is not known -  It may have been catalogued in a previous run of this connector - or not yet catalogued.  The retrieve
             * should return all elements with the same qualified name - there should be only one.  This code could be enhanced to validate that
             * when there are duplicates, they are logged and the selected one is the one created by this process.
             */
            List<APIOperationElement> apiOperationElements = myContext.getAPIOperationsByName(apiOperationQualifiedName, 0, 0);

            if (apiOperationElements != null)
            {
                for (APIOperationElement apiOperationElement : apiOperationElements)
                {
                    if (apiOperationQualifiedName.equals(apiOperationElement.getProperties().getQualifiedName()))
                    {
                        apiOperationGUID = apiOperationElement.getElementHeader().getGUID();
                    }
                }
            }

            if (apiOperationGUID == null)
            {
                /*
                 * Catalog the API Operation
                 */
                APIOperationProperties properties = new APIOperationProperties();

                properties.setQualifiedName(apiOperationQualifiedName);
                properties.setDisplayName(operation.getOperationId());
                properties.setDescription(operation.getDescription());

                apiOperationGUID = myContext.createAPIOperation(apiGUID, properties);
            }
        }

        return apiOperationGUID;
    }


    /**
     * Shutdown monitoring
     *
     * @throws ConnectorCheckedException something failed in the super class
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        final String methodName = "disconnect";



        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                OpenAPIIntegrationConnectorAuditCode.CONNECTOR_STOPPING.getMessageDefinition(connectorName));
        }

        super.disconnect();
    }
}
