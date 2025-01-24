package agus.prasetyo.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testListOK() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders.get("/api/user?sortBy=createdAt&sortDirection=asc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJSb2xlcyI6IlJPTEVfQUxMIiwic3ViIjoidXNlcjEiLCJpYXQiOjE3Mzc3MTg0ODksImV4cCI6MTczNzc1NDQ4OX0.bg7YK0XAehegUW4XwyMjIm9YfqdMmoU2v9GH4jI1SAk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    public void testListFailed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user?sortBy=createdAt&sortDirection=asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(401));
    }

}
