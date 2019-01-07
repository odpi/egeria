/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities.ContactDetailsMapper;

public class ContactThroughMapper_Team extends RelationshipMapping {

    private static class Singleton {
        private static final ContactThroughMapper_Team INSTANCE = new ContactThroughMapper_Team();
    }
    public static ContactThroughMapper_Team getInstance() {
        return Singleton.INSTANCE;
    }

    private ContactThroughMapper_Team() {
        super(
                "group",
                "group",
                SELF_REFERENCE_SENTINEL,
                SELF_REFERENCE_SENTINEL,
                "ContactThrough",
                "contactDetails",
                "contacts",
                null,
                ContactDetailsMapper.IGC_RID_PREFIX
        );
    }

}
