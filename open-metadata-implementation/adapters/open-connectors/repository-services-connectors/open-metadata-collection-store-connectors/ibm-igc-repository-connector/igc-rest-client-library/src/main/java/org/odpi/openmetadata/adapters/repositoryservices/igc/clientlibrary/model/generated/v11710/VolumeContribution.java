/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11710;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code volume_contribution} asset type in IGC, displayed as '{@literal Volume Contribution}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
public class VolumeContribution extends Reference {

    public static String getIgcTypeId() { return "volume_contribution"; }
    public static String getIgcTypeDisplayName() { return "Volume Contribution"; }

    /**
     * The {@code infoset} property, displayed as '{@literal Infoset}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Infoset} object.
     */
    protected Reference infoset;

    /**
     * The {@code volume} property, displayed as '{@literal Volume}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Volume} object.
     */
    protected Reference volume;

    /**
     * The {@code object_count} property, displayed as '{@literal Number of Objects}' in the IGC UI.
     */
    protected Number object_count;

    /**
     * The {@code size} property, displayed as '{@literal Size (Bytes)}' in the IGC UI.
     */
    protected Number size;


    /** @see #infoset */ @JsonProperty("infoset")  public Reference getInfoset() { return this.infoset; }
    /** @see #infoset */ @JsonProperty("infoset")  public void setInfoset(Reference infoset) { this.infoset = infoset; }

    /** @see #volume */ @JsonProperty("volume")  public Reference getVolume() { return this.volume; }
    /** @see #volume */ @JsonProperty("volume")  public void setVolume(Reference volume) { this.volume = volume; }

    /** @see #object_count */ @JsonProperty("object_count")  public Number getObjectCount() { return this.object_count; }
    /** @see #object_count */ @JsonProperty("object_count")  public void setObjectCount(Number object_count) { this.object_count = object_count; }

    /** @see #size */ @JsonProperty("size")  public Number getSize() { return this.size; }
    /** @see #size */ @JsonProperty("size")  public void setSize(Number size) { this.size = size; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "object_count",
        "size"
    );
    private static final List<String> STRING_PROPERTIES = new ArrayList<>();
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "infoset",
        "volume",
        "object_count",
        "size"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isVolumeContribution(Object obj) { return (obj.getClass() == VolumeContribution.class); }

}
