/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.businesssystems;

import org.odpi.openmetadata.samples.archiveutilities.sustainability.FacilityDefinition;

import java.util.Arrays;
import java.util.List;

/**
 * The HostDefinition is used to feed the definition of the organization's IT machines (hosts) for Coco Pharmaceuticals scenarios.
 * The hosts are important in the sustainability scenario to look at power usage.
 */
public enum HostDefinition
{
    /**
     * ams45 - Amsterdam data centre standard machine 45.
     */
    COCO_PROCUREMENT_AMS_HOST("ams45",
                              "Amsterdam data centre standard machine 45.",
                              HostTypeDefinition.BARE_METAL,
                              FacilityDefinition.AMSTERDAM_DC,
                              "https://ams45.coco.com",
                              "Ubuntu",
                              "23.04",
                              null,
                              new String[]{"machines"},
                              0),

    /**
     * 327-3 - NY data center standard machine 327-3.
     */
    COCO_PROCUREMENT_NY_HOST("327-3",
                             "NY data center standard machine 327-3.",
                             HostTypeDefinition.BARE_METAL,
                             FacilityDefinition.NEW_YORK_DC,
                             "https://ny327-3.coco.com",
                             "Ubuntu",
                             "23.04",
                             null,
                             new String[]{"machines"},
                             0),

    /**
     * winch12 - Winchester data centre standard machine 12.
     */
    COCO_PROCUREMENT_WINCH_HOST("winch12",
                                "Winchester data centre standard machine 12.",
                                HostTypeDefinition.BARE_METAL,
                                FacilityDefinition.WINCHESTER_DC,
                                "https://winch12.coco.com",
                                "Ubuntu",
                                "23.04",
                                null,
                                new String[]{"machines"},
                                34),

    /**
     * capital - Austin data centre standard machine capital.
     */
    COCO_PROCUREMENT_AUS_HOST("capital",
                              "Austin data centre standard machine capital.",
                              HostTypeDefinition.BARE_METAL,
                              FacilityDefinition.AUSTIN_DC,
                              "https://capital.aus.coco.com",
                              "Linux",
                              "21.1",
                              null,
                              new String[]{"machines"},
                              1001),

    /**
     * ed01 - Edmonton data centre standard machine 01.
     */
    COCO_PROCUREMENT_ED_HOST("ed01",
                             "Edmonton data centre standard machine 01.",
                             HostTypeDefinition.BARE_METAL,
                             FacilityDefinition.EDMONTON_OFFICE,
                             "https://ed01.coco.com",
                             "Ubuntu",
                             "22.04",
                             null,
                             new String[]{"machines"},
                             1501),

    /**
     * ams23 - Amsterdam data centre standard machine 23.
     */
    COCO_INVENTORY_HOST("ams23",
                        "Amsterdam data centre standard machine 23.",
                        HostTypeDefinition.BARE_METAL,
                        FacilityDefinition.AMSTERDAM_DC,
                        "https://ams23.coco.com",
                        "Ubuntu",
                        "23.04",
                        null,
                        new String[]{"machines"},
                        0),

    /**
     * ams02 - Amsterdam data centre standard machine 02.
     */
    COCO_HRIM_HOST("ams02",
                   "Amsterdam data centre standard machine 02.",
                   HostTypeDefinition.BARE_METAL,
                   FacilityDefinition.AMSTERDAM_DC,
                   "https://ams02.coco.com",
                   "Ubuntu",
                   "23.04",
                   null,
                   new String[]{"machines"},
                   0),

    /**
     * ams06 - Amsterdam data centre standard machine 06.
     */
    COCO_PAGES_HOST("ams06",
                    "Amsterdam data centre standard machine 06.",
                    HostTypeDefinition.BARE_METAL,
                    FacilityDefinition.AMSTERDAM_DC,
                    "https://ams06.coco.com",
                    "Ubuntu",
                    "23.04",
                    null,
                    new String[]{"machines"},
                    0),

    /**
     * ams01 - Amsterdam data centre standard machine 01.
     */
    SEC_ADMIN_HOST("ams01",
                   "Amsterdam data centre standard machine 01.",
                   HostTypeDefinition.BARE_METAL,
                   FacilityDefinition.AMSTERDAM_DC,
                   "https://ams01.coco.com",
                   "Ubuntu",
                   "23.04",
                   null,
                   new String[]{"machines"},
                   0),

    /**
     * lon02 - London data centre standard machine 02.
     */
    UK_PAYROLL_HOST("lon02",
                    "London data centre standard machine 02.",
                    HostTypeDefinition.BARE_METAL,
                    FacilityDefinition.LONDON_DC,
                    "https://lon02.coco.com",
                    "Ubuntu",
                    "23.04",
                    null,
                    new String[]{"machines"},
                    0),

    /**
     * ams56 - Amsterdam data centre standard machine 56.
     */
    NL_PAYROLL_HOST("ams56",
                    "Amsterdam data centre standard machine 56.",
                    HostTypeDefinition.BARE_METAL,
                    FacilityDefinition.AMSTERDAM_DC,
                    "https://ams56.coco.com",
                    "Ubuntu",
                    "23.04",
                    null,
                    new String[]{"machines"},
                    0),

