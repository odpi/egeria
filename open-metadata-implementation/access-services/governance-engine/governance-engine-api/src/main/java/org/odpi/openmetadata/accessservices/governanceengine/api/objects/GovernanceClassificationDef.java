/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.api.objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

// Used to store the governance classification, including a definition of optional attributes
public class GovernanceClassificationDef extends GovernanceClassificationBase {

    private Map<String, String> attributeDefinitions;

    /**
     * @return attributeDefinitions - String Map of attribute names, types
     */
    public Map<String, String> getAttributeDefinitions() {
        return attributeDefinitions;
    }

    /**
     * @param attributeDefinitions - String Map of attribute names, types
     */
    public void setAttributeDefinitions(Map<String, String> attributeDefinitions) {
        this.attributeDefinitions = attributeDefinitions;
    }

}

