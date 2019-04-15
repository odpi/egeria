/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.SchemaTypeDefinition;
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
 * SchemaTypeDefinition is a relationships between an entity of type SchemaAnalysisAnnotation and an entity of type SchemaType.
 * The ends of the relationships are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has deployedSchemaTypes as the proxy name for entity type SchemaAnalysisAnnotation.
 * The second entity proxy has schemaTypeDefinition as the proxy name for entity type SchemaType.
 *
 * Each entity proxy also stores the entities guid.

 Link between schema analysis annotation and the identified schema type definition.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SchemaTypeDefinition extends OMRSLine {
    private static final Logger log = LoggerFactory.getLogger(SchemaTypeDefinition.class);
    private static final String className = SchemaTypeDefinition.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "assetGUID",

      // Terminate the list
          null
      };
      public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
          "assetGUID",

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


    public SchemaTypeDefinition() {
        initialise();
    }

    private void initialise()
    {
       name = "SchemaTypeDefinition";
       entity1Name = "deployedSchemaTypes";
       entity1Type = "SchemaAnalysisAnnotation";
       entity2Name = "schemaTypeDefinition";
       entity2Type = "SchemaType";
       typeDefGuid = "60f2d263-e24d-4f20-8c0d-b5e24648cd54";
    }

    public SchemaTypeDefinition(OMRSLine template) {
        super(template);
        initialise();
    }
    public SchemaTypeDefinition(Line template) {
        super(template);
        initialise();
    }

    public SchemaTypeDefinition(Relationship omrsRelationship) {
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
          instanceProperties.setProperty("assetGUID",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private String assetGUID;
        /**
            * {@literal Unique identifier for the analyzed asset. }
            * @return {@code String }
            */
         public String getAssetGUID() {
             return this.assetGUID;
         }
         public void setAssetGUID(String assetGUID)  {
            this.assetGUID = assetGUID;
        }

      @Override
         public StringBuilder toString(StringBuilder sb)
         {
             if (sb == null)
             {
                 sb = new StringBuilder();
             }
             sb.append(" SchemaTypeDefinition=");
             sb.append(super.toString(sb));
             sb.append(" SchemaTypeDefinition Attributes{");
             sb.append("assetGUID=" + this.assetGUID +",");
             sb.append("}");
             return sb;
         }
         @Override
         public String toString() {
             return toString(new StringBuilder()).toString();
         }


}
