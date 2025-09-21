/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.productcatalog;


import org.odpi.openmetadata.archiveutilities.openconnectors.core.CorePackArchiveWriter;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.AssignmentType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.Category;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ResourceUse;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.samples.archiveutilities.EgeriaBaseArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.businesssystems.CocoBusinessSystemsArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.governanceprogram.CocoGovernanceProgramArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.governanceprogram.CocoSubjectAreaDefinition;
import org.odpi.openmetadata.samples.archiveutilities.organization.CocoOrganizationArchiveWriter;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ScopeDefinition;
import org.odpi.openmetadata.samples.archiveutilities.sustainability.CocoSustainabilityArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.sustainability.SustainabilitySubjectAreaDefinition;

import java.util.*;


/**
 * CocoProductCatalogArchiveWriter creates a physical open metadata archive file containing Coco's digital product
 * catalog structure that is populated by a solution that harvests Coco's valid values into reference data products.
 */
public class CocoProductCatalogArchiveWriter extends EgeriaBaseArchiveWriter
{
    private static final String archiveFileName = "CocoProductCatalogArchive.omarchive";

    private static final String digitalProductsSubjectArea = "SubjectArea:DigitalProducts";
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "34eae1f9-1a81-473b-8252-7c83ae9803d9";
    private static final String                  archiveName        = "Coco Pharmaceuticals Digital Project Catalog";
    private static final String                  archiveDescription = "The base definitions for Coco Pharmaceuticals' digital product catalog.";

    private static final Date                    creationDate       = new Date(1639984840038L);

    /**
     * Default constructor initializes the archive.
     */
    public CocoProductCatalogArchiveWriter()
    {
        super(archiveGUID,
              archiveName,
              archiveDescription,
              creationDate,
              archiveFileName,
              new OpenMetadataArchive[]{ new CorePackArchiveWriter().getOpenMetadataArchive(),
                      new CocoOrganizationArchiveWriter().getOpenMetadataArchive(),
                      new CocoGovernanceProgramArchiveWriter().getOpenMetadataArchive(),
                      new CocoSustainabilityArchiveWriter().getOpenMetadataArchive(),
                      new CocoBusinessSystemsArchiveWriter().getOpenMetadataArchive()});
    }


    /**
     * Add the content to the archive builder.
     */
    @Override
    public void getArchiveContent()
    {
        writeDomains();
        writeCategoryValidValueSet();
        writeQueryTypeValidValueSet();
        writeSubjectAreaDefinitions();
        writeProductCatalogFolders();
        writeDigitalProducts();
        writeGlossary();
        writeGovernanceDefinitions();
        writeRoles();
    }


    /**
     * Creates Category valid value set to show the common categories used in CocoPharmaceuticals.
     */
    private void writeCategoryValidValueSet()
    {
        String validValueSetQName = openMetadataValidValueSetPrefix + ProductCategoryDefinition.validValueSetName;

        String validValueSetGUID = archiveHelper.addValidValue(OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                               validValueSetQName,
                                                               ProductCategoryDefinition.validValueSetName,
                                                               ProductCategoryDefinition.validValueSetDescription,
                                                               ProductCategoryDefinition.validValueSetUsage,
                                                               ProductCategoryDefinition.validValueSetScope,
                                                               null,
                                                               null,
                                                               null);

        if (validValueSetGUID != null)
        {
            for (ProductCategoryDefinition productCategoryDefinition : ProductCategoryDefinition.values())
            {
                String validValueQName = openMetadataValidValueSetPrefix + ProductCategoryDefinition.validValueSetName + "." + productCategoryDefinition.getPreferredValue();

                String validValueGUID = archiveHelper.addValidValue(OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                    validValueQName,
                                                                    productCategoryDefinition.getDisplayName(),
                                                                    null,
                                                                    ProductCategoryDefinition.validValueSetUsage,
                                                                    ProductCategoryDefinition.validValueSetScope,
                                                                    productCategoryDefinition.getPreferredValue(),
                                                                    null,
                                                                    null);

                if (validValueGUID != null)
                {
                    archiveHelper.addValidValueMembershipRelationship(validValueSetQName, validValueQName, false /* no default value */);
                }
            }
        }
    }


