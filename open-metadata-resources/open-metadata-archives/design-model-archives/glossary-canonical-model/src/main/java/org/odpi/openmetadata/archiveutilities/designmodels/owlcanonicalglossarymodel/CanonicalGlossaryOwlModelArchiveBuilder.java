/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.designmodels.owlcanonicalglossarymodel;


import org.apache.jena.rdf.model.Literal;
import org.odpi.openmetadata.archiveutilities.designmodels.base.DesignModelArchiveBuilder;
import org.odpi.openmetadata.archiveutilities.designmodels.owlcanonicalglossarymodel.properties.GlossaryModel;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;


/**
 * CloudInformationModelArchiveBuilder creates an open metadata archive for the cloud information model.
 * It uses the parser to read the model content into Java Beans.  These java beans are then used by this
 * archive builder to create the open metadata archive.
 * <p>
 * The archive builder needs to ensure it uses the same
 */
class CanonicalGlossaryOwlModelArchiveBuilder extends DesignModelArchiveBuilder {
    /*
     * Specific values for initializing TypeDefs
     */
    private static final String archiveDescription = "Archive produced using the Owl Canonical Glossary Model";
    private static final long versionNumber = 1L;
    private static final String versionName = "1.0";
    private static final Logger log = LoggerFactory.getLogger(CanonicalGlossaryOwlModelArchiveBuilder.class);

    private GlossaryModel model;

    /**
     * Default constructor sets up the archive builder.  This in turn sets up the header for the archive.
     */
    CanonicalGlossaryOwlModelArchiveBuilder(CanonicalGlossaryOwlParser parser) throws Exception {
        super();
        final String methodName = "CanonicalGlossaryOwlModelArchiveBuilder";
        if (parser != null) {
            model = parser.getModel();
            if (model == null) {
                super.logBadArchiveContent(methodName);
                //TODO do proper exception handling
                throw new Exception("no parser model supplied to the archive builder");
            } else {
                // TODO probably need to store this so it is not generated each time.
                String archiveGUID = UUID.randomUUID().toString();
                String archiveName = model.getModelName();
                String archiveDescription = model.getModelDescription();
                OpenMetadataArchiveType archiveType = OpenMetadataArchiveType.CONTENT_PACK;
                String archiveRootName = model.getModelName();
                String originatorName = "...";
                String archiveLicense = model.getLicense();
                // TODO pick up from the model
                Date creationDate = new Date();
                initialize(archiveGUID,
                           archiveName,
                           archiveDescription,
                           archiveType,
                           archiveRootName,
                           originatorName,
                           archiveLicense,
                           creationDate,
                           versionNumber,
                           versionName);
            }
        } else {

            super.logBadArchiveContent(methodName);
            //TODO do proper exception handling
            throw new Exception("no parser supplied to the archive builder");
        }

    }
    CanonicalGlossaryOwlModelArchiveBuilder(CanonicalGlossaryOwlParser parser, boolean writeToFile) throws Exception {
        this(parser);
        this.writeToFile = writeToFile;
    }



