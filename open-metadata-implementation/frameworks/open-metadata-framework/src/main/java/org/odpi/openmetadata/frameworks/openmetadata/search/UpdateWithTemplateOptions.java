/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The UpdateWithTemplateOptions class provides the additional options when updating elements using templates.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UpdateWithTemplateOptions extends UpdateOptions
{
    private boolean mergeClassifications = true;


    /**
     * Typical constructor
     */
    public UpdateWithTemplateOptions()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public UpdateWithTemplateOptions(UpdateWithTemplateOptions template)
    {
        super(template);

        if (template != null)
        {
            mergeClassifications = template.getMergeClassifications();
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public UpdateWithTemplateOptions(MetadataSourceOptions template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public UpdateWithTemplateOptions(UpdateOptions template)
    {
        super(template);
    }


    /**
     * Return whether  the classification be merged or replace the target entity?e (default is true).
     *
     * @return boolean
     */
    public boolean getMergeClassifications()
    {
        return mergeClassifications;
    }


    /**
     * Set up whether the code allowed to retrieve an existing element, or must it create a new one - the match is done on the
     * qualified name (default is false).
     *
     * @param mergeClassifications boolean
     */
    public void setMergeClassifications(boolean mergeClassifications)
    {
        this.mergeClassifications = mergeClassifications;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "UpdateWithTemplateOptions{" +
                "mergeClassifications=" + mergeClassifications +
                "} " + super.toString();
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        UpdateWithTemplateOptions that = (UpdateWithTemplateOptions) objectToCompare;
        return mergeClassifications == that.mergeClassifications;
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), mergeClassifications);
    }
}

