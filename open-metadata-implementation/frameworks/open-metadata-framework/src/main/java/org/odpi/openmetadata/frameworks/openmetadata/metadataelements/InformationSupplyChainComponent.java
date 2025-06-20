/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * InformationSupplyChainComponent contains a component that is either linked to the information supply
 * directly via the ImplementedBy relationship, or it is a solution port linked to a component that
 * is linked to the
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformationSupplyChainComponent extends RelatedMetadataElementSummary
{
    private List<InformationSupplyChainComponent> nestedElements = null;

    /**
     * Default constructor
     */
    public InformationSupplyChainComponent()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public InformationSupplyChainComponent(InformationSupplyChainComponent template)
    {
        super(template);

        if (template != null)
        {
            nestedElements = template.getNestedElements();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public InformationSupplyChainComponent(RelatedMetadataElementSummary template)
    {
        super(template);
    }


    /**
     * Return any relevant nested components.
     *
     * @return list of components
     */
    public List<InformationSupplyChainComponent> getNestedElements()
    {
        return nestedElements;
    }


    /**
     * Set up any relevant nested components.
     *
     * @param nestedElements list of components
     */
    public void setNestedElements(List<InformationSupplyChainComponent> nestedElements)
    {
        this.nestedElements = nestedElements;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "InformationSupplyChainComponent{" +
                "nestedElements=" + nestedElements +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        InformationSupplyChainComponent that = (InformationSupplyChainComponent) objectToCompare;
        return Objects.equals(nestedElements, that.nestedElements);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), nestedElements);
    }
}
