/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.organization;


/**
 * The PersonDefinition is used to feed the definition of the Actors that work for/with Coco Pharmaceuticals.  It is used to bootstrap
 * multiple Coco Pharmaceuticals demos.
 */
public enum PersonDefinition
{
    /**
     * Dr Zachary P Now
     */
    ZACH_NOW("Zach Now",
             "He/him/his",
             OrganizationDefinition.COCO,
             "Dr",
             "Zachary",
             "Z P",
             "Now",
             "Dr Zachary P Now",
             "133777",
             CountryCodeDefinition.USA,
             EmployeeTypeDefinition.EXECUTIVE,
             "Founder and Head of NY Site",
             JobLevelDefinition.LEVEL_9,
             "zachnow",
             "cn=zachnow,ou=people,ou=users,o=cocoPharma",
             "ZachNow@Coco-Pharmaceuticals.org",
             WorkLocationDefinition.NEW_YORK_SITE),

    /**
     * Dr Steven James Starter
     */
    STEVE_STARTER("Steve Starter",
                  "He/him/his",
                  OrganizationDefinition.COCO,
                  "Dr",
                  "Steven James",
                  "S J",
                  "Starter",
                  "Dr Steven James Starter",
                  "439222",
                  CountryCodeDefinition.NL,
                  EmployeeTypeDefinition.EXECUTIVE,
                  "Founder and Head of Amsterdam Site",
                  JobLevelDefinition.LEVEL_9,
                  "stevestarter",
                  "cn=stevestarter,ou=people,ou=users,o=cocoPharma",
                  "SteveStarter@Coco-Pharmaceuticals.org",
                  WorkLocationDefinition.AMSTERDAM_SITE),

    /**
     * Dr Terri Daring
     */
    TERRI_DARING("Terri Daring",
                 "She/her/hers",
                 OrganizationDefinition.COCO,
                 "Dr",
                 "Teresa",
                 "T",
                 "Daring",
                 "Dr Terri Daring",
                 "371803",
                 CountryCodeDefinition.UK,
                 EmployeeTypeDefinition.EXECUTIVE,
                 "Founder and Head of London Site",
                 JobLevelDefinition.LEVEL_9,
                 "terridaring",
                 "cn=terridaring,ou=people,ou=users,o=cocoPharma",
                 "TerriDaring@Coco-Pharmaceuticals.org",
                 WorkLocationDefinition.LONDON_SITE),

    /**
     * Mr Reginald Sidney Mint
     */
    REGGIE_MINT("Reggie Mint",
                "He/him/his",
                OrganizationDefinition.COCO,
                "Mr",
                "Reginald Sidney",
                "R S",
                "Mint",
                "Mr Reginald Sidney Mint",
                "188888",
                CountryCodeDefinition.UK,
                EmployeeTypeDefinition.EXECUTIVE,
                "Chief Finance Officer",
                JobLevelDefinition.LEVEL_8,
                "reggiemint",
                "cn=reggiemint,ou=people,ou=users,o=cocoPharma",
                "ReggieMint@Coco-Pharmaceuticals.org",
                WorkLocationDefinition.LONDON_SITE),

    /**
     * Dr Thomas Tally
     */
    TOM_TALLY("Tom Tally",
              "She/her/hers",
              OrganizationDefinition.COCO,
              "Dr",
              "Thomas",
              "T",
              "Tally",
              "Dr Thomas Tally",
              "896419",
              CountryCodeDefinition.UK,
              EmployeeTypeDefinition.FULL_TIME_PERMANENT,
              "Accounts Manager, Finance HQ",
              JobLevelDefinition.LEVEL_6,
              "tomtally",
              "cn=tomtally,ou=people,ou=users,o=cocoPharma",
              "TomTally@Coco-Pharmaceuticals.org",
              WorkLocationDefinition.LONDON_SITE),

