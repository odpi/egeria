/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * FirstGovernanceActionProcessStepElement contains the properties and header for a governance action process step entity plus the
 * properties of a NextGovernanceActionProcessStep relationship retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FirstGovernanceActionProcessStepElement
{
    private GovernanceActionProcessStepElement element  = null;
    private String                             linkGUID = null;
    private String                             guard    = null;



    /**
     * Default constructor
     */
    public FirstGovernanceActionProcessStepElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public FirstGovernanceActionProcessStepElement(FirstGovernanceActionProcessStepElement template)
    {
        if (template != null)
        {
            element  = template.getElement();
            linkGUID = template.getLinkGUID();
            guard    = template.getGuard();
        }
    }


    /**
     * Return details of the first governance action process step
     *
     * @return governance action process step properties
     */
    public GovernanceActionProcessStepElement getElement()
    {
        return element;
    }


    /**
     * Set up details of the first governance action process step
     *
     * @param element governance action process step properties
     */
    public void setElement(GovernanceActionProcessStepElement element)
    {
        this.element = element;
    }


    /**
     * Return the unique identifier of the relationship.
     *
     * @return string guid
     */
    public String getLinkGUID()
    {
        return linkGUID;
    }


    /**
     * Set up the unique identifier of the relationship.
     *
     * @param linkGUID string guid
     */
    public void setLinkGUID(String linkGUID)
    {
        this.linkGUID = linkGUID;
    }


    /**
     * Return the triggering guard (or null for any guard).
     *
     * @return string name
     */
    public String getGuard()
    {
        return guard;
    }


    /**
     * Set up the triggering guard (or null for any guard).
     *
     * @param guard string name
     */
    public void setGuard(String guard)
    {
        this.guard = guard;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "FirstGovernanceActionProcessStepElement{" +
                       "nextProcessStep=" + element +
                       ", nextProcessStepLinkGUID='" + linkGUID + '\'' +
                       ", guard='" + guard + '\'' +
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
        FirstGovernanceActionProcessStepElement that = (FirstGovernanceActionProcessStepElement) objectToCompare;
        return Objects.equals(element, that.element) &&
                       Objects.equals(linkGUID, that.linkGUID) &&
                       Objects.equals(guard, that.guard);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(element, linkGUID, guard);
    }
}
