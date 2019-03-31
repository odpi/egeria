/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Many open metadata entities are referenceable.  It means that they have a qualified name and additional
 * properties.  In addition the Referenceable class adds support for the parent asset, guid, url and type
 * for the entity through extending ElementHeader.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = Asset.class, name = "Asset"),
                @JsonSubTypes.Type(value = Certification.class, name = "Certification"),
                @JsonSubTypes.Type(value = Comment.class, name = "Comment"),
                @JsonSubTypes.Type(value = Connection.class, name = "Connection"),
                @JsonSubTypes.Type(value = ConnectorType.class, name = "ConnectorType"),
                @JsonSubTypes.Type(value = Endpoint.class, name = "Endpoint"),
                @JsonSubTypes.Type(value = ExternalIdentifier.class, name = "ExternalIdentifier"),
                @JsonSubTypes.Type(value = ExternalReference.class, name = "ExternalReference"),
                @JsonSubTypes.Type(value = License.class, name = "License"),
                @JsonSubTypes.Type(value = Location.class, name = "Location"),
                @JsonSubTypes.Type(value = Note.class, name = "Note"),
                @JsonSubTypes.Type(value = NoteLog.class, name = "NoteLog"),
                @JsonSubTypes.Type(value = RelatedMediaReference.class, name = "RelatedMediaReference"),
                @JsonSubTypes.Type(value = SchemaElement.class, name = "SchemaElement")
        })
public class Referenceable extends ElementHeader
{
    /*
     * Attributes of a Referenceable
     */
    protected String              qualifiedName        = null;
    protected Map<String, String> additionalProperties = null;
    protected Map<String, Object> extendedProperties   = null;
    protected List<Meaning>       meanings             = null;

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
        super(template);

        if (template != null)
        {
            qualifiedName = template.getQualifiedName();
            additionalProperties = template.getAdditionalProperties();
            extendedProperties = template.getExtendedProperties();
            meanings = template.getMeanings();
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
     * Set up properties from subclasses properties.
     *
     * @param extendedProperties asset properties map
     */
    public void setExtendedProperties(Map<String, Object> extendedProperties)
    {
        this.extendedProperties = extendedProperties;
    }


    /**
     * Return a copy of the properties from subclasses.  Null means no extended properties are available.
     *
     * @return asset property map
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
                ", extendedProperties=" + extendedProperties +
                ", meanings=" + meanings +
                ", type=" + getType() +
                ", GUID='" + getGUID() + '\'' +
                ", URL='" + getURL() + '\'' +
                ", classifications=" + getClassifications() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        Referenceable that = (Referenceable) objectToCompare;
        return Objects.equals(getQualifiedName(), that.getQualifiedName()) &&
                Objects.equals(getAdditionalProperties(), that.getAdditionalProperties()) &&
                Objects.equals(getExtendedProperties(), that.getExtendedProperties()) &&
                Objects.equals(getMeanings(), that.getMeanings());
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(),
                            getQualifiedName(),
                            getAdditionalProperties(),
                            getExtendedProperties(),
                            getMeanings());
    }
}