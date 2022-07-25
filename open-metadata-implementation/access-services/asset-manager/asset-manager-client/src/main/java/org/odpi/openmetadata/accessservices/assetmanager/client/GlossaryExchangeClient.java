/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.client;

import org.odpi.openmetadata.accessservices.assetmanager.api.GlossaryExchangeInterface;
import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.accessservices.assetmanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;

/**
 * GlossaryExchangeClient is the client for managing resources from a glossary.
 */
public class GlossaryExchangeClient extends ExchangeClientBase implements GlossaryExchangeInterface
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GlossaryExchangeClient(String   serverName,
                                  String   serverPlatformURLRoot,
                                  AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GlossaryExchangeClient(String serverName,
                                  String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GlossaryExchangeClient(String   serverName,
                                  String   serverPlatformURLRoot,
                                  String   userId,
                                  String   password,
                                  AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GlossaryExchangeClient(String                 serverName,
                                  String                 serverPlatformURLRoot,
                                  AssetManagerRESTClient restClient,
                                  int                    maxPageSize,
                                  AuditLog               auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GlossaryExchangeClient(String serverName,
                                  String serverPlatformURLRoot,
                                  String userId,
                                  String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /* ========================================================
     * The Glossary entity is the top level element in a glossary.
     */


    /**
     * Create a new metadata element to represent the root of a glossary.  All categories and terms are linked
     * to a single glossary.  They are owned by this glossary and if the glossary is deleted, any linked terms and
     * categories are deleted as well.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param glossaryProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createGlossary(String                       userId,
                                 String                       assetManagerGUID,
                                 String                       assetManagerName,
                                 ExternalIdentifierProperties externalIdentifierProperties,
                                 GlossaryProperties           glossaryProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName                  = "createGlossary";
        final String propertiesParameterName     = "glossaryProperties";
        final String qualifiedNameParameterName  = "glossaryProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(glossaryProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(glossaryProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        GlossaryRequestBody requestBody = new GlossaryRequestBody();
        requestBody.setElementProperties(glossaryProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a glossary using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new glossary.
     *
     * All categories and terms are linked to a single glossary.  They are owned by this glossary and if the
     * glossary is deleted, any linked terms and categories are deleted as well.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createGlossaryFromTemplate(String                       userId,
                                             String                       assetManagerGUID,
                                             String                       assetManagerName,
                                             String                       templateGUID,
                                             ExternalIdentifierProperties externalIdentifierProperties,
                                             TemplateProperties           templateProperties) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName                  = "createGlossaryFromTemplate";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "templateProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        TemplateRequestBody requestBody = new TemplateRequestBody();
        requestBody.setElementProperties(templateProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/from-template/{2}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  templateGUID);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a glossary.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the metadata element to update
     * @param glossaryExternalIdentifier unique identifier of the glossary in the external asset manager
     * @param glossaryProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateGlossary(String             userId,
                               String             assetManagerGUID,
                               String             assetManagerName,
                               String             glossaryGUID,
                               String             glossaryExternalIdentifier,
                               GlossaryProperties glossaryProperties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName                  = "updateGlossary";
        final String glossaryGUIDParameterName   = "glossaryGUID";
        final String propertiesParameterName     = "glossaryProperties";
        final String qualifiedNameParameterName  = "glossaryProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(glossaryProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(glossaryProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        GlossaryRequestBody requestBody = new GlossaryRequestBody();
        requestBody.setElementProperties(glossaryProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   glossaryExternalIdentifier,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/{2}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        glossaryGUID);
    }


    /**
     * Classify the glossary to indicate that it can be used as a taxonomy.
     * This means each term is attached to one, and only one category and the categories are organized as a hierarchy
     * with a single root category.
     *
     * Taxonomies are used as a way of organizing assets and other related metadata.  The terms in the taxonomy
     * are linked to the assets etc. and as such they are logically categorized by the linked category.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param glossaryExternalIdentifier unique identifier of the glossary in the external asset manager
     * @param organizingPrinciple description of how the glossary is organized
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setGlossaryAsTaxonomy(String userId,
                                      String assetManagerGUID,
                                      String assetManagerName,
                                      String glossaryGUID,
                                      String glossaryExternalIdentifier,
                                      String organizingPrinciple) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName                  = "setGlossaryAsTaxonomy";
        final String glossaryGUIDParameterName   = "glossaryGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);

        TaxonomyClassificationRequestBody requestBody = new TaxonomyClassificationRequestBody();
        requestBody.setOrganizingPrinciple(organizingPrinciple);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   glossaryExternalIdentifier,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/{2}/is-taxonomy";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        glossaryGUID);
    }


    /**
     * Remove the taxonomy designation from the glossary.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param glossaryExternalIdentifier unique identifier of the glossary in the external asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearGlossaryAsTaxonomy(String userId,
                                        String assetManagerGUID,
                                        String assetManagerName,
                                        String glossaryGUID,
                                        String glossaryExternalIdentifier) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName                  = "clearGlossaryAsTaxonomy";
        final String glossaryGUIDParameterName   = "glossaryGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/{2}/is-taxonomy/remove";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        this.getCorrelationProperties(assetManagerGUID,
                                                                      assetManagerName,
                                                                      glossaryExternalIdentifier,
                                                                      methodName),
                                        serverName,
                                        userId,
                                        glossaryGUID);
    }


    /**
     * Classify a glossary to declare that it has no two GlossaryTerm definitions with
     * the same name.  This means there is only one definition for each term.  Typically, the terms are also of a similar
     * level of granularity and are limited to a specific scope of use.
     *
     * Canonical vocabularies are used to semantically classify assets in an unambiguous way.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param glossaryExternalIdentifier unique identifier of the glossary in the external asset manager
     * @param scope description of the situations where this glossary is relevant.
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setGlossaryAsCanonical(String userId,
                                       String assetManagerGUID,
                                       String assetManagerName,
                                       String glossaryGUID,
                                       String glossaryExternalIdentifier,
                                       String scope) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String methodName                  = "setGlossaryAsCanonical";
        final String glossaryGUIDParameterName   = "glossaryGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);

        CanonicalVocabularyClassificationRequestBody requestBody = new CanonicalVocabularyClassificationRequestBody();
        requestBody.setScope(scope);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   glossaryExternalIdentifier,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/{2}/is-canonical";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        glossaryGUID);
    }


    /**
     * Remove the canonical designation from the glossary.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param glossaryExternalIdentifier unique identifier of the glossary in the external asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearGlossaryAsCanonical(String userId,
                                         String assetManagerGUID,
                                         String assetManagerName,
                                         String glossaryGUID,
                                         String glossaryExternalIdentifier) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName                  = "clearGlossaryAsCanonical";
        final String glossaryGUIDParameterName   = "glossaryGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/{2}/is-canonical/remove";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        this.getCorrelationProperties(assetManagerGUID,
                                                                      assetManagerName,
                                                                      glossaryExternalIdentifier,
                                                                      methodName),
                                        serverName,
                                        userId,
                                        glossaryGUID);
    }


    /**
     * Remove the metadata element representing a glossary.  This will delete the glossary and all categories
     * and terms.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param glossaryExternalIdentifier unique identifier of the glossary in the external asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeGlossary(String userId,
                               String assetManagerGUID,
                               String assetManagerName,
                               String glossaryGUID,
                               String glossaryExternalIdentifier) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName                  = "removeGlossary";
        final String glossaryGUIDParameterName   = "glossaryGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/{2}/remove";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        this.getCorrelationProperties(assetManagerGUID,
                                                                      assetManagerName,
                                                                      glossaryExternalIdentifier,
                                                                      methodName),
                                        serverName,
                                        userId,
                                        glossaryGUID);
    }


    /**
     * Retrieve the list of glossary metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned*
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<GlossaryElement>   findGlossaries(String  userId,
                                                  String  assetManagerGUID,
                                                  String  assetManagerName,
                                                  String  searchString,
                                                  int     startFrom,
                                                  int     pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName                = "findGlossaries";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/by-search-string?startFrom={2}&pageSize={3}";

        GlossaryElementsResponse restResult = restClient.callGlossariesPostRESTCall(methodName,
                                                                                    urlTemplate,
                                                                                    requestBody,
                                                                                    serverName,
                                                                                    userId,
                                                                                    startFrom,
                                                                                    validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of glossary metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<GlossaryElement>   getGlossariesByName(String  userId,
                                                       String  assetManagerGUID,
                                                       String  assetManagerName,
                                                       String  name,
                                                       int     startFrom,
                                                       int     pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName        = "getGlossariesByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/by-name?startFrom={2}&pageSize={3}";

        GlossaryElementsResponse restResult = restClient.callGlossariesPostRESTCall(methodName,
                                                                                    urlTemplate,
                                                                                    requestBody,
                                                                                    serverName,
                                                                                    userId,
                                                                                    startFrom,
                                                                                    validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of glossaries created on behalf of the named asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<GlossaryElement>   getGlossariesForAssetManager(String  userId,
                                                                String  assetManagerGUID,
                                                                String  assetManagerName,
                                                                int     startFrom,
                                                                int     pageSize) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "getGlossariesForAssetManager";

        invalidParameterHandler.validateUserId(userId, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/by-asset-manager?startFrom={2}&pageSize={3}";

        GlossaryElementsResponse restResult = restClient.callGlossariesPostRESTCall(methodName,
                                                                                    urlTemplate,
                                                                                    getAssetManagerIdentifiersRequestBody(assetManagerGUID, assetManagerName),
                                                                                    serverName,
                                                                                    userId,
                                                                                    startFrom,
                                                                                    validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the glossary metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public GlossaryElement getGlossaryByGUID(String  userId,
                                             String  assetManagerGUID,
                                             String  assetManagerName,
                                             String  guid) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "getGlossaryByGUID";
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/{2}/retrieve";

        GlossaryElementResponse restResult = restClient.callGlossaryPostRESTCall(methodName,
                                                                                 urlTemplate,
                                                                                 getAssetManagerIdentifiersRequestBody(assetManagerGUID, assetManagerName),
                                                                                 serverName,
                                                                                 userId,
                                                                                 guid);

        return restResult.getElement();
    }



    /* =====================================================================================================================
     * A glossary may host one or more glossary categories depending on its capability
     */

    /**
     * Create a new metadata element to represent a glossary category.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the glossary where the category is located
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param glossaryCategoryProperties properties about the glossary category to store
     *
     * @return unique identifier of the new glossary category
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createGlossaryCategory(String                       userId,
                                         String                       assetManagerGUID,
                                         String                       assetManagerName,
                                         String                       glossaryGUID,
                                         ExternalIdentifierProperties externalIdentifierProperties,
                                         GlossaryCategoryProperties   glossaryCategoryProperties) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName                  = "createGlossaryCategory";
        final String guidParameterName           = "glossaryGUID";
        final String propertiesParameterName     = "glossaryCategoryProperties";
        final String qualifiedNameParameterName  = "glossaryCategoryProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(glossaryCategoryProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(glossaryCategoryProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        GlossaryCategoryRequestBody requestBody = new GlossaryCategoryRequestBody();
        requestBody.setElementProperties(glossaryCategoryProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/{2}/categories";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  glossaryGUID);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a glossary category using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new glossary category
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createGlossaryCategoryFromTemplate(String                       userId,
                                                     String                       assetManagerGUID,
                                                     String                       assetManagerName,
                                                     String                       templateGUID,
                                                     ExternalIdentifierProperties externalIdentifierProperties,
                                                     TemplateProperties           templateProperties) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        final String methodName                  = "createGlossaryCategoryFromTemplate";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "templateProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        TemplateRequestBody requestBody = new TemplateRequestBody();
        requestBody.setElementProperties(templateProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/categories/from-template/{1}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  templateGUID);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a glossary category.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the metadata element to update
     * @param glossaryCategoryExternalIdentifier unique identifier of the glossary category in the external asset manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param glossaryCategoryProperties new properties for the metadata element
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateGlossaryCategory(String                     userId,
                                       String                     assetManagerGUID,
                                       String                     assetManagerName,
                                       String                     glossaryCategoryGUID,
                                       String                     glossaryCategoryExternalIdentifier,
                                       boolean                    isMergeUpdate,
                                       GlossaryCategoryProperties glossaryCategoryProperties,
                                       Date                       effectiveTime,
                                       boolean                    forLineage,
                                       boolean                    forDuplicateProcessing) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName                  = "updateGlossaryCategory";
        final String glossaryGUIDParameterName   = "glossaryCategoryGUID";
        final String propertiesParameterName     = "glossaryCategoryProperties";
        final String qualifiedNameParameterName  = "glossaryCategoryProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryCategoryGUID, glossaryGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(glossaryCategoryProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(glossaryCategoryProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        GlossaryCategoryRequestBody requestBody = new GlossaryCategoryRequestBody();
        requestBody.setElementProperties(glossaryCategoryProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   glossaryCategoryExternalIdentifier,
                                                                                   methodName));
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/categories/{2}?isMergeUpdate={3}&forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        glossaryCategoryGUID,
                                        isMergeUpdate,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Create a parent-child relationship between two categories.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryParentCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the super-category
     * @param glossaryChildCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the subcategory
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupCategoryParent(String  userId,
                                    String  assetManagerGUID,
                                    String  assetManagerName,
                                    String  glossaryParentCategoryGUID,
                                    String  glossaryChildCategoryGUID,
                                    Date    effectiveTime,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName                      = "setupCategoryParent";
        final String glossaryParentGUIDParameterName = "glossaryParentCategoryGUID";
        final String glossaryChildGUIDParameterName  = "glossaryChildCategoryGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryParentCategoryGUID, glossaryParentGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(glossaryChildCategoryGUID, glossaryChildGUIDParameterName, methodName);


        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/categories/{2}/subcategories/{3}?forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        glossaryParentCategoryGUID,
                                        glossaryChildCategoryGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove a parent-child relationship between two categories.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryParentCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the super-category
     * @param glossaryChildCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the subcategory
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearCategoryParent(String  userId,
                                    String  assetManagerGUID,
                                    String  assetManagerName,
                                    String  glossaryParentCategoryGUID,
                                    String  glossaryChildCategoryGUID,
                                    Date    effectiveTime,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName                      = "clearCategoryParent";
        final String glossaryParentGUIDParameterName = "glossaryParentCategoryGUID";
        final String glossaryChildGUIDParameterName  = "glossaryChildCategoryGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryParentCategoryGUID, glossaryParentGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(glossaryChildCategoryGUID, glossaryChildGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/categories/{2}/subcategory/{3}/remove?forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        glossaryParentCategoryGUID,
                                        glossaryChildCategoryGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the metadata element representing a glossary category.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the metadata element to remove
     * @param glossaryCategoryExternalIdentifier unique identifier of the glossary category in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeGlossaryCategory(String  userId,
                                       String  assetManagerGUID,
                                       String  assetManagerName,
                                       String  glossaryCategoryGUID,
                                       String  glossaryCategoryExternalIdentifier,
                                       Date    effectiveTime,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName                  = "removeGlossaryCategory";
        final String glossaryGUIDParameterName   = "glossaryCategoryGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryCategoryGUID, glossaryGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/categories/{2}/remove?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, glossaryCategoryExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        glossaryCategoryGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Retrieve the list of glossary category metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<GlossaryCategoryElement> findGlossaryCategories(String  userId,
                                                                String  assetManagerGUID,
                                                                String  assetManagerName,
                                                                String  searchString,
                                                                int     startFrom,
                                                                int     pageSize,
                                                                Date    effectiveTime,
                                                                boolean forLineage,
                                                                boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName                = "findGlossaryCategories";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/categories/by-search-string?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        GlossaryCategoryElementsResponse restResult = restClient.callGlossaryCategoriesPostRESTCall(methodName,
                                                                                                    urlTemplate,
                                                                                                    requestBody,
                                                                                                    serverName,
                                                                                                    userId,
                                                                                                    startFrom,
                                                                                                    validatedPageSize,
                                                                                                    forLineage,
                                                                                                    forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Return the list of categories associated with a glossary.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the glossary to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of metadata elements describing the categories associated with the requested glossary
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<GlossaryCategoryElement>   getCategoriesForGlossary(String  userId,
                                                                    String  assetManagerGUID,
                                                                    String  assetManagerName,
                                                                    String  glossaryGUID,
                                                                    int     startFrom,
                                                                    int     pageSize,
                                                                    Date    effectiveTime,
                                                                    boolean forLineage,
                                                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String methodName        = "getCategoriesForGlossary";
        final String guidParameterName = "glossaryGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, guidParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/{2}/categories/retrieve?startFrom={3}&pageSize={4}&forLineage={5}&forDuplicateProcessing={6}";

        GlossaryCategoryElementsResponse restResult = restClient.callGlossaryCategoriesPostRESTCall(methodName,
                                                                                                    urlTemplate,
                                                                                                    getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                                    serverName,
                                                                                                    userId,
                                                                                                    glossaryGUID,
                                                                                                    startFrom,
                                                                                                    validatedPageSize,
                                                                                                    forLineage,
                                                                                                    forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of glossary category metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<GlossaryCategoryElement>   getGlossaryCategoriesByName(String  userId,
                                                                       String  assetManagerGUID,
                                                                       String  assetManagerName,
                                                                       String  name,
                                                                       int     startFrom,
                                                                       int     pageSize,
                                                                       Date    effectiveTime,
                                                                       boolean forLineage,
                                                                       boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException
    {
        final String methodName        = "getGlossaryCategoriesByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/categories/by-name?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        GlossaryCategoryElementsResponse restResult = restClient.callGlossaryCategoriesPostRESTCall(methodName,
                                                                                                    urlTemplate,
                                                                                                    requestBody,
                                                                                                    serverName,
                                                                                                    userId,
                                                                                                    startFrom,
                                                                                                    validatedPageSize,
                                                                                                    forLineage,
                                                                                                    forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public GlossaryCategoryElement getGlossaryCategoryByGUID(String  userId,
                                                             String  assetManagerGUID,
                                                             String  assetManagerName,
                                                             String  glossaryCategoryGUID,
                                                             Date    effectiveTime,
                                                             boolean forLineage,
                                                             boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName = "getGlossaryCategoryByGUID";
        final String guidParameterName = "glossaryCategoryGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryCategoryGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/categories/{2}/retrieve?forLineage={3}&forDuplicateProcessing={4}";

        GlossaryCategoryElementResponse restResult = restClient.callGlossaryCategoryPostRESTCall(methodName,
                                                                                                 urlTemplate,
                                                                                                 getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                                 serverName,
                                                                                                 userId,
                                                                                                 glossaryCategoryGUID,
                                                                                                 forLineage,
                                                                                                 forDuplicateProcessing);

        return restResult.getElement();
    }


    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return parent glossary category element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public GlossaryCategoryElement getGlossaryCategoryParent(String  userId,
                                                             String  assetManagerGUID,
                                                             String  assetManagerName,
                                                             String  glossaryCategoryGUID,
                                                             Date    effectiveTime,
                                                             boolean forLineage,
                                                             boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName = "getGlossaryCategoryParent";
        final String guidParameterName = "glossaryCategoryGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryCategoryGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/categories/{2}/parent/retrieve?forLineage={3}&forDuplicateProcessing={4}";

        GlossaryCategoryElementResponse restResult = restClient.callGlossaryCategoryPostRESTCall(methodName,
                                                                                                 urlTemplate,
                                                                                                 getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                                 serverName,
                                                                                                 userId,
                                                                                                 glossaryCategoryGUID,
                                                                                                 forLineage,
                                                                                                 forDuplicateProcessing);

        return restResult.getElement();
    }


    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of glossary category element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<GlossaryCategoryElement> getGlossarySubCategories(String  userId,
                                                                  String  assetManagerGUID,
                                                                  String  assetManagerName,
                                                                  String  glossaryCategoryGUID,
                                                                  int     startFrom,
                                                                  int     pageSize,
                                                                  Date    effectiveTime,
                                                                  boolean forLineage,
                                                                  boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName        = "getGlossaryCategoriesByName";
        final String guidParameterName = "glossaryCategoryGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryCategoryGUID, guidParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/categories/{2}/subcategories/retrieve?startFrom={3}&pageSize={4}&forLineage={5}&forDuplicateProcessing={6}";

        GlossaryCategoryElementsResponse restResult = restClient.callGlossaryCategoriesPostRESTCall(methodName,
                                                                                                    urlTemplate,
                                                                                                    getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                                    serverName,
                                                                                                    userId,
                                                                                                    glossaryCategoryGUID,
                                                                                                    startFrom,
                                                                                                    validatedPageSize,
                                                                                                    forLineage,
                                                                                                    forDuplicateProcessing);

        return restResult.getElementList();
    }


    /* ===============================================================================
     * A glossary typically contains many glossary terms, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a glossary term.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the glossary where the term is located
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param glossaryTermProperties properties for the glossary term
     *
     * @return unique identifier of the new metadata element for the glossary term
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createGlossaryTerm(String                       userId,
                                     String                       assetManagerGUID,
                                     String                       assetManagerName,
                                     String                       glossaryGUID,
                                     ExternalIdentifierProperties externalIdentifierProperties,
                                     GlossaryTermProperties       glossaryTermProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName                  = "createGlossaryTerm";
        final String guidParameterName           = "glossaryGUID";
        final String propertiesParameterName     = "glossaryTermProperties";
        final String qualifiedNameParameterName  = "glossaryTermProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(glossaryTermProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(glossaryTermProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        GlossaryTermRequestBody requestBody = new GlossaryTermRequestBody();
        requestBody.setElementProperties(glossaryTermProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/{2}/terms";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  glossaryGUID);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a glossary term whose lifecycle is managed through a controlled workflow.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the glossary where the term is located
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param glossaryTermProperties properties for the glossary term
     * @param initialStatus glossary term status to use when the object is created
     *
     * @return unique identifier of the new metadata element for the glossary term
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createControlledGlossaryTerm(String                       userId,
                                               String                       assetManagerGUID,
                                               String                       assetManagerName,
                                               String                       glossaryGUID,
                                               ExternalIdentifierProperties externalIdentifierProperties,
                                               GlossaryTermProperties       glossaryTermProperties,
                                               GlossaryTermStatus           initialStatus) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName                  = "createControlledGlossaryTerm";
        final String guidParameterName           = "glossaryGUID";
        final String propertiesParameterName     = "glossaryTermProperties";
        final String qualifiedNameParameterName  = "glossaryTermProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(glossaryTermProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(glossaryTermProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        ControlledGlossaryTermRequestBody requestBody = new ControlledGlossaryTermRequestBody();
        requestBody.setElementProperties(glossaryTermProperties);
        requestBody.setInitialStatus(initialStatus);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/{2}/terms/new-controlled";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  glossaryGUID);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a glossary term using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the glossary term
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createGlossaryTermFromTemplate(String                       userId,
                                                 String                       assetManagerGUID,
                                                 String                       assetManagerName,
                                                 String                       templateGUID,
                                                 ExternalIdentifierProperties externalIdentifierProperties,
                                                 TemplateProperties           templateProperties) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName                  = "createGlossaryTermFromTemplate";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "templateProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        TemplateRequestBody requestBody = new TemplateRequestBody();
        requestBody.setElementProperties(templateProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/from-template/{1}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  templateGUID);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a glossary term.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param glossaryTermProperties new properties for the glossary term
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateGlossaryTerm(String                 userId,
                                   String                 assetManagerGUID,
                                   String                 assetManagerName,
                                   String                 glossaryTermGUID,
                                   String                 glossaryTermExternalIdentifier,
                                   boolean                isMergeUpdate,
                                   GlossaryTermProperties glossaryTermProperties,
                                   Date                   effectiveTime,
                                   boolean                forLineage,
                                   boolean                forDuplicateProcessing) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName                  = "updateGlossaryTerm";
        final String glossaryGUIDParameterName   = "glossaryTermGUID";
        final String propertiesParameterName     = "glossaryTermProperties";
        final String qualifiedNameParameterName  = "glossaryTermProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(glossaryTermProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(glossaryTermProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        GlossaryTermRequestBody requestBody = new GlossaryTermRequestBody();
        requestBody.setElementProperties(glossaryTermProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   glossaryTermExternalIdentifier,
                                                                                   methodName));
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}?isMergeUpdate={3}&forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        glossaryTermGUID,
                                        isMergeUpdate,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Update the status of the metadata element representing a glossary term.  This is only valid on
     * a controlled glossary term.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param glossaryTermStatus new properties for the glossary term
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateGlossaryTermStatus(String             userId,
                                         String             assetManagerGUID,
                                         String             assetManagerName,
                                         String             glossaryTermGUID,
                                         String             glossaryTermExternalIdentifier,
                                         GlossaryTermStatus glossaryTermStatus,
                                         Date               effectiveTime,
                                         boolean            forLineage,
                                         boolean            forDuplicateProcessing) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName                  = "updateGlossaryTermStatus";
        final String glossaryGUIDParameterName   = "glossaryTermGUID";
        final String propertiesParameterName     = "glossaryTermStatus";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(glossaryTermStatus, propertiesParameterName, methodName);

        GlossaryTermStatusRequestBody requestBody = new GlossaryTermStatusRequestBody();
        requestBody.setGlossaryTermStatus(glossaryTermStatus);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   glossaryTermExternalIdentifier,
                                                                                   methodName));
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}/status?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        glossaryTermGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Link a term to a category.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the glossary category
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param categorizationProperties properties for the categorization relationship
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupTermCategory(String                     userId,
                                  String                     assetManagerGUID,
                                  String                     assetManagerName,
                                  String                     glossaryCategoryGUID,
                                  String                     glossaryTermGUID,
                                  GlossaryTermCategorization categorizationProperties,
                                  Date                       effectiveTime,
                                  boolean                    forLineage,
                                  boolean                    forDuplicateProcessing) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName                      = "setupTermCategory";
        final String glossaryParentGUIDParameterName = "glossaryCategoryGUID";
        final String glossaryChildGUIDParameterName  = "glossaryTermGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryCategoryGUID, glossaryParentGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryChildGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/categories/{2}/terms/{3}?forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getRelationshipRequestBody(assetManagerGUID, assetManagerName, effectiveTime, categorizationProperties),
                                        serverName,
                                        userId,
                                        glossaryCategoryGUID,
                                        glossaryTermGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Unlink a term from a category.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the glossary category
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearTermCategory(String  userId,
                                  String  assetManagerGUID,
                                  String  assetManagerName,
                                  String  glossaryCategoryGUID,
                                  String  glossaryTermGUID,
                                  Date    effectiveTime,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName                      = "clearTermCategory";
        final String glossaryParentGUIDParameterName = "glossaryCategoryGUID";
        final String glossaryChildGUIDParameterName  = "glossaryTermGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryCategoryGUID, glossaryParentGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryChildGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/categories/{2}/terms/{3}/remove?forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        glossaryCategoryGUID,
                                        glossaryTermGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Link two terms together using a specialist relationship.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param relationshipsProperties properties for the relationship
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupTermRelationship(String                   userId,
                                      String                   assetManagerGUID,
                                      String                   assetManagerName,
                                      String                   relationshipTypeName,
                                      String                   glossaryTermOneGUID,
                                      String                   glossaryTermTwoGUID,
                                      GlossaryTermRelationship relationshipsProperties,
                                      Date                     effectiveTime,
                                      boolean                  forLineage,
                                      boolean                  forDuplicateProcessing) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName                      = "setupTermRelationship";
        final String glossaryParentGUIDParameterName = "glossaryTermOneGUID";
        final String glossaryChildGUIDParameterName  = "glossaryTermTwoGUID";
        final String glossaryTypeParameterName       = "relationshipTypeName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermOneGUID, glossaryParentGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(glossaryTermTwoGUID, glossaryChildGUIDParameterName, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, glossaryTypeParameterName, methodName);

        TermRelationshipRequestBody requestBody = new TermRelationshipRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setProperties(relationshipsProperties);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}/relationships/{3}/terms/{4}?forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        glossaryTermOneGUID,
                                        relationshipTypeName,
                                        glossaryTermTwoGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Update the relationship properties for the two terms.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param relationshipsProperties properties for the relationship
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateTermRelationship(String                   userId,
                                       String                   assetManagerGUID,
                                       String                   assetManagerName,
                                       String                   relationshipTypeName,
                                       String                   glossaryTermOneGUID,
                                       String                   glossaryTermTwoGUID,
                                       GlossaryTermRelationship relationshipsProperties,
                                       Date                     effectiveTime,
                                       boolean                  forLineage,
                                       boolean                  forDuplicateProcessing) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName                      = "updateTermRelationship";
        final String glossaryParentGUIDParameterName = "glossaryTermOneGUID";
        final String glossaryChildGUIDParameterName  = "glossaryTermTwoGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermOneGUID, glossaryParentGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(glossaryTermTwoGUID, glossaryChildGUIDParameterName, methodName);

        TermRelationshipRequestBody requestBody = new TermRelationshipRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setProperties(relationshipsProperties);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}/relationships/{3}/terms/{4}/update?forLineage={5}&forDuplicateProcessing={6}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        glossaryTermOneGUID,
                                        relationshipTypeName,
                                        glossaryTermTwoGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the relationship between two terms.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearTermRelationship(String  userId,
                                      String  assetManagerGUID,
                                      String  assetManagerName,
                                      String  relationshipTypeName,
                                      String  glossaryTermOneGUID,
                                      String  glossaryTermTwoGUID,
                                      Date    effectiveTime,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName                      = "clearTermRelationship";
        final String glossaryParentGUIDParameterName = "glossaryTermOneGUID";
        final String glossaryChildGUIDParameterName  = "glossaryTermTwoGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermOneGUID, glossaryParentGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(glossaryTermTwoGUID, glossaryChildGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}/relationships/{3}/terms/{4}/remove?forLineage={5}&forDuplicateProcessing={6}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        glossaryTermOneGUID,
                                        relationshipTypeName,
                                        glossaryTermTwoGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Classify the glossary term to indicate that it describes an abstract concept.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setTermAsAbstractConcept(String  userId,
                                         String  assetManagerGUID,
                                         String  assetManagerName,
                                         String  glossaryTermGUID,
                                         String  glossaryTermExternalIdentifier,
                                         Date    effectiveTime,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "setTermAsAbstractConcept";
        final String glossaryGUIDParameterName = "glossaryTermGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}/is-abstract-concept?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, glossaryTermExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        glossaryTermGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the abstract concept designation from the glossary term.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearTermAsAbstractConcept(String  userId,
                                           String  assetManagerGUID,
                                           String  assetManagerName,
                                           String  glossaryTermGUID,
                                           String  glossaryTermExternalIdentifier,
                                           Date    effectiveTime,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "clearTermAsAbstractConcept";
        final String glossaryGUIDParameterName = "glossaryTermGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}/is-abstract-concept/remove?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, glossaryTermExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        glossaryTermGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Classify the glossary term to indicate that it describes a data value.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setTermAsDataValue(String  userId,
                                   String  assetManagerGUID,
                                   String  assetManagerName,
                                   String  glossaryTermGUID,
                                   String  glossaryTermExternalIdentifier,
                                   Date    effectiveTime,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName = "setTermAsDataValue";
        final String glossaryGUIDParameterName = "glossaryTermGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}/is-data-value?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, glossaryTermExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        glossaryTermGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the data value designation from the glossary term.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearTermAsDataValue(String  userId,
                                     String  assetManagerGUID,
                                     String  assetManagerName,
                                     String  glossaryTermGUID,
                                     String  glossaryTermExternalIdentifier,
                                     Date    effectiveTime,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName = "clearTermAsDataValue";
        final String glossaryGUIDParameterName = "glossaryTermGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}/is-data-value/remove?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, glossaryTermExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        glossaryTermGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Classify the glossary term to indicate that it describes a data value.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param activityType type of activity
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setTermAsActivity(String                   userId,
                                  String                   assetManagerGUID,
                                  String                   assetManagerName,
                                  String                   glossaryTermGUID,
                                  String                   glossaryTermExternalIdentifier,
                                  GlossaryTermActivityType activityType,
                                  Date                     effectiveTime,
                                  boolean                  forLineage,
                                  boolean                  forDuplicateProcessing) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName = "setTermAsActivity";
        final String glossaryGUIDParameterName = "glossaryTermGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryGUIDParameterName, methodName);

        ActivityTermClassificationRequestBody requestBody = new ActivityTermClassificationRequestBody();
        requestBody.setActivityType(activityType);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   glossaryTermExternalIdentifier,
                                                                                   methodName));
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}/is-activity?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        glossaryTermGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the activity designation from the glossary term.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearTermAsActivity(String  userId,
                                    String  assetManagerGUID,
                                    String  assetManagerName,
                                    String  glossaryTermGUID,
                                    String  glossaryTermExternalIdentifier,
                                    Date    effectiveTime,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "clearTermAsActivity";
        final String glossaryGUIDParameterName = "glossaryTermGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}/is-activity/remove?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, glossaryTermExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        glossaryTermGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Classify the glossary term to indicate that it describes a context.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param contextDefinition more details of the context
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setTermAsContext(String                        userId,
                                 String                        assetManagerGUID,
                                 String                        assetManagerName,
                                 String                        glossaryTermGUID,
                                 String                        glossaryTermExternalIdentifier,
                                 GlossaryTermContextDefinition contextDefinition,
                                 Date                          effectiveTime,
                                 boolean                       forLineage,
                                 boolean                       forDuplicateProcessing) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName = "setTermAsContext";
        final String glossaryGUIDParameterName = "glossaryTermGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryGUIDParameterName, methodName);

        ContextDefinitionClassificationRequestBody requestBody = new ContextDefinitionClassificationRequestBody();
        requestBody.setContextDefinition(contextDefinition);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   glossaryTermExternalIdentifier,
                                                                                   methodName));
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}/is-context-definition?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        glossaryTermGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the context definition designation from the glossary term.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearTermAsContext(String  userId,
                                   String  assetManagerGUID,
                                   String  assetManagerName,
                                   String  glossaryTermGUID,
                                   String  glossaryTermExternalIdentifier,
                                   Date    effectiveTime,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName = "clearTermAsContext";
        final String glossaryGUIDParameterName = "glossaryTermGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}/is-context-definition/remove?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, glossaryTermExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        glossaryTermGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Classify the glossary term to indicate that it describes a spine object.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setTermAsSpineObject(String  userId,
                                     String  assetManagerGUID,
                                     String  assetManagerName,
                                     String  glossaryTermGUID,
                                     String  glossaryTermExternalIdentifier,
                                     Date    effectiveTime,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName = "setTermAsSpineObject";
        final String glossaryGUIDParameterName = "glossaryTermGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}/is-spine-object?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, glossaryTermExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        glossaryTermGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the spine object designation from the glossary term.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearTermAsSpineObject(String  userId,
                                       String  assetManagerGUID,
                                       String  assetManagerName,
                                       String  glossaryTermGUID,
                                       String  glossaryTermExternalIdentifier,
                                       Date    effectiveTime,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "clearTermAsSpineObject";
        final String glossaryGUIDParameterName = "glossaryTermGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}/is-spine-object/remove?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, glossaryTermExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        glossaryTermGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Classify the glossary term to indicate that it describes a spine attribute.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setTermAsSpineAttribute(String  userId,
                                        String  assetManagerGUID,
                                        String  assetManagerName,
                                        String  glossaryTermGUID,
                                        String  glossaryTermExternalIdentifier,
                                        Date    effectiveTime,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "setTermAsSpineAttribute";
        final String glossaryGUIDParameterName = "glossaryTermGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}/is-spine-attribute?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, glossaryTermExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        glossaryTermGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the spine attribute designation from the glossary term.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearTermAsSpineAttribute(String  userId,
                                          String  assetManagerGUID,
                                          String  assetManagerName,
                                          String  glossaryTermGUID,
                                          String  glossaryTermExternalIdentifier,
                                          Date    effectiveTime,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "clearTermAsSpineAttribute";
        final String glossaryGUIDParameterName = "glossaryTermGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}/is-spine-attribute/remove?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, glossaryTermExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        glossaryTermGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Classify the glossary term to indicate that it describes an object identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setTermAsObjectIdentifier(String  userId,
                                          String  assetManagerGUID,
                                          String  assetManagerName,
                                          String  glossaryTermGUID,
                                          String  glossaryTermExternalIdentifier,
                                          Date    effectiveTime,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "setTermAsObjectIdentifier";
        final String glossaryGUIDParameterName = "glossaryTermGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}/is-object-identifier?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, glossaryTermExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        glossaryTermGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the object identifier designation from the glossary term.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearTermAsObjectIdentifier(String  userId,
                                            String  assetManagerGUID,
                                            String  assetManagerName,
                                            String  glossaryTermGUID,
                                            String  glossaryTermExternalIdentifier,
                                            Date    effectiveTime,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName = "clearTermAsObjectIdentifier";
        final String glossaryGUIDParameterName = "glossaryTermGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}/is-object-identifier/remove&forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, glossaryTermExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        glossaryTermGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the metadata element representing a glossary term.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to remove
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeGlossaryTerm(String  userId,
                                   String  assetManagerGUID,
                                   String  assetManagerName,
                                   String  glossaryTermGUID,
                                   String  glossaryTermExternalIdentifier,
                                   Date    effectiveTime,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName                  = "removeGlossaryTerm";
        final String glossaryGUIDParameterName   = "glossaryTermGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}/remove?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, glossaryTermExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        glossaryTermGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Retrieve the list of glossary term metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<GlossaryTermElement>   findGlossaryTerms(String  userId,
                                                         String  assetManagerGUID,
                                                         String  assetManagerName,
                                                         String  searchString,
                                                         int     startFrom,
                                                         int     pageSize,
                                                         Date    effectiveTime,
                                                         boolean forLineage,
                                                         boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName                = "findGlossaryTerms";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/by-search-string?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        GlossaryTermElementsResponse restResult = restClient.callGlossaryTermsPostRESTCall(methodName,
                                                                                           urlTemplate,
                                                                                           requestBody,
                                                                                           serverName,
                                                                                           userId,
                                                                                           startFrom,
                                                                                           validatedPageSize,
                                                                                           forLineage,
                                                                                           forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of glossary terms associated with a glossary.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the glossary of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<GlossaryTermElement>    getTermsForGlossary(String  userId,
                                                            String  assetManagerGUID,
                                                            String  assetManagerName,
                                                            String  glossaryGUID,
                                                            int     startFrom,
                                                            int     pageSize,
                                                            Date    effectiveTime,
                                                            boolean forLineage,
                                                            boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName        = "getTermsForGlossary";
        final String guidParameterName = "glossaryGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, guidParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/{2}/terms/retrieve?startFrom={3}&pageSize={4}&forLineage={5}&forDuplicateProcessing={6}";

        GlossaryTermElementsResponse restResult = restClient.callGlossaryTermsPostRESTCall(methodName,
                                                                                           urlTemplate,
                                                                                           getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                           serverName,
                                                                                           userId,
                                                                                           glossaryGUID,
                                                                                           startFrom,
                                                                                           validatedPageSize,
                                                                                           forLineage,
                                                                                           forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of glossary terms associated with a glossary category.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the glossary category of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<GlossaryTermElement>    getTermsForGlossaryCategory(String  userId,
                                                                    String  assetManagerGUID,
                                                                    String  assetManagerName,
                                                                    String  glossaryCategoryGUID,
                                                                    int     startFrom,
                                                                    int     pageSize,
                                                                    Date    effectiveTime,
                                                                    boolean forLineage,
                                                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String methodName        = "getTermsForGlossaryCategory";
        final String guidParameterName = "glossaryCategoryGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryCategoryGUID, guidParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/categories/{2}/terms/retrieve?startFrom={3}&pageSize={4}&forLineage={5}&forDuplicateProcessing={6}";

        GlossaryTermElementsResponse restResult = restClient.callGlossaryTermsPostRESTCall(methodName,
                                                                                           urlTemplate,
                                                                                           getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                           serverName,
                                                                                           userId,
                                                                                           glossaryCategoryGUID,
                                                                                           startFrom,
                                                                                           validatedPageSize,
                                                                                           forLineage,
                                                                                           forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of glossary term metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<GlossaryTermElement>   getGlossaryTermsByName(String  userId,
                                                              String  assetManagerGUID,
                                                              String  assetManagerName,
                                                              String  name,
                                                              int     startFrom,
                                                              int     pageSize,
                                                              Date    effectiveTime,
                                                              boolean forLineage,
                                                              boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName        = "getGlossaryTermsByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/by-name?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        GlossaryTermElementsResponse restResult = restClient.callGlossaryTermsPostRESTCall(methodName,
                                                                                           urlTemplate,
                                                                                           requestBody,
                                                                                           serverName,
                                                                                           userId,
                                                                                           startFrom,
                                                                                           validatedPageSize,
                                                                                           forLineage,
                                                                                           forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the glossary term metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the requested metadata element
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public GlossaryTermElement getGlossaryTermByGUID(String  userId,
                                                     String  assetManagerGUID,
                                                     String  assetManagerName,
                                                     String  glossaryTermGUID,
                                                     Date    effectiveTime,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "getGlossaryTermByGUID";
        final String guidParameterName = "glossaryTermGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}/retrieve?forLineage={3}&forDuplicateProcessing={4}";

        GlossaryTermElementResponse restResult = restClient.callGlossaryTermPostRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                         serverName,
                                                                                         userId,
                                                                                         glossaryTermGUID,
                                                                                         forLineage,
                                                                                         forDuplicateProcessing);

        return restResult.getElement();
    }


    /* =========================================================================================
     * Support for linkage to external glossary resources.  These glossary resources are not
     * stored as metadata - they could be web pages, ontologies or some other format.
     * It is possible that the external glossary resource may have been generated by the metadata
     * representation or vice versa.
     */


    /**
     * Create a link to an external glossary resource.  This is associated with a glossary to show that they have equivalent content.
     * It is possible that this resource was generated from the glossary content or was the source for it.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param linkProperties properties of the link
     *
     * @return unique identifier of the external reference
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createExternalGlossaryLink(String                         userId,
                                             String                         assetManagerGUID,
                                             String                         assetManagerName,
                                             ExternalGlossaryLinkProperties linkProperties) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName                  = "createExternalGlossaryLink";
        final String propertiesParameterName     = "linkProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(linkProperties, propertiesParameterName, methodName);

        ExternalGlossaryLinkRequestBody requestBody = new ExternalGlossaryLinkRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setElementProperties(linkProperties);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/external-links";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Update the properties of a reference to an external glossary resource.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param linkProperties properties of the link
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateExternalGlossaryLink(String                         userId,
                                           String                         assetManagerGUID,
                                           String                         assetManagerName,
                                           String                         externalLinkGUID,
                                           boolean                        isMergeUpdate,
                                           ExternalGlossaryLinkProperties linkProperties,
                                           Date                           effectiveTime,
                                           boolean                        forLineage,
                                           boolean                        forDuplicateProcessing) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName                  = "updateExternalGlossaryLink";
        final String guidParameterName           = "externalLinkGUID";
        final String propertiesParameterName     = "linkProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalLinkGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(linkProperties, propertiesParameterName, methodName);

        ExternalGlossaryLinkRequestBody requestBody = new ExternalGlossaryLinkRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setElementProperties(linkProperties);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/external-links/{2}?isMergeUpdate={3}&forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        externalLinkGUID,
                                        isMergeUpdate,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove information about a link to an external glossary resource (and the relationships that attached it to the glossaries).
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeExternalGlossaryLink(String  userId,
                                           String  assetManagerGUID,
                                           String  assetManagerName,
                                           String  externalLinkGUID,
                                           Date    effectiveTime,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName        = "removeExternalGlossaryLink";
        final String guidParameterName = "externalLinkGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalLinkGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/external-links/{2}/remove&forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        externalLinkGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Connect a glossary to a reference to an external glossary resource.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryGUID unique identifier of the metadata element to attach
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void attachExternalLinkToGlossary(String  userId,
                                             String  assetManagerGUID,
                                             String  assetManagerName,
                                             String  externalLinkGUID,
                                             String  glossaryGUID,
                                             Date    effectiveTime,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName                = "attachExternalLinkToGlossary";
        final String guidParameterName         = "externalLinkGUID";
        final String glossaryGUIDParameterName = "glossaryGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(externalLinkGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/{2}/external-links/{3}?forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        glossaryGUID,
                                        externalLinkGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Disconnect a glossary from a reference to an external glossary resource.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void detachExternalLinkFromGlossary(String  userId,
                                               String  assetManagerGUID,
                                               String  assetManagerName,
                                               String  externalLinkGUID,
                                               String  glossaryGUID,
                                               Date    effectiveTime,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName                = "detachExternalLinkFromGlossary";
        final String guidParameterName         = "externalLinkGUID";
        final String glossaryGUIDParameterName = "glossaryGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(externalLinkGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/{2}/external-links/{3}/remove?forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        glossaryGUID,
                                        externalLinkGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Retrieve the list of links to external glossary resources attached to a glossary.
     *
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element for the glossary of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of attached links to external glossary resources
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ExternalGlossaryLinkElement> getExternalLinksForGlossary(String  userId,
                                                                         String  assetManagerGUID,
                                                                         String  assetManagerName,
                                                                         String  glossaryGUID,
                                                                         int     startFrom,
                                                                         int     pageSize,
                                                                         Date    effectiveTime,
                                                                         boolean forLineage,
                                                                         boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        final String methodName        = "getExternalLinksForGlossary";
        final String guidParameterName = "glossaryGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, guidParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/{2}/external-links/retrieve?startFrom={3}&pageSize={4}&forLineage={5}&forDuplicateProcessing={6}";

        ExternalGlossaryLinkElementsResponse restResult = restClient.callExternalGlossaryLinksPostRESTCall(methodName,
                                                                                                           urlTemplate,
                                                                                                           getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                                           serverName,
                                                                                                           userId,
                                                                                                           glossaryGUID,
                                                                                                           startFrom,
                                                                                                           validatedPageSize,
                                                                                                           forLineage,
                                                                                                           forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Return the glossaries connected to an external glossary source.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the metadata element for the external glossary link of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of glossaries
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<GlossaryElement> getGlossariesForExternalLink(String  userId,
                                                              String  assetManagerGUID,
                                                              String  assetManagerName,
                                                              String  externalLinkGUID,
                                                              int     startFrom,
                                                              int     pageSize,
                                                              Date    effectiveTime,
                                                              boolean forLineage,
                                                              boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName        = "getGlossariesForExternalLink";
        final String guidParameterName = "externalLinkGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalLinkGUID, guidParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/by-external-links/{2}/retrieve?startFrom={3}&pageSize={4}&forLineage={5}&forDuplicateProcessing={6}";

        GlossaryElementsResponse restResult = restClient.callGlossariesPostRESTCall(methodName,
                                                                                    urlTemplate,
                                                                                    getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                    serverName,
                                                                                    userId,
                                                                                    externalLinkGUID,
                                                                                    startFrom,
                                                                                    validatedPageSize,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Create a link to an external glossary category resource.  This is associated with a category to show that they have equivalent content.
     * It is possible that this resource was generated from the glossary content or was the source for it.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryCategoryGUID unique identifier for the glossary category
     * @param linkProperties properties of the link
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void attachExternalCategoryLink(String                                userId,
                                           String                                assetManagerGUID,
                                           String                                assetManagerName,
                                           String                                externalLinkGUID,
                                           String                                glossaryCategoryGUID,
                                           ExternalGlossaryElementLinkProperties linkProperties,
                                           Date                                  effectiveTime,
                                           boolean                               forLineage,
                                           boolean                               forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        final String methodName                = "attachExternalCategoryLink";
        final String guidParameterName         = "externalLinkGUID";
        final String glossaryGUIDParameterName = "glossaryCategoryGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryCategoryGUID, glossaryGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(externalLinkGUID, guidParameterName, methodName);

        ExternalGlossaryElementLinkRequestBody requestBody = new ExternalGlossaryElementLinkRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setElementProperties(linkProperties);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/categories/{2}/external-links/{3}?forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        glossaryCategoryGUID,
                                        externalLinkGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the link to an external glossary category resource.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryCategoryGUID unique identifier for the glossary category
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void detachExternalCategoryLink(String  userId,
                                           String  assetManagerGUID,
                                           String  assetManagerName,
                                           String  externalLinkGUID,
                                           String  glossaryCategoryGUID,
                                           Date    effectiveTime,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName                = "detachExternalCategoryLink";
        final String guidParameterName         = "externalLinkGUID";
        final String glossaryGUIDParameterName = "glossaryCategoryGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryCategoryGUID, glossaryGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(externalLinkGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/categories/{2}/external-links/{3}/remove?forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        glossaryCategoryGUID,
                                        externalLinkGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Create a link to an external glossary term resource.  This is associated with a term to show that they have equivalent content.
     * It is possible that this resource was generated from the glossary content or was the source for it.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryTermGUID unique identifier for the glossary category
     * @param linkProperties properties of the link
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void attachExternalTermLink(String                                userId,
                                       String                                assetManagerGUID,
                                       String                                assetManagerName,
                                       String                                externalLinkGUID,
                                       String                                glossaryTermGUID,
                                       ExternalGlossaryElementLinkProperties linkProperties,
                                       Date                                  effectiveTime,
                                       boolean                               forLineage,
                                       boolean                               forDuplicateProcessing) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        final String methodName                = "attachExternalTermLink";
        final String guidParameterName         = "externalLinkGUID";
        final String glossaryGUIDParameterName = "glossaryTermGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(externalLinkGUID, guidParameterName, methodName);

        ExternalGlossaryElementLinkRequestBody requestBody = new ExternalGlossaryElementLinkRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setElementProperties(linkProperties);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}/external-links/{3}?forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        glossaryTermGUID,
                                        externalLinkGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the link to an external glossary term resource.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryTermGUID unique identifier for the glossary category
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void detachExternalTermLink(String  userId,
                                       String  assetManagerGUID,
                                       String  assetManagerName,
                                       String  externalLinkGUID,
                                       String  glossaryTermGUID,
                                       Date    effectiveTime,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName                = "detachExternalTermLink";
        final String guidParameterName         = "externalLinkGUID";
        final String glossaryGUIDParameterName = "glossaryTermGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(externalLinkGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/{2}/external-links/{3}/remove?forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        glossaryTermGUID,
                                        externalLinkGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }
}
