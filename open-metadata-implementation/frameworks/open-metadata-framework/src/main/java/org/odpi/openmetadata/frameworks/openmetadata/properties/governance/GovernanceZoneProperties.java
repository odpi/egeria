/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

/**
 * GovernanceZoneProperties describes a governance zone which is a grouping of assets that are used for a specific
 * purpose.
 */
public class GovernanceZoneProperties extends ReferenceableProperties
{
    private String zoneName         = null;
    private String criteria         = null;
    private String scope            = null;
    private int    domainIdentifier = 0;


    /**
     * Default constructor
     */
    public GovernanceZoneProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.GOVERNANCE_ZONE.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceZoneProperties(GovernanceZoneProperties template)
    {
        super(template);

        if (template != null)
        {
            this.zoneName = template.getZoneName();
            this.criteria = template.getCriteria();
            this.scope = template.getScope();
            this.domainIdentifier = template.getDomainIdentifier();
        }
    }


    /**
     * Return the unque name of the zone - this is the value configured in properties such as supportedZones and AssetZoneMembership.
     *
     * @return string name
     */
    public String getZoneName()
    {
        return zoneName;
    }


    /**
     * Set up unique name for the zone - this is the value configured in properties such as supportedZones and AssetZoneMembership.
     *
     * @param zoneName string name
     */
    public void setZoneName(String zoneName)
    {
        this.zoneName = zoneName;
    }


    /**
     * Return details of the criteria that Assets have when they are placed in these zones.
     *
     * @return text
     */
    public String getCriteria()
    {
        return criteria;
    }


    /**
     * Set up the details of the criteria that Assets have when they are placed in these zones.
     *
     * @param criteria text
     */
    public void setCriteria(String criteria)
    {
        this.criteria = criteria;
    }


    /**
     * Return the definition of the scope of this zone.
     *
     * @return scope definition
     */
    public String getScope()
    {
        return scope;
    }


    /**
     * Set up the scope definition of this zone.
     *
     * @param scope string definition
     */
    public void setScope(String scope)
    {
        this.scope = scope;
    }


    /**
     * Return the identifier of the governance domain that this zone is managed by.
     *
     * @return int identifier
     */
    public int getDomainIdentifier()
    {
        return domainIdentifier;
    }


    /**
     * Set up the identifier of the governance domain that this zone is managed by.
     *
     * @param domainIdentifier int identifier
     */
    public void setDomainIdentifier(int domainIdentifier)
    {
        this.domainIdentifier = domainIdentifier;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceZoneProperties{" +
                       "zoneName='" + zoneName + '\'' +
                       ", criteria='" + criteria + '\'' +
                       ", scope='" + scope + '\'' +
                       ", domainIdentifier=" + domainIdentifier +
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
        GovernanceZoneProperties that = (GovernanceZoneProperties) objectToCompare;
        return domainIdentifier == that.domainIdentifier &&
                       Objects.equals(zoneName, that.zoneName) &&
                       Objects.equals(criteria, that.criteria) &&
                       Objects.equals(scope, that.scope);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getZoneName(), getDisplayName(), getDescription(), getCriteria(), getScope(), getDomainIdentifier());
    }
}
