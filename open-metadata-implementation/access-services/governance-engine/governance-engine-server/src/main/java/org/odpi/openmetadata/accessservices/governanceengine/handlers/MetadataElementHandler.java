/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.handlers;

import org.odpi.openmetadata.accessservices.governanceengine.converters.RelatedElementConverter;
import org.odpi.openmetadata.accessservices.governanceengine.converters.RelatedElementsConverter;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElements;
import org.odpi.openmetadata.frameworks.governanceaction.search.*;
import org.odpi.openmetadata.frameworks.governanceaction.search.ArrayPropertyValue;
import org.odpi.openmetadata.frameworks.governanceaction.search.EnumPropertyValue;
import org.odpi.openmetadata.frameworks.governanceaction.search.MapPropertyValue;
import org.odpi.openmetadata.frameworks.governanceaction.search.PrimitivePropertyValue;
import org.odpi.openmetadata.frameworks.governanceaction.search.StructPropertyValue;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumElementDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.*;

/**
 * MetadataElementHandler manages MetadataElement objects from the Governance Action Framework (GAF).
 * These objects are 1-1 with an open metadata entity.
 */
public class MetadataElementHandler<B> extends OpenMetadataAPIGenericHandler<B>
{
    private PropertyHelper propertyHelper = new PropertyHelper();

    private RelatedElementsConverter<RelatedMetadataElements> relatedElementsConverter;
    private RelatedElementConverter<RelatedMetadataElement>   relatedElementConverter;

