/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.feedback;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * InformalTagProperties stores information about a tag connected to an asset.
 * InformalTags provide informal classifications to assets
 * and can be added at any time.
 * <br><br>
 * The content of the tag is a personal judgement
 * and there is no formal review of the tags.  However, they can be used as a basis for crowdsourcing
 * Glossary terms.
 * <br><br>
 * Private InformalTags are only returned to the user that created them.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformalTagProperties extends OpenMetadataRootProperties
{
    private String displayName = null;
    private String description = null;


    /**
     * Default constructor
     */
    public InformalTagProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.INFORMAL_TAG.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public InformalTagProperties(InformalTagProperties template)
    {
        super(template);

        if (template != null)
        {
            displayName = template.getDisplayName();
            description = template.getDescription();
        }
    }


    /**
     * Return the name of the tag.  It is not valid to have a tag with no name.  However, there is a point where
     * the tag object is created and the tag name not yet set, so null is a possible response.
     *
     * @return String tag name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the name of the tag.  It is not valid to have a tag with no name.  However, there is a point where
     * the tag object is created and the tag name not yet set, so null is a possible response.
     *
     * @param displayName String tag name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the tag description null means no description is available.
     *
     * @return String tag description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the tag description null means no description is available.
     *
     * @param tagDescription  tag description
     */
    public void setDescription(String tagDescription) {
        this.description = tagDescription;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "InformalTagProperties{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
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
        if (!(objectToCompare instanceof InformalTagProperties that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(getDisplayName(), that.getDisplayName()) &&
                Objects.equals(getDescription(), that.getDescription());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), displayName, description);
    }
}