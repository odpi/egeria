/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RegulationProperties defines a regulation of interest.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RegulationProperties extends GovernanceDefinitionProperties
{
    private String       regulationSource = null;
    private List<String> regulators       = null;


    /**
     * Default Constructor
     */
    public RegulationProperties()
    {
        super();
        super.typeName = OpenMetadataType.REGULATION.typeName;
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public RegulationProperties(RegulationProperties template)
    {
        super(template);

        if (template != null)
        {
            this.regulationSource = template.getRegulationSource();
            this.regulators = template.getRegulators();
        }
    }


    /**
     * Return the source of the regulation.
     *
     * @return organization name
     */
    public String getRegulationSource()
    {
        return regulationSource;
    }


    /**
     * Set up the source of the regulation.
     *
     * @param regulationSource organization name
     */
    public void setRegulationSource(String regulationSource)
    {
        this.regulationSource = regulationSource;
    }


    /**
     * Return the list of regulators for this regulation.
     *
     * @return list of organization names
     */
    public List<String> getRegulators()
    {
        return regulators;
    }


    /**
     * Set up the list of organization names.
     *
     * @param regulators list of organization names
     */
    public void setRegulators(List<String> regulators)
    {
        this.regulators = regulators;
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "RegulationProperties{" +
                "regulationSource='" + regulationSource + '\'' +
                ", regulators=" + regulators +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        RegulationProperties that = (RegulationProperties) objectToCompare;
        return Objects.equals(regulationSource, that.regulationSource) &&
                Objects.equals(regulators, that.regulators);
    }



    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), regulationSource, regulators);
    }
}
