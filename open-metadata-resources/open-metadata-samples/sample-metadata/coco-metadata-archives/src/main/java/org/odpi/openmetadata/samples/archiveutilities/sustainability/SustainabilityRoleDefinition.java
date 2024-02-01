/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.sustainability;


import org.odpi.openmetadata.samples.archiveutilities.organization.BusinessAreaDefinition;
import org.odpi.openmetadata.samples.archiveutilities.organization.PersonDefinition;
import org.odpi.openmetadata.samples.archiveutilities.organization.ScopeDefinition;

/**
 * The SustainabilityRoleDefinition is used to feed the definition of the governance roles for
 * Coco Pharmaceuticals' sustainability program.
 */
public enum SustainabilityRoleDefinition
{
    /**
     * Sustainability Lead
     */
    SUSTAINABILITY_OFFICER("GovernanceOfficer",
                           "SUSTAINABILITY-LEAD",
                           "Sustainability Lead",
                           "1) Conduct sustainability or environment-related risk assessments.\n" +
                                   "2) Create and maintain sustainability program documents, such as schedules and budgets.\n" +
                                   "3) Write project proposals, grant applications, or other documents to pursue funding for environmental initiatives.\n",
                           ScopeDefinition.ALL_COCO,
                           true, 1,
                           null,
                           null),

    /**
     * Research Sustainability Champion
     */
    RESEARCH_CHAMPION("GovernanceRepresentative",
                      "RES-SUSTAINABILITY-CHAMP",
                      "Research Sustainability Champion",
                      "Provides support and education to Coco Pharmaceuticals employees to help them improve the sustainability of their work.",
                      ScopeDefinition.ALL_COCO,
                      false, 0,
                      null,
                      null),

    /**
     * Clinical Trials Sustainability Champion
     */
    CLINICAL_TRIALS_CHAMPION("GovernanceRepresentative",
                            "C-TRL-SUSTAINABILITY-CHAMP",
                            "Clinical Trials Sustainability Champion",
                            "Provides support and education to Coco Pharmaceuticals employees to help them improve the sustainability of their work.",
                            ScopeDefinition.ALL_COCO,
                            false, 0,
                            BusinessAreaDefinition.CLINICAL_TRIALS,
                            PersonDefinition.TANYA_TIDIE),

    /**
     * Finance Sustainability Champion
     */
    FINANCE_TRIALS_CHAMPION("GovernanceRepresentative",
                             "FIN-SUSTAINABILITY-CHAMP",
                             "Finance Sustainability Champion",
                             "Provides support and education to Coco Pharmaceuticals employees to help them improve the sustainability of their work.",
                             ScopeDefinition.ALL_COCO,
                             false, 0,
                             BusinessAreaDefinition.FINANCE,
                             PersonDefinition.TANYA_TIDIE),

    /**
     * HR Sustainability Champion
     */
    HR_CHAMPION("GovernanceRepresentative",
                            "HR-SUSTAINABILITY-CHAMP",
                            "HR Sustainability Champion",
                            "Provides support and education to Coco Pharmaceuticals employees to help them improve the sustainability of their work.",
                            ScopeDefinition.ALL_COCO,
                            false, 0,
                            BusinessAreaDefinition.HR,
                            PersonDefinition.FAITH_BROKER),

    /**
     * IT Sustainability Champion
     */
    IT_CHAMPION("GovernanceRepresentative",
                            "IT-SUSTAINABILITY-CHAMP",
                            "IT Sustainability Champion",
                            "Provides support and education to Coco Pharmaceuticals employees to help them improve the sustainability of their work.",
                            ScopeDefinition.ALL_COCO,
                            false, 0,
                            BusinessAreaDefinition.IT,
                            PersonDefinition.GARY_GEEKE),

    /**
     * Manufacturing Sustainability Champion
     */
    MANUFACTURING_CHAMPION("GovernanceRepresentative",
                "MFG-SUSTAINABILITY-CHAMP",
                "Manufacturing Sustainability Champion",
                "Provides support and education to Coco Pharmaceuticals employees to help them improve the sustainability of their work.",
                ScopeDefinition.ALL_COCO,
                false, 0,
                BusinessAreaDefinition.MANUFACTURING,
                PersonDefinition.STEW_FASTER),

    /**
     * Distribution Sustainability Champion
     */
    DIST_CHAMPION("GovernanceRepresentative",
                           "DIST-SUSTAINABILITY-CHAMP",
                           "Distribution Sustainability Champion",
                           "Provides support and education to Coco Pharmaceuticals employees to help them improve the sustainability of their work.",
                           ScopeDefinition.ALL_COCO,
                           false, 0,
                           BusinessAreaDefinition.DISTRIBUTION,
                           null),

