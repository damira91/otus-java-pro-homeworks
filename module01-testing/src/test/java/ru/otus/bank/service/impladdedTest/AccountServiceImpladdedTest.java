package ru.otus.bank.service.impladdedTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.bank.dao.AccountDao;
import ru.otus.bank.entity.Account;
import ru.otus.bank.entity.Agreement;
import ru.otus.bank.service.exception.AccountException;
import ru.otus.bank.service.impl.AccountServiceImpl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AccountServiceImpladdedTest {
    @Mock
    AccountDao accountDao;

    @InjectMocks
    AccountServiceImpl accountServiceImpl;

    @Test
    public void testAddAccount() {
        Agreement agreement = new Agreement();
        agreement.setId(1L);
        String accountNumber = "123456";
        Integer type = 1;
        BigDecimal amount = new BigDecimal(1000);

        Account expectedAccount = new Account();
        expectedAccount.setAgreementId(agreement.getId());
        expectedAccount.setNumber(accountNumber);
        expectedAccount.setType(type);
        expectedAccount.setAmount(amount);

        ArgumentMatcher<Account> argMatcher = argument ->
                        argument.getAgreementId().equals(agreement.getId()) &&
                        argument.getNumber().equals(accountNumber) &&
                        argument.getType().equals(type) &&
                        argument.getAmount().equals(amount);

        when(accountDao.save(any())).thenReturn(expectedAccount);

        accountServiceImpl.addAccount(agreement, accountNumber, type, amount);

        verify(accountDao).save(argThat(argMatcher));
    }

    @Test
    public void testAddAccountWithCaptor() {
        Agreement agreement = new Agreement();
        agreement.setId(1L);
        agreement.setName("TestAgreement");
        String accountNumber = "123456";
        Integer type = 1;
        BigDecimal amount = new BigDecimal(1000);

        Account expectedAccount = new Account();
        expectedAccount.setAgreementId(agreement.getId());
        expectedAccount.setNumber(accountNumber);
        expectedAccount.setType(type);
        expectedAccount.setAmount(amount);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);

        when(accountDao.save(any())).thenReturn(expectedAccount);

        accountServiceImpl.addAccount(agreement, accountNumber, type, amount);

        verify(accountDao).save(captor.capture());

        Account capturedAccount = captor.getValue();

        assertEquals(expectedAccount.getAgreementId(), capturedAccount.getAgreementId());
        assertEquals(expectedAccount.getNumber(), capturedAccount.getNumber());
        assertEquals(expectedAccount.getType(), capturedAccount.getType());
        assertEquals(expectedAccount.getAmount(), capturedAccount.getAmount());
    }

    @Test
    public void testGetAccounts() {
        List<Account> expectedAccounts = Arrays.asList(
                new Account(1L, "123456", 1, new BigDecimal(1000)),
                new Account(2L, "789012", 2, new BigDecimal(2000))
        );

        when(accountDao.findAll()).thenReturn(expectedAccounts);

        AccountServiceImpl accountService = new AccountServiceImpl(accountDao);

        List<Account> actualAccounts = accountService.getAccounts();

        assertEquals(expectedAccounts.size(), actualAccounts.size());
        assertEquals(expectedAccounts, actualAccounts);
    }

    @Test
    public void testCharge_Successful() {
        Account account = new Account();
        account.setId(1L);
        account.setAmount(new BigDecimal(1000));

        when(accountDao.findById(1L)).thenReturn(Optional.of(account));

        AccountServiceImpl accountService = new AccountServiceImpl(accountDao);
        boolean result = accountService.charge(1L, new BigDecimal(100));

        assertTrue(result);
        assertEquals(new BigDecimal(900), account.getAmount());
    }

    @Test
    public void testCharge_NullAccountAmount() {
        Account account = new Account();
        account.setId(1L);
        account.setAmount(null);

        when(accountDao.findById(1L)).thenReturn(Optional.of(account));

        AccountServiceImpl accountService = new AccountServiceImpl(accountDao);
        assertThrows(AccountException.class, () -> {
            accountService.charge(1L, new BigDecimal(100));
        });

        verify(accountDao, times(1)).findById(1L);
        verify(accountDao, never()).save(any());
    }
}


