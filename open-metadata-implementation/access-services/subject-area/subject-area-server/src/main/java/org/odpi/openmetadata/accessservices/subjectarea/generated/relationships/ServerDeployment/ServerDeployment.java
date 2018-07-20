/* SPDX-License-Identifier: Apache-2.0 */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ServerDeployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.*;

//omrs
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
//omrs beans
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.Line;

/**
 * ServerDeployment is a relationship between an entity of type Host and an entity of type SoftwareServer.
 * The ends of the relationship are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has host as the proxy name for entity type Host.
 * The second entity proxy has deployedServers as the proxy name for entity type SoftwareServer.
 *
 * Each entity proxy also stores the entities guid.

 Defines the host that a software server is deployed to.
 */
public class ServerDeployment extends Line {
    private static final Logger log = LoggerFactory.getLogger(ServerDeployment.class);
    private static final String className = ServerDeployment.class.getName();

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


    
    public ServerDeployment() {
        super("ServerDeployment");
        super.entity1Name = "host";
        super.entity1Type = "Host";
        super.entity2Name = "deployedServers";
        super.entity2Type = "SoftwareServer";
    }

    public ServerDeployment(Relationship omrsRelationship) {
        super(omrsRelationship);

        if (!omrsRelationship.getEntityOnePropertyName().equals("host")){
            //error
        }
        if (!omrsRelationship.getEntityTwoPropertyName().equals("deployedServers")){
            //error
        }
        if (!omrsRelationship.getEntityOneProxy().getType().getTypeDefName().equals("Host")){
            //error
        }
        if (!omrsRelationship.getEntityTwoProxy().getType().getTypeDefName().equals("SoftwareServer")){
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
          // the operational status of the software server on this host.
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
          * Time that the software server was deployed to the host.
          * @return Date
          */
         public Date getDeploymentTime() {
             return this.deploymentTime;
         }
         public void setDeploymentTime(Date deploymentTime)  {
            this.deploymentTime = deploymentTime;
        }
         private String deployer;
         /**
          * Person, organization or engine that deployed the software server.
          * @return String
          */
         public String getDeployer() {
             return this.deployer;
         }
         public void setDeployer(String deployer)  {
            this.deployer = deployer;
        }
         private OperationalStatus serverCapabilityStatus;
         /**
          * The operational status of the software server on this host.
          * @return OperationalStatus
          */
         public OperationalStatus getServerCapabilityStatus() {
             return this.serverCapabilityStatus;
         }
         public void setServerCapabilityStatus(OperationalStatus serverCapabilityStatus)  {
            this.serverCapabilityStatus = serverCapabilityStatus;
        }
}
