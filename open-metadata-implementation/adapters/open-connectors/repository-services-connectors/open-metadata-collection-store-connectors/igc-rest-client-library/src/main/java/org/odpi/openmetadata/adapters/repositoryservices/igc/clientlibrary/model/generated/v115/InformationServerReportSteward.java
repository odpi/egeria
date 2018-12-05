/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'information_server_report_(steward)' asset type in IGC, displayed as 'Information Server Report (Steward)' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformationServerReportSteward extends MainObject {

    public static final String IGC_TYPE_ID = "information_server_report_(steward)";

    /**
     * The 'description' property, displayed as 'Description' in the IGC UI.
     */
    protected String description;

    /**
     * The 'creator' property, displayed as 'Creator' in the IGC UI.
     */
    protected String creator;

    /**
     * The 'product' property, displayed as 'Product' in the IGC UI.
     */
    protected String product;

    /**
     * The 'data_steward' property, displayed as 'Data Steward' in the IGC UI.
     */
    protected ArrayList<String> data_steward;


    /** @see #description */ @JsonProperty("description")  public String getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(String description) { this.description = description; }

    /** @see #creator */ @JsonProperty("creator")  public String getCreator() { return this.creator; }
    /** @see #creator */ @JsonProperty("creator")  public void setCreator(String creator) { this.creator = creator; }

    /** @see #product */ @JsonProperty("product")  public String getProduct() { return this.product; }
    /** @see #product */ @JsonProperty("product")  public void setProduct(String product) { this.product = product; }

    /** @see #data_steward */ @JsonProperty("data_steward")  public ArrayList<String> getDataSteward() { return this.data_steward; }
    /** @see #data_steward */ @JsonProperty("data_steward")  public void setDataSteward(ArrayList<String> data_steward) { this.data_steward = data_steward; }


    public static final Boolean isInformationServerReportSteward(Object obj) { return (obj.getClass() == InformationServerReportSteward.class); }

}
