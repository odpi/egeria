/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.properties.GlossaryTerm;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GlossaryTermResponse is the response structure used on the Asset Consumer OMAS REST API calls that returns a
 * Glossary Term object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GlossaryTermResponse extends FFDCResponseBase
{
    private GlossaryTerm glossaryTerm = null;

    /**
     * Default constructor
     */
    public GlossaryTermResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GlossaryTermResponse(GlossaryTermResponse template)
    {
        super(template);

        if (template != null)
        {
            this.glossaryTerm = template.getGlossaryTerm();
        }
    }


    /**
     * Return the glossary term object.
     *
     * @return glossary term object
     */
    public GlossaryTerm getGlossaryTerm()
    {
        if (glossaryTerm == null)
        {
            return null;
        }
        else
        {
            return glossaryTerm;
        }
    }


    /**
     * Set up the glossary term object.
     *
     * @param glossaryTerm - glossary term object
     */
    public void setGlossaryTerm(GlossaryTerm glossaryTerm)
    {
        this.glossaryTerm = glossaryTerm;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GlossaryTermResponse{" +
                "glossaryTerm=" + glossaryTerm +
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
        if (!(objectToCompare instanceof GlossaryTermResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        GlossaryTermResponse that = (GlossaryTermResponse) objectToCompare;
        return Objects.equals(getGlossaryTerm(), that.getGlossaryTerm());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        if (glossaryTerm == null)
        {
            return super.hashCode();
        }
        else
        {
            return glossaryTerm.hashCode();
        }
    }

}
