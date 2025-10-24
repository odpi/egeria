/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.csvfile.controls;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.TemplateType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ReplacementAttributeType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.TemplateDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TemplateTypeEnum characterises the type of templates supported by an integration connector.  It should be copied
 * by developers of an integration connector to help populate the supportedTemplates property of its provider.
 */
public enum CSVFileTemplateType implements TemplateDefinition
{
    CSV_TABULAR_DATA_SET_TEMPLATE(DeployedImplementationType.CSV_TABULAR_DATA_SET.getDeployedImplementationType(),
                                  "Create a " + DeployedImplementationType.CSV_TABULAR_DATA_SET.getAssociatedTypeName() + " asset.",
                                  DeployedImplementationType.CSV_TABULAR_DATA_SET.getAssociatedTypeName(),
                                  false,
                                  "29ee189f-34db-4c3a-966c-40b04ad45103",
                                  DeployedImplementationType.CSV_TABULAR_DATA_SET,
                                  getTabularDataSetPlaceholderPropertyTypes(),
                                  null),

    CSV_TABULAR_DATA_SET_COLLECTION_TEMPLATE(DeployedImplementationType.CSV_TABULAR_DATA_SET_COLLECTION.getDeployedImplementationType(),
                                             "Create a " + DeployedImplementationType.CSV_TABULAR_DATA_SET_COLLECTION.getAssociatedTypeName() + " asset.",
                                             DeployedImplementationType.CSV_TABULAR_DATA_SET_COLLECTION.getAssociatedTypeName(),
                                             false,
                                             "b2ec58c6-7c70-4f8e-b7a2-221fd1941b2c",
                                             DeployedImplementationType.CSV_TABULAR_DATA_SET_COLLECTION,
                                             getTabularDataSetCollectionPlaceholderPropertyTypes(),
                                             null),

    ;


    /**
     * Symbolic name of the template.
     */
    private final String templateName;

    /**
     * Description of the value to provide for this template.
     */
    private final String templateDescription;

    /**
     * Open metadata type name of the template.
     */
    private final String typeName;

    /**
     * Is this catalog template required for the connector to work successfully.
     */
    private final boolean required;


    /**
     * Option guid for a template to use if no template is specified.
     */
    private final String defaultTemplateGUID;

    /**
     * A map of property name to property value for values that should match in the catalog template for it to be compatible with this integration
     * connector.
     */
    private final Map<String, String> otherPropertyValues;

    private final DeployedImplementationType deployedImplementationType;

    private final List<PlaceholderPropertyType>        placeholderPropertyTypes;


    /**
     * Constructor for Enum
     *
     * @param templateName catalog template name
     * @param typeName open metadata type name for the linked element
     * @param templateDescription deployed implementation type for the linked element
     * @param required is this template required bu the connector
     * @param defaultTemplateGUID is there a default template
     * @param deployedImplementationType       deployed implementation type for the technology
     * @param placeholderPropertyTypes         placeholder variables used in the supplied parameters
     * @param otherPropertyValues other values
     */
    CSVFileTemplateType(String                             templateName,
                        String                             templateDescription,
                        String                             typeName,
                        boolean                            required,
                        String                             defaultTemplateGUID,
                        DeployedImplementationType         deployedImplementationType,
                        List<PlaceholderPropertyType>      placeholderPropertyTypes,
                        Map<String, String>                otherPropertyValues)
    {
        this.templateName               = templateName;
        this.templateDescription        = templateDescription;
        this.typeName                   = typeName;
        this.required                   = required;
        this.defaultTemplateGUID        = defaultTemplateGUID;
        this.deployedImplementationType = deployedImplementationType;
        this.placeholderPropertyTypes   = placeholderPropertyTypes;
        this.otherPropertyValues        = otherPropertyValues;
    }


    /**
     * Return the unique identifier of the template.
     *
     * @return name
     */
    @Override
    public String getTemplateGUID()
    {
        return defaultTemplateGUID;
    }

    /**
     * Return the name of the template.
     *
     * @return name
     */
    @Override
    public String getTemplateName()
    {
        return templateName;
    }


    /**
     * Return the description of the template, such as its content.
     *
     * @return description
     */
    @Override
    public String getTemplateDescription()
    {
        return templateDescription;
    }


    /**
     * Return the version identifier for the template classification.
     *
     * @return string
     */
    @Override
    public String getTemplateVersionIdentifier()
    {
        return "6.0-SNAPSHOT";
    }


    /**
     * Return the supported deployed implementation for this template.
     *
     * @return enum
     */
    @Override
    public DeployedImplementationTypeDefinition getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Return the value to use in the element that describes its version.
     *
     * @return version identifier placeholder
     */
    @Override
    public String getElementVersionIdentifier()
    {
        return PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder();
    }


    /**
     * Return the list of placeholders supported by this template.
     *
     * @return list of placeholder types
     */
    @Override
    public List<PlaceholderPropertyType> getPlaceholders()
    {
        return placeholderPropertyTypes;
    }


    /**
     * Return the list of attributes that should be supplied by the caller using this template.
     *
     * @return list of replacement attributes
     */
    @Override
    public List<ReplacementAttributeType> getReplacementAttributes()
    {
        return null;
    }


    /**
     * Return the open metadata type name.
     *
     * @return open metadata type name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Return whether this catalog template is required for this service to work successful.
     *
     * @return boolean flag
     */
    public boolean getRequired()
    {
        return required;
    }


    /**
     * Return a map of property name to property value that the catalog template should have to be valid for this integration connector.
     *
     * @return map of string to string
     */
    public Map<String, String> getOtherPropertyValues()
    {
        return otherPropertyValues;
    }


    /**
     * Retrieve all the defined placeholder properties for data files
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getTabularDataSetPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PlaceholderProperty.DISPLAY_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.TABLE_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.TABLE_DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DIRECTORY_PATH_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());

        return placeholderPropertyTypes;
    }


    /**
     * Retrieve all the defined placeholder properties for data files
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getTabularDataSetCollectionPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PlaceholderProperty.DISPLAY_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DIRECTORY_PATH_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());

        return placeholderPropertyTypes;
    }

    /**
     * Return all the template types defined by this enum.
     *
     * @return list of catalog template type
     */
    public static List<TemplateType> getTemplateTypes()
    {
        List<TemplateType> templateTypes = new ArrayList<>();

        for (CSVFileTemplateType templateTypeEnum : CSVFileTemplateType.values())
        {
            templateTypes.add(templateTypeEnum.getTemplateType());
        }

        return templateTypes;
    }


    /**
     * Return the catalog template type for a specific catalog template enum.
     *
     * @return catalog template type
     */
    public TemplateType getTemplateType()
    {
        TemplateType templateType = new TemplateType();

        templateType.setTemplateName(templateName);
        templateType.setTypeName(typeName);
        templateType.setDescription(templateDescription);
        templateType.setRequired(required);
        templateType.setDefaultTemplateGUID(defaultTemplateGUID);
        templateType.setOtherPropertyValues(otherPropertyValues);

        return templateType;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "TemplateType{templateName='" + templateName + "'}";
    }
}
