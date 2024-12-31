/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.client.exchange;

import org.odpi.openmetadata.accessservices.assetmanager.api.exchange.ValidValuesExchangeInterface;
import org.odpi.openmetadata.accessservices.assetmanager.client.converters.ReferenceValueAssignmentDefinitionConverter;
import org.odpi.openmetadata.accessservices.assetmanager.client.converters.ReferenceValueAssignmentItemConverter;
import org.odpi.openmetadata.accessservices.assetmanager.client.converters.ValidValueConverter;
import org.odpi.openmetadata.accessservices.assetmanager.client.converters.ValidValueMemberConverter;
import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.*;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * ValidValuesExchangeClient provides the API operations to create and maintain lists of valid
 * value definitions grouped into a valid value set.  Both valid value definitions and valid value sets have
 * the same attributes and so inherit from ValidValue where all the attributes are defined.
 * A set is just grouping of valid values.   Valid value definitions and set can be nested many times in other
 * valid value sets.
 */
public class ValidValuesExchangeClient extends ExchangeClientBase implements ValidValuesExchangeInterface
{
    final private ValidValueConverter<ValidValueElement>                                                 validValueConverter;
    final private Class<ValidValueElement>                                                               validValueBeanClass                         = ValidValueElement.class;
    final private ValidValueMemberConverter<ValidValueMember>                                            validValueMemberConverter;
    final private Class<ValidValueMember>                                                                validValueMemberClass                       = ValidValueMember.class;
    final private ReferenceValueAssignmentItemConverter<ReferenceValueAssignmentItemElement>             referenceValueAssignmentItemConverter;
    final private Class<ReferenceValueAssignmentItemElement>                                             referenceValueAssignmentItemBeanClass       = ReferenceValueAssignmentItemElement.class;
    final private ReferenceValueAssignmentDefinitionConverter<ReferenceValueAssignmentDefinitionElement> referenceValueAssignmentDefinitionConverter;
    final private Class<ReferenceValueAssignmentDefinitionElement>                                       referenceValueAssignmentDefinitionBeanClass = ReferenceValueAssignmentDefinitionElement.class;

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ValidValuesExchangeClient(String   serverName,
                                     String   serverPlatformURLRoot,
                                     AuditLog auditLog,
                                     int      maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog, maxPageSize);

        validValueConverter = new ValidValueConverter<>(propertyHelper,
                                                        AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                                        serverName);

        validValueMemberConverter = new ValidValueMemberConverter<>(propertyHelper,
                                                                    AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                                                    serverName);

        referenceValueAssignmentItemConverter = new ReferenceValueAssignmentItemConverter<>(propertyHelper,
                                                                                            AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                                                                            serverName);
        
        referenceValueAssignmentDefinitionConverter = new ReferenceValueAssignmentDefinitionConverter<>(propertyHelper,
                                                                                            AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                                                                            serverName);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ValidValuesExchangeClient(String serverName,
                                     String serverPlatformURLRoot,
                                     int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize);

        validValueConverter = new ValidValueConverter<>(propertyHelper,
                                                        AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                                        serverName);

        validValueMemberConverter = new ValidValueMemberConverter<>(propertyHelper,
                                                                    AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                                                    serverName);
        
        referenceValueAssignmentItemConverter = new ReferenceValueAssignmentItemConverter<>(propertyHelper,
                                                                                            AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                                                                            serverName);

        referenceValueAssignmentDefinitionConverter = new ReferenceValueAssignmentDefinitionConverter<>(propertyHelper,
                                                                                                        AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                                                                                        serverName);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     * @param maxPageSize maximum value allowed for page size
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ValidValuesExchangeClient(String   serverName,
                                     String   serverPlatformURLRoot,
                                     String   userId,
                                     String   password,
                                     AuditLog auditLog,
                                     int      maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog, maxPageSize);

        validValueConverter = new ValidValueConverter<>(propertyHelper,
                                                        AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                                        serverName);

