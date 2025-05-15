/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel.properties.Concept;
import org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel.properties.ConceptGroup;
import org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel.properties.Model;
import org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel.properties.PropertyDescription;
import org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel.properties.PropertyGroup;
import org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel.properties.SubjectArea;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveWriter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.samples.archiveutilities.SimpleCatalogArchiveHelper;

import java.util.*;

/**
 * CloudInformationModelArchiveWriter creates a physical open metadata archive file for the data model and glossary
 * content found in the Cloud Information Model (CIM).
 */
public class CloudInformationModelArchiveWriter extends OMRSArchiveWriter
{
    static final String defaultOpenMetadataArchiveFileName = "content-packs/CloudInformationModel.omarchive";

    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "9dc75637-92a7-4926-b47b-a3d407546f89";
    private static final String                  archiveName        = "CloudInformationModel";
    private static final String                  archiveLicense     = "SPDX-License-Identifier: Apache-2.0";
    private static final String                  archiveDescription = "Cloud Information Model (CIM) glossary and concept model.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  originatorName     = "The Cloud Information Model";
    private static final Date                    creationDate       = new Date(1570383385107L);

    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "1.0";

    private final SimpleCatalogArchiveHelper archiveHelper;
    private final OMRSArchiveBuilder         archiveBuilder;

    private final CloudInformationModelParser parser;

    /**
     * Default constructor
     *
     * @param cimModelLocation directory name for the CIM model's JSON-LD files.
     */
    CloudInformationModelArchiveWriter(String cimModelLocation)
    {
        List<OpenMetadataArchive> dependentOpenMetadataArchives = new ArrayList<>();

        /*
         * This value allows the CIM to be based on the existing open metadata types
         */
        dependentOpenMetadataArchives.add(new OpenMetadataTypesArchive().getOpenMetadataArchive());

        this.archiveBuilder = new OMRSArchiveBuilder(archiveGUID,
                                                     archiveName,
                                                     archiveDescription,
                                                     archiveType,
                                                     originatorName,
                                                     archiveLicense,
                                                     creationDate,
                                                     dependentOpenMetadataArchives);

        this.archiveHelper = new SimpleCatalogArchiveHelper(archiveBuilder,
                                                            archiveGUID,
                                                            archiveName,
                                                            originatorName,
                                                            creationDate,
                                                            versionNumber,
                                                            versionName,
                                                            archiveName + "GUIDMap.json");

        this.parser = new CloudInformationModelParser(cimModelLocation);
    }


