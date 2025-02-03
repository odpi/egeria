/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.businesssystems;


import org.odpi.openmetadata.samples.archiveutilities.organization.PersonDefinition;
import org.odpi.openmetadata.samples.archiveutilities.sustainability.FacilityDefinition;

import java.util.Arrays;
import java.util.List;

/**
 * The SystemDefinition is used to feed the definition of the organization's systems for Coco Pharmaceuticals scenarios.
 */
public enum SystemDefinition
{
    /**
     * Ordering system for Coco Pharmaceuticals
     */
    COCO_CRM("a2e43afa-386a-4acd-ba1d-7773b368c34a",
             "globalCRM",
             "Global customer ordering system.",
             "globalcrmnpa",
             SystemTypeDefinition.CLOUD_SAAS_SERVICE,
             "",
             PersonDefinition.ZACH_NOW,
             FacilityDefinition.CLOUD_PROVIDER_SALES,
             "https://order.coco.com",
             null,
             new String[]{"business-systems", "sustainability"},
             100,
             null),


    COCO_PRODUCT_PLANNING("dd424526-24e9-4a10-a575-0700dfb8743a",
                          "cocoProducts",
                          "Product Management System used by the board to plan product offerings.",
                          "cocoproductnpa",
                          SystemTypeDefinition.HOME_GROWN_APP_SERVER,
                          "23.4.1",
                          PersonDefinition.TERRI_DARING,
                          FacilityDefinition.AMSTERDAM_DC,
                          "https://cocoproducts.coco.com",
                          null,
                          new String[]{"business-systems", "sustainability"},
                          100,
                          null),

    /**
     * procurement01 - Purchasing system for Coco Pharmaceuticals.
     */
    COCO_PROCUREMENT_AMS("cbe8c0bb-0e27-4d2a-825f-f017ee939028",
                         "procurement01",
                         "Purchasing system for Coco Pharmaceuticals.",
                         "procurement01npa",
                         SystemTypeDefinition.COTS_SERVER,
                         "V2.6",
                         PersonDefinition.REGGIE_MINT,
                         FacilityDefinition.AMSTERDAM_DC,
                         "https://ams45.coco.com",
                         new HostDefinition[]{HostDefinition.COCO_PROCUREMENT_AMS_HOST},
                         new String[]{"business-systems", "sustainability"},
                         0,
                         null),

    /**
     * procurement02 - Local purchasing system for New York Site.
     */
    COCO_PROCUREMENT_NY("0ba9a684-9665-4026-990b-ac90a3db6134",
                        "procurement02",
                        "Local purchasing system for New York Site.",
                        "procurement02npa",
                        SystemTypeDefinition.COTS_SERVER,
                        "V2.6",
                        PersonDefinition.REGGIE_MINT,
                        FacilityDefinition.NEW_YORK_DC,
                        "https://ny327-3.coco.com",
                        new HostDefinition[]{HostDefinition.COCO_PROCUREMENT_NY_HOST},
                        new String[]{"business-systems", "sustainability"},
                        0,
                        null),

    /**
     * procurement03 - Local purchasing system for Winchester Factory Site.
     */
    COCO_PROCUREMENT_WINCH("db144477-e6cb-4fd1-9521-4a577de9af04",
                           "procurement03",
                           "Local purchasing system for Winchester Factory Site.",
                           "procurement03npa",
                           SystemTypeDefinition.COTS_SERVER,
                           "V1.0",
                           PersonDefinition.REGGIE_MINT,
                           FacilityDefinition.WINCHESTER_DC,
                           "https://winch12.coco.com",
                           new HostDefinition[]{HostDefinition.COCO_PROCUREMENT_WINCH_HOST},
                           new String[]{"business-systems", "sustainability"},
                           34,
                           null),

    /**
     * procurement04 - Local purchasing system for Austin Factory Site.
     */
    COCO_PROCUREMENT_AUS("23fb5e33-11c9-4eb6-91d6-f2e873f8da68",
                         "procurement04",
                         "Local purchasing system for Austin Factory Site.",
                         "procurement04npa",
                         SystemTypeDefinition.COTS_SERVER,
                         "V7.2",
                         PersonDefinition.REGGIE_MINT,
                         FacilityDefinition.AUSTIN_DC,
                         "https://capital.aus.coco.com",
                         new HostDefinition[]{HostDefinition.COCO_PROCUREMENT_AUS_HOST},
                         new String[]{"business-systems", "sustainability"},
                         1001,
                         null),

