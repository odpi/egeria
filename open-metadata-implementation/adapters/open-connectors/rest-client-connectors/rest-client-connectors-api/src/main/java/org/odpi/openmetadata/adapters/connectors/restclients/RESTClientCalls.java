/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.restclients;

import org.odpi.openmetadata.adapters.connectors.restclients.ffdc.exceptions.RESTServerException;

/**
 * RESTClientCalls provides a generic interface for calling REST Clients.
 */
public interface RESTClientCalls
{
    /**
     * Issue a GET REST call that returns a response object.
     *
     * @param <T> class name
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     *
     * @return response object
     * @throws RESTServerException something went wrong with the REST call stack.
     */
    <T> T callGetRESTCallNoParams(String    methodName,
                                  Class<T>  returnClass,
                                  String    urlTemplate) throws RESTServerException;


    /**
     * Issue a GET REST call that returns a response object.
     *
     * @param <T> class name
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws RESTServerException something went wrong with the REST call stack.
     */
    <T> T callGetRESTCall(String    methodName,
                          Class<T>  returnClass,
                          String    urlTemplate,
                          Object... params) throws RESTServerException;


    /**
     * Issue a POST REST call that returns a response object.  This is typically a create, update, or find with
     * complex parameters.
     *
     * @param <T> class name
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     *
     * @return response object
     * @throws RESTServerException something went wrong with the REST call stack.
     */
    <T> T callPostRESTCallNoParams(String    methodName,
                                   Class<T>  returnClass,
                                   String    urlTemplate,
                                   Object    requestBody) throws RESTServerException;


    /**
     * Issue a POST REST call that returns a response object.  This is typically a create, update, or find with
     * complex parameters.
     *
     * @param <T> class name
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws RESTServerException something went wrong with the REST call stack.
     */
    <T> T callPostRESTCall(String    methodName,
                           Class<T>  returnClass,
                           String    urlTemplate,
                           Object    requestBody,
                           Object... params) throws RESTServerException;

    /**
     * Issue a PUT REST call that returns a response object.  This is typically an update.
     *
     * @param <T> class name
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws RESTServerException something went wrong with the REST call stack.
     */
    <T> T callPutRESTCall(String    methodName,
                           Class<T>  returnClass,
                           String    urlTemplate,
                           Object    requestBody,
                           Object... params) throws RESTServerException;


    /**
     * Issue a DELETE REST call that returns a response object.
     *
     * @param <T> class name
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     *
     * @return Object
     * @throws RESTServerException something went wrong with the REST call stack.
     */
    <T> T callDeleteRESTCallNoParams(String    methodName,
                                     Class<T>  returnClass,
                                     String    urlTemplate,
                                     Object    requestBody) throws RESTServerException;


    /**
     * Issue a DELETE REST call that returns a response object.
     *
     * @param <T> class name
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return Object
     * @throws RESTServerException something went wrong with the REST call stack.
     */
    <T> T callDeleteRESTCall(String    methodName,
                             Class<T>  returnClass,
                             String    urlTemplate,
                             Object    requestBody,
                             Object... params) throws RESTServerException;
}
