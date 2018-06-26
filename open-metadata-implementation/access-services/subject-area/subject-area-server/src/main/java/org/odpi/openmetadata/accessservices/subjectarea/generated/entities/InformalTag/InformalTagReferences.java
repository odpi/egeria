/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.odpi.openmetadata.accessservices.subjectarea.generated.entities.InformalTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.InformalTagToReferenceable.TaggedElementReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedTag.AttachedTag;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedTag.AttachedTagMapper;

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
public class InformalTagReferences implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(InformalTagReferences.class);
    private static final String className = InformalTagReferences.class.getName();

    public static final String[] REFERENCE_NAMES_SET_VALUES = new String[] {
             "taggedElement",
             // Terminate the list
             null
    };

     public static final String[] RELATIONSHIP_NAMES_SET_VALUES = new String[] {
             "AttachedTag",
              // Terminate the list
              null
     };
     /**
       * We are passed a set of omrs relationships that are associated with a entity InformalTag
       * Each of these relationships should map to a reference (a uniquely named attribute in InformalTag).
       *
       * Relationships have cardinality. There are 2 sorts of cardinality relevant here, whether the relationship can be related to one or many
       * entities.
       *
       * @param lines
       * @param entityGuid
       * @throws InvalidParameterException
       */
     public InformalTagReferences(String entityGuid, List<Line> lines) throws InvalidParameterException {
         for (Line relationship: lines) {
            for (int i=0;i< RELATIONSHIP_NAMES_SET_VALUES.length; i++) {
               if (relationship.getName().equals(RELATIONSHIP_NAMES_SET_VALUES[i])) {
                    String referenceName = REFERENCE_NAMES_SET_VALUES[i];


                    if ("taggedElement".equals(referenceName)) {
                         AttachedTag attachedTag_relationship = (AttachedTag)relationship;
                         taggedElement = new TaggedElementReference(entityGuid, attachedTag_relationship);
                    }
                 }
             }
         }
     }

    public static final Set<String> REFERENCE_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(REFERENCE_NAMES_SET_VALUES)));
    // there may be duplicate strings in RELATIONSHIP_NAMES_SET_VALUES, the following line deduplicates the Strings into a Set.
    public static final Set<String> RELATIONSHIP_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(RELATIONSHIP_NAMES_SET_VALUES)));
// Singular properties
    private TaggedElementReference taggedElement;
// Set properties


// List properties

    // setters and setters
    public TaggedElementReference getTaggedElementReference() {
        return taggedElement;   }

    public void setTaggedElementReference(TaggedElementReference taggedElement) {
        this.taggedElement = taggedElement; }

// Sets

// Lists

 public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("InformalTagReferences{");
        sb.append("taggedElementReference='").append(taggedElement.toString());

        sb.append('}');

        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        if (!super.equals(o)) { return false; }
         InformalTagReferences typedThat = (InformalTagReferences) o;
      // compare single cardinality attributes
         if (this.taggedElement != null && !Objects.equals(this.taggedElement,typedThat.taggedElement)) {
                            return false;
                 }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode()
    ,this.taggedElement
       );
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}