    /**
     * procurement05 - Local purchasing system for Edmonton Factory Site.
     */
    COCO_PROCUREMENT_ED("a5abdf72-0e5f-4366-8072-fac3bbb41ea6",
                        "procurement05",
                        "Local purchasing system for Edmonton Factory Site.",
                        "procurement05npa",
                        SystemTypeDefinition.COTS_SERVER,
                        "V7.3",
                        PersonDefinition.REGGIE_MINT,
                        FacilityDefinition.EDMONTON_OFFICE,
                        "https://ed01.coco.com",
                        new HostDefinition[]{HostDefinition.COCO_PROCUREMENT_ED_HOST},
                        new String[]{"business-systems"},
                        1501,
                        null),

    /**
     * coco-inventory - Inventory for raw materials and products produced across all Coco Pharmaceuticals sites.
     */
    COCO_INVENTORY("467dd9a0-89d6-4796-a058-c079bf54b6c7",
                   "coco-inventory",
                   "Inventory for raw materials and products produced across all Coco Pharmaceuticals sites (except Austin).",
                   "cocoinventorynpa",
                   SystemTypeDefinition.HOME_GROWN_APP_SERVER,
                   "V5.2",
                   PersonDefinition.ZACH_NOW,
                   FacilityDefinition.AMSTERDAM_DC,
                   "https://ams23.coco.com",
                   new HostDefinition[]{HostDefinition.COCO_INVENTORY_HOST},
                   new String[]{"business-systems"},
                   0,
                   new SolutionComponent[]{SolutionComponent.GOODS_INVENTORY}),

    /**
     * aus-inventory - Inventory for raw materials and products produced across all Coco Pharmaceuticals sites (except Austin).
     */
    AUS_INVENTORY("9e29ee1f-01d3-482c-9d1b-8b3f36ce62d2",
                  "aus-inventory",
                  "Inventory for raw materials and products produced across all Coco Pharmaceuticals sites .",
                  "ausinventorynpa",
                  SystemTypeDefinition.HOME_GROWN_APP_SERVER,
                  "V23.2",
                  PersonDefinition.STEW_FASTER,
                  FacilityDefinition.AUSTIN_DC,
                  "https://ams23.coco.com",
                  new HostDefinition[]{HostDefinition.AUS_MANUFACTURING_CONTROL_HOST},
                  new String[]{"business-systems"},
                  0,
                  new SolutionComponent[]{SolutionComponent.GOODS_INVENTORY}),

    /**
     * coco-hrim - Human Resources Information Manager (HRIM) provides the central management application for employee management including hiring, skills management, recognition and all reasons for termination of employment.
     */
    COCO_HRIM("88c42780-6fb1-49e2-a584-8af90cdc7728",
              "coco-hrim",
              "Human Resources Information Manager (HRIM) provides the central management application for employee management including hiring, skills management, recognition and all reasons for termination of employment.",
              "cocohrinnpa",
              SystemTypeDefinition.COTS_SERVER,
              "V5.2",
              PersonDefinition.FAITH_BROKER,
              FacilityDefinition.AMSTERDAM_DC,
              "https://ams02.coco.com",
              new HostDefinition[]{HostDefinition.COCO_HRIM_HOST},
              new String[]{"business-systems", "sustainability"},
              0,
              null),

    /**
     * cocopages - Employee directory with business partners.
     */
    COCO_PAGES("a47906f0-67f0-46ed-890d-85cb049ffa60",
               "cocopages",
               "Employee directory with business partners.",
               "cocopagesnpa",
               SystemTypeDefinition.HOME_GROWN_APP_SERVER,
               "V5.2",
               PersonDefinition.FAITH_BROKER,
               FacilityDefinition.AMSTERDAM_DC,
               "https://ams06.coco.com",
               new HostDefinition[]{HostDefinition.COCO_PAGES_HOST},
               new String[]{"business-systems", "sustainability"},
               0,
               null),

