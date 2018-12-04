/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.properties.relationships;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.*;

//omrs
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
//omrs beans
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.LineType;

/**
 * ServerSupportedCapability is a relationship between an entity of type SoftwareServer and an entity of type SoftwareServerCapability.
 * The ends of the relationship are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has servers as the proxy name for entity type SoftwareServer.
 * The second entity proxy has capabilities as the proxy name for entity type SoftwareServerCapability.
 *
 * Each entity proxy also stores the entities guid.

 Identifies a software capability that is deployed to a software server.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
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
    protected String entity1Guid;
    protected String entity2Guid;


    public ServerSupportedCapability() {
        initialise();
    }

    private void initialise()
    {
       name = "ServerSupportedCapability";
       // set the LineType if this is a LineType enum value.
       try {
           lineType = LineType.valueOf(name);
        }
        catch (IllegalArgumentException e) {
           lineType = LineType.Other;
        }
        entity1Name = "servers";
        entity1Type = "SoftwareServer";
        entity2Name = "capabilities";
        entity2Type = "SoftwareServerCapability";
        typeDefGuid = "8b7d7da5-0668-4174-a43b-8f8c6c068dd0";
    }

    public ServerSupportedCapability(Line template) {
        super(template);
        initialise();
    }

    public ServerSupportedCapability(Relationship omrsRelationship) {
        super(omrsRelationship);
        name = "ServerSupportedCapability";
       // set the LineType if this is a LineType enum value.
       try {
           lineType = LineType.valueOf(name);
        }
        catch (IllegalArgumentException e) {
           lineType = LineType.Other;
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
            * {@literal Time that the software server capability was deployed to the software server. }
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
            * {@literal Person, organization or engine that deployed the software server capability. }
            * @return {@code String }
            */
         public String getDeployer() {
             return this.deployer;
         }
         public void setDeployer(String deployer)  {
            this.deployer = deployer;
        }
         private OperationalStatus serverCapabilityStatus;
        /**
            * {@literal The operational status of the software server capability on this software server. }
            * @return {@code OperationalStatus }
            */
         public OperationalStatus getServerCapabilityStatus() {
             return this.serverCapabilityStatus;
         }
         public void setServerCapabilityStatus(OperationalStatus serverCapabilityStatus)  {
            this.serverCapabilityStatus = serverCapabilityStatus;
        }

      @Override
         public StringBuilder toString(StringBuilder sb)
         {
             if (sb == null)
             {
                 sb = new StringBuilder();
             }
             sb.append(" ServerSupportedCapability=");
             sb.append(super.toString(sb));
             sb.append(" ServerSupportedCapability Attributes{");
             sb.append("deploymentTime=" + this.deploymentTime +",");
             sb.append("deployer=" + this.deployer +",");
             if ( serverCapabilityStatus!=null) {
                 sb.append("serverCapabilityStatus=" + serverCapabilityStatus.name());
             }
             sb.append("}");
             return sb;
         }
         @Override
         public String toString() {
             return toString(new StringBuilder()).toString();
         }


}
