/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.service.graphrules;

import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Graph;

public interface Rule {

    void apply(Graph graph, String queriedNodeGUID);

}
