/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.secretsstore.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.ffdc.YAMLAuditCode;
import org.odpi.openmetadata.frameworks.connectors.properties.users.*;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStoreCollectionProperty;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * YAMLSecretsStoreConnector retrieves secrets from a YAML File
 */
public class YAMLSecretsStoreConnector extends SecretsStoreConnector
{
    private static final ObjectMapper yamlObjectMapper = new ObjectMapper(new YAMLFactory());
    private static final ObjectMapper jsonObjectMapper = new ObjectMapper();

    protected File                               secretsStoreFile         = null;
    protected SecretsStore                       secretsStore             = null;
    protected Map<String, UserAccount>           userAccountMap           = new HashMap<>();
    protected Map<String, SecurityAccessControl> securityAccessControlMap = new HashMap<>();


    /**
     * Call made by the ConnectorProvider to initialize the Connector with the base services.
     *
     * @param connectorInstanceId unique id for the connector instance   useful for messages etc
     * @param connectionDetails   POJO for the configuration used to create the connector
     * @throws ConnectorCheckedException  a problem within the connector.
     */
    @Override
    public void initialize(String     connectorInstanceId,
                           Connection connectionDetails) throws ConnectorCheckedException
    {
        super.initialize(connectorInstanceId, connectionDetails);
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
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
     * @throws ConnectorCheckedException a problem with the connector
     */
    @Override
    public NamedList getNamedList(String listName) throws ConnectorCheckedException
    {
        super.checkSecretsStillValid();

        if (secretsStore != null)
        {
            SecretsCollection secretsCollection = secretsStore.getSecretsCollections().get(secretsCollectionName);

            if ((secretsCollection != null) && (secretsCollection.getNamedLists() != null))
            {
                return secretsCollection.getNamedLists().get(listName);
            }
        }

        return null;
    }


    /**
     * Return all the known named lists in this collection
     *
     * @return map of named lists in this collection
     * @throws ConnectorCheckedException a problem with the connector
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
     * Look up a particular named security access control in the collection.
     *
     * @param controlName name of the control
     * @return corresponding named control or null
     * @throws ConnectorCheckedException a problem with the connector
     */
    @Override
    public SecurityAccessControl getSecurityAccessControl(String controlName) throws ConnectorCheckedException
    {
        super.checkSecretsStillValid();

        if (securityAccessControlMap.containsKey(controlName))
        {
            return securityAccessControlMap.get(controlName);
        }

        if (secretsStore != null)
        {
            SecretsCollection secretsCollection = secretsStore.getSecretsCollections().get(secretsCollectionName);

            if ((secretsCollection != null) && (secretsCollection.getSecurityAccessControls() != null))
            {
                return populateSecurityAccessControl(controlName, secretsCollection.getSecurityAccessControls().get(controlName), secretsCollection.getNamedLists());
            }
        }

        return null;
    }


    /**
     * Save the requested security access control in the secrets collection.
     *
     * @param controlName           controlName for the lookup
     * @param securityAccessControl associated control details
     * @throws ConnectorCheckedException a problem with the connector
     */
    @Override
    public void saveSecurityAccessControl(String                controlName,
                                          SecurityAccessControl securityAccessControl) throws ConnectorCheckedException
    {
        if (secretsStore != null)
        {
            SecretsCollection secretsCollection = secretsStore.getSecretsCollections().get(secretsCollectionName);

            if (secretsCollection != null)
            {
                /*
                 * Ensure addition information from a subclass is not included
                 */
                SecurityAccessControl newSecurityAccessControl = new SecurityAccessControl(securityAccessControl);

                /*
                 * Set up the new control ...
                 */
                secretsCollection.getSecurityAccessControls().put(controlName, newSecurityAccessControl);

                saveSecrets();

                /*
                 * Refresh the user cache.
                 */
                populateSecurityAccessControl(controlName, newSecurityAccessControl, secretsCollection.getNamedLists());
            }
        }
    }


    /**
     * Delete the requested security access control stored in the secrets collection.
     *
     * @param controlName control for the lookup
     * @throws ConnectorCheckedException a problem with the connector
     */
    @Override
    public void deleteSecurityAccessControl(String controlName) throws ConnectorCheckedException
    {
        if (secretsStore != null)
        {
            SecretsCollection secretsCollection = secretsStore.getSecretsCollections().get(secretsCollectionName);

            if (secretsCollection != null)
            {
                secretsCollection.getSecurityAccessControls().remove(controlName);
                saveSecrets();
                securityAccessControlMap.remove(controlName);
            }
        }
    }


    /**
     * Populate the security access control details with the linked named lists.  The result is cached before
     * returning to speed this process for future requests.  This is important
     * because some access secuirty controls are used on many requests.
     *
     * @param controlName           name of the security access control
     * @param securityAccessControl security access control details
     * @param securityLists         security lists
     * @return populated security access control details
     */
    private SecurityAccessControl populateSecurityAccessControl(String                 controlName,
                                                                SecurityAccessControl  securityAccessControl,
                                                                Map<String, NamedList> securityLists)
    {
        if (securityAccessControl != null)
        {
            SecurityAccessControl newSecurityAccessControl = new SecurityAccessControl(securityAccessControl);

            if ((securityLists != null) && (newSecurityAccessControl.getAssociatedSecurityList() != null))
            {
                Map<String, List<String>> associatedSecurityList = new HashMap<>();

                for (String operation : newSecurityAccessControl.getAssociatedSecurityList().keySet())
                {
                    if (newSecurityAccessControl.getAssociatedSecurityList().get(operation) != null)
                    {
                        List<String> permissionList = new ArrayList<>(newSecurityAccessControl.getAssociatedSecurityList().get(operation));

                        for (String permission : newSecurityAccessControl.getAssociatedSecurityList().get(operation))
                        {
                            if (securityLists.containsKey(permission))
                            {
                                NamedList namedList = securityLists.get(permission);

                                if (namedList != null)
                                {
                                    if (namedList.getListMembers() != null)
                                    {
                                        permissionList.addAll(namedList.getListMembers());

                                        for (String nestedPermission : namedList.getListMembers())
                                        {
                                            permissionList.addAll(getNestedPermissions(nestedPermission, securityLists));
                                        }
                                    }

                                    if (namedList.getUserMembers() != null)
                                    {
                                        permissionList.addAll(namedList.getUserMembers());
                                    }
                                }
                            }
                        }

                        associatedSecurityList.put(operation, permissionList);
                    }
                }

                newSecurityAccessControl.setAssociatedSecurityList(associatedSecurityList);
            }

            securityAccessControlMap.put(controlName, newSecurityAccessControl);
            return newSecurityAccessControl;
        }

        return null;
    }


    /**
     * Return all the known security access controls in this collection.
     *
     * @param permissionName name of control
     * @param securityLists defined lists
     * @return set of members
     */
    private Set<String> getNestedPermissions(String                 permissionName,
                                             Map<String, NamedList> securityLists)
    {
        List<String> nestedPermissions = new ArrayList<>();

        NamedList namedList = securityLists.get(permissionName);
        if (namedList != null)
        {
            if (namedList.getListMembers() != null)
            {
                nestedPermissions.addAll(namedList.getListMembers());

                for (String nestedPermission : namedList.getListMembers())
                {
                    nestedPermissions.addAll(getNestedPermissions(nestedPermission, securityLists));
                }
            }
        }

        return new HashSet<>(nestedPermissions);
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

        /*
         * If the user is already in the cache, return it.
         */
        if (userAccountMap.containsKey(userId))
        {
            return userAccountMap.get(userId);
        }

        if (secretsStore != null)
        {
            SecretsCollection secretsCollection = secretsStore.getSecretsCollections().get(secretsCollectionName);

            if ((secretsCollection != null) && (secretsCollection.getUsers() != null))
            {
                UserAccount userAccount = secretsCollection.getUsers().get(userId);
                if (userAccount != null)
                {
                    return populateUserAccount(userId, userAccount, secretsCollection.getNamedLists());
                }
            }
        }

        /*
         * Unable to resolve user.  Refresh the secrets and try again because this may be a new user.
         */
        refreshSecrets();

        if (secretsStore != null)
        {
            SecretsCollection secretsCollection = secretsStore.getSecretsCollections().get(secretsCollectionName);

            if ((secretsCollection != null) && (secretsCollection.getUsers() != null))
            {
                UserAccount userAccount = secretsCollection.getUsers().get(userId);
                if (userAccount != null)
                {
                    return populateUserAccount(userId, userAccount, secretsCollection.getNamedLists());
                }
            }
        }

        return null;
    }


    /**
     * Populate the user account with the security lists that the user is a member of.
     *
     * @param userId calling user
     * @param userAccount user account to populate
     * @param securityLists security lists
     * @return populated user account
     */
    private UserAccount populateUserAccount(String                 userId,
                                            UserAccount            userAccount,
                                            Map<String, NamedList> securityLists)
    {
        if (userAccount != null)
        {
            UserAccount newUserAccount = new UserAccount(userAccount);

            if (securityLists != null)
            {
                /*
                 * Locate all the lists that the user is directly named in (memberLists).
                 */
                Set<String> memberLists = new HashSet<>();

                for (String listName : securityLists.keySet())
                {
                    if ((listName != null) && (securityLists.get(listName) != null))
                    {
                        NamedList namedList = securityLists.get(listName);

                        if (namedList.getUserMembers() != null)
                        {
                            if (namedList.getUserMembers().contains(userId))
                            {
                                memberLists.add(listName);
                            }
                        }
                    }
                }

                /*
                 * Navigate to find the parent lists.
                 */
                Set<String> parentLists = addParentLists(memberLists, securityLists);

                /*
                 * Gather the results.
                 */
                Set<String> securityRoles = new HashSet<>();
                Set<String> securityGroups = new HashSet<>();

                /*
                 * Save any values that are explicitly set up in the user account.
                 */
                if (userAccount.getSecurityRoles() != null)
                {
                    securityRoles.addAll(userAccount.getSecurityRoles());
                }
                if (userAccount.getSecurityGroups() != null)
                {
                    securityGroups.addAll(userAccount.getSecurityGroups());
                }

                /*
                 * Add both the user groups and roles that the user is directly connected to.
                 */
                for (String listName : memberLists)
                {
                    String listTypeName = securityLists.get(listName).getListTypeName();
                    if (OpenMetadataType.SECURITY_ROLE.typeName.equals(listTypeName))
                    {
                        securityRoles.add(listName);
                    }
                    else if (OpenMetadataType.SECURITY_GROUP.typeName.equals(listTypeName))
                    {
                        securityGroups.add(listName);
                    }
                }

                /*
                 * Now add the groups and roles that the user is connected to through a parent list.
                 */
                for (String listName : parentLists)
                {
                    String listTypeName = securityLists.get(listName).getListTypeName();
                    if (OpenMetadataType.SECURITY_ROLE.typeName.equals(listTypeName))
                    {
                        securityRoles.add(listName);
                    }
                    else if (OpenMetadataType.SECURITY_GROUP.typeName.equals(listTypeName))
                    {
                        securityGroups.add(listName);
                    }
                }

                if (securityRoles.isEmpty())
                {
                    newUserAccount.setSecurityRoles(null);
                }
                else
                {
                    newUserAccount.setSecurityRoles(new ArrayList<>(securityRoles));
                }

                if (securityGroups.isEmpty())
                {
                    newUserAccount.setSecurityGroups(null);
                }
                else
                {
                    newUserAccount.setSecurityGroups(new ArrayList<>(securityGroups));
                }
            }

            userAccountMap.put(userId, newUserAccount);
            return newUserAccount;
        }

        return null;
    }


    /**
     * Navigate up the list hierarchy to find all lists.
     *
     * @param currentParentSecurityLists current list of parents
     * @param securityLists full set of named lists
     * @return set of grandparent lists
     */
    private Set<String> addParentLists(Set<String>            currentParentSecurityLists,
                                       Map<String, NamedList> securityLists)
    {
        Set<String> grandParentLists = new HashSet<>();

        /*
         * For each of the current parent groups, find the groups
         */
        for (String currentParentListName : currentParentSecurityLists)
        {
            for (String listName : securityLists.keySet())
            {
                NamedList namedList = securityLists.get(listName);

                if ((namedList != null) &&
                        (namedList.getListMembers() != null) &&
                        (namedList.getListMembers().contains(currentParentListName)))
                {
                    grandParentLists.add(listName);

                    /*
                     * Iterate to find great-grand parent.
                     */
                    HashSet<String> greatGrandParentGroups = new HashSet<>();;
                    greatGrandParentGroups.add(listName);

                    grandParentLists.addAll(addParentLists(greatGrandParentGroups, securityLists));
                }
            }
        }

        return grandParentLists;
    }


    /**
     * Save the requested user definitions in the secrets collection.
     *
     * @param userId      userId for the lookup
     * @param userAccount associated user details
     * @throws ConnectorCheckedException a problem with the connector
     */
    @Override
    public void saveUser(String      userId,
                         UserAccount userAccount) throws ConnectorCheckedException
    {
        if (secretsStore != null)
        {
            SecretsCollection secretsCollection = secretsStore.getSecretsCollections().get(secretsCollectionName);

            if (secretsCollection != null)
            {
                /*
                 * Ensure properties from a subtype are not included
                 */
                UserAccount newUserAccount = new UserAccount(userAccount);

                /*
                 * Set up the user account ...
                 */
                secretsCollection.getUsers().put(userId, newUserAccount);

                /*
                 * Add the user to any named security roles ...
                 */
                if (newUserAccount.getSecurityRoles() != null)
                {
                    for (String securityRole : newUserAccount.getSecurityRoles())
                    {
                        if (securityRole != null)
                        {
                            addUserToNamedList(userId, securityRole, OpenMetadataType.SECURITY_ROLE.typeName, secretsCollection);
                        }
                    }
                }

                /*
                 * Add the user to any named security groups ...
                 */
                if (newUserAccount.getSecurityGroups() != null)
                {
                    for (String securityGroup : newUserAccount.getSecurityGroups())
                    {
                        if (securityGroup != null)
                        {
                            addUserToNamedList(userId, securityGroup, OpenMetadataType.SECURITY_GROUP.typeName, secretsCollection);
                        }
                    }
                }

                saveSecrets();

                /*
                 * Refresh the user cache.
                 */
                populateUserAccount(userId, newUserAccount, secretsCollection.getNamedLists());
            }
        }
    }


    /**
     * Update one of the requested named lists for a new user.
     *
     * @param userId            requesting user
     * @param listName          name of the list to update
     * @param listTypeName      type of list
     * @param secretsCollection secrets collection
     */
    private void addUserToNamedList(String            userId,
                                    String            listName,
                                    String            listTypeName,
                                    SecretsCollection secretsCollection)
    {
        NamedList namedList = secretsStore.getSecretsCollections().get(secretsCollectionName).getNamedLists().get(listName);

        if (namedList != null)
        {
            if (namedList.getListMembers() == null)
            {
                namedList.setListMembers(Collections.singletonList(userId));
            }
            else if (! namedList.getListMembers().contains(userId))
            {
                namedList.getListMembers().add(userId);
            }
        }
        else
        {
            namedList = new NamedList();
            namedList.setListTypeName(listTypeName);
            namedList.setUserMembers(Collections.singletonList(userId));
            secretsCollection.getNamedLists().put(listName, namedList);
        }
    }


    /**
     * Delete the requested user definitions stored in the secrets collection.
     *
     * @param userId userId for the lookup
     * @throws ConnectorCheckedException a problem with the connector
     */
    @Override
    public void deleteUser(String userId) throws ConnectorCheckedException
    {
        if (secretsStore != null)
        {
            SecretsCollection secretsCollection = secretsStore.getSecretsCollections().get(secretsCollectionName);

            if (secretsCollection != null)
            {
                secretsCollection.getUsers().remove(userId);
                saveSecrets();
                userAccountMap.remove(userId);
            }
        }
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
                secretsStoreFile         = new File(connectionBean.getEndpoint().getNetworkAddress());
                userAccountMap           = new HashMap<>();
                securityAccessControlMap = new HashMap<>();
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
            secretsStore = null;
            super.logRecord(methodName,
                            YAMLAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                    methodName,
                                                                                    error.getMessage()));
        }
    }


    /**
     * Request that the subclass save its secrets.
     *
     * @throws ConnectorCheckedException problem with secrets file
     */
    @Override
    protected void saveSecrets() throws ConnectorCheckedException
    {
        final String methodName = "saveSecrets";

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
            yamlObjectMapper.writeValue(secretsStoreFile, secretsStore);
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
