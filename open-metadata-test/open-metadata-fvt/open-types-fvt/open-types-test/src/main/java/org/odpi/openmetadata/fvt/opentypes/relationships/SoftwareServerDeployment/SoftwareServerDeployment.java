/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.SoftwareServerDeployment;
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
 * SoftwareServerDeployment is a relationships between an entity of type SoftwareServerPlatform and an entity of type SoftwareServer.
 * The ends of the relationships are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has hostingPlatform as the proxy name for entity type SoftwareServerPlatform.
 * The second entity proxy has deployedSoftwareServers as the proxy name for entity type SoftwareServer.
 *
 * Each entity proxy also stores the entities guid.

 Defines the platform that a software server is deployed to.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SoftwareServerDeployment extends OMRSLine {
    private static final Logger log = LoggerFactory.getLogger(SoftwareServerDeployment.class);
    private static final String className = SoftwareServerDeployment.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "deploymentTime",
          "deployer",
          "serverStatus",

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
           "serverStatus",

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


    public SoftwareServerDeployment() {
        initialise();
    }

    private void initialise()
    {
       name = "SoftwareServerDeployment";
       entity1Name = "hostingPlatform";
       entity1Type = "SoftwareServerPlatform";
       entity2Name = "deployedSoftwareServers";
       entity2Type = "SoftwareServer";
       typeDefGuid = "d909eb3b-5205-4180-9f63-122a65b30738";
    }

    public SoftwareServerDeployment(OMRSLine template) {
        super(template);
        initialise();
    }
    public SoftwareServerDeployment(Line template) {
        super(template);
        initialise();
    }

    public SoftwareServerDeployment(Relationship omrsRelationship) {
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
          // the operational status of the software server on this platform.
          enumPropertyValue.setOrdinal(serverStatus.ordinal());
          enumPropertyValue.setSymbolicName(serverStatus.name());
          instanceProperties.setProperty("serverStatus",enumPropertyValue);
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
          instanceProperties.setProperty("serverStatus",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private Date deploymentTime;
        /**
            * {@literal Time that the software server was deployed to the platform. }
            * @return {@code Date }
            */
         public Date getDeploymentTime() {
             return this.deploymentTime;
         }
         public void setDeploymentTime(Date deploymentTime)  {
            this.deploymentTime = deploymentTime;
        }
         private String deployer;
        /**
            * {@literal Person, organization or engine that deployed the software server. }
            * @return {@code String }
            */
         public String getDeployer() {
             return this.deployer;
         }
         public void setDeployer(String deployer)  {
            this.deployer = deployer;
        }
         private OperationalStatus serverStatus;
        /**
            * {@literal The operational status of the software server on this platform. }
            * @return {@code OperationalStatus }
            */
         public OperationalStatus getServerStatus() {
             return this.serverStatus;
         }
         public void setServerStatus(OperationalStatus serverStatus)  {
            this.serverStatus = serverStatus;
        }

      @Override
         public StringBuilder toString(StringBuilder sb)
         {
             if (sb == null)
             {
                 sb = new StringBuilder();
             }
             sb.append(" SoftwareServerDeployment=");
             sb.append(super.toString(sb));
             sb.append(" SoftwareServerDeployment Attributes{");
             sb.append("deploymentTime=" + this.deploymentTime +",");
             sb.append("deployer=" + this.deployer +",");
             if ( serverStatus!=null) {
                 sb.append("serverStatus=" + serverStatus.name());
             }
             sb.append("}");
             return sb;
         }
         @Override
         public String toString() {
             return toString(new StringBuilder()).toString();
         }


}
