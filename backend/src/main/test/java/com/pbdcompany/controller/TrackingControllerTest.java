package com.pbdcompany.controller;

import com.pbdcompany.dto.response.OrderTrackingResponse;
import com.pbdcompany.service.TrackingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TrackingControllerTest {

    @InjectMocks
    private TrackingController trackingController;

    @Mock
    private TrackingService trackingService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(trackingController).build();
    }

    @Test
    void getTrackingInfo_success() throws Exception {
        OrderTrackingResponse response = new OrderTrackingResponse();
        when(trackingService.getTrackingInfoByOrderId(1)).thenReturn(response);

        mockMvc.perform(get("/api/order/tracking/1"))
                .andExpect(status().isOk());
    }
}
