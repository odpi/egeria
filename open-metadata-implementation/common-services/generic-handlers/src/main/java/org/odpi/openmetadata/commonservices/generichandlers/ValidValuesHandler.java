/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;

import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ValidValuesHandler provides the methods to create and maintain lists of valid
 * value definitions grouped into a valid value set.  Both valid value definitions and valid value sets have
 * the same attributes and so inherit from ValidValue where all the attributes are defined.
 * <p>
 * A set is just grouping of valid values.   Valid value definitions and set can be nested many times in other
 * valid value sets.
 */
public class ValidValuesHandler<B> extends ReferenceableHandler<B>
{
    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param converter               specific converter for the B bean class
     * @param beanClass               name of bean class that is represented by the generic class B
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
    public ValidValuesHandler(OpenMetadataAPIGenericConverter<B> converter,
                              Class<B>                           beanClass,
                              String                             serviceName,
                              String                             serverName,
                              InvalidParameterHandler            invalidParameterHandler,
                              RepositoryHandler                  repositoryHandler,
                              OMRSRepositoryHelper               repositoryHelper,
                              String                             localServerUserId,
                              OpenMetadataServerSecurityVerifier securityVerifier,
                              List<String>                       supportedZones,
                              List<String>                       defaultZones,
                              List<String>                       publishZones,
                              AuditLog                           auditLog)

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
    }



    /**
     * Create a new valid value set/definition.
     *
     * @param userId               calling user.
     * @param externalSourceGUID   guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software capability entity that represented the external source
     * @param qualifiedName        unique name.
     * @param displayName          displayable descriptive name.
     * @param description          further information.
     * @param category             what is the category of reference data does this fall into?
     * @param usage                how/when should this set be used.
     * @param scope                what is the scope of this set's values.
     * @param isDeprecated         is the valid value deprecated
     * @param isCaseSensitive         is the valid value case-sensitive
     * @param preferredValue       value to use to represent this option.
     * @param dataType the data type of the preferred value.
     * @param additionalProperties additional properties for this set.
     * @param suppliedTypeName     optional type name (default is ValidValueSet since it is the most flexible)
     * @param extendedProperties   properties that need to be populated into a subtype.
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param effectiveFrom        starting time for this relationship (null for all time)
     * @param effectiveTo          ending time for this relationship (null for all time)
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName           calling method
     * @return unique identifier for the new set
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public String createValidValue(String              userId,
                                   String              externalSourceGUID,
                                   String              externalSourceName,
                                   String              qualifiedName,
                                   String              displayName,
                                   String              description,
                                   String              category,
                                   String              usage,
                                   String              scope,
                                   boolean             isDeprecated,
                                   boolean             isCaseSensitive,
                                   String              preferredValue,
                                   String              dataType,
                                   Map<String, String> additionalProperties,
                                   String              suppliedTypeName,
                                   Map<String, Object> extendedProperties,
                                   List<String>        suppliedSupportedZones,
                                   Date                effectiveFrom,
                                   Date                effectiveTo,
                                   Date                effectiveTime,
                                   String              methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String nameParameter = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);

        return createValidValue(userId,
                                externalSourceGUID,
                                externalSourceName,
                                null,
                                null,
                                suppliedTypeName,
                                false,
                                qualifiedName,
                                displayName,
                                description,
                                category,
                                usage,
                                scope,
                                preferredValue,
                                dataType,
                                isDeprecated,
                                isCaseSensitive,
                                additionalProperties,
                                extendedProperties,
                                suppliedSupportedZones,
                                effectiveFrom,
                                effectiveTo,
                                false,
                                false,
                                effectiveTime,
                                methodName);
    }


    /**
     * Create a new valid value set.  This just creates the Set itself.  Members are added either as they are
     * created, or they can be attached to a set after they are created.
     *
     * @param userId               calling user.
     * @param externalSourceGUID   guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software capability entity that represented the external source
     * @param qualifiedName        unique name.
     * @param displayName          displayable descriptive name.
     * @param description          further information.
     * @param category             what is the category of reference data does this fall into?
     * @param usage                how/when should this set be used.
     * @param scope                what is the scope of this set's values.
     * @param isDeprecated         is the valid value deprecated
     * @param isCaseSensitive         is the valid value case-sensitive
     * @param additionalProperties additional properties for this set.
     * @param extendedProperties   properties that need to be populated into a subtype.
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param effectiveFrom        starting time for this relationship (null for all time)
     * @param effectiveTo          ending time for this relationship (null for all time)
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                      String              category,
                                      String              usage,
                                      String              scope,
                                      boolean             isDeprecated,
                                      boolean             isCaseSensitive,
                                      Map<String, String> additionalProperties,
                                      Map<String, Object> extendedProperties,
                                      List<String>        suppliedSupportedZones,
                                      Date                effectiveFrom,
                                      Date                effectiveTo,
                                      Date                effectiveTime,
                                      String              methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        return createValidValue(userId,
                                externalSourceGUID,
                                externalSourceName,
                                null,
                                null,
                                OpenMetadataType.VALID_VALUE_SET.typeName,
                                false,
                                qualifiedName,
                                displayName,
                                description,
                                category,
                                usage,
                                scope,
                                null,
                                null,
                                isDeprecated,
                                isCaseSensitive,
                                additionalProperties,
                                extendedProperties,
                                suppliedSupportedZones,
                                effectiveFrom,
                                effectiveTo,
                                false,
                                false,
                                effectiveTime,
                                methodName);
    }


    /**
     * Create a new valid value definition.
     *
     * @param userId               calling user.
     * @param externalSourceGUID   guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software capability entity that represented the external source
     * @param setGUID              unique identifier of the set to attach this to.
     * @param isDefaultValue       is this the default value for the set?
     * @param qualifiedName        unique name.
     * @param displayName          displayable descriptive name.
     * @param description          further information.
     * @param category             what is the category of reference data does this fall into?
     * @param usage                how/when should this value be used.
     * @param scope                what is the scope of the values.
     * @param preferredValue       the value that should be used in an implementation if possible.
     * @param dataType the data type of the preferred value.
     * @param isDeprecated         is the valid value deprecated
     * @param isCaseSensitive         is the valid value case-sensitive
     * @param additionalProperties additional properties for this definition.
     * @param extendedProperties   properties that need to be populated into a subtype.
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                             boolean             isDefaultValue,
                                             String              qualifiedName,
                                             String              displayName,
                                             String              description,
                                             String              category,
                                             String              usage,
                                             String              scope,
                                             String              preferredValue,
                                             String              dataType,
                                             boolean             isDeprecated,
                                             boolean             isCaseSensitive,
                                             Map<String, String> additionalProperties,
                                             Map<String, Object> extendedProperties,
                                             List<String>        suppliedSupportedZones,
                                             Date                effectiveFrom,
                                             Date                effectiveTo,
                                             boolean             forLineage,
                                             boolean             forDuplicateProcessing,
                                             Date                effectiveTime,
                                             String              methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return createValidValue(userId,
                                externalSourceGUID,
                                externalSourceName,
                                null,
                                setGUID,
                                OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                isDefaultValue,
                                qualifiedName,
                                displayName,
                                description,
                                category,
                                usage,
                                scope,
                                preferredValue,
                                dataType,
                                isDeprecated,
                                isCaseSensitive,
                                additionalProperties,
                                extendedProperties,
                                suppliedSupportedZones,
                                effectiveFrom,
                                effectiveTo,
                                forLineage,
                                forDuplicateProcessing,
                                effectiveTime,
                                methodName);
    }


    /**
     * Create a new valid value definition.
     *
     * @param userId               calling user.
     * @param externalSourceGUID   guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software capability entity that represented the external source
     * @param anchorGUID           unique identifier of the anchor for this valid value
     * @param setGUID              unique identifier of the set to attach this to
     * @param suppliedTypeName     name of the type to create
     * @param isDefaultValue       is this the default value for the set?
     * @param qualifiedName        unique name
     * @param displayName          displayable descriptive name.
     * @param description          further information.
     * @param category             what is the category of reference data does this fall into?
     * @param usage                how/when should this value be used.
     * @param scope                what is the scope of the values.
     * @param preferredValue       the value that should be used in an implementation if possible.
     * @param dataType             the data type of the preferred value.
     * @param isDeprecated         is the valid value deprecated
     * @param isCaseSensitive         is the valid value case-sensitive
     * @param additionalProperties additional properties for this definition.
     * @param extendedProperties   properties that need to be populated into a subtype.
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName           calling method
     * @return unique identifier for the new definition
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public String createValidValue(String              userId,
                                   String              externalSourceGUID,
                                   String              externalSourceName,
                                   String              anchorGUID,
                                   String              setGUID,
                                   String              suppliedTypeName,
                                   boolean             isDefaultValue,
                                   String              qualifiedName,
                                   String              displayName,
                                   String              description,
                                   String              category,
                                   String              usage,
                                   String              scope,
                                   String              preferredValue,
                                   String              dataType,
                                   boolean             isDeprecated,
                                   boolean             isCaseSensitive,
                                   Map<String, String> additionalProperties,
                                   Map<String, Object> extendedProperties,
                                   List<String>        suppliedSupportedZones,
                                   Date                effectiveFrom,
                                   Date                effectiveTo,
                                   boolean             forLineage,
                                   boolean             forDuplicateProcessing,
                                   Date                effectiveTime,
                                   String              methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String nameParameter       = "qualifiedName";
        final String setParameter        = "setGUID";
        final String anchorGUIDParameter = "anchorGUID";
        final String definitionParameter = "definitionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);

        String typeName = OpenMetadataType.VALID_VALUE_SET.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName, OpenMetadataType.VALID_VALUE_DEFINITION.typeName, serviceName, methodName, repositoryHelper);

        ValidValuesBuilder builder = new ValidValuesBuilder(qualifiedName,
                                                            displayName,
                                                            description,
                                                            category,
                                                            usage,
                                                            scope,
                                                            preferredValue,
                                                            dataType,
                                                            isDeprecated,
                                                            isCaseSensitive,
                                                            additionalProperties,
                                                            extendedProperties,
                                                            repositoryHelper,
                                                            serviceName,
                                                            serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.addAnchorGUIDToBuilder(userId,
                                    anchorGUID,
                                    anchorGUIDParameter,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    suppliedSupportedZones,
                                    builder,
                                    methodName);

        String definitionGUID = this.createBeanInRepository(userId,
                                                            externalSourceGUID,
                                                            externalSourceName,
                                                            typeGUID,
                                                            typeName,
                                                            builder,
                                                            effectiveTime,
                                                            methodName);

        if ((definitionGUID != null) && (setGUID != null))
        {
            InstanceProperties relationshipProperties = repositoryHelper.addBooleanPropertyToInstance(serviceName, null, OpenMetadataProperty.IS_DEFAULT_VALUE.name, isDefaultValue, methodName);

            this.uncheckedLinkElementToElement(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               setGUID,
                                               setParameter,
                                               definitionGUID,
                                               definitionParameter,
                                               OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeGUID,
                                               relationshipProperties,
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
     * @param externalSourceGUID   guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software capability entity that represented the external source
     * @param validValueGUID       unique identifier of the valid value.
     * @param qualifiedName        unique name.
     * @param displayName          displayable descriptive name.
     * @param description          further information.
     * @param category             what is the category of reference data does this fall into?
     * @param usage                how/when should this value be used.
     * @param scope                what is the scope of the values.
     * @param preferredValue       the value that should be used in an implementation if possible.
     * @param dataType the data type of the preferred value.
     * @param isDeprecated         is the valid value deprecated
     * @param isCaseSensitive      is the valid value case-sensitive
     * @param additionalProperties additional properties for this valid value.
     * @param extendedProperties   properties that need to be populated into a subtype.
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param effectiveFrom        starting time for this relationship (null for all time)
     * @param effectiveTo          ending time for this relationship (null for all time)
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                 String              category,
                                 String              usage,
                                 String              scope,
                                 boolean             isDeprecated,
                                 boolean             isCaseSensitive,
                                 String              preferredValue,
                                 String              dataType,
                                 Map<String, String> additionalProperties,
                                 Map<String, Object> extendedProperties,
                                 List<String>        suppliedSupportedZones,
                                 Date                effectiveFrom,
                                 Date                effectiveTo,
                                 boolean             isMergeUpdate,
                                 boolean             forLineage,
                                 boolean             forDuplicateProcessing,
                                 Date                effectiveTime,
                                 String              methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String guidParameter = "validValueGUID";
        final String nameParameter = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, guidParameter, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);
        }

        ValidValuesBuilder builder = new ValidValuesBuilder(qualifiedName,
                                                            displayName,
                                                            description,
                                                            category,
                                                            usage,
                                                            scope,
                                                            preferredValue,
                                                            dataType,
                                                            isDeprecated,
                                                            isCaseSensitive,
                                                            additionalProperties,
                                                            extendedProperties,
                                                            repositoryHelper,
                                                            serviceName,
                                                            serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    validValueGUID,
                                    guidParameter,
                                    OpenMetadataType.VALID_VALUE_DEFINITION.typeGUID,
                                    OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    suppliedSupportedZones,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Remove the valid value form the repository.  All links to it are deleted too.
     *
     * @param userId             calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param validValueGUID     unique identifier of the value to delete
     * @param qualifiedName      unique name of the value to delete.  This is used to verify that
     *                           the correct valid value is being deleted.
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName         calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void deleteValidValue(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  validValueGUID,
                                 String  qualifiedName,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime,
                                 String  methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String guidParameter = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, guidParameter, methodName);

        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    validValueGUID,
                                    guidParameter,
                                    OpenMetadataType.VALID_VALUE_DEFINITION.typeGUID,
                                    OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                    false,
                                    OpenMetadataProperty.QUALIFIED_NAME.name,
                                    qualifiedName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Create a link between a valid value set or definition and a set.  This means the valid
     * value is a member of the set.
     *
     * @param userId             calling user.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param setGUID            unique identifier of the set.
     * @param validValueGUID     unique identifier of the valid value to add to the set.
     * @param isDefaultValue     is this the default value for the set?
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName         calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void attachValidValueToSet(String       userId,
                                      String       externalSourceGUID,
                                      String       externalSourceName,
                                      String       setGUID,
                                      String       validValueGUID,
                                      boolean      isDefaultValue,
                                      Date         effectiveFrom,
                                      Date         effectiveTo,
                                      List<String> suppliedSupportedZones,
                                      boolean      forLineage,
                                      boolean      forDuplicateProcessing,
                                      Date         effectiveTime,
                                      String        methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String setGUIDParameter        = "setGUID";
        final String validValueGUIDParameter = "validValueGUID";

        InstanceProperties properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                                      null,
                                                                                      OpenMetadataProperty.IS_DEFAULT_VALUE.name,
                                                                                      isDefaultValue,
                                                                                      methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  setGUID,
                                  setGUIDParameter,
                                  OpenMetadataType.VALID_VALUE_SET.typeName,
                                  validValueGUID,
                                  validValueGUIDParameter,
                                  OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  suppliedSupportedZones,
                                  OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeGUID,
                                  OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName,
                                  this.setUpEffectiveDates(properties, effectiveFrom, effectiveTo),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove the link between a valid value and a set it is a member of.
     *
     * @param userId             calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param setGUID            owning set
     * @param validValueGUID     unique identifier of the member to be removed.
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName         calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void detachValidValueFromSet(String  userId,
                                        String  externalSourceGUID,
                                        String  externalSourceName,
                                        String  setGUID,
                                        String  validValueGUID,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing,
                                        Date    effectiveTime,
                                        String  methodName) throws InvalidParameterException,
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
                                      OpenMetadataType.VALID_VALUE_SET.typeName,
                                      validValueGUID,
                                      validValueGUIDParameter,
                                      OpenMetadataType.VALID_VALUE_DEFINITION.typeGUID,
                                      OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeGUID,
                                      OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Create a link between two valid values that should be used together to populate or validate elements for consistency.
     *
     * @param userId             calling user.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param validValue1GUID    unique identifier of first valid value
     * @param validValue2GUID    unique identifier of second valid value
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName         calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void attachConsistentValidValues(String       userId,
                                            String       externalSourceGUID,
                                            String       externalSourceName,
                                            String       validValue1GUID,
                                            String       validValue2GUID,
                                            Date         effectiveFrom,
                                            Date         effectiveTo,
                                            List<String> suppliedSupportedZones,
                                            boolean      forLineage,
                                            boolean      forDuplicateProcessing,
                                            Date         effectiveTime,
                                            String       methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String validValue1GUIDParameter = "validValue1GUID";
        final String validValue2GUIDParameter = "validValue2GUID";

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  validValue1GUID,
                                  validValue1GUIDParameter,
                                  OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                  validValue2GUID,
                                  validValue2GUIDParameter,
                                  OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  suppliedSupportedZones,
                                  OpenMetadataType.CONSISTENT_VALID_VALUES_RELATIONSHIP.typeGUID,
                                  OpenMetadataType.CONSISTENT_VALID_VALUES_RELATIONSHIP.typeName,
                                  this.setUpEffectiveDates(null, effectiveFrom, effectiveTo),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Link a valid value typically to a schema element or glossary term to show that it uses
     * the valid values.
     *
     * @param userId             calling user.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param validValueGUID     unique identifier of the valid value.
     * @param consumerGUID       unique identifier of the element to link to.
     * @param strictRequirement  the valid values defines the only values that are permitted.
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName         calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void assignValidValueToConsumer(String       userId,
                                           String       externalSourceGUID,
                                           String       externalSourceName,
                                           String       validValueGUID,
                                           String       consumerGUID,
                                           boolean      strictRequirement,
                                           Date         effectiveFrom,
                                           Date         effectiveTo,
                                           List<String> suppliedSupportedZones,
                                           boolean      forLineage,
                                           boolean      forDuplicateProcessing,
                                           Date         effectiveTime,
                                           String       methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String validValueGUIDParameter = "validValueGUID";
        final String consumerGUIDParameter   = "consumerGUID";

        InstanceProperties relationshipProperties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                                                  null,
                                                                                                  OpenMetadataProperty.STRICT_REQUIREMENT.name,
                                                                                                  strictRequirement,
                                                                                                  methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  consumerGUID,
                                  consumerGUIDParameter,
                                  OpenMetadataType.REFERENCEABLE.typeName,
                                  validValueGUID,
                                  validValueGUIDParameter,
                                  OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  suppliedSupportedZones,
                                  OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP.typeGUID,
                                  OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP.typeName,
                                  this.setUpEffectiveDates(relationshipProperties, effectiveFrom, effectiveTo),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove the link between a valid value and a consumer.
     *
     * @param userId             calling user.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param validValueGUID     unique identifier of the valid value.
     * @param consumerGUID       unique identifier of the element to remove the link from.
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName         calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void unassignValidValueFromConsumer(String  userId,
                                               String  externalSourceGUID,
                                               String  externalSourceName,
                                               String  validValueGUID,
                                               String  consumerGUID,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing,
                                               Date    effectiveTime,
                                               String  methodName) throws InvalidParameterException,
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
                                      OpenMetadataType.REFERENCEABLE.typeName,
                                      validValueGUID,
                                      validValueGUIDParameter,
                                      OpenMetadataType.VALID_VALUE_DEFINITION.typeGUID,
                                      OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP.typeGUID,
                                      OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP.typeName,
                                      effectiveTime,
                                      methodName);

    }


    /**
     * Link a valid value as a reference value to a referencable to act as a tag/classification to help with locating and
     * grouping the referenceable.
     *
     * @param userId             calling user.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param validValueGUID     unique identifier of the valid value.
     * @param referenceableGUID  unique identifier of the element to link to.
     * @param attributeName      name of the attribute that this relationship represents
     * @param confidence         how confident is the steward that this mapping is correct (0-100).
     * @param steward                identifier of steward
     * @param stewardTypeName        type of element that represents steward
     * @param stewardPropertyName    property name of steward identifier
     * @param notes              additional notes from the steward
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName         calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void assignReferenceValueToItem(String       userId,
                                           String       externalSourceGUID,
                                           String       externalSourceName,
                                           String       validValueGUID,
                                           String       referenceableGUID,
                                           String       attributeName,
                                           int          confidence,
                                           String       steward,
                                           String       stewardTypeName,
                                           String       stewardPropertyName,
                                           String       notes,
                                           List<String> suppliedSupportedZones,
                                           Date         effectiveFrom,
                                           Date         effectiveTo,
                                           boolean      forLineage,
                                           boolean      forDuplicateProcessing,
                                           Date         effectiveTime,
                                           String       methodName) throws InvalidParameterException,
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
                                                                                              OpenMetadataProperty.CONFIDENCE.name,
                                                                                              confidence,
                                                                                              methodName);

        relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                              relationshipProperties,
                                                                              OpenMetadataProperty.ATTRIBUTE_NAME.name,
                                                                              attributeName,
                                                                              methodName);

        relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                              relationshipProperties,
                                                                              OpenMetadataProperty.STEWARD.name,
                                                                              steward,
                                                                              methodName);

        relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                              relationshipProperties,
                                                                              OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                                              stewardTypeName,
                                                                              methodName);

        relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                              relationshipProperties,
                                                                              OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                                              stewardPropertyName,
                                                                              methodName);

       relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                             relationshipProperties,
                                                                             OpenMetadataProperty.NOTES.name,
                                                                             notes,
                                                                             methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  referenceableGUID,
                                  referenceableGUIDParameter,
                                  OpenMetadataType.REFERENCEABLE.typeName,
                                  validValueGUID,
                                  validValueGUIDParameter,
                                  OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  suppliedSupportedZones,
                                  OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeGUID,
                                  OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName,
                                  this.setUpEffectiveDates(relationshipProperties, effectiveFrom, effectiveTo),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove the reference value link between a valid value and a referenceable (item).
     *
     * @param userId             calling user.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param validValueGUID     unique identifier of the valid value.
     * @param referenceableGUID  unique identifier of the element to remove the link from.
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName         calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void unassignReferenceValueFromItem(String  userId,
                                               String  externalSourceGUID,
                                               String  externalSourceName,
                                               String  validValueGUID,
                                               String  referenceableGUID,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing,
                                               Date    effectiveTime,
                                               String  methodName) throws InvalidParameterException,
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
                                      OpenMetadataType.REFERENCEABLE.typeName,
                                      validValueGUID,
                                      validValueGUIDParameter,
                                      OpenMetadataType.VALID_VALUE_DEFINITION.typeGUID,
                                      OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeGUID,
                                      OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Retrieve a specific valid value from the repository.
     *
     * @param userId         calling user
     * @param validValueGUID unique identifier of the valid value.
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName     calling method
     * @return Valid value bean
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public B getValidValueByGUID(String       userId,
                                 String       validValueGUID,
                                 boolean      forLineage,
                                 boolean      forDuplicateProcessing,
                                 List<String> suppliedSupportedZones,
                                 Date         effectiveTime,
                                 String       methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String validValueGUIDParameter = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);

        return this.getBeanFromRepository(userId,
                                          validValueGUID,
                                          validValueGUIDParameter,
                                          OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                          forLineage,
                                          forDuplicateProcessing,
                                          suppliedSupportedZones,
                                          effectiveTime,
                                          methodName);
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
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName        calling method
     * @return Valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<B> getValidValueByName(String       userId,
                                       String       name,
                                       String       nameParameterName,
                                       int          startFrom,
                                       int          pageSize,
                                       boolean      forLineage,
                                       boolean      forDuplicateProcessing,
                                       List<String> suppliedSupportedZones,
                                       Date         effectiveTime,
                                       String       methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);
        specificMatchPropertyNames.add(OpenMetadataProperty.NAME.name);
        specificMatchPropertyNames.add(OpenMetadataProperty.PREFERRED_VALUE.name);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    OpenMetadataType.VALID_VALUE_DEFINITION.typeGUID,
                                    OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                    specificMatchPropertyNames,
                                    true,
                                    null,
                                    null,
                                    null,
                                    null,
                                    SequencingOrder.CREATION_DATE_RECENT,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    suppliedSupportedZones,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
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
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName                calling method
     * @return list of valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<B> findValidValues(String       userId,
                                   String       searchString,
                                   String       searchStringParameterName,
                                   int          startFrom,
                                   int          pageSize,
                                   boolean      forLineage,
                                   boolean      forDuplicateProcessing,
                                   List<String> suppliedSupportedZones,
                                   Date         effectiveTime,
                                   String       methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        return this.findBeans(userId,
                              searchString,
                              searchStringParameterName,
                              OpenMetadataType.VALID_VALUE_DEFINITION.typeGUID,
                              OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                              null,
                              null,
                              SequencingOrder.CREATION_DATE_RECENT,
                              null,
                              forLineage,
                              forDuplicateProcessing,
                              suppliedSupportedZones,
                              startFrom,
                              pageSize,
                              effectiveTime,
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
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param effectiveTime              the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName                 calling method
     * @return list of valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<B> getValidValueSetMembers(String       userId,
                                           String       validValueSetGUID,
                                           String       validValueSetGUIDParameter,
                                           int          startFrom,
                                           int          pageSize,
                                           boolean      forLineage,
                                           boolean      forDuplicateProcessing,
                                           List<String> suppliedSupportedZones,
                                           Date         effectiveTime,
                                           String       methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        validValueSetGUID,
                                        validValueSetGUIDParameter,
                                        OpenMetadataType.VALID_VALUE_SET.typeName,
                                        OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName,
                                        OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                        (String)null,
                                        null,
                                        2,
                                        null,
                                        null,
                                        SequencingOrder.CREATION_DATE_RECENT,
                                        null,
                                        forLineage,
                                        forDuplicateProcessing,
                                        suppliedSupportedZones,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Page through the consistent values from different valid value sets.
     *
     * @param userId                     calling user
     * @param validValueGUID          unique identifier of the valid value set
     * @param validValueGUIDParameter name of parameter providing the validValueGUID
     * @param startFrom                  paging starting point
     * @param pageSize                   maximum number of return values.
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName                 calling method
     * @return list of valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<B> getConsistentValidValues(String       userId,
                                            String       validValueGUID,
                                            String       validValueGUIDParameter,
                                            int          startFrom,
                                            int          pageSize,
                                            boolean      forLineage,
                                            boolean      forDuplicateProcessing,
                                            List<String> suppliedSupportedZones,
                                            Date         effectiveTime,
                                            String       methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        validValueGUID,
                                        validValueGUIDParameter,
                                        OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                        OpenMetadataType.CONSISTENT_VALID_VALUES_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.CONSISTENT_VALID_VALUES_RELATIONSHIP.typeName,
                                        OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                        (String)null,
                                        null,
                                        0,
                                        null,
                                        null,
                                        SequencingOrder.CREATION_DATE_RECENT,
                                        null,
                                        forLineage,
                                        forDuplicateProcessing,
                                        suppliedSupportedZones,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
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
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName              calling method
     * @return list of valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<B> getSetsForValidValue(String       userId,
                                        String       validValueGUID,
                                        String       validValueGUIDParameter,
                                        int          startFrom,
                                        int          pageSize,
                                        boolean      forLineage,
                                        boolean      forDuplicateProcessing,
                                        List<String> suppliedSupportedZones,
                                        Date         effectiveTime,
                                        String       methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        validValueGUID,
                                        validValueGUIDParameter,
                                        OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                        OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName,
                                        OpenMetadataType.VALID_VALUE_SET.typeName,
                                        (String)null,
                                        null,
                                        1,
                                        null,
                                        null,
                                        SequencingOrder.CREATION_DATE_RECENT,
                                        null,
                                        forLineage,
                                        forDuplicateProcessing,
                                        suppliedSupportedZones,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Page through the list of valid values assigned to referenceable element.
     *
     * @param userId            calling user
     * @param referenceableGUID unique identifier of anchoring referenceable
     * @param referenceableGUIDParameter name of parameter for referenceableGUID
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName        calling method
     * @return list of valid value consumer beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public B getAssignedValidValues(String       userId,
                                    String       referenceableGUID,
                                    String       referenceableGUIDParameter,
                                    boolean      forLineage,
                                    boolean      forDuplicateProcessing,
                                    List<String> suppliedSupportedZones,
                                    Date         effectiveTime,
                                    String       methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        return this.getAttachedElement(userId,
                                       referenceableGUID,
                                       referenceableGUIDParameter,
                                       OpenMetadataType.REFERENCEABLE.typeName,
                                       OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP.typeGUID,
                                       OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP.typeName,
                                       OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                       2,
                                       null,
                                       null,
                                       SequencingOrder.CREATION_DATE_RECENT,
                                       null,
                                       forLineage,
                                       forDuplicateProcessing,
                                       suppliedSupportedZones,
                                       effectiveTime,
                                       methodName);
    }


    /**
     * Page through the list of assigned reference values for a referenceable.
     *
     * @param userId            calling user
     * @param referenceableGUID unique identifier of assigned item
     * @param referenceableGUIDParameterName name of parameter for referenceableGUID
     * @param startFrom         paging starting point
     * @param pageSize          maximum number of return values.
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName        calling method
     * @return list of valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<B> getReferenceValues(String       userId,
                                      String       referenceableGUID,
                                      String       referenceableGUIDParameterName,
                                      int          startFrom,
                                      int          pageSize,
                                      boolean      forLineage,
                                      boolean      forDuplicateProcessing,
                                      List<String> suppliedSupportedZones,
                                      Date         effectiveTime,
                                      String       methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        referenceableGUID,
                                        referenceableGUIDParameterName,
                                        OpenMetadataType.REFERENCEABLE.typeName,
                                        OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName,
                                        OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                        (String)null,
                                        null,
                                        2,
                                        null,
                                        null,
                                        SequencingOrder.CREATION_DATE_RECENT,
                                        null,
                                        forLineage,
                                        forDuplicateProcessing,
                                        suppliedSupportedZones,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Retrieve the list of valid values metadata elements.
     *
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)

     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>  getValidValues(String       userId,
                                   int          startFrom,
                                   int          pageSize,
                                   boolean      forLineage,
                                   boolean      forDuplicateProcessing,
                                   List<String> suppliedSupportedZones,
                                   Date         effectiveTime,
                                   String       methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        return this.getBeansByType(userId,
                                   OpenMetadataType.VALID_VALUE_DEFINITION.typeGUID,
                                   OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                   null,
                                   null,
                                   SequencingOrder.CREATION_DATE_RECENT,
                                   null,
                                   forLineage,
                                   forDuplicateProcessing,
                                   suppliedSupportedZones,
                                   startFrom,
                                   pageSize,
                                   effectiveTime,
                                   methodName);
    }
}
