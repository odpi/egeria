/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GlossaryTermRelationshipStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GlossaryTermCategorization describes a type of relationship between a glossary term and a glossary category in a glossary.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlossaryTermCategorization extends RelationshipProperties
{
    private String                         description = null;
    private GlossaryTermRelationshipStatus status      = null;



    /**
     * Default constructor
     */
    public GlossaryTermCategorization()
    {
        super();
        super.setTypeName(OpenMetadataType.TERM_CATEGORIZATION.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public GlossaryTermCategorization(GlossaryTermCategorization template)
    {
        if (template != null)
        {
            description = template.getDescription();
            status = template.getStatus();
        }
    }


    /**
     * Set up description of the categorization.
     *
     * @param description String
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the description for the categorization.
     *
     * @return String description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up whether this relationship should be used.
     *
     * @param status status enum (draft, active, deprecated, obsolete, other)
     */
    public void setStatus(GlossaryTermRelationshipStatus status)
    {
        this.status = status;
    }


    /**
     * Returns whether this relationship should be used.
     *
     * @return status enum (draft, active, deprecated, obsolete, other)
     */
    public GlossaryTermRelationshipStatus getStatus()
    {
        return status;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "GlossaryTermCategorization{" +
                "description='" + description + '\'' +
                ", status=" + status +
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
        GlossaryTermCategorization that = (GlossaryTermCategorization) objectToCompare;
        return Objects.equals(description, that.description) && status == that.status;
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), description, status);
    }
}
