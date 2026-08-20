/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.openapis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.ffdc.OpenAPIIntegrationConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.properties.OpenAPIMediaType;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.properties.OpenAPIOperation;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.properties.OpenAPIParameter;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.properties.OpenAPIRequestBody;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.properties.OpenAPIResponse;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.properties.OpenAPISchema;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.properties.OpenAPISecurityScheme;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.properties.OpenAPISpecification;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.properties.OpenAPITag;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.EndpointClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaAttributeClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaTypeClient;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.DeployedAPIProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.AttributeForSchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.NestedSchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.TypeEmbeddedAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIHeaderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIOperationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIOperationsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIParameterListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIParameterProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIRequestProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIResponseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APISchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.APIParameterType;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * OpenAPIMonitorIntegrationConnector builds an open metadata representation of the open API specifications published
 * by the servers it is monitoring.  For each API specification it catalogues a DeployedAPI asset linked to the
 * server's Endpoint, an APISchemaType describing the API, an APIOperation for each path/command combination, and
 * the request, response and header structures for each operation, built out of APIParameterList and APIParameter
 * schema elements.  See <a href="https://egeria-project.org/concepts/schema/">...</a> for more information on schema types and
 * schema attributes.
 */
public class OpenAPIMonitorIntegrationConnector extends IntegrationConnectorBase implements OpenMetadataEventListener
{
    private static final String urlMarker = "http";

    private static final String REQUEST_BODY_NAME = "Request Body";
    private static final String SECURITY_GROUP_NAME = "Security";
    private static final String HEADERS_GROUP_NAME = "Headers";

    private String templateQualifiedName = null;
    private String targetRootURL = null;

    private final Map<String, RESTClient> restClients = new HashMap<>();

    private             IntegrationContext myContext     = null;
    public static final ObjectReader       OBJECT_READER = new ObjectMapper().reader();


    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        myContext = integrationContext;

        /*
         * Retrieve information from the supplied connection.
         */
        Endpoint endpoint = connectionBean.getEndpoint();

        if (endpoint != null)
        {
            targetRootURL = endpoint.getNetworkAddress();
        }

        Map<String, Object> configurationProperties = connectionBean.getConfigurationProperties();

        if (configurationProperties != null)
        {
            templateQualifiedName = super.getStringConfigurationProperty(OpenAPIMonitorIntegrationProvider.TEMPLATE_QUALIFIED_NAME_CONFIGURATION_PROPERTY, configurationProperties);
        }

