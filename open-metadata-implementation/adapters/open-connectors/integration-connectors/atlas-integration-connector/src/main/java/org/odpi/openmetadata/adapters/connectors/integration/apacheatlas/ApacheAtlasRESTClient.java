/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas;


import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.ffdc.ApacheAtlasAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.ffdc.ApacheAtlasErrorCode;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.ffdc.NameConflictException;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties.AtlasGlossaryCategoryElement;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties.AtlasGlossaryElement;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties.AtlasGlossaryTermElement;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.adapters.connectors.restclients.factory.RESTClientFactory;
import org.odpi.openmetadata.adapters.connectors.restclients.spring.SpringRESTClientConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.springframework.core.ParameterizedTypeReference;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * RESTClient is responsible for issuing calls to the OMAS REST APIs.
 */
public class ApacheAtlasRESTClient
{
    protected String   serverName;             /* Initialized in constructor */
    protected String   baseURL;                /* Initialized in constructor */
    protected AuditLog auditLog;               /* Initialized in constructor */


    private final RESTClientConnector clientConnector;    /* Initialized in constructor */


    /**
     * Constructor for simple userId and password authentication with audit log.
     *
     * @param connectorName name of the connector
     * @param serverName name of the server
     * @param baseURL URL root of the server platform where the OMAG Server is running.
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    protected ApacheAtlasRESTClient(String   connectorName,
                                    String   serverName,
                                    String   baseURL,
                                    String   userId,
                                    String   password,
                                    AuditLog auditLog) throws InvalidParameterException
    {
        final String  methodName = "RESTClient(userId and password)";

        this.serverName = serverName;
        this.baseURL = baseURL;
        this.auditLog = auditLog;

        RESTClientFactory  factory = new RESTClientFactory(serverName,
                                                           baseURL,
                                                           userId,
                                                           password);

        try
        {
            this.clientConnector = factory.getClientConnector();
        }
        catch (Exception  error)
        {
            throw new InvalidParameterException(ApacheAtlasErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName, error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error,
                                                "baseURL, serverName, userId or password");
        }
    }


    /**
     * Return a glossary based on the paging count.
     *
     * @param glossaryCount position of the glossary in the paging list.
     * @return glossary or null
     * @throws PropertyServerException problem with the request
     */
    @SuppressWarnings(value = "unchecked")
    AtlasGlossaryElement getAtlasGlossary(int glossaryCount) throws PropertyServerException
    {
        final String methodName = "getAtlasGlossary(glossaryCount)";
        final String url = baseURL + "/api/atlas/v2/glossary?limit=1&offset=" + glossaryCount + "&sort=ASC";


        List<LinkedHashMap<String, Object>> glossaryElements = (ArrayList<LinkedHashMap<String, Object>>)this.callGetRESTCallNoParams(methodName, ArrayList.class, url);

        if ((glossaryElements != null) && (! glossaryElements.isEmpty()))
        {
            LinkedHashMap<String, Object> requestedGlossary = glossaryElements.get(0);

            return this.getAtlasGlossary(requestedGlossary.get("guid").toString());
        }

        return null;
    }


    /**
     * Return a glossary based on its guid.
     *
     * @param glossaryGUID unique identifier of the glossary.
     * @return glossary or null
     * @throws PropertyServerException problem with the request
     */
    AtlasGlossaryElement getAtlasGlossary(String glossaryGUID) throws PropertyServerException
    {
        final String methodName = "getAtlasGlossary(glossaryGUID)";
        final String url = baseURL + "/api/atlas/v2/glossary/" + glossaryGUID;

        return this.callGetRESTCallNoParams(methodName, AtlasGlossaryElement.class, url);
    }


    /**
     * Create a new empty glossary.
     *
     * @param glossary glossary to create
     * @return glossaryGUID
     * @throws PropertyServerException problem with the request
     */
    String createAtlasGlossary(AtlasGlossaryElement glossary) throws PropertyServerException
    {
        final String methodName = "createAtlasGlossary(glossary)";
        final String url = baseURL + "/api/atlas/v2/glossary";

        AtlasGlossaryElement newGlossary = this.callPostRESTCallNoParams(methodName, AtlasGlossaryElement.class, url, glossary);

        if (newGlossary != null)
        {
            return newGlossary.getGuid();
        }

        return null;
    }


