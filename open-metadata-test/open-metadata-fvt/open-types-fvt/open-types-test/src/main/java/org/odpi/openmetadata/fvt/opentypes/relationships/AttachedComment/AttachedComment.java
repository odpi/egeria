/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.AttachedComment;
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
 * AttachedComment is a relationships between an entity of type Referenceable and an entity of type Comment.
 * The ends of the relationships are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has commentAnchor as the proxy name for entity type Referenceable.
 * The second entity proxy has comments as the proxy name for entity type Comment.
 *
 * Each entity proxy also stores the entities guid.

 Links a comment to an item, or another comment.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AttachedComment extends OMRSLine {
    private static final Logger log = LoggerFactory.getLogger(AttachedComment.class);
    private static final String className = AttachedComment.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "isPublic",

      // Terminate the list
          null
      };
      public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
          "isPublic",

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


    public AttachedComment() {
        initialise();
    }

    private void initialise()
    {
       name = "AttachedComment";
       entity1Name = "commentAnchor";
       entity1Type = "Referenceable";
       entity2Name = "comments";
       entity2Type = "Comment";
       typeDefGuid = "0d90501b-bf29-4621-a207-0c8c953bdac9";
    }

    public AttachedComment(OMRSLine template) {
        super(template);
        initialise();
    }
    public AttachedComment(Line template) {
        super(template);
        initialise();
    }

    public AttachedComment(Relationship omrsRelationship) {
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
          instanceProperties.setProperty("isPublic",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private Boolean isPublic;
        /**
            * {@literal Is the attached comment visible to more than the originator? }
            * @return {@code Boolean }
            */
         public Boolean getIsPublic() {
             return this.isPublic;
         }
         public void setIsPublic(Boolean isPublic)  {
            this.isPublic = isPublic;
        }

      @Override
         public StringBuilder toString(StringBuilder sb)
         {
             if (sb == null)
             {
                 sb = new StringBuilder();
             }
             sb.append(" AttachedComment=");
             sb.append(super.toString(sb));
             sb.append(" AttachedComment Attributes{");
             sb.append("isPublic=" + this.isPublic +",");
             sb.append("}");
             return sb;
         }
         @Override
         public String toString() {
             return toString(new StringBuilder()).toString();
         }


}
