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
public class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testListOK() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders.get("/api/article?sortBy=createdAt&sortDirection=asc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJSb2xlcyI6IlJPTEVfQUxMIiwic3ViIjoidXNlcjEiLCJpYXQiOjE3Mzc3MTg0ODksImV4cCI6MTczNzc1NDQ4OX0.bg7YK0XAehegUW4XwyMjIm9YfqdMmoU2v9GH4jI1SAk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    public void testListFailed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/article?sortBy=createdAt&sortDirection=asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(401));
    }

    @Test
    public void testCreateOK() throws Exception {
        // Data request JSON
        String requestBody = "{ \"title\": \"test title\", \"content\": \"test content\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/article/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJSb2xlcyI6IlJPTEVfQUxMIiwic3ViIjoidXNlcjEiLCJpYXQiOjE3Mzc3MTg0ODksImV4cCI6MTczNzc1NDQ4OX0.bg7YK0XAehegUW4XwyMjIm9YfqdMmoU2v9GH4jI1SAk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testCreateFailed() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/article/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJSb2xlcyI6IlJPTEVfQUxMIiwic3ViIjoidXNlcjEiLCJpYXQiOjE3Mzc3MTg0ODksImV4cCI6MTczNzc1NDQ4OX0.bg7YK0XAehegUW4XwyMjIm9YfqdMmoU2v9GH4jI1SAk"))
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.success").value(false));
    }


    @Test
    public void testUpdateOK() throws Exception {
        // Data request JSON
        String requestBody = "{ \"id\": \"41b66766-58cb-4a96-9eed-558b188b23e3\", \"title\": \"test title\", \"content\": \"test content\" }";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/article/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJSb2xlcyI6IlJPTEVfQUxMIiwic3ViIjoidXNlcjEiLCJpYXQiOjE3Mzc3MTg0ODksImV4cCI6MTczNzc1NDQ4OX0.bg7YK0XAehegUW4XwyMjIm9YfqdMmoU2v9GH4jI1SAk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testUpdateIdFailed() throws Exception {
        // Data request JSON
        String requestBody = "{ \"id\": \"41b66766-58cb-4a96-9eed-558b188b23e31\", \"title\": \"test title\", \"content\": \"test content\" }";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/article/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJSb2xlcyI6IlJPTEVfQUxMIiwic3ViIjoidXNlcjEiLCJpYXQiOjE3Mzc3MTg0ODksImV4cCI6MTczNzc1NDQ4OX0.bg7YK0XAehegUW4XwyMjIm9YfqdMmoU2v9GH4jI1SAk"))
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("UUID string too large"));
    }

}
