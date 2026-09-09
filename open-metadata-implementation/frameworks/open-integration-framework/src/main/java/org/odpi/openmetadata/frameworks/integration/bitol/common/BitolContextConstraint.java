/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents negative guidance for AI agents in the AI context of a data contract, schema object, data product or output port: something that must not be done with the element (RFC 0038).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class BitolContextConstraint
{
    private String                             id = null;
    private String                             constraint = null;
    private List<BitolAuthoritativeDefinition> authoritativeDefinitions = null;
    private List<String>                       tags = null;
    private List<BitolCustomProperty>          customProperties = null;


    /**
     * Default constructor
     */
    public BitolContextConstraint()
    {
    }


    /**
     * Return the stable identifier of the constraint.
     *
     * @return String
     */
    public String getId()
    {
        return id;
    }


    /**
     * Set up the stable identifier of the constraint.
     *
     * @param id String
     */
    public void setId(String id)
    {
        this.id = id;
    }


    /**
     * Return the constraint text.
     *
     * @return String
     */
    public String getConstraint()
    {
        return constraint;
    }


    /**
     * Set up the constraint text.
     *
     * @param constraint String
     */
    public void setConstraint(String constraint)
    {
        this.constraint = constraint;
    }


    /**
     * Return links to the sources behind this constraint.
     *
     * @return List<BitolAuthoritativeDefinition>
     */
    public List<BitolAuthoritativeDefinition> getAuthoritativeDefinitions()
    {
        return authoritativeDefinitions;
    }


    /**
     * Set up links to the sources behind this constraint.
     *
     * @param authoritativeDefinitions List<BitolAuthoritativeDefinition>
     */
    public void setAuthoritativeDefinitions(List<BitolAuthoritativeDefinition> authoritativeDefinitions)
    {
        this.authoritativeDefinitions = authoritativeDefinitions;
    }


    /**
     * Return the tags attached to this constraint.
     *
     * @return List<String>
     */
    public List<String> getTags()
    {
        return tags;
    }


    /**
     * Set up the tags attached to this constraint.
     *
     * @param tags List<String>
     */
    public void setTags(List<String> tags)
    {
        this.tags = tags;
    }


    /**
     * Return the custom properties of this constraint.
     *
     * @return List<BitolCustomProperty>
     */
    public List<BitolCustomProperty> getCustomProperties()
    {
        return customProperties;
    }


    /**
     * Set up the custom properties of this constraint.
     *
     * @param customProperties List<BitolCustomProperty>
     */
    public void setCustomProperties(List<BitolCustomProperty> customProperties)
    {
        this.customProperties = customProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "BitolContextConstraint{" +
                       "id=" + id +
                       ", constraint=" + constraint +
                       ", authoritativeDefinitions=" + authoritativeDefinitions +
                       ", tags=" + tags +
                       ", customProperties=" + customProperties +
                       '}';
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
        BitolContextConstraint that = (BitolContextConstraint) objectToCompare;
        return Objects.equals(id, that.id) &&
                       Objects.equals(constraint, that.constraint) &&
                       Objects.equals(authoritativeDefinitions, that.authoritativeDefinitions) &&
                       Objects.equals(tags, that.tags) &&
                       Objects.equals(customProperties, that.customProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(id, constraint, authoritativeDefinitions, tags, customProperties);
    }
}
