package com.atlassian.DecisionDocumentation.db;

import net.java.ao.RawEntity;
import net.java.ao.schema.AutoIncrement;
import net.java.ao.schema.PrimaryKey;
import net.java.ao.schema.Table;

/**
 * @author Ewald Rode
 * @description model class for decisioncomponents
 */
@Table("DECISION")
public interface DecisionComponentEntity extends RawEntity<Integer>{
	@AutoIncrement
	@PrimaryKey("ID")
	public long getID();
	
	public String getKey();
	public void setKey(String key);
	
	public String getName();
	public void setName(String name);
	
	public String getDescription();
	public void setDescription(String description);
	
	public String getType();
	public void setType(String type);
	
	public String getProjectKey();
	public void setProjectKey(String projectKey);
}
