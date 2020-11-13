/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.properties;

import java.util.List;

/**
 * The DigitalServiceDependencyGraph is used to return a dependency graph for a digital service.
 */
public class DigitalServiceDependencyGraph extends DigitalServiceElementHeader
{
    private static final long    serialVersionUID = 1L;

    private DigitalServiceDependencyDirection direction                  = null;
    private DigitalService                    root                       = null;
    private List<DigitalService>              relatedDigitalServices     = null;
    private List<DigitalServiceDependency>    digitalServiceDependencies = null;
}
