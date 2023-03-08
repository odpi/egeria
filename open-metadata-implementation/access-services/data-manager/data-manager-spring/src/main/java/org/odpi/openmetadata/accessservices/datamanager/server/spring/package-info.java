/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


/**
 * REST API operations and Swagger Annotations
 *
 * The data manager has 3 parts to its REST API.
 *
 * The DataManagerOMASResource defines the general operations for all types of data managers.  This includes the
 * operations for retrieving the out topic, for registering a data manager
 * integrator and retrieving the identifiers of a pre-registered data manager.
 *
 * The DatabaseManagerResource supports metadata from database servers and
 * the FilesResource supports metadata about unmanaged files on disk.
 */
package org.odpi.openmetadata.accessservices.datamanager.server.spring;
