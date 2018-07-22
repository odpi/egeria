/* SPDX-License-Identifier: Apache-2.0 */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.EmbeddedConnection;
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
 * EmbeddedConnection is a relationship between an entity of type VirtualConnection and an entity of type Connection.
 * The ends of the relationship are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has supportingVirtualConnections as the proxy name for entity type VirtualConnection.
 * The second entity proxy has embeddedConnections as the proxy name for entity type Connection.
 *
 * Each entity proxy also stores the entities guid.

 A link between a virtual connection and one of the connections it depends on.
 */
public class EmbeddedConnection extends Line {
    private static final Logger log = LoggerFactory.getLogger(EmbeddedConnection.class);
    private static final String className = EmbeddedConnection.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "displayName",
          "arguments",

      // Terminate the list
          null
      };
      public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
          "displayName",

       // Terminate the list
          null
      };
      public static final String[] ENUM_NAMES_SET_VALUES = new String[] {

           // Terminate the list
            null
      };
      public static final String[] MAP_NAMES_SET_VALUES = new String[] {
           "arguments",

           // Terminate the list
           null
      };
      public static final java.util.Set<String> PROPERTY_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(PROPERTY_NAMES_SET_VALUES)));
      public static final java.util.Set<String> ATTRIBUTE_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ATTRIBUTE_NAMES_SET_VALUES)));
      public static final java.util.Set<String> ENUM_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ENUM_NAMES_SET_VALUES)));
      public static final java.util.Set<String> MAP_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(MAP_NAMES_SET_VALUES)));


    
    public EmbeddedConnection() {
        super("EmbeddedConnection");
        super.entity1Name = "supportingVirtualConnections";
        super.entity1Type = "VirtualConnection";
        super.entity2Name = "embeddedConnections";
        super.entity2Type = "Connection";
    }

    public EmbeddedConnection(Relationship omrsRelationship) {
        super(omrsRelationship);

        if (!omrsRelationship.getEntityOnePropertyName().equals("supportingVirtualConnections")){
            //error
        }
        if (!omrsRelationship.getEntityTwoPropertyName().equals("embeddedConnections")){
            //error
        }
        if (!omrsRelationship.getEntityOneProxy().getType().getTypeDefName().equals("VirtualConnection")){
            //error
        }
        if (!omrsRelationship.getEntityTwoProxy().getType().getTypeDefName().equals("Connection")){
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
          // Additional arguments needed by the virtual connector when using each connection.
          mapPropertyValue = new MapPropertyValue();
          PrimitivePropertyValue primitivePropertyValue=null;
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("displayName",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("arguments",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private String displayName;
         /**
          * Name for the embedded connection.
          * @return String
          */
         public String getDisplayName() {
             return this.displayName;
         }
         public void setDisplayName(String displayName)  {
            this.displayName = displayName;
        }
         private Map<String,String> arguments;
         /**
          * Additional arguments needed by the virtual connector when using each connection.
          * @return Map<String,String>
          */
         public Map<String,String> getArguments() {
             return this.arguments;
         }
         public void setArguments(Map<String,String> arguments)  {
            this.arguments = arguments;
        }
}
