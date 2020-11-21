package com.soundcloud.security;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.command.CommandType;
import com.soundcloud.role.Role;
import com.soundcloud.role.RoleType;
import com.soundcloud.user.UserDto;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Context for security of application
 */
public class SecurityContext {
    private static final SecurityContext SECURITY_CONTEXT = new SecurityContext();
    private final Map<String, UserDto> usersMap = new ConcurrentHashMap<>(ApplicationConstants.APPLICATION_INITIAL_CAPACITY);
    private final ThreadLocal<String> currentSessionId = new ThreadLocal<>();
    private final Properties securityProperties = new Properties();

    public static SecurityContext getInstance() {
        return SECURITY_CONTEXT;
    }

    public void initialize() {
        try (InputStream propertyStream = SecurityContext.class.getResourceAsStream(ApplicationConstants.SECURITY_PROPERTY_FILE)) {
            securityProperties.load(propertyStream);
        } catch (IOException e) {
            throw new IllegalStateException(ApplicationConstants.PROPERTY_FILE_NOT_FOUND_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    public String getCurrentSessionId() {
        return currentSessionId.get();
    }

    public void setCurrentSessionId(String sessionId) {
        this.currentSessionId.set(sessionId);
    }

    /**
     * method authorize user
     *
     * @param userDto   user
     * @param sessionId session id
     */
    public void authorize(UserDto userDto, String sessionId) {
        usersMap.put(sessionId, userDto);
        setCurrentSessionId(sessionId);
    }

    /**
     * method update current user information
     *
     * @param sessionId session id
     * @param userDto   user
     */
    public void updateCurrentUser(String sessionId, UserDto userDto) {
        usersMap.remove(sessionId);
        usersMap.put(sessionId, userDto);
    }

    public UserDto getCurrentUser() {
        String sessionId = getCurrentSessionId();
        return sessionId != null ? usersMap.get(sessionId) : null;
    }

    /**
     * method check if user has rights to execute command
     *
     * @param commandType command
     * @return if user has rights, then return true, else false
     */
    public boolean hasRights(CommandType commandType) {
        UserDto currentUser = getCurrentUser();
        if (currentUser == null) {
            UserDto notAuthorizedUser = new UserDto(new HashSet<>(Collections.singleton(new Role(RoleType.NOT_AUTHORIZED))));
            return hasRights(notAuthorizedUser, commandType);
        }
        return hasRights(currentUser, commandType);
    }

    /**
     * method check if user has rights to execute command
     *
     * @param userDto     user
     * @param commandType command
     * @return if user has rights, then return true, else false
     */
    public boolean hasRights(UserDto userDto, CommandType commandType) {
        String commandToRoles = securityProperties.getProperty(ApplicationConstants.PROPERTY_COMMAND + commandType.name());
        if (commandToRoles != null) {
            List<String> roles = Arrays.asList(commandToRoles.split(","));
            return roles.stream().anyMatch(userDto::haveRole);
        }
        return false;
    }

    /**
     * check if user is authorized
     *
     * @param sessionId session id
     * @return if user authorized, then return true, else false
     */
    public boolean isAuthorized(String sessionId) {
        return usersMap.containsKey(sessionId);
    }

    /**
     * method delete user by id
     *
     * @param userId user id
     */
    public void deleteUserById(Long userId) {
        usersMap.entrySet()
                .stream().
                filter(userMap -> userMap.getValue().getId().equals(userId))
                .findFirst()
                .ifPresent(userMap -> usersMap.remove(userMap.getKey(), userMap.getValue()));
    }

    /**
     * method delete user by session id
     *
     * @param sessionId session id
     */
    public boolean deleteUser(String sessionId) {
        return usersMap.remove(sessionId) != null;
    }

    /**
     * method unauthorized user
     *
     * @param sessionId session id
     */
    public void unauthorized(String sessionId) {
        usersMap.remove(sessionId);
    }

    /**
     * method destroy security context
     */
    public void destroy() {
        currentSessionId.remove();
        usersMap.clear();
    }
}
