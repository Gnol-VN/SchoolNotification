package uet.k59t.service;

/**
 * Created by Long on 11/20/2016.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uet.k59t.controller.HelloController;

@Service
public class HelloService {

    public String sayHello(String s){
        return "Hello, " +s;
    }
}