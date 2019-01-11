function() {
  karate.configure('connectTimeout', 5000);
  karate.configure('readTimeout', 5000);  
  var port = karate.properties['demo.server.port'];  
  if (!port) {
    port = karate.env == 'web' ? 8090 : 8080;
  }
  var protocol = 'http';
  if (karate.properties['demo.server.https'] == 'true') {
    protocol = 'https';
    karate.configure('ssl', true);
  }  
  var config = { demoBaseUrl: 'http://localhost:8080/nbi/api/v3' };
  if (karate.env == 'proxy') {
    var proxyPort = karate.properties['demo.proxy.port']
    karate.configure('proxy', 'http://127.0.0.1:' + proxyPort);
  }
  return config;
}
