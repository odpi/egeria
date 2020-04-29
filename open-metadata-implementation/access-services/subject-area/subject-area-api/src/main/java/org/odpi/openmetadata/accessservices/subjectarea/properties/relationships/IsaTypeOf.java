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
 * Defines an inheritance relationship between two spine objects.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IsaTypeOf extends Line {
    private static final Logger log = LoggerFactory.getLogger(IsaTypeOf.class);
    private static final String className = IsaTypeOf.class.getName();

      private static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "description",
          "status",
          "steward",
          "source",

      // Terminate the list
          null
      };
      private static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
          "description",
          "steward",
          "source",

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
      private String superTypeGuid;
      private String subTypeGuid;


    public IsaTypeOf() {
        initialise();
    }

    private void initialise()
    {
       name = "IsaTypeOf";
       // set the LineType if this is a LineType enum value.
       try {
           lineType = LineType.valueOf(name);
        }
        catch (IllegalArgumentException e) {
           lineType = LineType.Unknown;
        }
        entity1Name = "supertypes";
        entity1Type = "GlossaryTerm";
        entity2Name = "subtypes";
        entity2Type = "GlossaryTerm";
        typeDefGuid = "d5d588c3-46c9-420c-adff-6031802a7e51";
    }

    public IsaTypeOf(Line template) {
        super(template);
        initialise();
    }

    public IsaTypeOf(Relationship omrsRelationship) {
        super(omrsRelationship);
        name = "IsaTypeOf";
       // set the LineType if this is a LineType enum value.
       try {
           lineType = LineType.valueOf(name);
        }
        catch (IllegalArgumentException e) {
           lineType = LineType.Unknown;
        }
    }
    /**
     * {@literal Get the guid of type associated with the super type spine object - this is the spine object that is inherited from. }
     * @return {@code String }
     */
    public String getSuperTypeGuid()
    {
        return superTypeGuid;
    }

    public void setSuperTypeGuid(String superTypeGuid)
    {
        this.superTypeGuid = superTypeGuid;
    }
    /**
     * {@literal Get the guid of type associated with the sub type spine object - this is the spine object that inherits (specialises). }
     * @return {@code String }
     */
    public String getSubTypeGuid()
    {
        return subTypeGuid;
    }

    public void setSubTypeGuid(String subTypeGuid)
    {
        this.subTypeGuid = subTypeGuid;
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
          
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("description",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("status",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("steward",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("source",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private String description;
        /**
            * {@literal Description of the relationship. }
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
            * {@literal The status of or confidence in the relationship. }
            * @return {@code TermRelationshipStatus }
            */
         public TermRelationshipStatus getStatus() {
             return this.status;
         }
         public void setStatus(TermRelationshipStatus status)  {
            this.status = status;
        }
         private String steward;
        /**
            * {@literal Person responsible for the relationship. }
            * @return {@code String }
            */
         public String getSteward() {
             return this.steward;
         }
         public void setSteward(String steward)  {
            this.steward = steward;
        }
         private String source;
        /**
            * {@literal Person, organization or automated process that created the relationship. }
            * @return {@code String }
            */
         public String getSource() {
             return this.source;
         }
         public void setSource(String source)  {
            this.source = source;
        }

      @Override
         public StringBuilder toString(StringBuilder sb)
         {
             if (sb == null)
             {
                 sb = new StringBuilder();
             }
             sb.append(" IsaTypeOf=");
             sb.append(super.toString(sb));
             sb.append(" IsaTypeOf Attributes{");
             sb.append("description=" + this.description +",");
             sb.append("steward=" + this.steward +",");
             sb.append("source=" + this.source +",");
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
