/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.sustainability;


import org.odpi.openmetadata.samples.archiveutilities.organization.OrganizationDefinition;
import org.odpi.openmetadata.samples.archiveutilities.organization.PersonDefinition;
import org.odpi.openmetadata.samples.archiveutilities.organization.WorkLocationDefinition;

/**
 * The FacilityDefinition is used to feed the location definitions for the sites and facilities that Coco Pharmaceuticals operates.  It is used by
 * Coco Pharmaceuticals' sustainability program.  It links to the valid values for the major sites (WorkLocations).
 */
public enum FacilityDefinition
{
    /**
     * Amsterdam Site
     */
    AMSTERDAM_SITE("AMS-SITE",
                   "Amsterdam Site",
                   null,
                   FacilityTypeDefinition.SITE,
                   null,
                   WorkLocationDefinition.AMSTERDAM_SITE,
                   SustainabilityRoleDefinition.AMS_SITE_LEADER,
                   PersonDefinition.STEVE_STARTER,
                   OrganizationDefinition.COCO),

    /**
     * Van Leeuwenhoek Cancer Research Laboratory
     */
    AMSTERDAM_LAB("AMS-LAB",
                  "Van Leeuwenhoek Cancer Research Laboratory",
                  "Skin Oncology Research Lab.",
                  FacilityTypeDefinition.LAB,
                  FacilityDefinition.AMSTERDAM_SITE,
                  null,
                  SustainabilityRoleDefinition.AMS_LAB_LEADER,
                  PersonDefinition.STEVE_STARTER,
                  OrganizationDefinition.COCO),

    /**
     * Amsterdam Office
     */
    AMSTERDAM_OFFICE("AMS-OFFICE",
                     "Amsterdam Office",
                     "Amsterdam Sales, Administration and IT Office.",
                     FacilityTypeDefinition.OFFICE,
                     FacilityDefinition.AMSTERDAM_SITE,
                     null,
                     SustainabilityRoleDefinition.AMS_OFFICE_LEADER,
                     PersonDefinition.STEVE_STARTER,
                     OrganizationDefinition.COCO),

    /**
     * Amsterdam Data Centre
     */
    AMSTERDAM_DC("AMS-DATA-CENTER",
                 "Amsterdam Data Centre",
                 "Shared systems data located on the Amsterdam Site.",
                 FacilityTypeDefinition.DATA_CENTER,
                 FacilityDefinition.AMSTERDAM_SITE,
                 null,
                 SustainabilityRoleDefinition.AMS_OFFICE_LEADER,
                 PersonDefinition.GARY_GEEKE,
                 OrganizationDefinition.COCO),

    /**
     * Amsterdam Distribution Centre
     */
    AMSTERDAM_DEPOT("AMS-DEPOT",
                    "Amsterdam Distribution Centre",
                    "Distribution center and warehouse located on the Amsterdam Site.",
                    FacilityTypeDefinition.DEPOT,
                    FacilityDefinition.AMSTERDAM_SITE,
                    null,
                    SustainabilityRoleDefinition.AMS_DEPOT_LEADER,
                    PersonDefinition.STEW_FASTER,
                    OrganizationDefinition.COCO),

    /**
     * London Site
     */
    LONDON_SITE("LDN-SITE",
                "London Site",
                null,
                FacilityTypeDefinition.SITE,
                null,
                WorkLocationDefinition.LONDON_SITE,
                SustainabilityRoleDefinition.LONDON_SITE_LEADER,
                PersonDefinition.TERRI_DARING,
                OrganizationDefinition.COCO),

    /**
     * Nightingale Cancer Research Laboratory
     */
    LONDON_LAB("LDN-LAB",
               "Nightingale Cancer Research Laboratory",
               "Leukemia Research Lab.",
               FacilityTypeDefinition.LAB,
               FacilityDefinition.LONDON_SITE,
               null,
               SustainabilityRoleDefinition.LONDON_LAB_LEADER,
               PersonDefinition.TERRI_DARING,
               OrganizationDefinition.COCO),