    /**
     * sec-admin - Security administration for all access grants to Coco Pharmaceutical systems.
     */
    SEC_ADMIN("f49d1515-0f69-4a40-a674-7cc1cacca182",
              "sec-admin",
              "Security administration for all access grants to Coco Pharmaceutical systems.",
              "secadminnpa",
              SystemTypeDefinition.HOME_GROWN_APP_SERVER,
              "V5.2",
              PersonDefinition.IVOR_PADLOCK,
              FacilityDefinition.AMSTERDAM_DC,
              "https://ams01.coco.com",
              new HostDefinition[]{HostDefinition.SEC_ADMIN_HOST},
              new String[]{"business-systems", "sustainability"},
              0,
              null),

    /**
     * coco-expenses - Employee expense declaration for Coco Pharmaceuticals.
     */
    EMPLOYEE_EXPENSES("ad8eab54-c2f7-494d-82d0-bf5985e5a58d",
                      "coco-expenses",
                      "Employee expense declaration for Coco Pharmaceuticals.",
                      null,
                      SystemTypeDefinition.CLOUD_SAAS_SERVICE,
                      "V5.2",
                      PersonDefinition.REGGIE_MINT,
                      FacilityDefinition.CLOUD_PROVIDER_TRAVEL,
                      "https://bethere.com/expenses",
                      null,
                      new String[]{"business-systems"},
                      0,
                      new SolutionComponent[]{SolutionComponent.EMPLOYEE_EXPENSE_TOOL}),

    /**
     * UK payroll - Payroll and UK tax calculations.
     */
    UK_PAYROLL("12c9d1a4-2a5d-4e7c-a640-97f206270425",
               "UK payroll",
               "Payroll and UK tax calculations.",
               "ukpayrollnpa",
               SystemTypeDefinition.COTS_SERVER,
               "V1.0",
               PersonDefinition.FAITH_BROKER,
               FacilityDefinition.LONDON_DC,
               "https://lon02.coco.com",
               new HostDefinition[]{HostDefinition.UK_PAYROLL_HOST},
               new String[]{"business-systems"},
               0,
               null),

    /**
     * Netherlands payroll - Payroll and Dutch tax calculations.
     */
    NL_PAYROLL("ae0c7768-c01e-4bd1-8a47-fbacfccf2f63",
               "Netherlands payroll",
               "Payroll and Dutch tax calculations.",
               "nlpayrollnpa",
               SystemTypeDefinition.COTS_SERVER,
               "V5.4",
               PersonDefinition.FAITH_BROKER,
               FacilityDefinition.AMSTERDAM_DC,
               "https://ams56.coco.com",
               new HostDefinition[]{HostDefinition.NL_PAYROLL_HOST},
               new String[]{"business-systems"},
               0,
               null),

    /**
     * Canadian payroll - Payroll and Canadian tax calculations.
     */
    CA_PAYROLL("35bef6c5-25d7-47b1-91db-f9bce0238612",
               "Canadian payroll",
               "Payroll and Canadian tax calculations.",
               "nlpayrollnpa",
               SystemTypeDefinition.COTS_SERVER,
               "V5.4",
               PersonDefinition.REGGIE_MINT,
               FacilityDefinition.EDMONTON_OFFICE,
               "https://ed05.coco.com",
               new HostDefinition[]{HostDefinition.CA_PAYROLL_HOST},
               new String[]{"business-systems"},
               0,
               null),


    /**
     * coco-ledgers - Ledgers for Coco Pharmaceuticals.
     */
    COCO_LEDGERS("11071e76-23a8-4f3d-bd07-3b9aa9781bda",
                 "coco-ledgers",
                 "Ledgers for Coco Pharmaceuticals.",
                 "cocoledgersnpa",
                 SystemTypeDefinition.CLOUD_SAAS_SERVICE,
                 "V5.2",
                 PersonDefinition.STEW_FASTER,
                 FacilityDefinition.CLOUD_PROVIDER_FINANCE,
                 "https://fin-magic/ledgers",
                 null,
                 new String[]{"business-systems"},
                 0,
                 new SolutionComponent[]{SolutionComponent.ACCOUNTING_LEDGER}),


