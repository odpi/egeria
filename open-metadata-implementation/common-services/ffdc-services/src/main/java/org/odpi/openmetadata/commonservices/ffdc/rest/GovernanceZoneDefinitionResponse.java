/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceZoneDefinition;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GovernanceZoneDefinitionResponse is the response structure used on the OMAS REST API calls that return the properties
 * for a governance zone with the linked governance definitions.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceZoneDefinitionResponse extends FFDCResponseBase
{
    private GovernanceZoneDefinition properties = null;


    /**
     * Default constructor
     */
    public GovernanceZoneDefinitionResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceZoneDefinitionResponse(GovernanceZoneDefinitionResponse template)
    {
        super(template);

        if (template != null)
        {
            this.properties = template.getProperties();
        }
    }


    /**
     * Return the properties result.
     *
     * @return bean
     */
    public GovernanceZoneDefinition getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties result.
     *
     * @param properties - bean
     */
    public void setProperties(GovernanceZoneDefinition properties)
    {
        this.properties = properties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceZoneDefinitionResponse{" +
                "properties=" + properties +
                "} " + super.toString();
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
        if (!(objectToCompare instanceof GovernanceZoneDefinitionResponse that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(properties, that.properties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(properties);
    }
}
