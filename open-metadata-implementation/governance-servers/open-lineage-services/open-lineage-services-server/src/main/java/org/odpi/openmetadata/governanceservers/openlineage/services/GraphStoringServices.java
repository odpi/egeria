/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.services;

import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.GraphStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphStoringServices {

    private static final Logger log = LoggerFactory.getLogger(GraphStoringServices.class);

    private GraphStore graphStore;

    public GraphStoringServices(GraphStore graphStore) {
        this.graphStore = graphStore;
    }

    private void test(){
        graphStore.addEntity();
    }
}
