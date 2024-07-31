/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A governance zone defines a group of assets.  The governance zone definition defines
 * how the assets in the zone should be managed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceZoneDefinition extends GovernanceZoneElement
{
    private ElementStub       parentGovernanceZone  = null;
    private List<ElementStub> nestedGovernanceZones = null;
    private List<ElementStub> associatedGovernanceDefinitions = null;


    /**
     * Default Constructor
     */
    public GovernanceZoneDefinition()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceZoneDefinition(GovernanceZoneDefinition template)
    {
        super(template);

         if (template != null)
         {
             this.parentGovernanceZone = template.getParentGovernanceZone();
             this.nestedGovernanceZones = template.getNestedGovernanceZones();
             this.associatedGovernanceDefinitions = template.getAssociatedGovernanceDefinitions();
         }
    }


    /**
     * Copy/clone constructor for super class
     *
     * @param template object to copy
     */
    public GovernanceZoneDefinition(GovernanceZoneElement template)
    {
        super(template);
    }


    /**
     * Return the identifiers of the governance zone that this zone inherits governance definitions from.
     *
     * @return identifiers of zone
     */
    public ElementStub getParentGovernanceZone()
    {
        return parentGovernanceZone;
    }


    /**
     * Set up the unique identifier of the governance zone that this zone inherits governance definitions from.
     *
     * @param parentGovernanceZone identifiers of zone
     */
    public void setParentGovernanceZone(ElementStub parentGovernanceZone)
    {
        this.parentGovernanceZone = parentGovernanceZone;
    }


    /**
     * Return the list of unique identifiers of the governance zones that inherit governance definitions from this zone.
     *
     * @return list of identifiers of zones
     */
    public List<ElementStub> getNestedGovernanceZones()
    {
        return nestedGovernanceZones;
    }


    /**
     * Set up the list of unique identifiers of the governance zones that inherit governance definitions from this zone.
     *
     * @param nestedGovernanceZones list of identifiers of zones
     */
    public void setNestedGovernanceZones(List<ElementStub> nestedGovernanceZones)
    {
        this.nestedGovernanceZones = nestedGovernanceZones;
    }


    /**
     * Return the list of the governance definitions that control assets in this zone.
     *
     * @return list of identifiers for the linked definitions
     */
    public List<ElementStub> getAssociatedGovernanceDefinitions()
    {
        return associatedGovernanceDefinitions;
    }


    /**
     * Set up the list of the governance definitions that control assets in this zone.
     *
     * @param associatedGovernanceDefinitions list of identifiers for the linked definitions
     */
    public void setAssociatedGovernanceDefinitions(List<ElementStub> associatedGovernanceDefinitions)
    {
        this.associatedGovernanceDefinitions = associatedGovernanceDefinitions;
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "GovernanceZoneDefinition{" +
                       "parentGovernanceZone='" + parentGovernanceZone + '\'' +
                       ", nestedGovernanceZones=" + nestedGovernanceZones +
                       ", associatedGovernanceDefinitions=" + associatedGovernanceDefinitions +
                       ", elementHeader=" + getElementHeader() +
                       ", governanceZoneProperties=" + getGovernanceZoneProperties() +
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
        GovernanceZoneDefinition that = (GovernanceZoneDefinition) objectToCompare;
        return Objects.equals(parentGovernanceZone, that.parentGovernanceZone) &&
                       Objects.equals(nestedGovernanceZones, that.nestedGovernanceZones) &&
                       Objects.equals(associatedGovernanceDefinitions, that.associatedGovernanceDefinitions);
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), parentGovernanceZone, nestedGovernanceZones, associatedGovernanceDefinitions);
    }
}
