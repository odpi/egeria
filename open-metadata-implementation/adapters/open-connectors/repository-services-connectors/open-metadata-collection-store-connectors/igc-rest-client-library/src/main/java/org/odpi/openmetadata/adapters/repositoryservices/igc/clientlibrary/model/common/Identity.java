/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common;

import java.util.ArrayList;
import java.util.List;

/**
 * A representation of the unique characteristics of a particular asset, without relying on a unique ID string.
 * Two assets with the same identity may have different unique ID strings (eg. RIDs), but their Identity should
 * still be equal.
 */
public class Identity {

    private List<Reference> context;

    private String assetType;
    private String assetName;

    /**
     * Creates a new empty identity.
     */
    public Identity() {
        context = new ArrayList<>();
        assetType = "";
        assetName = "";
    }

    /**
     * Creates a new identity based on the identity characteristics provided.
     *
     * @param context the populated '_context' array from an asset
     * @param assetType the type of the asset
     * @param assetName the name of the asset
     */
    public Identity(List<Reference> context, String assetType, String assetName) {
        this();
        this.context = context;
        this.assetType = assetType;
        this.assetName = assetName;
    }

    /**
     * Returns true iff this identity is equivalent to the provided identity.
     *
     * @param identity the identity to compare against
     * @return boolean
     */
    public boolean sameas(Identity identity) {
        return this.toString().equals(identity.toString());
    }

    /**
     * Returns the Identity of the parent that contains the entity identified by this Identity.
     * <br><br>
     * If there is no parent identity (ie. this Identity represents a root-level asset with no
     * container above it), will return null.
     *
     * @return Identity
     */
    public Identity getParentIdentity() {
        Identity parent = null;
        if (!context.isEmpty()) {
            Integer lastIndex = context.size() - 1;
            Reference endOfCtx = context.get(lastIndex);
            List<Reference> parentCtx = context.subList(0, lastIndex);
            parent = new Identity(parentCtx, endOfCtx.getType(), endOfCtx.getName());
        }
        return parent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Reference ref : context) {
            sb.append("(" + ref.getType() + ")=" + ref.getName() + "::");
        }
        sb.append("(" + assetType + ")=" + assetName);
        return sb.toString();
    }

}
