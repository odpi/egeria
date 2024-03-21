/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.oif.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * MetadataSourceRequestBody is the request body structure used on OIF REST API calls that requests a new
 * element to represent a new metadata source.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MetadataSourceRequestBody
{
    private String typeName                   = null;
    private String classificationName         = null;
    private String qualifiedName              = null;
    private String deployedImplementationType = null;


    /**
     * Default constructor
     */
    public MetadataSourceRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public MetadataSourceRequestBody(MetadataSourceRequestBody template)
    {
        if (template != null)
        {
            typeName                   = template.getTypeName();
            classificationName         = template.getClassificationName();
            qualifiedName              = template.getQualifiedName();
            deployedImplementationType = template.getDeployedImplementationType();
        }
    }


    /**
     * Return the type name for the metadata source.
     *
     * @return string name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the type name for the metadata source.
     *
     * @param typeName string
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Return the classification name.
     *
     * @return string name
     */
    public String getClassificationName()
    {
        return classificationName;
    }


    /**
     * Set up the classification name.
     *
     * @param classificationName string
     */
    public void setClassificationName(String classificationName)
    {
        this.classificationName = classificationName;
    }


    /**
     * Return the qualified name.
     *
     * @return string name
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Set up the qualified name.
     *
     * @param qualifiedName string
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * Return the description of the type of capability this is.
     *
     * @return string description
     */
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Set up the description of the type of capability this is.
     *
     * @param deployedImplementationType string
     */
    public void setDeployedImplementationType(String deployedImplementationType)
    {
        this.deployedImplementationType = deployedImplementationType;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "NameRequestBody{" +
                "typeName='" + typeName + '\'' +
                ", classificationName='" + classificationName + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", deployedImplementationType='" + deployedImplementationType + '\'' +
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
        MetadataSourceRequestBody that = (MetadataSourceRequestBody) objectToCompare;
        return Objects.equals(typeName, that.typeName) &&
                       Objects.equals(classificationName, that.classificationName) &&
                       Objects.equals(qualifiedName, that.qualifiedName) &&
                Objects.equals(deployedImplementationType, that.deployedImplementationType);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(typeName, classificationName, qualifiedName, deployedImplementationType);
    }
}
