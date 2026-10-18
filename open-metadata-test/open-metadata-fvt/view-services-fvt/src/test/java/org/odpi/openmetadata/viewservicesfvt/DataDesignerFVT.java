/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataClassCompositionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataClassProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataStructureDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataStructureProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataValueAssignmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataValueDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataValueHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataValueSpecificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.LinkedDataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.MemberDataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.NestedDataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.SchemaAttributeDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.SchemaTypeDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.SemanticDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryTermProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.CertificationTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.StructSchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DataDesignerFVT covers every request of the Data Designer OMVS: the create/update/find/retrieve/delete
 * lifecycle of data structures, data fields and data value specifications (including data classes), and every
 * relationship it links and detaches - member data fields, nested and linked data fields, nested data classes,
 * specialized data value specifications, data value assignment and definition, semantic definitions, the
 * certification type's data structure definition, and the schema attribute and schema type definitions that tie a
 * data design to its implementation schemas.
 * <br>
 * The elements at the far end of some relationships belong to other services: the glossary term is created with
 * Glossary Manager, the certification type with Governance Officer, and the schema attribute and schema type
 * with Schema Maker.  Each link is checked by reading the Data Designer element back, finding the relationship
 * with its label (or position), then detaching it and checking it has gone.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataDesignerFVT
{
    private static final ViewServiceClient dataDesigner      = new ViewServiceClient(ViewServiceDescription.DATA_DESIGNER);
    private static final ViewServiceClient glossaryManager   = new ViewServiceClient(ViewServiceDescription.GLOSSARY_MANAGER);
    private static final ViewServiceClient governanceOfficer = new ViewServiceClient(ViewServiceDescription.GOVERNANCE_OFFICER);
    private static final ViewServiceClient schemaMaker       = new ViewServiceClient(ViewServiceDescription.SCHEMA_MAKER);

    private static final String DATA_STRUCTURES = "/data-structures/";
    private static final String DATA_FIELDS     = "/data-fields/";
    private static final String DVS             = "/data-value-specifications/";

    private static final List<String> createdDataStructures = new ArrayList<>();
    private static final List<String> createdDataFields     = new ArrayList<>();
    private static final List<String> createdSpecifications = new ArrayList<>();

    private static String dataStructureGUID = null;
    private static String dataFieldGUID     = null;
    private static String specificationGUID = null;


    /**
     * Create a data structure, read it back, update it, find it and copy it from a template.
     */
    @Test
    @Order(1)
    void dataStructureLifecycle()
    {
        String qualifiedName = ViewServicesFvtTestSupport.qualifiedName("DataStructure", "customer");

        dataStructureGUID = createDataStructure("customer");

        DesignFvtSupport.assertReadable(dataDesigner, retrieve(DATA_STRUCTURES, dataStructureGUID), OpenMetadataType.DATA_STRUCTURE.typeName, qualifiedName);

        DataStructureProperties updateProperties = new DataStructureProperties();

        updateProperties.setDescription("Updated by DataDesignerFVT");
        updateProperties.setNamespacePath("sales.customer");

        assertTrue(dataDesigner.post(DATA_STRUCTURES + dataStructureGUID + "/update", DesignFvtSupport.update(updateProperties), BooleanResponse.class).getFlag(),
                   "updateDataStructure reported no change");

        DataStructureProperties readBack = assertInstanceOf(DataStructureProperties.class, read(DATA_STRUCTURES, dataStructureGUID).getProperties());

        assertEquals("Updated by DataDesignerFVT", readBack.getDescription());
        assertEquals("sales.customer", readBack.getNamespacePath());
        assertEquals("customer", readBack.getDisplayName(), "a merge update should keep the display name");

        DesignFvtSupport.assertContains(dataDesigner.elements("/data-structures/by-name", DesignFvtSupport.byName(qualifiedName)),
                                        dataStructureGUID, "getDataStructuresByName");
        DesignFvtSupport.assertContains(dataDesigner.elements("/data-structures/by-search-string", DesignFvtSupport.runSearch()),
                                        dataStructureGUID, "findDataStructures");

        DataStructureProperties copyProperties = new DataStructureProperties();
        String                  copyName       = ViewServicesFvtTestSupport.qualifiedName("DataStructure", "customer-copy");

        copyProperties.setQualifiedName(copyName);

        String copyGUID = dataDesigner.create("/data-structures/from-template", DesignFvtSupport.fromTemplate(dataStructureGUID, copyProperties));

        createdDataStructures.add(copyGUID);
        assertNotEquals(dataStructureGUID, copyGUID);

        OpenMetadataRootElement copy = DesignFvtSupport.assertReadable(dataDesigner, retrieve(DATA_STRUCTURES, copyGUID), OpenMetadataType.DATA_STRUCTURE.typeName, copyName);

        assertEquals("sales.customer", assertInstanceOf(DataStructureProperties.class, copy.getProperties()).getNamespacePath(),
                     "the copy should take the template's namespace path");
    }


    /**
     * Create a data field, read it back, update it, find it and copy it from a template.
     */
    @Test
    @Order(2)
    void dataFieldLifecycle()
    {
        String qualifiedName = ViewServicesFvtTestSupport.qualifiedName("DataField", "customer-name");

        dataFieldGUID = createDataField("customer-name");

        DesignFvtSupport.assertReadable(dataDesigner, retrieve(DATA_FIELDS, dataFieldGUID), OpenMetadataType.DATA_FIELD.typeName, qualifiedName);

        DataFieldProperties updateProperties = new DataFieldProperties();

        updateProperties.setDescription("Updated by DataDesignerFVT");
        updateProperties.setDataType("string");

        assertTrue(dataDesigner.post(DATA_FIELDS + dataFieldGUID + "/update", DesignFvtSupport.update(updateProperties), BooleanResponse.class).getFlag(),
                   "updateDataField reported no change");

        DataFieldProperties readBack = assertInstanceOf(DataFieldProperties.class, read(DATA_FIELDS, dataFieldGUID).getProperties());

        assertEquals("Updated by DataDesignerFVT", readBack.getDescription());
        assertEquals("string", readBack.getDataType());
        assertEquals("customer-name", readBack.getDisplayName(), "a merge update should keep the display name");

        DesignFvtSupport.assertContains(dataDesigner.elements("/data-fields/by-name", DesignFvtSupport.byName(qualifiedName)),
                                        dataFieldGUID, "getDataFieldsByName");
        DesignFvtSupport.assertContains(dataDesigner.elements("/data-fields/by-search-string", DesignFvtSupport.runSearch()),
                                        dataFieldGUID, "findDataFields");

        DataFieldProperties copyProperties = new DataFieldProperties();
        String              copyName       = ViewServicesFvtTestSupport.qualifiedName("DataField", "customer-name-copy");

        copyProperties.setQualifiedName(copyName);

        String copyGUID = dataDesigner.create("/data-fields/from-template", DesignFvtSupport.fromTemplate(dataFieldGUID, copyProperties));

        createdDataFields.add(copyGUID);
        assertNotEquals(dataFieldGUID, copyGUID);

        OpenMetadataRootElement copy = DesignFvtSupport.assertReadable(dataDesigner, retrieve(DATA_FIELDS, copyGUID), OpenMetadataType.DATA_FIELD.typeName, copyName);

        assertEquals("string", assertInstanceOf(DataFieldProperties.class, copy.getProperties()).getDataType(),
                     "the copy should take the template's data type");
    }


    /**
     * Create a data value specification, read it back, update it, find it and copy it from a template.
     */
    @Test
    @Order(3)
    void dataValueSpecificationLifecycle()
    {
        String qualifiedName = ViewServicesFvtTestSupport.qualifiedName("DataValueSpecification", "customer-name-format");

        specificationGUID = createSpecification(new DataValueSpecificationProperties(), "customer-name-format");

        DesignFvtSupport.assertReadable(dataDesigner, retrieve(DVS, specificationGUID), OpenMetadataType.DATA_VALUE_SPECIFICATION.typeName, qualifiedName);

        DataValueSpecificationProperties updateProperties = new DataValueSpecificationProperties();

        updateProperties.setDescription("Updated by DataDesignerFVT");
        updateProperties.setDataType("string");

        assertTrue(dataDesigner.post(DVS + specificationGUID + "/update", DesignFvtSupport.update(updateProperties), BooleanResponse.class).getFlag(),
                   "updateDataValueSpecification reported no change");

        DataValueSpecificationProperties readBack = assertInstanceOf(DataValueSpecificationProperties.class, read(DVS, specificationGUID).getProperties());

        assertEquals("Updated by DataDesignerFVT", readBack.getDescription());
        assertEquals("string", readBack.getDataType());

        DesignFvtSupport.assertContains(dataDesigner.elements("/data-value-specifications/by-name", DesignFvtSupport.byName(qualifiedName)),
                                        specificationGUID, "getDataValueSpecificationsByName");
        DesignFvtSupport.assertContains(dataDesigner.elements("/data-value-specifications/by-search-string", DesignFvtSupport.runSearch()),
                                        specificationGUID, "findDataValueSpecifications");

        DataValueSpecificationProperties copyProperties = new DataValueSpecificationProperties();
        String                           copyName       = ViewServicesFvtTestSupport.qualifiedName("DataValueSpecification", "customer-name-format-copy");

        copyProperties.setQualifiedName(copyName);

        String copyGUID = dataDesigner.create("/data-value-specifications/from-template", DesignFvtSupport.fromTemplate(specificationGUID, copyProperties));

        createdSpecifications.add(copyGUID);
        assertNotEquals(specificationGUID, copyGUID);

        DesignFvtSupport.assertReadable(dataDesigner, retrieve(DVS, copyGUID), OpenMetadataType.DATA_VALUE_SPECIFICATION.typeName, copyName);
    }


    /**
     * MemberDataField links a data structure to its fields; NestedDataField and LinkedDataField link fields.
     */
    @Test
    @Order(4)
    void dataFieldRelationships()
    {
        DesignFvtSupport.linkCheckAndDetach(dataDesigner,
                                            DATA_STRUCTURES + dataStructureGUID + "/member-data-fields/" + dataFieldGUID,
                                            retrieve(DATA_STRUCTURES, dataStructureGUID),
                                            OpenMetadataType.MEMBER_DATA_FIELD_RELATIONSHIP.typeName,
                                            dataFieldGUID,
                                            DesignFvtSupport.positioned(new MemberDataFieldProperties(), 2));

        String firstNameGUID = createDataField("customer-first-name");

        DesignFvtSupport.linkCheckAndDetach(dataDesigner,
                                            DATA_FIELDS + dataFieldGUID + "/nested-data-fields/" + firstNameGUID,
                                            retrieve(DATA_FIELDS, dataFieldGUID),
                                            OpenMetadataType.NESTED_DATA_FIELD_RELATIONSHIP.typeName,
                                            firstNameGUID,
                                            DesignFvtSupport.positioned(new NestedDataFieldProperties(), 1));

        LinkedDataFieldProperties linkedDataFieldProperties = new LinkedDataFieldProperties();

        linkedDataFieldProperties.setDisplayName("same customer");

        String path = DATA_FIELDS + dataFieldGUID + "/linked-data-fields/" + firstNameGUID;

        DesignFvtSupport.linkCheckAndDetach(dataDesigner,
                                            path,
                                            retrieve(DATA_FIELDS, dataFieldGUID),
                                            OpenMetadataType.LINKED_DATA_FIELD_RELATIONSHIP.typeName,
                                            firstNameGUID,
                                            linkedDataFieldProperties);
    }


    /**
     * DataClassComposition nests data classes; DataValueHierarchy specializes data value specifications.
     */
    @Test
    @Order(5)
    void dataValueSpecificationHierarchies()
    {
        String parentClassGUID = createSpecification(new DataClassProperties(), "name-class");
        String childClassGUID  = createSpecification(new DataClassProperties(), "first-name-class");

        DesignFvtSupport.assertReadable(dataDesigner, retrieve(DVS, parentClassGUID), OpenMetadataType.DATA_CLASS.typeName,
                                        ViewServicesFvtTestSupport.qualifiedName(OpenMetadataType.DATA_CLASS.typeName, "name-class"));

        DesignFvtSupport.linkCheckAndDetach(dataDesigner,
                                            "/data-classes/" + parentClassGUID + "/nested-data-classes/" + childClassGUID,
                                            retrieve(DVS, parentClassGUID),
                                            OpenMetadataType.DATA_CLASS_COMPOSITION_RELATIONSHIP.typeName,
                                            childClassGUID,
                                            DesignFvtSupport.labeled(new DataClassCompositionProperties(), "first name is part of a name"));

        String specializedGUID = createSpecification(new DataValueSpecificationProperties(), "customer-name-format-uk");

        DesignFvtSupport.linkCheckAndDetach(dataDesigner,
                                            DVS + specificationGUID + "/specialized-data-value-specifications/" + specializedGUID,
                                            retrieve(DVS, specificationGUID),
                                            OpenMetadataType.DATA_VALUE_HIERARCHY_RELATIONSHIP.typeName,
                                            specializedGUID,
                                            DesignFvtSupport.labeled(new DataValueHierarchyProperties(), "UK format"));
    }


    /**
     * DataValueAssignment and DataValueDefinition link any element (here a data field) to a data value
     * specification.
     */
    @Test
    @Order(6)
    void dataValueAssignmentAndDefinition()
    {
        DataValueAssignmentProperties assignmentProperties = new DataValueAssignmentProperties();

        assignmentProperties.setMethod("profiled");
        assignmentProperties.setConfidence(80);

        String assignmentPath = "/elements/" + dataFieldGUID + "/data-value-specifications/" + specificationGUID;

        dataDesigner.call(assignmentPath + "/attach", DesignFvtSupport.relationship(assignmentProperties));

        RelatedMetadataElementSummary assignment = DesignFvtSupport.assertLinked(dataDesigner,
                                                                                 retrieve(DATA_FIELDS, dataFieldGUID),
                                                                                 OpenMetadataType.DATA_VALUE_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                                 specificationGUID);
        DataValueAssignmentProperties assignmentReadBack = assertInstanceOf(DataValueAssignmentProperties.class, assignment.getRelationshipProperties());

        assertEquals("profiled", assignmentReadBack.getMethod());
        assertEquals(80, assignmentReadBack.getConfidence());

        dataDesigner.call(assignmentPath + "/detach", new DeleteRelationshipRequestBody());
        DesignFvtSupport.assertNotLinked(dataDesigner, retrieve(DATA_FIELDS, dataFieldGUID), OpenMetadataType.DATA_VALUE_ASSIGNMENT_RELATIONSHIP.typeName, specificationGUID);

        DesignFvtSupport.linkCheckAndDetach(dataDesigner,
                                            "/data-definitions/" + dataFieldGUID + "/data-value-specification-definition/" + specificationGUID,
                                            retrieve(DATA_FIELDS, dataFieldGUID),
                                            OpenMetadataType.DATA_VALUE_DEFINITION_RELATIONSHIP.typeName,
                                            specificationGUID,
                                            DesignFvtSupport.labeled(new DataValueDefinitionProperties(), "defined format"));
    }


    /**
     * DataValueAssignmentProperties is a LabeledRelationshipProperties, so a caller can give the assignment a
     * label and description - but the DataValueAssignment relationship type has neither attribute, so the
     * repository refuses the request (OMRS-REPOSITORY-400-028).
     */
    @Test
    @Order(7)
    void labeledDataValueAssignment()
    {
        DesignFvtSupport.linkCheckAndDetach(dataDesigner,
                                            "/elements/" + dataFieldGUID + "/data-value-specifications/" + specificationGUID,
                                            retrieve(DATA_FIELDS, dataFieldGUID),
                                            OpenMetadataType.DATA_VALUE_ASSIGNMENT_RELATIONSHIP.typeName,
                                            specificationGUID,
                                            DesignFvtSupport.labeled(new DataValueAssignmentProperties(), "assigned format"));
    }


    /**
     * SemanticDefinition links a data definition to the glossary term that describes its meaning.
     */
    @Test
    @Order(8)
    void semanticDefinition()
    {
        GlossaryTermProperties termProperties = new GlossaryTermProperties();

        termProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("GlossaryTerm", "customer-name"));
        termProperties.setDisplayName("Customer Name");

        String termGUID = glossaryManager.create("/glossaries/terms", DesignFvtSupport.newElement(termProperties));

        try
        {
            DesignFvtSupport.linkCheckAndDetach(dataDesigner,
                                                "/data-definitions/" + dataFieldGUID + "/semantic-definition/" + termGUID,
                                                retrieve(DATA_FIELDS, dataFieldGUID),
                                                OpenMetadataType.SEMANTIC_DEFINITION_RELATIONSHIP.typeName,
                                                termGUID,
                                                DesignFvtSupport.labeled(new SemanticDefinitionProperties(), "means"));
        }
        finally
        {
            DesignFvtSupport.deleteAndCheck(glossaryManager, "/glossaries/terms/" + termGUID, "/glossaries/terms/" + termGUID);
        }
    }


    /**
     * DataStructureDefinition links a certification type to the data structure that a certified element
     * must conform to.
     */
    @Test
    @Order(9)
    void certificationTypeDataStructure()
    {
        CertificationTypeProperties certificationTypeProperties = new CertificationTypeProperties();

        certificationTypeProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("CertificationType", "customer-data"));
        certificationTypeProperties.setDisplayName("Certified customer data");

        String certificationTypeGUID = governanceOfficer.create("/governance-definitions", DesignFvtSupport.newElement(certificationTypeProperties));

        try
        {
            DesignFvtSupport.linkCheckAndDetach(dataDesigner,
                                                "/certification-types/" + certificationTypeGUID + "/data-structure-definition/" + dataStructureGUID,
                                                retrieve(DATA_STRUCTURES, dataStructureGUID),
                                                OpenMetadataType.DATA_STRUCTURE_DEFINITION_RELATIONSHIP.typeName,
                                                certificationTypeGUID,
                                                DesignFvtSupport.labeled(new DataStructureDefinitionProperties(), "certified structure"));
        }
        finally
        {
            DesignFvtSupport.deleteAndCheck(governanceOfficer,
                                            "/governance-definitions/" + certificationTypeGUID,
                                            "/governance-definitions/" + certificationTypeGUID + "/retrieve");
        }
    }


    /**
     * SchemaAttributeDefinition and SchemaTypeDefinition link the data design to the schema elements that
     * implement it.
     */
    @Test
    @Order(10)
    void schemaDefinitions()
    {
        SchemaAttributeProperties schemaAttributeProperties = new SchemaAttributeProperties();

        schemaAttributeProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("SchemaAttribute", "customer-name-column"));
        schemaAttributeProperties.setDisplayName("CUSTNAME");

        String schemaAttributeGUID = schemaMaker.create("/schema-attributes", DesignFvtSupport.newElement(schemaAttributeProperties));

        StructSchemaTypeProperties schemaTypeProperties = new StructSchemaTypeProperties();

        schemaTypeProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("StructSchemaType", "customer-table"));
        schemaTypeProperties.setDisplayName("CUSTOMER");

        String schemaTypeGUID = schemaMaker.create("/schema-types", DesignFvtSupport.newElement(schemaTypeProperties));

        try
        {
            DesignFvtSupport.linkCheckAndDetach(dataDesigner,
                                                DATA_FIELDS + dataFieldGUID + "/schema-attribute-definitions/" + schemaAttributeGUID,
                                                retrieve(DATA_FIELDS, dataFieldGUID),
                                                OpenMetadataType.SCHEMA_ATTRIBUTE_DEFINITION_RELATIONSHIP.typeName,
                                                schemaAttributeGUID,
                                                DesignFvtSupport.labeled(new SchemaAttributeDefinitionProperties(), "implemented by"));

            DesignFvtSupport.linkCheckAndDetach(dataDesigner,
                                                DATA_STRUCTURES + dataStructureGUID + "/schema-type-definitions/" + schemaTypeGUID,
                                                retrieve(DATA_STRUCTURES, dataStructureGUID),
                                                OpenMetadataType.SCHEMA_TYPE_DEFINITION_RELATIONSHIP.typeName,
                                                schemaTypeGUID,
                                                DesignFvtSupport.labeled(new SchemaTypeDefinitionProperties(), "implemented by"));
        }
        finally
        {
            DesignFvtSupport.deleteAndCheck(schemaMaker, "/schema-attributes/" + schemaAttributeGUID, "/schema-attributes/" + schemaAttributeGUID + "/retrieve");
            DesignFvtSupport.deleteAndCheck(schemaMaker, "/schema-types/" + schemaTypeGUID, "/schema-types/" + schemaTypeGUID + "/retrieve");
        }
    }


    /**
     * Delete everything this class created; deleted elements are no longer found.
     */
    @Test
    @Order(99)
    void deleteEverything()
    {
        for (String guid : createdDataFields)
        {
            DesignFvtSupport.deleteAndCheck(dataDesigner, DATA_FIELDS + guid, retrieve(DATA_FIELDS, guid));
        }

        for (String guid : createdDataStructures)
        {
            DesignFvtSupport.deleteAndCheck(dataDesigner, DATA_STRUCTURES + guid, retrieve(DATA_STRUCTURES, guid));
        }

        for (String guid : createdSpecifications)
        {
            DesignFvtSupport.deleteAndCheck(dataDesigner, DVS + guid, retrieve(DVS, guid));
        }

        if (dataStructureGUID != null)
        {
            DesignFvtSupport.assertNotContains(dataDesigner.elements("/data-structures/by-search-string", DesignFvtSupport.runSearch()),
                                               dataStructureGUID, "findDataStructures after delete");
        }

        if (dataFieldGUID != null)
        {
            DesignFvtSupport.assertNotContains(dataDesigner.elements("/data-fields/by-search-string", DesignFvtSupport.runSearch()),
                                               dataFieldGUID, "findDataFields after delete");
        }

        if (specificationGUID != null)
        {
            DesignFvtSupport.assertNotContains(dataDesigner.elements("/data-value-specifications/by-search-string", DesignFvtSupport.runSearch()),
                                               specificationGUID, "findDataValueSpecifications after delete");
        }
    }


    /*
     * Helpers
     */

    private static String createDataStructure(String name)
    {
        DataStructureProperties properties = new DataStructureProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("DataStructure", name));
        properties.setDisplayName(name);

        String guid = dataDesigner.create("/data-structures", DesignFvtSupport.newElement(properties));

        createdDataStructures.add(0, guid);

        return guid;
    }


    private static String createDataField(String name)
    {
        DataFieldProperties properties = new DataFieldProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("DataField", name));
        properties.setDisplayName(name);

        String guid = dataDesigner.create("/data-fields", DesignFvtSupport.newElement(properties));

        createdDataFields.add(0, guid);

        return guid;
    }


    private static String createSpecification(DataValueSpecificationProperties properties,
                                              String                           name)
    {
        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName(properties.getTypeName(), name));
        properties.setDisplayName(name);

        String guid = dataDesigner.create("/data-value-specifications", DesignFvtSupport.newElement(properties));

        createdSpecifications.add(0, guid);

        return guid;
    }


    private static OpenMetadataRootElement read(String collection,
                                                String guid)
    {
        return dataDesigner.element(retrieve(collection, guid), new GetRequestBody());
    }


    private static String retrieve(String collection,
                                   String guid)
    {
        return collection + guid + "/retrieve";
    }
}
