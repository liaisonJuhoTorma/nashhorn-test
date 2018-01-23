function Result (resp, code) {
    this.resp = resp;
    this.code = code;
}

function doGet() {
    print("Service : json2Table, called with GET");
    return "Method GET is not available. Use HTTP POST";
}

function doPost(body) {
    print("Service : json2Table, called with POST");
    var json = JSON.parse(body);

    columns = [];
    values = [];
    cWidths = [];

    for(var i=0; i < json.length; i++) {
        var row = [];
        for(var k in json[i]) {
            if(columns.indexOf(k) < 0) {
                columns.push(k);
            }
            for(var j=0; j < columns.length; j++){
                row.push("");
            }
            row[columns.indexOf(k)] = json[i][k];

            while(cWidths.length < columns.length) {
                cWidths.push(0);
            }

            if(cWidths[columns.indexOf(k)] < json[i][k].length) {
                cWidths[columns.indexOf(k)] = json[i][k].length;
            }

            if(cWidths[columns.indexOf(k)] < k.length) {
                cWidths[columns.indexOf(k)] = k.length;
            }
        }
        values.push(row);

    }

    var h = "";
    for(var i = 0; i < columns.length; i++) {
        var iLen = h.length;
        h = h + columns[i];
        var tLen = iLen + cWidths[i] + 2;

        while(h.length < tLen) {
            h = h + " ";
        }
    }

    print(h);

    var d = "";
    for(var i = 0; i < cWidths.length; i++) {
        var tLen = d.length + cWidths[i] + 2;

        while(d.length < tLen) {
            d = d + "-";
        }
    }
    print(d);

    for(var i = 0; i < values.length; i++) {
        var r = values[i];
        var v = "";
        for(var j = 0; j < r.length; j++) {
            var iLen = v.length;
            v = v + r[j];
            var tLen = iLen + cWidths[j] + 2;

            while(v.length < tLen) {
                v = v + " ";
            }
        }
        print(v);
    }

    var r = new Result("POST NOW OK", 200);

    return r;
}