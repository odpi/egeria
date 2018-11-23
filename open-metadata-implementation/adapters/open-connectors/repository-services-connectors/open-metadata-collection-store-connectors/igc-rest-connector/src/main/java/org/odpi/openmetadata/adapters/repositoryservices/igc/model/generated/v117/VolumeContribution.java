/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'volume_contribution' asset type in IGC, displayed as 'Volume Contribution' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class VolumeContribution extends MainObject {

    public static final String IGC_TYPE_ID = "volume_contribution";

    /**
     * The 'infoset' property, displayed as 'Infoset' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Infoset} object.
     */
    protected Reference infoset;

    /**
     * The 'volume' property, displayed as 'Volume' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Volume} object.
     */
    protected Reference volume;

    /**
     * The 'object_count' property, displayed as 'Number of Objects' in the IGC UI.
     */
    protected Number object_count;

    /**
     * The 'size' property, displayed as 'Size (Bytes)' in the IGC UI.
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


    public static final Boolean isVolumeContribution(Object obj) { return (obj.getClass() == VolumeContribution.class); }

}
