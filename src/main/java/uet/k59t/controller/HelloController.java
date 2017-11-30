package uet.k59t.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uet.k59t.service.HelloService;

/**
 * Created by Long on 11/21/2016.
 */
@RestController
public class HelloController {
    @Autowired
    private HelloService helloService;

    @RequestMapping(value = "/hello/{name}", method = RequestMethod.GET)
    public String upload(@PathVariable("name") String name){
        return helloService.sayHello(name);
    }

}
