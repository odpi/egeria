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
 * GovernanceDefinitionGraph documents the linked governance definitions of the governance program.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceDefinitionGraph extends OpenMetadataRootElement
{
    private List<GovernanceDefinitionElement> relatedGovernanceDefinitions = null;


    /**
     * Default Constructor
     */
    public GovernanceDefinitionGraph()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceDefinitionGraph(GovernanceDefinitionGraph template)
    {
        super (template);

        if (template != null)
        {
            relatedGovernanceDefinitions = template.getRelatedGovernanceDefinitions();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceDefinitionGraph(OpenMetadataRootElement template)
    {
        super (template);
    }


    /**
     * Return the list of governance definitions that are linked indirectly to this governance definition.
     *
     * @return list of governance definitions
     */
    public List<GovernanceDefinitionElement> getRelatedGovernanceDefinitions()
    {
        return relatedGovernanceDefinitions;
    }


    /**
     * Set up the list of governance definitions that are linked  indirectly to this governance definition.
     *
     * @param relatedGovernanceDefinitions list of governance definitions
     */
    public void setRelatedGovernanceDefinitions(List<GovernanceDefinitionElement> relatedGovernanceDefinitions)
    {
        this.relatedGovernanceDefinitions = relatedGovernanceDefinitions;
    }

    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "GovernanceDefinitionGraph{" +
                "relatedGovernanceDefinitions=" + relatedGovernanceDefinitions +
                "} " + super.toString();
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
        if (! (objectToCompare instanceof GovernanceDefinitionGraph that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(relatedGovernanceDefinitions, that.relatedGovernanceDefinitions);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), relatedGovernanceDefinitions);
    }
}
