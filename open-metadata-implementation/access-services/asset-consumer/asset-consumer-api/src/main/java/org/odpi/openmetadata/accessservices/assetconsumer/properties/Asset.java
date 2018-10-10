/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Asset describes the basic properties of an Asset that include its name, type, description and owner.
 * With this information it is possible to drill down and understand more details such as its location,
 * classification and internal structure through the Asset Consumer's integration with the Connected Asset OMAS.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Asset.class, name = "AssetCollectionMember")
})
public class Asset extends AssetConsumerElementHeader
{
    private String               guid                 = null;
    private String               typeName             = null;
    private String               typeDescription      = null;
    private String               qualifiedName        = null;
    private String               displayName          = null;
    private String               description          = null;
    private String               owner                = null;
    private List<String>         zoneMembership       = null;
    private Map<String, Object>  additionalProperties = null;
    private List<Classification> classifications      = null;


    /**
     * Default constructor
     */
    public Asset()
    {
        super();
    }


    /**
     * Copy/clone constructor
     */
    public Asset(Asset   template)
    {
        super(template);

        if (template != null)
        {
            this.guid = template.getGUID();
            this.typeName = template.getTypeName();
            this.typeDescription = template.getTypeDescription();
            this.qualifiedName = template.getQualifiedName();
            this.displayName = template.getDisplayName();
            this.description = template.getDescription();
            this.owner = template.getOwner();
            this.zoneMembership = template.getZoneMembership();
            this.additionalProperties = template.getAdditionalProperties();
            this.classifications = template.getClassifications();
        }
    }


    /**
     * Return the unique identifier for this asset.
     *
     * @return string guid
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Set up the unique identifier for this asset.
     *
     * @param guid string guid for this asset
     */
    public void setGUID(String guid)
    {
        this.guid = guid;
    }


    /**
     * Return the name for this Asset's type.
     *
     * @return string name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the name for this Asset's type.
     *
     * @param typeName string name
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Return the description for this Asset's type.
     *
     * @return string description
     */
    public String getTypeDescription()
    {
        return typeDescription;
    }


    /**
     * Set up the description for this Asset's type.
     *
     * @param typeDescription string description
     */
    public void setTypeDescription(String typeDescription)
    {
        this.typeDescription = typeDescription;
    }


    /**
     * Return the unique name for this asset.
     *
     * @return string name
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Set up the unique name for this asset.
     *
     * @param qualifiedName string name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * Return the display name for this asset (normally a shortened for of the qualified name).
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the display name for this asset (normally a shortened for of the qualified name).
     *
     * @param displayName string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description for this asset.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description for this asset.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the owner for this asset.  This is the user id of the person or engine that is responsible for
     * managing this asset.
     *
     * @return string id
     */
    public String getOwner()
    {
        return owner;
    }


    /**
     * Set up the owner for this asset.  This is the user id of the person or engine that is responsible for
     * managing this asset.
     *
     * @param owner string id
     */
    public void setOwner(String owner)
    {
        this.owner = owner;
    }


    /**
     * Return the names of the zones that this asset is a member of.
     *
     * @return list of zone names
     */
    public List<String> getZoneMembership()
    {
        if (zoneMembership == null)
        {
            return null;
        }
        else if (zoneMembership.isEmpty())
        {
            return null;
        }
        else
        {
            return zoneMembership;
        }
    }


    /**
     * Set up the names of the zones that this asset is a member of.
     *
     * @param zoneMembership list of zone names
     */
    public void setZoneMembership(List<String> zoneMembership)
    {
        this.zoneMembership = zoneMembership;
    }


    /**
     * Return any additional properties associated with the asset.
     *
     * @return map of property names to property values
     */
    public Map<String, Object> getAdditionalProperties()
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
     * Set up any additional properties associated with the asset.
     *
     * @param additionalProperties map of property names to property values
     */
    public void setAdditionalProperties(Map<String, Object> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return the list of active classifications for this asset.
     *
     * @return list of classification objects
     */
    public List<Classification> getClassifications()
    {
        if (classifications == null)
        {
            return null;
        }
        else if (classifications.isEmpty())
        {
            return null;
        }
        else
        {
            return classifications;
        }
    }


    /**
     * Set up the list of active classifications for this asset.
     *
     * @param classifications list of classification objects
     */
    public void setClassifications(List<Classification> classifications)
    {
        this.classifications = classifications;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "Asset{" +
                "guid='" + guid + '\'' +
                ", typeName='" + typeName + '\'' +
                ", typeDescription='" + typeDescription + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", owner='" + owner + '\'' +
                ", zoneMembership='" + zoneMembership + '\'' +
                ", additionalProperties='" + additionalProperties + '\'' +
                ", classifications='" + classifications + '\'' +
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
        if (!(objectToCompare instanceof Asset))
        {
            return false;
        }
        Asset asset = (Asset) objectToCompare;
        return  Objects.equals(getGUID(), asset.getGUID()) &&
                Objects.equals(getTypeName(), asset.getTypeName()) &&
                Objects.equals(getTypeDescription(), asset.getTypeDescription()) &&
                Objects.equals(getQualifiedName(), asset.getQualifiedName()) &&
                Objects.equals(getDisplayName(), asset.getDisplayName()) &&
                Objects.equals(getDescription(), asset.getDescription()) &&
                Objects.equals(getOwner(), asset.getOwner()) &&
                Objects.equals(getAdditionalProperties(), asset.getAdditionalProperties()) &&
                Objects.equals(getClassifications(), asset.getClassifications());

    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(getGUID(),
                            getTypeName(),
                            getTypeDescription(),
                            getQualifiedName(),
                            getDisplayName(),
                            getDescription(),
                            getOwner(),
                            getZoneMembership(),
                            getAdditionalProperties(),
                            getClassifications());
    }
}
