/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.viewgenerator.virtulaizationsolutionconnector;

public interface VirtualizationSolutionConnectorBase {

    Object query (String url);
    Boolean create (String url, Object dataSet);
    Boolean update (String url, Object dataSet);
    Boolean delete (String url);

}
