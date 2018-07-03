/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.common.objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class GovernanceClassificationDefinitionNotification extends GovernanceEngineNotificationBase {

    // handle changes by sending old and new values
    // CREATE - old value is null
    // DELETE - new value is null
    // UPDATE - old and new are set. Client needs to work out WHAT was changed
    private GovernanceClassificationDefinition oldGovernanceClassificationDefinition;
    private GovernanceClassificationDefinition newGovernanceClassificationDefinition;

    /**
     * @return oldGovernanceClassficationDefinition
     */
    public GovernanceClassificationDefinition getOldGovernanceClassificationDefinition() {
        return oldGovernanceClassificationDefinition;
    }

    /**
     *
     * @param oldGovernanceClassificationDefinition
     */
    public void setOldGovernanceClassificationDefinition(GovernanceClassificationDefinition oldGovernanceClassificationDefinition) {
        this.oldGovernanceClassificationDefinition = oldGovernanceClassificationDefinition;
    }

    /**
     * @return newGovernanceClassificationDefinition
     */
    public GovernanceClassificationDefinition getNewGovernanceClassificationDefinition() {
        return newGovernanceClassificationDefinition;
    }

    /**
     *
     * @param newGovernanceClassificationDefinition
     */
    public void setNewGovernanceClassificationDefinition(GovernanceClassificationDefinition newGovernanceClassificationDefinition) {
        this.newGovernanceClassificationDefinition = newGovernanceClassificationDefinition;
    }


}
