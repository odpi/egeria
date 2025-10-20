/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.basicfiles.controls;

import org.odpi.openmetadata.frameworks.connectors.controls.TemplateType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.ReplacementAttributeType;
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
public enum FilesTemplateType implements TemplateDefinition
{
    FILE_FOLDER_TEMPLATE(DeployedImplementationType.FILE_FOLDER.getDeployedImplementationType(),
                             "Create a " + DeployedImplementationType.FILE_FOLDER.getAssociatedTypeName() + " asset.",
                             DeployedImplementationType.FILE_FOLDER.getAssociatedTypeName(),
                             false,
                             "fbdd8efd-1b69-474c-bb6d-0a304b394146",
                             DeployedImplementationType.FILE_FOLDER,
                             PlaceholderProperty.getFolderPlaceholderPropertyTypes(),
                             null),

    DATA_FOLDER_TEMPLATE(DeployedImplementationType.DATA_FOLDER.getDeployedImplementationType(),
                         "Create a " + DeployedImplementationType.DATA_FOLDER.getAssociatedTypeName() + " asset.",
                         DeployedImplementationType.DATA_FOLDER.getAssociatedTypeName(),
                         false,
                         "372a0379-7060-4c9d-8d84-bc709b31794c",
                         DeployedImplementationType.DATA_FOLDER,
                         PlaceholderProperty.getFolderPlaceholderPropertyTypes(),
                         null),


    FILE_TEMPLATE(DeployedImplementationType.FILE.getDeployedImplementationType(),
                         "Create a " + DeployedImplementationType.FILE.getAssociatedTypeName() + " asset.",
                         DeployedImplementationType.FILE.getAssociatedTypeName(),
                         false,
                         "ae3067c7-cc72-4a18-88e1-746803c2c86f",
                         DeployedImplementationType.FILE,
                         PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                         null),

    DATA_FILE_TEMPLATE(DeployedImplementationType.DATA_FILE.getDeployedImplementationType(),
                       "Create a " + DeployedImplementationType.DATA_FILE.getAssociatedTypeName() + " asset.",
                       DeployedImplementationType.DATA_FILE.getAssociatedTypeName(),
                       false,
                       "66d8dda9-00cf-4e59-938c-4b0583596b1e",
                       DeployedImplementationType.DATA_FILE,
                       PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                       null),

    CSV_FILE_TEMPLATE(DeployedImplementationType.CSV_FILE.getDeployedImplementationType(),
                       "Create a " + DeployedImplementationType.CSV_FILE.getAssociatedTypeName() + " asset.",
                       DeployedImplementationType.CSV_FILE.getAssociatedTypeName(),
                       false,
                      "13770f93-13c8-42be-9bb8-e0b1b1e52b1f",
                       DeployedImplementationType.CSV_FILE,
                       PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                       null),

    AVRO_FILE_TEMPLATE(DeployedImplementationType.AVRO_FILE.getDeployedImplementationType(),
                       "Create a " + DeployedImplementationType.AVRO_FILE.getAssociatedTypeName() + " asset.",
                       DeployedImplementationType.AVRO_FILE.getAssociatedTypeName(),
                       false,
                       "9f5be428-058e-41dd-b506-3a222283b579",
                       DeployedImplementationType.AVRO_FILE,
                       PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                       null),

    JSON_FILE_TEMPLATE(DeployedImplementationType.JSON_FILE.getDeployedImplementationType(),
                       "Create a " + DeployedImplementationType.JSON_FILE.getAssociatedTypeName() + " asset.",
                       DeployedImplementationType.JSON_FILE.getAssociatedTypeName(),
                       false,
                       "c4836635-7e9e-446a-83b5-15e206b1aff3",
                       DeployedImplementationType.JSON_FILE,
                       PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                       null),

    PARQUET_FILE_TEMPLATE(DeployedImplementationType.PARQUET_FILE.getDeployedImplementationType(),
                       "Create a " + DeployedImplementationType.PARQUET_FILE.getAssociatedTypeName() + " asset.",
                       DeployedImplementationType.PARQUET_FILE.getAssociatedTypeName(),
                       false,
                       "7f6cd744-79c3-4d25-a056-eeb1a91574c3",
                       DeployedImplementationType.PARQUET_FILE,
                       PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                       null),

    SPREADSHEET_FILE_TEMPLATE(DeployedImplementationType.SPREADSHEET_FILE.getDeployedImplementationType(),
                              "Create a " + DeployedImplementationType.SPREADSHEET_FILE.getAssociatedTypeName() + " asset.",
                              DeployedImplementationType.SPREADSHEET_FILE.getAssociatedTypeName(),
                              false,
                              "e4fabff5-2ba9-4050-9076-6ed917970b4c",
                              DeployedImplementationType.SPREADSHEET_FILE,
                              PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                              null),