    /**
     * Sales Sustainability Champion
     */
    SALES_CHAMPION("GovernanceRepresentative",
                  "SALES-SUSTAINABILITY-CHAMP",
                  "Sales Sustainability Champion",
                  "Provides support and education to Coco Pharmaceuticals employees to help them improve the sustainability of their work.",
                  ScopeDefinition.ALL_COCO,
                  false, 0,
                  BusinessAreaDefinition.SALES,
                  PersonDefinition.HARRY_HOPEFUL),

    /**
     * Sustainability Technology Lead
     */
    SUSTAINABILITY_TECH_LEAD("SolutionOwner",
                             "SUSTAINABILITY-TECH-LEAD",
                             "Sustainability Technology Lead",
                             "Builds and maintains the information supply chain for sustainability reporting.",
                             ScopeDefinition.ALL_COCO,
                             false, 0,
                             null,
                             null),

    /**
     * Amsterdam Site Leader
     */
    AMS_SITE_LEADER("LocationOwner",
                    "AMS-SITE-LEADER",
                    "Amsterdam Site Leader",
                    "Leader responsible for the care and compliance of the Amsterdam site.",
                    ScopeDefinition.WITHIN_SITE,
                    true, 1,
                    null,
                    null),

    /**
     * Amsterdam Lab Leader
     */
    AMS_LAB_LEADER("LocationOwner",
                   "AMS-LAB-LEADER",
                   "Amsterdam Lab Leader",
                   "Leader responsible for the care and compliance of the Amsterdam lab.",
                   ScopeDefinition.WITHIN_FACILITY,
                   true, 1,
                   null,
                   null),

    /**
     * Amsterdam Office Leader
     */
    AMS_OFFICE_LEADER("LocationOwner",
                      "AMS-OFFICE-LEADER",
                      "Amsterdam Office Leader",
                      "Leader responsible for the care and compliance of the Amsterdam office.",
                      ScopeDefinition.WITHIN_FACILITY,
                      true, 1,
                      null,
                      null),

    /**
     * Amsterdam Data Center Leader
     */
    AMS_DC_LEADER("LocationOwner",
                  "AMS-DC-LEADER",
                  "Amsterdam Data Center Leader",
                  "Leader responsible for the care and compliance of the Amsterdam Data Center.",
                  ScopeDefinition.WITHIN_FACILITY,
                  true, 1,
                  null,
                  null),


    /**
     * Amsterdam Distribution Center Leader
     */
    AMS_DEPOT_LEADER("LocationOwner",
                     "AMS-DEPOT-LEADER",
                     "Amsterdam Distribution Center Leader",
                     "Leader responsible for the care and compliance of the Amsterdam Distribution Center.",
                     ScopeDefinition.WITHIN_FACILITY,
                     true, 1,
                     null,
                     null),

    /**
     * London Site Leader
     */
    LONDON_SITE_LEADER("LocationOwner",
                       "LDN-SITE-LEADER",
                       "London Site Leader",
                       "Leader responsible for the care and compliance of the London site.",
                       ScopeDefinition.WITHIN_SITE,
                       true, 1,
                       null,
                       null),

    /**
     * London Lab Leader
     */
    LONDON_LAB_LEADER("LocationOwner",
                      "LDN-LAB-LEADER",
                      "London Lab Leader",
                      "Leader responsible for the care and compliance of the London lab.",
                      ScopeDefinition.WITHIN_FACILITY,
                      true, 1,
                      null,
                      null),

    /**
     * London Office Leader
     */
    LONDON_OFFICE_LEADER("LocationOwner",
                         "LDN-OFFICE-LEADER",
                         "London Office Leader",
                         "Leader responsible for the care and compliance of the London office.",
                         ScopeDefinition.WITHIN_FACILITY,
                         true, 1,
                         null,
                         null),

    /**
     * London Data Center Leader
     */
    LONDON_DC_LEADER("LocationOwner",
                     "LDN-DC-LEADER",
                     "London Data Center Leader",
                     "Leader responsible for the care and compliance of the London Data Center.",
                     ScopeDefinition.WITHIN_FACILITY,
                     true, 1,
                     null,
                     null),

    /**
     * New York Site Leader
     */
    NEW_YORK_SITE_LEADER("LocationOwner",
                         "NY-SITE-LEADER",
                         "New York Site Leader",
                         "Leader responsible for the care and compliance of the New York site.",
                         ScopeDefinition.WITHIN_SITE,
                         true, 1,
                         null,
                         null),

