/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.fvt.opentypes.generators;

import org.odpi.openmetadata.fvt.opentypes.model.OmrsBeanAttribute;
import org.odpi.openmetadata.fvt.opentypes.model.OmrsBeanReference;
import org.odpi.openmetadata.fvt.opentypes.utils.GeneratorUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * This class tests the OMRS by generating OMRS java beans and driving that code. It generates a java class for each entity, relationship, reference, classification.
 * It also generates mapper classes to map between the omrs api objects to the omrs bean objects. There is a omrs bean accessor and test classes that exposes operations
 * like create ,get, read and delete.
 *
 * The OMAS API can then call the OMRS bean accessor and the OMRS bean accessor deals with mapping to the omrs interface.
 *
 * In addition to the bean classes this generator creates junits that are then run as part of the build. These tests should fail if the the OMRS archive types are changed in
 * an unexpected way.
 */
public class OmrsBeanGenerator {

    // This generator can be run in the top level Egeria folder - i.e. the folder you run git commands in. It is run automatically as part of the Maven build.

    private static final Logger log = LoggerFactory.getLogger(OmrsBeanGenerator.class);

    //public static final String OPEN_METADATA_IMPLEMENTATION = "open-metadata-implementation";
    public static final String OPEN_METADATA_TEST = "open-metadata-test";
    public static final String OPEN_METADATA_FVT = "open-metadata-fvt";

    public static final String OPEN_TYPES_TEST = "open-types-test";
    public static final String OPEN_TYPES_FVT = "open-types-fvt";

    public static final String OPEN_TYPES_TEST_GENERATOR = "open-types-test-generator";

    public static final String GENERATOR_PROJECT = OPEN_METADATA_TEST + "/" +OPEN_METADATA_FVT + "/" +OPEN_TYPES_FVT +"/" + OPEN_TYPES_TEST_GENERATOR;
    public static final String TARGET_PROJECT = OPEN_METADATA_TEST + "/" +OPEN_METADATA_FVT + "/" +OPEN_TYPES_FVT +"/" +OPEN_TYPES_TEST;

    public static final String CLASSIFICATIONS = "classifications";
    public static final String RELATIONSHIPS = "relationships";
    public static final String REFERENCES = "references";
    public static final String ENTITIES = "entities";
    public static final String ENUMS = "enums";

    public static final String GENERATION_OMRS_RELATIONSHIP_TO_LINES_CLASS_NAME="OMRSRelationshipToLines.java";

    public static final String TEMPLATES_FOLDER = GENERATOR_PROJECT + "/src/main/resources/templates/";

    //public static final String SRC_SUBJECT_AREA = "/src/main/java/org/odpi/openmetadata/fvt/opentypes/";
    public static final String GEN_PKG_BASE = "org.odpi.openmetadata.fvt.opentypes.";
    public static final String FVT_PACKAGE = "/java/org/odpi/openmetadata/fvt/opentypes/";

    public static final String GENERATION_FOLDER = TARGET_PROJECT + "/src/main"+FVT_PACKAGE;
    public static final String GENERATION_TEST_FOLDER = TARGET_PROJECT + "/src/test"+FVT_PACKAGE;
    public static final String SERVER_FOLDER = "server/";

    public static final String GENERATION_REST_FOLDER = GENERATION_FOLDER + SERVER_FOLDER;
    public static final String GENERATION_REST_TEST_FOLDER =  GENERATION_TEST_FOLDER + SERVER_FOLDER;
    public static final String GENERATION_REST_CLASS_NAME = "BeansToAccessOMRS.java";
    public static final String GENERATION_REST_TEST_CLASS_NAME = "TestBeansToAccessOMRS.java";

    public static final String GENERATION_REST_FILE = GENERATION_REST_FOLDER + GENERATION_REST_CLASS_NAME;
    public static final String GENERATION_REST_TEST_FILE = GENERATION_REST_TEST_FOLDER + GENERATION_REST_TEST_CLASS_NAME;

    public static final String GENERATION_REFERENCES_FOLDER = GENERATION_FOLDER  + REFERENCES + "/";
    public static final String GENERATION_ENTITIES_FOLDER = GENERATION_FOLDER  + ENTITIES + "/";
    public static final String GENERATION_ENUMS_FOLDER = GENERATION_FOLDER  + ENUMS + "/";
    public static final String GENERATION_CLASSIFICATIONS_FOLDER = GENERATION_FOLDER + CLASSIFICATIONS + "/";

    public static final String GEN_PKG_ENUMS = GEN_PKG_BASE+ENUMS;
    public static final String GEN_PKG_CLASSIFICATIONS = GEN_PKG_BASE+CLASSIFICATIONS;
    public static final String GEN_PKG_RELATIONSHIPS = GEN_PKG_BASE+RELATIONSHIPS;

    public static final String GENERATION_RELATIONSHIPS_FOLDER = GENERATION_FOLDER  + RELATIONSHIPS + "/";

    private static final String GENERATION_CLASSIFICATION_FACTORY_CLASS_NAME = "ClassificationBeanFactory";
    public static final String GENERATION_CLASSIFICATION_FACTORY_FILE_NAME = GENERATION_CLASSIFICATIONS_FOLDER + GENERATION_CLASSIFICATION_FACTORY_CLASS_NAME +".java";
    public static final String GENERATION_OMRS_RELATIONSHIP_TO_LINES_FILE_NAME = GENERATION_RELATIONSHIPS_FOLDER + GENERATION_OMRS_RELATIONSHIP_TO_LINES_CLASS_NAME;
    //template names
    // this is the template for a Reference - i.e. something that contains the relationship attributes and is our view of the relationship
    public static final String REFERENCE_TEMPLATE = TEMPLATES_FOLDER + "OmrsBeanReferenceTemplate";
    // this is the template to be able to create an
    public static final String ENTITY_TEMPLATE = TEMPLATES_FOLDER + "OmrsBeanEntityTemplate";
    // this is the template to be able to create classifications
    public static final String CLASSIFICATION_TEMPLATE = TEMPLATES_FOLDER + "OmrsBeanClassificationTemplate";
    // this is the template to be able to create relationship mappings
    public static final String RELATIONSHIP_MAPPER_TEMPLATE = TEMPLATES_FOLDER + "OmrsBeanRelationshipMapperTemplate";
    // this is the template to be able to create relationships
    public static final String RELATIONSHIP_TEMPLATE = TEMPLATES_FOLDER + "OmrsBeanRelationshipTemplate";
    // ths is the template to be able to create an omrsBean enum
    public static final String ENUM_TEMPLATE = TEMPLATES_FOLDER + "OmrsBeanEnumTemplate";
    public static final String RELATIONSHIP_TO_LINES_TEMPLATE = TEMPLATES_FOLDER + "OMRSRelationshipToLinesTemplate";
    // Entity mapper
    public static final String ENTITY_MAPPER_TEMPLATE = TEMPLATES_FOLDER + "OmrsBeanEntityMapperTemplate";
    // Entity mapper
    public static final String CLASSIFICATION_MAPPER_TEMPLATE = TEMPLATES_FOLDER + "OmrsBeanClassificationMapperTemplate";

    // this is the top level references e.g. TermReferences
    public static final String TOP_REFERENCE_TEMPLATE = TEMPLATES_FOLDER + "OmrsBeanTopReferenceTemplate";
    // this is the beans access file template
    public static final String OMRS_BEANS_TEMPLATE = TEMPLATES_FOLDER + "OmrsBeanAccessorTemplate";

    public static final String OMRS_BEANS_TEST_TEMPLATE = TEMPLATES_FOLDER + "TestOmrsBeanAccessorTemplate";

    public static final String CLASSIFICATION_FACTORY_TEMPLATE = TEMPLATES_FOLDER + "OmrsBeanClassificationFactoryTemplate";


    // the omrsBean model is a typed version of omrs - prepared for use by this omrsBean
    private org.odpi.openmetadata.fvt.opentypes.model.OmrsBeanModel omrsBeanModel;

    public OmrsBeanGenerator(org.odpi.openmetadata.fvt.opentypes.model.OmrsBeanModel omrsBeanModel) {
        this.omrsBeanModel = omrsBeanModel;
    }

    public static void main(String[] args) throws IOException {

        log.debug("Starting test code generation");
        if (checkCurrentFolder()) {
            // make sure we have the generation folders
            initializeFoldersandFiles();
            // run the generation
            generate(args);
        }
        else {
            log.error("Failed generation due to incorrect working directory. This must contain {}",OPEN_METADATA_TEST);
            // We need to fail to ensure build tools correctly see this as a failure
            System.exit(1);
        }
        log.debug("Ending test code generation");
    }

