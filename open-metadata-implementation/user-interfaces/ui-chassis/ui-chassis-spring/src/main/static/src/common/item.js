/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

export const ItemViewBehavior = {

    properties: { item: Object},

    observers:[ '_itemChanged(item)' ],

    _itemChanged(item) {
        console.debug('details items changed');
        if (item) {
            this.dispatchEvent(new CustomEvent('push-crumb', {
                bubbles: true,
                composed: true,
                detail: {label: this._itemName(item), href: null}
            }));
        }
        this._attributes(item);
    },

    _itemName(item) {
        if (item.properties.displayName && item.properties.displayName != null)
            return item.properties.displayName;
        else if (item.properties.name && item.properties.name != null)
            return item.properties.name;
        else
            return 'N/A';
    },

    _attributes(obj){
        var arr = [];
        Object.keys(obj).forEach(
            (key)=> {
                var value = obj[key];
                if(typeof value !== 'object' && value !== null)
                arr.push( { 'key' : this._camelCaseToSentence(key) , 'value' : value } );
            }
        );
        return arr;
    },

    _hasKeys(obj){
      return Object.keys(obj).length > 0;
    },

    _camelCaseToSentence(val){
        return val
            .replace(/([A-Z])/g, ' $1')
            .replace(/([A-Z]+\s+)/g, c => c.trim() )
            .replace(/_/g, ' ') // replace underscores with spaces
            .replace(/^\w/, c => c.toUpperCase()); //uppercase first letter
    }


}