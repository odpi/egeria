/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * Package defining the contract shared by all REST client connector implementations: the RESTClientCalls
 * interface and the abstract RESTClientConnector connector base class.  Egeria clients code only against
 * this contract, obtaining an implementation from the REST Client Factory, so that the HTTP library
 * actually used to issue REST calls can be changed without any client code changing.
 */
package org.odpi.openmetadata.adapters.connectors.restclients;
