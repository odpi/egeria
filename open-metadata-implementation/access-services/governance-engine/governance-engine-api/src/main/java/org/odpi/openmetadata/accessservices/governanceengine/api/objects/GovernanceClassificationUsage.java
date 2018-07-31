/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.api.objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

// We use this to manage objects associated with the classification of an asset component
// for example confidentiality=C3
@EqualsAndHashCode(callSuper=true)
public class GovernanceClassificationUsage extends GovernanceClassificationBase {

    // Additional Attributes of a Tag Definition

    private Map<String,String> AttributeValues;  // key-value pair of attribute definitions (name, type)

    /**
     *
     * @param attributeValues - key/value pairs for additional objects of Classification
     */
    public void setAttributeValues(Map <String,String> attributeValues) {
        this.AttributeValues = attributeValues;
    }

    /**
     * @return attributeValues - key/value pairs for additional objects of Classification
     */
    public Map<String,String> getAttributevalues() {
        return AttributeValues;
    }

}

