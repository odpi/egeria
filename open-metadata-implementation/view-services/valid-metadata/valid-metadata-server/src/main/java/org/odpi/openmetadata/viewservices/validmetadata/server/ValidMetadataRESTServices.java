/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.validmetadata.server;


import org.odpi.openmetadata.accessservices.digitalarchitecture.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The ValidMetadataRESTServices provides the server-side implementation of the Valid Metadata Open Metadata
 * View Service (OMVS).  
 */
public class ValidMetadataRESTServices extends TokenController
{
    private static final ValidMetadataInstanceHandler instanceHandler = new ValidMetadataInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(ValidMetadataRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public ValidMetadataRESTServices()
    {
    }



    /**
     * Create or update the valid value for a particular open metadata property name.  If the typeName is null, this valid value
     * applies to properties of this name from all types.  The valid value is stored in the preferredValue property.  If a valid value is
     * already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param requestBody preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    public VoidResponse setUpValidMetadataValue(String             serverName,
                                                String             typeName,
                                                String             propertyName,
                                                ValidMetadataValue requestBody)
    {
        final String methodName = "setUpValidMetadataValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataStoreClient client = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

                client.setUpValidMetadataValue(userId,
                                               typeName,
                                               propertyName,
                                               requestBody);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, ValidMetadataValue.class.getName());
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create or update the valid value for a name that can be stored in a particular open metadata property name.
     * This property is of type map from name to string.
     * The valid value is stored in the preferredValue property of validMetadataValue.
     * If the typeName is null, this valid value applies to properties of this name from any open metadata type.
     * If a valid value is already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param requestBody preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    public VoidResponse setUpValidMetadataMapName(String             serverName,
                                                  String             typeName,
                                                  String             propertyName,
                                                  ValidMetadataValue requestBody)
    {
        final String methodName = "setUpValidMetadataMapName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataStoreClient client = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

                client.setUpValidMetadataMapName(userId,
                                                 typeName,
                                                 propertyName,
                                                 requestBody);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, ValidMetadataValue.class.getName());
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create or update the valid value for a name that can be stored in a particular open metadata property name.
     * This property is of type map from name to string.
     * The valid value is stored in the preferredValue property of validMetadataValue.
     * If the typeName is null, this valid value applies to properties of this name from any open metadata type.
     * If a valid value is already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param requestBody preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    public VoidResponse setUpValidMetadataMapValue(String             serverName,
                                                   String             typeName,
                                                   String             propertyName,
                                                   String             mapName,
                                                   ValidMetadataValue requestBody)
    {
        final String methodName = "setUpValidMetadataMapValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataStoreClient client = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

                client.setUpValidMetadataMapValue(userId,
                                                  typeName,
                                                  propertyName,
                                                  mapName,
                                                  requestBody);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, ValidMetadataValue.class.getName());
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove a valid value for a property.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue specific valid value to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse clearValidMetadataValue(String          serverName,
                                                String          typeName,
                                                String          propertyName,
                                                String          preferredValue,
                                                NullRequestBody requestBody)
    {
        final String methodName = "clearValidMetadataValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataStoreClient client = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

            client.clearValidMetadataValue(userId,
                                           typeName,
                                           propertyName,
                                           preferredValue);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove a valid map name value for a property.  The match is done on preferred name.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue specific valid value to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse clearValidMetadataMapName(String          serverName,
                                                  String          typeName,
                                                  String          propertyName,
                                                  String          preferredValue,
                                                  NullRequestBody requestBody)
    {
        final String methodName = "clearValidMetadataMapName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataStoreClient client = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

            client.clearValidMetadataMapName(userId,
                                             typeName,
                                             propertyName,
                                             preferredValue);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove a valid map name value for a property.  The match is done on preferred name.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param preferredValue specific valid value to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse clearValidMetadataMapValue(String          serverName,
                                                   String          typeName,
                                                   String          propertyName,
                                                   String          mapName,
                                                   String          preferredValue,
                                                   NullRequestBody requestBody)
    {
        final String methodName = "clearValidMetadataMapValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataStoreClient client = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

            client.clearValidMetadataMapValue(userId,
                                              typeName,
                                              propertyName,
                                              mapName,
                                              preferredValue);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Validate whether the value found in an open metadata property is valid.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param actualValue value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value) or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    public BooleanResponse validateMetadataValue(String serverName,
                                                 String typeName,
                                                 String propertyName,
                                                 String actualValue)
    {
        final String methodName = "validateMetadataValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataStoreClient client = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

            response.setFlag(client.validateMetadataValue(userId,
                                                          typeName,
                                                          propertyName,
                                                          actualValue));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Validate whether the name found in an open metadata map property is valid.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param actualValue value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value) or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    public BooleanResponse validateMetadataMapName(String serverName,
                                                   String typeName,
                                                   String propertyName,
                                                   String actualValue)
    {
        final String methodName = "validateMetadataMapName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataStoreClient client = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

            response.setFlag(client.validateMetadataMapName(userId,
                                                            typeName,
                                                            propertyName,
                                                            actualValue));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Validate whether the name found in an open metadata map property is valid.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param actualValue value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value) or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    public BooleanResponse validateMetadataMapValue(String serverName,
                                                    String typeName,
                                                    String propertyName,
                                                    String mapName,
                                                    String actualValue)
    {
        final String methodName = "validateMetadataMapValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataStoreClient client = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

            response.setFlag(client.validateMetadataMapValue(userId,
                                                             typeName,
                                                             propertyName,
                                                             mapName,
                                                             actualValue));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve details of a specific valid value for a property.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    public ValidMetadataValueResponse getValidMetadataValue(String serverName,
                                                            String typeName,
                                                            String propertyName,
                                                            String preferredValue)
    {
        final String methodName = "getValidMetadataValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ValidMetadataValueResponse response = new ValidMetadataValueResponse();
        AuditLog                   auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataStoreClient client = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

            response.setElement(client.getValidMetadataValue(userId,
                                                             typeName,
                                                             propertyName,
                                                             preferredValue));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve details of a specific valid name for a map property.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    public ValidMetadataValueResponse getValidMetadataMapName(String serverName,
                                                              String typeName,
                                                              String propertyName,
                                                              String preferredValue)
    {
        final String methodName = "getValidMetadataMapName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ValidMetadataValueResponse response = new ValidMetadataValueResponse();
        AuditLog                   auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataStoreClient client = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

            response.setElement(client.getValidMetadataMapName(userId,
                                                               typeName,
                                                               propertyName,
                                                               preferredValue));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve details of a specific valid value for a map name.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param preferredValue valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    public ValidMetadataValueResponse getValidMetadataMapValue(String serverName,
                                                               String typeName,
                                                               String propertyName,
                                                               String mapName,
                                                               String preferredValue)
    {
        final String methodName = "getValidMetadataMapValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ValidMetadataValueResponse response = new ValidMetadataValueResponse();
        AuditLog                   auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataStoreClient client = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

            response.setElement(client.getValidMetadataMapValue(userId,
                                                                typeName,
                                                                propertyName,
                                                                mapName,
                                                                preferredValue));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve all the valid values for the requested property.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of valid values defined for the property or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    public ValidMetadataValueDetailListResponse getValidMetadataValues(String serverName,
                                                                       String typeName,
                                                                       String propertyName,
                                                                       int    startFrom,
                                                                       int    pageSize)
    {
        final String methodName = "getValidMetadataValues";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ValidMetadataValueDetailListResponse response = new ValidMetadataValueDetailListResponse();
        AuditLog                             auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataStoreClient client = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

            response.setElementList(client.getValidMetadataValues(userId,
                                                                  typeName,
                                                                  propertyName,
                                                                  startFrom,
                                                                  pageSize));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve all the consistent valid values for the requested property.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName optional name of map key that this valid value applies
     * @param preferredValue the value to match against
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of valid values defined for the property or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    public ValidMetadataValueListResponse getConsistentMetadataValues(String serverName,
                                                                      String typeName,
                                                                      String propertyName,
                                                                      String mapName,
                                                                      String preferredValue,
                                                                      int    startFrom,
                                                                      int    pageSize)
    {
        final String methodName = "getConsistentMetadataValues";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ValidMetadataValueListResponse response = new ValidMetadataValueListResponse();
        AuditLog                       auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataStoreClient client = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

            response.setElementList(client.getConsistentMetadataValues(userId,
                                                                       typeName,
                                                                       propertyName,
                                                                       mapName,
                                                                       preferredValue,
                                                                       startFrom,
                                                                       pageSize));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Set up consistent metadata values relationship between the two property values.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName1 type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName1 name of property that this valid value applies
     * @param mapName1 optional name of map key that this valid value applies
     * @param preferredValue1 the value to match against
     * @param typeName2 type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName2 name of property that this valid value applies
     * @param mapName2 optional name of map key that this valid value applies
     * @param preferredValue2 the value to match against
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse setConsistentMetadataValues(String          serverName,
                                                    String          typeName1,
                                                    String          propertyName1,
                                                    String          mapName1,
                                                    String          preferredValue1,
                                                    String          typeName2,
                                                    String          propertyName2,
                                                    String          mapName2,
                                                    String          preferredValue2,
                                                    NullRequestBody requestBody)
    {
        final String methodName = "setConsistentMetadataValues";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataStoreClient client = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

            client.setConsistentMetadataValues(userId,
                                               typeName1,
                                               propertyName1,
                                               mapName1,
                                               preferredValue1,
                                               typeName2,
                                               propertyName2,
                                               mapName2,
                                               preferredValue2);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Returns the list of different types of metadata organized into two groups.  The first are the
     * attribute type definitions (AttributeTypeDefs).  These provide types for properties in full
     * type definitions.  Full type definitions (TypeDefs) describe types for entities, relationships
     * and classifications.
     *
     * @param serverName unique identifier for requested server.
     * @return TypeDefGalleryResponse:
     * List of different categories of type definitions or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDefGalleryResponse getAllTypes(String serverName)
    {
        final String methodName = "getAllTypes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        TypeDefGalleryResponse response = new TypeDefGalleryResponse();
        AuditLog               auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataStoreClient client = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

            OpenMetadataTypeDefGallery typeDefGallery = client.getAllTypes(userId);

            if (typeDefGallery != null)
            {
                response.setTypeDefs(typeDefGallery.getTypeDefs());
                response.setAttributeTypeDefs(typeDefGallery.getAttributeTypeDefs());
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Returns all the TypeDefs for a specific category.
     *
     * @param serverName unique identifier for requested server.
     * @param category find parameters used to limit the returned results.
     * @return TypeDefListResponse:
     * TypeDefs list or
     * InvalidParameterException the TypeDefCategory is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDefListResponse getTypeDefsByCategory(String                      serverName,
                                                     OpenMetadataTypeDefCategory category)
    {
        final String methodName = "getTypeDefsByCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        TypeDefListResponse response = new TypeDefListResponse();
        AuditLog            auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataStoreClient client = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

            response.setTypeDefs(client.findTypeDefsByCategory(userId, category));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Returns all the TypeDefs for a specific subtype.  If a null result is returned it means the
     * type has no subtypes.
     *
     * @param serverName unique identifier for requested server.
     * @param typeName name of type to retrieve against.
     * @return TypeDefListResponse:
     * TypeDefs list or
     * InvalidParameterException the typeName is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDefListResponse getSubTypes(String serverName,
                                           String typeName)
    {
        final String methodName = "getSubTypes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        TypeDefListResponse response = new TypeDefListResponse();
        AuditLog            auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataStoreClient client = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

            response.setTypeDefs(client.getSubTypes(userId, typeName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Returns all the TypeDefs for relationships that can be attached to the requested entity type.
     *
     * @param serverName unique identifier for requested server.
     * @param typeName name of entity type to retrieve against.
     * @return TypeDefsGalleryResponse:
     * A list of types or
     * InvalidParameterException all attributes of the external id are null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDefListResponse getValidRelationshipTypes(String serverName,
                                                         String typeName)
    {
        final String methodName = "getValidRelationshipTypes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        TypeDefListResponse response = new TypeDefListResponse();
        AuditLog            auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataStoreClient client = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

            OpenMetadataTypeDef entityDef = client.getTypeDefByName(userId, typeName);

            if (entityDef != null)
            {
                List<String> entityTypeNames = this.getEntityTypeNames(userId, entityDef, client);

                List<OpenMetadataTypeDef> relationshipDefs = client.findTypeDefsByCategory(userId, OpenMetadataTypeDefCategory.RELATIONSHIP_DEF);

                if (relationshipDefs != null)
                {
                    Map<String, OpenMetadataTypeDef> results = new HashMap<>();

                    for (OpenMetadataTypeDef typeDef : relationshipDefs)
                    {
                        if (typeDef instanceof OpenMetadataRelationshipDef relationshipDef)
                        {
                            for (String entityTypeName : entityTypeNames)
                            {
                                if (entityTypeName.equals(relationshipDef.getEndDef1().getEntityType().getName()))
                                {
                                    results.put(relationshipDef.getName(), relationshipDef);
                                }

                                if (entityTypeName.equals(relationshipDef.getEndDef2().getEntityType().getName()))
                                {
                                    results.put(relationshipDef.getName(), relationshipDef);
                                }
                            }
                        }
                    }

                    response.setTypeDefs(new ArrayList<>(results.values()));
                }
            }

        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of entity type names starting from the requested entity, walking its suerType hierarchy.
     *
     * @param userId calling user
     * @param entityDef retrieved entity
     * @param client  client to retrieve more types
     * @return list of type names
     * @throws InvalidParameterException bad parameter
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     */
    List<String> getEntityTypeNames(String                  userId,
                                    OpenMetadataTypeDef     entityDef,
                                    OpenMetadataStoreClient client) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        List<String>        entityTypeNames = new ArrayList<>();

        OpenMetadataTypeDef currentEntityDef = entityDef;
        entityTypeNames.add(currentEntityDef.getName());

        while (currentEntityDef.getSuperType() != null)
        {
            currentEntityDef = client.getTypeDefByName(userId, currentEntityDef.getSuperType().getName());
            entityTypeNames.add(currentEntityDef.getName());
        }

        return entityTypeNames;
    }


