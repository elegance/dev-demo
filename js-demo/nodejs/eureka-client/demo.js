// github: https://github.com/jquatier/eureka-js-client
const Eureka = require('eureka-js-client').Eureka;

const client = new Eureka({
    instance: {
        app: 'node-client',
        hostName: 'localhost',
        ipAddr: '127.0.0.1',
        // instanceId: 'test-node',
        statusPageUrl: 'http://127.0.0.1/info',
        healthCheckUrl: 'http://127.0.0.1/',
        port: {
           '$': 8080,
           '@enabled': true,
        },
        vipAddress: 'test.something.com',
        dataCenterInfo: {
            '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
            name: 'MyOwn',
        },
    },
    eureka: {
        serviceUrls: {
            default: [
                'http://47.98.38.105:8761/eureka/apps/'
            ]
        },
        heartbeatInterval: 1000*10
    }
});

client.start();
