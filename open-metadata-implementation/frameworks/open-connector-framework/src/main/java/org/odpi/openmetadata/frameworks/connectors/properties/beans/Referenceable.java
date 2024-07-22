/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Many open metadata entities are referenceable.  It means that they have a qualified name and additional
 * properties.  In addition, the Referenceable class adds support for the parent asset, guid, url and type
 * for the entity through extending ElementBase.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
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
                @JsonSubTypes.Type(value = NoteLogHeader.class, name = "NoteLogHeader"),
                @JsonSubTypes.Type(value = RelatedMediaReference.class, name = "RelatedMediaReference"),
                @JsonSubTypes.Type(value = GovernedReferenceable.class, name = "GovernedReferenceable"),
                @JsonSubTypes.Type(value = ValidValue.class, name = "ValidValue")
        })
public class Referenceable extends ElementBase
{
    protected String              qualifiedName        = null;
    protected Map<String, String> additionalProperties = null;


    /**
     * Default constructor
     */
    public Referenceable()
    {
        super();
    }


    /**
     * Copy/clone constructor.  Retrieves the values from the supplied template
     *
     * @param template element to copy
     */
    public Referenceable(Referenceable template)
    {
        super(template);

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
        return additionalProperties;
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
                       "extendedProperties=" + getExtendedProperties() +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", GUID='" + getGUID() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", headerVersion=" + getHeaderVersion() +
                       ", qualifiedName='" + qualifiedName + '\'' +
                       ", additionalProperties=" + additionalProperties +
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