    /**
     * New York Lab Leader
     */
    NEW_YORK_LAB_LEADER("LocationOwner",
                        "NY-LAB-LEADER",
                        "New York Lab Leader",
                        "Leader responsible for the care and compliance of the New York lab.",
                        ScopeDefinition.WITHIN_FACILITY,
                        true, 1,
                        null,
                        null),

    /**
     * New York Office Leader
     */
    NEW_YORK_OFFICE_LEADER("LocationOwner",
                           "NY-OFFICE-LEADER",
                           "New York Office Leader",
                           "Leader responsible for the care and compliance of the New York office.",
                           ScopeDefinition.WITHIN_FACILITY,
                           true, 1,
                           null,
                           null),

    /**
     * New York Data Center Leader
     */
    NEW_YORK_DC_LEADER("LocationOwner",
                       "NY-DC-LEADER",
                       "New York Data Center Leader",
                       "Leader responsible for the care and compliance of the New York Data Center.",
                       ScopeDefinition.WITHIN_FACILITY,
                       true, 1,
                       null,
                       null),

    /**
     * Austin Site Leader
     */
    AUSTIN_SITE_LEADER("LocationOwner",
                       "AUS-SITE-LEADER",
                       "Austin Site Leader",
                       "Leader responsible for the care and compliance of the Austin site.",
                       ScopeDefinition.WITHIN_SITE,
                       true, 1,
                       null,
                       null),

    /**
     * Austin Office Leader
     */
    AUSTIN_OFFICE_LEADER("LocationOwner",
                         "AUS-OFFICE-LEADER",
                         "Austin Office Leader",
                         "Leader responsible for the care and compliance of the Austin office.",
                         ScopeDefinition.WITHIN_FACILITY,
                         true, 1,
                         null,
                         null),

    /**
     * Austin Data Center Leader
     */
    AUSTIN_DC_LEADER("LocationOwner",
                     "AUS-DC-LEADER",
                     "Austin Data Center Leader",
                     "Leader responsible for the care and compliance of the Austin Data Center.",
                     ScopeDefinition.WITHIN_FACILITY,
                     true, 1,
                     null,
                     null),


    /**
     * Austin Factory Leader
     */
    AUSTIN_FACTORY_LEADER("LocationOwner",
                          "AUS-FACTORY-LEADER",
                          "Austin Factory Leader",
                          "Leader responsible for the care and compliance of the Austin manufacturing facility.",
                          ScopeDefinition.WITHIN_FACILITY,
                          true, 1,
                          null,
                          null),

    /**
     * Winchester Site Leader
     */
    WINCHESTER_SITE_LEADER("LocationOwner",
                           "WINCH-SITE-LEADER",
                           "Winchester Site Leader",
                           "Leader responsible for the care and compliance of the Winchester site.",
                           ScopeDefinition.WITHIN_SITE,
                           true, 1,
                           null,
                           null),

    /**
     * Winchester Office Leader
     */
    WINCHESTER_OFFICE_LEADER("LocationOwner",
                             "WINCH-OFFICE-LEADER",
                             "Winchester Office Leader",
                             "Leader responsible for the care and compliance of the Winchester office.",
                             ScopeDefinition.WITHIN_FACILITY,
                             true, 1,
                             null,
                             null),

    /**
     * Winchester Data Centre Leader
     */
    WINCHESTER_DC_LEADER("LocationOwner",
                         "WINCH-DC-LEADER",
                         "Winchester Data Centre Leader",
                         "Leader responsible for the care and compliance of the Winchester Data Centre.",
                         ScopeDefinition.WITHIN_FACILITY,
                         true, 1,
                         null,
                         null),

    /**
     * Winchester Factory Leader
     */
    WINCHESTER_FACTORY_LEADER("LocationOwner",
                              "WINCH-FACTORY-LEADER",
                              "Winchester Factory Leader",
                              "Leader responsible for the care and compliance of the Winchester manufacturing facility.",
                              ScopeDefinition.WITHIN_FACILITY,
                              true, 1,
                              null,
                              null),

    /**
     * Winchester Distribution Centre Leader
     */
    WINCHESTER_DEPOT_LEADER("LocationOwner",
                            "WINCH-DEPOT-LEADER",
                            "Winchester Distribution Centre Leader",
                            "Leader responsible for the care and compliance of the Winchester Distribution Centre.",
                            ScopeDefinition.WITHIN_FACILITY,
                            true, 1,
                            null,
                            null),

