/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.properties.GlossaryTerm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 *  GlossaryTermListResponse returns a list of meanings from the server.   The list may be too long to
 *  retrieve in a single call so there is support for paging of replies.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GlossaryTermListResponse extends FFDCResponseBase
{
    private static final long    serialVersionUID = 1L;

    private List<GlossaryTerm> meanings            = null;
    private int                startingFromElement = 0;


    /**
     * Default constructor
     */
    public GlossaryTermListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GlossaryTermListResponse(GlossaryTermListResponse template)
    {
        super(template);

        if (template != null)
        {
            this.startingFromElement = template.getStartingFromElement();
            this.meanings = template.getMeanings();
        }
    }


    /**
     * Return the list of glossary terms in the response.
     *
     * @return list of glossary terms
     */
    public List<GlossaryTerm> getMeanings()
    {
        if (meanings == null)
        {
            return null;
        }
        else if (meanings.isEmpty())
        {
            return null;
        }
        else
        {
            List<GlossaryTerm>  clonedList = new ArrayList<>();

            for (GlossaryTerm  existingElement : meanings)
            {
                clonedList.add(new GlossaryTerm(existingElement));
            }

            return clonedList;
        }
    }


    /**
     * Set up the list of glossary terms for the response.
     *
     * @param meanings list
     */
    public void setMeanings(List<GlossaryTerm> meanings)
    {
        this.meanings = meanings;
    }


    /**
     * Return the starting element number from the server side list that this response contains.
     *
     * @return int
     */
    public int getStartingFromElement()
    {
        return startingFromElement;
    }


    /**
     * Set up the starting element number from the server side list that this response contains.
     *
     * @param startingFromElement int
     */
    public void setStartingFromElement(int startingFromElement)
    {
        this.startingFromElement = startingFromElement;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GlossaryTermListResponse{" +
                "meanings=" + meanings +
                ", startingFromElement=" + startingFromElement +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                ", actionDescription='" + getActionDescription() + '\'' +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        GlossaryTermListResponse that = (GlossaryTermListResponse) objectToCompare;
        return getStartingFromElement() == that.getStartingFromElement() &&
                Objects.equals(getMeanings(), that.getMeanings());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getMeanings(), getStartingFromElement());
    }
}