    /**
     * Construct the handler for metadata elements.
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones list of zones that the access service should set in all new Asset instances.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public MetadataElementHandler(OpenMetadataAPIGenericConverter<B> converter,
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

        relatedElementsConverter = new RelatedElementsConverter<>(repositoryHelper, serviceName, serverName);
        relatedElementConverter = new RelatedElementConverter<>(repositoryHelper, serviceName, serverName);
    }


    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param userId caller's userId
     * @param elementGUID unique identifier for the metadata element
     * @param methodName calling method
     *
     * @return metadata element properties
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public B getMetadataElementByGUID(String userId,
                                      String elementGUID,
                                      String methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String guidParameterName = "elementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameterName, methodName);

        return this.getBeanFromRepository(userId,
                                          elementGUID,
                                          guidParameterName,
                                          OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                          methodName);
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param userId caller's userId
     * @param uniqueName unique name for the metadata element
     * @param uniqueNameParameterName name of the parameter that passed the unique name (optional)
     * @param uniqueNamePropertyName name of the property from the open types to use in the look up
     * @param methodName calling method
     *
     * @return metadata element properties
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public B getMetadataElementByUniqueName(String userId,
                                            String uniqueName,
                                            String uniqueNameParameterName,
                                            String uniqueNamePropertyName,
                                            String methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String nameParameterName = "uniqueName";
        final String namePropertyName  = "uniqueNamePropertyName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(uniqueName, nameParameterName, methodName);
        invalidParameterHandler.validateName(uniqueNamePropertyName, namePropertyName, methodName);

        invalidParameterHandler.validateUserId(userId, methodName);

        if (uniqueNameParameterName != null)
        {
            invalidParameterHandler.validateName(uniqueName, uniqueNameParameterName, methodName);
        }
        else
        {
            invalidParameterHandler.validateName(uniqueName, nameParameterName, methodName);
        }

        return this.getBeanByUniqueName(userId,
                                        uniqueName,
                                        uniqueNameParameterName,
                                        uniqueNamePropertyName,
                                        OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_GUID,
                                        OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                        methodName);
    }


    /**
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param userId caller's userId
     * @param uniqueName unique name for the metadata element
     * @param uniqueNameParameterName name of the parameter that passed the unique name (optional)
     * @param uniqueNamePropertyName name of the property from the open types to use in the look up
     * @param methodName calling method
     *
     * @return metadata element unique identifier (guid)
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public String getMetadataElementGUIDByUniqueName(String userId,
                                                     String uniqueName,
                                                     String uniqueNameParameterName,
                                                     String uniqueNamePropertyName,
                                                     String methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String nameParameterName = "uniqueName";

        invalidParameterHandler.validateUserId(userId, methodName);

        if (uniqueNameParameterName != null)
        {
            invalidParameterHandler.validateName(uniqueName, uniqueNameParameterName, methodName);
        }
        else
        {
            invalidParameterHandler.validateName(uniqueName, nameParameterName, methodName);
        }

        return this.getBeanGUIDByUniqueName(userId,
                                            uniqueName,
                                            uniqueNameParameterName,
                                            uniqueNamePropertyName,
                                            OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_GUID,
                                            OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                            methodName);
    }



    /**
     * Retrieve the metadata elements that contain the requested string.
     *
     * @param userId caller's userId
     * @param searchString name to retrieve
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     * @throws InvalidParameterException the qualified name is null
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<B> findMetadataElementsWithString(String userId,
                                                  String searchString,
                                                  int    startFrom,
                                                  int    pageSize,
                                                  String methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        return this.findBeans(userId,
                              searchString,
                              searchStringParameterName,
                              OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_GUID,
                              OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                              startFrom,
                              pageSize,
                              methodName);
    }


    /**
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param userId caller's userId
     * @param elementGUID unique identifier for the starting metadata element
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of related elements
     *
     * @throws InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<RelatedMetadataElement> getRelatedMetadataElements(String userId,
                                                                   String elementGUID,
                                                                   int    startingAtEnd,
                                                                   String relationshipTypeName,
                                                                   int    startFrom,
                                                                   int    pageSize,
                                                                   String methodName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String guidParameterName = "elementGUID";
        final String typeNameParameterName = "relationshipTypeName";
        final String otherEndGUIDParameterName = "otherEnd.getGUID()";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, typeNameParameterName, methodName);

        String relationshipTypeGUID = null;

        if (relationshipTypeName != null)
        {
            relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName,
                                                                            null,
                                                                            serviceName,
                                                                            methodName,
                                                                            repositoryHelper);
        }

        int attachmentAtEnd = 0;

        if (startingAtEnd == 1)
        {
            attachmentAtEnd = 2;
        }
        else if (startingAtEnd == 2)
        {
            attachmentAtEnd = 1;
        }

        List<Relationship> relationships = super.getAttachmentLinks(userId,
                                                                    elementGUID,
                                                                    guidParameterName,
                                                                    OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                                                    relationshipTypeGUID,
                                                                    relationshipTypeName,
                                                                    null,
                                                                    OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                                                    attachmentAtEnd,
                                                                    startFrom,
                                                                    pageSize,
                                                                    methodName);

        if (relationships != null)
        {
            List<RelatedMetadataElement> results = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    EntityProxy otherEnd = repositoryHandler.getOtherEnd(elementGUID, relationship);

                    if (otherEnd != null)
                    {
                        EntityDetail otherEndEntity = this.getEntityFromRepository(userId,
                                                                                   otherEnd.getGUID(),
                                                                                   otherEndGUIDParameterName,
                                                                                   OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                                                                   methodName);
                        if (otherEndEntity != null)
                        {
                            results.add(relatedElementConverter.getNewBean(RelatedMetadataElement.class,
                                                                           otherEndEntity,
                                                                           relationship,
                                                                           methodName));
                        }
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
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param userId caller's userId
     * @param metadataElementTypeName type of interest (null means any element type)
     * @param metadataElementSubtypeName optional list of the subtypes of the metadataElementTypeName to
     *                           include in the search results. Null means all subtypes.
     * @param searchProperties Optional list of entity property conditions to match.
     * @param limitResultsByStatus By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param searchClassifications Optional list of classifications to match.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param startingFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<B> findMetadataElements(String                userId,
                                        String                metadataElementTypeName,
                                        List<String>          metadataElementSubtypeName,
                                        SearchProperties      searchProperties,
                                        List<ElementStatus>   limitResultsByStatus,
                                        SearchClassifications searchClassifications,
                                        String                sequencingProperty,
                                        SequencingOrder       sequencingOrder,
                                        int                   startingFrom,
                                        int                   pageSize,
                                        String                methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return super.findBeans(userId,
                               metadataElementTypeName,
                               metadataElementSubtypeName,
                               this.getSearchProperties(searchProperties),
                               this.getInstanceStatuses(limitResultsByStatus),
                               this.getSearchClassifications(searchClassifications),
                               null,
                               sequencingProperty,
                               this.getSequencingOrder(sequencingOrder),
                               startingFrom,
                               pageSize,
                               methodName);
    }


    /**
     * Convert the GAF searchProperties to OMRS searchProperties.
     *
     * @param gafSearchProperties GAF searchProperties
     * @return OMRS searchProperties
     * @throws InvalidParameterException invalid property specification
     */
    private org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties
                   getSearchProperties(SearchProperties gafSearchProperties) throws InvalidParameterException
    {
        if (gafSearchProperties != null)
        {
            org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties
                    omrsSearchProperties = new org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties();

            omrsSearchProperties.setConditions(getPropertyConditions(gafSearchProperties.getConditions()));
            omrsSearchProperties.setMatchCriteria(getMatchCriteria(gafSearchProperties.getMatchCriteria()));

            return omrsSearchProperties;
        }

        return null;
    }


    /**
     * Convert the GAF searchClassifications to OMRS searchClassifications.
     *
     * @param gafSearchClassifications GAF searchClassifications
     * @return OMRS searchClassifications
     * @throws InvalidParameterException invalid property specification
     */
    private org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications
                 getSearchClassifications(SearchClassifications gafSearchClassifications) throws InvalidParameterException
    {
        if (gafSearchClassifications != null)
        {
            org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications
                    omrsSearchClassifications = new org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications();

            if ((gafSearchClassifications.getConditions() != null) && (! gafSearchClassifications.getConditions().isEmpty()))
            {
                List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.ClassificationCondition>
                        omrsClassificationConditions = new ArrayList<>();

                for (ClassificationCondition gafClassificationCondition : gafSearchClassifications.getConditions())
                {
                    org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.ClassificationCondition
                            omrsClassificationCondition = new org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.ClassificationCondition();

                    omrsClassificationCondition.setName(gafClassificationCondition.getName());
                    omrsClassificationCondition.setMatchProperties(this.getSearchProperties(gafClassificationCondition.getSearchProperties()));

                    omrsClassificationConditions.add(omrsClassificationCondition);
                }

                if (! omrsClassificationConditions.isEmpty())
                {
                    omrsSearchClassifications.setConditions(omrsClassificationConditions);
                }
            }

            omrsSearchClassifications.setMatchCriteria(this.getMatchCriteria(gafSearchClassifications.getMatchCriteria()));
        }

        return null;
    }



