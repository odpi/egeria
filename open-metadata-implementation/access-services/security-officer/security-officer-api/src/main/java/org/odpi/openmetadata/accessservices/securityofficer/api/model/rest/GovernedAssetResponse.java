/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securityofficer.api.model.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.GovernedAsset;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TagMapResponse is the response structure used on the Governance Engine OMAS REST API calls that returns a
 * TagMap object as a response.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GovernedAssetResponse extends SecurityOfficerOMASAPIResponse implements Serializable {

    private static final long          serialVersionUID = 1L;
    private              GovernedAsset asset;

    /**
     * Return the Connection object.
     *
     * @return connection
     */
    public GovernedAsset getAsset() {
        return asset;
    }

    /**
     * Set up the Connection object.
     *
     * @param asset - TagMap object
     */
    public void setAsset(GovernedAsset asset) {
        this.asset = asset;
    }


}
