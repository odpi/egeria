"use strict";

exports.__esModule = true;
exports.default = void 0;

var _react = require("react");

var _useStableMemo = _interopRequireDefault(require("./useStableMemo"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * An _immediate_ effect that runs an effect callback when its dependency array
 * changes. This is helpful for updates should must run during render, most
 * commonly state derived from props; a more ergonomic version of https://reactjs.org/docs/hooks-faq.html#how-do-i-implement-getderivedstatefromprops
 *
 * ```ts
 * function Example({ value }) {
 *   const [intermediaryValue, setValue] = useState(value);
 *
 *   useImmediateUpdateEffect(() => {
 *     setValue(value)
 *   }, [value])
 * ```
 */
function useImmediateUpdateEffect(effect, deps) {
  var firstRef = (0, _react.useRef)(true);
  (0, _useStableMemo.default)(function () {
    if (firstRef.current) {
      firstRef.current = false;
      return;
    }

    effect();
  }, deps);
}

var _default = useImmediateUpdateEffect;
exports.default = _default;