    /**
     * Mrs Sally Counter
     */
    SALLY_COUNTER("Sally Counter",
                  "She/her/hers",
                  OrganizationDefinition.COCO,
                  "Mrs",
                  "Sally",
                  "S",
                  "Counter",
                  "Mrs Sally Counter",
                  "457911",
                  CountryCodeDefinition.UK,
                  EmployeeTypeDefinition.PART_TIME_PERMANENT,
                  "Payments Clerk",
                  JobLevelDefinition.LEVEL_3,
                  "sallycounter",
                  "cn=sallycounter,ou=people,ou=users,o=cocoPharma",
                  "SallyCounter@Coco-Pharmaceuticals.org",
                  WorkLocationDefinition.LONDON_SITE),

    /**
     * Mr Harold Percival Hopeful
     */
    HARRY_HOPEFUL("Harry Hopeful",
                  "He/him/his",
                  OrganizationDefinition.COCO,
                  "Mr",
                  "Harold Percival",
                  "H P",
                  "Hopeful",
                  "Mr Harold Percival Hopeful",
                  "144994",
                  CountryCodeDefinition.USA,
                  EmployeeTypeDefinition.FULL_TIME_PERMANENT,
                  "Sales Leader",
                  JobLevelDefinition.LEVEL_6,
                  "harryhopeful",
                  "cn=harryhopeful,ou=people,ou=users,o=cocoPharma",
                  "HarryHopeful@Coco-Pharmaceuticals.org",
                  WorkLocationDefinition.NEW_YORK_SITE),

    /**
     * Mr Julian Keeper
     */
    JULES_KEEPER("Jules Keeper",
                 "He/him/his",
                 OrganizationDefinition.COCO,
                 "Mr",
                 "Julian",
                 "J",
                 "Keeper",
                 "Mr Julian Keeper",
                 "296776",
                 CountryCodeDefinition.UK,
                 EmployeeTypeDefinition.EXECUTIVE,
                 "Chief Data Officer",
                 JobLevelDefinition.LEVEL_8,
                 "juleskeeper",
                 "cn=juleskeeper,ou=people,ou=users,o=cocoPharma",
                 "JulesKeeper@Coco-Pharmaceuticals.org",
                 WorkLocationDefinition.LONDON_SITE),

    /**
     * Mrs Faith Charity Jean Broker
     */
    FAITH_BROKER("Faith Broker",
                 "She/her/hers",
                 OrganizationDefinition.COCO,
                 "Mrs",
                 "Faith Charity Jean",
                 "F C J",
                 "Broker",
                 "Mrs Faith Charity Jean Broker",
                 "139870",
                 CountryCodeDefinition.NL,
                 EmployeeTypeDefinition.EXECUTIVE,
                 "HR Director and Chief Privacy Officer",
                 JobLevelDefinition.LEVEL_8,
                 "faithbroker",
                 "cn=faithbroker,ou=people,ou=users,o=cocoPharma",
                 "FaithBroker@Coco-Pharmaceuticals.org",
                 WorkLocationDefinition.AMSTERDAM_SITE),

    /**
     * Mr Ivor Padlock
     */
    IVOR_PADLOCK("Ivor Padlock",
                 "He/him/his",
                 OrganizationDefinition.COCO,
                 "Mr",
                 "Ivor",
                 "I",
                 "Padlock",
                 "Mr Ivor Padlock",
                 "499888",
                 CountryCodeDefinition.USA,
                 EmployeeTypeDefinition.EXECUTIVE,
                 "Chief Security Officer",
                 JobLevelDefinition.LEVEL_8,
                 "ivorpadlock",
                 "cn=ivorpadlock,ou=people,ou=users,o=cocoPharma",
                 "IvorPadlock@Coco-Pharmaceuticals.org",
                 WorkLocationDefinition.NEW_YORK_SITE),

    /**
     * Mr Gary Geeke
     */
    GARY_GEEKE("Gary Geeke",
               "He/him/his",
               OrganizationDefinition.COCO,
               "Mr",
               "Gary",
               "G",
               "Geeke",
               "Mr Gary Geeke",
               "199995",
               CountryCodeDefinition.NL,
               EmployeeTypeDefinition.FULL_TIME_PERMANENT,
               "IT Infrastructure Lead",
               JobLevelDefinition.LEVEL_6,
               "garygeeke",
               "cn=garygeeke,ou=people,ou=users,o=cocoPharma",
               "GaryGeeke@Coco-Pharmaceuticals.org",
               WorkLocationDefinition.AMSTERDAM_SITE),


