package com.pbdcompany.controller;

import com.pbdcompany.Utils.JwtUtils;
import com.pbdcompany.dto.request.ReviewRequest;
import com.pbdcompany.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @InjectMocks
    private ReviewController reviewController;

    @Mock
    private ReviewService reviewService;

    @Mock
    private JwtUtils jwtUtils;

    private MockMvc mockMvc;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }

    @Test
    void addReview_success() throws Exception {
        ReviewRequest req = new ReviewRequest();
        req.setOrderItemId(1);
        req.setRating(5);
        req.setComment("good");

        mockMvc.perform(post("/api/customer/review/1")
                        .header("Authorization", "Bearer validtoken")
                        .contentType("application/json")
                        .content("{\"orderItemId\":1,\"rating\":5,\"comment\":\"good\"}"))
                .andExpect(status().isOk());
    }
}
