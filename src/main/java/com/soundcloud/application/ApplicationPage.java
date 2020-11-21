package com.soundcloud.application;

/**
 * Enum for keeping all application pages
 */
public enum ApplicationPage {
    LOGIN("/jsp/login_page.jsp"),
    LIBRARY("/jsp/library_page.jsp"),
    REGISTRY("/jsp/registry_page.jsp"),
    PROFILE("/jsp/profile_page.jsp"),
    ALBUM("/jsp/album_page.jsp"),
    HOME("/jsp/index.jsp"),
    ADMIN("/jsp/admin_page.jsp"),
    ERROR("/jsp/error_page.jsp"),
    POLICY("/jsp/help_page.jsp"),
    HELP("/jsp/policy_page.jsp"),
    UPLOAD("/jsp/upload_page.jsp");

    public String getPagePath() {
        return pagePath;
    }

    private final String pagePath;

    ApplicationPage(String pagePath) {
        this.pagePath = pagePath;
    }

    public static ApplicationPage of(String page) {
        if (page != null) {
            for (ApplicationPage command :
                    ApplicationPage.values()) {
                if (command.name().equalsIgnoreCase(page)) {
                    return command;
                }
            }
        }
        return ApplicationPage.HOME;
    }
}
