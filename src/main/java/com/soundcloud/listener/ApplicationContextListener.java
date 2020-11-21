package com.soundcloud.listener;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.application.ApplicationContext;
import com.soundcloud.security.SecurityContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import static com.soundcloud.application.ApplicationConstants.LISTENER_CONTEXT_INITIALIZE;

@WebListener
public class ApplicationContextListener implements ServletContextListener {
    private static final Logger LOGGER = LogManager.getLogger(ApplicationContextListener.class);

    /**
     * method initialize listener
     * @param sce servlet context event
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ApplicationContext.initialize();
        SecurityContext.getInstance().initialize();
        LOGGER.info(LISTENER_CONTEXT_INITIALIZE);
    }
    /**
     * method destroy listener
     * @param sce servlet context event
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ApplicationContext.getInstance().destroy();
        SecurityContext.getInstance().destroy();
        LOGGER.info(ApplicationConstants.LISTENER_CONTEXT_DESTROYED);

    }

}