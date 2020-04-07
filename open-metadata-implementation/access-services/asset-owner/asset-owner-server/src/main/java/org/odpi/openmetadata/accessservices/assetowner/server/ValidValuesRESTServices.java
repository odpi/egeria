/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.server;

import org.odpi.openmetadata.accessservices.assetowner.rest.ValidValuesRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.ValidValuesHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;

/**
 * ValidValuesAssetOwner provides the API operations to create and maintain lists of valid
 * value definitions grouped into a valid value set.  Both valid value definitions and valid value sets have
 * the same attributes and so inherit from ValidValue where all of the attributes are defined.
 *
 * A set is just grouping of valid values.   Valid value definitions and set can be nested many times in other
 * valid value sets.
 */
public class ValidValuesRESTServices
{
    private static AssetOwnerInstanceHandler   instanceHandler     = new AssetOwnerInstanceHandler();
    private static RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
    private static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(ValidValuesRESTServices.class),
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
                ValidValuesHandler handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

                response.setGUID(handler.createValidValueSet(userId,
                                                             requestBody.getQualifiedName(),
                                                             requestBody.getDisplayName(),
                                                             requestBody.getDescription(),
                                                             requestBody.getUsage(),
                                                             requestBody.getScope(),
                                                             requestBody.getAdditionalProperties(),
                                                             requestBody.getExtendedProperties(),
                                                             methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
                ValidValuesHandler handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

                response.setGUID(handler.createValidValueDefinition(userId,
                                                                    setGUID,
                                                                    requestBody.getQualifiedName(),
                                                                    requestBody.getDisplayName(),
                                                                    requestBody.getDescription(),
                                                                    requestBody.getUsage(),
                                                                    requestBody.getScope(),
                                                                    requestBody.getPreferredValue(),
                                                                    requestBody.getAdditionalProperties(),
                                                                    requestBody.getExtendedProperties(),
                                                                    methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
                ValidValuesHandler handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

                handler.updateValidValue(userId,
                                         validValueGUID,
                                         requestBody.getQualifiedName(),
                                         requestBody.getDisplayName(),
                                         requestBody.getDescription(),
                                         requestBody.getUsage(),
                                         requestBody.getScope(),
                                         requestBody.getPreferredValue(),
                                         requestBody.getAdditionalProperties(),
                                         requestBody.getExtendedProperties(),
                                         methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
                ValidValuesHandler handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

                handler.deleteValidValue(userId,
                                         validValueGUID,
                                         qualifiedName,
                                         methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
     * @param requestBody null request body supplied to satisfy REST protocol
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse    attachValidValueToSet(String          serverName,
                                                 String          userId,
                                                 String          setGUID,
                                                 String          validValueGUID,
                                                 NullRequestBody requestBody)
    {
        final String   methodName = "attachValidValueToSet";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            handler.attachValidValueToSet(userId, setGUID, validValueGUID, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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

            ValidValuesHandler handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            handler.detachValidValueFromSet(userId, setGUID, validValueGUID, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Link a valid value to an asset that provides the implementation.  Typically this method is
     * used to link a valid value set to a code table.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param assetGUID unique identifier of the asset that implements the valid value.
     * @param requestBody null request body supplied to satisfy REST protocol
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse  linkValidValueToImplementation(String          serverName,
                                                        String          userId,
                                                        String          validValueGUID,
                                                        String          assetGUID,
                                                        NullRequestBody requestBody)
    {
        final String   methodName = "linkValidValueToImplementation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            handler.linkValidValueToImplementation(userId, validValueGUID, assetGUID, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Add the ReferenceData classification to an asset.  IF the asset is already classified
     * in this way, the method is a no-op.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param assetGUID unique identifier of the asset that contains reference data.
     * @param requestBody null request body supplied to satisfy REST protocol
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse  classifyAssetAsReferenceData(String          serverName,
                                                      String          userId,
                                                      String          assetGUID,
                                                      NullRequestBody requestBody)
    {
        final String   methodName = "classifyAssetAsReferenceData";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            handler.classifyAssetAsReferenceData(userId, assetGUID, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the link between a valid value and an implementing asset.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param assetGUID unique identifier of the asset that used to implement the valid value.
     * @param requestBody null request body supplied to satisfy REST protocol
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse  unlinkValidValueFromImplementation(String          serverName,
                                                            String          userId,
                                                            String          validValueGUID,
                                                            String          assetGUID,
                                                            NullRequestBody requestBody)
    {
        final String   methodName = "unlinkValidValueFromImplementation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            handler.unlinkValidValueFromImplementation(userId, validValueGUID, assetGUID, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the ReferenceData classification form an Asset.  If the asset was not classified in this way,
     * this call is a no-op.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param assetGUID unique identifier of asset.
     * @param requestBody null request body supplied to satisfy REST protocol
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse  declassifyAssetAsReferenceData(String          serverName,
                                                        String          userId,
                                                        String          assetGUID,
                                                        NullRequestBody requestBody)
    {
        final String   methodName = "declassifyAssetAsReferenceData";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            handler.declassifyAssetAsReferenceData(userId, assetGUID, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Link a valid value typically to a schema element or glossary term to show that it uses
     * the valid values.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param consumerGUID unique identifier of the element to link to.
     * @param requestBody boolean request body supplied to pass the strictRequirement flag
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse    assignValidValueToConsumer(String             serverName,
                                                      String             userId,
                                                      String             validValueGUID,
                                                      String             consumerGUID,
                                                      BooleanRequestBody requestBody)
    {
        final String   methodName = "assignValidValueToConsumer";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            handler.assignValidValueToConsumer(userId, validValueGUID, consumerGUID, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the link between a valid value and a consumer.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param consumerGUID unique identifier of the element to remove the link from.
     * @param requestBody null request body supplied to satisfy REST protocol
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse unassignValidValueFromConsumer(String          serverName,
                                                       String          userId,
                                                       String          validValueGUID,
                                                       String          consumerGUID,
                                                       NullRequestBody requestBody)
    {
        final String   methodName = "unassignValidValueFromConsumer";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            handler.unassignValidValueFromConsumer(userId, validValueGUID, consumerGUID, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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

            ValidValuesHandler handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            response.setValidValue(handler.getValidValueByGUID(userId, validValueGUID, methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
     *
     * @return Valid value beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public ValidValuesResponse getValidValueByName(String   serverName,
                                                   String   userId,
                                                   String   validValueName)
    {
        final String   methodName = "getValidValueByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValuesResponse response = new ValidValuesResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (validValueName != null)
            {
                ValidValuesHandler handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

                response.setValidValues(handler.getValidValueByName(userId, validValueName, methodName));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
     * @param searchString string value to look for - may contain RegEx characters.
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of valid value beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public ValidValuesResponse findValidValues(String   serverName,
                                               String   userId,
                                               String   searchString,
                                               int      startFrom,
                                               int      pageSize)
    {
        final String   methodName = "findValidValues";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValuesResponse response = new ValidValuesResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (searchString != null)
            {
                ValidValuesHandler handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

                response.setValidValues(handler.findValidValues(userId, searchString, startFrom, pageSize, methodName));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
        final String   methodName = "getValidValueSetMembers";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValuesResponse response = new ValidValuesResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            response.setValidValues(handler.getValidValueSetMembers(userId,
                                                                    validValueSetGUID,
                                                                    startFrom,
                                                                    pageSize,
                                                                    methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
        final String   methodName = "getSetsForValidValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValuesResponse response = new ValidValuesResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            response.setValidValues(handler.getSetsForValidValue(userId,
                                                                 validValueGUID,
                                                                 startFrom,
                                                                 pageSize,
                                                                 methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Page through the list of consumers for a valid value.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of consumers beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public ValidValueConsumersResponse getValidValuesConsumers(String   serverName,
                                                               String   userId,
                                                               String   validValueGUID,
                                                               int      startFrom,
                                                               int      pageSize)
    {
        final String   methodName = "getValidValuesConsumers";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValueConsumersResponse response = new ValidValueConsumersResponse();
        AuditLog                    auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            response.setValidValueConsumers(handler.getValidValuesConsumers(userId,
                                                                            validValueGUID,
                                                                            startFrom,
                                                                            pageSize,
                                                                            methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Pag through the list of implementations for a valid value.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of asset beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public AssetsResponse getValidValuesImplementations(String   serverName,
                                                        String   userId,
                                                        String   validValueGUID,
                                                        int      startFrom,
                                                        int      pageSize)
    {
        final String   methodName = "getValidValuesImplementations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AssetsResponse response = new AssetsResponse();
        AuditLog       auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            response.setAssets(handler.getValidValuesImplementations(userId,
                                                                     validValueGUID,
                                                                     startFrom,
                                                                     pageSize,
                                                                     methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
