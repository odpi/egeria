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
    private String rid;

    /**
     * Creates a new empty identity.
     */
    public Identity() {
        context = new ArrayList<>();
        assetType = "";
        assetName = "";
        rid = null;
    }

    /**
     * Creates a new identity based on the identity characteristics provided.
     *
     * @param context the populated '_context' array from an asset
     * @param assetType the type of the asset
     * @param assetName the name of the asset
     */
    public Identity(List<Reference> context, String assetType, String assetName) {
        this(context, assetType, assetName, null);
    }

    /**
     * Creates a new identity based on the identity characteristics provided.
     * (Also keeps a record of Repository ID (RID) for potential efficiency of parent traversal)
     *
     * @param context the populated '_context' array from an asset
     * @param assetType the type of the asset
     * @param assetName the name of the asset
     * @param rid the Repository ID (RID) of the asset
     */
    public Identity(List<Reference> context, String assetType, String assetName, String rid) {
        this();
        this.context = context;
        this.assetType = assetType;
        this.assetName = assetName;
        this.rid = rid;
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
            parent = new Identity(parentCtx, endOfCtx.getType(), endOfCtx.getName(), endOfCtx.getId());
        }
        return parent;
    }

    /**
     * Returns the Repository ID (RID) of this identity (if available), or null.
     *
     * @return String
     */
    public String getRid() { return this.rid; }

    /**
     * Indicates whether the asset type requires its Repository ID (RID) in order to be unique. In other words, the name
     * and context of the asset type are insufficient to make the identity unique.
     *
     * @param assetType the IGC asset type to check
     * @return boolean
     */
    private static final boolean requiresRidToBeUnique(String assetType) {
        return (assetType.equals("data_connection"));
    }

    /**
     * Composes a unique identity string from the provided parameters.
     *
     * @param sb the object into which to compose the string
     * @param type the IGC asset type
     * @param name the name of the IGC asset
     * @param id the Repository ID (RID) of the IGC asset
     */
    private static final void composeString(StringBuilder sb, String type, String name, String id) {
        sb.append("(");
        sb.append(type);
        sb.append(")=");
        sb.append(name);
        if (requiresRidToBeUnique(type)) {
            sb.append("_");
            sb.append(id);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Reference ref : context) {
            composeString(sb, ref.getType(), ref.getName(), ref.getId());
            sb.append("::");
        }
        composeString(sb, assetType, assetName, rid);
        return sb.toString();
    }

}
