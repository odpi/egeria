/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.entities.DataField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import  org.odpi.openmetadata.fvt.opentypes.references.DataFieldToDataFieldAnnotation.DataFieldAnnotationsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataFieldAnalysis.DataFieldAnalysis;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataFieldAnalysis.DataFieldAnalysisMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.DataFieldToDataField.NestedDataFieldsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.DiscoveredNestedDataField.DiscoveredNestedDataField;
import org.odpi.openmetadata.fvt.opentypes.relationships.DiscoveredNestedDataField.DiscoveredNestedDataFieldMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.DataFieldToDataField.ParentDataFieldReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.DiscoveredNestedDataField.DiscoveredNestedDataField;
import org.odpi.openmetadata.fvt.opentypes.relationships.DiscoveredNestedDataField.DiscoveredNestedDataFieldMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.DataFieldToSchemaAttribute.SchemaAttributeDefinitionReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.SchemaAttributeDefinition.SchemaAttributeDefinition;
import org.odpi.openmetadata.fvt.opentypes.relationships.SchemaAttributeDefinition.SchemaAttributeDefinitionMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.DataFieldToDataClass.DataClassDefinitionReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataClassDefinition.DataClassDefinition;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataClassDefinition.DataClassDefinitionMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.DataFieldToSchemaAnalysisAnnotation.SchemaAnalysisAnnotationReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.DiscoveredDataField.DiscoveredDataField;
import org.odpi.openmetadata.fvt.opentypes.relationships.DiscoveredDataField.DiscoveredDataFieldMapper;

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
 * References for entity  DataField. References are relationships represented as an attribute. Exposing relationships information
 * as references can make the relationships information more digestable.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataFieldReferences implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(DataFieldReferences.class);
    private static final String className = DataFieldReferences.class.getName();

    public static final String[] REFERENCE_NAMES_SET_VALUES = new String[] {
             "dataFieldAnnotations",
             "nestedDataFields",
             "parentDataField",
             "schemaAttributeDefinition",
             "dataClassDefinition",
             "schemaAnalysisAnnotation",
             // Terminate the list
             null
    };

     public static final String[] RELATIONSHIP_NAMES_SET_VALUES = new String[] {
             "DataFieldAnalysis",
             "DiscoveredNestedDataField",
             "DiscoveredNestedDataField",
             "SchemaAttributeDefinition",
             "DataClassDefinition",
             "DiscoveredDataField",
              // Terminate the list
              null
     };
     /**
       * We are passed a set of omrs relationships that are associated with a entity DataField
       * Each of these relationships should map to a reference (a uniquely named attribute in DataField).
       *
       * Relationships have cardinality. There are 2 sorts of cardinality relevant here, whether the relationships can be related to one or many
       * entities.
       *
       * @param lines the relationships lines.
       * @param entityGuid The GUID of the entity.
       */
     public DataFieldReferences(String entityGuid, Set<Line> lines)  {

        if (lines !=null) {
         for (Line relationships: lines) {
            for (int i=0;i< RELATIONSHIP_NAMES_SET_VALUES.length; i++) {
               if (relationships.getName().equals(RELATIONSHIP_NAMES_SET_VALUES[i])) {
                    String referenceName = REFERENCE_NAMES_SET_VALUES[i];

                    if ("dataFieldAnnotations".equals(referenceName)) {
                         DataFieldAnalysis dataFieldAnalysis_relationship = (DataFieldAnalysis)relationships;
                         DataFieldAnnotationsReference dataFieldAnnotationsReference = new DataFieldAnnotationsReference(entityGuid,dataFieldAnalysis_relationship);
                         if ( dataFieldAnnotations== null ) {
                              dataFieldAnnotations = new HashSet();
                         }
                          dataFieldAnnotations.add(dataFieldAnnotationsReference);
                    }
                    if ("nestedDataFields".equals(referenceName)) {
                         DiscoveredNestedDataField discoveredNestedDataField_relationship = (DiscoveredNestedDataField)relationships;
                         NestedDataFieldsReference nestedDataFieldsReference = new NestedDataFieldsReference(entityGuid,discoveredNestedDataField_relationship);
                         if ( nestedDataFields== null ) {
                              nestedDataFields = new HashSet();
                         }
                          nestedDataFields.add(nestedDataFieldsReference);
                    }

                    if ("parentDataField".equals(referenceName)) {
                         DiscoveredNestedDataField discoveredNestedDataField_relationship = (DiscoveredNestedDataField)relationships;
                         parentDataField = new ParentDataFieldReference(entityGuid, discoveredNestedDataField_relationship);
                    }
                    if ("schemaAttributeDefinition".equals(referenceName)) {
                         SchemaAttributeDefinition schemaAttributeDefinition_relationship = (SchemaAttributeDefinition)relationships;
                         schemaAttributeDefinition = new SchemaAttributeDefinitionReference(entityGuid, schemaAttributeDefinition_relationship);
                    }
                    if ("dataClassDefinition".equals(referenceName)) {
                         DataClassDefinition dataClassDefinition_relationship = (DataClassDefinition)relationships;
                         dataClassDefinition = new DataClassDefinitionReference(entityGuid, dataClassDefinition_relationship);
                    }
                    if ("schemaAnalysisAnnotation".equals(referenceName)) {
                         DiscoveredDataField discoveredDataField_relationship = (DiscoveredDataField)relationships;
                         schemaAnalysisAnnotation = new SchemaAnalysisAnnotationReference(entityGuid, discoveredDataField_relationship);
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
    private ParentDataFieldReference parentDataField;
    private SchemaAttributeDefinitionReference schemaAttributeDefinition;
    private DataClassDefinitionReference dataClassDefinition;
    private SchemaAnalysisAnnotationReference schemaAnalysisAnnotation;
// Set properties

    private Set<DataFieldAnnotationsReference> dataFieldAnnotations;
    private Set<NestedDataFieldsReference> nestedDataFields;

// List properties

    // setters and setters
    public ParentDataFieldReference getParentDataFieldReference() {
        return parentDataField;   }

    public void setParentDataFieldReference(ParentDataFieldReference parentDataField) {
        this.parentDataField = parentDataField; }
    public SchemaAttributeDefinitionReference getSchemaAttributeDefinitionReference() {
        return schemaAttributeDefinition;   }

    public void setSchemaAttributeDefinitionReference(SchemaAttributeDefinitionReference schemaAttributeDefinition) {
        this.schemaAttributeDefinition = schemaAttributeDefinition; }
    public DataClassDefinitionReference getDataClassDefinitionReference() {
        return dataClassDefinition;   }

    public void setDataClassDefinitionReference(DataClassDefinitionReference dataClassDefinition) {
        this.dataClassDefinition = dataClassDefinition; }
    public SchemaAnalysisAnnotationReference getSchemaAnalysisAnnotationReference() {
        return schemaAnalysisAnnotation;   }

    public void setSchemaAnalysisAnnotationReference(SchemaAnalysisAnnotationReference schemaAnalysisAnnotation) {
        this.schemaAnalysisAnnotation = schemaAnalysisAnnotation; }

// Sets
    public Set<DataFieldAnnotationsReference> getDataFieldAnnotationsReferences() {
        return dataFieldAnnotations;
    }

    public void setDataFieldAnnotationsReferences(Set<DataFieldAnnotationsReference> dataFieldAnnotations) {
        this.dataFieldAnnotations =dataFieldAnnotations;
    }
    public Set<NestedDataFieldsReference> getNestedDataFieldsReferences() {
        return nestedDataFields;
    }

    public void setNestedDataFieldsReferences(Set<NestedDataFieldsReference> nestedDataFields) {
        this.nestedDataFields =nestedDataFields;
    }

// Lists

 public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("DataFieldReferences{");
        sb.append("dataFieldAnnotationsReference='").append(dataFieldAnnotations.toString());
        sb.append("nestedDataFieldsReference='").append(nestedDataFields.toString());
        sb.append("parentDataFieldReference='").append(parentDataField.toString());
        sb.append("schemaAttributeDefinitionReference='").append(schemaAttributeDefinition.toString());
        sb.append("dataClassDefinitionReference='").append(dataClassDefinition.toString());
        sb.append("schemaAnalysisAnnotationReference='").append(schemaAnalysisAnnotation.toString());

        sb.append('}');

        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        if (!super.equals(o)) { return false; }
         DataFieldReferences typedThat = (DataFieldReferences) o;
      // compare single cardinality attributes
         if (this.dataFieldAnnotations != null && !Objects.equals(this.dataFieldAnnotations,typedThat.dataFieldAnnotations)) {
                            return false;
                 }
         if (this.nestedDataFields != null && !Objects.equals(this.nestedDataFields,typedThat.nestedDataFields)) {
                            return false;
                 }
         if (this.parentDataField != null && !Objects.equals(this.parentDataField,typedThat.parentDataField)) {
                            return false;
                 }
         if (this.schemaAttributeDefinition != null && !Objects.equals(this.schemaAttributeDefinition,typedThat.schemaAttributeDefinition)) {
                            return false;
                 }
         if (this.dataClassDefinition != null && !Objects.equals(this.dataClassDefinition,typedThat.dataClassDefinition)) {
                            return false;
                 }
         if (this.schemaAnalysisAnnotation != null && !Objects.equals(this.schemaAnalysisAnnotation,typedThat.schemaAnalysisAnnotation)) {
                            return false;
                 }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode()
    ,this.dataFieldAnnotations
    ,this.nestedDataFields
    ,this.parentDataField
    ,this.schemaAttributeDefinition
    ,this.dataClassDefinition
    ,this.schemaAnalysisAnnotation
       );
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}