    /**
     * Convert the GAF propertyConditions to OMRS propertyConditions.
     *
     * @param gafPropertyConditions GAF propertyConditions
     * @return OMRS propertyConditions
     * @throws InvalidParameterException invalid property specification
     */
    private List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition>
           getPropertyConditions(List<PropertyCondition> gafPropertyConditions) throws InvalidParameterException
    {
        if ((gafPropertyConditions != null) && (! gafPropertyConditions.isEmpty()))
        {
            for (PropertyCondition propertyCondition : gafPropertyConditions)
            {
                if (propertyCondition != null)
                {
                    org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition
                            omrsPropertyCondition = new org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition();

                    omrsPropertyCondition.setProperty(propertyCondition.getProperty());
                    omrsPropertyCondition.setOperator(this.getPropertyComparisonOperator(propertyCondition.getOperator()));
                    omrsPropertyCondition.setValue(this.getInstancePropertyValue(propertyCondition.getValue()));
                    omrsPropertyCondition.setNestedConditions(this.getSearchProperties(propertyCondition.getNestedConditions()));
                }
            }
        }

        return null;
    }


    /**
     * Convert the GAF property value to the OMRS InstancePropertyValue.
     *
     * @param propertyValue GAF property value
     * @return OMRS InstancePropertyValue
     * @throws InvalidParameterException invalid property specification
     */
    private InstancePropertyValue getInstancePropertyValue(PropertyValue propertyValue) throws InvalidParameterException
    {
        if (propertyValue != null)
        {
            final String methodName = "getInstancePropertyValue";
            final String typeParameterName = "propertyValue.getTypeName()";

            AttributeTypeDef typeDef = repositoryHelper.getAttributeTypeDefByName(serviceName, propertyValue.getTypeName());

            invalidParameterHandler.validateObject(typeDef, typeParameterName, methodName);

            if (propertyValue instanceof ArrayPropertyValue)
            {
                ArrayPropertyValue gafPropertyValue = (ArrayPropertyValue)propertyValue;

                return this.getArrayPropertyValue(typeDef,
                                                  gafPropertyValue.getArrayCount(),
                                                  propertyHelper.getElementPropertiesAsMap(gafPropertyValue.getArrayValues()));
            }
            else if (propertyValue instanceof EnumPropertyValue)
            {
                EnumPropertyValue gafPropertyValue = (EnumPropertyValue)propertyValue;

                return this.getEnumPropertyValue(typeDef,
                                                 gafPropertyValue.getSymbolicName());
            }
            else if (propertyValue instanceof MapPropertyValue)
            {
                MapPropertyValue gafPropertyValue = (MapPropertyValue)propertyValue;

                return this.getMapPropertyValue(typeDef, propertyHelper.getElementPropertiesAsMap(gafPropertyValue.getMapValues()));
            }
            else if (propertyValue instanceof PrimitivePropertyValue)
            {
                PrimitivePropertyValue gafPropertyValue = (PrimitivePropertyValue)propertyValue;

                return this.getPrimitivePropertyValue(typeDef, gafPropertyValue.getPrimitiveDefCategory(), gafPropertyValue.getPrimitiveValue());
            }
            else if (propertyValue instanceof StructPropertyValue)
            {
                StructPropertyValue gafPropertyValue = (StructPropertyValue)propertyValue;

                return this.getStructPropertyValue(typeDef, propertyHelper.getElementPropertiesAsMap(gafPropertyValue.getAttributes()));
            }
        }

        return null;
    }


    /**
     * Create an OMRS instance property value from a GAF property value.
     *
     * @param typeDef property's type definition
     * @param arrayCount number of elements in the array
     * @param arrayValues values in the array
     * @return OMRS property value
     * @throws InvalidParameterException invalid property specification
     */
    private org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ArrayPropertyValue
    getArrayPropertyValue(AttributeTypeDef     typeDef,
                          int                  arrayCount,
                          Map<String, Object>  arrayValues) throws InvalidParameterException
    {
        final String methodName = "getArrayPropertyValue";

        org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ArrayPropertyValue
                omrsPropertyValue = new org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ArrayPropertyValue();

        omrsPropertyValue.setInstancePropertyCategory(InstancePropertyCategory.ARRAY);
        omrsPropertyValue.setTypeGUID(typeDef.getGUID());
        omrsPropertyValue.setTypeName(typeDef.getName());
        omrsPropertyValue.setArrayCount(arrayCount);

        try
        {
            omrsPropertyValue.setArrayValues(repositoryHelper.addPropertyMapToInstance(serviceName, null, arrayValues, methodName));
        }
        catch (OCFCheckedExceptionBase error)
        {
            final String parameterName = "searchProperties";

            throw new InvalidParameterException(error, parameterName);
        }

        return omrsPropertyValue;
    }


