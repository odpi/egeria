/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.SubjectAreaNodeClients;

import java.util.HashSet;
import java.util.Set;

/**
 * The Graph handler is initialised with the userId under which those calls should be issued.
 */
public class GraphHandler {
    private Set<String> guids = new HashSet<>();
    private String userId;



    public GraphHandler(SubjectAreaNodeClients clients, String userId) {
        this.userId = userId;
    }

}