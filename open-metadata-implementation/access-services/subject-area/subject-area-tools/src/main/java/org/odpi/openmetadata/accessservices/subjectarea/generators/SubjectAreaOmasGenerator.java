/**
 * Licensed to the Apache Software Foundation (ASF) under oneØ
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.odpi.openmetadata.accessservices.subjectarea.generators;

import org.odpi.openmetadata.accessservices.subjectarea.model.*;
import org.odpi.openmetadata.accessservices.subjectarea.utils.GeneratorUtilities;

import java.io.*;
import java.util.*;


public class SubjectAreaOmasGenerator {

    // This generator needs to be run in the top level atlas folder - i.e. the folder you run git commands in.

    public static final String SUBJECTAREA_OMAS_SERVER = "open-metadata-implementation/access-services/subject-area/subject-area-server";
    public static final String GENERATOR = "open-metadata-implementation/access-services/subject-area/subject-area-tools";

    public static final String CLASSIFICATIONS = "classifications";
    public static final String RELATIONSHIPS = "relationships";
    public static final String REFERENCES = "references";
    public static final String ENTITIES = "entities";
    public static final String ENUMS = "enums";

    public static final String GENERATION_CLASSIFICATION_FACTORY_CLASS_NAME = "ClassificationFactory.java";

    public static final String TEMPLATES_FOLDER = GENERATOR + "/src/main/resources/templates/";
    public static final String GENERATION_FOLDER = SUBJECTAREA_OMAS_SERVER + "/src/main/java/org/odpi/openmetadata/accessservices/subjectarea/generated";
    public static final String GENERATION_REST_FOLDER = GENERATION_FOLDER + "/server/";
    public static final String GENERATION_REST_TEST_FOLDER = SUBJECTAREA_OMAS_SERVER + "/src/test/java/org/odpi/openmetadata/accessservices/subjectarea/generated/server/";
    public static final String GENERATION_REST_CLASS_NAME = "SubjectAreaOmasREST.java";

    public static final String GENERATION_REST_FILE = GENERATION_REST_FOLDER + GENERATION_REST_CLASS_NAME;
    public static final String GENERATION_REST_TEST_FILE = GENERATION_REST_TEST_FOLDER + "Test" + GENERATION_REST_CLASS_NAME;

    public static final String GENERATION_REFERENCES_FOLDER = GENERATION_FOLDER + "/" + REFERENCES + "/";
    public static final String GENERATION_ENTITIES_FOLDER = GENERATION_FOLDER + "/" + ENTITIES + "/";
    public static final String GENERATION_ENUMS_FOLDER = GENERATION_FOLDER + "/" + ENUMS + "/";
    public static final String GENERATION_CLASSIFICATIONS_FOLDER = GENERATION_FOLDER + "/" + CLASSIFICATIONS + "/";
    public static final String GENERATION_RELATIONSHIPS_FOLDER = GENERATION_FOLDER + "/" + RELATIONSHIPS + "/";

    public static final String GENERATION_CLASSIFICATION_FACTORY_FILE_NAME = GENERATION_CLASSIFICATIONS_FOLDER + GENERATION_CLASSIFICATION_FACTORY_CLASS_NAME;

    //template names
    // this is the template for a Reference - i.e. something that contains the relationship attributes and is our view of the relationship
    public static final String OMAS_REFERENCE_TEMPLATE = TEMPLATES_FOLDER + "OmasReferenceTemplate";
    // this is the template to be able to create the top level omas entity e.g. Glossary
    public static final String OMAS_ENTITY_TEMPLATE = TEMPLATES_FOLDER + "OmasEntityTemplate";
    // this is the template to be able to create classifications
    public static final String OMAS_CLASSIFICATION_TEMPLATE = TEMPLATES_FOLDER + "OmasClassificationTemplate";
    // this is the template to be able to create relationship mappings
    public static final String OMAS_RELATIONSHIP_MAPPER_TEMPLATE = TEMPLATES_FOLDER + "OmasRelationshipMapperTemplate";
    // this is the template to be able to create relationships
    public static final String OMAS_RELATIONSHIP_TEMPLATE = TEMPLATES_FOLDER + "OmasRelationshipTemplate";
    // ths is the template to be able to create an omas enum
    public static final String OMAS_ENUM_TEMPLATE = TEMPLATES_FOLDER + "OmasEnumTemplate";

    // Entity mapper
    public static final String OMAS_ENTITY_MAPPER_TEMPLATE = TEMPLATES_FOLDER + "OmasEntityMapperTemplate";
    // this is the top level references e.g. TermReferences
    public static final String OMAS_TOP_REFERENCE_TEMPLATE = TEMPLATES_FOLDER + "OmasTopReferenceTemplate";
    // this is the rest file template
    public static final String OMAS_REST_TEMPLATE = TEMPLATES_FOLDER + "OmasRestTemplate";

    public static final String OMAS_REST_TEST_TEMPLATE = TEMPLATES_FOLDER + "TestOmasRestTemplate";

    public static final String OMAS_CLASSIFICATION_FACTORY_TEMPLATE = TEMPLATES_FOLDER + "OmasClassificationFactoryTemplate";

    // the omas model is a typed version of omrs - prepared for use by this omas
    private SubjectAreaOmasModel subjectAreaOmasModel;

    public SubjectAreaOmasGenerator(SubjectAreaOmasModel subjectAreaOmasModel) {
        this.subjectAreaOmasModel = subjectAreaOmasModel;
    }

    /**
     * this can be run as a java app during development or as part of the maven build
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // make sure we have the generation folders
        initializeFoldersandFiles();
        // run the generation
        generate(args);
    }

    private static void initializeFoldersandFiles() throws FileNotFoundException {
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

    }

    /**
     * This generation creates 8 types of files:
     * <p>
     * All the files are under the generation folder in SubjectAreaOmas
     * Under the generation folder there are sub folders for different resources. The rest API is generated in the web app project.
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
     * - Rest API file
     *
     * @param args
     * @throws IOException
     */
    public static void generate(String[] args) throws IOException {
        SubjectAreaOmasModel subjectAreaOmasModel = new SubjectAreaOmasModel();

        SubjectAreaOmasGenerator generator = new SubjectAreaOmasGenerator(subjectAreaOmasModel);

        // generate the enum files
        generator.generateEnumFiles();
        // generate the classification files and their attributes
        generator.generateClassificationFiles();
        generator.generateClassificationFactory();
        // generate the entity and attribute files
        generator.generateEntityRelatedFiles(subjectAreaOmasModel.getOmasEntityAttributeMap());
        // generate relationships and their attributes
        generator.generateRelationshipFiles();
        // generate the reference files
        generator.generateReferenceFiles();
        // generate the rest file
        generator.generateRestFile();
        // generate the rest test file
        generator.generateRestTestFile();
    }

    private void generateEnumFiles() throws IOException {

        Map<String, List<OmasEnumValue>> enumsMap = subjectAreaOmasModel.getEnumsMap();
        for (String enumName : enumsMap.keySet()) {
            final String enumFileName = GENERATION_ENUMS_FOLDER + "/" + enumName + ".java";
            generateEnumFile(enumName, enumsMap.get(enumName), enumFileName);
        }
    }

    private void generateClassificationFiles() throws IOException {
        Map<String, List<OmasAttribute>> omasAttributeMap = subjectAreaOmasModel.getOmasClassificationAttributesMap();
        for (String classificationName : omasAttributeMap.keySet()) {
            if (classificationName.equals("Confidentiality")) {
                int a = 10;
            }
            String outputFolder = this.createClassificationJavaFolderIfRequired(classificationName);
            final String classificationFileName = outputFolder + "/" + classificationName + ".java";
            generateClassificationFile(classificationName, classificationFileName);
        }
    }

    private void generateClassificationFactory() throws IOException {
        FileWriter outputFileWriter = new FileWriter(GENERATION_CLASSIFICATION_FACTORY_FILE_NAME);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(OMAS_CLASSIFICATION_FACTORY_TEMPLATE));
            String line = reader.readLine();
            while (line != null) {
                List<String> loopLines = new ArrayList();

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
                    for (String classificationName : subjectAreaOmasModel.getOmasClassificationAttributesMap().keySet()) {
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
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        outputFileWriter.close();

    }


    private void generateRelationshipFiles() throws IOException {
        Map<String, List<OmasAttribute>> omasAttributeMap = subjectAreaOmasModel.getOmasRelationshipAttributeMap();
        for (String relationshipName : omasAttributeMap.keySet()) {
            // upper case the first letter.
            relationshipName = GeneratorUtilities.uppercase1stLetter(relationshipName);
            String outputFolder = this.createRelationshipJavaFolderIfRequired(relationshipName);
            final String relationshipFileName = outputFolder + "/" + relationshipName + ".java";
            OmasRelationship omasRelationship = subjectAreaOmasModel.getOmasRelationshipByName(relationshipName);
            generateRelationshipFile(omasRelationship, relationshipFileName);
            final String relationshipMapperFileName = outputFolder + "/" + relationshipName + "Mapper.java";
            generateRelationshipMapperFile(omasRelationship, relationshipMapperFileName);
        }
    }

    private void generateEntityRelatedFiles(Map<String, List<OmasAttribute>> omasAttributeMap) throws IOException {
        for (String entityName : omasAttributeMap.keySet()) {

            // do not generate internal entities
            String outputFolder = this.createEntityJavaFolderIfRequired(entityName);
            final String entityFileName = outputFolder + "/" + entityName + ".java";
            generateEntityFile(entityName, entityFileName);
            final String mapperFileName = outputFolder + "/" + entityName + "Mapper.java";
            generateMapperFile(entityName, mapperFileName);
        }
    }

    private void generateTopReferenceFile(String entityName, List<org.odpi.openmetadata.accessservices.subjectarea.model.OmasAttribute> omasAttributes, String topReferenceFileName) throws IOException {
        FileWriter outputFileWriter = new FileWriter(topReferenceFileName);
        System.err.println("DEBUG generateTopReferenceFile(String entityName" + entityName);

        if (GeneratorUtilities.uppercase1stLetter(entityName).equals("LicenseType")){
            int t=11;
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(OMAS_TOP_REFERENCE_TEMPLATE));
            String line = reader.readLine();
            while (line != null) {
                Map<String, String> replacementMap = new HashMap();
                replacementMap.put("package", ENTITIES + "." + entityName);
                replacementMap.put("name", org.odpi.openmetadata.accessservices.subjectarea.utils.GeneratorUtilities.lowercase1stLetter(entityName));
                replacementMap.put("uname", GeneratorUtilities.uppercase1stLetter(entityName));
                System.err.println("generateTopReferenceFile ::About to call  writeTopReferenceAttributesToFile for name "+entityName);
                writeTopReferenceAttributesToFile(omasAttributes, replacementMap, outputFileWriter, reader, line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputFileWriter.close();

    }

    private void generateEnumFile(String enumName, List<OmasEnumValue> enumValues, String enumFileName) throws IOException {
        FileWriter outputFileWriter = new FileWriter(enumFileName);

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(OMAS_ENUM_TEMPLATE));
            String line = reader.readLine();
            while (line != null) {
                Map<String, String> replacementMap = new HashMap();
                replacementMap.put("description", this.subjectAreaOmasModel.getTypeDefDescription(GeneratorUtilities.uppercase1stLetter(enumName)));
                replacementMap.put("package", "enums." + enumName);
                replacementMap.put("name", org.odpi.openmetadata.accessservices.subjectarea.utils.GeneratorUtilities.lowercase1stLetter(enumName));
                replacementMap.put("uname", GeneratorUtilities.uppercase1stLetter(enumName));
                //String enumName, List<OmasEnumValue> enumValues, Map<String, String> replacementMap,
                writeEnumToFile(enumName, enumValues, replacementMap, outputFileWriter, reader, line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputFileWriter.close();

    }

    private void generateMapperFile(String entityName, String fileName) throws IOException {
        FileWriter outputFileWriter = new FileWriter(fileName);
        List<String> loopEntityLines = new ArrayList();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(OMAS_ENTITY_MAPPER_TEMPLATE));
            String line = reader.readLine();
            while (line != null) {
                loopEntityLines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> replacementMap = new HashMap();

        replacementMap.put("package", ENTITIES + "." + entityName);
        mapOMRSToOMAS("Entity", replacementMap, outputFileWriter, loopEntityLines, entityName);
        outputFileWriter.close();

    }

    private void generateRestFile() throws IOException {

        FileWriter outputFileWriter = new FileWriter(GENERATION_REST_FILE);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(OMAS_REST_TEMPLATE));
            String line = reader.readLine();
            while (line != null) {
                Map<String, String> replacementMap = new HashMap();
                writeRestFile(replacementMap, outputFileWriter, reader, line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputFileWriter.close();
    }

    private void generateRestTestFile() throws IOException {
        // for each entity create a test file
        FileWriter outputFileWriter = new FileWriter(GENERATION_REST_TEST_FILE);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(OMAS_REST_TEST_TEMPLATE));
            String line = reader.readLine();
            while (line != null) {
                Map<String, String> replacementMap = new HashMap();
                writeRestFile(replacementMap, outputFileWriter, reader, line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputFileWriter.close();
    }

    /**
     * Write out the line from the template.
     * <p>
     * <p>
     * <p>
     * This method also has logic to loop through lines associated with attributes
     *
     * @param replacementMap
     * @param outputFileWriter
     * @param reader
     * @param line
     * @throws IOException
     */
    private void writeRestFile(Map<String, String> replacementMap, FileWriter outputFileWriter, BufferedReader reader, String line) throws IOException {
        Set<String> allEntities = subjectAreaOmasModel.getOmasEntityAttributeMap().keySet();
        List<String> loopEntityLines = new ArrayList();
        List<String> loopRelationshipLines = new ArrayList();

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
            Map<String, List<OmasAttribute>> omasAttributeMap = subjectAreaOmasModel.getOmasRelationshipAttributeMap();
            for (String relationshipName : omasAttributeMap.keySet()) {
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
        List<OmasAttribute> allOmasAttributes = null;
        if (omrsType.equals("Entity")) {
            allOmasAttributes = subjectAreaOmasModel.getOmasEntityAttributeMap().get(uppercaseArtifact);
        } else {
            allOmasAttributes = subjectAreaOmasModel.getOmasRelationshipAttributeMap().get(uppercaseArtifact);
        }
        List<OmasAttribute> omasAttributes = new ArrayList<OmasAttribute>();
        List<OmasAttribute> omasEnumAttributes = new ArrayList<OmasAttribute>();
        List<OmasAttribute> omasMapAttributes = new ArrayList<OmasAttribute>();
        if (allOmasAttributes != null) {
            for (OmasAttribute omasAttribute : allOmasAttributes) {
                if (omasAttribute.isEnum) {
                    omasEnumAttributes.add(omasAttribute);
                } else if (omasAttribute.isMap) {
                    omasMapAttributes.add(omasAttribute);
                } else {
                    omasAttributes.add(omasAttribute);
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
                OmasRelationship omasRelationship = subjectAreaOmasModel.getOmasRelationshipByName(uppercaseArtifact);

                newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("uRelationshipEntity1"), GeneratorUtilities.uppercase1stLetter(omasRelationship.entityProxy1Name));
                newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("uRelationshipEntity2"), GeneratorUtilities.uppercase1stLetter(omasRelationship.entityProxy2Name));
                newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("uRelationshipEntityType1"), GeneratorUtilities.uppercase1stLetter(omasRelationship.entityProxy1Type));
                newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("uRelationshipEntityType2"), GeneratorUtilities.uppercase1stLetter(omasRelationship.entityProxy2Type));
            }
            //If the line is the governance action one then put out the contents if we need to.
            boolean ignoreLine = false;
            // do not expose Create update or delete rest calls for assets or the relationship to assets.
            if (newLine.contains("@RequestMapping(method = RequestMethod.POST")) {
                if (!allowRest(newLine)) {
                    // bump to the next line
                    ignoreLine = true;

                }

            } else if (newLine.contains("@RequestMapping(method = RequestMethod.PUT")) {
                if (!allowRest(newLine)) {
                    // bump to the next line
                    ignoreLine = true;

                }
            } else if (newLine.contains("@RequestMapping(method = RequestMethod.DELETE")) {
                if (!allowRest(newLine)) {
                    // bump to the next line
                    ignoreLine = true;
                }
            }

            if (ignoreLine) {
                outputFileWriter.write("// Not required as a rest API for the Subject Area OMAS. \n");
            } else {
                if (newLine.contains("<$GovAction$$")) {
                    while (newLine != null) {
                        // bump to the next line
                        loopArtifactCounter++;
                        newLine = loopArtifactLines.get(loopArtifactCounter);
                        if (newLine.contains("$GovAction$$>")) {
                            loopArtifactCounter++;
                            newLine = loopArtifactLines.get(loopArtifactCounter);
                            break;
                        }
                        if (omrsType.equals("Entity") && GeneratorUtilities.isTopLevelGlossaryObject(uppercaseArtifact)) {
                            newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("EntityName"), lowercaseArtifact);
                            outputFileWriter.write(newLine + "\n");
                        }
                    }
                }
                if (newLine.contains("<$$Attr$$")) {
                    loopArtifactCounter++;
                    // we have attribute code that we need to repeat
                    List<String> loopAttrLines = new ArrayList();
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
                    if (!omasAttributes.isEmpty()) {
                        //uAttrType
                        for (int attributeNumber = 0; attributeNumber < omasAttributes.size(); attributeNumber++) {
                            OmasAttribute attribute = omasAttributes.get(attributeNumber);

                            if (attribute.name.toUpperCase().equals("MEDIAUSAGE")){
                                int a=0;
                            }

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

                                        //TODO do other types date and int and array are used in the json models
                                        String attrValue = "";
                                        if (uppercaseattributeType.equals("STRING")) {
                                            attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("AttrType"), "String");

                                            attrValue = "\"string" + attributeNumber + "\"";
                                        } else if (uppercaseattributeType.equals("DATE")) {
                                            attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("AttrType"), "java.util.Date");
                                            attrValue = "new java.util.Date()";
                                        } else if (uppercaseattributeType.equals("INT")) {
                                            attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("AttrType"), "Integer");
                                            attrValue = "new Integer(" + attributeNumber + ")";
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
                    List<String> loopAttrLines = new ArrayList();
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
                    if (!omasAttributes.isEmpty()) {
                        //uAttrType
                        for (int attributeNumber = 0; attributeNumber < omasAttributes.size(); attributeNumber++) {
                            OmasAttribute attribute = omasAttributes.get(attributeNumber);
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
                                    } else if (uppercaseattributeType.equals("DATE")) {
                                        attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("AttrType"), "java.util.Date");
                                        attrValue = "new java.util.Date()";
                                    } else if (uppercaseattributeType.equals("INT")) {
                                        attrLine = attrLine.replaceAll(GeneratorUtilities.getRegexToken("AttrType"), "Integer");
                                        attrValue = "new Integer(" + attributeNumber + ")";
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
                    List<String> loopEnumLines = new ArrayList();
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

                    if (!omasEnumAttributes.isEmpty()) {
                        for (OmasAttribute enumAttribute : omasEnumAttributes) {
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
                    List<String> loopMapLines = new ArrayList();
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

                    if (!omasMapAttributes.isEmpty()) {
                        for (OmasAttribute mapAttribute : omasMapAttributes) {
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
                    List<String> loopRefLines = new ArrayList();
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

                    List<OmasAttribute> omasReferences = subjectAreaOmasModel.getOmasReferencesAsAttributesByEntity().get(omrsArtifactName);
                    if (omasReferences != null) {
                        for (OmasAttribute reference : omasReferences) {
                            if (!reference.isList) {
                                System.err.println("Mapper reference not list " + reference.name);
                                if (reference.name.equals("knownLocations")) {
                                    int o = 0;
                                }
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
                    List<String> loopRefLines = new ArrayList();
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

                    List<OmasAttribute> omasReferences = subjectAreaOmasModel.getOmasReferencesAsAttributesByEntity().get(omrsArtifactName);
                    if (omasReferences != null) {
                        for (OmasAttribute reference : omasReferences) {
                            if (reference.isList) {
                                System.err.println("Mapper reference list" + reference.name);
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
            }  // end if (ignoreLine)
        } // end for loop
    }

    private boolean allowRest(String newLine) {
        boolean allowRest = true;
        if (newLine.contains("/relationships/"))  {
            if ( newLine.contains("/semanticAssignments")) {
                allowRest =false;
            }
        } else {
            if ( newLine.contains("/assets")) {
                allowRest =false;
            }
        }
        return allowRest;
    }

    private void generateEntityFile(String entityName, String fileName) throws IOException {

        FileWriter outputFileWriter = new FileWriter(fileName);
        String uEntityName = GeneratorUtilities.uppercase1stLetter(entityName);
        //ensure entityName is lower case
        entityName = GeneratorUtilities.lowercase1stLetter(entityName);


        List<OmasAttribute> attributeMap = subjectAreaOmasModel.getOmasEntityAttributeMap().get(uEntityName);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(OMAS_ENTITY_TEMPLATE));
            String line = reader.readLine();
            while (line != null) {
                Map<String, String> replacementMap = new HashMap();
                replacementMap.put("uname", uEntityName);
                replacementMap.put("name", entityName);
                replacementMap.put("description", subjectAreaOmasModel.getTypeDefDescription(uEntityName));
                replacementMap.put("package", "entities." + uEntityName);
                //If the line is the governance action one then put out the contents if we need to.
                if (line.contains("<$GovAction$$")) {
                    while (line != null) {
                        // bump to the next line
                        line = reader.readLine();
                        if (line.contains("$GovAction$$>")) {
                            line = reader.readLine();
                            break;
                        }
                        if (GeneratorUtilities.isTopLevelGlossaryObject(uEntityName)) {
                            outputFileWriter.write(line + "\n");
                        }
                    }
                }
                writeAttributesToFile(attributeMap, replacementMap, outputFileWriter, reader, line);
                line = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        outputFileWriter.close();
    }

    private void generateClassificationFile(String classificationName, String fileName) throws IOException {

        FileWriter outputFileWriter = new FileWriter(fileName);
        BufferedReader reader;
        classificationName = GeneratorUtilities.lowercase1stLetter(classificationName);
        String uClassificationName = GeneratorUtilities.uppercase1stLetter(classificationName);


        List<OmasAttribute> attrList = subjectAreaOmasModel.getOmasClassificationAttributesMap().get(uClassificationName);
        try {
            reader = new BufferedReader(new FileReader(OMAS_CLASSIFICATION_TEMPLATE));
            String line = reader.readLine();
            while (line != null) {
                Map<String, String> replacementMap = new HashMap();

                replacementMap.put("uname", uClassificationName);
                replacementMap.put("name",classificationName);
                replacementMap.put("description", subjectAreaOmasModel.getTypeDefDescription(uClassificationName));

                writeAttributesToFile(attrList, replacementMap, outputFileWriter, reader, line);
                line = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        outputFileWriter.close();
    }

    private void generateRelationshipFile(OmasRelationship omasRelationship, String fileName) throws IOException {

        FileWriter outputFileWriter = new FileWriter(fileName);
        String relationshipName = omasRelationship.label;
        relationshipName = GeneratorUtilities.lowercase1stLetter(relationshipName);
        String uRelationshipName = GeneratorUtilities.uppercase1stLetter(relationshipName);


        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(OMAS_RELATIONSHIP_TEMPLATE));
            String line = reader.readLine();
            while (line != null) {
                Map<String, String> replacementMap = new HashMap();
                List<OmasAttribute> attrList = subjectAreaOmasModel.getOmasRelationshipAttributeMap().get(uRelationshipName);
                replacementMap.put("uname", uRelationshipName);
                replacementMap.put("description", this.subjectAreaOmasModel.getTypeDefDescription(GeneratorUtilities.uppercase1stLetter(GeneratorUtilities.uppercase1stLetter(relationshipName))));

                replacementMap.put("name", relationshipName);
                replacementMap.put("entityProxy1Name", omasRelationship.entityProxy1Name);
                replacementMap.put("entityProxy1Type", omasRelationship.entityProxy1Type);
                replacementMap.put("entityProxy2Name", omasRelationship.entityProxy2Name);
                replacementMap.put("entityProxy2Type", omasRelationship.entityProxy2Type);

                writeAttributesToFile(attrList, replacementMap, outputFileWriter, reader, line);
                line = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        outputFileWriter.close();
    }

    private void generateRelationshipMapperFile(OmasRelationship omasRelationship, String fileName) throws IOException {

        FileWriter outputFileWriter = new FileWriter(fileName);
        String label = omasRelationship.label;
        BufferedReader reader;
        Map<String, String> replacementMap = new HashMap();
        List<String> loopRelationshipLines = new ArrayList();
        try {
            reader = new BufferedReader(new FileReader(OMAS_RELATIONSHIP_MAPPER_TEMPLATE));

            String line = reader.readLine();
            while (line != null) {
                replacementMap.put("uname", GeneratorUtilities.uppercase1stLetter(label));
                replacementMap.put("name", GeneratorUtilities.lowercase1stLetter(label));
                loopRelationshipLines.add(line);
                line = reader.readLine();
            }
            reader.close();
            mapOMRSToOMAS("Relationship", replacementMap, outputFileWriter, loopRelationshipLines, label);
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputFileWriter.close();
    }

    private void generateReferenceFiles() throws IOException {
        List<OmasReference> omasReferences = subjectAreaOmasModel.getOmasReferences();
        for (OmasReference omasReference : omasReferences) {

            // for each omasReference we need to create a java file in the right folder.
            String outputFolder = this.createReferenceJavaFolderIfRequired(omasReference);
            final String fileName = outputFolder + "/" + omasReference.uReferenceName + "Reference.java";
            FileWriter outputFileWriter = new FileWriter(fileName);
            BufferedReader reader;

            reader = new BufferedReader(new FileReader(OMAS_REFERENCE_TEMPLATE));
            String line = reader.readLine();
            if (omasReference.myType.equals("LicenseType")) {
                int a=8;
            }

            while (line != null) {
                List<OmasAttribute> attrList = omasReference.attrList;
                Map<String, String> replacementMap = new HashMap();
                replacementMap.put("uname", omasReference.uReferenceName);
                replacementMap.put("mytype", omasReference.myType);

                final String refPackage = GeneratorUtilities.getReferencePackage(omasReference.myType, omasReference.relatedEndType);
                replacementMap.put("refpackage", refPackage);
                replacementMap.put("name", omasReference.referenceName);
                replacementMap.put("relationshiptype", omasReference.relationshipType);
                replacementMap.put("relatedendtype",  GeneratorUtilities.lowercase1stLetter(omasReference.relatedEndType));
                replacementMap.put("urelatedendtype",  GeneratorUtilities.uppercase1stLetter(omasReference.relatedEndType));
                replacementMap.put("AttrDescription", omasReference.description);
                writeAttributesToFile(attrList, replacementMap, outputFileWriter, reader, line);
                line = reader.readLine();
            }
            reader.close();
            outputFileWriter.close();
        }
        //generate the top references file
        Map<String, List<OmasAttribute>> omasReferencesAsAttributesByEntity = subjectAreaOmasModel.getOmasReferencesAsAttributesByEntity();

        final Set<String> entitiesWithRelationships = omasReferencesAsAttributesByEntity.keySet();
        for (String entityName : entitiesWithRelationships) {

            String outputFolder = this.createEntityJavaFolderIfRequired(entityName);
            final String topReferenceFileName = outputFolder + "/" + entityName + "References.java";
            final List<OmasAttribute> omasAttributes = omasReferencesAsAttributesByEntity.get(entityName);
            if (org.odpi.openmetadata.accessservices.subjectarea.utils.GeneratorUtilities.lowercase1stLetter(entityName).equals("GlossaryTerm")) {
                int p= 0;
            }
            generateTopReferenceFile(entityName, omasAttributes, topReferenceFileName);
        }
    }

    private void writeEnumToFile(String enumName, List<OmasEnumValue> enumValues, Map<String, String> replacementMap, FileWriter outputFileWriter, BufferedReader reader, String line) throws IOException {
        if (line.contains("<$$$")) {
            // read all the lines in the loop
            String loopline = reader.readLine();
            List<String> loopLines = new ArrayList();
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
                OmasEnumValue omasEnumValue = enumValues.get(i);
                String enumValue = omasEnumValue.name;
                String enumDescription = omasEnumValue.description;
                for (int loopCounter = 0; loopCounter < loopLines.size(); loopCounter++) {
                    String newLine = loopLines.get(loopCounter);
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("enum"), enumValue);
                    if (enumDescription == null) {
                        // enum values may not have descriptions - we need to tolerate this
                        enumDescription = "Enumeration value for " + enumValue;
                    }
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("enumdescription"), enumDescription);

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

    private void writeAttributesToFile(List<OmasAttribute> attrList, Map<String, String> replacementMap, FileWriter outputFileWriter, BufferedReader reader, String line) throws IOException {
        List<String> loopPropertyLines = new ArrayList();
        List<String> loopAttrLines = new ArrayList();
        List<String> loopEnumLines = new ArrayList();
        List<String> loopMapLines = new ArrayList();
        List<String> loopRelationshipLines = new ArrayList();

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
            for (OmasAttribute attr : attrList) {
                if (!loopPropertyLines.isEmpty()) {

                    for (int loopCounter = 0; loopCounter < loopPropertyLines.size(); loopCounter++) {
                        String newLine = loopPropertyLines.get(loopCounter);
                        newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("PropertyName"), GeneratorUtilities.lowercase1stLetter(attr.name));
                        newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("uPropertyName"), GeneratorUtilities.uppercase1stLetter(attr.name));
                        newLine = replaceTokensInLineFromMap(replacementMap, newLine);
                        newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken("PropertyType"), GeneratorUtilities.uppercase1stLetter(attr.type));
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
                        if (!(attr.isEnum || attr.isMap)) {
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
    private void writeTopReferenceAttributesToFile(List<OmasAttribute> allProperties, Map<String, String> replacementMap, FileWriter outputFileWriter, BufferedReader reader, String line) throws IOException {
        List<String> loopPropertyLines = new ArrayList();
        List<String> loopSetAttrLines = new ArrayList();
        List<String> loopListAttrLines = new ArrayList();
        List<String> loopSingleAttrLines = new ArrayList();

        List<OmasAttribute> setProperties = new ArrayList();
        List<OmasAttribute> listProperties = new ArrayList();
        List<OmasAttribute> singleProperties = new ArrayList();

        // split out the properties
        for (OmasAttribute property : allProperties) {

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
            if (newLine.toUpperCase().contains("COMMENTS;")) {
                System.err.println("newLine is :" + newLine);
            }
            outputFileWriter.write(newLine + "\n");
        }

        // process the $$$ loop for all properties
        if (!loopPropertyLines.isEmpty()) {
            for (OmasAttribute property : allProperties) {
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
                    if (newLine.toUpperCase().contains("COMMENTS;")) {
                        System.err.println("newLine is :" + newLine);
                    }
                    outputFileWriter.write(newLine + "\n");
                }
            }
        } else if (!loopSetAttrLines.isEmpty()) {
            for (OmasAttribute property : setProperties) {
                final String lowerCaseAttrName = GeneratorUtilities.lowercase1stLetter(property.name);
                final String upperCaseAttrName = GeneratorUtilities.uppercase1stLetter(property.name);
                final String upperCaserelationshipName = GeneratorUtilities.uppercase1stLetter(property.referenceRelationshipName);
                final String lowerCaserelationshipName = GeneratorUtilities.lowercase1stLetter(property.referenceRelationshipName);
                if (  upperCaseAttrName.toUpperCase().startsWith("COMMENT")) {
                    int o=0;
                }

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
            for (OmasAttribute property : listProperties) {
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
            for (OmasAttribute property : singleProperties) {
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

    private String replaceTokensInLineFromMap(Map<String, String> referenceMap, String newLine) {
        if (referenceMap != null) {
            for (String key : referenceMap.keySet()) {
                if (referenceMap.get(key) != null) {
                    newLine = newLine.replaceAll(GeneratorUtilities.getRegexToken(key), referenceMap.get(key));
                }
            }
        }
        return newLine;
    }

    private String createReferenceJavaFolderIfRequired(OmasReference rgi) {
        String outputpackage = rgi.myType + "To" + rgi.relatedEndType;
        File newFolder = GeneratorUtilities.writeFolder(GENERATION_REFERENCES_FOLDER + outputpackage + "/");
        return newFolder.getPath();
    }

    private String createEntityJavaFolderIfRequired(String outputpackage) {
        File newFolder = GeneratorUtilities.writeFolder(GENERATION_ENTITIES_FOLDER + outputpackage + "/");
        return newFolder.getPath();
    }

    private String createClassificationJavaFolderIfRequired(String outputpackage) {
        File newFolder = GeneratorUtilities.writeFolder(GENERATION_CLASSIFICATIONS_FOLDER + outputpackage + "/");
        return newFolder.getPath();
    }

    private String createRelationshipJavaFolderIfRequired(String outputpackage) {
        File newFolder = GeneratorUtilities.writeFolder(GENERATION_RELATIONSHIPS_FOLDER + outputpackage + "/");
        return newFolder.getPath();
    }
}
