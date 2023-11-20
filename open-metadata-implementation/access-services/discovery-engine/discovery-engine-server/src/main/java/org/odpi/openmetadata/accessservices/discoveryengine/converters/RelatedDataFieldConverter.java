/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.converters;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.discovery.properties.DataField;
import org.odpi.openmetadata.frameworks.discovery.properties.DataFieldLink;
import org.odpi.openmetadata.frameworks.discovery.properties.RelatedDataField;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;

/**
 * DataFieldConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a DataField bean.  It should be passed the data field entity and all relationships linked to it.
 */
public class RelatedDataFieldConverter<B> extends DiscoveryEngineOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName this server
     */
    public RelatedDataFieldConverter(OMRSRepositoryHelper repositoryHelper,
                                     String               serviceName,
                                     String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
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

        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof RelatedDataField)
            {
                RelatedDataField bean = (RelatedDataField) returnBean;
                DataField dataField = new DataField();
                DataFieldLink dataFieldLink = new DataFieldLink();

                dataField.setElementHeader(super.getMetadataElementHeader(beanClass, entity, methodName));
                dataFieldLink.setElementHeader(super.getMetadataElementHeader(beanClass, relationship, null, methodName));

                InstanceProperties instanceProperties = null;

                /*
                 * The initial set of values come from the entity.
                 */
                if (entity != null)
                {
                    instanceProperties = new InstanceProperties(entity.getProperties());

                    dataField.setDataFieldName(this.removeDataFieldName(instanceProperties));
                    dataField.setDataFieldType(this.removeDataFieldType(instanceProperties));
                    dataField.setDataFieldDescription(this.removeDataFieldDescription(instanceProperties));
                    dataField.setDataFieldNamespace(this.removeDataFieldNamespace(instanceProperties));
                    dataField.setDataFieldAliases(this.removeDataFieldAliases(instanceProperties));
                    dataField.setDataFieldSortOrder(this.removeSortOrder(instanceProperties));
                    dataField.setMinCardinality(this.removeMinCardinality(instanceProperties));
                    dataField.setMaxCardinality(this.removeMaxCardinality(instanceProperties));
                    dataField.setIsNullable(this.removeIsNullable(instanceProperties));
                    dataField.setMinimumLength(this.removeMinimumLength(instanceProperties));
                    dataField.setLength(this.removeLength(instanceProperties));
                    dataField.setPrecision(this.removePrecision(instanceProperties));
                    dataField.setSignificantDigits(this.removeSignificantDigits(instanceProperties));
                    dataField.setDefaultValue(this.removeDefaultValue(instanceProperties));
                    dataField.setIsDeprecated(this.removeIsDeprecated(instanceProperties));
                    dataField.setVersion(this.removeVersion(instanceProperties));
                    dataField.setVersionIdentifier(this.removeVersionIdentifier(instanceProperties));
                    dataField.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    dataField.setTypeName(dataField.getElementHeader().getType().getTypeName());
                    dataField.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));
                }

                if (relationship != null)
                {
                    instanceProperties = new InstanceProperties(relationship.getProperties());

                    dataFieldLink.setRelationshipTypeName(this.removeRelationshipTypeName(instanceProperties));
                    dataFieldLink.setRelationshipEnd(this.removeRelationshipEnd(instanceProperties));
                    dataFieldLink.setName(this.removeName(instanceProperties));
                    dataFieldLink.setDescription(this.removeDescription(instanceProperties));
                    dataFieldLink.setMinCardinality(this.removeMinCardinality(instanceProperties));
                    dataFieldLink.setMaxCardinality(this.removeMaxCardinality(instanceProperties));
                    dataFieldLink.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                }

                bean.setAttachedDataField(dataField);
                bean.setDataFieldLink(dataFieldLink);
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
