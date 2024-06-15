/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossarymanager.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetmanager.properties.TemplateProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * TemplateRequestBody describes the request body used to create/update elements from templates.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TemplateRequestBody extends EffectiveTimeQueryRequestBody
{
    private TemplateProperties elementProperties = null;
    private String             parentGUID        = null;


    /**
     * Default constructor
     */
    public TemplateRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public TemplateRequestBody(TemplateRequestBody template)
    {
        super(template);

        if (template != null)
        {
            elementProperties = template.getElementProperties();
            parentGUID        = template.getParentGUID();
        }
    }


    /**
     * Return the properties for the element.
     *
     * @return properties object
     */
    public TemplateProperties getElementProperties()
    {
        return elementProperties;
    }


    /**
     * Set up the properties for the element.
     *
     * @param elementProperties properties object
     */
    public void setElementProperties(TemplateProperties elementProperties)
    {
        this.elementProperties = elementProperties;
    }


    /**
     * Return an optional parent GUID to attach the new element to.
     *
     * @return guid
     */
    public String getParentGUID()
    {
        return parentGUID;
    }


    /**
     * Set up an optional parent GUID to attach the new element to.
     *
     * @param parentGUID guid
     */
    public void setParentGUID(String parentGUID)
    {
        this.parentGUID = parentGUID;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "TemplateRequestBody{" +
                       "elementProperties=" + elementProperties +
                       ", parentGUID='" + parentGUID + '\'' +
                       ", effectiveTime=" + getEffectiveTime() +
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
        if (! (objectToCompare instanceof TemplateRequestBody that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(elementProperties, that.elementProperties) && Objects.equals(parentGUID, that.parentGUID);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementProperties, parentGUID);
    }
}
