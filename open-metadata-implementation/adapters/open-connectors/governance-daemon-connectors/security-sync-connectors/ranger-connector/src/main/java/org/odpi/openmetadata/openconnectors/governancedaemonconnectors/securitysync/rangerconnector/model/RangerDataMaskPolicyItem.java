/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model;

public class RangerDataMaskPolicyItem extends RangerPolicyItem {

    private RangerPolicyItemDataMaskInfo dataMaskInfo;

    public RangerPolicyItemDataMaskInfo getDataMaskInfo() {
        return dataMaskInfo;
    }

    public void setDataMaskInfo(RangerPolicyItemDataMaskInfo dataMaskInfo) {
        this.dataMaskInfo = dataMaskInfo;
    }
}