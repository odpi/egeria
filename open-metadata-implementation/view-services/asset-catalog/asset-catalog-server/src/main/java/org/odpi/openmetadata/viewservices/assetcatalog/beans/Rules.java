/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.beans;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Rules {
    private List<IncompleteRule> incomplete;
    private List<InvertEdgeRule> invertEdge;
    private List<PrimaryCategoryRule> primaryCategory;
    private List<RemoveNodesRule> removeNodes;

    public List<IncompleteRule> getIncomplete() {
        return incomplete;
    }

    public void setIncomplete(List<IncompleteRule> incomplete) {
        this.incomplete = incomplete;
    }

    public List<InvertEdgeRule> getInvertEdge() {
        return invertEdge;
    }

    public void setInvertEdge(List<InvertEdgeRule> invertEdge) {
        this.invertEdge = invertEdge;
    }

    public List<PrimaryCategoryRule> getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(List<PrimaryCategoryRule> primaryCategory) {
        this.primaryCategory = primaryCategory;
    }

    public List<RemoveNodesRule> getRemoveNodes() {
        return removeNodes;
    }

    public void setRemoveNodes(List<RemoveNodesRule> removeNodes) {
        this.removeNodes = removeNodes;
    }

    public List<Rule> getAllRules() {
        return Stream.of(incomplete, invertEdge, primaryCategory, removeNodes)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

}
