package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import app.db.HcsEntityDao;

@Controller
public class AppController {
    @Autowired
    private HcsEntityDao hcsDao;

    @RequestMapping(method = RequestMethod.PUT, path = "assert", produces = "application/json")
    public ResponseEntity attest() {
        // TODO: Post a new entity, get back annotated entity? or just transaction information from hcs?
    }

    @RequestMapping(method = RequestMethod.PUT, path = "verify", produces = "application/json")
    public ResponseEntity verify() {
        // TODO: Post information from HCS or about entity (received from attest), receive 200 (entity) or 400 (not entity)?
    }
}