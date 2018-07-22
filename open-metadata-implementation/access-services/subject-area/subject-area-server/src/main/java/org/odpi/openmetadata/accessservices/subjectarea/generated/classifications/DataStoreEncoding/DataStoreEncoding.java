/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.DataStoreEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.MapPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;

import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

// uncomment to generate a class that json serialises
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.fasterxml.jackson.annotation.JsonProperty;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.*;

/**
 * Description for how data is organized and represented in a data store.
 */
 // uncomment to generate a class that json serialises
//@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
//@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
//@JsonIgnoreProperties(ignoreUnknown=true)
public class DataStoreEncoding extends Classification {
    private static final Logger log = LoggerFactory.getLogger( DataStoreEncoding.class);
    private static final String className =  DataStoreEncoding.class.getName();
    private Map<String, Object> extraAttributes;


 public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
        "encoding",
        "language",
        "description",
        "steward",
        "source",
        "properties",

    // Terminate the list
        null
    };
    public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
        "encoding",
        "language",
        "description",
        "steward",
        "source",

     // Terminate the list
        null
    };
    public static final String[] ENUM_NAMES_SET_VALUES = new String[] {

         // Terminate the list
          null
    };
    public static final String[] MAP_NAMES_SET_VALUES = new String[] {
         "properties",

         // Terminate the list
         null
    };
    // note the below definitions needs to be fully qualified
    public static final java.util.Set<String> PROPERTY_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(PROPERTY_NAMES_SET_VALUES)));
    public static final java.util.Set<String> ATTRIBUTE_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ATTRIBUTE_NAMES_SET_VALUES)));
    public static final java.util.Set<String> ENUM_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ENUM_NAMES_SET_VALUES)));
    public static final java.util.Set<String> MAP_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(MAP_NAMES_SET_VALUES)));
    /**
     * Default constructor
     */
    public DataStoreEncoding() {
            super.classificationName="DataStoreEncoding";
    }
    @Override
    public InstanceProperties obtainInstanceProperties() {
        final String methodName = "obtainInstanceProperties";
        if (log.isDebugEnabled()) {
               log.debug("==> Method: " + methodName);
        }
        InstanceProperties instanceProperties = new InstanceProperties();
        EnumPropertyValue enumPropertyValue=null;
        MapPropertyValue mapPropertyValue=null;
        // Additional properties for the encoding.
        mapPropertyValue = new MapPropertyValue();
        //mapPropertyValue.setMapValues(this.<$$PropertyName$$>);
        PrimitivePropertyValue primitivePropertyValue=null;
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(encoding);
        instanceProperties.setProperty("encoding",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(language);
        instanceProperties.setProperty("language",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(description);
        instanceProperties.setProperty("description",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(steward);
        instanceProperties.setProperty("steward",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(source);
        instanceProperties.setProperty("source",primitivePropertyValue);
        if (log.isDebugEnabled()) {
               log.debug("<== Method: " + methodName);
        }
        return instanceProperties;
    }

       private String encoding;
       /**
        * Encoding type.
        * @return String
        */
       public String getEncoding() {
           return this.encoding;
       }
       public void setEncoding(String encoding)  {
           this.encoding = encoding;
       }


       private String language;
       /**
        * Language used in the encoding.
        * @return String
        */
       public String getLanguage() {
           return this.language;
       }
       public void setLanguage(String language)  {
           this.language = language;
       }


       private String description;
       /**
        * Description the encoding.
        * @return String
        */
       public String getDescription() {
           return this.description;
       }
       public void setDescription(String description)  {
           this.description = description;
       }


       private String steward;
       /**
        * Person, organization or engine that defined the encoding.
        * @return String
        */
       public String getSteward() {
           return this.steward;
       }
       public void setSteward(String steward)  {
           this.steward = steward;
       }


       private String source;
       /**
        * Source of the encoding.
        * @return String
        */
       public String getSource() {
           return this.source;
       }
       public void setSource(String source)  {
           this.source = source;
       }


       private Map<String,String> properties;
       /**
        * Additional properties for the encoding.
        * @return Map<String,String>
        */
       public Map<String,String> getProperties() {
           return this.properties;
       }
       public void setProperties(Map<String,String> properties)  {
           this.properties = properties;
       }



    /**
      * Get the extra attributes - ones that are in addition to the standard types.
      * @return extra attributes
      */
    public Map<String, Object> getExtraAttributes() {
          return extraAttributes;
    }
    public void setExtraAttributes(Map<String, Object> extraAttributes) {
          this.extraAttributes = extraAttributes;
    }
}
