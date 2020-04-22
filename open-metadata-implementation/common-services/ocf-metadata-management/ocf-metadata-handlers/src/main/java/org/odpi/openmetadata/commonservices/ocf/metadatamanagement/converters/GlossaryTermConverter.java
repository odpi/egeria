/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters;


import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.GlossaryTermMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.properties.GlossaryTerm;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.ReferenceableConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * GlossaryTermConverter takes a glossary term entity and turns it into a glossary term bean.
 */
public class GlossaryTermConverter extends ReferenceableConverter
{
    /**
     * Constructor takes in the source of information for the bean
     *
     * @param entity properties to convert
     * @param repositoryHelper helper object to parse entity
     * @param componentName name of this component
     */
    public GlossaryTermConverter(EntityDetail         entity,
                                 OMRSRepositoryHelper repositoryHelper,
                                 String               componentName)
    {
        super(entity, repositoryHelper, componentName);
    }


    /**
     * This method is overridable by the subclasses.
     *
     * @return empty bean
     */
    protected GlossaryTerm  getNewGlossaryTermBean()
    {
        return new GlossaryTerm();
    }


    /**
     * Return the converted bean.
     *
     * @return bean populated with properties from the entity supplied in the constructor
     */
    public GlossaryTerm getBean()
    {
        GlossaryTerm  bean = null;

        if (entity != null)
        {
            bean = getNewGlossaryTermBean();

            updateBean(bean);
        }

        return bean;
    }


    /**
     * Request the bean is extracted from the repository objects
     *
     * @param bean  output bean
     */
    protected void updateBean(GlossaryTerm bean)
    {
        final String  methodName = "updateBean";

        if (entity != null)
        {
            super.updateBean(bean);

            /*
             * The properties are removed from the instance properties and stowed in the bean.
             * Any remaining properties are stored in extendedProperties.
             */
            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName,
                                                                            ReferenceableMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                            instanceProperties,
                                                                            methodName));
                bean.setDisplayName(repositoryHelper.removeStringProperty(serviceName,
                                                                          GlossaryTermMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setSummary(repositoryHelper.removeStringProperty(serviceName,
                                                                          GlossaryTermMapper.SUMMARY_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                          GlossaryTermMapper.DESCRIPTION_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setAbbreviation(repositoryHelper.removeStringProperty(serviceName,
                                                                          GlossaryTermMapper.ABBREVIATION_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setUsage(repositoryHelper.removeStringProperty(serviceName,
                                                                          GlossaryTermMapper.USAGE_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                                          ReferenceableMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                                          instanceProperties,
                                                                                          methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }
        }
    }
}
