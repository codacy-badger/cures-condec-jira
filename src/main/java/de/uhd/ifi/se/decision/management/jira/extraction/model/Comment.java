package de.uhd.ifi.se.decision.management.jira.extraction.model;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import de.uhd.ifi.se.decision.management.jira.extraction.persistence.ActiveObjectsManager;

public class Comment {

	private List<Sentence> sentences;

	private String body = "";

	private long jiraCommentId;

	private String authorFullName;

	private long authorId;

	private Date created;

	private String projectKey;

	private CommentSplitter splitter;

	public Comment() {
		this.sentences = new ArrayList<Sentence>();
		this.created = new Date();
		this.authorFullName = " ";
		this.jiraCommentId = 0;
		this.authorId = 0;
		this.splitter = new CommentSplitter();
		this.projectKey = " ";
	}

	public Comment(com.atlassian.jira.issue.comments.Comment comment) {
		this();
		this.projectKey = comment.getIssue().getProjectObject().getKey();
		this.body = textRule(comment.getBody());
		this.created = comment.getCreated();
		this.authorFullName = comment.getAuthorFullName();
		this.jiraCommentId = comment.getId();
		this.authorId = comment.getAuthorApplicationUser().getId();
		splitCommentIntoSentences(true, comment.getIssue().getId());
	}

	public static String textRule(String text) {
		return text.replace("<br>", " ").toString();
	}

	private void splitCommentIntoSentences(boolean addSentencesToAo, Long issueId) {
		List<String> rawSentences = this.splitter.sliceCommentRecursionCommander(this.body);
		runBreakIterator(rawSentences);
		ActiveObjectsManager.checkIfCommentBodyHasChangedOutsideOfPlugin(this);
		// Create AO entries
		for (int i = 0; i < this.splitter.getStartSubstringCount().size(); i++) {
			int startIndex = this.splitter.getStartSubstringCount().get(i);
			int endIndex = this.splitter.getEndSubstringCount().get(i);
			long aoId2 = ActiveObjectsManager.addNewSentenceintoAo(this.jiraCommentId, endIndex, startIndex,
					this.authorId, issueId, this.projectKey);
			this.sentences.add(new SentenceImpl(this.body.substring(startIndex, endIndex), aoId2));

		}
	}

	private void runBreakIterator(List<String> rawSentences) {
		BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);

		for (String currentSentence : rawSentences) {
			if (StringUtils.indexOfAny(currentSentence, CommentSplitter.excludedTagList) == -1) {
				iterator.setText(currentSentence);
				int start = iterator.first();
				for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
					if (end - start > 1) {
						int startOfSentence = this.body.indexOf(currentSentence.substring(start, end));
						int endOfSentence = currentSentence.substring(start, end).length() + startOfSentence;
						this.splitter.addSentenceIndex(startOfSentence, endOfSentence);
					}
				}
			} else {
				int start1 = this.body.indexOf(currentSentence);
				int end1 = currentSentence.length() + start1;
				this.splitter.addSentenceIndex(start1, end1);
			}
		}
	}

	public List<Sentence> getSentences() {
		return sentences;
	}

	public void setSentences(ArrayList<Sentence> sentences) {
		this.sentences = sentences;
	}

	public String getTaggedBody(int index) {
		HTMLCodeGeneratorForSentences hTMLGen = new HTMLCodeGeneratorForSentences();
		String result = "<span id=\"comment" + index + "\">";
		for (Sentence sentence : this.sentences) {
			result += hTMLGen.getCodedElement(sentence);
		}
		return result + "</span>";
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public long getJiraCommentId() {
		return jiraCommentId;
	}

	public void setJiraCommentId(long id) {
		this.jiraCommentId = id;
	}

	public String getAuthorFullName() {
		return authorFullName;
	}

	public void setAuthorFullName(String authorApplicationUser) {
		this.authorFullName = authorApplicationUser;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(long authorId) {
		this.authorId = authorId;
	}

	public List<Integer> getStartSubstringCount() {
		return this.splitter.getStartSubstringCount();
	}

	public List<Integer> getEndSubstringCount() {
		return this.splitter.getEndSubstringCount();
	}

	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

}
