package com.devflow.tracker.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.devflow.tracker.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.context.ActiveProfiles;
import com.devflow.tracker.repository.TaskRepository;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;



    @BeforeEach
    void resetDatabase() {
        taskRepository.deleteAll();
    }


    @Test
    void createAndFetchTask() throws Exception {
        String json = """
            {
              "title": "Write tests"
            }
            """;

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Write tests"))
                .andExpect(jsonPath("$.completed").value(false));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Write tests"));
    }

    @Test
    void updateTask() throws Exception {
        String createJson = """
            {
            "title": "Initial task"
            }
            """;

        String response = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String updateJson = """
            {
            "title": "Updated task",
            "completed": true
            }
            """;

        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated task"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void deleteTask() throws Exception {
        String json = """
            {
            "title": "Task to delete"
            }
            """;

        String response = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract id from response
        ObjectMapper mapper = new ObjectMapper();
        Long id = mapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/tasks/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/tasks/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTaskFailsWhenTitleIsBlank() throws Exception {
        String json = """
            {
            "title": ""
            }
            """;

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }


}
