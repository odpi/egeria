/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnector.governancedarmonconnectors.securityofficerconnectors.securitytagconnector;


import org.odpi.openmetadata.accessservices.securityofficer.api.model.BusinessTerm;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.Classification;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SchemaElementEntity;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SecurityClassification;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.openconnector.governancedarmonconnectors.securityofficerconnectors.SecurityOfficerConnector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SecurityTagConnector extends ConnectorBase implements SecurityOfficerConnector {

    private static final String CONFIDENTIALITY = "Confidentiality";
    private static final String LEVEL = "level";

    @Override
    public SecurityClassification resolveSecurityClassification(SchemaElementEntity schemaElementEntity) {
        List<Classification> classifications = schemaElementEntity.getClassifications();
        List<Classification> existingClassifications = getExistingClassifications(schemaElementEntity, classifications);

        SecurityClassification securityClassification;
        if (!existingClassifications.isEmpty()) {
             securityClassification = getMoreRestrictiveClassification(existingClassifications);

        } else {
            securityClassification = getDefaultSecurityTags();
        }

        Map<String, Object> properties = securityClassification.getSecurityProperties();
        if(properties == null) {
            properties = new HashMap<>();
        }
        properties.put("source", schemaElementEntity.getType());
        securityClassification.setSecurityProperties(properties);

        return securityClassification;
    }

    private SecurityClassification getDefaultSecurityTags() {
        SecurityClassification securityClassification = new SecurityClassification();
        securityClassification.setSecurityLabels(Collections.singletonList("C99"));

        Map<String, Object> properties = new HashMap<>();
        properties.put("user", "SecurityTagConnector");
        properties.put("label", "Secret");
        securityClassification.setSecurityProperties(properties);
        return securityClassification;
    }

    private List<Classification> getExistingClassifications(SchemaElementEntity schemaElementEntity, List<Classification> classifications) {
        List<Classification> potentialClassifications = new ArrayList<>();
        if (classifications != null && !classifications.isEmpty()) {
            potentialClassifications = getPotentialClassifications(classifications);
        }

        if (potentialClassifications.isEmpty()) {
            BusinessTerm businessTerm = schemaElementEntity.getBusinessTerm();
            if (businessTerm != null && !businessTerm.getClassifications().isEmpty()) {
                return getPotentialClassifications(businessTerm.getClassifications());
            }
        }
        return potentialClassifications;
    }

    private SecurityClassification getMoreRestrictiveClassification(List<Classification> classifications) {
        if (classifications.isEmpty()) {
            return null;
        }

        if (classifications.size() == 1) {
            return buildSecurityClassification(classifications.get(0));
        }

        Integer mostRestrictiveClassification = getLevelOfClassification(classifications.get(0).getProperties());
        int index = 0;
        for (int i = 1; i < classifications.size(); i++) {
            Integer levelOfClassification = getLevelOfClassification(classifications.get(i).getProperties());
            if (levelOfClassification < mostRestrictiveClassification) {
                index = i;
            }
        }

        return buildSecurityClassification(classifications.get(index));
    }

    private Integer getLevelOfClassification(Map<String, String> properties) {

        if (properties.containsKey(LEVEL)) {
            String level = properties.get(LEVEL);
            return Integer.valueOf(level);
        }

        return 101;
    }

    private SecurityClassification buildSecurityClassification(Classification classification) {
        Map<String, String> properties = classification.getProperties();

        SecurityClassification securityClassification = new SecurityClassification();
        getSecurityLabels(properties, securityClassification);
        Map<String, Object> securityProperties = getSecurityProperties(properties);

        securityClassification.setSecurityProperties(securityProperties);
        return securityClassification;
    }

    private void getSecurityLabels(Map<String, String> properties, SecurityClassification securityClassification) {
        Integer label = getLevelOfClassification(properties);
        List<String> securityLabels = new ArrayList<>();
        securityLabels.add("C" + label);
        securityClassification.setSecurityLabels(securityLabels);
    }

    private Map<String, Object> getSecurityProperties(Map<String, String> properties) {
        Map<String, Object> securityProperties = new HashMap<>();
        properties.forEach((key, value) -> {
            if (!key.equals(LEVEL) && !value.isEmpty()) {
                securityProperties.put(key, value);
            }
        });

        return securityProperties;
    }

    private List<Classification> getPotentialClassifications(List<Classification> classifications) {
        return classifications.stream().filter(classification -> classification.getName().equals(CONFIDENTIALITY)).collect(Collectors.toList());
    }

}