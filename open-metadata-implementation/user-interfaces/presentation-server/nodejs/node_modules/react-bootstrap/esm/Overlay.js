import _extends from "@babel/runtime/helpers/esm/extends";
import _objectWithoutPropertiesLoose from "@babel/runtime/helpers/esm/objectWithoutPropertiesLoose";
import React from 'react';
import { findDOMNode } from 'react-dom';
import classNames from 'classnames';
import BaseOverlay from 'react-overlays/Overlay';
import Fade from './Fade';
var defaultProps = {
  transition: Fade,
  rootClose: false,
  show: false,
  placement: 'top'
};

function wrapRefs(props, arrowProps) {
  var ref = props.ref;
  var aRef = arrowProps.ref;

  props.ref = ref.__wrapped || (ref.__wrapped = function (r) {
    return ref(findDOMNode(r));
  });

  arrowProps.ref = aRef.__wrapped || (aRef.__wrapped = function (r) {
    return aRef(findDOMNode(r));
  });
}

function Overlay(_ref) {
  var overlay = _ref.children,
      transition = _ref.transition,
      outerProps = _objectWithoutPropertiesLoose(_ref, ["children", "transition"]);

  transition = transition === true ? Fade : transition || null;
  return React.createElement(BaseOverlay, _extends({}, outerProps, {
    transition: transition
  }), function (_ref2) {
    var overlayProps = _ref2.props,
        arrowProps = _ref2.arrowProps,
        show = _ref2.show,
        props = _objectWithoutPropertiesLoose(_ref2, ["props", "arrowProps", "show"]);

    wrapRefs(overlayProps, arrowProps);
    if (typeof overlay === 'function') return overlay(_extends({}, props, {}, overlayProps, {
      show: show,
      arrowProps: arrowProps
    }));
    return React.cloneElement(overlay, _extends({}, props, {}, overlayProps, {
      arrowProps: arrowProps,
      className: classNames(overlay.props.className, !transition && show && 'show'),
      style: _extends({}, overlay.props.style, {}, overlayProps.style)
    }));
  });
}

Overlay.defaultProps = defaultProps;
export default Overlay;