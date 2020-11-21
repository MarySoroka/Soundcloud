package com.soundcloud.application;

import com.soundcloud.album.*;
import com.soundcloud.artist.ArtistBuilder;
import com.soundcloud.artist.ArtistDaoImpl;
import com.soundcloud.artist.ArtistServiceImpl;
import com.soundcloud.bean.BeanEnrolment;
import com.soundcloud.bean.BeanEnrolmentImpl;
import com.soundcloud.command.GetPageCommand;
import com.soundcloud.command.GetUploadPageCommand;
import com.soundcloud.command.SearchCommand;
import com.soundcloud.connection.ConnectionManager;
import com.soundcloud.connection.ConnectionManagerImpl;
import com.soundcloud.dao.*;
import com.soundcloud.dao.DataSourceException;
import com.soundcloud.role.RoleBuilder;
import com.soundcloud.role.RoleDaoImpl;
import com.soundcloud.role.RoleServiceImpl;
import com.soundcloud.role.UpdateRolesCommand;
import com.soundcloud.subscription.SubscriptionBuilder;
import com.soundcloud.subscription.SubscriptionDaoImpl;
import com.soundcloud.subscription.SubscriptionServiceImpl;
import com.soundcloud.track.*;
import com.soundcloud.user.*;
import com.soundcloud.validation.*;
import com.soundcloud.wallet.RefillWalletBalanceCommand;
import com.soundcloud.wallet.WalletBuilder;
import com.soundcloud.wallet.WalletDaoImpl;
import com.soundcloud.wallet.WalletServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.soundcloud.application.ApplicationConstants.DATABASE_PROPERTY_FILE;
import static com.soundcloud.application.ApplicationConstants.DESTROY_POOL_EXCEPTION;

/**
 * Singleton, that build all application
 */
