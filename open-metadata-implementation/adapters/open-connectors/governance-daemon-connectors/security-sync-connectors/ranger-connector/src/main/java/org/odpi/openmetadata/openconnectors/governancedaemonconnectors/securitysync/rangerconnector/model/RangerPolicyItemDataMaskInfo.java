/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model;

public class RangerPolicyItemDataMaskInfo {

    private String dataMaskType;
    private String conditionExpr;
    private String valueExpr;

    public String getDataMaskType() {
        return dataMaskType;
    }

    public void setDataMaskType(String dataMaskType) {
        this.dataMaskType = dataMaskType;
    }

    public String getConditionExpr() {
        return conditionExpr;
    }

    public void setConditionExpr(String conditionExpr) {
        this.conditionExpr = conditionExpr;
    }

    public String getValueExpr() {
        return valueExpr;
    }

    public void setValueExpr(String valueExpr) {
        this.valueExpr = valueExpr;
    }
}