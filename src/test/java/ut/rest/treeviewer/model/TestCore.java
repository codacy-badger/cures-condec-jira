package ut.rest.treeviewer.model;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

<<<<<<< Updated upstream:src/test/java/ut/rest/treeviewer/model/TestCore.java
import com.atlassian.DecisionDocumentation.rest.treeviewer.model.Core;
import com.atlassian.DecisionDocumentation.rest.treeviewer.model.Data;
=======
import de.uhd.ifi.se.decision.documentation.jira.view.treeviewer.Core;
import de.uhd.ifi.se.decision.documentation.jira.view.treeviewer.Data;
>>>>>>> Stashed changes:src/test/java/ut/de/uhd/ifi/se/decision/documentation/jira/rest/treeviewer/model/TestCore.java

public class TestCore {

	private boolean multiple;
	private boolean checkCallback;
	private Map<String, Boolean> themes;
	private HashSet<Data> data;
	private Core  core;
	
	
	@Before 
	public void setUp() {
		multiple=false;
		checkCallback=true;
		themes= new HashMap<>();
		themes.put("Test", false);
		data = new HashSet<>();
		data.add(new Data());
		core = new Core();
		core.setMultiple(multiple);
		core.setCheckCallback(checkCallback);
		core.setThemes(themes);
		core.setData(data);
	}
	
	@Test
	public void testIsMultiple() {
		assertEquals(core.isMultiple(), multiple);
	}
	
	@Test
	public void testisCheckCallBack() {
		assertEquals(core.isCheckCallback(), checkCallback);
	}
	
	@Test
	public void testGetThemes() {
		assertEquals(core.getThemes(), themes);
	}
	
	@Test
	public void testGetData() {
		assertEquals(core.getData(), data);
	}
	
	@Test
	public void testsetMultiple() {
		core.setMultiple(true);
		assertEquals(core.isMultiple(), true);
	}
	
	@Test
	public void testSetCheckCallback() {
		core.setCheckCallback(true);
		assertEquals(core.isCheckCallback(), true);
	}
	
	@Test
	public void testSetThemes() {
		Map<String, Boolean> newThemes = new HashMap<>();
		core.setThemes(newThemes);
		assertEquals(core.getThemes(), newThemes);
	}
	
	@Test
	public void testSetData() { 
		HashSet<Data> newData = new HashSet<>();
		core.setData(newData);
		assertEquals(core.getData(), newData);
	}
}
