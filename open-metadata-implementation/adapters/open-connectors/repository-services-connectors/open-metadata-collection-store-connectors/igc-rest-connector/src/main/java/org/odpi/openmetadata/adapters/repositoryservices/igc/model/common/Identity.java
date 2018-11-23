/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.common;

import java.util.ArrayList;

/**
 * A representation of the unique characteristics of a particular asset, without relying on a unique ID string.
 * Two assets with the same identity may have different unique ID strings (eg. RIDs), but their Identity should
 * still be equal.
 */
public class Identity {

    private ArrayList<String> typesInOrder;
    private ArrayList<String> namesInOrder;

    private String assetType;
    private String assetName;

    /**
     * Creates a new empty identity.
     */
    public Identity() {
        typesInOrder = new ArrayList<>();
        namesInOrder = new ArrayList<>();
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
    public Identity(ArrayList<Reference> context, String assetType, String assetName) {
        this();
        for (Reference ref : context) {
            this.typesInOrder.add(ref.getType());
            this.namesInOrder.add(ref.getName());
        }
        this.assetType = assetType;
        this.assetName = assetName;
    }

    /**
     * Returns true iff this identity is equivalent to the provided identity
     *
     * @param identity the identity to compare against
     * @return Boolean
     */
    public Boolean equals(Identity identity) {
        return this.toString().equals(identity.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < typesInOrder.size(); i++) {
            String type = typesInOrder.get(i);
            String name = namesInOrder.get(i);
            sb.append("(" + type + ")=" + name + "::");
        }
        sb.append("(" + assetType + ")=" + assetName);
        return sb.toString();
    }

}
