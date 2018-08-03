/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.api.objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GovernedAssetNotification extends GovernanceEngineNotificationBase{


        // handle changes by sending old and new values
        // CREATE - old value is null
        // DELETE - new value is null
        // UPDATE - old and new are set. Client needs to work out WHAT was changed
        private GovernedAsset oldGovernedAsset;
        private GovernedAsset newGovernedAsset;

    /**
     * @return oldGovernedAsset
     */
        public GovernedAsset getOldGovernedAsset() {
            return oldGovernedAsset;
        }

    /**
     *
     * @param oldGovernedAsset
     */
        public void setOldGovernedAsset(GovernedAsset oldGovernedAsset) {
            this.oldGovernedAsset = oldGovernedAsset;
        }

    /**
     * @return newGovernedAsset
     */
        public GovernedAsset getNewGovernanceClassificationDefinition() {
            return newGovernedAsset;
        }

    /**
     *
     * @param newGovernedAsset
     */
        public void setNewGovernanceClassificationDefinition(GovernedAsset newGovernedAsset) {
            this.newGovernedAsset = newGovernedAsset;
        }



        private GovernedAsset asset;
}
