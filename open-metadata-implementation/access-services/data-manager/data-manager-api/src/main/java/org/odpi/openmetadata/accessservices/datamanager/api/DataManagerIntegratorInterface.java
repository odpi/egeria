/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.api;

import org.odpi.openmetadata.accessservices.datamanager.properties.SoftwareServerCapabilitiesProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * DataManagerIntegratorInterface is the interface used to define information about the integration daemon that is
 * extracting metadata from a data manager and maintaining it in open metadata.
 *
 * The integration daemon is represented by a software server capability in open metadata.  It has a classification
 * to indicate that it is data manager integrator.
 */
public interface DataManagerIntegratorInterface
{
    /**
     * Create information about the integration daemon that is managing the acquisition of metadata from the
     * data manager.  Typically this is Egeria's data manager proxy.
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
    String  createDataManagerIntegrator(String                                userId,
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
    String  getDataManagerIntegratorGUID(String  userId,
                                         String  qualifiedName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;
}
