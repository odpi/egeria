/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.entities.DataProfileLogAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import  org.odpi.openmetadata.fvt.opentypes.references.DataProfileLogAnnotationToLogFile.DataProfileLogFilesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataProfileLogFile.DataProfileLogFile;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataProfileLogFile.DataProfileLogFileMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.AnnotationToAnnotationReview.AnnotationReviewsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.AnnotationReviewLink.AnnotationReviewLink;
import org.odpi.openmetadata.fvt.opentypes.relationships.AnnotationReviewLink.AnnotationReviewLinkMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.AnnotationToAnnotation.ExtendedAnnotationsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.AnnotationExtension.AnnotationExtension;
import org.odpi.openmetadata.fvt.opentypes.relationships.AnnotationExtension.AnnotationExtensionMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.AnnotationToOpenDiscoveryAnalysisReport.FromAnalysisReportReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.DiscoveredAnnotation.DiscoveredAnnotation;
import org.odpi.openmetadata.fvt.opentypes.relationships.DiscoveredAnnotation.DiscoveredAnnotationMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.DataFieldAnnotationToDataField.AnnotatedDataFieldsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataFieldAnalysis.DataFieldAnalysis;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataFieldAnalysis.DataFieldAnalysisMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.AnnotationToAnnotation.AnnotationExtensionsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.AnnotationExtension.AnnotationExtension;
import org.odpi.openmetadata.fvt.opentypes.relationships.AnnotationExtension.AnnotationExtensionMapper;

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
 * References for entity  DataProfileLogAnnotation. References are relationships represented as an attribute. Exposing relationships information
 * as references can make the relationships information more digestable.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataProfileLogAnnotationReferences implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(DataProfileLogAnnotationReferences.class);
    private static final String className = DataProfileLogAnnotationReferences.class.getName();

    public static final String[] REFERENCE_NAMES_SET_VALUES = new String[] {
             "dataProfileLogFiles",
             "annotationReviews",
             "extendedAnnotations",
             "fromAnalysisReport",
             "annotatedDataFields",
             "annotationExtensions",
             // Terminate the list
             null
    };

     public static final String[] RELATIONSHIP_NAMES_SET_VALUES = new String[] {
             "DataProfileLogFile",
             "AnnotationReviewLink",
             "AnnotationExtension",
             "DiscoveredAnnotation",
             "DataFieldAnalysis",
             "AnnotationExtension",
              // Terminate the list
              null
     };
     /**
       * We are passed a set of omrs relationships that are associated with a entity DataProfileLogAnnotation
       * Each of these relationships should map to a reference (a uniquely named attribute in DataProfileLogAnnotation).
       *
       * Relationships have cardinality. There are 2 sorts of cardinality relevant here, whether the relationships can be related to one or many
       * entities.
       *
       * @param lines the relationships lines.
       * @param entityGuid The GUID of the entity.
       */
     public DataProfileLogAnnotationReferences(String entityGuid, Set<Line> lines)  {

        if (lines !=null) {
         for (Line relationships: lines) {
            for (int i=0;i< RELATIONSHIP_NAMES_SET_VALUES.length; i++) {
               if (relationships.getName().equals(RELATIONSHIP_NAMES_SET_VALUES[i])) {
                    String referenceName = REFERENCE_NAMES_SET_VALUES[i];

                    if ("dataProfileLogFiles".equals(referenceName)) {
                         DataProfileLogFile dataProfileLogFile_relationship = (DataProfileLogFile)relationships;
                         DataProfileLogFilesReference dataProfileLogFilesReference = new DataProfileLogFilesReference(entityGuid,dataProfileLogFile_relationship);
                         if ( dataProfileLogFiles== null ) {
                              dataProfileLogFiles = new HashSet();
                         }
                          dataProfileLogFiles.add(dataProfileLogFilesReference);
                    }
                    if ("annotationReviews".equals(referenceName)) {
                         AnnotationReviewLink annotationReviewLink_relationship = (AnnotationReviewLink)relationships;
                         AnnotationReviewsReference annotationReviewsReference = new AnnotationReviewsReference(entityGuid,annotationReviewLink_relationship);
                         if ( annotationReviews== null ) {
                              annotationReviews = new HashSet();
                         }
                          annotationReviews.add(annotationReviewsReference);
                    }
                    if ("extendedAnnotations".equals(referenceName)) {
                         AnnotationExtension annotationExtension_relationship = (AnnotationExtension)relationships;
                         ExtendedAnnotationsReference extendedAnnotationsReference = new ExtendedAnnotationsReference(entityGuid,annotationExtension_relationship);
                         if ( extendedAnnotations== null ) {
                              extendedAnnotations = new HashSet();
                         }
                          extendedAnnotations.add(extendedAnnotationsReference);
                    }
                    if ("annotatedDataFields".equals(referenceName)) {
                         DataFieldAnalysis dataFieldAnalysis_relationship = (DataFieldAnalysis)relationships;
                         AnnotatedDataFieldsReference annotatedDataFieldsReference = new AnnotatedDataFieldsReference(entityGuid,dataFieldAnalysis_relationship);
                         if ( annotatedDataFields== null ) {
                              annotatedDataFields = new HashSet();
                         }
                          annotatedDataFields.add(annotatedDataFieldsReference);
                    }
                    if ("annotationExtensions".equals(referenceName)) {
                         AnnotationExtension annotationExtension_relationship = (AnnotationExtension)relationships;
                         AnnotationExtensionsReference annotationExtensionsReference = new AnnotationExtensionsReference(entityGuid,annotationExtension_relationship);
                         if ( annotationExtensions== null ) {
                              annotationExtensions = new HashSet();
                         }
                          annotationExtensions.add(annotationExtensionsReference);
                    }

                    if ("fromAnalysisReport".equals(referenceName)) {
                         DiscoveredAnnotation discoveredAnnotation_relationship = (DiscoveredAnnotation)relationships;
                         fromAnalysisReport = new FromAnalysisReportReference(entityGuid, discoveredAnnotation_relationship);
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
    private FromAnalysisReportReference fromAnalysisReport;
// Set properties

    private Set<DataProfileLogFilesReference> dataProfileLogFiles;
    private Set<AnnotationReviewsReference> annotationReviews;
    private Set<ExtendedAnnotationsReference> extendedAnnotations;
    private Set<AnnotatedDataFieldsReference> annotatedDataFields;
    private Set<AnnotationExtensionsReference> annotationExtensions;

// List properties

    // setters and setters
    public FromAnalysisReportReference getFromAnalysisReportReference() {
        return fromAnalysisReport;   }

    public void setFromAnalysisReportReference(FromAnalysisReportReference fromAnalysisReport) {
        this.fromAnalysisReport = fromAnalysisReport; }

// Sets
    public Set<DataProfileLogFilesReference> getDataProfileLogFilesReferences() {
        return dataProfileLogFiles;
    }

    public void setDataProfileLogFilesReferences(Set<DataProfileLogFilesReference> dataProfileLogFiles) {
        this.dataProfileLogFiles =dataProfileLogFiles;
    }
    public Set<AnnotationReviewsReference> getAnnotationReviewsReferences() {
        return annotationReviews;
    }

    public void setAnnotationReviewsReferences(Set<AnnotationReviewsReference> annotationReviews) {
        this.annotationReviews =annotationReviews;
    }
    public Set<ExtendedAnnotationsReference> getExtendedAnnotationsReferences() {
        return extendedAnnotations;
    }

    public void setExtendedAnnotationsReferences(Set<ExtendedAnnotationsReference> extendedAnnotations) {
        this.extendedAnnotations =extendedAnnotations;
    }
    public Set<AnnotatedDataFieldsReference> getAnnotatedDataFieldsReferences() {
        return annotatedDataFields;
    }

    public void setAnnotatedDataFieldsReferences(Set<AnnotatedDataFieldsReference> annotatedDataFields) {
        this.annotatedDataFields =annotatedDataFields;
    }
    public Set<AnnotationExtensionsReference> getAnnotationExtensionsReferences() {
        return annotationExtensions;
    }

    public void setAnnotationExtensionsReferences(Set<AnnotationExtensionsReference> annotationExtensions) {
        this.annotationExtensions =annotationExtensions;
    }

// Lists

 public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("DataProfileLogAnnotationReferences{");
        sb.append("dataProfileLogFilesReference='").append(dataProfileLogFiles.toString());
        sb.append("annotationReviewsReference='").append(annotationReviews.toString());
        sb.append("extendedAnnotationsReference='").append(extendedAnnotations.toString());
        sb.append("annotatedDataFieldsReference='").append(annotatedDataFields.toString());
        sb.append("annotationExtensionsReference='").append(annotationExtensions.toString());
        sb.append("fromAnalysisReportReference='").append(fromAnalysisReport.toString());

        sb.append('}');

        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        if (!super.equals(o)) { return false; }
         DataProfileLogAnnotationReferences typedThat = (DataProfileLogAnnotationReferences) o;
      // compare single cardinality attributes
         if (this.dataProfileLogFiles != null && !Objects.equals(this.dataProfileLogFiles,typedThat.dataProfileLogFiles)) {
                            return false;
                 }
         if (this.annotationReviews != null && !Objects.equals(this.annotationReviews,typedThat.annotationReviews)) {
                            return false;
                 }
         if (this.extendedAnnotations != null && !Objects.equals(this.extendedAnnotations,typedThat.extendedAnnotations)) {
                            return false;
                 }
         if (this.fromAnalysisReport != null && !Objects.equals(this.fromAnalysisReport,typedThat.fromAnalysisReport)) {
                            return false;
                 }
         if (this.annotatedDataFields != null && !Objects.equals(this.annotatedDataFields,typedThat.annotatedDataFields)) {
                            return false;
                 }
         if (this.annotationExtensions != null && !Objects.equals(this.annotationExtensions,typedThat.annotationExtensions)) {
                            return false;
                 }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode()
    ,this.dataProfileLogFiles
    ,this.annotationReviews
    ,this.extendedAnnotations
    ,this.fromAnalysisReport
    ,this.annotatedDataFields
    ,this.annotationExtensions
       );
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}
