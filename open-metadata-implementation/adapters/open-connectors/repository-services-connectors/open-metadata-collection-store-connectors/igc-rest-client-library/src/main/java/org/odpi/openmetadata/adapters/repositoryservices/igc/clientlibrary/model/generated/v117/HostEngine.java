/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POJO for the 'host_(engine)' asset type in IGC, displayed as 'Host (Engine)' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class HostEngine extends Host {

    public static String getIgcTypeId() { return "host_(engine)"; }
    public static String getIgcTypeDisplayName() { return "Host (Engine)"; }

    public static final Boolean isHostEngine(Object obj) { return (obj.getClass() == HostEngine.class); }

}