    /**
     * check that the current folder is as expected
     * @return where we have a valid folder to be able to generateClientSideRelationshipImpl in
     */
    private static boolean checkCurrentFolder() {
        // only run the generator if the current directory looks as expected.
        File curDir = new File(".");
        File[] filesList = curDir.listFiles();
        boolean validCurrentFolder =false;
        for(File f : filesList){
            String folderName = f.getName();
            log.debug("Found folder {}",folderName);
            if (folderName.equals(OPEN_METADATA_TEST)) {
                log.debug("Good folder found");
                validCurrentFolder = true;
            }
        }
        return validCurrentFolder;
    }

    private static void initializeFoldersandFiles() throws FileNotFoundException {

        log.debug("Creating folders");
        GeneratorUtilities.createEmptyFolder(GENERATION_FOLDER);
        GeneratorUtilities.writeFolder(GENERATION_REFERENCES_FOLDER);
        GeneratorUtilities.writeFolder(GENERATION_ENTITIES_FOLDER);
        GeneratorUtilities.writeFolder(GENERATION_ENUMS_FOLDER);
        GeneratorUtilities.writeFolder(GENERATION_CLASSIFICATIONS_FOLDER);
        GeneratorUtilities.writeFolder(GENERATION_RELATIONSHIPS_FOLDER);
        GeneratorUtilities.writeFolder(GENERATION_REST_FOLDER);
        GeneratorUtilities.writeFolder(GENERATION_REST_TEST_FOLDER);

        File classificationFactoryFile = new File(GENERATION_CLASSIFICATION_FACTORY_FILE_NAME);
        if (classificationFactoryFile.exists()) {
            classificationFactoryFile.delete();
        }

        File relationshipToLinesFile = new File(GENERATION_OMRS_RELATIONSHIP_TO_LINES_FILE_NAME);
        if (relationshipToLinesFile.exists()) {
            relationshipToLinesFile.delete();
        }
        log.debug("Folder creation complete");
    }

    /**
     * This generation creates 8 types of files:
     * <p>
     * All the files are generated in the 
     * <p>
     * - a reference java file - lives in a references sub folder named sourceTotarget type.
     * for example generation.references.TermToGlossary.AnchorReference.
     * - an attribute java file - lives in an entities subfolder named with the entity Name. The file is called entityName Attributes.
     * for example generation.entities.Asset.AssetAttribute
     * - an entity java file - lives lives in an entities subfolder named with the entity Name. The file is called entityName.
     * for example generation.entities.Asset.Asset
     * The entity file contains the core and identifiable attributes, a member of type attribute java class and
     * a member of type top reference class
     * - a topreference java file contains members for each of the java reference type.
     * for example generation.entities.Asset.AssetReferences
     * - a enum java file to hold enums
     * - classifications files
     * - relationships files
     * - beans access file
     *
     * @param args arguments supplied to the java program
     * @throws IOException IO Exception
     */
    public static void generate(String[] args) throws IOException {
        org.odpi.openmetadata.fvt.opentypes.model.OmrsBeanModel omrsBeanModel = new org.odpi.openmetadata.fvt.opentypes.model.OmrsBeanModel();

        OmrsBeanGenerator generator = new OmrsBeanGenerator(omrsBeanModel);

        log.debug("starting actual generation");
        // generate the enum files
        log.debug("Generating Enum files");
        generator.generateEnumFiles();
        // generate the classification files and their attributes
        log.debug("Generating Classification files");
        generator.generateClassificationFiles();
        // generate the classification bean factory
        log.debug("Generating Classification bean factory");
        generator.generateClassificationBeanFactory();
        // generate OMRSRelationshipToLines.
        log.debug("Generating OMRS Relationship to Lines");
        generator.generateOMRSRelationshipToLines();
        // generate the entity and attribute files
        log.debug("Generating Entity related files");
        generator.generateEntityRelatedFiles(omrsBeanModel.getOmrsBeanEntityAttributeMap());
        // generate relationships and their attributes
        log.debug("Generating Relationship files");
        generator.generateRelationshipFiles();
        // generate the reference files
        log.debug("Generating Reference files");
        generator.generateReferenceFiles();
        // generate the omrs beans accessor file
        log.debug("Generating OMRS Beans Accessor file");
        generator.generateOMRSBeansAccessorFile(GENERATION_REST_FILE, OMRS_BEANS_TEMPLATE);
        // generate the omrs beans test file
        log.debug("Generating OMRS Beans Accessor file");
        generator.generateOMRSBeansAccessorFile(GENERATION_REST_TEST_FILE, OMRS_BEANS_TEST_TEMPLATE);
    }

    private void generateEnumFiles() throws IOException {

        Map<String, List<org.odpi.openmetadata.fvt.opentypes.model.OmrsBeanEnumValue>> enumsMap = omrsBeanModel.getEnumsMap();
        for (String enumName : enumsMap.keySet()) {
            final String enumFileName = GENERATION_ENUMS_FOLDER + enumName + ".java";
            log.debug("Generating enum file {}",enumFileName);
            generateEnumFile(enumName, enumsMap.get(enumName), enumFileName,GEN_PKG_ENUMS);
        }
    }

    private void generateClassificationFiles() throws IOException {
        Map<String, List<OmrsBeanAttribute>> omrsBeanAttributeMap = omrsBeanModel.getOmrsBeanClassificationAttributeMap();
        for (String classificationName : omrsBeanAttributeMap.keySet()) {

            String outputFolder = this.createClassificationJavaFolderIfRequired(classificationName);
            final String classificationFileName = outputFolder + "/" + classificationName + ".java";
            final String classificationMapperFileName = outputFolder + "/" + classificationName + "Mapper.java";
            log.debug("Generating classification file {}",classificationFileName);
            generateClassificationFile(classificationName, classificationFileName,GEN_PKG_CLASSIFICATIONS+"."+classificationName);
            log.debug("Generating classification mapper file {}",classificationMapperFileName);
            generateClassificationMapperFile(classificationName, classificationMapperFileName);
        }
    }
    private void generateClassificationMapperFile(String classificationName, String fileName) throws IOException {
        FileWriter outputFileWriter = null;
        BufferedReader reader = null;
        try {
            outputFileWriter = new FileWriter(fileName);
            List<String> loopEntityLines = new ArrayList<>();
            reader = new BufferedReader(new FileReader(CLASSIFICATION_MAPPER_TEMPLATE));
            String line = reader.readLine();
            while (line != null) {
                loopEntityLines.add(line);
                line = reader.readLine();
            }
            reader.close();

            Map<String, String> replacementMap = new HashMap<>();
            replacementMap.put("package", CLASSIFICATIONS + "." + classificationName);
            mapOMRSToOMAS("Classification", replacementMap, outputFileWriter, loopEntityLines, classificationName);
        } finally {
            closeReaderAndFileWriter(outputFileWriter, reader);
        }
    }

