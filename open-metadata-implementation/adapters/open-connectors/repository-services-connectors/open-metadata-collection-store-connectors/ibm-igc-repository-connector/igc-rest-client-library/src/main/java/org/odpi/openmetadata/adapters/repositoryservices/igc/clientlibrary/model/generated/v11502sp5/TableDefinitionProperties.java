/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11502sp5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code table_definition_properties} asset type in IGC, displayed as '{@literal Table Definition Properties}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
public class TableDefinitionProperties extends Reference {

    public static String getIgcTypeId() { return "table_definition_properties"; }
    public static String getIgcTypeDisplayName() { return "Table Definition Properties"; }

    /**
     * The {@code of_ds_table_definition} property, displayed as '{@literal Of DS Table Definition}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link TableDefinition} object.
     */
    protected Reference of_ds_table_definition;

    /**
     * The {@code seq_col_space} property, displayed as '{@literal SEQ Col Space}' in the IGC UI.
     */
    protected Number seq_col_space;

    /**
     * The {@code seq_fixed_width} property, displayed as '{@literal SEQ Fixed Width}' in the IGC UI.
     */
    protected Boolean seq_fixed_width;

    /**
     * The {@code seq_delimiter} property, displayed as '{@literal SEQ Delimiter}' in the IGC UI.
     */
    protected String seq_delimiter;

    /**
     * The {@code seq_quote_char} property, displayed as '{@literal SEQ Quote Char}' in the IGC UI.
     */
    protected String seq_quote_char;

    /**
     * The {@code locator} property, displayed as '{@literal Locator}' in the IGC UI.
     */
    protected String locator;

    /**
     * The {@code access_type} property, displayed as '{@literal Access Type}' in the IGC UI.
     */
    protected String access_type;

    /**
     * The {@code apt_record_prop} property, displayed as '{@literal APT Record Prop}' in the IGC UI.
     */
    protected String apt_record_prop;

    /**
     * The {@code seq_col_headers} property, displayed as '{@literal SEQ Col Headers}' in the IGC UI.
     */
    protected Boolean seq_col_headers;

    /**
     * The {@code import_location} property, displayed as '{@literal Import Location}' in the IGC UI.
     */
    protected String import_location;

    /**
     * The {@code nls_map_name} property, displayed as '{@literal NLS Map Name}' in the IGC UI.
     */
    protected String nls_map_name;

    /**
     * The {@code seq_omit_new_line} property, displayed as '{@literal SEQ Omit New Line}' in the IGC UI.
     */
    protected Boolean seq_omit_new_line;

    /**
     * The {@code platform_type} property, displayed as '{@literal Platform Type}' in the IGC UI.
     */
    protected String platform_type;

    /**
     * The {@code version} property, displayed as '{@literal Version}' in the IGC UI.
     */
    protected String version;

    /**
     * The {@code a_xmeta_locking_root} property, displayed as '{@literal A XMeta Locking Root}' in the IGC UI.
     */
    protected String a_xmeta_locking_root;

    /**
     * The {@code registration_timestamp} property, displayed as '{@literal Registration Timestamp}' in the IGC UI.
     */
    protected String registration_timestamp;

    /**
     * The {@code allow_column_mapping} property, displayed as '{@literal Allow Column Mapping}' in the IGC UI.
     */
    protected Boolean allow_column_mapping;

    /**
     * The {@code sp_error_codes} property, displayed as '{@literal SP Error Codes}' in the IGC UI.
     */
    protected String sp_error_codes;

    /**
     * The {@code seq_null_string} property, displayed as '{@literal SEQ Null String}' in the IGC UI.
     */
    protected String seq_null_string;

    /**
     * The {@code multivalued} property, displayed as '{@literal Multivalued}' in the IGC UI.
     */
    protected Boolean multivalued;

    /**
     * The {@code pad_char} property, displayed as '{@literal Pad Char}' in the IGC UI.
     */
    protected String pad_char;

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


