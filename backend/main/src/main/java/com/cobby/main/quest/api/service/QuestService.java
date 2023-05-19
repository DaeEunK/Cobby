package com.cobby.main.quest.api.service;

import java.util.List;

import com.cobby.main.quest.api.dto.response.CurrentQuest;
import com.cobby.main.quest.api.dto.request.QuestPostRequest;
import com.cobby.main.quest.api.dto.response.QuestGetResponse;
import com.cobby.main.quest.db.entity.enumtype.QuestCategory;

public interface QuestService {

	QuestGetResponse selectQuest(Long questId);
	List<QuestGetResponse> selectAllQuest();
	List<QuestGetResponse> selectAllQuestByQuestType(QuestCategory questCategory);
	Long insertQuest(QuestPostRequest questInfo);
	Long deleteQuest(Long questId);

	List<CurrentQuest> selectCurrentQuests(String userId);
	CurrentQuest selectNextQuest(String userId, Long questId);
}
