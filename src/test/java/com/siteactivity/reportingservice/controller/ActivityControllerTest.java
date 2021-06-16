package com.siteactivity.reportingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siteactivity.reportingservice.ReportingServiceApplication;
import com.siteactivity.reportingservice.error.ApiErrorHandler;
import com.siteactivity.reportingservice.model.Hits;
import com.siteactivity.reportingservice.service.ActivityService;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestMethodArgumentResolver;
import static org.hamcrest.CoreMatchers.is;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ReportingServiceApplication.class)
@AutoConfigureMockMvc
public class ActivityControllerTest {

    @Autowired
    private ActivityController controller;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private MockMvc mockMvc;

    private String jsonString = asJsonString(new Hits(4));

    @Test
    public void testPostValueWithoutHeader_error() throws Exception
    {
        this.mockMvc.perform(post("/activity/crossover"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPostValueWithHeader_success() throws Exception
    {
        this.mockMvc.perform(post("/activity/crossover")
                .param("value", "4"))
                .andExpect(status().isOk());
    }

    @Test
    public void testPostValueWithJson_success() throws Exception
    {
        this.mockMvc.perform(post("/activity/crossover")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetValueReturnType_success() throws Exception
    {
        this.mockMvc.perform(get("/activity/crossover/total"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetValueMissingKey_success() throws Exception
    {
        this.mockMvc.perform(get("/activity/newKey/total"))
                .andExpect(status().isBadRequest());
    }


    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
