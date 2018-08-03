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
public class GovernanceClassificationDefAPIResponse extends GovernanceEngineOMASAPIResponse {



    private GovernanceClassificationDef GovernanceClassificationDef = null;

    /**
     * Default constructor
     */
    public GovernanceClassificationDefAPIResponse() {
    }


    public GovernanceClassificationDef getGovernanceClassificationDef() {
        return GovernanceClassificationDef;
    }

    /**
     *
     * @param governanceClassificationDef salary to set (in cents)
     */
    public void setGovernanceClassificationDef(GovernanceClassificationDef governanceClassificationDef) {
        this.GovernanceClassificationDef = governanceClassificationDef;
    }




}
