
/* global print */
/* eslint-disable no-var */

/**
 * Utility function to cheat ESLint
 * @param {String to print} str
 */
function println(str) {
  print(str); // eslint-disable-line no-restricted-globals
}
/**
 * Utility function to padd string to desired lenght
 * @param {String to padd} str
 * @param {Padding character} char
 * @param {Desired lenght} len
 */
function paddTo(str, char, len) {
  var formattedStr = str;

  while (formattedStr.length < len) {
    formattedStr += char;
  }

  return formattedStr;
}

function Result(resp, code) {
  this.resp = resp;
  this.code = code;
}

function RequestModel() {
  this.values = [];
  this.headers = [];
  this.columnWidths = [];
}

RequestModel.prototype.parseJSONToArray = function parseJSONToArray(json) {
  var i = 0;
  var k = 0;
  var row = [];
  var columnsKeys = null;
  var key = '';

  for (i = 0; i < json.length; i += 1) {
    row = [];
    columnsKeys = Object.keys(json[i]);
    for (k = 0; k < columnsKeys.length; k += 1) {
      key = columnsKeys[k];
      if (this.headers.indexOf(key) < 0) {
        this.headers.push(key);
      }
      while (row.length < this.headers.length) {
        row.push('');
      }
      row[this.headers.indexOf(key)] = json[i][key];

      while (this.columnWidths.length < this.headers.length) {
        this.columnWidths.push(0);
      }

      if (this.columnWidths[this.headers.indexOf(key)] < json[i][key].length) {
        this.columnWidths[this.headers.indexOf(key)] = json[i][key].length;
      }

      if (this.columnWidths[this.headers.indexOf(key)] < key.length) {
        this.columnWidths[this.headers.indexOf(key)] = key.length;
      }
    }
    this.values.push(row);
  }
};

RequestModel.prototype.getArrayHeader = function getArrayHeader() {
  var i = 0;
  var headerString = '';
  for (i = 0; i < this.headers.length; i += 1) {
    headerString += paddTo(this.headers[i], ' ', this.columnWidths[i] + 2);
  }

  return headerString;
};

RequestModel.prototype.getArrayLimitter = function getArrayLimitter() {
  var i = 0;
  var limitterString = '';
  for (i = 0; i < this.columnWidths.length; i += 1) {
    limitterString += paddTo('', '-', this.columnWidths[i] + 2);
  }

  return limitterString;
};

RequestModel.prototype.getArrayValues = function getArrayValues() {
  var i = 0;
  var j = 0;
  var row = null;
  var valueString = '';

  for (i = 0; i < this.values.length; i += 1) {
    row = this.values[i];
    for (j = 0; j < row.length; j += 1) {
      valueString += paddTo(row[j], ' ', this.columnWidths[j] + 2);
    }
    valueString += '\n';
  }

  return valueString;
};


function doGet() { // eslint-disable-line no-unused-vars
  println('Service : json2Table, called with GET');
  return 'Method GET is not available. Use HTTP POST';
}

function doPost(body) { // eslint-disable-line no-unused-vars
  // JSON request to array transformation
  var model = new RequestModel();

  println('Service : json2Table, called with POST');
  model.parseJSONToArray(JSON.parse(body));

  println(model.getArrayHeader());
  println(model.getArrayLimitter());
  println(model.getArrayValues());

  return new Result('POST NOW OK', 200);
}
