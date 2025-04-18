/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.governanceprogram;


import org.odpi.openmetadata.samples.archiveutilities.organization.PersonDefinition;

/**
 * The SustainabilityRoleDefinition is used to feed the definition of the governance roles for
 * Coco Pharmaceuticals' sustainability program.
 */
public enum GovernanceRoleDefinition
{
    /**
     * Chief Governance Officer (CGO)
     */
    GOVERNANCE_LEADER("GovernanceOfficer",
                      GovernanceDomainDefinition.ALL,
                      "GOVERNANCE_LEADER",
                      "Chief Governance Officer (CGO)",
                      "Leader of all the governance officers",
                      "Coco Pharmaceuticals",
                      true, 1,
                      new PersonDefinition[]{PersonDefinition.JULES_KEEPER}),


    ;

    private final String typeName;
    private final GovernanceDomainDefinition    domain;
    private final String identifier;
    private final String  displayName;
    private final String  description;
    private final String  scope;
    private final boolean          headCountSet;
    private final int headCount;
    private final PersonDefinition[] appointees;


    /**
     * SustainabilityRoleDefinition constructor creates an instance of the enum
     *
     * @param typeName name of the type for the role
     * @param domain governance domain for this role
     * @param identifier   unique Id for the role
     * @param displayName   text for the role
     * @param scope scope of the role
     * @param description   description of the assets in the role
     * @param headCountSet should the headcount property be set?
     * @param headCount   criteria for inclusion
     */
    GovernanceRoleDefinition(String                     typeName,
                             GovernanceDomainDefinition domain,
                             String                     identifier,
                             String                     displayName,
                             String                     description,
                             String                     scope,
                             boolean                    headCountSet,
                             int                        headCount,
                             PersonDefinition[]         appointees)
    {
        this.typeName = typeName;
        this.domain = domain;
        this.identifier = identifier;
        this.displayName = displayName;
        this.description = description;
        this.scope = scope;
        this.headCountSet = headCountSet;
        this.headCount = headCount;
        this.appointees = appointees;
    }


    /**
     * Return the name of the PersonRole type to use.
     *
     * @return type name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Returns the unique name for the role entity.
     *
     * @return identifier
     */
    public String getQualifiedName()
    {
        return typeName + "::" + identifier;
    }

    
    /**
     * Returns the unique name for the role.
     *
     * @return identifier
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Return the domain of the governance role.
     *
     * @return domain
     */
    public GovernanceDomainDefinition getDomain()
    {
        return domain;
    }


    /**
     * Return the appointees.
     *
     * @return list of profiles
     */
    public PersonDefinition[] getAppointees()
    {
        return appointees;
    }


    /**
     * Returns a descriptive name of the role.
     *
     * @return display name
     */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * Returns a detailed description of the role.
     *
     * @return description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Returns the breadth of responsibility for the role.
     *
     * @return scope
     */
    public String getScope()
    {
        return scope;
    }


    /**
     * Should the headcount property be set on the role.
     * 
     * @return flag
     */
    public boolean isHeadCountSet()
    {
        return headCountSet;
    }


    /**
     * Returns the number of people that can be appointed to the role.
     *
     * @return number
     */
    public int getHeadCount()
    {
        return headCount;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "SustainabilityRoleDefinition{" + "identifier='" + identifier + '}';
    }
}
