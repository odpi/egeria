/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.clinicaltrialtemplates;


import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreProvider;
import org.odpi.openmetadata.archiveutilities.openconnectors.core.CorePackArchiveWriter;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.FileType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.samples.archiveutilities.EgeriaBaseArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.governanceprogram.CocoGovernanceProgramArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.governanceprogram.CocoGovernanceZoneDefinition;
import org.odpi.openmetadata.samples.archiveutilities.governanceprogram.DataProcessingPurposeDefinition;
import org.odpi.openmetadata.samples.archiveutilities.governanceprogram.LicenseTypeDefinition;
import org.odpi.openmetadata.samples.archiveutilities.organization.ScopeDefinition;
import org.odpi.openmetadata.samples.governanceactions.clinicaltrials.CocoClinicalTrialPlaceholderProperty;

import java.util.*;


/**
 * CocoClinicalTrialsArchiveWriter creates a physical open metadata archive file containing the clinical trials templates
 * needed by Coco Pharmaceuticals.
 */
public class CocoClinicalTrialsArchiveWriter extends EgeriaBaseArchiveWriter
{
    private static final String archiveFileName = "CocoClinicalTrialsTemplatesArchive.omarchive";

    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "74a786b2-d6d7-401d-b8c1-7d798f752c55";
    private static final String                  archiveName        = "Coco Pharmaceuticals Clinical Trials Templates";
    private static final String                  archiveDescription = "Templates for new assets relating to a clinical trial.";
    private static final Date                    creationDate       = new Date(1639984840038L);

    private static final String clinicalTrialsSubjectArea = "SubjectArea::ClinicalTrial:TeddyBearDropFoot";

    /**
     * Default constructor initializes the archive.
     */
    public CocoClinicalTrialsArchiveWriter()
    {
        super(archiveGUID,
              archiveName,
              archiveDescription,
              creationDate,
              archiveFileName,
              new OpenMetadataArchive[]{ new CorePackArchiveWriter().getOpenMetadataArchive(),
                                         new CocoGovernanceProgramArchiveWriter().getOpenMetadataArchive()});
    }


    /**
     * Add the content to the archive builder.
     */
    @Override
    public void getArchiveContent()
    {
        writeGlossary();
        writeLandingAreaWeeklyMeasurementsTemplate();
        writeDataLakeWeeklyMeasurementsTemplate();
    }


    /**
     * Create glossary
     */
    private void writeGlossary()
    {
        final String methodName = "writeGlossary";

        String glossaryGUID = archiveHelper.addGlossary("Glossary::Coco::ClinicalTrialTerminology",
                                                        "Coco Pharmaceuticals Clinical Trial Terminology",
                                                        "This glossary describes terminology invented for the fictitious Coco Pharmaceuticals case study.  Used in scenarios that show techniques for the collection and processing of data associated with a clinical trial.",
                                                        "English",
                                                        "Used for demonstration of governance and data management techniques using the Egeria technology.  Not to be used for real clinical trials.",
                                                        null,
                                                        ScopeDefinition.ALL_COCO.getPreferredValue());

        archiveHelper.addSubjectAreaClassification(glossaryGUID, clinicalTrialsSubjectArea);

        Map<String, String> categoryLookup = new HashMap<>();
        for (GlossaryCategoryDefinition glossaryCategoryDefinition : GlossaryCategoryDefinition.values())
        {
            String glossaryCategoryGUID = archiveHelper.addGlossaryCategory(glossaryGUID,
                                                                            glossaryCategoryDefinition.getQualifiedName(),
                                                                            glossaryCategoryDefinition.getName(),
                                                                            glossaryCategoryDefinition.getDescription(),
                                                                            null);

            categoryLookup.put(glossaryCategoryDefinition.getName(), glossaryCategoryGUID);
        }

        for (GlossaryTermDefinition glossaryTermDefinition : GlossaryTermDefinition.values())
        {
            String glossaryTermGUID = archiveHelper.addTerm(glossaryGUID,
                                                            null,
                                                            false,
                                                            glossaryTermDefinition.getQualifiedName(),
                                                            glossaryTermDefinition.getName(),
                                                            glossaryTermDefinition.getSummary(),
                                                            glossaryTermDefinition.getDescription(),
                                                            null,
                                                            glossaryTermDefinition.getAbbreviation(),
                                                            glossaryTermDefinition.getUsage(),
                                                            false,
                                                            false,
                                                            false,
                                                            null,
                                                            null,
                                                            null,
                                                            null);

            List<Classification> classificationList = new ArrayList<>();

            classificationList.add(archiveHelper.getTemplateSubstituteClassification());

            String substituteGlossaryTermGUID = archiveHelper.addTerm(glossaryGUID,
                                                                      null,
                                                                      false,
                                                                      glossaryTermDefinition.getTemplateSubstituteQualifiedName(),
                                                                      glossaryTermDefinition.getTemplateSubstituteName(),
                                                                      glossaryTermDefinition.getTemplateSubstituteSummary(),
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      glossaryTermDefinition.getTemplateSubstituteUsage(),
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      classificationList);


            archiveHelper.addSourcedFromRelationship(substituteGlossaryTermGUID,
                                                     1,
                                                     glossaryTermGUID,
                                                     methodName);

            if (glossaryTermDefinition.getCategory() != null)
            {
                archiveHelper.addTermToCategory(categoryLookup.get(glossaryTermDefinition.getCategory().getName()),
                                                glossaryTermGUID);
            }

            archiveHelper.addTermToCategory(categoryLookup.get(glossaryTermDefinition.getSubstituteCategory().getName()),
                                            substituteGlossaryTermGUID);
        }
    }


