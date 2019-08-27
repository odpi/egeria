/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model;

public class RangerRowFilterPolicyItem extends RangerPolicyItem {

    private RangerPolicyItemRowFilterInfo rowFilterInfo;

    public RangerPolicyItemRowFilterInfo getRowFilterInfo() {
        return rowFilterInfo;
    }

    public void setRowFilterInfo(RangerPolicyItemRowFilterInfo rowFilterInfo) {
        this.rowFilterInfo = rowFilterInfo;
    }
}