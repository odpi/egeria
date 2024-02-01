/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.server;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.ValidValueElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.ReferenceValueAssignmentProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.ValidValueAssignmentProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.ValidValueMembershipProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.ValidValueProperties;
import org.odpi.openmetadata.accessservices.datamanager.rest.ExternalSourceRequestBody;
import org.odpi.openmetadata.accessservices.datamanager.rest.ReferenceableRequestBody;
import org.odpi.openmetadata.accessservices.datamanager.rest.RelatedElementListResponse;
import org.odpi.openmetadata.accessservices.datamanager.rest.RelationshipRequestBody;
import org.odpi.openmetadata.accessservices.datamanager.rest.ValidValueListResponse;
import org.odpi.openmetadata.accessservices.datamanager.rest.ValidValueResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ValidValuesHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * ValidValuesRESTServices provides the API operations to create and maintain validValues information.
 */
public class ValidValuesRESTServices
{
    private static final DataManagerInstanceHandler   instanceHandler     = new DataManagerInstanceHandler();
    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(ValidValuesRESTServices.class),
                                                                            instanceHandler.getServiceName());


    /**
     * Default constructor
     */
    public ValidValuesRESTServices()
    {
    }


    /**
     * Create a new metadata element to represent a validValues.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createValidValue(String                   serverName,
                                         String                   userId,
                                         ReferenceableRequestBody requestBody)
    {
        final String methodName = "createValidValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValueHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ValidValueProperties properties)
                {
                    String validValueGUID = handler.createValidValue(userId,
                                                                     requestBody.getExternalSourceGUID(),
                                                                     requestBody.getExternalSourceName(),
                                                                     properties.getQualifiedName(),
                                                                     properties.getDisplayName(),
                                                                     properties.getDescription(),
                                                                     properties.getCategory(),
                                                                     properties.getUsage(),
                                                                     properties.getScope(),
                                                                     properties.getIsDeprecated(),
                                                                     properties.getIsCaseSensitive(),
                                                                     properties.getPreferredValue(),
                                                                     properties.getAdditionalProperties(),
                                                                     properties.getTypeName(),
                                                                     properties.getExtendedProperties(),
                                                                     instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                     properties.getEffectiveFrom(),
                                                                     properties.getEffectiveTo(),
                                                                     new Date(),
                                                                     methodName);

                    if (validValueGUID != null)
                    {
                        handler.setVendorProperties(userId,
                                                    validValueGUID,
                                                    properties.getVendorProperties(),
                                                    false,
                                                    false,
                                                    new Date(),
                                                    methodName);
                    }

                    response.setGUID(validValueGUID);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ValidValueProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Update the metadata element representing a validValues.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param validValueGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with the existing properties of overlay them?
     * @param requestBody new properties for this element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateValidValue(String                   serverName,
                                         String                   userId,
                                         String                   validValueGUID,
                                         boolean                  isMergeUpdate,
                                         ReferenceableRequestBody requestBody)
    {
        final String methodName = "updateValidValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;


        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValueHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ValidValueProperties properties)
                {
                    handler.updateValidValue(userId,
                                             requestBody.getExternalSourceGUID(),
                                             requestBody.getExternalSourceName(),
                                             validValueGUID,
                                             properties.getQualifiedName(),
                                             properties.getDisplayName(),
                                             properties.getDescription(),
                                             properties.getCategory(),
                                             properties.getUsage(),
                                             properties.getScope(),
                                             properties.getIsDeprecated(),
                                             properties.getIsCaseSensitive(),
                                             properties.getPreferredValue(),
                                             properties.getAdditionalProperties(),
                                             properties.getExtendedProperties(),
                                             instanceHandler.getSupportedZones(userId, serverName, methodName),
                                             properties.getEffectiveFrom(),
                                             properties.getEffectiveTo(),
                                             isMergeUpdate,
                                             false,
                                             false,
                                             new Date(),
                                             methodName);

                    if ((! isMergeUpdate) || (properties.getVendorProperties() != null))
                    {
                        handler.setVendorProperties(userId,
                                                    validValueGUID,
                                                    properties.getVendorProperties(),
                                                    false,
                                                    false,
                                                    new Date(),
                                                    methodName);
                    }
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ValidValueProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Create a membership relationship between a validValue and a validValueSet that it belongs to.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param validValueSetGUID unique identifier of the valid value set
     * @param validValueMemberGUID unique identifier of the member
     * @param requestBody relationship properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupValidValueMember(String                  serverName,
                                              String                  userId,
                                              String                  validValueSetGUID,
                                              String                  validValueMemberGUID,
                                              RelationshipRequestBody requestBody)
    {
        final String methodName = "setupValidValueMember";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValueHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ValidValueMembershipProperties properties)
                {
                    handler.attachValidValueToSet(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  validValueSetGUID,
                                                  validValueMemberGUID,
                                                  properties.getDefaultValue(),
                                                  properties.getEffectiveFrom(),
                                                  properties.getEffectiveTo(),
                                                  instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                  false,
                                                  false,
                                                  new Date(),
                                                  methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.attachValidValueToSet(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  validValueSetGUID,
                                                  validValueMemberGUID,
                                                  false,
                                                  null,
                                                  null,
                                                  instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                  false,
                                                  false,
                                                  new Date(),
                                                  methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ValidValueMembershipProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Remove a membership relationship between a validValue and a validValueSet that it belongs to.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param validValueSetGUID unique identifier of the valid value set
     * @param validValueMemberGUID unique identifier of the member
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearValidValueMember(String                   serverName,
                                              String                    userId,
                                              String                    validValueSetGUID,
                                              String                    validValueMemberGUID,
                                              ExternalSourceRequestBody requestBody)
    {
        final String methodName = "clearValidValueMember";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValueHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.detachValidValueFromSet(userId,
                                                requestBody.getExternalSourceGUID(),
                                                requestBody.getExternalSourceName(),
                                                validValueSetGUID,
                                                validValueMemberGUID,
                                                false,
                                                false,
                                                new Date(),
                                                methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Create a valid value assignment relationship between an element and a valid value (typically, a valid value set) to show that
     * the valid value defines the values that can be stored in the data item that the element represents.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param validValueGUID unique identifier of the valid value
     * @param requestBody relationship properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupValidValues(String                  serverName,
                                         String                  userId,
                                         String                  elementGUID,
                                         String                  validValueGUID,
                                         RelationshipRequestBody requestBody)
    {
        final String methodName = "setupValidValues";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValueHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ValidValueAssignmentProperties properties)
                {
                    handler.assignValidValueToConsumer(userId,
                                                       requestBody.getExternalSourceGUID(),
                                                       requestBody.getExternalSourceName(),
                                                       validValueGUID,
                                                       elementGUID,
                                                       properties.getStrictRequirement(),
                                                       properties.getEffectiveFrom(),
                                                       properties.getEffectiveTo(),
                                                       instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                       false,
                                                       false,
                                                       new Date(),
                                                       methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.assignValidValueToConsumer(userId,
                                                       requestBody.getExternalSourceGUID(),
                                                       requestBody.getExternalSourceName(),
                                                       validValueGUID,
                                                       elementGUID,
                                                       false,
                                                       null,
                                                       null,
                                                       instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                       false,
                                                       false,
                                                       new Date(),
                                                       methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ValidValueAssignmentProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Remove a valid value assignment relationship between an element and a valid value.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param validValueGUID unique identifier of the valid value
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearValidValues(String                    serverName,
                                         String                    userId,
                                         String                    elementGUID,
                                         String                    validValueGUID,
                                         ExternalSourceRequestBody requestBody)
    {
        final String methodName = "clearValidValues";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValueHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.unassignValidValueFromConsumer(userId,
                                                       requestBody.getExternalSourceGUID(),
                                                       requestBody.getExternalSourceName(),
                                                       validValueGUID,
                                                       elementGUID,
                                                       false,
                                                       false,
                                                       new Date(),
                                                       methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Create a reference value assignment relationship between an element and a valid value to show that
     * the valid value is a semiformal tag/classification.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param validValueGUID unique identifier of the valid value
     * @param requestBody relationship properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupReferenceValueTag(String                  serverName,
                                               String                  userId,
                                               String                  elementGUID,
                                               String                  validValueGUID,
                                               RelationshipRequestBody requestBody)
    {
        final String methodName = "setupReferenceValueTag";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValueHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ReferenceValueAssignmentProperties properties)
                {
                    handler.assignReferenceValueToItem(userId,
                                                       requestBody.getExternalSourceGUID(),
                                                       requestBody.getExternalSourceName(),
                                                       validValueGUID,
                                                       elementGUID,
                                                       properties.getAttributeName(),
                                                       properties.getConfidence(),
                                                       properties.getSteward(),
                                                       properties.getStewardTypeName(),
                                                       properties.getStewardPropertyName(),
                                                       properties.getNotes(),
                                                       instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                       properties.getEffectiveFrom(),
                                                       properties.getEffectiveTo(),
                                                       false,
                                                       false,
                                                       new Date(),
                                                       methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.assignReferenceValueToItem(userId,
                                                       requestBody.getExternalSourceGUID(),
                                                       requestBody.getExternalSourceName(),
                                                       validValueGUID,
                                                       elementGUID,
                                                       null,
                                                       100,
                                                       null,
                                                       null,
                                                       null,
                                                       null,
                                                       instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                       null,
                                                       null,
                                                       false,
                                                       false,
                                                       new Date(),
                                                       methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ReferenceValueAssignmentProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Remove a reference value assignment relationship between an element and a valid value.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param validValueGUID unique identifier of the valid value
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearReferenceValueTag(String                    serverName,
                                               String                    userId,
                                               String                    elementGUID,
                                               String                    validValueGUID,
                                               ExternalSourceRequestBody requestBody)
    {
        final String methodName = "clearReferenceValueTag";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValueHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.unassignValidValueFromConsumer(userId,
                                                       requestBody.getExternalSourceGUID(),
                                                       requestBody.getExternalSourceName(),
                                                       validValueGUID,
                                                       elementGUID,
                                                       false,
                                                       false,
                                                       new Date(),
                                                       methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Remove the metadata element representing a validValue.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param validValueGUID unique identifier of the metadata element to remove
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeValidValue(String                    serverName,
                                         String                    userId,
                                         String                    validValueGUID,
                                         ExternalSourceRequestBody requestBody)
    {
        final String methodName = "removeValidValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValueHandler(userId, serverName, methodName);

                handler.deleteValidValue(userId,
                                         requestBody.getExternalSourceGUID(),
                                         requestBody.getExternalSourceName(),
                                         validValueGUID,
                                         null,
                                         false,
                                         false,
                                         new Date(),
                                         methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Retrieve the list of validValue metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ValidValueListResponse findValidValues(String                  serverName,
                                                  String                  userId,
                                                  SearchStringRequestBody requestBody,
                                                  int                     startFrom,
                                                  int                     pageSize)
    {
        final String methodName = "findValidValues";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValueListResponse response = new ValidValueListResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValueHandler(userId, serverName, methodName);

                List<ValidValueElement> validValues = handler.findValidValues(userId,
                                                                              requestBody.getSearchString(),
                                                                              searchStringParameterName,
                                                                              startFrom,
                                                                              pageSize,
                                                                              false,
                                                                              false,
                                                                              instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                              new Date(),
                                                                              methodName);

                response.setElementList(setUpVendorProperties(userId, validValues, handler, methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Retrieve the list of validValue metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ValidValueListResponse getValidValuesByName(String          serverName,
                                                       String          userId,
                                                       NameRequestBody requestBody,
                                                       int             startFrom,
                                                       int             pageSize)
    {
        final String methodName = "getValidValuesByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValueListResponse response = new ValidValueListResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValueHandler(userId, serverName, methodName);

                List<ValidValueElement> validValues = handler.getValidValueByName(userId,
                                                                                  requestBody.getName(),
                                                                                  nameParameterName,
                                                                                  startFrom,
                                                                                  pageSize,
                                                                                  false,
                                                                                  false,
                                                                                  instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                                  new Date(),
                                                                                  methodName);

                response.setElementList(setUpVendorProperties(userId, validValues, handler, methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Page through the members of a valid value set.
     *
     * @param serverName called server
     * @param userId calling user
     * @param validValueSetGUID  unique identifier of the valid value set
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of valid value beans
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public ValidValueListResponse getValidValueSetMembers(String          serverName,
                                                          String          userId,
                                                          String          validValueSetGUID,
                                                          int             startFrom,
                                                          int             pageSize)
    {
        final String methodName         = "getValidValueSetMembers";
        final String guidParameterName  = "validValueSetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValueListResponse response = new ValidValueListResponse();
        AuditLog               auditLog = null;

        try
        {
            ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValueHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setElementList(handler.getValidValueSetMembers(userId,
                                                                    validValueSetGUID,
                                                                    guidParameterName,
                                                                    startFrom,
                                                                    pageSize,
                                                                    false,
                                                                    false,
                                                                    instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                    new Date(),
                                                                    methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Page through the list of valid value sets that a valid value definition/set belongs to.
     *
     * @param serverName called server
     * @param userId calling user
     * @param validValueGUID  unique identifier of the valid value
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of valid value beans
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public ValidValueListResponse getSetsForValidValue(String          serverName,
                                                       String          userId,
                                                       String          validValueGUID,
                                                       int             startFrom,
                                                       int             pageSize)
    {
        final String methodName         = "getSetsForValidValue";
        final String guidParameterName  = "validValueGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValueListResponse response = new ValidValueListResponse();
        AuditLog               auditLog = null;

        try
        {
            ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValueHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setElementList(handler.getSetsForValidValue(userId,
                                                                 validValueGUID,
                                                                 guidParameterName,
                                                                 startFrom,
                                                                 pageSize,
                                                                 false,
                                                                 false,
                                                                 instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                 new Date(),
                                                                 methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return information about the valid value set linked to an element as its
     * set of valid values.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier for the element using the valid value set
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ValidValueResponse getValidValuesForConsumer(String serverName,
                                                        String userId,
                                                        String elementGUID)
    {
        final String methodName = "getValidValuesForConsumer";
        final String guidParameterName  = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValueResponse response = new ValidValueResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValueHandler(userId, serverName, methodName);

            ValidValueElement validValue = handler.getAssignedValidValues(userId,
                                                                          elementGUID,
                                                                          guidParameterName,
                                                                          false,
                                                                          false,
                                                                          instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                          new Date(),
                                                                          methodName);

            response.setElement(setUpVendorProperties(userId, validValue, handler, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return information about the consumers linked to a valid value.
     *
     * @param serverName called server
     * @param userId calling user
     * @param validValueGUID unique identifier for the validValue
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching person roles
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public RelatedElementListResponse getConsumersOfValidValue(String          serverName,
                                                               String          userId,
                                                               String          validValueGUID,
                                                               int             startFrom,
                                                               int             pageSize)
    {
        final String methodName         = "getConsumersOfValidValue";
        final String guidParameterName  = "validValueGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setElementList(handler.getValidValueConsumers(userId,
                                                                   validValueGUID,
                                                                   guidParameterName,
                                                                   startFrom,
                                                                   pageSize,
                                                                   false,
                                                                   false,
                                                                   new Date(),
                                                                   methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return information about the valid values linked as reference value tags to an element.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier for the element using the valid value set
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ValidValueListResponse getReferenceValues(String serverName,
                                                     String userId,
                                                     String elementGUID,
                                                     int    startFrom,
                                                     int    pageSize)
    {
        final String methodName = "getReferenceValues";
        final String guidParameterName  = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValueListResponse response = new ValidValueListResponse();
        AuditLog               auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValueHandler(userId, serverName, methodName);

            List<ValidValueElement> validValue = handler.getReferenceValues(userId,
                                                                            elementGUID,
                                                                            guidParameterName,
                                                                            startFrom,
                                                                            pageSize,
                                                                            false,
                                                                            false,
                                                                            instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                            new Date(),
                                                                            methodName);

            response.setElementList(setUpVendorProperties(userId, validValue, handler, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return information about the consumers linked to a valid value.
     *
     * @param serverName called server
     * @param userId calling user
     * @param validValueGUID unique identifier for the validValue
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching person roles
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public RelatedElementListResponse getAssigneesOfReferenceValue(String          serverName,
                                                                   String          userId,
                                                                   String          validValueGUID,
                                                                   int             startFrom,
                                                                   int             pageSize)
    {
        final String methodName         = "getAssigneesOfReferenceValue";
        final String guidParameterName  = "validValueGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setElementList(handler.getReferenceValueAssignees(userId,
                                                                       validValueGUID,
                                                                       guidParameterName,
                                                                       startFrom,
                                                                       pageSize,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of validValue metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ValidValueListResponse getAllValidValues(String          serverName,
                                                    String          userId,
                                                    int             startFrom,
                                                    int             pageSize)
    {
        final String methodName = "getAllValidValues";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValueListResponse response = new ValidValueListResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValueHandler(userId, serverName, methodName);

            List<ValidValueElement> validValues = handler.getValidValues(userId,
                                                                         startFrom,
                                                                         pageSize,
                                                                         false,
                                                                         false,
                                                                         instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                         new Date(),
                                                                         methodName);

            response.setElementList(setUpVendorProperties(userId, validValues, handler, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the validValue metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ValidValueResponse getValidValueByGUID(String serverName,
                                                  String userId,
                                                  String guid)
    {
        final String methodName = "getValidValueByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValueResponse response = new ValidValueResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValueHandler(userId, serverName, methodName);

            ValidValueElement validValue = handler.getValidValueByGUID(userId,
                                                                       guid,
                                                                       false,
                                                                       false,
                                                                       instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                       new Date(),
                                                                       methodName);

            response.setElement(setUpVendorProperties(userId, validValue, handler, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Set up the vendor properties in the retrieved elements.
     *
     * @param userId calling user
     * @param retrievedResults results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<ValidValueElement> setUpVendorProperties(String                                userId,
                                                          List<ValidValueElement>               retrievedResults,
                                                          ValidValuesHandler<ValidValueElement> handler,
                                                          String                                methodName) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        if (retrievedResults != null)
        {
            for (ValidValueElement element : retrievedResults)
            {
                if (element != null)
                {
                    setUpVendorProperties(userId, element, handler, methodName);
                }
            }
        }

        return retrievedResults;
    }


    /**
     * Set up the vendor properties in the retrieved element.
     *
     * @param userId calling user
     * @param element results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private ValidValueElement setUpVendorProperties(String                               userId,
                                                    ValidValueElement                     element,
                                                    ValidValuesHandler<ValidValueElement> handler,
                                                    String                                methodName) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        final String elementGUIDParameterName = "element.getElementHeader().getGUID()";

        if (element != null)
        {
            ValidValueProperties properties = element.getValidValueProperties();

            properties.setVendorProperties(handler.getVendorProperties(userId,
                                                                       element.getElementHeader().getGUID(),
                                                                       elementGUIDParameterName,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName));
        }

        return element;
    }
}
