import _extends from "@babel/runtime/helpers/esm/extends";
import _objectWithoutPropertiesLoose from "@babel/runtime/helpers/esm/objectWithoutPropertiesLoose";
import classNames from 'classnames';
import React from 'react';
import { useBootstrapPrefix } from './ThemeProvider';
var defaultProps = {
  noGutters: false
};
var Row = React.forwardRef(function (props, ref) {
  var bsPrefix = props.bsPrefix,
      noGutters = props.noGutters,
      _props$as = props.as,
      Component = _props$as === void 0 ? 'div' : _props$as,
      className = props.className,
      otherProps = _objectWithoutPropertiesLoose(props, ["bsPrefix", "noGutters", "as", "className"]);

  var decoratedBsPrefix = useBootstrapPrefix(bsPrefix, 'row');
  return React.createElement(Component, _extends({
    ref: ref
  }, otherProps, {
    className: classNames(className, decoratedBsPrefix, noGutters && 'no-gutters')
  }));
});
Row.defaultProps = defaultProps;
export default Row;