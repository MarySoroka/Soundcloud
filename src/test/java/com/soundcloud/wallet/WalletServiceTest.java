package com.soundcloud.wallet;

import com.soundcloud.connection.ConnectionManager;
import com.soundcloud.connection.ConnectionManagerImpl;
import com.soundcloud.dao.*;
import com.soundcloud.service.ServiceException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static com.soundcloud.application.ApplicationConstants.DATABASE_PROPERTY_FILE;

public class WalletServiceTest {
    @Mock
    private final WalletService walletService;
    @Mock
    private final WalletDao walletDao;
    private final Wallet wallet = new Wallet(1L, new BigDecimal(0));
    public WalletServiceTest() {
        DataSource dataSource = DataSourceImpl.getInstance(DATABASE_PROPERTY_FILE);
        TransactionManager transactionManager = new TransactionManagerImpl(dataSource);
        ConnectionManager connectionManager = new ConnectionManagerImpl(transactionManager, dataSource);
        WalletBuilder walletBuilder = new WalletBuilder();


        this.walletDao = Mockito.spy(new WalletDaoImpl(walletBuilder, connectionManager));
        this.walletService = Mockito.spy(new WalletServiceImpl(walletDao));
    }

    @Test
    public void saveDefaultWallet_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(1L).when(walletDao).save(Mockito.any(Wallet.class));
        Assert.assertEquals(new Long(1),walletService.saveDefaultWallet());
    }

    @Test
    public void setWalletAmount_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(true).when(walletDao).update(Mockito.anyLong(),Mockito.any(Wallet.class));
        Assert.assertTrue(walletService.setWalletAmount(1L, wallet));
    }

    @Test
    public void getWalletByWalletId_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(Optional.of(wallet)).when(walletDao).getById(Mockito.anyLong());
        Assert.assertTrue(walletService.getWalletByWalletId(1L).isPresent());
    }

    @Test
    public void deleteWallet_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(true).when(walletDao).delete(Mockito.anyLong());
        Assert.assertTrue(walletService.deleteWallet(1L));

    }

    @Test
    public void getAllWallets_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(Collections.EMPTY_LIST).when(walletDao).getAll();
        Assert.assertTrue(walletService.getAllWallets().isEmpty());
    }
}