    /**
     * Dr Erin Overview
     */
    ERIN_OVERVIEW("Erin Overview",
                  "She/her/hers",
                  OrganizationDefinition.COCO,
                  "Dr",
                  "Erin",
                  "E",
                  "Overview",
                  "Dr Erin Overview",
                  "324713",
                  CountryCodeDefinition.UK,
                  EmployeeTypeDefinition.FULL_TIME_PERMANENT,
                  "Information Architect",
                  JobLevelDefinition.LEVEL_5,
                  "erinoverview",
                  "cn=erinoverview,ou=people,ou=users,o=cocoPharma",
                  "ErinOverview@Coco-Pharmaceuticals.org",
                  WorkLocationDefinition.LONDON_SITE),

    /**
     * Mr Peter Profile
     */
    PETER_PROFILE("Peter Profile",
                  "He/him/his",
                  OrganizationDefinition.COCO,
                  "Mr",
                  "Peter",
                  "P",
                  "Profile",
                  "Mr Peter Profile",
                  "986419",
                  CountryCodeDefinition.UK,
                  EmployeeTypeDefinition.FULL_TIME_PERMANENT,
                  "Information Analyst",
                  JobLevelDefinition.LEVEL_3,
                  "peterprofile",
                  "cn=peterprofile,ou=people,ou=users,o=cocoPharma",
                  "PeterProfile@Coco-Pharmaceuticals.org",
                  WorkLocationDefinition.LONDON_SITE),

    /**
     * Ms Polly Tasker
     */
    POLLY_TASKER("Polly Tasker",
                 "She/her/hers",
                 OrganizationDefinition.COCO,
                 "Ms",
                 "Polly",
                 "P",
                 "Tasker",
                 "Ms Polly Tasker",
                 "338575",
                 CountryCodeDefinition.NL,
                 EmployeeTypeDefinition.FULL_TIME_PERMANENT,
                 "IT Project Leader",
                 JobLevelDefinition.LEVEL_7,
                 "pollytasker",
                 "cn=pollytasker,ou=people,ou=users,o=cocoPharma",
                 "PollyTasker@Coco-Pharmaceuticals.org",
                 WorkLocationDefinition.AMSTERDAM_SITE),

    /**
     * Mr Bob Nitter
     */
    BOB_NITTER("Bob Nitter",
               "He/him/his",
               OrganizationDefinition.COCO,
               "Mr",
               "Robert",
               "R",
               "Nitter",
               "Mr Bob Nitter",
               "458109",
               CountryCodeDefinition.NL,
               EmployeeTypeDefinition.PART_TIME_PERMANENT,
               "Integration Architect/Developer (API developer)",
               JobLevelDefinition.LEVEL_3,
               "bobnitter",
               "cn=bobnitter,ou=people,ou=users,o=cocoPharma",
               "BobNitter@Coco-Pharmaceuticals.org",
               WorkLocationDefinition.AMSTERDAM_SITE),

    /**
     * Mr Lemmie Stage
     */
    LEMMIE_STAGE("Lemmie Stage",
                 "He/him/his",
                 OrganizationDefinition.COCO,
                 "Mr",
                 "Lemmie",
                 "L",
                 "Stage",
                 "Mr Lemmie Stage",
                 "818928",
                 CountryCodeDefinition.NL,
                 EmployeeTypeDefinition.FULL_TIME_PERMANENT,
                 "DataStage specialist",
                 JobLevelDefinition.LEVEL_2,
                 "lemmiestage",
                 "cn=lemmiestage,ou=people,ou=users,o=cocoPharma",
                 "LemmieStage@Coco-Pharmaceuticals.org",
                 WorkLocationDefinition.AMSTERDAM_SITE),

