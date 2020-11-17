/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetmanager.properties.MetadataCorrelationProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CanonicalVocabularyClassificationRequestBody is used to classify a glossary that has no term definitions with
 * the same name.  This means there is only one definition for each term.  Typically the terms are also of a similar
 * level of granularity and are limited to a specific scope of use.
 *
 * Canonical vocabularies are used to semantically classify assets in an unambiguous way.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CanonicalVocabularyClassificationRequestBody implements Serializable
{
    private static final long   serialVersionUID = 1L;

    private MetadataCorrelationProperties metadataCorrelationProperties = null;
    private String                        scope = null;


    /**
     * Default constructor
     */
    public CanonicalVocabularyClassificationRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor for a primary key.
     *
     * @param template template object to copy.
     */
    public CanonicalVocabularyClassificationRequestBody(CanonicalVocabularyClassificationRequestBody template)
    {
        if (template != null)
        {
            metadataCorrelationProperties = template.getMetadataCorrelationProperties();
            scope = template.getScope();
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
     * Return the scope that the terms in the glossary covers.
     *
     * @return string description
     */
    public String getScope()
    {
        return scope;
    }


    /**
     * Set up the scope that the terms in the glossary covers.
     *
     * @param scope string description
     */
    public void setScope(String scope)
    {
        this.scope = scope;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "CanonicalVocabularyClassificationRequestBody{" +
                       "metadataCorrelationProperties=" + metadataCorrelationProperties +
                       ", scope='" + scope + '\'' +
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
        CanonicalVocabularyClassificationRequestBody that = (CanonicalVocabularyClassificationRequestBody) objectToCompare;
        return Objects.equals(getMetadataCorrelationProperties(), that.getMetadataCorrelationProperties()) &&
                       Objects.equals(getScope(), that.getScope());
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getMetadataCorrelationProperties(), getScope());
    }
}
