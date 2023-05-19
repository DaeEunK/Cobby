package com.cobby.main.activitylog.api.controller;

import java.util.Map;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cobby.main.activitylog.api.service.ActivityLogService;
import com.cobby.main.activitylog.db.entity.ActivityType;
import com.cobby.main.common.response.BaseResponseBody;
import com.cobby.main.common.util.ApiDocumentResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/activityLog")
@Tag(name = "회원 활동 정보", description = "회원 활동 정보 관련 API 문서입니다.")
public class ActivityLogController {

	private final ActivityLogService activityLogService;

	// webhook을 통해 받아오는 정보들
	@Hidden
	@PostMapping("/webhooks")
	@ApiDocumentResponse
	@Operation(summary = "*웹훅 감지", description = "사용자의 깃헙 커밋 활동이 감지되면 해당 요청을 받습니다.")
	public ResponseEntity<? extends BaseResponseBody> createCommitLogByWebhook(
		@RequestHeader Map<String, String> headers,
		@RequestBody String payload
	) {
		activityLogService.webhookCreate(headers, payload);

		return ResponseEntity
			.ok()
			.body(new BaseResponseBody<>(200, "OK", "감지되었습니다."));
	}

	@GetMapping("/attendance") // 회원 연속 출석 조회
	@ApiDocumentResponse
	@Operation(summary = "회원 연속 출석 조회", description = "금일 기준 사용자의 cobby 서비스 연속 출석 기록을 조회합니다.")
	public ResponseEntity<? extends BaseResponseBody> getActivityLogInfo(
		@RequestAttribute(value = "userId", required = false)
		@Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = "올바르지 않은 ID 양식입니다.")
		String userId) {

		var info = activityLogService.getAttendanceActivityLog(userId);

		return ResponseEntity
			.ok()
			.body(new BaseResponseBody<>(200, "OK", info));
	}

	@GetMapping("/commit") // 회원 연속 커밋 조회
	@ApiDocumentResponse
	@Operation(summary = "회원 연속 커밋 조회", description = "금일 기준 사용자의 github 연속 커밋 기록을 조회합니다.")
	public ResponseEntity<? extends BaseResponseBody> getActivityLogCommit(
		@RequestAttribute(value = "userId", required = false)
		@Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = "올바르지 않은 ID 양식입니다.")
		String userId) {

		var info = activityLogService.getCommitActivityLog(userId);

		return ResponseEntity
			.ok()
			.body(new BaseResponseBody<>(200, "OK", info));
	}

	@GetMapping("/attendance/server") // 회원 연속 출석 조회
	@ApiDocumentResponse
	@Operation(summary = "회원 연속 출석 조회", description = "금일 기준 사용자의 cobby 서비스 연속 출석 기록을 조회합니다.")
	public ResponseEntity<? extends BaseResponseBody> getActivityLogServerInfo(
		@RequestHeader(value = "userId", required = false)
		@Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = "올바르지 않은 ID 양식입니다.")
		String userId) {

		var info = activityLogService.getAttendanceActivityLog(userId);

		return ResponseEntity
			.ok()
			.body(new BaseResponseBody<>(200, "OK", info));
	}

	@GetMapping("/commit/server") // 회원 연속 커밋 조회
	@ApiDocumentResponse
	@Operation(summary = "회원 연속 커밋 조회", description = "금일 기준 사용자의 github 연속 커밋 기록을 조회합니다.")
	public ResponseEntity<? extends BaseResponseBody> getActivityLogServerCommit(
		@RequestHeader(value = "userId", required = false)
		@Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = "올바르지 않은 ID 양식입니다.")
		String userId) {

		var info = activityLogService.getCommitActivityLog(userId);

		return ResponseEntity
			.ok()
			.body(new BaseResponseBody<>(200, "OK", info));
	}
}