    /**
     * Returns the open metadata type archive containing all the elements extracted from the CIM.
     *
     * @return populated open metadata archive object
     */
    private OpenMetadataArchive getOpenMetadataArchive()
    {
        final String methodName = "getOpenMetadataArchive";

        if (parser != null)
        {
            Model model = parser.getModel();

            /*
             * Convert the metadata extracted by the parser into content for the open metadata archive.
             */
            String  modelId = archiveHelper.addDesignModel(null,
                                                           "ConceptModel",
                                                           "DesignModel::" + model.getModelTechnicalName(),
                                                           "Concept Model::" + model.getModelName(),
                                                           model.getModelName(),
                                                           model.getModelSummary(),
                                                           null,
                                                           null,
                                                           null,
                                                           null);

            String  dataDictionaryId = archiveHelper.addCollection(null,
                                                                   null,
                                                                   OpenMetadataType.COLLECTION.typeName,
                                                                   OpenMetadataType.COLLECTION.typeName,
                                                                   null,
                                                                   OpenMetadataType.DATA_DICTIONARY_COLLECTION.typeName,
                                                                   "DataDictionary::" + model.getModelName(),
                                                                   model.getModelName(),
                                                                   model.getModelSummary(),
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   null);

            archiveHelper.addMoreInformationLink(dataDictionaryId, modelId);

            String  glossaryId = archiveHelper.addGlossary("Glossary::" + model.getModelTechnicalName(),
                                                           model.getModelName(),
                                                           model.getModelSummary(),
                                                           model.getModelLanguage(),
                                                           archiveDescription,
                                                           model.getModelLocation(),
                                                           model.getModelScope());

            archiveHelper.addMoreInformationLink(dataDictionaryId, glossaryId);
            archiveHelper.addMoreInformationLink(modelId, glossaryId);

            /*
             * Create a top level category to hold all the glossary terms for the model
             */
            String cimModelCategoryId = archiveHelper.addGlossaryCategory(glossaryId,
                                                                          true,
                                                                          "GlossaryCategory::ModelVocabulary:" + model.getModelTechnicalName(),
                                                                          "Model Vocabulary: " + model.getModelName(),
                                                                          model.getModelSummary(),
                                                                          null,
                                                                          null);

            String modelTermId = archiveHelper.addTerm(glossaryId,
                                                       Collections.singletonList(cimModelCategoryId),
                                                       false,
                                                       "GlossaryTerm::CIMDescription-" + model.getModelTechnicalName(),
                                                       model.getModelName(),
                                                       model.getModelSummary(),
                                                       model.getModelDescription(),
                                                       null,
                                                       null,
                                                       model.getModelUsage(),
                                                       false,
                                                       false,
                                                       false,
                                                       null,
                                                       null,
                                                       null,
                                                       null);

            archiveHelper.addMoreInformationLink(modelTermId, cimModelCategoryId);

            /*
             * Create a top level category to hold the property groups
             */
            String propertyGroupsCategoryId = archiveHelper.addGlossaryCategory(glossaryId,
                                                                                "GlossaryCategory::PropertyGroups-" + model.getModelTechnicalName(),
                                                                                "Property Groups for the " + model.getModelName(),
                                                                                "Collections of properties found in the CIM Model.",
                                                                                null);

            archiveHelper.addCategoryToCategory(cimModelCategoryId, propertyGroupsCategoryId);

            Map<String, PropertyGroup> propertyGroupMap = model.getPropertyGroupMap();

            if (propertyGroupMap != null)
            {
                for (PropertyGroup propertyGroup : propertyGroupMap.values())
                {
                    /*
                     * Each property group has a category under the main property groups category.
                     * Then each property within the property group is a term linked to its property group category.
                     */
                    String propertyGroupCategoryId = archiveHelper.addGlossaryCategory(glossaryId,
                                                                                       "GlossaryCategory::" + propertyGroup.getGUID() + "::" + propertyGroup.getTechnicalName(),
                                                                                       propertyGroup.getDisplayName(),
                                                                                       propertyGroup.getDescription(),
                                                                                       null);

                    archiveHelper.addCategoryToCategory(propertyGroupsCategoryId, propertyGroupCategoryId);

                    String propertyGroupFolderId = archiveHelper.addCollection(null,
                                                                               dataDictionaryId,
                                                                               OpenMetadataType.COLLECTION.typeName,
                                                                               OpenMetadataType.COLLECTION.typeName,
                                                                               dataDictionaryId,
                                                                               OpenMetadataType.FOLDER.typeName,
                                                                               "Folder::" + propertyGroup.getGUID() + "::" + propertyGroup.getTechnicalName(),
                                                                               propertyGroup.getDisplayName(),
                                                                               propertyGroup.getDescription(),
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               null);

                    archiveHelper.addMemberToCollection(dataDictionaryId, propertyGroupFolderId, null);

                    List<String>  categoryList = new ArrayList<>();

                    categoryList.add(propertyGroupCategoryId);

                    List<PropertyDescription> propertyDescriptions = propertyGroup.getPropertyDescriptions();

                    if (propertyDescriptions != null)
                    {
                        for (PropertyDescription propertyDescription : propertyDescriptions)
                        {
                            if (propertyDescription != null)
                            {
                                String propertyTermId = archiveHelper.addTerm(glossaryId,
                                                                              categoryList,
                                                                              propertyGroup.getGUID() + propertyGroup.getTechnicalName() + "::" + propertyDescription.getTechnicalName(),
                                                                              propertyDescription.getDisplayName(),
                                                                              propertyDescription.getDescription());

                                String dataFieldId = archiveHelper.addDataField(null,
                                                                                dataDictionaryId,
                                                                                OpenMetadataType.COLLECTION.typeName,
                                                                                OpenMetadataType.COLLECTION.typeName,
                                                                                dataDictionaryId,
                                                                                null,
                                                                                "DataField::" + propertyGroup.getGUID() + "::" + propertyGroup.getTechnicalName() + "::" + propertyDescription.getTechnicalName(),
                                                                                propertyDescription.getDisplayName(),
                                                                                propertyDescription.getDescription(),
                                                                                propertyDescription.getVersion(),
                                                                                null,
                                                                                propertyDescription.getDataTypeId(),
                                                                                null,
                                                                                null,
                                                                                null);

                                archiveHelper.addSemanticDefinition(dataFieldId, propertyTermId);

                                archiveHelper.addMemberToCollection(propertyGroupFolderId, dataFieldId, null);
                            }
                        }
                    }
                }
            }

            /*
             * Create a top level category to hold the subject areas
             */
            String subjectAreasCategoryId = archiveHelper.addGlossaryCategory(glossaryId,
                                                                      "GlossaryCategory::ModelSubjectAreas-" + model.getModelTechnicalName(),
                                                                      "Subject Areas for the " + model.getModelName() + " model",
                                                                      "Collections of related concepts (entities and relationships) found in the CIM Model that describe an area of interest.",
                                                                      null);

            archiveHelper.addCategoryToCategory(cimModelCategoryId, subjectAreasCategoryId);


            Map<String, SubjectArea> subjectAreaMap = model.getSubjectAreaMap();

            if (subjectAreaMap != null)
            {
                for (SubjectArea subjectArea : subjectAreaMap.values())
                {
                    String subjectAreaCategoryId = archiveHelper.addGlossaryCategory(glossaryId,
                                                                             "GlossaryCategory::SubjectArea::" + subjectArea.getTechnicalName(),
                                                                             subjectArea.getDisplayName(),
                                                                             subjectArea.getDescription(),
                                                                             subjectArea.getDisplayName());

                    archiveHelper.addCategoryToCategory(subjectAreasCategoryId, subjectAreaCategoryId);

                    String subjectAreaTermId = archiveHelper.addTerm(glossaryId,
                                                                     null,
                                                                     "GlossaryTerm::ModelSubjectArea::" + model.getModelTechnicalName() + "::" + subjectArea.getTechnicalName(),
                                                                     subjectArea.getDisplayName(),
                                                                     subjectArea.getDescription());

                    archiveHelper.addMoreInformationLink(subjectAreaTermId, subjectAreaCategoryId);

                    Map<String, ConceptGroup> conceptGroupMap = subjectArea.getConceptGroups();

                    if (conceptGroupMap != null)
                    {
                        for (ConceptGroup conceptGroup : conceptGroupMap.values())
                        {
                            if (conceptGroup != null)
                            {
                                String conceptGroupCategoryId = archiveHelper.addGlossaryCategory(glossaryId,
                                                                                                  "GlossaryCategory::ConceptGroup::" + conceptGroup.getGUID() + "::" + conceptGroup.getTechnicalName(),
                                                                                                  conceptGroup.getDisplayName(),
                                                                                                  conceptGroup.getDescription(),
                                                                                                  conceptGroup.getDisplayName());

                                archiveHelper.addCategoryToCategory(subjectAreaCategoryId, conceptGroupCategoryId);

                                String conceptGroupTermId = archiveHelper.addTerm(glossaryId,
                                                                                  null,
                                                                                  "ModelConceptGroup::" + model.getModelTechnicalName() + "::" + subjectArea.getTechnicalName() + "::" + conceptGroup.getGUID() + "::" + conceptGroup.getTechnicalName(),
                                                                                  conceptGroup.getDisplayName(),
                                                                                  conceptGroup.getDescription());

                                archiveHelper.addMoreInformationLink(conceptGroupTermId, conceptGroupCategoryId);

                                List<String>  categoryList = new ArrayList<>();

                                categoryList.add(conceptGroupCategoryId);

                                List<Concept> concepts = conceptGroup.getConcepts();

                                if (concepts != null)
                                {
                                    for (Concept concept : concepts)
                                    {
                                        if (concept != null)
                                        {
                                            String conceptTermId = archiveHelper.addTerm(glossaryId,
                                                                                         categoryList,
                                                                                         "SubjectArea::" + subjectArea.getTechnicalName() + "::" + conceptGroup.getGUID() + "::" + concept.getTechnicalName(),
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
            archiveHelper.saveGUIDs();
            return archiveBuilder.getOpenMetadataArchive();
        }
        else
        {
            archiveBuilder.logBadArchiveContent(methodName);
            return null;
        }
    }


    /**
     * Generates and writes out an open metadata archive containing all the elements extracted from the CIM.
     */
    void writeOpenMetadataArchive()
    {
        try
        {
            super.writeOpenMetadataArchive(defaultOpenMetadataArchiveFileName, this.getOpenMetadataArchive());
        }
        catch (Exception error)
        {
            System.out.println("error is " + error);
        }
    }


    /**
     * Main program to initiate the archive writer for the Cloud Information Model (CIM).
     *
     * @param args list of arguments - first one should be the directory where the model
     *             content is located.  Any other arguments passed are ignored.
     */
    public static void main(String[] args)
    {
        String fileName = "cloud-information-model.jsonld";

        if (args.length > 0)
        {
            fileName = args[0];
        }

        try
        {
            CloudInformationModelArchiveWriter archiveWriter = new CloudInformationModelArchiveWriter(fileName);

            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}
