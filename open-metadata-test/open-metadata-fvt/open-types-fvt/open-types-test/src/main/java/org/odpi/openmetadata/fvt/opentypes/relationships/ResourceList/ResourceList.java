/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.ResourceList;
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
 * ResourceList is a relationships between an entity of type Referenceable and an entity of type Referenceable.
 * The ends of the relationships are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has resourceListAnchors as the proxy name for entity type Referenceable.
 * The second entity proxy has supportingResources as the proxy name for entity type Referenceable.
 *
 * Each entity proxy also stores the entities guid.

 Links supporting resources to an anchor object (typically an Actor Profile, Project, Meeting or Community).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ResourceList extends OMRSLine {
    private static final Logger log = LoggerFactory.getLogger(ResourceList.class);
    private static final String className = ResourceList.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "resourceUse",
          "watchResource",

      // Terminate the list
          null
      };
      public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
          "resourceUse",
          "watchResource",

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


    public ResourceList() {
        initialise();
    }

    private void initialise()
    {
       name = "ResourceList";
       entity1Name = "resourceListAnchors";
       entity1Type = "Referenceable";
       entity2Name = "supportingResources";
       entity2Type = "Referenceable";
       typeDefGuid = "73cf5658-6a73-4ebc-8f4d-44fdfac0b437";
    }

    public ResourceList(OMRSLine template) {
        super(template);
        initialise();
    }
    public ResourceList(Line template) {
        super(template);
        initialise();
    }

    public ResourceList(Relationship omrsRelationship) {
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
          instanceProperties.setProperty("resourceUse",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("watchResource",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private String resourceUse;
        /**
            * {@literal Description of how the resource is used, or why it is useful. }
            * @return {@code String }
            */
         public String getResourceUse() {
             return this.resourceUse;
         }
         public void setResourceUse(String resourceUse)  {
            this.resourceUse = resourceUse;
        }
         private Boolean watchResource;
        /**
            * {@literal Indicator whether the anchor should receive notifications of changes to the resource. }
            * @return {@code Boolean }
            */
         public Boolean getWatchResource() {
             return this.watchResource;
         }
         public void setWatchResource(Boolean watchResource)  {
            this.watchResource = watchResource;
        }

      @Override
         public StringBuilder toString(StringBuilder sb)
         {
             if (sb == null)
             {
                 sb = new StringBuilder();
             }
             sb.append(" ResourceList=");
             sb.append(super.toString(sb));
             sb.append(" ResourceList Attributes{");
             sb.append("resourceUse=" + this.resourceUse +",");
             sb.append("watchResource=" + this.watchResource +",");
             sb.append("}");
             return sb;
         }
         @Override
         public String toString() {
             return toString(new StringBuilder()).toString();
         }


}
