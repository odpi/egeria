/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.unitycatalog.controls;

import org.odpi.openmetadata.frameworks.connectors.controls.TemplateType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TemplateTypeEnum characterises the type of templates supported by an integration connector.  It should be copied
 * by developers of an integration connector to help populate the supportedTemplates property of its provider.
 */
public enum UnityCatalogTemplateType
{
    OSS_UC_SERVER_TEMPLATE(UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getDeployedImplementationType(),
                           "A SoftwareServer asset with connection and REST API Manager software capability representing a Unity Catalog (UC) Server.",
                           UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getAssociatedTypeName(),
                           false,
                           "dcca9788-b30f-4007-b1ac-ec634aff6879",
                           null),

    DB_UC_SERVER_TEMPLATE(UnityCatalogDeployedImplementationType.DB_UNITY_CATALOG_SERVER.getDeployedImplementationType(),
                           "A SoftwareServer asset with connection and REST API Manager software capability representing a Unity Catalog (UC) Server running in a Databricks managed platform.",
                           UnityCatalogDeployedImplementationType.DB_UNITY_CATALOG_SERVER.getAssociatedTypeName(),
                           false,
                           "3f7f62f6-4abc-424e-9f92-523306e7d5d5",
                           null),

    OSS_UC_CATALOG_TEMPLATE(UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getDeployedImplementationType(),
                            "Create a " + UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getAssociatedTypeName() + " SoftwareCapability.",
                            UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getAssociatedTypeName(),
                            false,
                            "5ee006aa-a6d6-411b-9b8d-5f720c079cae",
                            null),

    OSS_UC_SCHEMA_TEMPLATE(UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA.getDeployedImplementationType(),
                           "Create a DeployedDatabaseSchema to represent a schema from the Unity Catalog (UC).",
                           UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA.getAssociatedTypeName(),
                           false,
                           "5bf92b0f-3970-41ea-b0a3-aacfbf6fd92e",
                           null),

    OSS_UC_VOLUME_TEMPLATE(UnityCatalogDeployedImplementationType.OSS_UC_VOLUME.getDeployedImplementationType(),
                           "Create a DataFolder asset to represent a volume from the Unity Catalog (UC).",
                           UnityCatalogDeployedImplementationType.OSS_UC_VOLUME.getAssociatedTypeName(),
                           false,
                           "92d2d2dc-0798-41f0-9512-b10548d312b7",
                           null),

    OSS_UC_TABLE_TEMPLATE(UnityCatalogDeployedImplementationType.OSS_UC_TABLE.getDeployedImplementationType(),
                          "Create a VirtualTableAsset asset to represent a table from the Unity Catalog (UC).",
                          UnityCatalogDeployedImplementationType.OSS_UC_TABLE.getAssociatedTypeName(),
                          false,
                          "6cc1e5f5-4c1e-4290-a80e-e06643ffb13d",
                          null),

    OSS_UC_FUNCTION_TEMPLATE(UnityCatalogDeployedImplementationType.OSS_UC_FUNCTION.getDeployedImplementationType(),
                             "Create a DeployedSoftwareComponent asset to represent a function from the Unity Catalog (UC).",
                             UnityCatalogDeployedImplementationType.OSS_UC_FUNCTION.getAssociatedTypeName(),
                             false,
                             "a490ba65-6104-4213-9be9-524e16fed8aa",
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


    /**
     * Constructor for Enum
     *
     * @param templateName catalog template name
     * @param typeName open metadata type name for the linked element
     * @param templateDescription deployed implementation type for the linked element
     * @param required is this template required bu the connector
     * @param defaultTemplateGUID is there a default template
     * @param otherPropertyValues other values
     */
    UnityCatalogTemplateType(String              templateName,
                             String              templateDescription,
                             String              typeName,
                             boolean             required,
                             String              defaultTemplateGUID,
                             Map<String, String> otherPropertyValues)
    {
        this.templateName        = templateName;
        this.templateDescription = templateDescription;
        this.typeName            = typeName;
        this.required            = required;
        this.defaultTemplateGUID = defaultTemplateGUID;
        this.otherPropertyValues = otherPropertyValues;
    }


    /**
     * Return the name of the template.
     *
     * @return name
     */
    public String getTemplateName()
    {
        return templateName;
    }


    /**
     * Return the description of the template, such as its content.
     *
     * @return description
     */
    public String getTemplateDescription()
    {
        return templateDescription;
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
     * Return the unique identifier of the default template to use with this connector - this is optional.
     *
     * @return string
     */
    public String getDefaultTemplateGUID()
    {
        return defaultTemplateGUID;
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
     * Return all the template types defined by this enum.
     *
     * @return list of catalog template type
     */
    public static List<TemplateType> getTemplateTypes()
    {
        List<TemplateType> templateTypes = new ArrayList<>();

        for (UnityCatalogTemplateType templateTypeEnum : UnityCatalogTemplateType.values())
        {
            templateTypes.add(templateTypeEnum.getTemplateType());
        }

        return templateTypes;
    }



    /**
     * Return all the template types defined by this enum.
     *
     * @return list of catalog template type
     */
    public static List<TemplateType> getInsideCatalogTemplateTypes()
    {
        List<TemplateType> templateTypes = new ArrayList<>();

        templateTypes.add(OSS_UC_CATALOG_TEMPLATE.getTemplateType());
        templateTypes.add(OSS_UC_SCHEMA_TEMPLATE.getTemplateType());
        templateTypes.add(OSS_UC_VOLUME_TEMPLATE.getTemplateType());
        templateTypes.add(OSS_UC_TABLE_TEMPLATE.getTemplateType());
        templateTypes.add(OSS_UC_FUNCTION_TEMPLATE.getTemplateType());

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
        templateType.setTemplateDescription(templateDescription);
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
