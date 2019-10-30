/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.maingraph;

public class LineageVertex {

    protected String nodeID;
    protected String nodeType;
    protected String guid;

    public LineageVertex(String nodeID, String nodeType, String guid){
        this.nodeID = nodeID;
        this.nodeType = nodeType;
        this.guid = guid;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
