/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity;

import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.properties.Glossary;


/**
 * OpenMetadataGlossarySecurity assures the access to glossary content.
 * Glossary categories and terms are anchored to a single glossary.
 * Glossary terms may be linked to multiple categories, some in different glossaries.
 * This interface allows the security connector to control whether a user can:
 *
 * <ul>
 *     <li>Create a new glossary.</li>
 *     <li>Update the properties of a glossary.</li>
 *     <li>Delete a glossary and all of its anchored contents.</li>
 *     <li>Read glossary terms and categories linked to a glossary.</li>
 *     <li>Create new terms and categories within (ie anchored to) the glossary.</li>
 *     <li>Create new term-to-term relationships with a term within (ie anchored to) the glossary.</li>
 *     <li>Add feedback to terms and categories within (ie anchored to) the glossary.</li>
 *     <li>Update terms and categories anchored to the glossary.</li>
 *     <li>Update instance status of a term anchored to the glossary.</li>
 *     <li>Update term-to-term relationships with a term within (ie anchored to) the glossary.</li>
 *     <li>Delete terms and categories anchored to the glossary.</li>
 *     <li>Delete term-to-term relationships with a term within (ie anchored to) the glossary.</li>
 *     <li>Link a term to a category that is anchored to the glossary.</li>
 *     <li>Unlink a term from a category that is anchored to the glossary.</li>
 * </ul>
 */
public interface OpenMetadataGlossarySecurity
{
    /**
     * Tests for whether a specific user should have the right to create a glossary.
     *
     * @param userId identifier of user
     * @param glossary new glossary details
     * @throws UserNotAuthorizedException the user is not authorized to change this glossary
     */
    void  validateUserForGlossaryCreate(String    userId,
                                        Glossary  glossary) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have read access to a specific glossary and its contents.
     *
     * @param userId identifier of user
     * @param glossary glossary details
     * @throws UserNotAuthorizedException the user is not authorized to access this glossary
     */
    void  validateUserForGlossaryRead(String    userId,
                                      Glossary  glossary) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to update the properties of a glossary.
     *
     * @param userId identifier of user
     * @param originalGlossary original glossary details
     * @param newGlossary new glossary details
     * @throws UserNotAuthorizedException the user is not authorized to change this glossary
     */
    void  validateUserForGlossaryDetailUpdate(String   userId,
                                              Glossary originalGlossary,
                                              Glossary newGlossary) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to update elements attached directly
     * to a glossary such as glossary terms and categories.  These updates could be to their properties,
     * classifications and relationships.  It also includes attaching valid values but not semantic assignments
     * since they are considered updates to the associated asset.
     *
     * @param userId identifier of user
     * @param glossary glossary details
     * @throws UserNotAuthorizedException the user is not authorized to change this glossary
     */
    void  validateUserForGlossaryMemberUpdate(String    userId,
                                              Glossary  glossary) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to update the instance status of a term
     * anchored in a glossary.
     *
     * @param userId identifier of user
     * @param glossary glossary details
     * @throws UserNotAuthorizedException the user is not authorized to change this glossary
     */
    void  validateUserForGlossaryMemberStatusUpdate(String    userId,
                                                    Glossary  glossary) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to attach feedback - such as comments,
     * ratings, tags and likes, to the glossary.
     *
     * @param userId identifier of user
     * @param glossary original glossary details
     * @throws UserNotAuthorizedException the user is not authorized to change this glossary
     */
    void  validateUserForGlossaryFeedback(String   userId,
                                          Glossary glossary) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to delete a glossary and all of its contents.
     *
     * @param userId identifier of user
     * @param glossary original glossary details
     * @throws UserNotAuthorizedException the user is not authorized to change this glossary
     */
    void  validateUserForGlossaryDelete(String   userId,
                                        Glossary glossary) throws UserNotAuthorizedException;
}
