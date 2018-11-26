/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'xml_schema_definition' asset type in IGC, displayed as 'XML Schema Definition' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class XmlSchemaDefinition extends MainObject {

    public static final String IGC_TYPE_ID = "xml_schema_definition";

    /**
     * The 'target_namespace' property, displayed as 'Namespace' in the IGC UI.
     */
    protected String target_namespace;

    /**
     * The 'xml_schema_library' property, displayed as 'XML Schema Library' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XmlSchemaLibrary} objects.
     */
    protected ReferenceList xml_schema_library;

    /**
     * The 'contains_xsd_elements' property, displayed as 'XSD Elements' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList contains_xsd_elements;

    /**
     * The 'contains_xsd_attributes' property, displayed as 'XSD Attributes' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdAttribute} objects.
     */
    protected ReferenceList contains_xsd_attributes;

    /**
     * The 'contains_xsd_simple_types' property, displayed as 'XSD Simple Types' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdSimpleType} objects.
     */
    protected ReferenceList contains_xsd_simple_types;

    /**
     * The 'contains_xsd_complex_types' property, displayed as 'XSD Complex Types' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdComplexType} objects.
     */
    protected ReferenceList contains_xsd_complex_types;

    /**
     * The 'contains_xsd_element_groups' property, displayed as 'XSD Element Groups' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdElementGroup} objects.
     */
    protected ReferenceList contains_xsd_element_groups;

    /**
     * The 'contains_xsd_attribute_groups' property, displayed as 'XSD Attribute Groups' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdAttributeGroup} objects.
     */
    protected ReferenceList contains_xsd_attribute_groups;

    /**
     * The 'includes_xsd_schemas' property, displayed as 'Includes XML Schema Definitions' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList includes_xsd_schemas;

    /**
     * The 'imports_xsd_schemas' property, displayed as 'Imports XML Schema Definitions' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList imports_xsd_schemas;

    /**
     * The 'redefines_xsd_schemas' property, displayed as 'Redefines XML Schema Definitions' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList redefines_xsd_schemas;

    /**
     * The 'overrides_xsd_schemas' property, displayed as 'Overrides XML Schema Definitions' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList overrides_xsd_schemas;

    /**
     * The 'id' property, displayed as 'Id' in the IGC UI.
     */
    protected String id;

    /**
     * The 'xpath' property, displayed as 'Path' in the IGC UI.
     */
    protected String xpath;

    /**
     * The 'default_namespace' property, displayed as 'Default Namespace' in the IGC UI.
     */
    protected String default_namespace;

    /**
     * The 'language' property, displayed as 'Language' in the IGC UI.
     */
    protected String language;

    /**
     * The 'version' property, displayed as 'Version' in the IGC UI.
     */
    protected String version;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #target_namespace */ @JsonProperty("target_namespace")  public String getTargetNamespace() { return this.target_namespace; }
    /** @see #target_namespace */ @JsonProperty("target_namespace")  public void setTargetNamespace(String target_namespace) { this.target_namespace = target_namespace; }

    /** @see #xml_schema_library */ @JsonProperty("xml_schema_library")  public ReferenceList getXmlSchemaLibrary() { return this.xml_schema_library; }
    /** @see #xml_schema_library */ @JsonProperty("xml_schema_library")  public void setXmlSchemaLibrary(ReferenceList xml_schema_library) { this.xml_schema_library = xml_schema_library; }

    /** @see #contains_xsd_elements */ @JsonProperty("contains_xsd_elements")  public ReferenceList getContainsXsdElements() { return this.contains_xsd_elements; }
    /** @see #contains_xsd_elements */ @JsonProperty("contains_xsd_elements")  public void setContainsXsdElements(ReferenceList contains_xsd_elements) { this.contains_xsd_elements = contains_xsd_elements; }

    /** @see #contains_xsd_attributes */ @JsonProperty("contains_xsd_attributes")  public ReferenceList getContainsXsdAttributes() { return this.contains_xsd_attributes; }
    /** @see #contains_xsd_attributes */ @JsonProperty("contains_xsd_attributes")  public void setContainsXsdAttributes(ReferenceList contains_xsd_attributes) { this.contains_xsd_attributes = contains_xsd_attributes; }

    /** @see #contains_xsd_simple_types */ @JsonProperty("contains_xsd_simple_types")  public ReferenceList getContainsXsdSimpleTypes() { return this.contains_xsd_simple_types; }
    /** @see #contains_xsd_simple_types */ @JsonProperty("contains_xsd_simple_types")  public void setContainsXsdSimpleTypes(ReferenceList contains_xsd_simple_types) { this.contains_xsd_simple_types = contains_xsd_simple_types; }

    /** @see #contains_xsd_complex_types */ @JsonProperty("contains_xsd_complex_types")  public ReferenceList getContainsXsdComplexTypes() { return this.contains_xsd_complex_types; }
    /** @see #contains_xsd_complex_types */ @JsonProperty("contains_xsd_complex_types")  public void setContainsXsdComplexTypes(ReferenceList contains_xsd_complex_types) { this.contains_xsd_complex_types = contains_xsd_complex_types; }

    /** @see #contains_xsd_element_groups */ @JsonProperty("contains_xsd_element_groups")  public ReferenceList getContainsXsdElementGroups() { return this.contains_xsd_element_groups; }
    /** @see #contains_xsd_element_groups */ @JsonProperty("contains_xsd_element_groups")  public void setContainsXsdElementGroups(ReferenceList contains_xsd_element_groups) { this.contains_xsd_element_groups = contains_xsd_element_groups; }

    /** @see #contains_xsd_attribute_groups */ @JsonProperty("contains_xsd_attribute_groups")  public ReferenceList getContainsXsdAttributeGroups() { return this.contains_xsd_attribute_groups; }
    /** @see #contains_xsd_attribute_groups */ @JsonProperty("contains_xsd_attribute_groups")  public void setContainsXsdAttributeGroups(ReferenceList contains_xsd_attribute_groups) { this.contains_xsd_attribute_groups = contains_xsd_attribute_groups; }

    /** @see #includes_xsd_schemas */ @JsonProperty("includes_xsd_schemas")  public ReferenceList getIncludesXsdSchemas() { return this.includes_xsd_schemas; }
    /** @see #includes_xsd_schemas */ @JsonProperty("includes_xsd_schemas")  public void setIncludesXsdSchemas(ReferenceList includes_xsd_schemas) { this.includes_xsd_schemas = includes_xsd_schemas; }

    /** @see #imports_xsd_schemas */ @JsonProperty("imports_xsd_schemas")  public ReferenceList getImportsXsdSchemas() { return this.imports_xsd_schemas; }
    /** @see #imports_xsd_schemas */ @JsonProperty("imports_xsd_schemas")  public void setImportsXsdSchemas(ReferenceList imports_xsd_schemas) { this.imports_xsd_schemas = imports_xsd_schemas; }

    /** @see #redefines_xsd_schemas */ @JsonProperty("redefines_xsd_schemas")  public ReferenceList getRedefinesXsdSchemas() { return this.redefines_xsd_schemas; }
    /** @see #redefines_xsd_schemas */ @JsonProperty("redefines_xsd_schemas")  public void setRedefinesXsdSchemas(ReferenceList redefines_xsd_schemas) { this.redefines_xsd_schemas = redefines_xsd_schemas; }

    /** @see #overrides_xsd_schemas */ @JsonProperty("overrides_xsd_schemas")  public ReferenceList getOverridesXsdSchemas() { return this.overrides_xsd_schemas; }
    /** @see #overrides_xsd_schemas */ @JsonProperty("overrides_xsd_schemas")  public void setOverridesXsdSchemas(ReferenceList overrides_xsd_schemas) { this.overrides_xsd_schemas = overrides_xsd_schemas; }

    /** @see #id */ @JsonProperty("id")  public String getTheId() { return this.id; }
    /** @see #id */ @JsonProperty("id")  public void setTheId(String id) { this.id = id; }

    /** @see #xpath */ @JsonProperty("xpath")  public String getXpath() { return this.xpath; }
    /** @see #xpath */ @JsonProperty("xpath")  public void setXpath(String xpath) { this.xpath = xpath; }

    /** @see #default_namespace */ @JsonProperty("default_namespace")  public String getDefaultNamespace() { return this.default_namespace; }
    /** @see #default_namespace */ @JsonProperty("default_namespace")  public void setDefaultNamespace(String default_namespace) { this.default_namespace = default_namespace; }

    /** @see #language */ @JsonProperty("language")  public String getLanguage() { return this.language; }
    /** @see #language */ @JsonProperty("language")  public void setLanguage(String language) { this.language = language; }

    /** @see #version */ @JsonProperty("version")  public String getVersion() { return this.version; }
    /** @see #version */ @JsonProperty("version")  public void setVersion(String version) { this.version = version; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isXmlSchemaDefinition(Object obj) { return (obj.getClass() == XmlSchemaDefinition.class); }

}
