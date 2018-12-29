/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;

import java.util.ArrayList;
import java.util.List;

public class OMRSStub extends MainObject {

    public static String getIgcTypeId() { return "$OMRS-Stub"; }
    public static String getIgcTypeDisplayName() { return "Stub"; }

    /**
     * The 'sourceType' property, displayed as 'Type' in the IGC UI.
     * <br><br>
     * Provides the type of asset for which this stub represents a shadow copy.
     */
    protected String $sourceType;

    /**
     * The 'sourceRID' property, displayed as 'RID' in the IGC UI.
     * <br><br>
     * Provides the Repository ID (RID) of the asset for which this stub represents a shadow copy.
     */
    protected String $sourceRID;

    /**
     * The 'payload' property, displayed as 'Last Version' in the IGC UI.
     * <br><br>
     * Provides the JSON payload of the last version of the asset for which this stub represents a shadow copy.
     */
    protected String $payload;

    // TODO: add notes object reference

    /** @see #$sourceType */ @JsonProperty("$sourceType") public String getSourceType() { return this.$sourceType; }
    /** @see #$sourceType */ @JsonProperty("$sourceType") public void setSourceType(String sourceType) { this.$sourceType = sourceType; }

    /** @see #$sourceRID */ @JsonProperty("$sourceRID") public String getSourceRID() { return this.$sourceRID; }
    /** @see #$sourceRID */ @JsonProperty("$sourceRID") public void setSourceRID(String sourceRID) { this.$sourceRID = sourceRID; }

    /** @see #$payload */ @JsonProperty("$payload") public String getPayload() { return this.$payload; }
    /** @see #$payload */ @JsonProperty("$payload") public void setPayload(String payload) { this.$payload = payload; }

    public static Boolean canBeCreated() { return true; }
    public static Boolean includesModificationDetails() { return true; }
    public static ArrayList<String> NON_RELATIONAL_PROPERTIES = new ArrayList<String>() {{
        add("name");
        add("short_description");
        add("long_description");
        add("created_by");
        add("created_on");
        add("modified_by");
        add("modified_on");
        add("$sourceType");
        add("$sourceRID");
        add("$payload");
    }};
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static final Boolean isOMRSStub(Object obj) { return (obj.getClass() == OMRSStub.class); }

}
