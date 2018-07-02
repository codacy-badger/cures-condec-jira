package de.uhd.ifi.se.decision.management.jira.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import de.uhd.ifi.se.decision.management.jira.persistence.AbstractPersistenceStrategy;
import de.uhd.ifi.se.decision.management.jira.persistence.StrategyProvider;

/**
 * Model class for a graph of decision knowledge elements
 */
@JsonAutoDetect
public class GraphImpl implements Graph {

	private AbstractPersistenceStrategy persistenceStrategy;
	private List<Long> linkIds;
	private DecisionKnowledgeElement rootElement;

	public GraphImpl() {
		linkIds = new ArrayList<>();
	}

	public GraphImpl(String projectKey) {
		this();
		StrategyProvider strategyProvider = new StrategyProvider();
		this.persistenceStrategy = strategyProvider.getStrategy(projectKey);
	}

	public GraphImpl(String projectKey, String rootElementKey) {
		this(projectKey);
		this.rootElement = persistenceStrategy.getDecisionKnowledgeElement(rootElementKey);
	}

	public GraphImpl(DecisionKnowledgeElement rootElement) {
		this(rootElement.getProjectKey());
		this.rootElement = rootElement;
	}

	public Map<DecisionKnowledgeElement, Link> getLinkedElementsAndLinks(DecisionKnowledgeElement element) {
		Map<DecisionKnowledgeElement, Link> linkedElementsAndLinks = new HashMap<DecisionKnowledgeElement, Link>();
		linkedElementsAndLinks.putAll(this.getElementsLinkedWithOutwardLinks(element));
		linkedElementsAndLinks.putAll(this.getElementsLinkedWithInwardLinks(element));
		return linkedElementsAndLinks;
	}

	public List<DecisionKnowledgeElement> getLinkedElements(DecisionKnowledgeElement element) {
		Map<DecisionKnowledgeElement, Link> linkedElementsAndLinks = this.getLinkedElementsAndLinks(element);
		List<DecisionKnowledgeElement> linkedElements = new ArrayList<DecisionKnowledgeElement>(linkedElementsAndLinks.keySet());
		return linkedElements;
	}

	public Map<DecisionKnowledgeElement, Link> getElementsLinkedWithOutwardLinks(DecisionKnowledgeElement element) {
		Map<DecisionKnowledgeElement, Link> linkedElementsAndLinks = new HashMap<DecisionKnowledgeElement, Link>();

		if (element == null) {
			return linkedElementsAndLinks;
		}

		List<Link> outwardLinks = persistenceStrategy.getOutwardLinks(element);
		for (Link link : outwardLinks) {
			if (!linkIds.contains(link.getLinkId())) {
				DecisionKnowledgeElement outwardElement = link.getDestinationObject();
				if (outwardElement != null) {
					linkIds.add(link.getLinkId());
					linkedElementsAndLinks.put(outwardElement, link);
				}
			}
		}

		return linkedElementsAndLinks;
	}

	public Map<DecisionKnowledgeElement, Link> getElementsLinkedWithInwardLinks(DecisionKnowledgeElement element) {
		Map<DecisionKnowledgeElement, Link> linkedElementsAndLinks = new HashMap<DecisionKnowledgeElement, Link>();

		if (element == null) {
			return linkedElementsAndLinks;
		}

		List<Link> inwardLinks = persistenceStrategy.getInwardLinks(element);
		for (Link link : inwardLinks) {
			if (!linkIds.contains(link.getLinkId())) {
				DecisionKnowledgeElement inwardElement = link.getSourceObject();
				if (inwardElement != null) {
					linkIds.add(link.getLinkId());
					linkedElementsAndLinks.put(inwardElement, link);
				}
			}
		}

		return linkedElementsAndLinks;
	}

	public DecisionKnowledgeElement getRootElement() {
		return rootElement;
	}

	public void setRootElement(DecisionKnowledgeElement rootElement) {
		this.rootElement = rootElement;
	}
}