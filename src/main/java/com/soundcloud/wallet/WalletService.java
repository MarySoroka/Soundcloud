package com.soundcloud.wallet;

import com.soundcloud.service.ServiceException;

import java.util.List;
import java.util.Optional;

public interface WalletService {
    /**
     * save default wallet for new user
     *
     * @return wallet id
     * @throws ServiceException if we get exception from dao
     */
    Long saveDefaultWallet() throws ServiceException;

    /**
     * set wallet amount
     *
     * @param key    wallet id
     * @param wallet wallet object
     * @return if wallet amount has been set successfully, then return true, else false
     * @throws ServiceException if we get exception from dao
     */
    boolean setWalletAmount(Long key, Wallet wallet) throws ServiceException;

    /**
     * method return wallet by wallet id
     *
     * @param walletId wallet id
     * @return if wallet has been found, then return optional of wallet, else Optional.empty()
     * @throws ServiceException if we get exception from dao
     */
    Optional<Wallet> getWalletByWalletId(Long walletId) throws ServiceException;

    /**
     * method delete wallet
     *
     * @param key wallet id
     * @return if wallet was deleted, then return true, else false
     * @throws ServiceException if we get exception from dao
     */
    boolean deleteWallet(Long key) throws ServiceException;

    /**
     * method return the list of all wallets
     *
     * @return the list of all wallets
     * @throws ServiceException if we get exception from dao
     */
    List<Wallet> getAllWallets() throws ServiceException;
}