    /**
     * London Office
     */
    LONDON_OFFICE("LDN-OFFICE",
                  "London Office",
                  "Finance and HR Office",
                  FacilityTypeDefinition.OFFICE,
                  FacilityDefinition.LONDON_SITE,
                  null,
                  SustainabilityRoleDefinition.LONDON_OFFICE_LEADER,
                  PersonDefinition.REGGIE_MINT,
                  OrganizationDefinition.COCO),

    /**
     * London Data Centre
     */
    LONDON_DC("LDN-DC",
              "London Data Centre",
              "",
              FacilityTypeDefinition.DATA_CENTER,
              FacilityDefinition.LONDON_SITE,
              null,
              SustainabilityRoleDefinition.LONDON_DC_LEADER,
              PersonDefinition.GARY_GEEKE,
              OrganizationDefinition.COCO),

    /**
     * New York Site
     */
    NEW_YORK_SITE("NY-SITE",
                  "New York Site",
                  null,
                  FacilityTypeDefinition.SITE,
                  null,
                  WorkLocationDefinition.NEW_YORK_SITE,
                  SustainabilityRoleDefinition.NEW_YORK_SITE_LEADER,
                  PersonDefinition.ZACH_NOW,
                  OrganizationDefinition.COCO),

    /**
     * Salk Cancer Research Laboratory
     */
    NEW_YORK_LAB("NY-LAB",
                 "Salk Cancer Research Laboratory",
                 "Organ Oncology Research Lab.",
                 FacilityTypeDefinition.LAB,
                 FacilityDefinition.NEW_YORK_SITE,
                 null,
                 SustainabilityRoleDefinition.NEW_YORK_LAB_LEADER,
                 PersonDefinition.TESSA_TUBE,
                 OrganizationDefinition.COCO),

    /**
     * New York Office
     */
    NEW_YORK_OFFICE("NY-OFFICE",
                    "New York Office",
                    "",
                    FacilityTypeDefinition.OFFICE,
                    FacilityDefinition.NEW_YORK_SITE,
                    null,
                    SustainabilityRoleDefinition.NEW_YORK_OFFICE_LEADER,
                    PersonDefinition.ZACH_NOW,
                    OrganizationDefinition.COCO),

    /**
     * New York Data Center
     */
    NEW_YORK_DC("NY-DC",
                "New York Data Center",
                "",
                FacilityTypeDefinition.DATA_CENTER,
                FacilityDefinition.NEW_YORK_SITE,
                null,
                SustainabilityRoleDefinition.NEW_YORK_DC_LEADER,
                PersonDefinition.GARY_GEEKE,
                OrganizationDefinition.COCO),

    /**
     * Winchester Site
     */
    WINCHESTER_SITE("WINCH-SITE",
                    "Winchester Site",
                    "UK Manufacturing site",
                    FacilityTypeDefinition.SITE,
                    null,
                    WorkLocationDefinition.WINCHESTER_SITE,
                    SustainabilityRoleDefinition.WINCHESTER_SITE_LEADER,
                    null,
                    OrganizationDefinition.COCO),

    /**
     * Rosalind Franklin Factory
     */
    WINCHESTER_FACTORY("WINCH-FACTORY",
                       "Rosalind Franklin Factory",
                       "UK and EU Manufacturing.",
                       FacilityTypeDefinition.FACTORY,
                       FacilityDefinition.WINCHESTER_SITE,
                       null,
                       SustainabilityRoleDefinition.WINCHESTER_FACTORY_LEADER,
                       PersonDefinition.STEW_FASTER,
                       OrganizationDefinition.COCO),

