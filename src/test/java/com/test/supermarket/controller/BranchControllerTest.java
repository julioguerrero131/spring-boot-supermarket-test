package com.test.supermarket.controller;

import com.test.supermarket.dto.BranchDTO;
import com.test.supermarket.service.IBranchService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BranchController.class)
class BranchControllerTest {

    private static final String BRANCHES_URL = "/api/branches";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IBranchService branchService;

    @Test
    void shouldReturnAllBranches() throws Exception {
        given(branchService.getBranches()).willReturn(List.of(
                BranchDTO.builder().id(1L).name("Downtown Branch").address("123 Main St").build(),
                BranchDTO.builder().id(2L).name("North Branch").address("456 North Ave").build()
        ));

        mockMvc.perform(get(BRANCHES_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Downtown Branch"))
                .andExpect(jsonPath("$[0].address").value("123 Main St"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("North Branch"))
                .andExpect(jsonPath("$[1].address").value("456 North Ave"));

        verify(branchService).getBranches();
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoBranches() throws Exception {
        given(branchService.getBranches()).willReturn(List.of());

        mockMvc.perform(get(BRANCHES_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(branchService).getBranches();
    }

    @Test
    void shouldCreateBranchAndReturnCreatedStatus() throws Exception {
        BranchDTO createdBranch = BranchDTO.builder()
                .id(10L)
                .name("East Branch")
                .address("789 East Blvd")
                .build();
        given(branchService.createBranch(any(BranchDTO.class))).willReturn(createdBranch);

        mockMvc.perform(post(BRANCHES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "East Branch",
                                  "address": "789 East Blvd"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, BRANCHES_URL + "/10"))
                .andExpect(content().string(""));

        ArgumentCaptor<BranchDTO> captor = ArgumentCaptor.forClass(BranchDTO.class);
        verify(branchService).createBranch(captor.capture());

        BranchDTO request = captor.getValue();
        assertThat(request.getId()).isNull();
        assertThat(request.getName()).isEqualTo("East Branch");
        assertThat(request.getAddress()).isEqualTo("789 East Blvd");
    }

    @Test
    void shouldUpdateBranchAndReturnUpdatedBranch() throws Exception {
        BranchDTO updatedBranch = BranchDTO.builder()
                .id(5L)
                .name("Renovated Downtown Branch")
                .address("123 Main St")
                .build();
        given(branchService.updateBranch(eq(5L), any(BranchDTO.class))).willReturn(updatedBranch);

        mockMvc.perform(put(BRANCHES_URL + "/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Renovated Downtown Branch",
                                  "address": "123 Main St"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("Renovated Downtown Branch"))
                .andExpect(jsonPath("$.address").value("123 Main St"));

        ArgumentCaptor<BranchDTO> captor = ArgumentCaptor.forClass(BranchDTO.class);
        verify(branchService).updateBranch(eq(5L), captor.capture());

        BranchDTO request = captor.getValue();
        assertThat(request.getName()).isEqualTo("Renovated Downtown Branch");
        assertThat(request.getAddress()).isEqualTo("123 Main St");
    }

    @Test
    void shouldDeleteBranchAndReturnNoContent() throws Exception {
        mockMvc.perform(delete(BRANCHES_URL + "/{id}", 7L))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(branchService).deleteBranch(7L);
    }
}
