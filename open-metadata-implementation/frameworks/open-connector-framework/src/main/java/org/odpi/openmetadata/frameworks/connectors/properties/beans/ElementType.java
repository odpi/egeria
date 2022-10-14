/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The ElementType bean provides details of the type information associated with a metadata element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ElementType implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String       typeId          = null;
    private String       typeName        = null;
    private List<String> superTypeNames  = null;
    private long         typeVersion     = 0;
    private String       typeDescription = null;


    /**
     * Default constructor
     */
    public ElementType()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param templateType type to clone
     */
    public ElementType(ElementType templateType)
    {
        if (templateType != null)
        {
            typeId                            = templateType.getTypeId();
            typeName                          = templateType.getTypeName();
            superTypeNames                    = templateType.getSuperTypeNames();
            typeVersion                       = templateType.getTypeVersion();
            typeDescription                   = templateType.getTypeDescription();
        }
    }


    /**
     * Set up the unique identifier for the element's type.
     *
     * @param typeId String identifier
     */
    public void setTypeId(String typeId)
    {
        this.typeId = typeId;
    }


    /**
     * Return unique identifier for the element's type.
     *
     * @return element type id
     */
    public String getTypeId()
    {
        return typeId;
    }


    /**
     * Set up the name of this element's type
     *
     * @param typeName String name
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Return name of element's type.
     *
     * @return elementTypeName
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the list of type names for this type's supertypes.
     *
     * @param superTypeNames list of type names
     */
    public void setSuperTypeNames(List<String> superTypeNames)
    {
        this.superTypeNames = superTypeNames;
    }


    /**
     * Return the list of type names for this type's supertypes.
     *
     * @return list of type names
     */
    public List<String> getSuperTypeNames()
    {
        if (superTypeNames == null)
        {
            return null;
        }
        else if (superTypeNames.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(superTypeNames);
        }
    }


    /**
     * Set up the version number for this element's type
     *
     * @param typeVersion version number for the element type.
     */
    public void setTypeVersion(long typeVersion)
    {
        this.typeVersion = typeVersion;
    }


    /**
     * Return the version number for this element's type.
     *
     * @return elementTypeVersion version number for the element type.
     */
    public long getTypeVersion()
    {
        return typeVersion;
    }


    /**
     * Set up a short description of this element's type.
     *
     * @param typeDescription set up the description for this element's type
     */
    public void setTypeDescription(String typeDescription)
    {
        this.typeDescription = typeDescription;
    }


    /**
     * Return the description for this element's type.
     *
     * @return elementTypeDescription String description for the element type
     */
    public String getTypeDescription()
    {
        return typeDescription;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ElementType{" +
                       "elementTypeId='" + typeId + '\'' +
                       ", elementTypeName='" + typeName + '\'' +
                       ", elementSuperTypeNames=" + superTypeNames +
                       ", elementTypeVersion=" + typeVersion +
                       ", elementTypeDescription='" + typeDescription +
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
        if (!(objectToCompare instanceof ElementType))
        {
            return false;
        }
        ElementType that = (ElementType) objectToCompare;
        return getTypeVersion() == that.getTypeVersion() &&
                Objects.equals(getTypeId(), that.getTypeId()) &&
                Objects.equals(getTypeName(), that.getTypeName()) &&
                Objects.equals(getSuperTypeNames(), that.getSuperTypeNames()) &&
                Objects.equals(getTypeDescription(), that.getTypeDescription());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getTypeId(), getTypeName(), getSuperTypeNames(), getTypeVersion(), getTypeDescription());
    }
}