    /**
     * MFCTRL9482 - Manufacturing control system for the Austin factory.
     */
    AUS_MANUFACTURING_CONTROL("c24e9ea0-171a-46d8-a5d8-3fedcfe97aa4",
                              "MFCTRL9482",
                              "Manufacturing control system for the Austin factory.",
                              "ausmfgctrl",
                              SystemTypeDefinition.HOME_GROWN_APP_SERVER,
                              "V1.2",
                              PersonDefinition.STEW_FASTER,
                              FacilityDefinition.AUSTIN_DC,
                              "https://mopac.aus.coc.com",
                              new HostDefinition[]{HostDefinition.AUS_MANUFACTURING_CONTROL_HOST},
                              new String[]{"manufacturing-systems"},
                              1000,
                              null),

    /**
     * winch-mfg-control - Manufacturing control system for the Winchester factory.
     */
    WINCH_MANUFACTURING_CONTROL("0f59833f-8a8a-488b-808a-9ea24618bb6c",
                                "winch-mfg-control",
                                "Manufacturing control system for the Winchester factory.",
                                "winchmfgnpa",
                                SystemTypeDefinition.HOME_GROWN_APP_SERVER,
                                "V1.2",
                                PersonDefinition.STEW_FASTER,
                                FacilityDefinition.AUSTIN_DC,
                                "https://winch01.coco.com",
                                new HostDefinition[]{HostDefinition.WINCH_MANUFACTURING_CONTROL_HOST},
                                new String[]{"manufacturing-systems"},
                                1000,
                                null),

    /**
     * ed-mfg-control - Manufacturing control system for the Edmonton factory.
     */
    ED_MANUFACTURING_CONTROL("bdf60c10-e8f8-4b49-9f92-e0c6a40b0d7b",
                             "ed-mfg-control",
                             "Manufacturing control system for the Edmonton factory.",
                             "edmfgnpa",
                             SystemTypeDefinition.HOME_GROWN_APP_SERVER,
                             "V1.2",
                             null,
                             FacilityDefinition.AUSTIN_DC,
                             "https://ed01.coco.com",
                             new HostDefinition[]{HostDefinition.ED_MANUFACTURING_CONTROL_HOST},
                             new String[]{"manufacturing-systems"},
                             1000,
                             null),

    /**
     * ed-mfg-control - Manufacturing control system for the Edmonton factory.
     */
    MANUFACTURING_PLANNING("25e2e56d-2e2f-4f6f-a1ed-019dd76d589a",
                           "manufacturing-planning",
                           "Global manufacturing planning.",
                           "globmfgplannpa",
                           SystemTypeDefinition.COTS_SERVER,
                           "V7.8",
                           null,
                           FacilityDefinition.AMSTERDAM_DC,
                           "https://mfgplan.coco.com",
                           new HostDefinition[]{HostDefinition.COCO_MFG_PLANNING_AMS_HOST},
                           new String[]{"manufacturing-systems"},
                           1000,
                           null),

    /**
     * coco-haz-mat - Coco Pharmaceuticals Hazardous Materials Inventory.
     */
    HAZ_MAT("6e786315-ae54-445b-b516-f3c1f0e5f289",
            "coco-haz-mat",
            "Coco Pharmaceuticals Hazardous Materials Inventory.",
            "edmfgnpa",
            SystemTypeDefinition.HOME_GROWN_APP_SERVER,
            "V1.2",
            PersonDefinition.STEVE_STARTER,
            FacilityDefinition.AUSTIN_DC,
            "https://ed01.coco.com",
            new HostDefinition[]{HostDefinition.HAZ_MAT_HOST},
            new String[]{"compliance-systems"},
            1000,
            new SolutionComponent[]{SolutionComponent.HAZMAT_INVENTORY}),

    /**
     * austin-haz-mat - Austin Hazardous Materials Inventory.
     */
    AUS_HAZ_MAT("047691ac-8628-472d-aca0-82bedc947ef4",
                "austin-haz-mat",
                "Austin Manufacturing Hazardous Materials Inventory.",
                "aushazmatnpa",
                SystemTypeDefinition.HOME_GROWN_APP_SERVER,
                "V1.2",
                PersonDefinition.STEW_FASTER,
                FacilityDefinition.AUSTIN_DC,
                "https://mucky.coco.com",
                new HostDefinition[]{HostDefinition.AUS_MANUFACTURING_CONTROL_HOST},
                new String[]{"compliance-systems"},
                5000,
                new SolutionComponent[]{SolutionComponent.HAZMAT_INVENTORY}),


