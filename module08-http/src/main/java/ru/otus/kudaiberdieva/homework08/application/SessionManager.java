package ru.otus.kudaiberdieva.homework08.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class SessionManager {
    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);
    private static final long TEN_MIN = 10 * 60 * 1000L;
    private static final String SESSIONID = "SESSIONID";
    private static Map<String, Date> sessions;
    private static SessionManager instance;
    private String sessionId;
    private String cookies;

    static {
        sessions = new HashMap<>();
    }

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
        this.sessionId = getSessionIdFromCookies(cookies);
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public String getSessionIdFromCookies(String cookies) {
        if (cookies == null) return null;
        int start = cookies.indexOf(SESSIONID) + SESSIONID.length() + 1;
        int stop = cookies.indexOf(";", start);
        if (stop == -1) stop = cookies.length();
        String sessionId = cookies.substring(start, stop);
        setSessionId(sessionId);
        return sessionId;
    }

    private void setSessionId(String sessionId) {
        if (sessionId == null || !sessions.containsKey(sessionId)) {
            sessionId = UUID.randomUUID().toString();
            sessions.put(sessionId, new Date());
        } else {
            Date sessionIdDate = sessions.get(sessionId);
            if (new Date().getTime() - sessionIdDate.getTime() > TEN_MIN) {
                sessions.remove(sessionId);
                sessionId = UUID.randomUUID().toString();
                sessions.put(sessionId, new Date());
            }
        }
    }
}