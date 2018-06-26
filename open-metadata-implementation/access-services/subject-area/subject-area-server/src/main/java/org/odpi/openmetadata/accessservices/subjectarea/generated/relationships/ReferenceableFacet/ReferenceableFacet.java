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
package org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReferenceableFacet;
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
 * ReferenceableFacet is a relationship between an entity of type Referenceable and an entity of type PropertyFacet.
 * The ends of the relationship are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has relatedEntity as the proxy name for entity type Referenceable.
 * The second entity proxy has facets as the proxy name for entity type PropertyFacet.
 *
 * Each entity proxy also stores the entities guid.

 Link between a property facet and the resource it relates to.
 */
public class ReferenceableFacet extends Line {
    private static final Logger log = LoggerFactory.getLogger(ReferenceableFacet.class);
    private static final String className = ReferenceableFacet.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "source",

      // Terminate the list
          null
      };
      public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
          "source",

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


    
    public ReferenceableFacet() {
        super("ReferenceableFacet");
        super.entity1Name = "relatedEntity";
        super.entity1Type = "Referenceable";
        super.entity2Name = "facets";
        super.entity2Type = "PropertyFacet";
    }

    public ReferenceableFacet(Relationship omrsRelationship) {
        super(omrsRelationship);

        if (!omrsRelationship.getEntityOnePropertyName().equals("relatedEntity")){
            //error
        }
        if (!omrsRelationship.getEntityTwoPropertyName().equals("facets")){
            //error
        }
        if (!omrsRelationship.getEntityOneProxy().getType().getTypeDefName().equals("Referenceable")){
            //error
        }
        if (!omrsRelationship.getEntityTwoProxy().getType().getTypeDefName().equals("PropertyFacet")){
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
          instanceProperties.setProperty("source",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private String source;
         /**
          * Source of this property facet.
          * @return String
          */
         public String getSource() {
             return this.source;
         }

         public void setSource(Object source) throws InvalidParameterException {
             // accept an object and cast to the appropriate type.

             final String methodName = "obtainInstanceProperties";
             try {
                   this.source = (String)source;
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
