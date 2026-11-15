package com.test.supermarket.controller;

import com.test.supermarket.dto.SaleDTO;
import com.test.supermarket.dto.SaleDetailDTO;
import com.test.supermarket.model.SaleState;
import com.test.supermarket.service.ISaleService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SaleController.class)
class SaleControllerTest {

    private static final String SALES_URL = "/api/sales";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ISaleService saleService;

    @Test
    void shouldReturnAllSalesWithTheirDetails() throws Exception {
        given(saleService.getSales()).willReturn(List.of(
                SaleDTO.builder()
                        .id(1L)
                        .date(LocalDate.of(2026, 11, 10))
                        .state(SaleState.PAID)
                        .branchId(1L)
                        .details(List.of(
                                SaleDetailDTO.builder()
                                        .id(1L)
                                        .nameProd("Whole Milk")
                                        .quantProd(2)
                                        .price(2.50)
                                        .subtotal(5.00)
                                        .build(),
                                SaleDetailDTO.builder()
                                        .id(2L)
                                        .nameProd("Whole Wheat Bread")
                                        .quantProd(3)
                                        .price(3.00)
                                        .subtotal(9.00)
                                        .build()
                        ))
                        .total(14.00)
                        .build(),
                SaleDTO.builder()
                        .id(2L)
                        .date(LocalDate.of(2026, 11, 11))
                        .state(SaleState.PENDING)
                        .branchId(2L)
                        .details(List.of(
                                SaleDetailDTO.builder()
                                        .id(3L)
                                        .nameProd("Greek Yogurt")
                                        .quantProd(1)
                                        .price(4.25)
                                        .subtotal(4.25)
                                        .build()
                        ))
                        .total(4.25)
                        .build()
        ));

        mockMvc.perform(get(SALES_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].date").value("2026-11-10"))
                .andExpect(jsonPath("$[0].state").value("PAID"))
                .andExpect(jsonPath("$[0].branchId").value(1))
                .andExpect(jsonPath("$[0].details").isArray())
                .andExpect(jsonPath("$[0].details.length()").value(2))
                .andExpect(jsonPath("$[0].details[0].nameProd").value("Whole Milk"))
                .andExpect(jsonPath("$[0].details[0].quantProd").value(2))
                .andExpect(jsonPath("$[0].details[0].price").value(2.50))
                .andExpect(jsonPath("$[0].details[0].subtotal").value(5.00))
                .andExpect(jsonPath("$[0].details[1].nameProd").value("Whole Wheat Bread"))
                .andExpect(jsonPath("$[0].details[1].quantProd").value(3))
                .andExpect(jsonPath("$[0].details[1].subtotal").value(9.00))
                .andExpect(jsonPath("$[0].total").value(14.00))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].details.length()").value(1))
                .andExpect(jsonPath("$[1].total").value(4.25));

        verify(saleService).getSales();
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoSales() throws Exception {
        given(saleService.getSales()).willReturn(List.of());

        mockMvc.perform(get(SALES_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(saleService).getSales();
    }

    @Test
    void shouldCreateSaleWithDetailsAndReturnCreatedSale() throws Exception {
        SaleDTO createdSale = SaleDTO.builder()
                .id(100L)
                .date(LocalDate.of(2026, 11, 10))
                .state(SaleState.PAID)
                .branchId(1L)
                .details(List.of(
                        SaleDetailDTO.builder()
                                .id(1L)
                                .nameProd("Whole Milk")
                                .quantProd(2)
                                .price(2.50)
                                .subtotal(5.00)
                                .build(),
                        SaleDetailDTO.builder()
                                .id(2L)
                                .nameProd("Whole Wheat Bread")
                                .quantProd(3)
                                .price(3.00)
                                .subtotal(9.00)
                                .build()
                ))
                .total(14.00)
                .build();
        given(saleService.createSale(any(SaleDTO.class))).willReturn(createdSale);

        mockMvc.perform(post(SALES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "date": "2026-11-10",
                                  "state": "PAID",
                                  "branchId": 1,
                                  "details": [
                                    {
                                      "nameProd": "Whole Milk",
                                      "quantProd": 2,
                                      "price": 2.50
                                    },
                                    {
                                      "nameProd": "Whole Wheat Bread",
                                      "quantProd": 3,
                                      "price": 3.00
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, SALES_URL + "/100"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.date").value("2026-11-10"))
                .andExpect(jsonPath("$.state").value("PAID"))
                .andExpect(jsonPath("$.branchId").value(1))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details.length()").value(2))
                .andExpect(jsonPath("$.details[0].nameProd").value("Whole Milk"))
                .andExpect(jsonPath("$.details[0].quantProd").value(2))
                .andExpect(jsonPath("$.details[0].subtotal").value(5.00))
                .andExpect(jsonPath("$.details[1].nameProd").value("Whole Wheat Bread"))
                .andExpect(jsonPath("$.details[1].quantProd").value(3))
                .andExpect(jsonPath("$.details[1].subtotal").value(9.00))
                .andExpect(jsonPath("$.total").value(14.00));

        ArgumentCaptor<SaleDTO> captor = ArgumentCaptor.forClass(SaleDTO.class);
        verify(saleService).createSale(captor.capture());

        SaleDTO request = captor.getValue();
        assertThat(request.getId()).isNull();
        assertThat(request.getDate()).isEqualTo(LocalDate.of(2026, 11, 10));
        assertThat(request.getState()).isEqualTo(SaleState.PAID);
        assertThat(request.getBranchId()).isEqualTo(1L);
        assertThat(request.getDetails()).hasSize(2);
        assertThat(request.getDetails().get(0).getNameProd()).isEqualTo("Whole Milk");
        assertThat(request.getDetails().get(0).getQuantProd()).isEqualTo(2);
        assertThat(request.getDetails().get(0).getPrice()).isEqualTo(2.50);
        assertThat(request.getDetails().get(1).getNameProd()).isEqualTo("Whole Wheat Bread");
        assertThat(request.getDetails().get(1).getQuantProd()).isEqualTo(3);
        assertThat(request.getDetails().get(1).getPrice()).isEqualTo(3.00);
    }

    @Test
    void shouldUpdateSaleAndReturnUpdatedSale() throws Exception {
        SaleDTO updatedSale = SaleDTO.builder()
                .id(5L)
                .date(LocalDate.of(2026, 11, 12))
                .state(SaleState.CANCELLED)
                .branchId(2L)
                .details(List.of(
                        SaleDetailDTO.builder()
                                .id(4L)
                                .nameProd("Greek Yogurt")
                                .quantProd(1)
                                .price(4.25)
                                .subtotal(4.25)
                                .build()
                ))
                .total(4.25)
                .build();
        given(saleService.updateSale(eq(5L), any(SaleDTO.class))).willReturn(updatedSale);

        mockMvc.perform(put(SALES_URL + "/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "date": "2026-11-12",
                                  "state": "CANCELLED",
                                  "branchId": 2,
                                  "details": [
                                    {
                                      "nameProd": "Greek Yogurt",
                                      "quantProd": 1,
                                      "price": 4.25
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.date").value("2026-11-12"))
                .andExpect(jsonPath("$.state").value("CANCELLED"))
                .andExpect(jsonPath("$.branchId").value(2))
                .andExpect(jsonPath("$.details.length()").value(1))
                .andExpect(jsonPath("$.details[0].nameProd").value("Greek Yogurt"))
                .andExpect(jsonPath("$.total").value(4.25));

        ArgumentCaptor<SaleDTO> captor = ArgumentCaptor.forClass(SaleDTO.class);
        verify(saleService).updateSale(eq(5L), captor.capture());

        SaleDTO request = captor.getValue();
        assertThat(request.getState()).isEqualTo(SaleState.CANCELLED);
        assertThat(request.getBranchId()).isEqualTo(2L);
        assertThat(request.getDetails()).hasSize(1);
        assertThat(request.getDetails().get(0).getNameProd()).isEqualTo("Greek Yogurt");
    }

    @Test
    void shouldRejectSaleWhenStateIsNotInEnglish() throws Exception {
        mockMvc.perform(post(SALES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "date": "2026-11-10",
                                  "state": "REGISTRADA",
                                  "branchId": 1,
                                  "details": [
                                    {
                                      "nameProd": "Whole Milk",
                                      "quantProd": 1,
                                      "price": 2.50
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(saleService);
    }

    @Test
    void shouldDeleteSaleAndReturnNoContent() throws Exception {
        mockMvc.perform(delete(SALES_URL + "/{id}", 7L))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(saleService).deleteSale(7L);
    }
}
