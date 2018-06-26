/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.GovernanceResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

//omrs
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;

//omas
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.generated.enums.*;



/**
 * GovernanceResults is a relationship between an entity of type GovernanceMetric and an entity of type DataSet.
 * The ends of the relationship are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has metrics as the proxy name for entity type GovernanceMetric.
 * The second entity proxy has measurements as the proxy name for entity type DataSet.
 *
 * Each entity proxy also stores the entities guid.

 Link between a governance metric and a data set used to gather measurements from the landscape.
 */
public class GovernanceResults extends Line {
    private static final Logger log = LoggerFactory.getLogger(GovernanceResults.class);
    private static final String className = GovernanceResults.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "query",

      // Terminate the list
          null
      };
      public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
          "query",

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


    
    public GovernanceResults() {
        super("GovernanceResults");
        super.entity1Name = "metrics";
        super.entity1Type = "GovernanceMetric";
        super.entity2Name = "measurements";
        super.entity2Type = "DataSet";
    }

    public GovernanceResults(Relationship omrsRelationship) {
        super(omrsRelationship);

        if (!omrsRelationship.getEntityOnePropertyName().equals("metrics")){
            //error
        }
        if (!omrsRelationship.getEntityTwoPropertyName().equals("measurements")){
            //error
        }
        if (!omrsRelationship.getEntityOneProxy().getType().getTypeDefName().equals("GovernanceMetric")){
            //error
        }
        if (!omrsRelationship.getEntityTwoProxy().getType().getTypeDefName().equals("DataSet")){
            //error
        }
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
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("query",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private String query;
         /**
          * Defines how the data items from the data set are converted in measurements for the metric.
          * @return String
          */
         public String getQuery() {
             return this.query;
         }

         public void setQuery(Object query) throws InvalidParameterException {
             // accept an object and cast to the appropriate type.

             final String methodName = "obtainInstanceProperties";
             try {
                   this.query = (String)query;
             } catch (ClassCastException e) {
                    SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.SET_ATTRIBUTE_WRONG_TYPE;
                    String errorMessage = errorCode.getErrorMessageId()
                            + errorCode.getFormattedErrorMessage(className,
                            methodName);
                    log.error(errorMessage,e);
                    throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                            className,
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());

             }
         }
}
