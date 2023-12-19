/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.organization;

import java.util.Arrays;
import java.util.List;

/**
 * The DeptDefinition is used to feed the definition of the organization's departments for Coco Pharmaceuticals scenarios.
 */
public enum DeptDefinition
{
    BOARD("4302",
          "Board",
          "Members of the Board of Coco Pharmaceuticals.",
          null,
          new PersonDefinition[]{PersonDefinition.ZACH_NOW, PersonDefinition.TERRI_DARING, PersonDefinition.STEVE_STARTER},
          new PersonDefinition[]{PersonDefinition.REGGIE_MINT, PersonDefinition.JULES_KEEPER, PersonDefinition.FAITH_BROKER, PersonDefinition.IVOR_PADLOCK, PersonDefinition.STEW_FASTER},
          0,
          null,
          BusinessAreaDefinition.GOVERNANCE,
          ScopeDefinition.ALL_COCO),

    FOUNDERS("9999",
             "Founders Team",
             "Founders of Coco Pharmaceuticals.",
             null,
             new PersonDefinition[]{PersonDefinition.ZACH_NOW, PersonDefinition.TERRI_DARING, PersonDefinition.STEVE_STARTER},
             null,
             0,
             null,
             BusinessAreaDefinition.GOVERNANCE,
             ScopeDefinition.ALL_COCO),

    AMSTERDAM ("9998",
               "Amsterdam Site",
               "Amsterdam Employees",
               FOUNDERS,
               new PersonDefinition[]{PersonDefinition.STEVE_STARTER},
               null,
               1,
               WorkLocationDefinition.AMSTERDAM_SITE,
               null,
               ScopeDefinition.WITHIN_SITE),

    HR_GLOBAL("9657",
              "Human Resources",
              "Human Resources for Coco Pharmaceuticals.",
              DeptDefinition.BOARD,
              new PersonDefinition[]{PersonDefinition.FAITH_BROKER},
              null,
              3,
              WorkLocationDefinition.AMSTERDAM_SITE,
              BusinessAreaDefinition.HR,
              ScopeDefinition.ALL_COCO),

    SEC_GLOBAL("0001",
              "Security",
              "Security for Coco Pharmaceuticals.",
              DeptDefinition.BOARD,
              new PersonDefinition[]{PersonDefinition.IVOR_PADLOCK},
              null,
              2,
              WorkLocationDefinition.AMSTERDAM_SITE,
              BusinessAreaDefinition.GOVERNANCE,
              ScopeDefinition.ALL_COCO),

    SALES_EU("0002",
             "Sales",
             "Team selling Coco Pharmaceuticals products to hospitals in EU.",
             DeptDefinition.AMSTERDAM,
             null,
             null,
             5,
             WorkLocationDefinition.AMSTERDAM_SITE,
             BusinessAreaDefinition.SALES,
             ScopeDefinition.WITHIN_REGION),

    AMSTERDAM_LAB("2373",
                  "Van Leeuwenhoek Lab Team",
                  "Amsterdam researchers.",
                  DeptDefinition.AMSTERDAM,
                  new PersonDefinition[]{PersonDefinition.STEVE_STARTER},
                  null,
                  12,
                  WorkLocationDefinition.AMSTERDAM_SITE,
                  BusinessAreaDefinition.RESEARCH,
                  ScopeDefinition.WITHIN_SITE),

    CLINICAL_TRIALS_EU("0003",
                       "Clinical Trials Team (EU)",
                       "Team coordinating the clinical trials with a variety of hospitals in the EU.",
                       DeptDefinition.AMSTERDAM,
                       null,
                       null,
                       8,
                       WorkLocationDefinition.AMSTERDAM_SITE,
                       BusinessAreaDefinition.CLINICAL_TRIALS,
                       ScopeDefinition.WITHIN_REGION),

    IT_GLOBAL("0004",
                 "Global Information Technology Team",
                 "Team managing shared IT resources for Coco Pharmaceuticals.",
                 DeptDefinition.AMSTERDAM,
                 new PersonDefinition[]{PersonDefinition.STEVE_STARTER},
                 new PersonDefinition[]{PersonDefinition.GARY_GEEKE, PersonDefinition.POLLY_TASKER},
                 0,
                 WorkLocationDefinition.AMSTERDAM_SITE,
                 BusinessAreaDefinition.IT,
                 ScopeDefinition.ALL_COCO),

