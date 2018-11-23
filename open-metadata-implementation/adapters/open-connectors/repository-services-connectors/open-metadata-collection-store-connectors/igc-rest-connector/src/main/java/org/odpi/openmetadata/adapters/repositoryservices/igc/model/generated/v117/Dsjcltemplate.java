/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'dsjcltemplate' asset type in IGC, displayed as 'DSJCLTemplate' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Dsjcltemplate extends MainObject {

    public static final String IGC_TYPE_ID = "dsjcltemplate";

    /**
     * The 'template_type' property, displayed as 'Template Type' in the IGC UI.
     */
    protected String template_type;

    /**
     * The 'category' property, displayed as 'Category' in the IGC UI.
     */
    protected String category;

    /**
     * The 'ds_name_space' property, displayed as 'DS Name Space' in the IGC UI.
     */
    protected String ds_name_space;

    /**
     * The 'platform_type' property, displayed as 'Platform Type' in the IGC UI.
     */
    protected String platform_type;

    /**
     * The 'code_template' property, displayed as 'Code Template' in the IGC UI.
     */
    protected String code_template;


    /** @see #template_type */ @JsonProperty("template_type")  public String getTemplateType() { return this.template_type; }
    /** @see #template_type */ @JsonProperty("template_type")  public void setTemplateType(String template_type) { this.template_type = template_type; }

    /** @see #category */ @JsonProperty("category")  public String getCategory() { return this.category; }
    /** @see #category */ @JsonProperty("category")  public void setCategory(String category) { this.category = category; }

    /** @see #ds_name_space */ @JsonProperty("ds_name_space")  public String getDsNameSpace() { return this.ds_name_space; }
    /** @see #ds_name_space */ @JsonProperty("ds_name_space")  public void setDsNameSpace(String ds_name_space) { this.ds_name_space = ds_name_space; }

    /** @see #platform_type */ @JsonProperty("platform_type")  public String getPlatformType() { return this.platform_type; }
    /** @see #platform_type */ @JsonProperty("platform_type")  public void setPlatformType(String platform_type) { this.platform_type = platform_type; }

    /** @see #code_template */ @JsonProperty("code_template")  public String getCodeTemplate() { return this.code_template; }
    /** @see #code_template */ @JsonProperty("code_template")  public void setCodeTemplate(String code_template) { this.code_template = code_template; }


    public static final Boolean isDsjcltemplate(Object obj) { return (obj.getClass() == Dsjcltemplate.class); }

}
