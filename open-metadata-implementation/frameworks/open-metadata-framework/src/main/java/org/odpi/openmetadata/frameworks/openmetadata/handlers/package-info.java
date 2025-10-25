/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * Provides specialised handlers that use the Open Metadata Store Services client.  These handlers provide the
 * common function that serves the view services REST APIs and the client contents of the connectors.
 * Most of the function is located in the base class.  This is possible because the return class is typically an
 * OpenMetadataRoot element that uses polymorphism to support all types of entity property beans
 * (OpenMetadataRootProperties).  This is enabled through common builders (incoming properties) and
 * converters (return values) that work up and down the type/bean inheritance hierarchy.
 */
package org.odpi.openmetadata.frameworks.openmetadata.handlers;