    /**
     * Create an OMRS instance property value from a GAF property value.
     *
     * @param typeDef  property's type definition
     * @param symbolicName enum value
     * @return OMRS property value
     * @throws InvalidParameterException invalid property specification
     */
    private org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue
    getEnumPropertyValue(AttributeTypeDef typeDef,
                         String           symbolicName) throws InvalidParameterException
    {
        final String methodName = "getEnumPropertyValue";
        final String symbolicNameParameterName = "symbolicName";
        final String propertyParameterName = "omrsPropertyValue";

        invalidParameterHandler.validateName(symbolicName, symbolicNameParameterName, methodName);
        org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue omrsPropertyValue = null;

        if (typeDef instanceof EnumDef)
        {
            EnumDef enumDef = (EnumDef)typeDef;

            List<EnumElementDef> enumElementDefs = enumDef.getElementDefs();

            if ((enumElementDefs != null) && (! enumElementDefs.isEmpty()))
            {
                for (EnumElementDef enumElementDef : enumElementDefs)
                {
                    if ((enumElementDef != null) && (symbolicName.equals(enumElementDef.getValue())))
                    {
                        omrsPropertyValue =
                                new org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue();

                        omrsPropertyValue.setInstancePropertyCategory(InstancePropertyCategory.ENUM);
                        omrsPropertyValue.setTypeGUID(typeDef.getGUID());
                        omrsPropertyValue.setTypeName(typeDef.getName());
                        omrsPropertyValue.setSymbolicName(symbolicName);
                        omrsPropertyValue.setOrdinal(enumElementDef.getOrdinal());
                        omrsPropertyValue.setDescription(enumElementDef.getDescription());
                    }
                }
            }
        }

        invalidParameterHandler.validateObject(omrsPropertyValue, propertyParameterName, methodName);
        return omrsPropertyValue;
    }


    /**
     * Create an OMRS instance property value from a GAF property value.
     *
     * @param typeDef property's type definition
     * @param mapValues values in the array
     * @return OMRS property value
     * @throws InvalidParameterException invalid property specification
     */
    private org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.MapPropertyValue
    getMapPropertyValue(AttributeTypeDef    typeDef,
                        Map<String, Object> mapValues) throws InvalidParameterException
    {
        final String methodName = "getMapPropertyValue";

        org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.MapPropertyValue
                omrsPropertyValue = new org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.MapPropertyValue();

        omrsPropertyValue.setInstancePropertyCategory(InstancePropertyCategory.MAP);
        omrsPropertyValue.setTypeGUID(typeDef.getGUID());
        omrsPropertyValue.setTypeName(typeDef.getName());

        try
        {
            omrsPropertyValue.setMapValues(repositoryHelper.addPropertyMapToInstance(serviceName, null, mapValues, methodName));
        }
        catch (OCFCheckedExceptionBase error)
        {
            final String parameterName = "searchProperties";

            throw new InvalidParameterException(error, parameterName);
        }

        return omrsPropertyValue;
    }


    /**
     * Create an OMRS instance property value from a GAF property value.
     *
     * @param typeDef property's type definition
     * @param primitiveDefCategory value type
     * @param primitiveValue value
     * @return OMRS property value
     * @throws InvalidParameterException invalid property specification
     */
    private org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue
    getPrimitivePropertyValue(AttributeTypeDef     typeDef,
                              PrimitiveDefCategory primitiveDefCategory,
                              Object               primitiveValue) throws InvalidParameterException
    {
        final String methodName = "getPrimitivePropertyValue";
        final String valueParameterName = "primitiveValue";
        final String categoryParameterName = "primitiveDefCategory";

        invalidParameterHandler.validateObject(primitiveDefCategory, categoryParameterName, methodName);
        invalidParameterHandler.validateObject(primitiveValue, valueParameterName, methodName);

        org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue
                omrsPropertyValue = new org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue();

        omrsPropertyValue.setInstancePropertyCategory(InstancePropertyCategory.PRIMITIVE);
        omrsPropertyValue.setTypeGUID(typeDef.getGUID());
        omrsPropertyValue.setTypeName(typeDef.getName());
        omrsPropertyValue.setPrimitiveDefCategory(this.getPrimitiveDefCategory(primitiveDefCategory));
        omrsPropertyValue.setPrimitiveValue(primitiveValue);

        return omrsPropertyValue;
    }


