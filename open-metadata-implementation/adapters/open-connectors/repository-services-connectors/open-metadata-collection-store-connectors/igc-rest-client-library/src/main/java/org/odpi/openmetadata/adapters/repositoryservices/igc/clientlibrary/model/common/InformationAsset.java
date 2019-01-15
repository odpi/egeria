/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common;

/**
 * The supertype of the IGC technical objects (including all OpenIGC assets).
 * <br><br>
 * Simply define a new POJO as extending this base class to inherit the attributes that are found
 * on virtually all IGC asset types.
 */
public class InformationAsset extends MainObject {
    public static String getIgcTypeId() { return "information_asset"; }
    public static String getIgcTypeDisplayName() { return "Information Asset"; }
    public static final Boolean isInformationAsset(Object obj) { return (obj.getClass() == InformationAsset.class); }
}
