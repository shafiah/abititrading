package com.abiti_app_service.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.abiti_app_service.dao.UsersDao;
import com.abiti_app_service.models.Users;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class DeviceSessionFilter extends OncePerRequestFilter {

    @Autowired
    private UsersDao usersDao;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String phone = request.getHeader("phoneNumber");
        String deviceId = request.getHeader("deviceId");

        // login and register API skip
        String path = request.getRequestURI();

        if (path.contains("/user/login") || path.contains("/user/create") || path.contains("/h2-console")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (phone != null && deviceId != null) {

            Users user = usersDao.findByPhoneNumber(phone);

            if (user != null) {

                if (!deviceId.equals(user.getDeviceId())) {

                	System.out.println("Phone Header: " + phone);
                	System.out.println("Device Header: " + deviceId);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Session expired. Please login again.");
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
