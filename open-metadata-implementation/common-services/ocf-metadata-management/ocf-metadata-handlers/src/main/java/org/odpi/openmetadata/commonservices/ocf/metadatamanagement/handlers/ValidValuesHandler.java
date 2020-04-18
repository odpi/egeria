/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.ValidValuesBuilder;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.ValidValuesConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ValidValuesMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositorySelectedEntitiesIterator;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ValidValuesHandler provides the methods to create and maintain lists of valid
 * value definitions grouped into a valid value set.  Both valid value definitions and valid value sets have
 * the same attributes and so inherit from ValidValue where all of the attributes are defined.
 *
 * A set is just grouping of valid values.   Valid value definitions and set can be nested many times in other
 * valid value sets.
 */
public class ValidValuesHandler
{
    private String                  serviceName;
    private String                  serverName;
    private OMRSRepositoryHelper    repositoryHelper;
    private RepositoryHandler       repositoryHandler;
    private InvalidParameterHandler invalidParameterHandler;
    private LastAttachmentHandler   lastAttachmentHandler;

    private AssetHandler            assetHandler = null;


    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param lastAttachmentHandler handler for recording last attachment
     */
    public ValidValuesHandler(String                  serviceName,
                              String                  serverName,
                              InvalidParameterHandler invalidParameterHandler,
                              RepositoryHandler       repositoryHandler,
                              OMRSRepositoryHelper    repositoryHelper,
                              LastAttachmentHandler   lastAttachmentHandler)
    {
        this.serviceName             = serviceName;
        this.serverName              = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHandler       = repositoryHandler;
        this.repositoryHelper        = repositoryHelper;
        this.lastAttachmentHandler   = lastAttachmentHandler;
    }


    /**
     * Set up the asset handler.  This cannot be passed on the constructor because the validValuesHandler
     * is needed to create the assetHandler.
     *
     * @param assetHandler handler for retrieving assets
     */
    public void setAssetHandler(AssetHandler assetHandler)
    {
        this.assetHandler = assetHandler;
    }


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
     * @param isDeprecated is the valid value deprecated
     * @param additionalProperties additional properties for this set.
     * @param extendedProperties properties that need to be populated into a subtype.
     * @param methodName calling method
     *
     * @return unique identifier for the new set
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public String  createValidValueSet(String              userId,
                                       String              qualifiedName,
                                       String              displayName,
                                       String              description,
                                       String              usage,
                                       String              scope,
                                       boolean             isDeprecated,
                                       Map<String, String> additionalProperties,
                                       Map<String, Object> extendedProperties,
                                       String              methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String   nameParameter = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);

        ValidValuesBuilder builder = new ValidValuesBuilder(qualifiedName,
                                                            displayName,
                                                            description,
                                                            usage,
                                                            scope,
                                                            null,
                                                            isDeprecated,
                                                            additionalProperties,
                                                            extendedProperties,
                                                            repositoryHelper,
                                                            serviceName,
                                                            serverName);

