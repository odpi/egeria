/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ControlFlowProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * ControlFlowElement contains the properties and header for a control flow relationship retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ControlFlowElement implements Serializable
{
    private static final long     serialVersionUID = 1L;

    private ElementHeader         controlFlowHeader     = null;
    private ControlFlowProperties controlFlowProperties = null;
    private ElementHeader         currentStep           = null;
    private ElementHeader         nextStep              = null;

    /**
     * Default constructor
     */
    public ControlFlowElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ControlFlowElement(ControlFlowElement template)
    {
        if (template != null)
        {
            controlFlowHeader = template.getControlFlowHeader();
            controlFlowProperties = template.getControlFlowProperties();
            currentStep = template.getCurrentStep();
            nextStep = template.getNextStep();
        }
    }


    /**
     * Return the element header associated with the relationship.
     *
     * @return element header object
     */
    public ElementHeader getControlFlowHeader()
    {
        return controlFlowHeader;
    }


    /**
     * Set up the element header associated with the relationship.
     *
     * @param controlFlowHeader element header object
     */
    public void setControlFlowHeader(ElementHeader controlFlowHeader)
    {
        this.controlFlowHeader = controlFlowHeader;
    }


    /**
     * Return details of the relationship
     *
     * @return relationship properties
     */
    public ControlFlowProperties getControlFlowProperties()
    {
        return controlFlowProperties;
    }


    /**
     * Set up relationship properties
     *
     * @param controlFlowProperties relationship properties
     */
    public void setControlFlowProperties(ControlFlowProperties controlFlowProperties)
    {
        this.controlFlowProperties = controlFlowProperties;
    }


    /**
     * Return the element header associated with end 1 of the relationship.
     *
     * @return element header object
     */
    public ElementHeader getCurrentStep()
    {
        return currentStep;
    }


    /**
     * Set up the element header associated with end 1 of the relationship.
     *
     * @param currentStep element header object
     */
    public void setCurrentStep(ElementHeader currentStep)
    {
        this.currentStep = currentStep;
    }



    /**
     * Return the element header associated with end 2 of the relationship.
     *
     * @return element header object
     */
    public ElementHeader getNextStep()
    {
        return nextStep;
    }


    /**
     * Set up the element header associated with end 2 of the relationship.
     *
     * @param nextStep element header object
     */
    public void setNextStep(ElementHeader nextStep)
    {
        this.nextStep = nextStep;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ControlFlowElement{" +
                       "controlFlowHeader=" + controlFlowHeader +
                       ", controlFlowProperties=" + controlFlowProperties +
                       ", currentStep=" + currentStep +
                       ", nextStep=" + nextStep +
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
        ControlFlowElement that = (ControlFlowElement) objectToCompare;
        return Objects.equals(getControlFlowHeader(), that.getControlFlowHeader()) &&
                       Objects.equals(getControlFlowProperties(), that.getControlFlowProperties()) &&
                       Objects.equals(getCurrentStep(), that.getCurrentStep()) &&
                       Objects.equals(getNextStep(), that.getNextStep());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), controlFlowHeader, controlFlowProperties, currentStep, nextStep);
    }
}
