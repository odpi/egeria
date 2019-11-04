/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters;


import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.InformalTagMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.InformalTag;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * InformalTagConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into an InformalTag bean.
 */
public class InformalTagConverter extends ElementHeaderConverter
{
    /**
     * Constructor captures the initial content
     *
     * @param entity properties to convert
     * @param relationship properties to convert
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     */
    public InformalTagConverter(EntityDetail         entity,
                                Relationship         relationship,
                                OMRSRepositoryHelper repositoryHelper,
                                String               serviceName)
    {
        super(entity, relationship, repositoryHelper, serviceName);
    }


    /**
     * Constructor captures the initial content
     *
     * @param entity properties to convert
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     */
    public InformalTagConverter(EntityDetail         entity,
                                OMRSRepositoryHelper repositoryHelper,
                                String               serviceName)
    {
        super(entity, null, repositoryHelper, serviceName);
    }


    /**
     * Request the bean is extracted from the repository entity.
     *
     * @return output bean
     */
    public InformalTag getBean()
    {
        final String  methodName = "getBean";

        InformalTag  bean = null;

        if (relationship != null)
        {
            bean = new InformalTag();

            super.updateBean(bean);

            bean.setUser(relationship.getCreatedBy());

            InstanceProperties instanceProperties = relationship.getProperties();

            if (instanceProperties != null)
            {
                bean.setPublic(repositoryHelper.getBooleanProperty(serviceName, InformalTagMapper.IS_PUBLIC_PROPERTY_NAME, instanceProperties, methodName));
            }
        }

        if (entity != null)
        {
            /*
             * The properties are removed from the instance properties and stowed in the bean.
             * Any remaining properties are stored in extendedProperties.
             */
            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                bean.setPrivateTag(! repositoryHelper.removeBooleanProperty(serviceName,
                                                                            InformalTagMapper.IS_PUBLIC_PROPERTY_NAME,
                                                                            instanceProperties,
                                                                            methodName));

                bean.setName(repositoryHelper.removeStringProperty(serviceName,
                                                                   InformalTagMapper.TAG_NAME_PROPERTY_NAME,
                                                                   instanceProperties,
                                                                   methodName));

                bean.setDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                   InformalTagMapper.TAG_DESCRIPTION_PROPERTY_NAME,
                                                                   instanceProperties,
                                                                   methodName));

                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }
        }

        return bean;
    }
}
