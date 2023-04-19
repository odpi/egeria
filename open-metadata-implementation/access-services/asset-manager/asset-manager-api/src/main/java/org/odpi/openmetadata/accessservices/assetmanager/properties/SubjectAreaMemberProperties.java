/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.properties;

import java.io.Serial;
import java.util.Objects;

/**
 * A subject area defines a group of definitions for governing assets related to a specific topic.  The subject area definition defines
 * how the assets related to the topic should be managed.  This classification associates an element as one of the materials used to
 * implement the subject area.
 */
public class SubjectAreaMemberProperties extends ClassificationProperties
{
    @Serial
    private static final long serialVersionUID = 1L;

    private String subjectAreaName  = null;


    /**
     * Default constructor
     */
    public SubjectAreaMemberProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SubjectAreaMemberProperties(SubjectAreaMemberProperties template)
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
        return "SubjectAreaMemberProperties{" +
                       "effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", subjectAreaName='" + subjectAreaName + '\'' +
                       '}';
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
        if (! (objectToCompare instanceof SubjectAreaMemberProperties))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        SubjectAreaMemberProperties that = (SubjectAreaMemberProperties) objectToCompare;
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