    XML_FILE_TEMPLATE(DeployedImplementationType.XML_FILE.getDeployedImplementationType(),
                      "Create a " + DeployedImplementationType.XML_FILE.getAssociatedTypeName() + " asset.",
                      DeployedImplementationType.XML_FILE.getAssociatedTypeName(),
                      false,
                      "ea67ae71-c674-473e-b38b-689879d2a7d9",
                      DeployedImplementationType.XML_FILE,
                      PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                      null),

    DOCUMENT_TEMPLATE(DeployedImplementationType.DOCUMENT.getDeployedImplementationType(),
                      "Create a " + DeployedImplementationType.DOCUMENT.getAssociatedTypeName() + " asset.",
                      DeployedImplementationType.DOCUMENT.getAssociatedTypeName(),
                      false,
                      "eb6f728d-fa54-4350-9807-1199cbf96851",
                      DeployedImplementationType.DOCUMENT,
                      PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                      null),

    AUDIO_DATA_FILE_TEMPLATE(DeployedImplementationType.AUDIO_DATA_FILE.getDeployedImplementationType(),
                             "Create a " + DeployedImplementationType.AUDIO_DATA_FILE.getAssociatedTypeName() + " asset.",
                             DeployedImplementationType.AUDIO_DATA_FILE.getAssociatedTypeName(),
                             false,
                             "39b4b670-7f15-4744-a5ba-62e8edafbcee",
                             DeployedImplementationType.AUDIO_DATA_FILE,
                             PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                             null),

    VIDEO_DATA_FILE_TEMPLATE(DeployedImplementationType.VIDEO_DATA_FILE.getDeployedImplementationType(),
                             "Create a " + DeployedImplementationType.VIDEO_DATA_FILE.getAssociatedTypeName() + " asset.",
                             DeployedImplementationType.VIDEO_DATA_FILE.getAssociatedTypeName(),
                             false,
                             "93b2b722-ec0f-4da4-960a-b8d4922f8bf5",
                             DeployedImplementationType.VIDEO_DATA_FILE,
                             PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                             null),

    THREE_D_IMAGE_DATA_FILE_TEMPLATE(DeployedImplementationType.THREE_D_IMAGE_DATA_FILE.getDeployedImplementationType(),
                                     "Create a " + DeployedImplementationType.THREE_D_IMAGE_DATA_FILE.getAssociatedTypeName() + " asset.",
                                     DeployedImplementationType.THREE_D_IMAGE_DATA_FILE.getAssociatedTypeName(),
                                     false,
                                     "0059ea2b-6292-4cac-aa6f-a80a605f1114",
                                     DeployedImplementationType.THREE_D_IMAGE_DATA_FILE,
                                     PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                                     null),

    RASTER_DATA_FILE_TEMPLATE(DeployedImplementationType.RASTER_DATA_FILE.getDeployedImplementationType(),
                              "Create a " + DeployedImplementationType.RASTER_DATA_FILE.getAssociatedTypeName() + " asset.",
                              DeployedImplementationType.RASTER_DATA_FILE.getAssociatedTypeName(),
                              false,
                              "47211156-f03f-4881-8526-015e695a3dac",
                              DeployedImplementationType.RASTER_DATA_FILE,
                              PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                              null),

    VECTOR_DATA_FILE_TEMPLATE(DeployedImplementationType.VECTOR_DATA_FILE.getDeployedImplementationType(),
                              "Create a " + DeployedImplementationType.VECTOR_DATA_FILE.getAssociatedTypeName() + " asset.",
                              DeployedImplementationType.RASTER_DATA_FILE.getAssociatedTypeName(),
                              false,
                              "db1bec7f-55a9-40d3-91c0-a57b76d422e2",
                              DeployedImplementationType.VECTOR_DATA_FILE,
                              PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                              null),

    ARCHIVE_FILE_TEMPLATE(DeployedImplementationType.ARCHIVE_FILE.getDeployedImplementationType(),
                          "Create a " + DeployedImplementationType.ARCHIVE_FILE.getAssociatedTypeName() + " asset.",
                          DeployedImplementationType.ARCHIVE_FILE.getAssociatedTypeName(),
                          false,
                          "7578e504-d00f-406d-a194-3fc0a351cdf9",
                          DeployedImplementationType.ARCHIVE_FILE,
                          PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                          null),