    /**
     * Return the GUID of the template.
     */
    private void writeLandingAreaWeeklyMeasurementsTemplate()
    {
        final String methodName = "writeLandingAreaWeeklyMeasurementsTemplate";
        Map<String, Object>  extendedProperties = new HashMap<>();

        extendedProperties.put(OpenMetadataProperty.PATH_NAME.name, PlaceholderProperty.FILE_PATH_NAME.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_NAME.name, PlaceholderProperty.FILE_NAME.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, FileType.CSV_FILE.getDeployedImplementationType().getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.FILE_TYPE.name, FileType.CSV_FILE.getFileTypeName());
        extendedProperties.put(OpenMetadataProperty.FILE_EXTENSION.name, "csv");
        extendedProperties.put(OpenMetadataProperty.DELIMITER_CHARACTER.name, ",");
        extendedProperties.put(OpenMetadataProperty.QUOTE_CHARACTER.name, "\"");

        List<Classification> classifications = new ArrayList<>();

        List<String> zones = new ArrayList<>();
        zones.add(CocoGovernanceZoneDefinition.LANDING_AREA.getZoneName());
        zones.add(CocoGovernanceZoneDefinition.QUARANTINE.getZoneName());
        zones.add(CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_ID.getPlaceholder());
        classifications.add(archiveHelper.getAssetZoneMembershipClassification(zones));

        Map<String, String> otherOriginValues = new HashMap<>();
        otherOriginValues.put("contact", CocoClinicalTrialPlaceholderProperty.CONTACT_NAME.getPlaceholder());
        otherOriginValues.put("dept", CocoClinicalTrialPlaceholderProperty.CONTACT_EMAIL.getPlaceholder());

        classifications.add(archiveHelper.getAssetOriginClassification(CocoClinicalTrialPlaceholderProperty.HOSPITAL_NAME.getPlaceholder(),
                                                                       OpenMetadataProperty.NAME.name,
                                                                       null,
                                                                       null,
                                                                       otherOriginValues));

        classifications.add(archiveHelper.getConfidentialityClassification(3,
                                                                           100,
                                                                           "tanyatidie",
                                                                           OpenMetadataType.USER_IDENTITY.typeName,
                                                                           OpenMetadataProperty.USER_ID.name,
                                                                           "Clinical Trial Board",
                                                                           "Level approved assuming the data remains anonymized.",
                                                                           2));

        classifications.add(archiveHelper.getTemplateClassification("Landing Area weekly teddy bear measurements for drop foot clinical trial",
                                                                    "This template supports the cataloguing of weekly measurement files. " +
                                                                            "Use it to catalog the files as they come into the landing area.",
                                                                    "2.4",
                                                                    null,
                                                                    methodName));

        String qualifiedName = "LandingArea::" + CocoClinicalTrialPlaceholderProperty.HOSPITAL_NAME.getPlaceholder() + "::" + CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_ID.getPlaceholder() + "::CSVFile::" + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder();

        String assetGUID = archiveHelper.addAsset(FileType.CSV_FILE.getAssetSubTypeName(),
                                                  qualifiedName,
                                                  CocoClinicalTrialPlaceholderProperty.HOSPITAL_NAME.getPlaceholder() + " teddy bear measurements received on "+ PlaceholderProperty.RECEIVED_DATE.getPlaceholder() +" for " + CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_NAME.getPlaceholder(),
                                                  "V1.0",
                                                  "Dated measurements of patient's progression presented in a tabular format with columns of PatientId, Date, AngleLeft and AngleRight.",
                                                  this.getAssetAdditionalProperties(),
                                                  extendedProperties,
                                                  classifications);

        String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                        FileType.CSV_FILE.getAssetSubTypeName(),
                                                        OpenMetadataType.ASSET.typeName,
                                                        null,
                                                        qualifiedName + "_endpoint",
                                                        null,
                                                        null,
                                                        PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                                                        null,
                                                        null);

