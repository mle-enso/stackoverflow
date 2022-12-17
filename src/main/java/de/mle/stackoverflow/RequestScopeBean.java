package de.mle.stackoverflow;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import jakarta.servlet.http.HttpServletRequest;

@Component
@RequestScope
public class RequestScopeBean {
    public RequestScopeBean(HttpServletRequest request) {
    }
}
