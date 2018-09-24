/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.subjectarea.properties.classifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.MapPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;

import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    private Map<String, Object> extraAttributes;


 public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
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
    public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
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
    public static final String[] ENUM_NAMES_SET_VALUES = new String[] {
         "status",
         "basis",

         // Terminate the list
          null
    };
    public static final String[] MAP_NAMES_SET_VALUES = new String[] {

         // Terminate the list
         null
    };
    // note the below definitions needs to be fully qualified
    public static final java.util.Set<String> PROPERTY_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(PROPERTY_NAMES_SET_VALUES)));
    public static final java.util.Set<String> ATTRIBUTE_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ATTRIBUTE_NAMES_SET_VALUES)));
    public static final java.util.Set<String> ENUM_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ENUM_NAMES_SET_VALUES)));
    public static final java.util.Set<String> MAP_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(MAP_NAMES_SET_VALUES)));
    /**
     * Default constructor
     */
    public Retention() {
            super.classificationName="Retention";
    }
    @Override
    public InstanceProperties obtainInstanceProperties() {
        final String methodName = "obtainInstanceProperties";
        if (log.isDebugEnabled()) {
               log.debug("==> Method: " + methodName);
        }
        InstanceProperties instanceProperties = new InstanceProperties();
        EnumPropertyValue enumPropertyValue=null;
        enumPropertyValue = new EnumPropertyValue();
        // status of this classification.
        if (status !=null) {
            enumPropertyValue.setOrdinal(status.ordinal());
            enumPropertyValue.setSymbolicName(status.name());
            instanceProperties.setProperty("status",enumPropertyValue);
        }
        enumPropertyValue = new EnumPropertyValue();
        // basis on which the retention period is defined.
        if (basis !=null) {
            enumPropertyValue.setOrdinal(basis.ordinal());
            enumPropertyValue.setSymbolicName(basis.name());
            instanceProperties.setProperty("basis",enumPropertyValue);
        }
        MapPropertyValue mapPropertyValue=null;
        PrimitivePropertyValue primitivePropertyValue=null;
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(confidence);
        instanceProperties.setProperty("confidence",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(steward);
        instanceProperties.setProperty("steward",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(source);
        instanceProperties.setProperty("source",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(notes);
        instanceProperties.setProperty("notes",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(associatedGUID);
        instanceProperties.setProperty("associatedGUID",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(archiveAfter);
        instanceProperties.setProperty("archiveAfter",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(deleteAfter);
        instanceProperties.setProperty("deleteAfter",primitivePropertyValue);
        if (log.isDebugEnabled()) {
               log.debug("<== Method: " + methodName);
        }
        return instanceProperties;
    }

       private GovernanceClassificationStatus status;
       /**
        * {@literal Status of this classification. }
        * @return {@code GovernanceClassificationStatus }
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
        * @return {@code Integer }
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
        * @return {@code String }
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
        * @return {@code String }
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
        * @return {@code String }
        */
       public String getNotes() {
           return this.notes;
       }
       public void setNotes(String notes)  {
           this.notes = notes;
       }


       private RetentionBasis basis;
       /**
        * {@literal Basis on which the retention period is defined. }
        * @return {@code RetentionBasis }
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
        * @return {@code String }
        */
       public String getAssociatedGUID() {
           return this.associatedGUID;
       }
       public void setAssociatedGUID(String associatedGUID)  {
           this.associatedGUID = associatedGUID;
       }


       private Date archiveAfter;
       /**
        * {@literal Related entity used to determine the retention period. }
        * @return {@code Date }
        */
       public Date getArchiveAfter() {
           return this.archiveAfter;
       }
       public void setArchiveAfter(Date archiveAfter)  {
           this.archiveAfter = archiveAfter;
       }


       private Date deleteAfter;
       /**
        * {@literal Related entity used to determine the retention period. }
        * @return {@code Date }
        */
       public Date getDeleteAfter() {
           return this.deleteAfter;
       }
       public void setDeleteAfter(Date deleteAfter)  {
           this.deleteAfter = deleteAfter;
       }



    /**
      * Get the extra attributes - ones that are in addition to the standard types.
      * @return extra attributes
      */
    public Map<String, Object> getExtraAttributes() {
          return extraAttributes;
    }
    public void setExtraAttributes(Map<String, Object> extraAttributes) {
          this.extraAttributes = extraAttributes;
    }
}
