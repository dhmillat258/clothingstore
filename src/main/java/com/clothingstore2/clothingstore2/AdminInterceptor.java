
package com.clothingstore2.clothingstore2;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        Boolean loggedIn =
                (Boolean) request.getSession()
                        .getAttribute("adminLoggedIn");

        if (Boolean.TRUE.equals(loggedIn)) {
            return true;
        }

        response.sendRedirect("/admin/login");

        return false;
    }
}