        ConnectorProvider connectorProvider = new CSVFileStoreProvider();
        String            connectorTypeGUID = connectorProvider.getConnectorType().getGUID();

        String connectionGUID = archiveHelper.addConnection(qualifiedName + "_connection",
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            connectorTypeGUID,
                                                            endpointGUID,
                                                            assetGUID,
                                                            FileType.CSV_FILE.getAssetSubTypeName(),
                                                            OpenMetadataType.ASSET.typeName,
                                                            null);

        archiveHelper.addConnectionForAsset(assetGUID, connectionGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               FileType.CSV_FILE.getAssetSubTypeName(),
                                               assetGUID,
                                               FileType.CSV_FILE.getAssetSubTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               null,
                                               CocoClinicalTrialPlaceholderProperty.getPlaceholderPropertyTypes());

        String licenseTypeGUID = archiveHelper.getGUID(LicenseTypeDefinition.CLINICAL_TRIAL_LICENSE.getQualifiedName());

        Map<String, String> entitlements = new HashMap<>();
        entitlements.put("research", "true");
        entitlements.put("marketing", "false");
        Map<String, String> restrictions = new HashMap<>();
        restrictions.put("copying", "in-house-only");
        Map<String, String> obligations = new HashMap<>();
        obligations.put("retention", "20 years");
        archiveHelper.addLicense(assetGUID,
                                 "ClinicalTrial:" + CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_ID.getPlaceholder() + ":WeeklyMeasurement:" + CocoClinicalTrialPlaceholderProperty.HOSPITAL_NAME.getPlaceholder(),
                                 null,
                                 null,
                                 null,
                                 CocoClinicalTrialPlaceholderProperty.HOSPITAL_NAME.getPlaceholder(),
                                 "Organization",
                                 "name",
                                 "tanyatidie",
                                 "UserIdentity",
                                 "userId",
                                 "tessatube",
                                 "UserIdentity",
                                 "userId",
                                 entitlements,
                                 restrictions,
                                 obligations,
                                 null,
                                 licenseTypeGUID);

        String dataProcessingPurposeGUID = archiveHelper.getGUID(DataProcessingPurposeDefinition.CLINICAL_TRIAL_VALIDATION.getQualifiedName());

        archiveHelper.addApprovedPurpose(assetGUID, dataProcessingPurposeGUID);

        String topLevelSchemaTypeGUID = archiveHelper.addTopLevelSchemaType(assetGUID,
                                                                            FileType.CSV_FILE.getAssetSubTypeName(),
                                                                            OpenMetadataType.TABULAR_SCHEMA_TYPE.typeName,
                                                                            qualifiedName + "_schemaType",
                                                                            null,
                                                                            null,
                                                                            null);

        String schemaAttributeGUID = archiveHelper.addSchemaAttribute(assetGUID,
                                                                      FileType.CSV_FILE.getAssetSubTypeName(),
                                                                      OpenMetadataType.TABULAR_COLUMN.typeName,
                                                                      OpenMetadataType.PRIMITIVE_SCHEMA_TYPE.typeName,
                                                                      qualifiedName + "_patientId_column",
                                                                      "PatientId",
                                                                      null,
                                                                      "string",
                                                                      10,
                                                                      null,
                                                                      null);

