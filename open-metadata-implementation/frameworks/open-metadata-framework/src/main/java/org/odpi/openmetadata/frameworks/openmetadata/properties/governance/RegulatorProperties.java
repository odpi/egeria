/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RegulatorProperties links a regulator organization to the regulation it enforces.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RegulatorProperties extends LabeledRelationshipProperties
{
    private String         scope          = null;


    /**
     * Default constructor
     */
    public RegulatorProperties()
    {
        super();
        super.typeName = OpenMetadataType.REGULATOR_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RegulatorProperties(RegulatorProperties template)
    {
        super(template);

        if (template != null)
        {
            this.scope = template.getScope();
        }
    }


    /**
     * Return the scope of responsibility of the regulator for this regulation.
     *
     * @return string
     */
    public String getScope()
    {
        return scope;
    }


    /**
     * Set up the scope of responsibility of the regulator for this regulation.
     *
     * @param scope string
     */
    public void setScope(String scope)
    {
        this.scope = scope;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "RegulatorProperties{" +
                "scope='" + scope + '\'' +
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
        RegulatorProperties that = (RegulatorProperties) objectToCompare;
        return Objects.equals(scope, that.scope);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), scope);
    }
}
