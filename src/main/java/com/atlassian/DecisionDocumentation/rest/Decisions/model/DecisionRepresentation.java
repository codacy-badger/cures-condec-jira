package com.atlassian.DecisionDocumentation.rest.Decisions.model;

import com.atlassian.jira.issue.Issue;

/**
 * @author Ewald Rode
 * @description model class for decisioncomponents
 */
public class DecisionRepresentation {
	private Long id;
	private String name;
	private String description;
	private String type;
	private String projectKey;
	
	public DecisionRepresentation() {
		
	}
	
	public DecisionRepresentation(long id, String name, String description, String type, String projectKey) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.type = type;
		this.projectKey = projectKey;
	}
	
	public DecisionRepresentation(Issue issue) {
		id = issue.getId();
		name = issue.getSummary();
		description = issue.getDescription();
		type = issue.getIssueType().getName();
		projectKey = issue.getProjectObject().getKey();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getProjectKey() {
		return projectKey;
	}
	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}
}