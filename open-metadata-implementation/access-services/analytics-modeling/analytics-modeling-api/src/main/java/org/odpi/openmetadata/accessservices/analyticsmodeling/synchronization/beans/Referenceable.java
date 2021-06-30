/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.beans;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * Many open metadata entities are referenceable. It means that they have a qualified name and additional
 * properties.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = Asset.class, name = "Asset"),
                @JsonSubTypes.Type(value = SchemaElement.class, name = "SchemaElement"),
        })
public class Referenceable {

    protected String              qualifiedName        = null;
    protected Map<String, String> additionalProperties = null;
    
    
    /**
     * GUID of the entity in repository used to create bean.
     */
    protected String              guid                 = null;


    /**
     * Default constructor
     */
    public Referenceable()
    {
        super();
    }


    /**
     * Copy/clone constructor.  Retrieves values from the supplied template
     *
     * @param template element to copy
     */
    public Referenceable(Referenceable template)
    {
        if (template != null)
        {
            qualifiedName                           = template.getQualifiedName();
            additionalProperties                    = template.getAdditionalProperties();
        }
    }


    /**
     * Set up the fully qualified name.
     *
     * @param qualifiedName String name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * Returns the stored guid for the metadata entity.
     * If no guid is available then the null is returned.
     *
     * @return guid
     */
    public String getGuid()
    {
        return guid;
    }



    /**
     * Set up the guid.
     *
     * @param guid String name
     */
    public void setGuid(String guid)
    {
        this.guid = guid;
    }


    /**
     * Returns the stored qualified name property for the metadata entity.
     * If no qualified name is available then the empty string is returned.
     *
     * @return qualifiedName
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }

    /**
     * Set up additional properties.
     *
     * @param additionalProperties Additional properties object
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return a copy of the additional properties.  Null means no additional properties are available.
     *
     * @return AdditionalProperties
     */
    public Map<String, String> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalProperties);
        }
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Referenceable{" +
                "qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     * 
     * GUID is not used in equals(...) to compare beans from repository and request. 
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

        Referenceable that = (Referenceable) objectToCompare;
        return Objects.equals(qualifiedName, that.qualifiedName) &&
                Objects.equals(additionalProperties, that.additionalProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), qualifiedName, additionalProperties);
    }
}