        archiveHelper.addAttributeForSchemaType(topLevelSchemaTypeGUID, 1, 1, 1, schemaAttributeGUID);

        String glossaryTermGUID = archiveHelper.getGUID(GlossaryTermDefinition.PATIENT_IDENTIFIER.getTemplateSubstituteQualifiedName());
        archiveHelper.addSemanticAssignment(schemaAttributeGUID, glossaryTermGUID);

        schemaAttributeGUID = archiveHelper.addSchemaAttribute(assetGUID,
                                                               FileType.CSV_FILE.getAssetSubTypeName(),
                                                               OpenMetadataType.TABULAR_COLUMN.typeName,
                                                               OpenMetadataType.PRIMITIVE_SCHEMA_TYPE.typeName,
                                                               qualifiedName + "_date_column",
                                                               "Date",
                                                               null,
                                                               "date",
                                                               10,
                                                               "YYYY-MM-DD",
                                                               null);

        archiveHelper.addAttributeForSchemaType(topLevelSchemaTypeGUID, 2, 1,1, schemaAttributeGUID);

        glossaryTermGUID = archiveHelper.getGUID(GlossaryTermDefinition.MEASUREMENT_DATE.getTemplateSubstituteQualifiedName());
        archiveHelper.addSemanticAssignment(schemaAttributeGUID, glossaryTermGUID);

        schemaAttributeGUID = archiveHelper.addSchemaAttribute(assetGUID,
                                                               FileType.CSV_FILE.getAssetSubTypeName(),
                                                               OpenMetadataType.TABULAR_COLUMN.typeName,
                                                               OpenMetadataType.PRIMITIVE_SCHEMA_TYPE.typeName,
                                                               qualifiedName + "_angleLeft_column",
                                                               "AngleLeft",
                                                               null,
                                                               "integer",
                                                               10,
                                                               null,
                                                               null);

        archiveHelper.addAttributeForSchemaType(topLevelSchemaTypeGUID, 3, 1, 1, schemaAttributeGUID);

        glossaryTermGUID = archiveHelper.getGUID(GlossaryTermDefinition.ANGLE_LEFT.getTemplateSubstituteQualifiedName());
        archiveHelper.addSemanticAssignment(schemaAttributeGUID, glossaryTermGUID);

        schemaAttributeGUID = archiveHelper.addSchemaAttribute(assetGUID,
                                                               FileType.CSV_FILE.getAssetSubTypeName(),
                                                               OpenMetadataType.TABULAR_COLUMN.typeName,
                                                               OpenMetadataType.PRIMITIVE_SCHEMA_TYPE.typeName,
                                                               qualifiedName + "_angleRight_column",
                                                               "AngleRight",
                                                               null,
                                                               "integer",
                                                               10,
                                                               null,
                                                               null);

        archiveHelper.addAttributeForSchemaType(topLevelSchemaTypeGUID, 4, 1,1, schemaAttributeGUID);

