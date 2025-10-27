/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.unitycatalog.controls;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.TemplateType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ReplacementAttributeType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.TemplateDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TemplateTypeEnum characterises the type of templates supported by an integration connector.  It should be copied
 * by developers of an integration connector to help populate the supportedTemplates property of its provider.
 */
public enum UnityCatalogTemplateType implements TemplateDefinition
{
    OSS_UC_SERVER_TEMPLATE(UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getDeployedImplementationType(),
                           "A SoftwareServer asset with connection and REST API Manager software capability representing a Unity Catalog (UC) Server.",
                           UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getAssociatedTypeName(),
                           false,
                           "dcca9788-b30f-4007-b1ac-ec634aff6879",
                           UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER,
                           UnityCatalogPlaceholderProperty.getServerPlaceholderPropertyTypes(),
                           null),

    DB_UC_SERVER_TEMPLATE(UnityCatalogDeployedImplementationType.DB_UNITY_CATALOG_SERVER.getDeployedImplementationType(),
                          "A SoftwareServer asset with connection and REST API Manager software capability representing a Unity Catalog (UC) Server running in a Databricks managed platform.",
                          UnityCatalogDeployedImplementationType.DB_UNITY_CATALOG_SERVER.getAssociatedTypeName(),
                          false,
                          "3f7f62f6-4abc-424e-9f92-523306e7d5d5",
                          UnityCatalogDeployedImplementationType.DB_UNITY_CATALOG_SERVER,
                          UnityCatalogPlaceholderProperty.getServerPlaceholderPropertyTypes(),
                          null),

    OSS_UC_CATALOG_TEMPLATE(UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getDeployedImplementationType(),
                            "Create a " + UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getAssociatedTypeName() + " SoftwareCapability.",
                            UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getAssociatedTypeName(),
                            false,
                            "5ee006aa-a6d6-411b-9b8d-5f720c079cae",
                            UnityCatalogDeployedImplementationType.OSS_UC_CATALOG,
                            UnityCatalogPlaceholderProperty.getCatalogPlaceholderPropertyTypes(),
                            null),

    OSS_UC_SCHEMA_TEMPLATE(UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA.getDeployedImplementationType(),
                           "Create a DeployedDatabaseSchema to represent a schema from the Unity Catalog (UC).",
                           UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA.getAssociatedTypeName(),
                           false,
                           "5bf92b0f-3970-41ea-b0a3-aacfbf6fd92e",
                           UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA,
                           UnityCatalogPlaceholderProperty.getSchemaPlaceholderPropertyTypes(),
                           null),

    OSS_UC_VOLUME_TEMPLATE(UnityCatalogDeployedImplementationType.OSS_UC_VOLUME.getDeployedImplementationType(),
                           "Create a DataFolder asset to represent a volume from the Unity Catalog (UC).",
                           UnityCatalogDeployedImplementationType.OSS_UC_VOLUME.getAssociatedTypeName(),
                           false,
                           "92d2d2dc-0798-41f0-9512-b10548d312b7",
                           UnityCatalogDeployedImplementationType.OSS_UC_VOLUME,
                           UnityCatalogPlaceholderProperty.getVolumePlaceholderPropertyTypes(),
                           null),

    OSS_UC_TABLE_TEMPLATE(UnityCatalogDeployedImplementationType.OSS_UC_TABLE.getDeployedImplementationType(),
                          "Create a VirtualTableAsset asset to represent a table from the Unity Catalog (UC).",
                          UnityCatalogDeployedImplementationType.OSS_UC_TABLE.getAssociatedTypeName(),
                          false,
                          "6cc1e5f5-4c1e-4290-a80e-e06643ffb13d",
                          UnityCatalogDeployedImplementationType.OSS_UC_TABLE,
                          UnityCatalogPlaceholderProperty.getTablePlaceholderPropertyTypes(),
                          null),

    OSS_UC_FUNCTION_TEMPLATE(UnityCatalogDeployedImplementationType.OSS_UC_FUNCTION.getDeployedImplementationType(),
                             "Create a DeployedSoftwareComponent asset to represent a function from the Unity Catalog (UC).",
                             UnityCatalogDeployedImplementationType.OSS_UC_FUNCTION.getAssociatedTypeName(),
                             false,
                             "a490ba65-6104-4213-9be9-524e16fed8aa",
                             UnityCatalogDeployedImplementationType.OSS_UC_FUNCTION,
                             UnityCatalogPlaceholderProperty.getFunctionPlaceholderPropertyTypes(),
                             null),

    OSS_UC_REGISTERED_MODEL_TEMPLATE(UnityCatalogDeployedImplementationType.OSS_UC_REGISTERED_MODEL.getDeployedImplementationType(),
                                     "Create a DeployedAnalyticsModel asset to represent an analytics model deployed to the Unity Catalog (UC).",
                                     UnityCatalogDeployedImplementationType.OSS_UC_REGISTERED_MODEL.getAssociatedTypeName(),
                                     false,
                                     "0d762ec5-c1f5-4364-aa64-e7e00d27f837",
                                     UnityCatalogDeployedImplementationType.OSS_UC_REGISTERED_MODEL,
                                     UnityCatalogPlaceholderProperty.getRegisteredModelPlaceholderPropertyTypes(),
                                     null),

    OSS_UC_MODEL_VERSION_TEMPLATE(UnityCatalogDeployedImplementationType.OSS_UC_REGISTERED_MODEL_VERSION.getDeployedImplementationType(),
                                  "Create an AnalyticsModelRun asset to represent a version of an analytics model deployed to the Unity Catalog (UC).",
                                  UnityCatalogDeployedImplementationType.OSS_UC_REGISTERED_MODEL_VERSION.getAssociatedTypeName(),
                                  false,
                                  "1364bfe7-8295-4e99-9243-8840aeac4cf1",
                                  UnityCatalogDeployedImplementationType.OSS_UC_REGISTERED_MODEL_VERSION,
                                  UnityCatalogPlaceholderProperty.getModelVersionPlaceholderPropertyTypes(),
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
     * Official description of the technology
     */
    private final UnityCatalogDeployedImplementationType deployedImplementationType;

    /**
     * Placeholder properties used in the template.
     */
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
    UnityCatalogTemplateType(String                                 templateName,
                             String                                 templateDescription,
                             String                                 typeName,
                             boolean                                required,
                             String                                 defaultTemplateGUID,
                             UnityCatalogDeployedImplementationType deployedImplementationType,
                             List<PlaceholderPropertyType>          placeholderPropertyTypes,
                             Map<String, String>                    otherPropertyValues)
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

        templateType.setName(templateName);
        templateType.setOpenMetadataTypeName(typeName);
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
