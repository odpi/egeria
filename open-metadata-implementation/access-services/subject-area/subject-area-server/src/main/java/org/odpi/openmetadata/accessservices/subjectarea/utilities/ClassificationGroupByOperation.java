/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.utilities;

import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * A list of exiting classifications are compared with a list of requested Classifications, and the classifications are grouped into whether they
 * need to be added (if they are requested but do not exist), removed (if they are not requested but already exist) or updated if they exist and are requested.
 * This class only effects the relevant classifictions; a set of classification names - so that other classifications that may by in the existing Classification
 * List are not effected.
 */
public class ClassificationGroupByOperation
{
    Map<String,Classification> existingMap = new HashMap<>();
    Map<String,Classification> requestedMap = new HashMap<>();
    List<Classification> add = new ArrayList<>();
    List<String> remove = new ArrayList<>();
    List<Classification> update = new ArrayList<>();

    /**
     * Constructor
     * @param relevantClassificationNames - this is a Set of relevant classifications names,
     * @param existing This is expected to be a non null list of Classifications that already exist in the repository
     * @param requested This is expected to be a non null list of clssifications that are the desired state for the classifications
     */
    public ClassificationGroupByOperation(Set<String>relevantClassificationNames, Set<Classification> existing, Set<Classification> requested) {
        Set<String> existingNames =null;
        if (!existing.isEmpty()) {
           existingNames = existing.stream().map(Classification::getClassificationName).collect(Collectors.toSet());
           existingMap = existing.stream().collect(Collectors.toMap(Classification::getClassificationName,
                           Function.identity()));
       }
       Set<String> requestedNames =null;
       if (!requested.isEmpty()) {
          requestedNames =requested.stream().map(Classification::getClassificationName).collect(Collectors.toSet());
          requestedMap = requested.stream().collect(Collectors.toMap(Classification::getClassificationName,
                           Function.identity()));
       }
       if (existingNames== null && requestedNames != null) {
           // add all the requested names
           add = new ArrayList<>(requested);
       }
       if (requestedNames== null && existingNames != null) {
           // remove the classifications we care about.

           Set<String> intersection = new HashSet<String>(relevantClassificationNames); // use the copy constructor
           intersection.retainAll(existingNames);
           remove = new ArrayList<>(intersection);
       }
       if (existingNames!= null && requestedNames!= null) {
           for (String existingName : existingNames) {
               if (requestedNames.contains(existingName)) {
                    // update
                   update.add(requestedMap.get(existingName));
               } else {
                   // remove only if a relevant classification
                   if (relevantClassificationNames.contains(existingName)) {
                       remove.add(existingName);
                   }
               }
           }
           for (String requestedName : requestedNames) {
               if (!existingNames.contains(requestedName)) {
                   // add
                   add.add(requestedMap.get(requestedName));
               }
           }
       }
   }

    /**
     * List of classifications to be added
     * @return List of classifications to be added
     */
    public List<Classification> getAddClassifications() {
       return this.add;
    }
    /**
     * List of classification names to removed
     * @return List of classification names to be removed
     */
    public List<String> getRemoveClassifications() {

       return this.remove;
    }
    /**
     * List of classifications to be updated
     * @return List of classifications to be updated
     */
    public List<Classification> getUpdateClassifications() {
       return this.update;
    }
}