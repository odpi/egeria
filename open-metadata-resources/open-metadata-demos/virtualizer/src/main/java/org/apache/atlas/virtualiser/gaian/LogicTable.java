/* SPDX-License-Identifier: Apache-2.0 */
package org.apache.atlas.virtualiser.gaian;

import java.util.Map;

/**
 * LogicTable is a POJO which stores Logical Table information in Gaian
 */
public class LogicTable {
    private String LTName;
    private String GDBNode;
    private Map<String, String> LTDef;

    public Map<String, String> getLTDef() {
        return LTDef;
    }

    public void setLTDef(Map<String, String> LTDef) {
        this.LTDef = LTDef;
    }

    public String getGDBNode() {
        return GDBNode;
    }

    public void setGDBNode(String GDBNode) {
        this.GDBNode = GDBNode;
    }

    public String getLTName() {
        return LTName;
    }

    public void setLTName(String LTName) {
        this.LTName = LTName;
    }
}
