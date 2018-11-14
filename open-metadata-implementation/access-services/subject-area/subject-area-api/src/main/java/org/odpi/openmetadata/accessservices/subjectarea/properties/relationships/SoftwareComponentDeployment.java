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
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.LineType;

/**
 * SoftwareComponentDeployment is a relationship between an entity of type SoftwareServerCapability and an entity of type DeployedSoftwareComponent.
 * The ends of the relationship are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has deploymentLocations as the proxy name for entity type SoftwareServerCapability.
 * The second entity proxy has deployedComponents as the proxy name for entity type DeployedSoftwareComponent.
 *
 * Each entity proxy also stores the entities guid.

 Identifies where a software component is deployed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SoftwareComponentDeployment extends Line {
    private static final Logger log = LoggerFactory.getLogger(SoftwareComponentDeployment.class);
    private static final String className = SoftwareComponentDeployment.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "deploymentDate",
          "deploymentProcess",

      // Terminate the list
          null
      };
      public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
          "deploymentDate",
          "deploymentProcess",

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


    public SoftwareComponentDeployment() {
        initialise();
    }

    private void initialise()
    {
       name = "SoftwareComponentDeployment";
       // set the LineType if this is a LineType enum value.
       try {
           lineType = LineType.valueOf(name);
        }
        catch (IllegalArgumentException e) {
           lineType = LineType.Other;
        }
        entity1Name = "deploymentLocations";
        entity1Type = "SoftwareServerCapability";
        entity2Name = "deployedComponents";
        entity2Type = "DeployedSoftwareComponent";
        typeDefGuid = "de2d7f2e-1759-44e3-b8a6-8af53e8fb0ee";
    }

    public SoftwareComponentDeployment(Line template) {
        super(template);
        initialise();
    }

    public SoftwareComponentDeployment(Relationship omrsRelationship) {
        super(omrsRelationship);
        name = "SoftwareComponentDeployment";
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
          MapPropertyValue mapPropertyValue=null;
          PrimitivePropertyValue primitivePropertyValue=null;
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("deploymentDate",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("deploymentProcess",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private String deploymentDate;
        /**
            * {@literal Date that the component was last deployed. }
            * @return {@code String }
            */
         public String getDeploymentDate() {
             return this.deploymentDate;
         }
         public void setDeploymentDate(String deploymentDate)  {
            this.deploymentDate = deploymentDate;
        }
         private String deploymentProcess;
        /**
            * {@literal Process used to deploy the component. }
            * @return {@code String }
            */
         public String getDeploymentProcess() {
             return this.deploymentProcess;
         }
         public void setDeploymentProcess(String deploymentProcess)  {
            this.deploymentProcess = deploymentProcess;
        }

      @Override
         public StringBuilder toString(StringBuilder sb)
         {
             if (sb == null)
             {
                 sb = new StringBuilder();
             }
             sb.append(" SoftwareComponentDeployment=");
             sb.append(super.toString(sb));
             sb.append(" SoftwareComponentDeployment Attributes{");
             sb.append("deploymentDate=" + this.deploymentDate +",");
             sb.append("deploymentProcess=" + this.deploymentProcess +",");
             sb.append("}");
             return sb;
         }
         @Override
         public String toString() {
             return toString(new StringBuilder()).toString();
         }


}
