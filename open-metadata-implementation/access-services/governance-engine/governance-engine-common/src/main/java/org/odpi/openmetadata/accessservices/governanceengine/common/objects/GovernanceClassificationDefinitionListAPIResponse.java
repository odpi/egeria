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
public class GovernanceClassificationDefinitionListAPIResponse extends GovernanceEngineOMASAPIResponse {



    private List<GovernanceClassificationDefinition> GovernanceClassificationDefinitionList = null;

    /**
     * Default constructor
     */
    public GovernanceClassificationDefinitionListAPIResponse() {
    }


    public List<GovernanceClassificationDefinition> getTagList() {
        return GovernanceClassificationDefinitionList;
    }

    /**
     *
     * @param governanceClassificationDefinitionList salary to set (in cents)
     */
    public void setTagList(List<GovernanceClassificationDefinition> governanceClassificationDefinitionList) {
        this.GovernanceClassificationDefinitionList = governanceClassificationDefinitionList;
    }



    @Override
    public String toString() {

        return "TagMapResponse{" +
                "tagmap=" + GovernanceClassificationDefinitionList +
                ", relatedHTTPCode=" + relatedHTTPCode +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                '}';
    }
}