    /** @see #of_ds_table_definition */ @JsonProperty("of_ds_table_definition")  public Reference getOfDsTableDefinition() { return this.of_ds_table_definition; }
    /** @see #of_ds_table_definition */ @JsonProperty("of_ds_table_definition")  public void setOfDsTableDefinition(Reference of_ds_table_definition) { this.of_ds_table_definition = of_ds_table_definition; }

    /** @see #seq_col_space */ @JsonProperty("seq_col_space")  public Number getSeqColSpace() { return this.seq_col_space; }
    /** @see #seq_col_space */ @JsonProperty("seq_col_space")  public void setSeqColSpace(Number seq_col_space) { this.seq_col_space = seq_col_space; }

    /** @see #seq_fixed_width */ @JsonProperty("seq_fixed_width")  public Boolean getSeqFixedWidth() { return this.seq_fixed_width; }
    /** @see #seq_fixed_width */ @JsonProperty("seq_fixed_width")  public void setSeqFixedWidth(Boolean seq_fixed_width) { this.seq_fixed_width = seq_fixed_width; }

    /** @see #seq_delimiter */ @JsonProperty("seq_delimiter")  public String getSeqDelimiter() { return this.seq_delimiter; }
    /** @see #seq_delimiter */ @JsonProperty("seq_delimiter")  public void setSeqDelimiter(String seq_delimiter) { this.seq_delimiter = seq_delimiter; }

    /** @see #seq_quote_char */ @JsonProperty("seq_quote_char")  public String getSeqQuoteChar() { return this.seq_quote_char; }
    /** @see #seq_quote_char */ @JsonProperty("seq_quote_char")  public void setSeqQuoteChar(String seq_quote_char) { this.seq_quote_char = seq_quote_char; }

    /** @see #locator */ @JsonProperty("locator")  public String getLocator() { return this.locator; }
    /** @see #locator */ @JsonProperty("locator")  public void setLocator(String locator) { this.locator = locator; }

    /** @see #access_type */ @JsonProperty("access_type")  public String getAccessType() { return this.access_type; }
    /** @see #access_type */ @JsonProperty("access_type")  public void setAccessType(String access_type) { this.access_type = access_type; }

    /** @see #apt_record_prop */ @JsonProperty("apt_record_prop")  public String getAptRecordProp() { return this.apt_record_prop; }
    /** @see #apt_record_prop */ @JsonProperty("apt_record_prop")  public void setAptRecordProp(String apt_record_prop) { this.apt_record_prop = apt_record_prop; }

    /** @see #seq_col_headers */ @JsonProperty("seq_col_headers")  public Boolean getSeqColHeaders() { return this.seq_col_headers; }
    /** @see #seq_col_headers */ @JsonProperty("seq_col_headers")  public void setSeqColHeaders(Boolean seq_col_headers) { this.seq_col_headers = seq_col_headers; }

    /** @see #import_location */ @JsonProperty("import_location")  public String getImportLocation() { return this.import_location; }
    /** @see #import_location */ @JsonProperty("import_location")  public void setImportLocation(String import_location) { this.import_location = import_location; }

    /** @see #nls_map_name */ @JsonProperty("nls_map_name")  public String getNlsMapName() { return this.nls_map_name; }
    /** @see #nls_map_name */ @JsonProperty("nls_map_name")  public void setNlsMapName(String nls_map_name) { this.nls_map_name = nls_map_name; }

    /** @see #seq_omit_new_line */ @JsonProperty("seq_omit_new_line")  public Boolean getSeqOmitNewLine() { return this.seq_omit_new_line; }
    /** @see #seq_omit_new_line */ @JsonProperty("seq_omit_new_line")  public void setSeqOmitNewLine(Boolean seq_omit_new_line) { this.seq_omit_new_line = seq_omit_new_line; }

    /** @see #platform_type */ @JsonProperty("platform_type")  public String getPlatformType() { return this.platform_type; }
    /** @see #platform_type */ @JsonProperty("platform_type")  public void setPlatformType(String platform_type) { this.platform_type = platform_type; }

