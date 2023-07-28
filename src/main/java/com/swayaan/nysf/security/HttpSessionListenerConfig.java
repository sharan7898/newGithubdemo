package com.swayaan.nysf.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swayaan.nysf.controller.UserAuditLogController;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


@Component
public class HttpSessionListenerConfig implements HttpSessionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpSessionListenerConfig.class);

    @Autowired UserAuditLogController userAuditLogController;

    public HttpSessionListenerConfig() {
        super();
        userAuditLogController = new UserAuditLogController();
    }


    /**
     * This method will be automatically called when session destroyed
     * @param sessionEvent
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
    	String sessionId = sessionEvent.getSession().getId();
    	userAuditLogController.saveUserLogoutTimeForSession(sessionId);
        LOGGER.info("-------Session Destroyed--------");
    }

}