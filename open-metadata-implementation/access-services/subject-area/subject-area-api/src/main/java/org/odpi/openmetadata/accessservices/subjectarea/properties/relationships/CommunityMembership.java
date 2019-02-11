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
 * CommunityMembership is a relationship between an entity of type Community and an entity of type ActorProfile.
 * The ends of the relationship are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has communities as the proxy name for entity type Community.
 * The second entity proxy has members as the proxy name for entity type ActorProfile.
 *
 * Each entity proxy also stores the entities guid.

 Associates an actor profile with a community.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CommunityMembership extends Line {
    private static final Logger log = LoggerFactory.getLogger(CommunityMembership.class);
    private static final String className = CommunityMembership.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "relationshipType",

      // Terminate the list
          null
      };
      public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {

       // Terminate the list
          null
      };
      public static final String[] ENUM_NAMES_SET_VALUES = new String[] {
           "relationshipType",

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


    public CommunityMembership() {
        initialise();
    }

    private void initialise()
    {
       name = "CommunityMembership";
       // set the LineType if this is a LineType enum value.
       try {
           lineType = LineType.valueOf(name);
        }
        catch (IllegalArgumentException e) {
           lineType = LineType.Unknown;
        }
        entity1Name = "communities";
        entity1Type = "Community";
        entity2Name = "members";
        entity2Type = "ActorProfile";
        typeDefGuid = "7c7da1a3-01b3-473e-972e-606eff0cb112";
    }

    public CommunityMembership(Line template) {
        super(template);
        initialise();
    }

    public CommunityMembership(Relationship omrsRelationship) {
        super(omrsRelationship);
        name = "CommunityMembership";
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
          // type of membership to the community.
          enumPropertyValue.setOrdinal(relationshipType.ordinal());
          enumPropertyValue.setSymbolicName(relationshipType.name());
          instanceProperties.setProperty("relationshipType",enumPropertyValue);
          MapPropertyValue mapPropertyValue=null;
          PrimitivePropertyValue primitivePropertyValue=null;
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("relationshipType",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private CommunityMembershipType relationshipType;
        /**
            * {@literal Type of membership to the community. }
            * @return {@code CommunityMembershipType }
            */
         public CommunityMembershipType getRelationshipType() {
             return this.relationshipType;
         }
         public void setRelationshipType(CommunityMembershipType relationshipType)  {
            this.relationshipType = relationshipType;
        }

      @Override
         public StringBuilder toString(StringBuilder sb)
         {
             if (sb == null)
             {
                 sb = new StringBuilder();
             }
             sb.append(" CommunityMembership=");
             sb.append(super.toString(sb));
             sb.append(" CommunityMembership Attributes{");
             if ( relationshipType!=null) {
                 sb.append("relationshipType=" + relationshipType.name());
             }
             sb.append("}");
             return sb;
         }
         @Override
         public String toString() {
             return toString(new StringBuilder()).toString();
         }


}
