package de.uhd.ifi.se.decision.documentation.jira.config;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import de.uhd.ifi.se.decision.documentation.jira.persistence.ConfigPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class UserServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServlet.class);
    private ConfigPersistence configPersistence;

    @ComponentImport
    private UserManager userManager;
    @ComponentImport
    private LoginUriProvider loginUriProvider;
    @ComponentImport
    private TemplateRenderer templateRenderer;

    @Inject
    public UserServlet(@ComponentImport UserManager userManager, @ComponentImport LoginUriProvider loginUriProvider,
                        @ComponentImport TemplateRenderer renderer) {
        this.userManager = userManager;
        this.loginUriProvider = loginUriProvider;
        this.templateRenderer = renderer;
        this.configPersistence = new ConfigPersistence();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request == null || response == null) {
            LOGGER.error("Request or response in UserServlet is null");
            return;
        }
        String username = userManager.getRemoteUsername(request);
        if (username == null || userManager.isAdmin(username)) {
            redirectToLogin(request, response);
            return;
        }
        String projectKey = request.getParameter("projectKey");
        Boolean isActivated = configPersistence.isActivated(projectKey);
        Boolean isIssueStrategy = configPersistence.isIssueStrategy(projectKey);

        response.setContentType("text/html;charset=utf-8");

        Map<String, Object> velocityParams = new HashMap<String, Object>();
        velocityParams.put("requestUrl", request.getRequestURL());
        velocityParams.put("projectKey", projectKey);
        velocityParams.put("isActivated", isActivated);
        velocityParams.put("isIssueStrategy", isIssueStrategy);
        templateRenderer.render("templates/projectSettings.vm", velocityParams, response.getWriter());
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(loginUriProvider.getLoginUri(getUri(request)).toASCIIString());
        LOGGER.info("User with Name('{}') tried to access UserServlet and has been redirected to Login.",
                userManager.getRemoteUsername(request));
    }

    private URI getUri(HttpServletRequest request) {
        StringBuffer builder = request.getRequestURL();
        if (request.getQueryString() != null) {
            builder.append("?");
            builder.append(request.getQueryString());
        }
        return URI.create(builder.toString());
    }

}