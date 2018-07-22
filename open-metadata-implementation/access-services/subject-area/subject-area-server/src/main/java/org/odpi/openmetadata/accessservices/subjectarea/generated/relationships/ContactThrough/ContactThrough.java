/* SPDX-License-Identifier: Apache-2.0 */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ContactThrough;
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
 * ContactThrough is a relationship between an entity of type ActorProfile and an entity of type ContactDetails.
 * The ends of the relationship are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has contactDetails as the proxy name for entity type ActorProfile.
 * The second entity proxy has contacts as the proxy name for entity type ContactDetails.
 *
 * Each entity proxy also stores the entities guid.

 The contact details associated with an actor profile.
 */
public class ContactThrough extends Line {
    private static final Logger log = LoggerFactory.getLogger(ContactThrough.class);
    private static final String className = ContactThrough.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "contactMethodType",
          "contactMethodValue",

      // Terminate the list
          null
      };
      public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
          "contactMethodValue",

       // Terminate the list
          null
      };
      public static final String[] ENUM_NAMES_SET_VALUES = new String[] {
           "contactMethodType",

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


    
    public ContactThrough() {
        super("ContactThrough");
        super.entity1Name = "contactDetails";
        super.entity1Type = "ActorProfile";
        super.entity2Name = "contacts";
        super.entity2Type = "ContactDetails";
    }

    public ContactThrough(Relationship omrsRelationship) {
        super(omrsRelationship);

        if (!omrsRelationship.getEntityOnePropertyName().equals("contactDetails")){
            //error
        }
        if (!omrsRelationship.getEntityTwoPropertyName().equals("contacts")){
            //error
        }
        if (!omrsRelationship.getEntityOneProxy().getType().getTypeDefName().equals("ActorProfile")){
            //error
        }
        if (!omrsRelationship.getEntityTwoProxy().getType().getTypeDefName().equals("ContactDetails")){
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
          enumPropertyValue = new EnumPropertyValue();
          // mechanism to use.
          enumPropertyValue.setOrdinal(contactMethodType.ordinal());
          enumPropertyValue.setSymbolicName(contactMethodType.name());
          instanceProperties.setProperty("contactMethodType",enumPropertyValue);
          MapPropertyValue mapPropertyValue=null;
          PrimitivePropertyValue primitivePropertyValue=null;
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("contactMethodType",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("contactMethodValue",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private ContactMethodType contactMethodType;
         /**
          * Mechanism to use.
          * @return ContactMethodType
          */
         public ContactMethodType getContactMethodType() {
             return this.contactMethodType;
         }
         public void setContactMethodType(ContactMethodType contactMethodType)  {
            this.contactMethodType = contactMethodType;
        }
         private String contactMethodValue;
         /**
          * Contact address.
          * @return String
          */
         public String getContactMethodValue() {
             return this.contactMethodValue;
         }
         public void setContactMethodValue(String contactMethodValue)  {
            this.contactMethodValue = contactMethodValue;
        }
}
