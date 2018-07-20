/* SPDX-License-Identifier: Apache-2.0 */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Certification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

/**
 * Certification is a relationship between an entity of type Referenceable and an entity of type CertificationType.
 * The ends of the relationship are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has certifies as the proxy name for entity type Referenceable.
 * The second entity proxy has certifications as the proxy name for entity type CertificationType.
 *
 * Each entity proxy also stores the entities guid.

 An awarded certification of a specific type.
 */
public class Certification extends Line {
    private static final Logger log = LoggerFactory.getLogger(Certification.class);
    private static final String className = Certification.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "certificateGUID",
          "start",
          "end",
          "conditions",
          "certifiedBy",
          "custodian",
          "recipient",
          "notes",

      // Terminate the list
          null
      };
      public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
          "certificateGUID",
          "start",
          "end",
          "conditions",
          "certifiedBy",
          "custodian",
          "recipient",
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


    
    public Certification() {
        super("Certification");
        super.entity1Name = "certifies";
        super.entity1Type = "Referenceable";
        super.entity2Name = "certifications";
        super.entity2Type = "CertificationType";
    }

    public Certification(Relationship omrsRelationship) {
        super(omrsRelationship);

        if (!omrsRelationship.getEntityOnePropertyName().equals("certifies")){
            //error
        }
        if (!omrsRelationship.getEntityTwoPropertyName().equals("certifications")){
            //error
        }
        if (!omrsRelationship.getEntityOneProxy().getType().getTypeDefName().equals("Referenceable")){
            //error
        }
        if (!omrsRelationship.getEntityTwoProxy().getType().getTypeDefName().equals("CertificationType")){
            //error
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
          instanceProperties.setProperty("certificateGUID",primitivePropertyValue);
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
          instanceProperties.setProperty("certifiedBy",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("custodian",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("recipient",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("notes",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private String certificateGUID;
         /**
          * Unique identifier of the actual certificate.
          * @return String
          */
         public String getCertificateGUID() {
             return this.certificateGUID;
         }
         public void setCertificateGUID(String certificateGUID)  {
            this.certificateGUID = certificateGUID;
        }
         private Date start;
         /**
          * Start date for the certification.
          * @return Date
          */
         public Date getStart() {
             return this.start;
         }
         public void setStart(Date start)  {
            this.start = start;
        }
         private Date end;
         /**
          * End date for the certification.
          * @return Date
          */
         public Date getEnd() {
             return this.end;
         }
         public void setEnd(Date end)  {
            this.end = end;
        }
         private String conditions;
         /**
          * Any special conditions or endorsements over the basic certification type.
          * @return String
          */
         public String getConditions() {
             return this.conditions;
         }
         public void setConditions(String conditions)  {
            this.conditions = conditions;
        }
         private String certifiedBy;
         /**
          * Person or organization awarded the certification.
          * @return String
          */
         public String getCertifiedBy() {
             return this.certifiedBy;
         }
         public void setCertifiedBy(String certifiedBy)  {
            this.certifiedBy = certifiedBy;
        }
         private String custodian;
         /**
          * The person, engine or organization tht will ensure the certification is honored.
          * @return String
          */
         public String getCustodian() {
             return this.custodian;
         }
         public void setCustodian(String custodian)  {
            this.custodian = custodian;
        }
         private String recipient;
         /**
          * The person or organization that received the certification.
          * @return String
          */
         public String getRecipient() {
             return this.recipient;
         }
         public void setRecipient(String recipient)  {
            this.recipient = recipient;
        }
         private String notes;
         /**
          * Additional notes about the certification.
          * @return String
          */
         public String getNotes() {
             return this.notes;
         }
         public void setNotes(String notes)  {
            this.notes = notes;
        }
}