    /**
     * Returns the open metadata type archive containing all of the elements extracted from the CIM.
     *
     * @return populated open metadata archive object
     */
    @Override
    protected OpenMetadataArchive getOpenMetadataArchive() {
        Map<String, String> isDefinedByMap = model.getIsDefinedByMap();
        Map<String, Set<String>> conceptTermMemberMap = model.getConceptTermMemberMap();
        Map<String, Set<String>> propertyTermMemberMap = model.getPropertyTermMemberMap();
        Map<String, Set<String>> containmentMemberMap = model.getContainmentMemberMap();
        /*
         * Convert the metadata extracted by the parser into content for the open metadata archive.
         */
        String glossaryId = super.addGlossary(model.getModelTechnicalName(),
                                              model.getModelName(),
                                              model.getModelDescription(),
                                              model.getModelLanguage(),
                                              archiveDescription,
                                              model.getModelLocation(),
                                              model.getModelScope());



        /*
         * Create categories
         */
        Map<String, Map<String, Literal>> categoryLiteralMap = model.getCategoryLiteralMap();
        for (String categoryQName : categoryLiteralMap.keySet()) {
            Map<String, Literal> literalMap = categoryLiteralMap.get(categoryQName);
            if (model.getModelTechnicalName().equals(isDefinedByMap.get(categoryQName))) {
                super.addCategory(glossaryId,
                                  categoryQName,
                                  literalMap.get("displayName").getString(),
                                  literalMap.get("description").getString(),
                                  null);
            } else {
                System.err.println("expected category " + categoryQName + " to be in the isDefined map for glossary " +model.getModelTechnicalName());
            }
        }

        /*
         * Create spine objects
         */
        Map<String, Map<String, Literal>> conceptTermLiteralMap = model.getConceptTermLiteralMap();
        for (String termQName : conceptTermLiteralMap.keySet()) {
            Map<String, Literal> literalMap = conceptTermLiteralMap.get(termQName);
            if (model.getModelTechnicalName().equals(isDefinedByMap.get(termQName))) {
               Set<String> containerNamesSet =  conceptTermMemberMap.get(termQName);
               List<String> containerNamesList = null;
               if (containerNamesSet != null && !containerNamesSet.isEmpty()) {
                   containerNamesList = containerNamesSet.stream().collect(Collectors.toList());
               }


               String termGuid = super.addTerm(glossaryId, containerNamesList, termQName, literalMap.get("displayName").getString(),
                          literalMap.get("description").getString(), null, true, false,true );
                log.debug("Adding concept term name=" +termQName  + " guid="+termGuid);
            } else {
                System.err.println("expected concept term " + termQName + " to be in the isDefined map for glossary " +model.getModelTechnicalName());
            }
        }
        /*
         * Create spine attributes
         */

        Map<String, Map<String, Literal>> objectPropertyTermLiteralMap = model.getObjectPropertyTermLiteralMap();
        for (String termQName : objectPropertyTermLiteralMap.keySet()) {
            Map<String, Literal> literalMap = objectPropertyTermLiteralMap.get(termQName);
            if (model.getModelTechnicalName().equals(isDefinedByMap.get(termQName))) {
                Set<String> containerNamesSet =  propertyTermMemberMap.get(termQName);
                List<String> containerNamesList = null;

                if (containerNamesSet != null && !containerNamesSet.isEmpty()) {
                    containerNamesList = containerNamesSet.stream().collect(Collectors.toList());
                }
               String termGuid = super.addTerm(glossaryId,  containerNamesList , termQName, literalMap.get("displayName").getString(),
                          literalMap.get("description").getString(), null, false, true,true);
                log.debug("Adding property term name=" +termQName  + " guid="+termGuid);

                // if this has a range then create related term relationship
            } else {
                System.err.println("expected property term " + termQName + " to be in the isDefined map for glossary " +model.getModelTechnicalName());
            }
        }

        Map<String, Map<String, Literal>> datatypePropertyTermLiteralMap = model.getDatatypePropertyTermLiteralMap();
        for (String termQName : datatypePropertyTermLiteralMap.keySet()) {
            Map<String, Literal> literalMap =  datatypePropertyTermLiteralMap.get(termQName);
            if (model.getModelTechnicalName().equals(isDefinedByMap.get(termQName))) {
                Set<String> containerNamesSet =  propertyTermMemberMap.get(termQName);
                List<String> containerNamesList = null;

                if (containerNamesSet != null && !containerNamesSet.isEmpty()) {
                    containerNamesList = containerNamesSet.stream().collect(Collectors.toList());
                }
                String exampleValue = null;
                Set<String> examplesSet =model.getExampleMap().get(termQName);
                if (examplesSet !=null && !examplesSet.isEmpty()) {
                    StringBuffer exampleValueSB = new StringBuffer();
                    for (String example : examplesSet) {
                        // concatinate the examples
                        exampleValueSB.append(example);
                        exampleValueSB.append("\r\n");
                    }
                    exampleValue = exampleValueSB.toString();
                }
                String termGuid = super.addTerm(glossaryId,  containerNamesList , termQName, literalMap.get("displayName").getString(),
                                                literalMap.get("description").getString(), exampleValue, false, true,true);
                log.debug("Adding property term name=" +termQName  + " guid="+termGuid);

                // if this has a range then create related term relationship
            } else {
                System.err.println("expected property term " + termQName + " to be in the isDefined map for glossary " +model.getModelTechnicalName());
            }
        }
        // process has-a relationships
        Map<String, Set<String>> hasaMap = model.getDomainsMap();
        for (String nameOfProperty : hasaMap.keySet()) {
            Set<String> conceptSet = hasaMap.get(nameOfProperty);
            for (String nameOfConcept: conceptSet) {
                log.debug("Adding HAS-A nameOfConcept=" + nameOfConcept + " propertyName="+nameOfProperty);
                super.addHasARelationship(nameOfProperty,nameOfConcept);
            }
        }
        // process related term relationships
        Map<String, Set<String>> relatedTermMap = model.getRangesMap();
        for (String nameOfProperty : relatedTermMap.keySet()) {
            Set<String> conceptSet = relatedTermMap.get(nameOfProperty);
            for (String nameOfConcept: conceptSet) {
                /*
                 * the ranges we are concerned with are those that have URIs pointing to classes.
                 * this check ensure we do not pick up xsd content . Note there are more than one xsd identifier used produced on
                 * different dates, so we are just checking the root URI.
                 */
                if (!nameOfConcept.startsWith("http://www.w3.org")) {
                    log.debug("Adding related nameOfConcept=" + nameOfConcept + " propertyName=" + nameOfProperty);
                    super.addRelatedTermRelationship(nameOfProperty, nameOfConcept);
                }
            }
        }

        Map<String, String> resourceSubClassMap = model.getResourceSubClassMap();
        for (String subclass : resourceSubClassMap.keySet()) {
            super.addISARelationship(subclass, resourceSubClassMap.get(subclass));
        }
        for (String containerName : containmentMemberMap.keySet()) {
            super.addCategoryHierarchy(containerName, containmentMemberMap.get(containerName));
        }

        /*
         * Retrieve the assembled archive content.
         */
        return super.getOpenMetadataArchive();
    }

}