/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.metadataelements;

import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceOfficerProperties;

import java.util.Objects;

/**
 * GovernanceOfficerElement is the bean used to return a governance zone definition stored in the open metadata repositories.
 */
public class GovernanceOfficerElement extends GovernanceOfficerProperties
{
    private static final long serialVersionUID = 1L;

    private ElementHeader elementHeader = null;


    /**
     * Default constructor
     */
    public GovernanceOfficerElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceOfficerElement(GovernanceOfficerElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceOfficerElement{" +
                "elementHeader=" + elementHeader +
                ", governanceDomain=" + getGovernanceDomain() +
                ", appointmentId='" + getAppointmentId() + '\'' +
                ", appointmentContext='" + getAppointmentContext() + '\'' +
                ", appointee=" + getAppointee() +
                ", predecessors=" + getPredecessors() +
                ", successors=" + getSuccessors() +
                ", typeName='" + getTypeName() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", extendedProperties=" + getExtendedProperties() +
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
        GovernanceOfficerElement that = (GovernanceOfficerElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader);
    }
}
