/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermContextDefinition;
import org.odpi.openmetadata.accessservices.assetmanager.properties.MetadataCorrelationProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ContextDefinitionClassificationRequestBody is used to classify a glossary term to say it described a context.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContextDefinitionClassificationRequestBody extends UpdateRequestBody
{
    private static final long   serialVersionUID = 1L;

    private GlossaryTermContextDefinition contextDefinition = null;


    /**
     * Default constructor
     */
    public ContextDefinitionClassificationRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor for a primary key.
     *
     * @param template template object to copy.
     */
    public ContextDefinitionClassificationRequestBody(ContextDefinitionClassificationRequestBody template)
    {
        super(template);

        if (template != null)
        {
            contextDefinition = template.getContextDefinition();
        }
    }


    /**
     * Return the context definition for this term.
     *
     * @return string description
     */
    public GlossaryTermContextDefinition getContextDefinition()
    {
        return contextDefinition;
    }


    /**
     * Set up the contextDefinition for this term.
     *
     * @param contextDefinition string description
     */
    public void setContextDefinition(GlossaryTermContextDefinition contextDefinition)
    {
        this.contextDefinition = contextDefinition;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ContextDefinitionClassificationRequestBody{" +
                       "contextDefinition=" + contextDefinition +
                       ", metadataCorrelationProperties=" + getMetadataCorrelationProperties() +
                       ", effectiveTime=" + getEffectiveTime() +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ContextDefinitionClassificationRequestBody that = (ContextDefinitionClassificationRequestBody) objectToCompare;
        return Objects.equals(contextDefinition, that.contextDefinition);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), contextDefinition);
    }
}
