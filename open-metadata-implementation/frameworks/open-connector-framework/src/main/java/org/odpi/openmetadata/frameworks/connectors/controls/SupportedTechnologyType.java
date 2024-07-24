/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.controls;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SupportedTechnologyType characterises one of the deployed implementation types that this connector works with.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SupportedTechnologyType
{
    /**
     * Value to use for the name of the request parameter.
     */
    private String name = null;

    /**
     * Description of the request parameter.
     */
    private String description = null;

    /**
     * The type name of the value for this parameter.
     */
    private String dataType = null;


    /**
     * Default constructor
     */
    public SupportedTechnologyType()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SupportedTechnologyType(SupportedTechnologyType template)
    {
        if (template != null)
        {
            this.name        = template.getName();
            this.description = template.getDescription();
            this.dataType    = template.getDataType();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SupportedTechnologyType(DeployedImplementationType template)
    {
        if (template != null)
        {
            this.name        = template.getDeployedImplementationType();
            this.description = template.getDescription();
            this.dataType    = template.getAssociatedTypeName();
        }
    }


    /**
     * Return the string to use as the name of the request parameter.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the string to use as the name of the request parameter.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }



    /**
     * Return the description of the request parameter.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the request parameter.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the data type name for the parameter value.
     *
     * @return data type
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Set up data type name for the parameter value.
     *
     * @param dataType data type
     */
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }


    /**
     * convert the array of technology types into a list of technology types.
     *
     * @param technologyTypes technology types
     * @return list of analysis step types for the service provider
     */
    public static List<SupportedTechnologyType> getSupportedTechnologyTypes(SupportedTechnologyType[] technologyTypes)
    {
        if ((technologyTypes == null) || (technologyTypes.length == 0))
        {
            return null;
        }

        return new ArrayList<>(Arrays.asList(technologyTypes));
    }


    /**
     * convert the array of deployed implementation types into a list of supported technology types.
     *
     * @param deployedImplementationTypes deployed implementation type enums
     * @return list of analysis step types for the service provider
     */
    public static List<SupportedTechnologyType> getSupportedTechnologyTypes(DeployedImplementationType[] deployedImplementationTypes)
    {
        if ((deployedImplementationTypes == null) || (deployedImplementationTypes.length == 0))
        {
            return null;
        }

        List<SupportedTechnologyType> supportedTechnologyTypes = new ArrayList<>();

        for (DeployedImplementationType deployedImplementationType : deployedImplementationTypes)
        {
            supportedTechnologyTypes.add(new SupportedTechnologyType(deployedImplementationType));
        }

        return supportedTechnologyTypes;
    }

    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SupportedTechnologyType{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dataType='" + dataType + '\'' +
                '}';
    }

    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof SupportedTechnologyType that))
        {
            return false;
        }
        return Objects.equals(dataType, that.dataType) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(name, description, dataType);
    }
}
