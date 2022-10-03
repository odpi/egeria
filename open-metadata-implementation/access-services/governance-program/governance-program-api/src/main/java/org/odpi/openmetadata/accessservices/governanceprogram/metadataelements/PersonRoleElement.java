/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.PersonRoleProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PersonRoleElement is the bean used to return a person role description.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PersonRoleElement implements MetadataElement, Serializable
{
    private static final long serialVersionUID = 1L;

    private ElementHeader        elementHeader = null;
    private PersonRoleProperties role          = null;


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
            role = template.getRole();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the description of the governance officer role.
     *
     * @return role description
     */
    public PersonRoleProperties getRole()
    {
        return role;
    }


    /**
     * Set up the description of the governance officer role.
     *
     * @param role role description
     */
    public void setRole(PersonRoleProperties role)
    {
        this.role = role;
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
                       ", role=" + role +
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
                       Objects.equals(role, that.role);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, role);
    }
}
