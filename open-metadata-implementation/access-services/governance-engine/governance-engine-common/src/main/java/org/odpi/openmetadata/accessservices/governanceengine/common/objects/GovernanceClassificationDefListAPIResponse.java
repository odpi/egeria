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
public class GovernanceClassificationDefListAPIResponse extends GovernanceEngineOMASAPIResponse {



    private List<GovernanceClassificationDef> governanceClassificationDefList = null;

    /**
     * Default constructor
     */
    public GovernanceClassificationDefListAPIResponse() {
    }


    public List<GovernanceClassificationDef> getTagList() {
        return governanceClassificationDefList;
    }

    /**
     *
     * @param governanceClassificationDefList salary to set (in cents)
     */
    public void setTagList(List<GovernanceClassificationDef> governanceClassificationDefList) {
        this.governanceClassificationDefList = governanceClassificationDefList;
    }



    @Override
    public String toString() {

        return "TagMapResponse{" +
                "tagmap=" + governanceClassificationDefList +
                ", relatedHTTPCode=" + relatedHTTPCode +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                '}';
    }
}
