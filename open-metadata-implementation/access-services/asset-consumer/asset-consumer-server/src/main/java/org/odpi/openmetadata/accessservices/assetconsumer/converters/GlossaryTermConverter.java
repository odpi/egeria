/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.converters;


import org.odpi.openmetadata.accessservices.assetconsumer.properties.GlossaryTerm;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

public class GlossaryTermConverter extends ReferenceableHeaderConverter
{
    public static final String DISPLAY_NAME_PROPERTY_NAME = "displayName";

    private static final String summaryPropertyName       = "summary";
    private static final String descriptionPropertyName   = "description";
    private static final String examplesPropertyName      = "usage";
    private static final String abbreviationPropertyName  = "abbreviation";
    private static final String usagePropertyName         = "usage";


    private GlossaryTerm  glossaryTermBean = null;


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

        if (entity != null)
        {
            this.glossaryTermBean = new GlossaryTerm();
            super.setBean(glossaryTermBean);
            this.updateGlossaryTermBean(entity, repositoryHelper, componentName);
        }
    }


    /**
     * Extract the Asset specific properties from the entity.
     *
     * @param entity entity containing the relevant properties
     * @param repositoryHelper helper object to parse entity
     * @param componentName name of this component
     */
    private void updateGlossaryTermBean(EntityDetail         entity,
                                        OMRSRepositoryHelper repositoryHelper,
                                        String               componentName)
    {
        final String  methodName = "updateGlossaryTermBean";

        if (entity != null)
        {
            InstanceProperties instanceProperties = entity.getProperties();

            glossaryTermBean.setDisplayName(repositoryHelper.getStringProperty(componentName,
                                                                               DISPLAY_NAME_PROPERTY_NAME, instanceProperties, methodName));
            glossaryTermBean.setSummary(repositoryHelper.getStringProperty(componentName, summaryPropertyName, instanceProperties, methodName));
            glossaryTermBean.setDescription(repositoryHelper.getStringProperty(componentName, descriptionPropertyName, instanceProperties, methodName));
            glossaryTermBean.setUsage(repositoryHelper.getStringProperty(componentName, usagePropertyName, instanceProperties, methodName));
            glossaryTermBean.setAbbreviation(repositoryHelper.getStringProperty(componentName, abbreviationPropertyName, instanceProperties, methodName));
            glossaryTermBean.setExamples(repositoryHelper.getStringProperty(componentName, examplesPropertyName, instanceProperties, methodName));
        }
    }


    /**
     * Return the converted bean.
     *
     * @return bean populated with properties from the entity supplied in the constructor
     */
    public GlossaryTerm getBean()
    {
        return glossaryTermBean;
    }
}
