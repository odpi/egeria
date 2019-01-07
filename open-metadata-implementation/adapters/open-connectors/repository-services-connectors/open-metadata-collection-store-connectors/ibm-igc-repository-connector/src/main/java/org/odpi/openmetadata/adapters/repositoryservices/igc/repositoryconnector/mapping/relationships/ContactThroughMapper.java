/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

/**
 * Singleton to map the OMRS "ContactThrough" relationship.
 * @see ContactThroughMapper_Person
 * @see ContactThroughMapper_Team
 */
public class ContactThroughMapper extends RelationshipMapping {

    private static class Singleton {
        private static final ContactThroughMapper INSTANCE = new ContactThroughMapper();
    }
    public static ContactThroughMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private ContactThroughMapper() {
        super(
                "",
                "",
                "",
                "",
                "ContactThrough",
                "contactDetails",
                "contacts"
        );
        addSubType(ContactThroughMapper_Person.getInstance());
        addSubType(ContactThroughMapper_Team.getInstance());
    }

}