    /**
     * Returns all the TypeDefs for classifications that can be attached to the requested entity type.
     *
     * @param serverName unique identifier for requested server.
     * @param typeName name of type to retrieve against.
     * @return TypeDefsGalleryResponse:
     * A list of types or
     * InvalidParameterException all attributes of the external id are null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDefListResponse getValidClassificationTypes(String serverName,
                                                           String typeName)
    {
        final String methodName = "getValidClassificationTypes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        TypeDefListResponse response = new TypeDefListResponse();
        AuditLog            auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataStoreClient client = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

            OpenMetadataTypeDef entityDef = client.getTypeDefByName(userId, typeName);

            if (entityDef != null)
            {
                List<String> entityTypeNames = this.getEntityTypeNames(userId, entityDef, client);

                List<OpenMetadataTypeDef> classificationDefs = client.findTypeDefsByCategory(userId, OpenMetadataTypeDefCategory.CLASSIFICATION_DEF);

                if (classificationDefs != null)
                {
                    Map<String, OpenMetadataTypeDef> results = new HashMap<>();

                    for (OpenMetadataTypeDef typeDef : classificationDefs)
                    {
                        if (typeDef instanceof OpenMetadataClassificationDef classificationDef)
                        {
                            if (classificationDef.getValidEntityDefs() != null)
                            {
                                for (String entityTypeName : entityTypeNames)
                                {
                                    for (OpenMetadataTypeDefLink assignToEntity : classificationDef.getValidEntityDefs())
                                    {
                                        if (entityTypeName.equals(assignToEntity.getName()))
                                        {
                                            results.put(classificationDef.getName(), classificationDef);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    response.setTypeDefs(new ArrayList<>(results.values()));
                }
            }

        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the TypeDef identified by the unique name.
     *
     * @param serverName unique identifier for requested server.
     * @param name String name of the TypeDef.
     * @return TypeDefResponse:
     * TypeDef structure describing its category and properties or
     * InvalidParameterException the name is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * TypeDefNotKnownException the requested TypeDef is not found in the metadata collection or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDefResponse getTypeDefByName(String    serverName,
                                            String    name)
    {
        final String methodName = "getTypeDefByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        TypeDefResponse response = new TypeDefResponse();
        AuditLog            auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataStoreClient client = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

            response.setTypeDef(client.getTypeDefByName(userId, name));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
