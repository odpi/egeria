/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'dsfolder' asset type in IGC, displayed as 'Folder' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Dsfolder extends MainObject {

    public static final String IGC_TYPE_ID = "dsfolder";

    /**
     * The 'transformation_project' property, displayed as 'Transformation Project' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link TransformationProject} object.
     */
    protected Reference transformation_project;

    /**
     * The 'parent_folder' property, displayed as 'Parent Folder' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Dsfolder} object.
     */
    protected Reference parent_folder;

    /**
     * The 'contains_folders' property, displayed as 'Contains Folders' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Dsfolder} objects.
     */
    protected ReferenceList contains_folders;

    /**
     * The 'contains_assets' property, displayed as 'Contains Assets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList contains_assets;


    /** @see #transformation_project */ @JsonProperty("transformation_project")  public Reference getTransformationProject() { return this.transformation_project; }
    /** @see #transformation_project */ @JsonProperty("transformation_project")  public void setTransformationProject(Reference transformation_project) { this.transformation_project = transformation_project; }

    /** @see #parent_folder */ @JsonProperty("parent_folder")  public Reference getParentFolder() { return this.parent_folder; }
    /** @see #parent_folder */ @JsonProperty("parent_folder")  public void setParentFolder(Reference parent_folder) { this.parent_folder = parent_folder; }

    /** @see #contains_folders */ @JsonProperty("contains_folders")  public ReferenceList getContainsFolders() { return this.contains_folders; }
    /** @see #contains_folders */ @JsonProperty("contains_folders")  public void setContainsFolders(ReferenceList contains_folders) { this.contains_folders = contains_folders; }

    /** @see #contains_assets */ @JsonProperty("contains_assets")  public ReferenceList getContainsAssets() { return this.contains_assets; }
    /** @see #contains_assets */ @JsonProperty("contains_assets")  public void setContainsAssets(ReferenceList contains_assets) { this.contains_assets = contains_assets; }


    public static final Boolean isDsfolder(Object obj) { return (obj.getClass() == Dsfolder.class); }

}
