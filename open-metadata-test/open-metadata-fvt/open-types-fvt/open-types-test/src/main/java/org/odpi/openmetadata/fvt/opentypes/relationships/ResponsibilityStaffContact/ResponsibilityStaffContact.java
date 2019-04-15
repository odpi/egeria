/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.ResponsibilityStaffContact;
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
 * ResponsibilityStaffContact is a relationships between an entity of type GovernanceResponsibility and an entity of type ActorProfile.
 * The ends of the relationships are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has contactFor as the proxy name for entity type GovernanceResponsibility.
 * The second entity proxy has assignedStaff as the proxy name for entity type ActorProfile.
 *
 * Each entity proxy also stores the entities guid.

 Identifies a person, team or engine assigned to a governance responsibility.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ResponsibilityStaffContact extends OMRSLine {
    private static final Logger log = LoggerFactory.getLogger(ResponsibilityStaffContact.class);
    private static final String className = ResponsibilityStaffContact.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "context",

      // Terminate the list
          null
      };
      public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
          "context",

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


    public ResponsibilityStaffContact() {
        initialise();
    }

    private void initialise()
    {
       name = "ResponsibilityStaffContact";
       entity1Name = "contactFor";
       entity1Type = "GovernanceResponsibility";
       entity2Name = "assignedStaff";
       entity2Type = "ActorProfile";
       typeDefGuid = "49f2ecb5-6bf7-4324-9824-ac98d595c404";
    }

    public ResponsibilityStaffContact(OMRSLine template) {
        super(template);
        initialise();
    }
    public ResponsibilityStaffContact(Line template) {
        super(template);
        initialise();
    }

    public ResponsibilityStaffContact(Relationship omrsRelationship) {
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
          instanceProperties.setProperty("context",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private String context;
        /**
            * {@literal The context in which this person, team or engine is to be contacted. }
            * @return {@code String }
            */
         public String getContext() {
             return this.context;
         }
         public void setContext(String context)  {
            this.context = context;
        }

      @Override
         public StringBuilder toString(StringBuilder sb)
         {
             if (sb == null)
             {
                 sb = new StringBuilder();
             }
             sb.append(" ResponsibilityStaffContact=");
             sb.append(super.toString(sb));
             sb.append(" ResponsibilityStaffContact Attributes{");
             sb.append("context=" + this.context +",");
             sb.append("}");
             return sb;
         }
         @Override
         public String toString() {
             return toString(new StringBuilder()).toString();
         }


}
