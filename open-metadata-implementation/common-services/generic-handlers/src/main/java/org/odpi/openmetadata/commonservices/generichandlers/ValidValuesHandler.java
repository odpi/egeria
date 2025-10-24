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
     * @param userDefinedStatus         is the valid value deprecated
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
                                   String              userDefinedStatus,
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

        String typeName = OpenMetadataType.VALID_VALUE_DEFINITION.typeName;

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
                                                            userDefinedStatus,
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
                                        OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
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
}
