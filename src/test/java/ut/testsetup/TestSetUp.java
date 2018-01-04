package ut.testsetup;

import java.util.ArrayList;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.config.ConstantsManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.atlassian.jira.issue.issuetype.MockIssueType;
import com.atlassian.jira.issue.link.IssueLinkManager;
import com.atlassian.jira.issue.link.IssueLinkTypeManager;
import com.atlassian.jira.mock.MockConstantsManager;
import com.atlassian.jira.mock.MockProjectManager;
import com.atlassian.jira.mock.component.MockComponentWorker;
import com.atlassian.jira.mock.issue.MockIssue;
import com.atlassian.jira.project.MockProject;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;

import ut.mocks.MockIssueLinkManager;
import ut.mocks.MockIssueLinkTypeManager;
import ut.mocks.MockIssueManager;
import ut.mocks.MockIssueService;

public class TestSetUp {
	
	private ProjectManager projectManager;
	private IssueManager issueManager;
	private ConstantsManager constManager;
	
	
	public void initialisation(){		
		projectManager = new MockProjectManager();		
		issueManager= new MockIssueManager();	
		constManager = new  MockConstantsManager();
		IssueService issueService=new MockIssueService();
		
		creatingProjectIssueStructure();				
				
		new MockComponentWorker().init().addMock(IssueManager.class, issueManager)
		.addMock(IssueLinkManager.class, new MockIssueLinkManager())
		.addMock(IssueLinkTypeManager.class, new MockIssueLinkTypeManager())
		.addMock(IssueService.class,issueService)
		.addMock(ProjectManager.class,projectManager)
		.addMock(ConstantsManager.class,constManager);
	}
	
	private void creatingProjectIssueStructure() {
		Project project = new MockProject(1,"TEST");
		((MockProject)project).setKey("TEST");			
		((MockProjectManager) projectManager).addProject(project);
		
		ArrayList<String> types= new ArrayList<>();
		
		types.add("Bug");
		types.add("Decision");
		types.add("Question");
		types.add("Issue");
		types.add("Goal");
		types.add("Solution");
		types.add("Alternative");
		types.add("Claim");
		types.add("Context");
		types.add("Assumption");
		types.add("Constraint");
		types.add("Implication");
		types.add("Assessment");
		types.add("Argument");
		
		
		for(int i=2;i<types.size()+2;i++) {
			MutableIssue issue = new MockIssue(i,"TEST-"+i);
			((MockIssue)issue).setProjectId(project.getId());
			((MockIssue)issue).setProjectObject(project);
			IssueType issueType = new MockIssueType(i, types.get(i-2));
			((MockConstantsManager)constManager).addIssueType(issueType);
			((MockIssue)issue).setIssueType(issueType);
			((MockIssue)issue).setSummary("Test");
			((MockIssueManager)issueManager).addIssue(issue);
			if(i>types.size()) {
				((MockIssue)issue).setParentId((long) 3);
			}
		}
		MutableIssue issue = new MockIssue(50,"TEST-50");
		((MockIssue)issue).setProjectId(project.getId());
		((MockIssue)issue).setProjectObject(project);
		IssueType issueType = new MockIssueType(50, "Class");
		((MockConstantsManager)constManager).addIssueType(issueType);
		((MockIssue)issue).setIssueType(issueType);
		((MockIssue)issue).setSummary("Test");
		((MockIssueManager)issueManager).addIssue(issue);
		((MockIssue)issue).setParentId((long) 3);
	}

}
