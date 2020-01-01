/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;


import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PersonalRole covers a role that has been assigned to an individual.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PersonRole.class, name = "PersonRole"),
        @JsonSubTypes.Type(value = CommunityMember.class, name = "CommunityMember"),
        @JsonSubTypes.Type(value = TeamLeader.class, name = "TeamLeader"),
        @JsonSubTypes.Type(value = TeamMember.class, name = "TeamMember")
})
public class PersonalRole extends ReferenceableHeader
{
    private static final long    serialVersionUID = 1L;

    private String              scope                = null;
    private Map<String, Object> extendedProperties   = null;
    private Map<String, String> additionalProperties = null;


    /**
     * Default constructor
     */
    public PersonalRole()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PersonalRole(PersonalRole template)
    {
        super(template);

        if (template != null)
        {
            this.scope = template.getScope();
            this.additionalProperties = template.getAdditionalProperties();
            this.extendedProperties = template.getExtendedProperties();
        }
    }


    /**
     * Return the scope of the role.
     *
     * @return string
     */
    public String getScope()
    {
        return scope;
    }


    /**
     * Set up the scope of the role.
     *
     * @param scope string
     */
    public void setScope(String scope)
    {
        this.scope = scope;
    }


    /**
     * Return any properties associated with the subclass of this element.
     *
     * @return map of property names to property values
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
     * Set up any properties associated with the subclass of this element.
     *
     * @param extendedProperties map of property names to property values
     */
    public void setExtendedProperties(Map<String, Object> extendedProperties)
    {
        this.extendedProperties = extendedProperties;
    }


    /**
     * Return any additional properties associated with the element.
     *
     * @return map of property names to property values
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
     * Set up any additional properties associated with the element.
     *
     * @param additionalProperties map of property names to property values
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "PersonalRole{" +
                "extendedProperties=" + extendedProperties +
                ", additionalProperties=" + additionalProperties +
                ", scope=" + scope +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", classifications=" + getClassifications() +
                ", GUID='" + getGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", typeDescription='" + getTypeDescription() + '\'' +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        PersonalRole that = (PersonalRole) objectToCompare;
        return Objects.equals(getScope(), that.getScope()) &&
               Objects.equals(getExtendedProperties(), that.getExtendedProperties()) &&
               Objects.equals(getAdditionalProperties(), that.getAdditionalProperties());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getScope(), getExtendedProperties(), getAdditionalProperties());
    }
}