    /**
     * Dr Stew Faster
     */
    STEW_FASTER("Stew Faster",
                "He/him/his",
                OrganizationDefinition.COCO,
                "Dr",
                "Stewart",
                "S",
                "Faster",
                "Dr Stew Faster",
                "483942",
                CountryCodeDefinition.UK,
                EmployeeTypeDefinition.EXECUTIVE,
                "Head of Manufacturing",
                JobLevelDefinition.LEVEL_8,
                "stewfaster",
                "cn=stewfaster,ou=people,ou=users,o=cocoPharma",
                "StewFaster@Coco-Pharmaceuticals.org",
                WorkLocationDefinition.LONDON_SITE),

    /**
     * Dr Tessa Tube
     */
    TESSA_TUBE("Tessa Tube",
               "She/her/hers",
               OrganizationDefinition.COCO,
               "Dr",
               "Tessa",
               "T",
               "Tube",
               "Dr Tessa Tube",
               "302145",
               CountryCodeDefinition.USA,
               EmployeeTypeDefinition.FULL_TIME_PERMANENT,
               "Lead Researcher and head of Salk Research Lab",
               JobLevelDefinition.LEVEL_7,
               "tessatube",
               "cn=tessatube,ou=people,ou=users,o=cocoPharma",
               "TessaTube@Coco-Pharmaceuticals.org",
               WorkLocationDefinition.NEW_YORK_SITE),

    /**
     * Dr Callie R Quartile
     */
    CALLIE_QUARTILE("Callie Quartile",
                    "She/her/hers",
                    OrganizationDefinition.COCO,
                    "Dr",
                    "Callie",
                    "C R",
                    "quartile",
                    "Dr Callie R Quartile",
                    "328080",
                    CountryCodeDefinition.USA,
                    EmployeeTypeDefinition.FULL_TIME_PERMANENT,
                    "Data Scientist",
                    JobLevelDefinition.LEVEL_5,
                    "calliequartile",
                    "cn=calliequartile,ou=people,ou=users,o=cocoPharma",
                    "CallieQuartile@Coco-Pharmaceuticals.org",
                    WorkLocationDefinition.NEW_YORK_SITE),

    /**
     * Mrs Tanya Tidie
     */
    TANYA_TIDIE("Tanya Tidie",
                "She/her/hers",
                OrganizationDefinition.COCO,
                "Mrs",
                "Tanya",
                "T",
                "Tidie",
                "Mrs Tanya Tidie",
                "209482",
                CountryCodeDefinition.USA,
                EmployeeTypeDefinition.FULL_TIME_PERMANENT,
                "Clinical Records Clerk",
                JobLevelDefinition.LEVEL_2,
                "tanyatidie",
                "cn=tanyatidie,ou=people,ou=users,o=cocoPharma",
                "TanyaTidie@Coco-Pharmaceuticals.org",
                WorkLocationDefinition.NEW_YORK_SITE),

    /**
     * Mr Grant Harold Able
     */
    GRANT_ABLE("Grant Able",
               "He/him/his",
               OrganizationDefinition.COCO,
               "Mr",
               "Grant Harold",
               "G H",
               "Able",
               "Mr Grant Harold Able",
               null,
               CountryCodeDefinition.USA,
               EmployeeTypeDefinition.PARTNER,
               "Consultant (Hampton Hospital)",
               null,
               "grantable",
               "cn=grantable,ou=people,ou=users,o=cocoPharma",
               "Grant@hh-care.org",
               WorkLocationDefinition.HAMPTON_HOSPITAL),

    /**
     * Mrs Julie Stitched
     */
    JULIE_STITCHED("Julie Stitched",
                   "She/her/hers",
                   OrganizationDefinition.COCO,
                   "Mrs",
                   "Julie",
                   "J",
                   "Stitched",
                   "Mrs Julie Stitched",
                   null,
                   CountryCodeDefinition.USA,
                   EmployeeTypeDefinition.PARTNER,
                   "Surgeon (Hampton Hospital)",
                   null,
                   "juliestitched",
                   "cn=juliestitched,ou=people,ou=users,o=cocoPharma",
                   "Julie-Stitched@hh-care.org",
                   WorkLocationDefinition.HAMPTON_HOSPITAL),