    SE_GLOBAL("3082",
              "Software Development and Testing Team",
              "Team managing bespoke software development for Coco Pharmaceuticals.",
              DeptDefinition.IT_GLOBAL,
              new PersonDefinition[]{PersonDefinition.POLLY_TASKER},
              new PersonDefinition[]{PersonDefinition.BOB_NITTER, PersonDefinition.LEMMIE_STAGE, PersonDefinition.NANCY_NOAH, PersonDefinition.DES_SIGNA},
              1,
              WorkLocationDefinition.AMSTERDAM_SITE,
              null,
              ScopeDefinition.ALL_COCO),

    IT_OPS_GLOBAL("3067",
                  "IT Operations Team",
                  "Team managing shared IT resources for Coco Pharmaceuticals.",
                  DeptDefinition.IT_GLOBAL,
                  new PersonDefinition[]{PersonDefinition.GARY_GEEKE},
                  null,
                  2,
                  WorkLocationDefinition.AMSTERDAM_SITE,
                  null,
                  ScopeDefinition.ALL_COCO),

    AMSTERDAM_DEPOT("0005",
                  "Amsterdam Depot",
                  "Team managing the warehouse and distribution for the EU.",
                  DeptDefinition.AMSTERDAM,
                  null,
                  null,
                  6,
                  WorkLocationDefinition.AMSTERDAM_SITE,
                  BusinessAreaDefinition.DISTRIBUTION,
                  ScopeDefinition.WITHIN_REGION),

    LONDON ("9997",
            "London Site",
            "London Employees",
            FOUNDERS,
            new PersonDefinition[]{PersonDefinition.TERRI_DARING},
            null,
            1,
            WorkLocationDefinition.LONDON_SITE,
            null,
            ScopeDefinition.WITHIN_SITE),

    LONDON_LAB("5656",
               "Nightingale Lab Team",
               "London researchers.",
               DeptDefinition.LONDON,
               new PersonDefinition[]{PersonDefinition.TERRI_DARING},
               null,
               7,
               WorkLocationDefinition.LONDON_SITE,
               BusinessAreaDefinition.RESEARCH,
               ScopeDefinition.WITHIN_SITE),

    CLINICAL_TRIALS_UK("0006",
                       "Clinical Trials Team (UK)",
                       "Team coordinating the clinical trials with a variety of hospitals in the UK.",
                       DeptDefinition.LONDON,
                       null,
                       null,
                       2,
                       WorkLocationDefinition.LONDON_SITE,
                       BusinessAreaDefinition.CLINICAL_TRIALS,
                       ScopeDefinition.WITHIN_REGION),

    CDO("7432",
        "Chief Data Office",
        "Team focusing on data value, use and governance within Coco Pharmaceuticals.",
        DeptDefinition.BOARD,
        new PersonDefinition[]{PersonDefinition.JULES_KEEPER},
        new PersonDefinition[]{PersonDefinition.ERIN_OVERVIEW, PersonDefinition.PETER_PROFILE},
        2,
        WorkLocationDefinition.LONDON_SITE,
        BusinessAreaDefinition.GOVERNANCE,
        ScopeDefinition.ALL_COCO),

    FINANCE("6788",
            "Finance Team",
            "Team responsible for the financial health of Coco Pharmaceuticals.",
            DeptDefinition.BOARD,
            new PersonDefinition[]{PersonDefinition.REGGIE_MINT},
            new PersonDefinition[]{PersonDefinition.TOM_TALLY, PersonDefinition.SIDNEY_SEEKER},
            1,
            WorkLocationDefinition.LONDON_SITE,
            BusinessAreaDefinition.FINANCE,
            ScopeDefinition.ALL_COCO),

    ACCOUNTS("6877",
             "Accounts Team",
             "Team accounting the cash flow through Coco Pharmaceuticals.",
             DeptDefinition.FINANCE,
             new PersonDefinition[]{PersonDefinition.TOM_TALLY},
             new PersonDefinition[]{PersonDefinition.SALLY_COUNTER},
             4,
             WorkLocationDefinition.LONDON_SITE,
             null,
             ScopeDefinition.ALL_COCO),

    SALES_UK("0007",
             "Sales",
             "Team selling Coco Pharmaceuticals products to hospitals in UK.",
             DeptDefinition.LONDON,
             null,
             null,
             4,
             WorkLocationDefinition.LONDON_SITE,
             BusinessAreaDefinition.SALES,
             ScopeDefinition.WITHIN_REGION),

