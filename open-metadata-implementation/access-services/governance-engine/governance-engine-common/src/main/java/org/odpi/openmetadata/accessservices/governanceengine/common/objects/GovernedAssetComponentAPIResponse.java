/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.common.objects;

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
public class GovernedAssetComponentAPIResponse extends GovernanceEngineOMASAPIResponse {


    public GovernedAssetComponent getAsset() {
        return asset;
    }

    /**
     * Return the Connection object.
     *
     * @return connection
     */



    private GovernedAssetComponent asset = null;

    /**
     * Default constructor
     */
    public GovernedAssetComponentAPIResponse() {
    }




    /**
     * Set up the Connection object.
     *
     * @param asset - TagMap object
     */
    public void setAsset(GovernedAssetComponent asset) {
        this.asset = asset;
    }


    @Override
    public String toString() {
        return "GovernedAssetComponentListAPIResponse{" +
                "assetList=" + asset +
                ", relatedHTTPCode=" + relatedHTTPCode +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                '}';
    }
}
