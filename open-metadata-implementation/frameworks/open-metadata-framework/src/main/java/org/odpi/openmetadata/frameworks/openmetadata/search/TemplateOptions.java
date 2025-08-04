/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The TemplateOptions class provides the additional options when creating elements using templates.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TemplateOptions extends NewElementOptions
{
    private Date    effectiveFrom      = null;
    private Date    effectiveTo        = null;
    private boolean deepCopy           = true;
    private boolean templateSubstitute = false;
    private boolean allowRetrieve      = true;


    /**
     * Typical constructor
     */
    public TemplateOptions()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public TemplateOptions(TemplateOptions template)
    {
        super(template);

        if (template != null)
        {
            effectiveFrom      = template.getEffectiveFrom();
            effectiveTo        = template.getEffectiveTo();
            deepCopy           = template.getDeepCopy();
            templateSubstitute = template.getIsTemplateSubstitute();
            allowRetrieve      = template.getAllowRetrieve();
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public TemplateOptions(MetadataSourceOptions template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public TemplateOptions(NewElementOptions template)
    {
        super(template);
    }


    /**
     * Return the date/time that this element is effective from (null means effective from the epoch).
     *
     * @return date object
     */
    public Date getEffectiveFrom()
    {
        return effectiveFrom;
    }


    /**
     * Set up the date/time that this element is effective from (null means effective from the epoch).
     *
     * @param effectiveFrom date object
     */
    public void setEffectiveFrom(Date effectiveFrom)
    {
        this.effectiveFrom = effectiveFrom;
    }


    /**
     * Return the date/time that element is effective to (null means that it is effective indefinitely into the future).
     *
     * @return date object
     */
    public Date getEffectiveTo()
    {
        return effectiveTo;
    }


    /**
     * Set the date/time that element is effective to (null means that it is effective indefinitely into the future).
     *
     * @param effectiveTo date object
     */
    public void setEffectiveTo(Date effectiveTo)
    {
        this.effectiveTo = effectiveTo;
    }


    /**
     * Return whether the template creation extends to the anchored elements (true) or just the direct entity (false).
     *
     * @return boolean
     */
    public boolean getDeepCopy()
    {
        return deepCopy;
    }


    /**
     * Set up whether the template creation extends to the anchored elements (true) or just the direct entity (false).
     *
     * @param deepCopy boolean
     */
    public void setDeepCopy(boolean deepCopy)
    {
        this.deepCopy = deepCopy;
    }


    /**
     * Return whether the new element should have the TemplateSubstitute classification attached, or not.
     * (A template substitute acts a proxy to the real entity that an element created by the template will be linked to.)
     *
     * @return boolean
     */
    public boolean getIsTemplateSubstitute()
    {
        return templateSubstitute;
    }


    /**
     * Set up whether the new element should have the TemplateSubstitute classification attached, or not.
     * (A template substitute acts a proxy to the real entity that an element created by the template will be linked to.)
     *
     * @param templateSubstitute boolean
     */
    public void setIsTemplateSubstitute(boolean templateSubstitute)
    {
        this.templateSubstitute = templateSubstitute;
    }


    /**
     * Return whether the code allowed to retrieve an existing element, or must it create a new one - the match is done on the
     * qualified name (default is false).
     *
     * @return boolean
     */
    public boolean getAllowRetrieve()
    {
        return allowRetrieve;
    }


    /**
     * Set up whether the code allowed to retrieve an existing element, or must it create a new one - the match is done on the
     * qualified name (default is false).
     *
     * @param allowRetrieve boolean
     */
    public void setAllowRetrieve(boolean allowRetrieve)
    {
        this.allowRetrieve = allowRetrieve;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "TemplateOptions{" +
                "effectiveFrom=" + effectiveFrom +
                ", effectiveTo=" + effectiveTo +
                ", deepCopy=" + deepCopy +
                ", templateSubstitute=" + templateSubstitute +
                ", allowRetrieve=" + allowRetrieve +
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
        TemplateOptions that = (TemplateOptions) objectToCompare;
        return allowRetrieve == that.allowRetrieve &&
                deepCopy == that.deepCopy &&
                templateSubstitute == that.templateSubstitute &&
                Objects.equals(effectiveFrom, that.effectiveFrom) &&
                Objects.equals(effectiveTo, that.effectiveTo);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), effectiveFrom, effectiveTo, deepCopy, templateSubstitute, allowRetrieve);
    }
}

