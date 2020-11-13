/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.subjectarea.properties.classifications;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A uniquely identifying relational column.
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PrimaryKey extends Classification {
    private static final Logger log = LoggerFactory.getLogger( PrimaryKey.class);
    private static final String className =  PrimaryKey.class.getName();
    private Map<String, String> extraAttributes;


    private static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
        "keyPattern",
        "name",

    // Terminate the list
        null
    };
    private static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
        "name",

     // Terminate the list
        null
    };
    private static final String[] ENUM_NAMES_SET_VALUES = new String[] {
         "keyPattern",

         // Terminate the list
          null
    };
    private static final String[] MAP_NAMES_SET_VALUES = new String[] {

         // Terminate the list
         null
    };
    // note the below definitions needs to be fully qualified
    private static final java.util.Set<String> PROPERTY_NAMES_SET = new HashSet<>(Arrays.asList(PROPERTY_NAMES_SET_VALUES));
    private static final java.util.Set<String> ATTRIBUTE_NAMES_SET = new HashSet<>(Arrays.asList(ATTRIBUTE_NAMES_SET_VALUES));
    private static final java.util.Set<String> ENUM_NAMES_SET = new HashSet<>(Arrays.asList(ENUM_NAMES_SET_VALUES));
    private static final java.util.Set<String> MAP_NAMES_SET = new HashSet<>(Arrays.asList(MAP_NAMES_SET_VALUES));
    /**
     * Default constructor
     */
    public PrimaryKey() {
            super.classificationName="PrimaryKey";
    }

       private KeyPattern keyPattern;
       /**
        * {@literal Type of primary key. }
        * @return Key Pattern
        */
       public KeyPattern getKeyPattern() {
           return this.keyPattern;
       }
       public void setKeyPattern(KeyPattern keyPattern)  {
           this.keyPattern = keyPattern;
       }


       private String name;
       /**
        * {@literal Display name for the primary key. }
        * @return Display name for the primary key.
        */
       public String getName() {
           return this.name;
       }
       public void setName(String name)  {
           this.name = name;
       }



    /**
      * Get the extra attributes - ones that are in addition to the standard types.
      * @return extra attributes
      */
    public Map<String, String> getAdditionalProperties() {
          return extraAttributes;
    }
    public void setAdditionalProperties(Map<String, String> additionalProperties) {
          this.extraAttributes = additionalProperties;
    }
}
