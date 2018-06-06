/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Lineage shows the origin of the connected asset.  It covers:
 * <ul>
 *     <li>Design lineage - the known data movement and data stores that can supply data to this asset.</li>
 *     <li>Operational lineage - showing the jobs that ran to create this asset</li>
 * </ul>
 *
 * Currently lineage is not implemented in the ConnectedAssetProperties interface because more design work is needed.
 * This class is therefore a placeholder for lineage information.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Lineage extends PropertyBase
{
    /**
     * Default constructor.
     */
    public Lineage()
    {
        super();
    }


    /**
     * Copy/clone constructor - - the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the lineage clone to point to the
     * asset clone and not the original asset.
     *
     * @param templateLineage - lineage object to copy.
     */
    public Lineage(Lineage   templateLineage)
    {
        super(templateLineage);

        /*
         * The open lineage design is still in progress so for the time being, this object does not do anything
         * useful
         */
    }
}