    /**
     * Convert the GAF primitive def category to the OMRS version
     *
     * @param gafPrimitiveDefCategory GAF version
     * @return OMRS version
     */
    private org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory
            getPrimitiveDefCategory(PrimitiveDefCategory gafPrimitiveDefCategory)
    {
        if (gafPrimitiveDefCategory != null)
        {
            switch (gafPrimitiveDefCategory)
            {
                case OM_PRIMITIVE_TYPE_UNKNOWN:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;

                case OM_PRIMITIVE_TYPE_BOOLEAN:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BOOLEAN;

                case OM_PRIMITIVE_TYPE_BYTE:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BYTE;

                case OM_PRIMITIVE_TYPE_CHAR:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_CHAR;

                case OM_PRIMITIVE_TYPE_SHORT:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_SHORT;

                case OM_PRIMITIVE_TYPE_INT:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT;

                case OM_PRIMITIVE_TYPE_LONG:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG;

                case OM_PRIMITIVE_TYPE_FLOAT:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_FLOAT;

                case OM_PRIMITIVE_TYPE_DOUBLE:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DOUBLE;

                case OM_PRIMITIVE_TYPE_BIGINTEGER:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BIGINTEGER;

                case OM_PRIMITIVE_TYPE_BIGDECIMAL:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BIGDECIMAL;

                case OM_PRIMITIVE_TYPE_STRING:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING;

                case OM_PRIMITIVE_TYPE_DATE:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE;

            }
        }

        return null;
    }


    /**
     * Create an OMRS instance property value from a GAF property value.
     *
     * @param typeDef property's type definition
     * @param attributes values in the array
     * @return OMRS property value
     * @throws InvalidParameterException invalid property specification
     */
    private org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.StructPropertyValue
    getStructPropertyValue(AttributeTypeDef    typeDef,
                           Map<String, Object> attributes) throws InvalidParameterException
    {
        final String methodName = "getStructPropertyValue";

        org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.StructPropertyValue
                omrsPropertyValue = new org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.StructPropertyValue();

        omrsPropertyValue.setInstancePropertyCategory(InstancePropertyCategory.STRUCT);
        omrsPropertyValue.setTypeGUID(typeDef.getGUID());
        omrsPropertyValue.setTypeName(typeDef.getName());

        try
        {
            omrsPropertyValue.setAttributes(repositoryHelper.addPropertyMapToInstance(serviceName, null, attributes, methodName));
        }
        catch (OCFCheckedExceptionBase error)
        {
            final String parameterName = "searchProperties";

            throw new InvalidParameterException(error, parameterName);
        }

        return omrsPropertyValue;
    }


    /**
     * Convert the GAF matchCriteria to OMRS matchCriteria.
     *
     * @param gafMatchCriteria GAF matchCriteria
     * @return OMRS matchCriteria
     */
    private org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria
                  getMatchCriteria(MatchCriteria gafMatchCriteria)
    {
        if (gafMatchCriteria != null)
        {
            switch (gafMatchCriteria)
            {
                case ALL:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria.ALL;

                case ANY:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria.ANY;

                case NONE:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria.NONE;
            }
        }

        return null;
    }


    /**
     * Convert the GAF propertyComparisonOperator to the OMRS propertyComparisonOperator.
     *
     * @param gafPropertyComparisonOperator GAF propertyComparisonOperator
     * @return OMRS propertyComparisonOperator
     */
    private org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator
                 getPropertyComparisonOperator(PropertyComparisonOperator gafPropertyComparisonOperator)
    {
        if (gafPropertyComparisonOperator != null)
        {
            switch (gafPropertyComparisonOperator)
            {
                case EQ:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator.EQ;

                case NEQ:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator.NEQ;

                case LT:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator.LT;

                case LTE:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator.LTE;

                case GT:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator.GT;

                case GTE:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator.GTE;

                case IN:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator.IN;

                case IS_NULL:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator.IS_NULL;

                case NOT_NULL:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator.NOT_NULL;

                case LIKE:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator.LIKE;
            }
        }

        return null;
    }


    /**
     * Convert the GAF sequencingOrder to the OMRS sequencingOrder.
     *
     * @param gafSequencingOrder GAF sequencingOrder
     * @return OMRS sequencingOrder
     */
    private org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder
                getSequencingOrder(SequencingOrder gafSequencingOrder)
    {
        if (gafSequencingOrder != null)
        {
            switch (gafSequencingOrder)
            {
                case ANY:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder.ANY;

                case GUID:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder.GUID;

                case CREATION_DATE_RECENT:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder.CREATION_DATE_RECENT;

                case CREATION_DATE_OLDEST:
                    return  org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder.CREATION_DATE_OLDEST;

                case LAST_UPDATE_RECENT:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder.LAST_UPDATE_RECENT;

                case LAST_UPDATE_OLDEST:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder.LAST_UPDATE_OLDEST;

                case PROPERTY_ASCENDING:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder.PROPERTY_ASCENDING;

                case PROPERTY_DESCENDING:
                    return org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder.PROPERTY_DESCENDING;
            }
        }

        return null;
    }

    /**
     * Convert a list of GAF ElementStatuses into a list of OMRS InstanceStatuses.
     *
     * @param elementStatuses GAF ElementStatuses
     * @return OMRS InstanceStatuses
     */
    private List<InstanceStatus> getInstanceStatuses(List<ElementStatus> elementStatuses)
    {
        if (elementStatuses != null)
        {
            List<InstanceStatus> instanceStatuses = new ArrayList<>();

            for(ElementStatus elementStatus : elementStatuses)
            {
                if (elementStatus != null)
                {
                    instanceStatuses.add(getInstanceStatus(elementStatus));
                }
            }

            if (! instanceStatuses.isEmpty())
            {
                return instanceStatuses;
            }
        }

        return null;
    }


