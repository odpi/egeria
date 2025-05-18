/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.enums.DataItemSortOrder;
import org.odpi.openmetadata.frameworks.openmetadata.enums.KeyPattern;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementOriginCategory;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.*;


/**
 * OpenMetadataAPIGenericConverter provides the generic methods for the bean converters used to provide translation between
 * specific Open Metadata API beans and the repository services API beans.
 * Generic classes have limited knowledge of the classes these are working on and this means creating a new instance of a
 * class from within a generic is a little involved.  This class provides the generic method for creating
 * and initializing an Open Metadata API bean.
 */
public abstract class OpenMetadataAPIGenericConverter<B>
{
    protected OMRSRepositoryHelper   repositoryHelper;
    protected String                 serviceName;
    protected String                 serverName;


    /**
     * Constructor captures the initial content
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName name of this server
     */
    public OpenMetadataAPIGenericConverter(OMRSRepositoryHelper   repositoryHelper,
                                           String                 serviceName,
                                           String                 serverName)
    {
        this.repositoryHelper = repositoryHelper;
        this.serviceName = serviceName;
        this.serverName = serverName;
    }


    /* ====================================================================================================
     * This first set of methods represents the external interface of the converter.  These are the methods
     * called from the generic handlers.  They define which type of bean is required and provide a set
     * of repository instances to use to fill the bean.  These methods are overridden by the specific
     * converters.
     *
     * To decide which methods to implement, look at the code in the generic handler that the OMAS is using.
     * If an OMAS does not implement one of the methods that the handlers they are using call then there will be a
     * PropertyServerException.
     */


    /**
     * Using the supplied entity, return a new instance of the bean. This is used for most beans that have
     * a one to one correspondence with the repository instances.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the entity supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        String       methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewBean(entity)";

        handleUnimplementedConverterMethod(beanClass.getName(), thisMethodName, this.getClass().getName(), methodName);

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the bean. This is used for beans that
     * contain a combination of the properties from an entity and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param relationship relationship used to access the entity
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        Relationship relationship,
                        String       methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewBean(entity, relationship)";

        handleUnimplementedConverterMethod(beanClass.getName(), thisMethodName, this.getClass().getName(), methodName);

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * an Annotation or DataField bean which combine knowledge from the entity and its linked relationships.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewComplexBean(Class<B>           beanClass,
                               EntityDetail       primaryEntity,
                               List<Relationship> relationships,
                               String             methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewComplexBean";

        handleUnimplementedConverterMethod(beanClass.getName(), thisMethodName, this.getClass().getName(), methodName);

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * an Annotation or DataField bean which combine knowledge from the entity and its linked relationships.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param relationship relationship used to access the entity
     * @param relatedEntities relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewComplexBean(Class<B>            beanClass,
                               EntityDetail        primaryEntity,
                               Relationship        relationship,
                               List<RelatedEntity> relatedEntities,
                               String              methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewComplexBean";

        handleUnimplementedConverterMethod(beanClass.getName(), thisMethodName, this.getClass().getName(), methodName);

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * a connection bean which made up of 3 entities (Connection, ConnectorType and Endpoint) plus the
     * relationships between them.  The relationships may be omitted if they do not have any properties.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the collection of entities that make up the content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewComplexBean(Class<B>           beanClass,
                               EntityDetail       primaryEntity,
                               List<EntityDetail> supplementaryEntities,
                               List<Relationship> relationships,
                               String             methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewComplexBean(with supplementary entities)";

        handleUnimplementedConverterMethod(beanClass.getName(), thisMethodName, this.getClass().getName(), methodName);

        return null;
    }


    /**
     * Using the supplied relationship, return a new instance of the bean.  It is used for beans that
     * represent a simple relationship between two entities.
     *
     * @param beanClass name of the class to create
     * @param relationship relationship linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewRelationshipBean(Class<B>     beanClass,
                                    Relationship relationship,
                                    String       methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewRelationshipBean";

        handleUnimplementedConverterMethod(beanClass.getName(), thisMethodName, this.getClass().getName(), methodName);

        return null;
    }


    /**
     * Return the converted bean.  This is a special method used for schema types since they are stored
     * as a collection of instances.
     *
     * @param beanClass name of the class to create
     * @param schemaRootHeader header of the schema element that holds the root information
     * @param schemaTypeTypeName name of type of the schema type to create
     * @param instanceProperties properties describing the schema type
     * @param schemaRootClassifications classifications from the schema root entity
     * @param attributeCount number of attributes (for a complex schema type)
     * @param validValueSetGUID unique identifier of the set of valid values (for an enum schema type)
     * @param externalSchemaType unique identifier for the properties of the schema type that is shared by multiple attributes/assets
     * @param mapFromSchemaType bean containing the properties of the schema type that is part of a map definition
     * @param mapToSchemaType bean containing the properties of the schema type that is part of a map definition
     * @param schemaTypeOptions list of schema types that could be the type for this attribute
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewSchemaTypeBean(Class<B>             beanClass,
                                  InstanceHeader       schemaRootHeader,
                                  String               schemaTypeTypeName,
                                  InstanceProperties   instanceProperties,
                                  List<Classification> schemaRootClassifications,
                                  int                  attributeCount,
                                  String               validValueSetGUID,
                                  B                    externalSchemaType,
                                  B                    mapFromSchemaType,
                                  B                    mapToSchemaType,
                                  List<B>              schemaTypeOptions,
                                  String               methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewSchemaTypeBean";

        handleUnimplementedConverterMethod(beanClass.getName(), thisMethodName, this.getClass().getName(), methodName);

        return null;
    }


    /**
     * Return the converted bean.  This is a special method used for schema types since they are stored
     * as a collection of instances.  For external schema types and map elements, both the GUID and the bean are returned to
     * allow the consuming OMAS to choose whether it is returning GUIDs of the linked to schema or the schema type bean itself.
     *
     * @param beanClass name of the class to create
     * @param schemaRootHeader header of the schema element that holds the root information
     * @param schemaTypeTypeName name of type of the schema type to create
     * @param instanceProperties properties describing the schema type
     * @param schemaRootClassifications classifications from the schema root entity
     * @param attributeCount number of attributes (for a complex schema type)
     * @param validValueSetGUID unique identifier of the set of valid values (for an enum schema type)
     * @param externalSchemaTypeGUID unique identifier of the external schema type
     * @param externalSchemaType unique identifier for the properties of the schema type that is shared by multiple attributes/assets
     * @param mapFromSchemaTypeGUID unique identifier of the mapFrom schema type
     * @param mapFromSchemaType bean containing the properties of the schema type that is part of a map definition
     * @param mapToSchemaTypeGUID unique identifier of the mapTo schema type
     * @param mapToSchemaType bean containing the properties of the schema type that is part of a map definition
     * @param schemaTypeOptionGUIDs list of unique identifiers for schema types that could be the type for this attribute
     * @param schemaTypeOptions list of schema types that could be the type for this attribute
     * @param queryTargets list of relationships to schema types that contain data values used to derive the schema type value(s)
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewSchemaTypeBean(Class<B>             beanClass,
                                  InstanceHeader       schemaRootHeader,
                                  String               schemaTypeTypeName,
                                  InstanceProperties   instanceProperties,
                                  List<Classification> schemaRootClassifications,
                                  int                  attributeCount,
                                  String               validValueSetGUID,
                                  String               externalSchemaTypeGUID,
                                  B                    externalSchemaType,
                                  String               mapFromSchemaTypeGUID,
                                  B                    mapFromSchemaType,
                                  String               mapToSchemaTypeGUID,
                                  B                    mapToSchemaType,
                                  List<String>         schemaTypeOptionGUIDs,
                                  List<B>              schemaTypeOptions,
                                  List<Relationship>   queryTargets,
                                  String               methodName) throws PropertyServerException
    {
        return this.getNewSchemaTypeBean(beanClass,
                                         schemaRootHeader,
                                         schemaTypeTypeName,
                                         instanceProperties,
                                         schemaRootClassifications,
                                         attributeCount,
                                         validValueSetGUID,
                                         externalSchemaType,
                                         mapFromSchemaType,
                                         mapToSchemaType,
                                         schemaTypeOptions,
                                         methodName);
    }


    /**
     * Extract the properties from the schema attribute entity.  Each API creates a specialization of this method for its beans.
     *
     * @param beanClass name of the class to create
     * @param schemaAttributeEntity entity containing the properties for the main schema attribute
     * @param typeClass name of type used to describe the schema type
     * @param schemaType bean containing the properties of the schema type - this is filled out by the schema type converter
     * @param schemaAttributeRelationships relationships containing the links to other schema attributes
     * @param methodName calling method
     * @param <T> bean type used to create the schema type
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public <T> B getNewSchemaAttributeBean(Class<B>           beanClass,
                                           EntityDetail       schemaAttributeEntity,
                                           Class<T>           typeClass,
                                           T                  schemaType,
                                           List<Relationship> schemaAttributeRelationships,
                                           String             methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewSchemaAttributeBean)";

        handleUnimplementedConverterMethod(beanClass.getName(), thisMethodName, this.getClass().getName(), methodName);

        return null;
    }


    /* ==========================================================
     * This method throws the exception that occurs if an OMAS fails to implement one of the updateXXXBean methods or
     * the converter is configured with an invalid bean class.
     */

    /**
     * Throw an exception to indicate that one of the update methods has not been implemented by an OMAS.
     *
     * @param beanClassName class name of bean
     * @param missingMethodName method tha has not been implemented
     * @param converterClassName class that detected the missing method
     * @param methodName method that is missing
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been called with a method that is unexpected for the specific type of
     * bean that this converter is implemented for.
     */
    private void handleUnimplementedConverterMethod(String beanClassName,
                                                    String missingMethodName,
                                                    String converterClassName,
                                                    String methodName) throws PropertyServerException
    {
        throw new PropertyServerException(GenericHandlersErrorCode.MISSING_CONVERTER_METHOD.getMessageDefinition(serviceName,
                                                                                                                 missingMethodName,
                                                                                                                 converterClassName,
                                                                                                                 beanClassName,
                                                                                                                 methodName),
                                          this.getClass().getName(),
                                          methodName);
    }


    /**
     * Throw an exception to indicate that one of the update methods has not been implemented by an OMAS.
     *
     * @param beanClassName class name of bean
     * @param error exception generated when the new bean is created
     * @param methodName method that is missing
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is not a known class
     */
    protected void handleInvalidBeanClass(String    beanClassName,
                                          Exception error,
                                          String    methodName) throws PropertyServerException
    {
        throw new PropertyServerException(GenericHandlersErrorCode.INVALID_BEAN_CLASS.getMessageDefinition(beanClassName,
                                                                                                           methodName,
                                                                                                           serviceName,
                                                                                                           serverName,
                                                                                                           error.getMessage()),
                                          this.getClass().getName(),
                                          methodName,
                                          error);
    }


