/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DcatDocumentResponse is the response structure used on the Asset Catalog OMVS REST API calls that return the
 * description of an asset in the W3C Data Catalog Vocabulary (DCAT).  The document property holds a complete
 * JSON-LD document, including its context, that can be extracted and passed to any tool that understands DCAT.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DcatDocumentResponse extends FFDCResponseBase
{
    private Map<String, Object> document = null;


    /**
     * Default constructor
     */
    public DcatDocumentResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DcatDocumentResponse(DcatDocumentResponse template)
    {
        super(template);

        if (template != null)
        {
            this.document = template.getDocument();
        }
    }


    /**
     * Return the DCAT description as a JSON-LD document.
     *
     * @return JSON-LD document
     */
    public Map<String, Object> getDocument()
    {
        return document;
    }


    /**
     * Set up the DCAT description as a JSON-LD document.
     *
     * @param document JSON-LD document
     */
    public void setDocument(Map<String, Object> document)
    {
        this.document = document;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DcatDocumentResponse{" +
                       "document=" + document +
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
        DcatDocumentResponse that = (DcatDocumentResponse) objectToCompare;
        return Objects.equals(document, that.document);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), document);
    }
}
