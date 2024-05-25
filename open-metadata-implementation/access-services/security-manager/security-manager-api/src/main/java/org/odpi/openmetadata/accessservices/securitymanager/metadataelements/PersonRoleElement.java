/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.securitymanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.securitymanager.properties.PersonRoleProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;

/**
 * PersonalRoleElement contains the properties and header for a person role assigned to a profile retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PersonRoleElement implements MetadataElement
{
    private ElementHeader        elementHeader = null;
    private PersonRoleProperties properties = null;
    private List<ElementStub>    appointees = null;


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
            appointees = template.getAppointees();
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
     * Return the list of Person profiles that are appointed to the role.
     *
     * @return list of element stubs
     */
    public List<ElementStub> getAppointees()
    {
        return appointees;
    }


    /**
     * Set up  the list of Person profiles that are appointed to the role.
     *
     * @param appointees list of element stubs
     */
    public void setAppointees(List<ElementStub> appointees)
    {
        this.appointees = appointees;
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
                       ", appointees=" + appointees +
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
                       Objects.equals(appointees, that.appointees);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, properties, appointees);
    }
}
