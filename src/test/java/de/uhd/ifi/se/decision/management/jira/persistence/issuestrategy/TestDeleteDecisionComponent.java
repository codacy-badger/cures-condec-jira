package de.uhd.ifi.se.decision.management.jira.persistence.issuestrategy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.MockApplicationUser;

import de.uhd.ifi.se.decision.management.jira.model.DecisionKnowledgeElementImpl;
import de.uhd.ifi.se.decision.management.jira.model.KnowledgeType;


public class TestDeleteDecisionComponent extends TestIssueStrategySetUp {

	@Test
	(expected = NullPointerException.class)
	public void testDecisionRepresNullUserNull() {
		issueStrategy.deleteDecisionKnowledgeElement(null, null);
	}

	@Test
	(expected = NullPointerException.class)
	public void testDecisionRepresFilledUserNull() {
		DecisionKnowledgeElementImpl dec = new DecisionKnowledgeElementImpl();
		issueStrategy.deleteDecisionKnowledgeElement(dec, null);
	}

	@Test
	public void testDecisionRepresFilledUserFilledRight() {
		DecisionKnowledgeElementImpl dec = new DecisionKnowledgeElementImpl();
		dec.setId(1);
		dec.setProjectKey("TEST");
		dec.setType(KnowledgeType.SOLUTION);
		ApplicationUser user = new MockApplicationUser("NoFails");
		assertTrue(issueStrategy.deleteDecisionKnowledgeElement(dec, user));
	}

	@Test
	public void testDecisionRepresIssueUnvalid() {
		DecisionKnowledgeElementImpl dec = new DecisionKnowledgeElementImpl();
		dec.setId(1);
		dec.setProjectKey("TEST");
		dec.setType(KnowledgeType.SOLUTION);
		ApplicationUser user = new MockApplicationUser("WithFails");
		assertFalse(issueStrategy.deleteDecisionKnowledgeElement(dec, user));
	}

	@Test
	public void testDecisionRepresFilledUserFilledResultErrors() {
		DecisionKnowledgeElementImpl dec = new DecisionKnowledgeElementImpl();
		dec.setId(1);
		dec.setProjectKey("TEST");
		dec.setType(KnowledgeType.SOLUTION);
		ApplicationUser user = new MockApplicationUser("WithResFails");
		assertFalse(issueStrategy.deleteDecisionKnowledgeElement(dec, user));
	}

	@Test
	public void testDecisionRepresNoResultErrors() {
		DecisionKnowledgeElementImpl dec = new DecisionKnowledgeElementImpl();
		dec.setId(1);
		dec.setProjectKey("TEST");
		dec.setType(KnowledgeType.SOLUTION);
		ApplicationUser user = new MockApplicationUser("ValidNoResErrors");
		assertFalse(issueStrategy.deleteDecisionKnowledgeElement(dec, user));
	}

}