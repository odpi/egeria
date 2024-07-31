/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GlossaryTermStatus;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GlossaryTermStatusRequestBody describes the request body used to update a glossary term's status.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GlossaryTermStatusRequestBody extends UpdateRequestBody
{
    private GlossaryTermStatus glossaryTermStatus = null;


    /**
     * Default constructor
     */
    public GlossaryTermStatusRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public GlossaryTermStatusRequestBody(GlossaryTermStatusRequestBody template)
    {
        super(template);

        if (template != null)
        {
            glossaryTermStatus = template.getGlossaryTermStatus();
        }
    }


    /**
     * Return the status for the element.
     *
     * @return enum object
     */
    public GlossaryTermStatus getGlossaryTermStatus()
    {
        return glossaryTermStatus;
    }


    /**
     * Set up the status for the element.
     *
     * @param glossaryTermStatus enum object
     */
    public void setGlossaryTermStatus(GlossaryTermStatus glossaryTermStatus)
    {
        this.glossaryTermStatus = glossaryTermStatus;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GlossaryTermStatusRequestBody{" +
                       "glossaryTermStatus=" + glossaryTermStatus +
                       ", metadataCorrelationProperties=" + getMetadataCorrelationProperties() +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        GlossaryTermStatusRequestBody that = (GlossaryTermStatusRequestBody) objectToCompare;
        return glossaryTermStatus == that.glossaryTermStatus;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), glossaryTermStatus);
    }
}
