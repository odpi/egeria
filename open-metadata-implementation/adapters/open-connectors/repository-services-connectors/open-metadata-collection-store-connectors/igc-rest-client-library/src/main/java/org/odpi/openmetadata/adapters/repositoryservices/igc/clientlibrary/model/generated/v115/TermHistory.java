/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'term_history' asset type in IGC, displayed as 'Term History' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class TermHistory extends Reference {

    @JsonIgnore public static final String IGC_TYPE_ID = "term_history";

    /**
     * The 'term' property, displayed as 'Term' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link InformationAsset} object.
     */
    protected Reference term;

    /**
     * The 'date' property, displayed as 'Date' in the IGC UI.
     */
    protected Date date;

    /**
     * The 'comment' property, displayed as 'Comment' in the IGC UI.
     */
    protected ArrayList<String> comment;

    /**
     * The 'edited_by' property, displayed as 'Edited By' in the IGC UI.
     */
    protected String edited_by;

    /**
     * The 'changed_properties' property, displayed as 'Changed Properties' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ChangedProperties} objects.
     */
    protected ReferenceList changed_properties;


    /** @see #term */ @JsonProperty("term")  public Reference getTerm() { return this.term; }
    /** @see #term */ @JsonProperty("term")  public void setTerm(Reference term) { this.term = term; }

    /** @see #date */ @JsonProperty("date")  public Date getDate() { return this.date; }
    /** @see #date */ @JsonProperty("date")  public void setDate(Date date) { this.date = date; }

    /** @see #comment */ @JsonProperty("comment")  public ArrayList<String> getComment() { return this.comment; }
    /** @see #comment */ @JsonProperty("comment")  public void setComment(ArrayList<String> comment) { this.comment = comment; }

    /** @see #edited_by */ @JsonProperty("edited_by")  public String getEditedBy() { return this.edited_by; }
    /** @see #edited_by */ @JsonProperty("edited_by")  public void setEditedBy(String edited_by) { this.edited_by = edited_by; }

    /** @see #changed_properties */ @JsonProperty("changed_properties")  public ReferenceList getChangedProperties() { return this.changed_properties; }
    /** @see #changed_properties */ @JsonProperty("changed_properties")  public void setChangedProperties(ReferenceList changed_properties) { this.changed_properties = changed_properties; }


    public static final Boolean isTermHistory(Object obj) { return (obj.getClass() == TermHistory.class); }

}
