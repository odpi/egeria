/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * The accumulators are thread-safe caches of types and instances from the open metadata repositories that
 * are building a list of unique results. A new accumulator is created for each federated request.
 * an accumulator is associated with one or more executor.
 */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators;

