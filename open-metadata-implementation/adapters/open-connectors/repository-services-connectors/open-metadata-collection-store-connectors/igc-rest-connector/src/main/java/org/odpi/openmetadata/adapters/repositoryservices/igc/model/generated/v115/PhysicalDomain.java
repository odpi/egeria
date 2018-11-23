/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'physical_domain' asset type in IGC, displayed as 'Physical Domain' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class PhysicalDomain extends MainObject {

    public static final String IGC_TYPE_ID = "physical_domain";

    /**
     * The 'short_&amp;_long_description' property, displayed as 'Short &amp; Long Description' in the IGC UI.
     */
    @JsonProperty("short_&_long_description") protected String short___long_description;

    /**
     * The 'physical_data_model' property, displayed as 'Physical Data Model' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link PhysicalDataModel} object.
     */
    protected Reference physical_data_model;

    /**
     * The 'used_by_data_items' property, displayed as 'Used by Data Items' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DesignColumn} objects.
     */
    protected ReferenceList used_by_data_items;

    /**
     * The 'data_type' property, displayed as 'Data Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>INT8 (displayed in the UI as 'INT8')</li>
     *     <li>INT16 (displayed in the UI as 'INT16')</li>
     *     <li>INT32 (displayed in the UI as 'INT32')</li>
     *     <li>INT64 (displayed in the UI as 'INT64')</li>
     *     <li>SFLOAT (displayed in the UI as 'SFLOAT')</li>
     *     <li>DFLOAT (displayed in the UI as 'DFLOAT')</li>
     *     <li>QFLOAT (displayed in the UI as 'QFLOAT')</li>
     *     <li>DECIMAL (displayed in the UI as 'DECIMAL')</li>
     *     <li>STRING (displayed in the UI as 'STRING')</li>
     *     <li>BINARY (displayed in the UI as 'BINARY')</li>
     *     <li>BOOLEAN (displayed in the UI as 'BOOLEAN')</li>
     *     <li>DATE (displayed in the UI as 'DATE')</li>
     *     <li>TIME (displayed in the UI as 'TIME')</li>
     *     <li>DATETIME (displayed in the UI as 'DATETIME')</li>
     *     <li>DURATION (displayed in the UI as 'DURATION')</li>
     *     <li>CHOICE (displayed in the UI as 'CHOICE')</li>
     *     <li>ORDERED_GROUP (displayed in the UI as 'ORDERED_GROUP')</li>
     *     <li>UNORDERED_GROUP (displayed in the UI as 'UNORDERED_GROUP')</li>
     *     <li>GUID (displayed in the UI as 'GUID')</li>
     *     <li>UNKNOWN (displayed in the UI as 'UNKNOWN')</li>
     *     <li>JSON (displayed in the UI as 'JSON')</li>
     *     <li>XML (displayed in the UI as 'XML')</li>
     * </ul>
     */
    protected String data_type;

    /**
     * The 'odbc_type' property, displayed as 'ODBC Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>CHAR (displayed in the UI as 'CHAR')</li>
     *     <li>VARCHAR (displayed in the UI as 'VARCHAR')</li>
     *     <li>LONGVARCHAR (displayed in the UI as 'LONGVARCHAR')</li>
     *     <li>WCHAR (displayed in the UI as 'WCHAR')</li>
     *     <li>WVARCHAR (displayed in the UI as 'WVARCHAR')</li>
     *     <li>WLONGVARCHAR (displayed in the UI as 'WLONGVARCHAR')</li>
     *     <li>DECIMAL (displayed in the UI as 'DECIMAL')</li>
     *     <li>NUMERIC (displayed in the UI as 'NUMERIC')</li>
     *     <li>SMALLINT (displayed in the UI as 'SMALLINT')</li>
     *     <li>INTEGER (displayed in the UI as 'INTEGER')</li>
     *     <li>REAL (displayed in the UI as 'REAL')</li>
     *     <li>FLOAT (displayed in the UI as 'FLOAT')</li>
     *     <li>DOUBLE (displayed in the UI as 'DOUBLE')</li>
     *     <li>BIT (displayed in the UI as 'BIT')</li>
     *     <li>TINYINT (displayed in the UI as 'TINYINT')</li>
     *     <li>BIGINT (displayed in the UI as 'BIGINT')</li>
     *     <li>BINARY (displayed in the UI as 'BINARY')</li>
     *     <li>VARBINARY (displayed in the UI as 'VARBINARY')</li>
     *     <li>LONGVARBINARY (displayed in the UI as 'LONGVARBINARY')</li>
     *     <li>DATE (displayed in the UI as 'DATE')</li>
     *     <li>TIME (displayed in the UI as 'TIME')</li>
     *     <li>TIMESTAMP (displayed in the UI as 'TIMESTAMP')</li>
     *     <li>GUID (displayed in the UI as 'GUID')</li>
     *     <li>UNKNOWN (displayed in the UI as 'UNKNOWN')</li>
     * </ul>
     */
    protected String odbc_type;

    /**
     * The 'length' property, displayed as 'Length' in the IGC UI.
     */
    protected Number length;

    /**
     * The 'fraction' property, displayed as 'Fraction' in the IGC UI.
     */
    protected Number fraction;


    /** @see #short___long_description */ @JsonProperty("short_&_long_description")  public String getShortLongDescription() { return this.short___long_description; }
    /** @see #short___long_description */ @JsonProperty("short_&_long_description")  public void setShortLongDescription(String short___long_description) { this.short___long_description = short___long_description; }

    /** @see #physical_data_model */ @JsonProperty("physical_data_model")  public Reference getPhysicalDataModel() { return this.physical_data_model; }
    /** @see #physical_data_model */ @JsonProperty("physical_data_model")  public void setPhysicalDataModel(Reference physical_data_model) { this.physical_data_model = physical_data_model; }

    /** @see #used_by_data_items */ @JsonProperty("used_by_data_items")  public ReferenceList getUsedByDataItems() { return this.used_by_data_items; }
    /** @see #used_by_data_items */ @JsonProperty("used_by_data_items")  public void setUsedByDataItems(ReferenceList used_by_data_items) { this.used_by_data_items = used_by_data_items; }

    /** @see #data_type */ @JsonProperty("data_type")  public String getDataType() { return this.data_type; }
    /** @see #data_type */ @JsonProperty("data_type")  public void setDataType(String data_type) { this.data_type = data_type; }

    /** @see #odbc_type */ @JsonProperty("odbc_type")  public String getOdbcType() { return this.odbc_type; }
    /** @see #odbc_type */ @JsonProperty("odbc_type")  public void setOdbcType(String odbc_type) { this.odbc_type = odbc_type; }

    /** @see #length */ @JsonProperty("length")  public Number getLength() { return this.length; }
    /** @see #length */ @JsonProperty("length")  public void setLength(Number length) { this.length = length; }

    /** @see #fraction */ @JsonProperty("fraction")  public Number getFraction() { return this.fraction; }
    /** @see #fraction */ @JsonProperty("fraction")  public void setFraction(Number fraction) { this.fraction = fraction; }


    public static final Boolean isPhysicalDomain(Object obj) { return (obj.getClass() == PhysicalDomain.class); }

}
