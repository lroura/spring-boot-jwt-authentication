package com.example.demo.controllers.management;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/someOtherThing")
@PreAuthorize("hasAnyAuthority('ROLE1')")
public class SomeOtherThingController {

    @GetMapping
    public Map<String, Object> index() {
        Map<String, Object> r = new HashMap<>();
        r.put("name", "Hello");
        r.put("value", "world");
        return r;
    }
}