    IT_LONDON("0008",
              "London Information Technology Team",
              "Team managing IT resources for Coco Pharmaceuticals Finance.",
              DeptDefinition.LONDON,
              null,
              null,
              2,
              WorkLocationDefinition.LONDON_SITE,
              BusinessAreaDefinition.IT,
              ScopeDefinition.WITHIN_SITE),

    NEW_YORK ("9996",
              "New York Site",
              "New York Employees",
              FOUNDERS,
              new PersonDefinition[]{PersonDefinition.ZACH_NOW},
              null,
              1,
              WorkLocationDefinition.NEW_YORK_SITE,
              null,
              ScopeDefinition.WITHIN_SITE),

    NEW_YORK_LAB("2343",
                 "Salk Lab Team",
                 "New York researchers.",
                 DeptDefinition.NEW_YORK,
                 new PersonDefinition[]{PersonDefinition.TESSA_TUBE},
                 null,
                 10,
                 WorkLocationDefinition.NEW_YORK_SITE,
                 BusinessAreaDefinition.RESEARCH,
                 ScopeDefinition.WITHIN_SITE),

    CLINICAL_TRIALS_NA("4051",
                       "Clinical Trials Team (NA)",
                       "Team coordinating the clinical trials with a variety of hospitals in USA/Canada.",
                       DeptDefinition.NEW_YORK_LAB,
                       new PersonDefinition[]{PersonDefinition.TESSA_TUBE},
                       new PersonDefinition[]{PersonDefinition.CALLIE_QUARTILE, PersonDefinition.TANYA_TIDIE},
                       8,
                       WorkLocationDefinition.NEW_YORK_SITE,
                       BusinessAreaDefinition.CLINICAL_TRIALS,
                       ScopeDefinition.WITHIN_REGION),

    SALES_NA("0009",
             "Sales",
             "Team selling Coco Pharmaceuticals products to hospitals in NA.",
             DeptDefinition.NEW_YORK,
             new PersonDefinition[]{PersonDefinition.HARRY_HOPEFUL},
             null,
             6,
             WorkLocationDefinition.NEW_YORK_SITE,
             BusinessAreaDefinition.SALES,
             ScopeDefinition.WITHIN_REGION),

    IT_NY("0010",
              "New York Information Technology Team",
              "Team managing IT resources for New York Site.",
              DeptDefinition.NEW_YORK,
              null,
              null,
              2,
              WorkLocationDefinition.NEW_YORK_SITE,
              BusinessAreaDefinition.IT,
              ScopeDefinition.WITHIN_SITE),

    AUSTIN ("9995",
            "Austin Site",
            "Austin Employees",
            FOUNDERS,
            null,
            null,
            1,
            WorkLocationDefinition.AUSTIN_SITE,
            null,
            ScopeDefinition.WITHIN_SITE),

    AUSTIN_PLANT("0011",
                 "Austin Factory",
                 "Team producing and delivering products to customers based in Austin, TX.",
                 DeptDefinition.AUSTIN,
                 null,
                 null,
                 16,
                 WorkLocationDefinition.AUSTIN_SITE,
                 BusinessAreaDefinition.MANUFACTURING,
                 ScopeDefinition.WITHIN_REGION),

    AUSTIN_OFFICE("0012",
                  "Austin Office Team",
                  "Team managing administration for Austin operation.",
                  DeptDefinition.AUSTIN,
                  null,
                  null,
                  9,
                  WorkLocationDefinition.AUSTIN_SITE,
                  BusinessAreaDefinition.MANUFACTURING,
                  ScopeDefinition.WITHIN_SITE),

    IT_AUSTIN("0013",
          "Austin Information Technology Team",
          "Team managing IT resources for Austin manufacturing.",
          DeptDefinition.AUSTIN,
          null,
          null,
          4,
          WorkLocationDefinition.AUSTIN_SITE,
          BusinessAreaDefinition.IT,
          ScopeDefinition.WITHIN_SITE),


    WINCHESTER ("9994",
                "Winchester Site",
                "Winchester Employees",
                FOUNDERS,
                new PersonDefinition[]{PersonDefinition.STEW_FASTER},
                null,
                1,
                WorkLocationDefinition.WINCHESTER_SITE,
                null,
                ScopeDefinition.WITHIN_SITE),

