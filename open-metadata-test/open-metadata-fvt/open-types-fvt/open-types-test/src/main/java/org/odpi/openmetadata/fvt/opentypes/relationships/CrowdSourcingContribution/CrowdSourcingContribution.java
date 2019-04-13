/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.CrowdSourcingContribution;
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
 * CrowdSourcingContribution is a relationships between an entity of type Referenceable and an entity of type CrowdSourcingContributor.
 * The ends of the relationships are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has contributions as the proxy name for entity type Referenceable.
 * The second entity proxy has contributors as the proxy name for entity type CrowdSourcingContributor.
 *
 * Each entity proxy also stores the entities guid.

 Defines one of the actors contributing content to a new description or asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CrowdSourcingContribution extends OMRSLine {
    private static final Logger log = LoggerFactory.getLogger(CrowdSourcingContribution.class);
    private static final String className = CrowdSourcingContribution.class.getName();

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


    public CrowdSourcingContribution() {
        initialise();
    }

    private void initialise()
    {
       name = "CrowdSourcingContribution";
       entity1Name = "contributions";
       entity1Type = "Referenceable";
       entity2Name = "contributors";
       entity2Type = "CrowdSourcingContributor";
       typeDefGuid = "4db83564-b200-4956-94a4-c95a5c30e65a";
    }

    public CrowdSourcingContribution(OMRSLine template) {
        super(template);
        initialise();
    }
    public CrowdSourcingContribution(Line template) {
        super(template);
        initialise();
    }

    public CrowdSourcingContribution(Relationship omrsRelationship) {
        super(omrsRelationship);
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
             sb.append(" CrowdSourcingContribution=");
             sb.append(super.toString(sb));
             sb.append(" CrowdSourcingContribution Attributes{");
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
