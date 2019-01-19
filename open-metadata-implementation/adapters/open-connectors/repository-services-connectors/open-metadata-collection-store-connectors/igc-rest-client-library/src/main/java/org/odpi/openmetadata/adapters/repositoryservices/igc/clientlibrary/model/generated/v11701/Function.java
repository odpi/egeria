/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11701;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'function' asset type in IGC, displayed as 'Function' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Function extends Reference {

    public static String getIgcTypeId() { return "function"; }
    public static String getIgcTypeDisplayName() { return "Function"; }

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'short_description' property, displayed as 'Short Description' in the IGC UI.
     */
    protected String short_description;

    /**
     * The 'long_description' property, displayed as 'Long Description' in the IGC UI.
     */
    protected String long_description;

    /**
     * The 'labels' property, displayed as 'Labels' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Label} objects.
     */
    protected ReferenceList labels;

    /**
     * The 'stewards' property, displayed as 'Stewards' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AsclSteward} objects.
     */
    protected ReferenceList stewards;

    /**
     * The 'assigned_to_terms' property, displayed as 'Assigned to Terms' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The 'implements_rules' property, displayed as 'Implements Rules' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList implements_rules;

    /**
     * The 'governed_by_rules' property, displayed as 'Governed by Rules' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList governed_by_rules;

    /**
     * The 'module_name' property, displayed as 'Module Name' in the IGC UI.
     */
    protected String module_name;

    /**
     * The 'module_path' property, displayed as 'Module Path' in the IGC UI.
     */
    protected String module_path;

    /**
     * The 'is_inline' property, displayed as 'Is Inline' in the IGC UI.
     */
    protected Boolean is_inline;

    /**
     * The 'source_code' property, displayed as 'Source Code' in the IGC UI.
     */
    protected String source_code;

    /**
     * The 'vendor' property, displayed as 'Vendor' in the IGC UI.
     */
    protected String vendor;

    /**
     * The 'has_function_call' property, displayed as 'Has Function Call' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link FunctionCall2} objects.
     */
    protected ReferenceList has_function_call;

    /**
     * The 'returns_parameter_def' property, displayed as 'Returns Parameter Def' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Parameter} object.
     */
    protected Reference returns_parameter_def;

    /**
     * The 'platform_type' property, displayed as 'Platform Type' in the IGC UI.
     */
    protected String platform_type;

    /**
     * The 'external_name' property, displayed as 'External Name' in the IGC UI.
     */
    protected String external_name;

    /**
     * The 'language' property, displayed as 'Language' in the IGC UI.
     */
    protected String language;

    /**
     * The 'version' property, displayed as 'Version' in the IGC UI.
     */
    protected String version;

    /**
     * The 'executed_by_function_call' property, displayed as 'Executed By Function Call' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link FunctionCall2} objects.
     */
    protected ReferenceList executed_by_function_call;

    /**
     * The 'has_parameter_def' property, displayed as 'Has Parameter Def' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Parameter} objects.
     */
    protected ReferenceList has_parameter_def;

    /**
     * The 'author' property, displayed as 'Author' in the IGC UI.
     */
    protected String author;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

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

    /** @see #module_name */ @JsonProperty("module_name")  public String getModuleName() { return this.module_name; }
    /** @see #module_name */ @JsonProperty("module_name")  public void setModuleName(String module_name) { this.module_name = module_name; }

    /** @see #module_path */ @JsonProperty("module_path")  public String getModulePath() { return this.module_path; }
    /** @see #module_path */ @JsonProperty("module_path")  public void setModulePath(String module_path) { this.module_path = module_path; }

    /** @see #is_inline */ @JsonProperty("is_inline")  public Boolean getIsInline() { return this.is_inline; }
    /** @see #is_inline */ @JsonProperty("is_inline")  public void setIsInline(Boolean is_inline) { this.is_inline = is_inline; }

    /** @see #source_code */ @JsonProperty("source_code")  public String getSourceCode() { return this.source_code; }
    /** @see #source_code */ @JsonProperty("source_code")  public void setSourceCode(String source_code) { this.source_code = source_code; }

    /** @see #vendor */ @JsonProperty("vendor")  public String getVendor() { return this.vendor; }
    /** @see #vendor */ @JsonProperty("vendor")  public void setVendor(String vendor) { this.vendor = vendor; }

    /** @see #has_function_call */ @JsonProperty("has_function_call")  public ReferenceList getHasFunctionCall() { return this.has_function_call; }
    /** @see #has_function_call */ @JsonProperty("has_function_call")  public void setHasFunctionCall(ReferenceList has_function_call) { this.has_function_call = has_function_call; }

    /** @see #returns_parameter_def */ @JsonProperty("returns_parameter_def")  public Reference getReturnsParameterDef() { return this.returns_parameter_def; }
    /** @see #returns_parameter_def */ @JsonProperty("returns_parameter_def")  public void setReturnsParameterDef(Reference returns_parameter_def) { this.returns_parameter_def = returns_parameter_def; }

    /** @see #platform_type */ @JsonProperty("platform_type")  public String getPlatformType() { return this.platform_type; }
    /** @see #platform_type */ @JsonProperty("platform_type")  public void setPlatformType(String platform_type) { this.platform_type = platform_type; }

    /** @see #external_name */ @JsonProperty("external_name")  public String getExternalName() { return this.external_name; }
    /** @see #external_name */ @JsonProperty("external_name")  public void setExternalName(String external_name) { this.external_name = external_name; }

    /** @see #language */ @JsonProperty("language")  public String getLanguage() { return this.language; }
    /** @see #language */ @JsonProperty("language")  public void setLanguage(String language) { this.language = language; }

    /** @see #version */ @JsonProperty("version")  public String getVersion() { return this.version; }
    /** @see #version */ @JsonProperty("version")  public void setVersion(String version) { this.version = version; }

    /** @see #executed_by_function_call */ @JsonProperty("executed_by_function_call")  public ReferenceList getExecutedByFunctionCall() { return this.executed_by_function_call; }
    /** @see #executed_by_function_call */ @JsonProperty("executed_by_function_call")  public void setExecutedByFunctionCall(ReferenceList executed_by_function_call) { this.executed_by_function_call = executed_by_function_call; }

    /** @see #has_parameter_def */ @JsonProperty("has_parameter_def")  public ReferenceList getHasParameterDef() { return this.has_parameter_def; }
    /** @see #has_parameter_def */ @JsonProperty("has_parameter_def")  public void setHasParameterDef(ReferenceList has_parameter_def) { this.has_parameter_def = has_parameter_def; }

    /** @see #author */ @JsonProperty("author")  public String getAuthor() { return this.author; }
    /** @see #author */ @JsonProperty("author")  public void setAuthor(String author) { this.author = author; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "module_name",
        "module_path",
        "is_inline",
        "source_code",
        "vendor",
        "platform_type",
        "external_name",
        "language",
        "version",
        "author"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "module_name",
        "module_path",
        "source_code",
        "vendor",
        "platform_type",
        "external_name",
        "language",
        "version",
        "author"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "has_function_call",
        "executed_by_function_call",
        "has_parameter_def"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "module_name",
        "module_path",
        "is_inline",
        "source_code",
        "vendor",
        "has_function_call",
        "returns_parameter_def",
        "platform_type",
        "external_name",
        "language",
        "version",
        "executed_by_function_call",
        "has_parameter_def",
        "author"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isFunction(Object obj) { return (obj.getClass() == Function.class); }

}