    WINCH_PLANT("0014",
                "Winchester Manufacturing",
                "Team responsible for producing and delivering products to customers based in Winchester, UK.",
                DeptDefinition.WINCHESTER,
                new PersonDefinition[]{PersonDefinition.STEW_FASTER},
                null,
                14,
                WorkLocationDefinition.WINCHESTER_SITE,
                BusinessAreaDefinition.MANUFACTURING,
                ScopeDefinition.WORLD),

    WINCH_OFFICE("0015",
                "Winchester Manufacturing Office",
                "Office team based in Winchester, UK.",
                DeptDefinition.WINCHESTER,
                new PersonDefinition[]{PersonDefinition.STEW_FASTER},
                null,
                0,
                WorkLocationDefinition.WINCHESTER_SITE,
                BusinessAreaDefinition.MANUFACTURING,
                ScopeDefinition.WORLD),

    WINCH_OFFICE_LOCAL("0016",
                 "Winchester Manufacturing Office",
                 "Team responsible for administration of the Winchester Manufacturing Operation based in Winchester, UK.",
                 DeptDefinition.WINCH_OFFICE,
                 null,
                 null,
                 4,
                 WorkLocationDefinition.WINCHESTER_SITE,
                 BusinessAreaDefinition.MANUFACTURING,
                 ScopeDefinition.WORLD),

    MANUFACTURING_GLOBAL("4332",
                 "Global Manufacturing Office",
                 "Team responsible for administration of the Global Manufacturing Operation based in Winchester, UK.",
                 DeptDefinition.BOARD,
                 new PersonDefinition[]{PersonDefinition.STEW_FASTER},
                 null,
                 5,
                 WorkLocationDefinition.WINCHESTER_SITE,
                 BusinessAreaDefinition.MANUFACTURING,
                 ScopeDefinition.WORLD),

    IT_WINCH("0017",
              "Winchester Information Technology Team",
              "Team managing IT resources for Winchester manufacturing.",
              DeptDefinition.WINCHESTER,
              null,
              null,
              5,
              WorkLocationDefinition.WINCHESTER_SITE,
              BusinessAreaDefinition.IT,
              ScopeDefinition.WITHIN_SITE),

    WINCH_DEPOT("0018",
                    "Winchester Depot",
                    "Team managing the warehouse and distribution for the UK.",
                    DeptDefinition.WINCHESTER,
                    null,
                    null,
                    10,
                    WorkLocationDefinition.WINCHESTER_SITE,
                    BusinessAreaDefinition.DISTRIBUTION,
                    ScopeDefinition.WITHIN_COUNTRY),

    KC ("9993",
        "Kansas City Site",
        "Kansas City Employees",
        FOUNDERS,
        null,
        null,
        1,
        WorkLocationDefinition.KANSAS_CITY_SITE,
        null,
        ScopeDefinition.WITHIN_SITE),

    KC_OFFICE("0019",
              "Kansas City Office Team",
              "Team managing administration for Kansas City operation.",
              DeptDefinition.KC,
              null,
              null,
              3,
              WorkLocationDefinition.KANSAS_CITY_SITE,
              BusinessAreaDefinition.DISTRIBUTION,
              ScopeDefinition.WITHIN_SITE),

    KC_DEPOT("0020",
                "Kansas City Depot",
                "Team managing the warehouse and distribution for the USA.",
                DeptDefinition.KC,
                null,
                null,
                7,
                WorkLocationDefinition.KANSAS_CITY_SITE,
                BusinessAreaDefinition.DISTRIBUTION,
                ScopeDefinition.WITHIN_COUNTRY),

    EDMONTON ("9992",
              "Edmonton Site",
              "Edmonton Employees",
              FOUNDERS,
              null,
              null,
              1,
              WorkLocationDefinition.EDMONTON_SITE,
              null,
              ScopeDefinition.WITHIN_SITE),

    EDMONTON_PLANT("0021",
                   "Edmonton Factory",
                   "Team producing and delivering products to customers based in Edmonton, CA.",
                   DeptDefinition.AUSTIN,
                   null,
                   null,
                   17,
                   WorkLocationDefinition.AUSTIN_SITE,
                   BusinessAreaDefinition.MANUFACTURING,
                   ScopeDefinition.WITHIN_REGION),