    /**
     * Creates QueryType valid value set to show the common queryTypes used to create data sets in CocoPharmaceuticals.
     */
    private void writeQueryTypeValidValueSet()
    {
        String validValueSetQName = openMetadataValidValueSetPrefix + ProductQueryTypeDefinition.validValueSetName;

        String validValueSetGUID = archiveHelper.addValidValue(OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                               validValueSetQName,
                                                               ProductQueryTypeDefinition.validValueSetName,
                                                               ProductQueryTypeDefinition.validValueSetDescription,
                                                               ProductQueryTypeDefinition.validValueSetUsage,
                                                               ProductQueryTypeDefinition.validValueSetScope,
                                                               null,
                                                               null,
                                                               null);

        if (validValueSetGUID != null)
        {
            for (ProductQueryTypeDefinition productQueryTypeDefinition : ProductQueryTypeDefinition.values())
            {
                String validValueQName = openMetadataValidValueSetPrefix + ProductQueryTypeDefinition.validValueSetName + "." + productQueryTypeDefinition.getPreferredValue();

                String validValueGUID = archiveHelper.addValidValue(OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                    validValueQName,
                                                                    productQueryTypeDefinition.getDisplayName(),
                                                                    null,
                                                                    ProductQueryTypeDefinition.validValueSetUsage,
                                                                    ProductQueryTypeDefinition.validValueSetScope,
                                                                    productQueryTypeDefinition.getPreferredValue(),
                                                                    null,
                                                                    null);

                if (validValueGUID != null)
                {
                    archiveHelper.addValidValueMembershipRelationship(validValueSetQName, validValueQName, false /* no default value */);
                }
            }
        }
    }


    /**
     * Populates the digital product catalog
     */
    private void writeDigitalProducts()
    {
    }


    /**
     * Creates SubjectArea definitions.
     */
    private void writeSubjectAreaDefinitions()
    {
        for (ProductSubjectAreaDefinition subjectAreaDefinition : ProductSubjectAreaDefinition.values())
        {
            String subjectAreaGUID = archiveHelper.addSubjectAreaDefinition(subjectAreaDefinition.getQualifiedName(),
                                                                            subjectAreaDefinition.getSubjectAreaName(),
                                                                            subjectAreaDefinition.getDisplayName(),
                                                                            subjectAreaDefinition.getDescription(),
                                                                            subjectAreaDefinition.getScope(),
                                                                            subjectAreaDefinition.getUsage(),
                                                                            subjectAreaDefinition.getDomain(),
                                                                            null,
                                                                            null);

            if (subjectAreaDefinition.getParent() != null)
            {
                String subjectAreaParentGUID = archiveHelper.queryGUID(subjectAreaDefinition.getParent().getQualifiedName());
                archiveHelper.addSubjectAreaHierarchy(subjectAreaParentGUID, subjectAreaGUID);
            }
        }
    }


