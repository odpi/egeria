/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.subjectarea.properties.classifications;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.ConfidenceLevel;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.GovernanceClassificationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Defines the level of confidence that should be placed in the accuracy of related data items.
 */

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Confidence extends Classification {
    private static final Logger log = LoggerFactory.getLogger(Confidence.class);
    private static final String className = Confidence.class.getName();
    private Map<String, String> extraAttributes;


    private static final String[] PROPERTY_NAMES_SET_VALUES = new String[]{
            "status",
            "confidence",
            "steward",
            "source",
            "notes",
            "level",

            // Terminate the list
            null
    };
    private static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[]{
            "confidence",
            "steward",
            "source",
            "notes",

            // Terminate the list
            null
    };
    private static final String[] ENUM_NAMES_SET_VALUES = new String[]{
            "status",
            "level",

            // Terminate the list
            null
    };
    private static final String[] MAP_NAMES_SET_VALUES = new String[]{

            // Terminate the list
            null
    };
    // note the below definitions needs to be fully qualified
    private static final java.util.Set<String> PROPERTY_NAMES_SET = new HashSet<>(Arrays.asList(PROPERTY_NAMES_SET_VALUES));
    private static final java.util.Set<String> ATTRIBUTE_NAMES_SET = new HashSet<>(Arrays.asList(ATTRIBUTE_NAMES_SET_VALUES));
    private static final java.util.Set<String> ENUM_NAMES_SET = new HashSet<>(Arrays.asList(ENUM_NAMES_SET_VALUES));
    private static final java.util.Set<String> MAP_NAMES_SET = new HashSet<>(Arrays.asList(MAP_NAMES_SET_VALUES));

    /**
     * Default constructor
     */
    public Confidence() {
        super.classificationName = "Confidence";
    }

    private GovernanceClassificationStatus status;

    /**
     * {@literal Status of this classification. }
     *
     * @return Status of this classification.
     */
    public GovernanceClassificationStatus getStatus() {
        return this.status;
    }

    public void setStatus(GovernanceClassificationStatus status) {
        this.status = status;
    }


    private Integer confidence;

    /**
     * {@literal Level of confidence in the classification (0=none -> 100=excellent). }
     *
     * @return Level of confidence in the classification
     */
    public Integer getConfidence() {
        return this.confidence;
    }

    public void setConfidence(Integer confidence) {
        this.confidence = confidence;
    }


    private String steward;

    /**
     * {@literal Person responsible for maintaining this classification. }
     *
     * @return Person responsible for maintaining this classification.
     */
    public String getSteward() {
        return this.steward;
    }

    public void setSteward(String steward) {
        this.steward = steward;
    }


    private String source;

    /**
     * {@literal Source of the classification. }
     *
     * @return Source of the classification.
     */
    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    private String notes;

    /**
     * {@literal Information relating to the classification. }
     *
     * @return Information relating to the classification.
     */
    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    private ConfidenceLevel level;

    /**
     * {@literal Level of confidence in the quality of this data. }
     *
     * @return Level of confidence in the quality of this data.
     */
    public ConfidenceLevel getLevel() {
        return this.level;
    }

    public void setLevel(ConfidenceLevel level) {
        this.level = level;
    }

    /**
     * Confidence property names
     *
     * @return Set of Strings of the Confidence property names
     */
    public static Set<String> getPropertyNames() {
        return PROPERTY_NAMES_SET;
    }

    /**
     * Get the extra attributes - ones that are in addition to the standard types.
     *
     * @return extra attributes
     */
    public Map<String, String> getAdditionalProperties() {
        return extraAttributes;
    }

    public void setAdditionalProperties(Map<String, String> additionalProperties) {
        this.extraAttributes = additionalProperties;
    }
}