/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.sustainability;


import org.odpi.openmetadata.frameworks.openmetadata.refdata.ActorRoleGroup;
import org.odpi.openmetadata.samples.archiveutilities.organization.BusinessAreaDefinition;
import org.odpi.openmetadata.samples.archiveutilities.organization.PersonDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ScopeDefinition;

/**
 * The SustainabilityRoleDefinition is used to feed the definition of the governance roles for
 * Coco Pharmaceuticals sustainability program.
 */
public enum SustainabilityRoleDefinition
{
    /**
     * Sustainability Lead
     */
    SUSTAINABILITY_OFFICER(ActorRoleGroup.GOVERNANCE_OFFICER.getName(),
                           "SUSTAINABILITY-LEAD",
                           "Sustainability Lead",
                           """
                                   1) Conduct sustainability or environment-related risk assessments.
                                   2) Create and maintain sustainability program documents, such as schedules and budgets.
                                   3) Write project proposals, grant applications, or other documents to pursue funding for environmental initiatives.
                                   """,
                           ScopeDefinition.WITHIN_ORGANIZATION,
                           true, 1,
                           null,
                           PersonDefinition.TOM_TALLY,
                           GovernanceDefinition.SUSTAINABILITY_LEADER_RESPONSIBILITY),

    /**
     * Research Sustainability Champion
     */
    RESEARCH_CHAMPION(ActorRoleGroup.GOVERNANCE_REPRESENTATIVE.getName(),
                      "RES-SUSTAINABILITY-CHAMP",
                      "Research Sustainability Champion",
                      "Provides support and education to Coco Pharmaceuticals employees to help them improve the sustainability of their work.",
                      ScopeDefinition.WITHIN_ORGANIZATION,
                      false, 0,
                      BusinessAreaDefinition.RESEARCH,
                      PersonDefinition.TESSA_TUBE,
                      GovernanceDefinition.SUSTAINABILITY_CHAMPION_RESPONSIBILITY),

    /**
     * Clinical Trials Sustainability Champion
     */
    CLINICAL_TRIALS_CHAMPION(ActorRoleGroup.GOVERNANCE_REPRESENTATIVE.getName(),
                             "C-TRL-SUSTAINABILITY-CHAMP",
                             "Clinical Trials Sustainability Champion",
                             "Provides support and education to Coco Pharmaceuticals employees to help them improve the sustainability of their work.",
                             ScopeDefinition.WITHIN_ORGANIZATION,
                             false, 0,
                             BusinessAreaDefinition.CLINICAL_TRIALS,
                             PersonDefinition.TANYA_TIDIE,
                             GovernanceDefinition.SUSTAINABILITY_CHAMPION_RESPONSIBILITY),

    /**
     * Finance Sustainability Champion
     */
    FINANCE_TRIALS_CHAMPION(ActorRoleGroup.GOVERNANCE_REPRESENTATIVE.getName(),
                            "FIN-SUSTAINABILITY-CHAMP",
                            "Finance Sustainability Champion",
                            "Provides support and education to Coco Pharmaceuticals employees to help them improve the sustainability of their work.",
                            ScopeDefinition.WITHIN_ORGANIZATION,
                            false, 0,
                            BusinessAreaDefinition.FINANCE,
                            PersonDefinition.SALLY_COUNTER,
                            GovernanceDefinition.SUSTAINABILITY_CHAMPION_RESPONSIBILITY),

    /**
     * HR Sustainability Champion
     */
    HR_CHAMPION(ActorRoleGroup.GOVERNANCE_REPRESENTATIVE.getName(),
                "HR-SUSTAINABILITY-CHAMP",
                "HR Sustainability Champion",
                "Provides support and education to Coco Pharmaceuticals employees to help them improve the sustainability of their work.",
                ScopeDefinition.WITHIN_ORGANIZATION,
                false, 0,
                BusinessAreaDefinition.HR,
                PersonDefinition.FAITH_BROKER,
                GovernanceDefinition.SUSTAINABILITY_CHAMPION_RESPONSIBILITY),