    /**
     * UK and EU Manufacturing Office.
     */
    WINCHESTER_OFFICE("WINCH-OFFICE",
                      "Winchester Office",
                      "UK and EU Manufacturing Office.",
                      FacilityTypeDefinition.OFFICE,
                      FacilityDefinition.WINCHESTER_SITE,
                      null,
                      SustainabilityRoleDefinition.WINCHESTER_OFFICE_LEADER,
                      PersonDefinition.STEW_FASTER,
                      OrganizationDefinition.COCO),

    /**
     * UK and EU Manufacturing Data Centre.
     */
    WINCHESTER_DC("WINCH-DC",
                  "Winchester Data Centre",
                  "UK and EU Manufacturing Data Centre.",
                  FacilityTypeDefinition.DATA_CENTER,
                  FacilityDefinition.WINCHESTER_SITE,
                  null,
                  SustainabilityRoleDefinition.WINCHESTER_DC_LEADER,
                  PersonDefinition.STEW_FASTER,
                  OrganizationDefinition.COCO),

    /**
     * Winchester Distribution Center
     */
    WINCHESTER_DEPOT("WINCH-DEPOT",
                     "Winchester Distribution Center",
                     "Distribution center and warehouse located on the Winchester Site.",
                     FacilityTypeDefinition.DEPOT,
                     FacilityDefinition.WINCHESTER_SITE,
                     null,
                     SustainabilityRoleDefinition.WINCHESTER_DEPOT_LEADER,
                     PersonDefinition.STEW_FASTER,
                     OrganizationDefinition.COCO),

    /**
     * Kansas City Site
     */
    KANSAS_CITY_SITE("KC-SITE",
                     "Kansas City Site",
                     "NA Distribution Center",
                     FacilityTypeDefinition.SITE,
                     null,
                     WorkLocationDefinition.KANSAS_CITY_SITE,
                     SustainabilityRoleDefinition.KANSAS_CITY_SITE_LEADER,
                     null,
                     OrganizationDefinition.COCO),

    /**
     * Kansas City Office
     */
    KANSAS_CITY_OFFICE("KC-OFFICE",
                       "Kansas City Office",
                       "Distribution center and warehouse office, Kansas City.",
                       FacilityTypeDefinition.OFFICE,
                       FacilityDefinition.KANSAS_CITY_SITE,
                       null,
                       SustainabilityRoleDefinition.KANSAS_CITY_OFFICE_LEADER,
                       PersonDefinition.STEW_FASTER,
                       OrganizationDefinition.COCO),

    /**
     * Kansas City Distribution Center
     */
    KANSAS_CITY_DEPOT("KC-DEPOT",
                      "Kansas City Distribution Center",
                      "Distribution center and warehouse located on the Kansas City Site.",
                      FacilityTypeDefinition.DEPOT,
                      FacilityDefinition.KANSAS_CITY_SITE,
                      null,
                      SustainabilityRoleDefinition.KANSAS_CITY_DEPOT_LEADER,
                      PersonDefinition.STEW_FASTER,
                      OrganizationDefinition.COCO),

    /**
     * Edmonton Site
     */
    EDMONTON_SITE("ED-SITE",
                  "Edmonton Site",
                  "NA Manufacturing Site",
                  FacilityTypeDefinition.SITE,
                  null,
                  WorkLocationDefinition.EDMONTON_SITE,
                  SustainabilityRoleDefinition.EDMONTON_SITE_LEADER,
                  null,
                  OrganizationDefinition.COCO),

    /**
     * Edmonton Office
     */
    EDMONTON_OFFICE("ED-OFFICE",
                    "Edmonton Office",
                    "Edmonton office.",
                    FacilityTypeDefinition.OFFICE,
                    FacilityDefinition.EDMONTON_SITE,
                    null,
                    SustainabilityRoleDefinition.EDMONTON_OFFICE_LEADER,
                    null,
                    OrganizationDefinition.COCO),

