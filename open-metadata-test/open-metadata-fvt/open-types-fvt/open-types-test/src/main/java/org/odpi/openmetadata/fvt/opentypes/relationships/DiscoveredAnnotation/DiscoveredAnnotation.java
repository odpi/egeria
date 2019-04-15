/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.DiscoveredAnnotation;
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
 * DiscoveredAnnotation is a relationships between an entity of type Annotation and an entity of type OpenDiscoveryAnalysisReport.
 * The ends of the relationships are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has reportedAnnotations as the proxy name for entity type Annotation.
 * The second entity proxy has fromAnalysisReport as the proxy name for entity type OpenDiscoveryAnalysisReport.
 *
 * Each entity proxy also stores the entities guid.

 The annotations that make up a discovery analysis report.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscoveredAnnotation extends OMRSLine {
    private static final Logger log = LoggerFactory.getLogger(DiscoveredAnnotation.class);
    private static final String className = DiscoveredAnnotation.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {

      // Terminate the list
          null
      };
      public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {

       // Terminate the list
          null
      };
      public static final String[] ENUM_NAMES_SET_VALUES = new String[] {

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


    public DiscoveredAnnotation() {
        initialise();
    }

    private void initialise()
    {
       name = "DiscoveredAnnotation";
       entity1Name = "reportedAnnotations";
       entity1Type = "Annotation";
       entity2Name = "fromAnalysisReport";
       entity2Type = "OpenDiscoveryAnalysisReport";
       typeDefGuid = "51d386a3-3857-42e3-a3df-14a6cad08b93";
    }

    public DiscoveredAnnotation(OMRSLine template) {
        super(template);
        initialise();
    }
    public DiscoveredAnnotation(Line template) {
        super(template);
        initialise();
    }

    public DiscoveredAnnotation(Relationship omrsRelationship) {
        super(omrsRelationship);
    }

    InstanceProperties obtainInstanceProperties() {
          final String methodName = "obtainInstanceProperties";
          if (log.isDebugEnabled()) {
                 log.debug("==> Method: " + methodName);
          }
          InstanceProperties instanceProperties = new InstanceProperties();
          EnumPropertyValue enumPropertyValue=null;
          MapPropertyValue mapPropertyValue=null;
          PrimitivePropertyValue primitivePropertyValue=null;
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }


      @Override
         public StringBuilder toString(StringBuilder sb)
         {
             if (sb == null)
             {
                 sb = new StringBuilder();
             }
             sb.append(" DiscoveredAnnotation=");
             sb.append(super.toString(sb));
             sb.append(" DiscoveredAnnotation Attributes{");
             sb.append("}");
             return sb;
         }
         @Override
         public String toString() {
             return toString(new StringBuilder()).toString();
         }


}
