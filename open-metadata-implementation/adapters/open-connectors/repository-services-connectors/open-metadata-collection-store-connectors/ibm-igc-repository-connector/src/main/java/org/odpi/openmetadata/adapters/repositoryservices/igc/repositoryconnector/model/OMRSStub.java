/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;

import java.util.Arrays;
import java.util.List;

public class OMRSStub extends MainObject {

    public static String getIgcTypeId() { return "$OMRS-Stub"; }
    public static String getIgcTypeDisplayName() { return "OMRS Stub"; }

    /**
     * The 'sourceType' property, displayed as 'IGC Type' in the IGC UI.
     * <br><br>
     * Provides the type of asset for which this stub represents a shadow copy.
     */
    protected String $sourceType;

    /**
     * The 'sourceRID' property, displayed as 'IGC RID' in the IGC UI.
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
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
            "name",
            "short_description",
            "long_description",
            "created_by",
            "created_on",
            "modified_by",
            "modified_on",
            "$sourceType",
            "$sourceRID",
            "$payload"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static Boolean isOMRSStub(Object obj) { return (obj.getClass() == OMRSStub.class); }

}
