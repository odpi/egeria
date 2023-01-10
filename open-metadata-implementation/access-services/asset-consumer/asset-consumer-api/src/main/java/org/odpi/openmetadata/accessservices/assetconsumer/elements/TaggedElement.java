/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetconsumer.elements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelatedElement contains the properties and header for a relationship retrieved from the metadata repository along with the stub
 * of the related element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TaggedElement implements Serializable
{
    private static final long     serialVersionUID = 1L;

    private ElementHeader          relationshipHeader     = null;
    private ElementStub            relatedElement         = null;
    private boolean                isPublic               = false;

    /**
     * Default constructor
     */
    public TaggedElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TaggedElement(TaggedElement template)
    {
        if (template != null)
        {
            relationshipHeader = template.getRelationshipHeader();
            relatedElement = template.getRelatedElement();
            isPublic = template.getIsPublic();
        }
    }



    /**
     * Return if the link to the tag is private to the creating user.
     *
     * @return boolean
     */
    public boolean getIsPublic()
    {
        return isPublic;
    }


    /**
     * Set up whether the link to the tag is private to the creating user or not.
     *
     * @param aPublic boolean
     */
    public void setIsPublic(boolean aPublic)
    {
        isPublic = aPublic;
    }


    /**
     * Return the element header associated with the relationship.
     *
     * @return element header object
     */
    public ElementHeader getRelationshipHeader()
    {
        return relationshipHeader;
    }


    /**
     * Set up the element header associated with the relationship.
     *
     * @param relationshipHeader element header object
     */
    public void setRelationshipHeader(ElementHeader relationshipHeader)
    {
        this.relationshipHeader = relationshipHeader;
    }


    /**
     * Return the element header associated with end 1 of the relationship.
     *
     * @return element stub object
     */
    public ElementStub getRelatedElement()
    {
        return relatedElement;
    }


    /**
     * Set up the element header associated with end 1 of the relationship.
     *
     * @param relatedElement element stub object
     */
    public void setRelatedElement(ElementStub relatedElement)
    {
        this.relatedElement = relatedElement;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "TaggedElement{" +
                       "relationshipHeader=" + relationshipHeader +
                       ", relatedElement=" + relatedElement +
                       ", isPublic=" + isPublic +
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
        if (! (objectToCompare instanceof TaggedElement))
        {
            return false;
        }

        TaggedElement that = (TaggedElement) objectToCompare;

        if (isPublic != that.isPublic)
        {
            return false;
        }
        if (relationshipHeader != null ? ! relationshipHeader.equals(that.relationshipHeader) : that.relationshipHeader != null)
        {
            return false;
        }
        return relatedElement != null ? relatedElement.equals(that.relatedElement) : that.relatedElement == null;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        int result = relationshipHeader != null ? relationshipHeader.hashCode() : 0;
        result = 31 * result + (relatedElement != null ? relatedElement.hashCode() : 0);
        result = 31 * result + (isPublic ? 1 : 0);
        return result;
    }
}
