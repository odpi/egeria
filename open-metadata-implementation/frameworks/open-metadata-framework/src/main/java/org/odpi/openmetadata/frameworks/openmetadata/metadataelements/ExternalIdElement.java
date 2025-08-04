/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EndpointElement contains the properties and header for an endpoint retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExternalIdElement extends OpenMetadataRootElement
{
    private List<RelatedMetadataElementSummary> externalIdScope = null;


    /**
     * Default constructor
     */
    public ExternalIdElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ExternalIdElement(ExternalIdElement template)
    {
        super(template);

        if (template != null)
        {
            externalIdScope = template.getExternalIdScope();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ExternalIdElement(OpenMetadataRootElement template)
    {
        super(template);
    }


    /**
     * Return the list of external sources that use this external identifier.
     *
     * @return related elements
     */
    public List<RelatedMetadataElementSummary> getExternalIdScope()
    {
        return externalIdScope;
    }


    /**
     * Set up the list of external sources that use this external identifier.
     *
     * @param externalIdScope related elements
     */
    public void setExternalIdScope(List<RelatedMetadataElementSummary> externalIdScope)
    {
        this.externalIdScope = externalIdScope;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ExternalIdElement{" +
                "externalIdScope=" + externalIdScope +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ExternalIdElement that = (ExternalIdElement) objectToCompare;
        return Objects.equals(externalIdScope, that.externalIdScope);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), externalIdScope);
    }
}
