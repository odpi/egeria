/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * This package provides the first failure data capture support for the OCF module.
 * This includes an error code enum, a base class for runtime exceptions, a base class for checked exceptions plus
 * implementation of each specific exception.
 *
 * The error code enum (OCFErrorCode) has an entry for each unique situation
 * where an exception is returned.  Each entry defines:
 *
 * <ul>
 *     <li>A unique id for the error</li>
 *     <li>An HTTP error code for rest calls</li>
 *     <li>A unique message Id</li>
 *     <li>Message text with place holders for specific values</li>
 *     <li>A description of the cause of the error and system action as a result.</li>
 *     <li>A description of how to correct the error (if known)</li>
 * </ul>
 *
 * Each exception (whether a checked or runtime exception) has two constructors.
 * The first constructor is used when a new error has been detected.
 * The second constructor is used when another exception has been caught.
 * This caught exception is passed on the constructor so it is effectively
 * embedded in the resulting OCF exception.
 *
 * All constructors take the values from the error code enum to define the cause and resolution.
 * There is also care to ensure that an error message is passed to the super class as the default
 * behaviour is to use toString() to populate the error message.
 */
package org.odpi.openmetadata.frameworks.connectors.ffdc;
