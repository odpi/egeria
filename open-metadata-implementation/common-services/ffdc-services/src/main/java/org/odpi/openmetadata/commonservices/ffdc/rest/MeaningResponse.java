/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MeaningElement;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * MeaningResponse is the response structure used on the Asset Consumer OMAS REST API calls that returns a
 * Glossary Term object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MeaningResponse extends FFDCResponseBase
{
    private MeaningElement meaning = null;

    /**
     * Default constructor
     */
    public MeaningResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public MeaningResponse(MeaningResponse template)
    {
        super(template);

        if (template != null)
        {
            this.meaning = template.getMeaning();
        }
    }


    /**
     * Return the glossary term object.
     *
     * @return glossary term object
     */
    public MeaningElement getMeaning()
    {
        return meaning;
    }


    /**
     * Set up the glossary term object.
     *
     * @param meaning - glossary term object
     */
    public void setMeaning(MeaningElement meaning)
    {
        this.meaning = meaning;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "MeaningResponse{" +
                "meaning=" + meaning +
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
        if (!(objectToCompare instanceof MeaningResponse that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(getMeaning(), that.getMeaning());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        if (meaning == null)
        {
            return super.hashCode();
        }
        else
        {
            return meaning.hashCode();
        }
    }

}
