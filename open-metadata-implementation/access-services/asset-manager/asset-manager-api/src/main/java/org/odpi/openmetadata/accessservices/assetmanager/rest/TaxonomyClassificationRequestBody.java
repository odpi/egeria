/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetmanager.properties.MetadataCorrelationProperties;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TaxonomyClassificationRequestBody is used to classify a glossary that has the terms organized in a taxonomy.
 * This means each term is attached to one, and only one category and the categories are organized as a hierarchy
 * with a single root category.
 *
 * Taxonomies are used as a way of organizing assets and other related metadata.  The terms in the taxonomy
 * are linked to the assets etc and as such they are logically categorized by the linked category.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxonomyClassificationRequestBody implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    private MetadataCorrelationProperties metadataCorrelationProperties = null;
    private String                        organizingPrinciple = null;


    /**
     * Default constructor
     */
    public TaxonomyClassificationRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor for a primary key.
     *
     * @param template template object to copy.
     */
    public TaxonomyClassificationRequestBody(TaxonomyClassificationRequestBody template)
    {
        if (template != null)
        {
            metadataCorrelationProperties = template.getMetadataCorrelationProperties();
            organizingPrinciple   = template.getOrganizingPrinciple();
        }
    }


    /**
     * Return the properties used to correlate the external metadata element with the open metadata element.
     *
     * @return properties object
     */
    public MetadataCorrelationProperties getMetadataCorrelationProperties()
    {
        return metadataCorrelationProperties;
    }


    /**
     * Set up the properties used to correlate the external metadata element with the open metadata element.
     *
     * @param metadataCorrelationProperties properties object
     */
    public void setMetadataCorrelationProperties(MetadataCorrelationProperties metadataCorrelationProperties)
    {
        this.metadataCorrelationProperties = metadataCorrelationProperties;
    }


    /**
     * Return the organizing principle used to create the taxonomy in the glossary.
     *
     * @return string description
     */
    public String getOrganizingPrinciple()
    {
        return organizingPrinciple;
    }


    /**
     * Set up the organizing principle used to create the taxonomy in the glossary.
     *
     * @param organizingPrinciple string description
     */
    public void setOrganizingPrinciple(String organizingPrinciple)
    {
        this.organizingPrinciple = organizingPrinciple;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "TaxonomyClassificationRequestBody{" +
                       "metadataCorrelationProperties=" + metadataCorrelationProperties +
                       ", organizingPrinciple='" + organizingPrinciple + '\'' +
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
        TaxonomyClassificationRequestBody that = (TaxonomyClassificationRequestBody) objectToCompare;
        return Objects.equals(getMetadataCorrelationProperties(), that.getMetadataCorrelationProperties()) &&
                       Objects.equals(getOrganizingPrinciple(), that.getOrganizingPrinciple());
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getMetadataCorrelationProperties(), getOrganizingPrinciple());
    }
}