    /**
     * Save a glossary with all of its links to terms and categories.
     *
     * @param glossary glossary to create/update
     * @return glossary or null
     * @throws PropertyServerException problem with the request
     */
    AtlasGlossaryElement saveAtlasGlossary(AtlasGlossaryElement glossary) throws PropertyServerException
    {
        final String methodName = "saveAtlasGlossary(glossary)";
        final String url = baseURL + "/api/atlas/v2/glossary/{0}";

        return this.callPutRESTCall(methodName, AtlasGlossaryElement.class, url, glossary, glossary.getGuid());
    }



    /**
     * Delete a glossary with all of its links to terms and categories.
     *
     * @param glossary glossary to delete
     * @throws PropertyServerException problem with the request
     */
    void deleteAtlasGlossary(AtlasGlossaryElement glossary) throws PropertyServerException
    {
        final String methodName = "deleteAtlasGlossary()";
        final String url = baseURL + "/api/atlas/v2/glossary/" + glossary.getGuid();

        this.callDeleteRESTCall(methodName, url);
    }


    /**
     * Return a glossary term based on its guid.
     *
     * @param glossaryTermGUID unique identifier of the glossary term.
     * @return glossary or null
     * @throws PropertyServerException problem with the request
     */
    AtlasGlossaryTermElement getAtlasGlossaryTerm(String glossaryTermGUID) throws PropertyServerException
    {
        final String methodName = "getAtlasGlossaryTerm(glossaryTermGUID)";
        final String url = baseURL + "/api/atlas/v2/glossary/term/" + glossaryTermGUID;

        return this.callGetRESTCallNoParams(methodName, AtlasGlossaryTermElement.class, url);
    }


    /**
     * Create a new term linked to its glossary, other terms and categories.
     *
     * @param term term to create
     * @return glossaryTermGUID
     * @throws PropertyServerException problem with the request
     * @throws NameConflictException the name supplied clashes with another term
     */
    String createAtlasGlossaryTerm(AtlasGlossaryTermElement term) throws PropertyServerException,
                                                                         NameConflictException
    {
        final String methodName = "createAtlasGlossaryTerm()";
        final String url = baseURL + "/api/atlas/v2/glossary/term";

        AtlasGlossaryTermElement newTerm;


        try
        {
            newTerm = this.callPostRESTCallNameConflict(methodName, AtlasGlossaryTermElement.class, url, term);
        }
        catch (PropertyServerException error)
        {
            if (error.getMessage().contains("org.springframework.web.client.HttpClientErrorException$Conflict"))
            {
                throw new NameConflictException(ApacheAtlasErrorCode.TERM_ALREADY_EXISTS.getMessageDefinition(term.getName()),
                                                this.getClass().getName(),
                                                methodName,
                                                "name",
                                                error);
            }

            throw error;
        }

        if (newTerm != null)
        {
            return newTerm.getGuid();
        }

        return null;
    }


    /**
     * Save a term with all of its links to other terms and categories.
     *
     * @param term term to create/update
     * @return term or null
     * @throws PropertyServerException problem with the request
     */
    AtlasGlossaryTermElement saveAtlasGlossaryTerm(AtlasGlossaryTermElement term) throws PropertyServerException
    {
        final String methodName = "saveAtlasGlossaryTerm()";
        final String url = baseURL + "/api/atlas/v2/glossary/term/{0}";

        return this.callPutRESTCall(methodName, AtlasGlossaryTermElement.class, url, term, term.getGuid());
    }


    /**
     * Delete a term with all of its links to other terms and categories.
     *
     * @param term term to delete
     * @throws PropertyServerException problem with the request
     */
    void deleteAtlasGlossaryTerm(AtlasGlossaryTermElement term) throws PropertyServerException
    {
        final String methodName = "deleteAtlasGlossaryTerm()";
        final String url = baseURL + "/api/atlas/v2/glossary/term/" + term.getGuid();

        this.callDeleteRESTCall(methodName, url);
    }


    /**
     * Return a category based on its guid.
     *
     * @param glossaryCategoryGUID unique identifier of the category.
     * @return glossary or null
     * @throws PropertyServerException problem with the request
     */
    AtlasGlossaryCategoryElement getAtlasGlossaryCategory(String glossaryCategoryGUID) throws PropertyServerException
    {
        final String methodName = "getAtlasGlossaryCategory(glossaryCategoryGUID)";
        final String url = baseURL + "/api/atlas/v2/glossary/category/" + glossaryCategoryGUID;

        return this.callGetRESTCallNoParams(methodName, AtlasGlossaryCategoryElement.class, url);
    }