    /**
     * Mr Robbert Records
     */
    ROBBIE_RECORDS("Robbie Records",
                   "He/him/his",
                   OrganizationDefinition.COCO,
                   "Mr",
                   "Robert",
                   "J",
                   "Records",
                   "Mr Robbert Records",
                   null,
                   CountryCodeDefinition.USA,
                   EmployeeTypeDefinition.PARTNER,
                   "Patient Data Manager (Oak Dene Hospital)",
                   null,
                   "robbierecords",
                   "cn=robbierecords,ou=people,ou=users,o=cocoPharma",
                   "RobbieRec@oak-dene.org",
                   WorkLocationDefinition.OAK_DENE_HOSPITAL),

    /**
     * Miss Angela Cummings
     */
    ANGELA_CUMMINGS("Angela Cummings",
                    "She/her/hers",
                    OrganizationDefinition.COCO,
                    "Miss",
                    "Angela",
                    "J",
                    "Cummings",
                    "Miss Angela Cummings",
                    null,
                    CountryCodeDefinition.USA,
                    EmployeeTypeDefinition.PARTNER,
                    "Nurse (Hampton Hospital)",
                    null,
                    "angelacummings",
                    "cn=angelacummings,ou=people,ou=users,o=cocoPharma",
                    "angela@hh-care.org",
                    WorkLocationDefinition.HAMPTON_HOSPITAL),

    /**
     * Mr Robbert Records
     */
    NELLIE_DUNN("Nellie Dunn",
                   "She/her/hers",
                   OrganizationDefinition.COCO,
                   "Miss",
                   "Eleanor",
                   "F",
                   "Dunn",
                   "Miss Eleanor Dunn",
                   null,
                   CountryCodeDefinition.USA,
                   EmployeeTypeDefinition.PARTNER,
                   "Data Office (Old Market Hospital)",
                   null,
                   "nelliedunn",
                   "cn=nellie,ou=people,ou=users,o=cocoPharma",
                   "nellie_dunn@OldMarketHospital.com",
                   WorkLocationDefinition.OLD_MARKET_HOSPITAL),

    /**
     * Mr Sidney Seeker
     */
    SIDNEY_SEEKER("Sidney Seeker",
                  "He/him/his",
                  OrganizationDefinition.SEC_INC,
                  "Mr",
                  "Sidney",
                  "S",
                  "Seeker",
                  "Mr Sidney Seeker",
                  null,
                  CountryCodeDefinition.UK,
                  EmployeeTypeDefinition.CONTRACTOR,
                  "Fraud Investigator",
                  null,
                  "sidneyseeker",
                  "cn=sidneyseeker,ou=people,ou=users,o=cocoPharma",
                  "SJKSeeker@SecIncConSvcs.gmail",
                  WorkLocationDefinition.LONDON_SITE),

    /**
     * Mr Desmond Signa
     */
    DES_SIGNA("Des Signa",
              "They/them/their",
              OrganizationDefinition.XDC,
              "Mr",
              "Desmond",
              "D",
              "Signa",
              "Mr Desmond Signa",
              null,
              CountryCodeDefinition.NL,
              EmployeeTypeDefinition.CONTRACTOR,
              "Mobile Developer",
              null,
              "dessigna",
              "cn=dessigna,ou=people,ou=users,o=cocoPharma",
              "des_signa@gb.xdc.com",
              WorkLocationDefinition.AMSTERDAM_SITE),

    /**
     * Mrs Nancy Noah
     */
    NANCY_NOAH("Nancy Noah",
               "She/her/hers",
               OrganizationDefinition.XDC,
               "Mrs",
               "Nancy",
               "N",
               "Noah",
               "Mrs Nancy Noah",
               null,
               CountryCodeDefinition.NL,
               EmployeeTypeDefinition.CONTRACTOR,
               "Cloud Architect",
               null,
               "nancynoah",
               "cn=nancynoah,ou=people,ou=users,o=cocoPharma",
               "nnoah@xdc.com",
               WorkLocationDefinition.AMSTERDAM_SITE),

