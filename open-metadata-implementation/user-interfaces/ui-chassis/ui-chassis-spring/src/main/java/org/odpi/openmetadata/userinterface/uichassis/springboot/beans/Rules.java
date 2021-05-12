/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.beans;

import org.odpi.openmetadata.userinterface.uichassis.springboot.service.graphrules.InvertEdgeRule;
import org.odpi.openmetadata.userinterface.uichassis.springboot.service.graphrules.PrimaryCategoryRule;
import org.odpi.openmetadata.userinterface.uichassis.springboot.service.graphrules.RemoveNodesRule;
import org.odpi.openmetadata.userinterface.uichassis.springboot.service.graphrules.Rule;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Rules {
    private List<InvertEdgeRule> invertEdge;
    private List<PrimaryCategoryRule> primaryCategory;
    private List<RemoveNodesRule> removeNodes;

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
        return Stream.of(invertEdge, primaryCategory, removeNodes)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

}
