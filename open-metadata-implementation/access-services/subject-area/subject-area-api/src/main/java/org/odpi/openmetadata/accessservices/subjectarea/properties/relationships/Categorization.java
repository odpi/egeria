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
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.LineType;

/**
 * TermCategorizationRelationship is a relationship between an entity of type GlossaryCategory and an entity of type GlossaryTerm.
 * The ends of the relationship are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has categories as the proxy name for entity type GlossaryCategory.
 * The second entity proxy has terms as the proxy name for entity type GlossaryTerm.
 *
 * Each entity proxy also stores the entities guid.

 Links a glossary term into a glossary category.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Categorization extends Line {
    private static final Logger log = LoggerFactory.getLogger(Categorization.class);
    private static final String className = Categorization.class.getName();

      private static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "description",
          "status",

      // Terminate the list
          null
      };
      private static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
          "description",

       // Terminate the list
          null
      };
      private static final String[] ENUM_NAMES_SET_VALUES = new String[] {
           "status",

           // Terminate the list
            null
      };
      private static final String[] MAP_NAMES_SET_VALUES = new String[] {

           // Terminate the list
           null
      };
      private static final java.util.Set<String> PROPERTY_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(PROPERTY_NAMES_SET_VALUES)));
      private static final java.util.Set<String> ATTRIBUTE_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ATTRIBUTE_NAMES_SET_VALUES)));
      private static final java.util.Set<String> ENUM_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ENUM_NAMES_SET_VALUES)));
      private static final java.util.Set<String> MAP_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(MAP_NAMES_SET_VALUES)));
      private String categoryGuid;
      private String termGuid;


    public Categorization() {
        initialise();
    }

    private void initialise()
    {
       name = "TermCategorization";
       // set the LineType if this is a LineType enum value.
       try {
           lineType = LineType.valueOf(name);
        }
        catch (IllegalArgumentException e) {
           lineType = LineType.Unknown;
        }
        entity1Name = "categories";
        entity1Type = "GlossaryCategory";
        entity2Name = "terms";
        entity2Type = "GlossaryTerm";
        typeDefGuid = "696a81f5-ac60-46c7-b9fd-6979a1e7ad27";
    }

    public Categorization(Line template) {
        super(template);
        initialise();
    }

    public Categorization(Relationship omrsRelationship) {
        super(omrsRelationship);
        name = "TermCategorizationRelationship";
       // set the LineType if this is a LineType enum value.
       try {
           lineType = LineType.valueOf(name);
        }
        catch (IllegalArgumentException e) {
           lineType = LineType.Unknown;
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
          // status of the relationship.
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
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

    public String getCategoryGuid() {
        return categoryGuid;
    }

    public void setCategoryGuid(String categoryGuid) {
        this.categoryGuid = categoryGuid;
    }

    public String getTermGuid() {
        return termGuid;
    }

    public void setTermGuid(String termGuid) {
        this.termGuid = termGuid;
    }

    private String description;
        /**
            * {@literal Explanation of why this term is in this categorization. }
            * @return {@code String }
            */
         public String getDescription() {
             return this.description;
         }
         public void setDescription(String description)  {
            this.description = description;
        }
         private TermRelationshipStatus status;
        /**
            * {@literal Status of the relationship. }
            * @return {@code TermRelationshipStatus }
            */
         public TermRelationshipStatus getStatus() {
             return this.status;
         }
         public void setStatus(TermRelationshipStatus status)  {
            this.status = status;
        }

      @Override
         public StringBuilder toString(StringBuilder sb)
         {
             if (sb == null)
             {
                 sb = new StringBuilder();
             }
             sb.append(" TermCategorizationRelationship=");
             sb.append(super.toString(sb));
             sb.append(" TermCategorizationRelationship Attributes{");
             sb.append("description=" + this.description +",");
             if ( status!=null) {
                 sb.append("status=" + status.name());
             }
             sb.append("}");
             return sb;
         }
         @Override
         public String toString() {
             return toString(new StringBuilder()).toString();
         }


}
