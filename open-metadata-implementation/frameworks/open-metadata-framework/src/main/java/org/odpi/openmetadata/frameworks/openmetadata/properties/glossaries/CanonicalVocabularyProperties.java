/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CanonicalVocabularyProperties is used to classify a glossary that has no term definitions with
 * the same name.  This means there is only one definition for each term.  Typically, the terms are also of a similar
 * level of granularity and are limited to a specific scope of use.
 * <br><br>
 * Canonical vocabularies are typically used to semantically classify assets in an unambiguous way.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CanonicalVocabularyProperties extends ClassificationBeanProperties
{
    private String scope = null;


    /**
     * Default constructor
     */
    public CanonicalVocabularyProperties()
    {
        super();
        super.typeName = OpenMetadataType.CANONICAL_VOCABULARY_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor for a primary key.
     *
     * @param template template object to copy.
     */
    public CanonicalVocabularyProperties(CanonicalVocabularyProperties template)
    {
        super(template);

        if (template != null)
        {
            scope = template.getScope();
        }
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
        return "CanonicalVocabularyProperties{" +
                "scope='" + scope + '\'' +
                "} " + super.toString();
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
        if (! (objectToCompare instanceof CanonicalVocabularyProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(scope, that.scope);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), scope);
    }
}
