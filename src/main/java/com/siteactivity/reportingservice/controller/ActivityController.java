package com.siteactivity.reportingservice.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.siteactivity.reportingservice.service.ActivityService;
import com.siteactivity.reportingservice.model.Hits;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @RequestMapping(value = "/")
    public void redirectToServices(HttpServletResponse httpServletResponse){
        httpServletResponse.setHeader("Location", "/activity");
        httpServletResponse.setStatus(302);
    }

    @RequestMapping(path ="/activity")
    public String Activity(){
        return "<h4>This is the Home Page of Site Activity Reporting Service</h4><br>"
        +"<b><u>Post Request:</b></u> curl -H \"Content-Type: application/json\" -d {\\\"value\\\":4} http://localhost:8080/activity/{key}" + "<br><br>"
                +"<b><u>Get Request:</b></u> curl -H \"Content-Type: application/json\" http://localhost:8080/activity/{key}/total";
    }

    @PostMapping(value ="/activity/{key}", consumes = "application/json")
    public ResponseEntity postActivity(@PathVariable final String key, @RequestBody Hits hit) {
        if(activityService.postActivity(key, hit.getValue())) {
            return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
        } else {
            return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value ="/activity/{key}")
    public ResponseEntity postActivity(@PathVariable final String key, @RequestParam("value") int value) {
        if(activityService.postActivity(key, value)) {
            return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
        } else {
            return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value ="/activity/{key}/total", produces = "application/json")
    public ResponseEntity<JSONObject> getActivity(@PathVariable final String key){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        String value = activityService.getActivity(key);
        JSONObject entity = new JSONObject();

        if(value.equalsIgnoreCase("No record")) {
            entity.put("Message", "No Activity recorded for the website");
            return new ResponseEntity<>(entity, headers, HttpStatus.BAD_REQUEST);
        }
        else if (value.equalsIgnoreCase("Error")) {
            entity.put("Message", "Error while retrieving data");
            return new ResponseEntity<>(entity, headers, HttpStatus.BAD_GATEWAY);
        }
        else {
            entity.put("value", new Integer(value));
            return new ResponseEntity<>(entity, headers, HttpStatus.OK);
        }
    }
}

@JsonSerialize
class EmptyJsonResponse { }