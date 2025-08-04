/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A subject area defines a group of definitions for governing assets related to a specific topic.  The subject area definition defines
 * how the assets related to the topic should be managed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SubjectAreaProperties extends ClassificationBeanProperties
{
    private String subjectAreaName  = null;


    /**
     * Default constructor
     */
    public SubjectAreaProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SUBJECT_AREA_CLASSIFICATION.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SubjectAreaProperties(SubjectAreaProperties template)
    {
        super(template);

        if (template != null)
        {
            this.subjectAreaName = template.getSubjectAreaName();
        }
    }


    /**
     * Return the name of the subject area - this is added to the SubjectArea classification.
     *
     * @return string name
     */
    public String getSubjectAreaName()
    {
        return subjectAreaName;
    }


    /**
     * Set up the name of the subject area - this is added to the SubjectArea classification.
     *
     * @param subjectAreaName string name
     */
    public void setSubjectAreaName(String subjectAreaName)
    {
        this.subjectAreaName = subjectAreaName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SubjectAreaProperties{" +
                "subjectAreaName='" + subjectAreaName + '\'' +
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
        if (! (objectToCompare instanceof SubjectAreaProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(subjectAreaName, that.subjectAreaName);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getSubjectAreaName());
    }
}
