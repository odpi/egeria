/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'bi_root_folder' asset type in IGC, displayed as 'BI Folder' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class BiRootFolder extends MainObject {

    public static final String IGC_TYPE_ID = "bi_root_folder";

    /**
     * The 'bi_server' property, displayed as 'BI Server' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiServer} object.
     */
    protected Reference bi_server;

    /**
     * The 'contains_bi_folders' property, displayed as 'Contains BI Folders' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiFolder} objects.
     */
    protected ReferenceList contains_bi_folders;

    /**
     * The 'bi_reports' property, displayed as 'BI Reports' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiReport} objects.
     */
    protected ReferenceList bi_reports;

    /**
     * The 'bi_models' property, displayed as 'BI Models' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiModel} objects.
     */
    protected ReferenceList bi_models;


    /** @see #bi_server */ @JsonProperty("bi_server")  public Reference getBiServer() { return this.bi_server; }
    /** @see #bi_server */ @JsonProperty("bi_server")  public void setBiServer(Reference bi_server) { this.bi_server = bi_server; }

    /** @see #contains_bi_folders */ @JsonProperty("contains_bi_folders")  public ReferenceList getContainsBiFolders() { return this.contains_bi_folders; }
    /** @see #contains_bi_folders */ @JsonProperty("contains_bi_folders")  public void setContainsBiFolders(ReferenceList contains_bi_folders) { this.contains_bi_folders = contains_bi_folders; }

    /** @see #bi_reports */ @JsonProperty("bi_reports")  public ReferenceList getBiReports() { return this.bi_reports; }
    /** @see #bi_reports */ @JsonProperty("bi_reports")  public void setBiReports(ReferenceList bi_reports) { this.bi_reports = bi_reports; }

    /** @see #bi_models */ @JsonProperty("bi_models")  public ReferenceList getBiModels() { return this.bi_models; }
    /** @see #bi_models */ @JsonProperty("bi_models")  public void setBiModels(ReferenceList bi_models) { this.bi_models = bi_models; }


    public static final Boolean isBiRootFolder(Object obj) { return (obj.getClass() == BiRootFolder.class); }

}
