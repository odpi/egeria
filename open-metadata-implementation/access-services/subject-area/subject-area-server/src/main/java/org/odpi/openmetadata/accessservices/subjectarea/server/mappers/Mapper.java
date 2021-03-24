/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.OmasObject;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceAuditHeader;

public interface Mapper<OMRS extends InstanceAuditHeader, OMAS extends OmasObject> {

    /**
     * Map from an OMRS object to a Subject Area OMAS object
     * @param omrsObject OMRS Relationship
     * @return Subject Area OMAS object
     */
    OMAS map(OMRS omrsObject);

    /**
     * Map from a Subject Area OMAS object to an OMRS object
     * @param omasObject a Subject Area OMAS object
     * @return  an OMRS object
     */
    OMRS map(OMAS omasObject);

    /**
     * get the Guid
     * This method should be overridden to provide the appropriate guid for the type.
     *
     * @return the guid of the typedef
     */
    String getTypeDefGuid();

    /**
     * Get the type name
     * @return type name
     */
    String getTypeName();
}
