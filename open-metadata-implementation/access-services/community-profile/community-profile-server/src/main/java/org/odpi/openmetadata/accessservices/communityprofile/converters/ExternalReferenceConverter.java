/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.mappers.ExternalReferenceMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ExternalReference;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ExternalReferenceConverter generates an ExternalReference bean from an ExternalReference entity
 * and a Relationship to it.
 */
public class ExternalReferenceConverter extends CommonHeaderConverter
{
    private static final Logger log = LoggerFactory.getLogger(ExternalReferenceConverter.class);

    /**
     * Constructor captures the initial content with relationship
     *
     * @param entity properties to convert
     * @param relationship properties to convert
     * @param repositoryHelper helper object to parse entity/relationship
     * @param componentName name of this component
     */
    ExternalReferenceConverter(EntityDetail         entity,
                               Relationship         relationship,
                               OMRSRepositoryHelper repositoryHelper,
                               String               componentName)
    {
        super(entity, relationship, repositoryHelper, componentName);
    }


    /**
     * Return the bean constructed from the repository content.
     *
     * @return bean
     */
    public ExternalReference getBean()
    {
        final String methodName = "getBean";

        ExternalReference  bean = new ExternalReference();

        super.updateBean(bean);

        if (entity != null)
        {
            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                /*
                 * As properties are retrieved, they are removed from the instance properties object so that what is left going into
                 * resource properties.
                 */
                bean.setResourceId(repositoryHelper.removeStringProperty(serviceName, ExternalReferenceMapper.RESOURCE_ID_PROPERTY_NAME, instanceProperties, methodName));
                bean.setResourceDisplayName(repositoryHelper.removeStringProperty(serviceName, ExternalReferenceMapper.RESOURCE_DISPLAY_NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setResourceDescription(repositoryHelper.removeStringProperty(serviceName, ExternalReferenceMapper.RESOURCE_DESCRIPTION_PROPERTY_NAME, instanceProperties, methodName));
                bean.setResourceURL(repositoryHelper.removeStringProperty(serviceName, ExternalReferenceMapper.RESOURCE_URL_PROPERTY_NAME, instanceProperties, methodName));
                bean.setResourceVersion(repositoryHelper.removeStringProperty(serviceName, ExternalReferenceMapper.RESOURCE_VERSION_PROPERTY_NAME, instanceProperties, methodName));
                bean.setOwningOrganization(repositoryHelper.removeStringProperty(serviceName, ExternalReferenceMapper.OWNING_ORGANIZATION_PROPERTY_NAME, instanceProperties, methodName));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName, ExternalReferenceMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, instanceProperties, methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }
        }

        if (relationship != null)
        {
            InstanceProperties instanceProperties = relationship.getProperties();

            if (instanceProperties != null)
            {
                bean.setLinkId(repositoryHelper.getStringProperty(serviceName, ExternalReferenceMapper.LINK_ID_PROPERTY_NAME, instanceProperties, methodName));
                bean.setLinkDescription(repositoryHelper.getStringProperty(serviceName, ExternalReferenceMapper.LINK_DESCRIPTION_PROPERTY_NAME, instanceProperties, methodName));
            }
        }

        log.debug("Bean: " + bean.toString());

        return bean;
    }
}
