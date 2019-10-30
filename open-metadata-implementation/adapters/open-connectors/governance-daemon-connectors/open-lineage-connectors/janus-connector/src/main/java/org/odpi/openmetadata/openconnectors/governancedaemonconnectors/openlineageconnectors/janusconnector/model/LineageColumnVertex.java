/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.maingraph;

public class LineageColumnVertex extends LineageVertex{


    private String glossaryTerm;
    private String displayName;

    public LineageColumnVertex(String nodeID, String nodeType, String guid){
        super(nodeID, nodeType, guid);
    }

    public String getGlossaryTerm() {
        return glossaryTerm;
    }

    public void setGlossaryTerm(String glossaryTerm) {
        this.glossaryTerm = glossaryTerm;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


}
