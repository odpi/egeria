/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermCategorization;
import org.odpi.openmetadata.accessservices.assetmanager.properties.MetadataCorrelationProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * CategorizationRequestBody describes the request body used when linking a glossary term to a category.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CategorizationRequestBody extends AssetManagerIdentifiersRequestBody
{
    private static final long    serialVersionUID = 1L;

    private GlossaryTermCategorization    categorizationProperties      = null;


    /**
     * Default constructor
     */
    public CategorizationRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public CategorizationRequestBody(CategorizationRequestBody template)
    {
        super(template);

        if (template != null)
        {
            categorizationProperties = template.getCategorizationProperties();
        }
    }


    /**
     * Return the properties for the relationship.
     *
     * @return properties object
     */
    public GlossaryTermCategorization getCategorizationProperties()
    {
        return categorizationProperties;
    }


    /**
     * Set up the properties for the relationship.
     *
     * @param categorizationProperties properties object
     */
    public void setCategorizationProperties(GlossaryTermCategorization categorizationProperties)
    {
        this.categorizationProperties = categorizationProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CategorizationRequestBody{" +
                       "categorizationProperties=" + categorizationProperties +
                       ", assetManagerGUID='" + getAssetManagerGUID() + '\'' +
                       ", assetManagerName='" + getAssetManagerName() + '\'' +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        CategorizationRequestBody that = (CategorizationRequestBody) objectToCompare;
        return Objects.equals(getCategorizationProperties(), that.getCategorizationProperties());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), categorizationProperties);
    }
}
