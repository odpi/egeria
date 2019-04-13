/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.SoftwareServerPlatformDeployment;
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
 * SoftwareServerPlatformDeployment is a relationships between an entity of type Host and an entity of type SoftwareServerPlatform.
 * The ends of the relationships are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has host as the proxy name for entity type Host.
 * The second entity proxy has deployedServerPlatforms as the proxy name for entity type SoftwareServerPlatform.
 *
 * Each entity proxy also stores the entities guid.

 Defines the host that a software server platform is deployed to.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SoftwareServerPlatformDeployment extends OMRSLine {
    private static final Logger log = LoggerFactory.getLogger(SoftwareServerPlatformDeployment.class);
    private static final String className = SoftwareServerPlatformDeployment.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "deploymentTime",
          "deployer",
          "platformStatus",

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
           "platformStatus",

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


    public SoftwareServerPlatformDeployment() {
        initialise();
    }

    private void initialise()
    {
       name = "SoftwareServerPlatformDeployment";
       entity1Name = "host";
       entity1Type = "Host";
       entity2Name = "deployedServerPlatforms";
       entity2Type = "SoftwareServerPlatform";
       typeDefGuid = "b909eb3b-5205-4180-9f63-122a65b30738";
    }

    public SoftwareServerPlatformDeployment(OMRSLine template) {
        super(template);
        initialise();
    }
    public SoftwareServerPlatformDeployment(Line template) {
        super(template);
        initialise();
    }

    public SoftwareServerPlatformDeployment(Relationship omrsRelationship) {
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
          // the operational status of the software server platform on this host.
          enumPropertyValue.setOrdinal(platformStatus.ordinal());
          enumPropertyValue.setSymbolicName(platformStatus.name());
          instanceProperties.setProperty("platformStatus",enumPropertyValue);
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
          instanceProperties.setProperty("platformStatus",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private Date deploymentTime;
        /**
            * {@literal Time that the software server platform was deployed to the host. }
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
            * {@literal Person, organization or engine that deployed the software server platform. }
            * @return {@code String }
            */
         public String getDeployer() {
             return this.deployer;
         }
         public void setDeployer(String deployer)  {
            this.deployer = deployer;
        }
         private OperationalStatus platformStatus;
        /**
            * {@literal The operational status of the software server platform on this host. }
            * @return {@code OperationalStatus }
            */
         public OperationalStatus getPlatformStatus() {
             return this.platformStatus;
         }
         public void setPlatformStatus(OperationalStatus platformStatus)  {
            this.platformStatus = platformStatus;
        }

      @Override
         public StringBuilder toString(StringBuilder sb)
         {
             if (sb == null)
             {
                 sb = new StringBuilder();
             }
             sb.append(" SoftwareServerPlatformDeployment=");
             sb.append(super.toString(sb));
             sb.append(" SoftwareServerPlatformDeployment Attributes{");
             sb.append("deploymentTime=" + this.deploymentTime +",");
             sb.append("deployer=" + this.deployer +",");
             if ( platformStatus!=null) {
                 sb.append("platformStatus=" + platformStatus.name());
             }
             sb.append("}");
             return sb;
         }
         @Override
         public String toString() {
             return toString(new StringBuilder()).toString();
         }


}
