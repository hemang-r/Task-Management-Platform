package com.tmp.user_auth_service.utills;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmp.user_auth_service.dao.UserDao;
import com.tmp.user_auth_service.dto.TmpUserDetail;
import com.tmp.user_auth_service.entity.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CommonServices commonServices;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try {
            if (resolver == null) {
                ServletContext servletContext = request.getServletContext();
                WebApplicationContext webApplicationContext =
                        WebApplicationContextUtils.getWebApplicationContext(servletContext);

                resolver = webApplicationContext.getBean(
                        "handlerExceptionResolver",
                        HandlerExceptionResolver.class);
            }

            if (tokenUtil == null) {
                ServletContext servletContext = request.getServletContext();
                WebApplicationContext webApplicationContext =
                        WebApplicationContextUtils.getWebApplicationContext(servletContext);

                tokenUtil = webApplicationContext.getBean(TokenUtil.class);
            }


            if (commonServices == null) {
                ServletContext servletContext = request.getServletContext();
                WebApplicationContext webApplicationContext =
                        WebApplicationContextUtils.getWebApplicationContext(servletContext);

                commonServices = webApplicationContext.getBean(CommonServices.class);
            }

            String authHeader =
                    request.getHeader(ApplicationConstants.AUTH_HEADER);
            

            if (authHeader != null
                    && authHeader.startsWith(ApplicationConstants.BEARER)) {

            	String token = authHeader.substring(7);
            	 String userId =
                         tokenUtil.getUserIdFromToken(token);
            	 
            	 User user =
                         userDao.findById(
                                 UUID.fromString(userId))
                                 .orElse(null);

            	 if (user != null
                         && tokenUtil.validateToken(token)) {


//                            UsernamePasswordAuthenticationToken authenticationToken =
//                                    new UsernamePasswordAuthenticationToken(
//                                            new TmpUserDetail(user),
//                                            user.getPasswordHash(),
//                                            new ArrayList<>());
//
//                            Authentication authentication = authenticationToken;
//
//                            SecurityContextHolder.getContext()
//                                    .setAuthentication(authentication);
//
//                            filterChain.doFilter(request, response);

            		 TmpUserDetail userDetails =
                             new TmpUserDetail(user);

                     UsernamePasswordAuthenticationToken authentication =
                             new UsernamePasswordAuthenticationToken(
                                     userDetails,
                                     null,
                                     userDetails.getAuthorities());

                     SecurityContextHolder
                             .getContext()
                             .setAuthentication(authentication);
                     filterChain.doFilter(request, response);
                        
                    
                } else {
                    throw new BadCredentialsException(
                            commonServices.getMessageByCode(
                            		ErrorDataEnum.INVALID_TOKEN_MESSAGE.getCode()));
                }
            } else {
                throw new BadCredentialsException(
                        commonServices.getMessageByCode(
                        		ErrorDataEnum.INVALID_TOKEN_MESSAGE.getCode()));
            }

        } catch (BadCredentialsException | LockedException e) {
            resolver.resolveException(request, response, null, e);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {
        String path = request.getServletPath();

        return !path.startsWith("/api")
                || path.equals("/api/v1/auth/register")
                || path.equals("/api/v1/auth/login");
    }
}