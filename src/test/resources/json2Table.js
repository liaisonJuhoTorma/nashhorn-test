function Result (r, c) {
	this.resp = r;
	this.code = c;
}

function doGet() {
	print('doGet called');
	return 'GET OK';
}

function doPost(body) {
	print('doPost called');
	return new Result('POST OK', 200);
}