        validValueMemberConverter = new ValidValueMemberConverter<>(propertyHelper,
                                                                    AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                                                    serverName);
        
        referenceValueAssignmentItemConverter = new ReferenceValueAssignmentItemConverter<>(propertyHelper,
                                                                                            AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                                                                            serverName);

        referenceValueAssignmentDefinitionConverter = new ReferenceValueAssignmentDefinitionConverter<>(propertyHelper,
                                                                                                        AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                                                                                        serverName);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ValidValuesExchangeClient(String                 serverName,
                                     String                 serverPlatformURLRoot,
                                     AssetManagerRESTClient restClient,
                                     int                    maxPageSize,
                                     AuditLog               auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize, auditLog);

        validValueConverter = new ValidValueConverter<>(propertyHelper,
                                                        AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                                        serverName);

        validValueMemberConverter = new ValidValueMemberConverter<>(propertyHelper,
                                                                    AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                                                    serverName);
        
        referenceValueAssignmentItemConverter = new ReferenceValueAssignmentItemConverter<>(propertyHelper,
                                                                                            AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                                                                            serverName);

        referenceValueAssignmentDefinitionConverter = new ReferenceValueAssignmentDefinitionConverter<>(propertyHelper,
                                                                                                        AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                                                                                        serverName);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ValidValuesExchangeClient(String serverName,
                                     String serverPlatformURLRoot,
                                     String userId,
                                     String password,
                                     int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize);

        validValueConverter = new ValidValueConverter<>(propertyHelper,
                                                        AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                                        serverName);

        validValueMemberConverter = new ValidValueMemberConverter<>(propertyHelper,
                                                                    AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                                                    serverName);
        
        referenceValueAssignmentItemConverter = new ReferenceValueAssignmentItemConverter<>(propertyHelper,
                                                                                            AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                                                                            serverName);

