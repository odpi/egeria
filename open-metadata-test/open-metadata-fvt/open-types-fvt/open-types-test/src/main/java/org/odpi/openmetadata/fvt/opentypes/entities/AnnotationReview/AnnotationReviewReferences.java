/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.entities.AnnotationReview;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import  org.odpi.openmetadata.fvt.opentypes.references.AnnotationReviewToAnnotation.ReviewedAnnotationsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.AnnotationReviewLink.AnnotationReviewLink;
import org.odpi.openmetadata.fvt.opentypes.relationships.AnnotationReviewLink.AnnotationReviewLinkMapper;

import java.io.Serializable;
import java.util.*;
import org.odpi.openmetadata.fvt.opentypes.common.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * References for entity  AnnotationReview. References are relationships represented as an attribute. Exposing relationships information
 * as references can make the relationships information more digestable.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AnnotationReviewReferences implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(AnnotationReviewReferences.class);
    private static final String className = AnnotationReviewReferences.class.getName();

    public static final String[] REFERENCE_NAMES_SET_VALUES = new String[] {
             "reviewedAnnotations",
             // Terminate the list
             null
    };

     public static final String[] RELATIONSHIP_NAMES_SET_VALUES = new String[] {
             "AnnotationReviewLink",
              // Terminate the list
              null
     };
     /**
       * We are passed a set of omrs relationships that are associated with a entity AnnotationReview
       * Each of these relationships should map to a reference (a uniquely named attribute in AnnotationReview).
       *
       * Relationships have cardinality. There are 2 sorts of cardinality relevant here, whether the relationships can be related to one or many
       * entities.
       *
       * @param lines the relationships lines.
       * @param entityGuid The GUID of the entity.
       */
     public AnnotationReviewReferences(String entityGuid, Set<Line> lines)  {

        if (lines !=null) {
         for (Line relationships: lines) {
            for (int i=0;i< RELATIONSHIP_NAMES_SET_VALUES.length; i++) {
               if (relationships.getName().equals(RELATIONSHIP_NAMES_SET_VALUES[i])) {
                    String referenceName = REFERENCE_NAMES_SET_VALUES[i];

                    if ("reviewedAnnotations".equals(referenceName)) {
                         AnnotationReviewLink annotationReviewLink_relationship = (AnnotationReviewLink)relationships;
                         ReviewedAnnotationsReference reviewedAnnotationsReference = new ReviewedAnnotationsReference(entityGuid,annotationReviewLink_relationship);
                         if ( reviewedAnnotations== null ) {
                              reviewedAnnotations = new HashSet();
                         }
                          reviewedAnnotations.add(reviewedAnnotationsReference);
                    }

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

    private Set<ReviewedAnnotationsReference> reviewedAnnotations;

// List properties

    // setters and setters

// Sets
    public Set<ReviewedAnnotationsReference> getReviewedAnnotationsReferences() {
        return reviewedAnnotations;
    }

    public void setReviewedAnnotationsReferences(Set<ReviewedAnnotationsReference> reviewedAnnotations) {
        this.reviewedAnnotations =reviewedAnnotations;
    }

// Lists

 public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("AnnotationReviewReferences{");
        sb.append("reviewedAnnotationsReference='").append(reviewedAnnotations.toString());

        sb.append('}');

        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        if (!super.equals(o)) { return false; }
         AnnotationReviewReferences typedThat = (AnnotationReviewReferences) o;
      // compare single cardinality attributes
         if (this.reviewedAnnotations != null && !Objects.equals(this.reviewedAnnotations,typedThat.reviewedAnnotations)) {
                            return false;
                 }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode()
    ,this.reviewedAnnotations
       );
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}
