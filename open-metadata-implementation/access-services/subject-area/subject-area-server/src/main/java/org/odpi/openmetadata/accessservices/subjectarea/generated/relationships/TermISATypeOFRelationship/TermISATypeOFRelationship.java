/* SPDX-License-Identifier: Apache-2.0 */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermISATypeOFRelationship;
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
 * TermISATypeOFRelationship is a relationship between an entity of type GlossaryTerm and an entity of type GlossaryTerm.
 * The ends of the relationship are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has supertypes as the proxy name for entity type GlossaryTerm.
 * The second entity proxy has subtypes as the proxy name for entity type GlossaryTerm.
 *
 * Each entity proxy also stores the entities guid.

 Defines an inheritance relationship between two spine objects.
 */
public class TermISATypeOFRelationship extends Line {
    private static final Logger log = LoggerFactory.getLogger(TermISATypeOFRelationship.class);
    private static final String className = TermISATypeOFRelationship.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "description",
          "status",
          "steward",
          "source",

      // Terminate the list
          null
      };
      public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
          "description",
          "steward",
          "source",

       // Terminate the list
          null
      };
      public static final String[] ENUM_NAMES_SET_VALUES = new String[] {
           "status",

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


    
    public TermISATypeOFRelationship() {
        super("TermISATypeOFRelationship");
        super.entity1Name = "supertypes";
        super.entity1Type = "GlossaryTerm";
        super.entity2Name = "subtypes";
        super.entity2Type = "GlossaryTerm";
    }

    public TermISATypeOFRelationship(Relationship omrsRelationship) {
        super(omrsRelationship);

        if (!omrsRelationship.getEntityOnePropertyName().equals("supertypes")){
            //error
        }
        if (!omrsRelationship.getEntityTwoPropertyName().equals("subtypes")){
            //error
        }
        if (!omrsRelationship.getEntityOneProxy().getType().getTypeDefName().equals("GlossaryTerm")){
            //error
        }
        if (!omrsRelationship.getEntityTwoProxy().getType().getTypeDefName().equals("GlossaryTerm")){
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
          // the status of or confidence in the relationship.
          enumPropertyValue.setOrdinal(status.ordinal());
          enumPropertyValue.setSymbolicName(status.name());
          instanceProperties.setProperty("status",enumPropertyValue);
          MapPropertyValue mapPropertyValue=null;
          PrimitivePropertyValue primitivePropertyValue=null;
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("description",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("status",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("steward",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("source",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private String description;
         /**
          * Description of the relationship.
          * @return String
          */
         public String getDescription() {
             return this.description;
         }
         public void setDescription(String description)  {
            this.description = description;
        }
         private TermRelationshipStatus status;
         /**
          * The status of or confidence in the relationship.
          * @return TermRelationshipStatus
          */
         public TermRelationshipStatus getStatus() {
             return this.status;
         }
         public void setStatus(TermRelationshipStatus status)  {
            this.status = status;
        }
         private String steward;
         /**
          * Person responsible for the relationship.
          * @return String
          */
         public String getSteward() {
             return this.steward;
         }
         public void setSteward(String steward)  {
            this.steward = steward;
        }
         private String source;
         /**
          * Person, organization or automated process that created the relationship.
          * @return String
          */
         public String getSource() {
             return this.source;
         }
         public void setSource(String source)  {
            this.source = source;
        }
}
