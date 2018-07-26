/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.api.objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * TagMapResponse is the response structure used on the Governance Engine OMAS REST API calls that returns a
 * TagMap object as a response.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GovernedAssetAPIResponse extends GovernanceEngineOMASAPIResponse {


    public GovernedAsset getAsset() {
        return asset;
    }

    /**
     * Return the Connection object.
     *
     * @return connection
     */



    private GovernedAsset asset = null;

    /**
     * Default constructor
     */
    public GovernedAssetAPIResponse() {
    }




    /**
     * Set up the Connection object.
     *
     * @param asset - TagMap object
     */
    public void setAsset(GovernedAsset asset) {
        this.asset = asset;
    }


    @Override
    public String toString() {
        return "GovernedAssetListAPIResponse{" +
                "assetList=" + asset +
                ", relatedHTTPCode=" + relatedHTTPCode +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                '}';
    }
}