    /**
     * Edmonton Office
     */
    EDMONTON_DC("ED-DC",
                    "Edmonton Data Centre",
                    "Edmonton data centre.",
                    FacilityTypeDefinition.DATA_CENTER,
                    FacilityDefinition.EDMONTON_SITE,
                    null,
                    SustainabilityRoleDefinition.EDMONTON_DC_LEADER,
                    null,
                    OrganizationDefinition.COCO),

    /**
     * Edmonton Manufacturing Center
     */
    EDMONTON_FACTORY("ED-FACTORY",
                     "Edmonton Manufacturing Center",
                     "",
                     FacilityTypeDefinition.FACTORY,
                     FacilityDefinition.EDMONTON_SITE,
                     null,
                     SustainabilityRoleDefinition.EDMONTON_FACTORY_LEADER,
                     null,
                     OrganizationDefinition.COCO),

    /**
     * Edmonton Distribution Center
     */
    EDMONTON_DEPOT("ED-DEPOT",
                   "Edmonton Distribution Center",
                   "Distribution center and warehouse located on the Edmonton Site.",
                   FacilityTypeDefinition.DEPOT,
                   FacilityDefinition.EDMONTON_SITE,
                   null,
                   SustainabilityRoleDefinition.EDMONTON_DEPOT_LEADER,
                   PersonDefinition.STEW_FASTER,
                   OrganizationDefinition.COCO),

    /**
     * Austin Site
     */
    AUSTIN_SITE("AUS-SITE",
                "Austin Site",
                "US advanced manufacturing site",
                FacilityTypeDefinition.SITE,
                null,
                WorkLocationDefinition.AUSTIN_SITE,
                SustainabilityRoleDefinition.AUSTIN_SITE_LEADER,
                null,
                OrganizationDefinition.COCO),

    /**
     * Austin Factory
     */
    AUSTIN_FACTORY("AUS-FACTORY",
                   "Austin Factory",
                   "US manufacturing site specializing in small batches.",
                   FacilityTypeDefinition.FACTORY,
                   FacilityDefinition.AUSTIN_SITE,
                   null,
                   SustainabilityRoleDefinition.AUSTIN_FACTORY_LEADER,
                   PersonDefinition.STEW_FASTER,
                   OrganizationDefinition.COCO),

    /**
     * Austin Office
     */
    AUSTIN_OFFICE("AUS-OFFICE",
                  "Austin Office",
                  "Austin Manufacturing Office.",
                  FacilityTypeDefinition.OFFICE,
                  FacilityDefinition.AUSTIN_SITE,
                  null,
                  SustainabilityRoleDefinition.AUSTIN_OFFICE_LEADER,
                  PersonDefinition.STEW_FASTER,
                  OrganizationDefinition.COCO),

    /**
     * Austin Data Center
     */
    AUSTIN_DC("AUS-DC",
              "Austin Data Center",
              "Austin Data Center.",
              FacilityTypeDefinition.DATA_CENTER,
              FacilityDefinition.AUSTIN_SITE,
              null,
              SustainabilityRoleDefinition.AUSTIN_DC_LEADER,
              PersonDefinition.STEW_FASTER,
              OrganizationDefinition.COCO),

    /**
     * Hampton Hospital
     */
    HAMPTON_HOSPITAL("HH",
                     "Hampton Hospital",
                     "Oncology Unit 1, Hampton Hospital",
                     FacilityTypeDefinition.SITE,
                     null,
                     WorkLocationDefinition.HAMPTON_HOSPITAL,
                     null,
                     null,
                     OrganizationDefinition.HAMPTON),

    /**
     * Oak Dene Hospital
     */
    OAK_DENE_HOSPITAL("ODH",
                      "Oak Dene Hospital",
                      null,
                      FacilityTypeDefinition.SITE,
                      null,
                      WorkLocationDefinition.OAK_DENE_HOSPITAL,
                      null,
                      null,
                      OrganizationDefinition.OAK_DENE_HOSPITAL),

