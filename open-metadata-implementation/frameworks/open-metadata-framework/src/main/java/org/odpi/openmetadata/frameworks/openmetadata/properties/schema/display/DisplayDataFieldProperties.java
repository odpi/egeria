/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.schema.display;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DisplayDataFieldProperties describes a data field that is part of a report, form or query.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DisplayDataFieldProperties extends SchemaAttributeProperties
{
    private boolean inputField = false;


    /**
     * Default constructor
     */
    public DisplayDataFieldProperties()
    {
        super();
        super.typeName = OpenMetadataType.DISPLAY_DATA_FIELD.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public DisplayDataFieldProperties(DisplayDataFieldProperties template)
    {
        super(template);

        if (template != null)
        {
            inputField = template.getInputField();
        }
    }


    /**
     * Return whether this data field is accepting new data from the end user or not.
     *
     * @return boolean flag
     */
    public boolean getInputField()
    {
        return inputField;
    }


    /**
     * Set up whether this data field is accepting new data from the end user or not.
     *
     * @param inputField boolean flag
     */
    public void setInputField(boolean inputField)
    {
        this.inputField = inputField;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DisplayDataFieldProperties{" +
                "inputField=" + inputField +
                "} " + super.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        DisplayDataFieldProperties that = (DisplayDataFieldProperties) objectToCompare;
        return inputField == that.inputField;
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), inputField);
    }
}
