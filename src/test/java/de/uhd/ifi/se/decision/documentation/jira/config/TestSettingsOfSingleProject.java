package de.uhd.ifi.se.decision.documentation.jira.config;

import com.atlassian.activeobjects.test.TestActiveObjects;
import com.atlassian.jira.mock.servlet.MockHttpServletResponse;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import de.uhd.ifi.se.decision.documentation.jira.ComponentGetter;
import de.uhd.ifi.se.decision.documentation.jira.TestSetUp;
import de.uhd.ifi.se.decision.documentation.jira.mocks.*;
import net.java.ao.EntityManager;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RunWith(ActiveObjectsJUnitRunner.class)
public class TestSettingsOfSingleProject extends TestSetUp {

	private EntityManager entityManager;
	private SettingsOfSingleProject servlet;
	private HttpServletRequest request;
	private HttpServletResponse response;

	@Before
	public void setUp() {
		initialization();
		new ComponentGetter().init(new TestActiveObjects(entityManager), new MockTransactionTemplate(),
				new MockDefaultUserManager());

		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();

		((MockHttpServletRequest) request).setParameter("projectKey", "TEST");
		LoginUriProvider login = new MockLoginUriProvider();
		TemplateRenderer renderer = new MockTemplateRenderer();
		UserManager userManager = new MockAdminUserManager();
		servlet = new SettingsOfSingleProject(userManager, login, renderer);
	}

	@Test
	public void testReqNullResNull() throws IOException {
		servlet.doGet(null, null);
	}

	@Test
	public void testRequestFilledResponseNull() throws IOException, ServletException {
		request.setAttribute("NoSysAdmin", false);
		request.setAttribute("SysAdmin", false);
		servlet.doGet(request, null);
	}

	@Test
	public void testRequestNullResponseFilled() throws IOException, ServletException {
		servlet.doGet(null, response);
	}

	@Test
	public void testRequestFilledResponseFilled() throws IOException, ServletException {
		request.setAttribute("NoSysAdmin", false);
		request.setAttribute("SysAdmin", false);
		servlet.doGet(request, response);
	}

	@Test
	public void testNoUserManager() throws IOException, ServletException {
		((MockHttpServletRequest) request).setQueryString("Test");
		request.setAttribute("NoSysAdmin", true);
		servlet.doGet(request, response);
	}

	@Test
	public void testNoUserManagerQueryNull() throws IOException, ServletException {
		((MockHttpServletRequest) request).setQueryString(null);
		request.setAttribute("NoSysAdmin", true);
		servlet.doGet(request, response);
	}

	@Test
	public void testUserManager() throws IOException, ServletException {
		request.setAttribute("NoSysAdmin", false);
		request.setAttribute("SysAdmin", true);
		servlet.doGet(request, response);
	}
}