    /**
     * Creates the structure of the product catalog.
     */
    private void writeProductCatalogFolders()
    {
        String rootFolderGUID = archiveHelper.addCollection(OpenMetadataType.COLLECTION.typeName,
                                                            null,
                                                            OpenMetadataType.COLLECTION.typeName,
                                                            OpenMetadataType.COLLECTION.typeName,
                                                            null,
                                                            OpenMetadataType.ROOT_COLLECTION_CLASSIFICATION.typeName,
                                                            "Collection::DigitalProductCatalogRootCollection",
                                                            "Coco Pharmaceuticals' digital product catalog",
                                                            "Manages the key digital resources needed to run Coco Pharmaceuticals' business.  The digital resources are organized by subject area and are managed using the digital product management governance model.",
                                                            ProductCategoryDefinition.DIGITAL_PRODUCT_CATALOG.getDisplayName(),
                                                            null,
                                                            null,
                                                            null);

        for (CocoSubjectAreaDefinition subjectAreaDefinition : CocoSubjectAreaDefinition.values())
        {
            if (subjectAreaDefinition.getParent() != null)
            {
                writeProductCatalogFolder(rootFolderGUID,
                                          subjectAreaDefinition.getSubjectAreaName(),
                                          subjectAreaDefinition.getDisplayName(),
                                          subjectAreaDefinition.getDescription(),
                                          subjectAreaDefinition.getParent().getSubjectAreaName());
            }
            else
            {
                writeProductCatalogFolder(rootFolderGUID,
                                          subjectAreaDefinition.getSubjectAreaName(),
                                          subjectAreaDefinition.getDisplayName(),
                                          subjectAreaDefinition.getDescription(),
                                          null);
            }
        }

        for (SustainabilitySubjectAreaDefinition subjectAreaDefinition : SustainabilitySubjectAreaDefinition.values())
        {
            if (subjectAreaDefinition.getParent() != null)
            {
                writeProductCatalogFolder(rootFolderGUID,
                                          subjectAreaDefinition.getSubjectAreaName(),
                                          subjectAreaDefinition.getDisplayName(),
                                          subjectAreaDefinition.getDescription(),
                                          subjectAreaDefinition.getParent().getSubjectAreaName());
            }
            else
            {
                writeProductCatalogFolder(rootFolderGUID,
                                          subjectAreaDefinition.getSubjectAreaName(),
                                          subjectAreaDefinition.getDisplayName(),
                                          subjectAreaDefinition.getDescription(),
                                          null);
            }
        }

        for (ProductSubjectAreaDefinition subjectAreaDefinition : ProductSubjectAreaDefinition.values())
        {
            if (subjectAreaDefinition.getParent() != null)
            {
                writeProductCatalogFolder(rootFolderGUID,
                                          subjectAreaDefinition.getSubjectAreaName(),
                                          subjectAreaDefinition.getDisplayName(),
                                          subjectAreaDefinition.getDescription(),
                                          subjectAreaDefinition.getParent().getSubjectAreaName());
            }
            else
            {
                writeProductCatalogFolder(rootFolderGUID,
                                          subjectAreaDefinition.getSubjectAreaName(),
                                          subjectAreaDefinition.getDisplayName(),
                                          subjectAreaDefinition.getDescription(),
                                          null);
            }
        }
    }


    /**
     * Creates a single folder in the product catalog.
     *
     * @param rootFolderGUID top level folder for subject areas with no parent
     * @param subjectAreaName full name of the subject area
     * @param displayName display name
     * @param description description
     * @param parentSubjectAreaName name of parent or null for top level
     */
    private void writeProductCatalogFolder(String rootFolderGUID,
                                           String subjectAreaName,
                                           String displayName,
                                           String description,
                                           String parentSubjectAreaName)
    {
        String folderGUID = archiveHelper.addCollection(OpenMetadataType.COLLECTION.typeName,
                                                        rootFolderGUID,
                                                        OpenMetadataType.COLLECTION.typeName,
                                                        OpenMetadataType.COLLECTION.typeName,
                                                        null,
                                                        OpenMetadataType.FOLDER_COLLECTION_CLASSIFICATION.typeName,
                                                        "DigitalProductCatalogFolder::" + subjectAreaName,
                                                        displayName,
                                                        description,
                                                        ProductCategoryDefinition.DIGITAL_PRODUCT_CATALOG.getDisplayName(),
                                                        null,
                                                        null,
                                                        null);

        archiveHelper.addSubjectAreaClassification(folderGUID, subjectAreaName);

        if (parentSubjectAreaName != null)
        {
            String folderParentGUID = archiveHelper.queryGUID("DigitalProductCatalogFolder::" + parentSubjectAreaName);
            archiveHelper.addMemberToCollection(folderParentGUID, folderGUID, null);
        }
        else
        {
            archiveHelper.addMemberToCollection(rootFolderGUID, folderGUID, null);
        }
    }