    /**
     * Create a new category linked to its glossary and potentially other categories and terms.
     *
     * @param category category to create
     * @return glossaryCategoryGUID
     * @throws PropertyServerException problem with the request
     * @throws NameConflictException the name supplied clashes with another term
     */
    String createAtlasGlossaryCategory(AtlasGlossaryCategoryElement category) throws PropertyServerException,
                                                                                     NameConflictException
    {
        final String methodName = "createAtlasGlossaryCategory(category)";
        final String url = baseURL + "/api/atlas/v2/glossary/category";

        AtlasGlossaryCategoryElement newGlossaryCategory;


        try
        {
            newGlossaryCategory = this.callPostRESTCallNameConflict(methodName, AtlasGlossaryCategoryElement.class, url, category);
        }
        catch (PropertyServerException error)
        {
            if (error.getMessage().contains("org.springframework.web.client.HttpClientErrorException$Conflict"))
            {
                throw new NameConflictException(ApacheAtlasErrorCode.CATEGORY_ALREADY_EXISTS.getMessageDefinition(category.getName()),
                                                this.getClass().getName(),
                                                methodName,
                                                "name",
                                                error);
            }

            throw error;
        }
        if (newGlossaryCategory != null)
        {
            return newGlossaryCategory.getGuid();
        }

        return null;
    }


    /**
     * Save a category with all of its links to terms and categories.
     *
     * @param category category to create/update
     * @return glossary category or null
     * @throws PropertyServerException problem with the request
     */
    AtlasGlossaryCategoryElement saveAtlasGlossaryCategory(AtlasGlossaryCategoryElement category) throws PropertyServerException
    {
        final String methodName = "saveAtlasGlossaryCategory(glossaryGUID)";
        final String url = baseURL + "/api/atlas/v2/glossary/category/{0}";

        return this.callPutRESTCall(methodName, AtlasGlossaryCategoryElement.class, url, category, category.getGuid());
    }


    /**
     * Delete a category with all of its links to terms and categories.
     *
     * @param category category to create/update
     * @throws PropertyServerException problem with the request
     */
    void deleteAtlasGlossaryCategory(AtlasGlossaryCategoryElement category) throws PropertyServerException
    {
        final String methodName = "deleteAtlasGlossaryCategory(glossaryGUID)";
        final String url = baseURL + "/api/atlas/v2/glossary/category/" + category.getGuid();

        this.callDeleteRESTCall(methodName, url);
    }


    /**
     * Issue a GET REST call that returns a response object.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected <T> T callGetRESTCallNoParams(String    methodName,
                                            Class<T>  returnClass,
                                            String    urlTemplate) throws PropertyServerException
    {
        try
        {
            return clientConnector.callGetRESTCall(methodName, returnClass, urlTemplate);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, error);
        }

        return null;
    }


    /**
     * Issue a GET REST call that returns a response object.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected  <T> T callGetRESTCall(String    methodName,
                                     Class<T>  returnClass,
                                     String    urlTemplate,
                                     Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callGetRESTCall(methodName, returnClass, urlTemplate, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, error);
        }

        return null;
    }

    /**
     * Issue a GET REST call that returns a response object. It's working only with {@link SpringRESTClientConnector}
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param responseType class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected  <T> T callGetRESTCall(String                        methodName,
                                     ParameterizedTypeReference<T> responseType,
                                     String                        urlTemplate,
                                     Object...                     params) throws PropertyServerException
    {
        try
        {
            SpringRESTClientConnector clientConnector = (SpringRESTClientConnector) this.clientConnector;
            return clientConnector.callGetRESTCall(methodName, responseType, urlTemplate, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, error);
        }

        return null;
    }


    /**
     * Issue a POST REST call that returns a response object.  This is typically a create, update, or find with
     * complex parameters.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected <T> T callPostRESTCallNoParams(String    methodName,
                                             Class<T>  returnClass,
                                             String    urlTemplate,
                                             Object    requestBody) throws PropertyServerException
    {
        try
        {
            return clientConnector.callPostRESTCallNoParams(methodName, returnClass, urlTemplate, requestBody);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, error);
        }

        return null;
    }


    /**
     * Issue a POST REST call that returns a response object.  This is typically a create, update, or find with
     * complex parameters.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected <T> T callPostRESTCallNameConflict(String    methodName,
                                                 Class<T>  returnClass,
                                                 String    urlTemplate,
                                                 Object    requestBody) throws PropertyServerException
    {
        try
        {
            return clientConnector.callPostRESTCallNoParams(methodName, returnClass, urlTemplate, requestBody);
        }
        catch (Exception error)
        {
            /*
             * Avoid logging error to the audit log if this is just a name conflict
             */
            if (! error.getMessage().contains("org.springframework.web.client.HttpClientErrorException$Conflict"))
            {
                logRESTCallException(methodName, error);
            }

