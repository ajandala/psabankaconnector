package com.convidad.service;

import com.convidad.bankA.client.BankAClient;
import com.convidad.bankA.common.dto.Payment;
import com.convidad.client.TrackerClient;
import com.convidad.dto.PaymentDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
public class PaymentServiceTest {


    @InjectMocks
    private PaymentService testSubject;

    @Mock
    private BankAClient bankAClient;

    @Mock
    private TrackerClient trackerClient;

    @Captor
    private ArgumentCaptor<Payment> paymentCaptor;

    @Test
    public void shouldCallBankAClientWithCorrectParameters_whenPayment() {

        //GIVEN
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setAccountFrom("accountFrom");
        paymentDTO.setAccountTo("accountTo");
        paymentDTO.setIdCardNumber("idCardNumber");
        paymentDTO.setQuantity(BigDecimal.TEN);

        //WHEN
        testSubject.payment(paymentDTO);

        //THEN
        Mockito.verify(bankAClient).transferMoney(paymentCaptor.capture());
        Payment paymentToVerify = paymentCaptor.getValue();
        Assert.assertEquals("accountFrom", paymentToVerify.getAccountNumberFrom());
        Assert.assertEquals("accountTo", paymentToVerify.getAccountNumberTo());
        Assert.assertEquals(BigDecimal.TEN, paymentToVerify.getQuantity());
    }

    @Test
    public void shouldTrackerClientWithCorrectParameters_whenPayment() {

        //GIVEN
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setAccountFrom("accountFrom");
        paymentDTO.setAccountTo("accountTo");
        paymentDTO.setIdCardNumber("idCardNumber");
        paymentDTO.setQuantity(BigDecimal.TEN);

        //WHEN
        testSubject.payment(paymentDTO);

        //THEN
        Mockito.verify(trackerClient).registerTransaction(paymentDTO);
    }

    @Test
    public void shouldReturnCorrectTransaction_whenGetTransactionsWithOneTransaction() {

        //GIVEN
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setAccountFrom("accountToTest");
        paymentDTO.setAccountTo("accountTo");
        paymentDTO.setIdCardNumber("idCardNumber");
        paymentDTO.setQuantity(BigDecimal.TEN);

        testSubject.payment(paymentDTO);

        //WHEN
        List<PaymentDTO> result = testSubject.getTransactions("accountToTest");

        //THEN
        PaymentDTO transactionToVerify = result.get(0);
        Assert.assertEquals("accountToTest", transactionToVerify.getAccountFrom());
        Assert.assertEquals("accountTo", transactionToVerify.getAccountTo());
        Assert.assertEquals("idCardNumber", transactionToVerify.getIdCardNumber());
        Assert.assertEquals(BigDecimal.TEN, transactionToVerify.getQuantity());
    }

    @Test
    public void shouldReturnTwoTransactions_whenGetTransactionsWithTwoTransactions() {

        //GIVEN
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setAccountFrom("accountToTest");
        paymentDTO.setAccountTo("accountTo");
        paymentDTO.setIdCardNumber("idCardNumber");
        paymentDTO.setQuantity(BigDecimal.TEN);

        testSubject.payment(paymentDTO);
        testSubject.payment(paymentDTO);


        //WHEN
        List<PaymentDTO> result = testSubject.getTransactions("accountToTest");

        //THEN
        Assert.assertEquals(2, result.size());
    }

    @Test
    public void shouldCallTrackerClient_whenGetTransactions() {

        //WHEN
        List<PaymentDTO> result = testSubject.getTransactions("accountToTest");

        //THEN
        Mockito.verify(trackerClient).registerTransactionRequestPerAccount("accountToTest");
    }


}