    private void writeGovernanceDefinitions()
    {
        for (ProductGovernanceDefinition governanceDefinition : ProductGovernanceDefinition.values())
        {
            archiveHelper.setGUID(governanceDefinition.getQualifiedName(), governanceDefinition.getGUID());
            String governanceDefinitionGUID = archiveHelper.addGovernanceDefinition(governanceDefinition.getType(),
                                                                                    governanceDefinition.getQualifiedName(),
                                                                                    governanceDefinition.getTitle(),
                                                                                    governanceDefinition.getSummary(),
                                                                                    governanceDefinition.getDescription(),
                                                                                    governanceDefinition.getScope().getPreferredValue(),
                                                                                    null,
                                                                                    governanceDefinition.getDomain(),
                                                                                    governanceDefinition.getImportance(),
                                                                                    governanceDefinition.getImplications(),
                                                                                    governanceDefinition.getOutcomes(),
                                                                                    governanceDefinition.getResults(),
                                                                                    null,
                                                                                    null);

            assert governanceDefinition.getGUID().equals(governanceDefinitionGUID);
        }
        /*
        for (GovernanceDefinitionLink link : GovernanceDefinitionLink.values())
        {
            archiveHelper.addGovernanceDefinitionDelegationRelationship(link.getRelationshipType(),
                                                                        link.getParentDefinition().getQualifiedName(),
                                                                        link.getChildDefinition().getQualifiedName(),
                                                                        null);
        }

         */
    }


    /**
     * Creates Governance Role definitions and links them to .
     */
    private void writeRoles()
    {
        String communityQName = "Community: " + ProductDomainDefinition.DIGITAL_PRODUCT_MANAGEMENT.getQualifiedName();

        for (ProductRoleDefinition roleDefinition : ProductRoleDefinition.values())
        {
            archiveHelper.addActorRole(roleDefinition.getTypeName(),
                                       roleDefinition.getQualifiedName(),
                                       roleDefinition.getIdentifier(),
                                       roleDefinition.getDisplayName(),
                                       roleDefinition.getDescription(),
                                       roleDefinition.getScope().getPreferredValue(),
                                       false,
                                       0,
                                       null,
                                       null);

            archiveHelper.addCommunityMembershipRelationship(communityQName,
                                                             roleDefinition.getQualifiedName(),
                                                             AssignmentType.CONTRIBUTOR.getName());
        }
    }


    /**
     * Creates Governance Domain definitions.
     */
    private void writeDomains()
    {
        String governanceDomainSetGUID = this.getParentSet(null,
                                                           null,
                                                           OpenMetadataProperty.DOMAIN_IDENTIFIER.name,
                                                           null);

        for (ProductDomainDefinition domainDefinition : ProductDomainDefinition.values())
        {
            this.archiveHelper.addValidValue(null,
                                             governanceDomainSetGUID,
                                             governanceDomainSetGUID,
                                             OpenMetadataType.VALID_METADATA_VALUE.typeName,
                                             OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                             null,
                                             OpenMetadataType.VALID_METADATA_VALUE.typeName,
                                             domainDefinition.getQualifiedName(),
                                             Category.VALID_METADATA_VALUES.getName(),
                                             domainDefinition.getDisplayName(),
                                             domainDefinition.getDescription(),
                                             domainDefinition.getNamespace(),
                                             OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                             DataType.INT.getName(),
                                             OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                             Integer.toString(domainDefinition.getDomainIdentifier()),
                                             null,
                                             false,
                                             null);

            String communityQName = "Community: " + domainDefinition.getQualifiedName();

            archiveHelper.addCommunity(null,
                                       communityQName,
                                       domainDefinition.getCommunityName(),
                                       "Community supporting " + domainDefinition.getDisplayName() + " that is lead by the governance domain leader and includes all the people supporting the domain.",
                                       "To provide a mechanism for communication and coordination of work across Coco Pharmaceuticals that supports this governance domain.",
                                       null,
                                       null);


            archiveHelper.addResourceListRelationship(domainDefinition.getQualifiedName(),
                                                      communityQName,
                                                      ResourceUse.SUPPORTING_PEOPLE.getResourceUse(),
                                                      null);


            String governanceOfficerQName = OpenMetadataType.GOVERNANCE_OFFICER.typeName + ": " + domainDefinition.getQualifiedName();
            archiveHelper.addGovernanceRole(OpenMetadataType.GOVERNANCE_OFFICER.typeName,
                                            governanceOfficerQName,
                                            domainDefinition.getDomainIdentifier(),
                                            "GOV_OFFICER:" + domainDefinition.getDomainIdentifier(),
                                            "Governance Officer for " + domainDefinition.getDisplayName(),
                                            null,
                                            null,
                                            true,
                                            1,
                                            null,
                                            null);

            if (domainDefinition.getGovernanceOfficer() != null)
            {
                archiveHelper.addPersonRoleAppointmentRelationship(domainDefinition.getGovernanceOfficer().getQualifiedName(),
                                                                   governanceOfficerQName,
                                                                   false,
                                                                   0);
            }
        }
    }


