/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CommentRequestBody provides a structure for passing a comment as a request body over a REST API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscoveryRequestRequestBody extends ODFOMASAPIRequestBody
{
    private static final long    serialVersionUID = 1L;

    protected Map<String, String>    analysisParameters     = null;
    protected List<String>           annotationTypes        = null;


    /**
     * Default constructor
     */
    public DiscoveryRequestRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DiscoveryRequestRequestBody(DiscoveryRequestRequestBody template)
    {
        super(template);

        if (template != null)
        {
            analysisParameters = template.getAnalysisParameters();
            annotationTypes = template.getAnnotationTypes();
        }
    }


    /**
     * Return the parameters used to drive the discovery service's analysis.
     *
     * @return map storing the analysis parameters
     */
    public Map<String, String> getAnalysisParameters()
    {
        return analysisParameters;
    }


    /**
     * Set up the parameters used to drive the discovery service's analysis.
     *
     * @param analysisParameters map storing the analysis parameters
     */
    public void setAnalysisParameters(Map<String, String> analysisParameters)
    {
        this.analysisParameters = analysisParameters;
    }


    /**
     * Return the list of annotation types.
     *
     * @return list of type names
     */
    public List<String> getAnnotationTypes()
    {
        return annotationTypes;
    }


    /**
     * Set up the status of the discovery process.
     *
     * @param annotationTypes list of types
     */
    public void setAnnotationTypes(List<String> annotationTypes)
    {
        this.annotationTypes = annotationTypes;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "DiscoveryRequestRequestBody{" +
                "analysisParameters=" + analysisParameters +
                ", annotationTypes=" + annotationTypes +
                '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
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
        DiscoveryRequestRequestBody that = (DiscoveryRequestRequestBody) objectToCompare;
        return Objects.equals(getAnalysisParameters(), that.getAnalysisParameters()) &&
                Objects.equals(getAnnotationTypes(), that.getAnnotationTypes());
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getAnalysisParameters(), getAnnotationTypes());
    }
}
