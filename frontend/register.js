var axios = require("axios");
var data = JSON.stringify({
  username: "lidongxu01",
  password: "666666",
  repassword: "666666",
});

var config = {
  method: "post",
  url: "http://big-event-vue-api-t.itheima.net/api/reg",
  headers: {
    "Content-Type": "application/json",
  },
  data: data,
};

axios(config)
  .then(function (response) {
    console.log(JSON.stringify(response.data));
  })
  .catch(function (error) {
    console.log(error);
  });
