/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.secretsstore.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.ffdc.YAMLAuditCode;
import org.odpi.openmetadata.frameworks.connectors.properties.users.SecretsCollection;
import org.odpi.openmetadata.frameworks.connectors.properties.users.SecretsStore;
import org.odpi.openmetadata.frameworks.connectors.properties.users.TokenAPI;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStoreCollectionProperty;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.users.NamedList;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccount;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * YAMLSecretsStoreConnector retrieves secrets from a YAML File
 */
public class YAMLSecretsStoreConnector extends SecretsStoreConnector
{
    private static final ObjectMapper yamlObjectMapper = new ObjectMapper(new YAMLFactory());
    private static final ObjectMapper jsonObjectMapper = new ObjectMapper();

    private File         secretsStoreFile = null;
    private SecretsStore secretsStore     = null;



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
    }



    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        /*
         * All handled by SecretsStoreConnector
         */
        super.start();
    }


    /**
     * Retrieve the refresh time from the secrets store.
     *
     * @return how long the secrets can be cached - 0 means indefinitely
     */
    @Override
    public long   getRefreshTimeInterval()
    {
        if (secretsStore != null)
        {
            SecretsCollection secretsCollection = secretsStore.getSecretsCollections().get(secretsCollectionName);

            if (secretsCollection != null)
            {
                return secretsCollection.getRefreshTimeInterval();
            }
        }

        return 0L;
    }



    /**
     * Retrieve a secret from the secrets store.
     *
     * @param secretName name of the secret.
     * @return secret
     * @throws ConnectorCheckedException problem with the store
     */
    @Override
    public String getSecret(String secretName) throws ConnectorCheckedException
    {
        super.checkSecretsStillValid();

        if ((secretsStore != null) && (secretsCollectionName != null))
        {
            SecretsCollection secretsCollection = secretsStore.getSecretsCollections().get(secretsCollectionName);

            if (secretsCollection != null)
            {
                /*
                 * Return the secret if found in the collection
                 */
                if ((secretsCollection.getSecrets() != null) && (secretsCollection.getSecrets().get(secretName) != null))
                {
                    return secretsCollection.getSecrets().get(secretName);
                }

                /*
                 * A token is requested - is there a token API to use?
                 */
                if (SecretsStoreCollectionProperty.TOKEN.getName().equals(secretName))
                {
                    if (secretsCollection.getTokenAPI() != null)
                    {
                        /*
                         * It is possible to request the token through the API
                         */
                        String token = this.getToken(secretsCollection.getTokenAPI());

                        if (token != null)
                        {
                            /*
                             * Save the token for next call.  It will be removed when the secrets store refreshes.
                             */
                            Map<String,String> secrets = secretsCollection.getSecrets();

                            if (secrets == null)
                            {
                                secrets = new HashMap<>();
                            }

                            secrets.put(SecretsStoreCollectionProperty.TOKEN.getName(), token);

                            secretsCollection.setSecrets(secrets);

                            return token;
                        }
                    }
                }
            }
        }

        /*
         * Unable to resolve secret
         */
        return null;
    }




    /**
     * Look up a particular named list in the collection.
     *
     * @param listName name of a list
     * @return corresponding named list or null
     * @throws ConnectorCheckedException there is a problem with the connector
     */
    @Override
    public NamedList getNamedList(String listName) throws ConnectorCheckedException
    {
        super.checkSecretsStillValid();

        if (secretsStore != null)
        {
            SecretsCollection secretsCollection = secretsStore.getSecretsCollections().get(secretsCollectionName);

            if ((secretsCollection != null) && (secretsCollection.getUsers() != null))
            {
                return secretsCollection.getNamedLists().get(listName);
            }
        }

        return null;
    }



    /**
     * Return all of the known named lists in this collection
     *
     * @return map of named lists in this collection
     * @throws ConnectorCheckedException there is a problem with the connector
     */
    @Override
    public Map<String, NamedList> getNamedLists() throws ConnectorCheckedException
    {
        super.checkSecretsStillValid();

        if (secretsStore != null)
        {
            SecretsCollection secretsCollection = secretsStore.getSecretsCollections().get(secretsCollectionName);

            if (secretsCollection != null)
            {
                return secretsCollection.getNamedLists();
            }
        }

        return null;
    }



    /**
     * Retrieve the requested user definitions stored in the secrets collection.
     *
     * @param userId userId for the lookup
     * @return associated user details or null
     * @throws ConnectorCheckedException problem with the store
     */
    @Override
    public UserAccount getUser(String userId) throws ConnectorCheckedException
    {
        super.checkSecretsStillValid();

        if (secretsStore != null)
        {
            SecretsCollection secretsCollection = secretsStore.getSecretsCollections().get(secretsCollectionName);

            if ((secretsCollection != null) && (secretsCollection.getUsers() != null))
            {
                return secretsCollection.getUsers().get(userId);
            }
        }

        return null;
    }


    /**
     * Retrieve any user definitions stored in the secrets collection.
     *
     * @return map of userIds to user details
     * @throws ConnectorCheckedException problem with the store
     */
    @Override
    public Map<String, UserAccount> getUsers() throws ConnectorCheckedException
    {
        super.checkSecretsStillValid();

        if (secretsStore != null)
        {
            SecretsCollection secretsCollection = secretsStore.getSecretsCollections().get(secretsCollectionName);

            if (secretsCollection != null)
            {
                return secretsCollection.getUsers();
            }
        }

        return null;
    }



    /**
     * Request a new token from the token API.
     *
     * @param tokenAPI request specification
     * @return new token or null
     */
    private String getToken(TokenAPI tokenAPI)
    {
        final String methodName = "getToken";

        if (tokenAPI != null)
        {
            /*
             * Determine the content type to use
             */
            String contentType = "application/json";

            if (tokenAPI.getContentType() != null)
            {
                contentType = tokenAPI.getContentType();
            }

            /*
             * Build the HTTP request
             */
            if (tokenAPI.getRequestBody() != null)
            {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(tokenAPI.getURL()))
                        .header("Content-Type", contentType)
                        .method(tokenAPI.getHttpRequestType(), buildRequestBody(contentType,
                                                                                tokenAPI.getRequestBody()))
                        .build();

                try
                {
                    HttpResponse<String> response = HttpClient.newHttpClient()
                            .send(request, HttpResponse.BodyHandlers.ofString());

                    if (response.statusCode() == 200)
                    {
                        return getTokenFromResponseBody(response.body(),
                                                        tokenAPI.getResponseBodyMapping());
                    }
                }
                catch (Exception error)
                {
                    super.logExceptionRecord(methodName,
                                             YAMLAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                     methodName,
                                                                                                     error.getMessage()),
                                             error);
                    return null;
                }
            }
        }

        return null;
    }


    private HttpRequest.BodyPublisher buildRequestBody(String              contentType,
                                                       Map<String, Object> requestBodyParameters)
    {
        if ("application/json".equals(contentType))
        {
            return ofJSON(requestBodyParameters);
        }
        else if ("application/x-www-form-urlencoded".equals(contentType))
        {
            return ofForm(requestBodyParameters);
        }

        return null;
    }


    /**
     * Return the HTTPBodyPublisher populated with the data for the http request.
     *
     * @param data HTTP request parameters
     * @return populated HTTPBodyPublisher
     */
    public HttpRequest.BodyPublisher ofForm(Map<String, Object> data)
    {
        if (data != null)
        {
            StringBuilder body = new StringBuilder();

            for (String dataKey : data.keySet())
            {

                if (! body.isEmpty())
                {
                    body.append("&");
                }

                body.append(encode(dataKey))
                        .append("=")
                        .append(encode(data.get(dataKey)));
            }

            return HttpRequest.BodyPublishers.ofString(body.toString());
        }

        return null;
    }


    /**
     * Return the HTTPBodyPublisher populated with the data for the http request.
     *
     * @param data HTTP request parameters
     * @return populated HTTPBodyPublisher
     */
    public HttpRequest.BodyPublisher ofJSON(Map<String, Object> data)
    {
        if (data != null)
        {
            StringBuilder body = new StringBuilder();

            for (String dataKey : data.keySet())
            {
                if (! body.isEmpty())
                {
                    body.append(",");
                }
                else
                {
                    body.append("{");
                }

                body.append("\"")
                        .append(encode(dataKey))
                        .append("\":\"")
                        .append(encode(data.get(dataKey)))
                        .append("\"");
            }

            if (! body.isEmpty())
            {
                body.append("}");
                return HttpRequest.BodyPublishers.ofString(body.toString());
            }
        }

        return null;
    }


    /**
     * User to ensure the values supplied for the token API are using the required character set for HTTP.
     * This is probably unnecessary because it has already come through the object builder.
     *
     * @param obj object to convert
     * @return converted object
     */
    private String encode(Object obj)
    {
        return URLEncoder.encode(obj.toString(), StandardCharsets.UTF_8);
    }


    /**
     * Extract the token from the response body.
     *
     * @param responseBody response body from token service
     * @param responseBodyMapping information on how to map the results
     * @return token or null
     */
    @SuppressWarnings(value = "unchecked")
    private String getTokenFromResponseBody(String              responseBody,
                                            Map<String, String> responseBodyMapping)
    {
        final String methodName = "getTokenFromResponseBody";

        if ((responseBodyMapping == null) || (responseBodyMapping.isEmpty()))
        {
            /*
             * The whole response body is the token
             */
            return responseBody;
        }

        String tokenVariableName = responseBodyMapping.get(SecretsStoreCollectionProperty.TOKEN.getName());

        if (tokenVariableName != null)
        {
            try
            {
                /*
                 * Assuming the response body is JSON
                 */
                HashMap<String, Object> responseBodyValues = jsonObjectMapper.readValue(responseBody, HashMap.class);

                /*
                 * Any problem retrieving a map (ie it is null) should result in an exception which will be logged.
                 */
                responseBodyValues.get(tokenVariableName);
            }
            catch (Exception error)
            {
                super.logExceptionRecord(methodName,
                                         YAMLAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                 methodName,
                                                                                                 error.getMessage()),
                                         error);
            }
        }

        return null;
    }


    /**
     * Request that the subclass refreshes its secrets.
     *
     * @throws ConnectorCheckedException problem with secrets file
     */
    @Override
    protected void refreshSecrets() throws ConnectorCheckedException
    {
        final String methodName = "refreshSecrets";

        if (secretsStoreFile == null)
        {
            if (connectionBean.getEndpoint().getNetworkAddress() != null)
            {
                secretsStoreFile = new File(connectionBean.getEndpoint().getNetworkAddress());
            }
            else
            {
                throwMissingEndpointAddress(connectionBean.getDisplayName(), methodName);
            }
        }

        try
        {
            secretsStore = yamlObjectMapper.readValue(secretsStoreFile, SecretsStore.class);
        }
        catch (Exception error)
        {
            super.logRecord(methodName,
                            YAMLAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                    methodName,
                                                                                    error.getMessage()));
        }
    }
}