        referenceValueAssignmentDefinitionConverter = new ReferenceValueAssignmentDefinitionConverter<>(propertyHelper,
                                                                                                        AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                                                                                        serverName);
    }

    /* =======================================================
     * Valid values methods
     */


    /**
     * Create a new valid value set.  This just creates the Set itself.  Members are added either as they are
     * created, or they can be attached to a set after they are created.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param validValueProperties properties to store
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return unique identifier for the new set
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public String  createValidValueSet(String                       userId,
                                       String                       assetManagerGUID,
                                       String                       assetManagerName,
                                       ExternalIdentifierProperties externalIdentifierProperties,
                                       ValidValueProperties         validValueProperties,
                                       Date                         effectiveTime,
                                       boolean                      forLineage,
                                       boolean                      forDuplicateProcessing) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "createValidValueSet";
        final String validValuePropertiesName = "validValueProperties";
        final String qualifiedNameParameterName = "validValueProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(validValueProperties, validValuePropertiesName, methodName);
        invalidParameterHandler.validateName(validValueProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String validValueTypeName = OpenMetadataType.VALID_VALUE_SET.typeName;

        if (validValueProperties.getTypeName() != null)
        {
            validValueTypeName = validValueProperties.getTypeName();
        }

        String validValueGUID = openMetadataStoreClient.createMetadataElementInStore(userId,
                                                                                     validValueTypeName,
                                                                                     ElementStatus.ACTIVE,
                                                                                     validValueProperties.getEffectiveFrom(),
                                                                                     validValueProperties.getEffectiveTo(),
                                                                                     this.getElementProperties(validValueProperties));

        super.addExternalIdentifier(userId,
                                    assetManagerGUID,
                                    assetManagerName,
                                    validValueGUID,
                                    validValueTypeName,
                                    externalIdentifierProperties);

        return validValueGUID;
    }


    /**
     * Create a new valid value definition.
     *
     * @param userId calling user.
     * @param setGUID unique identifier of the set to attach this to.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param validValueProperties properties to store
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return unique identifier for the new definition
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public String  createValidValueDefinition(String                       userId,
                                              String                       assetManagerGUID,
                                              String                       assetManagerName,
                                              String                       setGUID,
                                              ExternalIdentifierProperties externalIdentifierProperties,
                                              ValidValueProperties         validValueProperties,
                                              Date                         effectiveTime,
                                              boolean                      forLineage,
                                              boolean                      forDuplicateProcessing) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        final String methodName = "createValidValueDefinition";
        final String validValuePropertiesName = "validValueProperties";
        final String qualifiedNameParameterName = "validValueProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(validValueProperties, validValuePropertiesName, methodName);
        invalidParameterHandler.validateName(validValueProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String validValueTypeName = OpenMetadataType.VALID_VALUE_DEFINITION.typeName;

        if (validValueProperties.getTypeName() != null)
        {
            validValueTypeName = validValueProperties.getTypeName();
        }

        String validValueGUID = openMetadataStoreClient.createMetadataElementInStore(userId,
                                                                                     validValueTypeName,
                                                                                     ElementStatus.ACTIVE,
                                                                                     null,
                                                                                     null,
                                                                                     false,
                                                                                     validValueProperties.getEffectiveFrom(),
                                                                                     validValueProperties.getEffectiveTo(),
                                                                                     this.getElementProperties(validValueProperties),
                                                                                     setGUID,
                                                                                     OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName,
                                                                                     null,
                                                                                     true);

        super.addExternalIdentifier(userId,
                                    assetManagerGUID,
                                    assetManagerName,
                                    validValueGUID,
                                    validValueTypeName,
                                    externalIdentifierProperties);

        return validValueGUID;
    }


    /**
     * Update the properties of the valid value.  All properties are updated.
     * If only changing some if the properties, retrieve the current values from the repository
     * and pass existing values back on this call if they are not to change.
     *
     * @param userId calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param validValueGUID unique identifier of the valid value.
     * @param validValueExternalIdentifier unique identifier of the valid value in the external asset manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param validValueProperties properties to store
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public void    updateValidValue(String               userId,
                                    String               assetManagerGUID,
                                    String               assetManagerName,
                                    String               validValueGUID,
                                    String               validValueExternalIdentifier,
                                    boolean              isMergeUpdate,
                                    ValidValueProperties validValueProperties,
                                    Date                 effectiveTime,
                                    boolean              forLineage,
                                    boolean              forDuplicateProcessing) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "updateValidValue";
        final String validValuePropertiesName = "validValueProperties";
        final String qualifiedNameParameterName = "validValueProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(validValueProperties, validValuePropertiesName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(validValueProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        openMetadataStoreClient.validateExternalIdentifier(userId,
                                                           assetManagerGUID,
                                                           assetManagerName,
                                                           validValueGUID,
                                                           OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                           validValueExternalIdentifier,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime);

        openMetadataStoreClient.updateMetadataElementInStore(userId,
                                                             assetManagerGUID,
                                                             assetManagerName,
                                                             validValueGUID,
                                                             ! isMergeUpdate,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             this.getElementProperties(validValueProperties),
                                                             new Date());
    }


    /**
     * Remove the valid value form the repository.  All links to it are deleted too.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param validValueGUID unique identifier of the valid value.
     * @param validValueExternalIdentifier unique identifier of the valid value in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public void    removeValidValue(String  userId,
                                    String  assetManagerGUID,
                                    String  assetManagerName,
                                    String  validValueGUID,
                                    String  validValueExternalIdentifier,
                                    Date    effectiveTime,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "removeValidValue";
        final String validValueGUIDParameterName = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameterName, methodName);

        openMetadataStoreClient.validateExternalIdentifier(userId,
                                                           assetManagerGUID,
                                                           assetManagerName,
                                                           validValueGUID,
                                                           OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                           validValueExternalIdentifier,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime);
        
        openMetadataStoreClient.deleteMetadataElementInStore(userId,
                                                             assetManagerGUID,
                                                             assetManagerName,
                                                             validValueGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Create a link between a valid value set or definition and a set.  This means the valid
     * value is a member of the set.
     *
     * @param userId calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param setGUID unique identifier of the set.
     * @param validValueGUID unique identifier of the valid value to add to the set.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public void    attachValidValueToSet(String  userId,
                                         String  assetManagerGUID,
                                         String  assetManagerName,
                                         String  setGUID,
                                         String  validValueGUID,
                                         Date    effectiveTime,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        this.attachValidValueToSet(userId,
                                   assetManagerGUID,
                                   assetManagerName,
                                   setGUID,
                                   validValueGUID,
                                   null,
                                   effectiveTime,
                                   forLineage,
                                   forDuplicateProcessing);
    }


    /**
     * Create a link between a valid value set or definition and a set.  This means the valid
     * value is a member of the set.
     *
     * @param userId calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param setGUID unique identifier of the set.
     * @param validValueGUID unique identifier of the valid value to add to the set.
     * @param properties is this the default value - used when creating a list of valid values
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public void    attachValidValueToSet(String                         userId,
                                         String                         assetManagerGUID,
                                         String                         assetManagerName,
                                         String                         setGUID,
                                         String                         validValueGUID,
                                         ValidValueMembershipProperties properties,
                                         Date                           effectiveTime,
                                         boolean                        forLineage,
                                         boolean                        forDuplicateProcessing) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName                  = "attachValidValueToSet";
        final String validValueGUIDParameterName = "validValueGUID";
        final String parentGUIDParameterName     = "setGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(setGUID, parentGUIDParameterName, methodName);

        if (properties != null)
        {
            ElementProperties elementProperties = propertyHelper.addBooleanProperty(null,
                                                                                    OpenMetadataType.IS_DEFAULT_VALUE_PROPERTY_NAME,
                                                                                    properties.getDefaultValue());

            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName,
                                                                 setGUID,
                                                                 validValueGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 properties.getEffectiveFrom(),
                                                                 properties.getEffectiveTo(),
                                                                 elementProperties,
                                                                 effectiveTime);
        }
        else
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName,
                                                                 setGUID,
                                                                 validValueGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 effectiveTime);
        }
    }



    /**
     * Remove the link between a valid value and a set it is a member of.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param setGUID owning set
     * @param validValueGUID unique identifier of the member to be removed.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public void    detachValidValueFromSet(String  userId,
                                           String  assetManagerGUID,
                                           String  assetManagerName,
                                           String  setGUID,
                                           String  validValueGUID,
                                           Date    effectiveTime,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "detachValidValue";

        final String validValueGUIDParameterName = "validValueGUID";
        final String parentGUIDParameterName = "setGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(setGUID, parentGUIDParameterName, methodName);

        openMetadataStoreClient.deleteRelatedElementsInStore(userId,
                                                             OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName,
                                                             setGUID,
                                                             validValueGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Link a valid value as a reference value to a referencable to act as a tag/classification to help with locating and
     * grouping the referenceable.
     *
     * @param userId            calling user.
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param validValueGUID    unique identifier of the valid value.
     * @param referenceableGUID unique identifier of the element to link to.
     * @param properties        details of the relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    @Override
    public void assignReferenceValueToItem(String                             userId,
                                           String                             assetManagerGUID,
                                           String                             assetManagerName,
                                           String                             validValueGUID,
                                           String                             referenceableGUID,
                                           ReferenceValueAssignmentProperties properties,
                                           Date                               effectiveTime,
                                           boolean                            forLineage,
                                           boolean                            forDuplicateProcessing) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        final String methodName = "assignReferenceValueToItem";
        final String validValueGUIDParameterName = "validValueGUID";
        final String itemGUIDParameterName = "parentGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(referenceableGUID, itemGUIDParameterName, methodName);

        if (properties != null)
        {
            ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                                   OpenMetadataType.ATTRIBUTE_NAME_PROPERTY_NAME,
                                                                                   properties.getAttributeName());

            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                              OpenMetadataProperty.CONFIDENCE.name,
                                                              properties.getConfidence());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.STEWARD.name,
                                                                 properties.getSteward());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                                 properties.getStewardTypeName());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                                 properties.getStewardPropertyName());
            
            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.NOTES.name,
                                                                 properties.getNotes());

            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                 referenceableGUID,
                                                                 validValueGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 properties.getEffectiveFrom(),
                                                                 properties.getEffectiveTo(),
                                                                 elementProperties,
                                                                 effectiveTime);
        }
        else
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                 referenceableGUID,
                                                                 validValueGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 effectiveTime);
        }

    }


    /**
     * Remove the reference value link between a valid value and a referenceable (item).
     *
     * @param userId            calling user.
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param validValueGUID    unique identifier of the valid value.
     * @param referenceableGUID unique identifier of the element to remove the link from.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    @Override
    public void unassignReferenceValueFromItem(String  userId,
                                               String  assetManagerGUID,
                                               String  assetManagerName,
                                               String  validValueGUID,
                                               String  referenceableGUID,
                                               Date    effectiveTime,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "detachValidValue";

        final String validValueGUIDParameterName = "validValueGUID";
        final String itemGUIDParameterName = "referenceableGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(referenceableGUID, itemGUIDParameterName, methodName);

        openMetadataStoreClient.deleteRelatedElementsInStore(userId,
                                                             OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName,
                                                             referenceableGUID,
                                                             validValueGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Retrieve a specific valid value from the repository.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param validValueGUID unique identifier of the valid value.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return Valid value bean
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public ValidValueElement getValidValueByGUID(String  userId,
                                                 String  assetManagerGUID,
                                                 String  assetManagerName,
                                                 String  validValueGUID,
                                                 Date    effectiveTime,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "getValidValueByGUID";
        final String guidParameterName = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, guidParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataStoreClient.getMetadataElementByGUID(userId,
                                                                                                   validValueGUID,
                                                                                                   forLineage,
                                                                                                   forDuplicateProcessing,
                                                                                                   null,
                                                                                                   effectiveTime);

        return convertValidValue(userId, assetManagerGUID, assetManagerName, openMetadataElement, methodName);
    }


    /**
     * Retrieve a specific valid value from the repository.  Duplicates may be returned if
     * multiple valid values have been assigned the same qualified name.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param validValueName qualified name of the valid value.
     * @param startFrom         starting element (used in paging through large result sets)
     * @param pageSize          maximum number of results to return
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return Valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public List<ValidValueElement> getValidValueByName(String  userId,
                                                       String  assetManagerGUID,
                                                       String  assetManagerName,
                                                       String  validValueName,
                                                       int     startFrom,
                                                       int     pageSize,
                                                       Date    effectiveTime,
                                                       boolean forLineage,
                                                       boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName = "getValidValuesByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(validValueName, nameParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.NAME.name);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElements(userId,
                                                                                                      OpenMetadataType.COLLECTION.typeName,
                                                                                                      null,
                                                                                                      propertyHelper.getSearchPropertiesByName(propertyNames, validValueName, PropertyComparisonOperator.EQ),
                                                                                                      null,
                                                                                                      null,
                                                                                                      null,
                                                                                                      OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                      SequencingOrder.PROPERTY_ASCENDING,
                                                                                                      forLineage,
                                                                                                      forDuplicateProcessing,
                                                                                                      effectiveTime,
                                                                                                      startFrom,
                                                                                                      pageSize);

        return convertValidValues(userId, assetManagerGUID, assetManagerName, openMetadataElements, methodName);
    }
    

    /**
     * Locate valid values that match the search string.  It considers the names, description, scope,
     * usage and preferred value.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string value to look for - may contain RegEx characters.
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public List<ValidValueElement> findValidValues(String  userId,
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
        final String methodName = "findValidValues";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElementsWithString(userId,
                                                                                                                searchString,
                                                                                                                OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                                                                null,
                                                                                                                null,
                                                                                                                null,
                                                                                                                null,
                                                                                                                forLineage,
                                                                                                                forDuplicateProcessing,
                                                                                                                effectiveTime,
                                                                                                                startFrom,
                                                                                                                pageSize);

        return convertValidValues(userId, assetManagerGUID, assetManagerName, openMetadataElements, methodName);
    }


    /**
     * Page through the members of a valid value set.
     *
     * @param userId calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param validValueSetGUID unique identifier of the valid value set.
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public List<ValidValueMember> getValidValueSetMembers(String  userId,
                                                          String  assetManagerGUID,
                                                          String  assetManagerName,
                                                          String  validValueSetGUID,
                                                          int     startFrom,
                                                          int     pageSize,
                                                          Date    effectiveTime,
                                                          boolean forLineage,
                                                          boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName = "getValidValueSetMembers";
        final String validValueGUIDParameterName = "validValueSetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueSetGUID, validValueGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        RelatedMetadataElementList linkedResources = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                          validValueSetGUID,
                                                                                                          1,
                                                                                                          OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName,
                                                                                                          null,
                                                                                                          null,
                                                                                                          null,
                                                                                                          SequencingOrder.CREATION_DATE_RECENT,
                                                                                                          forLineage,
                                                                                                          forDuplicateProcessing,
                                                                                                          effectiveTime,
                                                                                                          startFrom,
                                                                                                          pageSize);

        if ((linkedResources != null) && (linkedResources.getElementList() != null))
        {
            List<ValidValueMember> validValueMembers = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : linkedResources.getElementList())
            {
                if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.VALID_VALUE_DEFINITION.typeName))
                {
                    ValidValueMember validValueMember = validValueMemberConverter.getNewBean(validValueMemberClass, relatedMetadataElement, methodName);

                    if ((validValueMember != null) && (validValueMember.getValidValueElement() != null))
                    {
                        validValueMember.getValidValueElement().setCorrelationHeaders(this.getMetadataCorrelationHeaders(userId,
                                                                                                                         assetManagerGUID,
                                                                                                                         assetManagerName,
                                                                                                                         validValueMember.getValidValueElement().getElementHeader().getGUID(),
                                                                                                                         OpenMetadataType.VALID_VALUE_DEFINITION.typeName));
                    }

                    validValueMembers.add(validValueMember);
                }
            }

            if (! validValueMembers.isEmpty())
            {
                return validValueMembers;
            }
        }

        return null;
    }



    /**
     * Page through the list of valid value sets that a valid value definition/set belongs to.
     *
     * @param userId calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public List<ValidValueElement> getSetsForValidValue(String  userId,
                                                        String  assetManagerGUID,
                                                        String  assetManagerName,
                                                        String  validValueGUID,
                                                        int     startFrom,
                                                        int     pageSize,
                                                        Date    effectiveTime,
                                                        boolean forLineage,
                                                        boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "getSetsForValidValue";
        final String validValueGUIDParameterName = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        RelatedMetadataElementList linkedResources = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                          validValueGUID,
                                                                                                          2,
                                                                                                          OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName,
                                                                                                          null,
                                                                                                          null,
                                                                                                          null,
                                                                                                          SequencingOrder.CREATION_DATE_RECENT,
                                                                                                          forLineage,
                                                                                                          forDuplicateProcessing,
                                                                                                          effectiveTime,
                                                                                                          startFrom,
                                                                                                          pageSize);

        if ((linkedResources != null) && (linkedResources.getElementList() != null))
        {
            List<ValidValueElement> validValueSets = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : linkedResources.getElementList())
            {
                if (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), OpenMetadataType.VALID_VALUE_SET.typeName))
                {
                    validValueSets.add(this.convertValidValue(userId,
                                                              assetManagerGUID,
                                                              assetManagerName,
                                                              relatedMetadataElement.getElement(),
                                                              methodName));
                }
            }

            if (! validValueSets.isEmpty())
            {
                return validValueSets;
            }
        }

        return null;
    }


    /**
     * Page through the list of referenceables that have this valid value as a reference value.
     *
     * @param userId         calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom      paging starting point
     * @param pageSize       maximum number of return values.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of referenceable beans
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    @Override
    public List<ReferenceValueAssignmentItemElement> getReferenceValueAssignedItems(String  userId,
                                                                                    String  assetManagerGUID,
                                                                                    String  assetManagerName,
                                                                                    String  validValueGUID,
                                                                                    int     startFrom,
                                                                                    int     pageSize,
                                                                                    Date    effectiveTime,
                                                                                    boolean forLineage,
                                                                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                           UserNotAuthorizedException,
                                                                                                                           PropertyServerException
    {
        final String methodName = "getReferenceValueAssignedItems";
        final String validValueGUIDParameterName = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        RelatedMetadataElementList linkedResources = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                          validValueGUID,
                                                                                                          2,
                                                                                                          OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                                                          null,
                                                                                                          null,
                                                                                                          null,
                                                                                                          SequencingOrder.CREATION_DATE_RECENT,
                                                                                                          forLineage,
                                                                                                          forDuplicateProcessing,
                                                                                                          effectiveTime,
                                                                                                          startFrom,
                                                                                                          pageSize);

        if ((linkedResources != null) && (linkedResources.getElementList() != null))
        {
            List<ReferenceValueAssignmentItemElement> results = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : linkedResources.getElementList())
            {
                if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.REFERENCEABLE.typeName))
                {
                    ReferenceValueAssignmentItemElement bean = referenceValueAssignmentItemConverter.getNewBean(referenceValueAssignmentItemBeanClass, relatedMetadataElement, methodName);

                    if ((bean != null) && (bean.getAssignedItem() != null))
                    {
                        bean.getAssignedItem().setCorrelationHeaders(this.getMetadataCorrelationHeaders(userId,
                                                                                                        assetManagerGUID,
                                                                                                        assetManagerName,
                                                                                                        bean.getAssignedItem().getElementHeader().getGUID(),
                                                                                                        OpenMetadataType.REFERENCEABLE.typeName));
                        results.add(bean);
                    }
                }
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Page through the list of assigned reference values for a referenceable.
     *
     * @param userId            calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param referenceableGUID unique identifier of assigned item
     * @param startFrom         paging starting point
     * @param pageSize          maximum number of return values.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    @Override
    public List<ReferenceValueAssignmentDefinitionElement> getReferenceValueAssignments(String  userId,
                                                                                        String  assetManagerGUID,
                                                                                        String  assetManagerName,
                                                                                        String  referenceableGUID,
                                                                                        int     startFrom,
                                                                                        int     pageSize,
                                                                                        Date    effectiveTime,
                                                                                        boolean forLineage,
                                                                                        boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                               UserNotAuthorizedException,
                                                                                                                               PropertyServerException
    {
        final String methodName = "getReferenceValueAssignments";
        final String itemGUIDParameterName = "referenceableGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(referenceableGUID, itemGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        RelatedMetadataElementList linkedResources = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                        referenceableGUID,
                                                                                                        1,
                                                                                                        OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                                                        null,
                                                                                                        null,
                                                                                                        null,
                                                                                                        SequencingOrder.CREATION_DATE_RECENT,
                                                                                                        forLineage,
                                                                                                        forDuplicateProcessing,
                                                                                                        effectiveTime,
                                                                                                        startFrom,
                                                                                                        pageSize);

        if ((linkedResources != null) && (linkedResources.getElementList() != null))
        {
            List<ReferenceValueAssignmentDefinitionElement> results = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : linkedResources.getElementList())
            {
                if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.VALID_VALUE_DEFINITION.typeName))
                {
                    ReferenceValueAssignmentDefinitionElement bean = referenceValueAssignmentDefinitionConverter.getNewBean(referenceValueAssignmentDefinitionBeanClass, relatedMetadataElement, methodName);

                    if ((bean != null) && (bean.getValidValueElement() != null))
                    {
                        bean.getValidValueElement().setCorrelationHeaders(this.getMetadataCorrelationHeaders(userId,
                                                                                                             assetManagerGUID,
                                                                                                             assetManagerName,
                                                                                                             bean.getValidValueElement().getElementHeader().getGUID(),
                                                                                                             OpenMetadataType.VALID_VALUE_DEFINITION.typeName));

                        results.add(bean);
                    }
                }
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Convert the validValue properties into a set of element properties for the open metadata client.
     *
     * @param validValueProperties supplied validValue properties
     * @return element properties
     */
    private ElementProperties getElementProperties(ValidValueProperties validValueProperties)
    {
        if (validValueProperties != null)
        {
            ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                                   OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                   validValueProperties.getQualifiedName());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.NAME.name,
                                                                 validValueProperties.getDisplayName());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.DESCRIPTION.name,
                                                                 validValueProperties.getDescription());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.USAGE.name,
                                                                 validValueProperties.getUsage());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.CATEGORY.name,
                                                                 validValueProperties.getUsage());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.PREFERRED_VALUE.name,
                                                                 validValueProperties.getPreferredValue());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.DATA_TYPE.name,
                                                                 validValueProperties.getDataType());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.SCOPE.name,
                                                                 validValueProperties.getScope());

            elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                                  OpenMetadataProperty.IS_DEPRECATED.name,
                                                                  validValueProperties.getIsDeprecated());

            elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                                  OpenMetadataProperty.IS_CASE_SENSITIVE.name,
                                                                  validValueProperties.getIsCaseSensitive());

            elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                    OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                    validValueProperties.getAdditionalProperties());

            elementProperties = propertyHelper.addPropertyMap(elementProperties,
                                                              validValueProperties.getExtendedProperties());

            return elementProperties;
        }

        return null;
    }


    /**
     * Convert a validValue object from the OpenMetadataClient to local beans.
     *
     * @param openMetadataElement retrieved element
     * @param methodName calling method
     *
     * @return list of validValue elements
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     * */
    private ValidValueElement convertValidValue(String               userId,
                                                String               assetManagerGUID,
                                                String               assetManagerName,
                                                OpenMetadataElement  openMetadataElement,
                                                String               methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        if ((openMetadataElement != null) && (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.VALID_VALUE_DEFINITION.typeName)))
        {
            ValidValueElement bean = validValueConverter.getNewBean(validValueBeanClass, openMetadataElement, methodName);

            if (bean != null)
            {
                bean.setCorrelationHeaders(this.getMetadataCorrelationHeaders(userId,
                                                                              assetManagerGUID,
                                                                              assetManagerName,
                                                                              openMetadataElement.getElementGUID(),
                                                                              OpenMetadataType.VALID_VALUE_DEFINITION.typeName));
                return bean;
            }
        }

        return null;
    }


    /**
     * Convert validValue objects from the OpenMetadataClient to local beans.
     *
     * @param openMetadataElements retrieved elements
     * @param methodName calling method
     *
     * @return list of validValue elements
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     * */
    private List<ValidValueElement> convertValidValues(String                     userId,
                                                       String                     assetManagerGUID,
                                                       String                     assetManagerName,
                                                       List<OpenMetadataElement>  openMetadataElements,
                                                       String                     methodName) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        if (openMetadataElements != null)
        {
            List<ValidValueElement> validValueElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    ValidValueElement validValueElement = validValueConverter.getNewBean(validValueBeanClass, openMetadataElement, methodName);

                    if (validValueElement != null)
                    {
                        validValueElement.setCorrelationHeaders(this.getMetadataCorrelationHeaders(userId,
                                                                                                   assetManagerGUID,
                                                                                                   assetManagerName,
                                                                                                   openMetadataElement.getElementGUID(),
                                                                                                   OpenMetadataType.VALID_VALUE_DEFINITION.typeName));
                        validValueElements.add(validValueElement);
                    }
                }
            }

            return validValueElements;
        }

        return null;
    }
}
