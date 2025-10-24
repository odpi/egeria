/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;


import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.PlaceholderPropertyType;

import java.util.ArrayList;
import java.util.List;

/**
 * CocoClinicalTrialPlaceholderProperty provides some standard definitions for placeholder properties used to pass properties
 * to services that use templates.
 */
public enum CocoClinicalTrialPlaceholderProperty
{
    CLINICAL_TRIAL_ID("clinicalTrialId", "Add the identifier of the clinical trial.", "string", "PROJ-CT-TBDF"),
    CLINICAL_TRIAL_NAME("clinicalTrialName", "Add the display name of the clinical trial.", "string", "Teddy Bear Drop Foot"),
    HOSPITAL_NAME("hospitalName", "Add the name of the hospital that sent the measurements.", "string", "Oak Dene Hospital"),
    CONTACT_NAME("contactName", "Add the name of the person at the hospital who is responsible for supplying the measurements.", "string", "Robbie Records"),
    CONTACT_EMAIL("contactEmail", "Add the email used to contact the person from the hospital.", "string", "RobbieRec@oak-dene.org"),
    ;

    public final String           name;
    public final String           description;
    public final String           dataType;
    public final String           example;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the placeholder property
     * @param description description of the placeholder property
     * @param dataType type of value of the placeholder property
     * @param example example of the placeholder property
     */
    CocoClinicalTrialPlaceholderProperty(String name,
                                         String description,
                                         String dataType,
                                         String example)
    {
        this.name        = name;
        this.description = description;
        this.dataType    = dataType;
        this.example     = example;
    }


    /**
     * Return the name of the placeholder property.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the placeholder to use when building templates.
     *
     * @return placeholder property
     */
    public String getPlaceholder()
    {
        return "~{" + name + "}~";
    }


    /**
     * Return the description of the placeholder property.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the data type for the placeholder property.
     *
     * @return data type name
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return an example of the placeholder property to help users understand how to set it up.
     *
     * @return example
     */
    public String getExample()
    {
        return example;
    }


    /**
     * Retrieve all the defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        for (CocoClinicalTrialPlaceholderProperty placeholderProperty : CocoClinicalTrialPlaceholderProperty.values())
        {
            placeholderPropertyTypes.add(placeholderProperty.getPlaceholderType());
        }

        placeholderPropertyTypes.add(PlaceholderProperty.FILE_PATH_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.FILE_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.RECEIVED_DATE.getPlaceholderType());

        return placeholderPropertyTypes;
    }



    /**
     * Return a summary of this enum to use in a service provider.
     *
     * @return placeholder property type
     */
    public PlaceholderPropertyType getPlaceholderType()
    {
        PlaceholderPropertyType placeholderPropertyType = new PlaceholderPropertyType();

        placeholderPropertyType.setName(name);
        placeholderPropertyType.setDescription(description);
        placeholderPropertyType.setDataType(dataType);
        placeholderPropertyType.setExample(example);
        placeholderPropertyType.setRequired(true);

        return placeholderPropertyType;
    }

    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "CocoClinicalTrialPlaceholderProperty{ name=" + name + "}";
    }
}
