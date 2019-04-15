/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.DiscoveryEngineReport;
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
 * DiscoveryEngineReport is a relationships between an entity of type OpenDiscoveryEngine and an entity of type OpenDiscoveryAnalysisReport.
 * The ends of the relationships are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has sourceDiscoveryEngine as the proxy name for entity type OpenDiscoveryEngine.
 * The second entity proxy has discoveryEngineAnalysisReports as the proxy name for entity type OpenDiscoveryAnalysisReport.
 *
 * Each entity proxy also stores the entities guid.

 A discovery analysis report created by a discovery engine.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscoveryEngineReport extends OMRSLine {
    private static final Logger log = LoggerFactory.getLogger(DiscoveryEngineReport.class);
    private static final String className = DiscoveryEngineReport.class.getName();

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


    public DiscoveryEngineReport() {
        initialise();
    }

    private void initialise()
    {
       name = "DiscoveryEngineReport";
       entity1Name = "sourceDiscoveryEngine";
       entity1Type = "OpenDiscoveryEngine";
       entity2Name = "discoveryEngineAnalysisReports";
       entity2Type = "OpenDiscoveryAnalysisReport";
       typeDefGuid = "2c318c3a-5dc2-42cd-a933-0087d852f67f";
    }

    public DiscoveryEngineReport(OMRSLine template) {
        super(template);
        initialise();
    }
    public DiscoveryEngineReport(Line template) {
        super(template);
        initialise();
    }

    public DiscoveryEngineReport(Relationship omrsRelationship) {
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
             sb.append(" DiscoveryEngineReport=");
             sb.append(super.toString(sb));
             sb.append(" DiscoveryEngineReport Attributes{");
             sb.append("}");
             return sb;
         }
         @Override
         public String toString() {
             return toString(new StringBuilder()).toString();
         }


}
