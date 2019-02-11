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
 * Contributor is a relationship between an entity of type Referenceable and an entity of type ActorProfile.
 * The ends of the relationship are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has contributions as the proxy name for entity type Referenceable.
 * The second entity proxy has contributors as the proxy name for entity type ActorProfile.
 *
 * Each entity proxy also stores the entities guid.

 Defines one of the actors contributing content to a new description or asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Contributor extends Line {
    private static final Logger log = LoggerFactory.getLogger(Contributor.class);
    private static final String className = Contributor.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "roleType",

      // Terminate the list
          null
      };
      public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {

       // Terminate the list
          null
      };
      public static final String[] ENUM_NAMES_SET_VALUES = new String[] {
           "roleType",

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


    public Contributor() {
        initialise();
    }

    private void initialise()
    {
       name = "Contributor";
       // set the LineType if this is a LineType enum value.
       try {
           lineType = LineType.valueOf(name);
        }
        catch (IllegalArgumentException e) {
           lineType = LineType.Unknown;
        }
        entity1Name = "contributions";
        entity1Type = "Referenceable";
        entity2Name = "contributors";
        entity2Type = "ActorProfile";
        typeDefGuid = "4db83564-b200-4956-94a4-c95a5c30e65a";
    }

    public Contributor(Line template) {
        super(template);
        initialise();
    }

    public Contributor(Relationship omrsRelationship) {
        super(omrsRelationship);
        name = "Contributor";
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
          // type of contribution.
          enumPropertyValue.setOrdinal(roleType.ordinal());
          enumPropertyValue.setSymbolicName(roleType.name());
          instanceProperties.setProperty("roleType",enumPropertyValue);
          MapPropertyValue mapPropertyValue=null;
          PrimitivePropertyValue primitivePropertyValue=null;
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("roleType",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private CrowdSourcingRole roleType;
        /**
            * {@literal Type of contribution. }
            * @return {@code CrowdSourcingRole }
            */
         public CrowdSourcingRole getRoleType() {
             return this.roleType;
         }
         public void setRoleType(CrowdSourcingRole roleType)  {
            this.roleType = roleType;
        }

      @Override
         public StringBuilder toString(StringBuilder sb)
         {
             if (sb == null)
             {
                 sb = new StringBuilder();
             }
             sb.append(" Contributor=");
             sb.append(super.toString(sb));
             sb.append(" Contributor Attributes{");
             if ( roleType!=null) {
                 sb.append("roleType=" + roleType.name());
             }
             sb.append("}");
             return sb;
         }
         @Override
         public String toString() {
             return toString(new StringBuilder()).toString();
         }


}