    /**
     * Convert a GAF ElementStatus into an OMRS InstanceStatus.
     *
     * @param elementStatus GAF ElementStatus
     * @return OMRS InstanceStatus
     */
    private InstanceStatus getInstanceStatus(ElementStatus elementStatus)
    {
        if (elementStatus != null)
        {
            switch (elementStatus)
            {
                case UNKNOWN:
                    return InstanceStatus.UNKNOWN;

                case DRAFT:
                    return InstanceStatus.DRAFT;

                case PREPARED:
                    return InstanceStatus.PREPARED;

                case PROPOSED:
                    return InstanceStatus.PROPOSED;

                case APPROVED:
                    return InstanceStatus.APPROVED;

                case REJECTED:
                    return InstanceStatus.REJECTED;

                case APPROVED_CONCEPT:
                    return InstanceStatus.APPROVED_CONCEPT;

                case UNDER_DEVELOPMENT:
                    return InstanceStatus.UNDER_DEVELOPMENT;

                case DEVELOPMENT_COMPLETE:
                    return InstanceStatus.DEVELOPMENT_COMPLETE;

                case APPROVED_FOR_DEPLOYMENT:
                    return InstanceStatus.APPROVED_FOR_DEPLOYMENT;

                case STANDBY:
                    return InstanceStatus.STANDBY;

                case ACTIVE:
                    return InstanceStatus.ACTIVE;

                case FAILED:
                    return InstanceStatus.FAILED;

                case DISABLED:
                    return InstanceStatus.DISABLED;

                case COMPLETE:
                    return InstanceStatus.COMPLETE;

                case DEPRECATED:
                    return InstanceStatus.DEPRECATED;

                case OTHER:
                    return InstanceStatus.OTHER;
            }
        }

        return null;
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param userId caller's userId
     * @param relationshipTypeName relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param searchProperties Optional list of relationship property conditions to match.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public  List<RelatedMetadataElements> findRelationshipsBetweenMetadataElements(String           userId,
                                                                                   String           relationshipTypeName,
                                                                                   SearchProperties searchProperties,
                                                                                   String           sequencingProperty,
                                                                                   SequencingOrder  sequencingOrder,
                                                                                   int              startFrom,
                                                                                   int              pageSize,
                                                                                   String           methodName) throws InvalidParameterException,
                                                                                                                       UserNotAuthorizedException,
                                                                                                                       PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        List<Relationship> relationships = this.findAttachmentLinks(userId,
                                                                    relationshipTypeName,
                                                                    this.getSearchProperties(searchProperties),
                                                                    null,
                                                                    null,
                                                                    sequencingProperty,
                                                                    this.getSequencingOrder(sequencingOrder),
                                                                    startFrom,
                                                                    pageSize,
                                                                    methodName);

        if (relationships != null)
        {
            List<RelatedMetadataElements> results = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    results.add(relatedElementsConverter.getNewRelationshipBean(RelatedMetadataElements.class,
                                                                                relationship,
                                                                                methodName));
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
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param userId caller's userId
     * @param metadataElementTypeName type name of the new metadata element
     * @param initialStatus initial status of the metadata element
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties properties of the new metadata element
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all of the attachments such as nested content, schema
     *                     connection etc)
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createMetadataElementInStore(String            userId,
                                               String            metadataElementTypeName,
                                               ElementStatus     initialStatus,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties,
                                               String            templateGUID,
                                               String            methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String elementTypeParameterName  = "metadataElementTypeName";
        final String templateGUIDParameterName = "templateGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(metadataElementTypeName, elementTypeParameterName, methodName);

        String metadataElementTypeGUID = invalidParameterHandler.validateTypeName(metadataElementTypeName,
                                                                                  OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                                                                  serviceName,
                                                                                  methodName,
                                                                                  repositoryHelper);

        MetadataElementBuilder builder = new MetadataElementBuilder(propertyHelper.getElementPropertiesAsMap(properties),
                                                                    this.getInstanceStatus(initialStatus),
                                                                    effectiveFrom,
                                                                    effectiveTo,
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);

        if (templateGUID == null)
        {
            return this.createBeanInRepository(userId,
                                               null,
                                               null,
                                               metadataElementTypeGUID,
                                               metadataElementTypeName,
                                               null,
                                               null,
                                               builder,
                                               methodName);
        }
        else
        {
            return this.createBeanFromTemplate(userId,
                                               null,
                                               null,
                                               templateGUID,
                                               templateGUIDParameterName,
                                               metadataElementTypeGUID,
                                               metadataElementTypeName,
                                               null,
                                               null,
                                               builder,
                                               methodName);
        }
    }



    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all of them by
     * the value used in the replaceProperties flag.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the metadata element
     * @param methodName calling method
     *
     * @throws InvalidParameterException either the unique identifier or the properties are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateMetadataElementInStore(String            userId,
                                             String            metadataElementGUID,
                                             boolean           replaceProperties,
                                             ElementProperties properties,
                                             String            methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String guidParameterName = "metadataElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        MetadataElementBuilder builder = new MetadataElementBuilder(propertyHelper.getElementPropertiesAsMap(properties),
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);

        this.updateBeanInRepository(userId,
                                    null,
                                    null,
                                    metadataElementGUID,
                                    guidParameterName,
                                    OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_GUID,
                                    OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                    builder.getInstanceProperties(methodName),
                                    ! replaceProperties,
                                    methodName);
    }


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.  The effectivity dates control the visibility of the element
     * through specific APIs.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param newElementStatus new status value - or null to leave as is
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param methodName calling method
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateMetadataElementStatusInStore(String        userId,
                                                   String        metadataElementGUID,
                                                   ElementStatus newElementStatus,
                                                   Date          effectiveFrom,
                                                   Date          effectiveTo,
                                                   String        methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String guidParameterName = "metadataElementGUID";
        final String statusParameterName = "newElementStatus";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        super.updateBeanStatusInRepository(userId,
                                           null,
                                           null,
                                           metadataElementGUID,
                                           guidParameterName,
                                           OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_GUID,
                                           OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                           this.getInstanceStatus(newElementStatus),
                                           statusParameterName,
                                           methodName);

        super.updateBeanEffectivityDates(userId,
                                         null,
                                         null,
                                         metadataElementGUID,
                                         guidParameterName,
                                         OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_GUID,
                                         OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                         effectiveFrom,
                                         effectiveTo,
                                         methodName);
    }


