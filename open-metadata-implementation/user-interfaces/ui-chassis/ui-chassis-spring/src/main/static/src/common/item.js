/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

export const ItemViewBehavior = {

    properties: { item: Object },


    observers:[ '_itemChanged(item)' ],


    _itemChanged(item) {
        console.debug('details items changed');
        if (item) {
            this.dispatchEvent(new CustomEvent('push-crumb', {
                bubbles: true,
                composed: true,
                detail: {label: this._itemName(item), href: '/view/' + item.guid}
            }));
        }
    },

    _itemName(item) {
        if (item.properties.displayName && item.properties.displayName != null)
            return item.properties.displayName;
        else if (item.properties.name && item.properties.name != null)
            return item.properties.name;
        else
            return 'N/A';
    }

}