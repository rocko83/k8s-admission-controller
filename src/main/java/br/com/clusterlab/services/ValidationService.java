package br.com.clusterlab.services;

import br.com.clusterlab.dto.response.AdmissionReview;
import br.com.clusterlab.dto.response.Response;
import br.com.clusterlab.dto.response.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/validate")
public class ValidationService {
    Logger logger = LoggerFactory.getLogger(ValidationService.class);
    @PostMapping({"/pods"})
    public String pods(@RequestBody ObjectNode request) {
        ObjectMapper om = new ObjectMapper();
        String uid = request.path("request").required("uid").asText();
        Status status = new Status();
        Response response = new Response();
        AdmissionReview admissionReview = new AdmissionReview();
        logger.info(request.path("request").path("resource").required("resource").asText());
        if (request.path("request").path("resource").required("resource").asText().toString().toLowerCase() != "pods") {
            status.setCode(403);
            status.setMessage("Resource NOT Authorized. \"D'Amato disse que vai subir ninguem!\"");
            response.setAllowed(false);
        } else {
            status.setCode(200);
            status.setMessage("Resource Authorized");
            response.setAllowed(true);
        }

        response.setUid(uid);
        response.setStatus(status);

        admissionReview.setResponse(response);
        String responseData;
        try {
            responseData = om.writeValueAsString(admissionReview);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        logger.info(request.toString());
        logger.info("RESPONSEDATA " + responseData);
        return responseData;
    }
}