    /**
     * Delete a specific metadata element.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param methodName calling method
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public  void deleteMetadataElementInStore(String userId,
                                              String metadataElementGUID,
                                              String methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String guidParameterName = "metadataElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        super.deleteBeanInRepository(userId,
                                     null,
                                     null,
                                     metadataElementGUID,
                                     guidParameterName,
                                     OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_GUID,
                                     OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                     null,
                                     null,
                                     methodName);
    }


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param effectiveFrom the date when this classification is active - null for active now
     * @param effectiveTo the date when this classification becomes inactive - null for active until deleted
     * @param properties properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     * @param methodName calling method
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void classifyMetadataElementInStore(String            userId,
                                               String            metadataElementGUID,
                                               String            classificationName,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties,
                                               String            methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String guidParameterName = "metadataElementGUID";
        final String classificationParameterName = "classificationName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        String classificationTypeGUID = invalidParameterHandler.validateTypeName(classificationName,
                                                                                 OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                                                                 serviceName,
                                                                                 methodName,
                                                                                 repositoryHelper);

        MetadataElementBuilder builder = new MetadataElementBuilder(repositoryHelper, serviceName, serverName);

        InstanceProperties classificationProperties = builder.getInstanceProperties(this.getElementPropertiesAsOMRSMap(properties),
                                                                                    effectiveFrom,
                                                                                    effectiveTo);

        this.setClassificationInRepository(userId,
                                           null,
                                           null,
                                           metadataElementGUID,
                                           guidParameterName,
                                           OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                           classificationTypeGUID,
                                           classificationName,
                                           classificationProperties,
                                           false,
                                           methodName);
    }


    /**
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the classification
     * @param methodName calling method
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element/classification
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public  void reclassifyMetadataElementInStore(String            userId,
                                                  String            metadataElementGUID,
                                                  String            classificationName,
                                                  boolean           replaceProperties,
                                                  ElementProperties properties,
                                                  String            methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String guidParameterName = "metadataElementGUID";
        final String classificationParameterName = "classificationName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        String classificationTypeGUID = invalidParameterHandler.validateTypeName(classificationName,
                                                                                 OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                                                                 serviceName,
                                                                                 methodName,
                                                                                 repositoryHelper);

        MetadataElementBuilder builder = new MetadataElementBuilder(repositoryHelper, serviceName, serverName);

        InstanceProperties classificationProperties = builder.getInstanceProperties(this.getElementPropertiesAsOMRSMap(properties),
                                                                                    null,
                                                                                    null);

        this.setClassificationInRepository(userId,
                                           null,
                                           null,
                                           metadataElementGUID,
                                           guidParameterName,
                                           OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                           classificationTypeGUID,
                                           classificationName,
                                           classificationProperties,
                                           ! replaceProperties,
                                           methodName);
    }


    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param methodName calling method
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateClassificationStatusInStore(String userId,
                                                  String metadataElementGUID,
                                                  String classificationName,
                                                  Date   effectiveFrom,
                                                  Date   effectiveTo,
                                                  String methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String guidParameterName = "metadataElementGUID";
        final String classificationParameterName = "classificationName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        String classificationTypeGUID = invalidParameterHandler.validateTypeName(classificationName,
                                                                                 OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                                                                 serviceName,
                                                                                 methodName,
                                                                                 repositoryHelper);

        super.updateClassificationEffectivityDates(userId,
                                                   null,
                                                   null,
                                                   metadataElementGUID,
                                                   guidParameterName,
                                                   OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                                   classificationTypeGUID,
                                                   classificationName,
                                                   effectiveFrom,
                                                   effectiveTo,
                                                   methodName);
    }


    /**
     * Remove the named classification from a specific metadata element.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to remove
     * @param methodName calling method
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to remove this classification
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public  void unclassifyMetadataElementInStore(String userId,
                                                  String metadataElementGUID,
                                                  String classificationName,
                                                  String methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String guidParameterName = "metadataElementGUID";
        final String classificationParameterName = "classificationName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        String classificationTypeGUID = invalidParameterHandler.validateTypeName(classificationName,
                                                                                 OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                                                                 serviceName,
                                                                                 methodName,
                                                                                 repositoryHelper);
        super.removeClassificationFromRepository(userId,
                                                 null,
                                                 null,
                                                 metadataElementGUID,
                                                 guidParameterName,
                                                 OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                                 classificationTypeGUID,
                                                 classificationName,
                                                 methodName);
    }



    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param userId caller's userId
     * @param relationshipTypeName name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                             related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties the properties of the relationship
     * @param methodName calling method
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createRelatedElementsInStore(String            userId,
                                               String            relationshipTypeName,
                                               String            metadataElement1GUID,
                                               String            metadataElement2GUID,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties,
                                               String            methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String elementTypeParameterName = "relationshipTypeName";
        final String end1ParameterName = "metadataElement1GUID";
        final String end2ParameterName = "metadataElement2GUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, elementTypeParameterName, methodName);
        invalidParameterHandler.validateGUID(metadataElement1GUID, end1ParameterName, methodName);
        invalidParameterHandler.validateGUID(metadataElement2GUID, end2ParameterName, methodName);

        String relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName,
                                                                               null,
                                                                               serviceName,
                                                                               methodName,
                                                                               repositoryHelper);

        MetadataElementBuilder builder = new MetadataElementBuilder(repositoryHelper, serviceName, serverName);

        InstanceProperties relationshipProperties = builder.getInstanceProperties(this.getElementPropertiesAsOMRSMap(properties),
                                                                                  effectiveFrom,
                                                                                  effectiveTo);
        super.linkElementToElement(userId,
                                   null,
                                   null,
                                   metadataElement1GUID,
                                   end1ParameterName,
                                   OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                   metadataElement2GUID,
                                   end2ParameterName,
                                   OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                   relationshipTypeGUID,
                                   relationshipTypeName,
                                   relationshipProperties,
                                   methodName);

        return null;
    }


    /**
     * Convert an element properties object into a map.
     *
     * @param properties packed properties
     * @return properties stored in Java map
     */
    public Map<String, InstancePropertyValue> getElementPropertiesAsOMRSMap(ElementProperties    properties) throws InvalidParameterException
    {
        if (properties != null)
        {
            Map<String, PropertyValue>         propertyValues = properties.getInstanceProperties();
            Map<String, InstancePropertyValue> resultingMap   = new HashMap<>();

            if (propertyValues != null)
            {
                for (String mapPropertyName : propertyValues.keySet())
                {
                    PropertyValue         actualPropertyValue = properties.getPropertyValue(mapPropertyName);
                    InstancePropertyValue instancePropertyValue = this.getInstancePropertyValue(actualPropertyValue);

                    resultingMap.put(mapPropertyName, instancePropertyValue);
                }
            }

            return resultingMap;
        }

        return null;
    }