        try
        {
            if (targetRootURL != null)
            {
                RESTClient restClient = new RESTClient(connectorName, targetRootURL, secretsStoreConnectorMap, auditLog);

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
        catch (OMFCheckedExceptionBase error)
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
    public void processEvent(OpenMetadataOutTopicEvent event)
    {
        /*
         * Only interested in Endpoint events.
         */
        final String methodName = "processEvent";

        if ((event.getEventType() == OpenMetadataEventType.NEW_ELEMENT_CREATED) &&
                (propertyHelper.isTypeOf(event.getElementHeader(), OpenMetadataType.ENDPOINT.typeName)))
        {
            try
            {
                EndpointClient          endpointClient  = myContext.getEndpointClient();
                OpenMetadataRootElement endpointElement = endpointClient.getEndpointByGUID(event.getElementHeader().getGUID(), endpointClient.getGetOptions());

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
            List<OpenMetadataRootElement> endpointElements = myContext.getEndpointClient().findEndpoints(urlMarker, myContext.getEndpointClient().getSearchOptions());

            if (endpointElements != null)
            {
                for (OpenMetadataRootElement endpointElement : endpointElements)
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
    private void registerEndpoint(OpenMetadataRootElement endpointElement,
                                  String                  methodName)
    {

        if ((endpointElement.getProperties() instanceof EndpointProperties endpointProperties) &&
            (endpointProperties.getNetworkAddress() != null))
        {
            if (endpointProperties.getNetworkAddress().startsWith(urlMarker))
            {
                try
                {
                    /*
                     * If this is new endpoint then add it to the restClients being monitored.
                     */
                    if (restClients.get(endpointProperties.getNetworkAddress()) == null)
                    {
                        if (auditLog != null)
                        {
                            auditLog.logMessage(methodName,
                                                OpenAPIIntegrationConnectorAuditCode.NEW_ENDPOINT.getMessageDefinition(connectorName,
                                                                                                                       endpointProperties.getDisplayName(),
                                                                                                                       endpointProperties.getNetworkAddress()));
                        }

                        RESTClient restClient = new RESTClient(connectorName,
                                                               endpointProperties.getNetworkAddress(),
                                                               secretsStoreConnectorMap,
                                                               auditLog);

                        restClients.put(endpointProperties.getNetworkAddress(), restClient);
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
                                    OpenAPIIntegrationConnectorAuditCode.BAD_ENDPOINT.getMessageDefinition(connectorName,
                                                                                                           elementType,
                                                                                                           methodName,
                                                                                                           endpointElement.getElementHeader().getGUID()));


            }
        }
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     */
    @Override
    public void refresh()
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
                        OpenAPISpecification openAPISpecification = OBJECT_READER.readValue(openAPIJSON, OpenAPISpecification.class);

                        if (openAPISpecification != null)
                        {
                            catalogAPISpecification(url, openAPISpecification, methodName);
                        }
                    }
                }
                catch (Exception error)
                {
                    super.logRecord(methodName,
                                    OpenAPIIntegrationConnectorAuditCode.UNABLE_TO_RETRIEVE_OPEN_API_SPEC.getMessageDefinition(error.getClass().getName(),
                                                                                                                               connectorName,
                                                                                                                               methodName,
                                                                                                                               url,
                                                                                                                               error.getMessage()));
                }
            }
        }
    }


    /**
     * Catalog the whole open API specification retrieved from a single server: its DeployedAPI asset, its
     * APISchemaType, and an APIOperation (with request/response/header structure) for every path/command
     * combination it defines.
     *
     * @param url URL of the API
     * @param openAPISpecification the parsed open API specification
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is not correct
     * @throws UserNotAuthorizedException the connector's userId is not able to work with open metadata
     * @throws PropertyServerException there is an issue with one of the open metadata repositories
     */
    private void catalogAPISpecification(String                url,
                                         OpenAPISpecification  openAPISpecification,
                                         String                methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        String title = "<Untitled>";

        if (openAPISpecification.getInfo() != null)
        {
            title = openAPISpecification.getInfo().getTitle();
        }

        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                OpenAPIIntegrationConnectorAuditCode.RETRIEVED_OPEN_API_SPEC.getMessageDefinition(connectorName,
                                                                                                                  url,
                                                                                                                  title));
        }

        String endpointGUID = this.getEndpointGUID(url, openAPISpecification);

        if (endpointGUID == null)
        {
            return;
        }

        String apiQualifiedName = "DeployedAPI:" + url;
        String apiGUID          = this.getAPIGUID(url, apiQualifiedName, endpointGUID, openAPISpecification);

        if (apiGUID == null)
        {
            return;
        }

        String apiSchemaTypeGUID = this.getAPISchemaTypeGUID(apiGUID, apiQualifiedName);

        int operationCount = 0;

        if (openAPISpecification.getPaths() != null)
        {
            Map<String, Map<String, OpenAPIOperation>> paths = openAPISpecification.getPaths();

            for (String pathName : paths.keySet())
            {
                Map<String, OpenAPIOperation> pathDescription = paths.get(pathName);

                if (pathDescription != null)
                {
                    for (String command : pathDescription.keySet())
                    {
                        OpenAPIOperation operation = pathDescription.get(command);

                        if (operation != null)
                        {
                            this.catalogOperation(apiSchemaTypeGUID, apiGUID, apiQualifiedName, pathName, command, operation, openAPISpecification);
                            operationCount++;
                        }
                    }
                }
            }
        }

        super.logRecord(methodName,
                        OpenAPIIntegrationConnectorAuditCode.CATALOGUED_OPEN_API_SPEC.getMessageDefinition(connectorName,
                                                                                                           url,
                                                                                                           title,
                                                                                                           apiGUID,
                                                                                                           Integer.toString(1),
                                                                                                           Integer.toString(operationCount)));
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
        List<OpenMetadataRootElement> endpointElements = myContext.getEndpointClient().findEndpoints(null, myContext.getEndpointClient().getSearchOptions());
        String                        endpointGUID = null;
        String                        endpointQualifiedName = "ServerEndpoint:" + url;

        if (endpointElements != null)
        {
            for (OpenMetadataRootElement endpointElement : endpointElements)
            {
                if ((endpointElement != null) && (endpointElement.getProperties() instanceof EndpointProperties endpointProperties))
                {
                    if (endpointQualifiedName.equals(endpointProperties.getQualifiedName()))
                    {
                        endpointGUID = endpointElement.getElementHeader().getGUID();

                        if (endpointProperties.getNetworkAddress() == null)
                        {
                            /*
                             * This corrects a bug where an earlier version of the connector did not set the network address.
                             */
                            endpointProperties.setNetworkAddress(url);

                            myContext.getEndpointClient().updateEndpoint(endpointElement.getElementHeader().getGUID(),
                                                                         myContext.getEndpointClient().getUpdateOptions(true),
                                                                         endpointProperties);
                        }
                    }
                }
            }
        }

        if (endpointGUID == null)
        {
            EndpointProperties properties = new EndpointProperties();

            properties.setQualifiedName(endpointQualifiedName);
            properties.setNetworkAddress(url);

            if (openAPISpecification.getInfo() != null)
            {
                properties.setDisplayName(openAPISpecification.getInfo().getTitle());
                properties.setDescription(openAPISpecification.getInfo().getDescription());
            }

            endpointGUID = myContext.getEndpointClient().createEndpoint(new NewElementOptions(),
                                                                        null,
                                                                        properties,
                                                                        null);
        }

        /*
         * The connector could be improved here by updating the Endpoint catalog element with the latest values from the openAPI spec.
         */

        return endpointGUID;
    }


    /**
     * Return the DeployedAPI's GUID for this API specification - it may create a catalog entry if it does not exist.
     * There is one DeployedAPI asset per API specification (ie per server monitored), linked to its Endpoint via the
     * APIEndpoint relationship, and self-anchored (the DeployedAPI asset, and everything catalogued beneath it, is
     * anchored to itself).
     *
     * @param url URL of the API
     * @param apiQualifiedName unique name to use for the DeployedAPI asset
     * @param endpointGUID unique identifier for endpoint element that the API should be attached to
     * @param openAPISpecification descriptive information from the open API spec
     * @return unique identifier of the metadata element for the DeployedAPI element
     * @throws InvalidParameterException one of the parameters is not correct
     * @throws UserNotAuthorizedException the connector's userId is not able to work with open metadata
     * @throws PropertyServerException there is an issue with one of the open metadata repositories
     */
    private String getAPIGUID(String               url,
                              String               apiQualifiedName,
                              String               endpointGUID,
                              OpenAPISpecification openAPISpecification) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        AssetClient apiClient = myContext.getAssetClient(OpenMetadataType.DEPLOYED_API.typeName);

        String apiGUID = this.findExistingAsset(apiClient, apiQualifiedName);

        if (apiGUID == null)
        {
            DeployedAPIProperties properties = new DeployedAPIProperties();

            properties.setQualifiedName(apiQualifiedName);

            if (openAPISpecification.getInfo() != null)
            {
                properties.setDisplayName(openAPISpecification.getInfo().getTitle());
                properties.setDescription(openAPISpecification.getInfo().getDescription());
                properties.setVersionIdentifier(openAPISpecification.getInfo().getVersion());
            }

            if (openAPISpecification.getTags() != null)
            {
                Map<String, String> additionalProperties = new HashMap<>();
                StringBuilder        tagNames = new StringBuilder();

                for (OpenAPITag tag : openAPISpecification.getTags())
                {
                    if ((tag != null) && (tag.getName() != null))
                    {
                        if (! tagNames.isEmpty())
                        {
                            tagNames.append(", ");
                        }

                        tagNames.append(tag.getName());
                    }
                }

                if (! tagNames.isEmpty())
                {
                    additionalProperties.put("tags", tagNames.toString());
                    properties.setAdditionalProperties(additionalProperties);
                }
            }

            NewElementOptions newElementOptions = new NewElementOptions(apiClient.getMetadataSourceOptions());

            newElementOptions.setIsOwnAnchor(true);
            newElementOptions.setParentGUID(endpointGUID);
            newElementOptions.setParentAtEnd1(false);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.API_ENDPOINT_RELATIONSHIP.typeName);

            apiGUID = apiClient.createAsset(newElementOptions, null, properties, null);

            if (auditLog != null)
            {
                auditLog.logMessage("getAPIGUID",
                                    OpenAPIIntegrationConnectorAuditCode.NEW_DEPLOYED_API.getMessageDefinition(connectorName,
                                                                                                                apiQualifiedName,
                                                                                                                apiGUID,
                                                                                                                url));
            }
        }