public class ApplicationContext implements BeanEnrolment {
    private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);
    private static final Lock INITIALIZE_LOCK = new ReentrantLock();
    private static final Logger LOGGER = LogManager.getLogger(ApplicationContext.class);
    private static ApplicationContext instance;
    private final BeanEnrolment beanRegistry = new BeanEnrolmentImpl();

    private ApplicationContext() {

    }

    /**
     * initialisation of context
     */
    public static void initialize() {
        INITIALIZE_LOCK.lock();
        try {
            if (instance != null && INITIALIZED.get()) {
                throw new IllegalStateException("Context was already initialized");
            } else {
                ApplicationContext context = new ApplicationContext();
                context.init();
                instance = context;
                INITIALIZED.set(true);
                LOGGER.info("Initialize context");
            }
        } finally {
            INITIALIZE_LOCK.unlock();
        }
    }

    public static ApplicationContext getInstance() {

        if (instance != null && INITIALIZED.get()) {
            return instance;
        } else {
            throw new IllegalStateException("Context wasn't initialized");
        }
    }

    /**
     * initialisation of whole application with dependencies
     */
    private void init() {
        DataSource dataSource = DataSourceImpl.getInstance(DATABASE_PROPERTY_FILE);
        TransactionManager transactionManager = new TransactionManagerImpl(dataSource);
        ConnectionManager connectionManager = new ConnectionManagerImpl(transactionManager, dataSource);
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor(transactionManager);
        UserBuilder userBuilder = new UserBuilder();
        RoleBuilder roleBuilder = new RoleBuilder();
        AlbumBuilder albumBuilder = new AlbumBuilder();
        TrackBuilder trackBuilder = new TrackBuilder();
        WalletBuilder walletBuilder = new WalletBuilder();
        ArtistBuilder artistBuilder = new ArtistBuilder();
        SubscriptionBuilder subscriptionBuilder = new SubscriptionBuilder();
        Map<Class<? extends Annotation>, FieldValidator> validatorMap = new HashMap<>();
        validatorMap.put(MaxLength.class, new MaxLengthFieldValidator());
        validatorMap.put(MinLength.class, new MinLengthFieldValidator());
        validatorMap.put(NotEmpty.class, new NotEmptyFieldValidator());
        validatorMap.put(Regex.class, new RegexFieldValidator());
        BeanValidator beanValidator = new BeanValidatorImpl(validatorMap);

        enrollBean(subscriptionBuilder);
        enrollBean(artistBuilder);
        enrollBean(walletBuilder);
        enrollBean(albumBuilder);
        enrollBean(trackBuilder);
        enrollBean(beanValidator);
        enrollBean(userBuilder);
        enrollBean(roleBuilder);
        enrollBean(dataSource);
        enrollBean(transactionManager);
        enrollBean(connectionManager);
        enrollBean(transactionInterceptor);


        enrollBean(SubscriptionDaoImpl.class);
        enrollBean(WalletDaoImpl.class);
        enrollBean(UserDaoImpl.class);
        enrollBean(RoleDaoImpl.class);
        enrollBean(TrackDaoImpl.class);
        enrollBean(AlbumDaoImpl.class);
        enrollBean(ArtistDaoImpl.class);

        enrollBean(SubscriptionServiceImpl.class);
        enrollBean(ArtistServiceImpl.class);
        enrollBean(WalletServiceImpl.class);
        enrollBean(UserServiceImpl.class);
        enrollBean(RoleServiceImpl.class);
        enrollBean(AlbumServiceImpl.class);
        enrollBean(TrackServiceImpl.class);

        enrollBean(UnsubscribeUserCommand.class);
        enrollBean(SubscribeUserCommand.class);
        enrollBean(UpdateRolesCommand.class);
        enrollBean(GetAdminUserPageCommand.class);
        enrollBean(DeleteUserCommand.class);
        enrollBean(UploadTrackCommand.class);
        enrollBean(DeleteTrackCommand.class);
        enrollBean(UnfollowUserCommand.class);
        enrollBean(DeleteLikeAlbumCommand.class);
        enrollBean(EditProfileInformationCommand.class);
        enrollBean(UploadAlbumCommand.class);
        enrollBean(DeleteAlbumCommand.class);
        enrollBean(EditAlbumCommand.class);
        enrollBean(LoginUserCommand.class);
        enrollBean(LogoutUserCommand.class);
        enrollBean(RegisterUserCommand.class);
        enrollBean(GetUploadPageCommand.class);
        enrollBean(FollowUserCommand.class);
        enrollBean(GetUserLibraryCommand.class);
        enrollBean(GetAlbumCommand.class);
        enrollBean(GetAllUserArtistAlbumsCommand.class);
        enrollBean(GetAllUsersCommand.class);
        enrollBean(GetAllUserAlbumsCommand.class);
        enrollBean(GetPageCommand.class);
        enrollBean(GetFollowerUsersCommand.class);
        enrollBean(GetLikedAlbumsCommand.class);
        enrollBean(GetFollowUsersCommand.class);
        enrollBean(SaveLikeAlbumCommand.class);
        enrollBean(RefillWalletBalanceCommand.class);
        enrollBean(SearchCommand.class);
        LOGGER.info("Enrolled beans");
    }

    /**
     * method enroll bean
     *
     * @param bean bean
     */
    @Override
    public <T> void enrollBean(T bean) {
        beanRegistry.enrollBean(bean);
    }

    /**
     * method enroll bean by bean class
     *
     * @param beanClass beanClass
     */
    @Override
    public <T> void enrollBean(Class<T> beanClass) {
        beanRegistry.enrollBean(beanClass);
    }

    /**
     * get bean by bean class
     * @param beanClass bean class
     * @return bean
     */
    @Override
    public <T> T getBean(Class<T> beanClass) {
        return beanRegistry.getBean(beanClass);
    }
    /**
     * get bean by bean name
     * @param beanName bean name
     * @return bean
     */
    @Override
    public <T> T getBean(String beanName) {
        return beanRegistry.getBean(beanName);
    }

    /**
     * method remove bean
     * @param bean bean
     * @return if bean has been deleted, then return true, else false
     */
    @Override
    public <T> boolean removeBean(T bean) {
        return beanRegistry.removeBean(bean);
    }

    /**
     * method destroy application context
     */
    @Override
    public void destroy() {
        try {
            DataSourceImpl bean = getBean(DataSourceImpl.class);
            bean.closeConnections();
            beanRegistry.destroy();
        } catch (DataSourceException e) {
            LOGGER.error(DESTROY_POOL_EXCEPTION.replace("0", e.getMessage()));
        }
    }
}
