/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.governednode;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A Governance level is a type of Governance Action classification that has an associated GovernanceClassificationLevel.
 * These classifications can be higher or lower; this allows goverance rules to treat higher levels with more restrictively.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceLevel {
    private String classificationName =null;

    private String description=null;

    private String icon=null;

    private GovernanceClassificationLevel level=null;

    /**
     * The name of the goverance level classification
     * @return classification name
     */
    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

    /**
     * Governance level classification description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * The icon url associated with the governance level classification.
     * @return icon url
     */
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * The level of this classification.
     * @return level of classification
     */
    public GovernanceClassificationLevel getLevel() {
        return level;
    }

    public void setLevel(GovernanceClassificationLevel level) {
        this.level = level;
    }

}
