package com.orh;

import java.util.ServiceLoader;

import com.orh.service.Service;

public class UseDemo {

    public static void main(String[] args) {
        ServiceLoader<Service> services = ServiceLoader.load(Service.class);

        for (Service service : services) {
            service.say();
        }
        System.out.println("over!");
        
    }
}
