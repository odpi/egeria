/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The supertype of the IGC technical objects (including all OpenIGC assets).
 * <br><br>
 * Simply define a new POJO as extending this base class to inherit the attributes that are found
 * on virtually all IGC asset types.
 */
public class InformationAsset extends MainObject {
    @JsonIgnore public static final String IGC_TYPE_ID = "information_asset";
}