    /**
     * Update the properties associated with a relationship.
     *
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the relationship
     * @param methodName calling method
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateRelatedElementsInStore(String            userId,
                                             String            relationshipGUID,
                                             boolean           replaceProperties,
                                             ElementProperties properties,
                                             String            methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String guidParameterName = "relationshipGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, guidParameterName, methodName);

        MetadataElementBuilder builder = new MetadataElementBuilder(repositoryHelper, serviceName, serverName);

        InstanceProperties relationshipProperties = builder.getInstanceProperties(this.getElementPropertiesAsOMRSMap(properties),
                                                                                  null,
                                                                                  null);
        this.updateRelationshipProperties(userId,
                                          null,
                                          null,
                                          relationshipGUID,
                                          guidParameterName,
                                          null,
                                          ! replaceProperties,
                                          relationshipProperties,
                                          methodName);
    }


    /**
     * Update the effectivity dates of a specific relationship between metadata elements.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to update
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param methodName calling method
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public  void updateRelatedElementsStatusInStore(String userId,
                                                    String relationshipGUID,
                                                    Date   effectiveFrom,
                                                    Date   effectiveTo,
                                                    String methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String guidParameterName = "relationshipGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, guidParameterName, methodName);

        super.updateRelationshipEffectivityDates(userId,
                                                 null,
                                                 null,
                                                 relationshipGUID,
                                                 guidParameterName,
                                                 null,
                                                 effectiveFrom,
                                                 effectiveTo,
                                                 methodName);

    }


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to delete
     * @param methodName calling method
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void deleteRelatedElementsInStore(String userId,
                                             String relationshipGUID,
                                             String methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String guidParameterName = "relationshipGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, guidParameterName, methodName);

        repositoryHandler.removeRelationship(userId,
                                             null,
                                             null,
                                             null,
                                             relationshipGUID,
                                             methodName);
    }
}
