package com.pbdcompany.controller;

import com.pbdcompany.Utils.JwtUtils;
import com.pbdcompany.dto.request.UpdateMerchantProfileRequest;
import com.pbdcompany.dto.response.MerchantProfileResponse;
import com.pbdcompany.service.MerchantService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MerchantControllerTest {

    @InjectMocks
    private MerchantController merchantController;

    @Mock
    private MerchantService merchantService;

    @Mock
    private JwtUtils jwtUtils;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(merchantController).build();

        when(jwtUtils.getUsernameFromToken("validtoken")).thenReturn("merchant");
    }

    @Test
    void getCurrentMerchantProfile_success() throws Exception {
        MerchantProfileResponse response = new MerchantProfileResponse();
        when(merchantService.getMerchantProfileByUsername("merchant")).thenReturn(response);

        mockMvc.perform(get("/api/merchants/profile")
                        .header("Authorization", "Bearer validtoken"))
                .andExpect(status().isOk());
    }

    @Test
    void updateCurrentMerchantProfile_success() throws Exception {
        UpdateMerchantProfileRequest request = new UpdateMerchantProfileRequest();
        request.setPhoneNumber("12345678901");

        when(merchantService.updateMerchantProfile("merchant", request)).thenReturn(true);

        mockMvc.perform(put("/api/merchants/putProfile")
                        .header("Authorization", "Bearer validtoken")
                        .contentType("application/json")
                        .content("{\"phoneNumber\":\"12345678901\"}"))
                .andExpect(status().isOk());
    }
}
