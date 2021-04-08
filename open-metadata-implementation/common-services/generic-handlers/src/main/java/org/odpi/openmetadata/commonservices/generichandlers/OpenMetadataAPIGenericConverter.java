/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

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
     * Using the supplied instances, return a new instance of the bean. This is used for beans that have
     * contain a combination of the properties from an entity and a that of a connected relationship.
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
     * as a collection of instances.
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
                                  String               externalSchemaTypeGUID,
                                  B                    externalSchemaType,
                                  B                    mapFromSchemaType,
                                  B                    mapToSchemaType,
                                  List<B>              schemaTypeOptions,
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


    /* ======================================================
     * The methods that follow are used by the subclasses to extract specific properties from the instance properties.
     * They are used vor all properties except enums which need a specific method in the OMAS converters.
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
     * Extract and delete the coordinates property from the supplied instance properties.
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
     * Extract and delete the recognized additional properties property from the supplied instance properties.
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
     * Extract and delete the recognized secured properties property from the supplied instance properties.
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
     * Extract and delete the secured properties property from the supplied instance properties.
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
     * Extract the arguments property from the supplied instance properties.
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
                                                      OpenMetadataAPIMapper.ORGANIZATION_GUID_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.BUSINESS_CAPABILITY_GUID_PROPERTY_NAME,
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


    /* =====================================================
     * Schema Attribute properties
     */


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
     * Extract and delete the significant digits property from the supplied instance properties.
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
     * Extract and delete the aliases property from the supplied instance properties.
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
     * Extract and delete the implications property from the supplied instance properties.
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
     * Extract and delete the outcomes property from the supplied instance properties.
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
     * Extract and delete the results property from the supplied instance properties.
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
     * Extract and delete the description property from the supplied instance properties.
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
                                                      OpenMetadataAPIMapper.CRITERIA_PROPERTY_NAME,
                                                      instanceProperties,
                                                      methodName);
        }

        return 0;
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
     * Extract and delete the requestSourceName property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return date
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
     * @return date
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
     * Extract the conditions property from the supplied instance properties.
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
                                                      OpenMetadataAPIMapper.LICENSE_RECIPIENT_PROPERTY_NAME,
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
                                                   OpenMetadataAPIMapper.VALID_VALUES_CONFIDENCE_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.VALID_VALUES_STEWARD_PROPERTY_NAME,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Extract the notes property from the supplied instance properties.
     *
     * @param instanceProperties properties from ReferenceValueAssignment or ValidValuesMapping relationship
     * @return string text or null
     */
    protected String getNotes(InstanceProperties  instanceProperties)
    {
        final String methodName = "getNotes";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataAPIMapper.VALID_VALUES_NOTES_PROPERTY_NAME,
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
                                                      OpenMetadataAPIMapper.VALID_VALUES_ASSOCIATION_DESCRIPTION_PROPERTY_NAME,
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
     * Extract and delete the encoding standing property from the supplied instance properties.
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
                                                         OpenMetadataAPIMapper.DS_PHYSICAL_ENCODING_PROPERTY_NAME,
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

