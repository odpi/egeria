/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.digitalarchitecture.properties;

import java.util.List;

/**
 * The DigitalServiceDependencyGraph is used to return a dependency graph for a digital service.
 */
public class DigitalServiceDependencyGraph extends DigitalArchitectureElementHeader
{
    private DigitalServiceDependencyDirection direction                  = null;
    private DigitalService                    root                       = null;
    private List<DigitalService>              relatedDigitalServices     = null;
    private List<DigitalServiceDependency>    digitalServiceDependencies = null;
}
