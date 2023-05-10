package com.cobby.main.avatar.api.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cobby.main.avatar.api.dto.request.AvatarUpdateRequest;
import com.cobby.main.avatar.api.dto.response.AvatarGetResponse;
import com.cobby.main.avatar.api.service.AvatarService;
import com.cobby.main.avatar.db.entity.Avatar;
import com.cobby.main.avatar.db.repository.AvatarCostumeRepository;
import com.cobby.main.avatar.db.repository.AvatarRepository;
import com.cobby.main.costume.db.entity.Costume;
import com.cobby.main.costume.db.entity.enumtype.CostumeCategory;
import com.cobby.main.costume.db.repository.CostumeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AvatarServiceImpl implements AvatarService {

	private final AvatarRepository avatarRepository;

	private final CostumeRepository costumeRepository;

	private final ObjectMapper objectMapper;
	private final AvatarCostumeRepository avatarCostumeRepository;

	@Override
	public AvatarGetResponse selectAvatar(String avatarId) throws JsonProcessingException {
		var avatar = avatarRepository.findById(avatarId)
			.orElseThrow(() -> new IllegalArgumentException("아바타 정보가 없습니다. (ID=" + avatarId + ")"));

		Map<String, String> currentCostumeIds = objectMapper.readValue(
			avatar.getCurrentCostumes(),
			objectMapper.getTypeFactory().constructParametricType(Map.class, String.class, String.class));

		var outfits = new HashMap<String, String>();

		currentCostumeIds.forEach((part, costumeId) ->
			outfits.put(part,
					(CostumeCategory.NO_COSTUME.ordinal() == Integer.parseInt(costumeId))
						? CostumeCategory.NO_COSTUME.name()
						: avatarCostumeRepository.findByCostume_CostumeId(Long.parseUnsignedLong(costumeId))
					.orElseThrow(() -> new IllegalArgumentException("보유하고 있지 않은 코스튬입니다. (PART=" + part + ", ID=" + costumeId + ")"))
				.getCostume()
				.getGifUrl()));

		return AvatarGetResponse.builder()
			.avatar(avatar)
			.outfits(outfits)
			.build();
	}

	@Override
	public String insertDefaultAvatar(String avatarId) throws JsonProcessingException {
		avatarRepository.findById(avatarId)
			.ifPresent((x) -> {
				throw new IllegalArgumentException("이미 존재하는 아바타입니다.");
			});

		var newAvatar = getDefaultAvatar(avatarId);

		return avatarRepository.save(newAvatar).getAvatarId();
	}

	@Override
	public String updateAvatar(String avatarId, AvatarUpdateRequest avatarUpdateInfo) throws JsonProcessingException {

		var avatar = avatarRepository.findById(avatarId)
			.orElseThrow(() -> new IllegalArgumentException("아바타 정보가 없습니다. (ID=" + avatarId + ")"));

		Map<String, Long> currentCostumeIds = objectMapper.readValue(
			avatar.getCurrentCostumes(),
			objectMapper.getTypeFactory().constructParametricType(Map.class, String.class, Long.class));

		var level = Optional.ofNullable(avatarUpdateInfo.level())
			.orElse(avatar.getLevel());
		var exp = Optional.ofNullable(avatarUpdateInfo.exp())
			.orElse(avatar.getExp());
		var head = Optional.ofNullable(avatarUpdateInfo.head())
			.orElse(currentCostumeIds.get("head"));
		var body = Optional.ofNullable(avatarUpdateInfo.face())
			.orElse(currentCostumeIds.get("body"));
		var effect = Optional.ofNullable(avatarUpdateInfo.body())
			.orElse(currentCostumeIds.get("effect"));

		avatar = avatar.toBuilder()
			.level(level)
			.exp(exp)
			.currentCostumes(objectMapper.writeValueAsString(Map.of("head", head, "body", body, "effect", effect)))
			.build();

		return avatarRepository.save(avatar).getAvatarId();
	}

	@Override
	public String resetAvatar(String avatarId) throws JsonProcessingException {
		var avatar = avatarRepository.findById(avatarId)
			.orElseThrow(() -> new IllegalArgumentException("아바타 정보가 없습니다. (ID=" + avatarId + ")"));

		return avatarRepository.save(getDefaultAvatar(avatarId)).getAvatarId();
	}

	@Override
	public String deleteAvatar(String avatarId) {
		var avatar = avatarRepository.findById(avatarId)
			.orElseThrow(() -> new IllegalArgumentException("아바타 정보가 없습니다. (ID=" + avatarId + ")"));

		avatarRepository.deleteById(avatarId);

		return avatar.getAvatarId();
	}

	private Avatar getDefaultAvatar(String userId) throws JsonProcessingException {

		var currentCostumes = Map.of("head", 0L, "body", 0L, "effect", 0L);

		return Avatar.builder()
			.avatarId(userId)
			.level(1)
			.exp(0)
			.currentCostumes(objectMapper.writeValueAsString(currentCostumes))
			.build();
	}
}
