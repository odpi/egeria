/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.dataplatform.api;

import org.odpi.openmetadata.accessservices.dataplatform.properties.SoftwareServerCapabilitiesProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * DataPlatformIntegratorInterface is the interface used to define information about the integration daemon that is
 * extracting metadata from a data platform and maintaining it in open metadata.
 *
 * The integration daemon is represented by a software server capability in open metadata.  It has a classification
 * to indicate that it is data platform integrator.
 */
public interface DataPlatformIntegratorInterface
{
    /**
     * Create information about the integration daemon that is managing the acquisition of metadata from the
     * data platform.  Typically this is Egeria's data platform proxy.
     *
     * @param userId calling user
     * @param integratorCapabilities description of the integration daemon (specify qualified name at a minimum)
     *
     * @return unique identifier of the integration daemon's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    String  createDataPlatformIntegrator(String                                userId,
                                         SoftwareServerCapabilitiesProperties  integratorCapabilities) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException;


    /**
     * Retrieve the unique identifier of the integration daemon.
     *
     * @param userId calling user
     * @param qualifiedName unique name of the integration daemon
     *
     * @return unique identifier of the integration daemon's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    String  getDataPlatformIntegratorGUID(String  userId,
                                          String  qualifiedName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;
}