    /** @see #version */ @JsonProperty("version")  public String getVersion() { return this.version; }
    /** @see #version */ @JsonProperty("version")  public void setVersion(String version) { this.version = version; }

    /** @see #a_xmeta_locking_root */ @JsonProperty("a_xmeta_locking_root")  public String getAXmetaLockingRoot() { return this.a_xmeta_locking_root; }
    /** @see #a_xmeta_locking_root */ @JsonProperty("a_xmeta_locking_root")  public void setAXmetaLockingRoot(String a_xmeta_locking_root) { this.a_xmeta_locking_root = a_xmeta_locking_root; }

    /** @see #registration_timestamp */ @JsonProperty("registration_timestamp")  public String getRegistrationTimestamp() { return this.registration_timestamp; }
    /** @see #registration_timestamp */ @JsonProperty("registration_timestamp")  public void setRegistrationTimestamp(String registration_timestamp) { this.registration_timestamp = registration_timestamp; }

    /** @see #allow_column_mapping */ @JsonProperty("allow_column_mapping")  public Boolean getAllowColumnMapping() { return this.allow_column_mapping; }
    /** @see #allow_column_mapping */ @JsonProperty("allow_column_mapping")  public void setAllowColumnMapping(Boolean allow_column_mapping) { this.allow_column_mapping = allow_column_mapping; }

    /** @see #sp_error_codes */ @JsonProperty("sp_error_codes")  public String getSpErrorCodes() { return this.sp_error_codes; }
    /** @see #sp_error_codes */ @JsonProperty("sp_error_codes")  public void setSpErrorCodes(String sp_error_codes) { this.sp_error_codes = sp_error_codes; }

    /** @see #seq_null_string */ @JsonProperty("seq_null_string")  public String getSeqNullString() { return this.seq_null_string; }
    /** @see #seq_null_string */ @JsonProperty("seq_null_string")  public void setSeqNullString(String seq_null_string) { this.seq_null_string = seq_null_string; }

    /** @see #multivalued */ @JsonProperty("multivalued")  public Boolean getMultivalued() { return this.multivalued; }
    /** @see #multivalued */ @JsonProperty("multivalued")  public void setMultivalued(Boolean multivalued) { this.multivalued = multivalued; }

    /** @see #pad_char */ @JsonProperty("pad_char")  public String getPadChar() { return this.pad_char; }
    /** @see #pad_char */ @JsonProperty("pad_char")  public void setPadChar(String pad_char) { this.pad_char = pad_char; }

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
        "seq_col_space",
        "seq_fixed_width",
        "seq_delimiter",
        "seq_quote_char",
        "locator",
        "access_type",
        "apt_record_prop",
        "seq_col_headers",
        "import_location",
        "nls_map_name",
        "seq_omit_new_line",
        "platform_type",
        "version",
        "a_xmeta_locking_root",
        "registration_timestamp",
        "allow_column_mapping",
        "sp_error_codes",
        "seq_null_string",
        "multivalued",
        "pad_char",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "seq_delimiter",
        "seq_quote_char",
        "locator",
        "access_type",
        "apt_record_prop",
        "import_location",
        "nls_map_name",
        "platform_type",
        "version",
        "a_xmeta_locking_root",
        "registration_timestamp",
        "sp_error_codes",
        "seq_null_string",
        "pad_char",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "of_ds_table_definition",
        "seq_col_space",
        "seq_fixed_width",
        "seq_delimiter",
        "seq_quote_char",
        "locator",
        "access_type",
        "apt_record_prop",
        "seq_col_headers",
        "import_location",
        "nls_map_name",
        "seq_omit_new_line",
        "platform_type",
        "version",
        "a_xmeta_locking_root",
        "registration_timestamp",
        "allow_column_mapping",
        "sp_error_codes",
        "seq_null_string",
        "multivalued",
        "pad_char",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isTableDefinitionProperties(Object obj) { return (obj.getClass() == TableDefinitionProperties.class); }

}