    /**
     * coco-sus - Coco Pharmaceuticals Sustainability Data Marts.
     */
    COCO_SUS("8a578f0d-f7ae-4255-b4a5-236241fa5449",
             "coco-sus",
             "Coco Pharmaceuticals Sustainability Data Marts.",
             "cocosusnpa",
             SystemTypeDefinition.DATABASE_SERVER,
             "V3.4",
             PersonDefinition.TOM_TALLY,
             FacilityDefinition.AMSTERDAM_DC,
             "https://ams04.coco.com:5432",
             new HostDefinition[]{HostDefinition.COCO_SUS_AMS_HOST},
             new String[]{"compliance-systems"},
             5000,
             new SolutionComponent[]{SolutionComponent.SUSTAINABILITY_ODS}),

    /**
     * coco-sus-dashboards - Coco Pharmaceuticals Sustainability Dashboards and Reporting.
     */
    COCO_SUS_DASHBOARDS("5a2927f3-eed6-4509-bceb-2c29aa415090",
                        "coco-sus-dashboards",
                        "Coco Pharmaceuticals Sustainability Dashboards and Reporting on SuperSet.",
                        "cocosusssnpa",
                        SystemTypeDefinition.COTS_SERVER,
                        "V1.2",
                        PersonDefinition.TOM_TALLY,
                        FacilityDefinition.AMSTERDAM_DC,
                        "https://ams04.coco.com:8080",
                        new HostDefinition[]{HostDefinition.COCO_SUS_AMS_HOST},
                        new String[]{"compliance-systems"},
                        5000,
                        new SolutionComponent[]{SolutionComponent.SUSTAINABILITY_DASHBOARDS}),

    /**
     * coco-sus-calculators - Coco Pharmaceuticals Sustainability Coco Pharmaceuticals Sustainability Calculators on Airflow..
     */
    COCO_SUS_CALCULATORS("3dbe7e96-c9e5-4336-a29a-9342258e50f8",
                         "coco-sus-calculators",
                         "Coco Pharmaceuticals Sustainability Calculators on Airflow.",
                         "cocosuscalcnpa",
                         SystemTypeDefinition.ETL_ENGINE,
                         "V3.4",
                         PersonDefinition.TOM_TALLY,
                         FacilityDefinition.AMSTERDAM_DC,
                         "https://ams04.coco.com:8070",
                         new HostDefinition[]{HostDefinition.COCO_SUS_AMS_HOST},
                         new String[]{"compliance-systems"},
                         5000,
                         new SolutionComponent[]{SolutionComponent.SUSTAINABILITY_CALCULATORS}),

    WINCHESTER_DEPOT_MANAGEMENT("c7b1467a-dea2-4288-95e5-8e1bc3ab2ed3",
                                "WINCHDEPOT01",
                                "Depot management system in Winchester",
                                "winchdepotmgt01",
                                SystemTypeDefinition.COTS_SERVER,
                                "V27.6",
                                PersonDefinition.STEW_FASTER,
                                FacilityDefinition.WINCHESTER_DC,
                                "https://winch5.coco.com:8070",
                                new HostDefinition[]{HostDefinition.DEPOT_MANAGEMENT_WINCH_HOST},
                                new String[]{"depot-systems"},
                                560,
                                null),

    KANSAS_CITY_DEPOT_MANAGEMENT("987df321-d940-4ce5-9ae1-8258736f0445",
                                 "KCDEPOT01",
                                 "Depot management system in Kansas City",
                                 "kansasdepotmgt01",
                                 SystemTypeDefinition.COTS_SERVER,
                                 "V27.6",
                                 PersonDefinition.STEW_FASTER,
                                 FacilityDefinition.WINCHESTER_DC,
                                 "https://kansas.coco.com:9070",
                                 new HostDefinition[]{HostDefinition.DEPOT_MANAGEMENT_KC_HOST},
                                 new String[]{"depot-systems"},
                                 560,
                                 null),

    EDMONTON_DEPOT_MANAGEMENT("afefd870-5616-4f35-a557-774b88b96bef",
                              "EDDEPOT01",
                              "Depot management system in Edmonton",
                              "eddepotmgt01",
                              SystemTypeDefinition.COTS_SERVER,
                              "V27.6",
                              PersonDefinition.STEW_FASTER,
                              FacilityDefinition.EDMONTON_OFFICE,
                              "https://ed02.coco.com:9876",
                              new HostDefinition[]{HostDefinition.DEPOT_MANAGEMENT_ED_HOST},
                              new String[]{"depot-systems"},
                              560,
                              null),


