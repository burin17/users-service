package com.gmail.burinigor7.apigatewayservice.filter;

import com.gmail.burinigor7.apigatewayservice.security.JwtUser;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;

@Component
public class AdminDeletionFilter extends ZuulFilter {


    public static final int FILTER_ORDER = 10;
    public static final String FILTER_TYPE = "pre";

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public String filterType() {
        return FILTER_TYPE;
    }

    @Override
    public boolean shouldFilter() {
        final String path = "/admin/users";
        final String methodName = "delete";
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        return request.getRequestURI().contains(path) &&
                request.getMethod().equalsIgnoreCase(methodName);
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        Object originalUri = context.get(FilterConstants.REQUEST_URI_KEY);
        context.set(FilterConstants.REQUEST_URI_KEY, originalUri + "/" + getCurrentUserId());
        return null;
    }

    private Long getCurrentUserId() {
        return ((JwtUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).getUser().getId();
    }
}
