package de.uhd.ifi.se.decision.management.jira.extraction.persistance;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import de.uhd.ifi.se.decision.management.jira.ComponentGetter;
import de.uhd.ifi.se.decision.management.jira.extraction.model.Rationale;
import de.uhd.ifi.se.decision.management.jira.extraction.model.Sentence;

public class ActiveObjectsManager {

	private static ActiveObjects ao;

	public static void init() {
		if (ao == null) {
			ao = ComponentGetter.getActiveObjects();
		}
	}

	public static long addElement(long commentId, boolean isRelevant, int endSubStringCount, int startSubstringCount,
			long userId) {
		init();
		if (!checkElementExistingInAO(commentId, endSubStringCount, startSubstringCount, userId)) {
			return ao.executeInTransaction(new TransactionCallback<DecisionKnowledgeInCommentEntity>() {
				@Override
				public DecisionKnowledgeInCommentEntity doInTransaction() {
					DecisionKnowledgeInCommentEntity todo = ao.create(DecisionKnowledgeInCommentEntity.class); // (2)
					todo.setCommentId(commentId);
					todo.setEndSubstringCount(endSubStringCount);
					todo.setStartSubstringCount(startSubstringCount);
					todo.setUserId(userId);
					todo.save();
					return todo;
				}
			}).getId();
		} else {
			return getElementFromAO(commentId, endSubStringCount, startSubstringCount, userId).getId();
		}
	}

	public static boolean checkElementExistingInAO(long commentId, int endSubtringCount, int startSubstringCount,
			long userId) {
		init();
		DecisionKnowledgeInCommentEntity databaseEntry = getElementFromAO(commentId, endSubtringCount,
				startSubstringCount, userId);
		if (databaseEntry != null) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean checkCommentExistingInAO(long commentId,boolean getIsTagged) {
		init();
		DecisionKnowledgeInCommentEntity dbEntry =  ao.executeInTransaction(new TransactionCallback<DecisionKnowledgeInCommentEntity>() {
			@Override
			public DecisionKnowledgeInCommentEntity doInTransaction() {
				for (DecisionKnowledgeInCommentEntity databaseEntry : ao.find(DecisionKnowledgeInCommentEntity.class)) {
					if (databaseEntry.getId() == commentId) {
						return databaseEntry;
					}
				}
				return null;
			}
		});
		if(getIsTagged && dbEntry != null) {
			return dbEntry.getIsRelevant();
		}
		return (dbEntry != null);
	}

	public static DecisionKnowledgeInCommentEntity getElementFromAO(long commentId, int endSubtringCount,
			int startSubstringCount, long userId) {
		init();
		return ao.executeInTransaction(new TransactionCallback<DecisionKnowledgeInCommentEntity>() {
			@Override
			public DecisionKnowledgeInCommentEntity doInTransaction() {
				for (DecisionKnowledgeInCommentEntity databaseEntry : ao.find(DecisionKnowledgeInCommentEntity.class)) {
					if (equalsDatabase(databaseEntry, commentId, endSubtringCount, startSubstringCount, userId)) {
						return databaseEntry;
					}
				}
				return null;
			}
		});

	}

	public static DecisionKnowledgeInCommentEntity getElementFromAO(long aoId) {
		init();
		return ao.executeInTransaction(new TransactionCallback<DecisionKnowledgeInCommentEntity>() {
			@Override
			public DecisionKnowledgeInCommentEntity doInTransaction() {
				for (DecisionKnowledgeInCommentEntity databaseEntry : ao.find(DecisionKnowledgeInCommentEntity.class)) {
					if (databaseEntry.getId() == aoId) {
						return databaseEntry;
					}
				}
				return null;
			}
		});
	}

	private static boolean equalsDatabase(DecisionKnowledgeInCommentEntity databaseEntry, long commentId,
			int endSubtringCount, int startSubstringCount, long userId) {
		if (databaseEntry.getCommentId() == commentId && databaseEntry.getEndSubstringCount() == endSubtringCount
				&& databaseEntry.getStartSubstringCount() == startSubstringCount
				&& databaseEntry.getUserId() == userId) {
			return true;
		}
		return false;
	}


	public static boolean updateSentenceClassifications(Sentence sentence) {
		init();
		DecisionKnowledgeInCommentEntity databaseEntry = ao.executeInTransaction(new TransactionCallback<DecisionKnowledgeInCommentEntity>() {
			@Override
			public DecisionKnowledgeInCommentEntity doInTransaction() {
				for (DecisionKnowledgeInCommentEntity databaseEntry : ao.find(DecisionKnowledgeInCommentEntity.class)) {
					if (databaseEntry.getId() == sentence.getActiveObjectId()) {
						for(Rationale rationale: sentence.getClassification()) {
							switch(Rationale.getString(rationale)) {
							case "isIssue":
								databaseEntry.setIsIssue(true);
								break;
							case "isDecision":
								databaseEntry.setIsDecision(true);
								break;
							case "isAlternative":
								databaseEntry.setIsAlternative(true);
								break;
							case "isPro":
								databaseEntry.setIsPro(true);
								break;
							case "isCon":
								databaseEntry.setIsCon(true);
								break;
							}
						}
						databaseEntry.setIsTaggedFineGrained(true);
						databaseEntry.save();
						return databaseEntry;
					}
				}
				return null;
			}
		});
		if (databaseEntry == null) {
			return false;
		}
		return true;
	}

	public static boolean updateRelevance(long activeObjectId, boolean isRelevant) {
		init();
		DecisionKnowledgeInCommentEntity databaseEntry = ao.executeInTransaction(new TransactionCallback<DecisionKnowledgeInCommentEntity>() {
			@Override
			public DecisionKnowledgeInCommentEntity doInTransaction() {
				for (DecisionKnowledgeInCommentEntity databaseEntry : ao.find(DecisionKnowledgeInCommentEntity.class)) {
					if (databaseEntry.getId() == activeObjectId) {
						databaseEntry.setIsRelevant(isRelevant);
						//If relevant is true or false, it's tagged, so set it on true
						databaseEntry.setIsTagged(true);
						databaseEntry.save();
						return databaseEntry;
					}
				}
				return null;
			}
		});
		if (databaseEntry == null) {
			return false;
		}
		return true;
	}

	public static List<Rationale> getRationaleType (long activeObjectId){
		init();
		DecisionKnowledgeInCommentEntity databaseEntry = ao.executeInTransaction(new TransactionCallback<DecisionKnowledgeInCommentEntity>() {
			@Override
			public DecisionKnowledgeInCommentEntity doInTransaction() {
				for (DecisionKnowledgeInCommentEntity databaseEntry : ao.find(DecisionKnowledgeInCommentEntity.class)) {
					if (databaseEntry.getId() == activeObjectId) {
						return databaseEntry;
					}
				}
				return null;
			}
		});
		List<Rationale> rationale = new ArrayList<Rationale>();
		if(databaseEntry.getIsIssue()) {
			rationale.add(Rationale.isIssue);
		}
		if(databaseEntry.getIsAlternative()) {
			rationale.add(Rationale.isAlternative);
		}
		if(databaseEntry.getIsDecision()) {
			rationale.add(Rationale.isDecision);
		}
		if(databaseEntry.getIsPro()) {
			rationale.add(Rationale.isPro);
		}
		if(databaseEntry.getIsCon()) {
			rationale.add(Rationale.isCon);
		}

		return rationale;
	}


}