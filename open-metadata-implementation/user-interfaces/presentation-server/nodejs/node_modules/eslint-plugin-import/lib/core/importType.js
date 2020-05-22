'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _slicedToArray = function () { function sliceIterator(arr, i) { var _arr = []; var _n = true; var _d = false; var _e = undefined; try { for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) { _arr.push(_s.value); if (i && _arr.length === i) break; } } catch (err) { _d = true; _e = err; } finally { try { if (!_n && _i["return"]) _i["return"](); } finally { if (_d) throw _e; } } return _arr; } return function (arr, i) { if (Array.isArray(arr)) { return arr; } else if (Symbol.iterator in Object(arr)) { return sliceIterator(arr, i); } else { throw new TypeError("Invalid attempt to destructure non-iterable instance"); } }; }();

exports.isAbsolute = isAbsolute;
exports.isBuiltIn = isBuiltIn;
exports.isExternalModule = isExternalModule;
exports.isExternalModuleMain = isExternalModuleMain;
exports.isScoped = isScoped;
exports.isScopedMain = isScopedMain;
exports.isScopedModule = isScopedModule;
exports.default = resolveImportType;

var _core = require('resolve/lib/core');

var _core2 = _interopRequireDefault(_core);

var _path = require('path');

var _resolve = require('eslint-module-utils/resolve');

var _resolve2 = _interopRequireDefault(_resolve);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function baseModule(name) {
  if (isScoped(name)) {
    var _name$split = name.split('/'),
        _name$split2 = _slicedToArray(_name$split, 2);

    const scope = _name$split2[0],
          pkg = _name$split2[1];

    return `${scope}/${pkg}`;
  }

  var _name$split3 = name.split('/'),
      _name$split4 = _slicedToArray(_name$split3, 1);

  const pkg = _name$split4[0];

  return pkg;
}

function isAbsolute(name) {
  return name.indexOf('/') === 0;
}

// path is defined only when a resolver resolves to a non-standard path
function isBuiltIn(name, settings, path) {
  if (path) return false;
  const base = baseModule(name);
  const extras = settings && settings['import/core-modules'] || [];
  return _core2.default[base] || extras.indexOf(base) > -1;
}

function isExternalPath(path, name, settings) {
  const folders = settings && settings['import/external-module-folders'] || ['node_modules'];

  // extract the part before the first / (redux-saga/effects => redux-saga)
  const packageName = name.match(/([^/]+)/)[0];

  return !path || folders.some(folder => -1 < path.indexOf((0, _path.join)(folder, packageName)));
}

const externalModuleRegExp = /^\w/;
function isExternalModule(name, settings, path) {
  return externalModuleRegExp.test(name) && isExternalPath(path, name, settings);
}

const externalModuleMainRegExp = /^[\w]((?!\/).)*$/;
function isExternalModuleMain(name, settings, path) {
  return externalModuleMainRegExp.test(name) && isExternalPath(path, name, settings);
}

const scopedRegExp = /^@[^/]+\/?[^/]+/;
function isScoped(name) {
  return scopedRegExp.test(name);
}

const scopedMainRegExp = /^@[^/]+\/?[^/]+$/;
function isScopedMain(name) {
  return scopedMainRegExp.test(name);
}

function isInternalModule(name, settings, path) {
  const internalScope = settings && settings['import/internal-regex'];
  const matchesScopedOrExternalRegExp = scopedRegExp.test(name) || externalModuleRegExp.test(name);
  return matchesScopedOrExternalRegExp && (internalScope && new RegExp(internalScope).test(name) || !isExternalPath(path, name, settings));
}

function isRelativeToParent(name) {
  return (/^\.\.[\\/]/.test(name)
  );
}

const indexFiles = ['.', './', './index', './index.js'];
function isIndex(name) {
  return indexFiles.indexOf(name) !== -1;
}

function isRelativeToSibling(name) {
  return (/^\.[\\/]/.test(name)
  );
}

function typeTest(name, settings, path) {
  if (isAbsolute(name, settings, path)) {
    return 'absolute';
  }
  if (isBuiltIn(name, settings, path)) {
    return 'builtin';
  }
  if (isInternalModule(name, settings, path)) {
    return 'internal';
  }
  if (isExternalModule(name, settings, path)) {
    return 'external';
  }
  if (isScoped(name, settings, path)) {
    return 'external';
  }
  if (isRelativeToParent(name, settings, path)) {
    return 'parent';
  }
  if (isIndex(name, settings, path)) {
    return 'index';
  }
  if (isRelativeToSibling(name, settings, path)) {
    return 'sibling';
  }
  return 'unknown';
}

function isScopedModule(name) {
  return name.indexOf('@') === 0;
}

