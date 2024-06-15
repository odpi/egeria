/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ReferenceDataHandler provides the methods to create and maintain lists of valid
 * value definitions grouped into a valid value set.  Both valid value definitions and valid value sets have
 * the same attributes and so inherit from ValidValue where all the attributes are defined.
 * <p>
 * A set is just grouping of valid values.   Valid value definitions and set can be nested many times in other
 * valid value sets.
 */
public class ReferenceDataHandler<VALID_VALUE,
                                  VALID_VALUE_ASSIGNMENT,
                                  VALID_VALUE_ASSIGNMENT_DEF,
                                  VALID_VALUE_IMPLEMENTATION,
                                  VALID_VALUE_IMPLEMENTATION_DEF,
                                  VALID_VALUE_MAPPING,
                                  REFERENCE_VALUE_ASSIGNMENT,
                                  REFERENCE_VALUE_ASSIGNED_ITEM> extends ValidValuesHandler<VALID_VALUE>
{
    private final OpenMetadataAPIGenericConverter<VALID_VALUE_ASSIGNMENT>         validValueAssignmentConverter;
    private final Class<VALID_VALUE_ASSIGNMENT>                                   validValueAssignmentClass;
    private final OpenMetadataAPIGenericConverter<VALID_VALUE_ASSIGNMENT_DEF>     validValueAssignmentDefConverter;
    private final Class<VALID_VALUE_ASSIGNMENT_DEF>                               validValueAssignmentDefClass;
    private final OpenMetadataAPIGenericConverter<VALID_VALUE_IMPLEMENTATION>     validValueImplementationConverter;
    private final Class<VALID_VALUE_IMPLEMENTATION>                               validValueImplementationClass;
    private final OpenMetadataAPIGenericConverter<VALID_VALUE_IMPLEMENTATION_DEF> validValueImplementationDefConverter;
    private final Class<VALID_VALUE_IMPLEMENTATION_DEF>                           validValueImplementationDefClass;
    private final OpenMetadataAPIGenericConverter<VALID_VALUE_MAPPING>            validValueMappingConverter;
    private final Class<VALID_VALUE_MAPPING>                                      validValueMappingClass;
    private final OpenMetadataAPIGenericConverter<REFERENCE_VALUE_ASSIGNMENT>     referenceValueAssignmentConverter;
    private final Class<REFERENCE_VALUE_ASSIGNMENT>                               referenceValueAssignmentClass;
    private final OpenMetadataAPIGenericConverter<REFERENCE_VALUE_ASSIGNED_ITEM>  referenceValueAssignedItemConverter;
    private final Class<REFERENCE_VALUE_ASSIGNED_ITEM>                            referenceValueAssignedItemClass;

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
    public ReferenceDataHandler(OpenMetadataAPIGenericConverter<VALID_VALUE>                    converter,
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
     * Link a valid value to an asset that provides the implementation.  Typically, this method is
     * used to link a valid value set to a code table.
     *
     * @param userId              calling user.
     * @param externalSourceGUID  guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName  name of the software capability entity that represented the external source
     * @param validValueGUID      unique identifier of the valid value.
     * @param assetGUID           unique identifier of the asset that implements the valid value.
     * @param symbolicName        lookup name for valid value
     * @param implementationValue value used in implementation
     * @param additionalValues    additional values stored under the symbolic name
     * @param effectiveFrom       starting time for this relationship (null for all time)
     * @param effectiveTo         ending time for this relationship (null for all time)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                               Date                effectiveFrom,
                                               Date                effectiveTo,
                                               boolean             forLineage,
                                               boolean             forDuplicateProcessing,
                                               Date                effectiveTime,
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
                                                                      OpenMetadataType.SYMBOLIC_NAME_PROPERTY_NAME,
                                                                      symbolicName,
                                                                      methodName);
        }

        if (implementationValue != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataType.IMPLEMENTATION_VALUE_PROPERTY_NAME,
                                                                      implementationValue,
                                                                      methodName);
        }

        if ((additionalValues != null) && (!additionalValues.isEmpty()))
        {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         OpenMetadataType.ADDITIONAL_VALUES_PROPERTY_NAME,
                                                                         additionalValues,
                                                                         methodName);
        }

        this.multiLinkElementToElement(userId,
                                       externalSourceGUID,
                                       externalSourceName,
                                       validValueGUID,
                                       validValueGUIDParameter,
                                       OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                       assetGUID,
                                       assetGUIDParameter,
                                       OpenMetadataType.ASSET.typeName,
                                       forLineage,
                                       forDuplicateProcessing,
                                       supportedZones,
                                       OpenMetadataType.VALID_VALUES_IMPL_RELATIONSHIP.typeGUID,
                                       OpenMetadataType.VALID_VALUES_IMPL_RELATIONSHIP.typeName,
                                       setUpEffectiveDates(properties, effectiveFrom, effectiveTo),
                                       effectiveTime,
                                       methodName);
    }


    /**
     * Remove the link between a valid value and an implementing asset.
     *
     * @param userId             calling user.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param validValueGUID     unique identifier of the valid value.
     * @param assetGUID          unique identifier of the asset that used to implement the valid value.
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName         calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void unlinkValidValueFromImplementation(String  userId,
                                                   String  externalSourceGUID,
                                                   String  externalSourceName,
                                                   String  validValueGUID,
                                                   String  assetGUID,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing,
                                                   Date    effectiveTime,
                                                   String  methodName) throws InvalidParameterException,
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
                                      OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                      assetGUID,
                                      assetGUIDParameter,
                                      OpenMetadataType.ASSET.typeGUID,
                                      OpenMetadataType.ASSET.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.VALID_VALUES_IMPL_RELATIONSHIP.typeGUID,
                                      OpenMetadataType.VALID_VALUES_IMPL_RELATIONSHIP.typeName,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Link together 2 valid values from different sets that have equivalent values/meanings.
     *
     * @param userId                 calling user.
     * @param externalSourceGUID     guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName     name of the software capability entity that represented the external source
     * @param validValue1GUID        unique identifier of the valid value.
     * @param validValue2GUID        unique identifier of the other valid value to link to.
     * @param associationDescription how are the valid values related?
     * @param confidence             how confident is the steward that this mapping is correct (0-100).
     * @param steward                identifier of steward
     * @param stewardTypeName        type of element that represents steward
     * @param stewardPropertyName    property name of steward identifier
     * @param notes                  additional notes from the steward
     * @param effectiveFrom          starting time for this relationship (null for all time)
     * @param effectiveTo            ending time for this relationship (null for all time)
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName             calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void mapValidValues(String  userId,
                               String  externalSourceGUID,
                               String  externalSourceName,
                               String  validValue1GUID,
                               String  validValue2GUID,
                               String  associationDescription,
                               int     confidence,
                               String  steward,
                               String  stewardTypeName,
                               String  stewardPropertyName,
                               String  notes,
                               Date    effectiveFrom,
                               Date    effectiveTo,
                               boolean forLineage,
                               boolean forDuplicateProcessing,
                               Date    effectiveTime,
                               String  methodName) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        final String validValue1GUIDParameter = "validValue1GUID";
        final String validValue2GUIDParameter = "validValue2GUID";

        InstanceProperties relationshipProperties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                                                              null,
                                                                                              OpenMetadataProperty.CONFIDENCE.name,
                                                                                              confidence,
                                                                                              methodName);

        relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                              relationshipProperties,
                                                                              OpenMetadataType.ASSOCIATION_DESCRIPTION_PROPERTY_NAME,
                                                                              associationDescription,
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
                                  validValue2GUID,
                                  validValue2GUIDParameter,
                                  OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                  validValue1GUID,
                                  validValue1GUIDParameter,
                                  OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.VALID_VALUES_MAPPING_RELATIONSHIP.typeGUID,
                                  OpenMetadataType.VALID_VALUES_MAPPING_RELATIONSHIP.typeName,
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
     * @param validValue1GUID    unique identifier of the valid value.
     * @param validValue2GUID    unique identifier of the other valid value element to remove the link from.
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName         calling method
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void unmapValidValues(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  validValue1GUID,
                                 String  validValue2GUID,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime,
                                 String  methodName) throws InvalidParameterException,
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
                                      OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                      validValue1GUID,
                                      validValue1GUIDParameter,
                                      OpenMetadataType.VALID_VALUE_DEFINITION.typeGUID,
                                      OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.VALID_VALUES_MAPPING_RELATIONSHIP.typeGUID,
                                      OpenMetadataType.VALID_VALUES_MAPPING_RELATIONSHIP.typeName,
                                      effectiveTime,
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
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                                                          boolean      forLineage,
                                                                          boolean      forDuplicateProcessing,
                                                                          Date         effectiveTime,
                                                                          String       methodName) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        final String guidParameterName = "relationship.end1.guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);

        /*
         * Validates the parameters and retrieves the links to attached keywords that are visible to this user.
         * Relationships are returned so that the isPublic property from the relationship can be retrieved.
         */
        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   validValueGUID,
                                                                   validValueGUIDParameter,
                                                                   OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                   OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP.typeGUID,
                                                                   OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                   null,
                                                                   OpenMetadataType.REFERENCEABLE.typeName,
                                                                   1,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   serviceSupportedZones,
                                                                   startFrom,
                                                                   pageSize,
                                                                   effectiveTime,
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
                                                                                        OpenMetadataType.REFERENCEABLE.typeName,
                                                                                        forLineage,
                                                                                        forDuplicateProcessing,
                                                                                        effectiveTime,
                                                                                        methodName);

                        this.validateAnchorEntity(userId,
                                                  end1.getGUID(),
                                                  OpenMetadataType.REFERENCEABLE.typeName,
                                                  consumerEntity,
                                                  guidParameterName,
                                                  true,
                                                  false,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  serviceSupportedZones,
                                                  effectiveTime,
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
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName        calling method
     * @return list of valid value consumer beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<VALID_VALUE_ASSIGNMENT_DEF> getValidValuesAssignmentDefinition(String  userId,
                                                                               String  referenceableGUID,
                                                                               String  referenceableGUIDParameter,
                                                                               int     startFrom,
                                                                               int     pageSize,
                                                                               boolean forLineage,
                                                                               boolean forDuplicateProcessing,
                                                                               Date    effectiveTime,
                                                                               String  methodName) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String guidParameterName = "relationship.end2.guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(referenceableGUID, referenceableGUIDParameter, methodName);


        /*
         * Validates the parameters and retrieves the links to attached keywords that are visible to this user.
         * Relationships are returned so that the isPublic property from the relationship can be retrieved.
         */
        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   referenceableGUID,
                                                                   referenceableGUIDParameter,
                                                                   OpenMetadataType.REFERENCEABLE.typeName,
                                                                   OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP.typeGUID,
                                                                   OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                   null,
                                                                   OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                   2,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   supportedZones,
                                                                   startFrom,
                                                                   pageSize,
                                                                   effectiveTime,
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
                                                                                        OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                                        forLineage,
                                                                                        forDuplicateProcessing,
                                                                                        effectiveTime,
                                                                                        methodName);

                        this.validateAnchorEntity(userId,
                                                  end2.getGUID(),
                                                  OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                  consumerEntity,
                                                  guidParameterName,
                                                  true,
                                                  false,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  supportedZones,
                                                  effectiveTime,
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
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                                                               boolean      forLineage,
                                                                               boolean      forDuplicateProcessing,
                                                                               Date         effectiveTime,
                                                                               String       methodName) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        final String guidParameterName = "relationship.end1.guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);

        /*
         * Validates the parameters and retrieves the links to attached keywords that are visible to this user.
         * Relationships are returned so that the isPublic property from the relationship can be retrieved.
         */
        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   validValueGUID,
                                                                   validValueGUIDParameter,
                                                                   OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                   OpenMetadataType.VALID_VALUES_IMPL_RELATIONSHIP.typeGUID,
                                                                   OpenMetadataType.VALID_VALUES_IMPL_RELATIONSHIP.typeName,
                                                                   null,
                                                                   OpenMetadataType.ASSET.typeName,
                                                                   2,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   serviceSupportedZones,
                                                                   startFrom,
                                                                   pageSize,
                                                                   effectiveTime,
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
                EntityProxy end2 = relationship.getEntityTwoProxy();
                if (end2 != null)
                {
                    try
                    {
                        EntityDetail consumerEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                        end2.getGUID(),
                                                                                        guidParameterName,
                                                                                        OpenMetadataType.ASSET.typeName,
                                                                                        forLineage,
                                                                                        forDuplicateProcessing,
                                                                                        effectiveTime,
                                                                                        methodName);

                        this.validateAnchorEntity(userId,
                                                  end2.getGUID(),
                                                  OpenMetadataType.ASSET.typeName,
                                                  consumerEntity,
                                                  guidParameterName,
                                                  true,
                                                  false,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  serviceSupportedZones,
                                                  effectiveTime,
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
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<VALID_VALUE_IMPLEMENTATION_DEF> getValidValuesImplementationDefinitions(String  userId,
                                                                                        String  assetGUID,
                                                                                        String  assetGUIDParameter,
                                                                                        int     startFrom,
                                                                                        int     pageSize,
                                                                                        boolean forLineage,
                                                                                        boolean forDuplicateProcessing,
                                                                                        Date    effectiveTime,
                                                                                        String  methodName) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        final String guidParameterName = "relationship.end2.guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        /*
         * Validates the parameters and retrieves the links to attached keywords that are visible to this user.
         * Relationships are returned so that the isPublic property from the relationship can be retrieved.
         */
        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   assetGUID,
                                                                   assetGUIDParameter,
                                                                   OpenMetadataType.ASSET.typeName,
                                                                   OpenMetadataType.VALID_VALUES_IMPL_RELATIONSHIP.typeGUID,
                                                                   OpenMetadataType.VALID_VALUES_IMPL_RELATIONSHIP.typeName,
                                                                   null,
                                                                   OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                   1,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   supportedZones,
                                                                   startFrom,
                                                                   pageSize,
                                                                   effectiveTime,
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
                EntityProxy end1 = relationship.getEntityOneProxy();
                if (end1 != null)
                {
                    try
                    {
                        EntityDetail consumerEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                        end1.getGUID(),
                                                                                        guidParameterName,
                                                                                        OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                                        forLineage,
                                                                                        forDuplicateProcessing,
                                                                                        effectiveTime,
                                                                                        methodName);

                        this.validateAnchorEntity(userId,
                                                  end1.getGUID(),
                                                  OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                  consumerEntity,
                                                  guidParameterName,
                                                  true,
                                                  false,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  supportedZones,
                                                  effectiveTime,
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
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName     calling method
     * @return list of mappings to other valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<VALID_VALUE_MAPPING> getValidValueMappings(String  userId,
                                                           String  validValueGUID,
                                                           String  validValueGUIDParameter,
                                                           int     startFrom,
                                                           int     pageSize,
                                                           boolean forLineage,
                                                           boolean forDuplicateProcessing,
                                                           Date    effectiveTime,
                                                           String  methodName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String guidParameterName = "relationship.end1.guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);

        EntityDetail startingEntity = repositoryHandler.getEntityByGUID(userId,
                                                                        validValueGUID,
                                                                        validValueGUIDParameter,
                                                                        OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

        /*
         * Validates the parameters and retrieves the links to attached keywords that are visible to this user.
         * Relationships are returned so that the isPublic property from the relationship can be retrieved.
         */
        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   startingEntity,
                                                                   validValueGUIDParameter,
                                                                   OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                   OpenMetadataType.VALID_VALUES_MAPPING_RELATIONSHIP.typeGUID,
                                                                   OpenMetadataType.VALID_VALUES_MAPPING_RELATIONSHIP.typeName,
                                                                   null,
                                                                   OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                   0,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   supportedZones,
                                                                   startFrom,
                                                                   pageSize,
                                                                   effectiveTime,
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
                EntityProxy proxy = repositoryHandler.getOtherEnd(startingEntity.getGUID(),
                                                                  OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                  relationship,
                                                                  0,
                                                                  methodName);
                if (proxy != null)
                {
                    try
                    {
                        EntityDetail consumerEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                        proxy.getGUID(),
                                                                                        guidParameterName,
                                                                                        OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                                        forLineage,
                                                                                        forDuplicateProcessing,
                                                                                        effectiveTime,
                                                                                        methodName);

                        this.validateAnchorEntity(userId,
                                                  proxy.getGUID(),
                                                  OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                  consumerEntity,
                                                  guidParameterName,
                                                  true,
                                                  false,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  supportedZones,
                                                  effectiveTime,
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
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                                                              boolean      forLineage,
                                                                              boolean      forDuplicateProcessing,
                                                                              Date         effectiveTime,
                                                                              String       methodName) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException
    {
        final String guidParameterName = "relationship.end1.guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, validValueGUIDParameter, methodName);

        /*
         * Validates the parameters and retrieves the links to attached keywords that are visible to this user.
         * Relationships are returned so that the isPublic property from the relationship can be retrieved.
         */
        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   validValueGUID,
                                                                   validValueGUIDParameter,
                                                                   OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                   OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeGUID,
                                                                   OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                   null,
                                                                   OpenMetadataType.REFERENCEABLE.typeName,
                                                                   1,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   serviceSupportedZones,
                                                                   startFrom,
                                                                   pageSize,
                                                                   effectiveTime,
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
                                                                                        OpenMetadataType.REFERENCEABLE.typeName,
                                                                                        false,
                                                                                        false,
                                                                                        effectiveTime,
                                                                                        methodName);

                        this.validateAnchorEntity(userId,
                                                  end1.getGUID(),
                                                  OpenMetadataType.REFERENCEABLE.typeName,
                                                  consumerEntity,
                                                  guidParameterName,
                                                  true,
                                                  false,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  serviceSupportedZones,
                                                  effectiveTime,
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
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName        calling method
     * @return list of valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<REFERENCE_VALUE_ASSIGNMENT> getReferenceValueAssignments(String  userId,
                                                                         String  referenceableGUID,
                                                                         String  referenceableGUIDParameterName,
                                                                         int     startFrom,
                                                                         int     pageSize,
                                                                         boolean forLineage,
                                                                         boolean forDuplicateProcessing,
                                                                         Date    effectiveTime,
                                                                         String  methodName) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String guidParameterName = "relationship.end2.guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(referenceableGUID, referenceableGUIDParameterName, methodName);

        /*
         * Validates the parameters and retrieves the links to attached keywords that are visible to this user.
         * Relationships are returned so that the isPublic property from the relationship can be retrieved.
         */
        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   referenceableGUID,
                                                                   referenceableGUIDParameterName,
                                                                   OpenMetadataType.REFERENCEABLE.typeName,
                                                                   OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeGUID,
                                                                   OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                   null,
                                                                   OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                   2,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   supportedZones,
                                                                   startFrom,
                                                                   pageSize,
                                                                   effectiveTime,
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
                                                                                        OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                                        forLineage,
                                                                                        forDuplicateProcessing,
                                                                                        effectiveTime,
                                                                                        methodName);

                        this.validateAnchorEntity(userId,
                                                  end2.getGUID(),
                                                  OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                  consumerEntity,
                                                  guidParameterName,
                                                  true,
                                                  false,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  supportedZones,
                                                  effectiveTime,
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
