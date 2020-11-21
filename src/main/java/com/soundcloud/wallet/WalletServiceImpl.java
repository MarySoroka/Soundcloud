package com.soundcloud.wallet;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.bean.Bean;
import com.soundcloud.dao.TransactionSupport;
import com.soundcloud.dao.Transactional;
import com.soundcloud.dao.DaoException;
import com.soundcloud.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Bean
@TransactionSupport
public class WalletServiceImpl implements WalletService {
    private static final Logger LOGGER = LogManager.getLogger(WalletServiceImpl.class);
    private final WalletDao walletDao;

    public WalletServiceImpl(WalletDao walletDao) {
        this.walletDao = walletDao;
    }


    @Transactional
    @Override
    public Long saveDefaultWallet() throws ServiceException {
        try {
            return walletDao.save(new Wallet(2L,new BigDecimal(0)));
        }catch (DaoException e) {
            LOGGER.error(ApplicationConstants.SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(ApplicationConstants.SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public boolean setWalletAmount(Long key, Wallet wallet) throws ServiceException {
        try {
            return walletDao.update(key,wallet);
        } catch (DaoException e) {
            LOGGER.error(ApplicationConstants.SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(ApplicationConstants.SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public Optional<Wallet> getWalletByWalletId(Long walletId) throws ServiceException {
        try {
            return walletDao.getById(walletId);
        } catch (DaoException e) {
            LOGGER.error(ApplicationConstants.SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(ApplicationConstants.SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        }

    }

    @Override
    public boolean deleteWallet(Long key) throws ServiceException {
        try {
            return walletDao.delete(key);
        } catch (DaoException e) {
            LOGGER.error(ApplicationConstants.SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(ApplicationConstants.SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public List<Wallet> getAllWallets() throws ServiceException {
        try {
            return walletDao.getAll();
        } catch (DaoException e) {
            LOGGER.error(ApplicationConstants.SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(ApplicationConstants.SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }
}