    /**
     * Throw an exception to indicate that one of the update methods has not been implemented by an OMAS.
     *
     * @param beanClassName class name of bean
     * @param expectedBeanClass class name that the converter is able to process
     * @param methodName method that is missing
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    protected void handleUnexpectedBeanClass(String    beanClassName,
                                             String    expectedBeanClass,
                                             String    methodName) throws PropertyServerException
    {
        throw new PropertyServerException(GenericHandlersErrorCode.UNEXPECTED_BEAN_CLASS.getMessageDefinition(beanClassName,
                                                                                                           methodName,
                                                                                                           serviceName,
                                                                                                           serverName,
                                                                                                           expectedBeanClass),
                                          this.getClass().getName(),
                                          methodName);
    }


    /**
     * Throw an exception to indicate that a critical instance (typically the main entity) has not been passed
     * to the converter.
     *
     * @param beanClassName class name of bean
     * @param typeDefCategory class name that the converter is able to process
     * @param methodName method that is missing
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    protected void handleMissingMetadataInstance(String          beanClassName,
                                                 TypeDefCategory typeDefCategory,
                                                 String          methodName) throws PropertyServerException
    {
        throw new PropertyServerException(GenericHandlersErrorCode.MISSING_METADATA_INSTANCE.getMessageDefinition(serviceName,
                                                                                                                  beanClassName,
                                                                                                                  typeDefCategory.getName(),
                                                                                                                  methodName),
                                          this.getClass().getName(),
                                          methodName);
    }


    /**
     * Throw an exception to indicate that a critical instance (typically the main entity) has not been passed
     * to the converter.
     *
     * @param expectedTypeName name of the type that the instance should match
     * @param beanClassName class name of bean
     * @param instanceAuditHeader header section of the instance
     * @param methodName calling method
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    void validateInstanceType(String              expectedTypeName,
                              String              beanClassName,
                              InstanceAuditHeader instanceAuditHeader,
                              String              methodName) throws PropertyServerException
    {
        String actualTypeName = "<null>";

        if ((instanceAuditHeader.getType() != null) && (instanceAuditHeader.getType().getTypeDefName() != null))
        {
            actualTypeName = instanceAuditHeader.getType().getTypeDefName();
        }

        validateInstanceType(expectedTypeName,
                             beanClassName,
                             actualTypeName,
                             methodName);
    }


    /**
     * Throw an exception to indicate that a critical instance (typically the main entity) has not been passed
     * to the converter.
     *
     * @param expectedTypeName name of the type that the instance should match
     * @param beanClassName class name of bean
     * @param actualTypeName type of instance
     * @param methodName calling method
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    protected void validateInstanceType(String expectedTypeName,
                                        String beanClassName,
                                        String actualTypeName,
                                        String methodName) throws PropertyServerException
    {

        if (repositoryHelper.isTypeOf(serviceName, actualTypeName, expectedTypeName))
        {
                return;
        }

        throw new PropertyServerException(GenericHandlersErrorCode.BAD_INSTANCE_TYPE.getMessageDefinition(serviceName,
                                                                                                          beanClassName,
                                                                                                          actualTypeName,
                                                                                                          methodName,
                                                                                                          expectedTypeName),
                                          this.getClass().getName(),
                                          methodName);
    }


    /**
     * Throw an exception to indicate that a retrieved entity has missing information.
     *
     * @param beanClassName class name of bean
     * @param entity the entity with the bad header
     * @param methodName calling method
     * @throws PropertyServerException an invalid instance has been returned from the metadata repositories
     */
    protected void handleBadEntity(String          beanClassName,
                                   EntityDetail    entity,
                                   String          methodName) throws PropertyServerException
    {
        if (entity == null)
        {
            handleMissingMetadataInstance(beanClassName, TypeDefCategory.ENTITY_DEF, methodName);
        }
        else
        {
            throw new PropertyServerException(GenericHandlersErrorCode.BAD_ENTITY.getMessageDefinition(methodName,
                                                                                                       serviceName,
                                                                                                       entity.toString()),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Throw an exception to indicate that a critical instance (typically the main entity) has not been passed
     * to the converter.
     *
     * @param beanClassName class name of bean
     * @param relationship the relationship with the bad header
     * @param methodName calling method
     * @throws PropertyServerException an invalid instance has been returned from the metadata repositories
     */
    protected void handleBadRelationship(String       beanClassName,
                                         Relationship relationship,
                                         String       methodName) throws PropertyServerException
    {
        if (relationship == null)
        {
            handleMissingMetadataInstance(beanClassName, TypeDefCategory.RELATIONSHIP_DEF, methodName);
        }
        else
        {
            throw new PropertyServerException(GenericHandlersErrorCode.BAD_RELATIONSHIP.getMessageDefinition(methodName,
                                                                                                             serviceName,
                                                                                                             relationship.toString()),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /* ======================================================
     * The methods that follow are used by the subclasses to extract specific properties from the instance properties.
     * They are used for all properties except enums which need a specific method in the OMAS converters.
     */

    /**
     * Extract the properties for the requested classification from the entity.
     *
     * @param classificationName name of classification
     * @param entity entity containing classification
     * @return list of properties for the named classification
     */
    protected InstanceProperties getClassificationProperties(String       classificationName,
                                                             EntityDetail entity)
    {
        if (entity != null)
        {
            List<Classification> entityClassifications = entity.getClassifications();

            if (entityClassifications != null)
            {
                return getClassificationProperties(classificationName, entityClassifications);
            }
        }

        return null;
    }


    /**
     * Extract the properties for the requested classification from the list of classifications.
     *
     * @param classificationName name of classification
     * @param entityClassifications list of classifications from an entity
     * @return list of properties for the named classification
     */
    protected InstanceProperties getClassificationProperties(String               classificationName,
                                                             List<Classification> entityClassifications)
    {
        if (entityClassifications != null)
        {
            for (Classification entityClassification : entityClassifications)
            {
                if (entityClassification != null)
                {
                    if (classificationName.equals(entityClassification.getName()))
                    {
                        return entityClassification.getProperties();
                    }
                }
            }
        }

        return null;
    }


    /**
     * Extract the properties from the entity.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param methodName calling method
     * @return filled out element header
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    protected ElementHeader getMetadataElementHeader(Class<B>     beanClass,
                                                     EntityDetail entity,
                                                     String       methodName) throws PropertyServerException
    {
        if (entity != null)
        {
            return getMetadataElementHeader(beanClass,
                                            entity,
                                            entity.getClassifications(),
                                            methodName);
        }
        else
        {
            this.handleMissingMetadataInstance(beanClass.getName(),
                                                TypeDefCategory.ENTITY_DEF,
                                                methodName);
        }

        return null;
    }


    /**
     * Extract the properties from the entity.
     *
     * @param beanClass name of the class to create
     * @param header header from the entity containing the properties
     * @param entityClassifications classification if this is an entity
     * @param methodName calling method
     * @return filled out element header
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    public ElementHeader getMetadataElementHeader(Class<B>             beanClass,
                                                  InstanceHeader       header,
                                                  List<Classification> entityClassifications,
                                                  String               methodName) throws PropertyServerException
    {
        if (header != null)
        {
            ElementHeader elementHeader = new ElementHeader();

            elementHeader.setGUID(header.getGUID());
            elementHeader.setStatus(this.getElementStatus(header.getStatus()));
            elementHeader.setClassifications(this.getElementClassifications(entityClassifications));
            elementHeader.setType(this.getElementType(header));

            ElementOrigin elementOrigin = new ElementOrigin();

            elementOrigin.setSourceServer(serverName);
            elementOrigin.setOriginCategory(this.getElementOriginCategory(header.getInstanceProvenanceType()));
            elementOrigin.setHomeMetadataCollectionId(header.getMetadataCollectionId());
            elementOrigin.setHomeMetadataCollectionName(header.getMetadataCollectionName());
            elementOrigin.setLicense(header.getInstanceLicense());

            elementHeader.setOrigin(elementOrigin);

            elementHeader.setVersions(this.getElementVersions(header));

            return elementHeader;
        }
        else
        {
            this.handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
        }

        return null;
    }


    /**
     * Translate the repository services' InstanceStatus to an ElementStatus.
     *
     * @param instanceStatus value from the repository services
     * @return ElementStatus enum
     */
    protected ElementStatus getElementStatus(InstanceStatus instanceStatus)
    {
        if (instanceStatus != null)
        {
            switch (instanceStatus)
            {
                case UNKNOWN ->
                {
                    return ElementStatus.UNKNOWN;
                }
                case DRAFT ->
                {
                    return ElementStatus.DRAFT;
                }
                case PREPARED ->
                {
                    return ElementStatus.PREPARED;
                }
                case PROPOSED ->
                {
                    return ElementStatus.PROPOSED;
                }
                case APPROVED ->
                {
                    return ElementStatus.APPROVED;
                }
                case REJECTED ->
                {
                    return ElementStatus.REJECTED;
                }
                case APPROVED_CONCEPT ->
                {
                    return ElementStatus.APPROVED_CONCEPT;
                }
                case UNDER_DEVELOPMENT ->
                {
                    return ElementStatus.UNDER_DEVELOPMENT;
                }
                case DEVELOPMENT_COMPLETE ->
                {
                    return ElementStatus.DEVELOPMENT_COMPLETE;
                }
                case APPROVED_FOR_DEPLOYMENT ->
                {
                    return ElementStatus.APPROVED_FOR_DEPLOYMENT;
                }
                case STANDBY ->
                {
                    return ElementStatus.STANDBY;
                }
                case ACTIVE ->
                {
                    return ElementStatus.ACTIVE;
                }
                case FAILED ->
                {
                    return ElementStatus.FAILED;
                }
                case DISABLED ->
                {
                    return ElementStatus.DISABLED;
                }
                case COMPLETE ->
                {
                    return ElementStatus.COMPLETE;
                }
                case DEPRECATED ->
                {
                    return ElementStatus.DEPRECATED;
                }
                case DELETED ->
                {
                    return ElementStatus.DELETED;
                }
                case OTHER ->
                {
                    return ElementStatus.OTHER;
                }
            }
        }

        return ElementStatus.UNKNOWN;
    }


    /**
     * Extract the properties from the entity.
     *
     * @param beanClass name of the class to create
     * @param entityProxy entityProxy from the relationship containing the properties
     * @param methodName calling method
     * @return filled out element header
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    public ElementStub getElementStub(Class<B>    beanClass,
                                      EntityProxy entityProxy,
                                      String      methodName) throws PropertyServerException
    {
        if (entityProxy != null)
        {
            ElementHeader elementHeader = getMetadataElementHeader(beanClass, entityProxy, entityProxy.getClassifications(), methodName);
            ElementStub   elementStub   = new ElementStub(elementHeader);

            elementStub.setUniqueName(repositoryHelper.getStringProperty(serviceName,
                                                                         OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                         entityProxy.getUniqueProperties(),
                                                                         methodName));

            return elementStub;
        }
        else
        {
            this.handleMissingMetadataInstance(beanClass.getName(),
                                               TypeDefCategory.ENTITY_DEF,
                                               methodName);
        }

        return null;
    }



    /**
     * Extract the properties from the entity.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param methodName calling method
     * @return filled out element header
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    public ElementStub getElementStub(Class<B>     beanClass,
                                      EntityDetail entity,
                                      String       methodName) throws PropertyServerException
    {
        if (entity != null)
        {
            ElementHeader elementHeader = getMetadataElementHeader(beanClass, entity, methodName);
            ElementStub   elementStub   = new ElementStub(elementHeader);

            elementStub.setUniqueName(repositoryHelper.getStringProperty(serviceName,
                                                                         OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                         entity.getProperties(),
                                                                         methodName));

            return elementStub;
        }
        else
        {
            this.handleMissingMetadataInstance(beanClass.getName(),
                                               TypeDefCategory.ENTITY_DEF,
                                               methodName);
        }

        return null;
    }



    /**
     * Extract the properties from the relationship.
     *
     * @param beanClass name of the class to create
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return filled out element header
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    public ElementStub getElementStub(Class<B>     beanClass,
                                      Relationship relationship,
                                      String       methodName) throws PropertyServerException
    {
        if (relationship != null)
        {
            ElementHeader elementHeader = getMetadataElementHeader(beanClass, relationship, null, methodName);

            return new ElementStub(elementHeader);
        }
        else
        {
            this.handleMissingMetadataInstance(beanClass.getName(),
                                               TypeDefCategory.RELATIONSHIP_DEF,
                                               methodName);
        }

        return null;
    }


    /**
     * Extract the classifications from the entity.
     *
     * @param entity entity containing the classifications
     * @return list of bean classifications
     */
    List<ElementClassification> getEntityClassifications(EntityDetail entity)
    {
        if (entity != null)
        {
            return this.getElementClassifications(entity.getClassifications());
        }

        return null;
    }


    /**
     * Extract the classifications from the entity.
     *
     * @param entityClassifications classifications direct from the entity
     * @return list of bean classifications
     */
    protected List<ElementClassification> getElementClassifications(List<Classification> entityClassifications)
    {
        List<ElementClassification> beanClassifications = null;

        if (entityClassifications != null)
        {
            beanClassifications = new ArrayList<>();

            for (Classification entityClassification : entityClassifications)
            {
                if (entityClassification != null)
                {
                    ElementClassification beanClassification = new ElementClassification();

                    beanClassification.setStatus(this.getElementStatus(entityClassification.getStatus()));
                    beanClassification.setType(this.getElementType(entityClassification));

                    ElementOrigin elementOrigin = new ElementOrigin();

                    elementOrigin.setSourceServer(serverName);
                    elementOrigin.setOriginCategory(this.getElementOriginCategory(entityClassification.getInstanceProvenanceType()));
                    elementOrigin.setHomeMetadataCollectionId(entityClassification.getMetadataCollectionId());
                    elementOrigin.setHomeMetadataCollectionName(entityClassification.getMetadataCollectionName());
                    elementOrigin.setLicense(entityClassification.getInstanceLicense());

                    beanClassification.setOrigin(elementOrigin);

                    beanClassification.setVersions(this.getElementVersions(entityClassification));

                    beanClassification.setClassificationName(entityClassification.getName());
                    beanClassification.setClassificationProperties(repositoryHelper.getInstancePropertiesAsMap(entityClassification.getProperties()));

                    beanClassifications.add(beanClassification);
                }
            }

        }

        return beanClassifications;
    }


    /**
     * Convert information from a repository instance into an Open Metadata Framework ElementType.
     *
     * @param instanceHeader values from the server
     * @return OCF ElementType object
     */
    public ElementType getElementType(InstanceAuditHeader instanceHeader)
    {
        ElementType elementType = new ElementType();

        InstanceType instanceType = instanceHeader.getType();

        if (instanceType != null)
        {
            String typeDefName = instanceType.getTypeDefName();
            TypeDef typeDef = repositoryHelper.getTypeDefByName(serviceName, typeDefName);

            elementType.setTypeId(instanceType.getTypeDefGUID());
            elementType.setTypeName(typeDefName);
            elementType.setTypeVersion(instanceType.getTypeDefVersion());
            elementType.setTypeDescription(typeDef.getDescription());

            List<TypeDefLink> typeDefSuperTypes = repositoryHelper.getSuperTypes(serviceName, typeDefName);

            if ((typeDefSuperTypes != null) && (! typeDefSuperTypes.isEmpty()))
            {
                List<String>   superTypes = new ArrayList<>();

                for (TypeDefLink typeDefLink : typeDefSuperTypes)
                {
                    if (typeDefLink != null)
                    {
                        superTypes.add(typeDefLink.getName());
                    }
                }

                if (! superTypes.isEmpty())
                {
                    elementType.setSuperTypeNames(superTypes);
                }
            }
        }

        return elementType;
    }


    /**
     * Extract detail of the version of the element and the user's maintaining it.
     *
     * @param header audit header from the repository
     * @return ElementVersions object
     */
    protected ElementVersions getElementVersions(InstanceAuditHeader header)
    {
        ElementVersions elementVersions = new ElementVersions();

        elementVersions.setCreatedBy(header.getCreatedBy());
        elementVersions.setCreateTime(header.getCreateTime());
        elementVersions.setUpdatedBy(header.getUpdatedBy());
        elementVersions.setUpdateTime(header.getUpdateTime());
        elementVersions.setMaintainedBy(header.getMaintainedBy());
        elementVersions.setVersion(header.getVersion());

        return elementVersions;
    }


    /**
     * Translate the repository services' InstanceProvenanceType to an ElementOrigin.
     *
     * @param instanceProvenanceType value from the repository services
     * @return ElementOrigin enum
     */
    protected ElementOriginCategory getElementOriginCategory(InstanceProvenanceType instanceProvenanceType)
    {
        if (instanceProvenanceType != null)
        {
            return switch (instanceProvenanceType)
                           {
                               case DEREGISTERED_REPOSITORY -> ElementOriginCategory.DEREGISTERED_REPOSITORY;
                               case EXTERNAL_SOURCE -> ElementOriginCategory.EXTERNAL_SOURCE;
                               case EXPORT_ARCHIVE -> ElementOriginCategory.EXPORT_ARCHIVE;
                               case LOCAL_COHORT -> ElementOriginCategory.LOCAL_COHORT;
                               case CONTENT_PACK -> ElementOriginCategory.CONTENT_PACK;
                               case CONFIGURATION -> ElementOriginCategory.CONFIGURATION;
                               case UNKNOWN -> ElementOriginCategory.UNKNOWN;
                           };
        }

        return ElementOriginCategory.UNKNOWN;
    }



    /**
     * Extract and delete the sortOrder property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return DataItemSortOrder enum
     */
    public DataItemSortOrder removeSortOrder(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeSortOrder";

        if (instanceProperties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataProperty.SORT_ORDER.name,
                                                                     instanceProperties,
                                                                     methodName);

            for (DataItemSortOrder dataItemSortOrder : DataItemSortOrder.values())
            {
                if (dataItemSortOrder.getOrdinal() == ordinal)
                {
                    return dataItemSortOrder;
                }
            }
        }

        return null;
    }


    /**
     * Extract and delete the KeyPattern property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return KeyPattern enum
     */
    public KeyPattern removeKeyPattern(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeKeyPattern";

        if (instanceProperties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataProperty.KEY_PATTERN.name,
                                                                     instanceProperties,
                                                                     methodName);

            for (KeyPattern keyPattern : KeyPattern.values())
            {
                if (keyPattern.getOrdinal() == ordinal)
                {
                    return keyPattern;
                }
            }
        }

        return null;
    }


    /**
     * Set up the properties that can be extracted form the schema type.
     *
     * @param schemaAttributeEntity entity to unpack
     * @param schemaTypeElement schema type properties
     * @param properties output column properties
     */
    public void setUpSchemaAttribute(EntityDetail              schemaAttributeEntity,
                                     SchemaTypeElement         schemaTypeElement,
                                     SchemaAttributeProperties properties)
    {
        /*
         * The initial set of values come from the entity.
         */
        InstanceProperties instanceProperties = new InstanceProperties(schemaAttributeEntity.getProperties());

        properties.setQualifiedName(this.removeQualifiedName(instanceProperties));
        properties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
        properties.setDisplayName(this.removeDisplayName(instanceProperties));
        properties.setDescription(this.removeDescription(instanceProperties));

        properties.setElementPosition(this.removePosition(instanceProperties));
        properties.setMinCardinality(this.removeMinCardinality(instanceProperties));
        properties.setMaxCardinality(this.removeMaxCardinality(instanceProperties));
        properties.setAllowsDuplicateValues(this.removeAllowsDuplicateValues(instanceProperties));
        properties.setOrderedValues(this.removeOrderedValues(instanceProperties));
        properties.setDefaultValueOverride(this.removeDefaultValueOverride(instanceProperties));
        properties.setSortOrder(this.removeSortOrder(instanceProperties));
        properties.setMinimumLength(this.removeMinimumLength(instanceProperties));
        properties.setLength(this.removeLength(instanceProperties));
        properties.setPrecision(this.removePrecision(instanceProperties));
        properties.setIsNullable(this.removeIsNullable(instanceProperties));
        properties.setNativeJavaClass(this.removeNativeClass(instanceProperties));
        properties.setAliases(this.removeAliases(instanceProperties));

        /*
         * Any remaining properties are returned in the extended properties.  They are assumed to be defined in a subtype.
         */
        properties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

        if (schemaTypeElement != null)
        {
            this.addSchemaTypeToAttribute(schemaTypeElement, properties);
        }
    }


    /**
     * Set up the properties that can be extracted form the schema type. There are two strategies to try.
     * The Schema Type Converter in the OCF creates a bean of a specific type to reflect the type of schema.
     * The Schema Type Converter in the generic handler always creates a bean of type SchemaTypeProperties with the
     * subtype's properties in extendedProperties.
     *
     * @param schemaTypeElement schema type properties
     * @param attributeProperties output column properties
     */
    public void addSchemaTypeToAttribute(SchemaTypeElement         schemaTypeElement,
                                         SchemaAttributeProperties attributeProperties)
    {
        SchemaTypeProperties schemaTypeProperties = schemaTypeElement.getSchemaTypeProperties();

        if (schemaTypeProperties instanceof PrimitiveSchemaTypeProperties)
        {
            attributeProperties.setDataType(((PrimitiveSchemaTypeProperties) schemaTypeProperties).getDataType());
            attributeProperties.setDefaultValue(((PrimitiveSchemaTypeProperties) schemaTypeProperties).getDefaultValue());
        }
        else if (schemaTypeProperties instanceof LiteralSchemaTypeProperties)
        {
            attributeProperties.setDataType(((LiteralSchemaTypeProperties) schemaTypeProperties).getDataType());
            attributeProperties.setFixedValue(((LiteralSchemaTypeProperties) schemaTypeProperties).getFixedValue());
        }
        else if (schemaTypeProperties instanceof EnumSchemaTypeProperties)
        {
            attributeProperties.setDataType(((EnumSchemaTypeProperties) schemaTypeProperties).getDataType());
            attributeProperties.setDefaultValue(((EnumSchemaTypeProperties) schemaTypeProperties).getDefaultValue());
            attributeProperties.setValidValuesSetGUID(((EnumSchemaTypeProperties) schemaTypeProperties).getValidValueSetGUID());
        }
        else if (schemaTypeProperties instanceof ExternalSchemaTypeProperties)
        {
            SchemaTypeElement externalSchemaType = schemaTypeElement.getExternalSchemaType();
            attributeProperties.setExternalTypeGUID(externalSchemaType.getElementHeader().getGUID());
        }
        else
        {
            /*
             * The schema type is unspecialized, so just pull any additional properties out of extended properties.
             */
            if (schemaTypeProperties.getExtendedProperties() != null)
            {
                Map<String, Object> extendedProperties = schemaTypeProperties.getExtendedProperties();

                Object propertyObject = extendedProperties.get(OpenMetadataProperty.DATA_TYPE.name);
                if (propertyObject != null)
                {
                    attributeProperties.setDataType(propertyObject.toString());
                }

                propertyObject = extendedProperties.get(OpenMetadataProperty.FIXED_VALUE.name);
                if (propertyObject != null)
                {
                    attributeProperties.setFixedValue(propertyObject.toString());
                }

                propertyObject = extendedProperties.get(OpenMetadataProperty.DEFAULT_VALUE.name);
                if (propertyObject != null)
                {
                    attributeProperties.setDefaultValue(propertyObject.toString());
                }
            }
        }

        attributeProperties.setSchemaType(schemaTypeProperties);
    }


    /**
     * Using the supplied instances, return a new instance of a relatedElement bean. This is used for beans that
     * contain a combination of the properties from an entity and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public RelatedElementStub getRelatedElement(Class<B>     beanClass,
                                                EntityDetail entity,
                                                Relationship relationship,
                                                String       methodName) throws PropertyServerException
    {
        RelatedElementStub relatedElement = new RelatedElementStub();

        relatedElement.setRelationshipHeader(this.getMetadataElementHeader(beanClass, relationship, null, methodName));

        if (relationship != null)
        {
            InstanceProperties instanceProperties = new InstanceProperties(relationship.getProperties());

            RelationshipProperties relationshipProperties = new RelationshipProperties();

            relationshipProperties.setEffectiveFrom(instanceProperties.getEffectiveFromTime());
            relationshipProperties.setEffectiveTo(instanceProperties.getEffectiveToTime());
            relationshipProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

            relatedElement.setRelationshipProperties(relationshipProperties);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.RELATIONSHIP_DEF, methodName);
        }


        if (entity != null)
        {
            ElementStub elementStub = this.getElementStub(beanClass, entity, methodName);

            relatedElement.setRelatedElement(elementStub);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
        }

        return relatedElement;
    }




    /**
     * Using the supplied instances, return a new instance of a relatedElement bean. This is used for beans that
     * contain a combination of the properties from an entityProxy and that of a connected relationship.
     *
     * @param beanClass    name of the class to create
     * @param entityProxy  entityProxy containing the properties
     * @param relationship relationship containing the properties
     * @param methodName   calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public RelatedElementStub getRelatedElement(Class<B> beanClass,
                                                Relationship relationship,
                                                EntityProxy entityProxy,
                                                String methodName) throws PropertyServerException
    {
        RelatedElementStub relatedElementStub = new RelatedElementStub();

        relatedElementStub.setRelationshipHeader(this.getMetadataElementHeader(beanClass, relationship, null, methodName));

        if (relationship != null)
        {
            InstanceProperties instanceProperties = new InstanceProperties(relationship.getProperties());

            RelationshipProperties relationshipProperties = new RelationshipProperties();

            relationshipProperties.setEffectiveFrom(instanceProperties.getEffectiveFromTime());
            relationshipProperties.setEffectiveTo(instanceProperties.getEffectiveToTime());
            relationshipProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

            relatedElementStub.setRelationshipProperties(relationshipProperties);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.RELATIONSHIP_DEF, methodName);
        }


        if (entityProxy != null)
        {
            ElementStub elementStub = this.getElementStub(beanClass, entityProxy, methodName);

            relatedElementStub.setRelatedElement(elementStub);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
        }

        return relatedElementStub;
    }


    /**
     * Using the supplied instances, return a new instance of a relatedElement bean. This is used for beans that
     * contain a combination of the properties from an entity and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public RelatedBy getRelatedBy(Class<B>     beanClass,
                                  Relationship relationship,
                                  String       methodName) throws PropertyServerException
    {
        RelatedBy relatedBy = new RelatedBy();

        relatedBy.setRelationshipHeader(this.getMetadataElementHeader(beanClass, relationship, null, methodName));

        if (relationship != null)
        {
            InstanceProperties instanceProperties = new InstanceProperties(relationship.getProperties());

            RelationshipProperties relationshipProperties = new RelationshipProperties();

            relationshipProperties.setEffectiveFrom(instanceProperties.getEffectiveFromTime());
            relationshipProperties.setEffectiveTo(instanceProperties.getEffectiveToTime());
            relationshipProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

            relatedBy.setRelationshipProperties(relationshipProperties);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.RELATIONSHIP_DEF, methodName);
        }

        return relatedBy;
    }


    /**
     * Extract the qualifiedName property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String getQualifiedName(InstanceProperties  instanceProperties)
    {
        final String methodName = "getQualifiedName";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.QUALIFIED_NAME.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }



    /**
     * Extract and delete the qualifiedName property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String removeQualifiedName(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeQualifiedName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.QUALIFIED_NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the qualifiedName property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return map or null
     */
    protected Map<String, String> removeAdditionalProperties(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeAdditionalProperties";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                instanceProperties,
                                                                methodName);
        }

        return null;
    }


    /**
     * Convert the remaining properties into a map that is returned as the extended properties.
     *
     * @param instanceProperties properties from entity
     * @return map or null
     */
    protected Map<String, Object> getRemainingExtendedProperties(InstanceProperties  instanceProperties)
    {
        if (instanceProperties != null)
        {
            Map<String, Object> extendedProperties = repositoryHelper.getInstancePropertiesAsMap(instanceProperties);

            if ((extendedProperties != null) && (! extendedProperties.isEmpty()))
            {
                return extendedProperties;
            }
        }

        return null;
    }


    /**
     * Convert the remaining properties into a map that is returned as the extended properties.
     *
     * @param instanceProperties properties from entity
     * @return map or null
     */
    protected Map<String, String> getPropertiesAsStrings(InstanceProperties  instanceProperties)
    {
        if (instanceProperties != null)
        {
            Map<String, InstancePropertyValue> propertyMap = instanceProperties.getInstanceProperties();

            if ((propertyMap != null) && (! propertyMap.isEmpty()))
            {
                Map<String, String> stringPropertyMap = new HashMap<>();

                for (String propertyName : propertyMap.keySet())
                {
                    InstancePropertyValue instancePropertyValue = propertyMap.get(propertyName);

                    if (instancePropertyValue != null)
                    {
                        stringPropertyMap.put(propertyName, instancePropertyValue.valueAsString());
                    }
                    else
                    {
                        stringPropertyMap.put(propertyName, null);
                    }
                }

                return stringPropertyMap;
            }
        }

        return null;
    }


    /**
     * Extract and delete the displayName property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String removeDisplayName(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeDisplayName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.DISPLAY_NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the displayName property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String getDisplayName(InstanceProperties  instanceProperties)
    {
        final String methodName = "getDisplayName";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.DISPLAY_NAME.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract and delete the name property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String removeName(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the name property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String removeResourceName(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.RESOURCE_NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the version identifier property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String removeVersionIdentifier(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeVersionIdentifier";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.VERSION_IDENTIFIER.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the description property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String getDescription(InstanceProperties  instanceProperties)
    {
        final String methodName = "getDescription";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.DESCRIPTION.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract and delete the description property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeDescription(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeDescription";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.DESCRIPTION.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the collectionType property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeCollectionType(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeCollectionType";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.COLLECTION_TYPE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the keyword property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeKeyword(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeKeyword";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.KEYWORD.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the topicType property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeTopicType(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeTopicType";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.TOPIC_TYPE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }

    /**
     * Extract and delete the topicName property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeTopicName(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeTopicName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.TOPIC_NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the contactType property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String removeContactType(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeContactType";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.CONTACT_TYPE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the contactMethodService property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String removeContactMethodService(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeContactMethodService";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.CONTACT_METHOD_SERVICE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the contactMethodValue property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String removeContactMethodValue(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeContactMethodValue";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.CONTACT_METHOD_VALUE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }



    /**
     * Extract and delete the mission property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeMission(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeMission";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.MISSION.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }




    /**
     * Extract and delete the associationType property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeAssociationType(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeAssociationType";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.ASSOCIATION_TYPE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the identifier property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeIdentifier(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeIdentifier";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.IDENTIFIER.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the externalInstanceTypeName property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeIdentifierTypeName(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeIdentifierTypeName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.EXT_INSTANCE_TYPE_NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the externalInstanceCreatedBy property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeExternalInstanceCreatedBy(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeExternalInstanceCreatedBy";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.EXT_INSTANCE_CREATED_BY.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the externalInstanceCreationTime property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected Date removeExternalInstanceCreationTime(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeExternalInstanceCreationTime";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeDateProperty(serviceName,
                                                       OpenMetadataProperty.EXT_INSTANCE_CREATION_TIME.name,
                                                       instanceProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the externalInstanceLastUpdatedBy property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeExternalInstanceLastUpdatedBy(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeExternalInstanceLastUpdatedBy";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.EXT_INSTANCE_LAST_UPDATED_BY.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the externalInstanceLastUpdateTime property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected Date removeExternalInstanceLastUpdateTime(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeExternalInstanceCreationTime";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeDateProperty(serviceName,
                                                       OpenMetadataProperty.EXT_INSTANCE_LAST_UPDATE_TIME.name,
                                                       instanceProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the externalInstanceVersion property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected long removeExternalInstanceVersion(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeExternalInstanceVersion";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeLongProperty(serviceName,
                                                       OpenMetadataProperty.EXT_INSTANCE_VERSION.name,
                                                       instanceProperties,
                                                       methodName);
        }

        return 0L;
    }

    /**
     * Extract and delete the URL property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeURL(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeURL";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.URL.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the organization property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeOwningOrganization(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeOwningOrganization";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.ORGANIZATION.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }



    /**
     * Extract and delete the referenceVersion property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeReferenceVersion(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeReferenceVersion";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.REFERENCE_VERSION.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the referenceId property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeReferenceId(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeReferenceId";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.REFERENCE_ID.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the referenceId property from the supplied instance properties.
     *
     * @param instanceProperties properties from relationship
     * @return string text or null
     */
    protected String getReferenceId(InstanceProperties  instanceProperties)
    {
        final String methodName = "getReferenceId";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.REFERENCE_ID.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract and delete the mappingProperties property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return map or null
     */
    protected Map<String, String> removeMappingProperties(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeMappingProperties";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                OpenMetadataProperty.MAPPING_PROPERTIES.name,
                                                                instanceProperties,
                                                                methodName);
        }

        return null;
    }



    /**
     * Extract and delete the lastSynchronized property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return map or null
     */
    protected Date removeLastSynchronized(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeLastSynchronized";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeDateProperty(serviceName,
                                                       OpenMetadataProperty.LAST_SYNCHRONIZED.name,
                                                       instanceProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the networkAddress property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeNetworkAddress(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeNetworkAddress";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.NETWORK_ADDRESS.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the protocol property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeProtocol(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeProtocol";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.PROTOCOL.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the encryption method property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeEncryptionMethod(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeEncryptionMethod";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.ENCRYPTION_METHOD.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the connector provider class name property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeConnectorProviderClassName(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeConnectorProviderClassName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.CONNECTOR_PROVIDER_CLASS_NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the supported asset type name property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeSupportedAssetTypeName(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeSupportedAssetTypeName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.SUPPORTED_ASSET_TYPE_NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the expected data format property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeExpectedDataFormat(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeExpectedDataFormat";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.EXPECTED_DATA_FORMAT.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the connector framework name property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeConnectorFrameworkName(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeConnectorFrameworkName";

        if (instanceProperties != null)
        {
            String connectorFrameworkName = repositoryHelper.removeStringProperty(serviceName,
                                                                                  OpenMetadataProperty.CONNECTOR_FRAMEWORK_NAME.name,
                                                                                  instanceProperties,
                                                                                  methodName);
            if (connectorFrameworkName != null)
            {
                return connectorFrameworkName;
            }
        }

        return OpenMetadataValidValues.CONNECTOR_FRAMEWORK_NAME_DEFAULT;
    }


    /**
     * Extract and delete the connector interface language property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeConnectorInterfaceLanguage(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeConnectorInterfaceLanguage";

        if (instanceProperties != null)
        {
            String connectorInterfaceLanguage = repositoryHelper.removeStringProperty(serviceName,
                                                                                      OpenMetadataProperty.CONNECTOR_INTERFACE_LANGUAGE.name,
                                                                                      instanceProperties,
                                                                                      methodName);
            if (connectorInterfaceLanguage != null)
            {
                return connectorInterfaceLanguage;
            }
        }

        return OpenMetadataValidValues.CONNECTOR_INTERFACE_LANGUAGE_DEFAULT;
    }


    /**
     * Extract and delete the connector interfaces property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string list or null
     */
    protected List<String> removeConnectorInterfaces(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeConnectorInterfaces";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataProperty.CONNECTOR_INTERFACES.name,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the target technology source property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeTargetTechnologySource(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeTargetTechnologySource";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.TARGET_TECHNOLOGY_SOURCE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the target technology name property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeTargetTechnologyName(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeTargetTechnologyName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.TARGET_TECHNOLOGY_NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the target technology interfaces property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string list or null
     */
    protected List<String> removeTargetTechnologyInterfaces(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeTargetTechnologyInterfaces";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataProperty.TARGET_TECHNOLOGY_INTERFACES.name,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the target technology versions property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string list or null
     */
    protected List<String> removeTargetTechnologyVersions(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeTargetTechnologyVersions";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataProperty.TARGET_TECHNOLOGY_VERSIONS.name,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the recognizedAdditionalProperties property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string list or null
     */
    protected List<String> removeRecognizedAdditionalProperties(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeRecognizedAdditionalProperties";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataProperty.RECOGNIZED_ADDITIONAL_PROPERTIES.name,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the recognizedSecuredProperties property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string list or null
     */
    protected List<String> removeRecognizedSecuredProperties(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeRecognizedSecuredProperties";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataProperty.RECOGNIZED_SECURED_PROPERTIES.name,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }



    /**
     * Extract and delete the recognized configuration properties property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string list or null
     */
    protected List<String> removeRecognizedConfigurationProperties(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeRecognizedConfigurationProperties";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataProperty.RECOGNIZED_CONFIGURATION_PROPERTIES.name,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the securedProperties property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string list or null
     */
    protected Map<String, String> removeSecuredProperties(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeSecuredProperties";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                OpenMetadataProperty.SECURED_PROPERTIES.name,
                                                                instanceProperties,
                                                                methodName);
        }

        return null;
    }


    /**
     * Extract and delete the  configuration properties property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string list or null
     */
    protected Map<String, Object> removeConfigurationProperties(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeConfigurationProperties";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeMapFromProperty(serviceName,
                                                          OpenMetadataProperty.CONFIGURATION_PROPERTIES.name,
                                                          instanceProperties,
                                                          methodName);
        }

        return null;
    }


    /**
     * Extract and delete the userId property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeUserId(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeUserId";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.USER_ID.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the clear password property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeClearPassword(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeClearPassword";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.CLEAR_PASSWORD.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the encrypted password property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeEncryptedPassword(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeEncryptedPassword";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.ENCRYPTED_PASSWORD.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the assetSummary property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String getAssetSummary(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeEncryptedPassword";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.ASSET_SUMMARY.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the "arguments" property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected Map<String, Object> getArguments(InstanceProperties  instanceProperties)
    {
        final String methodName = "getArguments";

        if (instanceProperties != null)
        {
            return repositoryHelper.getMapFromProperty(serviceName,
                                                       OpenMetadataProperty.ARGUMENTS.name,
                                                       instanceProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Retrieve the zoneName from the properties.
     *
     * @param instanceProperties properties from the entity
     * @return zone name
     */
    protected String removeZoneName(InstanceProperties instanceProperties)
    {
        final String methodName = "removeZoneName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.ZONE_NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Retrieve the subjectAreaName from the properties.
     *
     * @param instanceProperties properties from the entity
     * @return subject area name
     */
    protected String removeSubjectAreaName(InstanceProperties instanceProperties)
    {
        final String methodName = "removeSubjectAreaName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.SUBJECT_AREA_NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the distinguishedName property from the supplied instance properties.
     *
     * @param instanceProperties properties from classification
     * @return string
     */
    protected String removeDistinguishedName(InstanceProperties instanceProperties)

    {
        final String methodName = "removeDistinguishedName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.DISTINGUISHED_NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the karmaPoints property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return ordinal or 0 for not specified
     */
    protected long removeKarmaPoints(InstanceProperties instanceProperties)
    {
        final String methodName = "removeKarmaPoints";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeLongProperty(serviceName,
                                                      OpenMetadataProperty.KARMA_POINTS.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the resourceCreateTime property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return date or null
     */
    protected Date removeResourceCreateTime(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeResourceCreateTime";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeDateProperty(serviceName,
                                                       OpenMetadataProperty.RESOURCE_CREATE_TIME.name,
                                                       instanceProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the resourceUpdateTime property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return date or null
     */
    protected Date removeResourceUpdateTime(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeResourceUpdateTime";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeDateProperty(serviceName,
                                                       OpenMetadataProperty.RESOURCE_UPDATE_TIME.name,
                                                       instanceProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the resourceLastAccessedTime property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return date or null
     */
    protected Date removeResourceLastAccessedTime(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeResourceLastAccessedTime";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeDateProperty(serviceName,
                                                       OpenMetadataProperty.RESOURCE_LAST_ACCESSED_TIME.name,
                                                       instanceProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the pathName property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string or null
     */
    protected String removePathName(InstanceProperties  instanceProperties)
    {
        final String methodName = "removePathName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.PATH_NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the sourceCreateTime property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return date or null
     */
    protected Date removeStoreCreateTime(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeStoreCreateTime";

        if (instanceProperties != null)
        {
            Date createTime1 = repositoryHelper.removeDateProperty(serviceName,
                                                                   OpenMetadataProperty.STORE_CREATE_TIME.name,
                                                                   instanceProperties,
                                                                   methodName);
            Date createTime2 = repositoryHelper.removeDateProperty(serviceName,
                                                                   OpenMetadataProperty.CREATE_TIME.name,
                                                                   instanceProperties,
                                                                   methodName);
            return createTime1 == null ? createTime2 : createTime1;
        }

        return null;
    }


    /**
     * Extract and delete the storeUpdateTime property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return date or null
     */
    protected Date removeStoreUpdateTime(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeStoreUpdateTime";

        if (instanceProperties != null)
        {
            Date modifiedTime1 = repositoryHelper.removeDateProperty(serviceName,
                                                                     OpenMetadataProperty.STORE_UPDATE_TIME.name,
                                                                     instanceProperties,
                                                                     methodName);
            Date modifiedTime2 = repositoryHelper.removeDateProperty(serviceName,
                                                                     OpenMetadataProperty.UPDATE_TIME.name,
                                                                     instanceProperties,
                                                                     methodName);
            return modifiedTime1 == null ? modifiedTime2 : modifiedTime1;
        }

        return null;
    }



    /**
     * Extract the encoding property from the supplied instance properties.
     *
     * @param instanceProperties properties from classification
     * @return string text or null
     */
    protected String getDataStoreEncodingType(InstanceProperties  instanceProperties)
    {
        final String methodName = "getDataStoreEncodingType";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.ENCODING.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the encoding language property from the supplied instance properties.
     *
     * @param instanceProperties properties from classification
     * @return string text or null
     */
    protected String getDataStoreEncodingLanguage(InstanceProperties  instanceProperties)
    {
        final String methodName = "getDataStoreEncodingLanguage";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.ENCODING_LANGUAGE.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the encoding description property from the supplied instance properties.
     *
     * @param instanceProperties properties from classification
     * @return string text or null
     */
    protected String getDataStoreEncodingDescription(InstanceProperties  instanceProperties)
    {
        final String methodName = "getDataStoreEncodingDescription";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.ENCODING_DESCRIPTION.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the encoding properties property from the supplied instance properties.
     *
     * @param instanceProperties properties from classification
     * @return string map or null
     */
    protected Map<String, String> getEncodingProperties(InstanceProperties  instanceProperties)
    {
        final String methodName = "getEncodingProperties";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringMapFromProperty(serviceName,
                                                             OpenMetadataProperty.ENCODING_PROPERTIES.name,
                                                             instanceProperties,
                                                             methodName);
        }

        return null;
    }


    /**
     * Extract and delete the database version property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String removeDatabaseVersion(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeDatabaseVersion";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.DATABASE_VERSION.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the database instance property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String removeDatabaseInstance(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeDatabaseInstance";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.INSTANCE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the database importedFrom property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String removeDatabaseImportedFrom(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeDatabaseImportedFrom";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.IMPORTED_FROM.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the fileName property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String removeFileName(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeFileName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.FILE_NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }



    /**
     * Extract and delete the fileType property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String removeFileType(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeFileType";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.FILE_TYPE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the fileExtension property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String removeFileExtension(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeFileExtension";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.FILE_EXTENSION.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }



    /**
     * Extract and delete the format property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String getFormat(InstanceProperties  instanceProperties)
    {
        final String methodName = "getFormat";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.FORMAT.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract and delete the encryption property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String getEncryption(InstanceProperties  instanceProperties)
    {
        final String methodName = "getEncryption";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.ENCRYPTION.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract and delete the type property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeDeployedImplementationType(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeDeployedImplementationType";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }




    /**
     * Extract and delete the type property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeSupportedDeployedImplementationType(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeDeployedImplementationType";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.SUPPORTED_DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }



    /**
     * Extract and delete the type property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeCapabilityType(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeCapabilityType";

        if (instanceProperties != null)
        {
            String type = repositoryHelper.removeStringProperty(serviceName,
                                                                OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                                instanceProperties,
                                                                methodName);

            if (type == null)
            {
                type = repositoryHelper.removeStringProperty(serviceName,
                                                             OpenMetadataProperty.CAPABILITY_TYPE.name,
                                                             instanceProperties,
                                                             methodName);
            }

            return type;
        }

        return null;
    }


    /**
     * Extract and delete the capabilityVersion property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeCapabilityVersion(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeCapabilityVersion";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.CAPABILITY_VERSION.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the patchLevel property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removePatchLevel(InstanceProperties  instanceProperties)
    {
        final String methodName = "removePatchLevel";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.PATCH_LEVEL.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }





    /**
     * Retrieve the isDeprecated flag from the properties from the supplied instance properties.
     *
     * @param instanceProperties properties from the classification
     * @return boolean - default is false
     */
    protected boolean removeIsDeprecated(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeIsDeprecated";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeBooleanProperty(serviceName,
                                                          OpenMetadataProperty.IS_DEPRECATED.name,
                                                          instanceProperties,
                                                          methodName);
        }

        return false;
    }


    /**
     * Retrieve the isDefaultValue flag from the properties from the supplied instance properties.
     *
     * @param instanceProperties properties from the classification
     * @return boolean - default is false
     */
    protected boolean removeIsDefaultValue(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeIsDefaultValue";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeBooleanProperty(serviceName,
                                                          OpenMetadataProperty.IS_DEFAULT_VALUE.name,
                                                          instanceProperties,
                                                          methodName);
        }

        return false;
    }


    /**
     * Extract and delete the data type property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String removeDataType(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeDataType";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.DATA_TYPE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the defaultValue property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeDefaultValue(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeDefaultValue";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.DEFAULT_VALUE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the defaultValue property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeFixedValue(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeFixedValue";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.FIXED_VALUE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the query property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String getQuery(InstanceProperties  instanceProperties)
    {
        final String methodName = "setQuery";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.QUERY.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the queryId property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String getQueryId(InstanceProperties  instanceProperties)
    {
        final String methodName = "setQueryId";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.QUERY_ID.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }



    /**
     * Extract and delete the version number property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String removeVersionNumber(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeVersionNumber";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.VERSION_NUMBER.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the id property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeId(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeId";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.ID.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the createdTime property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected Date removeCreatedTime(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeCreatedTime";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeDateProperty(serviceName,
                                                       OpenMetadataProperty.CREATED_TIME.name,
                                                       instanceProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the createdTime property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected Date removeLastModifiedTime(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeLastModifiedTime";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeDateProperty(serviceName,
                                                       OpenMetadataProperty.LAST_MODIFIED_TIME.name,
                                                       instanceProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the lastModifier property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeLastModifier(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeId";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.LAST_MODIFIER.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the author property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeAuthor(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeAuthor";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.AUTHOR.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the encoding standing property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeEncodingStandard(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeEncodingStandard";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.ENCODING_STANDARD.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the namespace property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String removeNamespace(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeNamespace";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.NAMESPACE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the position property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return integer - default 0
     */
    protected int removePosition(InstanceProperties  instanceProperties)
    {
        final String methodName = "removePosition";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeIntProperty(serviceName,
                                                      OpenMetadataProperty.POSITION.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
    }


    /**
     * Extract the position property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return integer - default 0
     */
    protected int getPosition(InstanceProperties  instanceProperties)
    {
        final String methodName = "getPosition";

        if (instanceProperties != null)
        {
            return repositoryHelper.getIntProperty(serviceName,
                                                   OpenMetadataProperty.POSITION.name,
                                                   instanceProperties,
                                                   methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the minCardinality property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return integer - default 0
     */
    protected int removeMinCardinality(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeMinCardinality";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeIntProperty(serviceName,
                                                      OpenMetadataProperty.MIN_CARDINALITY.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
    }


    /**
     * Extract the minCardinality property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return integer - default 0
     */
    protected int getMinCardinality(InstanceProperties  instanceProperties)
    {
        final String methodName = "getMinCardinality";

        if (instanceProperties != null)
        {
            return repositoryHelper.getIntProperty(serviceName,
                                                   OpenMetadataProperty.MIN_CARDINALITY.name,
                                                   instanceProperties,
                                                   methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the maxCardinality property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return integer - default -1 which is unlimited
     */
    protected int removeMaxCardinality(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeMaxCardinality";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeIntProperty(serviceName,
                                                      OpenMetadataProperty.MAX_CARDINALITY.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return -1;
    }


    /**
     * Extract the maxCardinality property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return integer - default -1 which is unlimited
     */
    protected int getMaxCardinality(InstanceProperties  instanceProperties)
    {
        final String methodName = "getMaxCardinality";

        if (instanceProperties != null)
        {
            return repositoryHelper.getIntProperty(serviceName,
                                                   OpenMetadataProperty.MAX_CARDINALITY.name,
                                                   instanceProperties,
                                                   methodName);
        }

        return -1;
    }


    /**
     * Retrieve the allowsDuplicateValues flag from the properties of the zone membership classification.
     *
     * @param instanceProperties properties from the classification
     * @return boolean - default is true
     */
    protected boolean removeAllowsDuplicateValues(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeAllowsDuplicateValues";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeBooleanProperty(serviceName,
                                                          OpenMetadataProperty.ALLOWS_DUPLICATE_VALUES.name,
                                                          instanceProperties,
                                                          methodName);
        }

        return true;
    }


    /**
     * Retrieve the orderedValues flag from the properties of the zone membership classification.
     *
     * @param instanceProperties properties from the classification
     * @return boolean - default is false
     */
    protected boolean removeOrderedValues(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeOrderedValues";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeBooleanProperty(serviceName,
                                                          OpenMetadataProperty.ORDERED_VALUES.name,
                                                          instanceProperties,
                                                          methodName);
        }

        return false;
    }


    /**
     * Extract and delete the defaultValueOverride property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeDefaultValueOverride(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeDefaultValueOverride";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.DEFAULT_VALUE_OVERRIDE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the minimumLength property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return integer - default 0
     */
    protected int removeMinimumLength(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeMinimumLength";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeIntProperty(serviceName,
                                                      OpenMetadataProperty.MINIMUM_LENGTH.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the length property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return integer - default 0
     */
    protected int removeLength(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeLength";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeIntProperty(serviceName,
                                                      OpenMetadataProperty.LENGTH.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the precision property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return integer - default 0
     */
    protected int removePrecision(InstanceProperties  instanceProperties)
    {
        final String methodName = "removePrecision";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeIntProperty(serviceName,
                                                      OpenMetadataProperty.PRECISION.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
    }


    /**
     * Retrieve the isNullable flag from the properties from the supplied instance properties.
     *
     * @param instanceProperties properties from the classification
     * @return boolean - default is false
     */
    protected boolean removeIsNullable(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeIsNullable";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeBooleanProperty(serviceName,
                                                          OpenMetadataProperty.IS_NULLABLE.name,
                                                          instanceProperties,
                                                          methodName);
        }

        return false;
    }


    /**
     * Retrieve the required flag from the properties from the supplied instance properties.
     *
     * @param instanceProperties properties from the classification
     * @return boolean - default is false
     */
    protected boolean removeRequired(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeRequired";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeBooleanProperty(serviceName,
                                                          OpenMetadataProperty.REQUIRED.name,
                                                          instanceProperties,
                                                          methodName);
        }

        return false;
    }


    /**
     * Extract and delete the native class property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String removeNativeClass(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeNativeClass";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.NATIVE_CLASS.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the "aliases" property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected List<String> removeAliases(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeAliases";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataProperty.ALIASES.name,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the guard property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String getGuard(InstanceProperties  instanceProperties)
    {
        final String methodName = "getGuard";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.GUARD.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the iscQualifiedName property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String getISCQualifiedName(InstanceProperties  instanceProperties)
    {
        final String methodName = "getISCQualifiedName";

        if (instanceProperties != null)
        {
            String iscQualifiedName = repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                      instanceProperties,
                                                      methodName);

            if (iscQualifiedName != null)
            {
                return iscQualifiedName;
            }

            /*
             * Backwards compatibility
             */
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.QUALIFIED_NAME.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the label property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String getLabel(InstanceProperties  instanceProperties)
    {
        final String methodName = "getLabel";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.LABEL.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the formula property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String getFormula(InstanceProperties  instanceProperties)
    {
        final String methodName = "getFormula";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.FORMULA.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the formulaType property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String getFormulaType(InstanceProperties  instanceProperties)
    {
        final String methodName = "getFormulaType";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.FORMULA_TYPE.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract and delete the formula property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeFormula(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeFormula";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.FORMULA.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the formulaType property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeFormulaType(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeFormulaType";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.FORMULA_TYPE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and remove the implementationLanguage property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeImplementationLanguage(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeImplementationLanguage";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.IMPLEMENTATION_LANGUAGE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the processStartTime property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected Date removeProcessStartTime(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeProcessStartTime";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeDateProperty(serviceName,
                                                         OpenMetadataProperty.PROCESS_START_TIME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the processStartTime property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected Date removeProcessEndTime(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeProcessEndTime";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeDateProperty(serviceName,
                                                       OpenMetadataProperty.PROCESS_END_TIME.name,
                                                       instanceProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and remove the usesBlockingCalls property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected boolean removeUsesBlockingCalls(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeUsesBlockingCalls";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeBooleanProperty(serviceName,
                                                          OpenMetadataProperty.USES_BLOCKING_CALLS.name,
                                                          instanceProperties,
                                                          methodName);
        }

        return false;
    }


    /**
     * Extract and delete the type property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeSource(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeSource";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.SOURCE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the usage property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String getUsage(InstanceProperties instanceProperties)
    {
        final String methodName = "getUsage";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.USAGE.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract and delete the usage property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeUsage(InstanceProperties instanceProperties)
    {
        final String methodName = "removeUsage";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.USAGE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the language property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeLanguage(InstanceProperties instanceProperties)
    {
        final String methodName = "removeLanguage";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.LANGUAGE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }




    /**
     * Extract the summary property from the supplied instance properties.
     *
     * @param instanceProperties properties from governance entities
     * @return string property or null
     */
    protected String getSummary(InstanceProperties instanceProperties)
    {
        final String methodName = "getSummary";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.SUMMARY.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract and remove the summary property from the supplied instance properties.
     *
     * @param instanceProperties properties from governance entities
     * @return string property or null
     */
    protected String removeSummary(InstanceProperties instanceProperties)
    {
        final String methodName = "removeSummary";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.SUMMARY.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }



    /**
     * Extract and remove the publishVersionIdentifier property from the supplied instance properties.
     *
     * @param instanceProperties properties from governance entities
     * @return string property or null
     */
    protected String removePublishVersionIdentifier(InstanceProperties instanceProperties)
    {
        final String methodName = "removePublishVersionIdentifier";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.PUBLISH_VERSION_ID.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }





    /**
     * Extract the abbreviation property from the supplied instance properties.
     *
     * @param instanceProperties properties from governance entities
     * @return string property or null
     */
    protected String getAbbreviation(InstanceProperties instanceProperties)
    {
        final String methodName = "getAbbreviation";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.ABBREVIATION.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract and remove the abbreviation property from the supplied instance properties.
     *
     * @param instanceProperties properties from governance entities
     * @return string property or null
     */
    protected String removeAbbreviation(InstanceProperties instanceProperties)
    {
        final String methodName = "removeAbbreviation";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.ABBREVIATION.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }



    /**
     * Extract and remove the "examples" property from the supplied instance properties.
     *
     * @param instanceProperties properties from governance entities
     * @return string property or null
     */
    protected String removeExamples(InstanceProperties instanceProperties)
    {
        final String methodName = "removeExamples";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.EXAMPLES.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the title property from the supplied instance properties.
     *
     * @param instanceProperties properties from governance entities
     * @return string property or null
     */
    protected String removeTitle(InstanceProperties instanceProperties)
    {
        final String methodName = "removeTitle";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.TITLE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the text property from the supplied instance properties.
     *
     * @param instanceProperties properties from governance entities
     * @return string property or null
     */
    protected String removeText(InstanceProperties instanceProperties)
    {
        final String methodName = "removeText";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.TEXT.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the priority(importance) property from the supplied instance properties.
     *
     * @param instanceProperties properties from governance entities
     * @return string property or null
     */
    protected String removeImportance(InstanceProperties instanceProperties)
    {
        final String methodName = "removeImportance";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.IMPORTANCE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the priority integer property from the supplied instance properties.
     *
     * @param instanceProperties properties from governance entities
     * @return string property or null
     */
    protected int removeIntPriority(InstanceProperties instanceProperties)
    {
        final String methodName = "removeIntPriority";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeIntProperty(serviceName,
                                                      OpenMetadataProperty.PRIORITY.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
    }


    /**
     * Extract the headcount integer property from the supplied instance properties.
     *
     * @param instanceProperties properties from governance entities
     * @return string property or null
     */
    protected int removeHeadCount(InstanceProperties instanceProperties)
    {
        final String methodName = "removeHeadCount";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeIntProperty(serviceName,
                                                      OpenMetadataProperty.HEAD_COUNT.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the scope property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeScope(InstanceProperties instanceProperties)
    {
        final String methodName = "removeScope";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.SCOPE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the category property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeCategory(InstanceProperties instanceProperties)
    {
        final String methodName = "removeCategory";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.CATEGORY.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }



    /**
     * Extract and delete the "outcomes" property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string list or null
     */
    protected List<String> removeOutcomes(InstanceProperties instanceProperties)
    {
        final String methodName = "removeOutcomes";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataProperty.OUTCOMES.name,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the "implications" property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string list or null
     */
    protected List<String> removeImplications(InstanceProperties instanceProperties)
    {
        final String methodName = "removeImplications";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataProperty.IMPLICATIONS.name,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the "results" property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string list or null
     */
    protected List<String> removeResults(InstanceProperties instanceProperties)
    {
        final String methodName = "removeResults";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataProperty.RESULTS.name,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the "details" property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeDetails(InstanceProperties instanceProperties)
    {
        final String methodName = "removeDetails";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.DETAILS.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the criteria property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeCriteria(InstanceProperties instanceProperties)
    {
        final String methodName = "removeCriteria";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.CRITERIA.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the domain identifier property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return integer = default is 0 which is ALL
     */
    protected int removeDomainIdentifier(InstanceProperties instanceProperties)

    {
        final String methodName = "removeDomainIdentifier";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeIntProperty(serviceName,
                                                      OpenMetadataProperty.DOMAIN_IDENTIFIER.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the measurement property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string
     */
    protected String removeMeasurement(InstanceProperties instanceProperties)

    {
        final String methodName = "removeMeasurement";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.MEASUREMENT.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the target property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string
     */
    protected String removeTarget(InstanceProperties instanceProperties)

    {
        final String methodName = "removeTarget";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.TARGET.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the processingEngineUserId property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string
     */
    protected String removeProcessingEngineUserId(InstanceProperties instanceProperties)

    {
        final String methodName = "removeProcessingEngineUserId";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.PROCESSING_ENGINE_USER_ID.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the requesterUserId property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string
     */
    protected String removeRequesterUserId(InstanceProperties instanceProperties)

    {
        final String methodName = "removeRequesterUserId";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.REQUESTER_USER_ID.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the requestType property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string
     */
    protected String removeRequestType(InstanceProperties instanceProperties)
    {
        final String methodName = "removeRequestType";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.REQUEST_TYPE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the requestParameters property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string
     */
    protected Map<String, String> removeRequestParameters(InstanceProperties instanceProperties)

    {
        final String methodName = "removeRequestParameters";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                OpenMetadataProperty.REQUEST_PARAMETERS.name,
                                                                instanceProperties,
                                                                methodName);
        }

        return null;
    }




    /**
     * Extract and delete the executorEngineGUID property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string
     */
    protected String removeExecutorEngineGUID(InstanceProperties instanceProperties)

    {
        final String methodName = "removeExecutorEngineGUID";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.EXECUTOR_ENGINE_GUID.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the executorEngineName property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string
     */
    protected String removeExecutorEngineName(InstanceProperties instanceProperties)

    {
        final String methodName = "removeExecutorEngineName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.EXECUTOR_ENGINE_NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }



    /**
     * Extract and delete the processName property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string
     */
    protected String removeProcessName(InstanceProperties instanceProperties)

    {
        final String methodName = "removeProcessName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.PROCESS_NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the processStepGUID property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string
     */
    protected String removeProcessStepGUID(InstanceProperties instanceProperties)

    {
        final String methodName = "removeProcessStepGUID";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.PROCESS_STEP_GUID.name,
                                                         instanceProperties,
                                                         methodName);

        }

        return null;
    }


    /**
     * Extract and delete the processStepName property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string
     */
    protected String removeProcessStepName(InstanceProperties instanceProperties)

    {
        final String methodName = "removeProcessStepName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.PROCESS_STEP_NAME.name,
                                                         instanceProperties,
                                                         methodName);

        }

        return null;
    }



    /**
     * Extract and delete the governanceActionTypeGUID property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string
     */
    protected String removeGovernanceActionTypeGUID(InstanceProperties instanceProperties)

    {
        final String methodName = "removeGovernanceActionTypeGUID";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.GOVERNANCE_ACTION_TYPE_GUID.name,
                                                         instanceProperties,
                                                         methodName);

        }

        return null;
    }


    /**
     * Extract and delete the governanceActionTypeName property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string
     */
    protected String removeGovernanceActionTypeName(InstanceProperties instanceProperties)

    {
        final String methodName = "removeGovernanceActionTypeName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.GOVERNANCE_ACTION_TYPE_NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the guard property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string
     */
    protected String removeGuard(InstanceProperties instanceProperties)

    {
        final String methodName = "removeGuard";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.GUARD.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the mandatoryGuards property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return list of guards
     */
    protected List<String> removeMandatoryGuards(InstanceProperties instanceProperties)

    {
        final String methodName = "removeMandatoryGuards";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataProperty.MANDATORY_GUARDS.name,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the mandatoryGuard property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return flag
     */
    protected boolean removeMandatoryGuard(InstanceProperties instanceProperties)

    {
        final String methodName = "removeMandatoryGuard";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeBooleanProperty(serviceName,
                                                          OpenMetadataProperty.MANDATORY_GUARD.name,
                                                          instanceProperties,
                                                          methodName);
        }

        return false;
    }


    /**
     * Extract and delete the ignoreMultipleTriggers property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return flag
     */
    protected boolean removeIgnoreMultipleTriggers(InstanceProperties instanceProperties)

    {
        final String methodName = "removeIgnoreMultipleTriggers";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeBooleanProperty(serviceName,
                                                          OpenMetadataProperty.IGNORE_MULTIPLE_TRIGGERS.name,
                                                          instanceProperties,
                                                          methodName);
        }

        return false;
    }


    /**
     * Extract and delete the waitTime property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return integer
     */
    protected int removeWaitTime(InstanceProperties instanceProperties)

    {
        final String methodName = "removeWaitTime";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeIntProperty(serviceName,
                                                      OpenMetadataProperty.WAIT_TIME.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the receivedGuards property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return list of guards
     */
    protected List<String> removeReceivedGuards(InstanceProperties instanceProperties)

    {
        final String methodName = "removeReceivedGuards";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataProperty.RECEIVED_GUARDS.name,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the completionGuards property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return list of guards
     */
    protected List<String> removeCompletionGuards(InstanceProperties instanceProperties)

    {
        final String methodName = "removeCompletionGuards";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataProperty.COMPLETION_GUARDS.name,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the completionMessage property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string
     */
    protected String removeCompletionMessage(InstanceProperties instanceProperties)

    {
        final String methodName = "removeCompletionMessage";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.COMPLETION_MESSAGE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the startDate property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return date
     */
    protected Date removeStartDate(InstanceProperties instanceProperties)

    {
        final String methodName = "removeStartDate";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeDateProperty(serviceName,
                                                       OpenMetadataProperty.START_DATE.name,
                                                       instanceProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the requestedStartDate property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return date
     */
    protected Date removeRequestedStartDate(InstanceProperties instanceProperties)

    {
        final String methodName = "removeRequestedStartDate";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeDateProperty(serviceName,
                                                       OpenMetadataProperty.REQUESTED_START_DATE.name,
                                                       instanceProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the plannedEndDate property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return date
     */
    protected Date removePlannedEndDate(InstanceProperties instanceProperties)

    {
        final String methodName = "removePlannedEndDate";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeDateProperty(serviceName,
                                                       OpenMetadataProperty.PLANNED_END_DATE.name,
                                                       instanceProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the completionDate property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return date
     */
    protected Date removeCompletionDate(InstanceProperties instanceProperties)

    {
        final String methodName = "removeCompletionDate";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeDateProperty(serviceName,
                                                       OpenMetadataProperty.COMPLETION_DATE.name,
                                                       instanceProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the project status property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return String text or null
     */
    protected String removeProjectStatus(InstanceProperties instanceProperties)

    {
        final String methodName = "removeProjectStatus";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.PROJECT_STATUS.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the project phase property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return String text or null
     */
    protected String removeProjectPhase(InstanceProperties instanceProperties)

    {
        final String methodName = "removeProjectPhase";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.PROJECT_PHASE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }



    /**
     * Extract and delete the project health property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return String text or null
     */
    protected String removeProjectHealth(InstanceProperties instanceProperties)

    {
        final String methodName = "removeProjectHealth";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.PROJECT_HEALTH.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the requestSourceName property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return String text or null
     */
    protected String removeRequestSourceName(InstanceProperties instanceProperties)

    {
        final String methodName = "removeRequestSourceName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.REQUEST_SOURCE_NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the actionTargetName property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return String text or null
     */
    protected String removeActionTargetName(InstanceProperties instanceProperties)

    {
        final String methodName = "removeActionTargetName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.ACTION_TARGET_NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the originGovernanceService property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return String text or null
     */
    protected String removeOriginGovernanceService(InstanceProperties instanceProperties)

    {
        final String methodName = "removeOriginGovernanceService";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.ORIGIN_GOVERNANCE_SERVICE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the originGovernanceEngine property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return String text or null
     */
    protected String removeOriginGovernanceEngine(InstanceProperties instanceProperties)

    {
        final String methodName = "removeOriginGovernanceEngine";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.ORIGIN_GOVERNANCE_ENGINE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the certificationGUID property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return String text or null
     */
    protected String getCertificationGUID(InstanceProperties instanceProperties)

    {
        final String methodName = "getCertificationGUID";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.CERTIFICATE_GUID.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the start property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return Date/timestamp or null
     */
    protected Date getStart(InstanceProperties instanceProperties)

    {
        final String methodName = "getStart";

        if (instanceProperties != null)
        {
            return repositoryHelper.getDateProperty(serviceName,
                                                    OpenMetadataProperty.START.name,
                                                    instanceProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the end property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return Date/timestamp or null
     */
    protected Date getEnd(InstanceProperties instanceProperties)

    {
        final String methodName = "getEnd";

        if (instanceProperties != null)
        {
            return repositoryHelper.getDateProperty(serviceName,
                                                    OpenMetadataProperty.END.name,
                                                    instanceProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the "conditions" property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String getConditions(InstanceProperties instanceProperties)
    {
        final String methodName = "getConditions";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.CONDITIONS.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the custodian property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String getCustodian(InstanceProperties instanceProperties)
    {
        final String methodName = "getCustodian";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.CUSTODIAN.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the certifiedBy property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String getCertifiedBy(InstanceProperties instanceProperties)
    {
        final String methodName = "getCertifiedBy";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.CERTIFIED_BY.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the recipient property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String getRecipient(InstanceProperties instanceProperties)
    {
        final String methodName = "getRecipient";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.RECIPIENT.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }



    /**
     * Extract the licensedBy property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String getLicensedBy(InstanceProperties instanceProperties)
    {
        final String methodName = "getLicensedBy";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.LICENSED_BY.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the licensee property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String getLicensee(InstanceProperties instanceProperties)
    {
        final String methodName = "getLicensee";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.LICENSEE.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }




    /**
     * Extract and delete the preferredValue property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removePreferredValue(InstanceProperties instanceProperties)
    {
        final String methodName = "removePreferredValue";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.PREFERRED_VALUE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the isCaseSensitive property from the supplied instance properties.
     *
     * @param instanceProperties properties from ValidValueDefinition relationship
     * @return boolean
     */
    protected boolean removeIsCaseSensitive(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeIsCaseSensitive";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeBooleanProperty(serviceName,
                                                          OpenMetadataProperty.IS_CASE_SENSITIVE.name,
                                                          instanceProperties,
                                                          methodName);
        }

        return false;
    }


    /**
     * Extract the strictRequirement property from the supplied instance properties.
     *
     * @param instanceProperties properties from ValidValuesAssignment relationship
     * @return boolean
     */
    protected boolean getStrictRequirement(InstanceProperties  instanceProperties)
    {
        final String methodName = "getStrictRequirement";

        if (instanceProperties != null)
        {
            return repositoryHelper.getBooleanProperty(serviceName,
                                                       OpenMetadataProperty.STRICT_REQUIREMENT.name,
                                                       instanceProperties,
                                                       methodName);
        }

        return false;
    }


    /**
     * Extract the confidence property from the supplied instance properties.
     *
     * @param instanceProperties properties from ReferenceValueAssignment or ValidValuesMapping relationship
     * @return int
     */
    protected int getConfidence(InstanceProperties  instanceProperties)
    {
        final String methodName = "getConfidence";

        if (instanceProperties != null)
        {
            return repositoryHelper.getIntProperty(serviceName,
                                                   OpenMetadataProperty.CONFIDENCE.name,
                                                   instanceProperties,
                                                   methodName);
        }

        return 0;
    }


    /**
     * Extract the steward property from the supplied instance properties.
     *
     * @param instanceProperties properties from ReferenceValueAssignment or ValidValuesMapping relationship
     * @return string text or null
     */
    protected String getSteward(InstanceProperties  instanceProperties)
    {
        final String methodName = "getSteward";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.STEWARD.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }



    /**
     * Extract the stewardTypeName property from the supplied instance properties.
     *
     * @param instanceProperties properties from ReferenceValueAssignment or ValidValuesMapping relationship
     * @return string text or null
     */
    protected String getStewardTypeName(InstanceProperties  instanceProperties)
    {
        final String methodName = "getStewardTypeName";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the stewardTypeName property from the supplied instance properties.
     *
     * @param instanceProperties properties from ReferenceValueAssignment or ValidValuesMapping relationship
     * @return string text or null
     */
    protected String getStewardPropertyName(InstanceProperties  instanceProperties)
    {
        final String methodName = "getStewardPropertyName";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the "notes" property from the supplied instance properties.
     *
     * @param instanceProperties properties from GovernanceRuleImplementation, GovernanceProcessImplementation,
     *                           ReferenceValueAssignment or ValidValuesMapping relationship
     * @return string text or null
     */
    protected String getNotes(InstanceProperties  instanceProperties)
    {
        final String methodName = "getNotes";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.NOTES.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the "attributeName" property from the supplied instance properties.
     *
     * @param instanceProperties properties from ReferenceValueAssignment relationship
     * @return string text or null
     */
    protected String getAttributeName(InstanceProperties  instanceProperties)
    {
        final String methodName = "getAttributeName";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.ATTRIBUTE_NAME.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the associationDescription property from the supplied instance properties.
     *
     * @param instanceProperties properties from ValidValuesMapping relationship
     * @return string text or null
     */
    protected String getAssociationDescription(InstanceProperties  instanceProperties)
    {
        final String methodName = "getAssociationDescription";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.ASSOCIATION_DESCRIPTION.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the symbolicName property from the supplied instance properties.
     *
     * @param instanceProperties properties from ValidValuesImplementation relationship
     * @return string text or null
     */
    protected String getSymbolicName(InstanceProperties  instanceProperties)
    {
        final String methodName = "getSymbolicName";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.SYMBOLIC_NAME.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the implementationValue property from the supplied instance properties.
     *
     * @param instanceProperties properties from ValidValuesImplementation relationship
     * @return string text or null
     */
    protected String getImplementationValue(InstanceProperties  instanceProperties)
    {
        final String methodName = "getImplementationValue";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.IMPLEMENTATION_VALUE.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the additionalValues property from the supplied instance properties.
     *
     * @param instanceProperties properties from ValidValuesImplementation relationship
     * @return map of name-value pairs
     */
    protected Map<String, String> getAdditionalValues(InstanceProperties  instanceProperties)
    {
        final String methodName = "getAdditionalValues";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringMapFromProperty(serviceName,
                                                             OpenMetadataProperty.ADDITIONAL_VALUES.name,
                                                             instanceProperties,
                                                             methodName);
        }

        return null;
    }


    /**
     * Extract and delete the commentText property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeCommentText(InstanceProperties instanceProperties)
    {
        final String methodName = "removeCommentText";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.TEXT.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the isPublic property from the supplied instance properties.
     *
     * @param instanceProperties properties from feedback relationships
     * @return boolean
     */
    protected boolean getIsPublic(InstanceProperties instanceProperties)
    {
        final String methodName = "getIsPublic";

        if (instanceProperties != null)
        {
            return repositoryHelper.getBooleanProperty(serviceName,
                                                       OpenMetadataProperty.IS_PUBLIC.name,
                                                       instanceProperties,
                                                       methodName);
        }

        return false;
    }


    /**
     * Extract the isPublic property from the supplied instance properties.
     *
     * @param instanceProperties properties from feedback relationships
     * @return boolean
     */
    protected boolean removeIsPublic(InstanceProperties instanceProperties)
    {
        final String methodName = "removeIsPublic";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeBooleanProperty(serviceName,
                                                          OpenMetadataProperty.IS_PUBLIC.name,
                                                          instanceProperties,
                                                          methodName);
        }

        return false;
    }


    /**
     * Extract the review property from the supplied instance properties.
     *
     * @param instanceProperties properties from review/rating entities
     * @return string property or null
     */
    protected String removeReview(InstanceProperties instanceProperties)
    {
        final String methodName = "removeReview";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.REVIEW.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the tagName property from the supplied instance properties.
     *
     * @param instanceProperties properties from informal tag entities
     * @return string property or null
     */
    protected String removeTagName(InstanceProperties instanceProperties)
    {
        final String methodName = "removeTagName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.TAG_NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the tagDescription property from the supplied instance properties.
     *
     * @param instanceProperties properties from informal tag entities
     * @return string property or null
     */
    protected String removeTagDescription(InstanceProperties instanceProperties)
    {
        final String methodName = "removeTagDescription";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.TAG_DESCRIPTION.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the analysis parameters property from the supplied instance properties.
     *
     * @param instanceProperties properties from discovery analysis report entities
     * @return string property or null
     */
    protected Map<String, String> removeAnalysisParameters(InstanceProperties instanceProperties)
    {
        final String methodName = "removeAnalysisParameters";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                OpenMetadataProperty.ANALYSIS_PARAMETERS.name,
                                                                instanceProperties,
                                                                methodName);
        }

        return null;
    }


    /**
     * Extract the analysis step property from the supplied instance properties.
     *
     * @param instanceProperties properties from discovery analysis report entities
     * @return string property or null
     */
    protected String removeAnalysisStep(InstanceProperties instanceProperties)
    {
        final String methodName = "removeAnalysisStep";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.ANALYSIS_STEP.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the annotation type property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeAnnotationType(InstanceProperties instanceProperties)
    {
        final String methodName = "removeAnnotationType";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.ANNOTATION_TYPE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the confidence level property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return integer or 0
     */
    protected int removeConfidenceLevel(InstanceProperties instanceProperties)
    {
        final String methodName = "removeConfidenceLevel";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeIntProperty(serviceName,
                                                      OpenMetadataProperty.CONFIDENCE_LEVEL.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
    }


    /**
     * Extract the confidence property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return integer or 0
     */
    protected int removeConfidence(InstanceProperties instanceProperties)
    {
        final String methodName = "removeConfidence";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeIntProperty(serviceName,
                                                      OpenMetadataProperty.CONFIDENCE.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
    }


    /**
     * Extract the expression property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeExpression(InstanceProperties instanceProperties)
    {
        final String methodName = "removeExpression";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.EXPRESSION.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the explanation property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeExplanation(InstanceProperties instanceProperties)
    {
        final String methodName = "removeExplanation";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.EXPLANATION.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the jsonProperties property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeJsonProperties(InstanceProperties instanceProperties)
    {
        final String methodName = "removeJsonProperties";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.JSON_PROPERTIES.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the reviewDate property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation review entities
     * @return date or null
     */
    protected Date removeReviewDate(InstanceProperties instanceProperties)
    {
        final String methodName = "removeReviewDate";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeDateProperty(serviceName,
                                                       OpenMetadataProperty.REVIEW_DATE.name,
                                                       instanceProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the steward property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation review entities
     * @return string property or null
     */
    protected String removeSteward(InstanceProperties instanceProperties)
    {
        final String methodName = "removeSteward";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.STEWARD.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the comment property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation review entities
     * @return string property or null
     */
    protected String removeComment(InstanceProperties instanceProperties)
    {
        final String methodName = "removeComment";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.COMMENT.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the schemaName property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeSchemaName(InstanceProperties instanceProperties)
    {
        final String methodName = "removeSchemaName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.SCHEMA_NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the schemaType property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeSchemaType(InstanceProperties instanceProperties)
    {
        final String methodName = "removeSchemaType";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.SCHEMA_TYPE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the candidateClassifications property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return map of name value pairs
     */
    protected Map<String, String> removeCandidateClassifications(InstanceProperties instanceProperties)
    {
        final String methodName = "removeCandidateClassifications";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                OpenMetadataProperty.CANDIDATE_CLASSIFICATIONS.name,
                                                                instanceProperties,
                                                                methodName);
        }

        return null;
    }


    /**
     * Extract the candidateDataClassGUIDs property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return list of string guids
     */
    protected List<String> removeCandidateDataClassGUIDs(InstanceProperties instanceProperties)
    {
        final String methodName = "removeCandidateDataClassGUIDs";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataProperty.CANDIDATE_DATA_CLASS_GUIDS.name,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the inferredDataType property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeInferredDataType(InstanceProperties instanceProperties)
    {
        final String methodName = "removeInferredDataType";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.INFERRED_DATA_TYPE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the inferredFormat property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeInferredFormat(InstanceProperties instanceProperties)
    {
        final String methodName = "removeInferredFormat";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.INFERRED_FORMAT.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the inferredLength property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return int property or 0
     */
    protected int removeInferredLength(InstanceProperties instanceProperties)
    {
        final String methodName = "removeInferredLength";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeIntProperty(serviceName,
                                                      OpenMetadataProperty.INFERRED_LENGTH.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
    }


    /**
     * Extract the inferredPrecision property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return int property or 0
     */
    protected int removeInferredPrecision(InstanceProperties instanceProperties)
    {
        final String methodName = "removeInferredPrecision";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeIntProperty(serviceName,
                                                      OpenMetadataProperty.INFERRED_PRECISION.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
    }


    /**
     * Extract the inferredScale property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return int property or 0
     */
    protected int removeInferredScale(InstanceProperties instanceProperties)
    {
        final String methodName = "removeInferredScale";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeIntProperty(serviceName,
                                                      OpenMetadataProperty.INFERRED_SCALE.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
    }


    /**
     * Extract the profileStartDate property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return date
     */
    protected Date removeProfileStartDate(InstanceProperties instanceProperties)
    {
        final String methodName = "removeProfileStartDate";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeDateProperty(serviceName,
                                                       OpenMetadataProperty.PROFILE_START_DATE.name,
                                                       instanceProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the profileEndDate property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return date
     */
    protected Date removeProfileEndDate(InstanceProperties instanceProperties)
    {
        final String methodName = "removeProfileEndDate";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeDateProperty(serviceName,
                                                       OpenMetadataProperty.PROFILE_END_DATE.name,
                                                       instanceProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract the profileProperties property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return map of name value pairs
     */
    protected Map<String, String> removeProfileProperties(InstanceProperties instanceProperties)
    {
        final String methodName = "removeProfileProperties";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                OpenMetadataProperty.PROFILE_PROPERTIES.name,
                                                                instanceProperties,
                                                                methodName);
        }

        return null;
    }


    /**
     * Extract the profilePropertyNames property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return list of names
     */
    protected List<String> removeProfilePropertyNames(InstanceProperties instanceProperties)
    {
        final String methodName = "removeProfilePropertyNames";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataProperty.PROFILE_PROPERTY_NAMES.name,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the profileFlags property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return map of name to boolean pairs
     */
    protected Map<String, Boolean> removeProfileFlags(InstanceProperties instanceProperties)
    {
        final String methodName = "removeProfileFlags";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeBooleanMapFromProperty(serviceName,
                                                                 OpenMetadataProperty.PROFILE_FLAGS.name,
                                                                 instanceProperties,
                                                                 methodName);
        }

        return null;
    }


    /**
     * Extract the profileDates property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return map of name to boolean pairs
     */
    protected Map<String, Date> removeProfileDates(InstanceProperties instanceProperties)
    {
        final String methodName = "removeProfileDates";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeDateMapFromProperty(serviceName,
                                                              OpenMetadataProperty.PROFILE_DATES.name,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the profileCounts property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return map of name to long pairs
     */
    protected Map<String, Long> removeProfileCounts(InstanceProperties instanceProperties)
    {
        final String methodName = "removeProfileCounts";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeLongMapFromProperty(serviceName,
                                                              OpenMetadataProperty.PROFILE_COUNTS.name,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the profileCounts property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return map of name to long pairs
     */
    protected Map<String, Double> removeProfileDoubles(InstanceProperties instanceProperties)
    {
        final String methodName = "removeProfileDoubles";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeDoubleMapFromProperty(serviceName,
                                                                OpenMetadataProperty.PROFILE_DOUBLES.name,
                                                                instanceProperties,
                                                                methodName);
        }

        return null;
    }


    /**
     * Extract the valueList property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return list of values
     */
    protected List<String> removeValueList(InstanceProperties instanceProperties)
    {
        final String methodName = "removeValueList";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataProperty.VALUE_LIST.name,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the valueCount property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return map of name to integer pairs
     */
    protected Map<String, Integer> removeValueCount(InstanceProperties instanceProperties)
    {
        final String methodName = "removeValueCount";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeIntegerMapFromProperty(serviceName,
                                                                 OpenMetadataProperty.VALUE_COUNT.name,
                                                                 instanceProperties,
                                                                 methodName);
        }

        return null;
    }


    /**
     * Extract and delete the valueRangeFrom property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeValueRangeFrom(InstanceProperties instanceProperties)
    {
        final String methodName = "removeValueRangeFrom";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.VALUE_RANGE_FROM.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the valueRangeTo property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeValueRangeTo(InstanceProperties instanceProperties)
    {
        final String methodName = "removeValueRangeTo";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.VALUE_RANGE_TO.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the averageValue property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeAverageValue(InstanceProperties instanceProperties)
    {
        final String methodName = "removeAverageValue";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.AVERAGE_VALUE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the dataSourceProperties property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return map of name value pairs
     */
    protected Map<String, String> removeResourceProperties(InstanceProperties instanceProperties)
    {
        final String methodName = "removeDataSourceProperties";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                OpenMetadataProperty.RESOURCE_PROPERTIES.name,
                                                                instanceProperties,
                                                                methodName);
        }

        return null;
    }


    /**
     * Extract and delete the size property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return int property or 0
     */
    protected int removeSize(InstanceProperties instanceProperties)
    {
        final String methodName = "removeSize";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeIntProperty(serviceName,
                                                      OpenMetadataProperty.SIZE.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the encoding property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeEncoding(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeEncoding";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.ENCODING.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the parameterType property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeParameterType(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeParameterType";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.PARAMETER_TYPE.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }



    /**
     * Extract and delete the qualityDimension standing property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeQualityDimension(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeQualityDimension";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.QUALITY_DIMENSION.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }



    /**
     * Extract and delete the qualityScore property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return int property or 0
     */
    protected int removeQualityScore(InstanceProperties instanceProperties)
    {
        final String methodName = "removeQualityScore";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeIntProperty(serviceName,
                                                      OpenMetadataProperty.QUALITY_SCORE.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the relatedEntityGUID standing property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeRelatedEntityGUID(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeRelatedEntityGUID";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.RELATED_ENTITY_GUID.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the relatedEntityGUID standing property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeRelationshipTypeName(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeRelationshipTypeName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.RELATIONSHIP_TYPE_NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the relationshipProperties property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return map of name-value pairs
     */
    protected Map<String, String> removeRelationshipProperties(InstanceProperties instanceProperties)
    {
        final String methodName = "removeRelationshipProperties";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                OpenMetadataProperty.RELATIONSHIP_PROPERTIES.name,
                                                                instanceProperties,
                                                                methodName);
        }

        return null;
    }


    /**
     * Extract and delete the discoveryActivity standing property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeSurveyActivity(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeSurveyActivity";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.ACTION_SOURCE_NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the actionRequested standing property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeActionRequested(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeActionRequested";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.ACTION_REQUESTED.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the actionProperties property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return map of name-value pairs
     */
    protected Map<String, String> removeActionProperties(InstanceProperties instanceProperties)
    {
        final String methodName = "removeActionProperties";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                OpenMetadataProperty.ACTION_PROPERTIES.name,
                                                                instanceProperties,
                                                                methodName);
        }

        return null;
    }


    /**
     * Extract and delete the informalTerm standing property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeInformalTerm(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeInformalTerm";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.INFORMAL_TERM.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the candidateGlossaryTermGUIDs property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return list of guids
     */
    protected List<String> removeCandidateGlossaryTermGUIDs(InstanceProperties instanceProperties)
    {
        final String methodName = "removeCandidateGlossaryTermGUIDs";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataProperty.CANDIDATE_GLOSSARY_TERM_GUIDS.name,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the informalTopic standing property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeInformalTopic(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeInformalTopic";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataProperty.INFORMAL_CATEGORY.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the candidateGlossaryCategoryGUIDs property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return list of guids
     */
    protected List<String> removeCandidateGlossaryCategoryGUIDs(InstanceProperties instanceProperties)
    {
        final String methodName = "removeCandidateGlossaryCategoryGUIDs";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataProperty.CANDIDATE_GLOSSARY_CATEGORY_GUIDS.name,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }
}