/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'function' asset type in IGC, displayed as 'Function' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Function extends MainObject {

    public static final String IGC_TYPE_ID = "function";

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


    public static final Boolean isFunction(Object obj) { return (obj.getClass() == Function.class); }

}
