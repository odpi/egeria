/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.api.objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class GovernedAsset {

    // Attributes of a Governed Asset Component - we use this term as it may refer to only part of an asset - for example a single
    // column (referred to in a schema - not an asset in OMRS terms), or it may be the asset itself
    // Mostly we focus on the granularity needed by enforcement engines like Apache Ranger or other governance engines
    // Also note that this is a tiny subset of what can be retrieved via the Asset Consumer OMAS

    private String guid; // GUID of the asset that has been classified (TODO: Not sure what we'll put here for part of an asset)
    private String fqname; // Fully qualified Common name of the asset for example MYDATABASE.MYTABLE.MYCOLUMN

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type; // The asset type (name)
    private String owner;// asset owner


    // Asset components of interest will have classifications associated with them )
    List<GovernanceClassificationUsage> assignedGovernanceClassifications;

    public String getGuid() {
        return guid;
    }

    /**
     *
     * @param guid - unique identifier
     */
    public void setGuid(String guid) {
        this.guid = guid;
    }


    /**
     * @return fqname - fully qualified name
     */
    public String getFqName() {
        return fqname;
    }

    /**
     *
     * @param fqname - fully qualified asset / asset component name
     */
    public void setFqName(String fqname) {
        this.fqname = fqname;
    }

    /**
     * @return assignedGovernanceClassifications
     */
    public List<GovernanceClassificationUsage> getAssignedGovernanceClassifications() {
        return assignedGovernanceClassifications;
    }
    /**
     *
     * @param assignedGovernanceClassifications
     */
    public void setAssignedGovernanceClassifications(List<GovernanceClassificationUsage> assignedGovernanceClassifications) {
        this.assignedGovernanceClassifications = assignedGovernanceClassifications;
    }


}
