/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.ExternalIdScope;
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
 * ExternalIdScope is a relationships between an entity of type Referenceable and an entity of type ExternalId.
 * The ends of the relationships are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has scopedTo as the proxy name for entity type Referenceable.
 * The second entity proxy has managedResources as the proxy name for entity type ExternalId.
 *
 * Each entity proxy also stores the entities guid.

 Places where an external identifier is recognized.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExternalIdScope extends OMRSLine {
    private static final Logger log = LoggerFactory.getLogger(ExternalIdScope.class);
    private static final String className = ExternalIdScope.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "description",

      // Terminate the list
          null
      };
      public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
          "description",

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


    public ExternalIdScope() {
        initialise();
    }

    private void initialise()
    {
       name = "ExternalIdScope";
       entity1Name = "scopedTo";
       entity1Type = "Referenceable";
       entity2Name = "managedResources";
       entity2Type = "ExternalId";
       typeDefGuid = "8c5b1415-2d1f-4190-ba6c-1fdd47f03269";
    }

    public ExternalIdScope(OMRSLine template) {
        super(template);
        initialise();
    }
    public ExternalIdScope(Line template) {
        super(template);
        initialise();
    }

    public ExternalIdScope(Relationship omrsRelationship) {
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
          instanceProperties.setProperty("description",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private String description;
        /**
            * {@literal Description of the relationship between the resources and the managing component. }
            * @return {@code String }
            */
         public String getDescription() {
             return this.description;
         }
         public void setDescription(String description)  {
            this.description = description;
        }

      @Override
         public StringBuilder toString(StringBuilder sb)
         {
             if (sb == null)
             {
                 sb = new StringBuilder();
             }
             sb.append(" ExternalIdScope=");
             sb.append(super.toString(sb));
             sb.append(" ExternalIdScope Attributes{");
             sb.append("description=" + this.description +",");
             sb.append("}");
             return sb;
         }
         @Override
         public String toString() {
             return toString(new StringBuilder()).toString();
         }


}
