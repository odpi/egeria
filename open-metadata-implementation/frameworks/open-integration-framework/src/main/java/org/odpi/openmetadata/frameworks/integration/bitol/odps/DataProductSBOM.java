/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odps;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a link to a software bill of materials (SBOM) for an output port of an Open Data Product
 * Standard (ODPS) data product.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataProductSBOM
{
    private String type = null;
    private String url = null;


    /**
     * Default constructor
     */
    public DataProductSBOM()
    {
    }


    /**
     * Return the type of SBOM reference.  The default is external.
     *
     * @return string type name
     */
    public String getType()
    {
        return type;
    }


    /**
     * Set up the type of SBOM reference.  The default is external.
     *
     * @param type string type name
     */
    public void setType(String type)
    {
        this.type = type;
    }


    /**
     * Return the URL of the SBOM.
     *
     * @return string URL
     */
    public String getUrl()
    {
        return url;
    }


    /**
     * Set up the URL of the SBOM.
     *
     * @param url string URL
     */
    public void setUrl(String url)
    {
        this.url = url;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataProductSBOM{" +
                       "type='" + type + '\'' +
                       ", url='" + url + '\'' +
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
        DataProductSBOM that = (DataProductSBOM) objectToCompare;
        return Objects.equals(type, that.type) &&
                       Objects.equals(url, that.url);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(type, url);
    }
}
