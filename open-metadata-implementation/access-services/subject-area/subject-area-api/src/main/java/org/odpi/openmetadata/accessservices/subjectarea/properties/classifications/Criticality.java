/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

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
 * Defines how critical the related data items are to the organization.
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Criticality extends Classification {
    private static final Logger log = LoggerFactory.getLogger( Criticality.class);
    private static final String className =  Criticality.class.getName();
    private Map<String, Object> extraAttributes;


 public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
        "status",
        "confidence",
        "steward",
        "source",
        "notes",
        "level",

    // Terminate the list
        null
    };
    public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
        "confidence",
        "steward",
        "source",
        "notes",

     // Terminate the list
        null
    };
    public static final String[] ENUM_NAMES_SET_VALUES = new String[] {
         "status",
         "level",

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
    public Criticality() {
            super.classificationName="Criticality";
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
        // how critical is this data to the organization.
        if (level !=null) {
            enumPropertyValue.setOrdinal(level.ordinal());
            enumPropertyValue.setSymbolicName(level.name());
            instanceProperties.setProperty("level",enumPropertyValue);
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
        if (log.isDebugEnabled()) {
               log.debug("<== Method: " + methodName);
        }
        return instanceProperties;
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
        * @return Level of confidence in the classification
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
        * @return Person responsible for maintaining this classification.
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


       private CriticalityLevel level;
       /**
        * {@literal How critical is this data to the organization. }
        * @return How critical is this data to the organization.
        */
       public CriticalityLevel getLevel() {
           return this.level;
       }
       public void setLevel(CriticalityLevel level)  {
           this.level = level;
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
