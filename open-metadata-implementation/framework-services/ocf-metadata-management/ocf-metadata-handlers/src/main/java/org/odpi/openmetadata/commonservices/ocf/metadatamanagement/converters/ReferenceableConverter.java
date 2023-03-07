/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementClassification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Referenceable;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * ReferenceableConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a ReferenceableHeader bean.
 */
public class ReferenceableConverter extends ElementHeaderConverter
{
    /**
     * Constructor captures the initial content
     *
     * @param entity properties to convert
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName called server
     */
    public ReferenceableConverter(EntityDetail         entity,
                                  OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName,
                                  String               serverName)
    {
        super(entity, repositoryHelper, serviceName, serverName);
    }


    /**
     * Constructor captures the initial content with relationship
     *
     * @param entity properties to convert
     * @param relationship properties to convert
     * @param repositoryHelper helper object to parse entity/relationship
     * @param serviceName name of this component
     * @param serverName called server
     */
    public ReferenceableConverter(EntityDetail         entity,
                                  Relationship         relationship,
                                  OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName,
                                  String               serverName)
    {
        super(entity, relationship, repositoryHelper, serviceName, serverName);
    }


    /**
     * Set up the bean to convert.
     *
     * @param bean output bean
     */
    protected void updateBean(Referenceable bean)
    {
        if (entity != null)
        {
            super.updateBean(bean);

            bean.setClassifications(this.getClassificationsFromEntity());
        }
    }


    /**
     * Return the converted bean.
     *
     * @return bean populated with properties from the entity supplied in the constructor
     */
    public Referenceable getBean()
    {
        Referenceable  bean = null;

        if (entity != null)
        {
            bean = new Referenceable();

            updateBean(bean);
        }

        return bean;
    }


    /**
     * Extract the classifications from the entity.
     *
     * @return list of bean classifications
     */
    private List<ElementClassification> getClassificationsFromEntity()
    {
        List<ElementClassification> classifications = null;

        if (entity != null)
        {
            List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> entityClassifications = entity.getClassifications();

            if (entityClassifications != null)
            {
                classifications = new ArrayList<>();

                for (org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification entityClassification : entityClassifications)
                {
                    if (entityClassification != null)
                    {
                        ElementClassification beanClassification = new ElementClassification();

                        beanClassification.setClassificationName(entityClassification.getName());
                        beanClassification.setClassificationProperties(repositoryHelper.getInstancePropertiesAsMap(entityClassification.getProperties()));
                    }
                }
            }
        }

        return classifications;
    }


    /**
     * Extract the properties for the requested classification from the entity.
     *
     * @param classificationName name of classification
     * @return list of properties for the named classification
     */
    protected InstanceProperties getClassificationProperties(String  classificationName)
    {
        if (entity != null)
        {
            List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> entityClassifications = entity.getClassifications();

            if (entityClassifications != null)
            {
                for (org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification entityClassification : entityClassifications)
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
        }

        return null;
    }
}