    KEYSTORE_FILE_TEMPLATE(DeployedImplementationType.KEYSTORE_FILE.getDeployedImplementationType(),
                           "Create a " + DeployedImplementationType.KEYSTORE_FILE.getAssociatedTypeName() + " asset.",
                           DeployedImplementationType.KEYSTORE_FILE.getAssociatedTypeName(),
                           false,
                           "fbcfcc0c-1652-421f-b49b-c3e1c108768f",
                           DeployedImplementationType.KEYSTORE_FILE,
                           PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                           null),

    PROGRAM_FILE_TEMPLATE(DeployedImplementationType.PROGRAM_FILE.getDeployedImplementationType(),
                          "Create a " + DeployedImplementationType.PROGRAM_FILE.getAssociatedTypeName() + " asset.",
                          DeployedImplementationType.PROGRAM_FILE.getAssociatedTypeName(),
                          false,
                          "32d27e9c-1fdf-455a-ad2a-42b4d7d99108",
                          DeployedImplementationType.PROGRAM_FILE,
                          PlaceholderProperty.getSoftwareFilesPlaceholderPropertyTypes(),
                          null),

    SOURCE_CODE_FILE_TEMPLATE(DeployedImplementationType.SOURCE_CODE_FILE.getDeployedImplementationType(),
                          "Create a " + DeployedImplementationType.SOURCE_CODE_FILE.getAssociatedTypeName() + " asset.",
                          DeployedImplementationType.SOURCE_CODE_FILE.getAssociatedTypeName(),
                          false,
                          "9c7013ef-f29b-4b01-a8ea-5ea14f64c67a",
                          DeployedImplementationType.SOURCE_CODE_FILE,
                          PlaceholderProperty.getSoftwareFilesPlaceholderPropertyTypes(),
                          null),

    BUILD_FILE_TEMPLATE(DeployedImplementationType.BUILD_FILE.getDeployedImplementationType(),
                        "Create a " + DeployedImplementationType.BUILD_FILE.getAssociatedTypeName() + " asset.",
                        DeployedImplementationType.BUILD_FILE.getAssociatedTypeName(),
                        false,
                        "fbb2fa2e-8bcb-402e-9be7-5c6db9f2c504",
                        DeployedImplementationType.BUILD_FILE,
                        PlaceholderProperty.getSoftwareFilesPlaceholderPropertyTypes(),
                        null),

    EXECUTABLE_FILE_TEMPLATE(DeployedImplementationType.EXECUTABLE_FILE.getDeployedImplementationType(),
                             "Create a " + DeployedImplementationType.EXECUTABLE_FILE.getAssociatedTypeName() + " asset.",
                             DeployedImplementationType.EXECUTABLE_FILE.getAssociatedTypeName(),
                             false,
                             "3d99a163-7a13-4576-a212-784010a8302a",
                             DeployedImplementationType.EXECUTABLE_FILE,
                             PlaceholderProperty.getSoftwareFilesPlaceholderPropertyTypes(),
                             null),

    SCRIPT_FILE_TEMPLATE(DeployedImplementationType.SCRIPT_FILE.getDeployedImplementationType(),
                         "Create a " + DeployedImplementationType.SCRIPT_FILE.getAssociatedTypeName() + " asset.",
                         DeployedImplementationType.SCRIPT_FILE.getAssociatedTypeName(),
                         false,
                         "dbd5e6bb-1ff8-46f4-a007-fb0485f68c92",
                         DeployedImplementationType.SCRIPT_FILE,
                         PlaceholderProperty.getSoftwareFilesPlaceholderPropertyTypes(),
                         null),

    PROPERTIES_FILE_TEMPLATE(DeployedImplementationType.PROPERTIES_FILE.getDeployedImplementationType(),
                             "Create a " + DeployedImplementationType.PROPERTIES_FILE.getAssociatedTypeName() + " asset.",
                             DeployedImplementationType.PROPERTIES_FILE.getAssociatedTypeName(),
                             false,
                             "3b281111-a0ef-4fc4-99e7-9a0ef84a7636",
                             DeployedImplementationType.PROPERTIES_FILE,
                             PlaceholderProperty.getSoftwareFilesPlaceholderPropertyTypes(),
                             null),

    YAML_FILE_TEMPLATE(DeployedImplementationType.YAML_FILE.getDeployedImplementationType(),
                             "Create a " + DeployedImplementationType.YAML_FILE.getAssociatedTypeName() + " asset.",
                             DeployedImplementationType.YAML_FILE.getAssociatedTypeName(),
                             false,
                             "2221855b-2b64-4b45-a2ee-c40adc5e2a64",
                             DeployedImplementationType.YAML_FILE,
                             PlaceholderProperty.getSoftwareFilesPlaceholderPropertyTypes(),
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
    FilesTemplateType(String                             templateName,
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
        return "5.4-SNAPSHOT";
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

        for (FilesTemplateType templateTypeEnum : FilesTemplateType.values())
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