    private void generateClassificationBeanFactory() throws IOException {
        FileWriter outputFileWriter = null;
        BufferedReader reader = null;
        try {
            outputFileWriter = new FileWriter(GENERATION_CLASSIFICATION_FACTORY_FILE_NAME);
            reader = new BufferedReader(new FileReader(CLASSIFICATION_FACTORY_TEMPLATE));
            String line = reader.readLine();
            while (line != null) {
                List<String> loopLines = new ArrayList<>();

                if (line.contains("<$$$")) {
                    // read all the lines in the loop
                    String loopline = reader.readLine();
                    while (loopline != null) {
                        //stash the lines for the loop and spit them out for each entity
                        if (loopline.contains("$$$>")) {
                            break;
                        }
                        loopLines.add(loopline);
                        loopline = reader.readLine();
                    }
                    // for each classification write out the loop line
                    for (String classificationName : omrsBeanModel.getOmrsBeanClassificationAttributeMap().keySet()) {
                        for (int loopCounter = 0; loopCounter < loopLines.size(); loopCounter++) {
                            String newLine = loopLines.get(loopCounter);
                            newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("uClassification"), GeneratorUtilities.uppercase1stLetter(classificationName));
                            outputFileWriter.write(newLine + "\n");
                        }
                    }
                } else {
                    outputFileWriter.write(line + "\n");
                }
                line = reader.readLine();
            }

        } finally {
            closeReaderAndFileWriter(outputFileWriter, reader);
        }

    }

    private void generateOMRSRelationshipToLines() throws IOException {
        FileWriter outputFileWriter = null;
        BufferedReader reader = null;
        try {
            outputFileWriter = new FileWriter(GENERATION_OMRS_RELATIONSHIP_TO_LINES_FILE_NAME);
            reader = new BufferedReader(new FileReader(RELATIONSHIP_TO_LINES_TEMPLATE));
            String line = reader.readLine();
            while (line != null) {
                List<String> loopLines = new ArrayList<>();

                if (line.contains(" <$$RELATIONSHIP$")) {
                    // read all the lines in the loop
                    String loopline = reader.readLine();
                    while (loopline != null) {
                        //stash the lines for the loop and spit them out for each relationship
                        if (loopline.contains(" $$RELATIONSHIP$>")) {
                            break;
                        }
                        loopLines.add(loopline);
                        loopline = reader.readLine();
                    }
                    // for each relationship write out the loop line
                    for (String relationshipName : omrsBeanModel.getOmrsBeanRelationshipMap().keySet()) {
                        for (int loopCounter = 0; loopCounter < loopLines.size(); loopCounter++) {
                            String newLine = loopLines.get(loopCounter);
                            newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("uRelationshipName"), GeneratorUtilities.uppercase1stLetter(relationshipName));
                            newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("RelationshipName"), GeneratorUtilities.lowercase1stLetter(relationshipName));
                            outputFileWriter.write(newLine + "\n");
                        }
                    }
                } else {
                    outputFileWriter.write(line + "\n");
                }
                line = reader.readLine();
            }
        } finally {
            closeReaderAndFileWriter(outputFileWriter, reader);
        }
    }

    private void generateRelationshipFiles() throws IOException {
        Map<String, List<OmrsBeanAttribute>> omrsBeanAttributeMap = omrsBeanModel.getOmrsBeanRelationshipAttributeMap();
        for (String relationshipName : omrsBeanAttributeMap.keySet()) {
            // upper case the first letter.
            relationshipName = GeneratorUtilities.uppercase1stLetter(relationshipName);
            String outputFolder = this.createRelationshipJavaFolderIfRequired(relationshipName);
            final String relationshipFileName = outputFolder + "/" + relationshipName + ".java";
            org.odpi.openmetadata.fvt.opentypes.model.OmrsBeanRelationship omrsBeanRelationship = omrsBeanModel.getOmrsBeanRelationshipByName(relationshipName);
            log.debug("Generating relationship file {}",relationshipFileName);

            generateRelationshipFile(omrsBeanRelationship, relationshipFileName,GEN_PKG_RELATIONSHIPS +"."+relationshipName);
            final String relationshipMapperFileName = outputFolder + "/" + relationshipName + "Mapper.java";
            log.debug("Generating relationship mapper file {}",relationshipMapperFileName);
            generateRelationshipMapperFile(omrsBeanRelationship, relationshipMapperFileName);
        }


    }
    private void generateEntityRelatedFiles(Map<String, List<OmrsBeanAttribute>> omrsBeanAttributeMap) throws IOException {
        for (String entityName : omrsBeanAttributeMap.keySet()) {
            String outputFolder = this.createEntityJavaFolderIfRequired(entityName);
            final String entityFileName = outputFolder + "/" + entityName + ".java";
            log.debug("Generating Entity file {}",entityFileName);
            generateEntityFile(entityName, entityFileName);
            final String mapperFileName = outputFolder + "/" + entityName + "Mapper.java";
            log.debug("Generating Entity mapper file {}",mapperFileName);
            generateEntityMapperFile(entityName, mapperFileName);
        }
    }

    /**
     * Generate the top reference file
     * @param entityName
     * @param omrsBeanAttributes
     * @param topReferenceFileName
     * @throws IOException
     */
    private void generateTopReferenceFile(String entityName, List<OmrsBeanAttribute> omrsBeanAttributes, String topReferenceFileName) throws IOException {
        FileWriter outputFileWriter = null;
        BufferedReader reader = null;
        try {
            outputFileWriter = new FileWriter(topReferenceFileName);
            reader = new BufferedReader(new FileReader(TOP_REFERENCE_TEMPLATE));
            String line = reader.readLine();
            if (omrsBeanAttributes !=null && omrsBeanAttributes.size() > 0) {
                while (line != null) {
                    Map<String, String> replacementMap = new HashMap<>();
                    replacementMap.put("package", ENTITIES + "." + entityName);
                    replacementMap.put("name", org.odpi.openmetadata.fvt.opentypes.utils.GeneratorUtilities.lowercase1stLetter(entityName));
                    replacementMap.put("uname", GeneratorUtilities.uppercase1stLetter(entityName));
                    writeTopReferenceAttributesToFile(omrsBeanAttributes, replacementMap, outputFileWriter, reader, line);
                    line = reader.readLine();
                }
            }
        } finally {
            closeReaderAndFileWriter(outputFileWriter, reader);
        }
    }

    /**
     * Generate EnumMapper file. One for each enum
     * @param enumName
     * @param enumValues
     * @param enumFileName
     * @throws IOException
     */
    private void generateEnumFile(String enumName, List<org.odpi.openmetadata.fvt.opentypes.model.OmrsBeanEnumValue> enumValues, String enumFileName, String pkg) throws IOException {
        FileWriter outputFileWriter = null;
        BufferedReader reader = null;
        try {
            outputFileWriter =       new FileWriter(enumFileName);

            reader = new BufferedReader(new FileReader(ENUM_TEMPLATE));
            String line = reader.readLine();
            while (line != null) {
                Map<String, String> replacementMap = new HashMap<>();
                replacementMap.put("description", this.omrsBeanModel.getTypeDefDescription(GeneratorUtilities.uppercase1stLetter(enumName)));
                replacementMap.put("package", pkg);
                replacementMap.put("name", org.odpi.openmetadata.fvt.opentypes.utils.GeneratorUtilities.lowercase1stLetter(enumName));
                replacementMap.put("uname", GeneratorUtilities.uppercase1stLetter(enumName));
                //String enumName, List<OmrsBeanEnumValue> enumValues, Map<String, String> replacementMap,
                writeEnumToFile(enumName, enumValues, replacementMap, outputFileWriter, reader, line);
                line = reader.readLine();
            }
        } finally {
            closeReaderAndFileWriter(outputFileWriter, reader);
        }
    }

    /**
     * Generate the EntityMapper file. There is one for each entity
     * @param entityName name of the entity
     * @param fileName name of the file
     * @throws IOException
     */
    private void generateEntityMapperFile(String entityName, String fileName) throws IOException {
        FileWriter outputFileWriter = null;
        BufferedReader reader = null;

        try {
            outputFileWriter = new FileWriter(fileName);
            List<String> loopEntityLines = new ArrayList<>();
            reader = new BufferedReader(new FileReader(ENTITY_MAPPER_TEMPLATE));
            String line = reader.readLine();
            while (line != null) {
                loopEntityLines.add(line);
                line = reader.readLine();
            }
            reader.close();
            Map<String, String> replacementMap = new HashMap<>();
            replacementMap.put("package", ENTITIES + "." + entityName);
            mapOMRSToOMAS("Entity", replacementMap, outputFileWriter, loopEntityLines, entityName);
        } finally {
            closeReaderAndFileWriter(outputFileWriter, reader);
        }

    }

    /**
     *  write out the omrs beans accessor file
     * @throws IOException
     */
    private void generateOMRSBeansAccessorFile(String fileName, String templateName) throws IOException {
        FileWriter outputFileWriter = null;
        BufferedReader reader = null;

        try {
            outputFileWriter =  new FileWriter(fileName);
            reader = new BufferedReader(new FileReader(templateName));
            String line = reader.readLine();
            while (line != null) {
                Map<String, String> replacementMap = new HashMap<>();
                writeOMRSBeansAccessorFile(replacementMap, outputFileWriter, reader, line);
                line = reader.readLine();
            }
        } finally {
            closeReaderAndFileWriter(outputFileWriter, reader);
        }
    }



    /**
     * Write out the line from the template.
     *
     * This method also has logic to loop through lines associated with attributes
     *
     * @param replacementMap
     * @param outputFileWriter
     * @param reader
     * @param line
     * @throws IOException
     */
    private void writeOMRSBeansAccessorFile(Map<String, String> replacementMap, FileWriter outputFileWriter, BufferedReader reader, String line) throws IOException {
        Set<String> allEntities = omrsBeanModel.getOmrsBeanEntityAttributeMap().keySet();
        List<String> loopEntityLines = new ArrayList<>();
        List<String> loopRelationshipLines = new ArrayList<>();
        if (line.contains("<$$$")) {
            // read all the lines in the loop
            String loopline = reader.readLine();
            while (loopline != null) {
                //stash the lines for the loop and spit them out for each entity
                if (loopline.contains("$$$>")) {
                    break;
                }
                loopEntityLines.add(loopline);
                loopline = reader.readLine();
            }
        }
        if (line.contains("<$$RELATIONSHIP$")) {
            // read all the lines in the loop
            String loopline = reader.readLine();

            while (loopline != null) {
                //stash the lines for the loop and spit them out for each entity
                if (loopline.contains("$$RELATIONSHIP$>")) {
                    break;
                }
                loopRelationshipLines.add(loopline);
                loopline = reader.readLine();
            }
        }
        if (!loopEntityLines.isEmpty()) {

            // loop through the entities and write out
            for (String entityName : allEntities) {
                mapOMRSToOMAS("Entity", replacementMap, outputFileWriter, loopEntityLines, entityName);
            }
        } else if (!loopRelationshipLines.isEmpty()) {
            // loop through the relationships and write out
            Map<String, List<OmrsBeanAttribute>> omrsBeanAttributeMap = omrsBeanModel.getOmrsBeanRelationshipAttributeMap();
            for (String relationshipName : omrsBeanAttributeMap.keySet()) {
                mapOMRSToOMAS("Relationship", replacementMap, outputFileWriter, loopRelationshipLines, relationshipName);
            }
        } else {
            // just write out the line
            String newLine = line;
            newLine = replaceTokensInLineFromMap(replacementMap, newLine);
            outputFileWriter.write(newLine + "\n");
        }
    }

    /**
     * @param omrsType          - Entity or Relationship
     * @param replacementMap
     * @param outputFileWriter
     * @param loopArtifactLines
     * @param omrsArtifactName
     * @throws IOException
     */
    private void mapOMRSToOMAS(String omrsType, Map<String, String> replacementMap, FileWriter outputFileWriter, List<String> loopArtifactLines, String omrsArtifactName) throws IOException {

        String lowercaseArtifact = GeneratorUtilities.lowercase1stLetter(omrsArtifactName);
        String uppercaseArtifact = GeneratorUtilities.uppercase1stLetter(omrsArtifactName);
        String lowerCasePluralArtifactName = "";

        if (lowercaseArtifact.endsWith("y")) {
            lowerCasePluralArtifactName = lowercaseArtifact.substring(0, lowercaseArtifact.length() - 1) + "ies";
        } else if (lowercaseArtifact.endsWith("s")) {
            lowerCasePluralArtifactName = lowercaseArtifact + "es";
        } else {
            lowerCasePluralArtifactName = lowercaseArtifact + "s";
        }
        List<OmrsBeanAttribute> allOmrsBeanAttributes = null;
        if (omrsType.equals("Entity")) {
            allOmrsBeanAttributes = omrsBeanModel.getOmrsBeanEntityAttributeMap().get(uppercaseArtifact);
        } else if (omrsType.equals("Classification")) {
            allOmrsBeanAttributes = omrsBeanModel.getOmrsBeanClassificationAttributeMap().get(uppercaseArtifact);
        } else {
            allOmrsBeanAttributes = omrsBeanModel.getOmrsBeanRelationshipAttributeMap().get(uppercaseArtifact);
        }
        List<OmrsBeanAttribute> omrsBeanAttributes = new ArrayList<OmrsBeanAttribute>();
        List<OmrsBeanAttribute> omrsBeanEnumAttributes = new ArrayList<OmrsBeanAttribute>();
        List<OmrsBeanAttribute> omrsBeanMapAttributes = new ArrayList<OmrsBeanAttribute>();
        if (allOmrsBeanAttributes != null) {
            for (OmrsBeanAttribute omrsBeanAttribute : allOmrsBeanAttributes) {
                if (omrsBeanAttribute.isEnum) {
                    omrsBeanEnumAttributes.add(omrsBeanAttribute);
                } else if (omrsBeanAttribute.isMap) {
                    omrsBeanMapAttributes.add(omrsBeanAttribute);
                } else {
                    omrsBeanAttributes.add(omrsBeanAttribute);
                }
            }
        }

        for (int loopArtifactCounter = 0; loopArtifactCounter < loopArtifactLines.size(); loopArtifactCounter++) {
            String newLine = loopArtifactLines.get(loopArtifactCounter);
            newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken(omrsType + "Name"), lowercaseArtifact);
            newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("u" + omrsType + "Name"), uppercaseArtifact);
            newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("lowerCasePlural" + omrsType + "Name"), lowerCasePluralArtifactName);
            if (omrsType.equals("Relationship")) {
                // add the relationship specific replacements
                org.odpi.openmetadata.fvt.opentypes.model.OmrsBeanRelationship omrsBeanRelationship = omrsBeanModel.getOmrsBeanRelationshipByName(uppercaseArtifact);

                newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("uRelationshipEntity1"), GeneratorUtilities.uppercase1stLetter(omrsBeanRelationship.entityProxy1Name));
                newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("uRelationshipEntity2"), GeneratorUtilities.uppercase1stLetter(omrsBeanRelationship.entityProxy2Name));
                newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("uRelationshipEntityType1"), GeneratorUtilities.uppercase1stLetter(omrsBeanRelationship.entityProxy1Type));
                newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("uRelationshipEntityType2"), GeneratorUtilities.uppercase1stLetter(omrsBeanRelationship.entityProxy2Type));
            }
            if (newLine.contains("<$$Attr$$")) {
                loopArtifactCounter++;
                // we have attribute code that we need to repeat
                List<String> loopAttrLines = new ArrayList<>();
                newLine = loopArtifactLines.get(loopArtifactCounter);
                while (newLine != null) {
                    //stash the lines for the loop and spit them out for each entity
                    if (newLine.contains("$$Attr$$>")) {
                        loopArtifactCounter++;
                        newLine = loopArtifactLines.get(loopArtifactCounter);
                        break;
                    }
                    loopAttrLines.add(newLine);
                    loopArtifactCounter++;
                    newLine = loopArtifactLines.get(loopArtifactCounter);
                }
                // we have the inner loop lines we need to text replace
                if (!omrsBeanAttributes.isEmpty()) {
                    //uAttrType
                    for (int attributeNumber = 0; attributeNumber < omrsBeanAttributes.size(); attributeNumber++) {
                        OmrsBeanAttribute attribute = omrsBeanAttributes.get(attributeNumber);

                        if (!attribute.isList) {

                            // write out inner loop contents for each attribute
                            for (String attrLine : loopAttrLines) {
                                attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken(omrsType + "Name"), lowercaseArtifact);
                                attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("u" + omrsType + "Name"), uppercaseArtifact);
                                attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("AttrName"), GeneratorUtilities.lowercase1stLetter(attribute.name));
                                attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("uAttrName"), GeneratorUtilities.uppercase1stLetter(attribute.name));

                                if (!attribute.isList) {
                                    String uppercaseattributeType = attribute.type.toUpperCase();

                                    //some primitives need amending
                                    if (uppercaseattributeType.equals("INTEGER")) {
                                        uppercaseattributeType = "INT";
                                    }

                                    if (uppercaseattributeType.startsWith("LIST<")) {
                                        // List will be of the form LIST<STRING> . It needs to be STRING or an INT
                                        uppercaseattributeType = uppercaseattributeType.substring(5, uppercaseattributeType.length() - 1);
                                    }

                                    attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("uAttrType"), uppercaseattributeType);

                                    //TODO do other types as required
                                    // AttrValue is used as the default value for the unit tests
                                    String attrValue = "";
                                    if (uppercaseattributeType.equals("STRING")) {
                                        attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("AttrType"), "String");

                                        attrValue = "\"string" + attributeNumber + "\"";
//                                    } else if (uppercaseattributeType.equals("DATE")) {
//                                        attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("AttrType"), "java.util.Date");
//                                        attrValue = "new java.util.Date()";
                                    } else if (uppercaseattributeType.equals("INT")) {
                                        attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("AttrType"), "Integer");
                                        attrValue = String.valueOf(attributeNumber);
                                    } else if (uppercaseattributeType.equals("LONG") || uppercaseattributeType.equals("DATE")) {
                                        attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("AttrType"), "Long");
                                        attrValue = String.valueOf(attributeNumber) +"L";
                                    } else if (uppercaseattributeType.equals("FLOAT")) {
                                        attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("AttrType"), "Float");
                                        attrValue = String.valueOf(attributeNumber) + "f";
                                    } else if (uppercaseattributeType.equals("BOOLEAN")) {
                                        attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("AttrType"), "Boolean");
                                        attrValue = String.valueOf(true);
                                    } else {
                                        attrValue = "";
                                    }

                                    attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("AttrValue"), attrValue);
                                    outputFileWriter.write(attrLine + "\n");
                                }
                            }
                        }
                    }
                }
            } else if (newLine.contains("<$$AttrList$$")) {
                loopArtifactCounter++;
                // we have attribute code that we need to repeat
                List<String> loopAttrLines = new ArrayList<>();
                newLine = loopArtifactLines.get(loopArtifactCounter);
                while (newLine != null) {
                    //stash the lines for the loop and spit them out for each entity
                    if (newLine.contains("$$AttrList$$>")) {
                        loopArtifactCounter++;
                        newLine = loopArtifactLines.get(loopArtifactCounter);
                        break;
                    }
                    loopAttrLines.add(newLine);
                    loopArtifactCounter++;
                    newLine = loopArtifactLines.get(loopArtifactCounter);
                }                 // we have the inner loop lines we need to text replace
                if (!omrsBeanAttributes.isEmpty()) {
                    //uAttrType
                    for (int attributeNumber = 0; attributeNumber < omrsBeanAttributes.size(); attributeNumber++) {
                        OmrsBeanAttribute attribute = omrsBeanAttributes.get(attributeNumber);
                        if (attribute.isList) {
                            // write out inner loop contents for each attribute
                            for (String attrLine : loopAttrLines) {
                                attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken(omrsType + "Name"), lowercaseArtifact);
                                attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("u" + omrsType + "Name"), uppercaseArtifact);
                                attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("AttrName"), GeneratorUtilities.lowercase1stLetter(attribute.name));
                                attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("uAttrName"), GeneratorUtilities.uppercase1stLetter(attribute.name));
                                String uppercaseattributeType = attribute.type.toUpperCase();

                                //some primitives need amending


                                if (uppercaseattributeType.startsWith("LIST<")) {
                                    // List will be of the form LIST<STRING> . It needs to be STRING
                                    uppercaseattributeType = uppercaseattributeType.substring(5, uppercaseattributeType.length() - 1);
                                }
                                if (uppercaseattributeType.equals("INTEGER")) {
                                    uppercaseattributeType = "INT";
                                }
                                attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("uAttrType"), uppercaseattributeType);

                                //TODO do other types date and int and array are used in the json models
                                String attrValue = "";
                                if (uppercaseattributeType.equals("STRING")) {
                                    attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("AttrType"), "String");

                                    attrValue = "\"string" + attributeNumber + "\"";
