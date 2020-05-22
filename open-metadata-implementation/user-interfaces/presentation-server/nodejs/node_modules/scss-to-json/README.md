[![Build Status](https://travis-ci.org/ryanbahniuk/scss-to-json.svg?branch=master)](https://travis-ci.org/ryanbahniuk/scss-to-json)

# scss-to-json

> A package to require SCSS variables in JSON format.

This package allows you to use your SCSS variables in your JS code. Specifically, it takes a SCSS variable file (example below) and will parse, run Sass functions, and convert to JSON format. This package is a function that will make this conversion for you in memory. If you want to output JSON to your file system, you should use the associated [grunt-scss-to-json](https://www.npmjs.com/package/grunt-scss-to-json "grunt-scss-to-json") package.

## Installation

Install via npm:

 ```sh
npm install scss-to-json
 ```

## Input and Output

This package requires a SCSS variables file that is isolated by itself with no other SCSS code. If you are working in a front-end framework or library it is likely that your SCSS code is already set up in this manner. For example, this package will work well with a variables.scss file that looks something like this:

```scss
// Font Sizes
$font-size: 14px;
$font-size-large: $font-size * 1.1;

// Colors
$text-color: #666;
$text-color-light: lighten($text-color, 15%);
$border-color: #123 !global; // use for all borders
```

When run on that code above, scss-to-json will output the below JSON:

```js
{
  '$font-size': '14px',
  '$font-size-large': '15.4px',
  '$text-color': '#666',
  '$text-color-light': '#8c8c8c',
  '$border-color': '#123'
}
```

Note that scss-to-json will filter out flags (marked with an !) and comments and evaluate Sass functions before it produces the JSON object.

## Using this Package

In your CommonJS JavaScript file, requiring this package will return a function that takes a file path of your SCSS variables file. It also takes an optional options object, which is detailed in the next section.

```js
var scssToJson = require('scss-to-json');
var path = require('path');

var filePath = path.resolve(__dirname, 'colors.scss');
var colors = scssToJson(filePath);
```

## Options

The second argument of the returned function is an optional options object. Each option is detailed below:

### Dependencies

SCSS variables files sometimes rely on other SCSS variables defined earlier in your import tree. In order to keep these files isolated (and still produce JSON), you can specify an array of files that your given file depends on. For example, below we are trying to convert our color mapping file, but it depends on the actual color definitions which are found in a different file.

```js
var scssToJson = require('scss-to-json');
var path = require('path');

var filePath = path.resolve(__dirname, 'color-mapping.scss');
var dependencyPath = path.resolve(__dirname, 'colors.scss');
var colors = scssToJson(filePath, {
  dependencies: [{path: dependencyPath}]
});
```

### Scoping

SCSS variable files are able to provide local and global scope with the following method:

```scss
%scoped {
  $font-size: 14px;
  $font-size-large: $font-size * 1.1 !global;
}

html {
  @extend %scoped;
}
```

This will keep `$font-size` scoped locally inside that block, while allowing it to be used to derive global variables marked with the `!global` flag. These variables will be available throughout your SCSS import tree.

If you use this method in your SCSS variables file, you can provide an option to scss-to-json to output only the global variables to JSON. The option takes the name of the scoping placeholder as a string.

```js
var scssToJson = require('scss-to-json');
var path = require('path');

var filePath = path.resolve(__dirname, 'variables.scss');
var colors = scssToJson(filePath, {
  scope: '%scoped'
});
```

## CLI

You can also use the CLI `scss-to-json <file>`.


## Contributing

Pull requests are welcome. If you add functionality, then please add unit tests
to cover it. Continuous Integration is handled by [Travis](https://travis-ci.org/ryanbahniuk/scss-to-json "Travis").

## License

MIT © Ryan Bahniuk

[ci]:      https://travis-ci.org/ryanbahniuk/scss-to-json
[npm]:     https://www.npmjs.com/package/scss-to-json
