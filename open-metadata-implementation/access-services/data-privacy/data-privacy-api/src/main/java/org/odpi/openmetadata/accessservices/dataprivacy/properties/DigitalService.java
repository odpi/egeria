/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataprivacy.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DigitalService is an anchor object for tracking the lifecycle of one of an organization's digital service.
 * The digital service instance is create when the digital service is just a concept.  It is used to record
 * the role and implementation style that it has along with information about how it will operate.
 * As the digital service moved through its lifecycle from implementation to deployment to use, more
 * information is attached to the digital service instance to support the correct management and compliance
 * of the service.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DigitalService extends DataPrivacyElementHeader
{
    private static final long    serialVersionUID = 1L;

    private String                            url                  = null;
    private String                            guid                 = null;
    private String                            typeId               = null;
    private String                            typeName             = null;
    private long                              typeVersion          = 0;
    private String                            typeDescription      = null;
    private String                            qualifiedName        = null;
    private String                            displayName          = null;
    private String                            description          = null;
    private DigitalServiceImplementationStyle implementationStyle  = null;
    private DigitalServiceStatus              status               = null;
    private DigitalServiceVisibility          visibility           = null;
    private DigitalServiceResponsibility      responsibility       = null;
    private Map<String, Object>               additionalProperties = null;
    private List<Classification>              classifications      = null;


    /**
     * Default constructor
     */
    public DigitalService()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DigitalService(DigitalService template)
    {
        super(template);

        if (template != null)
        {
            this.url = template.getURL();
            this.guid = template.getGUID();
            this.typeId = template.getTypeId();
            this.typeName = template.getTypeName();
            this.typeVersion = template.getTypeVersion();
            this.typeDescription = template.getTypeDescription();
            this.qualifiedName = template.getQualifiedName();
            this.displayName = template.getDisplayName();
            this.description = template.getDescription();
            this.implementationStyle = template.getImplementationStyle();
            this.status = template.getStatus();
            this.visibility = template.getVisibility();
            this.responsibility = template.getResponsibility();
            this.additionalProperties = template.getAdditionalProperties();
            this.classifications = template.getClassifications();
        }
    }


    /**
     * Return the URL for this asset.
     *
     * @return URL string
     */
    public String getURL()
    {
        return url;
    }


    /**
     * Set up the URL for this asset.
     *
     * @param url URL string
     */
    public void setURL(String url)
    {
        this.url = url;
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
     * Return the unique identifier for this Asset's type.
     *
     * @return string guid for type
     */
    public String getTypeId()
    {
        return typeId;
    }


    /**
     * Set up the unique identifier for this Asset's type.
     *
     * @param typeId string guid for type
     */
    public void setTypeId(String typeId)
    {
        this.typeId = typeId;
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
     * Return the version number for this Asset's type.
     *
     * @return long
     */
    public long getTypeVersion()
    {
        return typeVersion;
    }


    /**
     * Set up the version number for this Asset's type.
     *
     * @param typeVersion long
     */
    public void setTypeVersion(long typeVersion)
    {
        this.typeVersion = typeVersion;
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
     * Return the implementation style of the digital service.
     *
     * @return enum value
     */
    public DigitalServiceImplementationStyle getImplementationStyle()
    {
        return implementationStyle;
    }


    /**
     * Set up the implementation style of the digital service.
     *
     * @param implementationStyle enum value
     */
    public void setImplementationStyle(DigitalServiceImplementationStyle implementationStyle)
    {
        this.implementationStyle = implementationStyle;
    }


    /**
     * Return the status of the digital service.
     *
     * @return enum value
     */
    public DigitalServiceStatus getStatus()
    {
        return status;
    }


    /**
     * Set up the status of the digital service.
     *
     * @param status enum value
     */
    public void setStatus(DigitalServiceStatus status)
    {
        this.status = status;
    }


    /**
     * Return the visibility of the digital service's implementation.
     *
     * @return enum value
     */
    public DigitalServiceVisibility getVisibility()
    {
        return visibility;
    }


    /**
     * Set up the visibility of the digital service's implementation.
     *
     * @param visibility enum value
     */
    public void setVisibility(DigitalServiceVisibility visibility)
    {
        this.visibility = visibility;
    }


    /**
     * Return whether this is a data controller or data processor.
     *
     * @return enum value
     */
    public DigitalServiceResponsibility getResponsibility()
    {
        return responsibility;
    }


    /**
     * Set up whether this is a data controller or data processor.
     *
     * @param responsibility enum value
     */
    public void setResponsibility(DigitalServiceResponsibility responsibility)
    {
        this.responsibility = responsibility;
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
            return additionalProperties;
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
        return "DigitalService{" +
                "url='" + url + '\'' +
                ", guid='" + guid + '\'' +
                ", typeId='" + typeId + '\'' +
                ", typeName='" + typeName + '\'' +
                ", typeVersion=" + typeVersion +
                ", typeDescription='" + typeDescription + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
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
        if (!(objectToCompare instanceof DigitalService))
        {
            return false;
        }
        DigitalService asset = (DigitalService) objectToCompare;
        return getTypeVersion() == asset.getTypeVersion() &&
                Objects.equals(getURL(), asset.getURL()) &&
                Objects.equals(getGUID(), asset.getGUID()) &&
                Objects.equals(getTypeId(), asset.getTypeId()) &&
                Objects.equals(getTypeName(), asset.getTypeName()) &&
                Objects.equals(getTypeDescription(), asset.getTypeDescription()) &&
                Objects.equals(getQualifiedName(), asset.getQualifiedName()) &&
                Objects.equals(getDisplayName(), asset.getDisplayName()) &&
                Objects.equals(getDescription(), asset.getDescription()) &&
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

        return Objects.hash(getURL(),
                            getGUID(),
                            getTypeId(),
                            getTypeName(),
                            getTypeVersion(),
                            getTypeDescription(),
                            getQualifiedName(),
                            getDisplayName(),
                            getDescription(),
                            getAdditionalProperties(),
                            getClassifications());
    }
}