//                                } else if (uppercaseattributeType.equals("DATE")) {
//                                    attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("AttrType"), "java.util.Date");
//                                    attrValue = "new java.util.Date()";
                                } else if (uppercaseattributeType.equals("INT")) {
                                    attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("AttrType"), "Integer");
                                    attrValue = String.valueOf(attributeNumber);
                                } else if (uppercaseattributeType.equals("LONG") || uppercaseattributeType.equals("DATE")) {
                                    attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("AttrType"), "Long");
                                    attrValue = String.valueOf(attributeNumber) +"L";
                                } else if (uppercaseattributeType.equals("FLOAT")) {
                                    attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("AttrType"), "Float");
                                    attrValue = String.valueOf(attributeNumber) + "f";
                                } else {
                                    attrValue = "";
                                }
                                attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("AttrValue"), attrValue);
                                outputFileWriter.write(attrLine + "\n");
                            }
                        }
                    }
                }
            } else if (newLine.contains("<$$Enum$$")) {
                // TODO Lists of enums
                List<String> loopEnumLines = new ArrayList<>();
                loopArtifactCounter++;
                newLine = loopArtifactLines.get(loopArtifactCounter);
                while (newLine != null) {
                    //stash the lines for the loop and spit them out for each entity
                    if (newLine.contains("$$Enum$$>")) {
                        loopArtifactCounter++;
                        newLine = loopArtifactLines.get(loopArtifactCounter);
                        break;
                    }
                    loopEnumLines.add(newLine);
                    loopArtifactCounter++;
                    newLine = loopArtifactLines.get(loopArtifactCounter);
                }
                // we have the inner loop lines we need to text replace

                if (!omrsBeanEnumAttributes.isEmpty()) {
                    for (OmrsBeanAttribute enumAttribute : omrsBeanEnumAttributes) {
                        // write out inner loop contents for each enum attribute
                        for (String enumLine : loopEnumLines) {
                            enumLine = enumLine.replaceAll(GeneratorUtilities.getRegexToken(omrsType + "Name"), lowercaseArtifact);
                            enumLine = enumLine.replaceAll(GeneratorUtilities.getRegexToken("u" + omrsType + "Name"), uppercaseArtifact);

                            enumLine = enumLine.replaceAll(GeneratorUtilities.getRegexToken("EnumType"), lowercaseArtifact);
                            enumLine = enumLine.replaceAll(GeneratorUtilities.getRegexToken("uEnumType"), GeneratorUtilities.uppercase1stLetter(enumAttribute.type));
                            enumLine = enumLine.replaceAll(GeneratorUtilities.getRegexToken("EnumName"), GeneratorUtilities.lowercase1stLetter(enumAttribute.name));
                            enumLine = enumLine.replaceAll(GeneratorUtilities.getRegexToken("uEnumName"), GeneratorUtilities.uppercase1stLetter(enumAttribute.name));
                            outputFileWriter.write(enumLine + "\n");
                        }
                    }
                }
            } else if (newLine.contains("<$$Map$$")) {
                List<String> loopMapLines = new ArrayList<>();
                loopArtifactCounter++;
                newLine = loopArtifactLines.get(loopArtifactCounter);
                while (newLine != null) {
                    //stash the lines for the loop and spit them out for each entity
                    if (newLine.contains("$$Map$$>")) {
                        loopArtifactCounter++;
                        newLine = loopArtifactLines.get(loopArtifactCounter);
                        break;
                    }
                    loopMapLines.add(newLine);
                    loopArtifactCounter++;
                    newLine = loopArtifactLines.get(loopArtifactCounter);
                }
                // we have the inner loop lines we need to text replace

                if (!omrsBeanMapAttributes.isEmpty()) {
                    for (OmrsBeanAttribute mapAttribute : omrsBeanMapAttributes) {
                        // write out inner loop contents for each map attribute
                        for (String mapLine : loopMapLines) {
                            mapLine = mapLine.replaceAll(GeneratorUtilities.getRegexToken("u" + omrsType + "Name"), uppercaseArtifact);
                            mapLine = mapLine.replaceAll(GeneratorUtilities.getRegexToken(omrsType + "Name"), lowercaseArtifact);
                            mapLine = mapLine.replaceAll(GeneratorUtilities.getRegexToken("MapName"), GeneratorUtilities.lowercase1stLetter(mapAttribute.name));
                            mapLine = mapLine.replaceAll(GeneratorUtilities.getRegexToken("uMapName"), GeneratorUtilities.uppercase1stLetter(mapAttribute.name));
                            outputFileWriter.write(mapLine + "\n");
                        }
                    }
                }

                // TODO Lists of enums?
                // TODO Lists
                // TODO Maps
            } else if (newLine.contains("<$$ref$$")) {
                List<String> loopRefLines = new ArrayList<>();
                loopArtifactCounter++;
                newLine = loopArtifactLines.get(loopArtifactCounter);
                while (newLine != null) {
                    //stash the lines for the loop and spit them out for each entity
                    if (newLine.contains("$$ref$$>")) {
                        loopArtifactCounter++;
                        newLine = loopArtifactLines.get(loopArtifactCounter);
                        break;
                    }
                    loopRefLines.add(newLine);
                    loopArtifactCounter++;
                    newLine = loopArtifactLines.get(loopArtifactCounter);
                }
                // we have the inner loop lines we need to text replace

                List<OmrsBeanAttribute> omrsBeanReferences = omrsBeanModel.getOmrsBeanReferencesAsAttributesByEntity().get(omrsArtifactName);
                if (omrsBeanReferences != null) {
                    for (OmrsBeanAttribute reference : omrsBeanReferences) {
                        if (!reference.isList) {
                            // write out inner loop contents for each attribute
                            for (String refLine : loopRefLines) {
                                refLine = refLine.replaceAll(GeneratorUtilities.getRegexToken(omrsType + "Name"), lowercaseArtifact);
                                refLine = refLine.replaceAll(GeneratorUtilities.getRegexToken("referencePackage"), reference.referencePackage);
                                refLine = refLine.replaceAll(GeneratorUtilities.getRegexToken("refName"), GeneratorUtilities.lowercase1stLetter(reference.name));
                                refLine = refLine.replaceAll(GeneratorUtilities.getRegexToken("urefName"), GeneratorUtilities.uppercase1stLetter(reference.name));
                                outputFileWriter.write(refLine + "\n");
                            }
                        }
                    }
                }
            } else if (newLine.contains("<$$refList$$")) {
                List<String> loopRefLines = new ArrayList<>();
                loopArtifactCounter++;
                newLine = loopArtifactLines.get(loopArtifactCounter);
                while (newLine != null) {
                    //stash the lines for the loop and spit them out for each entity
                    if (newLine.contains("$$refList$$>")) {
                        loopArtifactCounter++;
                        newLine = loopArtifactLines.get(loopArtifactCounter);
                        break;
                    }
                    loopRefLines.add(newLine);
                    loopArtifactCounter++;
                    newLine = loopArtifactLines.get(loopArtifactCounter);
                }
                // we have the inner loop lines we need to text replace

                List<OmrsBeanAttribute> omrsBeanReferences = omrsBeanModel.getOmrsBeanReferencesAsAttributesByEntity().get(omrsArtifactName);
                if (omrsBeanReferences != null) {
                    for (OmrsBeanAttribute reference : omrsBeanReferences) {
                        if (reference.isList) {
                            // write out inner loop contents for each enum attribute
                            for (String refLine : loopRefLines) {
                                refLine = refLine.replaceAll(GeneratorUtilities.getRegexToken(omrsType + "Name"), lowercaseArtifact);
                                refLine = refLine.replaceAll(GeneratorUtilities.getRegexToken("referencePackage"), reference.referencePackage);
                                refLine = refLine.replaceAll(GeneratorUtilities.getRegexToken("refListName"), GeneratorUtilities.lowercase1stLetter(reference.name));
                                refLine = refLine.replaceAll(GeneratorUtilities.getRegexToken("urefListName"), GeneratorUtilities.uppercase1stLetter(reference.name));
                                outputFileWriter.write(refLine + "\n");
                            }
                        }
                    }
                }
            } else {
                // write out the non loop entity line
                newLine = replaceTokensInLineFromMap(replacementMap, newLine);
                outputFileWriter.write(newLine + "\n");
            }
        } // end for loop
    }

    private void generateEntityFile(String entityName, String fileName) throws IOException {

        FileWriter outputFileWriter = null;
        BufferedReader reader=null;
        try {
            outputFileWriter = new FileWriter(fileName);
            String uEntityName = GeneratorUtilities.uppercase1stLetter(entityName);
            //ensure entityName is lower case
            entityName = GeneratorUtilities.lowercase1stLetter(entityName);
            List<OmrsBeanAttribute> attributeMap = omrsBeanModel.getOmrsBeanEntityAttributeMap().get(uEntityName);
            reader= new BufferedReader(new FileReader(ENTITY_TEMPLATE));
            String line = reader.readLine();
            while (line != null) {
                Map<String, String> replacementMap = new HashMap<>();
                replacementMap.put("uname", uEntityName);
                replacementMap.put("name", entityName);
                replacementMap.put("description", omrsBeanModel.getTypeDefDescription(uEntityName));
                replacementMap.put("package", "entities." + uEntityName);
                writeAttributesToFile(attributeMap, replacementMap, outputFileWriter, reader, line);
                line = reader.readLine();
            }
        } finally {
            closeReaderAndFileWriter(outputFileWriter, reader);
        }
    }

    private void generateClassificationFile(String classificationName, String fileName,String pkg) throws IOException {
        FileWriter outputFileWriter = null;
        BufferedReader reader=null;
        try {
            outputFileWriter = new FileWriter(fileName);

            classificationName = GeneratorUtilities.lowercase1stLetter(classificationName);
            String uClassificationName = GeneratorUtilities.uppercase1stLetter(classificationName);
            List<OmrsBeanAttribute> attrList = omrsBeanModel.getOmrsBeanClassificationAttributeMap().get(uClassificationName);

            reader = new BufferedReader(new FileReader(CLASSIFICATION_TEMPLATE));
            String line = reader.readLine();
            while (line != null) {
                Map<String, String> replacementMap = new HashMap<>();

                replacementMap.put("uname", uClassificationName);
                replacementMap.put("name",classificationName);
                replacementMap.put("description", omrsBeanModel.getTypeDefDescription(uClassificationName));
                replacementMap.put("package",pkg);
                writeAttributesToFile(attrList, replacementMap, outputFileWriter, reader, line);
                line = reader.readLine();
            }
        } finally {
            closeReaderAndFileWriter(outputFileWriter, reader);
        }
    }

    private void generateRelationshipFile(org.odpi.openmetadata.fvt.opentypes.model.OmrsBeanRelationship omrsBeanRelationship, String fileName, String pkg) throws IOException {
        FileWriter outputFileWriter = null;
        BufferedReader reader=null;
        try {
            outputFileWriter = new FileWriter(fileName);
            String relationshipName = omrsBeanRelationship.label;
            relationshipName = GeneratorUtilities.lowercase1stLetter(relationshipName);
            String uRelationshipName = GeneratorUtilities.uppercase1stLetter(relationshipName);
            reader = new BufferedReader(new FileReader(RELATIONSHIP_TEMPLATE));
            String line = reader.readLine();
            while (line != null) {
                Map<String, String> replacementMap = new HashMap<>();
                List<OmrsBeanAttribute> attrList = omrsBeanModel.getOmrsBeanRelationshipAttributeMap().get(uRelationshipName);
                replacementMap.put("uname", uRelationshipName);
                replacementMap.put("description", this.omrsBeanModel.getTypeDefDescription(GeneratorUtilities.uppercase1stLetter(GeneratorUtilities.uppercase1stLetter(relationshipName))));

                replacementMap.put("name", relationshipName);
                replacementMap.put("package",pkg);
                replacementMap.put("entityProxy1Name", omrsBeanRelationship.entityProxy1Name);
                replacementMap.put("entityProxy1Type", omrsBeanRelationship.entityProxy1Type);
                replacementMap.put("entityProxy2Name", omrsBeanRelationship.entityProxy2Name);
                replacementMap.put("entityProxy2Type", omrsBeanRelationship.entityProxy2Type);
                replacementMap.put("typeDefGuid", omrsBeanRelationship.typeDefGuid);

                writeAttributesToFile(attrList, replacementMap, outputFileWriter, reader, line);
                line = reader.readLine();
            }
        } finally {
            closeReaderAndFileWriter(outputFileWriter, reader);
        }
    }

    private void generateRelationshipMapperFile(org.odpi.openmetadata.fvt.opentypes.model.OmrsBeanRelationship omrsBeanRelationship, String fileName) throws IOException {
        FileWriter outputFileWriter = null;
        BufferedReader reader=null;
        try {
            outputFileWriter = new FileWriter(fileName);

            String label = omrsBeanRelationship.label;
            Map<String, String> replacementMap = new HashMap<>();
            List<String> loopRelationshipLines = new ArrayList<>();
            reader = new BufferedReader(new FileReader(RELATIONSHIP_MAPPER_TEMPLATE));

            String line = reader.readLine();
            while (line != null) {
                replacementMap.put("uname", GeneratorUtilities.uppercase1stLetter(label));
                replacementMap.put("name", GeneratorUtilities.lowercase1stLetter(label));
                loopRelationshipLines.add(line);
                line = reader.readLine();
            }
            mapOMRSToOMAS("Relationship", replacementMap, outputFileWriter, loopRelationshipLines, label);
        } finally {
            closeReaderAndFileWriter(outputFileWriter, reader);
        }
    }

    private void generateReferenceFiles() throws IOException {
        // optimization to only process each entity once
        HashSet<String> processedEntities = new HashSet<String>();

        List<OmrsBeanReference> omrsBeanReferences = omrsBeanModel.getOmrsBeanReferences();
        for (org.odpi.openmetadata.fvt.opentypes.model.OmrsBeanReference omrsBeanReference : omrsBeanReferences) {

            // for each omrsBeanReference we need to create a java file in the right folder.
            String outputFolder = this.createReferenceJavaFolderIfRequired(omrsBeanReference);
            final String fileName = outputFolder + "/" + omrsBeanReference.uReferenceName + "Reference.java";
            FileWriter outputFileWriter = null;
            BufferedReader reader =null;
            try {
                log.debug("generating reference file {}",fileName);
                outputFileWriter = new FileWriter(fileName);
                reader = new BufferedReader(new FileReader(REFERENCE_TEMPLATE));
                String line = reader.readLine();
                while (line != null) {
                    List<OmrsBeanAttribute> attrList = omrsBeanReference.attrList;
                    Map<String, String> replacementMap = new HashMap<>();
                    replacementMap.put("uname", omrsBeanReference.uReferenceName);
                    replacementMap.put("mytype", omrsBeanReference.myType);
                    replacementMap.put("othertype", omrsBeanReference.relatedEndType);
                    replacementMap.put("name", omrsBeanReference.referenceName);
                    replacementMap.put("relationshiptype", omrsBeanReference.relationshipType);
                    replacementMap.put("relatedendtype", GeneratorUtilities.lowercase1stLetter(omrsBeanReference.relatedEndType));
                    replacementMap.put("urelatedendtype", GeneratorUtilities.uppercase1stLetter(omrsBeanReference.relatedEndType));
                    replacementMap.put("AttrDescription", omrsBeanReference.description);
                    writeAttributesToFile(attrList, replacementMap, outputFileWriter, reader, line);
                    line = reader.readLine();
                }
            } finally {
                closeReaderAndFileWriter(outputFileWriter, reader);
            }

            //generateClientSideRelationshipImpl the top references file
            Map<String, List<OmrsBeanAttribute>> omrsBeanReferencesAsAttributesByEntity = omrsBeanModel.getOmrsBeanReferencesAsAttributesByEntity();

            final Set<String> entitiesWithRelationships = omrsBeanReferencesAsAttributesByEntity.keySet();
            for (String entityName : entitiesWithRelationships) {
                if (!processedEntities.contains(entityName)) {
                    outputFolder = this.createEntityJavaFolderIfRequired(entityName);
                    final String topReferenceFileName = outputFolder + "/" + entityName + "References.java";
                    final List<OmrsBeanAttribute> omrsBeanAttributes = omrsBeanReferencesAsAttributesByEntity.get(entityName);
                    log.debug("generating top reference file {}", topReferenceFileName);
                    generateTopReferenceFile(entityName, omrsBeanAttributes, topReferenceFileName);
                    processedEntities.add(entityName);
                }
            }
        }
    }

    private void writeEnumToFile(String enumName, List<org.odpi.openmetadata.fvt.opentypes.model.OmrsBeanEnumValue> enumValues, Map<String, String> replacementMap, FileWriter outputFileWriter, BufferedReader reader, String line) throws IOException {
        if (line.contains("<$$$")) {
            // read all the lines in the loop
            String loopline = reader.readLine();
            List<String> loopLines = new ArrayList<>();
            while (loopline != null) {
                //stash the lines for the loop and spit them out for each attribute
                if (loopline.contains("$$$>")) {
                    break;
                }
                loopLines.add(loopline);
                loopline = reader.readLine();
            }

            // loop through the attributes and write out
            for (int i = 0; i < enumValues.size(); i++) {
                org.odpi.openmetadata.fvt.opentypes.model.OmrsBeanEnumValue omrsBeanEnumValue = enumValues.get(i);
                String enumValue = omrsBeanEnumValue.name;
                String enumDescription = omrsBeanEnumValue.description;
                for (int loopCounter = 0; loopCounter < loopLines.size(); loopCounter++) {
                    String newLine = loopLines.get(loopCounter);
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("enum"), enumValue);
                    if (enumDescription == null) {
                        // enum values may not have descriptions - we need to tolerate this
                        enumDescription = "Enumeration value for " + enumValue;
                    }
                    int enumOrdinal = omrsBeanEnumValue.ordinal;
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("enumdescription"), enumDescription);
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("enumordinal"), ""+ enumOrdinal);

                    outputFileWriter.write(newLine + "\n");
                }
                // enum values in Java need a commaed list - the lat entry should not have a comma after it.
                if (i < enumValues.size() - 1) {
                    outputFileWriter.write(",\n");
                }
            }
        } else {
            // not  in a loop
            String newLine = line;
            newLine = replaceTokensInLineFromMap(replacementMap, newLine);
            outputFileWriter.write(newLine + "\n");

        }
    }

    private void writeAttributesToFile(List<OmrsBeanAttribute> attrList, Map<String, String> replacementMap, FileWriter outputFileWriter, BufferedReader reader, String line) throws IOException {
        List<String> loopPropertyLines = new ArrayList<>();
        List<String> loopAttrLines = new ArrayList<>();
        List<String> loopEnumLines = new ArrayList<>();
        List<String> loopMapLines = new ArrayList<>();
        List<String> loopRelationshipLines = new ArrayList<>();

        if (line.contains("<$$$")) {
            // read all the lines in the loop
            String loopline = reader.readLine();

            while (loopline != null) {
                //stash the lines for the loop and spit them out for each attribute
                if (loopline.contains("$$$>")) {
                    break;
                }
                loopPropertyLines.add(loopline);
                loopline = reader.readLine();
            }
        } else if (line.contains("<$Enum$$")) {
            // read all the lines in the loop
            String loopline = reader.readLine();

            while (loopline != null) {
                //stash the lines for the loop and spit them out for each attribute
                if (loopline.contains("$Enum$$>")) {
                    break;
                }
                loopEnumLines.add(loopline);
                loopline = reader.readLine();
            }
        } else if (line.contains("<$Map$$")) {
            // read all the lines in the loop
            String loopline = reader.readLine();

            while (loopline != null) {
                //stash the lines for the loop and spit them out for each attribute
                if (loopline.contains("$Map$$>")) {
                    break;
                }
                loopMapLines.add(loopline);
                loopline = reader.readLine();
            }
        } else if (line.contains("<$Attr$$")) {
            // read all the lines in the loop
            String loopline = reader.readLine();

            while (loopline != null) {
                //stash the lines for the loop and spit them out for each attribute
                if (loopline.contains("$Attr$$>")) {
                    break;
                }
                loopAttrLines.add(loopline);
                loopline = reader.readLine();
            }
        } else if (line.contains("<$Relationship$$")) {
            // read all the lines in the loop
            String loopline = reader.readLine();

            while (loopline != null) {
                //stash the lines for the loop and spit them out for each attribute
                if (loopline.contains("$Relationship$$>")) {
                    break;
                }
                loopRelationshipLines.add(loopline);
                loopline = reader.readLine();
            }
        } else {
            // no loop
            String newLine = line;
            newLine = replaceTokensInLineFromMap(replacementMap, newLine);
            outputFileWriter.write(newLine + "\n");
        }

        if (attrList != null && !attrList.isEmpty()) {
            // loop through the attributes and write out
            for (OmrsBeanAttribute attr : attrList) {
                if (!loopPropertyLines.isEmpty()) {

                    for (int loopCounter = 0; loopCounter < loopPropertyLines.size(); loopCounter++) {

                        String newLine = loopPropertyLines.get(loopCounter);
                        newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("PropertyName"), GeneratorUtilities.lowercase1stLetter(attr.name));
                        newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("uPropertyName"), GeneratorUtilities.uppercase1stLetter(attr.name));
                        newLine = replaceTokensInLineFromMap(replacementMap, newLine);
                        newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("PropertyType"), GeneratorUtilities.uppercase1stLetter(attr.type));
                        /* Use the attribute type with the first letter capitalized as the javadoc
                         * We surround this with the {@code annotation for < and > characters - otherwise the javadoc would be invalid.
                         */
                        String javadoc = GeneratorUtilities.uppercase1stLetter(attr.type);
                        if (javadoc.contains("<") || javadoc.contains(">")) {
                            javadoc ="{@code " + javadoc + " }";
                        }
                        newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("PropertyTypeJavadoc"),javadoc );

                        // TODO handle non String types
                        newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("AttrDescription"), attr.description);
                        outputFileWriter.write(newLine + "\n");
                    }
                }
                if (!loopEnumLines.isEmpty()) {
                    for (int loopCounter = 0; loopCounter < loopEnumLines.size(); loopCounter++) {
                        String newLine = loopEnumLines.get(loopCounter);
                        if (attr.isEnum) {
                            newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("EnumName"), GeneratorUtilities.lowercase1stLetter(attr.name));
                            newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("description"), GeneratorUtilities.lowercase1stLetter(attr.description));
                            outputFileWriter.write(newLine + "\n");
                        }
                    }
                }
                if (!loopMapLines.isEmpty()) {
                    for (int loopCounter = 0; loopCounter < loopMapLines.size(); loopCounter++) {
                        String newLine = loopMapLines.get(loopCounter);
                        if (attr.isMap) {
                            newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("MapName"), GeneratorUtilities.lowercase1stLetter(attr.name));
                            newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("description"), attr.description);
                            outputFileWriter.write(newLine + "\n");
                        }
                    }
                }
                if (!loopAttrLines.isEmpty()) {
                    for (int loopCounter = 0; loopCounter < loopAttrLines.size(); loopCounter++) {
                        String newLine = loopAttrLines.get(loopCounter);
                        if ((!attr.isEnum) && !attr.isMap) {
                            newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("AttrName"), GeneratorUtilities.lowercase1stLetter(attr.name));
                            newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("description"), attr.description);
                            outputFileWriter.write(newLine + "\n");
                        }
                    }
                }
                if (!loopRelationshipLines.isEmpty()) {
                    for (int loopCounter = 0; loopCounter < loopRelationshipLines.size(); loopCounter++) {
                        String newLine = loopRelationshipLines.get(loopCounter);
                        if (attr.isReference) {
                            newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("description"), attr.description);
                            outputFileWriter.write(newLine + "\n");
                        }
                    }
                }
            }
        }
    }
    private void writeTopReferenceAttributesToFile(List<OmrsBeanAttribute> allProperties, Map<String, String> replacementMap, FileWriter outputFileWriter, BufferedReader reader, String line) throws IOException {
        List<String> loopPropertyLines = new ArrayList<>();
        List<String> loopSetAttrLines = new ArrayList<>();
        List<String> loopListAttrLines = new ArrayList<>();
        List<String> loopSingleAttrLines = new ArrayList<>();

        List<OmrsBeanAttribute> setProperties = new ArrayList<>();
        List<OmrsBeanAttribute> listProperties = new ArrayList<>();
        List<OmrsBeanAttribute> singleProperties = new ArrayList<>();

        // split out the properties
        for (OmrsBeanAttribute property : allProperties) {

            final String lowerCaseAttrName = GeneratorUtilities.lowercase1stLetter(property.name);
            final String upperCaseAttrName = GeneratorUtilities.uppercase1stLetter(property.name);
            if (property.isSet ) {
                setProperties.add(property);
            } else  if (property.isList ) {
                listProperties.add(property);
            } else {
                singleProperties.add(property);
            }
        }

        if (line.contains("<$$$")) {
            // read all the lines in the loop
            String loopline = reader.readLine();

            while (loopline != null) {
                //stash the lines for the loop and spit them out for each attribute
                if (loopline.contains("$$$>")) {
                    break;
                }
                loopPropertyLines.add(loopline);
                loopline = reader.readLine();
            }
        } else if (line.contains("<$SetAttr$$")) {
            // read all the lines in the loop
            String loopline = reader.readLine();

            while (loopline != null) {
                //stash the lines for the loop and spit them out for each attribute
                if (loopline.contains("$SetAttr$$>")) {
                    break;
                }
                loopSetAttrLines.add(loopline);
                loopline = reader.readLine();
            }

        } else if (line.contains("<$ListAttr$$")) {
            // read all the lines in the loop
            String loopline = reader.readLine();

            while (loopline != null) {
                //stash the lines for the loop and spit them out for each attribute
                if (loopline.contains("$ListAttr$$>")) {
                    break;
                }
                loopListAttrLines.add(loopline);
                loopline = reader.readLine();
            }
        } else if (line.contains("<$SingleAttr$$")) {
            // read all the lines in the loop
            String loopline = reader.readLine();

            while (loopline != null) {
                //stash the lines for the loop and spit them out for each attribute
                if (loopline.contains("$SingleAttr$$>")) {
                    break;
                }
                loopSingleAttrLines.add(loopline);
                loopline = reader.readLine();
            }
        } else {
            // no loop
            String newLine = line;
            newLine = replaceTokensInLineFromMap(replacementMap, newLine);
            outputFileWriter.write(newLine + "\n");
        }

        // process the $$$ loop for all properties
        if (!loopPropertyLines.isEmpty()) {
            for (OmrsBeanAttribute property : allProperties) {
                final String lowerCaseAttrName = GeneratorUtilities.lowercase1stLetter(property.name);
                final String upperCaseAttrName = GeneratorUtilities.uppercase1stLetter(property.name);
                final String upperCaserelationshipName = GeneratorUtilities.uppercase1stLetter(property.referenceRelationshipName);
                final String lowerCaserelationshipName = GeneratorUtilities.lowercase1stLetter(property.referenceRelationshipName);
                for (int loopCounter = 0; loopCounter < loopPropertyLines.size(); loopCounter++) {
                    String newLine = loopPropertyLines.get(loopCounter);
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("referencePackage"),property.referencePackage);
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("PropertyName"), lowerCaseAttrName);
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("uPropertyName"), upperCaseAttrName);
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("uRelationshipName"), upperCaserelationshipName);
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("RelationshipName"), lowerCaserelationshipName);
                    outputFileWriter.write(newLine + "\n");
                }
            }
        } else if (!loopSetAttrLines.isEmpty()) {
            for (OmrsBeanAttribute property : setProperties) {
                final String lowerCaseAttrName = GeneratorUtilities.lowercase1stLetter(property.name);
                final String upperCaseAttrName = GeneratorUtilities.uppercase1stLetter(property.name);
                final String upperCaserelationshipName = GeneratorUtilities.uppercase1stLetter(property.referenceRelationshipName);
                final String lowerCaserelationshipName = GeneratorUtilities.lowercase1stLetter(property.referenceRelationshipName);

                for (int loopCounter = 0; loopCounter < loopSetAttrLines.size(); loopCounter++) {
                    String newLine = loopSetAttrLines.get(loopCounter);

                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("setReferenceName"), lowerCaseAttrName);
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("uSetReferenceName"), upperCaseAttrName);
                    newLine = replaceTokensInLineFromMap(replacementMap, newLine);
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("PropertyType"), GeneratorUtilities.uppercase1stLetter(property.type));
                    // TODO handle non String types
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("AttrDescription"), property.description);
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("uRelationshipName"), upperCaserelationshipName);
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("RelationshipName"), lowerCaserelationshipName);
                    outputFileWriter.write(newLine + "\n");
                    //outputFileWriter.flush();
                }

            }
        } else  if (!loopListAttrLines.isEmpty()) {
            for (OmrsBeanAttribute property : listProperties) {
                final String lowerCaseAttrName = GeneratorUtilities.lowercase1stLetter(property.name);
                final String upperCaseAttrName = GeneratorUtilities.uppercase1stLetter(property.name);
                final String upperCaserelationshipName = GeneratorUtilities.uppercase1stLetter(property.referenceRelationshipName);
                final String lowerCaserelationshipName = GeneratorUtilities.lowercase1stLetter(property.referenceRelationshipName);

                for (int loopCounter = 0; loopCounter < loopListAttrLines.size(); loopCounter++) {
                    String newLine = loopListAttrLines.get(loopCounter);

                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("listReferenceName"), lowerCaseAttrName);
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("uListReferenceName"), upperCaseAttrName);

                    newLine = replaceTokensInLineFromMap(replacementMap, newLine);
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("PropertyType"), GeneratorUtilities.uppercase1stLetter(property.type));
                    // TODO handle non String types
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("AttrDescription"), property.description);
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("uRelationshipName"), upperCaserelationshipName);
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("RelationshipName"), lowerCaserelationshipName);
                    outputFileWriter.write(newLine + "\n");
                }

            }
        } else if (!loopSingleAttrLines.isEmpty()) {
            for (OmrsBeanAttribute property : singleProperties) {
                final String lowerCaseAttrName = GeneratorUtilities.lowercase1stLetter(property.name);
                final String upperCaseAttrName = GeneratorUtilities.uppercase1stLetter(property.name);
                final String upperCaserelationshipName = GeneratorUtilities.uppercase1stLetter(property.referenceRelationshipName);
                final String lowerCaserelationshipName = GeneratorUtilities.lowercase1stLetter(property.referenceRelationshipName);
                for (int loopCounter = 0; loopCounter < loopSingleAttrLines.size(); loopCounter++) {
                    String newLine = loopSingleAttrLines.get(loopCounter);
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("singleReferenceName"), lowerCaseAttrName);
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("uSingleReferenceName"), upperCaseAttrName);
                    newLine = replaceTokensInLineFromMap(replacementMap, newLine);
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("PropertyType"), GeneratorUtilities.uppercase1stLetter(property.type));
                    // TODO handle non String types
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("AttrDescription"), property.description);
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("uRelationshipName"), upperCaserelationshipName);
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("RelationshipName"), lowerCaserelationshipName);
                    outputFileWriter.write(newLine + "\n");
                }
            }
        }
    }

    public static  String replaceTokensInLineFromMap(Map<String, String> referenceMap, String newLine) {
        if (referenceMap != null) {
            for (String key : referenceMap.keySet()) {
                if (referenceMap.get(key) != null) {
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken(key), referenceMap.get(key));
                }
            }
        }
        return newLine;
    }
    private static  void closeReaderAndFileWriter(FileWriter outputFileWriter, BufferedReader reader) throws IOException {
        if (reader != null) {
            reader.close();
        }
        if (outputFileWriter != null) {
            outputFileWriter.close();
        }
    }

    private static String createReferenceJavaFolderIfRequired(org.odpi.openmetadata.fvt.opentypes.model.OmrsBeanReference rgi) {
        String outputpackage = rgi.myType + "To" + rgi.relatedEndType;
        File newFolder = GeneratorUtilities.writeFolder(GENERATION_REFERENCES_FOLDER + outputpackage + "/");
        return newFolder.getPath();
    }

    private static String createEntityJavaFolderIfRequired(String outputpackage) {
        File newFolder = GeneratorUtilities.writeFolder(GENERATION_ENTITIES_FOLDER + outputpackage + "/");
        return newFolder.getPath();
    }

    private static String createClassificationJavaFolderIfRequired(String outputpackage) {
        File newFolder = GeneratorUtilities.writeFolder(GENERATION_CLASSIFICATIONS_FOLDER + outputpackage + "/");
        return newFolder.getPath();
    }

    private static String createRelationshipJavaFolderIfRequired(String outputpackage) {
        File newFolder = GeneratorUtilities.writeFolder(GENERATION_RELATIONSHIPS_FOLDER + outputpackage + "/");
        return newFolder.getPath();
    }
}
