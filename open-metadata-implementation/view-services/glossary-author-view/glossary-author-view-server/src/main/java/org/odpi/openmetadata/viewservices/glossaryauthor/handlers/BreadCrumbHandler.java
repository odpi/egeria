/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.SubjectAreaNodeClients;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.RelationshipType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.viewservices.glossaryauthor.ffdc.GlossaryAuthorErrorCode;
import org.odpi.openmetadata.viewservices.glossaryauthor.properties.BreadCrumb;
import org.odpi.openmetadata.viewservices.glossaryauthor.properties.BreadCrumbTrail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The breadcrumb handler is initialised with Subject Area Node clients clients (allowing calls to the Subject Area OMAs to be made) and
 * the userId under which those calls should be issued.
 * This class exposes a method which is supplied the
 * <ul>
 *     <li>Glossary guid (the top of the bread crumb)</li>
 *     <li>the root Category the top of the Categories</li>
 *     <li>the leaf Category the bottom of the Categories </li>
 *     <li>a Term guid which if specified is the bottom of the breadcrumb trail.</li>
 * </ul>
 * The guids represent nodes that are expected to determine a breadcrumb trail. If the supplied guids do not form
 * a trail then an error occurs.
 *
 * The handler exposes methods for breadcrumb functionality for the Glossary Author view
 */
public class BreadCrumbHandler {
    static String className = BreadCrumbHandler.class.getName();
    static String restAPIName = "getBreadCrumbTrail";
    List<BreadCrumb> breadCrumbs = new ArrayList<>();
    Category currentCategory = null;
    private Set<String> guids = new HashSet<>();
    private SubjectAreaNodeClients clients;
    private String userId;



    public BreadCrumbHandler(SubjectAreaNodeClients clients, String userId) {
        this.clients =clients;
        this.userId = userId;
    }

    private boolean hasParentCategory() throws InvalidParameterException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, UserNotAuthorizedException {
        boolean hasParent = false;
        Category parentCategory = null;
        List<Relationship> categoryRelationships = clients.terms().getAllRelationships(userId, currentCategory.getSystemAttributes().getGUID());
        for (Relationship categoryRelationship : categoryRelationships) {
            if (categoryRelationship.getRelationshipType().equals(RelationshipType.CategoryHierarchyLink)) {
                // if there is more than one parent  - it will get the last one its sees
                String parentCategoryGuid =categoryRelationship.getEnd1().getNodeGuid();
                if (!parentCategoryGuid.equals(currentCategory.getSystemAttributes().getGUID())) {
                    parentCategory = clients.categories().getByGUID(userId, parentCategoryGuid);
                }
            }
        }
        if (parentCategory != null) {
            hasParent = true;
            currentCategory = parentCategory;
        }

        return hasParent;
    }