        glossaryTermGUID = archiveHelper.getGUID(GlossaryTermDefinition.ANGLE_RIGHT.getTemplateSubstituteQualifiedName());
        archiveHelper.addSemanticAssignment(schemaAttributeGUID, glossaryTermGUID);
    }


    /**
     * Use the additional properties to save the placeholder properties used in the template.
     */
    private Map<String, String> getAssetAdditionalProperties()
    {

        Map<String, String> additionalProperties = new HashMap<>();

        for (CocoClinicalTrialPlaceholderProperty placeholderProperty : CocoClinicalTrialPlaceholderProperty.values())
        {
            additionalProperties.put(placeholderProperty.getName(), placeholderProperty.getPlaceholder());
        }

        return additionalProperties;
    }


    /**
     * Create template
     */
    private void writeDataLakeWeeklyMeasurementsTemplate()
    {
        final String methodName = "writeDataLakeWeeklyMeasurementsTemplate";
        Map<String, Object>  extendedProperties = new HashMap<>();

        extendedProperties.put(OpenMetadataProperty.PATH_NAME.name, PlaceholderProperty.FILE_PATH_NAME.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_NAME.name, PlaceholderProperty.FILE_NAME.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, FileType.CSV_FILE.getDeployedImplementationType().getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.FILE_TYPE.name, FileType.CSV_FILE.getFileTypeName());
        extendedProperties.put(OpenMetadataProperty.FILE_EXTENSION.name, "csv");
        extendedProperties.put(OpenMetadataProperty.DELIMITER_CHARACTER.name, ",");
        extendedProperties.put(OpenMetadataProperty.QUOTE_CHARACTER.name, "\"");

        List<Classification> classifications = new ArrayList<>();

        List<String> zones = new ArrayList<>();
        zones.add(CocoGovernanceZoneDefinition.QUARANTINE.getZoneName());
        zones.add(CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_ID.getPlaceholder());
        classifications.add(archiveHelper.getAssetZoneMembershipClassification(zones));

        classifications.add(archiveHelper.getTemplateClassification("Data Lake weekly teddy bear measurements for drop foot clinical trial",
                                                                    "This template supports the cataloguing of weekly measurement files. " +
                                                                            "Use it to catalog the files as they come into the data lake.",
                                                                    "2.4",
                                                                    null,
                                                                    methodName));

        String qualifiedName = "DataLake::" + CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_ID.getPlaceholder() + "::CSVFile::" + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder();

        String assetGUID = archiveHelper.addAsset(FileType.CSV_FILE.getAssetSubTypeName(),
                                                  qualifiedName,
                                                  CocoClinicalTrialPlaceholderProperty.HOSPITAL_NAME.getPlaceholder() + " teddy bear measurements received on " + PlaceholderProperty.RECEIVED_DATE.getPlaceholder() +" for "+ CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_NAME.getPlaceholder(),
                                                  "V1.0",
                                                  "Dated measurements of patient's progression presented in a tabular format with columns of PatientId, Date, AngleLeft and AngleRight.",
                                                  this.getAssetAdditionalProperties(),
                                                  extendedProperties,
                                                  classifications);

        String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                        FileType.CSV_FILE.getAssetSubTypeName(),
                                                        OpenMetadataType.ASSET.typeName,
                                                        null,
                                                        qualifiedName + "_endpoint",
                                                        null,
                                                        null,
                                                        PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                                                        null,
                                                        null);

        ConnectorProvider connectorProvider = new CSVFileStoreProvider();
        String            connectorTypeGUID = connectorProvider.getConnectorType().getGUID();

        String connectionGUID = archiveHelper.addConnection(qualifiedName + "_connection",
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            connectorTypeGUID,
                                                            endpointGUID,
                                                            assetGUID,
                                                            FileType.CSV_FILE.getAssetSubTypeName(),
                                                            OpenMetadataType.ASSET.typeName,
                                                            null);

        archiveHelper.addConnectionForAsset(assetGUID, connectionGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               FileType.CSV_FILE.getAssetSubTypeName(),
                                               assetGUID,
                                               FileType.CSV_FILE.getAssetSubTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               null,
                                               CocoClinicalTrialPlaceholderProperty.getPlaceholderPropertyTypes());

        String licenseTypeGUID = archiveHelper.getGUID(LicenseTypeDefinition.CLINICAL_TRIAL_LICENSE.getQualifiedName());

        Map<String, String> entitlements = new HashMap<>();
        entitlements.put("research", "true");
        entitlements.put("marketing", "false");
        Map<String, String> restrictions = new HashMap<>();
        restrictions.put("copying", "in-house-only");
        Map<String, String> obligations = new HashMap<>();
        obligations.put("retention", "20 years");
        archiveHelper.addLicense(assetGUID,
                                 "ClinicalTrial:" + CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_ID.getPlaceholder() + ":WeeklyMeasurement:" + CocoClinicalTrialPlaceholderProperty.HOSPITAL_NAME.getPlaceholder(),
                                 null,
                                 null,
                                 null,
                                 CocoClinicalTrialPlaceholderProperty.HOSPITAL_NAME.getPlaceholder(),
                                 "Organization",
                                 "name",
                                 "tanyatidie",
                                 "UserIdentity",
                                 "userId",
                                 "tessatube",
                                 "UserIdentity",
                                 "userId",
                                 entitlements,
                                 restrictions,
                                 obligations,
                                 null,
                                 licenseTypeGUID);

        String dataProcessingPurposeGUID = archiveHelper.getGUID(DataProcessingPurposeDefinition.CLINICAL_TRIAL_VALIDATION.getQualifiedName());

        archiveHelper.addApprovedPurpose(assetGUID, dataProcessingPurposeGUID);

        String topLevelSchemaTypeGUID = archiveHelper.addTopLevelSchemaType(assetGUID,
                                                                            FileType.CSV_FILE.getAssetSubTypeName(),
                                                                            OpenMetadataType.TABULAR_SCHEMA_TYPE.typeName,
                                                                            qualifiedName + "_schemaType",
                                                                            null,
                                                                            null,
                                                                            null);

        String schemaAttributeGUID = archiveHelper.addSchemaAttribute(assetGUID,
                                                                      FileType.CSV_FILE.getAssetSubTypeName(),
                                                                      OpenMetadataType.TABULAR_COLUMN.typeName,
                                                                      OpenMetadataType.PRIMITIVE_SCHEMA_TYPE.typeName,
                                                                      qualifiedName + "_patientId_column",
                                                                      "PatientId",
                                                                      null,
                                                                      "string",
                                                                      0,
                                                                      null,
                                                                      null);

        archiveHelper.addAttributeForSchemaType(topLevelSchemaTypeGUID, 1, 1, 1, schemaAttributeGUID);

        String glossaryTermGUID = archiveHelper.getGUID(GlossaryTermDefinition.PATIENT_IDENTIFIER.getTemplateSubstituteQualifiedName());
        archiveHelper.addSemanticAssignment(schemaAttributeGUID, glossaryTermGUID);

        schemaAttributeGUID = archiveHelper.addSchemaAttribute(assetGUID,
                                                               FileType.CSV_FILE.getAssetSubTypeName(),
                                                               OpenMetadataType.TABULAR_COLUMN.typeName,
                                                               OpenMetadataType.PRIMITIVE_SCHEMA_TYPE.typeName,
                                                               qualifiedName + "_date_column",
                                                               "Date",
                                                               null,
                                                               "date",
                                                               0,
                                                               "YYYY-MM-DD",
                                                               null);

        archiveHelper.addAttributeForSchemaType(topLevelSchemaTypeGUID, 2, 1, 1, schemaAttributeGUID);

        glossaryTermGUID = archiveHelper.getGUID(GlossaryTermDefinition.MEASUREMENT_DATE.getTemplateSubstituteQualifiedName());
        archiveHelper.addSemanticAssignment(schemaAttributeGUID, glossaryTermGUID);

        schemaAttributeGUID = archiveHelper.addSchemaAttribute(assetGUID,
                                                               FileType.CSV_FILE.getAssetSubTypeName(),
                                                               OpenMetadataType.TABULAR_COLUMN.typeName,
                                                               OpenMetadataType.PRIMITIVE_SCHEMA_TYPE.typeName,
                                                               qualifiedName + "_angleLeft_column",
                                                               "AngleLeft",
                                                               null,
                                                               "integer",
                                                               0,
                                                               null,
                                                               null);

        archiveHelper.addAttributeForSchemaType(topLevelSchemaTypeGUID, 3, 1, 1, schemaAttributeGUID);

        glossaryTermGUID = archiveHelper.getGUID(GlossaryTermDefinition.ANGLE_LEFT.getTemplateSubstituteQualifiedName());
        archiveHelper.addSemanticAssignment(schemaAttributeGUID, glossaryTermGUID);

        schemaAttributeGUID = archiveHelper.addSchemaAttribute(assetGUID,
                                                               FileType.CSV_FILE.getAssetSubTypeName(),
                                                               OpenMetadataType.TABULAR_COLUMN.typeName,
                                                               OpenMetadataType.PRIMITIVE_SCHEMA_TYPE.typeName,
                                                               qualifiedName + "_angleRight_column",
                                                               "AngleRight",
                                                               null,
                                                               "integer",
                                                               0,
                                                               null,
                                                               null);

        archiveHelper.addAttributeForSchemaType(topLevelSchemaTypeGUID, 4, 1 ,1, schemaAttributeGUID);

        glossaryTermGUID = archiveHelper.getGUID(GlossaryTermDefinition.ANGLE_RIGHT.getTemplateSubstituteQualifiedName());
        archiveHelper.addSemanticAssignment(schemaAttributeGUID, glossaryTermGUID);
    }
}
