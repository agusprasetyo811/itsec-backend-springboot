package agus.prasetyo.backend.apps.controller;

import agus.prasetyo.backend.system.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class IndexController {

    @Autowired
    CacheService cacheService;

    @GetMapping
    public String index() {
        cacheService.clearAllCaches();
        return "Backend Services";
    }
}
