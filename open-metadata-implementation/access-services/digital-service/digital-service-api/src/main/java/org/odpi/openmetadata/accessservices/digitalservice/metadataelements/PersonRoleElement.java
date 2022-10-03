/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalservice.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.digitalservice.properties.PersonRoleProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PersonalRoleElement contains the properties and header for a person role assigned to a profile retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PersonRoleElement implements MetadataElement, Serializable
{
    private static final long     serialVersionUID = 1L;

    private ElementHeader        elementHeader = null;
    private PersonRoleProperties properties     = null;
    private RelatedElement       relatedElement = null;


    /**
     * Default constructor
     */
    public PersonRoleElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PersonRoleElement(PersonRoleElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            properties = template.getProperties();
            relatedElement = template.getRelatedElement();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param header element header object
     */
    @Override
    public void setElementHeader(ElementHeader header)
    {
        this.elementHeader = header;
    }


    /**
     * Return the properties of the role.
     *
     * @return properties
     */
    public PersonRoleProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the role properties.
     *
     * @param properties  properties
     */
    public void setProperties(PersonRoleProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return details of the relationship used to retrieve this element.
     * Will be null if the element was retrieved directly rather than via a relationship.
     *
     * @return list of element stubs
     */
    public RelatedElement getRelatedElement()
    {
        return relatedElement;
    }


    /**
     * Set up details of the relationship used to retrieve this element.
     * Will be null if the element was retrieved directly rather than via a relationship.
     *
     * @param relatedElement relationship details
     */
    public void setRelatedElement(RelatedElement relatedElement)
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
        return "PersonRoleElement{" +
                       "elementHeader=" + elementHeader +
                       ", properties=" + properties +
                       ", relatedElement=" + relatedElement +
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
        PersonRoleElement that = (PersonRoleElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(relatedElement, that.relatedElement);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, properties, relatedElement);
    }
}
