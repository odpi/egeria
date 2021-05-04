/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceZoneInAction returns information about an operational governance zone.  This includes the number of
 * Assets that are registered with the zone.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceZoneInAction extends GovernanceZoneDefinition
{
    private static final long    serialVersionUID = 1L;

    private int                         zoneMembershipCount = 0;


    /**
     * Default Constructor
     */
    public GovernanceZoneInAction()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceZoneInAction(GovernanceZoneInAction template)
    {
        super(template);

        if (template != null)
        {
            this.zoneMembershipCount = template.getZoneMembershipCount();
        }
    }


    /**
     * Return the count of assets that are associated with the zone.
     *
     * @return int count
     */
    public int getZoneMembershipCount()
    {
        return zoneMembershipCount;
    }


    public void setZoneMembershipCount(int zoneMembershipCount)
    {
        this.zoneMembershipCount = zoneMembershipCount;
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "GovernanceZoneInAction{" +
                "zoneMembershipCount=" + zoneMembershipCount +
                ", associatedGovernanceDefinitions=" + getAssociatedGovernanceDefinitions() +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", criteria='" + getCriteria() + '\'' +
                ", scope='" + getScope() + '\'' +
                ", domainIdentifier=" + getDomainIdentifier() +
                ", typeName='" + getTypeName() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
    }
}
