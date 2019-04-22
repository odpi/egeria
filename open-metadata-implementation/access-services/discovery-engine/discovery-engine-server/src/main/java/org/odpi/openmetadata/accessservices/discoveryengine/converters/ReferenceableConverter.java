/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.converters;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Classification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Referenceable;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * ReferenceableConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a ReferenceableHeader bean.
 */
class ReferenceableConverter extends ElementHeaderConverter
{
    /**
     * Constructor captures the initial content
     *
     * @param entity properties to convert
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     */
    ReferenceableConverter(EntityDetail         entity,
                           OMRSRepositoryHelper repositoryHelper,
                           String               serviceName)
    {
        super(entity, repositoryHelper, serviceName);
    }


    /**
     * Constructor captures the initial content with relationship
     *
     * @param entity properties to convert
     * @param relationship properties to convert
     * @param repositoryHelper helper object to parse entity/relationship
     * @param serviceName name of this component
     */
    ReferenceableConverter(EntityDetail         entity,
                           Relationship         relationship,
                           OMRSRepositoryHelper repositoryHelper,
                           String               serviceName)
    {
        super(entity, relationship, repositoryHelper, serviceName);
    }


    /**
     * Set up the bean to convert.
     *
     * @param bean output bean
     */
    void updateBean(Referenceable bean)
    {
        if (entity != null)
        {
            super.updateBean(bean);

            bean.setClassifications(this.getClassificationsFromEntity());
        }
    }


    /**
     * Extract the classifications from the entity.
     *
     * @return list of bean classifications
     */
    private List<Classification> getClassificationsFromEntity()
    {
        List<Classification> classifications       = null;

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
                        Classification beanClassification = new Classification();

                        beanClassification.setClassificationName(entityClassification.getName());
                        beanClassification.setClassificationProperties(repositoryHelper.getInstancePropertiesAsMap(entityClassification.getProperties()));
                    }
                }
            }
        }

        return classifications;
    }
}