function resolveImportType(name, context) {
  return typeTest(name, context.settings, (0, _resolve2.default)(name, context));
}
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uLy4uL3NyYy9jb3JlL2ltcG9ydFR5cGUuanMiXSwibmFtZXMiOlsiaXNBYnNvbHV0ZSIsImlzQnVpbHRJbiIsImlzRXh0ZXJuYWxNb2R1bGUiLCJpc0V4dGVybmFsTW9kdWxlTWFpbiIsImlzU2NvcGVkIiwiaXNTY29wZWRNYWluIiwiaXNTY29wZWRNb2R1bGUiLCJyZXNvbHZlSW1wb3J0VHlwZSIsImJhc2VNb2R1bGUiLCJuYW1lIiwic3BsaXQiLCJzY29wZSIsInBrZyIsImluZGV4T2YiLCJzZXR0aW5ncyIsInBhdGgiLCJiYXNlIiwiZXh0cmFzIiwiY29yZU1vZHVsZXMiLCJpc0V4dGVybmFsUGF0aCIsImZvbGRlcnMiLCJwYWNrYWdlTmFtZSIsIm1hdGNoIiwic29tZSIsImZvbGRlciIsImV4dGVybmFsTW9kdWxlUmVnRXhwIiwidGVzdCIsImV4dGVybmFsTW9kdWxlTWFpblJlZ0V4cCIsInNjb3BlZFJlZ0V4cCIsInNjb3BlZE1haW5SZWdFeHAiLCJpc0ludGVybmFsTW9kdWxlIiwiaW50ZXJuYWxTY29wZSIsIm1hdGNoZXNTY29wZWRPckV4dGVybmFsUmVnRXhwIiwiUmVnRXhwIiwiaXNSZWxhdGl2ZVRvUGFyZW50IiwiaW5kZXhGaWxlcyIsImlzSW5kZXgiLCJpc1JlbGF0aXZlVG9TaWJsaW5nIiwidHlwZVRlc3QiLCJjb250ZXh0Il0sIm1hcHBpbmdzIjoiOzs7Ozs7OztRQWNnQkEsVSxHQUFBQSxVO1FBS0FDLFMsR0FBQUEsUztRQWlCQUMsZ0IsR0FBQUEsZ0I7UUFLQUMsb0IsR0FBQUEsb0I7UUFLQUMsUSxHQUFBQSxRO1FBS0FDLFksR0FBQUEsWTtRQW1DQUMsYyxHQUFBQSxjO2tCQUlRQyxpQjs7QUExRnhCOzs7O0FBQ0E7O0FBRUE7Ozs7OztBQUVBLFNBQVNDLFVBQVQsQ0FBb0JDLElBQXBCLEVBQTBCO0FBQ3hCLE1BQUlMLFNBQVNLLElBQVQsQ0FBSixFQUFvQjtBQUFBLHNCQUNHQSxLQUFLQyxLQUFMLENBQVcsR0FBWCxDQURIO0FB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QUFKLEVBQW1DO0FBQUUsV0FBTyxPQUFQO0FBQWdCO0FBQ3JELE1BQUlzQixvQkFBb0I1QixJQUFwQixFQUEwQkssUUFBMUIsRUFBb0NDLElBQXBDLENBQUosRUFBK0M7QUFBRSxXQUFPLFNBQVA7QUFBa0I7QUFDbkUsU0FBTyxTQUFQO0FBQ0Q7O0FBRU0sU0FBU1QsY0FBVCxDQUF3QkcsSUFBeEIsRUFBOEI7QUFDbkMsU0FBT0EsS0FBS0ksT0FBTCxDQUFhLEdBQWIsTUFBc0IsQ0FBN0I7QUFDRDs7QUFFYyxTQUFTTixpQkFBVCxDQUEyQkUsSUFBM0IsRUFBaUM4QixPQUFqQyxFQUEwQztBQUN2RCxTQUFPRCxTQUFTN0IsSUFBVCxFQUFlOEIsUUFBUXpCLFFBQXZCLEVBQWlDLHVCQUFRTCxJQUFSLEVBQWM4QixPQUFkLENBQWpDLENBQVA7QUFDRCIsImZpbGUiOiJpbXBvcnRUeXBlLmpzIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IGNvcmVNb2R1bGVzIGZyb20gJ3Jlc29sdmUvbGliL2NvcmUnXG5pbXBvcnQgeyBqb2luIH0gZnJvbSAncGF0aCdcblxuaW1wb3J0IHJlc29sdmUgZnJvbSAnZXNsaW50LW1vZHVsZS11dGlscy9yZXNvbHZlJ1xuXG5mdW5jdGlvbiBiYXNlTW9kdWxlKG5hbWUpIHtcbiAgaWYgKGlzU2NvcGVkKG5hbWUpKSB7XG4gICAgY29uc3QgW3Njb3BlLCBwa2ddID0gbmFtZS5zcGxpdCgnLycpXG4gICAgcmV0dXJuIGAke3Njb3BlfS8ke3BrZ31gXG4gIH1cbiAgY29uc3QgW3BrZ10gPSBuYW1lLnNwbGl0KCcvJylcbiAgcmV0dXJuIHBrZ1xufVxuXG5leHBvcnQgZnVuY3Rpb24gaXNBYnNvbHV0ZShuYW1lKSB7XG4gIHJldHVybiBuYW1lLmluZGV4T2YoJy8nKSA9PT0gMFxufVxuXG4vLyBwYXRoIGlzIGRlZmluZWQgb25seSB3aGVuIGEgcmVzb2x2ZXIgcmVzb2x2ZXMgdG8gYSBub24tc3RhbmRhcmQgcGF0aFxuZXhwb3J0IGZ1bmN0aW9uIGlzQnVpbHRJbihuYW1lLCBzZXR0aW5ncywgcGF0aCkge1xuICBpZiAocGF0aCkgcmV0dXJuIGZhbHNlXG4gIGNvbnN0IGJhc2UgPSBiYXNlTW9kdWxlKG5hbWUpXG4gIGNvbnN0IGV4dHJhcyA9IChzZXR0aW5ncyAmJiBzZXR0aW5nc1snaW1wb3J0L2NvcmUtbW9kdWxlcyddKSB8fCBbXVxuICByZXR1cm4gY29yZU1vZHVsZXNbYmFzZV0gfHwgZXh0cmFzLmluZGV4T2YoYmFzZSkgPiAtMVxufVxuXG5mdW5jdGlvbiBpc0V4dGVybmFsUGF0aChwYXRoLCBuYW1lLCBzZXR0aW5ncykge1xuICBjb25zdCBmb2xkZXJzID0gKHNldHRpbmdzICYmIHNldHRpbmdzWydpbXBvcnQvZXh0ZXJuYWwtbW9kdWxlLWZvbGRlcnMnXSkgfHwgWydub2RlX21vZHVsZXMnXVxuXG4gIC8vIGV4dHJhY3QgdGhlIHBhcnQgYmVmb3JlIHRoZSBmaXJzdCAvIChyZWR1eC1zYWdhL2VmZmVjdHMgPT4gcmVkdXgtc2FnYSlcbiAgY29uc3QgcGFja2FnZU5hbWUgPSBuYW1lLm1hdGNoKC8oW14vXSspLylbMF1cblxuICByZXR1cm4gIXBhdGggfHwgZm9sZGVycy5zb21lKGZvbGRlciA9PiAtMSA8IHBhdGguaW5kZXhPZihqb2luKGZvbGRlciwgcGFja2FnZU5hbWUpKSlcbn1cblxuY29uc3QgZXh0ZXJuYWxNb2R1bGVSZWdFeHAgPSAvXlxcdy9cbmV4cG9ydCBmdW5jdGlvbiBpc0V4dGVybmFsTW9kdWxlKG5hbWUsIHNldHRpbmdzLCBwYXRoKSB7XG4gIHJldHVybiBleHRlcm5hbE1vZHVsZVJlZ0V4cC50ZXN0KG5hbWUpICYmIGlzRXh0ZXJuYWxQYXRoKHBhdGgsIG5hbWUsIHNldHRpbmdzKVxufVxuXG5jb25zdCBleHRlcm5hbE1vZHVsZU1haW5SZWdFeHAgPSAvXltcXHddKCg/IVxcLykuKSokL1xuZXhwb3J0IGZ1bmN0aW9uIGlzRXh0ZXJuYWxNb2R1bGVNYWluKG5hbWUsIHNldHRpbmdzLCBwYXRoKSB7XG4gIHJldHVybiBleHRlcm5hbE1vZHVsZU1haW5SZWdFeHAudGVzdChuYW1lKSAmJiBpc0V4dGVybmFsUGF0aChwYXRoLCBuYW1lLCBzZXR0aW5ncylcbn1cblxuY29uc3Qgc2NvcGVkUmVnRXhwID0gL15AW14vXStcXC8/W14vXSsvXG5leHBvcnQgZnVuY3Rpb24gaXNTY29wZWQobmFtZSkge1xuICByZXR1cm4gc2NvcGVkUmVnRXhwLnRlc3QobmFtZSlcbn1cblxuY29uc3Qgc2NvcGVkTWFpblJlZ0V4cCA9IC9eQFteL10rXFwvP1teL10rJC9cbmV4cG9ydCBmdW5jdGlvbiBpc1Njb3BlZE1haW4obmFtZSkge1xuICByZXR1cm4gc2NvcGVkTWFpblJlZ0V4cC50ZXN0KG5hbWUpXG59XG5cbmZ1bmN0aW9uIGlzSW50ZXJuYWxNb2R1bGUobmFtZSwgc2V0dGluZ3MsIHBhdGgpIHtcbiAgY29uc3QgaW50ZXJuYWxTY29wZSA9IChzZXR0aW5ncyAmJiBzZXR0aW5nc1snaW1wb3J0L2ludGVybmFsLXJlZ2V4J10pXG4gIGNvbnN0IG1hdGNoZXNTY29wZWRPckV4dGVybmFsUmVnRXhwID0gc2NvcGVkUmVnRXhwLnRlc3QobmFtZSkgfHwgZXh0ZXJuYWxNb2R1bGVSZWdFeHAudGVzdChuYW1lKVxuICByZXR1cm4gKG1hdGNoZXNTY29wZWRPckV4dGVybmFsUmVnRXhwICYmIChpbnRlcm5hbFNjb3BlICYmIG5ldyBSZWdFeHAoaW50ZXJuYWxTY29wZSkudGVzdChuYW1lKSB8fCAhaXNFeHRlcm5hbFBhdGgocGF0aCwgbmFtZSwgc2V0dGluZ3MpKSlcbn1cblxuZnVuY3Rpb24gaXNSZWxhdGl2ZVRvUGFyZW50KG5hbWUpIHtcbiAgcmV0dXJuIC9eXFwuXFwuW1xcXFwvXS8udGVzdChuYW1lKVxufVxuXG5jb25zdCBpbmRleEZpbGVzID0gWycuJywgJy4vJywgJy4vaW5kZXgnLCAnLi9pbmRleC5qcyddXG5mdW5jdGlvbiBpc0luZGV4KG5hbWUpIHtcbiAgcmV0dXJuIGluZGV4RmlsZXMuaW5kZXhPZihuYW1lKSAhPT0gLTFcbn1cblxuZnVuY3Rpb24gaXNSZWxhdGl2ZVRvU2libGluZyhuYW1lKSB7XG4gIHJldHVybiAvXlxcLltcXFxcL10vLnRlc3QobmFtZSlcbn1cblxuZnVuY3Rpb24gdHlwZVRlc3QobmFtZSwgc2V0dGluZ3MsIHBhdGgpIHtcbiAgaWYgKGlzQWJzb2x1dGUobmFtZSwgc2V0dGluZ3MsIHBhdGgpKSB7IHJldHVybiAnYWJzb2x1dGUnIH1cbiAgaWYgKGlzQnVpbHRJbihuYW1lLCBzZXR0aW5ncywgcGF0aCkpIHsgcmV0dXJuICdidWlsdGluJyB9XG4gIGlmIChpc0ludGVybmFsTW9kdWxlKG5hbWUsIHNldHRpbmdzLCBwYXRoKSkgeyByZXR1cm4gJ2ludGVybmFsJyB9XG4gIGlmIChpc0V4dGVybmFsTW9kdWxlKG5hbWUsIHNldHRpbmdzLCBwYXRoKSkgeyByZXR1cm4gJ2V4dGVybmFsJyB9XG4gIGlmIChpc1Njb3BlZChuYW1lLCBzZXR0aW5ncywgcGF0aCkpIHsgcmV0dXJuICdleHRlcm5hbCcgfVxuICBpZiAoaXNSZWxhdGl2ZVRvUGFyZW50KG5hbWUsIHNldHRpbmdzLCBwYXRoKSkgeyByZXR1cm4gJ3BhcmVudCcgfVxuICBpZiAoaXNJbmRleChuYW1lLCBzZXR0aW5ncywgcGF0aCkpIHsgcmV0dXJuICdpbmRleCcgfVxuICBpZiAoaXNSZWxhdGl2ZVRvU2libGluZyhuYW1lLCBzZXR0aW5ncywgcGF0aCkpIHsgcmV0dXJuICdzaWJsaW5nJyB9XG4gIHJldHVybiAndW5rbm93bidcbn1cblxuZXhwb3J0IGZ1bmN0aW9uIGlzU2NvcGVkTW9kdWxlKG5hbWUpIHtcbiAgcmV0dXJuIG5hbWUuaW5kZXhPZignQCcpID09PSAwXG59XG5cbmV4cG9ydCBkZWZhdWx0IGZ1bmN0aW9uIHJlc29sdmVJbXBvcnRUeXBlKG5hbWUsIGNvbnRleHQpIHtcbiAgcmV0dXJuIHR5cGVUZXN0KG5hbWUsIGNvbnRleHQuc2V0dGluZ3MsIHJlc29sdmUobmFtZSwgY29udGV4dCkpXG59XG4iXX0=