    /**
     * IT Sustainability Champion
     */
    IT_CHAMPION(ActorRoleGroup.GOVERNANCE_REPRESENTATIVE.getName(),
                "IT-SUSTAINABILITY-CHAMP",
                "IT Sustainability Champion",
                "Provides support and education to Coco Pharmaceuticals employees to help them improve the sustainability of their work.",
                ScopeDefinition.WITHIN_ORGANIZATION,
                false, 0,
                BusinessAreaDefinition.IT,
                PersonDefinition.GARY_GEEKE,
                GovernanceDefinition.SUSTAINABILITY_CHAMPION_RESPONSIBILITY),

    /**
     * Manufacturing Sustainability Champion
     */
    MANUFACTURING_CHAMPION(ActorRoleGroup.GOVERNANCE_REPRESENTATIVE.getName(),
                           "MFG-SUSTAINABILITY-CHAMP",
                           "Manufacturing Sustainability Champion",
                           "Provides support and education to Coco Pharmaceuticals employees to help them improve the sustainability of their work.",
                           ScopeDefinition.WITHIN_ORGANIZATION,
                           false, 0,
                           BusinessAreaDefinition.MANUFACTURING,
                           PersonDefinition.STEW_FASTER,
                           GovernanceDefinition.SUSTAINABILITY_CHAMPION_RESPONSIBILITY),

    /**
     * Distribution Sustainability Champion
     */
    DIST_CHAMPION(ActorRoleGroup.GOVERNANCE_REPRESENTATIVE.getName(),
                  "DIST-SUSTAINABILITY-CHAMP",
                  "Distribution Sustainability Champion",
                  "Provides support and education to Coco Pharmaceuticals employees to help them improve the sustainability of their work.",
                  ScopeDefinition.WITHIN_ORGANIZATION,
                  false, 0,
                  BusinessAreaDefinition.DISTRIBUTION,
                  null,
                  GovernanceDefinition.SUSTAINABILITY_CHAMPION_RESPONSIBILITY),

    /**
     * Sales Sustainability Champion
     */
    SALES_CHAMPION(ActorRoleGroup.GOVERNANCE_REPRESENTATIVE.getName(),
                   "SALES-SUSTAINABILITY-CHAMP",
                   "Sales Sustainability Champion",
                   "Provides support and education to Coco Pharmaceuticals employees to help them improve the sustainability of their work.",
                   ScopeDefinition.WITHIN_ORGANIZATION,
                   false, 0,
                   BusinessAreaDefinition.SALES,
                   PersonDefinition.HARRY_HOPEFUL,
                   GovernanceDefinition.SUSTAINABILITY_CHAMPION_RESPONSIBILITY),

    /**
     * Sustainability Technology Lead
     */
    SUSTAINABILITY_TECH_LEAD(ActorRoleGroup.SOLUTION_OWNER.getName(),
                             "SUSTAINABILITY-TECH-LEAD",
                             "Sustainability Technology Lead",
                             "Builds and maintains the information supply chain for sustainability reporting.",
                             ScopeDefinition.WITHIN_ORGANIZATION,
                             false, 0,
                             null,
                             PersonDefinition.PETER_PROFILE,
                             GovernanceDefinition.SUSTAINABILITY_TECHNOLOGY_LEAD_RESPONSIBILITY),

