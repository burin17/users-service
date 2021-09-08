const eureka = require('eureka-nodejs-client')({
    eureka: {
        serviceUrl: ['http://localhost:8761'],
        pollIntervalSeconds: 10,
        registerWithEureka: true,
    },
    instance:{
        app: 'admin-deletion-service',
        ipAddr: 'localhost',
        port: 9011
    }
}, 'info')

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

eureka.start()

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
