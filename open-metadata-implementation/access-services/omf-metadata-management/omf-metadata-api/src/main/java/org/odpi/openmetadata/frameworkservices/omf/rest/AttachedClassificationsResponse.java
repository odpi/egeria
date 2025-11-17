/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.omf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The response object for passing back a list of omf AttachedClassifications
 * or an exception if the request failed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AttachedClassificationsResponse extends OMAGOMFAPIResponse
{
    private List<AttachedClassification> classifications = null;


    /**
     * Default constructor
     */
    public AttachedClassificationsResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AttachedClassificationsResponse(AttachedClassificationsResponse template)
    {
        super(template);

        if (template != null)
        {
            classifications = template.getClassifications();
        }
    }


    /**
     * Return the list of metadata elements.
     *
     * @return result object
     */
    public List<AttachedClassification> getClassifications()
    {
        return classifications;
    }


    /**
     * Set up the metadata element to return.
     *
     * @param classifications result object
     */
    public void setClassifications(List<AttachedClassification> classifications)
    {
        this.classifications = classifications;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AttachedClassificationsResponse{" +
                "classifications=" + classifications +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AttachedClassificationsResponse that = (AttachedClassificationsResponse) objectToCompare;
        return Objects.equals(classifications, that.classifications);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), classifications);
    }
}
