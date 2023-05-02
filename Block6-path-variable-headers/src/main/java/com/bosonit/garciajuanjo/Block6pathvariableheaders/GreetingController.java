package com.bosonit.garciajuanjo.Block6pathvariableheaders;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class GreetingController {

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<Long> getGreeting(@PathVariable Long id) {
        return ResponseEntity.ok(id);
    }

    @PostMapping(value = "/")
    public ResponseEntity<Greeting> addGreeting(@RequestBody Greeting greeting) {
        return ResponseEntity.ok(greeting);
    }

    @PutMapping(value = "/put")
    public ResponseEntity<?> putData(@RequestParam String var1, @RequestParam String var2) {
        HashMap<String, String> map = new HashMap<>();
        map.put("var1", var1);
        map.put("var2", var2);

        return ResponseEntity.ok(map);
    }
}
