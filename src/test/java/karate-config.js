function() {
  var config = {
    nbiBaseUrl: 'http://localhost:8080/nbi/api/v4'
  };
  karate.configure('connectTimeout', 5000);
  karate.configure('readTimeout', 5000);
  return config;
}
