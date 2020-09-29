/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;

import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
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
 * <p>
 * A set is just grouping of valid values.   Valid value definitions and set can be nested many times in other
 * valid value sets.
 */
public class ValidValuesHandler<VALID_VALUE,
                                VALID_VALUE_ASSIGNMENT,
                                VALID_VALUE_ASSIGNMENT_DEF,
                                VALID_VALUE_IMPLEMENTATION,
                                VALID_VALUE_IMPLEMENTATION_DEF,
                                VALID_VALUE_MAPPING,
                                REFERENCE_VALUE_ASSIGNMENT,
                                REFERENCE_VALUE_ASSIGNED_ITEM> extends ReferenceableHandler<VALID_VALUE>
{
    private OpenMetadataAPIGenericConverter<VALID_VALUE_ASSIGNMENT>         validValueAssignmentConverter;
    private Class<VALID_VALUE_ASSIGNMENT>                                   validValueAssignmentClass;
    private OpenMetadataAPIGenericConverter<VALID_VALUE_ASSIGNMENT_DEF>     validValueAssignmentDefConverter;
    private Class<VALID_VALUE_ASSIGNMENT_DEF>                               validValueAssignmentDefClass;
    private OpenMetadataAPIGenericConverter<VALID_VALUE_IMPLEMENTATION>     validValueImplementationConverter;
    private Class<VALID_VALUE_IMPLEMENTATION>                               validValueImplementationClass;
    private OpenMetadataAPIGenericConverter<VALID_VALUE_IMPLEMENTATION_DEF> validValueImplementationDefConverter;
    private Class<VALID_VALUE_IMPLEMENTATION_DEF>                           validValueImplementationDefClass;
    private OpenMetadataAPIGenericConverter<VALID_VALUE_MAPPING>            validValueMappingConverter;
    private Class<VALID_VALUE_MAPPING>                                      validValueMappingClass;
    private OpenMetadataAPIGenericConverter<REFERENCE_VALUE_ASSIGNMENT>     referenceValueAssignmentConverter;
    private Class<REFERENCE_VALUE_ASSIGNMENT>                               referenceValueAssignmentClass;
    private OpenMetadataAPIGenericConverter<REFERENCE_VALUE_ASSIGNED_ITEM>  referenceValueAssignedItemConverter;
    private Class<REFERENCE_VALUE_ASSIGNED_ITEM>                            referenceValueAssignedItemClass;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param converter               specific converter for the VALID_VALUE bean class
     * @param beanClass               name of bean class that is represented by the generic class VALID_VALUE
     * @param validValueAssignmentConverter specific converter for the VALID_VALUE_ASSIGNMENT bean class
     * @param validValueAssignmentClass name of bean class that is represented by the generic class VALID_VALUE_ASSIGNMENT
     * @param validValueAssignmentDefConverter specific converter for the VALID_VALUE_ASSIGNMENT_DEF bean class
     * @param validValueAssignmentDefClass name of bean class that is represented by the generic class VALID_VALUE_ASSIGNMENT_DEF
     * @param validValueImplementationConverter specific converter for the VALID_VALUE_IMPLEMENTATION bean class
     * @param validValueImplementationClass name of bean class that is represented by the generic class VALID_VALUE_IMPLEMENTATION
     * @param validValueImplementationDefConverter specific converter for the VALID_VALUE_IMPLEMENTATION_DEF bean class
     * @param validValueImplementationDefClass name of bean class that is represented by the generic class VALID_VALUE_IMPLEMENTATION_DEF
     * @param validValueMappingConverter specific converter for the VALID_VALUE_MAPPING bean class
     * @param validValueMappingClass name of bean class that is represented by the generic class VALID_VALUE_MAPPING
     * @param referenceValueAssignmentConverter specific converter for the REFERENCE_VALUE_ASSIGNMENT bean class
     * @param referenceValueAssignmentClass name of bean class that is represented by the generic class REFERENCE_VALUE_ASSIGNMENT
     * @param referenceValueAssignedItemConverter specific converter for the REFERENCE_VALUE_ASSIGNED_ITEM bean class
     * @param referenceValueAssignedItemClass name of bean class that is represented by the generic class REFERENCE_VALUE_ASSIGNED_ITEM
     * @param serviceName             name of this service
     * @param serverName              name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler       manages calls to the repository services
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     * @param localServerUserId       userId for this server
     * @param securityVerifier        open metadata security services verifier
     * @param supportedZones          list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones            list of zones that the access service should set in all new Asset instances.
     * @param publishZones            list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public ValidValuesHandler(OpenMetadataAPIGenericConverter<VALID_VALUE>                    converter,
                              Class<VALID_VALUE>                                              beanClass,
                              OpenMetadataAPIGenericConverter<VALID_VALUE_ASSIGNMENT>         validValueAssignmentConverter,
                              Class<VALID_VALUE_ASSIGNMENT>                                   validValueAssignmentClass,
                              OpenMetadataAPIGenericConverter<VALID_VALUE_ASSIGNMENT_DEF>     validValueAssignmentDefConverter,
                              Class<VALID_VALUE_ASSIGNMENT_DEF>                               validValueAssignmentDefClass,
                              OpenMetadataAPIGenericConverter<VALID_VALUE_IMPLEMENTATION>     validValueImplementationConverter,
                              Class<VALID_VALUE_IMPLEMENTATION>                               validValueImplementationClass,
                              OpenMetadataAPIGenericConverter<VALID_VALUE_IMPLEMENTATION_DEF> validValueImplementationDefConverter,
                              Class<VALID_VALUE_IMPLEMENTATION_DEF>                           validValueImplementationDefClass,
                              OpenMetadataAPIGenericConverter<VALID_VALUE_MAPPING>            validValueMappingConverter,
                              Class<VALID_VALUE_MAPPING>                                      validValueMappingClass,
                              OpenMetadataAPIGenericConverter<REFERENCE_VALUE_ASSIGNMENT>     referenceValueAssignmentConverter,
                              Class<REFERENCE_VALUE_ASSIGNMENT>                               referenceValueAssignmentClass,
                              OpenMetadataAPIGenericConverter<REFERENCE_VALUE_ASSIGNED_ITEM>  referenceValueAssignedItemConverter,
                              Class<REFERENCE_VALUE_ASSIGNED_ITEM>                            referenceValueAssignedItemClass,
                              String                                                          serviceName,
                              String                                                          serverName,
                              InvalidParameterHandler                                         invalidParameterHandler,
                              RepositoryHandler                                               repositoryHandler,
                              OMRSRepositoryHelper                                            repositoryHelper,
                              String                                                          localServerUserId,
                              OpenMetadataServerSecurityVerifier                              securityVerifier,
                              List<String>                                                    supportedZones,
                              List<String>                                                    defaultZones,
                              List<String>                                                    publishZones,
                              AuditLog                                                        auditLog)

    {
        super(converter,
              beanClass,
              serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog);

        this.validValueAssignmentConverter        = validValueAssignmentConverter;
        this.validValueAssignmentClass            = validValueAssignmentClass;
        this.validValueAssignmentDefConverter     = validValueAssignmentDefConverter;
        this.validValueAssignmentDefClass         = validValueAssignmentDefClass;
        this.validValueImplementationConverter    = validValueImplementationConverter;
        this.validValueImplementationClass        = validValueImplementationClass;
        this.validValueImplementationDefConverter = validValueImplementationDefConverter;
        this.validValueImplementationDefClass     = validValueImplementationDefClass;
        this.validValueMappingConverter           = validValueMappingConverter;
        this.validValueMappingClass               = validValueMappingClass;
        this.referenceValueAssignmentConverter    = referenceValueAssignmentConverter;
        this.referenceValueAssignmentClass        = referenceValueAssignmentClass;
        this.referenceValueAssignedItemConverter  = referenceValueAssignedItemConverter;
        this.referenceValueAssignedItemClass      = referenceValueAssignedItemClass;
    }


    /**
     * Create a new valid value set.  This just creates the Set itself.  Members are added either as they are
     * created, or they can be attached to a set after they are created.
     *
     * @param userId               calling user.
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param qualifiedName        unique name.
     * @param displayName          displayable descriptive name.
     * @param description          further information.
     * @param usage                how/when should this set be used.
     * @param scope                what is the scope of this set's values.
     * @param isDeprecated         is the valid value deprecated
     * @param additionalProperties additional properties for this set.
     * @param extendedProperties   properties that need to be populated into a subtype.
     * @param methodName           calling method
     * @return unique identifier for the new set
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public String createValidValueSet(String              userId,
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
        final String nameParameter = "qualifiedName";

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

        return this.createBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           OpenMetadataAPIMapper.VALID_VALUE_SET_TYPE_GUID,
                                           OpenMetadataAPIMapper.VALID_VALUE_SET_TYPE_NAME,
                                           qualifiedName,
                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                           builder,
                                           methodName);
    }


    /**
     * Create a new valid value definition.
     *
     * @param userId               calling user.
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param setGUID              unique identifier of the set to attach this to.
     * @param qualifiedName        unique name.
     * @param displayName          displayable descriptive name.
     * @param description          further information.
     * @param usage                how/when should this value be used.
     * @param scope                what is the scope of the values.
     * @param preferredValue       the value that should be used in an implementation if possible.
     * @param isDeprecated         is the valid value deprecated
     * @param additionalProperties additional properties for this definition.
     * @param extendedProperties   properties that need to be populated into a subtype.
     * @param methodName           calling method
     * @return unique identifier for the new definition
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public String createValidValueDefinition(String              userId,
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
        final String nameParameter       = "qualifiedName";
        final String setParameter        = "setGUID";
        final String definitionParameter = "definitionGUID";

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

        String definitionGUID = this.createBeanInRepository(userId,
                                                            externalSourceGUID,
                                                            externalSourceName,
                                                            OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_GUID,
                                                            OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                            qualifiedName,
                                                            OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                            builder,
                                                            methodName);

        if ((definitionGUID != null) && (setGUID != null))
        {
            this.linkElementToElement(userId,
                                      externalSourceGUID,
                                      externalSourceName,
                                      setGUID,
                                      setParameter,
                                      OpenMetadataAPIMapper.VALID_VALUE_SET_TYPE_NAME,
                                      definitionGUID,
                                      definitionParameter,
                                      OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                      OpenMetadataAPIMapper.VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_GUID,
                                      OpenMetadataAPIMapper.VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_NAME,
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
     * @param userId               calling user.
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param validValueGUID       unique identifier of the valid value.
     * @param qualifiedName        unique name.
     * @param displayName          displayable descriptive name.
     * @param description          further information.
     * @param usage                how/when should this value be used.
     * @param scope                what is the scope of the values.
     * @param preferredValue       the value that should be used in an implementation if possible.
     * @param isDeprecated         is the valid value deprecated
     * @param additionalProperties additional properties for this valid value.
     * @param extendedProperties   properties that need to be populated into a subtype.
     * @param methodName           calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void updateValidValue(String              userId,
                                 String              externalSourceGUID,
                                 String              externalSourceName,
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
        final String guidParameter = "validValueGUID";
        final String nameParameter = "qualifiedName";

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

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    validValueGUID,
                                    guidParameter,
                                    OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_GUID,
                                    OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                    builder.getInstanceProperties(methodName),
                                    false,
                                    methodName);
    }


    /**
     * Remove the valid value form the repository.  All links to it are deleted too.
     *
     * @param userId             calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param validValueGUID     unique identifier of the value to delete
     * @param qualifiedName      unique name of the value to delete.  This is used to verify that
     *                           the correct valid value is being deleted.
     * @param methodName         calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void deleteValidValue(String userId,
                                 String externalSourceGUID,
                                 String externalSourceName,
                                 String validValueGUID,
                                 String qualifiedName,
                                 String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String guidParameter = "validValueGUID";
        final String nameParameter = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, guidParameter, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);

        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    validValueGUID,
                                    guidParameter,
                                    OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_GUID,
                                    OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                    OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                    qualifiedName,
                                    methodName);
    }


    /**
     * Create a link between a valid value set or definition and a set.  This means the valid
     * value is a member of the set.
     *
     * @param userId             calling user.
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param setGUID            unique identifier of the set.
     * @param validValueGUID     unique identifier of the valid value to add to the set.
     * @param methodName         calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void attachValidValueToSet(String userId,
                                      String externalSourceGUID,
                                      String externalSourceName,
                                      String setGUID,
                                      String validValueGUID,
                                      String methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String setGUIDParameter        = "setGUID";
        final String validValueGUIDParameter = "validValueGUID";

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  setGUID,
                                  setGUIDParameter,
                                  OpenMetadataAPIMapper.VALID_VALUE_SET_TYPE_NAME,
                                  validValueGUID,
                                  validValueGUIDParameter,
                                  OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                  OpenMetadataAPIMapper.VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_GUID,
                                  OpenMetadataAPIMapper.VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_NAME,
                                  null,
                                  methodName);
    }


    /**
     * Remove the link between a valid value and a set it is a member of.
     *
     * @param userId             calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param setGUID            owning set
     * @param validValueGUID     unique identifier of the member to be removed.
     * @param methodName         calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void detachValidValueFromSet(String userId,
                                        String externalSourceGUID,
                                        String externalSourceName,
                                        String setGUID,
                                        String validValueGUID,
                                        String methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String setGUIDParameter        = "setGUID";
        final String validValueGUIDParameter = "validValueGUID";

        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      setGUID,
                                      setGUIDParameter,
                                      OpenMetadataAPIMapper.VALID_VALUE_SET_TYPE_NAME,
                                      validValueGUID,
                                      validValueGUIDParameter,
                                      OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_GUID,
                                      OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                      OpenMetadataAPIMapper.VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_GUID,
                                      OpenMetadataAPIMapper.VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_NAME,
                                      methodName);
    }


    /**
     * Link a valid value to an asset that provides the implementation.  Typically this method is
     * used to link a valid value set to a code table.
     *
     * @param userId              calling user.
     * @param externalSourceGUID  guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName  name of the software server capability entity that represented the external source
     * @param validValueGUID      unique identifier of the valid value.
     * @param assetGUID           unique identifier of the asset that implements the valid value.
     * @param symbolicName        lookup name for valid value
     * @param implementationValue value used in implementation
     * @param additionalValues    additional values stored under the symbolic name
     * @param methodName          calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void linkValidValueToImplementation(String              userId,
                                               String              externalSourceGUID,
                                               String              externalSourceName,
                                               String              validValueGUID,
                                               String              assetGUID,
                                               String              symbolicName,
                                               String              implementationValue,
                                               Map<String, String> additionalValues,
                                               String              methodName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String validValueGUIDParameter = "validValueGUID";
        final String assetGUIDParameter      = "assetGUID";

        InstanceProperties properties = null;

        if (symbolicName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataAPIMapper.SYMBOLIC_NAME_PROPERTY_NAME,
                                                                      symbolicName,
                                                                      methodName);
        }

        if (implementationValue != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.IMPLEMENTATION_VALUE_PROPERTY_NAME,
                                                                      implementationValue,
                                                                      methodName);
        }

        if ((additionalValues != null) && (!additionalValues.isEmpty()))
        {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         OpenMetadataAPIMapper.ADDITIONAL_VALUES_PROPERTY_NAME,
                                                                         additionalValues,
                                                                         methodName);
        }

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  validValueGUID,
                                  validValueGUIDParameter,
                                  OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                  assetGUID,
                                  assetGUIDParameter,
                                  OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                  OpenMetadataAPIMapper.VALID_VALUES_IMPL_RELATIONSHIP_TYPE_GUID,
                                  OpenMetadataAPIMapper.VALID_VALUES_IMPL_RELATIONSHIP_TYPE_NAME,
                                  properties,
                                  methodName);
    }


    /**
     * Remove the link between a valid value and an implementing asset.
     *
     * @param userId             calling user.
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param validValueGUID     unique identifier of the valid value.
     * @param assetGUID          unique identifier of the asset that used to implement the valid value.
     * @param methodName         calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void unlinkValidValueFromImplementation(String userId,
                                                   String externalSourceGUID,
                                                   String externalSourceName,
                                                   String validValueGUID,
                                                   String assetGUID,
                                                   String methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String validValueGUIDParameter = "validValueGUID";
        final String assetGUIDParameter      = "assetGUID";

        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      validValueGUID,
                                      validValueGUIDParameter,
                                      OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                      assetGUID,
                                      assetGUIDParameter,
                                      OpenMetadataAPIMapper.ASSET_TYPE_GUID,
                                      OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                      OpenMetadataAPIMapper.VALID_VALUES_IMPL_RELATIONSHIP_TYPE_GUID,
                                      OpenMetadataAPIMapper.VALID_VALUES_IMPL_RELATIONSHIP_TYPE_NAME,
                                      methodName);
    }


    /**
     * Link a valid value typically to a schema element or glossary term to show that it uses
     * the valid values.
     *
     * @param userId             calling user.
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param validValueGUID     unique identifier of the valid value.
     * @param consumerGUID       unique identifier of the element to link to.
     * @param strictRequirement  the valid values defines the only values that are permitted.
     * @param methodName         calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void assignValidValueToConsumer(String  userId,
                                           String  externalSourceGUID,
                                           String  externalSourceName,
                                           String  validValueGUID,
                                           String  consumerGUID,
                                           boolean strictRequirement,
                                           String  methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String validValueGUIDParameter = "validValueGUID";
        final String consumerGUIDParameter   = "consumerGUID";

        InstanceProperties relationshipProperties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                                                  null,
                                                                                                  OpenMetadataAPIMapper.IS_STRICT_REQUIREMENT_PROPERTY_NAME,
                                                                                                  strictRequirement,
                                                                                                  methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  consumerGUID,
                                  consumerGUIDParameter,
                                  OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                  validValueGUID,
                                  validValueGUIDParameter,
                                  OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                  OpenMetadataAPIMapper.VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_GUID,
                                  OpenMetadataAPIMapper.VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_NAME,
                                  relationshipProperties,
                                  methodName);
    }


    /**
     * Remove the link between a valid value and a consumer.
     *
     * @param userId             calling user.
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param validValueGUID     unique identifier of the valid value.
     * @param consumerGUID       unique identifier of the element to remove the link from.
     * @param methodName         calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void unassignValidValueFromConsumer(String userId,
                                               String externalSourceGUID,
                                               String externalSourceName,
                                               String validValueGUID,
                                               String consumerGUID,
                                               String methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String validValueGUIDParameter = "validValueGUID";
        final String consumerGUIDParameter   = "consumerGUID";

        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      consumerGUID,
                                      consumerGUIDParameter,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                      validValueGUID,
                                      validValueGUIDParameter,
                                      OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_GUID,
                                      OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                      OpenMetadataAPIMapper.VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_GUID,
                                      OpenMetadataAPIMapper.VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_NAME,
                                      methodName);

    }


    /**
     * Link a valid value as a reference value to a referencable to act as a tag/classification to help with locating and
     * grouping the referenceable.
     *
     * @param userId             calling user.
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param validValueGUID     unique identifier of the valid value.
     * @param referenceableGUID  unique identifier of the element to link to.
     * @param confidence         how confident is the steward that this mapping is correct (0-100).
     * @param steward            identifier of steward
     * @param notes              additional notes from the steward
     * @param methodName         calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void assignReferenceValueToItem(String userId,
                                           String externalSourceGUID,
                                           String externalSourceName,
                                           String validValueGUID,
                                           String referenceableGUID,
                                           int    confidence,
                                           String steward,
                                           String notes,
                                           String methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String validValueGUIDParameter    = "validValueGUID";
        final String referenceableGUIDParameter = "referenceableGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(referenceableGUID, referenceableGUIDParameter, methodName);

        InstanceProperties relationshipProperties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                                                              null,
                                                                                              OpenMetadataAPIMapper.VALID_VALUES_CONFIDENCE_PROPERTY_NAME,
                                                                                              confidence,
                                                                                              methodName);

        if (steward != null)
        {
            relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                  null,
                                                                                  OpenMetadataAPIMapper.VALID_VALUES_STEWARD_PROPERTY_NAME,
                                                                                  steward,
                                                                                  methodName);
        }

        if (notes != null)
        {
            relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                  null,
                                                                                  OpenMetadataAPIMapper.VALID_VALUES_NOTES_PROPERTY_NAME,
                                                                                  notes,
                                                                                  methodName);
        }

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  referenceableGUID,
                                  referenceableGUIDParameter,
                                  OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                  validValueGUID,
                                  validValueGUIDParameter,
                                  OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                  OpenMetadataAPIMapper.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP_TYPE_GUID,
                                  OpenMetadataAPIMapper.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP_TYPE_NAME,
                                  relationshipProperties,
                                  methodName);
    }


    /**
     * Remove the reference value link between a valid value and a referenceable (item).
     *
     * @param userId             calling user.
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param validValueGUID     unique identifier of the valid value.
     * @param referenceableGUID  unique identifier of the element to remove the link from.
     * @param methodName         calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void unassignReferenceValueFromItem(String userId,
                                               String externalSourceGUID,
                                               String externalSourceName,
                                               String validValueGUID,
                                               String referenceableGUID,
                                               String methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String validValueGUIDParameter    = "validValueGUID";
        final String referenceableGUIDParameter = "referenceableGUID";

        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      referenceableGUID,
                                      referenceableGUIDParameter,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                      validValueGUID,
                                      validValueGUIDParameter,
                                      OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_GUID,
                                      OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                      OpenMetadataAPIMapper.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP_TYPE_GUID,
                                      OpenMetadataAPIMapper.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP_TYPE_NAME,
                                      methodName);
    }


    /**
     * Link together 2 valid values from different sets that have equivalent values/meanings.
     *
     * @param userId                 calling user.
     * @param externalSourceGUID     guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName     name of the software server capability entity that represented the external source
     * @param validValue1GUID        unique identifier of the valid value.
     * @param validValue2GUID        unique identifier of the other valid value to link to.
     * @param associationDescription how are the valid values related?
     * @param confidence             how confident is the steward that this mapping is correct (0-100).
     * @param steward                identifier of steward
     * @param notes                  additional notes from the steward
     * @param methodName             calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void mapValidValues(String userId,
                               String externalSourceGUID,
                               String externalSourceName,
                               String validValue1GUID,
                               String validValue2GUID,
                               String associationDescription,
                               int    confidence,
                               String steward,
                               String notes,
                               String methodName) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String validValue1GUIDParameter = "validValue1GUID";
        final String validValue2GUIDParameter = "validValue2GUID";

        InstanceProperties relationshipProperties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                                                              null,
                                                                                              OpenMetadataAPIMapper.VALID_VALUES_CONFIDENCE_PROPERTY_NAME,
                                                                                              confidence,
                                                                                              methodName);

        if (associationDescription != null)
        {
            relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                  null,
                                                                                  OpenMetadataAPIMapper.VALID_VALUES_ASSOCIATION_DESCRIPTION_PROPERTY_NAME,
                                                                                  associationDescription,
                                                                                  methodName);
        }

        if (steward != null)
        {
            relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                  null,
                                                                                  OpenMetadataAPIMapper.VALID_VALUES_STEWARD_PROPERTY_NAME,
                                                                                  steward,
                                                                                  methodName);
        }

        if (notes != null)
        {
            relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                  null,
                                                                                  OpenMetadataAPIMapper.VALID_VALUES_NOTES_PROPERTY_NAME,
                                                                                  notes,
                                                                                  methodName);
        }

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  validValue2GUID,
                                  validValue2GUIDParameter,
                                  OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                  validValue1GUID,
                                  validValue1GUIDParameter,
                                  OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                  OpenMetadataAPIMapper.VALID_VALUES_MAP_RELATIONSHIP_TYPE_GUID,
                                  OpenMetadataAPIMapper.VALID_VALUES_MAP_RELATIONSHIP_TYPE_GUID,
                                  relationshipProperties,
                                  methodName);
    }


    /**
     * Remove the reference value link between a valid value and a referenceable (item).
     *
     * @param userId             calling user.
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param validValue1GUID    unique identifier of the valid value.
     * @param validValue2GUID    unique identifier of the other valid value element to remove the link from.
     * @param methodName         calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void unmapValidValues(String userId,
                                 String externalSourceGUID,
                                 String externalSourceName,
                                 String validValue1GUID,
                                 String validValue2GUID,
                                 String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String validValue1GUIDParameter = "validValue1GUID";
        final String validValue2GUIDParameter = "validValue2GUID";

        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      validValue2GUID,
                                      validValue2GUIDParameter,
                                      OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                      validValue1GUID,
                                      validValue1GUIDParameter,
                                      OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_GUID,
                                      OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                      OpenMetadataAPIMapper.VALID_VALUES_MAP_RELATIONSHIP_TYPE_GUID,
                                      OpenMetadataAPIMapper.VALID_VALUES_MAP_RELATIONSHIP_TYPE_GUID,
                                      methodName);

    }


    /**
     * Retrieve a specific valid value from the repository.
     *
     * @param userId         calling user
     * @param validValueGUID unique identifier of the valid value.
     * @param methodName     calling method
     * @return Valid value bean
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public VALID_VALUE getValidValueByGUID(String userId,
                                           String validValueGUID,
                                           String methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String validValueGUIDParameter = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);

        VALID_VALUE validValue = null;

        EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                validValueGUID,
                                                                validValueGUIDParameter,
                                                                OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                methodName);

        if (entity != null)
        {
            validValue = converter.getNewBean(beanClass, entity, methodName);
        }

        return validValue;
    }


    /**
     * Retrieve a specific valid value from the repository.  Duplicates may be returned if
     * multiple valid values have been assigned the same qualified name.
     *
     * @param userId            calling user
     * @param name              name to search for - this must be an exact match on the display name or qualified name
     * @param nameParameterName property that provided the name
     * @param startFrom         starting element (used in paging through large result sets)
     * @param pageSize          maximum number of results to return
     * @param methodName        calling method
     * @return Valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<VALID_VALUE> getValidValueByName(String userId,
                                                 String name,
                                                 String nameParameterName,
                                                 int    startFrom,
                                                 int    pageSize,
                                                 String methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.VALID_VALUE_DISPLAY_NAME_PROPERTY_NAME);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_GUID,
                                    OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                    specificMatchPropertyNames,
                                    true,
                                    startFrom,
                                    pageSize,
                                    methodName);
    }


    /**
     * Locate valid values that match the search string.  It considers the names, description, scope,
     * usage and preferred value.
     *
     * @param userId                    calling user
     * @param searchString              string value to look for - may contain RegEx characters.
     * @param searchStringParameterName name of parameter providing search string
     * @param startFrom                 paging starting point
     * @param pageSize                  maximum number of return values.
     * @param methodName                calling method
     * @return list of valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<VALID_VALUE> findValidValues(String userId,
                                             String searchString,
                                             String searchStringParameterName,
                                             int    startFrom,
                                             int    pageSize,
                                             String methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return this.findBeans(userId,
                              searchString,
                              searchStringParameterName,
                              OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_GUID,
                              OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                              startFrom,
                              pageSize,
                              methodName);
    }


    /**
     * Page through the members of a valid value set.
     *
     * @param userId                     calling user
     * @param validValueSetGUID          unique identifier of the valid value set
     * @param validValueSetGUIDParameter name of parameter providing the validValueSetGUID
     * @param startFrom                  paging starting point
     * @param pageSize                   maximum number of return values.
     * @param methodName                 calling method
     * @return list of valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<VALID_VALUE> getValidValueSetMembers(String userId,
                                                     String validValueSetGUID,
                                                     String validValueSetGUIDParameter,
                                                     int    startFrom,
                                                     int    pageSize,
                                                     String methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        validValueSetGUID,
                                        validValueSetGUIDParameter,
                                        OpenMetadataAPIMapper.VALID_VALUE_SET_TYPE_NAME,
                                        OpenMetadataAPIMapper.VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_GUID,
                                        OpenMetadataAPIMapper.VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_NAME,
                                        OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                        null,
                                        null,
                                        2,
                                        supportedZones,
                                        startFrom,
                                        pageSize,
                                        methodName);
    }


    /**
     * Page through the list of valid value sets that a valid value definition/set belongs to.
     *
     * @param userId                  calling user
     * @param validValueGUID          unique identifier of valid value to query
     * @param validValueGUIDParameter parameter name providing the validValueGUID
     * @param startFrom               paging starting point
     * @param pageSize                maximum number of return values.
     * @param methodName              calling method
     * @return list of valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<VALID_VALUE> getSetsForValidValue(String userId,
                                                  String validValueGUID,
                                                  String validValueGUIDParameter,
                                                  int    startFrom,
                                                  int    pageSize,
                                                  String methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        validValueGUID,
                                        validValueGUIDParameter,
                                        OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                        OpenMetadataAPIMapper.VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_GUID,
                                        OpenMetadataAPIMapper.VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_NAME,
                                        OpenMetadataAPIMapper.VALID_VALUE_SET_TYPE_NAME,
                                        null,
                                        null,
                                        1,
                                        supportedZones,
                                        startFrom,
                                        pageSize,
                                        methodName);
    }


    /**
     * Page through the list of consumers for a valid value.
     *
     * @param userId                  calling user
     * @param validValueGUID          unique identifier of valid value to query
     * @param validValueGUIDParameter parameter name for validValueGUID
     * @param serviceSupportedZones   list of zones that define which assets can be retrieved.
     * @param startFrom               paging starting point
     * @param pageSize                maximum number of return values.
     * @param methodName              calling method
     * @return list of valid value consumer beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<VALID_VALUE_ASSIGNMENT> getValidValuesAssignmentConsumers(String       userId,
                                                                          String       validValueGUID,
                                                                          String       validValueGUIDParameter,
                                                                          List<String> serviceSupportedZones,
                                                                          int          startFrom,
                                                                          int          pageSize,
                                                                          String       methodName) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        final String guidParameterName = "relationship.end1.guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);

        this.validateAnchorEntity(userId,
                                  validValueGUID,
                                  validValueGUIDParameter,
                                  OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                  false,
                                  serviceSupportedZones,
                                  methodName);

        /*
         * Validates the parameters and retrieves the links to attached keywords that are visible to this user.
         * Relationships are returned so that the isPublic property from the relationship can be retrieved.
         */
        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   validValueGUID,
                                                                   validValueGUIDParameter,
                                                                   OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_GUID,
                                                                   OpenMetadataAPIMapper.VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                   startFrom,
                                                                   pageSize,
                                                                   methodName);

        if ((relationships == null) || (relationships.isEmpty()))
        {
            return null;
        }

        List<VALID_VALUE_ASSIGNMENT> results = new ArrayList<>();

        for (Relationship relationship : relationships)
        {
            if (relationship != null)
            {
                EntityProxy end1 = relationship.getEntityOneProxy();
                if (end1 != null)
                {
                    try
                    {
                        EntityDetail consumerEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                        end1.getGUID(),
                                                                                        guidParameterName,
                                                                                        OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                                        methodName);

                        this.validateAnchorEntity(userId,
                                                  end1.getGUID(),
                                                  OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                  consumerEntity,
                                                  guidParameterName,
                                                  false,
                                                  serviceSupportedZones,
                                                  methodName);

                        VALID_VALUE_ASSIGNMENT bean = validValueAssignmentConverter.getNewBean(validValueAssignmentClass,
                                                                                               consumerEntity,
                                                                                               relationship,
                                                                                               methodName);
                        if (bean != null)
                        {
                            results.add(bean);
                        }
                    }
                    catch (InvalidParameterException | UserNotAuthorizedException error)
                    {
                        /* ignore this entity and invisible to this user */
                    }
                }
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
     * Page through the list of valid values assigned to referenceable element.
     *
     * @param userId            calling user
     * @param referenceableGUID unique identifier of anchoring referenceable
     * @param referenceableGUIDParameter name of parameter for referenceableGUID
     * @param startFrom         paging starting point
     * @param pageSize          maximum number of return values.
     * @param methodName        calling method
     * @return list of valid value consumer beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<VALID_VALUE_ASSIGNMENT_DEF> getValidValuesAssignmentDefinition(String userId,
                                                                               String referenceableGUID,
                                                                               String referenceableGUIDParameter,
                                                                               int    startFrom,
                                                                               int    pageSize,
                                                                               String methodName) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String guidParameterName = "relationship.end2.guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(referenceableGUID, referenceableGUIDParameter, methodName);

        this.validateAnchorEntity(userId,
                                  referenceableGUID,
                                  referenceableGUIDParameter,
                                  OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                  false,
                                  supportedZones,
                                  methodName);

        /*
         * Validates the parameters and retrieves the links to attached keywords that are visible to this user.
         * Relationships are returned so that the isPublic property from the relationship can be retrieved.
         */
        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   referenceableGUID,
                                                                   referenceableGUIDParameter,
                                                                   OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_GUID,
                                                                   OpenMetadataAPIMapper.VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                   startFrom,
                                                                   pageSize,
                                                                   methodName);

        if ((relationships == null) || (relationships.isEmpty()))
        {
            return null;
        }

        List<VALID_VALUE_ASSIGNMENT_DEF> results = new ArrayList<>();

        for (Relationship relationship : relationships)
        {
            if (relationship != null)
            {
                EntityProxy end2 = relationship.getEntityTwoProxy();
                if (end2 != null)
                {
                    try
                    {
                        EntityDetail consumerEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                        end2.getGUID(),
                                                                                        guidParameterName,
                                                                                        OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                                        methodName);

                        this.validateAnchorEntity(userId,
                                                  end2.getGUID(),
                                                  OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                  consumerEntity,
                                                  guidParameterName,
                                                  false,
                                                  supportedZones,
                                                  methodName);

                        VALID_VALUE_ASSIGNMENT_DEF bean = validValueAssignmentDefConverter.getNewBean(validValueAssignmentDefClass,
                                                                                                      consumerEntity,
                                                                                                      relationship,
                                                                                                      methodName);
                        if (bean != null)
                        {
                            results.add(bean);
                        }
                    }
                    catch (InvalidParameterException | UserNotAuthorizedException error)
                    {
                        /* ignore this entity and invisible to this user */
                    }
                }
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
     * Page through the list of implementations for a valid value.
     *
     * @param userId         calling user
     * @param validValueGUID unique identifier of valid value to query
     * @param validValueGUIDParameter parameter supplying the validValueGUID value
     * @param serviceSupportedZones list of zones that define which assets can be retrieved.
     * @param startFrom      paging starting point
     * @param pageSize       maximum number of return values.
     * @param methodName     calling method
     * @return list of valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<VALID_VALUE_IMPLEMENTATION> getValidValuesImplementationAssets(String       userId,
                                                                               String       validValueGUID,
                                                                               String       validValueGUIDParameter,
                                                                               List<String> serviceSupportedZones,
                                                                               int          startFrom,
                                                                               int          pageSize,
                                                                               String       methodName) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        final String guidParameterName = "relationship.end1.guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);

        this.validateAnchorEntity(userId,
                                  validValueGUID,
                                  validValueGUIDParameter,
                                  OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                  false,
                                  serviceSupportedZones,
                                  methodName);

        /*
         * Validates the parameters and retrieves the links to attached keywords that are visible to this user.
         * Relationships are returned so that the isPublic property from the relationship can be retrieved.
         */
        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   validValueGUID,
                                                                   validValueGUIDParameter,
                                                                   OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.VALID_VALUES_IMPL_RELATIONSHIP_TYPE_GUID,
                                                                   OpenMetadataAPIMapper.VALID_VALUES_IMPL_RELATIONSHIP_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                   startFrom,
                                                                   pageSize,
                                                                   methodName);

        if ((relationships == null) || (relationships.isEmpty()))
        {
            return null;
        }

        List<VALID_VALUE_IMPLEMENTATION> results = new ArrayList<>();

        for (Relationship relationship : relationships)
        {
            if (relationship != null)
            {
                EntityProxy end1 = relationship.getEntityOneProxy();
                if (end1 != null)
                {
                    try
                    {
                        EntityDetail consumerEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                        end1.getGUID(),
                                                                                        guidParameterName,
                                                                                        OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                                        methodName);

                        this.validateAnchorEntity(userId,
                                                  end1.getGUID(),
                                                  OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                  consumerEntity,
                                                  guidParameterName,
                                                  false,
                                                  serviceSupportedZones,
                                                  methodName);

                        VALID_VALUE_IMPLEMENTATION bean = validValueImplementationConverter.getNewBean(validValueImplementationClass,
                                                                                                       consumerEntity,
                                                                                                       relationship,
                                                                                                       methodName);
                        if (bean != null)
                        {
                            results.add(bean);
                        }
                    }
                    catch (InvalidParameterException | UserNotAuthorizedException error)
                    {
                        /* ignore this entity and invisible to this user */
                    }
                }
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
     * Page through the list of valid values defining the content of a reference data asset.
     * This is always called from the assetHandler after it has checked that the asset is in the right zone.
     *
     * @param userId     calling user
     * @param assetGUID  unique identifier of asset to query
     * @param assetGUIDParameter parameter providing the assetGUID value
     * @param startFrom  paging starting point
     * @param pageSize   maximum number of return values.
     * @param methodName calling method
     * @return list of valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<VALID_VALUE_IMPLEMENTATION_DEF> getValidValuesImplementationDefinitions(String userId,
                                                                                        String assetGUID,
                                                                                        String assetGUIDParameter,
                                                                                        int    startFrom,
                                                                                        int    pageSize,
                                                                                        String methodName) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        final String guidParameterName = "relationship.end2.guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        this.validateAnchorEntity(userId,
                                  assetGUID,
                                  assetGUIDParameter,
                                  OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                  false,
                                  supportedZones,
                                  methodName);

        /*
         * Validates the parameters and retrieves the links to attached keywords that are visible to this user.
         * Relationships are returned so that the isPublic property from the relationship can be retrieved.
         */
        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   assetGUID,
                                                                   assetGUIDParameter,
                                                                   OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.VALID_VALUES_IMPL_RELATIONSHIP_TYPE_GUID,
                                                                   OpenMetadataAPIMapper.VALID_VALUES_IMPL_RELATIONSHIP_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                   startFrom,
                                                                   pageSize,
                                                                   methodName);

        if ((relationships == null) || (relationships.isEmpty()))
        {
            return null;
        }

        List<VALID_VALUE_IMPLEMENTATION_DEF> results = new ArrayList<>();

        for (Relationship relationship : relationships)
        {
            if (relationship != null)
            {
                EntityProxy end2 = relationship.getEntityTwoProxy();
                if (end2 != null)
                {
                    try
                    {
                        EntityDetail consumerEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                        end2.getGUID(),
                                                                                        guidParameterName,
                                                                                        OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                                        methodName);

                        this.validateAnchorEntity(userId,
                                                  end2.getGUID(),
                                                  OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                  consumerEntity,
                                                  guidParameterName,
                                                  false,
                                                  supportedZones,
                                                  methodName);

                        VALID_VALUE_IMPLEMENTATION_DEF bean = validValueImplementationDefConverter.getNewBean(validValueImplementationDefClass,
                                                                                                              consumerEntity,
                                                                                                              relationship,
                                                                                                              methodName);
                        if (bean != null)
                        {
                            results.add(bean);
                        }
                    }
                    catch (InvalidParameterException | UserNotAuthorizedException error)
                    {
                        /* ignore this entity and invisible to this user */
                    }
                }
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
     * Page through the list of mappings for a valid value.  These are other valid values from different valid value sets that are equivalent
     * in some way.  The association description covers the type of association.
     *
     * @param userId         calling user
     * @param validValueGUID unique identifier of valid value to query
     * @param validValueGUIDParameter name of parameter supplying the validValueGUID
     * @param startFrom      paging starting point
     * @param pageSize       maximum number of return values.
     * @param methodName     calling method
     * @return list of mappings to other valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<VALID_VALUE_MAPPING> getValidValueMappings(String userId,
                                                           String validValueGUID,
                                                           String validValueGUIDParameter,
                                                           int    startFrom,
                                                           int    pageSize,
                                                           String methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String guidParameterName = "relationship.end1.guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);

        this.validateAnchorEntity(userId,
                                  validValueGUID,
                                  validValueGUIDParameter,
                                  OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                  false,
                                  supportedZones,
                                  methodName);

        /*
         * Validates the parameters and retrieves the links to attached keywords that are visible to this user.
         * Relationships are returned so that the isPublic property from the relationship can be retrieved.
         */
        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   validValueGUID,
                                                                   validValueGUIDParameter,
                                                                   OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.VALID_VALUES_MAP_RELATIONSHIP_TYPE_GUID,
                                                                   OpenMetadataAPIMapper.VALID_VALUES_MAP_RELATIONSHIP_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                   startFrom,
                                                                   pageSize,
                                                                   methodName);

        if ((relationships == null) || (relationships.isEmpty()))
        {
            return null;
        }

        List<VALID_VALUE_MAPPING> results = new ArrayList<>();

        for (Relationship relationship : relationships)
        {
            if (relationship != null)
            {
                EntityProxy proxy = repositoryHandler.getOtherEnd(validValueGUID,
                                                                  OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                  relationship,
                                                                  methodName);
                if (proxy != null)
                {
                    try
                    {
                        EntityDetail consumerEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                        proxy.getGUID(),
                                                                                        guidParameterName,
                                                                                        OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                                        methodName);

                        this.validateAnchorEntity(userId,
                                                  proxy.getGUID(),
                                                  OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                  consumerEntity,
                                                  guidParameterName,
                                                  false,
                                                  supportedZones,
                                                  methodName);

                        VALID_VALUE_MAPPING bean = validValueMappingConverter.getNewBean(validValueMappingClass,
                                                                                         consumerEntity,
                                                                                         relationship,
                                                                                         methodName);
                        if (bean != null)
                        {
                            results.add(bean);
                        }
                    }
                    catch (InvalidParameterException | UserNotAuthorizedException error)
                    {
                        /* ignore this entity and invisible to this user */
                    }
                }
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
     * Page through the list of referenceables that have this valid value as a reference value.
     *
     * @param userId         calling user
     * @param validValueGUID unique identifier of valid value to query
     * @param validValueGUIDParameter name of parameter that provides the validValueGUID value
     * @param serviceSupportedZones list of zones that define which assets can be retrieved
     * @param startFrom      paging starting point
     * @param pageSize       maximum number of return values
     * @param methodName     calling method
     * @return list of referenceable beans
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to make this request
     * @throws PropertyServerException    the repository is not available or not working properly
     */
    public List<REFERENCE_VALUE_ASSIGNED_ITEM> getReferenceValueAssignedItems(String       userId,
                                                                              String       validValueGUID,
                                                                              String       validValueGUIDParameter,
                                                                              List<String> serviceSupportedZones,
                                                                              int          startFrom,
                                                                              int          pageSize,
                                                                              String       methodName) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException
    {
        final String guidParameterName = "relationship.end1.guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);

        this.validateAnchorEntity(userId,
                                  validValueGUID,
                                  validValueGUIDParameter,
                                  OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                  false,
                                  serviceSupportedZones,
                                  methodName);

        /*
         * Validates the parameters and retrieves the links to attached keywords that are visible to this user.
         * Relationships are returned so that the isPublic property from the relationship can be retrieved.
         */
        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   validValueGUID,
                                                                   validValueGUIDParameter,
                                                                   OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP_TYPE_GUID,
                                                                   OpenMetadataAPIMapper.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                   startFrom,
                                                                   pageSize,
                                                                   methodName);

        if ((relationships == null) || (relationships.isEmpty()))
        {
            return null;
        }

        List<REFERENCE_VALUE_ASSIGNED_ITEM> results = new ArrayList<>();

        for (Relationship relationship : relationships)
        {
            if (relationship != null)
            {
                EntityProxy end1 = relationship.getEntityOneProxy();
                if (end1 != null)
                {
                    try
                    {
                        EntityDetail consumerEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                        end1.getGUID(),
                                                                                        guidParameterName,
                                                                                        OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                                        methodName);

                        this.validateAnchorEntity(userId,
                                                  end1.getGUID(),
                                                  OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                  consumerEntity,
                                                  guidParameterName,
                                                  false,
                                                  serviceSupportedZones,
                                                  methodName);

                        REFERENCE_VALUE_ASSIGNED_ITEM bean = referenceValueAssignedItemConverter.getNewBean(referenceValueAssignedItemClass,
                                                                                                            consumerEntity,
                                                                                                            relationship,
                                                                                                            methodName);
                        if (bean != null)
                        {
                            results.add(bean);
                        }
                    }
                    catch (InvalidParameterException | UserNotAuthorizedException error)
                    {
                        /* ignore this entity and invisible to this user */
                    }
                }
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
     * Page through the list of assigned reference values for a referenceable.
     *
     * @param userId            calling user
     * @param referenceableGUID unique identifier of assigned item
     * @param referenceableGUIDParameterName name of parameter for referenceableGUID
     * @param startFrom         paging starting point
     * @param pageSize          maximum number of return values.
     * @param methodName        calling method
     * @return list of valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<REFERENCE_VALUE_ASSIGNMENT> getReferenceValueAssignments(String userId,
                                                                         String referenceableGUID,
                                                                         String referenceableGUIDParameterName,
                                                                         int    startFrom,
                                                                         int    pageSize,
                                                                         String methodName) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String guidParameterName = "relationship.end2.guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(referenceableGUID, referenceableGUIDParameterName, methodName);

        this.validateAnchorEntity(userId,
                                  referenceableGUID,
                                  referenceableGUIDParameterName,
                                  OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                  false,
                                  supportedZones,
                                  methodName);

        /*
         * Validates the parameters and retrieves the links to attached keywords that are visible to this user.
         * Relationships are returned so that the isPublic property from the relationship can be retrieved.
         */
        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   referenceableGUID,
                                                                   referenceableGUIDParameterName,
                                                                   OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP_TYPE_GUID,
                                                                   OpenMetadataAPIMapper.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                   startFrom,
                                                                   pageSize,
                                                                   methodName);

        if ((relationships == null) || (relationships.isEmpty()))
        {
            return null;
        }

        List<REFERENCE_VALUE_ASSIGNMENT> results = new ArrayList<>();

        for (Relationship relationship : relationships)
        {
            if (relationship != null)
            {
                EntityProxy end2 = relationship.getEntityTwoProxy();
                if (end2 != null)
                {
                    try
                    {
                        EntityDetail consumerEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                        end2.getGUID(),
                                                                                        guidParameterName,
                                                                                        OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                                                        methodName);

                        this.validateAnchorEntity(userId,
                                                  end2.getGUID(),
                                                  OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                  consumerEntity,
                                                  guidParameterName,
                                                  false,
                                                  supportedZones,
                                                  methodName);

                        REFERENCE_VALUE_ASSIGNMENT bean = referenceValueAssignmentConverter.getNewBean(referenceValueAssignmentClass,
                                                                                                       consumerEntity,
                                                                                                       relationship,
                                                                                                       methodName);
                        if (bean != null)
                        {
                            results.add(bean);
                        }
                    }
                    catch (InvalidParameterException | UserNotAuthorizedException error)
                    {
                        /* ignore this entity and invisible to this user */
                    }
                }
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
}