        /*
         * The connector could be enhanced here to update the API element if the values differ between the API specification and the
         * catalog.
         */

        return apiGUID;
    }


    /**
     * Return the APISchemaType's GUID for this API - it may create a catalog entry if it does not exist.  The
     * APISchemaType is anchored to the DeployedAPI asset and linked to it via the Schema relationship.
     *
     * @param apiGUID unique identifier for the DeployedAPI asset
     * @param apiQualifiedName unique name of the DeployedAPI asset
     * @return unique identifier of the metadata element for the APISchemaType element
     * @throws InvalidParameterException one of the parameters is not correct
     * @throws UserNotAuthorizedException the connector's userId is not able to work with open metadata
     * @throws PropertyServerException there is an issue with one of the open metadata repositories
     */
    private String getAPISchemaTypeGUID(String apiGUID,
                                        String apiQualifiedName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        SchemaTypeClient schemaTypeClient       = myContext.getSchemaTypeClient(OpenMetadataType.API_SCHEMA_TYPE.typeName);
        String           apiSchemaTypeQualifiedName = apiQualifiedName + "::Schema";
        String           apiSchemaTypeGUID      = this.findExistingSchemaType(schemaTypeClient, apiSchemaTypeQualifiedName);

        if (apiSchemaTypeGUID == null)
        {
            APISchemaTypeProperties properties = new APISchemaTypeProperties();

            properties.setQualifiedName(apiSchemaTypeQualifiedName);
            properties.setDisplayName("Schema");

            NewElementOptions newElementOptions = new NewElementOptions(schemaTypeClient.getMetadataSourceOptions());

            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorGUID(apiGUID);

            apiSchemaTypeGUID = schemaTypeClient.createSchemaType(newElementOptions, null, properties, null);

            schemaTypeClient.linkSchema(apiGUID, apiSchemaTypeGUID, schemaTypeClient.getMakeAnchorOptions(false), new SchemaProperties());
        }

        return apiSchemaTypeGUID;
    }


    /**
     * Catalog a single API operation (one path/command combination) and its request, response and header structure.
     *
     * @param apiSchemaTypeGUID unique identifier of the API's schema type
     * @param apiGUID unique identifier of the DeployedAPI asset that everything below it is anchored to
     * @param apiQualifiedName unique name of the DeployedAPI asset
     * @param pathName full path for this operation
     * @param command HTTP command (eg get, post) for this operation
     * @param operation the parsed details of this operation
     * @param openAPISpecification the parsed open API specification (for resolving $ref schemas)
     * @throws InvalidParameterException one of the parameters is not correct
     * @throws UserNotAuthorizedException the connector's userId is not able to work with open metadata
     * @throws PropertyServerException there is an issue with one of the open metadata repositories
     */
    private void catalogOperation(String               apiSchemaTypeGUID,
                                  String               apiGUID,
                                  String               apiQualifiedName,
                                  String               pathName,
                                  String               command,
                                  OpenAPIOperation     operation,
                                  OpenAPISpecification openAPISpecification) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        String apiOperationQualifiedName = apiQualifiedName + "::" + command.toUpperCase() + " " + pathName;

        String apiOperationGUID = this.getAPIOperationGUID(apiSchemaTypeGUID, apiGUID, apiOperationQualifiedName, pathName, command, operation);

        this.catalogRequest(apiOperationGUID, apiGUID, apiOperationQualifiedName, operation, openAPISpecification);
        this.catalogResponses(apiOperationGUID, apiGUID, apiOperationQualifiedName, operation, openAPISpecification);
        this.catalogHeaders(apiOperationGUID, apiGUID, apiOperationQualifiedName, operation, openAPISpecification);
    }


    /**
     * Catalog an API operation if not already catalogued.
     *
     * @param apiSchemaTypeGUID unique identifier of the parent APISchemaType
     * @param apiGUID unique identifier of the DeployedAPI asset that everything below it is anchored to
     * @param apiOperationQualifiedName unique name for the API operation in the open metadata catalog
     * @param pathName full path for this operation
     * @param command HTTP command (eg get, post) for this operation
     * @param operation operation extracted from the open metadata specification
     * @return unique identifier of the API operation in the open metadata catalog
     * @throws InvalidParameterException one of the parameters is not correct
     * @throws UserNotAuthorizedException the connector's userId is not able to work with open metadata
     * @throws PropertyServerException there is an issue with one of the open metadata repositories
     */
    private String getAPIOperationGUID(String           apiSchemaTypeGUID,
                                       String           apiGUID,
                                       String           apiOperationQualifiedName,
                                       String           pathName,
                                       String           command,
                                       OpenAPIOperation operation) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        SchemaTypeClient schemaTypeClient  = myContext.getSchemaTypeClient(OpenMetadataType.API_OPERATION.typeName);
        String           apiOperationGUID = this.findExistingSchemaType(schemaTypeClient, apiOperationQualifiedName);

        if (apiOperationGUID == null)
        {
            APIOperationProperties properties = new APIOperationProperties();

            properties.setQualifiedName(apiOperationQualifiedName);
            properties.setPath(pathName);
            properties.setCommand(command.toUpperCase());

            String displayName = operation.getOperationId();

            if (displayName == null)
            {
                displayName = command.toUpperCase() + " " + pathName;
            }

            properties.setDisplayName(displayName);

            String description = operation.getDescription();

            if (description == null)
            {
                description = operation.getSummary();
            }

            properties.setDescription(description);

            Map<String, String> additionalProperties = new HashMap<>();

            if (operation.getSummary() != null)
            {
                additionalProperties.put("summary", operation.getSummary());
            }

            if (operation.isDeprecated())
            {
                additionalProperties.put("deprecated", "true");
            }

            if (operation.getTags() != null)
            {
                additionalProperties.put("tags", String.join(", ", operation.getTags()));
            }

            if (! additionalProperties.isEmpty())
            {
                properties.setAdditionalProperties(additionalProperties);
            }

            NewElementOptions newElementOptions = new NewElementOptions(schemaTypeClient.getMetadataSourceOptions());

            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorGUID(apiGUID);
            newElementOptions.setParentGUID(apiSchemaTypeGUID);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.API_OPERATIONS_RELATIONSHIP.typeName);

            apiOperationGUID = schemaTypeClient.createSchemaType(newElementOptions, null, properties, new APIOperationsProperties());

            if (auditLog != null)
            {
                auditLog.logMessage("getAPIOperationGUID",
                                    OpenAPIIntegrationConnectorAuditCode.NEW_API_OPERATION.getMessageDefinition(connectorName,
                                                                                                                 apiOperationQualifiedName,
                                                                                                                 apiOperationGUID,
                                                                                                                 pathName,
                                                                                                                 command.toUpperCase(),
                                                                                                                 apiGUID));
            }
        }

        return apiOperationGUID;
    }


    /**
     * Catalog the request structure for an operation: the URL parameters and/or the request body.  An
     * APIParameterList is only created if there is something to catalog.
     *
     * @param apiOperationGUID unique identifier of the API operation
     * @param apiGUID unique identifier of the DeployedAPI asset that everything below it is anchored to
     * @param apiOperationQualifiedName unique name of the API operation
     * @param operation the parsed details of this operation
     * @param openAPISpecification the parsed open API specification (for resolving $ref schemas)
     * @throws InvalidParameterException one of the parameters is not correct
     * @throws UserNotAuthorizedException the connector's userId is not able to work with open metadata
     * @throws PropertyServerException there is an issue with one of the open metadata repositories
     */
    private void catalogRequest(String               apiOperationGUID,
                                String               apiGUID,
                                String               apiOperationQualifiedName,
                                OpenAPIOperation     operation,
                                OpenAPISpecification openAPISpecification) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        List<OpenAPIParameter> urlParameters = new ArrayList<>();

        if (operation.getParameters() != null)
        {
            for (OpenAPIParameter parameter : operation.getParameters())
            {
                if ((parameter != null) && (! "header".equals(parameter.getIn())))
                {
                    urlParameters.add(parameter);
                }
            }
        }

        OpenAPIRequestBody requestBody = operation.getRequestBody();

        if (urlParameters.isEmpty() && (requestBody == null))
        {
            return;
        }

        String requestListQualifiedName = apiOperationQualifiedName + "::Request";
        String requestListGUID = this.getOrCreateAPIParameterList(apiOperationGUID,
                                                                   OpenMetadataType.API_REQUEST_RELATIONSHIP.typeName,
                                                                   apiGUID,
                                                                   requestListQualifiedName,
                                                                   "Request");

        int position = 0;

        for (OpenAPIParameter parameter : urlParameters)
        {
            this.createSchemaAttribute(requestListGUID,
                                       true,
                                       apiGUID,
                                       requestListQualifiedName + "::" + parameter.getName(),
                                       parameter.getName(),
                                       parameter.getDescription(),
                                       parameter.getSchema(),
                                       APIParameterType.REQUEST_PARAMETER,
                                       ! parameter.isRequired(),
                                       position,
                                       openAPISpecification,
                                       new HashSet<>());
            position++;
        }

        if (requestBody != null)
        {
            OpenAPISchema bodySchema = this.getPreferredContentSchema(requestBody.getContent());

            this.createSchemaAttribute(requestListGUID,
                                       true,
                                       apiGUID,
                                       requestListQualifiedName + "::" + REQUEST_BODY_NAME,
                                       REQUEST_BODY_NAME,
                                       requestBody.getDescription(),
                                       bodySchema,
                                       APIParameterType.REQUEST_BODY,
                                       ! requestBody.isRequired(),
                                       position,
                                       openAPISpecification,
                                       new HashSet<>());
        }
    }


    /**
     * Catalog the response structure for an operation: one APIParameter per status code, each with a nested
     * structure matching its response body.  An APIParameterList is only created if there is a response to catalog.
     *
     * @param apiOperationGUID unique identifier of the API operation
     * @param apiGUID unique identifier of the DeployedAPI asset that everything below it is anchored to
     * @param apiOperationQualifiedName unique name of the API operation
     * @param operation the parsed details of this operation
     * @param openAPISpecification the parsed open API specification (for resolving $ref schemas)
     * @throws InvalidParameterException one of the parameters is not correct
     * @throws UserNotAuthorizedException the connector's userId is not able to work with open metadata
     * @throws PropertyServerException there is an issue with one of the open metadata repositories
     */
    private void catalogResponses(String               apiOperationGUID,
                                  String               apiGUID,
                                  String               apiOperationQualifiedName,
                                  OpenAPIOperation     operation,
                                  OpenAPISpecification openAPISpecification) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        if ((operation.getResponses() == null) || (operation.getResponses().isEmpty()))
        {
            return;
        }

        String responseListQualifiedName = apiOperationQualifiedName + "::Response";
        String responseListGUID = this.getOrCreateAPIParameterList(apiOperationGUID,
                                                                    OpenMetadataType.API_RESPONSE_RELATIONSHIP.typeName,
                                                                    apiGUID,
                                                                    responseListQualifiedName,
                                                                    "Response");

        int position = 0;

        for (String statusCode : operation.getResponses().keySet())
        {
            OpenAPIResponse response = operation.getResponses().get(statusCode);

            if (response != null)
            {
                OpenAPISchema bodySchema = this.getPreferredContentSchema(response.getContent());

                this.createSchemaAttribute(responseListGUID,
                                           true,
                                           apiGUID,
                                           responseListQualifiedName + "::" + statusCode,
                                           statusCode,
                                           response.getDescription(),
                                           bodySchema,
                                           APIParameterType.RESPONSE_STATUS,
                                           true,
                                           position,
                                           openAPISpecification,
                                           new HashSet<>());
                position++;
            }
        }
    }


    /**
     * Catalog the header structure for an operation: header parameters and/or security requirements.  An
     * APIParameterList is only created if there is header information to catalog.
     *
     * @param apiOperationGUID unique identifier of the API operation
     * @param apiGUID unique identifier of the DeployedAPI asset that everything below it is anchored to
     * @param apiOperationQualifiedName unique name of the API operation
     * @param operation the parsed details of this operation
     * @param openAPISpecification the parsed open API specification (for resolving $ref schemas and global security)
     * @throws InvalidParameterException one of the parameters is not correct
     * @throws UserNotAuthorizedException the connector's userId is not able to work with open metadata
     * @throws PropertyServerException there is an issue with one of the open metadata repositories
     */
    private void catalogHeaders(String               apiOperationGUID,
                                String               apiGUID,
                                String               apiOperationQualifiedName,
                                OpenAPIOperation     operation,
                                OpenAPISpecification openAPISpecification) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        List<OpenAPIParameter> headerParameters = new ArrayList<>();

        if (operation.getParameters() != null)
        {
            for (OpenAPIParameter parameter : operation.getParameters())
            {
                if ((parameter != null) && ("header".equals(parameter.getIn())))
                {
                    headerParameters.add(parameter);
                }
            }
        }

        List<Map<String, List<String>>> security = operation.getSecurity();

        if (security == null)
        {
            security = openAPISpecification.getSecurity();
        }

        Map<String, OpenAPISecurityScheme> securitySchemes = null;

        if ((openAPISpecification.getComponents() != null) && (openAPISpecification.getComponents().getSecuritySchemes() != null))
        {
            securitySchemes = openAPISpecification.getComponents().getSecuritySchemes();
        }

        boolean hasSecurity = (security != null) && (! security.isEmpty()) && (securitySchemes != null);

        if (headerParameters.isEmpty() && (! hasSecurity))
        {
            return;
        }

        String headerListQualifiedName = apiOperationQualifiedName + "::Header";
        String headerListGUID = this.getOrCreateAPIParameterList(apiOperationGUID,
                                                                  OpenMetadataType.API_HEADER_RELATIONSHIP.typeName,
                                                                  apiGUID,
                                                                  headerListQualifiedName,
                                                                  "Header");

        int position = 0;

        if (! headerParameters.isEmpty())
        {
            String headersGroupQualifiedName = headerListQualifiedName + "::" + HEADERS_GROUP_NAME;
            String headersGroupGUID = this.createGroupAPIParameter(headerListGUID, apiGUID, headersGroupQualifiedName, HEADERS_GROUP_NAME, position);

            int childPosition = 0;

            for (OpenAPIParameter parameter : headerParameters)
            {
                this.createSchemaAttribute(headersGroupGUID,
                                           false,
                                           apiGUID,
                                           headersGroupQualifiedName + "::" + parameter.getName(),
                                           parameter.getName(),
                                           parameter.getDescription(),
                                           parameter.getSchema(),
                                           APIParameterType.SECURITY_HEADER,
                                           ! parameter.isRequired(),
                                           childPosition,
                                           openAPISpecification,
                                           new HashSet<>());
                childPosition++;
            }

            position++;
        }

        if (hasSecurity)
        {
            String securityGroupQualifiedName = headerListQualifiedName + "::" + SECURITY_GROUP_NAME;
            String securityGroupGUID = this.createGroupAPIParameter(headerListGUID, apiGUID, securityGroupQualifiedName, SECURITY_GROUP_NAME, position);

            int childPosition = 0;

            Set<String> schemeNamesUsed = new HashSet<>();

            for (Map<String, List<String>> securityRequirement : security)
            {
                if (securityRequirement != null)
                {
                    for (String schemeName : securityRequirement.keySet())
                    {
                        if ((schemeName != null) && (! schemeNamesUsed.contains(schemeName)))
                        {
                            schemeNamesUsed.add(schemeName);

                            OpenAPISecurityScheme scheme = securitySchemes.get(schemeName);
                            OpenAPISchema         schemeSchema = this.buildSecuritySchemeSchema(scheme);

                            this.createSchemaAttribute(securityGroupGUID,
                                                       false,
                                                       apiGUID,
                                                       securityGroupQualifiedName + "::" + schemeName,
                                                       schemeName,
                                                       (scheme != null) ? scheme.getDescription() : null,
                                                       schemeSchema,
                                                       APIParameterType.SECURITY_HEADER,
                                                       false,
                                                       childPosition,
                                                       openAPISpecification,
                                                       new HashSet<>());
                            childPosition++;
                        }
                    }
                }
            }
        }
    }


    /**
     * Build a synthetic schema describing a named security scheme, so it can be catalogued through the same
     * schema-attribute logic used for real schema-backed parameters.
     *
     * @param scheme security scheme details, or null if not known
     * @return synthetic schema
     */
    private OpenAPISchema buildSecuritySchemeSchema(OpenAPISecurityScheme scheme)
    {
        OpenAPISchema schema = new OpenAPISchema();

        schema.setType("string");

        if (scheme != null)
        {
            StringBuilder description = new StringBuilder();

            if (scheme.getType() != null)
            {
                description.append(scheme.getType());
            }

            if (scheme.getScheme() != null)
            {
                if (! description.isEmpty())
                {
                    description.append(" - ");
                }

                description.append(scheme.getScheme());
            }

            if (scheme.getBearerFormat() != null)
            {
                description.append(" (").append(scheme.getBearerFormat()).append(")");
            }

            if (! description.isEmpty())
            {
                schema.setDescription(description.toString());
            }
        }

        return schema;
    }


    /**
     * Retrieve or create the APIParameterList used for an operation's request, response or header structure.
     *
     * @param apiOperationGUID unique identifier of the parent API operation
     * @param parentRelationshipTypeName relationship type linking the operation to this parameter list (APIRequest, APIResponse or APIHeader)
     * @param apiGUID unique identifier of the DeployedAPI asset that everything below it is anchored to
     * @param qualifiedName unique name for the parameter list
     * @param displayName display name for the parameter list
     * @return unique identifier of the APIParameterList
     * @throws InvalidParameterException one of the parameters is not correct
     * @throws UserNotAuthorizedException the connector's userId is not able to work with open metadata
     * @throws PropertyServerException there is an issue with one of the open metadata repositories
     */
    private String getOrCreateAPIParameterList(String apiOperationGUID,
                                               String parentRelationshipTypeName,
                                               String apiGUID,
                                               String qualifiedName,
                                               String displayName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        SchemaTypeClient schemaTypeClient = myContext.getSchemaTypeClient(OpenMetadataType.API_PARAMETER_LIST.typeName);
        String           listGUID = this.findExistingSchemaType(schemaTypeClient, qualifiedName);

        if (listGUID == null)
        {
            APIParameterListProperties properties = new APIParameterListProperties();

            properties.setQualifiedName(qualifiedName);
            properties.setDisplayName(displayName);

            NewElementOptions newElementOptions = new NewElementOptions(schemaTypeClient.getMetadataSourceOptions());

            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorGUID(apiGUID);
            newElementOptions.setParentGUID(apiOperationGUID);
            newElementOptions.setParentRelationshipTypeName(parentRelationshipTypeName);

            RelationshipPropertiesForList relationshipProperties = new RelationshipPropertiesForList(parentRelationshipTypeName);

            listGUID = schemaTypeClient.createSchemaType(newElementOptions, null, properties, relationshipProperties.get());
        }

        return listGUID;
    }


    /**
     * Small helper to select the correct empty relationship properties bean for the parent relationship used when
     * creating an APIParameterList, since each of APIRequest, APIResponse and APIHeader has its own properties class.
     */
    private static class RelationshipPropertiesForList
    {
        private final String relationshipTypeName;

        RelationshipPropertiesForList(String relationshipTypeName)
        {
            this.relationshipTypeName = relationshipTypeName;
        }

        org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties get()
        {
            if (OpenMetadataType.API_RESPONSE_RELATIONSHIP.typeName.equals(relationshipTypeName))
            {
                return new APIResponseProperties();
            }
            else if (OpenMetadataType.API_HEADER_RELATIONSHIP.typeName.equals(relationshipTypeName))
            {
                return new APIHeaderProperties();
            }

            return new APIRequestProperties();
        }
    }


    /**
     * Create a struct APIParameter that groups a set of related nested parameters together (eg "Security" or
     * "Headers"), where there is no underlying open API schema to describe the group itself.
     *
     * @param parentListGUID unique identifier of the parent APIParameterList
     * @param apiGUID unique identifier of the DeployedAPI asset that everything below it is anchored to
     * @param qualifiedName unique name for the group parameter
     * @param displayName display name for the group parameter
     * @param position position of this parameter amongst its siblings
     * @return unique identifier of the group APIParameter
     * @throws InvalidParameterException one of the parameters is not correct
     * @throws UserNotAuthorizedException the connector's userId is not able to work with open metadata
     * @throws PropertyServerException there is an issue with one of the open metadata repositories
     */
    private String createGroupAPIParameter(String parentListGUID,
                                           String apiGUID,
                                           String qualifiedName,
                                           String displayName,
                                           int    position) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        SchemaAttributeClient schemaAttributeClient = myContext.getSchemaAttributeClient(OpenMetadataType.API_PARAMETER.typeName);
        String                groupGUID = this.findExistingSchemaAttribute(schemaAttributeClient, qualifiedName);

        if (groupGUID != null)
        {
            return groupGUID;
        }

        APIParameterProperties properties = new APIParameterProperties();

        properties.setQualifiedName(qualifiedName);
        properties.setDisplayName(displayName);
        properties.setParameterType(APIParameterType.SECURITY_HEADER.getDisplayName());

        TypeEmbeddedAttributeProperties classification = new TypeEmbeddedAttributeProperties();

        classification.setSchemaTypeName(OpenMetadataType.STRUCT_SCHEMA_TYPE.typeName);

        Map<String, ClassificationProperties> initialClassifications = new HashMap<>();

        initialClassifications.put(OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName, classification);

        NewElementOptions newElementOptions = new NewElementOptions(schemaAttributeClient.getMetadataSourceOptions());

        newElementOptions.setIsOwnAnchor(false);
        newElementOptions.setAnchorGUID(apiGUID);

        groupGUID = schemaAttributeClient.createSchemaAttribute(newElementOptions, initialClassifications, properties, null);

        AttributeForSchemaProperties relationshipProperties = new AttributeForSchemaProperties();

        relationshipProperties.setPosition(position);

        schemaAttributeClient.linkAttributeForSchema(parentListGUID, groupGUID, schemaAttributeClient.getMakeAnchorOptions(false), relationshipProperties);

        return groupGUID;
    }


    /**
     * Create (if not already catalogued) an APIParameter schema attribute for a single value - a URL parameter, a
     * request/response body, a status code, a header or security scheme, or a field nested inside one of those
     * structures - and recurse to catalogue its own nested structure, if it has one.
     *
     * @param parentGUID unique identifier of the parent element (an APIParameterList schema type, or another APIParameter)
     * @param parentIsSchemaType true if the parent is an APIParameterList (link via AttributeForSchema), false if it is
     *                           another APIParameter (link via NestedSchemaAttribute)
     * @param apiGUID unique identifier of the DeployedAPI asset that everything below it is anchored to
     * @param qualifiedName unique name for this parameter
     * @param displayName display name for this parameter
     * @param description description for this parameter
     * @param schema the open API schema describing this parameter's type and structure - may be null
     * @param parameterType which part of the API schema this parameter is a part of
     * @param isNullable whether this parameter is optional
     * @param position position of this parameter amongst its siblings
     * @param openAPISpecification the parsed open API specification (for resolving $ref schemas)
     * @param visitedRefs $ref values already being expanded in this branch of the recursion, to avoid infinite loops on circular schemas
     * @return unique identifier of the APIParameter
     * @throws InvalidParameterException one of the parameters is not correct
     * @throws UserNotAuthorizedException the connector's userId is not able to work with open metadata
     * @throws PropertyServerException there is an issue with one of the open metadata repositories
     */
    private String createSchemaAttribute(String               parentGUID,
                                         boolean              parentIsSchemaType,
                                         String               apiGUID,
                                         String               qualifiedName,
                                         String               displayName,
                                         String               description,
                                         OpenAPISchema        schema,
                                         APIParameterType     parameterType,
                                         boolean              isNullable,
                                         int                  position,
                                         OpenAPISpecification openAPISpecification,
                                         Set<String>          visitedRefs) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        SchemaAttributeClient schemaAttributeClient = myContext.getSchemaAttributeClient(OpenMetadataType.API_PARAMETER.typeName);
        String                existingGUID = this.findExistingSchemaAttribute(schemaAttributeClient, qualifiedName);

        if (existingGUID != null)
        {
            return existingGUID;
        }

        String        ref = (schema != null) ? schema.get$ref() : null;
        OpenAPISchema resolvedSchema = this.resolveSchema(schema, openAPISpecification);

        APIParameterProperties properties = new APIParameterProperties();

        properties.setQualifiedName(qualifiedName);
        properties.setDisplayName(displayName);
        properties.setParameterType(parameterType.getDisplayName());
        properties.setIsNullable(isNullable);

        if (description != null)
        {
            properties.setDescription(description);
        }
        else if (resolvedSchema != null)
        {
            properties.setDescription(resolvedSchema.getDescription());
        }

        Map<String, ClassificationProperties> initialClassifications = new HashMap<>();

        initialClassifications.put(OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName, this.buildTypeEmbeddedAttribute(resolvedSchema));

        NewElementOptions newElementOptions = new NewElementOptions(schemaAttributeClient.getMetadataSourceOptions());

        newElementOptions.setIsOwnAnchor(false);
        newElementOptions.setAnchorGUID(apiGUID);

        String newGUID = schemaAttributeClient.createSchemaAttribute(newElementOptions, initialClassifications, properties, null);

        if (parentIsSchemaType)
        {
            AttributeForSchemaProperties relationshipProperties = new AttributeForSchemaProperties();

            relationshipProperties.setPosition(position);

            if ((resolvedSchema != null) && ("array".equals(resolvedSchema.getType())))
            {
                relationshipProperties.setMaxCardinality(-1);
            }

            schemaAttributeClient.linkAttributeForSchema(parentGUID, newGUID, schemaAttributeClient.getMakeAnchorOptions(false), relationshipProperties);
        }
        else
        {
            NestedSchemaAttributeProperties relationshipProperties = new NestedSchemaAttributeProperties();

            relationshipProperties.setPosition(position);

            if ((resolvedSchema != null) && ("array".equals(resolvedSchema.getType())))
            {
                relationshipProperties.setMaxCardinality(-1);
            }

            schemaAttributeClient.linkNestedSchemaAttribute(parentGUID, newGUID, schemaAttributeClient.getMakeAnchorOptions(false), relationshipProperties);
        }

        /*
         * Recurse into the structure of the schema (its own properties, or the properties of its array element
         * type) to catalogue any nested fields.  A $ref is tracked while it is being expanded so that a circular
         * schema does not send this recursion into an infinite loop.
         */
        boolean addedRef = false;

        if ((ref != null) && (! visitedRefs.contains(ref)))
        {
            visitedRefs.add(ref);
            addedRef = true;
        }
        else if ((ref != null) && (visitedRefs.contains(ref)))
        {
            resolvedSchema = null;
        }

        if (resolvedSchema != null)
        {
            OpenAPISchema structureSchema = resolvedSchema;

            if ("array".equals(resolvedSchema.getType()) && (resolvedSchema.getItems() != null))
            {
                structureSchema = this.resolveSchema(resolvedSchema.getItems(), openAPISpecification);
            }

            if ((structureSchema != null) && (structureSchema.getProperties() != null))
            {
                int childPosition = 0;

                for (String fieldName : structureSchema.getProperties().keySet())
                {
                    OpenAPISchema fieldSchema = structureSchema.getProperties().get(fieldName);
                    boolean       fieldRequired = (structureSchema.getRequired() != null) && (structureSchema.getRequired().contains(fieldName));

                    this.createSchemaAttribute(newGUID,
                                               false,
                                               apiGUID,
                                               qualifiedName + "::" + fieldName,
                                               fieldName,
                                               null,
                                               fieldSchema,
                                               APIParameterType.NESTED_ATTRIBUTE,
                                               ! fieldRequired,
                                               childPosition,
                                               openAPISpecification,
                                               visitedRefs);
                    childPosition++;
                }
            }
        }

        if (addedRef)
        {
            visitedRefs.remove(ref);
        }

        return newGUID;
    }


    /**
     * Build the TypeEmbeddedAttribute classification that captures the type of a schema attribute.  Struct types
     * (objects) and arrays of objects only carry the schemaTypeName; primitive types also carry the data type and,
     * where present, the format as the encoding standard.
     *
     * @param schema the resolved open API schema describing this attribute's type - may be null if not known
     * @return classification properties
     */
    private TypeEmbeddedAttributeProperties buildTypeEmbeddedAttribute(OpenAPISchema schema)
    {
        TypeEmbeddedAttributeProperties classification = new TypeEmbeddedAttributeProperties();

        OpenAPISchema effectiveSchema = schema;

        if ((effectiveSchema != null) && ("array".equals(effectiveSchema.getType())))
        {
            effectiveSchema = effectiveSchema.getItems();
        }

        if ((effectiveSchema == null) || ("object".equals(effectiveSchema.getType())) || (effectiveSchema.getProperties() != null))
        {
            classification.setSchemaTypeName(OpenMetadataType.STRUCT_SCHEMA_TYPE.typeName);
        }
        else
        {
            classification.setSchemaTypeName(OpenMetadataType.PRIMITIVE_SCHEMA_TYPE.typeName);
            classification.setDataType(effectiveSchema.getType());
            classification.setEncodingStandard(effectiveSchema.getFormat());
        }

        return classification;
    }


    /**
     * Resolve a $ref found in a schema to the schema it points to in the specification's components.  If the
     * schema has no $ref, or the reference cannot be resolved, the original schema is returned unchanged.
     *
     * @param schema schema that may contain a $ref
     * @param openAPISpecification the parsed open API specification
     * @return resolved schema
     */
    private OpenAPISchema resolveSchema(OpenAPISchema        schema,
                                        OpenAPISpecification openAPISpecification)
    {
        if (schema == null)
        {
            return null;
        }

        if ((schema.get$ref() != null) &&
                (openAPISpecification.getComponents() != null) &&
                (openAPISpecification.getComponents().getSchemas() != null))
        {
            String ref = schema.get$ref();
            int    lastSlash = ref.lastIndexOf('/');
            String refName = (lastSlash >= 0) ? ref.substring(lastSlash + 1) : ref;

            OpenAPISchema referencedSchema = openAPISpecification.getComponents().getSchemas().get(refName);

            if (referencedSchema != null)
            {
                return referencedSchema;
            }
        }

        return schema;
    }


    /**
     * Select the schema to use for a request/response body from its content map, preferring application/json if
     * present, otherwise using the first content type found.
     *
     * @param content map of content-type to media type description
     * @return schema, or null if there is no content
     */
    private OpenAPISchema getPreferredContentSchema(Map<String, OpenAPIMediaType> content)
    {
        if (content == null)
        {
            return null;
        }

        OpenAPIMediaType preferredMediaType = content.get("application/json");

        if (preferredMediaType == null)
        {
            for (OpenAPIMediaType mediaType : content.values())
            {
                if (mediaType != null)
                {
                    preferredMediaType = mediaType;
                    break;
                }
            }
        }

        if (preferredMediaType != null)
        {
            return preferredMediaType.getSchema();
        }

        return null;
    }


    /**
     * Find an existing asset with the requested qualified name.
     *
     * @param assetClient client to search with
     * @param qualifiedName unique name to search for
     * @return GUID of the matching asset, or null if not found
     * @throws InvalidParameterException one of the parameters is not correct
     * @throws UserNotAuthorizedException the connector's userId is not able to work with open metadata
     * @throws PropertyServerException there is an issue with one of the open metadata repositories
     */
    private String findExistingAsset(AssetClient assetClient,
                                     String      qualifiedName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        List<OpenMetadataRootElement> elements = assetClient.getAssetsByName(qualifiedName, assetClient.getQueryOptions());

        if (elements != null)
        {
            for (OpenMetadataRootElement element : elements)
            {
                if ((element != null) && (element.getProperties() instanceof AssetProperties assetProperties))
                {
                    if (qualifiedName.equals(assetProperties.getQualifiedName()))
                    {
                        return element.getElementHeader().getGUID();
                    }
                }
            }
        }

        return null;
    }


    /**
     * Find an existing schema type with the requested qualified name.
     *
     * @param schemaTypeClient client to search with
     * @param qualifiedName unique name to search for
     * @return GUID of the matching schema type, or null if not found
     * @throws InvalidParameterException one of the parameters is not correct
     * @throws UserNotAuthorizedException the connector's userId is not able to work with open metadata
     * @throws PropertyServerException there is an issue with one of the open metadata repositories
     */
    private String findExistingSchemaType(SchemaTypeClient schemaTypeClient,
                                          String           qualifiedName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        List<OpenMetadataRootElement> elements = schemaTypeClient.getSchemaTypesByName(qualifiedName, schemaTypeClient.getQueryOptions());

        if (elements != null)
        {
            for (OpenMetadataRootElement element : elements)
            {
                if ((element != null) && (element.getProperties() instanceof SchemaTypeProperties schemaTypeProperties))
                {
                    if (qualifiedName.equals(schemaTypeProperties.getQualifiedName()))
                    {
                        return element.getElementHeader().getGUID();
                    }
                }
            }
        }

        return null;
    }


    /**
     * Find an existing schema attribute with the requested qualified name.
     *
     * @param schemaAttributeClient client to search with
     * @param qualifiedName unique name to search for
     * @return GUID of the matching schema attribute, or null if not found
     * @throws InvalidParameterException one of the parameters is not correct
     * @throws UserNotAuthorizedException the connector's userId is not able to work with open metadata
     * @throws PropertyServerException there is an issue with one of the open metadata repositories
     */
    private String findExistingSchemaAttribute(SchemaAttributeClient schemaAttributeClient,
                                               String                qualifiedName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        List<OpenMetadataRootElement> elements = schemaAttributeClient.getSchemaAttributesByName(qualifiedName, schemaAttributeClient.getQueryOptions());

        if (elements != null)
        {
            for (OpenMetadataRootElement element : elements)
            {
                if ((element != null) && (element.getProperties() instanceof org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties schemaAttributeProperties))
                {
                    if (qualifiedName.equals(schemaAttributeProperties.getQualifiedName()))
                    {
                        return element.getElementHeader().getGUID();
                    }
                }
            }
        }

        return null;
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
