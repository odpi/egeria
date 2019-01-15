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
     */
    V11502   (11502000, "v11502", "xsd_unique_key"),
    V11502SP5(11502500, "v11502sp5", "Rule_Execution_Result"),
    V11702   (11702000, "v11702", "analytics_project");

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
