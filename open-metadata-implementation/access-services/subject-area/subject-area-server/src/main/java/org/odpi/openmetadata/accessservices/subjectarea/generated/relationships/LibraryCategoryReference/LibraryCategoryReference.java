/* SPDX-License-Identifier: Apache-2.0 */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.LibraryCategoryReference;
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
 * LibraryCategoryReference is a relationship between an entity of type GlossaryCategory and an entity of type ExternalGlossaryLink.
 * The ends of the relationship are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has localCategories as the proxy name for entity type GlossaryCategory.
 * The second entity proxy has externalGlossaryLinks as the proxy name for entity type ExternalGlossaryLink.
 *
 * Each entity proxy also stores the entities guid.

 Links a glossary category to a corresponding category in an external glossary.
 */
public class LibraryCategoryReference extends Line {
    private static final Logger log = LoggerFactory.getLogger(LibraryCategoryReference.class);
    private static final String className = LibraryCategoryReference.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "identifier",
          "description",
          "steward",
          "lastVerified",

      // Terminate the list
          null
      };
      public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
          "identifier",
          "description",
          "steward",
          "lastVerified",

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


    
    public LibraryCategoryReference() {
        super("LibraryCategoryReference");
        super.entity1Name = "localCategories";
        super.entity1Type = "GlossaryCategory";
        super.entity2Name = "externalGlossaryLinks";
        super.entity2Type = "ExternalGlossaryLink";
    }

    public LibraryCategoryReference(Relationship omrsRelationship) {
        super(omrsRelationship);

        if (!omrsRelationship.getEntityOnePropertyName().equals("localCategories")){
            //error
        }
        if (!omrsRelationship.getEntityTwoPropertyName().equals("externalGlossaryLinks")){
            //error
        }
        if (!omrsRelationship.getEntityOneProxy().getType().getTypeDefName().equals("GlossaryCategory")){
            //error
        }
        if (!omrsRelationship.getEntityTwoProxy().getType().getTypeDefName().equals("ExternalGlossaryLink")){
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
          instanceProperties.setProperty("identifier",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("description",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("steward",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("lastVerified",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private String identifier;
         /**
          * Identifier of the corresponding element from the external glossary.
          * @return String
          */
         public String getIdentifier() {
             return this.identifier;
         }
         public void setIdentifier(String identifier)  {
            this.identifier = identifier;
        }
         private String description;
         /**
          * Description of the corresponding element from the external glossary.
          * @return String
          */
         public String getDescription() {
             return this.description;
         }
         public void setDescription(String description)  {
            this.description = description;
        }
         private String steward;
         /**
          * Person who established the link to the external glossary.
          * @return String
          */
         public String getSteward() {
             return this.steward;
         }
         public void setSteward(String steward)  {
            this.steward = steward;
        }
         private Date lastVerified;
         /**
          * Date when this reference was last checked.
          * @return Date
          */
         public Date getLastVerified() {
             return this.lastVerified;
         }
         public void setLastVerified(Date lastVerified)  {
            this.lastVerified = lastVerified;
        }
}
