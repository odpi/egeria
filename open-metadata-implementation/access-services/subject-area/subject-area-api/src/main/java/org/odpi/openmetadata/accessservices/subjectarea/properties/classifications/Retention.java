/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.subjectarea.properties.classifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.*;

/**
 * Defines the retention requirements for related data items.
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Retention extends Classification {
    private static final Logger log = LoggerFactory.getLogger( Retention.class);
    private static final String className =  Retention.class.getName();
    private Map<String, String> extraAttributes;


    private static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
        "status",
        "confidence",
        "steward",
        "source",
        "notes",
        "basis",
        "associatedGUID",
        "archiveAfter",
        "deleteAfter",

    // Terminate the list
        null
    };
    private static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
        "confidence",
        "steward",
        "source",
        "notes",
        "associatedGUID",
        "archiveAfter",
        "deleteAfter",

     // Terminate the list
        null
    };
    private static final String[] ENUM_NAMES_SET_VALUES = new String[] {
         "status",
         "basis",

         // Terminate the list
          null
    };
    private static final String[] MAP_NAMES_SET_VALUES = new String[] {

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
    public Retention() {
            super.classificationName="Retention";
    }

       private GovernanceClassificationStatus status;
       /**
        * {@literal Status of this classification. }
        * @return Status of this classification.
        */
       public GovernanceClassificationStatus getStatus() {
           return this.status;
       }
       public void setStatus(GovernanceClassificationStatus status)  {
           this.status = status;
       }


       private Integer confidence;
       /**
        * {@literal Level of confidence in the classification (0=none -> 100=excellent). }
        * @return Level of confidence
        */
       public Integer getConfidence() {
           return this.confidence;
       }
       public void setConfidence(Integer confidence)  {
           this.confidence = confidence;
       }


       private String steward;
       /**
        * {@literal Person responsible for maintaining this classification. }
        * @return  Person responsible for maintaining this classification.
        */
       public String getSteward() {
           return this.steward;
       }
       public void setSteward(String steward)  {
           this.steward = steward;
       }


       private String source;
       /**
        * {@literal Source of the classification. }
        * @return Source of the classification.
        */
       public String getSource() {
           return this.source;
       }
       public void setSource(String source)  {
           this.source = source;
       }


       private String notes;
       /**
        * {@literal Information relating to the classification. }
        * @return Information relating to the classification.
        */
       public String getNotes() {
           return this.notes;
       }
       public void setNotes(String notes)  {
           this.notes = notes;
       }

    /**
     * Retention property names
     * @return Set of Strings of the Retention property names
     */
    public static Set<String> getPropertyNames() {
        return PROPERTY_NAMES_SET;
    }

       private RetentionBasis basis;
       /**
        * {@literal Basis on which the retention period is defined. }
        * @return Basis on which the retention period is defined.
        */
       public RetentionBasis getBasis() {
           return this.basis;
       }
       public void setBasis(RetentionBasis basis)  {
           this.basis = basis;
       }


       private String associatedGUID;
       /**
        * {@literal Related entity used to determine the retention period. }
        * @return Related entity used to determine the retention period.
        */
       public String getAssociatedGUID() {
           return this.associatedGUID;
       }
       public void setAssociatedGUID(String associatedGUID)  {
           this.associatedGUID = associatedGUID;
       }


       private Long archiveAfter;
       /**
        * {@literal Related entity used to determine the retention period. }
        * @return Related entity used to determine the retention period.
        */
       public Long getArchiveAfter() {
           return this.archiveAfter;
       }
       public void setArchiveAfter(Long archiveAfter)  {
           this.archiveAfter = archiveAfter;
       }


       private Long deleteAfter;
       /**
        * {@literal Related entity used to determine the retention period. }
        * @return  Related entity used to determine the retention period.
        */
       public Long getDeleteAfter() {
           return this.deleteAfter;
       }
       public void setDeleteAfter(Long deleteAfter)  {
           this.deleteAfter = deleteAfter;
       }



    /**
      * Get the extra attributes - ones that are in addition to the standard types.
      * @return extra attributes
      */
    public Map<String, String> getAdditionalProperties() {
          return extraAttributes;
    }
    public void setAdditionalProperties(Map<String, String> additionalProperties) {
          this.extraAttributes = additionalProperties;
    }
}
