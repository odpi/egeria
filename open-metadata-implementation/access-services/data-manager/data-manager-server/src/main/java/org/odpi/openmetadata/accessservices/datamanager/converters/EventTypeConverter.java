/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.converters;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.EventTypeElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.SchemaTypeElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.TopicElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * EventTypeConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a bean that inherits from EventTypeElement.  It is working with a ComplexSchemaType.
 */
public class EventTypeConverter<B> extends DataManagerOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public EventTypeConverter(OMRSRepositoryHelper repositoryHelper,
                              String               serviceName,
                              String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
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
     * @param queryTargetRelationships list of relationships to schema types that contain data values used to derive the schema type value(s)
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    @Override
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
                                  List<Relationship>   queryTargetRelationships,
                                  String               methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof EventTypeElement)
            {
                if ((schemaRootHeader != null) && (instanceProperties != null))
                {
                    EventTypeElement bean = (EventTypeElement) returnBean;

                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, schemaRootHeader, schemaRootClassifications, methodName));

                    EventTypeProperties eventType = new EventTypeProperties();

                    InstanceProperties propertiesCopy = new InstanceProperties(instanceProperties);

                    eventType.setDisplayName(this.removeDisplayName(propertiesCopy));
                    eventType.setDescription(this.removeDescription(propertiesCopy));
                    eventType.setIsDeprecated(this.removeIsDeprecated(propertiesCopy));
                    eventType.setVersionNumber(this.removeVersionNumber(propertiesCopy));
                    eventType.setAuthor(this.removeAuthor(propertiesCopy));
                    eventType.setUsage(this.removeUsage(propertiesCopy));
                    eventType.setEncodingStandard(this.removeEncodingStandard(propertiesCopy));
                    eventType.setNamespace(this.removeNamespace(propertiesCopy));
                    eventType.setAdditionalProperties(this.removeAdditionalProperties(propertiesCopy));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    eventType.setExtendedProperties(this.getRemainingExtendedProperties(propertiesCopy));

                    bean.setProperties(eventType);

                    bean.setAttributeCount(attributeCount);

                    return returnBean;
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }
            }
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }
    /**
     * Using the supplied entity, return a new instance of the bean.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        String       methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof EventTypeProperties)
            {
                EventTypeElement    bean            = (EventTypeElement) returnBean;
                EventTypeProperties  eventTypeProperties = new EventTypeProperties();

                if (entity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, entity, methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                    eventTypeProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    eventTypeProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    eventTypeProperties.setDisplayName(this.removeName(instanceProperties));
                    eventTypeProperties.setDescription(this.removeDescription(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    eventTypeProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    eventTypeProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setProperties(eventTypeProperties);
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }
            }

            return returnBean;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }
}
