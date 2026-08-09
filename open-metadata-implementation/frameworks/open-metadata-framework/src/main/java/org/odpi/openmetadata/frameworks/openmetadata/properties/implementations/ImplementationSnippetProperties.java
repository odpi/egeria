/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.implementations;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ImplementationSnippetProperties describes a code fragment defining a data structure that can be linked to the relevant
 * schema to show how the schema should be implemented.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ImplementationSnippetProperties extends ReferenceableProperties
{
    private String snippet                 = null;
    private String implementationLanguage  = null;
    private String usage                   = null;


    /**
     * Default constructor
     */
    public ImplementationSnippetProperties()
    {
        super();
        super.typeName = OpenMetadataType.IMPLEMENTATION_SNIPPET.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ImplementationSnippetProperties(ImplementationSnippetProperties template)
    {
        super(template);

        if (template != null)
        {
            snippet                = template.getSnippet();
            implementationLanguage = template.getImplementationLanguage();
            usage                  = template.getUsage();
        }
    }


    /**
     * Return the concrete implementation of the definition of a schema type or data field.
     *
     * @return string text
     */
    public String getSnippet()
    {
        return snippet;
    }


    /**
     * Set up the concrete implementation of the definition of a schema type or data field.
     *
     * @param snippet string text
     */
    public void setSnippet(String snippet)
    {
        this.snippet = snippet;
    }


    /**
     * Return the name of the language used to implement this component.
     *
     * @return string name
     */
    public String getImplementationLanguage()
    {
        return implementationLanguage;
    }


    /**
     * Set up the name of the language used to implement this component.
     *
     * @param implementationLanguage string name
     */
    public void setImplementationLanguage(String implementationLanguage)
    {
        this.implementationLanguage = implementationLanguage;
    }


    /**
     * Return guidance on how the element should be used.
     *
     * @return string text
     */
    public String getUsage()
    {
        return usage;
    }


    /**
     * Set up guidance on how the element should be used.
     *
     * @param usage string text
     */
    public void setUsage(String usage)
    {
        this.usage = usage;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ImplementationSnippetProperties{" +
                "snippet='" + snippet + '\'' +
                ", implementationLanguage='" + implementationLanguage + '\'' +
                ", usage='" + usage + '\'' +
                "} " + super.toString();
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ImplementationSnippetProperties that = (ImplementationSnippetProperties) objectToCompare;
        return Objects.equals(snippet, that.snippet) &&
                       Objects.equals(implementationLanguage, that.implementationLanguage) &&
                       Objects.equals(usage, that.usage);
    }


    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), snippet, implementationLanguage, usage);
    }
}
