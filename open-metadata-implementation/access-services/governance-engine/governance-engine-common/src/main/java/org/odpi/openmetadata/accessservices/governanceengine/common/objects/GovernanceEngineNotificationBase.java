/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.common.objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.governanceengine.common.events.GovernanceEngineEventType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class GovernanceEngineNotificationBase {

    // handle changes by sending old and new values
    // CREATE - old value is null
    // DELETE - new value is null
    // UPDATE - old and new are set. Client needs to work out WHAT was changed
    //
    // We could Template so we can use different types - for now will be explicit
    private GovernanceEngineEventType op;

    /**
     * @return current salary (in cents, may be imaginary for weird employees)
     */
    public GovernanceEngineEventType getOp() {
        return op;
    }
    /**
     *
     * @param op - message type
     */
    public void setOp(GovernanceEngineEventType op) {
        this.op = op;
    }


}
