/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetconsumer.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.GlossaryTerm;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ConnectionResponse is the response structure used on the Asset Consumer OMAS REST API calls that returns a
 * Connection object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MeaningResponse extends AssetConsumerOMASAPIResponse
{
    private GlossaryTerm glossaryTerm = null;

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
            this.glossaryTerm = template.getGlossaryTerm();
        }
    }


    /**
     * Return the Connection object.
     *
     * @return connection
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
     * Set up the Connection object.
     *
     * @param glossaryTerm - connection object
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
        return "MeaningResponse{" +
                "glossaryTerm=" + glossaryTerm +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
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
        if (!(objectToCompare instanceof MeaningResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        MeaningResponse that = (MeaningResponse) objectToCompare;
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
