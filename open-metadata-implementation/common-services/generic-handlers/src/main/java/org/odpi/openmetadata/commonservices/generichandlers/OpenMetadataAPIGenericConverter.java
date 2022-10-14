/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementClassification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementOrigin;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementOriginCategory;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementVersions;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * OpenMetadataAPIGenericConverter provides the generic methods for the bean converters used to provide translation between
 * specific Open Metadata API beans and the repository services API beans.
 *
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
     * @param relationship relationship containing the properties
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
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
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
     * a connection bean which made up of 3 entities (Connection, ConnectorType and Endpoint) plus the
     * relationships between them.  The relationships may be omitted if they do not have any properties.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the content of the bean
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
     * Throw an exception to indicate that a retrieved entity proxy is missing critical information.
     *
     * @param relationship the relationship with a bad entity proxy
     * @param end number of the end where the proxy is stored
     * @param entityProxy the entity proxy with the bad values
     * @param methodName calling method
     * @throws PropertyServerException an invalid instance has been returned from the metadata repositories
     */
    protected void handleBadEntityProxy(Relationship relationship,
                                        int          end,
                                        EntityProxy  entityProxy,
                                        String       methodName) throws PropertyServerException
    {
        String entityProxyString = "<null>";

        if (entityProxy != null)
        {
            entityProxyString = entityProxy.toString();
        }

        throw new PropertyServerException(GenericHandlersErrorCode.BAD_ENTITY_PROXY.getMessageDefinition(relationship.getGUID(),
                                                                                                         methodName,
                                                                                                         serviceName,
                                                                                                         Integer.toString(end),
                                                                                                         entityProxyString),
                                          this.getClass().getName(),
                                          methodName);
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
    public ElementHeader getMetadataElementHeader(Class<B> beanClass,
                                                  InstanceHeader header,
                                                  List<Classification> entityClassifications,
                                                  String methodName) throws PropertyServerException
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
                case UNKNOWN:
                    return ElementStatus.UNKNOWN;

                case DRAFT:
                    return ElementStatus.DRAFT;

                case PREPARED:
                    return ElementStatus.PREPARED;

                case PROPOSED:
                    return ElementStatus.PROPOSED;

                case APPROVED:
                    return ElementStatus.APPROVED;

                case REJECTED:
                    return ElementStatus.REJECTED;

                case APPROVED_CONCEPT:
                    return ElementStatus.APPROVED_CONCEPT;

                case UNDER_DEVELOPMENT:
                    return ElementStatus.UNDER_DEVELOPMENT;

                case DEVELOPMENT_COMPLETE:
                    return ElementStatus.DEVELOPMENT_COMPLETE;

                case APPROVED_FOR_DEPLOYMENT:
                    return ElementStatus.APPROVED_FOR_DEPLOYMENT;

                case STANDBY:
                    return ElementStatus.STANDBY;

                case ACTIVE:
                    return ElementStatus.ACTIVE;

                case FAILED:
                    return ElementStatus.FAILED;

                case DISABLED:
                    return ElementStatus.DISABLED;

                case COMPLETE:
                    return ElementStatus.COMPLETE;

                case DEPRECATED:
                    return ElementStatus.DEPRECATED;

                case OTHER:
                    return ElementStatus.OTHER;
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
    public ElementStub getElementStub(Class<B> beanClass,
                                      EntityProxy entityProxy,
                                      String methodName) throws PropertyServerException
    {
        if (entityProxy != null)
        {
            ElementHeader elementHeader = getMetadataElementHeader(beanClass, entityProxy, entityProxy.getClassifications(), methodName);
            ElementStub   elementStub   = new ElementStub(elementHeader);

            elementStub.setUniqueName(repositoryHelper.getStringProperty(serviceName,
                                                                         OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
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
                                                                         OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
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

                    beanClassification.setClassificationName(entityClassification.getName());
                    beanClassification.setClassificationProperties(repositoryHelper.getInstancePropertiesAsMap(entityClassification.getProperties()));

                    beanClassifications.add(beanClassification);
                }
            }

        }

        return beanClassifications;
    }


    /**
     * Convert information from a repository instance into an Open Connector Framework ElementType.
     *
     * @param instanceHeader values from the server
     * @return OCF ElementType object
     */
    protected ElementType getElementType(InstanceAuditHeader instanceHeader)
    {
        ElementType elementType = new ElementType();

        InstanceType instanceType = instanceHeader.getType();

        if (instanceType != null)
        {
            elementType.setTypeId(instanceType.getTypeDefGUID());
            elementType.setTypeName(instanceType.getTypeDefName());
            elementType.setTypeVersion(instanceType.getTypeDefVersion());
            elementType.setTypeDescription(instanceType.getTypeDefDescription());

            List<TypeDefLink> typeDefSuperTypes = instanceType.getTypeDefSuperTypes();

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
            switch (instanceProvenanceType)
            {
                case DEREGISTERED_REPOSITORY:
                    return ElementOriginCategory.DEREGISTERED_REPOSITORY;

                case EXTERNAL_SOURCE:
                    return ElementOriginCategory.EXTERNAL_SOURCE;

                case EXPORT_ARCHIVE:
                    return ElementOriginCategory.EXPORT_ARCHIVE;

                case LOCAL_COHORT:
                    return ElementOriginCategory.LOCAL_COHORT;

                case CONTENT_PACK:
                    return ElementOriginCategory.CONTENT_PACK;

                case CONFIGURATION:
                    return ElementOriginCategory.CONFIGURATION;

                case UNKNOWN:
                    return ElementOriginCategory.UNKNOWN;
            }
        }

        return ElementOriginCategory.UNKNOWN;
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
                                                      OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
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
                                                                OpenMetadataAPIMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
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
            return repositoryHelper.getInstancePropertiesAsMap(instanceProperties);
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
                                                         OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.NAME_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.VERSION_IDENTIFIER_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.KEYWORD_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.TOPIC_TYPE_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the operatingSystem property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeOperatingSystem(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeOperatingSystem";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.OPERATING_SYSTEM_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the operatingSystemPatchLevel property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeOperatingSystemPatchLevel(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeOperatingSystemPatchLevel";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.OPERATING_SYSTEM_PATCH_LEVEL_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the minimumInstances property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return int
     */
    protected int removeMinimumInstances(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeMinimumInstances";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeIntProperty(serviceName,
                                                      OpenMetadataAPIMapper.MINIMUM_INSTANCES_PROPERTY_NAME,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
    }

    /**
     * Extract and delete the maximumInstances property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return int
     */
    protected int removeMaximumInstances(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeMaximumInstances";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeIntProperty(serviceName,
                                                      OpenMetadataAPIMapper.MAXIMUM_INSTANCES_PROPERTY_NAME,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the initials property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeInitials(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeInitials";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.INITIALS_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the givenNames property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeGivenNames(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeGivenNames";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.GIVEN_NAMES_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the surname property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeSurname(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeSurname";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.SURNAME_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }



    /**
     * Extract and delete the fullName property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeFullName(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeFullName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.FULL_NAME_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the preferredLanguage property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removePreferredLanguage(InstanceProperties  instanceProperties)
    {
        final String methodName = "removePreferredLanguage";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.PREFERRED_LANGUAGE_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }



    /**
     * Extract and delete the jobTitle property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeJobTitle(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeJobTitle";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.JOB_TITLE_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }



    /**
     * Extract and delete the employeeNumber property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeEmployeeNumber(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeEmployeeNumber";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.EMPLOYEE_NUMBER_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }



    /**
     * Extract and delete the employeeType property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeEmployeeType(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeEmployeeType";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.EMPLOYEE_TYPE_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.CONTACT_TYPE_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.CONTACT_METHOD_SERVICE_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.CONTACT_METHOD_VALUE_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.MISSION_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.ASSOCIATION_TYPE_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.IDENTIFIER_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
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
                                                         OpenMetadataAPIMapper.URL_PROPERTY_NAME,
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
    protected String removeOrganization(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeOrganization";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.ORGANIZATION_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.REFERENCE_VERSION_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.REFERENCE_ID_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.REFERENCE_ID_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the orderPropertyName property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeOrderPropertyName(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeOrderPropertyName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.ORDER_PROPERTY_NAME_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the membershipRationale property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeMembershipRationale(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeMembershipRationale";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.MEMBERSHIP_RATIONALE_PROPERTY_NAME,
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
                                                                OpenMetadataAPIMapper.MAPPING_PROPERTIES_PROPERTY_NAME,
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
                                                       OpenMetadataAPIMapper.LAST_SYNCHRONIZED_PROPERTY_NAME,
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
            String networkAddress = repositoryHelper.removeStringProperty(serviceName,
                                                                          OpenMetadataAPIMapper.NETWORK_ADDRESS_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName);

            if (networkAddress == null)
            {
                networkAddress = repositoryHelper.removeStringProperty(serviceName,
                                                                       OpenMetadataAPIMapper.NETWORK_ADDRESS_PROPERTY_NAME_DEP,
                                                                       instanceProperties,
                                                                       methodName);
            }

            return networkAddress;
        }

        return null;
    }


    /**
     * Extract and delete the postalAddress property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removePostalAddress(InstanceProperties  instanceProperties)
    {
        final String methodName = "removePostalAddress";

        if (instanceProperties != null)
        {
            String postalAddress = repositoryHelper.removeStringProperty(serviceName,
                                                                          OpenMetadataAPIMapper.POSTAL_ADDRESS_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName);

            if (postalAddress == null)
            {
                postalAddress = repositoryHelper.removeStringProperty(serviceName,
                                                                       OpenMetadataAPIMapper.POSTAL_ADDRESS_PROPERTY_NAME_DEP,
                                                                       instanceProperties,
                                                                       methodName);
            }

            return postalAddress;
        }

        return null;
    }


    /**
     * Extract and delete the "coordinates" property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeCoordinates(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeCoordinates";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.COORDINATES_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the mapProjection property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeMapProjection(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeMapProjection";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.MAP_PROJECTION_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the timeZone property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeTimeZone(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeTimeZone";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.TIME_ZONE_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the level property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeLevel(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeLevel";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.LEVEL_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.PROTOCOL_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.ENCRYPTION_METHOD_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.CONNECTOR_PROVIDER_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.SUPPORTED_ASSET_TYPE_NAME,
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
                                                         OpenMetadataAPIMapper.EXPECTED_DATA_FORMAT,
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
                                                                                  OpenMetadataAPIMapper.CONNECTOR_FRAMEWORK_NAME,
                                                                                  instanceProperties,
                                                                                  methodName);
            if (connectorFrameworkName != null)
            {
                return connectorFrameworkName;
            }
        }

        return OpenMetadataAPIMapper.CONNECTOR_FRAMEWORK_NAME_DEFAULT;
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
                                                                                  OpenMetadataAPIMapper.CONNECTOR_INTERFACE_LANGUAGE,
                                                                                  instanceProperties,
                                                                                  methodName);
            if (connectorInterfaceLanguage != null)
            {
                return connectorInterfaceLanguage;
            }
        }

        return OpenMetadataAPIMapper.CONNECTOR_INTERFACE_LANGUAGE_DEFAULT;
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
                                                              OpenMetadataAPIMapper.CONNECTOR_INTERFACES,
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
                                                         OpenMetadataAPIMapper.TARGET_TECHNOLOGY_SOURCE,
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
                                                         OpenMetadataAPIMapper.TARGET_TECHNOLOGY_NAME,
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
                                                              OpenMetadataAPIMapper.TARGET_TECHNOLOGY_INTERFACES,
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
                                                              OpenMetadataAPIMapper.TARGET_TECHNOLOGY_VERSIONS,
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
                                                              OpenMetadataAPIMapper.RECOGNIZED_ADD_PROPS_PROPERTY_NAME,
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
                                                              OpenMetadataAPIMapper.RECOGNIZED_SEC_PROPS_PROPERTY_NAME,
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
                                                              OpenMetadataAPIMapper.RECOGNIZED_CONFIG_PROPS_PROPERTY_NAME,
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
                                                                OpenMetadataAPIMapper.SECURED_PROPERTIES_PROPERTY_NAME,
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
                                                          OpenMetadataAPIMapper.CONFIGURATION_PROPERTIES_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.USER_ID_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.CLEAR_PASSWORD_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.ENCRYPTED_PASSWORD_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.ASSET_SUMMARY_PROPERTY_NAME,
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
                                                       OpenMetadataAPIMapper.ARGUMENTS_PROPERTY_NAME,
                                                       instanceProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Retrieve the zone membership from the properties of the zone membership classification.
     *
     * @param instanceProperties properties from the classification
     * @return list of zone names
     */
    protected List<String> removeZoneMembership(InstanceProperties instanceProperties)
    {
        final String methodName = "removeZoneMembership";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataAPIMapper.ZONE_MEMBERSHIP_PROPERTY_NAME,
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
                                                              OpenMetadataAPIMapper.ZONE_NAME_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.SUBJECT_AREA_NAME_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }

    /**
     * Retrieve the zone membership from the properties of the zone membership classification.
     *
     * @param instanceProperties properties from the classification
     * @return list of zone names
     */
    protected List<String> getZoneMembership(InstanceProperties instanceProperties)
    {
        final String methodName = "getZoneMembership";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringArrayProperty(serviceName,
                                                           OpenMetadataAPIMapper.ZONE_MEMBERSHIP_PROPERTY_NAME,
                                                           instanceProperties,
                                                           methodName);
        }

        return null;
    }


    /**
     * Extract and delete the owner property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeOwner(InstanceProperties instanceProperties)
    {
        final String methodName = "removeOwner";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.OWNER_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the owner property from the supplied instance properties.
     *
     * @param instanceProperties properties from classification
     * @return string text or null
     */
    protected String getOwner(InstanceProperties instanceProperties)
    {
        final String methodName = "getOwner";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataAPIMapper.OWNER_PROPERTY_NAME,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }



    /**
     * Extract the ownerTypeName property from the supplied instance properties.
     *
     * @param instanceProperties properties from classification
     * @return string text or null
     */
    protected String getOwnerTypeName(InstanceProperties instanceProperties)
    {
        final String methodName = "getOwnerTypeName";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataAPIMapper.OWNER_TYPE_NAME_PROPERTY_NAME,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the ownerPropertyName property from the supplied instance properties.
     *
     * @param instanceProperties properties from classification
     * @return string text or null
     */
    protected String getOwnerPropertyName(InstanceProperties instanceProperties)
    {
        final String methodName = "getOwnerPropertyName";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataAPIMapper.OWNER_PROPERTY_NAME_PROPERTY_NAME,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the ownerType property from the supplied instance properties.
     *
     * @param instanceProperties properties from classification
     * @return ordinal or 0 for not specified
     */
    protected int getOwnerTypeOrdinal(InstanceProperties instanceProperties)
    {
        final String methodName = "getOwnerTypeOrdinal";

        if (instanceProperties != null)
        {
            return repositoryHelper.getEnumPropertyOrdinal(serviceName,
                                                           OpenMetadataAPIMapper.OWNER_TYPE_PROPERTY_NAME,
                                                           instanceProperties,
                                                           methodName);
        }

        return 0;
    }


    /**
     * Extract the ownerType property from the supplied instance properties.
     *
     * @param instanceProperties properties from classification
     * @return ordinal or 0 for not specified
     */
    protected int removeOwnerTypeOrdinal(InstanceProperties instanceProperties)
    {
        final String methodName = "removeOwnerTypeOrdinal";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                              OpenMetadataAPIMapper.OWNER_TYPE_PROPERTY_NAME,
                                                              instanceProperties,
                                                              methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the ownerPropertyName property from the supplied instance properties.
     *
     * @param instanceProperties properties from classification
     * @return string
     */
    protected String removeOwnerPropertyName(InstanceProperties instanceProperties)

    {
        final String methodName = "removeClassificationPropertyName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.OWNER_PROPERTY_NAME_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the ownerTypeName property from the supplied instance properties.
     *
     * @param instanceProperties properties from classification
     * @return string
     */
    protected String removeOwnerTypeName(InstanceProperties instanceProperties)

    {
        final String methodName = "removeTypePropertyName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.OWNER_TYPE_NAME_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the roleTypeName property from the supplied instance properties.
     *
     * @param instanceProperties properties from classification
     * @return string
     */
    protected String removeRoleTypeName(InstanceProperties instanceProperties)

    {
        final String methodName = "removeRoleTypeName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.ROLE_TYPE_NAME_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.DISTINGUISHED_NAME_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the "groups" property from the supplied instance properties.
     *
     * @param instanceProperties properties from classification
     * @return string map or null
     */
    protected List<String> getGroups(InstanceProperties  instanceProperties)
    {
        final String methodName = "getGroups";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringArrayProperty(serviceName,
                                                           OpenMetadataAPIMapper.GROUPS_PROPERTY_NAME,
                                                           instanceProperties,
                                                           methodName);
        }

        return null;
    }


    /**
     * Extract the securityLabels property from the supplied instance properties.
     *
     * @param instanceProperties properties from classification
     * @return string map or null
     */
    protected List<String> getSecurityLabels(InstanceProperties  instanceProperties)
    {
        final String methodName = "getSecurityLabels";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringArrayProperty(serviceName,
                                                           OpenMetadataAPIMapper.SECURITY_LABELS_PROPERTY_NAME,
                                                           instanceProperties,
                                                           methodName);
        }

        return null;
    }


    /**
     * Extract the securityProperties property from the supplied instance properties.
     *
     * @param instanceProperties properties from classification
     * @return string map or null
     */
    protected Map<String, String> getSecurityProperties(InstanceProperties  instanceProperties)
    {
        final String methodName = "getSecurityProperties";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringMapFromProperty(serviceName,
                                                             OpenMetadataAPIMapper.SECURITY_PROPERTIES_PROPERTY_NAME,
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
    protected int removeKarmaPoints(InstanceProperties instanceProperties)
    {
        final String methodName = "removeKarmaPoints";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeIntProperty(serviceName,
                                                      OpenMetadataAPIMapper.KARMA_POINTS_PROPERTY_NAME,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
    }


    /**
     * Extract the organizationGUID property from the supplied instance properties.
     *
     * @param instanceProperties properties from classification
     * @return string text or null
     */
    protected String getOriginOrganizationGUID(InstanceProperties  instanceProperties)
    {
        final String methodName = "getOriginOrganizationGUID";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataAPIMapper.ORGANIZATION_PROPERTY_NAME,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the businessCapabilityGUID property from the supplied instance properties.
     *
     * @param instanceProperties properties from classification
     * @return string text or null
     */
    protected String getOriginBusinessCapabilityGUID(InstanceProperties  instanceProperties)
    {
        final String methodName = "getOriginBusinessCapabilityGUID";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataAPIMapper.BUSINESS_CAPABILITY_PROPERTY_NAME,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the otherOriginValues property from the supplied instance properties.
     *
     * @param instanceProperties properties from classification
     * @return string map or null
     */
    protected Map<String, String> getOtherOriginValues(InstanceProperties  instanceProperties)
    {
        final String methodName = "getOtherOriginValues";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringMapFromProperty(serviceName,
                                                             OpenMetadataAPIMapper.OTHER_ORIGIN_VALUES_PROPERTY_NAME,
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
    protected Date removeSourceCreateTime(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeSourceCreateTime";

        if (instanceProperties != null)
        {
            Date createTime1 = repositoryHelper.removeDateProperty(serviceName,
                                                                   OpenMetadataAPIMapper.SOURCE_CREATE_TIME_PROPERTY_NAME,
                                                                   instanceProperties,
                                                                   methodName);
            Date createTime2 = repositoryHelper.removeDateProperty(serviceName,
                                                                   OpenMetadataAPIMapper.SOURCE_CREATE_TIME_PROPERTY_NAME_DEP,
                                                                   instanceProperties,
                                                                   methodName);
            return createTime1 == null ? createTime2 : createTime1;
        }

        return null;
    }


    /**
     * Extract and delete the sourceUpdateTime property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return date or null
     */
    protected Date removeSourceUpdateTime(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeSourceUpdateTime";

        if (instanceProperties != null)
        {
            Date modifiedTime1 = repositoryHelper.removeDateProperty(serviceName,
                                                                     OpenMetadataAPIMapper.SOURCE_UPDATE_TIME_PROPERTY_NAME,
                                                                     instanceProperties,
                                                                     methodName);
            Date modifiedTime2 = repositoryHelper.removeDateProperty(serviceName,
                                                                     OpenMetadataAPIMapper.SOURCE_UPDATE_TIME_PROPERTY_NAME_DEP,
                                                                     instanceProperties,
                                                                     methodName);
            return modifiedTime1 == null ? modifiedTime2 : modifiedTime1;
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
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataAPIMapper.PATH_NAME_PROPERTY_NAME,
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
                                                                   OpenMetadataAPIMapper.STORE_CREATE_TIME_PROPERTY_NAME,
                                                                   instanceProperties,
                                                                   methodName);
            Date createTime2 = repositoryHelper.removeDateProperty(serviceName,
                                                                   OpenMetadataAPIMapper.STORE_CREATE_TIME_PROPERTY_NAME_DEP,
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
                                                                     OpenMetadataAPIMapper.STORE_UPDATE_TIME_PROPERTY_NAME,
                                                                     instanceProperties,
                                                                     methodName);
            Date modifiedTime2 = repositoryHelper.removeDateProperty(serviceName,
                                                                     OpenMetadataAPIMapper.STORE_UPDATE_TIME_PROPERTY_NAME_DEP,
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
                                                      OpenMetadataAPIMapper.ENCODING_TYPE_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.ENCODING_LANGUAGE_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.ENCODING_DESCRIPTION_PROPERTY_NAME,
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
                                                             OpenMetadataAPIMapper.ENCODING_PROPERTIES_PROPERTY_NAME,
                                                             instanceProperties,
                                                             methodName);
        }

        return null;
    }


    /**
     * Extract and delete the database type property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String removeDatabaseType(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeDatabaseType";

        if (instanceProperties != null)
        {
            String type1 = repositoryHelper.removeStringProperty(serviceName,
                                                                 OpenMetadataAPIMapper.DATABASE_TYPE_PROPERTY_NAME,
                                                                 instanceProperties,
                                                                 methodName);
            String type2 = repositoryHelper.removeStringProperty(serviceName,
                                                                 OpenMetadataAPIMapper.DATABASE_TYPE_PROPERTY_NAME_DEP,
                                                                 instanceProperties,
                                                                 methodName);
            return type1 == null ? type2 : type1;
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
            String version1 = repositoryHelper.removeStringProperty(serviceName,
                                                                    OpenMetadataAPIMapper.DATABASE_VERSION_PROPERTY_NAME,
                                                                    instanceProperties,
                                                                    methodName);
            String version2 = repositoryHelper.removeStringProperty(serviceName,
                                                                    OpenMetadataAPIMapper.DATABASE_VERSION_PROPERTY_NAME_DEP,
                                                                    instanceProperties,
                                                                    methodName);
            return version1 == null ? version2 : version1;
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
                                                         OpenMetadataAPIMapper.DATABASE_INSTANCE_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.DATABASE_IMPORTED_FROM_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.FILE_TYPE_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.FORMAT_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.ENCRYPTION_PROPERTY_NAME,
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
            String type = repositoryHelper.removeStringProperty(serviceName,
                                                                OpenMetadataAPIMapper.DEPLOYED_IMPLEMENTATION_TYPE_PROPERTY_NAME,
                                                                instanceProperties,
                                                                methodName);
            if (type == null)
            {
                type = repositoryHelper.removeStringProperty(serviceName,
                                                             OpenMetadataAPIMapper.DEPLOYED_IMPLEMENTATION_TYPE_PROPERTY_NAME_DEP,
                                                             instanceProperties,
                                                             methodName);
            }

            return type;
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
                                                                OpenMetadataAPIMapper.CAPABILITY_TYPE_PROPERTY_NAME,
                                                                instanceProperties,
                                                                methodName);
            if (type == null)
            {
                type = repositoryHelper.removeStringProperty(serviceName,
                                                             OpenMetadataAPIMapper.CAPABILITY_TYPE_PROPERTY_NAME_DEP1,
                                                             instanceProperties,
                                                             methodName);
            }

            if (type == null)
            {
                type = repositoryHelper.removeStringProperty(serviceName,
                                                             OpenMetadataAPIMapper.CAPABILITY_TYPE_PROPERTY_NAME_DEP2,
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
            String version1 = repositoryHelper.removeStringProperty(serviceName,
                                                                    OpenMetadataAPIMapper.CAPABILITY_VERSION_PROPERTY_NAME,
                                                                    instanceProperties,
                                                                    methodName);
            String version2 = repositoryHelper.removeStringProperty(serviceName,
                                                                    OpenMetadataAPIMapper.CAPABILITY_VERSION_PROPERTY_NAME_DEP,
                                                                    instanceProperties,
                                                                    methodName);
            return version1 == null ? version2 : version1;
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
                                                         OpenMetadataAPIMapper.PATCH_LEVEL_PROPERTY_NAME,
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
                                                          OpenMetadataAPIMapper.IS_DEPRECATED_PROPERTY_NAME,
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
                                                          OpenMetadataAPIMapper.IS_DEFAULT_VALUE_PROPERTY_NAME,
                                                          instanceProperties,
                                                          methodName);
        }

        return false;
    }


    /**
     * Extract and delete the anchorGUID property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String removeAnchorGUID(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeAnchorGUID";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.ANCHOR_GUID_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the anchorGUID property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String getAnchorGUID(InstanceProperties  instanceProperties)
    {
        final String methodName = "getAnchorGUID";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataAPIMapper.ANCHOR_GUID_PROPERTY_NAME,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
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
                                                         OpenMetadataAPIMapper.DATA_TYPE_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.DEFAULT_VALUE_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.FIXED_VALUE_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.QUERY_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.QUERY_ID_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.VERSION_NUMBER_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.ID_PROPERTY_NAME,
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
                                                       OpenMetadataAPIMapper.CREATED_TIME_PROPERTY_NAME,
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
                                                       OpenMetadataAPIMapper.LAST_MODIFIED_TIME_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.LAST_MODIFIER_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.AUTHOR_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.ENCODING_STANDARD_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.NAMESPACE_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.POSITION_PROPERTY_NAME,
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
                                                   OpenMetadataAPIMapper.POSITION_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.MIN_CARDINALITY_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.MAX_CARDINALITY_PROPERTY_NAME,
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
                                                          OpenMetadataAPIMapper.ALLOWS_DUPLICATES_PROPERTY_NAME,
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
                                                          OpenMetadataAPIMapper.ORDERED_VALUES_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.DEFAULT_VALUE_OVERRIDE_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.MIN_LENGTH_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.LENGTH_PROPERTY_NAME,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the precision/significantDigits property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return integer - default 0
     */
    protected int removePrecision(InstanceProperties  instanceProperties)
    {
        final String methodName = "removePrecision";

        if (instanceProperties != null)
        {
            int retrievedValue = repositoryHelper.removeIntProperty(serviceName,
                                                                    OpenMetadataAPIMapper.PRECISION_PROPERTY_NAME,
                                                                    instanceProperties,
                                                                    methodName);

            if (retrievedValue == 0)
            {
                retrievedValue = repositoryHelper.removeIntProperty(serviceName,
                                                                    OpenMetadataAPIMapper.SIGNIFICANT_DIGITS_PROPERTY_NAME,
                                                                    instanceProperties,
                                                                    methodName);
            }

            return retrievedValue;
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
                                                          OpenMetadataAPIMapper.IS_NULLABLE_PROPERTY_NAME,
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
                                                          OpenMetadataAPIMapper.REQUIRED_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.NATIVE_CLASS_PROPERTY_NAME,
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
                                                              OpenMetadataAPIMapper.ALIASES_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.GUARD_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.FORMULA_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.FORMULA_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the implementationLanguage property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String getImplementationLanguage(InstanceProperties  instanceProperties)
    {
        final String methodName = "getImplementationLanguage";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataAPIMapper.IMPLEMENTATION_LANGUAGE_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.IMPLEMENTATION_LANGUAGE_PROPERTY_NAME,
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
    protected String removeSource(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeSource";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.USAGE_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.USAGE_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.LANGUAGE_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.SUMMARY_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.SUMMARY_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.ABBREVIATION_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.ABBREVIATION_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.EXAMPLES_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.TITLE_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }



    /**
     * Extract the "pronouns" property from the supplied instance properties.
     *
     * @param instanceProperties properties from governance entities
     * @return string property or null
     */
    protected String removePronouns(InstanceProperties instanceProperties)
    {
        final String methodName = "removePronouns";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.PRONOUNS_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.TEXT_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the priority property from the supplied instance properties.
     *
     * @param instanceProperties properties from governance entities
     * @return string property or null
     */
    protected String removePriority(InstanceProperties instanceProperties)
    {
        final String methodName = "removePriority";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.PRIORITY_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.PRIORITY_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.HEAD_COUNT_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.SCOPE_PROPERTY_NAME,
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
                                                              OpenMetadataAPIMapper.IMPLICATIONS_PROPERTY_NAME,
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
                                                              OpenMetadataAPIMapper.OUTCOMES_PROPERTY_NAME,
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
                                                              OpenMetadataAPIMapper.RESULTS_PROPERTY_NAME,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the businessImperatives property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string list or null
     */
    protected List<String> removeBusinessImperatives(InstanceProperties instanceProperties)
    {
        final String methodName = "removeBusinessImperatives";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataAPIMapper.BUSINESS_IMPERATIVES_PROPERTY_NAME,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the jurisdiction property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeJurisdiction(InstanceProperties instanceProperties)
    {
        final String methodName = "removeJurisdiction";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.JURISDICTION_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.DETAILS_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the rational property from the supplied instance properties.
     *
     * @param instanceProperties properties from relationship
     * @return string text or null
     */
    protected String getRationale(InstanceProperties instanceProperties)
    {
        final String methodName = "removeRationale";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataAPIMapper.RATIONALE_PROPERTY_NAME,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract and delete the implementationDescription property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeImplementationDescription(InstanceProperties instanceProperties)
    {
        final String methodName = "removeImplementationDescription";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.IMPLEMENTATION_DESCRIPTION_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.CRITERIA_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.DOMAIN_IDENTIFIER_PROPERTY_NAME,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the level identifier property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return integer = default is 0 which is ALL
     */
    protected int removeLevelIdentifier(InstanceProperties instanceProperties)

    {
        final String methodName = "removeLevelIdentifier";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeIntProperty(serviceName,
                                                      OpenMetadataAPIMapper.LEVEL_IDENTIFIER_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.MEASUREMENT_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.TARGET_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the classificationName property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string
     */
    protected String removeClassificationName(InstanceProperties instanceProperties)

    {
        final String methodName = "removeClassificationName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.CLASSIFICATION_NAME_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the classificationPropertyName property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string
     */
    protected String removeClassificationPropertyName(InstanceProperties instanceProperties)

    {
        final String methodName = "removeClassificationPropertyName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.CLASSIFICATION_PROPERTY_NAME_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.PROCESSING_ENGINE_USER_ID_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.REQUEST_TYPE_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.REQUEST_PARAMETERS_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.EXECUTOR_ENGINE_GUID_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.EXECUTOR_ENGINE_NAME_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.PROCESS_NAME_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_GUID_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }



    /**
     * Extract and delete the producedGuards property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return array of guards
     */
    protected List<String> removeProducedGuards(InstanceProperties instanceProperties)

    {
        final String methodName = "removeProducedGuards";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataAPIMapper.PRODUCED_GUARDS_PROPERTY_NAME,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the mandatoryGuards property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return array of guards
     */
    protected List<String> removeMandatoryGuards(InstanceProperties instanceProperties)

    {
        final String methodName = "removeMandatoryGuards";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataAPIMapper.MANDATORY_GUARDS_PROPERTY_NAME,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the receivedGuards property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return array of guards
     */
    protected List<String> removeReceivedGuards(InstanceProperties instanceProperties)

    {
        final String methodName = "removeReceivedGuards";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataAPIMapper.RECEIVED_GUARDS_PROPERTY_NAME,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the completionGuards property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return array of guards
     */
    protected List<String> removeCompletionGuards(InstanceProperties instanceProperties)

    {
        final String methodName = "removeCompletionGuards";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataAPIMapper.COMPLETION_GUARDS_PROPERTY_NAME,
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
                                                       OpenMetadataAPIMapper.START_DATE_PROPERTY_NAME,
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
                                                       OpenMetadataAPIMapper.PLANNED_END_DATE_PROPERTY_NAME,
                                                       instanceProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the creationTime property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return date
     */
    protected Date removeCreationTime(InstanceProperties instanceProperties)

    {
        final String methodName = "removeCreationTime";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeDateProperty(serviceName,
                                                       OpenMetadataAPIMapper.CREATION_TIME_PROPERTY_NAME,
                                                       instanceProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the dueTime property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return date
     */
    protected Date removeDueTime(InstanceProperties instanceProperties)

    {
        final String methodName = "removeDueTime";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeDateProperty(serviceName,
                                                       OpenMetadataAPIMapper.DUE_TIME_PROPERTY_NAME,
                                                       instanceProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the completionTime property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return date
     */
    protected Date removeCompletionTime(InstanceProperties instanceProperties)

    {
        final String methodName = "removeCompletionTime";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeDateProperty(serviceName,
                                                       OpenMetadataAPIMapper.COMPLETION_TIME_PROPERTY_NAME,
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
                                                       OpenMetadataAPIMapper.COMPLETION_DATE_PROPERTY_NAME,
                                                       instanceProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the status property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return String text or null
     */
    protected String removeStatus(InstanceProperties instanceProperties)

    {
        final String methodName = "removeStatus";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.STATUS_PROPERTY_NAME,
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
                                                       OpenMetadataAPIMapper.REQUEST_SOURCE_NAME_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.ACTION_TARGET_NAME_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.ORIGIN_GOVERNANCE_SERVICE_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.ORIGIN_GOVERNANCE_ENGINE_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the licenseGUID property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return String text or null
     */
    protected String getLicenseGUID(InstanceProperties instanceProperties)

    {
        final String methodName = "getLicenseGUID";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataAPIMapper.LICENSE_GUID_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.CERTIFICATE_GUID_PROPERTY_NAME,
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
                                                    OpenMetadataAPIMapper.START_PROPERTY_NAME,
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
                                                    OpenMetadataAPIMapper.END_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.CONDITIONS_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.CUSTODIAN_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.CERTIFIED_BY_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.RECIPIENT_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.LICENSED_BY_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.LICENSEE_PROPERTY_NAME,
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
    protected String removePreferredValue(InstanceProperties instanceProperties)
    {
        final String methodName = "removePreferredValue";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.PREFERRED_VALUE_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
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
                                                       OpenMetadataAPIMapper.IS_STRICT_REQUIREMENT_PROPERTY_NAME,
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
                                                   OpenMetadataAPIMapper.CONFIDENCE_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.STEWARD_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.STEWARD_TYPE_NAME_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.STEWARD_PROPERTY_NAME_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.NOTES_PROPERTY_NAME,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the pointType property from the supplied instance properties.
     *
     * @param instanceProperties properties from classification
     * @return string text or null
     */
    protected String getPointType(InstanceProperties  instanceProperties)
    {
        final String methodName = "getPointType";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataAPIMapper.POINT_TYPE_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.ASSOCIATION_DESCRIPTION_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.SYMBOLIC_NAME_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.IMPLEMENTATION_VALUE_PROPERTY_NAME,
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
                                                             OpenMetadataAPIMapper.ADDITIONAL_VALUES_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.COMMENT_TEXT_PROPERTY_NAME,
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
                                                       OpenMetadataAPIMapper.IS_PUBLIC_PROPERTY_NAME,
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
                                                          OpenMetadataAPIMapper.IS_PUBLIC_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.REVIEW_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.TAG_NAME_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.TAG_DESCRIPTION_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the executionDate property from the supplied instance properties.
     *
     * @param instanceProperties properties from discovery analysis report entities
     * @return string property or null
     */
    protected Date removeExecutionDate(InstanceProperties instanceProperties)
    {
        final String methodName = "removeExecutionDate";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeDateProperty(serviceName,
                                                       OpenMetadataAPIMapper.EXECUTION_DATE_PROPERTY_NAME,
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
                                                                OpenMetadataAPIMapper.ANALYSIS_PARAMS_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.ANALYSIS_STEP_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.ANNOTATION_TYPE_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.CONFIDENCE_LEVEL_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.ANNOTATION_TYPE_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.EXPLANATION_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.JSON_PROPERTIES_PROPERTY_NAME,
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
                                                       OpenMetadataAPIMapper.REVIEW_DATE_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.STEWARD_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.COMMENT_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.SCHEMA_NAME_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.SCHEMA_TYPE_PROPERTY_NAME,
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
                                                                OpenMetadataAPIMapper.CANDIDATE_CLASSIFICATIONS_PROPERTY_NAME,
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
                                                              OpenMetadataAPIMapper.CANDIDATE_DATA_CLASS_GUIDS_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.INFERRED_DATA_TYPE_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.INFERRED_FORMAT_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.INFERRED_LENGTH_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.INFERRED_PRECISION_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.INFERRED_SCALE_PROPERTY_NAME,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
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
                                                                OpenMetadataAPIMapper.PROFILE_PROPERTIES_PROPERTY_NAME,
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
                                                                 OpenMetadataAPIMapper.PROFILE_FLAGS_PROPERTY_NAME,
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
                                                              OpenMetadataAPIMapper.PROFILE_COUNTS_PROPERTY_NAME,
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
                                                              OpenMetadataAPIMapper.VALUE_LIST_PROPERTY_NAME,
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
                                                                 OpenMetadataAPIMapper.VALUE_COUNT_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.VALUE_RANGE_FROM_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.VALUE_RANGE_TO_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.AVERAGE_VALUE_PROPERTY_NAME,
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
    protected Map<String, String> removeDataSourceProperties(InstanceProperties instanceProperties)
    {
        final String methodName = "removeDataSourceProperties";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                OpenMetadataAPIMapper.DATA_SOURCE_PROPERTIES_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.DS_PHYSICAL_SIZE_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.ENCODING_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.PARAMETER_TYPE_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.QUALITY_DIMENSION_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.QUALITY_SCORE_PROPERTY_NAME,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
    }



    /**
     * Extract and delete the duplicateAnchorGUID standing property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeDuplicateAnchorGUID(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeDuplicateAnchorGUID";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.DUPLICATE_ANCHOR_GUID_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the divergentPropertyNames property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return list of values
     */
    protected List<String> removeDivergentPropertyNames(InstanceProperties instanceProperties)
    {
        final String methodName = "removeDivergentPropertyNames";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataAPIMapper.DIVERGENT_PROPERTY_NAMES_PROPERTY_NAME,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the divergentClassificationName standing property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeDivergentClassificationName(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeDivergentClassificationName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.DIVERGENT_CLASSIFICATION_NAME_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the divergentClassificationPropertyNames property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return list of values
     */
    protected List<String> removeDivergentClassificationPropertyNames(InstanceProperties instanceProperties)
    {
        final String methodName = "removeDivergentClassificationPropertyNames";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataAPIMapper.DIVERGENT_CLASSIFICATION_PROPERTY_NAMES_PROPERTY_NAME,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }



    /**
     * Extract and delete the divergentRelationshipGUID standing property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeDivergentRelationshipGUID(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeDivergentRelationshipGUID";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.DIVERGENT_RELATIONSHIP_GUID_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the divergentRelationshipPropertyNames property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return list of values
     */
    protected List<String> removeDivergentRelationshipPropertyNames(InstanceProperties instanceProperties)
    {
        final String methodName = "removeDivergentRelationshipPropertyNames";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataAPIMapper.DIVERGENT_RELATIONSHIP_PROPERTY_NAMES_PROPERTY_NAME,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }



    /**
     * Extract and delete the attachmentGUID standing property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeAttachmentGUID(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeAttachmentGUID";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.ATTACHMENT_GUID_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the duplicateAttachmentGUID standing property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeDuplicateAttachmentGUID(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeDuplicateAttachmentGUID";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.DUPLICATE_ATTACHMENT_GUID_PROPERTY_NAME,
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
    protected String removeRelatedEntityGUID(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeRelatedEntityGUID";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.RELATED_ENTITY_GUID_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.RELATIONSHIP_TYPE_NAME_PROPERTY_NAME,
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
                                                                OpenMetadataAPIMapper.RELATIONSHIP_PROPERTIES_PROPERTY_NAME,
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
    protected String removeDiscoveryActivity(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeDiscoveryActivity";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.DISCOVERY_ACTIVITY_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.ACTION_REQUESTED_PROPERTY_NAME,
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
                                                                OpenMetadataAPIMapper.ACTION_PROPERTIES_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.INFORMAL_TERM_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the candidateGlossaryTermGUIDs property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return map of name-value pairs
     */
    protected List<String> removeCandidateGlossaryTermGUIDs(InstanceProperties instanceProperties)
    {
        final String methodName = "removeCandidateGlossaryTermGUIDs";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataAPIMapper.CANDIDATE_GLOSSARY_TERM_GUIDS_PROPERTY_NAME,
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
                                                         OpenMetadataAPIMapper.INFORMAL_TOPIC_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the candidateGlossaryCategoryGUIDs property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return map of name-value pairs
     */
    protected List<String> removeCandidateGlossaryCategoryGUIDs(InstanceProperties instanceProperties)
    {
        final String methodName = "removeCandidateGlossaryCategoryGUIDs";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataAPIMapper.CANDIDATE_GLOSSARY_CATEGORY_GUIDS_PROPERTY_NAME,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the duplicateAnchorGUIDs property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return map of name-value pairs
     */
    protected List<String> removeDuplicateAnchorGUIDs(InstanceProperties instanceProperties)
    {
        final String methodName = "removeDuplicateAnchorGUIDs";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataAPIMapper.DUPLICATE_ANCHOR_GUIDS_PROPERTY_NAME,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the matchingPropertyNames property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return map of name-value pairs
     */
    protected List<String> removeMatchingPropertyNames(InstanceProperties instanceProperties)
    {
        final String methodName = "removeMatchingPropertyNames";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataAPIMapper.MATCHING_PROPERTY_NAMES_PROPERTY_NAME,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the matchingClassificationNames property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return map of name-value pairs
     */
    protected List<String> removeMatchingClassificationNames(InstanceProperties instanceProperties)
    {
        final String methodName = "removeMatchingClassificationNames";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataAPIMapper.MATCHING_CLASSIFICATION_NAMES_PROPERTY_NAME,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the matchingAttachmentGUIDs property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return map of name-value pairs
     */
    protected List<String> removeMatchingAttachmentGUIDs(InstanceProperties instanceProperties)
    {
        final String methodName = "removeMatchingAttachmentGUIDs";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataAPIMapper.MATCHING_ATTACHMENT_GUIDS_PROPERTY_NAME,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the matchingRelationshipGUIDs property from the supplied instance properties.
     *
     * @param instanceProperties properties from annotation entities
     * @return map of name-value pairs
     */
    protected List<String> removeMatchingRelationshipGUIDs(InstanceProperties instanceProperties)
    {
        final String methodName = "removeMatchingRelationshipGUIDs";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataAPIMapper.MATCHING_RELATIONSHIP_GUIDS_PROPERTY_NAME,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the dataFieldName standing property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeDataFieldName(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeDataFieldName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.DATA_FIELD_NAME_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the dataFieldType standing property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeDataFieldType(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeDataFieldType";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.DATA_FIELD_TYPE_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the dataFieldDescription standing property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string text or null
     */
    protected String removeDataFieldDescription(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeDataFieldDescription";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(serviceName,
                                                         OpenMetadataAPIMapper.DATA_FIELD_DESCRIPTION_PROPERTY_NAME,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the dataFieldAliases property from the supplied instance properties.
     *
     * @param instanceProperties properties from data field entities
     * @return map of name-value pairs
     */
    protected List<String> removeDataFieldAliases(InstanceProperties instanceProperties)
    {
        final String methodName = "removeDataFieldAliases";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringArrayProperty(serviceName,
                                                              OpenMetadataAPIMapper.DATA_FIELD_ALIASES_PROPERTY_NAME,
                                                              instanceProperties,
                                                              methodName);
        }

        return null;
    }

}

