/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.DiscoveryInvocationReport;
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
 * DiscoveryInvocationReport is a relationships between an entity of type OpenDiscoveryService and an entity of type OpenDiscoveryAnalysisReport.
 * The ends of the relationships are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has sourceDiscoveryServices as the proxy name for entity type OpenDiscoveryService.
 * The second entity proxy has serviceDiscoveryAnalysisReports as the proxy name for entity type OpenDiscoveryAnalysisReport.
 *
 * Each entity proxy also stores the entities guid.

 An analysis report from a discovery service.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscoveryInvocationReport extends OMRSLine {
    private static final Logger log = LoggerFactory.getLogger(DiscoveryInvocationReport.class);
    private static final String className = DiscoveryInvocationReport.class.getName();

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


    public DiscoveryInvocationReport() {
        initialise();
    }

    private void initialise()
    {
       name = "DiscoveryInvocationReport";
       entity1Name = "sourceDiscoveryServices";
       entity1Type = "OpenDiscoveryService";
       entity2Name = "serviceDiscoveryAnalysisReports";
       entity2Type = "OpenDiscoveryAnalysisReport";
       typeDefGuid = "1744d72b-903d-4273-9229-de20372a17e2";
    }

    public DiscoveryInvocationReport(OMRSLine template) {
        super(template);
        initialise();
    }
    public DiscoveryInvocationReport(Line template) {
        super(template);
        initialise();
    }

    public DiscoveryInvocationReport(Relationship omrsRelationship) {
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
             sb.append(" DiscoveryInvocationReport=");
             sb.append(super.toString(sb));
             sb.append(" DiscoveryInvocationReport Attributes{");
             sb.append("}");
             return sb;
         }
         @Override
         public String toString() {
             return toString(new StringBuilder()).toString();
         }


}
