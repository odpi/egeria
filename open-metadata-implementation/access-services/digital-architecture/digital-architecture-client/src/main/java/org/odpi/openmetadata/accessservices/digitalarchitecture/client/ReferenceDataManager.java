/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.client;

import org.odpi.openmetadata.accessservices.digitalarchitecture.api.ManageReferenceData;
import org.odpi.openmetadata.accessservices.digitalarchitecture.client.rest.DigitalArchitectureRESTClient;
import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.*;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.*;
import org.odpi.openmetadata.accessservices.digitalarchitecture.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;


import java.util.List;
import java.util.Map;

/**
 * ReferenceDataManager provides the API operations to create and maintain lists of valid
 * value definitions grouped into a valid value set.  Both valid value definitions and valid value sets have
 * the same attributes and so inherit from ValidValue where all the attributes are defined.
 *
 * A set is just grouping of valid values.   Valid value definitions and set can be nested many times in other
 * valid value sets.
 */
public class ReferenceDataManager extends DigitalArchitectureClientBase implements ManageReferenceData
{
    /**
     * Create a new client with no authentication embedded in the HTTP request and an audit log.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ReferenceDataManager(String   serverName,
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
    public ReferenceDataManager(String serverName,
                                String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     * There is also an audit log destination.
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
    public ReferenceDataManager(String   serverName,
                                String   serverPlatformURLRoot,
                                String   userId,
                                String   password,
                                AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog);
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
    public ReferenceDataManager(String serverName,
                                String serverPlatformURLRoot,
                                String userId,
                                String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server (view service or integration service typically).
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ReferenceDataManager(String                        serverName,
                                String                        serverPlatformURLRoot,
                                DigitalArchitectureRESTClient restClient,
                                int                           maxPageSize,
                                AuditLog                      auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize, auditLog);
    }


    /*
     * ==============================================
     * ManageReferenceData
     * ==============================================
     */

