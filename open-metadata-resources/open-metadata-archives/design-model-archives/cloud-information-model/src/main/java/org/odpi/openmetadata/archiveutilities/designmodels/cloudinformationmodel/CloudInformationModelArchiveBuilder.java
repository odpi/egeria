/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.designmodels.cloudinformationmodel;


import org.odpi.openmetadata.archiveutilities.designmodels.base.DesignModelArchiveBuilder;
import org.odpi.openmetadata.archiveutilities.designmodels.cloudinformationmodel.properties.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * CloudInformationModelArchiveBuilder creates an open metadata archive for the cloud information model.
 * It uses the parser to read the model content into Java Beans.  These java beans are then used by this
 * archive builder to create the open metadata archive.
 *
 * The archive builder needs to ensure it uses the same
 */
class CloudInformationModelArchiveBuilder extends DesignModelArchiveBuilder
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "9dc75637-92a7-4926-b47b-a3d407546f89";
    private static final String                  archiveRootName    = "CloudInformationModel";
    private static final String                  archiveName        = "Cloud Information Model (CIM) glossary and concept model";
    private static final String                  archiveLicense     = "Apache 2.0";
    private static final String                  archiveDescription = "Data types for commerce focused cloud applications.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  originatorName     = "The Cloud Information Model";
    private static final Date                    creationDate       = new Date(1570383385107L);



    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "1.0";


    private CloudInformationModelParser parser;


    /**
     * Default constructor sets up the archive builder.  This in turn sets up the header for the archive.
     */
    CloudInformationModelArchiveBuilder(CloudInformationModelParser parser)
    {
        super(archiveGUID,
              archiveName,
              archiveDescription,
              archiveType,
              archiveRootName,
              originatorName,
              archiveLicense,
              creationDate,
              versionNumber,
              versionName);

        this.parser = parser;
    }


    /**
     * Returns the open metadata type archive containing all of the elements extracted from the CIM.
     *
     * @return populated open metadata archive object
     */
    @Override
    protected OpenMetadataArchive getOpenMetadataArchive()
    {
        final String methodName = "getOpenMetadataArchive";

        if (parser != null)
        {
            Model model = parser.getModel();

            /*
             * Convert the metadata extracted by the parser into content for the open metadata archive.
             */
            String  glossaryId = super.addGlossary("Glossary-" + model.getModelTechnicalName(),
                                                   model.getModelName(),
                                                   model.getModelDescription(),
                                                   model.getModelLanguage(),
                                                   archiveDescription,
                                                   model.getModelLocation(),
                                                   model.getModelScope());

            /*
             * Create a top level category to hold all of the glossary terms for the model
             */
            String cimModelCategoryId = super.addCategory(glossaryId,
                                                          "ModelVocabulary-" + model.getModelTechnicalName(),
                                                          "Model Vocabulary: " + model.getModelName(),
                                                          model.getModelDescription(),
                                                          null);

            super.addMoreInformationLink(glossaryId, cimModelCategoryId);

            String modelTermId = super.addTerm(glossaryId,
                                               null,
                                               "GlossaryDescription-" + model.getModelTechnicalName(),
                                               model.getModelName(),
                                               model.getModelDescription());

            super.addMoreInformationLink(modelTermId, cimModelCategoryId);

            /*
             * Create a top level category to hold the property groups
             */
            String propertyGroupsCategoryId = super.addCategory(glossaryId,
                                                               "PropertyGroups-" + model.getModelTechnicalName(),
                                                               "Property Groups for the " + model.getModelName(),
                                                               "Collections of properties found in the CIM Model.",
                                                               null);

            super.addCategoryToCategory(cimModelCategoryId, propertyGroupsCategoryId);


            Map<String, PropertyGroup> propertyGroupMap = model.getPropertyGroupMap();

            if (propertyGroupMap != null)
            {
                for (PropertyGroup propertyGroup : propertyGroupMap.values())
                {
                    /*
                     * Each property group has a category under the main property groups category.
                     * Then each property within the property group is a term linked to its property group category.
                     */
                    String propertyGroupCategoryId = super.addCategory(glossaryId,
                                                                       propertyGroup.getId(),
                                                                       propertyGroup.getDisplayName(),
                                                                       propertyGroup.getDescription(),
                                                                       null);

                    super.addCategoryToCategory(propertyGroupsCategoryId, propertyGroupCategoryId);

                    List<String>  categoryList = new ArrayList<>();

                    categoryList.add(propertyGroupCategoryId);

                    List<PropertyDescription> propertyDescriptions = propertyGroup.getPropertyDescriptions();

                    if (propertyDescriptions != null)
                    {
                        for (PropertyDescription propertyDescription : propertyDescriptions)
                        {
                            if (propertyDescription != null)
                            {
                                String propertyTermId = super.addTerm(glossaryId,
                                                                      categoryList,
                                                                      propertyGroup.getId() + "-" + propertyDescription.getId(),
                                                                      propertyDescription.getDisplayName(),
                                                                      propertyDescription.getDescription());
                            }
                        }
                    }
                }
            }


            /*
             * Create a top level category to hold the subject areas
             */
            String subjectAreasCategoryId = super.addCategory(glossaryId,
                                                              "ModelSubjectAreas-" + model.getModelTechnicalName(),
                                                              "Subject Areas for the " + model.getModelName() + " model",
                                                              "Collections of related concepts (entities and relationships) found in the CIM Model that describe an area of interest.",
                                                              null);

            super.addCategoryToCategory(cimModelCategoryId, subjectAreasCategoryId);


            Map<String, SubjectArea> subjectAreaMap = model.getSubjectAreaMap();

            if (subjectAreaMap != null)
            {
                for (SubjectArea subjectArea : subjectAreaMap.values())
                {
                    String subjectAreaCategoryId = super.addCategory(glossaryId,
                                                                     subjectArea.getId(),
                                                                     subjectArea.getDisplayName(),
                                                                     subjectArea.getDescription(),
                                                                     subjectArea.getDisplayName());

                    super.addCategoryToCategory(subjectAreasCategoryId, subjectAreaCategoryId);

                    String subjectAreaTermId = super.addTerm(glossaryId,
                                                             null,
                                                             "ModelSubjectArea-" + model.getModelTechnicalName() + "-" + subjectArea.getId(),
                                                             subjectArea.getDisplayName(),
                                                             subjectArea.getDescription());

                    super.addMoreInformationLink(subjectAreaTermId, subjectAreaCategoryId);

                    Map<String, ConceptGroup> conceptGroupMap = subjectArea.getConceptGroups();

                    if (conceptGroupMap != null)
                    {
                        for (ConceptGroup conceptGroup : conceptGroupMap.values())
                        {
                            if (conceptGroup != null)
                            {
                                String conceptGroupCategoryId = super.addCategory(glossaryId,
                                                                                  conceptGroup.getId(),
                                                                                  conceptGroup.getDisplayName(),
                                                                                  conceptGroup.getDescription(),
                                                                                  conceptGroup.getDisplayName());

                                super.addCategoryToCategory(subjectAreaCategoryId, conceptGroupCategoryId);

                                String conceptGroupTermId = super.addTerm(glossaryId,
                                                                          null,
                                                                          "ModelConceptGroup-" + model.getModelTechnicalName() + "-" + subjectArea.getId() + "-" + conceptGroup.getId(),
                                                                          conceptGroup.getDisplayName(),
                                                                          conceptGroup.getDescription());

                                super.addMoreInformationLink(conceptGroupTermId, conceptGroupCategoryId);

                                List<String>  categoryList = new ArrayList<>();

                                categoryList.add(conceptGroupCategoryId);

                                List<Concept>  concepts = conceptGroup.getConcepts();

                                if (concepts != null)
                                {
                                    for (Concept concept : concepts)
                                    {
                                        if (concept != null)
                                        {
                                            String conceptTermId = super.addTerm(glossaryId,
                                                                                 categoryList,
                                                                                 subjectArea.getTechnicalName() + "::" + concept.getId(),
                                                                                 concept.getDisplayName(),
                                                                                 concept.getDescription());
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // todo export model content
                }
            }

            /*
             * Retrieve the assembled archive content.
             */
            return super.getOpenMetadataArchive();
        }
        else
        {
            super.logBadArchiveContent(methodName);
            return null;
        }
    }
}