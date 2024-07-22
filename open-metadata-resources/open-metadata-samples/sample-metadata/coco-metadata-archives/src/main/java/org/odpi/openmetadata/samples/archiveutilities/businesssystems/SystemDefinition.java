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
     * procurement01 - Purchasing system for Coco Pharmaceuticals.
     */
    COCO_PROCUREMENT_AMS("procurement01",
                         "Purchasing system for Coco Pharmaceuticals.",
                         "procurement01npa",
                         SystemTypeDefinition.COTS_SERVER,
                         "V2.6",
                         PersonDefinition.REGGIE_MINT,
                         FacilityDefinition.AMSTERDAM_DC,
                         "https://ams45.coco.com",
                         new HostDefinition[]{HostDefinition.COCO_PROCUREMENT_AMS_HOST},
                         new String[]{"business-systems", "sustainability"},
                         0),

    /**
     * procurement02 - Local purchasing system for New York Site.
     */
    COCO_PROCUREMENT_NY("procurement02",
                        "Local purchasing system for New York Site.",
                        "procurement02npa",
                        SystemTypeDefinition.COTS_SERVER,
                        "V2.6",
                        PersonDefinition.REGGIE_MINT,
                        FacilityDefinition.NEW_YORK_DC,
                        "https://ny327-3.coco.com",
                        new HostDefinition[]{HostDefinition.COCO_PROCUREMENT_NY_HOST},
                        new String[]{"business-systems", "sustainability"},
                        0),

    /**
     * procurement03 - Local purchasing system for Winchester Factory Site.
     */
    COCO_PROCUREMENT_WINCH("procurement03",
                           "Local purchasing system for Winchester Factory Site.",
                           "procurement03npa",
                           SystemTypeDefinition.COTS_SERVER,
                           "V1.0",
                           PersonDefinition.REGGIE_MINT,
                           FacilityDefinition.WINCHESTER_DC,
                           "https://winch12.coco.com",
                           new HostDefinition[]{HostDefinition.COCO_PROCUREMENT_WINCH_HOST},
                           new String[]{"business-systems", "sustainability"},
                           34),

    /**
     * procurement04 - Local purchasing system for Austin Factory Site.
     */
    COCO_PROCUREMENT_AUS("procurement04",
                         "Local purchasing system for Austin Factory Site.",
                         "procurement04npa",
                         SystemTypeDefinition.COTS_SERVER,
                         "V7.2",
                         PersonDefinition.REGGIE_MINT,
                         FacilityDefinition.AUSTIN_DC,
                         "https://capital.aus.coco.com",
                         new HostDefinition[]{HostDefinition.COCO_PROCUREMENT_AUS_HOST},
                         new String[]{"business-systems", "sustainability"},
                         1001),

    /**
     * procurement05 - Local purchasing system for Edmonton Factory Site.
     */
    COCO_PROCUREMENT_ED("procurement05",
                        "Local purchasing system for Edmonton Factory Site.",
                        "procurement05npa",
                        SystemTypeDefinition.COTS_SERVER,
                        "V7.3",
                        PersonDefinition.REGGIE_MINT,
                        FacilityDefinition.EDMONTON_OFFICE,
                        "https://ed01.coco.com",
                        new HostDefinition[]{HostDefinition.COCO_PROCUREMENT_ED_HOST},
                        new String[]{"business-systems"},
                        1501),

    /**
     * coco-inventory - Inventory for raw materials and products produced across all Coco Pharmaceuticals sites.
     */
    COCO_INVENTORY("coco-inventory",
                   "Inventory for raw materials and products produced across all Coco Pharmaceuticals sites.",
                   "cocoinventorynpa",
                   SystemTypeDefinition.HOME_GROWN_APP_SERVER,
                   "V5.2",
                   PersonDefinition.ZACH_NOW,
                   FacilityDefinition.AMSTERDAM_DC,
                   "https://ams23.coco.com",
                   new HostDefinition[]{HostDefinition.COCO_INVENTORY_HOST},
                   new String[]{"business-systems"},
                   0),

    /**
     * coco-hrim - Human Resources Information Manager (HRIM) provides the central management application for employee management including hiring, skills management, recognition and all reasons for termination of employment.
     */
    COCO_HRIM("coco-hrim",
              "Human Resources Information Manager (HRIM) provides the central management application for employee management including hiring, skills management, recognition and all reasons for termination of employment.",
              "cocohrinnpa",
              SystemTypeDefinition.COTS_SERVER,
              "V5.2",
              PersonDefinition.FAITH_BROKER,
              FacilityDefinition.AMSTERDAM_DC,
              "https://ams02.coco.com",
              new HostDefinition[]{HostDefinition.COCO_HRIM_HOST},
              new String[]{"business-systems", "sustainability"},
              0),

    /**
     * cocopages - Employee directory with business partners.
     */
    COCO_PAGES("cocopages",
               "Employee directory with business partners.",
               "cocopagesnpa",
               SystemTypeDefinition.HOME_GROWN_APP_SERVER,
               "V5.2",
               PersonDefinition.FAITH_BROKER,
               FacilityDefinition.AMSTERDAM_DC,
               "https://ams06.coco.com",
               new HostDefinition[]{HostDefinition.COCO_PAGES_HOST},
               new String[]{"business-systems", "sustainability"},
               0),

    /**
     * sec-admin - Security administration for all access grants to Coco Pharmaceutical systems.
     */
    SEC_ADMIN("sec-admin",
              "Security administration for all access grants to Coco Pharmaceutical systems.",
              "secadminnpa",
              SystemTypeDefinition.HOME_GROWN_APP_SERVER,
              "V5.2",
              PersonDefinition.IVOR_PADLOCK,
              FacilityDefinition.AMSTERDAM_DC,
              "https://ams01.coco.com",
              new HostDefinition[]{HostDefinition.SEC_ADMIN_HOST},
              new String[]{"business-systems", "sustainability"},
              0),

    /**
     * coco-expenses - Employee expense declaration for Coco Pharmaceuticals.
     */
    EMPLOYEE_EXPENSES("coco-expenses",
                      "Employee expense declaration for Coco Pharmaceuticals.",
                      null,
                      SystemTypeDefinition.CLOUD_SAAS_SERVICE,
                      "V5.2",
                      PersonDefinition.REGGIE_MINT,
                      FacilityDefinition.CLOUD_PROVIDER_TRAVEL,
                      "https://bethere.com/expenses",
                      null,
                      new String[]{"business-systems"},
                      0),

    /**
     * UK payroll - Payroll and UK tax calculations.
     */
    UK_PAYROLL("UK payroll",
               "Payroll and UK tax calculations.",
               "ukpayrollnpa",
               SystemTypeDefinition.COTS_SERVER,
               "V1.0",
               PersonDefinition.FAITH_BROKER,
               FacilityDefinition.LONDON_DC,
               "https://lon02.coco.com",
               new HostDefinition[]{HostDefinition.UK_PAYROLL_HOST},
               new String[]{"business-systems"},
               0),

    /**
     * Netherlands payroll - Payroll and Dutch tax calculations.
     */
    NL_PAYROLL("Netherlands payroll",
               "Payroll and Dutch tax calculations.",
               "nlpayrollnpa",
               SystemTypeDefinition.COTS_SERVER,
               "V5.4",
               PersonDefinition.FAITH_BROKER,
               FacilityDefinition.AMSTERDAM_DC,
               "https://ams56.coco.com",
               new HostDefinition[]{HostDefinition.NL_PAYROLL_HOST},
               new String[]{"business-systems"},
               0),

    /**
     * Canadian payroll - Payroll and Canadian tax calculations.
     */
    CA_PAYROLL("Canadian payroll",
               "Payroll and Canadian tax calculations.",
               "nlpayrollnpa",
               SystemTypeDefinition.COTS_SERVER,
               "V5.4",
               PersonDefinition.REGGIE_MINT,
               FacilityDefinition.EDMONTON_OFFICE,
               "https://ed05.coco.com",
               new HostDefinition[]{HostDefinition.CA_PAYROLL_HOST},
               new String[]{"business-systems"},
               0),


    /**
     * coco-ledgers - Ledgers for Coco Pharmaceuticals.
     */
    COCO_LEDGERS("coco-ledgers",
                 "Ledgers for Coco Pharmaceuticals.",
                 "cocoledgersnpa",
                 SystemTypeDefinition.CLOUD_SAAS_SERVICE,
                 "V5.2",
                 PersonDefinition.STEW_FASTER,
                 FacilityDefinition.CLOUD_PROVIDER_FINANCE,
                 "https://fin-magic/ledgers",
                 null,
                 new String[]{"business-systems"},
                 0),


    /**
     * MFCTRL9482 - Manufacturing control system for the Austin factory.
     */
    AUS_MANUFACTURING_CONTROL("MFCTRL9482",
                              "Manufacturing control system for the Austin factory.",
                              "ausmfgctrl",
                              SystemTypeDefinition.HOME_GROWN_APP_SERVER,
                              "V1.2",
                              PersonDefinition.STEW_FASTER,
                              FacilityDefinition.AUSTIN_DC,
                              "https://mopac.aus.coc.com",
                              new HostDefinition[]{HostDefinition.AUS_MANUFACTURING_CONTROL_HOST},
                              new String[]{"manufacturing-systems"},
                              1000),

    /**
     * winch-mfg-control - Manufacturing control system for the Winchester factory.
     */
    WINCH_MANUFACTURING_CONTROL("winch-mfg-control",
                                "Manufacturing control system for the Winchester factory.",
                                "winchmfgnpa",
                                SystemTypeDefinition.HOME_GROWN_APP_SERVER,
                                "V1.2",
                                PersonDefinition.STEW_FASTER,
                                FacilityDefinition.AUSTIN_DC,
                                "https://winch01.coco.com",
                                new HostDefinition[]{HostDefinition.WINCH_MANUFACTURING_CONTROL_HOST},
                                new String[]{"manufacturing-systems"},
                                1000),

    /**
     * ed-mfg-control - Manufacturing control system for the Edmonton factory.
     */
    ED_MANUFACTURING_CONTROL("ed-mfg-control",
                             "Manufacturing control system for the Edmonton factory.",
                             "edmfgnpa",
                             SystemTypeDefinition.HOME_GROWN_APP_SERVER,
                             "V1.2",
                             null,
                             FacilityDefinition.AUSTIN_DC,
                             "https://ed01.coco.com",
                             new HostDefinition[]{HostDefinition.ED_MANUFACTURING_CONTROL_HOST},
                             new String[]{"manufacturing-systems"},
                             1000),

    /**
     * coco-haz-mat - Coco Pharmaceuticals Hazardous Materials Inventory.
     */
    HAZ_MAT("coco-haz-mat",
            "Coco Pharmaceuticals Hazardous Materials Inventory.",
            "edmfgnpa",
            SystemTypeDefinition.HOME_GROWN_APP_SERVER,
            "V1.2",
            PersonDefinition.STEVE_STARTER,
            FacilityDefinition.AUSTIN_DC,
            "https://ed01.coco.com",
            new HostDefinition[]{HostDefinition.HAZ_MAT_HOST},
            new String[]{"compliance-systems"},
            1000),
    ;

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


    /**
     * The constructor creates an instance of the enum
     *
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
    SystemDefinition(String                 systemId,
                     String                 description,
                     String                 userId,
                     SystemTypeDefinition   systemType,
                     String                 versionIdentifier,
                     PersonDefinition       businessOwner,
                     FacilityDefinition     systemLocation,
                     String                 networkAddress,
                     HostDefinition[]       deployedOn,
                     String[]               zones,
                     long                   loadTime)
    {
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
     * @return list of hosts where this system is running
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
