/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'information_server_report_parameters' asset type in IGC, displayed as 'Information Server Report Parameters' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformationServerReportParameters extends MainObject {

    public static final String IGC_TYPE_ID = "information_server_report_parameters";

    /**
     * The 'value' property, displayed as 'Value' in the IGC UI.
     */
    protected ArrayList<String> value;


    /** @see #value */ @JsonProperty("value")  public ArrayList<String> getValue() { return this.value; }
    /** @see #value */ @JsonProperty("value")  public void setValue(ArrayList<String> value) { this.value = value; }


    public static final Boolean isInformationServerReportParameters(Object obj) { return (obj.getClass() == InformationServerReportParameters.class); }

}
