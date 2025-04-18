/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.businesssystems;


import java.util.Arrays;
import java.util.List;

/**
 * The DataStoreDefinition is used to feed the definition of the organization's data stores for Coco Pharmaceuticals scenarios.
 */
public enum DataStoreDefinition
{
    /**
     * DGG-ODS-01 - Central operational data store feeding the accounts/ledgers.
     */
    ODS("DGG-ODS-01",
             "Central operational data store feeding the accounts/ledgers.",
             "Database",
             "V2.6",
             new String[]{"business-systems", "sustainability"},
                0),
    ;

    private final String   dataStoreId;
    private final String   description;
    private final String   typeName;
    private final String   versionIdentifier;
    private final String[] zones;
    private final long     loadTime;


    /**
     * The constructor creates an instance of the enum
     *
     * @param dataStoreId          unique id for the enum
     * @param description       description of the use of this value
     * @param typeName          open metadata type for the data store
     * @param versionIdentifier version
     * @param zones             zone membership
     * @param loadTime          time offset to set creationTime
     */
    DataStoreDefinition(String   dataStoreId,
                        String   description,
                        String   typeName,
                        String   versionIdentifier,
                        String[] zones,
                        long     loadTime)
    {
        this.dataStoreId = dataStoreId;
        this.description = description;
        this.typeName = typeName;
        this.versionIdentifier = versionIdentifier;
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
        return "DataStore::" + typeName + "::" + dataStoreId;
    }


    /**
     * Return the deployed identifier for this data store.
     *
     * @return string
     */
    public String getDataStoreId()
    {
        return dataStoreId;
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
     * Return the userId used by this server.
     *
     * @return string
     */
    public String getTypeName() { return typeName; }


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
     * Return the version of the data store.
     *
     * @return string
     */
    public String getVersionIdentifier()
    {
        return versionIdentifier;
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
        return "DataStoreDefinition{" + dataStoreId + '}';
    }
}
