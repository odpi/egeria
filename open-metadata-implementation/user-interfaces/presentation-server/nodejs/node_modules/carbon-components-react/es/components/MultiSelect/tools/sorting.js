/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */

/**
 * Use the local `localCompare` with the `numeric` option to sort two,
 * potentially alpha-numeric, strings in a list of items.
 *
 * @param {string} itemA
 * @param {string} itemB
 * @param {object} options
 * @param {string} options.locale
 * @returns {number}
 */
export var defaultCompareItems = function defaultCompareItems(itemA, itemB, _ref) {
  var locale = _ref.locale;
  return itemA.localeCompare(itemB, locale, {
    numeric: true
  });
};
/**
 * Default sorting algorithm for options in a selection control
 */

export var defaultSortItems = function defaultSortItems(items, _ref2) {
  var _ref2$selectedItems = _ref2.selectedItems,
      selectedItems = _ref2$selectedItems === void 0 ? [] : _ref2$selectedItems,
      itemToString = _ref2.itemToString,
      compareItems = _ref2.compareItems,
      _ref2$locale = _ref2.locale,
      locale = _ref2$locale === void 0 ? 'en' : _ref2$locale;
  return items.sort(function (itemA, itemB) {
    var hasItemA = selectedItems.includes(itemA);
    var hasItemB = selectedItems.includes(itemB); // Prefer whichever item is in the `selectedItems` array first

    if (hasItemA && !hasItemB) {
      return -1;
    }

    if (hasItemB && !hasItemA) {
      return 1;
    }

    return compareItems(itemToString(itemA), itemToString(itemB), {
      locale: locale
    });
  });
};