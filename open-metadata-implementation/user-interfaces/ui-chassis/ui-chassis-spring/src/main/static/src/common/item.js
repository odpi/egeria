/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

export const ItemViewBehavior = {

    properties: { item: Object},

    observers:[ '_itemChanged(item)' ],

    _itemChanged(item) {
        console.debug('details items changed');
        if (item) {
            this._pushCrumb(this._itemName(item));
            this._attributes(item);
        }else{
            window.dispatchEvent(new CustomEvent('show-feedback', {
                bubbles: true,
                composed: true,
                detail: {message: "Item not found!", level: 'error'}}));
        }
    },

    _pushCrumb(val) {
            this.dispatchEvent(new CustomEvent('push-crumb', {
                bubbles: true,
                composed: true,
                detail: {label: val, href: null}
            }));

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

    _hasKey(obj,key){
        return key in obj;
    },

    _camelCaseToSentence(val){
        return val
            .replace(/([A-Z])/g, ' $1')
            .replace(/([A-Z]+\s+)/g, c => c.trim() )
            .replace(/_/g, ' ') // replace underscores with spaces
            .replace(/^\w/, c => c.toUpperCase()); //uppercase first letter
    }


}