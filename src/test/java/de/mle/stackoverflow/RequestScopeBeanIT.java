package de.mle.stackoverflow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

public class RequestScopeBeanIT extends IntegrationTestConfigWithPortAndTestProfile {
    @Autowired
    private RequestScopeBean requestScopeBean;
    @Mock
    private HttpServletRequest request;

    @BeforeEach
    public void setup() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @Test
    public void checkCreation() {
        System.out.println(requestScopeBean.toString());
    }
}