    private void writeGlossary()
    {
        String glossaryGUID = archiveHelper.addGlossary("Glossary::Digital Products",
                                                        "Digital Product Glossary",
                                                        "Terminology associated with Coco Pharmaceutical's digital product catalog.",
                                                        "English",
                                                        "For all Coco Pharmaceutical employees wishing to use or contribute to the digital product catalog.",
                                                        null,
                                                        ScopeDefinition.WITHIN_ORGANIZATION.getPreferredValue());

        archiveHelper.addSubjectAreaClassification(glossaryGUID, digitalProductsSubjectArea);

        Map<String, String> categoryLookup = new HashMap<>();
        for (ProductGlossaryCategoryDefinition productGlossaryCategoryDefinition : ProductGlossaryCategoryDefinition.values())
        {
            String glossaryCategoryGUID = archiveHelper.addGlossaryCategory(glossaryGUID,
                                                                            productGlossaryCategoryDefinition.getQualifiedName(),
                                                                            productGlossaryCategoryDefinition.getName(),
                                                                            productGlossaryCategoryDefinition.getDescription(),
                                                                            null);

            categoryLookup.put(productGlossaryCategoryDefinition.getName(), glossaryCategoryGUID);
        }

        for (ProductGlossaryTermDefinition productGlossaryTermDefinition : ProductGlossaryTermDefinition.values())
        {
            String glossaryTermGUID = archiveHelper.addTerm(glossaryGUID,
                                                            null,
                                                            false,
                                                            "GlossaryTerm::" + productGlossaryTermDefinition.getName(),
                                                            productGlossaryTermDefinition.getName(),
                                                            productGlossaryTermDefinition.getSummary(),
                                                            productGlossaryTermDefinition.getDescription(),
                                                            null,
                                                            productGlossaryTermDefinition.getAbbreviation(),
                                                            null,
                                                            false,
                                                            null,
                                                            null,
                                                            null,
                                                            null);

            if (productGlossaryTermDefinition.getCategory() != null)
            {
                archiveHelper.addTermToCategory(categoryLookup.get(productGlossaryTermDefinition.getCategory().getName()),
                                                glossaryTermGUID);
            }

            if (productGlossaryTermDefinition.getUrl() != null)
            {
                archiveHelper.addExternalReference(null,
                                                   glossaryTermGUID,
                                                   OpenMetadataType.GLOSSARY_TERM.typeName,
                                                   OpenMetadataType.GLOSSARY_TERM.typeName,
                                                   glossaryGUID,
                                                   OpenMetadataType.EXTERNAL_REFERENCE.typeName + "::" + productGlossaryTermDefinition.getName(),
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   productGlossaryTermDefinition.getUrl(),
                                                   originatorName,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   null);
            }
        }
    }



    /**
     * Create solution roles
     */
    private void addSolutionRoles()
    {
        for (ProductRoleDefinition productRoleDefinition : ProductRoleDefinition.values())
        {
            archiveHelper.setGUID(productRoleDefinition.getQualifiedName(), productRoleDefinition.getGUID());

            String solutionRoleGUID = archiveHelper.addActorRole(OpenMetadataType.SOLUTION_ACTOR_ROLE.typeName,
                                                                 productRoleDefinition.getQualifiedName(),
                                                                 productRoleDefinition.getIdentifier(),
                                                                 productRoleDefinition.getDisplayName(),
                                                                 productRoleDefinition.getDescription(),
                                                                 productRoleDefinition.getScope().getPreferredValue(),
                                                                 false,
                                                                 0,
                                                                 null,
                                                                 null);
            assert(solutionRoleGUID.equals(productRoleDefinition.getGUID()));
        }
    }