    /**
     * Create a new valid value set.  This just creates the Set itself.  Members are added either as they are
     * created, or they can be attached to a set after they are created.
     *
     * @param userId calling user.
     * @param qualifiedName unique name.
     * @param displayName displayable descriptive name.
     * @param description further information.
     * @param usage how/when should this set be used.
     * @param scope what is the scope of this set's values.
     * @param additionalProperties additional properties for this set.
     * @param typeName name of subtype of the definition (or null to use the standard open type)
     * @param extendedProperties properties that need to be populated into a subtype (or null for the standard open type).
     *
     * @return unique identifier for the new set
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public String  createValidValueSet(String              userId,
                                       String              qualifiedName,
                                       String              displayName,
                                       String              description,
                                       String              usage,
                                       String              scope,
                                       Map<String, String> additionalProperties,
                                       String              typeName,
                                       Map<String, Object> extendedProperties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String   methodName = "createValidValueSet";
        final String   nameParameter = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/valid-values/sets";

        ValidValueProperties requestBody = new ValidValueProperties();
        requestBody.setQualifiedName(qualifiedName);
        requestBody.setDisplayName(displayName);
        requestBody.setDescription(description);
        requestBody.setUsage(usage);
        requestBody.setScope(scope);
        requestBody.setAdditionalProperties(additionalProperties);
        requestBody.setTypeName(typeName);
        requestBody.setExtendedProperties(extendedProperties);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Create a new valid value definition.
     *
     * @param userId calling user.
     * @param setGUID unique identifier of the set to attach this to.
     * @param isDefaultValue     is this the default value for the set?
     * @param qualifiedName unique name.
     * @param displayName displayable descriptive name.
     * @param description further information.
     * @param usage how/when should this value be used.
     * @param preferredValue the value that should be used in an implementation if possible.
     * @param additionalProperties additional properties for this definition.
     * @param typeName name of subtype of the definition (or null to use the standard open type)
     * @param extendedProperties properties that need to be populated into a subtype (or null for the standard open type).
     *
     * @return unique identifier for the new definition
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public String  createValidValueDefinition(String              userId,
                                              String              setGUID,
                                              boolean             isDefaultValue,
                                              String              qualifiedName,
                                              String              displayName,
                                              String              description,
                                              String              usage,
                                              String              scope,
                                              String              preferredValue,
                                              Map<String, String> additionalProperties,
                                              String              typeName,
                                              Map<String, Object> extendedProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String   methodName = "createValidValueDefinition";
        final String   nameParameter = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/valid-values/sets/{2}?isDefaultValue={3}";

        ValidValueProperties requestBody = new ValidValueProperties();
        requestBody.setQualifiedName(qualifiedName);
        requestBody.setDisplayName(displayName);
        requestBody.setDescription(description);
        requestBody.setUsage(usage);
        requestBody.setScope(scope);
        requestBody.setPreferredValue(preferredValue);
        requestBody.setAdditionalProperties(additionalProperties);
        requestBody.setTypeName(typeName);
        requestBody.setExtendedProperties(extendedProperties);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  setGUID,
                                                                  isDefaultValue);

        return restResult.getGUID();
    }


    /**
     * Update the properties of the valid value.  All properties are updated.
     * If only changing some if the properties, retrieve the current values from the repository
     * and pass existing values back on this call if they are not to change.
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param qualifiedName unique name.
     * @param displayName displayable descriptive name.
     * @param description further information.
     * @param usage how/when should this value be used.
     * @param scope what is the scope of the values.
     * @param preferredValue the value that should be used in an implementation if possible.
     * @param isDeprecated is this value deprecated?
     * @param additionalProperties additional properties for this valid value.
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param typeName name of subtype of the definition (or null to use the standard open type)
     * @param extendedProperties properties that need to be populated into a subtype.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public void    updateValidValue(String              userId,
                                    String              validValueGUID,
                                    String              qualifiedName,
                                    String              displayName,
                                    String              description,
                                    String              usage,
                                    String              scope,
                                    String              preferredValue,
                                    boolean             isDeprecated,
                                    Map<String, String> additionalProperties,
                                    boolean             isMergeUpdate,
                                    String              typeName,
                                    Map<String, Object> extendedProperties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String   methodName = "updateValidValue";
        final String   guidParameter = "validValueGUID";
        final String   nameParameter = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, guidParameter, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/valid-values/{2}/update?isMergeUpdate={3}";

        ValidValueProperties requestBody = new ValidValueProperties();
        requestBody.setQualifiedName(qualifiedName);
        requestBody.setDisplayName(displayName);
        requestBody.setDescription(description);
        requestBody.setUsage(usage);
        requestBody.setScope(scope);
        requestBody.setPreferredValue(preferredValue);
        requestBody.setIsDeprecated(isDeprecated);
        requestBody.setAdditionalProperties(additionalProperties);
        requestBody.setExtendedProperties(extendedProperties);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        validValueGUID,
                                        isMergeUpdate);
    }


    /**
     * Remove the valid value form the repository.  All links to it are deleted too.
     *
     * @param userId calling user
     * @param validValueGUID unique identifier of the value to delete
     * @param qualifiedName unique name of the value to delete.  This is used to verify that
     *                      the correct valid value is being deleted.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public void    deleteValidValue(String   userId,
                                    String   validValueGUID,
                                    String   qualifiedName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String   methodName = "deleteValidValue";
        final String   guidParameter = "validValueGUID";
        final String   nameParameter = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, guidParameter, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/valid-values/{2}/delete";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        qualifiedName,
                                        serverName,
                                        userId,
                                        validValueGUID);
    }


    /**
     * Create a link between a valid value set or definition and a set.  This means the valid
     * value is a member of the set.
     *
     * @param userId calling user.
     * @param setGUID unique identifier of the set.
     * @param validValueGUID unique identifier of the valid value to add to the set.
     * @param isDefaultValue     is this the default value for the set?
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public void    attachValidValueToSet(String   userId,
                                         String   setGUID,
                                         String   validValueGUID,
                                         boolean  isDefaultValue) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String   methodName = "attachValidValueToSet";
        final String   setGUIDParameter = "setGUID";
        final String   validValueGUIDParameter = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(setGUID, setGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/valid-values/sets/{2}/members/{3}?isDefaultValue={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        setGUID,
                                        validValueGUID,
                                        isDefaultValue);
    }


    /**
     * Remove the link between a valid value and a set it is a member of.
     *
     * @param userId calling user
     * @param setGUID owning set
     * @param validValueGUID unique identifier of the member to be removed.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public void    detachValidValueFromSet(String   userId,
                                           String   setGUID,
                                           String   validValueGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String   methodName = "detachValidValueFromSet";
        final String   setGUIDParameter = "setGUID";
        final String   validValueGUIDParameter = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(setGUID, setGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/valid-values/sets/{2}/members/{3}/delete";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        setGUID,
                                        validValueGUID);
    }


    /**
     * Link a valid value to an asset that provides the implementation.  Typically, this method is
     * used to link a valid value set to a code table.
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param assetGUID unique identifier of the asset that implements the valid value.
     * @param symbolicName lookup name for valid value
     * @param implementationValue value used in implementation
     * @param additionalValues additional values stored under the symbolic name
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public void  linkValidValueToImplementation(String              userId,
                                                String              validValueGUID,
                                                String              assetGUID,
                                                String              symbolicName,
                                                String              implementationValue,
                                                Map<String, String> additionalValues) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String   methodName = "linkValidValueToImplementation";
        final String   validValueGUIDParameter = "validValueGUID";
        final String   assetGUIDParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/valid-values/{2}/implementations/{3}";

        ValidValuesImplProperties requestBody = new ValidValuesImplProperties();

        requestBody.setSymbolicName(symbolicName);
        requestBody.setImplementationValue(implementationValue);
        requestBody.setAdditionalValues(additionalValues);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        validValueGUID,
                                        assetGUID);
    }


    /**
     * Add the ReferenceData classification to an asset.  If the asset is already classified
     * in this way, the method is a no-op.
     *
     * @param userId calling user.
     * @param assetGUID unique identifier of the asset that contains reference data.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public void  classifyAssetAsReferenceData(String  userId,
                                              String  assetGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String   methodName = "classifyAssetAsReferenceData";
        final String   assetGUIDParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/assets/classify-as-reference-data";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        assetGUID);
    }


    /**
     * Remove the link between a valid value and an implementing asset.
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param assetGUID unique identifier of the asset that used to implement the valid value.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public void  unlinkValidValueFromImplementation(String   userId,
                                                    String   validValueGUID,
                                                    String   assetGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String   methodName = "unlinkValidValueFromImplementation";
        final String   validValueGUIDParameter = "validValueGUID";
        final String   assetGUIDParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/valid-values/{2}/implementations/{3}/delete";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        validValueGUID,
                                        assetGUID);
    }


    /**
     * Remove the ReferenceData classification form an Asset.  If the asset was not classified in this way,
     * this call is a no-op.
     *
     * @param userId calling user.
     * @param assetGUID unique identifier of asset.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public void  declassifyAssetAsReferenceData(String  userId,
                                                String  assetGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String   methodName = "declassifyAssetAsReferenceData";
        final String   assetGUIDParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/assets/declassify-as-reference-data";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        assetGUID);
    }


    /**
     * Link a valid value typically to a schema element or glossary term to show that it uses
     * the valid values.
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param consumerGUID unique identifier of the element to link to.
     * @param strictRequirement the valid values defines the only values that are permitted.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public void    assignValidValueToConsumer(String   userId,
                                              String   validValueGUID,
                                              String   consumerGUID,
                                              boolean  strictRequirement) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String   methodName = "assignValidValueToConsumer";
        final String   validValueGUIDParameter = "validValueGUID";
        final String   consumerGUIDParameter = "consumerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(consumerGUID, consumerGUIDParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/valid-values/{2}/consumers/{3}";

        ValidValueAssignmentProperties requestBody = new ValidValueAssignmentProperties();
        requestBody.setStrictRequirement(strictRequirement);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        validValueGUID,
                                        consumerGUID);
    }


    /**
     * Remove the link between a valid value and a consumer.
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param consumerGUID unique identifier of the element to remove the link from.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public void    unassignValidValueFromConsumer(String   userId,
                                                  String   validValueGUID,
                                                  String   consumerGUID) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String   methodName = "unassignValidValueFromConsumer";
        final String   validValueGUIDParameter = "validValueGUID";
        final String   consumerGUIDParameter = "consumerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(consumerGUID, consumerGUIDParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/valid-values/{2}/consumers/{3}/delete";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        validValueGUID,
                                        consumerGUID);
    }


    /**
     * Link a valid value as a reference value to a referencable to act as a tag/classification to help with locating and
     * grouping the referenceable.
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param referenceableGUID unique identifier of the element to link to.
     * @param confidence how confident is the steward that this mapping is correct (0-100).
     * @param steward identifier of steward
     * @param notes additional notes from the steward
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public void    assignReferenceValueToItem(String   userId,
                                              String   validValueGUID,
                                              String   referenceableGUID,
                                              int      confidence,
                                              String   steward,
                                              String   notes) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String   methodName = "assignReferenceValueToItem";
        final String   validValueGUIDParameter = "validValueGUID";
        final String   referenceableGUIDParameter = "referenceableGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(referenceableGUID, referenceableGUIDParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/reference-values/{2}/items/{3}";

        ReferenceValueAssignmentProperties requestBody = new ReferenceValueAssignmentProperties();
        requestBody.setConfidence(confidence);
        requestBody.setSteward(steward);
        requestBody.setNotes(notes);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        validValueGUID,
                                        referenceableGUID);
    }


    /**
     * Remove the reference value link between a valid value and a referenceable (item).
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param referenceableGUID unique identifier of the element to remove the link from.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public void    unassignReferenceValueFromItem(String   userId,
                                                  String   validValueGUID,
                                                  String   referenceableGUID) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String   methodName = "unassignReferenceValueFromItem";
        final String   validValueGUIDParameter = "validValueGUID";
        final String   referenceableGUIDParameter = "referenceableGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(referenceableGUID, referenceableGUIDParameter, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/reference-values/{2}/items/{3}/delete";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        validValueGUID,
                                        referenceableGUID);
    }


    /**
     * Link together 2 valid values from different sets that have equivalent values/meanings.
     *
     * @param userId calling user.
     * @param validValue1GUID unique identifier of the valid value.
     * @param validValue2GUID unique identifier of the other valid value to link to.
     * @param associationDescription how are the valid values related?
     * @param confidence how confident is the steward that this mapping is correct (0-100).
     * @param steward identifier of steward
     * @param notes additional notes from the steward
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public void    mapValidValues(String   userId,
                                  String   validValue1GUID,
                                  String   validValue2GUID,
                                  String   associationDescription,
                                  int      confidence,
                                  String   steward,
                                  String   notes) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String   methodName = "mapValidValues";

        final String   validValue1GUIDParameter = "validValue1GUID";
        final String   validValue2GUIDParameter = "validValue2GUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValue1GUID, validValue1GUIDParameter, methodName);
        invalidParameterHandler.validateGUID(validValue2GUID, validValue2GUIDParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/valid-values/{2}/map/{3}";

        ValidValuesMappingProperties requestBody = new ValidValuesMappingProperties();
        requestBody.setAssociationDescription(associationDescription);
        requestBody.setConfidence(confidence);
        requestBody.setSteward(steward);
        requestBody.setNotes(notes);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        validValue1GUID,
                                        validValue2GUID);
    }


    /**
     * Remove the reference value link between a valid value and a referenceable (item).
     *
     * @param userId calling user.
     * @param validValue1GUID unique identifier of the valid value.
     * @param validValue2GUID unique identifier of the other valid value element to remove the link from.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public void    unmapValidValues(String   userId,
                                    String   validValue1GUID,
                                    String   validValue2GUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String   methodName = "unmapValidValues";

        final String   validValue1GUIDParameter = "validValue1GUID";
        final String   validValue2GUIDParameter = "validValue2GUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValue1GUID, validValue1GUIDParameter, methodName);
        invalidParameterHandler.validateGUID(validValue2GUID, validValue2GUIDParameter, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/valid-values/{2}/map/{3}/delete";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        validValue1GUID,
                                        validValue2GUID);
    }


    /**
     * Retrieve a specific valid value from the repository.
     *
     * @param userId calling user
     * @param validValueGUID unique identifier of the valid value.
     *
     * @return Valid value bean
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public ValidValueElement getValidValueByGUID(String   userId,
                                                 String   validValueGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String   methodName = "getValidValueByGUID";
        final String   validValueGUIDParameter = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/valid-values/{2}";

        ValidValueResponse restResult = restClient.callValidValueGetRESTCall(methodName,
                                                                             urlTemplate,
                                                                             serverName,
                                                                             userId,
                                                                             validValueGUID);
        return restResult.getElement();
    }


    /**
     * Retrieve a specific valid value from the repository. Duplicates may be returned if
     * multiple valid values have been assigned the same qualified name.
     *
     * @param userId calling user
     * @param validValueName qualified name of the valid value.
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return Valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public List<ValidValueElement>   getValidValueByName(String   userId,
                                                         String   validValueName,
                                                         int      startFrom,
                                                         int      pageSize) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String   methodName = "getValidValueByName";
        final String   validValueNameParameter = "validValueName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(validValueName, validValueNameParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/valid-values/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(validValueName);
        requestBody.setNamePropertyName(validValueNameParameter);

        ValidValuesResponse restResult = restClient.callValidValuesPostRESTCall(methodName,
                                                                                urlTemplate,
                                                                                requestBody,
                                                                                serverName,
                                                                                userId,
                                                                                startFrom,
                                                                                pageSize);

        return restResult.getElementList();
    }


    /**
     * Locate valid values that match the search string.  It considers the names, description, scope,
     * usage and preferred value.
     *
     * @param userId calling user
     * @param searchString string value to look for - may contain RegEx characters.
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public List<ValidValueElement> findValidValues(String   userId,
                                                   String   searchString,
                                                   int      startFrom,
                                                   int      pageSize) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String   methodName = "findValidValues";
        final String   parameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, parameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/valid-values/by-search-string" +
                "?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(parameterName);

        ValidValuesResponse restResult = restClient.callValidValuesPostRESTCall(methodName,
                                                                                urlTemplate,
                                                                                requestBody,
                                                                                serverName,
                                                                                userId,
                                                                                startFrom,
                                                                                pageSize);

        return restResult.getElementList();
    }


    /**
     * Page through the members of a valid value set.
     *
     * @param userId calling user.
     * @param validValueSetGUID unique identifier of the valid value set.
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public List<ValidValueElement> getValidValueSetMembers(String   userId,
                                                           String   validValueSetGUID,
                                                           int      startFrom,
                                                           int      pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String   methodName = "getValidValueSetMembers";
        final String   validValueGUIDParameter = "validValueSetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueSetGUID, validValueGUIDParameter, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/valid-values/sets/{2}/members?startFrom={3}&pageSize={4}";

        ValidValuesResponse restResult = restClient.callValidValuesGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               validValueSetGUID,
                                                                               startFrom,
                                                                               pageSize);
        return restResult.getElementList();
    }


    /**
     * Page through the list of valid value sets that a valid value definition/set belongs to.
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public List<ValidValueElement> getSetsForValidValue(String   userId,
                                                        String   validValueGUID,
                                                        int      startFrom,
                                                        int      pageSize) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String   methodName = "getSetsForValidValue";
        final String   validValueGUIDParameter = "validValueSetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/valid-values/{2}/set-membership?startFrom={3}&pageSize={4}";

        ValidValuesResponse restResult = restClient.callValidValuesGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               validValueGUID,
                                                                               startFrom,
                                                                               pageSize);
        return restResult.getElementList();
    }


    /**
     * Page through the list of consumers for a valid value.
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of referenceable beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public List<ValidValueAssignmentConsumerElement> getValidValuesAssignmentConsumers(String   userId,
                                                                                       String   validValueGUID,
                                                                                       int      startFrom,
                                                                                       int      pageSize) throws InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException
    {
        final String   methodName = "getValidValuesAssignmentConsumers";
        final String   validValueGUIDParameter = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/valid-values/{2}/consumers?startFrom={3}&pageSize={4}";

        ValidValueAssignmentConsumersResponse restResult =
                restClient.callValidValueAssignmentConsumersGetRESTCall(methodName,
                                                                        urlTemplate,
                                                                        serverName,
                                                                        userId,
                                                                        validValueGUID,
                                                                        startFrom,
                                                                        pageSize);
        return restResult.getElementList();
    }


    /**
     * Page through the list of valid values assigned to referenceable element.
     *
     * @param userId calling user
     * @param referenceableGUID unique identifier of anchoring referenceable
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of valid value consumer beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public List<ValidValueAssignmentDefinitionElement> getValidValuesAssignmentDefinition(String   userId,
                                                                                          String   referenceableGUID,
                                                                                          int      startFrom,
                                                                                          int      pageSize) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException
    {
        final String   methodName = "getValidValuesAssignmentDefinition";
        final String   referenceableGUIDParameter = "referenceableGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(referenceableGUID, referenceableGUIDParameter, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/referenceables/{2}/valid-value-assignments?startFrom={3}&pageSize={4}";

        ValidValueAssignmentDefinitionsResponse restResult =
                restClient.callValidValueAssignmentDefinitionsGetRESTCall(methodName,
                                                                          urlTemplate,
                                                                          serverName,
                                                                          userId,
                                                                          referenceableGUID,
                                                                          startFrom,
                                                                          pageSize);
        return restResult.getElementList();
    }


    /**
     * Page through the list of implementations for a valid value.
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of asset beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public List<ValidValueImplAssetElement> getValidValuesImplementationAssets(String   userId,
                                                                               String   validValueGUID,
                                                                               int      startFrom,
                                                                               int      pageSize) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String   methodName = "getValidValuesImplementationAssets";
        final String   validValueGUIDParameter = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/valid-values/{2}/implementations" +
                "?startFrom={3}&pageSize={4}";

        ValidValuesImplAssetsResponse restResult = restClient.callValidValuesImplAssetsGetRESTCall(methodName,
                                                                                                   urlTemplate,
                                                                                                   serverName,
                                                                                                   userId,
                                                                                                   validValueGUID,
                                                                                                   startFrom,
                                                                                                   pageSize);
        return restResult.getElementList();
    }


    /**
     * Page through the list of valid values defining the content of a reference data asset.
     * This is always called from the assetHandler after it has checked that the asset is in the right zone.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public List<ValidValueImplDefinitionElement> getValidValuesImplementationDefinitions(String       userId,
                                                                                         String       assetGUID,
                                                                                         int          startFrom,
                                                                                         int          pageSize) throws InvalidParameterException,
                                                                                                                       UserNotAuthorizedException,
                                                                                                                       PropertyServerException
    {
        final String   methodName = "getValidValuesImplementationDefinitions";
        final String   assetGUIDParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/valid-values/{2}/implementations?startFrom={3}&pageSize={4}";

        ValidValuesImplDefinitionsResponse restResult =
                restClient.callValidValuesImplDefinitionsGetRESTCall(methodName,
                                                                     urlTemplate,
                                                                     serverName,
                                                                     userId,
                                                                     assetGUID,
                                                                     startFrom,
                                                                     pageSize);
        return restResult.getElementList();
    }


    /**
     * Page through the list of mappings for a valid value.  These are other valid values from different valid value sets that are equivalent
     * in some way.  The association description covers the type of association.
     *
     * @param userId calling user
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of mappings to other valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public List<ValidValueMappingElement> getValidValueMappings(String       userId,
                                                                String       validValueGUID,
                                                                int          startFrom,
                                                                int          pageSize) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String   methodName = "getValidValueMappings";
        final String   validValueGUIDParameter = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/valid-values/{2}/mapped-values" +
                "?startFrom={3}&pageSize={4}";

        ValidValueMappingsResponse restResult = restClient.callValidValueMappingsGetRESTCall(methodName,
                                                                                             urlTemplate,
                                                                                             serverName,
                                                                                             userId,
                                                                                             validValueGUID,
                                                                                             startFrom,
                                                                                             pageSize);
        return restResult.getElementList();
    }


    /**
     * Page through the list of mapping relationships associated with a valid value.
     * These are other valid values from different valid value sets that are equivalent
     * in some way.  The association description covers the type of association.
     *
     * @param userId calling user
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of mappings to other valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public List<ValidValuesMappingElement> getValidValuesMappings(String       userId,
                                                                  String       validValueGUID,
                                                                  int          startFrom,
                                                                  int          pageSize) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String   methodName = "getValidValuesMappings";
        final String   validValueGUIDParameter = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/valid-values/{2}/map-relationships" +
                "?startFrom={3}&pageSize={4}";

        ValidValuesMappingsResponse restResult = restClient.callValidValuesMappingsGetRESTCall(methodName,
                                                                                               urlTemplate,
                                                                                               serverName,
                                                                                               userId,
                                                                                               validValueGUID,
                                                                                               startFrom,
                                                                                               pageSize);
        return restResult.getElementList();
    }


    /**
     * Page through the list of referenceables that have this valid value as a reference value.
     *
     * @param userId calling user
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of referenceable beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public List<ReferenceValueAssignmentItemElement> getReferenceValueAssignedItems(String       userId,
                                                                                    String       validValueGUID,
                                                                                    int          startFrom,
                                                                                    int          pageSize) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        final String   methodName = "getReferenceValueAssignedItems";
        final String   validValueGUIDParameter = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/valid-values/{2}/reference-values/assigned-items" +
                "?startFrom={3}&pageSize={4}";

        ReferenceValueAssignmentItemsResponse restResult =
                restClient.callReferenceValueAssignmentItemsGetRESTCall(methodName,
                                                                        urlTemplate,
                                                                        serverName,
                                                                        userId,
                                                                        validValueGUID,
                                                                        startFrom,
                                                                        pageSize);
        return restResult.getElementList();
    }


    /**
     * Page through the list of assigned reference values for a referenceable.
     *
     * @param userId calling user
     * @param referenceableGUID unique identifier of assigned item
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public List<ReferenceValueAssignmentDefinitionElement> getReferenceValueAssignments(String       userId,
                                                                                        String       referenceableGUID,
                                                                                        int          startFrom,
                                                                                        int          pageSize) throws InvalidParameterException,
                                                                                                                      UserNotAuthorizedException,
                                                                                                                      PropertyServerException
    {
        final String   methodName = "getReferenceValueAssignments";
        final String   referenceableGUIDParameter = "referenceableGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(referenceableGUID, referenceableGUIDParameter, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/referenceables/{2}/reference-value-assignments" +
                "?startFrom={3}&pageSize={4}";

        ReferenceValueAssignmentDefinitionsResponse restResult =
                restClient.callReferenceValueAssignmentDefinitionsGetRESTCall(methodName,
                                                                              urlTemplate,
                                                                              serverName,
                                                                              userId,
                                                                              referenceableGUID,
                                                                              startFrom,
                                                                              pageSize);
        return restResult.getElementList();
    }
}
