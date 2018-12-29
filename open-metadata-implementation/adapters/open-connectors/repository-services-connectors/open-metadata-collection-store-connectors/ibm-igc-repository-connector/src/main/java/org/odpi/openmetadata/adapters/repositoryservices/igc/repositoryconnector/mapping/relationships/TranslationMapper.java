/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

public class TranslationMapper extends RelationshipMapping {

    private static class Singleton {
        private static final TranslationMapper INSTANCE = new TranslationMapper();
    }
    public static TranslationMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private static final String P_TRANSLATIONS = "translations";

    private TranslationMapper() {
        super(
                "term",
                "term",
                P_TRANSLATIONS,
                P_TRANSLATIONS,
                "Translation",
                P_TRANSLATIONS,
                P_TRANSLATIONS
        );
    }

}
