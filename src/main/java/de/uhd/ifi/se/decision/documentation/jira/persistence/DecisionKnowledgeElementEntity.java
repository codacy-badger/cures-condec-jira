package de.uhd.ifi.se.decision.documentation.jira.persistence;

import java.util.List;

import de.uhd.ifi.se.decision.documentation.jira.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.documentation.jira.model.DecisionKnowledgeProject;
import net.java.ao.RawEntity;
import net.java.ao.schema.AutoIncrement;
import net.java.ao.schema.Ignore;
import net.java.ao.schema.PrimaryKey;
import net.java.ao.schema.Table;

/**
 * @description Interface for decision knowledge elements used in the
 *              active object persistence strategy
 */
@Table("DECISION")
public interface DecisionKnowledgeElementEntity extends DecisionKnowledgeElement, RawEntity<Integer> {
	@AutoIncrement
	@PrimaryKey("ID")
	public long getId();

	@Ignore
	public void setType(String type);

	@Ignore
	public DecisionKnowledgeProject getProject();

	@Ignore
	public List<DecisionKnowledgeElement> getChildren();
}