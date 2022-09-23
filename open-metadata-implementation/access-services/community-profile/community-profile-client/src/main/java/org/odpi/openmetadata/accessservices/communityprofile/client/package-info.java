/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * The Community Profile OMAS's clients provide access to metadata about people, communities, organizations, teams, roles, profiles and user identities.
 *
 * Most clients issue REST API calls to an Open Metadata Server.  There is a shared base class (CommunityProfileBaseClient) that issues
 * the rest calls that create, update and deleted metadata.
 *
 * The REST API clients are:
 *
 * <ul>
 *     <li>
 *         OrganizationManagement - maintaining information about profiles, roles, contact details, contribution records.
 *     </li>
 *     <li>
 *         CommunityManagement - supporting communities of people collaborating across an organization.
 *     </li>
 *     <li>
 *         LocationManagement - maintaining information about the locations used by an organization.
 *     </li>
 *     <li>
 *         UserIdentityManagement - maintaining information about user identities.
 *     </li>
 *     <li>
 *         SecurityGroupManagement - maintaining information about security groups.
 *     </li>
 *     <li>
 *         ValidValueManagement - maintaining valid value sets and definitions.
 *     </li>
 *     <li>
 *         MetadataSourceClient - maintains information about external sources of information about the organization.  The identifiers of these systems are passed on the externalSourceGUID and externalSourceName parameters of methods that are populating metadata from an external source.
 *     </li>
 *     <li>
 *         ConnectedAssetClient - manages the retrieval of connections, and the creation of resource connectors used to access the content of data sources and services.
 *     </li>
 *     <li>
 *         OpenMetadataStoreClient - common metadata support for all access services that provide both generally useful services along with fine-grained, generic access to the metadata in the metadata store.
 *     </li>
 * </ul>
 *
 * CommunityProfileEventClient allows you to register a listener to receive Community Profile OMAS Out Topic events.
 */
package org.odpi.openmetadata.accessservices.communityprofile.client;