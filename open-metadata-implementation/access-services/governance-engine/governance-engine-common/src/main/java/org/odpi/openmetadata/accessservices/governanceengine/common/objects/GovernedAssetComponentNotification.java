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
public class GovernedAssetComponentNotification extends GovernanceEngineNotificationBase{


        // handle changes by sending old and new values
        // CREATE - old value is null
        // DELETE - new value is null
        // UPDATE - old and new are set. Client needs to work out WHAT was changed
        private GovernedAssetComponent OldGovernedAssetComponent;
        private GovernedAssetComponent NewGovernedAssetComponent;

    /**
     * @return OldGovernedAssetComponent
     */
        public GovernedAssetComponent getOldGovernedAssetComponent() {
            return OldGovernedAssetComponent;
        }

    /**
     *
     * @param oldGovernedAssetComponent
     */
        public void setOldGovernedAssetComponent(GovernedAssetComponent oldGovernedAssetComponent) {
            this.OldGovernedAssetComponent = oldGovernedAssetComponent;
        }

    /**
     * @return NewGovernedAssetComponent
     */
        public GovernedAssetComponent getNewGovernanceClassificationDefinition() {
            return NewGovernedAssetComponent;
        }

    /**
     *
     * @param newGovernedAssetComponent
     */
        public void setNewGovernanceClassificationDefinition(GovernedAssetComponent newGovernedAssetComponent) {
            this.NewGovernedAssetComponent = newGovernedAssetComponent;
        }



        private GovernedAssetComponent asset;
}