    /**
     * Old Market Hospital
     */
    OLD_MARKET_HOSPITAL("OMH",
                        "Old Market Hospital",
                        null,
                        FacilityTypeDefinition.SITE,
                        null,
                        WorkLocationDefinition.OLD_MARKET_HOSPITAL,
                        null,
                        null,
                        OrganizationDefinition.OLD_MARKET),

    /**
     * Customer Sales and Invoicing Cloud Services
     */
    CLOUD_PROVIDER_SALES("Sales4U",
                         "Sales4U",
                        "Customer Sales and Invoicing Cloud Services",
                        FacilityTypeDefinition.CLOUD,
                        null,
                        null,
                        null,
                        null,
                         OrganizationDefinition.SALES_CP),

    /**
     * FinMagic
     */
    CLOUD_PROVIDER_FINANCE("FinMagic",
                           "FinMagic",
                           "Accounting Cloud Services supporting book-keeping and ledgers for international businesses.",
                         FacilityTypeDefinition.CLOUD,
                         null,
                         null,
                         null,
                         null,
                           OrganizationDefinition.FINANCE_CP),

    /**
     * BeThere Travel and Expenses
     */
    CLOUD_PROVIDER_TRAVEL("BeThere",
                          "BeThere Travel and Expenses",
                           "Travel booking and employee expenses cloud services.",
                           FacilityTypeDefinition.CLOUD,
                           null,
                           null,
                           null,
                           null,
                          OrganizationDefinition.TRAVEL_CP),

    ;

    private final String                       identifier;
    private final String                       displayName;
    private final String                       description;
    private final FacilityTypeDefinition       facilityType;
    private final FacilityDefinition           parentSite;
    private final WorkLocationDefinition       associatedWorkLocation;
    private final SustainabilityRoleDefinition facilityLeaderRole;
    private final PersonDefinition             facilityLeader;
    private final OrganizationDefinition       owningOrganization;


    /**
     * The constructor creates an instance of the enum
     *
     * @param identifier   unique id for the location
     * @param displayName   name for the location
     * @param description   description of the location
     * @param facilityType classify the type of facility
     * @param parentSite define any parent relationship
     * @param associatedWorkLocation link to work location valid value
     * @param facilityLeaderRole role for the leader
     * @param facilityLeader  person to be appointed to the leader role
     *
     */
    FacilityDefinition(String                       identifier,
                       String                       displayName,
                       String                       description,
                       FacilityTypeDefinition       facilityType,
                       FacilityDefinition           parentSite,
                       WorkLocationDefinition       associatedWorkLocation,
                       SustainabilityRoleDefinition facilityLeaderRole,
                       PersonDefinition             facilityLeader,
                       OrganizationDefinition       owningOrganization)
    {
        this.identifier = identifier;
        this.displayName = displayName;
        this.description = description;
        this.facilityType = facilityType;
        this.parentSite = parentSite;
        this.associatedWorkLocation = associatedWorkLocation;
        this.facilityLeaderRole = facilityLeaderRole;
        this.facilityLeader = facilityLeader;
        this.owningOrganization = owningOrganization;
    }

    public String getQualifiedName()
    {
        return facilityType.getPreferredValue() + ":" + identifier;
    }


    public String getIdentifier()
    {
        return identifier;
    }


    public String getDisplayName()
    {
        return displayName;
    }


    public String getDescription()
    {
        return description;
    }


    public FacilityTypeDefinition getFacilityType()
    {
        return facilityType;
    }


    public FacilityDefinition getParentSite()
    {
        return parentSite;
    }


    public WorkLocationDefinition getAssociatedWorkLocation()
    {
        return associatedWorkLocation;
    }


    public SustainabilityRoleDefinition getFacilityLeaderRole()
    {
        return facilityLeaderRole;
    }


    public PersonDefinition getFacilityLeader()
    {
        return facilityLeader;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "FacilityDefinition{" + displayName + '}';
    }
}
