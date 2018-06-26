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
package org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ServerSupportedCapability;
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
 * ServerSupportedCapability is a relationship between an entity of type SoftwareServer and an entity of type SoftwareServerCapability.
 * The ends of the relationship are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has servers as the proxy name for entity type SoftwareServer.
 * The second entity proxy has capabilities as the proxy name for entity type SoftwareServerCapability.
 *
 * Each entity proxy also stores the entities guid.

 Identifies a software capability that is deployed to a software server.
 */
public class ServerSupportedCapability extends Line {
    private static final Logger log = LoggerFactory.getLogger(ServerSupportedCapability.class);
    private static final String className = ServerSupportedCapability.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "deploymentTime",
          "deployer",
          "serverCapabilityStatus",

      // Terminate the list
          null
      };
      public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
          "deploymentTime",
          "deployer",

       // Terminate the list
          null
      };
      public static final String[] ENUM_NAMES_SET_VALUES = new String[] {
           "serverCapabilityStatus",

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


    
    public ServerSupportedCapability() {
        super("ServerSupportedCapability");
        super.entity1Name = "servers";
        super.entity1Type = "SoftwareServer";
        super.entity2Name = "capabilities";
        super.entity2Type = "SoftwareServerCapability";
    }

    public ServerSupportedCapability(Relationship omrsRelationship) {
        super(omrsRelationship);

        if (!omrsRelationship.getEntityOnePropertyName().equals("servers")){
            //error
        }
        if (!omrsRelationship.getEntityTwoPropertyName().equals("capabilities")){
            //error
        }
        if (!omrsRelationship.getEntityOneProxy().getType().getTypeDefName().equals("SoftwareServer")){
            //error
        }
        if (!omrsRelationship.getEntityTwoProxy().getType().getTypeDefName().equals("SoftwareServerCapability")){
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
          enumPropertyValue = new EnumPropertyValue();
          // the operational status of the software server capability on this software server.
          enumPropertyValue.setOrdinal(serverCapabilityStatus.ordinal());
          enumPropertyValue.setSymbolicName(serverCapabilityStatus.name());
          instanceProperties.setProperty("serverCapabilityStatus",enumPropertyValue);
          MapPropertyValue mapPropertyValue=null;
          PrimitivePropertyValue primitivePropertyValue=null;
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("deploymentTime",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("deployer",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("serverCapabilityStatus",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private Date deploymentTime;
         /**
          * Time that the software server capability was deployed to the software server.
          * @return Date
          */
         public Date getDeploymentTime() {
             return this.deploymentTime;
         }

         public void setDeploymentTime(Object deploymentTime) throws InvalidParameterException {
             // accept an object and cast to the appropriate type.

             final String methodName = "obtainInstanceProperties";
             try {
                   this.deploymentTime = (Date)deploymentTime;
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
         private String deployer;
         /**
          * Person, organization or engine that deployed the software server capability.
          * @return String
          */
         public String getDeployer() {
             return this.deployer;
         }

         public void setDeployer(Object deployer) throws InvalidParameterException {
             // accept an object and cast to the appropriate type.

             final String methodName = "obtainInstanceProperties";
             try {
                   this.deployer = (String)deployer;
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
         private OperationalStatus serverCapabilityStatus;
         /**
          * The operational status of the software server capability on this software server.
          * @return OperationalStatus
          */
         public OperationalStatus getServerCapabilityStatus() {
             return this.serverCapabilityStatus;
         }

         public void setServerCapabilityStatus(Object serverCapabilityStatus) throws InvalidParameterException {
             // accept an object and cast to the appropriate type.

             final String methodName = "obtainInstanceProperties";
             try {
                   this.serverCapabilityStatus = (OperationalStatus)serverCapabilityStatus;
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
