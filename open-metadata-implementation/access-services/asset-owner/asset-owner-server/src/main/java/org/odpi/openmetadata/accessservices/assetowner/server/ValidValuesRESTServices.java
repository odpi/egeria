/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.server;

import org.odpi.openmetadata.accessservices.assetowner.metadataelements.ValidValueElement;
import org.odpi.openmetadata.accessservices.assetowner.rest.ValidValueResponse;
import org.odpi.openmetadata.accessservices.assetowner.rest.ValidValuesRequestBody;
import org.odpi.openmetadata.accessservices.assetowner.rest.ValidValuesResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.ValidValuesHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * ValidValuesAssetOwner provides the API operations to create and maintain lists of valid
 * value definitions grouped into a valid value set.  Both valid value definitions and valid value sets have
 * the same attributes and so inherit from ValidValue where all the attributes are defined.
 * A set is just grouping of valid values.   Valid value definitions and set can be nested many times in other
 * valid value sets.
 */
public class ValidValuesRESTServices
{
    private static final AssetOwnerInstanceHandler   instanceHandler      = new AssetOwnerInstanceHandler();
    private static final RESTExceptionHandler        restExceptionHandler = new RESTExceptionHandler();
    private static final RESTCallLogger              restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(ValidValuesRESTServices.class),
                                                                                               instanceHandler.getServiceName());


    /**
     * Default constructor
     */
    public ValidValuesRESTServices()
    {
    }


    /*
     * ==============================================
     * AssetOnboardingValidValues
     * ==============================================
     */

    /**
     * Create a new valid value set.  This just creates the Set itself.  Members are added either as they are
     * created, or they can be attached to a set after they are created.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param requestBody parameters for the new object.
     *
     * @return unique identifier for the new set or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public GUIDResponse createValidValueSet(String                 serverName,
                                            String                 userId,
                                            ValidValuesRequestBody requestBody)
    {
        final String methodName = "createValidValueSet";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

                response.setGUID(handler.createValidValueSet(userId,
                                                             null,
                                                             null,
                                                             requestBody.getQualifiedName(),
                                                             requestBody.getDisplayName(),
                                                             requestBody.getDescription(),
                                                             requestBody.getCategory(),
                                                             requestBody.getUsage(),
                                                             requestBody.getScope(),
                                                             false,
                                                             false,
                                                             requestBody.getAdditionalProperties(),
                                                             requestBody.getExtendedProperties(),
                                                             instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                             null,
                                                             null,
                                                             new Date(),
                                                             methodName));
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
     * Create a new valid value definition.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param setGUID unique identifier of the set to attach this to.
     * @param isDefaultValue     is this the default value for the set?
     * @param requestBody parameters to update.
     *
     * @return unique identifier for the new definition
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public GUIDResponse  createValidValueDefinition(String                 serverName,
                                                    String                 userId,
                                                    String                 setGUID,
                                                    boolean                isDefaultValue,
                                                    ValidValuesRequestBody requestBody)
    {
        final String methodName = "createValidValueDefinition";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

                response.setGUID(handler.createValidValueDefinition(userId,
                                                                    null,
                                                                    null,
                                                                    setGUID,
                                                                    isDefaultValue,
                                                                    requestBody.getQualifiedName(),
                                                                    requestBody.getDisplayName(),
                                                                    requestBody.getDescription(),
                                                                    requestBody.getCategory(),
                                                                    requestBody.getUsage(),
                                                                    requestBody.getScope(),
                                                                    requestBody.getPreferredValue(),
                                                                    requestBody.getDataType(),
                                                                    false,
                                                                    false,
                                                                    requestBody.getAdditionalProperties(),
                                                                    requestBody.getExtendedProperties(),
                                                                    instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                    null,
                                                                    null,
                                                                    false,
                                                                    false,
                                                                    new Date(),
                                                                    methodName));
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
     * Update the properties of the valid value.  All properties are updated.
     * If only changing some if the properties, retrieve the current values from the repository
     * and pass existing values back on this call if they are not to change.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param requestBody parameters to update.
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse    updateValidValue(String                 serverName,
                                            String                 userId,
                                            String                 validValueGUID,
                                            ValidValuesRequestBody requestBody)
    {
        final String   methodName = "updateValidValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

                handler.updateValidValue(userId,
                                         null,
                                         null,
                                         validValueGUID,
                                         requestBody.getQualifiedName(),
                                         requestBody.getDisplayName(),
                                         requestBody.getDescription(),
                                         requestBody.getCategory(),
                                         requestBody.getUsage(),
                                         requestBody.getScope(),
                                         false,
                                         false,
                                         requestBody.getPreferredValue(),
                                         requestBody.getDataType(),
                                         requestBody.getAdditionalProperties(),
                                         requestBody.getExtendedProperties(),
                                         instanceHandler.getSupportedZones(userId, serverName, methodName),
                                         null,
                                         null,
                                         true,
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
     * Remove the valid value form the repository.  All links to it are deleted too.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param validValueGUID unique identifier of the value to delete
     * @param qualifiedName unique name of the value to delete.  This is used to verify that
     *                      the correct valid value is being deleted.
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse    deleteValidValue(String   serverName,
                                            String   userId,
                                            String   validValueGUID,
                                            String   qualifiedName)
    {
        final String   methodName = "deleteValidValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (qualifiedName != null)
            {
                ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

                handler.deleteValidValue(userId,
                                         null,
                                         null,
                                         validValueGUID,
                                         qualifiedName,
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
     * Create a link between a valid value set or definition and a set.  This means the valid
     * value is a member of the set.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param setGUID unique identifier of the set.
     * @param validValueGUID unique identifier of the valid value to add to the set.
     * @param isDefaultValue     is this the default value for the set?
     * @param requestBody null request body supplied to satisfy REST protocol
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse    attachValidValueToSet(String          serverName,
                                                 String          userId,
                                                 String          setGUID,
                                                 String          validValueGUID,
                                                 boolean         isDefaultValue,
                                                 NullRequestBody requestBody)
    {
        final String   methodName = "attachValidValueToSet";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            handler.attachValidValueToSet(userId,
                                          null,
                                          null,
                                          setGUID,
                                          validValueGUID,
                                          isDefaultValue,
                                          null,
                                          null,
                                          instanceHandler.getSupportedZones(userId, serverName, methodName),
                                          false,
                                          false,
                                          new Date(),
                                          methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the link between a valid value and a set it is a member of.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param setGUID owning set
     * @param validValueGUID unique identifier of the member to be removed.
     * @param requestBody null request body supplied to satisfy REST protocol
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse    detachValidValueFromSet(String          serverName,
                                                   String          userId,
                                                   String          setGUID,
                                                   String          validValueGUID,
                                                   NullRequestBody requestBody)
    {
        final String   methodName = "detachValidValueFromSet";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            handler.detachValidValueFromSet(userId,
                                            null,
                                            null,
                                            setGUID,
                                            validValueGUID,
                                            false,
                                            false,
                                            new Date(),
                                            methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve a specific valid value from the repository.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param validValueGUID unique identifier of the valid value.
     *
     * @return Valid value bean or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public ValidValueResponse getValidValueByGUID(String   serverName,
                                                  String   userId,
                                                  String   validValueGUID)
    {
        final String   methodName = "getValidValueByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValueResponse response = new ValidValueResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            response.setElement(handler.getValidValueByGUID(userId,
                                                            validValueGUID,
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
     * Retrieve a specific valid value from the repository. Duplicates may be returned if
     * multiple valid values have been assigned the same qualified name.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param validValueName qualified name of the valid value.
     * @param startFrom         starting element (used in paging through large result sets)
     * @param pageSize          maximum number of results to return
     *
     * @return Valid value beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public ValidValuesResponse getValidValueByName(String   serverName,
                                                   String   userId,
                                                   String   validValueName,
                                                   int      startFrom,
                                                   int      pageSize)
    {
        final String nameParameterName = "validValueName";
        final String methodName        = "getValidValueByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValuesResponse response = new ValidValuesResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (validValueName != null)
            {
                ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

                response.setElementList(handler.getValidValueByName(userId,
                                                                    validValueName,
                                                                    nameParameterName,
                                                                    startFrom,
                                                                    pageSize,
                                                                    false,
                                                                    false,
                                                                    instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                    new Date(),
                                                                    methodName));
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
     * Locate valid values that match the search string.  It considers the names, description, scope,
     * usage and preferred value.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody string value to look for - may contain RegEx characters.
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of valid value beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public ValidValuesResponse findValidValues(String            serverName,
                                               String            userId,
                                               int               startFrom,
                                               int               pageSize,
                                               FilterRequestBody requestBody)
    {
        final String searchStringParameterName = "searchString";
        final String methodName                = "findValidValues";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValuesResponse response = new ValidValuesResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

                response.setElementList(handler.findValidValues(userId,
                                                                requestBody.getFilter(),
                                                                searchStringParameterName,
                                                                startFrom,
                                                                pageSize,
                                                                false,
                                                                false,
                                                                instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                requestBody.getEffectiveTime(),
                                                                methodName));
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
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueSetGUID unique identifier of the valid value set.
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of valid value beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public ValidValuesResponse getValidValueSetMembers(String   serverName,
                                                       String   userId,
                                                       String   validValueSetGUID,
                                                       int      startFrom,
                                                       int      pageSize)
    {
        final String validValueGUIDParameter = "validValueSetGUID";
        final String methodName              = "getValidValueSetMembers";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValuesResponse response = new ValidValuesResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            response.setElementList(handler.getValidValueSetMembers(userId,
                                                                    validValueSetGUID,
                                                                    validValueGUIDParameter,
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
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of valid value beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public ValidValuesResponse getSetsForValidValue(String   serverName,
                                                    String   userId,
                                                    String   validValueGUID,
                                                    int      startFrom,
                                                    int      pageSize)
    {
        final String validValueGUIDParameter = "validValueGUID";
        final String methodName              = "getSetsForValidValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValuesResponse response = new ValidValuesResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            response.setElementList(handler.getSetsForValidValue(userId,
                                                                 validValueGUID,
                                                                 validValueGUIDParameter,
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
}