    ;

    private final String               systemGUID;
    private final String               systemId;
    private final String               description;
    private final String               userId;
    private final SystemTypeDefinition systemType;
    private final String               versionIdentifier;
    private final PersonDefinition     businessOwner;
    private final FacilityDefinition   systemLocation;
    private final String               networkAddress;
    private final HostDefinition[]     deployedOn;
    private final String[]             zones;
    private final long                 loadTime;
    private final SolutionComponent[]  implementingComponents;


    /**
     * The constructor creates an instance of the enum
     *
     * @param systemGUID        open metadata guid
     * @param systemId          unique id for the enum
     * @param description       description of the use of this value
     * @param userId            userId of the server
     * @param systemType        category of system
     * @param versionIdentifier version
     * @param businessOwner     business stakeholder responsible for system
     * @param systemLocation    location
     * @param networkAddress    url of server
     * @param deployedOn        deployed on host
     * @param zones             zone membership
     * @param loadTime          time offset to set creationTime
     */
    SystemDefinition(String                 systemGUID,
                     String                 systemId,
                     String                 description,
                     String                 userId,
                     SystemTypeDefinition   systemType,
                     String                 versionIdentifier,
                     PersonDefinition       businessOwner,
                     FacilityDefinition     systemLocation,
                     String                 networkAddress,
                     HostDefinition[]       deployedOn,
                     String[]               zones,
                     long                   loadTime,
                     SolutionComponent[]    implementingComponents)
    {
        this.systemGUID = systemGUID;
        this.systemId = systemId;
        this.description = description;
        this.userId = userId;
        this.systemType = systemType;
        this.versionIdentifier = versionIdentifier;
        this.businessOwner = businessOwner;
        this.systemLocation = systemLocation;
        this.networkAddress = networkAddress;
        this.deployedOn = deployedOn;
        this.zones = zones;
        this.loadTime = loadTime;
        this.implementingComponents = implementingComponents;
    }


    /**
     * Return the open metadata GUID
     *
     * @return string
     */
    public String getSystemGUID()
    {
        return systemGUID;
    }


    /**
     * Return the manufactured qualified name.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return "System:" + systemId;
    }


    /**
     * Return the deployed identifier for this system.
     *
     * @return string
     */
    public String getSystemId()
    {
        return systemId;
    }


    /**
     * Return the description of this system.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the userId used by this server.  Will be null for a cloud service.
     *
     * @return string
     */
    public String getUserId() { return userId; }


    /**
     * Business owner responsible for the system.  Typically funds updates/maintenance etc.  Does not
     * perform updates/maintenance.
     *
     * @return person
     */
    public PersonDefinition getBusinessOwner()
    {
        return businessOwner;
    }


    /**
     * Return the network URL for the system.
     *
     * @return url
     */
    public String getNetworkAddress()
    {
        return networkAddress;
    }


    /**
     * Return the type of system (used to set deployedImplementationType).
     *
     * @return system type definition
     */
    public SystemTypeDefinition getSystemType()
    {
        return systemType;
    }


    /**
     * Return the version of the server.
     *
     * @return string
     */
    public String getVersionIdentifier()
    {
        return versionIdentifier;
    }


    /**
     * Return the facility where this server is located.
     *
     * @return facility identifier
     */
    public FacilityDefinition getSystemLocation()
    {
        return systemLocation;
    }


    /**
     * Return the list of zones that this server belongs to.
     *
     * @return list of strings
     */
    public List<String> getZones()
    {
        if (zones != null)
        {
            return Arrays.asList(zones);
        }

        return null;
    }


    /**
     * Return where this system is running.  May be null if running as a cloud service and so the hosts are not known.
     *
     * @return array of hosts where this system is running
     */
    public HostDefinition[] getDeployedOn()
    {
        return deployedOn;
    }


    /**
     * Return the time offset to set up the creation time.
     *
     * @return long
     */
    public long getLoadTime()
    {
        return loadTime;
    }


    /**
     * Return the solution components that should be inked with the ImplementedBy relationship.
     *
     * @return array
     */
    public SolutionComponent[] getImplementingComponents()
    {
        return implementingComponents;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "SystemDefinition{" + systemId + '}';
    }
}
