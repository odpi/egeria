/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.server;

import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.*;
import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.ElementHeader;
import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.ElementOrigin;
import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.ElementType;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.*;
import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.ElementClassification;
import org.odpi.openmetadata.accessservices.digitalarchitecture.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ValidValuesHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ValidValuesRESTServices provides the API operations to create and maintain lists of valid
 * value definitions grouped into a valid value set.  Both valid value definitions and valid value sets have
 * the same attributes and so inherit from ValidValue where all of the attributes are defined.
 *
 * A set is just grouping of valid values.   Valid value definitions and set can be nested many times in other
 * valid value sets.
 */
public class ValidValuesRESTServices
{
    private static DigitalArchitectureInstanceHandler   instanceHandler     = new DigitalArchitectureInstanceHandler();
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
    public GUIDResponse createValidValueSet(String               serverName,
                                            String               userId,
                                            ValidValueProperties requestBody)
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
                ValidValuesHandler<ValidValueElement,
                        ValidValueAssignmentConsumerElement,
                        ValidValueAssignmentDefinitionElement,
                        ValidValueImplAssetElement,
                        ValidValueImplDefinitionElement,
                        ValidValueMappingElement,
                        ReferenceValueAssignmentDefinitionElement,
                        ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

                response.setGUID(handler.createValidValueSet(userId,
                                                             null,
                                                             null,
                                                             requestBody.getQualifiedName(),
                                                             requestBody.getDisplayName(),
                                                             requestBody.getDescription(),
                                                             requestBody.getUsage(),
                                                             requestBody.getScope(),
                                                             requestBody.isDeprecated(),
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
    public GUIDResponse  createValidValueDefinition(String               serverName,
                                                    String               userId,
                                                    String               setGUID,
                                                    ValidValueProperties requestBody)
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
                ValidValuesHandler<ValidValueElement,
                        ValidValueAssignmentConsumerElement,
                        ValidValueAssignmentDefinitionElement,
                        ValidValueImplAssetElement,
                        ValidValueImplDefinitionElement,
                        ValidValueMappingElement,
                        ReferenceValueAssignmentDefinitionElement,
                        ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

                response.setGUID(handler.createValidValueDefinition(userId,
                                                                    null,
                                                                    null,
                                                                    setGUID,
                                                                    requestBody.getQualifiedName(),
                                                                    requestBody.getDisplayName(),
                                                                    requestBody.getDescription(),
                                                                    requestBody.getUsage(),
                                                                    requestBody.getScope(),
                                                                    requestBody.getPreferredValue(),
                                                                    requestBody.isDeprecated(),
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
    public VoidResponse    updateValidValue(String               serverName,
                                            String               userId,
                                            String               validValueGUID,
                                            ValidValueProperties requestBody)
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
                ValidValuesHandler<ValidValueElement,
                        ValidValueAssignmentConsumerElement,
                        ValidValueAssignmentDefinitionElement,
                        ValidValueImplAssetElement,
                        ValidValueImplDefinitionElement,
                        ValidValueMappingElement,
                        ReferenceValueAssignmentDefinitionElement,
                        ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

                handler.updateValidValue(userId,
                                         null,
                                         null,
                                         validValueGUID,
                                         requestBody.getQualifiedName(),
                                         requestBody.getDisplayName(),
                                         requestBody.getDescription(),
                                         requestBody.getUsage(),
                                         requestBody.getScope(),
                                         requestBody.getPreferredValue(),
                                         requestBody.isDeprecated(),
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
                ValidValuesHandler<ValidValueElement,
                        ValidValueAssignmentConsumerElement,
                        ValidValueAssignmentDefinitionElement,
                        ValidValueImplAssetElement,
                        ValidValueImplDefinitionElement,
                        ValidValueMappingElement,
                        ReferenceValueAssignmentDefinitionElement,
                        ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

                handler.deleteValidValue(userId,
                                         null,
                                         null,
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
    @SuppressWarnings(value = "unused")
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

            ValidValuesHandler<ValidValueElement,
                    ValidValueAssignmentConsumerElement,
                    ValidValueAssignmentDefinitionElement,
                    ValidValueImplAssetElement,
                    ValidValueImplDefinitionElement,
                    ValidValueMappingElement,
                    ReferenceValueAssignmentDefinitionElement,
                    ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            handler.attachValidValueToSet(userId, null, null, setGUID, validValueGUID, methodName);
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

            ValidValuesHandler<ValidValueElement,
                    ValidValueAssignmentConsumerElement,
                    ValidValueAssignmentDefinitionElement,
                    ValidValueImplAssetElement,
                    ValidValueImplDefinitionElement,
                    ValidValueMappingElement,
                    ReferenceValueAssignmentDefinitionElement,
                    ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            handler.detachValidValueFromSet(userId, null, null, setGUID, validValueGUID, methodName);
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
     * @param requestBody implementation relationship properties
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse  linkValidValueToImplementation(String                    serverName,
                                                        String                    userId,
                                                        String                    validValueGUID,
                                                        String                    assetGUID,
                                                        ValidValuesImplProperties requestBody)
    {
        final String   methodName = "linkValidValueToImplementation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse        response            = new VoidResponse();
        AuditLog            auditLog            = null;
        String              symbolicName        = null;
        String              implementationValue = null;
        Map<String, String> additionalValues    = null;

        if (requestBody != null)
        {
            symbolicName = requestBody.getSymbolicName();
            implementationValue = requestBody.getImplementationValue();
            additionalValues = requestBody.getAdditionalValues();
        }

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement,
                    ValidValueAssignmentConsumerElement,
                    ValidValueAssignmentDefinitionElement,
                    ValidValueImplAssetElement,
                    ValidValueImplDefinitionElement,
                    ValidValueMappingElement,
                    ReferenceValueAssignmentDefinitionElement,
                    ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            handler.linkValidValueToImplementation(userId,
                                                   null,
                                                   null,
                                                   validValueGUID,
                                                   assetGUID,
                                                   symbolicName,
                                                   implementationValue,
                                                   additionalValues,
                                                   methodName);
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
    @SuppressWarnings(value = "unused")
    public VoidResponse  classifyAssetAsReferenceData(String          serverName,
                                                      String          userId,
                                                      String          assetGUID,
                                                      NullRequestBody requestBody)
    {
        final String assetGUIDParameterName = "assetGUID";
        final String methodName             = "classifyAssetAsReferenceData";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<ReferenceDataAssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            handler.classifyAssetAsReferenceData(userId, assetGUID, assetGUIDParameterName, methodName);
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
    @SuppressWarnings(value = "unused")
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

            ValidValuesHandler<ValidValueElement,
                    ValidValueAssignmentConsumerElement,
                    ValidValueAssignmentDefinitionElement,
                    ValidValueImplAssetElement,
                    ValidValueImplDefinitionElement,
                    ValidValueMappingElement,
                    ReferenceValueAssignmentDefinitionElement,
                    ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            handler.unlinkValidValueFromImplementation(userId, null, null, validValueGUID, assetGUID, methodName);
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
    @SuppressWarnings(value = "unused")
    public VoidResponse  declassifyAssetAsReferenceData(String          serverName,
                                                        String          userId,
                                                        String          assetGUID,
                                                        NullRequestBody requestBody)
    {
        final String assetGUIDParameterName = "assetGUID";
        final String methodName             = "declassifyAssetAsReferenceData";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            handler.declassifyAssetAsReferenceData(userId, assetGUID, assetGUIDParameterName, methodName);
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
     * @param requestBody request body supplied to pass the strictRequirement flag
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse    assignValidValueToConsumer(String                         serverName,
                                                      String                         userId,
                                                      String                         validValueGUID,
                                                      String                         consumerGUID,
                                                      ValidValueAssignmentProperties requestBody)
    {
        final String   methodName = "assignValidValueToConsumer";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        boolean strictRequirement = false;
        if (requestBody != null)
        {
            strictRequirement = requestBody.isStrictRequirement();
        }

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement,
                    ValidValueAssignmentConsumerElement,
                    ValidValueAssignmentDefinitionElement,
                    ValidValueImplAssetElement,
                    ValidValueImplDefinitionElement,
                    ValidValueMappingElement,
                    ReferenceValueAssignmentDefinitionElement,
                    ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            handler.assignValidValueToConsumer(userId, null, null, validValueGUID, consumerGUID, strictRequirement, methodName);
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
    @SuppressWarnings(value = "unused")
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

            ValidValuesHandler<ValidValueElement,
                    ValidValueAssignmentConsumerElement,
                    ValidValueAssignmentDefinitionElement,
                    ValidValueImplAssetElement,
                    ValidValueImplDefinitionElement,
                    ValidValueMappingElement,
                    ReferenceValueAssignmentDefinitionElement,
                    ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            handler.unassignValidValueFromConsumer(userId, null, null, validValueGUID, consumerGUID, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Link a valid value as a reference value to a referencable to act as a tag/classification to help with locating and
     * grouping the referenceable.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param referenceableGUID unique identifier of the element to link to.
     * @param requestBody properties for the relationship.
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse    assignReferenceValueToItem(String                   serverName,
                                                      String                   userId,
                                                      String                   validValueGUID,
                                                      String                   referenceableGUID,
                                                      ReferenceValueAssignmentProperties requestBody)
    {
        final String   methodName = "assignReferenceValueToItem";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;
        int          confidence = 0;
        String       steward = null;
        String       notes = null;

        if (requestBody != null)
        {
            confidence = requestBody.getConfidence();
            steward    = requestBody.getSteward();
            notes      = requestBody.getNotes();
        }

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement,
                    ValidValueAssignmentConsumerElement,
                    ValidValueAssignmentDefinitionElement,
                    ValidValueImplAssetElement,
                    ValidValueImplDefinitionElement,
                    ValidValueMappingElement,
                    ReferenceValueAssignmentDefinitionElement,
                    ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            handler.assignReferenceValueToItem(userId,
                                               null,
                                               null,
                                               validValueGUID,
                                               referenceableGUID,
                                               confidence,
                                               steward,
                                               notes,
                                               methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the reference value link between a valid value and a referenceable (item).
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param referenceableGUID unique identifier of the element to remove the link from.
     * @param requestBody null request body supplied to satisfy REST protocol
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse unassignReferenceValueFromItem(String          serverName,
                                                       String          userId,
                                                       String          validValueGUID,
                                                       String          referenceableGUID,
                                                       NullRequestBody requestBody)
    {
        final String   methodName = "unassignReferenceValueFromItem";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement,
                    ValidValueAssignmentConsumerElement,
                    ValidValueAssignmentDefinitionElement,
                    ValidValueImplAssetElement,
                    ValidValueImplDefinitionElement,
                    ValidValueMappingElement,
                    ReferenceValueAssignmentDefinitionElement,
                    ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            handler.unassignReferenceValueFromItem(userId,
                                                   null,
                                                   null,
                                                   validValueGUID,
                                                   referenceableGUID,
                                                   methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Link together 2 valid values from different sets that have equivalent values/meanings.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValue1GUID unique identifier of the valid value.
     * @param validValue2GUID unique identifier of the other valid value to link to.
     * @param requestBody properties for the relationship.
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse    mapValidValues(String                       serverName,
                                          String                       userId,
                                          String                       validValue1GUID,
                                          String                       validValue2GUID,
                                          ValidValuesMappingProperties requestBody)
    {
        final String   methodName = "mapValidValues";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;
        String       associationDescription = null;
        int          confidence = 0;
        String       steward = null;
        String       notes = null;

        if (requestBody != null)
        {
            associationDescription = requestBody.getAssociationDescription();
            confidence             = requestBody.getConfidence();
            steward                = requestBody.getSteward();
            notes                  = requestBody.getNotes();
        }

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement,
                    ValidValueAssignmentConsumerElement,
                    ValidValueAssignmentDefinitionElement,
                    ValidValueImplAssetElement,
                    ValidValueImplDefinitionElement,
                    ValidValueMappingElement,
                    ReferenceValueAssignmentDefinitionElement,
                    ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            handler.mapValidValues(userId,
                                   null,
                                   null,
                                   validValue1GUID,
                                   validValue2GUID,
                                   associationDescription,
                                   confidence,
                                   steward,
                                   notes,
                                   methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the reference value link between a valid value and a referenceable (item).
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValue1GUID unique identifier of the valid value.
     * @param validValue2GUID unique identifier of the other valid value element to remove the link from.
     * @param requestBody null request body supplied to satisfy REST protocol
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse  unmapValidValues(String          serverName,
                                          String          userId,
                                          String          validValue1GUID,
                                          String          validValue2GUID,
                                          NullRequestBody requestBody)
    {
        final String   methodName = "unmapValidValues";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement,
                    ValidValueAssignmentConsumerElement,
                    ValidValueAssignmentDefinitionElement,
                    ValidValueImplAssetElement,
                    ValidValueImplDefinitionElement,
                    ValidValueMappingElement,
                    ReferenceValueAssignmentDefinitionElement,
                    ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            handler.unmapValidValues(userId, null, null, validValue1GUID, validValue2GUID, methodName);
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

            ValidValuesHandler<ValidValueElement,
                    ValidValueAssignmentConsumerElement,
                    ValidValueAssignmentDefinitionElement,
                    ValidValueImplAssetElement,
                    ValidValueImplDefinitionElement,
                    ValidValueMappingElement,
                    ReferenceValueAssignmentDefinitionElement,
                    ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            ValidValueElement validValue = handler.getValidValueByGUID(userId, validValueGUID, methodName);
            response.setElement(validValue);
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
                ValidValuesHandler<ValidValueElement,
                        ValidValueAssignmentConsumerElement,
                        ValidValueAssignmentDefinitionElement,
                        ValidValueImplAssetElement,
                        ValidValueImplDefinitionElement,
                        ValidValueMappingElement,
                        ReferenceValueAssignmentDefinitionElement,
                        ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

                List<ValidValueElement>  validValues = handler.getValidValueByName(userId,
                                                                                   validValueName,
                                                                                   nameParameterName,
                                                                                   startFrom,
                                                                                   pageSize,
                                                                                   methodName);
                response.setElementList(validValues);
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
        final String   searchStringParameterName = "searchString";
        final String   methodName                = "findValidValues";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValuesResponse response = new ValidValuesResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (searchString != null)
            {
                ValidValuesHandler<ValidValueElement,
                        ValidValueAssignmentConsumerElement,
                        ValidValueAssignmentDefinitionElement,
                        ValidValueImplAssetElement,
                        ValidValueImplDefinitionElement,
                        ValidValueMappingElement,
                        ReferenceValueAssignmentDefinitionElement,
                        ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

                List<ValidValueElement>  validValues = handler.findValidValues(userId,
                                                                               searchString,
                                                                               searchStringParameterName,
                                                                               startFrom,
                                                                               pageSize,
                                                                               methodName);
                response.setElementList(validValues);
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
        final String validValueGUIDParameter = "validValueSetGUID";
        final String   methodName = "getValidValueSetMembers";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValuesResponse response = new ValidValuesResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement,
                    ValidValueAssignmentConsumerElement,
                    ValidValueAssignmentDefinitionElement,
                    ValidValueImplAssetElement,
                    ValidValueImplDefinitionElement,
                    ValidValueMappingElement,
                    ReferenceValueAssignmentDefinitionElement,
                    ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            List<ValidValueElement>  validValues = handler.getValidValueSetMembers(userId,
                                                                                   validValueSetGUID,
                                                                                   validValueGUIDParameter,
                                                                                   startFrom,
                                                                                   pageSize,
                                                                                   methodName);
            response.setElementList(validValues);
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
        final String validValueGUIDParameter = "validValueGUID";
        final String methodName              = "getSetsForValidValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValuesResponse response = new ValidValuesResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement,
                    ValidValueAssignmentConsumerElement,
                    ValidValueAssignmentDefinitionElement,
                    ValidValueImplAssetElement,
                    ValidValueImplDefinitionElement,
                    ValidValueMappingElement,
                    ReferenceValueAssignmentDefinitionElement,
                    ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            List<ValidValueElement>  validValues = handler.getSetsForValidValue(userId,
                                                                                validValueGUID,
                                                                                validValueGUIDParameter,
                                                                                startFrom,
                                                                                pageSize,
                                                                                methodName);
            response.setElementList(validValues);
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
    public ValidValueAssignmentConsumersResponse getValidValuesAssignmentConsumers(String   serverName,
                                                                                   String   userId,
                                                                                   String   validValueGUID,
                                                                                   int      startFrom,
                                                                                   int      pageSize)
    {
        final String validValueGUIDParameter = "validValueGUID";
        final String methodName              = "getValidValuesAssignmentConsumers";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValueAssignmentConsumersResponse response = new ValidValueAssignmentConsumersResponse();
        AuditLog                              auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement,
                    ValidValueAssignmentConsumerElement,
                    ValidValueAssignmentDefinitionElement,
                    ValidValueImplAssetElement,
                    ValidValueImplDefinitionElement,
                    ValidValueMappingElement,
                    ReferenceValueAssignmentDefinitionElement,
                    ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            List<ValidValueAssignmentConsumerElement>  validValueConsumers = handler.getValidValuesAssignmentConsumers(userId,
                                                                                                                       validValueGUID,
                                                                                                                       validValueGUIDParameter,
                                                                                                                       instanceHandler.getSupportedZones(userId,
                                                                                                                                                         serverName,
                                                                                                                                                         methodName),
                                                                                                                       startFrom,
                                                                                                                       pageSize,
                                                                                                                       methodName);

            response.setElementList(validValueConsumers);

        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Page through the list of valid values assigned to referenceable element.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param referenceableGUID unique identifier of anchoring referenceable
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of consumers beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public ValidValueAssignmentDefinitionsResponse getValidValuesAssignmentDefinition(String   serverName,
                                                                                      String   userId,
                                                                                      String   referenceableGUID,
                                                                                      int      startFrom,
                                                                                      int      pageSize)
    {
        final String referenceableGUIDParameter = "referencableGUID";
        final String methodName                 = "getValidValuesAssignmentDefinition";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValueAssignmentDefinitionsResponse response = new ValidValueAssignmentDefinitionsResponse();
        AuditLog                                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement,
                    ValidValueAssignmentConsumerElement,
                    ValidValueAssignmentDefinitionElement,
                    ValidValueImplAssetElement,
                    ValidValueImplDefinitionElement,
                    ValidValueMappingElement,
                    ReferenceValueAssignmentDefinitionElement,
                    ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            List<ValidValueAssignmentDefinitionElement>  validValueAssignmentDefinitions = handler.getValidValuesAssignmentDefinition(userId,
                                                                                                                                      referenceableGUID,
                                                                                                                                      referenceableGUIDParameter,
                                                                                                                                      startFrom,
                                                                                                                                      pageSize,
                                                                                                                                      methodName);

            response.setElementList(validValueAssignmentDefinitions);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Page through the list of implementations for a valid value.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of asset beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public ValidValuesImplAssetsResponse getValidValuesImplementationAssets(String   serverName,
                                                                            String   userId,
                                                                            String   validValueGUID,
                                                                            int      startFrom,
                                                                            int      pageSize)
    {
        final String   methodName = "getValidValuesImplementationAssets";
        final String   validValueGUIDParameterName = "validValueGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValuesImplAssetsResponse response = new ValidValuesImplAssetsResponse();
        AuditLog                      auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement,
                    ValidValueAssignmentConsumerElement,
                    ValidValueAssignmentDefinitionElement,
                    ValidValueImplAssetElement,
                    ValidValueImplDefinitionElement,
                    ValidValueMappingElement,
                    ReferenceValueAssignmentDefinitionElement,
                    ReferenceValueAssignmentItemElement> validValuesHandler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            List<ValidValueImplAssetElement> validValueImplAssetElements = validValuesHandler.getValidValuesImplementationAssets(userId,
                                                                                                                                  validValueGUID,
                                                                                                                                  validValueGUIDParameterName,
                                                                                                                                  instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                                                                                  startFrom,
                                                                                                                                  pageSize,
                                                                                                                                  methodName);

            response.setElementList(validValueImplAssetElements);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Page through the list of valid values defining the content of a reference data asset.
     * This is always called from the assetHandler after it has checked that the asset is in the right zone.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param assetGUID unique identifier of asset to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of valid value beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public ValidValuesImplDefinitionsResponse getValidValuesImplementationDefinitions(String serverName,
                                                                                      String userId,
                                                                                      String assetGUID,
                                                                                      int    startFrom,
                                                                                      int    pageSize)
    {
        final String assetGUIDParameterName = "assetGUID";
        final String methodName             = "getValidValuesImplementationDefinitions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValuesImplDefinitionsResponse response = new ValidValuesImplDefinitionsResponse();
        AuditLog                           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement,
                    ValidValueAssignmentConsumerElement,
                    ValidValueAssignmentDefinitionElement,
                    ValidValueImplAssetElement,
                    ValidValueImplDefinitionElement,
                    ValidValueMappingElement,
                    ReferenceValueAssignmentDefinitionElement,
                    ReferenceValueAssignmentItemElement> validValuesHandler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            List<ValidValueImplDefinitionElement>  definitions =
                    validValuesHandler.getValidValuesImplementationDefinitions(userId,
                                                                               assetGUID,
                                                                               assetGUIDParameterName,
                                                                               startFrom,
                                                                               pageSize,
                                                                               methodName);

            response.setElementList(definitions);

        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Page through the list of mappings for a valid value.  These are other valid values from different valid value sets that are equivalent
     * in some way.  The association description covers the type of association.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of mappings to other valid value beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public ValidValueMappingsResponse getValidValueMappings(String serverName,
                                                            String userId,
                                                            String validValueGUID,
                                                            int    startFrom,
                                                            int    pageSize)
    {
        final String validValueGUIDParameter = "validValueGUID";
        final String   methodName = "getValidValueMappings";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValueMappingsResponse response = new ValidValueMappingsResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement,
                    ValidValueAssignmentConsumerElement,
                    ValidValueAssignmentDefinitionElement,
                    ValidValueImplAssetElement,
                    ValidValueImplDefinitionElement,
                    ValidValueMappingElement,
                    ReferenceValueAssignmentDefinitionElement,
                    ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            List<ValidValueMappingElement>  validValuesMappings = handler.getValidValueMappings(userId,
                                                                                                validValueGUID,
                                                                                                validValueGUIDParameter,
                                                                                                startFrom,
                                                                                                pageSize,
                                                                                                methodName);

            response.setElementList(validValuesMappings);

        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Page through the list of mapping relationships associated with a valid value.
     * These are other valid values from different valid value sets that are equivalent
     * in some way.  The association description covers the type of association.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of mappings to other valid value beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public ValidValuesMappingsResponse getValidValuesMappings(String serverName,
                                                              String userId,
                                                              String validValueGUID,
                                                              int    startFrom,
                                                              int    pageSize)
    {
        final String validValueGUIDParameter = "validValueGUID";
        final String methodName              = "getValidValuesMappings";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValuesMappingsResponse response = new ValidValuesMappingsResponse();
        AuditLog                    auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement,
                    ValidValueAssignmentConsumerElement,
                    ValidValueAssignmentDefinitionElement,
                    ValidValueImplAssetElement,
                    ValidValueImplDefinitionElement,
                    ValidValueMappingElement,
                    ReferenceValueAssignmentDefinitionElement,
                    ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            ValidValueElement  validValueElement = handler.getValidValueByGUID(userId, validValueGUID, methodName);

            List<ValidValueMappingElement>  validValuesMappings = handler.getValidValueMappings(userId,
                                                                                                validValueGUID,
                                                                                                validValueGUIDParameter,
                                                                                                startFrom,
                                                                                                pageSize,
                                                                                                methodName);

            if ((validValuesMappings != null) && (! validValuesMappings.isEmpty()))
            {
                List<ValidValuesMappingElement> elements = new ArrayList<>();

                for (ValidValueMappingElement validValueMapping : validValuesMappings)
                {
                    if (validValueMapping != null)
                    {
                        ValidValuesMappingElement validValuesMappingElement = new ValidValuesMappingElement();

                        if (validValueMapping.getValidValueElement() != null)
                        {
                            List<ValidValueElement> validValueElements = new ArrayList<>();

                            validValueElements.add(validValueElement);
                            validValueElements.add(validValueMapping.getValidValueElement());
                            validValuesMappingElement.setValidValueElements(validValueElements);
                        }

                        validValuesMappingElement.setAssociationDescription(validValueMapping.getAssociationDescription());
                        validValuesMappingElement.setConfidence(validValueMapping.getConfidence());
                        validValuesMappingElement.setSteward(validValueMapping.getSteward());
                        validValuesMappingElement.setNotes(validValueMapping.getNotes());

                        elements.add(validValuesMappingElement);
                    }
                }

                if (! elements.isEmpty())
                {
                    response.setElementList(elements);
                }
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
     * Page through the list of referenceables that have this valid value as a reference value.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of referenceable beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public ReferenceValueAssignmentItemsResponse getReferenceValueAssignedItems(String serverName,
                                                                                String userId,
                                                                                String validValueGUID,
                                                                                int    startFrom,
                                                                                int    pageSize)
    {
        final String validValueGUIDParameter = "validValueGUID";
        final String methodName              = "getReferenceValueAssignedItems";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ReferenceValueAssignmentItemsResponse response = new ReferenceValueAssignmentItemsResponse();
        AuditLog                              auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement,
                    ValidValueAssignmentConsumerElement,
                    ValidValueAssignmentDefinitionElement,
                    ValidValueImplAssetElement,
                    ValidValueImplDefinitionElement,
                    ValidValueMappingElement,
                    ReferenceValueAssignmentDefinitionElement,
                    ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            List<ReferenceValueAssignmentItemElement>  assignedItems = handler.getReferenceValueAssignedItems(userId,
                                                                                                              validValueGUID,
                                                                                                              validValueGUIDParameter,
                                                                                                              instanceHandler.getSupportedZones(userId,
                                                                                                                                                serverName,
                                                                                                                                                methodName),
                                                                                                              startFrom,
                                                                                                              pageSize,
                                                                                                              methodName);

            response.setElementList(assignedItems);

        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Page through the list of assigned reference values for a referenceable.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param referenceableGUID unique identifier of assigned item
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of valid value beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public ReferenceValueAssignmentDefinitionsResponse getReferenceValueAssignments(String serverName,
                                                                                    String userId,
                                                                                    String referenceableGUID,
                                                                                    int    startFrom,
                                                                                    int    pageSize)
    {
        final String   guidParameterName = "referenceableGUID";
        final String   methodName = "getValidValuesAssignmentDefinition";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ReferenceValueAssignmentDefinitionsResponse response = new ReferenceValueAssignmentDefinitionsResponse();
        AuditLog                                    auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValuesHandler<ValidValueElement,
                    ValidValueAssignmentConsumerElement,
                    ValidValueAssignmentDefinitionElement,
                    ValidValueImplAssetElement,
                    ValidValueImplDefinitionElement,
                    ValidValueMappingElement,
                    ReferenceValueAssignmentDefinitionElement,
                    ReferenceValueAssignmentItemElement> handler = instanceHandler.getValidValuesHandler(userId, serverName, methodName);

            List<ReferenceValueAssignmentDefinitionElement>  referenceValueAssignments = handler.getReferenceValueAssignments(userId,
                                                                                                                              referenceableGUID,
                                                                                                                              guidParameterName,
                                                                                                                              startFrom,
                                                                                                                              pageSize,
                                                                                                                              methodName);

            response.setElementList(referenceValueAssignments);

        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Convert the OCF element header into a digital architecture header.
     *
     * @param bean referenceable bean
     * @return element header
     */
    private ElementHeader getElementHeader(Referenceable   bean)
    {
        ElementHeader header = null;

        if (bean != null)
        {
            header = new ElementHeader();

            header.setGUID(bean.getGUID());

            if (bean.getType() != null)
            {
                ElementType type = new ElementType();
                ElementOrigin origin = new ElementOrigin();

                type.setTypeId(bean.getType().getElementTypeId());
                type.setTypeName(bean.getType().getElementTypeName());
                type.setTypeDescription(bean.getType().getElementTypeDescription());
                type.setTypeVersion(bean.getType().getElementTypeVersion());
                type.setSuperTypeNames(bean.getType().getElementSuperTypeNames());

                origin.setHomeMetadataCollectionId(bean.getType().getElementMetadataCollectionId());
                origin.setHomeMetadataCollectionName(bean.getType().getElementMetadataCollectionName());
                origin.setLicense(bean.getType().getElementLicense());
                origin.setSourceServer(bean.getType().getElementSourceServer());

                if (bean.getType().getElementOrigin() != null)
                switch (bean.getType().getElementOrigin())
                {
                    case UNKNOWN:
                        origin.setOriginCategory(ElementOriginCategory.UNKNOWN);
                        break;

                    case LOCAL_COHORT:
                        origin.setOriginCategory(ElementOriginCategory.LOCAL_COHORT);
                        break;

                    case EXPORT_ARCHIVE:
                        origin.setOriginCategory(ElementOriginCategory.EXPORT_ARCHIVE);
                        break;

                    case CONTENT_PACK:
                        origin.setOriginCategory(ElementOriginCategory.CONTENT_PACK);
                        break;

                    case DEREGISTERED_REPOSITORY:
                        origin.setOriginCategory(ElementOriginCategory.DEREGISTERED_REPOSITORY);
                        break;

                    case CONFIGURATION:
                        origin.setOriginCategory(ElementOriginCategory.CONFIGURATION);
                        break;

                    case EXTERNAL_SOURCE:
                        origin.setOriginCategory(ElementOriginCategory.EXTERNAL_SOURCE);
                        break;
                }

                header.setType(type);
                header.setOrigin(origin);
            }
        }

        return header;
    }


    /**
     * Convert the OCF classification list into a digital architecture classification list.
     *
     * @param bean referenceable bean
     * @return list of classifications
     */
    private List<ElementClassification> getClassifications(Referenceable   bean)
    {
        List<ElementClassification> classifications = null;
        if (bean != null)
        {
            if (bean.getClassifications() != null)
            {
                classifications = new ArrayList<>();

                for (org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementClassification classification : bean.getClassifications())
                {
                    if (classification != null)
                    {
                        ElementClassification result = new ElementClassification();

                        result.setClassificationName(classification.getClassificationName());
                        result.setClassificationProperties(classification.getClassificationProperties());

                        classifications.add(result);
                    }
                }

                if (classifications.isEmpty())
                {
                    classifications = null;
                }
            }
        }

        return classifications;
    }


    /**
     * Convert the OCF meanings list into a digital architecture list of guids.
     *
     * @param bean referenceable bean
     * @return list of guids
     */
    private List<String> getMeanings(Referenceable bean)
    {
        List<String> meanings = null;
        if (bean != null)
        {
            if (bean.getMeanings() != null)
            {
                meanings = new ArrayList<>();

                for (Meaning meaning : bean.getMeanings())
                {
                    if (meaning != null)
                    {
                        meanings.add(meaning.getGUID());
                    }
                }

                if (meanings.isEmpty())
                {
                    meanings = null;
                }
            }
        }

        return meanings;
    }


    /**
     * Convert the OCF valid value bean into a digital architecture valid value element.
     *
     * @param validValue OCF bean
     * @return valid value element
     */
    private ValidValueElement getValidValue(ValidValue validValue)
    {
        ValidValueElement validValueElement = null;

        if (validValue != null)
        {
            validValueElement = new ValidValueElement();

            validValueElement.setElementHeader(this.getElementHeader(validValue));
            validValueElement.setQualifiedName(validValue.getQualifiedName());
            validValueElement.setDisplayName(validValue.getDisplayName());
            validValueElement.setDescription(validValue.getDescription());
            validValueElement.setDeprecated(validValue.isDeprecated());
            validValueElement.setScope(validValue.getScope());
            validValueElement.setUsage(validValue.getUsage());
            validValueElement.setPreferredValue(validValue.getPreferredValue());

            validValueElement.setTypeName(validValue.getType().getElementTypeName());
            validValueElement.setAdditionalProperties(validValue.getAdditionalProperties());
            validValueElement.setExtendedProperties(validValue.getExtendedProperties());
            validValueElement.setClassifications(this.getClassifications(validValue));
            validValueElement.setMeanings(this.getMeanings(validValue));
        }

        return validValueElement;
    }


    /**
     * Convert the OCF valid value list into a digital architecture valid value element list.
     *
     * @param validValueBeans list of OCF beans
     * @return list of valid value elements
     */
    private List<ValidValueElement> getValidValues(List<ValidValue> validValueBeans)
    {
        List<ValidValueElement> validValueElements = null;

        if (validValueBeans != null)
        {
            validValueElements = new ArrayList<>();

            for (ValidValue validValue : validValueBeans)
            {
                if (validValue != null)
                {
                    validValueElements.add(this.getValidValue(validValue));
                }
            }

            if (validValueElements.isEmpty())
            {
                validValueElements = null;
            }
        }

        return validValueElements;
    }
}
