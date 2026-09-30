/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.controls;

import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.controls.TemplateDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ReplacementAttributeType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.TemplateType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DocumentManagementTemplateType describes the catalog templates supplied with Egeria for documents in document
 * management systems.  Each template creates just the asset, identified by its resource name within a namespace
 * (for example the name and namespace that OpenLineage gives a dataset or job).
 */
public enum DocumentManagementTemplateType implements TemplateDefinition
{
    /**
     * Create an asset with the Box File technology type.
     */
    BOX_FILE_TEMPLATE(DocumentManagementDeployedImplementationType.BOX_FILE.getDeployedImplementationType(),
                      "Create a " + DocumentManagementDeployedImplementationType.BOX_FILE.getAssociatedTypeName() + " asset with technology type " + DocumentManagementDeployedImplementationType.BOX_FILE.getDeployedImplementationType() + ".",
                      DocumentManagementDeployedImplementationType.BOX_FILE.getAssociatedTypeName(),
                      false,
                      "1cc2f7a3-4be0-4350-93ba-99eec2e27a56",
                      DocumentManagementDeployedImplementationType.BOX_FILE,
                      PlaceholderProperty.getFileResourcePlaceholderPropertyTypes(),
                      null),

    /**
     * Create an asset with the IBM FileNet Document technology type.
     */
    IBM_FILENET_DOCUMENT_TEMPLATE(DocumentManagementDeployedImplementationType.IBM_FILENET_DOCUMENT.getDeployedImplementationType(),
                                  "Create a " + DocumentManagementDeployedImplementationType.IBM_FILENET_DOCUMENT.getAssociatedTypeName() + " asset with technology type " + DocumentManagementDeployedImplementationType.IBM_FILENET_DOCUMENT.getDeployedImplementationType() + ".",
                                  DocumentManagementDeployedImplementationType.IBM_FILENET_DOCUMENT.getAssociatedTypeName(),
                                  false,
                                  "92c538d0-adbd-47b6-b46f-d6f72bc82247",
                                  DocumentManagementDeployedImplementationType.IBM_FILENET_DOCUMENT,
                                  PlaceholderProperty.getFileResourcePlaceholderPropertyTypes(),
                                  null),

    /**
     * Create an asset with the Microsoft SharePoint Document technology type.
     */
    MICROSOFT_SHAREPOINT_DOCUMENT_TEMPLATE(DocumentManagementDeployedImplementationType.MICROSOFT_SHAREPOINT_DOCUMENT.getDeployedImplementationType(),
                                           "Create a " + DocumentManagementDeployedImplementationType.MICROSOFT_SHAREPOINT_DOCUMENT.getAssociatedTypeName() + " asset with technology type " + DocumentManagementDeployedImplementationType.MICROSOFT_SHAREPOINT_DOCUMENT.getDeployedImplementationType() + ".",
                                           DocumentManagementDeployedImplementationType.MICROSOFT_SHAREPOINT_DOCUMENT.getAssociatedTypeName(),
                                           false,
                                           "3c243f0b-ca25-4a47-adc8-8493fcbdd985",
                                           DocumentManagementDeployedImplementationType.MICROSOFT_SHAREPOINT_DOCUMENT,
                                           PlaceholderProperty.getFileResourcePlaceholderPropertyTypes(),
                                           null)

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

    private final DeployedImplementationTypeDefinition deployedImplementationType;

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
    DocumentManagementTemplateType(String                               templateName,
                      String                               templateDescription,
                      String                               typeName,
                      boolean                              required,
                      String                               defaultTemplateGUID,
                      DeployedImplementationTypeDefinition deployedImplementationType,
                      List<PlaceholderPropertyType>        placeholderPropertyTypes,
                      Map<String, String>                  otherPropertyValues)
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
        return "6.2-SNAPSHOT";
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

        for (DocumentManagementTemplateType templateTypeEnum : DocumentManagementTemplateType.values())
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
        return "DocumentManagementTemplateType{templateName='" + templateName + "'}";
    }
}
