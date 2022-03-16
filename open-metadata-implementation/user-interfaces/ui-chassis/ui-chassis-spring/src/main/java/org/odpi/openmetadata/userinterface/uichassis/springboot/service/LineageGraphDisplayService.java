/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.service;

import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Edge;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Graph;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Node;
import org.odpi.openmetadata.userinterface.uichassis.springboot.service.graphrules.Rule;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Rules;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * This component matches the properties provided under lineage-display-config which provide rules
 * based on which the edge's direction will be inverted
 */
@Service
@ConfigurationProperties(prefix = "lineage-display-config")
public class LineageGraphDisplayService {

    private Rules rules;

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }


    /**
     * This method applies the rules defined in the properties file on the graph.
     *
     * @param graph           graph processed from the response from open lineage
     * @param queriedNodeGUID the GUID of the queried node
     */
    public void applyRules(Graph graph, String queriedNodeGUID) {
        if (rules == null) {
            return;
        }

        for (Rule rule : rules.getAllRules()) {
            rule.apply(graph, queriedNodeGUID);
        }
    }

    @Override
    public String toString() {
        return "LineageGraphDisplayRulesService{" +
                "rules=" + rules +
                '}';
    }
}
