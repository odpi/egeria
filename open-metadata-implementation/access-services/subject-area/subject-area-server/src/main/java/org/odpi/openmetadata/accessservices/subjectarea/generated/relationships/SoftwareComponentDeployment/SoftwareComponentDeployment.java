/* SPDX-License-Identifier: Apache-2.0 */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.SoftwareComponentDeployment;
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
 * SoftwareComponentDeployment is a relationship between an entity of type SoftwareServerCapability and an entity of type DeployedSoftwareComponent.
 * The ends of the relationship are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has deploymentLocations as the proxy name for entity type SoftwareServerCapability.
 * The second entity proxy has deployedComponents as the proxy name for entity type DeployedSoftwareComponent.
 *
 * Each entity proxy also stores the entities guid.

 Identifies where a software component is deployed.
 */
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
        super("SoftwareComponentDeployment");
        super.entity1Name = "deploymentLocations";
        super.entity1Type = "SoftwareServerCapability";
        super.entity2Name = "deployedComponents";
        super.entity2Type = "DeployedSoftwareComponent";
    }

    public SoftwareComponentDeployment(Relationship omrsRelationship) {
        super(omrsRelationship);

        if (!omrsRelationship.getEntityOnePropertyName().equals("deploymentLocations")){
            //error
        }
        if (!omrsRelationship.getEntityTwoPropertyName().equals("deployedComponents")){
            //error
        }
        if (!omrsRelationship.getEntityOneProxy().getType().getTypeDefName().equals("SoftwareServerCapability")){
            //error
        }
        if (!omrsRelationship.getEntityTwoProxy().getType().getTypeDefName().equals("DeployedSoftwareComponent")){
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
          * Date that the component was last deployed.
          * @return String
          */
         public String getDeploymentDate() {
             return this.deploymentDate;
         }
         public void setDeploymentDate(String deploymentDate)  {
            this.deploymentDate = deploymentDate;
        }
         private String deploymentProcess;
         /**
          * Process used to deploy the component.
          * @return String
          */
         public String getDeploymentProcess() {
             return this.deploymentProcess;
         }
         public void setDeploymentProcess(String deploymentProcess)  {
            this.deploymentProcess = deploymentProcess;
        }
}
