package de.uhd.ifi.se.decision.management.jira.mocks;

import java.util.ArrayList;
import java.util.Collection;

import com.atlassian.jira.config.ConstantsManager;
import com.atlassian.jira.config.IssueTypeManager;
import com.atlassian.jira.exception.CreateException;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.atlassian.jira.mock.MockConstantsManager;

public class MockIssueTypeManager implements IssueTypeManager {

	private Collection<IssueType> types;

	public MockIssueTypeManager() {
		super();
		types = new ArrayList<>();
	}

	public void addingAllIssueTypes() throws CreateException {
		ConstantsManager constManager = new MockConstantsManager();
		constManager.insertIssueType("Decision", (long) 20, "Test", "Test", (long) 12290);
		constManager.insertIssueType("Alternative", (long) 20, "Test", "Test", (long) 12290);
		constManager.insertIssueType("Argument", (long) 20, "Test", "Test", (long) 12290);
		constManager.insertIssueType("Assessment", (long) 20, "Test", "Test", (long) 12290);
		constManager.insertIssueType("Assumption", (long) 20, "Test", "Test", (long) 12290);
		constManager.insertIssueType("Claim", (long) 20, "Test", "Test", (long) 12290);
		constManager.insertIssueType("Constraint", (long) 20, "Test", "Test", (long) 12290);
		constManager.insertIssueType("Context", (long) 20, "Test", "Test", (long) 12290);
		constManager.insertIssueType("Goal", (long) 20, "Test", "Test", (long) 12290);
		constManager.insertIssueType("Implication", (long) 20, "Test", "Test", (long) 12290);
		constManager.insertIssueType("Issue", (long) 20, "Test", "Test", (long) 12290);
		constManager.insertIssueType("Problem", (long) 20, "Test", "Test", (long) 12290);
		constManager.insertIssueType("Solution", (long) 20, "Test", "Test", (long) 12290);
		types.addAll(constManager.getAllIssueTypeObjects());
	}

	public void addIssueType(Collection<IssueType> issueTypes) {
		types.addAll(issueTypes);
	}

	@Override
	public IssueType createIssueType(String arg0, String arg1, String arg2) {
		return null;
	}

	@Override
	public IssueType createIssueType(String arg0, String arg1, Long arg2) {
		return null;
	}

	@Override
	public IssueType createSubTaskIssueType(String arg0, String arg1, String arg2) {
		return null;
	}

	@Override
	public IssueType createSubTaskIssueType(String arg0, String arg1, Long arg2) {
		return null;
	}

	@Override
	public void editIssueType(IssueType arg0, String arg1, String arg2, String arg3) {
		// method empty since not used for testing
	}

	@Override
	public Collection<IssueType> getAvailableIssueTypes(IssueType arg0) {
		return null;
	}

	@Override
	public IssueType getIssueType(String arg0) {
		return null;
	}

	@Override
	public Collection<IssueType> getIssueTypes() {
		return types;
	}

	@Override
	public boolean hasAssociatedIssues(IssueType arg0) {
		return false;
	}

	@Override
	public void removeIssueType(String arg0, String arg1) {
		// method empty since not used for testing
	}

	@Override
	public void updateIssueType(IssueType arg0, String arg1, String arg2, Long arg3) {
		// method empty since not used for testing
	}

}
