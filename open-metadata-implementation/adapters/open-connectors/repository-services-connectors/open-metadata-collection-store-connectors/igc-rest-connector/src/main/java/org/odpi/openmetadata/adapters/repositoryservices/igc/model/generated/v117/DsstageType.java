/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'dsstage_type' asset type in IGC, displayed as 'Stage Type' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DsstageType extends MainObject {

    public static final String IGC_TYPE_ID = "dsstage_type";

    /**
     * The 'transformation_project' property, displayed as 'Transformation Project' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link TransformationProject} object.
     */
    protected Reference transformation_project;

    /**
     * The 'stages' property, displayed as 'Stages' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Stage} objects.
     */
    protected ReferenceList stages;

    /**
     * The 'ole_type' property, displayed as 'OLE Type' in the IGC UI.
     */
    protected String ole_type;

    /**
     * The 'dll_name' property, displayed as 'DLL Name' in the IGC UI.
     */
    protected String dll_name;

    /**
     * The 'vendor' property, displayed as 'Vendor' in the IGC UI.
     */
    protected String vendor;

    /**
     * The 'author' property, displayed as 'Author' in the IGC UI.
     */
    protected String author;

    /**
     * The 'version' property, displayed as 'Version' in the IGC UI.
     */
    protected String version;

    /**
     * The 'copyright' property, displayed as 'Copyright' in the IGC UI.
     */
    protected String copyright;

    /**
     * The 'properties' property, displayed as 'Properties' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link StageTypeDetail} objects.
     */
    protected ReferenceList properties;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #transformation_project */ @JsonProperty("transformation_project")  public Reference getTransformationProject() { return this.transformation_project; }
    /** @see #transformation_project */ @JsonProperty("transformation_project")  public void setTransformationProject(Reference transformation_project) { this.transformation_project = transformation_project; }

    /** @see #stages */ @JsonProperty("stages")  public ReferenceList getStages() { return this.stages; }
    /** @see #stages */ @JsonProperty("stages")  public void setStages(ReferenceList stages) { this.stages = stages; }

    /** @see #ole_type */ @JsonProperty("ole_type")  public String getOleType() { return this.ole_type; }
    /** @see #ole_type */ @JsonProperty("ole_type")  public void setOleType(String ole_type) { this.ole_type = ole_type; }

    /** @see #dll_name */ @JsonProperty("dll_name")  public String getDllName() { return this.dll_name; }
    /** @see #dll_name */ @JsonProperty("dll_name")  public void setDllName(String dll_name) { this.dll_name = dll_name; }

    /** @see #vendor */ @JsonProperty("vendor")  public String getVendor() { return this.vendor; }
    /** @see #vendor */ @JsonProperty("vendor")  public void setVendor(String vendor) { this.vendor = vendor; }

    /** @see #author */ @JsonProperty("author")  public String getAuthor() { return this.author; }
    /** @see #author */ @JsonProperty("author")  public void setAuthor(String author) { this.author = author; }

    /** @see #version */ @JsonProperty("version")  public String getVersion() { return this.version; }
    /** @see #version */ @JsonProperty("version")  public void setVersion(String version) { this.version = version; }

    /** @see #copyright */ @JsonProperty("copyright")  public String getCopyright() { return this.copyright; }
    /** @see #copyright */ @JsonProperty("copyright")  public void setCopyright(String copyright) { this.copyright = copyright; }

    /** @see #properties */ @JsonProperty("properties")  public ReferenceList getProperties() { return this.properties; }
    /** @see #properties */ @JsonProperty("properties")  public void setProperties(ReferenceList properties) { this.properties = properties; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isDsstageType(Object obj) { return (obj.getClass() == DsstageType.class); }

}
