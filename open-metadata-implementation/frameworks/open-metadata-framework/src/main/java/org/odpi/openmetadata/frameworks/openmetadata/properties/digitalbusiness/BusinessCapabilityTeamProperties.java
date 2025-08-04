/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * BusinessCapabilityTeamProperties describes the scope of responsibility that a team/organization has to a business capability.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessCapabilityTeamProperties extends RelationshipBeanProperties
{
    private String scope = null;


    /**
     * Default constructor
     */
    public BusinessCapabilityTeamProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.BUSINESS_CAPABILITY_TEAM_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public BusinessCapabilityTeamProperties(BusinessCapabilityTeamProperties template)
    {
        super(template);

        if (template != null)
        {
            scope = template.getScope();
        }
    }


    /**
     * Set up the display name for UIs and reports.
     *
     * @param scope String name
     */
    public void setScope(String scope)
    {
        this.scope = scope;
    }


    /**
     * Returns the stored display name property for the foreign key.
     *
     * @return String name
     */
    public String getScope()
    {
        return scope;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "BusinessCapabilityTeamProperties{" +
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
        if (! (objectToCompare instanceof BusinessCapabilityTeamProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
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
