/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.APIProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;

/**
 * APIElement contains the properties and header for a DeployedAPI entity retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class APIElement implements MetadataElement
{
    private ElementHeader elementHeader = null;
    private APIProperties apiProperties = null;

    /**
     * Default constructor
     */
    public APIElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public APIElement(APIElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            apiProperties = template.getAPIProperties();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return details of the API
     *
     * @return API properties
     */
    public APIProperties getAPIProperties()
    {
        return apiProperties;
    }


    /**
     * Set up API properties
     *
     * @param apiProperties API properties
     */
    public void setAPIProperties(APIProperties apiProperties)
    {
        this.apiProperties = apiProperties;
    }

    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "APIElement{" +
                "elementHeader=" + elementHeader +
                ", apiProperties=" + apiProperties +
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
        APIElement that = (APIElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(apiProperties, that.apiProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, apiProperties);
    }
}
