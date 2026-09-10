/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the sourceCode job facet.  It captures the source code of the job.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-0-1/SourceCodeJobFacet.json#/$defs/SourceCodeJobFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageSourceCodeJobFacet extends OpenLineageJobFacet
{
    private String language = null;
    private String sourceCode = null;


    /**
     * Default constructor
     */
    public OpenLineageSourceCodeJobFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-1/SourceCodeJobFacet.json#/$defs/SourceCodeJobFacet"));
    }


    /**
     * Return the language in which the source code of the job was written.
     *
     * @return string
     */
    public String getLanguage()
    {
        return language;
    }


    /**
     * Set up the language in which the source code of the job was written.
     *
     * @param language string
     */
    public void setLanguage(String language)
    {
        this.language = language;
    }


    /**
     * Return the source code of the job.
     *
     * @return string
     */
    public String getSourceCode()
    {
        return sourceCode;
    }


    /**
     * Set up the source code of the job.
     *
     * @param sourceCode string
     */
    public void setSourceCode(String sourceCode)
    {
        this.sourceCode = sourceCode;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageSourceCodeJobFacet{" +
                       "language='" + language + '\'' +
                       ", sourceCode='" + sourceCode + '\'' +
                       ", _producer=" + get_producer() +
                       ", _schemaURL=" + get_schemaURL() +
                       ", _deleted=" + get_deleted() +
                       ", additionalProperties=" + getAdditionalProperties() +
                       '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
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
        OpenLineageSourceCodeJobFacet that = (OpenLineageSourceCodeJobFacet) objectToCompare;
        return Objects.equals(language, that.language) &&
                       Objects.equals(sourceCode, that.sourceCode);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), language, sourceCode);
    }
}
