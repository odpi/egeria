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
 * NamingStandardRuleProperties defines the patterns for a naming standard..
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NamingStandardRuleProperties extends GovernanceControlProperties
{
    private List<String> namePatterns = null;


    /**
     * Default Constructor
     */
    public NamingStandardRuleProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.NAMING_STANDARD_RULE.typeName);
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public NamingStandardRuleProperties(NamingStandardRuleProperties template)
    {
        super(template);

        if (template != null)
        {
            this.namePatterns = template.getNamePatterns();
        }
    }


    /**
     * Return the list of name patterns that make up this standard.
     *
     * @return list
     */
    public List<String> getNamePatterns()
    {
        return namePatterns;
    }


    /**
     * Set up the list of name patterns that make up this standard.
     *
     * @param namePatterns list
     */
    public void setNamePatterns(List<String> namePatterns)
    {
        this.namePatterns = namePatterns;
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "GovernanceStrategyProperties{" +
                "businessImperatives=" + namePatterns +
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
        NamingStandardRuleProperties that = (NamingStandardRuleProperties) objectToCompare;
        return Objects.equals(namePatterns, that.namePatterns);
    }



    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), namePatterns);
    }
}
