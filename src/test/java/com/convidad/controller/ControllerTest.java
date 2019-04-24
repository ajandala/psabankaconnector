package com.convidad.controller;

import com.convidad.dto.PaymentDTO;
import com.convidad.service.PaymentService;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ControllerTest {

    private static final String BASE_PATH = "http://localhost:8001";

    private MockMvc mockMvc;

    @InjectMocks
    private Controller controller;

    @Mock
    private PaymentService paymentService;

    @Before
    public void setup() {

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void shouldReturnTransactionDetails() throws Exception {

        //GIVEN
        PaymentDTO paymentDTO = givenAPaymentDTO("expectedAccountFrom", "expectedAccountTo");
        Mockito.when(paymentService.getTransactions("accountToTest")).thenReturn(Lists.newArrayList(paymentDTO));

        //WHEN
        ResultActions result = mockMvc.perform(get(BASE_PATH + "//transactions/accountToTest"));

        //THEN
        result.andExpect(jsonPath("$[0].accountFrom", is("expectedAccountFrom")));
        result.andExpect(jsonPath("$[0].accountTo", is("expectedAccountTo")));
        result.andExpect(status().isOk());
    }

    private PaymentDTO givenAPaymentDTO(String accountFrom, String accountTo) {

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setAccountFrom(accountFrom);
        paymentDTO.setAccountTo(accountTo);

        return paymentDTO;
    }
}