    public List<BreadCrumb> getBreadCrumbTrail(String glossaryGuid, String rootCategoryGuid, String leafCategoryGuid, String termGuid) throws InvalidParameterException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException {

        Category leafCategory = null;
        Category rootCategory = null;
        Glossary glossary = null;
        Term term = null;
        // If a guid is specified then it needs to be valid
        if (leafCategoryGuid != null) {
            leafCategory = clients.categories().getByGUID(userId, leafCategoryGuid);
        }
        if (rootCategoryGuid != null) {
            rootCategory = clients.categories().getByGUID(userId, rootCategoryGuid);
        }
        if (glossaryGuid != null) {
            glossary = clients.glossaries().getByGUID(userId, glossaryGuid);
        }
        if (termGuid !=null) {
            term = clients.terms().getByGUID(userId, termGuid);
        }

        // if we got here the supplied guids must match the appropriate types.
        if (termGuid == null) {
            // set the lowest breadcrumb
            if (leafCategory != null) {
                breadCrumbs.add(new BreadCrumb(leafCategoryGuid, leafCategory.getName(), NodeType.Category));
            } else  if (rootCategory != null) {
                breadCrumbs.add(new BreadCrumb(rootCategoryGuid, rootCategory.getName(), NodeType.Category));
            }
        } else {
            // create term breadcrumb
            breadCrumbs.add(new BreadCrumb(termGuid, term.getName(), NodeType.Term));
            List<Relationship> termRelationships = clients.terms().getAllRelationships(userId, termGuid);
            if (rootCategory == null && leafCategory == null ) {
                boolean termCorrectlyAnchored = false;
               // check that the term is anchored in the glossary
                for (Relationship termRelationship : termRelationships) {
                    if (termRelationship.getRelationshipType().equals(RelationshipType.TermAnchor)) {
                        if (termRelationship.getEnd1().getNodeGuid().equals(glossaryGuid)) {
                            termCorrectlyAnchored = true;
                        } else {
                            // Found a TermAnchor, but not pointing to the right glossary
                            ExceptionMessageDefinition messageDefinition = GlossaryAuthorErrorCode.GET_BREADCRUMBS_TERM_NOT_IN_GLOSSARY.getMessageDefinition( term.getName(),termGuid,  glossary.getName(), glossaryGuid);
                            throw new org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException(messageDefinition, className, restAPIName, "TermGuid", termGuid);
                        }
                    }
                }
                if (!termCorrectlyAnchored) {
                    // Error term not in glossary as we did not find a TermAnchor
                    ExceptionMessageDefinition messageDefinition = GlossaryAuthorErrorCode.GET_BREADCRUMBS_TERM_NOT_IN_GLOSSARY.getMessageDefinition( term.getName(),termGuid,glossary.getName(), glossaryGuid );
                    throw new org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException(messageDefinition, className, restAPIName, "TermGuid", termGuid);

                }
                // if we did not find a term anchor - error
            } else if (rootCategory !=null && leafCategory == null ) {
                // check that the rootCategory categories this term
                boolean termCorrectlyCategorized = false;
                for (Relationship termRelationship : termRelationships) {
                    if (termRelationship.getRelationshipType().equals(RelationshipType.TermCategorization)) {
                        if (termRelationship.getEnd1().getNodeGuid().equals(rootCategoryGuid)) {
                            breadCrumbs.add(new BreadCrumb(rootCategoryGuid, rootCategory.getName(), NodeType.Category));
                            termCorrectlyCategorized = true;
                        }
                    }
                }
                if (!termCorrectlyCategorized) {
                    // Error term not in root category
                    ExceptionMessageDefinition messageDefinition = GlossaryAuthorErrorCode.GET_BREADCRUMBS_TERM_NOT_IN_ROOT_CATEGORY.getMessageDefinition( term.getName(),termGuid,rootCategory.getName(), rootCategoryGuid );
                    throw new org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException(messageDefinition, className, restAPIName, "TermGuid", termGuid);

                }
            } else if (rootCategory !=null && leafCategory != null ) {
                // check that the leaf category categories this term
                boolean termCorrectlyCategorized = false;
                for (Relationship termRelationship : termRelationships) {
                    if (termRelationship.getRelationshipType().equals(RelationshipType.TermCategorization)) {
                        if (termRelationship.getEnd1().getNodeGuid().equals(leafCategoryGuid)) {
                            breadCrumbs.add(new BreadCrumb(leafCategoryGuid, leafCategory.getName(), NodeType.Category));
                            termCorrectlyCategorized = true;
                        }
                    }
                }
                if (!termCorrectlyCategorized) {
                    // Error we did not find the expected categorization
                    ExceptionMessageDefinition messageDefinition = GlossaryAuthorErrorCode.GET_BREADCRUMBS_TERM_NOT_IN_LEAF_CATEGORY.getMessageDefinition(termGuid, term.getName(), leafCategoryGuid, leafCategory.getName());
                    throw new org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException(messageDefinition, className, restAPIName, "TermGuid", termGuid);

                }

            }
        }
        // At this stage we have processed the Term and the relationships that it has to its parent. We have also added the first Category breadcrumb

        if (leafCategory != null && rootCategory != null) {
            // we have at least 2 Categories in our tree.
            currentCategory = leafCategory;

            while (this.hasParentCategory()) {
                breadCrumbs.add(new BreadCrumb(currentCategory.getSystemAttributes().getGUID(), currentCategory.getName(), NodeType.Category));
            }
            // the currentCategory should be the top of the tree.
            if (!currentCategory.getSystemAttributes().getGUID().equals(rootCategoryGuid)) {
                // error the supplied rootCategory not found to be the root in the category parents
                ExceptionMessageDefinition messageDefinition = GlossaryAuthorErrorCode.GET_BREADCRUMBS_ROOT_CATEGORY_NOT_ROOT.getMessageDefinition(rootCategoryGuid, rootCategory.getName());
                throw new org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException(messageDefinition, className, restAPIName, "rootCategoryGuid", rootCategoryGuid);

            } else {
                // check that the CategoryAnchor glossary is the supplied one

                List<Relationship> categoryRelationships = clients.terms().getAllRelationships(userId, currentCategory.getSystemAttributes().getGUID());
                for (Relationship categoryRelationship : categoryRelationships) {
                    if (categoryRelationship.getRelationshipType().equals(RelationshipType.CategoryAnchor)) {
                        // if there is more than one parent  - it will get the last one its sees
                        if (!glossary.getSystemAttributes().getGUID().equals(glossaryGuid)) {
                            // the supplied glossary guid does not match the owning glossary of the rootCategory
                            ExceptionMessageDefinition messageDefinition = GlossaryAuthorErrorCode.GET_BREADCRUMBS_ROOT_CATEGORY_NOT_GLOSSARY.getMessageDefinition(rootCategoryGuid, rootCategory.getName(), glossaryGuid, glossary.getName());
                            throw new org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException(messageDefinition, className, restAPIName, "rootCategoryGuid", rootCategoryGuid);
                        }
                    }
                }
            }
        }
        // then add the glossary to the BreadCrumb.
        breadCrumbs.add(new BreadCrumb(glossaryGuid, glossary.getName(), NodeType.Glossary));
        BreadCrumbTrail breadCrumbTrail = new BreadCrumbTrail(breadCrumbs);

        return breadCrumbTrail.getBreadCrumbs();
    }
}