    ;

    private final String                 displayName;
    private final String                 pronouns;
    private final OrganizationDefinition organization;
    private final String                 title;
    private final String                 givenNames;
    private final String                 initials;
    private final String                 surname;
    private final String                 fullName;
    private final String                 employeeNumber;
    private final CountryCodeDefinition  countryCode;
    private final EmployeeTypeDefinition employeeType;
    private final String                 jobTitle;
    private final JobLevelDefinition     jobLevel;
    private final String                 userId;
    private final String                 distinguishedName;
    private final String                 email;
    private final WorkLocationDefinition workLocation;


    /**
     * Constructor that sets up the enum.
     *
     * @param displayName name of the person
     * @param organization which organization do they belong to
     * @param title courtesy title
     * @param givenNames given names
     * @param initials initials of person
     * @param surname surname
     * @param fullName full legal name
     * @param employeeNumber unique number for the employee
     * @param countryCode code that indicates which on
     * @param employeeType type of employment contract
     * @param jobTitle person's job title
     * @param jobLevel persona's job level
     * @param userId primary user id
     * @param distinguishedName primary user id
     * @param email primary work email address
     * @param workLocation primary work location
     */
    PersonDefinition(String                 displayName,
                     String                 pronouns,
                     OrganizationDefinition organization,
                     String                 title,
                     String                 givenNames,
                     String                 initials,
                     String                 surname,
                     String                 fullName,
                     String                 employeeNumber,
                     CountryCodeDefinition  countryCode,
                     EmployeeTypeDefinition employeeType,
                     String                 jobTitle,
                     JobLevelDefinition     jobLevel,
                     String                 userId,
                     String                 distinguishedName,
                     String                 email,
                     WorkLocationDefinition workLocation)
    {
        this.displayName = displayName;
        this.pronouns = pronouns;
        this.organization = organization;
        this.jobLevel = jobLevel;
        this.title = title;
        this.givenNames = givenNames;
        this.initials = initials;
        this.surname = surname;
        this.fullName = fullName;
        this.employeeNumber = employeeNumber;
        this.countryCode = countryCode;
        this.employeeType = employeeType;
        this.jobTitle = jobTitle;
        this.userId = userId;
        this.distinguishedName = distinguishedName;
        this.email = email;
        this.workLocation = workLocation;
    }


    public String getQualifiedName()
    {
        if (employeeNumber != null)
        {
            return "Person:" + countryCode.getPreferredValue() + ":" + employeeNumber;
        }
        else if (countryCode != null)
        {
            return "Person:" + countryCode.getPreferredValue() + ":" + fullName;
        }
        else
        {
            return "Person:EXTERNAL:" + fullName;
        }
    }


    public String getDisplayName()
    {
        return displayName;
    }


    public String getPronouns()
    {
        return pronouns;
    }


    public OrganizationDefinition getOrganization()
    {
        return organization;
    }

    public String getTitle()
    {
        return title;
    }


    public String getGivenNames()
    {
        return givenNames;
    }


    public String getInitials()
    {
        return initials;
    }


    public String getSurname()
    {
        return surname;
    }


    public String getFullName()
    {
        return fullName;
    }


    public String getEmployeeNumber()
    {
        return employeeNumber;
    }


    public CountryCodeDefinition getCountryCode()
    {
        return countryCode;
    }


    public EmployeeTypeDefinition getEmployeeType()
    {
        return employeeType;
    }


    public String getJobTitle()
    {
        return jobTitle;
    }


    public JobLevelDefinition getJobLevel()
    {
        return jobLevel;
    }


    public String getUserId()
    {
        return userId;
    }


    public String getDistinguishedName()
    {
        return distinguishedName;
    }


    public String getEmail()
    {
        return email;
    }


    public WorkLocationDefinition getWorkLocation()
    {
        return workLocation;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "Person{" + displayName + '}';
    }
}