    EDMONTON_OFFICE("0022",
                "Edmonton Office Team",
                "Team managing administration for Edmonton operation.",
                DeptDefinition.EDMONTON,
                null,
                null,
                4,
                WorkLocationDefinition.EDMONTON_SITE,
                BusinessAreaDefinition.MANUFACTURING,
                ScopeDefinition.WITHIN_SITE),

    IT_EDMONTON("0023",
             "Edmonton Information Technology Team",
             "Team managing IT resources for Edmonton operation.",
             DeptDefinition.EDMONTON,
             null,
             null,
             3,
             WorkLocationDefinition.EDMONTON_SITE,
             BusinessAreaDefinition.IT,
             ScopeDefinition.WITHIN_SITE),

    EDMONTON_DEPOT("0024",
             "Edmonton Depot",
             "Team managing the warehouse and distribution for Canada.",
             DeptDefinition.EDMONTON,
             null,
             null,
             5,
             WorkLocationDefinition.EDMONTON_SITE,
             BusinessAreaDefinition.DISTRIBUTION,
             ScopeDefinition.WITHIN_COUNTRY),

    ;

    private final String                 teamId;
    private final String                 displayName;
    private final String                 description;
    private final DeptDefinition         superTeam;
    private final PersonDefinition[]     leaders;
    private final PersonDefinition[]     members;
    private final int                    additionalMembers;
    private final WorkLocationDefinition workLocation;
    private final BusinessAreaDefinition businessArea;
    private final ScopeDefinition        businessAreaScope;

    /**
     * The constructor creates an instance of the enum
     *
     * @param teamId   unique id for the enum
     * @param displayName   name for the enum
     * @param description   description of the use of this value
     * @param superTeam team that this team reports to - can be null if the entry is an organization
     * @param leaders person to link into the leadership role
     * @param members list of people who are members of the team or organization
     * @param additionalMembers anonymous people to make up staff complement
     * @param workLocation which work location to associate the anonymous people with
     * @param businessArea which business area is this team supporting
     * @param businessAreaScope what is the scope of this team's responsibility?
     */
    DeptDefinition(String                 teamId,
                   String                 displayName,
                   String                 description,
                   DeptDefinition         superTeam,
                   PersonDefinition[]     leaders,
                   PersonDefinition[]     members,
                   int                    additionalMembers,
                   WorkLocationDefinition workLocation,
                   BusinessAreaDefinition businessArea,
                   ScopeDefinition        businessAreaScope)
    {
        this.teamId = teamId;
        this.displayName = displayName;
        this.description = description;
        this.superTeam = superTeam;
        this.leaders = leaders;
        this.members = members;
        this.additionalMembers = additionalMembers;
        this.workLocation = workLocation;
        this.businessArea = businessArea;
        this.businessAreaScope = businessAreaScope;
    }


    /**
     * Return the unique identifier.
     *
     * @return string identifier
     */
    public String getQualifiedName()
    {
        return "Department:" + teamId;
    }


    /**
     * Return the department code or teamId
     *
     * @return string code
     */
    public String getTeamId()
    {
        return teamId;
    }


    /**
     * Return the display name
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return this department's description.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return details of the department that this department reports to.
     *
     * @return department
     */
    public DeptDefinition getSuperTeam()
    {
        return superTeam;
    }


    /**
     * Return the leaders for the department.
     *
     * @return list of people
     */
    public List<PersonDefinition> getLeaders()
    {
        if (leaders != null)
        {
            return Arrays.asList(leaders);
        }
        else
        {
            return null;
        }
    }


    /**
     * Return the department members.
     *
     * @return list of people
     */
    public List<PersonDefinition> getMembers()
    {
        if (members != null)
        {
            return Arrays.asList(members);
        }
        else
        {
            return null;
        }
    }


    /**
     * Return the number of additional anonymous members to generate.
     *
     * @return int
     */
    public int getAdditionalMembers()
    {
        return additionalMembers;
    }


    /**
     * Return the work location.
     *
     * @return work location
     */
    public WorkLocationDefinition getWorkLocation()
    {
        return workLocation;
    }


    /**
     * Return the business area.
     *
     * @return business area
     */
    public BusinessAreaDefinition getBusinessArea()
    {
        return businessArea;
    }


    /**
     * Return business area scope.
     *
     * @return scope
     */
    public ScopeDefinition getBusinessAreaScope()
    {
        return businessAreaScope;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "DeptDefinition{" + displayName + '}';
    }
}
