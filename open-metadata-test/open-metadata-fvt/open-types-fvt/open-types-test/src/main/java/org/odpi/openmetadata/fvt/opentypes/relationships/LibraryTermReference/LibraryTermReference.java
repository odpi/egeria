/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.LibraryTermReference;
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
 * LibraryTermReference is a relationships between an entity of type GlossaryTerm and an entity of type ExternalGlossaryLink.
 * The ends of the relationships are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has localTerms as the proxy name for entity type GlossaryTerm.
 * The second entity proxy has externalGlossaryTerms as the proxy name for entity type ExternalGlossaryLink.
 *
 * Each entity proxy also stores the entities guid.

 Links a glossary term to a glossary term in an external glossary.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LibraryTermReference extends OMRSLine {
    private static final Logger log = LoggerFactory.getLogger(LibraryTermReference.class);
    private static final String className = LibraryTermReference.class.getName();

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


    public LibraryTermReference() {
        initialise();
    }

    private void initialise()
    {
       name = "LibraryTermReference";
       entity1Name = "localTerms";
       entity1Type = "GlossaryTerm";
       entity2Name = "externalGlossaryTerms";
       entity2Type = "ExternalGlossaryLink";
       typeDefGuid = "38c346e4-ddd2-42ef-b4aa-55d53c078d22";
    }

    public LibraryTermReference(OMRSLine template) {
        super(template);
        initialise();
    }
    public LibraryTermReference(Line template) {
        super(template);
        initialise();
    }

    public LibraryTermReference(Relationship omrsRelationship) {
        super(omrsRelationship);
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
            * {@literal Identifier of the corresponding element from the external glossary. }
            * @return {@code String }
            */
         public String getIdentifier() {
             return this.identifier;
         }
         public void setIdentifier(String identifier)  {
            this.identifier = identifier;
        }
         private String description;
        /**
            * {@literal Description of the corresponding element from the external glossary. }
            * @return {@code String }
            */
         public String getDescription() {
             return this.description;
         }
         public void setDescription(String description)  {
            this.description = description;
        }
         private String steward;
        /**
            * {@literal Person who established the link to the external glossary. }
            * @return {@code String }
            */
         public String getSteward() {
             return this.steward;
         }
         public void setSteward(String steward)  {
            this.steward = steward;
        }
         private Date lastVerified;
        /**
            * {@literal Date when this reference was last checked. }
            * @return {@code Date }
            */
         public Date getLastVerified() {
             return this.lastVerified;
         }
         public void setLastVerified(Date lastVerified)  {
            this.lastVerified = lastVerified;
        }

      @Override
         public StringBuilder toString(StringBuilder sb)
         {
             if (sb == null)
             {
                 sb = new StringBuilder();
             }
             sb.append(" LibraryTermReference=");
             sb.append(super.toString(sb));
             sb.append(" LibraryTermReference Attributes{");
             sb.append("identifier=" + this.identifier +",");
             sb.append("description=" + this.description +",");
             sb.append("steward=" + this.steward +",");
             sb.append("lastVerified=" + this.lastVerified +",");
             sb.append("}");
             return sb;
         }
         @Override
         public String toString() {
             return toString(new StringBuilder()).toString();
         }


}
