package de.hundhase.controller;

import de.hundhase.controller.model.Health;
import de.hundhase.controller.model.ImmutableHealth;
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
        return ResponseEntity.ok(ImmutableHealth.builder().status(Health.UP).build());
    }

}