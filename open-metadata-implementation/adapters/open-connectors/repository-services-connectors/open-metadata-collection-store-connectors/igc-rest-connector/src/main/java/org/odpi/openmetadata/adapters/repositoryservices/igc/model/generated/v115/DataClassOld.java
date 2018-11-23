/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'data_class_old' asset type in IGC, displayed as 'Data Class' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataClassOld extends MainObject {

    public static final String IGC_TYPE_ID = "data_class_old";

    /**
     * The 'is_sub_of_data_class' property, displayed as 'Is Sub Of Data Class' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataClassOld} object.
     */
    protected Reference is_sub_of_data_class;

    /**
     * The 'classifies_data_field' property, displayed as 'Classifies Data Field' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataItem} objects.
     */
    protected ReferenceList classifies_data_field;

    /**
     * The 'has_sub_data_class' property, displayed as 'Has Sub Data Class' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataClassOld} objects.
     */
    protected ReferenceList has_sub_data_class;

    /**
     * The 'class_code' property, displayed as 'Class Code' in the IGC UI.
     */
    protected String class_code;

    /**
     * The 'inferred_by_df_analysis_summary' property, displayed as 'Inferred By DF Analysis Summary' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ColumnAnalysisSummary} objects.
     */
    protected ReferenceList inferred_by_df_analysis_summary;

    /**
     * The 'is_user_defined' property, displayed as 'Is User Defined' in the IGC UI.
     */
    protected Boolean is_user_defined;


    /** @see #is_sub_of_data_class */ @JsonProperty("is_sub_of_data_class")  public Reference getIsSubOfDataClass() { return this.is_sub_of_data_class; }
    /** @see #is_sub_of_data_class */ @JsonProperty("is_sub_of_data_class")  public void setIsSubOfDataClass(Reference is_sub_of_data_class) { this.is_sub_of_data_class = is_sub_of_data_class; }

    /** @see #classifies_data_field */ @JsonProperty("classifies_data_field")  public ReferenceList getClassifiesDataField() { return this.classifies_data_field; }
    /** @see #classifies_data_field */ @JsonProperty("classifies_data_field")  public void setClassifiesDataField(ReferenceList classifies_data_field) { this.classifies_data_field = classifies_data_field; }

    /** @see #has_sub_data_class */ @JsonProperty("has_sub_data_class")  public ReferenceList getHasSubDataClass() { return this.has_sub_data_class; }
    /** @see #has_sub_data_class */ @JsonProperty("has_sub_data_class")  public void setHasSubDataClass(ReferenceList has_sub_data_class) { this.has_sub_data_class = has_sub_data_class; }

    /** @see #class_code */ @JsonProperty("class_code")  public String getClassCode() { return this.class_code; }
    /** @see #class_code */ @JsonProperty("class_code")  public void setClassCode(String class_code) { this.class_code = class_code; }

    /** @see #inferred_by_df_analysis_summary */ @JsonProperty("inferred_by_df_analysis_summary")  public ReferenceList getInferredByDfAnalysisSummary() { return this.inferred_by_df_analysis_summary; }
    /** @see #inferred_by_df_analysis_summary */ @JsonProperty("inferred_by_df_analysis_summary")  public void setInferredByDfAnalysisSummary(ReferenceList inferred_by_df_analysis_summary) { this.inferred_by_df_analysis_summary = inferred_by_df_analysis_summary; }

    /** @see #is_user_defined */ @JsonProperty("is_user_defined")  public Boolean getIsUserDefined() { return this.is_user_defined; }
    /** @see #is_user_defined */ @JsonProperty("is_user_defined")  public void setIsUserDefined(Boolean is_user_defined) { this.is_user_defined = is_user_defined; }


    public static final Boolean isDataClassOld(Object obj) { return (obj.getClass() == DataClassOld.class); }

}
