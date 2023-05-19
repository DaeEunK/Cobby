package com.cobby.main.avatar.api.controller;

import java.net.URI;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cobby.main.avatar.api.dto.request.AvatarPatchRequest;
import com.cobby.main.avatar.api.service.AvatarService;
import com.cobby.main.common.response.BaseResponseBody;
import com.cobby.main.common.util.ApiDocumentResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Cobby 캐릭터 관련", description = "Cobby 캐릭터 관련 API 문서입니다.")
@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("api/main/avatars")
public class AvatarController {

	private final AvatarService avatarService;

	@ApiDocumentResponse
	@Operation(summary = "아바타 조회", description = "user ID로 아바타를 조회하는 메서드 입니다.")
	@GetMapping
	public ResponseEntity<? extends BaseResponseBody> getAvatar(
		@RequestAttribute(value = "userId", required = false)
		@Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = "올바르지 않은 ID 양식입니다.")
		String userId) throws JsonProcessingException {
		log.info("userId{} : " + userId);
		var costumes = avatarService.selectAvatar(userId);

		return ResponseEntity
			.ok()
			.body(new BaseResponseBody<>(200, "OK", costumes));
	}

	@ApiDocumentResponse
	@Hidden
	@Operation(summary = "아바타 서버 조회", description = "user ID로 아바타를 조회하는 메서드 입니다.")
	@GetMapping("/server")
	public ResponseEntity<? extends BaseResponseBody> getAvatarServer(
		@RequestHeader(value = "userId", required = false)
		@Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = "올바르지 않은 ID 양식입니다.")
		String userId) throws JsonProcessingException {
		log.info("userId{} : " + userId);
		var costumes = avatarService.selectAvatar(userId);

		return ResponseEntity
			.ok()
			.body(new BaseResponseBody<>(200, "OK", costumes));
	}

	@ApiDocumentResponse
	@Operation(summary = "아바타 정보 수정", description = "user ID에 해당하는 아바타 정보를 업데이트 하는 메서드입니다.")
	@PatchMapping
	public ResponseEntity<? extends BaseResponseBody> updateAvatar(
		@RequestBody AvatarPatchRequest avatarUpdateInfo,
		@RequestAttribute("userId")
		@Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = "올바르지 않은 ID 양식입니다.")
		String userId) throws JsonProcessingException {

		var avatarId = avatarService.updateAvatar(userId, avatarUpdateInfo);

		var successMessage = "아바타 정보를 성공적으로 변경했습니다. (ID=" + avatarId + ")";

		return ResponseEntity
			.ok()
			.body(new BaseResponseBody<>(200, "OK", successMessage));
	}

	@Hidden
	@ApiDocumentResponse
	@Operation(summary = "#####아바타 정보 초기화#####", description = "user ID에 해당하는 아바타를 초기화하는 메서드 입니다.")
	@GetMapping("/reset")
	public ResponseEntity<? extends BaseResponseBody> resetAvatar(
		@RequestAttribute("userId")
		@Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = "올바르지 않은 ID 양식입니다.")
		String userId) throws JsonProcessingException {

		var avatarId = avatarService.resetAvatar(userId);

		var successMessage = "아바타 정보를 성공적으로 초기화했습니다. (ID=" + avatarId + ")";

		return ResponseEntity
			.ok()
			.body(new BaseResponseBody<>(200, "OK", successMessage));
	}

	@Hidden
	@ApiDocumentResponse
	@Operation(summary = "#####아바타 정보 삭제#####", description = "user ID에 해당하는 아바타를 삭제하는 메서드 입니다.")
	@DeleteMapping
	public ResponseEntity<? extends BaseResponseBody> deleteAvatar(
		@RequestAttribute("userId")
		@Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = "올바르지 않은 ID 양식입니다.")
		String userId) {

		var avatarId = avatarService.deleteAvatar(userId);

		var successMessage = "아바타 정보를 성공적으로 삭제했습니다. (ID=" + avatarId + ")";

		return ResponseEntity
			.ok()
			.body(new BaseResponseBody<>(200, "OK", successMessage));
	}
}
