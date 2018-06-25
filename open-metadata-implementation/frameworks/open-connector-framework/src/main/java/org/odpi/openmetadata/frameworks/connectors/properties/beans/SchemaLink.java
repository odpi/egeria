/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SchemaLink defines a relationship between 2 SchemaElements.  It is used in network type
 * schemas such as a graph.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SchemaLink extends PropertyBase
{
    /*
     * Attributes from the relationship
     */
    protected String linkGUID = null;
    protected String linkType = null;

    /*
     * Attributes specific to SchemaLink
     */
    protected String              linkName             = null;
    protected Map<String, Object> linkProperties       = null;
    protected List<String>        linkedAttributeGUIDs = null;


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
            linkGUID = template.getLinkGUID();
            linkName = template.getLinkName();
            linkType = template.getLinkType();
            linkProperties = template.getLinkProperties();
            linkedAttributeGUIDs = template.getLinkedAttributeGUIDs();
        }
    }


    /**
     * Return the identifier for the schema link.
     *
     * @return String guid
     */
    public String getLinkGUID()
    {
        return linkGUID;
    }


    /**
     * Set up the identifier for the schema link.
     *
     * @param linkGUID String guid
     */
    public void setLinkGUID(String linkGUID)
    {
        this.linkGUID = linkGUID;
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
    public Map<String, Object> getLinkProperties()
    {
        if (linkProperties == null)
        {
            return linkProperties;
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
    public void setLinkProperties(Map<String, Object> linkProperties)
    {
        this.linkProperties = linkProperties;
    }


    /**
     * Return the GUIDs of the schema attributes that this link connects together.
     *
     * @return  GUIDs for either end of the link - returned as a list.
     */
    public List<String> getLinkedAttributeGUIDs()
    {
        if (linkedAttributeGUIDs == null)
        {
            return linkedAttributeGUIDs;
        }
        else
        {
            return new ArrayList<>(linkedAttributeGUIDs);
        }
    }


    /**
     * Set up the GUIDs of the schema attributes that this link connects together.
     *
     * @param linkedAttributeGUIDs GUIDs for either end of the link - returned as a list.
     */
    public void setLinkedAttributeGUIDs(List<String> linkedAttributeGUIDs)
    {
        this.linkedAttributeGUIDs = linkedAttributeGUIDs;
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
                "linkGUID='" + linkGUID + '\'' +
                ", linkType='" + linkType + '\'' +
                ", linkName='" + linkName + '\'' +
                ", linkProperties=" + linkProperties +
                ", linkedAttributeGUIDs=" + linkedAttributeGUIDs +
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
        return Objects.equals(getLinkGUID(), that.getLinkGUID()) &&
                Objects.equals(getLinkType(), that.getLinkType()) &&
                Objects.equals(getLinkName(), that.getLinkName()) &&
                Objects.equals(getLinkProperties(), that.getLinkProperties()) &&
                Objects.equals(getLinkedAttributeGUIDs(), that.getLinkedAttributeGUIDs());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getLinkGUID(),
                            getLinkType(),
                            getLinkName(),
                            getLinkProperties(),
                            getLinkedAttributeGUIDs());
    }

}