    /**
     * Amsterdam Site Leader
     */
    AMS_SITE_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                    "AMS-SITE-LEADER",
                    "Amsterdam Site Leader",
                    "Leader responsible for the care and compliance of the Amsterdam site.",
                    ScopeDefinition.WITHIN_SITE,
                    true, 1,
                    null,
                    null,
                    GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * Amsterdam Lab Leader
     */
    AMS_LAB_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                   "AMS-LAB-LEADER",
                   "Amsterdam Lab Leader",
                   "Leader responsible for the care and compliance of the Amsterdam lab.",
                   ScopeDefinition.WITHIN_FACILITY,
                   true, 1,
                   null,
                   null,
                   GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * Amsterdam Office Leader
     */
    AMS_OFFICE_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                      "AMS-OFFICE-LEADER",
                      "Amsterdam Office Leader",
                      "Leader responsible for the care and compliance of the Amsterdam office.",
                      ScopeDefinition.WITHIN_FACILITY,
                      true, 1,
                      null,
                      null,
                      GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * Amsterdam Data Center Leader
     */
    AMS_DC_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                  "AMS-DC-LEADER",
                  "Amsterdam Data Center Leader",
                  "Leader responsible for the care and compliance of the Amsterdam Data Center.",
                  ScopeDefinition.WITHIN_FACILITY,
                  true, 1,
                  null,
                  null,
                  GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),


    /**
     * Amsterdam Distribution Center Leader
     */
    AMS_DEPOT_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                     "AMS-DEPOT-LEADER",
                     "Amsterdam Distribution Center Leader",
                     "Leader responsible for the care and compliance of the Amsterdam Distribution Center.",
                     ScopeDefinition.WITHIN_FACILITY,
                     true, 1,
                     null,
                     null,
                     GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * London Site Leader
     */
    LONDON_SITE_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                       "LDN-SITE-LEADER",
                       "London Site Leader",
                       "Leader responsible for the care and compliance of the London site.",
                       ScopeDefinition.WITHIN_SITE,
                       true, 1,
                       null,
                       null,
                       GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * London Lab Leader
     */
    LONDON_LAB_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                      "LDN-LAB-LEADER",
                      "London Lab Leader",
                      "Leader responsible for the care and compliance of the London lab.",
                      ScopeDefinition.WITHIN_FACILITY,
                      true, 1,
                      null,
                      null,
                      GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * London Office Leader
     */
    LONDON_OFFICE_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                         "LDN-OFFICE-LEADER",
                         "London Office Leader",
                         "Leader responsible for the care and compliance of the London office.",
                         ScopeDefinition.WITHIN_FACILITY,
                         true, 1,
                         null,
                         null,
                         GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * London Data Center Leader
     */
    LONDON_DC_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                     "LDN-DC-LEADER",
                     "London Data Center Leader",
                     "Leader responsible for the care and compliance of the London Data Center.",
                     ScopeDefinition.WITHIN_FACILITY,
                     true, 1,
                     null,
                     null,
                     GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * New York Site Leader
     */
    NEW_YORK_SITE_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                         "NY-SITE-LEADER",
                         "New York Site Leader",
                         "Leader responsible for the care and compliance of the New York site.",
                         ScopeDefinition.WITHIN_SITE,
                         true, 1,
                         null,
                         null,
                         GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * New York Lab Leader
     */
    NEW_YORK_LAB_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                        "NY-LAB-LEADER",
                        "New York Lab Leader",
                        "Leader responsible for the care and compliance of the New York lab.",
                        ScopeDefinition.WITHIN_FACILITY,
                        true, 1,
                        null,
                        null,
                        GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * New York Office Leader
     */
    NEW_YORK_OFFICE_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                           "NY-OFFICE-LEADER",
                           "New York Office Leader",
                           "Leader responsible for the care and compliance of the New York office.",
                           ScopeDefinition.WITHIN_FACILITY,
                           true, 1,
                           null,
                           null,
                           GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * New York Data Center Leader
     */
    NEW_YORK_DC_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                       "NY-DC-LEADER",
                       "New York Data Center Leader",
                       "Leader responsible for the care and compliance of the New York Data Center.",
                       ScopeDefinition.WITHIN_FACILITY,
                       true, 1,
                       null,
                       null,
                       GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * Austin Site Leader
     */
    AUSTIN_SITE_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                       "AUS-SITE-LEADER",
                       "Austin Site Leader",
                       "Leader responsible for the care and compliance of the Austin site.",
                       ScopeDefinition.WITHIN_SITE,
                       true, 1,
                       null,
                       null,
                       GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * Austin Office Leader
     */
    AUSTIN_OFFICE_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                         "AUS-OFFICE-LEADER",
                         "Austin Office Leader",
                         "Leader responsible for the care and compliance of the Austin office.",
                         ScopeDefinition.WITHIN_FACILITY,
                         true, 1,
                         null,
                         null,
                         GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * Austin Data Center Leader
     */
    AUSTIN_DC_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                     "AUS-DC-LEADER",
                     "Austin Data Center Leader",
                     "Leader responsible for the care and compliance of the Austin Data Center.",
                     ScopeDefinition.WITHIN_FACILITY,
                     true, 1,
                     null,
                     null,
                     GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),


    /**
     * Austin Factory Leader
     */
    AUSTIN_FACTORY_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                          "AUS-FACTORY-LEADER",
                          "Austin Factory Leader",
                          "Leader responsible for the care and compliance of the Austin manufacturing facility.",
                          ScopeDefinition.WITHIN_FACILITY,
                          true, 1,
                          null,
                          null,
                          GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * Winchester Site Leader
     */
    WINCHESTER_SITE_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                           "WINCH-SITE-LEADER",
                           "Winchester Site Leader",
                           "Leader responsible for the care and compliance of the Winchester site.",
                           ScopeDefinition.WITHIN_SITE,
                           true, 1,
                           null,
                           null,
                           GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * Winchester Office Leader
     */
    WINCHESTER_OFFICE_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                             "WINCH-OFFICE-LEADER",
                             "Winchester Office Leader",
                             "Leader responsible for the care and compliance of the Winchester office.",
                             ScopeDefinition.WITHIN_FACILITY,
                             true, 1,
                             null,
                             null,
                             GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * Winchester Data Centre Leader
     */
    WINCHESTER_DC_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                         "WINCH-DC-LEADER",
                         "Winchester Data Centre Leader",
                         "Leader responsible for the care and compliance of the Winchester Data Centre.",
                         ScopeDefinition.WITHIN_FACILITY,
                         true, 1,
                         null,
                         null,
                         GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * Winchester Factory Leader
     */
    WINCHESTER_FACTORY_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                              "WINCH-FACTORY-LEADER",
                              "Winchester Factory Leader",
                              "Leader responsible for the care and compliance of the Winchester manufacturing facility.",
                              ScopeDefinition.WITHIN_FACILITY,
                              true, 1,
                              null,
                              null,
                              GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * Winchester Distribution Centre Leader
     */
    WINCHESTER_DEPOT_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                            "WINCH-DEPOT-LEADER",
                            "Winchester Distribution Centre Leader",
                            "Leader responsible for the care and compliance of the Winchester Distribution Centre.",
                            ScopeDefinition.WITHIN_FACILITY,
                            true, 1,
                            null,
                            null,
                            GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * Kansas City Site Leader
     */
    KANSAS_CITY_SITE_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                            "KC-SITE-LEADER",
                            "Kansas City Site Leader",
                            "Leader responsible for the care and compliance of the Kansas City site.",
                            ScopeDefinition.WITHIN_SITE,
                            true, 1,
                            null,
                            null,
                            GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * Kansas City Office Leader
     */
    KANSAS_CITY_OFFICE_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                              "KC-OFFICE-LEADER",
                              "Kansas City Office Leader",
                              "Leader responsible for the care and compliance of the Kansas City office.",
                              ScopeDefinition.WITHIN_FACILITY,
                              true, 1,
                              null,
                              null,
                              GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),


    /**
     * Kansas City Distribution Center Leader
     */
    KANSAS_CITY_DEPOT_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                             "KC-DEPOT-LEADER",
                             "Kansas City Distribution Center Leader",
                             "Leader responsible for the care and compliance of the Kansas City Distribution Center.",
                             ScopeDefinition.WITHIN_FACILITY,
                             true, 1,
                             null,
                             null,
                             GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * Edmonton Site Leader
     */
    EDMONTON_SITE_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                         "ED-SITE-LEADER",
                         "Edmonton Site Leader",
                         "Leader responsible for the care and compliance of the Edmonton site.",
                         ScopeDefinition.WITHIN_SITE,
                         true, 1,
                         null,
                         null,
                         GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * Edmonton Office Leader
     */
    EDMONTON_OFFICE_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                           "ED-OFFICE-LEADER",
                           "Edmonton Office Leader",
                           "Leader responsible for the care and compliance of the Edmonton office.",
                           ScopeDefinition.WITHIN_FACILITY,
                           true, 1,
                           null,
                           null,
                           GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * Edmonton Data Centre Leader
     */
    EDMONTON_DC_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                       "ED-DC-LEADER",
                       "Edmonton Data Centre Leader",
                       "Leader responsible for the care and compliance of the Edmonton Data Centre.",
                       ScopeDefinition.WITHIN_FACILITY,
                       true, 1,
                       null,
                       null,
                       GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * Edmonton Factory Leader
     */
    EDMONTON_FACTORY_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                            "ED-FACTORY-LEADER",
                            "Edmonton Factory Leader",
                            "Leader responsible for the care and compliance of the Edmonton manufacturing facility.",
                            ScopeDefinition.WITHIN_FACILITY,
                            true, 1,
                            null,
                            null,
                            GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    /**
     * Edmonton Distribution Centre Leader
     */
    EDMONTON_DEPOT_LEADER(ActorRoleGroup.LOCATION_OWNER.getName(),
                          "ED-DEPOT-LEADER",
                          "Edmonton Distribution Centre Leader",
                          "Leader responsible for the care and compliance of the Edmonton Distribution Centre.",
                          ScopeDefinition.WITHIN_FACILITY,
                          true, 1,
                          null,
                          null,
                          GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),
    ;

    private final String                 groupName;
    private final String                 identifier;
    private final String                 displayName;
    private final String                 description;
    private final ScopeDefinition        scope;
    private final boolean                headCountSet;
    private final int                    headCount;
    private final BusinessAreaDefinition businessArea;
    private final PersonDefinition       appointee;
    private final GovernanceDefinition   governanceResponsibility;

    /**
     * SustainabilityRoleDefinition constructor creates an instance of the enum
     *
     * @param groupName name of the type for the role
     * @param identifier   unique Id for the role
     * @param displayName   text for the role
     * @param scope scope of the role
     * @param description   description of the assets in the role
     * @param headCountSet should the headcount property be set?
     * @param headCount   criteria for inclusion
     * @param businessArea which part of the business
     * @param appointee who is appointed
     * @param governanceResponsibility what is the associated responsibility
     */
    SustainabilityRoleDefinition(String groupName,
                                 String                 identifier,
                                 String                 displayName,
                                 String                 description,
                                 ScopeDefinition        scope,
                                 boolean                headCountSet,
                                 int                    headCount,
                                 BusinessAreaDefinition businessArea,
                                 PersonDefinition       appointee,
                                 GovernanceDefinition   governanceResponsibility)
    {
        this.groupName  = groupName;
        this.identifier = identifier;
        this.displayName = displayName;
        this.description = description;
        this.scope = scope;
        this.headCountSet = headCountSet;
        this.headCount = headCount;
        this.businessArea = businessArea;
        this.appointee = appointee;
        this.governanceResponsibility = governanceResponsibility;
    }


    /**
     * Return the name of the PersonRole group to use.
     *
     * @return type name
     */
    public String getGroupName()
    {
        return groupName;
    }


    /**
     * Returns the unique name for the role entity.
     *
     * @return identifier
     */
    public String getQualifiedName()
    {
        return groupName + "::" + identifier;
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
    public ScopeDefinition getScope()
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
     * The business area of the sustainability champion role.
     *
     * @return business area
     */
    public BusinessAreaDefinition getBusinessArea()
    {
        return businessArea;
    }


    /**
     * The person to be appointed to the role.
     *
     * @return person
     */
    public PersonDefinition getAppointee()
    {
        return appointee;
    }


    /**
     * Return the associated responsibility.
     *
     * @return governance definition
     */
    public GovernanceDefinition getGovernanceResponsibility()
    {
        return governanceResponsibility;
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
