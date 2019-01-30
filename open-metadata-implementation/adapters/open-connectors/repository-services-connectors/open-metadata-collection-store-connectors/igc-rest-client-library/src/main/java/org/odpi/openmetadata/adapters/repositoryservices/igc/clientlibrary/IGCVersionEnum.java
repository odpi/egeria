/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary;

import java.io.Serializable;

/**
 * Enum to capture the different versions of IBM Information Server against which bindings have been generated.
 */
public enum IGCVersionEnum implements Serializable {

    /**
     * The order of declaration here MUST be in ascending order (oldest version at the top, newest at the bottom).
     *  - Earliest supported version is v11.5.0.1 (v11.5 fixpack 1) -- earlier downloads are no longer available
     *  - If a specific version in-between is not listed, it is because there are no changes to the IGC assets in
     *    that in-between version. The detection will therefore simply rely on the version listed below that matches
     *    the IGC asset types available in your environment.
     *  - Specific versions tested:
     *      - v11.5.0.2 SP4: no change from v11.5.0.2 SP3
     *      - v11.7.0.0 SP2: no change from v11.7.0.0
     *      - v11.7.0.0 SP3: no change from v11.7.0.0 (other than 'volume' asset's 'type' property having minor enum difference)
     *      - v11.7.0.2 SP1: no change from v11.7.0.2
     */
    V11501   (11501000, "v11501", "xsd_unique_key"),
    V11501RU5(11501500, "v11501ru5", "development_log"),
    V11502   (11502000, "v11502", "term_assignment"),
    V11502SP3(11502300, "v11502sp3", "Rule_Execution_Result"),
    V11502SP5(11502500, "v11502sp5", "hbase_namespace"),
    V11700   (11700000, "v11700", "binary_infoset_operation"),
    V11701   (11701000, "v11701", "analytics_project"),
    V11701SP1(11701100, "v11701sp1", "automation_rule"),
    V11702   (11702000, "v11702", "lineage_container");

    private static final long serialVersionUID = 1L;

    private int    versionCode;
    private String versionName;
    private String typeNameFirstAvailable;

    /**
     * Constructor to set up a single instances of the enum.
     */
    IGCVersionEnum(int versionCode, String versionName, String typeNameFirstAvailable) {
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.typeNameFirstAvailable = typeNameFirstAvailable;
    }

    /**
     * Return the code for this enum instance
     *
     * @return int  type code
     */
    private int getOrdinal()
    {
        return versionCode;
    }

    /**
     * Return the default name for this enum instance.
     *
     * @return String  default name
     */
    public String getVersionString()
    {
        return versionName;
    }

    /**
     * Return the name of an IGC asset type that first became available in this version.
     *
     * @return String
     */
    public String getTypeNameFirstAvailableInThisVersion() { return typeNameFirstAvailable; }

    /**
     * Returns true iff this version is a lower version than the provided version
     *
     * @param version the version against which to check this version is lower
     * @return boolean
     */
    public boolean isLowerThan(IGCVersionEnum version) {
        return this.getOrdinal() < version.getOrdinal();
    }

    /**
     * Returns true iff this version is equal to the provided version
     *
     * @param version the version against which to check this version is equal
     * @return boolean
     */
    public boolean isEqualTo(IGCVersionEnum version) {
        return this.getOrdinal() == version.getOrdinal();
    }

    /**
     * Returns true iff this version is a higher version than the provided version
     *
     * @param version the version against which to check this version is higher
     * @return boolean
     */
    public boolean isHigherThan(IGCVersionEnum version) {
        return this.getOrdinal() > version.getOrdinal();
    }

    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "IGCVersion{" +
                "versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                '}';
    }

}
