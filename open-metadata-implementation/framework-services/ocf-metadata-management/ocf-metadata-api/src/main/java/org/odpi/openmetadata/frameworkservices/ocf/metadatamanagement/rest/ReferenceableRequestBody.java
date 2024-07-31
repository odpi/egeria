/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Meaning;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ReferenceableRequestBody provides a structure for passing a referenceables' properties as a request body over a REST API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReferenceableRequestBody extends OCFOMASAPIRequestBody
{
    protected String                      typeName             = null;
    protected List<ElementClassification> classifications      = null;
    protected String                      qualifiedName        = null;
    protected List<Meaning>               meanings             = null;
    protected Map<String, String>         additionalProperties = null;
    protected Map<String, Object>         extendedProperties   = null;


    /**
     * Default constructor
     */
    public ReferenceableRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ReferenceableRequestBody(ReferenceableRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.typeName = template.getTypeName();
            this.classifications = template.getClassifications();
            this.qualifiedName = template.getQualifiedName();
            this.meanings = template.getMeanings();
            this.additionalProperties = template.getAdditionalProperties();
            this.extendedProperties = template.getExtendedProperties();
        }
    }


    /**
     * Return the open metadata type name of this object - this is used to create a subtype of
     * the referenceable.  Any properties associated with this subtype are passed as extended properties.
     *
     * @return string type name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the open metadata type name of this object - this is used to create a subtype of
     * the referenceable.  Any properties associated with this subtype are passed as extended properties.
     *
     * @param typeName string type name
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Return the classifications associated with this referenceable.
     *
     * @return list of classifications with their properties
     */
    public List<ElementClassification> getClassifications()
    {
        return classifications;
    }


    /**
     * Set up the list of classifications associated with this referenceable.
     *
     * @param classifications list of classifications with their properties
     */
    public void setClassifications(List<ElementClassification> classifications)
    {
        this.classifications = classifications;
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
     * Set up the fully qualified name.
     *
     * @param qualifiedName String name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
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
     * Set up additional properties.
     *
     * @param additionalProperties Additional properties object
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return the assigned meanings for this metadata entity.
     *
     * @return list of meanings
     */
    public List<Meaning> getMeanings()
    {
        if (meanings == null)
        {
            return null;
        }
        else if (meanings.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(meanings);
        }
    }


    /**
     * Set up the assigned meanings for this metadata entity.
     *
     * @param meanings list of meanings
     */
    public void setMeanings(List<Meaning> meanings)
    {
        this.meanings = meanings;
    }


    /**
     * Return the properties that are defined for a subtype of referenceable but are not explicitly
     * supported by the bean.
     *
     * @return map of properties
     */
    public Map<String, Object> getExtendedProperties()
    {
        if (extendedProperties == null)
        {
            return null;
        }
        else if (extendedProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(extendedProperties);
        }
    }


    /**
     * Set up the properties that are defined for a subtype of referenceable but are not explicitly
     * supported by the bean.
     *
     * @param extendedProperties map of properties
     */
    public void setExtendedProperties(Map<String, Object> extendedProperties)
    {
        this.extendedProperties = extendedProperties;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ReferenceableRequestBody{" +
                "typeName='" + typeName + '\'' +
                ", classifications=" + classifications +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", meanings=" + meanings +
                ", additionalProperties=" + additionalProperties +
                ", extendedProperties=" + extendedProperties +
                '}';
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
        ReferenceableRequestBody that = (ReferenceableRequestBody) objectToCompare;
        return Objects.equals(getTypeName(), that.getTypeName()) &&
                Objects.equals(getClassifications(), that.getClassifications()) &&
                Objects.equals(getQualifiedName(), that.getQualifiedName()) &&
                Objects.equals(getMeanings(), that.getMeanings()) &&
                Objects.equals(getAdditionalProperties(), that.getAdditionalProperties()) &&
                Objects.equals(getExtendedProperties(), that.getExtendedProperties());
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getTypeName(), getClassifications(), getQualifiedName(), getMeanings(),
                            getAdditionalProperties(), getExtendedProperties());
    }
}
