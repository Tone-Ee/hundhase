package de.hundhase.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping(value = "/v1/health")
class HealthRestController implements IRestController {

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> health(WebRequest webRequest) {
        return ResponseEntity.ok("{status: UP}");
    }

}