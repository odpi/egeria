import _extends from "@babel/runtime/helpers/esm/extends";
import _objectWithoutPropertiesLoose from "@babel/runtime/helpers/esm/objectWithoutPropertiesLoose";
import classNames from 'classnames';
import React from 'react';
import SafeAnchor from './SafeAnchor';
import { useBootstrapPrefix } from './ThemeProvider';
var defaultProps = {
  active: false
};
var BreadcrumbItem = React.forwardRef( // Need to define the default "as" during prop destructuring to be compatible with styled-components github.com/react-bootstrap/react-bootstrap/issues/3595
function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      active = _ref.active,
      className = _ref.className,
      _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'li' : _ref$as,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "active", "className", "as"]);

  var prefix = useBootstrapPrefix(bsPrefix, 'breadcrumb-item');

  var href = props.href,
      title = props.title,
      target = props.target,
      elementProps = _objectWithoutPropertiesLoose(props, ["href", "title", "target"]);

  var linkProps = {
    href: href,
    title: title,
    target: target
  };
  return React.createElement(Component, {
    ref: ref,
    className: classNames(prefix, className, {
      active: active
    }),
    "aria-current": active ? 'page' : undefined
  }, active ? React.createElement("span", _extends({}, elementProps, {
    className: classNames({
      active: active
    })
  })) : React.createElement(SafeAnchor, _extends({}, elementProps, linkProps)));
});
BreadcrumbItem.displayName = 'BreadcrumbItem';
BreadcrumbItem.defaultProps = defaultProps;
export default BreadcrumbItem;