        return repositoryHandler.createEntity(userId,
                                              ValidValuesMapper.VALID_VALUE_SET_TYPE_GUID,
                                              ValidValuesMapper.VALID_VALUE_SET_TYPE_NAME,
                                              builder.getInstanceProperties(methodName),
                                              methodName);
    }


    /**
     * Create a new valid value definition.
     *
     * @param userId calling user.
     * @param setGUID unique identifier of the set to attach this to.
     * @param qualifiedName unique name.
     * @param displayName displayable descriptive name.
     * @param description further information.
     * @param usage how/when should this value be used.
     * @param scope what is the scope of the values.
     * @param preferredValue the value that should be used in an implementation if possible.
     * @param isDeprecated is the valid value deprecated
     * @param additionalProperties additional properties for this definition.
     * @param extendedProperties properties that need to be populated into a subtype.
     * @param methodName calling method
     *
     * @return unique identifier for the new definition
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public String  createValidValueDefinition(String              userId,
                                              String              setGUID,
                                              String              qualifiedName,
                                              String              displayName,
                                              String              description,
                                              String              usage,
                                              String              scope,
                                              String              preferredValue,
                                              boolean             isDeprecated,
                                              Map<String, String> additionalProperties,
                                              Map<String, Object> extendedProperties,
                                              String              methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String   nameParameter = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);

        ValidValuesBuilder builder = new ValidValuesBuilder(qualifiedName,
                                                            displayName,
                                                            description,
                                                            usage,
                                                            scope,
                                                            preferredValue,
                                                            isDeprecated,
                                                            additionalProperties,
                                                            extendedProperties,
                                                            repositoryHelper,
                                                            serviceName,
                                                            serverName);

        String definitionGUID = repositoryHandler.createEntity(userId,
                                                               ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_GUID,
                                                               ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                               builder.getInstanceProperties(methodName),
                                                               methodName);

        if ((definitionGUID != null) && (setGUID != null))
        {
            repositoryHandler.createRelationship(userId,
                                                 ValidValuesMapper.VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_GUID,
                                                 setGUID,
                                                 definitionGUID,
                                                 null,
                                                 methodName);
        }

        return definitionGUID;
    }


    /**
     * Create a new valid value set that is owned/managed by an external tool.  This just creates the Set itself.
     * Members are added either as they are created, or they can be attached to a set after they are created.
     *
     * @param userId calling user.
     * @param externalSourceGUID guid of the software server capability entity that represented the external source
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param qualifiedName unique name.
     * @param displayName displayable descriptive name.
     * @param description further information.
     * @param usage how/when should this set be used.
     * @param scope what is the scope of this set's values.
     * @param isDeprecated is the valid value deprecated
     * @param additionalProperties additional properties for this set.
     * @param extendedProperties properties that need to be populated into a subtype.
     * @param methodName calling method
     *
     * @return unique identifier for the new set
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public String  createExternalValidValueSet(String              userId,
                                               String              externalSourceGUID,
                                               String              externalSourceName,
                                               String              qualifiedName,
                                               String              displayName,
                                               String              description,
                                               String              usage,
                                               String              scope,
                                               boolean             isDeprecated,
                                               Map<String, String> additionalProperties,
                                               Map<String, Object> extendedProperties,
                                               String              methodName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String   nameParameter = "qualifiedName";
        final String   externalSourceGUIDParameter = "externalSourceGUID";
        final String   externalSourceNameParameter = "externalSourceName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalSourceGUID, externalSourceGUIDParameter, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);
        invalidParameterHandler.validateName(externalSourceName, externalSourceNameParameter, methodName);

        ValidValuesBuilder builder = new ValidValuesBuilder(qualifiedName,
                                                            displayName,
                                                            description,
                                                            usage,
                                                            scope,
                                                            null,
                                                            isDeprecated,
                                                            additionalProperties,
                                                            extendedProperties,
                                                            repositoryHelper,
                                                            serviceName,
                                                            serverName);

        return repositoryHandler.createExternalEntity(userId,
                                                      ValidValuesMapper.VALID_VALUE_SET_TYPE_GUID,
                                                      ValidValuesMapper.VALID_VALUE_SET_TYPE_NAME,
                                                      externalSourceGUID,
                                                      externalSourceName,
                                                      builder.getInstanceProperties(methodName),
                                                      methodName);
    }


    /**
     * Create a new valid value definition that is owned/managed by an external tool.
     *
     * @param userId calling user.
     * @param externalSourceGUID guid of the software server capability entity that represented the external source
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param setGUID unique identifier of the set to attach this to.
     * @param qualifiedName unique name.
     * @param displayName displayable descriptive name.
     * @param description further information.
     * @param usage how/when should this value be used.
     * @param scope what is the scope of the values.
     * @param preferredValue the value that should be used in an implementation if possible.
     * @param isDeprecated is the valid value deprecated
     * @param additionalProperties additional properties for this definition.
     * @param extendedProperties properties that need to be populated into a subtype.
     * @param methodName calling method
     *
     * @return unique identifier for the new definition
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public String  createExternalValidValueDefinition(String              userId,
                                                      String              externalSourceGUID,
                                                      String              externalSourceName,
                                                      String              setGUID,
                                                      String              qualifiedName,
                                                      String              displayName,
                                                      String              description,
                                                      String              usage,
                                                      String              scope,
                                                      String              preferredValue,
                                                      boolean             isDeprecated,
                                                      Map<String, String> additionalProperties,
                                                      Map<String, Object> extendedProperties,
                                                      String              methodName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String   nameParameter = "qualifiedName";
        final String   externalSourceGUIDParameter = "externalSourceGUID";
        final String   externalSourceNameParameter = "externalSourceName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalSourceGUID, externalSourceGUIDParameter, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);
        invalidParameterHandler.validateName(externalSourceName, externalSourceNameParameter, methodName);


        ValidValuesBuilder builder = new ValidValuesBuilder(qualifiedName,
                                                            displayName,
                                                            description,
                                                            usage,
                                                            scope,
                                                            preferredValue,
                                                            isDeprecated,
                                                            additionalProperties,
                                                            extendedProperties,
                                                            repositoryHelper,
                                                            serviceName,
                                                            serverName);

        String definitionGUID = repositoryHandler.createExternalEntity(userId,
                                                                       ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_GUID,
                                                                       ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                       externalSourceGUID,
                                                                       externalSourceName,
                                                                       builder.getInstanceProperties(methodName),
                                                                       methodName);

        if ((definitionGUID != null) && (setGUID != null))
        {
            repositoryHandler.createExternalRelationship(userId,
                                                         ValidValuesMapper.VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_GUID,
                                                         externalSourceGUID,
                                                         externalSourceName,
                                                         setGUID,
                                                         definitionGUID,
                                                         null,
                                                         methodName);
        }

        return definitionGUID;
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
     * @param isDeprecated is the valid value deprecated
     * @param additionalProperties additional properties for this valid value.
     * @param extendedProperties properties that need to be populated into a subtype.
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
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
                                    Map<String, Object> extendedProperties,
                                    String              methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String   guidParameter = "validValueGUID";
        final String   nameParameter = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, guidParameter, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);

        ValidValuesBuilder builder = new ValidValuesBuilder(qualifiedName,
                                                            displayName,
                                                            description,
                                                            usage,
                                                            scope,
                                                            preferredValue,
                                                            isDeprecated,
                                                            additionalProperties,
                                                            extendedProperties,
                                                            repositoryHelper,
                                                            serviceName,
                                                            serverName);

        repositoryHandler.updateEntity(userId,
                                       validValueGUID,
                                       ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_GUID,
                                       ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                       builder.getInstanceProperties(methodName),
                                       methodName);
    }


    /**
     * Remove the valid value form the repository.  All links to it are deleted too.
     *
     * @param userId calling user
     * @param validValueGUID unique identifier of the value to delete
     * @param qualifiedName unique name of the value to delete.  This is used to verify that
     *                      the correct valid value is being deleted.
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public void    deleteValidValue(String   userId,
                                    String   validValueGUID,
                                    String   qualifiedName,
                                    String   methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String   guidParameter = "validValueGUID";
        final String   nameParameter = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, guidParameter, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);

        repositoryHandler.removeEntity(userId,
                                       validValueGUID,
                                       ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_GUID,
                                       ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                       ReferenceableMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                       qualifiedName,
                                       methodName);
    }


    /**
     * Create a link between a valid value set or definition and a set.  This means the valid
     * value is a member of the set.
     *
     * @param userId calling user.
     * @param setGUID unique identifier of the set.
     * @param validValueGUID unique identifier of the valid value to add to the set.
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public void    attachValidValueToSet(String   userId,
                                         String   setGUID,
                                         String   validValueGUID,
                                         String   methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String setGUIDParameter        = "setGUID";
        final String validValueGUIDParameter = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(setGUID, setGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);

        repositoryHandler.createRelationship(userId,
                                             ValidValuesMapper.VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_GUID,
                                             setGUID,
                                             validValueGUID,
                                             null,
                                             methodName);
    }


    /**
     * Remove the link between a valid value and a set it is a member of.
     *
     * @param userId calling user
     * @param setGUID owning set
     * @param validValueGUID unique identifier of the member to be removed.
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public void    detachValidValueFromSet(String   userId,
                                           String   setGUID,
                                           String   validValueGUID,
                                           String   methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String   setGUIDParameter = "setGUID";
        final String   validValueGUIDParameter = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(setGUID, setGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);

        repositoryHandler.removeRelationshipBetweenEntities(userId,
                                                            ValidValuesMapper.VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_GUID,
                                                            ValidValuesMapper.VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_NAME,
                                                            setGUID,
                                                            ValidValuesMapper.VALID_VALUE_SET_TYPE_NAME,
                                                            validValueGUID,
                                                            methodName);
    }


    /**
     * Link a valid value to an asset that provides the implementation.  Typically this method is
     * used to link a valid value set to a code table.
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param assetGUID unique identifier of the asset that implements the valid value.
     * @param symbolicName lookup name for valid value
     * @param implementationValue value used in implementation
     * @param additionalValues additional values stored under the symbolic name
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public void  linkValidValueToImplementation(String              userId,
                                                String              validValueGUID,
                                                String              assetGUID,
                                                String              symbolicName,
                                                String              implementationValue,
                                                Map<String, String> additionalValues,
                                                String              methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String   validValueGUIDParameter = "validValueGUID";
        final String   assetGUIDParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        InstanceProperties properties = null;

        if (symbolicName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      ValidValuesMapper.SYMBOLIC_NAME_PROPERTY_NAME,
                                                                      symbolicName,
                                                                      methodName);
        }

        if (implementationValue != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      ValidValuesMapper.IMPLEMENTATION_VALUE_PROPERTY_NAME,
                                                                      implementationValue,
                                                                      methodName);
        }

        if ((additionalValues != null) && (! additionalValues.isEmpty()))
        {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         ValidValuesMapper.ADDITIONAL_VALUES_PROPERTY_NAME,
                                                                         additionalValues,
                                                                         methodName);
        }

        repositoryHandler.createRelationship(userId,
                                             ValidValuesMapper.VALID_VALUES_IMPL_RELATIONSHIP_TYPE_GUID,
                                             validValueGUID,
                                             assetGUID,
                                             properties,
                                             methodName);
    }


    /**
     * Remove the link between a valid value and an implementing asset.
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param assetGUID unique identifier of the asset that used to implement the valid value.
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public void  unlinkValidValueFromImplementation(String   userId,
                                                    String   validValueGUID,
                                                    String   assetGUID,
                                                    String   methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String   validValueGUIDParameter = "validValueGUID";
        final String   assetGUIDParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        repositoryHandler.removeRelationshipBetweenEntities(userId,
                                                            ValidValuesMapper.VALID_VALUES_IMPL_RELATIONSHIP_TYPE_GUID,
                                                            ValidValuesMapper.VALID_VALUES_IMPL_RELATIONSHIP_TYPE_NAME,
                                                            validValueGUID,
                                                            ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                            assetGUID,
                                                            methodName);
    }


    /**
     * Link a valid value typically to a schema element or glossary term to show that it uses
     * the valid values.
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param consumerGUID unique identifier of the element to link to.
     * @param strictRequirement the valid values defines the only values that are permitted.
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public void    assignValidValueToConsumer(String   userId,
                                              String   validValueGUID,
                                              String   consumerGUID,
                                              boolean  strictRequirement,
                                              String   methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String   validValueGUIDParameter = "validValueGUID";
        final String   consumerGUIDParameter = "consumerGUID";
        final String   attachmentDescription = "Attach valid value(s)";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(consumerGUID, consumerGUIDParameter, methodName);

        InstanceProperties relationshipProperties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                                                  null,
                                                                                                  ValidValuesMapper.IS_STRICT_REQUIREMENT_PROPERTY_NAME,
                                                                                                  strictRequirement,
                                                                                                  methodName);

        repositoryHandler.createRelationship(userId,
                                             ValidValuesMapper.VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_GUID,
                                             consumerGUID,
                                             validValueGUID,
                                             relationshipProperties,
                                             methodName);

        lastAttachmentHandler.updateLastAttachment(consumerGUID,
                                                   ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                   validValueGUID,
                                                   ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                   userId,
                                                   attachmentDescription,
                                                   methodName);
    }


    /**
     * Remove the link between a valid value and a consumer.
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param consumerGUID unique identifier of the element to remove the link from.
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public void    unassignValidValueFromConsumer(String   userId,
                                                  String   validValueGUID,
                                                  String   consumerGUID,
                                                  String   methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String   validValueGUIDParameter = "validValueGUID";
        final String   consumerGUIDParameter = "consumerGUID";
        final String   attachmentDescription = "Detach valid value(s)";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(consumerGUID, consumerGUIDParameter, methodName);

        repositoryHandler.removeRelationshipBetweenEntities(userId,
                                                            ValidValuesMapper.VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_GUID,
                                                            ValidValuesMapper.VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_NAME,
                                                            consumerGUID,
                                                            ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                            validValueGUID,
                                                            methodName);

        lastAttachmentHandler.updateLastAttachment(consumerGUID,
                                                   ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                   validValueGUID,
                                                   ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                   userId,
                                                   attachmentDescription,
                                                   methodName);
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
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public void    assignReferenceValueToItem(String   userId,
                                              String   validValueGUID,
                                              String   referenceableGUID,
                                              int      confidence,
                                              String   steward,
                                              String   notes,
                                              String   methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String   validValueGUIDParameter = "validValueGUID";
        final String   referenceableGUIDParameter = "referenceableGUID";
        final String   attachmentDescription = "Attach reference value(s)";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(referenceableGUID, referenceableGUIDParameter, methodName);

        InstanceProperties relationshipProperties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                                                              null,
                                                                                              ValidValuesMapper.CONFIDENCE_PROPERTY_NAME,
                                                                                              confidence,
                                                                                              methodName);

        if (steward != null)
        {
            relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                  null,
                                                                                  ValidValuesMapper.STEWARD_PROPERTY_NAME,
                                                                                  steward,
                                                                                  methodName);
        }

        if (notes != null)
        {
            relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                  null,
                                                                                  ValidValuesMapper.NOTES_PROPERTY_NAME,
                                                                                  notes,
                                                                                  methodName);
        }

        repositoryHandler.createRelationship(userId,
                                             ValidValuesMapper.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP_TYPE_GUID,
                                             referenceableGUID,
                                             validValueGUID,
                                             relationshipProperties,
                                             methodName);

        lastAttachmentHandler.updateLastAttachment(referenceableGUID,
                                                   ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                   validValueGUID,
                                                   ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                   userId,
                                                   attachmentDescription,
                                                   methodName);
    }


    /**
     * Remove the reference value link between a valid value and a referenceable (item).
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param referenceableGUID unique identifier of the element to remove the link from.
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public void    unassignReferenceValueFromItem(String   userId,
                                                  String   validValueGUID,
                                                  String   referenceableGUID,
                                                  String   methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String   validValueGUIDParameter = "validValueGUID";
        final String   referenceableGUIDParameter = "referenceableGUID";
        final String   attachmentDescription = "Detach reference value(s)";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(referenceableGUID, referenceableGUIDParameter, methodName);

        repositoryHandler.removeRelationshipBetweenEntities(userId,
                                                            ValidValuesMapper.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP_TYPE_GUID,
                                                            ValidValuesMapper.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP_TYPE_NAME,
                                                            referenceableGUID,
                                                            ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                            validValueGUID,
                                                            methodName);

        lastAttachmentHandler.updateLastAttachment(referenceableGUID,
                                                   ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                   validValueGUID,
                                                   ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                   userId,
                                                   attachmentDescription,
                                                   methodName);
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
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public void    mapValidValues(String   userId,
                                  String   validValue1GUID,
                                  String   validValue2GUID,
                                  String   associationDescription,
                                  int      confidence,
                                  String   steward,
                                  String   notes,
                                  String   methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String   validValue1GUIDParameter = "validValue1GUID";
        final String   validValue2GUIDParameter = "validValue2GUID";
        final String   attachmentDescription = "Map valid values";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValue1GUID, validValue1GUIDParameter, methodName);
        invalidParameterHandler.validateGUID(validValue2GUID, validValue2GUIDParameter, methodName);

        InstanceProperties relationshipProperties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                                                              null,
                                                                                              ValidValuesMapper.CONFIDENCE_PROPERTY_NAME,
                                                                                              confidence,
                                                                                              methodName);

        if (associationDescription != null)
        {
            relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                  null,
                                                                                  ValidValuesMapper.ASSOCIATION_DESCRIPTION_PROPERTY_NAME,
                                                                                  associationDescription,
                                                                                  methodName);
        }

        if (steward != null)
        {
            relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                  null,
                                                                                  ValidValuesMapper.STEWARD_PROPERTY_NAME,
                                                                                  steward,
                                                                                  methodName);
        }

        if (notes != null)
        {
            relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                  null,
                                                                                  ValidValuesMapper.NOTES_PROPERTY_NAME,
                                                                                  notes,
                                                                                  methodName);
        }

        repositoryHandler.createRelationship(userId,
                                             ValidValuesMapper.VALID_VALUES_MAP_RELATIONSHIP_TYPE_GUID,
                                             validValue2GUID,
                                             validValue1GUID,
                                             relationshipProperties,
                                             methodName);

        lastAttachmentHandler.updateLastAttachment(validValue2GUID,
                                                   ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                   validValue1GUID,
                                                   ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                   userId,
                                                   attachmentDescription,
                                                   methodName);
    }


    /**
     * Remove the reference value link between a valid value and a referenceable (item).
     *
     * @param userId calling user.
     * @param validValue1GUID unique identifier of the valid value.
     * @param validValue2GUID unique identifier of the other valid value element to remove the link from.
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public void    unmapValidValues(String   userId,
                                    String   validValue1GUID,
                                    String   validValue2GUID,
                                    String   methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String   validValue1GUIDParameter = "validValue1GUID";
        final String   validValue2GUIDParameter = "validValue2GUID";
        final String   attachmentDescription = "Unmap valid values";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValue1GUID, validValue1GUIDParameter, methodName);
        invalidParameterHandler.validateGUID(validValue2GUID, validValue2GUIDParameter, methodName);

        repositoryHandler.removeRelationshipBetweenEntities(userId,
                                                            ValidValuesMapper.VALID_VALUES_MAP_RELATIONSHIP_TYPE_GUID,
                                                            ValidValuesMapper.VALID_VALUES_MAP_RELATIONSHIP_TYPE_NAME,
                                                            validValue2GUID,
                                                            ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                            validValue1GUID,
                                                            methodName);

        lastAttachmentHandler.updateLastAttachment(validValue2GUID,
                                                   ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                   validValue1GUID,
                                                   ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                   userId,
                                                   attachmentDescription,
                                                   methodName);
    }


    /**
     * Retrieve a specific valid value from the repository.
     *
     * @param userId calling user
     * @param validValueGUID unique identifier of the valid value.
     * @param methodName calling method
     *
     * @return Valid value bean
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ValidValue getValidValueByGUID(String   userId,
                                          String   validValueGUID,
                                          String   methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String   validValueGUIDParameter = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);

        ValidValue validValue = null;

        EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                validValueGUID,
                                                                validValueGUIDParameter,
                                                                ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                methodName);

        if (entity != null)
        {
            ValidValuesConverter converter = new ValidValuesConverter(entity, repositoryHelper, serviceName);

            validValue = converter.getBean();
        }

        return validValue;
    }


    /**
     * Retrieve a specific valid value from the repository.  Duplicates may be returned if
     * multiple valid values have been assigned the same qualified name.
     *
     * @param userId calling user
     * @param validValueName qualified name of the valid value.
     * @param methodName calling method
     *
     * @return Valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public List<ValidValue>   getValidValueByName(String   userId,
                                                  String   validValueName,
                                                  String   methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String   validValueNameParameter = "validValueName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(validValueName, validValueNameParameter, methodName);

        ValidValuesBuilder builder = new ValidValuesBuilder(validValueName, validValueName, repositoryHelper, serviceName, methodName);

        List<EntityDetail> entities = repositoryHandler.getEntitiesByName(userId,
                                                                          builder.getNameInstanceProperties(methodName),
                                                                          ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_GUID,
                                                                          0,
                                                                          invalidParameterHandler.getMaxPagingSize(),
                                                                          methodName);

        List<ValidValue> validValues = null;
        if (entities != null)
        {
            validValues = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    ValidValuesConverter converter = new ValidValuesConverter(entity, repositoryHelper, serviceName);

                    validValues.add(converter.getBean());
                }
            }
        }

        return validValues;
    }


    /**
     * Locate valid values that match the search string.  It considers the names, description, scope,
     * usage and preferred value.
     *
     * @param userId calling user
     * @param searchString string value to look for - may contain RegEx characters.
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param methodName calling method
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public List<ValidValue> findValidValues(String   userId,
                                            String   searchString,
                                            int      startFrom,
                                            int      pageSize,
                                            String   methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String   parameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, parameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        ValidValuesBuilder builder = new ValidValuesBuilder(searchString,
                                                            searchString,
                                                            repositoryHelper,
                                                            serviceName,
                                                            serverName);

        List<ValidValue>  results = new ArrayList<>();

        RepositorySelectedEntitiesIterator iterator = new RepositorySelectedEntitiesIterator(repositoryHandler,
                                                                                             userId,
                                                                                             ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_GUID,
                                                                                             builder.getNameInstanceProperties(methodName),
                                                                                             MatchCriteria.ANY,
                                                                                             startFrom,
                                                                                             validatedPageSize,
                                                                                             methodName);
        while (iterator.moreToReceive())
        {
            EntityDetail validValueEntity = iterator.getNext();

            if (validValueEntity != null)
            {
                ValidValuesConverter converter = new ValidValuesConverter(validValueEntity, repositoryHelper, serviceName);

                results.add(converter.getBean());
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }


    /**
     * Page through the members of a valid value set.
     *
     * @param userId calling user
     * @param validValueSetGUID unique identifier of the valid value set.
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param methodName calling method
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public List<ValidValue> getValidValueSetMembers(String   userId,
                                                    String   validValueSetGUID,
                                                    int      startFrom,
                                                    int      pageSize,
                                                    String   methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String   validValueGUIDParameter = "validValueSetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueSetGUID, validValueGUIDParameter, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<ValidValue> members = null;

        List<EntityDetail>  memberEntities = repositoryHandler.getEntitiesForRelationshipType(userId,
                                                                                              validValueSetGUID,
                                                                                              ValidValuesMapper.VALID_VALUE_SET_TYPE_NAME,
                                                                                              ValidValuesMapper.VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_GUID,
                                                                                              ValidValuesMapper.VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_NAME,
                                                                                              startFrom,
                                                                                              validatedPageSize,
                                                                                              methodName);

        if (memberEntities != null)
        {
            members = new ArrayList<>();

            for (EntityDetail entity : memberEntities)
            {
                if (entity != null)
                {
                    ValidValuesConverter converter = new ValidValuesConverter(entity, repositoryHelper, serviceName);

                    members.add(converter.getBean());
                }
            }

            if (members.isEmpty())
            {
                members = null;
            }
        }

        return members;
    }


    /**
     * Page through the list of valid value sets that a valid value definition/set belongs to.
     *
     * @param userId calling user
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param methodName calling method
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public List<ValidValue> getSetsForValidValue(String   userId,
                                                 String   validValueGUID,
                                                 int      startFrom,
                                                 int      pageSize,
                                                 String   methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String   validValueGUIDParameter = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<ValidValue> sets = null;

        List<EntityDetail>  setEntities = repositoryHandler.getEntitiesForRelationshipType(userId,
                                                                                           validValueGUID,
                                                                                           ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                                           ValidValuesMapper.VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_GUID,
                                                                                           ValidValuesMapper.VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_NAME,
                                                                                           startFrom,
                                                                                           validatedPageSize,
                                                                                           methodName);

        if (setEntities != null)
        {
            sets = new ArrayList<>();

            for (EntityDetail entity : setEntities)
            {
                if (entity != null)
                {
                    ValidValuesConverter converter = new ValidValuesConverter(entity, repositoryHelper, serviceName);

                    sets.add(converter.getBean());
                }
            }

            if (sets.isEmpty())
            {
                sets = null;
            }
        }

        return sets;
    }


    /**
     * Page through the list of consumers for a valid value.
     *
     * @param userId calling user
     * @param validValueGUID unique identifier of valid value to query
     * @param supportedZones list of zones that define which assets can be retrieved.
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param methodName calling method
     *
     * @return list of valid value consumer beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public List<ValidValuesAssignmentConsumer> getValidValuesAssignmentConsumers(String       userId,
                                                                                 String       validValueGUID,
                                                                                 List<String> supportedZones,
                                                                                 int          startFrom,
                                                                                 int          pageSize,
                                                                                 String       methodName) throws InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException
    {
        final String   validValueGUIDParameter = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<ValidValuesAssignmentConsumer> consumers = null;

        List<Relationship>  relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                     validValueGUID,
                                                                                     ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                                     ValidValuesMapper.VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_GUID,
                                                                                     ValidValuesMapper.VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_NAME,
                                                                                     startFrom,
                                                                                     validatedPageSize,
                                                                                     methodName);

        if (relationships != null)
        {
            consumers = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    ValidValuesAssignmentConsumer consumer = new ValidValuesAssignmentConsumer();

                    InstanceProperties instanceProperties = relationship.getProperties();

                    if (instanceProperties != null)
                    {
                        consumer.setStrictRequirement(repositoryHelper.getBooleanProperty(serviceName,
                                                                                          ValidValuesMapper.IS_STRICT_REQUIREMENT_PROPERTY_NAME,
                                                                                          instanceProperties,
                                                                                          methodName));
                    }
                    EntityProxy end1 = relationship.getEntityOneProxy();

                    if ((end1 != null) && (end1.getGUID() != null))
                    {
                        final String guidParameterName = "end1.getGUID";
                        EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                                end1.getGUID(),
                                                                                guidParameterName,
                                                                                ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                                                methodName);

                        if (entity != null)
                        {
                            consumer.setConsumer(assetHandler.validatedVisibleReferenceable(userId,
                                                                                            supportedZones,
                                                                                            guidParameterName,
                                                                                            entity,
                                                                                            serviceName,
                                                                                            methodName));

                            consumers.add(consumer);
                        }
                    }
                }
            }

            if (consumers.isEmpty())
            {
                consumers = null;
            }
        }

        return consumers;
    }


    /**
     * Page through the list of valid values assigned to referenceable element.
     *
     * @param userId calling user
     * @param referenceableGUID unique identifier of anchoring referenceable
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param methodName calling method
     *
     * @return list of valid value consumer beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public List<ValidValuesAssignmentDefinition> getValidValuesAssignmentDefinition(String   userId,
                                                                                    String   referenceableGUID,
                                                                                    int      startFrom,
                                                                                    int      pageSize,
                                                                                    String   methodName) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        final String   referenceableGUIDParameter = "referenceableGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(referenceableGUID, referenceableGUIDParameter, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<ValidValuesAssignmentDefinition> definitions = null;

        List<Relationship>  relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                     referenceableGUID,
                                                                                     ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                                                     ValidValuesMapper.VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_GUID,
                                                                                     ValidValuesMapper.VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_NAME,
                                                                                     startFrom,
                                                                                     validatedPageSize,
                                                                                     methodName);

        if (relationships != null)
        {
            definitions = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    ValidValuesAssignmentDefinition definition = new ValidValuesAssignmentDefinition();

                    InstanceProperties instanceProperties = relationship.getProperties();

                    if (instanceProperties != null)
                    {
                        definition.setStrictRequirement(repositoryHelper.getBooleanProperty(serviceName,
                                                                                            ValidValuesMapper.IS_STRICT_REQUIREMENT_PROPERTY_NAME,
                                                                                            instanceProperties,
                                                                                            methodName));
                    }

                    EntityProxy end2 = relationship.getEntityTwoProxy();

                    if ((end2 != null) && (end2.getGUID() != null))
                    {
                        final String guidParameterName = "end2.getGUID";
                        EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                                end2.getGUID(),
                                                                                guidParameterName,
                                                                                ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                                methodName);

                        if (entity != null)
                        {
                            ValidValuesConverter converter = new ValidValuesConverter(entity, repositoryHelper, serviceName);
                            definition.setValidValue(converter.getBean());
                        }
                    }

                    definitions.add(definition);
                }
            }

            if (definitions.isEmpty())
            {
                definitions = null;
            }
        }

        return definitions;
    }



    /**
     * Page through the list of implementations for a valid value.
     *
     * @param userId calling user
     * @param validValueGUID unique identifier of valid value to query
     * @param supportedZones list of zones that define which assets can be retrieved.
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param methodName calling method
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public List<ValidValueImplementationAsset> getValidValuesImplementationAssets(String       userId,
                                                                                  String       validValueGUID,
                                                                                  List<String> supportedZones,
                                                                                  int          startFrom,
                                                                                  int          pageSize,
                                                                                  String       methodName) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        final String   validValueGUIDParameter = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<Relationship>  implementationRelationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                                   validValueGUID,
                                                                                                   ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                                                   ValidValuesMapper.VALID_VALUES_IMPL_RELATIONSHIP_TYPE_GUID,
                                                                                                   ValidValuesMapper.VALID_VALUES_IMPL_RELATIONSHIP_TYPE_NAME,
                                                                                                   startFrom,
                                                                                                   validatedPageSize,
                                                                                                   methodName);

        List<ValidValueImplementationAsset> implementationAssets = null;
        if (implementationRelationships != null)
        {
            implementationAssets = new ArrayList<>();

            for (Relationship relationship : implementationRelationships)
            {
                if (relationship != null)
                {
                    ValidValueImplementationAsset implementationAsset = new ValidValueImplementationAsset();

                    implementationAsset.setSymbolicName(repositoryHelper.getStringProperty(serviceName,
                                                                                  ValidValuesMapper.SYMBOLIC_NAME_PROPERTY_NAME,
                                                                                  relationship.getProperties(),
                                                                                  methodName));

                    implementationAsset.setImplementationValue(repositoryHelper.getStringProperty(serviceName,
                                                                                         ValidValuesMapper.IMPLEMENTATION_VALUE_PROPERTY_NAME,
                                                                                         relationship.getProperties(),
                                                                                         methodName));

                    implementationAsset.setAdditionalValues(repositoryHelper.getStringMapFromProperty(serviceName,
                                                                                             ValidValuesMapper.ADDITIONAL_VALUES_PROPERTY_NAME,
                                                                                             relationship.getProperties(),
                                                                                             methodName));
                    EntityProxy end2 = relationship.getEntityTwoProxy();

                    if ((end2 != null) && (end2.getGUID() != null))
                    {
                        try
                        {
                            implementationAsset.setReferenceDataAsset(assetHandler.getValidatedVisibleAsset(userId,
                                                                                                            supportedZones,
                                                                                                            end2.getGUID(),
                                                                                                            serviceName,
                                                                                                            methodName));
                            /*
                             * Only report about the asset if it is in a supported zone.
                             */
                            implementationAssets.add(implementationAsset);
                        }
                        catch (InvalidParameterException notInTheZone)
                        {
                            /*
                             * Ignore assets that are not visible
                             */
                        }
                    }
                }
            }

            if (implementationAssets.isEmpty())
            {
                implementationAssets = null;
            }
        }

        return implementationAssets;
    }


    /**
     * Page through the list of valid values defining the content of a reference data asset.
     * This is always called from the assetHandler after it has checked that the asset is in the right zone.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param methodName calling method
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    List<ValidValueImplementationDefinition> getValidValuesImplementationDefinitions(String       userId,
                                                                                     String       assetGUID,
                                                                                     int          startFrom,
                                                                                     int          pageSize,
                                                                                     String       methodName) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     PropertyServerException
    {
        final String   assetGUIDParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<Relationship>  implementationRelationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                                   assetGUID,
                                                                                                   AssetMapper.ASSET_TYPE_NAME,
                                                                                                   ValidValuesMapper.VALID_VALUES_IMPL_RELATIONSHIP_TYPE_GUID,
                                                                                                   ValidValuesMapper.VALID_VALUES_IMPL_RELATIONSHIP_TYPE_NAME,
                                                                                                   startFrom,
                                                                                                   validatedPageSize,
                                                                                                   methodName);

        List<ValidValueImplementationDefinition> definitions = null;
        if (implementationRelationships != null)
        {
            definitions = new ArrayList<>();

            for (Relationship relationship : implementationRelationships)
            {
                if (relationship != null)
                {
                    ValidValueImplementationDefinition definition = new ValidValueImplementationDefinition();

                    definition.setSymbolicName(repositoryHelper.getStringProperty(serviceName,
                                                                                  ValidValuesMapper.SYMBOLIC_NAME_PROPERTY_NAME,
                                                                                  relationship.getProperties(),
                                                                                  methodName));

                    definition.setImplementationValue(repositoryHelper.getStringProperty(serviceName,
                                                                                         ValidValuesMapper.IMPLEMENTATION_VALUE_PROPERTY_NAME,
                                                                                         relationship.getProperties(),
                                                                                         methodName));

                    definition.setAdditionalValues(repositoryHelper.getStringMapFromProperty(serviceName,
                                                                                             ValidValuesMapper.ADDITIONAL_VALUES_PROPERTY_NAME,
                                                                                             relationship.getProperties(),
                                                                                             methodName));
                    EntityProxy end1 = relationship.getEntityOneProxy();

                    if ((end1 != null) && (end1.getGUID() != null))
                    {
                        final String guidParameterName = "end1.getGUID";
                        EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                                end1.getGUID(),
                                                                                guidParameterName,
                                                                                ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                                methodName);

                        if (entity != null)
                        {
                            ValidValuesConverter converter = new ValidValuesConverter(entity, repositoryHelper, serviceName);
                            definition.setValidValue(converter.getBean());
                        }
                    }

                    definitions.add(definition);
                }
            }

            if (definitions.isEmpty())
            {
                definitions = null;
            }
        }

        return definitions;
    }


    /**
     * Page through the list of mappings for a valid value.  These are other valid values from different valid value sets that are equivalent
     * in some way.  The association description covers the type of association.
     *
     * @param userId calling user
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param methodName calling method
     *
     * @return list of mappings to other valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public List<ValidValueMapping> getValidValueMappings(String       userId,
                                                         String       validValueGUID,
                                                         int          startFrom,
                                                         int          pageSize,
                                                         String       methodName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String   validValueGUIDParameter = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<Relationship>  mappingRelationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                            validValueGUID,
                                                                                            ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                                            ValidValuesMapper.VALID_VALUES_MAP_RELATIONSHIP_TYPE_GUID,
                                                                                            ValidValuesMapper.VALID_VALUES_MAP_RELATIONSHIP_TYPE_NAME,
                                                                                            startFrom,
                                                                                            validatedPageSize,
                                                                                            methodName);

        List<ValidValueMapping> mappings = null;
        if (mappingRelationships != null)
        {
            mappings = new ArrayList<>();

            for (Relationship relationship : mappingRelationships)
            {
                if (relationship != null)
                {
                    ValidValueMapping mapping = new ValidValueMapping();

                    mapping.setAssociationDescription(repositoryHelper.getStringProperty(serviceName,
                                                                                         ValidValuesMapper.ASSOCIATION_DESCRIPTION_PROPERTY_NAME,
                                                                                         relationship.getProperties(),
                                                                                         methodName));

                    mapping.setConfidence(repositoryHelper.getIntProperty(serviceName,
                                                                          ValidValuesMapper.CONFIDENCE_PROPERTY_NAME,
                                                                          relationship.getProperties(),
                                                                          methodName));

                    mapping.setSteward(repositoryHelper.getStringProperty(serviceName,
                                                                          ValidValuesMapper.STEWARD_PROPERTY_NAME,
                                                                          relationship.getProperties(),
                                                                          methodName));

                    mapping.setNotes(repositoryHelper.getStringProperty(serviceName,
                                                                        ValidValuesMapper.NOTES_PROPERTY_NAME,
                                                                        relationship.getProperties(),
                                                                        methodName));

                    String proxyGUIDParameterName = null;
                    String otherGUID = null;

                    EntityProxy proxy = relationship.getEntityOneProxy();

                    if (proxy != null)
                    {
                        proxyGUIDParameterName = "EntityOneProxy.getGUID";
                        otherGUID = proxy.getGUID();
                    }
                    if (validValueGUID.equals(otherGUID))
                    {
                        proxyGUIDParameterName = null;
                        otherGUID = null;

                        proxy = relationship.getEntityTwoProxy();

                        if (proxy != null)
                        {
                            proxyGUIDParameterName = "EntityTwoProxy.getGUID";
                            otherGUID              = proxy.getGUID();
                        }
                    }

                    if (otherGUID != null)
                    {
                        EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                                proxy.getGUID(),
                                                                                proxyGUIDParameterName,
                                                                                ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                                methodName);

                        if (entity != null)
                        {
                            ValidValuesConverter converter = new ValidValuesConverter(entity, repositoryHelper, serviceName);
                            mapping.setValidValue(converter.getBean());
                        }
                    }

                    mappings.add(mapping);
                }
            }

            if (mappings.isEmpty())
            {
                mappings = null;
            }
        }

        return mappings;
    }


    /**
     * Page through the list of referenceables that have this valid value as a reference value.
     *
     * @param userId calling user
     * @param validValueGUID unique identifier of valid value to query
     * @param supportedZones list of zones that define which assets can be retrieved.
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param methodName calling method
     *
     * @return list of referenceable beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public List<ReferenceValueAssignedItem> getReferenceValueAssignedItems(String       userId,
                                                                           String       validValueGUID,
                                                                           List<String> supportedZones,
                                                                           int          startFrom,
                                                                           int          pageSize,
                                                                           String       methodName) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String   validValueGUIDParameter = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<ReferenceValueAssignedItem> assignedItems = null;

        List<Relationship>  relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                     validValueGUID,
                                                                                     ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                                     ValidValuesMapper.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP_TYPE_GUID,
                                                                                     ValidValuesMapper.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP_TYPE_NAME,
                                                                                     startFrom,
                                                                                     validatedPageSize,
                                                                                     methodName);

        if (relationships != null)
        {
            assignedItems = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    ReferenceValueAssignedItem assignedItem = new ReferenceValueAssignedItem();

                    InstanceProperties instanceProperties = relationship.getProperties();

                    if (instanceProperties != null)
                    {
                        assignedItem.setConfidence(repositoryHelper.getIntProperty(serviceName,
                                                                                   ValidValuesMapper.CONFIDENCE_PROPERTY_NAME,
                                                                                   relationship.getProperties(),
                                                                                   methodName));

                        assignedItem.setSteward(repositoryHelper.getStringProperty(serviceName,
                                                                                   ValidValuesMapper.STEWARD_PROPERTY_NAME,
                                                                                   relationship.getProperties(),
                                                                                   methodName));

                        assignedItem.setNotes(repositoryHelper.getStringProperty(serviceName,
                                                                                 ValidValuesMapper.NOTES_PROPERTY_NAME,
                                                                                 relationship.getProperties(),
                                                                                 methodName));
                    }

                    EntityProxy end1 = relationship.getEntityOneProxy();

                    if ((end1 != null) && (end1.getGUID() != null))
                    {
                        final String guidParameterName = "end1.getGUID";
                        EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                                end1.getGUID(),
                                                                                guidParameterName,
                                                                                ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                                                methodName);

                        if (entity != null)
                        {
                            assignedItem.setAssignedItem(assetHandler.validatedVisibleReferenceable(userId,
                                                                                                    supportedZones,
                                                                                                    guidParameterName,
                                                                                                    entity,
                                                                                                    serviceName,
                                                                                                    methodName));

                            assignedItems.add(assignedItem);
                        }
                    }

                    assignedItems.add(assignedItem);
                }
            }

            if (assignedItems.isEmpty())
            {
                assignedItems = null;
            }
        }

        return assignedItems;
    }


    /**
     * Page through the list of assigned reference values for a referenceable.
     *
     * @param userId calling user
     * @param referenceableGUID unique identifier of assigned item
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param methodName calling method
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public List<ReferenceValueAssignment> getReferenceValueAssignments(String       userId,
                                                                       String       referenceableGUID,
                                                                       int          startFrom,
                                                                       int          pageSize,
                                                                       String       methodName) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String   referenceableGUIDParameter = "referenceableGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(referenceableGUID, referenceableGUIDParameter, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<ReferenceValueAssignment> assignments = null;

        List<Relationship>  relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                     referenceableGUID,
                                                                                     ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                                                     ValidValuesMapper.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP_TYPE_GUID,
                                                                                     ValidValuesMapper.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP_TYPE_NAME,
                                                                                     startFrom,
                                                                                     validatedPageSize,
                                                                                     methodName);

        if (relationships != null)
        {
            assignments = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    ReferenceValueAssignment assignment = new ReferenceValueAssignment();

                    InstanceProperties instanceProperties = relationship.getProperties();

                    if (instanceProperties != null)
                    {
                        assignment.setConfidence(repositoryHelper.getIntProperty(serviceName,
                                                                                 ValidValuesMapper.CONFIDENCE_PROPERTY_NAME,
                                                                                 relationship.getProperties(),
                                                                                 methodName));

                        assignment.setSteward(repositoryHelper.getStringProperty(serviceName,
                                                                                 ValidValuesMapper.STEWARD_PROPERTY_NAME,
                                                                                 relationship.getProperties(),
                                                                                 methodName));

                        assignment.setNotes(repositoryHelper.getStringProperty(serviceName,
                                                                               ValidValuesMapper.NOTES_PROPERTY_NAME,
                                                                               relationship.getProperties(),
                                                                               methodName));
                    }

                    EntityProxy end2 = relationship.getEntityTwoProxy();

                    if ((end2 != null) && (end2.getGUID() != null))
                    {
                        final String guidParameterName = "end2.getGUID";
                        EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                                end2.getGUID(),
                                                                                guidParameterName,
                                                                                ValidValuesMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                                methodName);

                        if (entity != null)
                        {
                            ValidValuesConverter converter = new ValidValuesConverter(entity, repositoryHelper, serviceName);
                            assignment.setValidValue(converter.getBean());
                        }
                    }

                    assignments.add(assignment);
                }
            }

            if (assignments.isEmpty())
            {
                assignments = null;
            }
        }

        return assignments;
    }
}