            throw new PropertyServerException(ApacheAtlasErrorCode.CLIENT_SIDE_REST_API_ERROR.getMessageDefinition(methodName,
                                                                                                                   serverName,
                                                                                                                   baseURL,
                                                                                                                   error.getMessage()),
                                              this.getClass().getName(),
                                              methodName,
                                              error);
        }
    }


    /**
     * Issue a POST REST call that returns a response object.  This is typically a create, update, or find with
     * complex parameters.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected  <T> T callPostRESTCall(String    methodName,
                                      Class<T>  returnClass,
                                      String    urlTemplate,
                                      Object    requestBody,
                                      Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callPostRESTCall(methodName, returnClass, urlTemplate, requestBody, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, error);
        }

        return null;
    }

    /**
     * Issue a POST REST call that returns a response object.  This is typically a create, update, or find with
     * complex parameters. It's working only with {@link SpringRESTClientConnector}
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param responseType class of the response for generic object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected  <T> T callPostRESTCall(String                        methodName,
                                      ParameterizedTypeReference<T> responseType,
                                      String                        urlTemplate,
                                      Object                        requestBody,
                                      Object...                     params) throws PropertyServerException
    {
        try
        {
            SpringRESTClientConnector clientConnector = (SpringRESTClientConnector) this.clientConnector;
            return clientConnector.callPostRESTCall(methodName, responseType, urlTemplate, requestBody, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, error);
        }

        return null;
    }

    /**
     * Issue a PUT REST call that returns a response object.  This is typically an update.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected  <T> T callPutRESTCall(String    methodName,
                                     Class<T>  returnClass,
                                     String    urlTemplate,
                                     Object    requestBody,
                                     Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callPutRESTCall(methodName, returnClass, urlTemplate, requestBody, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, error);
        }

        return null;
    }

    /**
     * Issue a PUT REST call that returns a response object.  This is typically an update.
     * It's working only with {@link SpringRESTClientConnector}
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param responseType class of the response for generic object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected  <T> T callPutRESTCall(String                        methodName,
                                     ParameterizedTypeReference<T> responseType,
                                     String                        urlTemplate,
                                     Object                        requestBody,
                                     Object...                     params) throws PropertyServerException
    {
        try
        {
            SpringRESTClientConnector clientConnector = (SpringRESTClientConnector) this.clientConnector;
            return clientConnector.callPutRESTCall(methodName, responseType, urlTemplate, requestBody, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, error);
        }

        return null;
    }


    /**
     * Issue a Delete REST call that returns a response object.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected  <T> T callDeleteRESTCall(String    methodName,
                                        Class<T>  returnClass,
                                        String    urlTemplate,
                                        Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callDeleteRESTCall(methodName, returnClass, urlTemplate, null, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, error);
        }

        return null;
    }


    /**
     * Issue a Delete REST call that returns a response object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected  void callDeleteRESTCall(String    methodName,
                                       String    urlTemplate) throws PropertyServerException
    {
        try
        {
            clientConnector.callDeleteRESTCallNoParams(methodName, Object.class, urlTemplate, null);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, error);
        }
    }


    /**
     * Issue a Delete REST call that returns a response object.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param responseType class of the response for generic object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    protected  <T> T callDeleteRESTCall(String                        methodName,
                                        ParameterizedTypeReference<T> responseType,
                                        String                        urlTemplate, Object... params) throws PropertyServerException
    {
        try
        {
            SpringRESTClientConnector clientConnector = (SpringRESTClientConnector) this.clientConnector;
            return clientConnector.callDeleteRESTCall(methodName, responseType, urlTemplate, null, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, error);
        }

        return null;
    }


    /**
     * Provide detailed logging for exceptions.
     *
     * @param methodName calling method
     * @param error resulting exception
     * @throws PropertyServerException wrapping exception
     */
    private void logRESTCallException(String    methodName,
                                      Exception error) throws PropertyServerException
    {
        if (auditLog != null)
        {
            auditLog.logException(methodName,
                                  ApacheAtlasAuditCode.CLIENT_SIDE_REST_API_ERROR.getMessageDefinition(methodName,
                                                                                                       serverName,
                                                                                                       baseURL,
                                                                                                       error.getMessage()),
                                  error);
        }

        throw new PropertyServerException(ApacheAtlasErrorCode.CLIENT_SIDE_REST_API_ERROR.getMessageDefinition(methodName,
                                                                                                               serverName,
                                                                                                               baseURL,
                                                                                                               error.getMessage()),
                                          this.getClass().getName(),
                                          methodName,
                                          error);
    }
}
