package com.kottragu.umlproject.config;

import com.vaadin.flow.server.ServletHelper;
import com.vaadin.flow.shared.ApplicationConstants;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

public class SecurityUtils {
    private SecurityUtils() {
    }

    static boolean isFrameworkInternalRequest(HttpServletRequest request) {
        String paramValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
        return paramValue != null
                && Stream.of(ServletHelper.RequestType.values())
                .anyMatch(r -> r.getIdentifier().equals(paramValue));
    }
}
