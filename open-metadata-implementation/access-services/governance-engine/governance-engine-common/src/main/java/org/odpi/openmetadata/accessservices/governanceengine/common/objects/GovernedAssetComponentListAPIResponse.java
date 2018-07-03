/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.common.objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * TagMapResponse is the response structure used on the Governance Engine OMAS REST API calls that returns a
 * TagMap object as a response.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GovernedAssetComponentListAPIResponse extends GovernanceEngineOMASAPIResponse {
    private List<GovernedAssetComponent> assetList = null;

    /**
     * Default constructor
     */
    public GovernedAssetComponentListAPIResponse() {
    }


    /**
     * Return the Connection object.
     *
     * @return connection
     */
    public List<GovernedAssetComponent> getGovernedAssetList() {
        return assetList;
    }

    /**
     * Set up the Connection object.
     *
     * @param assetList - TagMap object
     */
    public void setGovernedAssetList(List<GovernedAssetComponent> assetList) {
        this.assetList = assetList;
    }


    @Override
    public String toString() {
        return "GovernedAssetComponentListAPIResponse{" +
                "assetList=" + assetList +
                ", relatedHTTPCode=" + relatedHTTPCode +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                '}';
    }
}
