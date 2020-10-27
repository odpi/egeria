/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.rest;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.KeyPattern;
import org.odpi.openmetadata.adminservices.configuration.properties.PermittedSynchronization;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalIdentifierRequestBody describes the properties describing an external identifier.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.PROPERTY,
              property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ExternalIdentifierProperties.class, name = "ExternalIdentifierProperties"),
        })
public class ExternalIdentifierRequestBody implements Serializable
{
    private static final long   serialVersionUID = 1L;

    private String                   assetManagerGUID           = null;
    private String                   assetManagerName           = null;
    private String                   externalIdentifier         = null;
    private KeyPattern               keyPattern                 = null;
    private Map<String, String>      mappingProperties          = null;

    /**
     * Default constructor
     */
    public ExternalIdentifierRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public ExternalIdentifierRequestBody(ExternalIdentifierRequestBody template)
    {
        if (template != null)
        {
            assetManagerGUID = template.getAssetManagerGUID();
            assetManagerName = template.getAssetManagerName();
            externalIdentifier = template.getExternalIdentifier();
            keyPattern = template.getKeyPattern();
            mappingProperties = template.getMappingProperties();
        }
    }


    /**
     * Return the unique identifier of the software server capability that represents the asset manager.
     *
     * @return string guid
     */
    public String getAssetManagerGUID()
    {
        return assetManagerGUID;
    }


    /**
     * Set up the unique identifier of the software server capability that represents the asset manager.
     *
     * @param assetManagerGUID string guid
     */
    public void setAssetManagerGUID(String assetManagerGUID)
    {
        this.assetManagerGUID = assetManagerGUID;
    }


    /**
     * Return the qualified name of the software server capability that represents the asset manager.
     *
     * @return string name
     */
    public String getAssetManagerName()
    {
        return assetManagerName;
    }


    /**
     * Set up the qualified name of the software server capability that represents the asset manager.
     *
     * @param assetManagerName string name
     */
    public void setAssetManagerName(String assetManagerName)
    {
        this.assetManagerName = assetManagerName;
    }


    /**
     * Return the unique identifier used in the external asset manager for this element.
     *
     * @return string identifier
     */
    public String getExternalIdentifier()
    {
        return externalIdentifier;
    }


    /**
     * Set up the unique identifier used in the external asset manager for this element.
     *
     * @param externalIdentifier string identifier
     */
    public void setExternalIdentifier(String externalIdentifier)
    {
        this.externalIdentifier = externalIdentifier;
    }


    /**
     * Set up the key pattern for the external identifier.
     *
     * @param keyPattern String name
     */
    public void setKeyPattern(KeyPattern keyPattern)
    {
        this.keyPattern = keyPattern;
    }


    /**
     * Returns the key pattern for the external identifier.
     *
     * @return String name
     */
    public KeyPattern getKeyPattern()
    {
        return keyPattern;
    }


    /**
     * Return any additional properties to help with the mapping of the external identifier.
     *
     * @return name-value pairs
     */
    public Map<String, String> getMappingProperties()
    {
        if (mappingProperties != null)
        {
            return null;
        }
        else if (mappingProperties.isEmpty())
        {
            return null;
        }

        return mappingProperties;
    }


    /**
     * Set up any additional properties to help with the mapping of the external identifier.
     *
     * @param mappingProperties name-value pairs
     */
    public void setMappingProperties(Map<String, String> mappingProperties)
    {
        this.mappingProperties = mappingProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ExternalIdentifierRequestBody{" +
                       "assetManagerGUID='" + assetManagerGUID + '\'' +
                       ", assetManagerName='" + assetManagerName + '\'' +
                       ", externalIdentifier='" + externalIdentifier + '\'' +
                       ", keyPattern=" + keyPattern +
                       ", mappingProperties=" + mappingProperties +
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
        ExternalIdentifierRequestBody that = (ExternalIdentifierRequestBody) objectToCompare;
        return Objects.equals(getAssetManagerGUID(), that.getAssetManagerGUID()) &&
                       Objects.equals(getAssetManagerName(), that.getAssetManagerName()) &&
                       Objects.equals(getExternalIdentifier(), that.getExternalIdentifier()) &&
                       getKeyPattern() == that.getKeyPattern() &&
                       Objects.equals(getMappingProperties(), that.getMappingProperties());
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getAssetManagerGUID(), getAssetManagerName(), getExternalIdentifier(), getKeyPattern(), getMappingProperties());
    }
}
