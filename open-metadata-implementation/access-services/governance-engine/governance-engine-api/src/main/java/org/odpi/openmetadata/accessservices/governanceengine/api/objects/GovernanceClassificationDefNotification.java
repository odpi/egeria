/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.api.objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GovernanceClassificationDefNotification extends GovernanceEngineNotificationBase {

    // handle changes by sending old and new values
    // CREATE - old value is null
    // DELETE - new value is null
    // UPDATE - old and new are set. Client needs to work out WHAT was changed
    private GovernanceClassificationDef oldGovernanceClassificationDef;
    private GovernanceClassificationDef newGovernanceClassificationDef;

    /**
     * @return oldGovernanceClassficationDefinition
     */
    public GovernanceClassificationDef getOldGovernanceClassificationDef() {
        return oldGovernanceClassificationDef;
    }

    /**
     *
     * @param oldGovernanceClassificationDef
     */
    public void setOldGovernanceClassificationDef(GovernanceClassificationDef oldGovernanceClassificationDef) {
        this.oldGovernanceClassificationDef = oldGovernanceClassificationDef;
    }

    /**
     * @return newGovernanceClassificationDef
     */
    public GovernanceClassificationDef getNewGovernanceClassificationDef() {
        return newGovernanceClassificationDef;
    }

    /**
     *
     * @param newGovernanceClassificationDef
     */
    public void setNewGovernanceClassificationDef(GovernanceClassificationDef newGovernanceClassificationDef) {
        this.newGovernanceClassificationDef = newGovernanceClassificationDef;
    }


}
