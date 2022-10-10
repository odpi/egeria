/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalservice.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PersonRoleAppointee is the bean used to return a role and current appointee(s).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
                  @JsonSubTypes.Type(value = PersonRoleHistory.class, name = "PersonRoleHistory"),
                  @JsonSubTypes.Type(value = AgreementRoleAppointee.class, name = "AgreementRoleAppointee"),
              })
public class PersonRoleAppointee extends PersonRoleElement
{
    private static final long serialVersionUID = 1L;

    private List<Appointee> currentAppointees = null;


    /**
     * Default constructor
     */
    public PersonRoleAppointee()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PersonRoleAppointee(PersonRoleAppointee template)
    {
        super(template);

        if (template != null)
        {
            currentAppointees = template.getCurrentAppointees();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PersonRoleAppointee(PersonRoleElement template)
    {
        super(template);
    }


    /**
     * Return information about the person appointed to the governance role.
     *
     * @return list of GovernanceAppointee objects
     */
    public List<Appointee> getCurrentAppointees()
    {
        if (currentAppointees == null)
        {
            return null;
        }
        else if (currentAppointees.isEmpty())
        {
            return null;
        }

        return currentAppointees;
    }


    /**
     * Set up the information about the person appointed to the governance role.
     *
     * @param currentAppointees  list of GovernanceAppointee objects
     */
    public void setCurrentAppointees(List<Appointee> currentAppointees)
    {
        this.currentAppointees = currentAppointees;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "PersonRoleAppointee{" +
                       "currentAppointees=" + currentAppointees +
                       ", elementHeader=" + getElementHeader() +
                       ", properties=" + getProperties() +
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
        PersonRoleAppointee that = (PersonRoleAppointee) objectToCompare;
        return Objects.equals(currentAppointees, that.currentAppointees);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), currentAppointees);
    }
}