    /**
     * Link solution components together.
     */
    private void addSolutionComponentWires()
    {
        for (SolutionComponentWire solutionComponentWire : SolutionComponentWire.values())
        {
            archiveHelper.addSolutionLinkingWireRelationship(solutionComponentWire.getComponent1().getGUID(),
                                                             solutionComponentWire.getComponent2().getGUID(),
                                                             solutionComponentWire.getLabel(),
                                                             solutionComponentWire.getDescription(),
                                                             solutionComponentWire.getISCQualifiedNames());
        }
    }


    /**
     * Create solution blueprints
     */
    private void addSolutionBlueprints()
    {
        for (ProductSolutionComponent solutionBlueprint : ProductSolutionComponent.values())
        {
            archiveHelper.setGUID(solutionBlueprint.getQualifiedName(), solutionBlueprint.getGUID());

            String blueprintGUID = archiveHelper.addSolutionBlueprint(OpenMetadataType.SOLUTION_BLUEPRINT.typeName,
                                                                      solutionBlueprint.getQualifiedName(),
                                                                      solutionBlueprint.getDisplayName(),
                                                                      solutionBlueprint.getDescription(),
                                                                      solutionBlueprint.getVersionIdentifier(),
                                                                      null,
                                                                      null);
            assert (blueprintGUID.equals(solutionBlueprint.getGUID()));
        }
    }


    /**
     * Create solution components
     */
    private void addSolutionComponents()
    {
        for (ProductSolutionComponent productSolutionComponent : ProductSolutionComponent.values())
        {
            archiveHelper.setGUID(productSolutionComponent.getQualifiedName(), productSolutionComponent.getGUID());

            List<ProductSolutionBlueprint> consumingSolutionBlueprints     = productSolutionComponent.getConsumingBlueprints();
            List<String>            consumingSolutionBlueprintGUIDs = null;

            if (consumingSolutionBlueprints != null)
            {
                consumingSolutionBlueprintGUIDs = new ArrayList<>();

                for (ProductSolutionBlueprint solutionBlueprint : consumingSolutionBlueprints)
                {
                    if (solutionBlueprint != null)
                    {
                        consumingSolutionBlueprintGUIDs.add(solutionBlueprint.getGUID());
                    }
                }
            }

            String componentGUID = archiveHelper.addSolutionComponent(consumingSolutionBlueprintGUIDs,
                                                                      OpenMetadataType.SOLUTION_COMPONENT.typeName,
                                                                      productSolutionComponent.getQualifiedName(),
                                                                      productSolutionComponent.getDisplayName(),
                                                                      productSolutionComponent.getDescription(),
                                                                      productSolutionComponent.getVersionIdentifier(),
                                                                      productSolutionComponent.getComponentType(),
                                                                      productSolutionComponent.getImplementationType(),
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null);
            assert(componentGUID.equals(productSolutionComponent.getGUID()));

            if (productSolutionComponent.getSubComponents() != null)
            {
                for (ProductSolutionComponent subComponent : productSolutionComponent.getSubComponents())
                {
                    archiveHelper.addSolutionCompositionRelationship(componentGUID, subComponent.getGUID());
                }
            }

            if (productSolutionComponent.getImplementationResource() != null)
            {
                archiveHelper.addImplementationResourceRelationship(productSolutionComponent.getGUID(),
                                                                    productSolutionComponent.getImplementationResource(),
                                                                    "Standard implementation of the main process step");
            }
        }
    }


    private void addSolutionComponentActorDefinitions()
    {
        for (SolutionComponentActor solutionRoleDefinition : SolutionComponentActor.values())
        {
            archiveHelper.addSolutionComponentActorRelationship(solutionRoleDefinition.getSolutionRole().getGUID(),
                                                                solutionRoleDefinition.getSolutionComponent().getGUID(),
                                                                solutionRoleDefinition.getRole(),
                                                                solutionRoleDefinition.getDescription());
        }
    }


}
