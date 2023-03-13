/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SchemaAttributesResponse is the response structure used on the OMAS REST API calls that return a
 * list of SchemaAttribute objects as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SchemaAttributesResponse extends PagedResponse
{
    private static final long    serialVersionUID = 1L;

    private List<SchemaAttribute> responseObjects = null;


    /**
     * Default constructor
     */
    public SchemaAttributesResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SchemaAttributesResponse(SchemaAttributesResponse template)
    {
        super(template);

        if (template != null)
        {
            this.responseObjects = template.getList();
        }
    }


    /**
     * Return the responseObjects result.
     *
     * @return list of response objects
     */
    public List<SchemaAttribute> getList()
    {
        if (responseObjects == null)
        {
            return null;
        }
        else if (responseObjects.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(responseObjects);
        }
    }


    /**
     * Set up the responseObjects result.
     *
     * @param responseObjects list of response objects
     */
    public void setList(List<SchemaAttribute> responseObjects)
    {
        this.responseObjects = responseObjects;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SchemaAttributesResponse{" +
                "list=" + getList() +
                ", startingFromElement=" + getStartingFromElement() +
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
        if (!(objectToCompare instanceof SchemaAttributesResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        SchemaAttributesResponse that = (SchemaAttributesResponse) objectToCompare;
        return Objects.equals(responseObjects, that.responseObjects);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(responseObjects);
    }
}
