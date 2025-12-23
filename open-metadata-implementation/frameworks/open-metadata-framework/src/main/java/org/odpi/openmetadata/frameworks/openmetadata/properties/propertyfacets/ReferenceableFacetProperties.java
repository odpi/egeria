/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.propertyfacets;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ReferenceableFacetProperties links an element to a property facet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReferenceableFacetProperties extends LabeledRelationshipProperties
{
    private String source = null;



    /**
     * Default constructor
     */
    public ReferenceableFacetProperties()
    {
        super();
        super.typeName = OpenMetadataType.REFERENCEABLE_FACET_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template to copy.
     */
    public ReferenceableFacetProperties(ReferenceableFacetProperties template)
    {
        super(template);

        if (template != null)
        {
            source = template.getSource();
        }
    }


    /**
     * Return the name of the method used to sample the full data collection.
     *
     * @return string
     */
    public String getSource() { return source; }


    /**
     * Set up the name of the method used to sample the full data collection.
     *
     * @param source string
     */
    public void setSource(String source)
    {
        this.source = source;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ReferenceableFacetProperties{" +
                "source='" + source + '\'' +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ReferenceableFacetProperties that = (ReferenceableFacetProperties) objectToCompare;
        return Objects.equals(source, that.source);
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), source);
    }
}