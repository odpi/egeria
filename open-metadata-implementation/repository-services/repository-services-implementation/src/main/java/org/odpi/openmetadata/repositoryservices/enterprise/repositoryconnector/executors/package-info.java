/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * The executors issue a request to one or more metadata repositories.  They store their results in an accumulator and so can be cloned to
 * operate across multiple threads - where there would be an executor for each thread that is executing a request.
 */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;
