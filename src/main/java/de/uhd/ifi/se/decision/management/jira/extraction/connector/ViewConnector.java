package de.uhd.ifi.se.decision.management.jira.extraction.connector;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.comments.CommentManager;

import de.uhd.ifi.se.decision.management.jira.extraction.classification.ClassificationManagerForCommentSentences;
import de.uhd.ifi.se.decision.management.jira.extraction.model.Comment;

public class ViewConnector {

	private Issue currentIssue;

	private List<Comment> commentsList;

	private CommentManager commentManager;

	public ViewConnector(Issue issue) {
		if (issue != null) {
			this.setCurrentIssue(issue);
			commentManager = ComponentAccessor.getCommentManager();
			this.commentsList = new ArrayList<Comment>();
		}
	}

	public ViewConnector(Issue issue, boolean doNotClassify) {
		this(issue);
		if (issue != null) {
			for (com.atlassian.jira.issue.comments.Comment comment : commentManager.getComments(issue)) {
				commentsList.add(new Comment(comment));
			}
		}
		if (!doNotClassify) {
			this.startClassification();
		}
	}

	public void startClassification() {
		ClassificationManagerForCommentSentences classifier = new ClassificationManagerForCommentSentences();
		this.commentsList = classifier.classifySentenceBinary(commentsList);
		this.commentsList = classifier.classifySentenceFineGrained(commentsList);
	}

	public Issue getCurrentIssue() {
		return currentIssue;
	}

	public void setCurrentIssue(Issue currentIssue) {
		this.currentIssue = currentIssue;
	}

	// two comment lists to remove empty comments
	// index for spanning index classes in html over single comments
	public List<String> getAllTaggedComments() {
		List<String> comments1 = new ArrayList<String>();
		int index = 0;
		for (Comment c : commentsList) {
			index++;
			comments1.add(c.getTaggedBody(index));
		}
		List<String> comments2 = new ArrayList<String>();
		for (String comment : comments1) {
			if (comment.length() > 0) {
				comments2.add(comment);
			}
		}
		comments1 = null;
		return comments2;
	}

	public List<Long> getAllCommentsIDs() {
		List<Long> comments = new ArrayList<Long>();
		for (Comment c : commentsList) {
			comments.add(c.getJiraCommentId());
		}
		return comments;
	}

	public List<String> getAllCommentsAuthorNames() {
		List<String> authorName = new ArrayList<String>();
		for (Comment c : commentsList) {
			authorName.add(c.getAuthorFullName());
		}
		return authorName;
	}

	public List<String> getAllCommentsDates() {
		List<String> commentDate = new ArrayList<String>();
		for (Comment c : commentsList) {
			commentDate.add(c.getCreated().toString().replace("CEST", ""));
		}
		return commentDate;
	}

	public String getSentenceStyles() {
		String style = "<style>";
		style += ".Issue {" + "    background-color: #F2F5A9;} ";
		style += ".Alternative {" + "    background-color: #f1ccf9;} ";
		style += ".Decision {" + "    background-color: #c5f2f9;} ";
		style += ".Pro {" + "    background-color: #b9f7c0;} ";
		style += ".Con {" + "    background-color: #ffdeb5;} ";
		style += ".tag {" + "    background-color: #ffffff;} ";
		style += "</style>";

		return style;
	}

}
