/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.RelationshipAnnotation;
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
 * RelationshipAnnotation is a relationships between an entity of type Referenceable and an entity of type Referenceable.
 * The ends of the relationships are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has relatedFromObjectAnnotations as the proxy name for entity type Referenceable.
 * The second entity proxy has relatedToObjectAnnotations as the proxy name for entity type Referenceable.
 *
 * Each entity proxy also stores the entities guid.

 Annotation relating two referenceables.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationshipAnnotation extends OMRSLine {
    private static final Logger log = LoggerFactory.getLogger(RelationshipAnnotation.class);
    private static final String className = RelationshipAnnotation.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "annotationType",
          "summary",
          "confidenceLevel",
          "expression",
          "explanation",
          "analysisStep",
          "jsonProperties",
          "additionalProperties",
          "annotationStatus",

      // Terminate the list
          null
      };
      public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
          "annotationType",
          "summary",
          "confidenceLevel",
          "expression",
          "explanation",
          "analysisStep",
          "jsonProperties",

       // Terminate the list
          null
      };
      public static final String[] ENUM_NAMES_SET_VALUES = new String[] {
           "annotationStatus",

           // Terminate the list
            null
      };
      public static final String[] MAP_NAMES_SET_VALUES = new String[] {
           "additionalProperties",

           // Terminate the list
           null
      };
      public static final java.util.Set<String> PROPERTY_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(PROPERTY_NAMES_SET_VALUES)));
      public static final java.util.Set<String> ATTRIBUTE_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ATTRIBUTE_NAMES_SET_VALUES)));
      public static final java.util.Set<String> ENUM_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ENUM_NAMES_SET_VALUES)));
      public static final java.util.Set<String> MAP_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(MAP_NAMES_SET_VALUES)));


    public RelationshipAnnotation() {
        initialise();
    }

    private void initialise()
    {
       name = "RelationshipAnnotation";
       entity1Name = "relatedFromObjectAnnotations";
       entity1Type = "Referenceable";
       entity2Name = "relatedToObjectAnnotations";
       entity2Type = "Referenceable";
       typeDefGuid = "73510abd-49e6-4097-ba4b-23bd3ef15baa";
    }

    public RelationshipAnnotation(OMRSLine template) {
        super(template);
        initialise();
    }
    public RelationshipAnnotation(Line template) {
        super(template);
        initialise();
    }

    public RelationshipAnnotation(Relationship omrsRelationship) {
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
          // status of the processing as a result of the annotation.
          enumPropertyValue.setOrdinal(annotationStatus.ordinal());
          enumPropertyValue.setSymbolicName(annotationStatus.name());
          instanceProperties.setProperty("annotationStatus",enumPropertyValue);
          MapPropertyValue mapPropertyValue=null;
          // Additional properties discovered during the analysis.
          mapPropertyValue = new MapPropertyValue();
          PrimitivePropertyValue primitivePropertyValue=null;
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("annotationType",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("summary",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("confidenceLevel",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("expression",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("explanation",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("analysisStep",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("jsonProperties",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("additionalProperties",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("annotationStatus",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private String annotationType;
        /**
            * {@literal Name of the type of annotation. }
            * @return {@code String }
            */
         public String getAnnotationType() {
             return this.annotationType;
         }
         public void setAnnotationType(String annotationType)  {
            this.annotationType = annotationType;
        }
         private String summary;
        /**
            * {@literal Description of the findings. }
            * @return {@code String }
            */
         public String getSummary() {
             return this.summary;
         }
         public void setSummary(String summary)  {
            this.summary = summary;
        }
         private Integer confidenceLevel;
        /**
            * {@literal Level of certainty in the accuracy of the results. }
            * @return {@code Integer }
            */
         public Integer getConfidenceLevel() {
             return this.confidenceLevel;
         }
         public void setConfidenceLevel(Integer confidenceLevel)  {
            this.confidenceLevel = confidenceLevel;
        }
         private String expression;
        /**
            * {@literal Expression used to create the annotation. }
            * @return {@code String }
            */
         public String getExpression() {
             return this.expression;
         }
         public void setExpression(String expression)  {
            this.expression = expression;
        }
         private String explanation;
        /**
            * {@literal Explanation of the analysis. }
            * @return {@code String }
            */
         public String getExplanation() {
             return this.explanation;
         }
         public void setExplanation(String explanation)  {
            this.explanation = explanation;
        }
         private String analysisStep;
        /**
            * {@literal The step in the pipeline that produced the annotation. }
            * @return {@code String }
            */
         public String getAnalysisStep() {
             return this.analysisStep;
         }
         public void setAnalysisStep(String analysisStep)  {
            this.analysisStep = analysisStep;
        }
         private String jsonProperties;
        /**
            * {@literal Additional properties used in the specification. }
            * @return {@code String }
            */
         public String getJsonProperties() {
             return this.jsonProperties;
         }
         public void setJsonProperties(String jsonProperties)  {
            this.jsonProperties = jsonProperties;
        }
         private Map<String,String> additionalProperties;
        /**
            * {@literal Additional properties discovered during the analysis. }
            * @return {@code {@code Map<String,String> } }
            */
         public Map<String,String> getAdditionalProperties() {
             return this.additionalProperties;
         }
         public void setAdditionalProperties(Map<String,String> additionalProperties)  {
            this.additionalProperties = additionalProperties;
        }
         private AnnotationStatus annotationStatus;
        /**
            * {@literal Status of the processing as a result of the annotation. }
            * @return {@code AnnotationStatus }
            */
         public AnnotationStatus getAnnotationStatus() {
             return this.annotationStatus;
         }
         public void setAnnotationStatus(AnnotationStatus annotationStatus)  {
            this.annotationStatus = annotationStatus;
        }

      @Override
         public StringBuilder toString(StringBuilder sb)
         {
             if (sb == null)
             {
                 sb = new StringBuilder();
             }
             sb.append(" RelationshipAnnotation=");
             sb.append(super.toString(sb));
             sb.append(" RelationshipAnnotation Attributes{");
             sb.append("annotationType=" + this.annotationType +",");
             sb.append("summary=" + this.summary +",");
             sb.append("confidenceLevel=" + this.confidenceLevel +",");
             sb.append("expression=" + this.expression +",");
             sb.append("explanation=" + this.explanation +",");
             sb.append("analysisStep=" + this.analysisStep +",");
             sb.append("jsonProperties=" + this.jsonProperties +",");
             if ( annotationStatus!=null) {
                 sb.append("annotationStatus=" + annotationStatus.name());
             }
             sb.append("}");
             return sb;
         }
         @Override
         public String toString() {
             return toString(new StringBuilder()).toString();
         }


}
