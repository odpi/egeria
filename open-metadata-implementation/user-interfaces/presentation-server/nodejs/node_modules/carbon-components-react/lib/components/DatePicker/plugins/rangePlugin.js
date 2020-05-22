"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _rangePlugin = _interopRequireDefault(require("flatpickr/dist/plugins/rangePlugin"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * @param {object} config Plugin configuration.
 * @returns {Plugin} An extension of Flatpickr `rangePlugin` that does the following:
 *   * Better ensures the calendar dropdown is always aligned to the `<input>` for the starting date.
 *     Workaround for: https://github.com/flatpickr/flatpickr/issues/1944
 *   * A logic to ensure `fp.setDate()` call won't end up with "startDate to endDate" set to the first `<input>`
 */
var _default = function _default(config) {
  var factory = (0, _rangePlugin.default)(Object.assign({
    position: 'left'
  }, config));
  return function (fp) {
    var origSetDate = fp.setDate;

    var init = function init() {
      fp.setDate = function setDate(dates, triggerChange, format) {
        origSetDate.call(this, dates, triggerChange, format); // If `triggerChange` is `true`, `onValueUpdate` Flatpickr event is fired
        // where Flatpickr's range plugin takes care of fixing the first `<input>`

        if (!triggerChange) {
          var inputFrom = fp._input;
          var inputTo = config.input;
          [inputFrom, inputTo].forEach(function (input, i) {
            if (input) {
              input.value = !dates[i] ? '' : fp.formatDate(new Date(dates[i]), fp.config.dateFormat);
            }
          });
        }
      };
    };

    var origRangePlugin = factory(fp);
    var origOnReady = origRangePlugin.onReady;
    return Object.assign(origRangePlugin, {
      onReady: [init, origOnReady],
      onPreCalendarPosition: function onPreCalendarPosition() {}
    });
  };
};

exports.default = _default;