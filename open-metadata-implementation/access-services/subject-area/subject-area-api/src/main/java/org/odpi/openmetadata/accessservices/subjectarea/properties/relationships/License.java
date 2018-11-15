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
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.*;

//omrs
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
//omrs beans
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.LineType;

/**
 * License is a relationship between an entity of type Referenceable and an entity of type LicenseType.
 * The ends of the relationship are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has licensed as the proxy name for entity type Referenceable.
 * The second entity proxy has licenses as the proxy name for entity type LicenseType.
 *
 * Each entity proxy also stores the entities guid.

 Link between an asset and its license.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class License extends Line {
    private static final Logger log = LoggerFactory.getLogger(License.class);
    private static final String className = License.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "licenseGUID",
          "start",
          "end",
          "conditions",
          "licensedBy",
          "custodian",
          "licensee",
          "notes",

      // Terminate the list
          null
      };
      public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
          "licenseGUID",
          "start",
          "end",
          "conditions",
          "licensedBy",
          "custodian",
          "licensee",
          "notes",

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


    public License() {
        initialise();
    }

    private void initialise()
    {
       name = "License";
       // set the LineType if this is a LineType enum value.
       try {
           lineType = LineType.valueOf(name);
        }
        catch (IllegalArgumentException e) {
           lineType = LineType.Other;
        }
        entity1Name = "licensed";
        entity1Type = "Referenceable";
        entity2Name = "licenses";
        entity2Type = "LicenseType";
        typeDefGuid = "35e53b7f-2312-4d66-ae90-2d4cb47901ee";
    }

    public License(Line template) {
        super(template);
        initialise();
    }

    public License(Relationship omrsRelationship) {
        super(omrsRelationship);
        name = "License";
       // set the LineType if this is a LineType enum value.
       try {
           lineType = LineType.valueOf(name);
        }
        catch (IllegalArgumentException e) {
           lineType = LineType.Other;
        }
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
          instanceProperties.setProperty("licenseGUID",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("start",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("end",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("conditions",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("licensedBy",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("custodian",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("licensee",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("notes",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private String licenseGUID;
        /**
            * {@literal Unique identifier of the actual license. }
            * @return {@code String }
            */
         public String getLicenseGUID() {
             return this.licenseGUID;
         }
         public void setLicenseGUID(String licenseGUID)  {
            this.licenseGUID = licenseGUID;
        }
         private Date start;
        /**
            * {@literal Start date for the license. }
            * @return {@code Date }
            */
         public Date getStart() {
             return this.start;
         }
         public void setStart(Date start)  {
            this.start = start;
        }
         private Date end;
        /**
            * {@literal End date for the license. }
            * @return {@code Date }
            */
         public Date getEnd() {
             return this.end;
         }
         public void setEnd(Date end)  {
            this.end = end;
        }
         private String conditions;
        /**
            * {@literal Any special conditions or endorsements over the basic license type. }
            * @return {@code String }
            */
         public String getConditions() {
             return this.conditions;
         }
         public void setConditions(String conditions)  {
            this.conditions = conditions;
        }
         private String licensedBy;
        /**
            * {@literal Person or organization that owns the intellectual property. }
            * @return {@code String }
            */
         public String getLicensedBy() {
             return this.licensedBy;
         }
         public void setLicensedBy(String licensedBy)  {
            this.licensedBy = licensedBy;
        }
         private String custodian;
        /**
            * {@literal The person, engine or organization tht will ensure the license is honored. }
            * @return {@code String }
            */
         public String getCustodian() {
             return this.custodian;
         }
         public void setCustodian(String custodian)  {
            this.custodian = custodian;
        }
         private String licensee;
        /**
            * {@literal The person or organization that holds the license. }
            * @return {@code String }
            */
         public String getLicensee() {
             return this.licensee;
         }
         public void setLicensee(String licensee)  {
            this.licensee = licensee;
        }
         private String notes;
        /**
            * {@literal Additional notes about the license. }
            * @return {@code String }
            */
         public String getNotes() {
             return this.notes;
         }
         public void setNotes(String notes)  {
            this.notes = notes;
        }

      @Override
         public StringBuilder toString(StringBuilder sb)
         {
             if (sb == null)
             {
                 sb = new StringBuilder();
             }
             sb.append(" License=");
             sb.append(super.toString(sb));
             sb.append(" License Attributes{");
             sb.append("licenseGUID=" + this.licenseGUID +",");
             sb.append("start=" + this.start +",");
             sb.append("end=" + this.end +",");
             sb.append("conditions=" + this.conditions +",");
             sb.append("licensedBy=" + this.licensedBy +",");
             sb.append("custodian=" + this.custodian +",");
             sb.append("licensee=" + this.licensee +",");
             sb.append("notes=" + this.notes +",");
             sb.append("}");
             return sb;
         }
         @Override
         public String toString() {
             return toString(new StringBuilder()).toString();
         }


}
