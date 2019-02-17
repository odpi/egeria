/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11701sp1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code xml_schema_definition} asset type in IGC, displayed as '{@literal XML Schema Definition}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class XmlSchemaDefinition extends Reference {

    public static String getIgcTypeId() { return "xml_schema_definition"; }
    public static String getIgcTypeDisplayName() { return "XML Schema Definition"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code short_description} property, displayed as '{@literal Short Description}' in the IGC UI.
     */
    protected String short_description;

    /**
     * The {@code long_description} property, displayed as '{@literal Long Description}' in the IGC UI.
     */
    protected String long_description;

    /**
     * The {@code target_namespace} property, displayed as '{@literal Namespace}' in the IGC UI.
     */
    protected String target_namespace;

    /**
     * The {@code xml_schema_library} property, displayed as '{@literal XML Schema Library}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XmlSchemaLibrary} objects.
     */
    protected ReferenceList xml_schema_library;

    /**
     * The {@code labels} property, displayed as '{@literal Labels}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Label} objects.
     */
    protected ReferenceList labels;

    /**
     * The {@code stewards} property, displayed as '{@literal Stewards}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AsclSteward} objects.
     */
    protected ReferenceList stewards;

    /**
     * The {@code assigned_to_terms} property, displayed as '{@literal Assigned to Terms}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The {@code implements_rules} property, displayed as '{@literal Implements Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList implements_rules;

    /**
     * The {@code governed_by_rules} property, displayed as '{@literal Governed by Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList governed_by_rules;

    /**
     * The {@code contains_xsd_elements} property, displayed as '{@literal XSD Elements}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList contains_xsd_elements;

    /**
     * The {@code contains_xsd_attributes} property, displayed as '{@literal XSD Attributes}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdAttribute} objects.
     */
    protected ReferenceList contains_xsd_attributes;

    /**
     * The {@code contains_xsd_simple_types} property, displayed as '{@literal XSD Simple Types}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdSimpleType} objects.
     */
    protected ReferenceList contains_xsd_simple_types;

    /**
     * The {@code contains_xsd_complex_types} property, displayed as '{@literal XSD Complex Types}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdComplexType} objects.
     */
    protected ReferenceList contains_xsd_complex_types;

    /**
     * The {@code contains_xsd_element_groups} property, displayed as '{@literal XSD Element Groups}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdElementGroup} objects.
     */
    protected ReferenceList contains_xsd_element_groups;

    /**
     * The {@code contains_xsd_attribute_groups} property, displayed as '{@literal XSD Attribute Groups}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdAttributeGroup} objects.
     */
    protected ReferenceList contains_xsd_attribute_groups;

    /**
     * The {@code includes_xsd_schemas} property, displayed as '{@literal Includes XML Schema Definitions}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList includes_xsd_schemas;

    /**
     * The {@code imports_xsd_schemas} property, displayed as '{@literal Imports XML Schema Definitions}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList imports_xsd_schemas;

    /**
     * The {@code redefines_xsd_schemas} property, displayed as '{@literal Redefines XML Schema Definitions}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList redefines_xsd_schemas;

    /**
     * The {@code overrides_xsd_schemas} property, displayed as '{@literal Overrides XML Schema Definitions}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList overrides_xsd_schemas;

    /**
     * The {@code id} property, displayed as '{@literal Id}' in the IGC UI.
     */
    protected String id;

    /**
     * The {@code xpath} property, displayed as '{@literal Path}' in the IGC UI.
     */
    protected String xpath;

    /**
     * The {@code default_namespace} property, displayed as '{@literal Default Namespace}' in the IGC UI.
     */
    protected String default_namespace;

    /**
     * The {@code language} property, displayed as '{@literal Language}' in the IGC UI.
     */
    protected String language;

    /**
     * The {@code version} property, displayed as '{@literal Version}' in the IGC UI.
     */
    protected String version;

    /**
     * The {@code in_collections} property, displayed as '{@literal In Collections}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;

    /**
     * The {@code created_by} property, displayed as '{@literal Created By}' in the IGC UI.
     */
    protected String created_by;

    /**
     * The {@code created_on} property, displayed as '{@literal Created On}' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The {@code modified_by} property, displayed as '{@literal Modified By}' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The {@code modified_on} property, displayed as '{@literal Modified On}' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #target_namespace */ @JsonProperty("target_namespace")  public String getTargetNamespace() { return this.target_namespace; }
    /** @see #target_namespace */ @JsonProperty("target_namespace")  public void setTargetNamespace(String target_namespace) { this.target_namespace = target_namespace; }

    /** @see #xml_schema_library */ @JsonProperty("xml_schema_library")  public ReferenceList getXmlSchemaLibrary() { return this.xml_schema_library; }
    /** @see #xml_schema_library */ @JsonProperty("xml_schema_library")  public void setXmlSchemaLibrary(ReferenceList xml_schema_library) { this.xml_schema_library = xml_schema_library; }

    /** @see #labels */ @JsonProperty("labels")  public ReferenceList getLabels() { return this.labels; }
    /** @see #labels */ @JsonProperty("labels")  public void setLabels(ReferenceList labels) { this.labels = labels; }

    /** @see #stewards */ @JsonProperty("stewards")  public ReferenceList getStewards() { return this.stewards; }
    /** @see #stewards */ @JsonProperty("stewards")  public void setStewards(ReferenceList stewards) { this.stewards = stewards; }

    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public ReferenceList getAssignedToTerms() { return this.assigned_to_terms; }
    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public void setAssignedToTerms(ReferenceList assigned_to_terms) { this.assigned_to_terms = assigned_to_terms; }

    /** @see #implements_rules */ @JsonProperty("implements_rules")  public ReferenceList getImplementsRules() { return this.implements_rules; }
    /** @see #implements_rules */ @JsonProperty("implements_rules")  public void setImplementsRules(ReferenceList implements_rules) { this.implements_rules = implements_rules; }

    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public ReferenceList getGovernedByRules() { return this.governed_by_rules; }
    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public void setGovernedByRules(ReferenceList governed_by_rules) { this.governed_by_rules = governed_by_rules; }

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

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return true; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "target_namespace",
        "id",
        "xpath",
        "default_namespace",
        "language",
        "version",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "target_namespace",
        "id",
        "xpath",
        "default_namespace",
        "language",
        "version",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "xml_schema_library",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "contains_xsd_elements",
        "contains_xsd_attributes",
        "contains_xsd_simple_types",
        "contains_xsd_complex_types",
        "contains_xsd_element_groups",
        "contains_xsd_attribute_groups",
        "includes_xsd_schemas",
        "imports_xsd_schemas",
        "redefines_xsd_schemas",
        "overrides_xsd_schemas",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "target_namespace",
        "xml_schema_library",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "contains_xsd_elements",
        "contains_xsd_attributes",
        "contains_xsd_simple_types",
        "contains_xsd_complex_types",
        "contains_xsd_element_groups",
        "contains_xsd_attribute_groups",
        "includes_xsd_schemas",
        "imports_xsd_schemas",
        "redefines_xsd_schemas",
        "overrides_xsd_schemas",
        "id",
        "xpath",
        "default_namespace",
        "language",
        "version",
        "in_collections",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isXmlSchemaDefinition(Object obj) { return (obj.getClass() == XmlSchemaDefinition.class); }

}