    /**
     * ed05 - Edmonton data centre standard machine 05.
     */
    CA_PAYROLL_HOST("ed05",
                    "Edmonton data centre standard machine 05.",
                    HostTypeDefinition.BARE_METAL,
                    FacilityDefinition.EDMONTON_OFFICE,
                    "https://ed05.coco.com",
                    "Ubuntu",
                    "23.04",
                    null,
                    new String[]{"machines"},
                    0),

    /**
     * mopac - Austin data center standard machine mopac.
     */
    AUS_MANUFACTURING_CONTROL_HOST("mopac",
                                   "Austin data center standard machine mopac.",
                                   HostTypeDefinition.BARE_METAL,
                                   FacilityDefinition.AUSTIN_DC,
                                   "https://mopac.aus.coc.com",
                                   "Linux",
                                   "21.1",
                                   null,
                                   new String[]{"machines"},
                                   1000),

    /**
     * winch01 - Winchester data centre standard machine 01.
     */
    WINCH_MANUFACTURING_CONTROL_HOST("winch01",
                                     "Winchester data centre standard machine 01.",
                                     HostTypeDefinition.BARE_METAL,
                                     FacilityDefinition.WINCHESTER_DC,
                                     "https://winch01.coco.com",
                                     "Ubuntu",
                                     "22.04",
                                     null,
                                     new String[]{"machines"},
                                     1000),

    /**
     * ed04 - Edmonton data centre standard machine 04.
     */
    ED_MANUFACTURING_CONTROL_HOST("ed04",
                                  "Edmonton data centre standard machine 04.",
                                  HostTypeDefinition.BARE_METAL,
                                  FacilityDefinition.EDMONTON_OFFICE,
                                  "https://ed04.coco.com",
                                  "Ubuntu",
                                  "23.04",
                                  null,
                                  new String[]{"machines"},
                                  1000),

    /**
     * ams14 - Amsterdam data centre standard machine ams14.
     */
    HAZ_MAT_HOST("ams14",
                 "Amsterdam data centre standard machine ams14.",
                 HostTypeDefinition.BARE_METAL,
                 FacilityDefinition.AMSTERDAM_DC,
                 "https://ams14.coco.com",
                 "Ubuntu",
                 "23.04",
                 null,
                 new String[]{"machines"},
                 1000),
    ;

    private final String                      hostId;
    private final String                      description;
    private final HostTypeDefinition          hostType;
    private final FacilityDefinition          hostLocation;
    private final String                      networkAddress;
    private final String                      operatingSystem;
    private final String                      patchLevel;
    private final HostDefinition              deployedOn;
    private final String[]                    zones;
    private final long                        loadTime;


    /**
     * The constructor creates an instance of the enum
     *
     * @param hostId            unique id for the enum
     * @param description       description of the use of this value
     * @param hostType          type name for host
     * @param hostLocation      location
     * @param networkAddress    url of server
     * @param operatingSystem   operating system running on host
     * @param patchLevel        patch level of operating system
     * @param deployedOn        host is deployed on another host
     * @param zones             zone membership
     * @param loadTime          time offset to set creationTime
     */
    HostDefinition(String                      hostId,
                   String                      description,
                   HostTypeDefinition          hostType,
                   FacilityDefinition          hostLocation,
                   String                      networkAddress,
                   String                      operatingSystem,
                   String                      patchLevel,
                   HostDefinition              deployedOn,
                   String[]                    zones,
                   long                        loadTime)
    {
        this.hostId = hostId;
        this.description = description;
        this.hostType = hostType;
        this.hostLocation = hostLocation;
        this.networkAddress = networkAddress;
        this.operatingSystem = operatingSystem;
        this.patchLevel = patchLevel;
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
        return "Host:" + getHostType().getOpenMetadataTypeName() + ":" + hostId;
    }


    /**
     * Return the deployed identifier for this system.
     *
     * @return string
     */
    public String getHostId()
    {
        return hostId;
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
     * Return the host name of this host.
     *
     * @return host URL
     */
    public String getNetworkAddress()
    {
        return networkAddress;
    }


    /**
     * Return the type of host (used to set the open metadata type name).
     *
     * @return system type definition
     */
    public HostTypeDefinition getHostType()
    {
        return hostType;
    }


    /**
     * Return the facility where this server is located.
     *
     * @return facility identifier
     */
    public FacilityDefinition getHostLocation()
    {
        return hostLocation;
    }


    /**
     * Return the operating system.
     *
     * @return string
     */
    public String getOperatingSystem()
    {
        return operatingSystem;
    }


    /**
     * Return the patch level of the operating system.
     *
     * @return string
     */
    public String getPatchLevel()
    {
        return patchLevel;
    }


    /**
     * Return the host that this system system is deployed on.
     *
     * @return
     */
    public HostDefinition getDeployedOn()
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
        return "HostDefinition{" + hostId + '}';
    }
}
