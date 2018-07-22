/* SPDX-License-Identifier: Apache-2.0 */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.generated.entities.OperatingPlatform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.OperatingPlatformToHost.HostReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.HostOperatingPlatform.HostOperatingPlatform;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.HostOperatingPlatform.HostOperatingPlatformMapper;

import java.io.Serializable;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 *
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OperatingPlatformReferences implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(OperatingPlatformReferences.class);
    private static final String className = OperatingPlatformReferences.class.getName();

    public static final String[] REFERENCE_NAMES_SET_VALUES = new String[] {
             "host",
             // Terminate the list
             null
    };

     public static final String[] RELATIONSHIP_NAMES_SET_VALUES = new String[] {
             "HostOperatingPlatform",
              // Terminate the list
              null
     };
     /**
       * We are passed a set of omrs relationships that are associated with a entity OperatingPlatform
       * Each of these relationships should map to a reference (a uniquely named attribute in OperatingPlatform).
       *
       * Relationships have cardinality. There are 2 sorts of cardinality relevant here, whether the relationship can be related to one or many
       * entities.
       *
       * @param lines
       * @param entityGuid
       * @throws InvalidParameterException
       */
     public OperatingPlatformReferences(String entityGuid, List<Line> lines) throws InvalidParameterException {
         for (Line relationship: lines) {
            for (int i=0;i< RELATIONSHIP_NAMES_SET_VALUES.length; i++) {
               if (relationship.getName().equals(RELATIONSHIP_NAMES_SET_VALUES[i])) {
                    String referenceName = REFERENCE_NAMES_SET_VALUES[i];

                    if ("host".equals(referenceName)) {
                         HostOperatingPlatform hostOperatingPlatform_relationship = (HostOperatingPlatform)relationship;
                         HostReference hostReference = new HostReference(entityGuid,hostOperatingPlatform_relationship);
                         if ( host== null ) {
                              host = new HashSet();
                         }
                          host.add(hostReference);
                    }

                 }
             }
         }
     }

    public static final Set<String> REFERENCE_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(REFERENCE_NAMES_SET_VALUES)));
    // there may be duplicate strings in RELATIONSHIP_NAMES_SET_VALUES, the following line deduplicates the Strings into a Set.
    public static final Set<String> RELATIONSHIP_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(RELATIONSHIP_NAMES_SET_VALUES)));
// Singular properties
// Set properties

    private Set<HostReference> host;

// List properties

    // setters and setters

// Sets
    public Set<HostReference> getHostReferences() {
        return host;
    }

    public void setHostReferences(Set<HostReference> host) {
        this.host =host;
    }

// Lists

 public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("OperatingPlatformReferences{");
        sb.append("hostReference='").append(host.toString());

        sb.append('}');

        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        if (!super.equals(o)) { return false; }
         OperatingPlatformReferences typedThat = (OperatingPlatformReferences) o;
      // compare single cardinality attributes
         if (this.host != null && !Objects.equals(this.host,typedThat.host)) {
                            return false;
                 }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode()
    ,this.host
       );
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}
