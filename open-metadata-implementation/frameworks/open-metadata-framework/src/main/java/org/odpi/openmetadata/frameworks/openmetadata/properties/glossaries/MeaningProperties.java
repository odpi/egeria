/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MeaningProperties is a cut-down summary of a glossary term to aid the asset consumer in understanding the content
 * of an asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MeaningProperties extends ReferenceableProperties
{
    private String summary = null;


    /**
     * Default constructor
     */
    public MeaningProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.GLOSSARY_TERM.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public MeaningProperties(MeaningProperties template)
    {
        super(template);

        if (template != null)
        {
            summary = template.getSummary();
        }
    }


    /**
     * Return the short summary of the term.
     *
     * @return string summary
     */
    public String getSummary()
    {
        return summary;
    }


    /**
     * Set up the short summary of the term.
     *
     * @param summary string summary
     */
    public void setSummary(String summary)
    {
        this.summary = summary;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "MeaningProperties{" +
                "summary='" + summary + '\'' +
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
        MeaningProperties that = (MeaningProperties) objectToCompare;
        return Objects.equals(summary, that.summary);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), summary);
    }
}