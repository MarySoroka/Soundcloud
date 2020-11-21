package com.soundcloud.application;

import com.soundcloud.command.Command;
import com.soundcloud.command.CommandType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Application servlet
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 20,
        maxRequestSize = 1024 * 1024 * 20 * 5)
@WebServlet(name = "index", urlPatterns = "/")
public class ApplicationServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(ApplicationServlet.class);

    /**
     * method execute http get method
     * @param req http request
     * @param resp http response
     * @throws ServletException if something went wrong with servlet
     * @throws IOException if we have input or output exception
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            LOGGER.info("Started doGet in servlet");
            String command = req.getParameter(ApplicationConstants.COMMAND_NAME_PARAM);
            CommandType commandType = CommandType.of(command);
            Command commandBean = ApplicationContext.getInstance().getBean(commandType.name());
            commandBean.execute(req, resp);
        } catch (Exception e) {
            LOGGER.error(ApplicationConstants.COMMAND_EXCEPTION, e);
            req.getRequestDispatcher(ApplicationPage.ERROR.getPagePath()).forward(req, resp);
        }

    }
    /**
     * method execute http post method
     * @param req http request
     * @param resp http response
     * @throws ServletException if something went wrong with servlet
     * @throws IOException if we have input or output exception
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("Started doPost in servlet");
        doGet(req, resp);
    }


}
