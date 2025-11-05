/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.automatedcuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.TemplateProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Capture information about a catalog template.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CatalogTemplate extends RefDataElementBase
{
    private String versionIdentifier  = null;


    /**
     * Default constructor
     */
    public CatalogTemplate()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CatalogTemplate(CatalogTemplate template)
    {
        super(template);

        if (template != null)
        {
            versionIdentifier = template.getVersionIdentifier();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CatalogTemplate(TemplateProperties template)
    {
        super();

        if (template != null)
        {
            displayName = template.getDisplayName();
            description = template.getDescription();
            versionIdentifier = template.getVersionIdentifier();
            additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Return the version identifier.
     *
     * @return string
     */
    public String getVersionIdentifier()
    {
        return versionIdentifier;
    }


    /**
     * Set up the version identifier.
     *
     * @param versionIdentifier string
     */
    public void setVersionIdentifier(String versionIdentifier)
    {
        this.versionIdentifier = versionIdentifier;
    }


    /**
     * Generate a string containing the properties.
     *
     * @return string value
     */
    @Override
    public String toString()
    {
        return "CatalogTemplate{" +
                "versionIdentifier='" + versionIdentifier + '\'' +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        CatalogTemplate that = (CatalogTemplate) objectToCompare;
        return Objects.equals(versionIdentifier, that.versionIdentifier);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), versionIdentifier);
    }
}
