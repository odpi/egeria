"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.ArrowDown = exports.ArrowRight = exports.ArrowUp = exports.ArrowLeft = exports.Home = exports.End = exports.PageDown = exports.PageUp = exports.Space = exports.Escape = exports.Enter = exports.Tab = void 0;

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
var Tab = {
  key: 'Tab',
  which: 9,
  keyCode: 9
};
exports.Tab = Tab;
var Enter = {
  key: 'Enter',
  which: 13,
  keyCode: 13
};
exports.Enter = Enter;
var Escape = {
  key: ['Escape', // IE11 Escape
  'Esc'],
  which: 27,
  keyCode: 27
};
exports.Escape = Escape;
var Space = {
  key: ' ',
  which: 32,
  keyCode: 32
};
exports.Space = Space;
var PageUp = {
  key: 'PageUp',
  which: 33,
  keyCode: 33
};
exports.PageUp = PageUp;
var PageDown = {
  key: 'PageDown',
  which: 34,
  keyCode: 34
};
exports.PageDown = PageDown;
var End = {
  key: 'End',
  which: 35,
  keyCode: 35
};
exports.End = End;
var Home = {
  key: 'Home',
  which: 36,
  keyCode: 36
};
exports.Home = Home;
var ArrowLeft = {
  key: 'ArrowLeft',
  which: 37,
  keyCode: 37
};
exports.ArrowLeft = ArrowLeft;
var ArrowUp = {
  key: 'ArrowUp',
  which: 38,
  keyCode: 38
};
exports.ArrowUp = ArrowUp;
var ArrowRight = {
  key: 'ArrowRight',
  which: 39,
  keyCode: 39
};
exports.ArrowRight = ArrowRight;
var ArrowDown = {
  key: 'ArrowDown',
  which: 40,
  keyCode: 40
};
exports.ArrowDown = ArrowDown;