    /**
     * Kansas City Site Leader
     */
    KANSAS_CITY_SITE_LEADER("LocationOwner",
                            "KC-SITE-LEADER",
                            "Kansas City Site Leader",
                            "Leader responsible for the care and compliance of the Kansas City site.",
                            ScopeDefinition.WITHIN_SITE,
                            true, 1,
                            null,
                            null),

    /**
     * Kansas City Office Leader
     */
    KANSAS_CITY_OFFICE_LEADER("LocationOwner",
                              "KC-OFFICE-LEADER",
                              "Kansas City Office Leader",
                              "Leader responsible for the care and compliance of the Kansas City office.",
                              ScopeDefinition.WITHIN_FACILITY,
                              true, 1,
                              null,
                              null),


    /**
     * Kansas City Distribution Center Leader
     */
    KANSAS_CITY_DEPOT_LEADER("LocationOwner",
                             "KC-DEPOT-LEADER",
                             "Kansas City Distribution Center Leader",
                             "Leader responsible for the care and compliance of the Kansas City Distribution Center.",
                             ScopeDefinition.WITHIN_FACILITY,
                             true, 1,
                             null,
                             null),

    /**
     * Edmonton Site Leader
     */
    EDMONTON_SITE_LEADER("LocationOwner",
                         "ED-SITE-LEADER",
                         "Edmonton Site Leader",
                         "Leader responsible for the care and compliance of the Edmonton site.",
                         ScopeDefinition.WITHIN_SITE,
                         true, 1,
                         null,
                         null),

    /**
     * Edmonton Office Leader
     */
    EDMONTON_OFFICE_LEADER("LocationOwner",
                           "ED-OFFICE-LEADER",
                           "Edmonton Office Leader",
                           "Leader responsible for the care and compliance of the Edmonton office.",
                           ScopeDefinition.WITHIN_FACILITY,
                           true, 1,
                           null,
                           null),

    /**
     * Edmonton Data Centre Leader
     */
    EDMONTON_DC_LEADER("LocationOwner",
                       "ED-DC-LEADER",
                       "Edmonton Data Centre Leader",
                       "Leader responsible for the care and compliance of the Edmonton Data Centre.",
                       ScopeDefinition.WITHIN_FACILITY,
                       true, 1,
                       null,
                       null),

    /**
     * Edmonton Factory Leader
     */
    EDMONTON_FACTORY_LEADER("LocationOwner",
                            "ED-FACTORY-LEADER",
                            "Edmonton Factory Leader",
                            "Leader responsible for the care and compliance of the Edmonton manufacturing facility.",
                            ScopeDefinition.WITHIN_FACILITY,
                            true, 1,
                            null,
                            null),

    /**
     * Edmonton Distribution Centre Leader
     */
    EDMONTON_DEPOT_LEADER("LocationOwner",
                          "ED-DEPOT-LEADER",
                          "Edmonton Distribution Centre Leader",
                          "Leader responsible for the care and compliance of the Edmonton Distribution Centre.",
                          ScopeDefinition.WITHIN_FACILITY,
                          true, 1,
                          null,
                          null),
    ;

    private final String                 typeName;
    private final String                 identifier;
    private final String                 displayName;
    private final String                 description;
    private final ScopeDefinition        scope;
    private final boolean                headCountSet;
    private final int                    headCount;
    private final BusinessAreaDefinition businessArea;
    private final PersonDefinition       appointee;

    /**
     * SustainabilityRoleDefinition constructor creates an instance of the enum
     *
     * @param typeName name of the type for the role
     * @param identifier   unique Id for the role
     * @param displayName   text for the role
     * @param scope scope of the role
     * @param description   description of the assets in the role
     * @param headCountSet should the headcount property be set?
     * @param headCount   criteria for inclusion
     */
    SustainabilityRoleDefinition(String                 typeName,
                                 String                 identifier,
                                 String                 displayName,
                                 String                 description,
                                 ScopeDefinition        scope,
                                 boolean                headCountSet,
                                 int                    headCount,
                                 BusinessAreaDefinition businessArea,
                                 PersonDefinition       appointee)
    {
        this.typeName = typeName;
        this.identifier = identifier;
        this.displayName = displayName;
        this.description = description;
        this.scope = scope;
        this.headCountSet = headCountSet;
        this.headCount = headCount;
        this.businessArea = businessArea;
        this.appointee = appointee;
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
        return typeName + identifier;
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
