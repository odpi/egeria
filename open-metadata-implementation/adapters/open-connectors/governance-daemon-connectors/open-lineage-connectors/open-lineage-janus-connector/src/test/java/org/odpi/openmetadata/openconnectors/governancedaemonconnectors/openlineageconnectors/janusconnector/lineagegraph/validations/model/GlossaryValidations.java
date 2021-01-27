/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.lineagegraph.validations.model;

import java.util.List;

public class GlossaryValidations {
    private List<String> tabularColumn;
    private List<String> relationalColumn;
    private List<String> glossaries;
    private List<String> glossaryCategories;

    public List<String> getTabularColumn() {
        return tabularColumn;
    }

    public void setTabularColumn(List<String> tabularColumn) {
        this.tabularColumn = tabularColumn;
    }

    public List<String> getRelationalColumn() {
        return relationalColumn;
    }

    public void setRelationalColumn(List<String> relationalColumn) {
        this.relationalColumn = relationalColumn;
    }

    public List<String> getGlossaries() {
        return glossaries;
    }

    public void setGlossaries(List<String> glossaries) {
        this.glossaries = glossaries;
    }

    public List<String> getGlossaryCategories() {
        return glossaryCategories;
    }

    public void setGlossaryCategories(List<String> glossaryCategories) {
        this.glossaryCategories = glossaryCategories;
    }

    @Override
    public String toString() {
        return "GlossaryValidations{" +
                "tabularColumn=" + tabularColumn +
                ", relationalColumn=" + relationalColumn +
                ", glossary=" + glossaries +
                ", glossaryCategories=" + glossaryCategories +
                '}';
    }
}
