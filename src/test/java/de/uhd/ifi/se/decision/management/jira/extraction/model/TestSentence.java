package de.uhd.ifi.se.decision.management.jira.extraction.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.atlassian.activeobjects.test.TestActiveObjects;
import de.uhd.ifi.se.decision.management.jira.TestComponentGetter;
import de.uhd.ifi.se.decision.management.jira.TestSetUp;
import de.uhd.ifi.se.decision.management.jira.extraction.persistence.DecisionKnowledgeInCommentEntity;
import de.uhd.ifi.se.decision.management.jira.mocks.MockDefaultUserManager;
import de.uhd.ifi.se.decision.management.jira.mocks.MockTransactionTemplate;
import de.uhd.ifi.se.decision.management.jira.model.KnowledgeType;
import net.java.ao.EntityManager;
import net.java.ao.test.jdbc.Data;
import net.java.ao.test.jdbc.DatabaseUpdater;
import net.java.ao.test.jdbc.NonTransactional;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;


@RunWith(ActiveObjectsJUnitRunner.class)
@Data(TestSentence.AoSentenceTestDatabaseUpdater.class) 
public class TestSentence extends TestSetUp {
	
	private EntityManager entityManager;

	@Before
	public void setUp() {
		initialization();
		TestComponentGetter.init(new TestActiveObjects(entityManager), new MockTransactionTemplate(),
				new MockDefaultUserManager());
	}

	@Test
	@NonTransactional
	public void testSetKnowledgeTypeEnum() {
		Sentence sentence = new Sentence();
		assertNotNull(sentence);
		sentence.setKnowledgeType(KnowledgeType.ALTERNATIVE);
		assertEquals(KnowledgeType.ALTERNATIVE,sentence.getKnowledgeType());
	}
	
	@Test
	@NonTransactional
	public void testSetKnowledgeTypeDoubleAlternative() {
		Sentence sentence = new Sentence();
		double[] classification = {1.0, 0.0 , 0.0, 0.0, 0.0};
		sentence.setKnowledgeType(classification);
		assertEquals(KnowledgeType.ALTERNATIVE,sentence.getKnowledgeType());
	}	

	@Test
	@NonTransactional
	public void testSetKnowledgeTypeDoubleArgumentPro() {
		Sentence sentence = new Sentence();
		double[] classification = {.0, 1.0 , 0.0, 0.0, 0.0};
		sentence.setKnowledgeType(classification);
		assertEquals(KnowledgeType.ARGUMENT,sentence.getKnowledgeType());
		assertEquals("pro", sentence.getArgument().toLowerCase());
	}
	
	@Test
	@NonTransactional
	public void testSetKnowledgeTypeDoubleArgumentCon() {
		Sentence sentence = new Sentence();
		double[] classification = {.0, .0 , 1.0, 0.0, 0.0};
		sentence.setKnowledgeType(classification);
		assertEquals(KnowledgeType.ARGUMENT,sentence.getKnowledgeType());
		assertEquals("con", sentence.getArgument().toLowerCase());
	}
	
	@Test
	@NonTransactional
	public void testSetKnowledgeTypeDoubleDecision() {
		Sentence sentence = new Sentence();
		double[] classification = {.0, 0.0 , 0.0, 1.0, 0.0};
		sentence.setKnowledgeType(classification);
		assertEquals(KnowledgeType.DECISION,sentence.getKnowledgeType());
	}	
	
	@Test
	@NonTransactional
	public void testSetKnowledgeTypeDoubleIssue() {
		Sentence sentence = new Sentence();
		double[] classification = {.0, 0.0 , 0.0, .0, 1.0};
		sentence.setKnowledgeType(classification);
		assertEquals(KnowledgeType.ISSUE,sentence.getKnowledgeType());
	}	
	
	@Test
	@Ignore
	@NonTransactional //TODO: Findout how to initialize project impl
	public void testGetKnowledgeTypeToString() {
		Sentence sentence = new Sentence();
		assertEquals("",sentence.getKnowledgeTypeString());
		sentence.setKnowledgeType(KnowledgeType.ALTERNATIVE);
		assertEquals("Alternative",sentence.getKnowledgeTypeString());
		sentence.setKnowledgeType(KnowledgeType.ARGUMENT);
		assertEquals("",sentence.getKnowledgeTypeString());
	}	
	
	@Test
	@NonTransactional
	public void testSetKnowledgeTypeString() {
		Sentence sentence = new Sentence();
		sentence.setKnowledgeType(KnowledgeType.ALTERNATIVE.toString());
		assertEquals(KnowledgeType.ALTERNATIVE,sentence.getKnowledgeType());
		sentence.setKnowledgeType("pro");
		assertEquals(KnowledgeType.ARGUMENT,sentence.getKnowledgeType());
		assertEquals("pro",sentence.getArgument().toLowerCase());
		sentence.setKnowledgeType("con");
		assertEquals(KnowledgeType.ARGUMENT,sentence.getKnowledgeType());
		assertEquals("con",sentence.getArgument().toLowerCase());
	}	
	
	@Test
	@NonTransactional
	public void testGetOpeningTagSpan() {
		Sentence sentence = new Sentence();
		assertEquals("<span class =tag></span>", sentence.getOpeningTagSpan());
		sentence.setKnowledgeType(KnowledgeType.ARGUMENT);
		sentence.setRelevant(true);
		sentence.setArgument("Pro");
		assertEquals("<span class =tag>[Pro]</span>", sentence.getOpeningTagSpan());
	}	
	
	@Test
	@NonTransactional
	public void testGetClosingTagSpan() {
		Sentence sentence = new Sentence();
		assertEquals("<span class =tag></span>", sentence.getClosingTagSpan());
		sentence.setKnowledgeType(KnowledgeType.ARGUMENT);
		sentence.setRelevant(true);
		sentence.setArgument("Pro");
		assertEquals("<span class =tag>[/Pro]</span>", sentence.getClosingTagSpan());
	}	
	
	@Test
	@NonTransactional
	public void testSetRelevantWithDouble() {
		Sentence sentence = new Sentence();
		sentence.setRelevant(1.0);
		assertTrue(sentence.isRelevant());
		sentence.setRelevant(.0);
		assertFalse(sentence.isRelevant());
	}	
	
	@Test
	@NonTransactional
	public void testToString() {
		Sentence sentence = new Sentence();
		assertNotNull(sentence.toString());
	}	
	
	
	
	
	
	
	
	

	
	public static final class AoSentenceTestDatabaseUpdater implements DatabaseUpdater // (2)
    {
        @SuppressWarnings("unchecked")
		@Override
        public void update(EntityManager entityManager) throws Exception
        {
            entityManager.migrate(DecisionKnowledgeInCommentEntity.class);
        }
    }
}
