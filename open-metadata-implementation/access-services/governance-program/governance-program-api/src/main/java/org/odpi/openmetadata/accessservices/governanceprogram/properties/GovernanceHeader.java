/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GovernanceHeader provides the base class for many of the definitions that define the governance program.
 * It includes many of the common fields:
 *
 * <ul>
 *     <li>GUID</li>
 *     <li>Type</li>
 *     <li>Title</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = GovernanceReferenceableHeader.class, name = "GovernanceReferenceableHeader"),
                @JsonSubTypes.Type(value = GovernanceRelationship.class, name = "GovernanceRelationship")
        })
public abstract class GovernanceHeader implements Serializable
{
    private static final long          serialVersionUID = 1L;

    private String                     guid                 = null;
    private String                     type                 = null;
    private String                     title                = null;


    /**
     * Default Constructor
     */
    public GovernanceHeader()
    {
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public GovernanceHeader(GovernanceHeader template)
    {
        if (template != null)
        {
            this.guid = template.getGUID();
            this.type = template.getType();
            this.title = template.getTitle();
        }
    }


    /**
     * Return the unique identifier for this definition.  This value is assigned by the metadata collection
     * when the governance definition is created.
     *
     * @return String guid
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Set up the unique identifier for this definition.  This value is assigned by the metadata collection
     * when the governance definition is created.
     *
     * @param guid String guid
     */
    public void setGUID(String guid)
    {
        this.guid = guid;
    }


    /**
     * Return the name of the specific type of governance definition.
     *
     * @return String type name
     */
    public String getType()
    {
        return type;
    }


    /**
     * Set up the name of the specific type of the governance definition.
     *
     * @param type String type name
     */
    public void setType(String type)
    {
        this.type = type;
    }


    /**
     * Return the title associated with this governance definition.
     *
     * @return String title
     */
    public String getTitle()
    {
        return title;
    }


    /**
     * Set up the title associated with this governance definition.
     *
     * @param title String title
     */
    public void setTitle(String title)
    {
        this.title = title;
    }



    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "GovernanceDefinition{" +
                "guid='" + guid + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title +
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
        if (!(objectToCompare instanceof GovernanceHeader))
        {
            return false;
        }
        GovernanceHeader that = (GovernanceHeader) objectToCompare;
        return Objects.equals(guid, that.guid) &&
                Objects.equals(getType(), that.getType()) &&
                Objects.equals(getTitle(), that.getTitle());
    }


    /**
     * Just use the GUID for the hash code as it should be unique.
     *
     * @return int code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(guid);
    }
}
