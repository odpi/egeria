/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

/**
 * The registration package provides the definitions and interfaces to describe each of the Open Metadata
 * Access Services and enable them to register with the admin services.  The admin services then
 * call the registered access services at start up and shutdown of a server instance if it is
 * configured in the server instance's configuration document.
 *
 * AccessServiceDescription lists all of the identified access services, giving them an unique identifier
 * and including a URL to their home page.
 *
 * AccessServiceAdmin is the API that each access service implements.  It is called to initialize and shutdown
 * the access service.
 *
 * AccessServiceRegistration holds the properties of an access service that is passed to the admin services
 * during registration.
 *
 * AccessServiceOperationalStatus indicates whether an access service is enabled or disabled.
 */