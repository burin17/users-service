const Eureka = require('eureka-js-client').Eureka
const express = require("express");
const app = express();

const Pool = require("pg").Pool
const pool = new Pool({
    user: 'postgres',
    host: 'localhost',
    database: 'admin_deletion_granted',
    password: '3546',
    port: 5432,
})

const eurekaClient = new Eureka({
   instance: {
       app: "admin-deletion-service",
       hostName: "localhost",
       ipAddr: '127.0.0.1',
       port: {
           '$': 9011,
           '@enabled': 'true',
       },
       statusPageUrl: 'http://localhost:9011/info',
       vipAddress: 'jq.test.something.com',
       dataCenterInfo: {
           '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
           name: 'MyOwn',
       },
   },
   eureka: {
       host: '127.0.0.1',
       port: 8761,
       servicePath: '/eureka/apps/'
   }
});

eurekaClient.start(error => {
    console.log(error || "user service registered")
});

app.get("/api/users/is-allowed/:id", function(request, response){
    console.log('node instance')
    const id = request.params.id; // получаем id
    pool.query('select * from usr where id = $1', [id], (err, res) => {
        if(err) {
            throw err;
        }
        return response.status(200).send(res.rows.length !== 0);
    });
});

app.listen(9011, function(){

});
