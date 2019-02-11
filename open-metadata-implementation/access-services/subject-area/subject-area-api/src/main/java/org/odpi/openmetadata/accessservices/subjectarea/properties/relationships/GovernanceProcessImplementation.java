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

//omrs
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
//omrs beans
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.LineType;

/**
 * GovernanceProcessImplementation is a relationship between an entity of type GovernanceProcess and an entity of type Process.
 * The ends of the relationship are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has implementsGovernanceProcesses as the proxy name for entity type GovernanceProcess.
 * The second entity proxy has implementations as the proxy name for entity type Process.
 *
 * Each entity proxy also stores the entities guid.

 Identifies the implementation of a governance process.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceProcessImplementation extends Line {
    private static final Logger log = LoggerFactory.getLogger(GovernanceProcessImplementation.class);
    private static final String className = GovernanceProcessImplementation.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "notes",

      // Terminate the list
          null
      };
      public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
          "notes",

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
    protected String entity1Guid;
    protected String entity2Guid;


    public GovernanceProcessImplementation() {
        initialise();
    }

    private void initialise()
    {
       name = "GovernanceProcessImplementation";
       // set the LineType if this is a LineType enum value.
       try {
           lineType = LineType.valueOf(name);
        }
        catch (IllegalArgumentException e) {
           lineType = LineType.Unknown;
        }
        entity1Name = "implementsGovernanceProcesses";
        entity1Type = "GovernanceProcess";
        entity2Name = "implementations";
        entity2Type = "Process";
        typeDefGuid = "a5a7b08a-73fd-4026-a9dd-d0fe55bea8a4";
    }

    public GovernanceProcessImplementation(Line template) {
        super(template);
        initialise();
    }

    public GovernanceProcessImplementation(Relationship omrsRelationship) {
        super(omrsRelationship);
        name = "GovernanceProcessImplementation";
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
          MapPropertyValue mapPropertyValue=null;
          PrimitivePropertyValue primitivePropertyValue=null;
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("notes",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private String notes;
        /**
            * {@literal Documents reasons for implementing the process using this implementation. }
            * @return {@code String }
            */
         public String getNotes() {
             return this.notes;
         }
         public void setNotes(String notes)  {
            this.notes = notes;
        }

      @Override
         public StringBuilder toString(StringBuilder sb)
         {
             if (sb == null)
             {
                 sb = new StringBuilder();
             }
             sb.append(" GovernanceProcessImplementation=");
             sb.append(super.toString(sb));
             sb.append(" GovernanceProcessImplementation Attributes{");
             sb.append("notes=" + this.notes +",");
             sb.append("}");
             return sb;
         }
         @Override
         public String toString() {
             return toString(new StringBuilder()).toString();
         }


}
