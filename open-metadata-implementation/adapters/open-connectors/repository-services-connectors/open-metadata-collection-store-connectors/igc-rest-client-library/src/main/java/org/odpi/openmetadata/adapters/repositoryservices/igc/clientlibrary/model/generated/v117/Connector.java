/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'connector' asset type in IGC, displayed as 'Connector' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Connector extends MainObject {

    public static final String IGC_TYPE_ID = "connector";

    /**
     * The 'host' property, displayed as 'Host' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Host} object.
     */
    protected Reference host;

    /**
     * The 'implements_stage_type' property, displayed as 'Implements Stage Type' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link StageType} object.
     */
    protected Reference implements_stage_type;

    /**
     * The 'data_connections' property, displayed as 'Data Connections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataConnection} objects.
     */
    protected ReferenceList data_connections;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     */
    protected String type;

    /**
     * The 'version' property, displayed as 'Version' in the IGC UI.
     */
    protected String version;

    /**
     * The 'variant' property, displayed as 'Variant' in the IGC UI.
     */
    protected String variant;

    /**
     * The 'library' property, displayed as 'Library' in the IGC UI.
     */
    protected String library;


    /** @see #host */ @JsonProperty("host")  public Reference getHost() { return this.host; }
    /** @see #host */ @JsonProperty("host")  public void setHost(Reference host) { this.host = host; }

    /** @see #implements_stage_type */ @JsonProperty("implements_stage_type")  public Reference getImplementsStageType() { return this.implements_stage_type; }
    /** @see #implements_stage_type */ @JsonProperty("implements_stage_type")  public void setImplementsStageType(Reference implements_stage_type) { this.implements_stage_type = implements_stage_type; }

    /** @see #data_connections */ @JsonProperty("data_connections")  public ReferenceList getDataConnections() { return this.data_connections; }
    /** @see #data_connections */ @JsonProperty("data_connections")  public void setDataConnections(ReferenceList data_connections) { this.data_connections = data_connections; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #version */ @JsonProperty("version")  public String getVersion() { return this.version; }
    /** @see #version */ @JsonProperty("version")  public void setVersion(String version) { this.version = version; }

    /** @see #variant */ @JsonProperty("variant")  public String getVariant() { return this.variant; }
    /** @see #variant */ @JsonProperty("variant")  public void setVariant(String variant) { this.variant = variant; }

    /** @see #library */ @JsonProperty("library")  public String getLibrary() { return this.library; }
    /** @see #library */ @JsonProperty("library")  public void setLibrary(String library) { this.library = library; }


    public static final Boolean isConnector(Object obj) { return (obj.getClass() == Connector.class); }

}
