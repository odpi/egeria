/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.projectmanagement.converters;

/**
 * The converters are responsible for converting entities, classifications and relationships retrieved from the
 * open metadata repositories into Community Profile OMAS beans.
 *
 * The inheritance structure follows closely to the inheritance structure of the beans themselves and the naming
 * convention is consistent.   The converter used is the one that corresponds to the desired bean.
 * When a converter is created, it is passed the entity with its embedded classifications and a relationship if
 * required on the constructor.  The bean is retrieved by calling getBean().
 */