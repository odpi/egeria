/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SchemaLink defines a relationship between a SchemaAttribute and a SchemaType defined in an external schema.
 * It is used in schemas that include external (standard) schema types in their definition.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SchemaLink extends SchemaElement
{
    private static final long     serialVersionUID = 1L;

    protected String              linkType             = null;
    protected String              linkName             = null;
    protected Map<String, String> linkProperties       = null;
    protected String              linkedSchemaTypeGUID = null;
    protected String              linkedSchemaTypeName = null;


    /**
     * Default constructor
     */
    public SchemaLink()
    {
        super();
    }


    /**
     * Copy/clone constructor   makes a copy of the supplied object.
     *
     * @param template template object to copy
     */
    public SchemaLink(SchemaLink template)
    {
        super(template);

        if (template != null)
        {
            linkName = template.getLinkName();
            linkType = template.getLinkType();
            linkProperties = template.getLinkProperties();
            linkedSchemaTypeGUID = template.getLinkedSchemaTypeGUID();
            linkedSchemaTypeName = template.getLinkedSchemaTypeName();
        }
    }

    /**
     * Returns a clone of this object as the abstract SchemaElement class.
     *
     * @return SchemaElement
     */
    @Override
    public SchemaElement cloneSchemaElement()
    {
        return new SchemaLink(this);
    }


    /**
     * Return the type of the link - this is related to the type of the schema it is a part of.
     *
     * @return String link type
     */
    public String getLinkType()
    {
        return linkType;
    }


    /**
     * Set up the type of the link - this is related to the type of the schema it is a part of.
     *
     * @param linkType String link type
     */
    public void setLinkType(String linkType)
    {
        this.linkType = linkType;
    }


    /**
     * Return the name of this link
     *
     * @return String name
     */
    public String getLinkName()
    {
        return linkName;
    }


    /**
     * Set up the name of this link.
     *
     * @param linkName String name
     */
    public void setLinkName(String linkName)
    {
        this.linkName = linkName;
    }


    /**
     * Return the properties associated with this schema link.
     *
     * @return property map
     */
    public Map<String, String> getLinkProperties()
    {
        if (linkProperties == null)
        {
            return null;
        }
        else if (linkProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(linkProperties);
        }
    }


    /**
     * Set up the properties associated with this schema link.
     *
     * @param linkProperties property map
     */
    public void setLinkProperties(Map<String, String> linkProperties)
    {
        this.linkProperties = linkProperties;
    }


    /**
     * Return the GUID of the schema type that this link connects together.
     *
     * @return  unique identifier
     */
    public String getLinkedSchemaTypeGUID()
    {
        return linkedSchemaTypeGUID;
    }


    /**
     * Set up the GUID of the schema type that this link connects together.
     *
     * @param linkedSchemaTypeGUID unique identifier
     */
    public void setLinkedSchemaTypeGUID(String linkedSchemaTypeGUID)
    {
        this.linkedSchemaTypeGUID = linkedSchemaTypeGUID;
    }


    /**
     * Return the name of the schema type that this link connects together.
     *
     * @return  unique name
     */
    public String getLinkedSchemaTypeName()
    {
        return linkedSchemaTypeName;
    }


    /**
     * Set up the name of the schema type that this link connects together.
     *
     * @param linkedSchemaTypeName unique name
     */
    public void setLinkedSchemaTypeName(String linkedSchemaTypeName)
    {
        this.linkedSchemaTypeName = linkedSchemaTypeName;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SchemaLink{" +
                "linkType='" + linkType + '\'' +
                ", linkName='" + linkName + '\'' +
                ", linkProperties=" + linkProperties +
                ", linkedSchemaTypeGUID='" + linkedSchemaTypeGUID + '\'' +
                ", linkedSchemaTypeName='" + linkedSchemaTypeName + '\'' +
                ", deprecated=" + isDeprecated() +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", meanings=" + getMeanings() +
                ", type=" + getType() +
                ", GUID='" + getGUID() + '\'' +
                ", URL='" + getURL() + '\'' +
                ", classifications=" + getClassifications() +
                ", extendedProperties=" + getExtendedProperties() +
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
        if (!(objectToCompare instanceof SchemaLink))
        {
            return false;
        }
        SchemaLink that = (SchemaLink) objectToCompare;
        return Objects.equals(getLinkType(), that.getLinkType()) &&
                Objects.equals(getLinkName(), that.getLinkName()) &&
                Objects.equals(getLinkProperties(), that.getLinkProperties()) &&
                Objects.equals(getLinkedSchemaTypeGUID(), that.getLinkedSchemaTypeGUID()) &&
                Objects.equals(getLinkedSchemaTypeName(), that.getLinkedSchemaTypeName());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getLinkType(),
                            getLinkName(),
                            getLinkProperties(),
                            getLinkedSchemaTypeGUID());
    }

}