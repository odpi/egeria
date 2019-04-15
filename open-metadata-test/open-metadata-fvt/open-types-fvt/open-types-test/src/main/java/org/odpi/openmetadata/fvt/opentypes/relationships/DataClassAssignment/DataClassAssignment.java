/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.DataClassAssignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.*;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.odpi.openmetadata.fvt.opentypes.common.*;
import org.odpi.openmetadata.fvt.opentypes.enums.*;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

//omrs
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;

/**
 * DataClassAssignment is a relationships between an entity of type Referenceable and an entity of type DataClass.
 * The ends of the relationships are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has elementsAssignedToDataClass as the proxy name for entity type Referenceable.
 * The second entity proxy has dataClassesAssignedToElement as the proxy name for entity type DataClass.
 *
 * Each entity proxy also stores the entities guid.

 Links a data class to an asset or schema element to define its logical data type.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataClassAssignment extends OMRSLine {
    private static final Logger log = LoggerFactory.getLogger(DataClassAssignment.class);
    private static final String className = DataClassAssignment.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "method",
          "status",
          "partialMatch",
          "confidence",
          "threshold",
          "valueFrequency",
          "steward",
          "source",

      // Terminate the list
          null
      };
      public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
          "method",
          "partialMatch",
          "confidence",
          "threshold",
          "valueFrequency",
          "steward",
          "source",

       // Terminate the list
          null
      };
      public static final String[] ENUM_NAMES_SET_VALUES = new String[] {
           "status",

           // Terminate the list
            null
      };
      public static final String[] MAP_NAMES_SET_VALUES = new String[] {

           // Terminate the list
           null
      };
      public static final java.util.Set<String> PROPERTY_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(PROPERTY_NAMES_SET_VALUES)));
      public static final java.util.Set<String> ATTRIBUTE_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ATTRIBUTE_NAMES_SET_VALUES)));
      public static final java.util.Set<String> ENUM_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ENUM_NAMES_SET_VALUES)));
      public static final java.util.Set<String> MAP_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(MAP_NAMES_SET_VALUES)));


    public DataClassAssignment() {
        initialise();
    }

    private void initialise()
    {
       name = "DataClassAssignment";
       entity1Name = "elementsAssignedToDataClass";
       entity1Type = "Referenceable";
       entity2Name = "dataClassesAssignedToElement";
       entity2Type = "DataClass";
       typeDefGuid = "4df37335-7f0c-4ced-82df-3b2fd07be1bd";
    }

    public DataClassAssignment(OMRSLine template) {
        super(template);
        initialise();
    }
    public DataClassAssignment(Line template) {
        super(template);
        initialise();
    }

    public DataClassAssignment(Relationship omrsRelationship) {
        super(omrsRelationship);
    }

    InstanceProperties obtainInstanceProperties() {
          final String methodName = "obtainInstanceProperties";
          if (log.isDebugEnabled()) {
                 log.debug("==> Method: " + methodName);
          }
          InstanceProperties instanceProperties = new InstanceProperties();
          EnumPropertyValue enumPropertyValue=null;
          enumPropertyValue = new EnumPropertyValue();
          // the status of the relationship.
          enumPropertyValue.setOrdinal(status.ordinal());
          enumPropertyValue.setSymbolicName(status.name());
          instanceProperties.setProperty("status",enumPropertyValue);
          MapPropertyValue mapPropertyValue=null;
          PrimitivePropertyValue primitivePropertyValue=null;
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("method",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("status",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("partialMatch",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("confidence",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("threshold",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("valueFrequency",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("steward",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("source",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private String method;
        /**
            * {@literal Method used to identify data class. }
            * @return {@code String }
            */
         public String getMethod() {
             return this.method;
         }
         public void setMethod(String method)  {
            this.method = method;
        }
         private DataClassAssignmentStatus status;
        /**
            * {@literal The status of the relationship. }
            * @return {@code DataClassAssignmentStatus }
            */
         public DataClassAssignmentStatus getStatus() {
             return this.status;
         }
         public void setStatus(DataClassAssignmentStatus status)  {
            this.status = status;
        }
         private Boolean partialMatch;
        /**
            * {@literal Are there data values outside of the data class specification? }
            * @return {@code Boolean }
            */
         public Boolean getPartialMatch() {
             return this.partialMatch;
         }
         public void setPartialMatch(Boolean partialMatch)  {
            this.partialMatch = partialMatch;
        }
         private Integer confidence;
        /**
            * {@literal Level of confidence in the correctness of the data class assignment. }
            * @return {@code Integer }
            */
         public Integer getConfidence() {
             return this.confidence;
         }
         public void setConfidence(Integer confidence)  {
            this.confidence = confidence;
        }
         private Float threshold;
        /**
            * {@literal What was the threshold result used to determine that the data class matched. }
            * @return {@code Float }
            */
         public Float getThreshold() {
             return this.threshold;
         }
         public void setThreshold(Float threshold)  {
            this.threshold = threshold;
        }
         private Long valueFrequency;
        /**
            * {@literal How often does the data class specification match the data values. }
            * @return {@code Long }
            */
         public Long getValueFrequency() {
             return this.valueFrequency;
         }
         public void setValueFrequency(Long valueFrequency)  {
            this.valueFrequency = valueFrequency;
        }
         private String steward;
        /**
            * {@literal Person responsible for validating the data class assignment. }
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
            * {@literal Person, organization or automated process that created the data class assignment. }
            * @return {@code String }
            */
         public String getSource() {
             return this.source;
         }
         public void setSource(String source)  {
            this.source = source;
        }

      @Override
         public StringBuilder toString(StringBuilder sb)
         {
             if (sb == null)
             {
                 sb = new StringBuilder();
             }
             sb.append(" DataClassAssignment=");
             sb.append(super.toString(sb));
             sb.append(" DataClassAssignment Attributes{");
             sb.append("method=" + this.method +",");
             sb.append("partialMatch=" + this.partialMatch +",");
             sb.append("confidence=" + this.confidence +",");
             sb.append("threshold=" + this.threshold +",");
             sb.append("valueFrequency=" + this.valueFrequency +",");
             sb.append("steward=" + this.steward +",");
             sb.append("source=" + this.source +",");
             if ( status!=null) {
                 sb.append("status=" + status.name());
             }
             sb.append("}");
             return sb;
         }
         @Override
         public String toString() {
             return toString(new StringBuilder()).toString();
         }


}
