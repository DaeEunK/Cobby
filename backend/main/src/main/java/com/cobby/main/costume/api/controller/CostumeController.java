package com.cobby.main.costume.api.controller;

import java.io.IOException;
import java.net.URI;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cobby.main.common.response.BaseResponseBody;
import com.cobby.main.common.util.ApiDocumentResponse;
import com.cobby.main.costume.api.dto.request.CostumePostRequest;
import com.cobby.main.costume.api.service.CostumeService;
import com.cobby.main.costume.db.entity.enumtype.CostumeCategory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@Tag(name = "코스튬", description = "코스튬 관련 API 문서입니다.")
@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("api/main/costumes")
public class CostumeController {
	private final CostumeService costumeService;

	@ApiDocumentResponse
	@Operation(summary = "코스튬 목록 전체 조회", description = "모든 코스튬을 조회합니다.")
	@GetMapping
	public ResponseEntity<? extends BaseResponseBody> getAllCostumes() {
		var costumes = costumeService.selectAllCostumes();

		return ResponseEntity
			.ok()
			.body(new BaseResponseBody<>(200, "OK", costumes));
	}

	@ApiDocumentResponse
	@Operation(summary = "카테고리별 코스튬 목록 조회", description = "코스튬의 카테고리(HEAD/BODY/EFFECT)별로 코스튬 목록을 조회합니다.")
	@GetMapping("/{category}")
	public ResponseEntity<? extends BaseResponseBody> getAllCostumesByCategory(
		@PathVariable @NotBlank(message = "필수 입력 항목입니다. ")
		String category
	) {
		var costumes = costumeService.selectAllCostumesByCategoryType(CostumeCategory.valueOf(category));

		return ResponseEntity
			.ok()
			.body(new BaseResponseBody<>(200, "OK", costumes));
	}

	// @Hidden
	// @ApiDocumentResponse
	// @Operation(summary = "#####코스튬 하나 조회#####", description = "코스튬 ID에 해당하는 코스튬을 조회합니다.")
	// @GetMapping("/{costumeId}")
	// public ResponseEntity<? extends BaseResponseBody> getCostume(
	// 	@PathVariable
	// 	@Positive(message = "올바르지 않은 ID 양식입니다.") // 최대 상한 조건(Max)도 추가를 고려해야 함
	// 	Long costumeId) {
	//
	// 	var costume = costumeService.selectCostume(costumeId);
	//
	// 	return ResponseEntity
	// 		.ok()
	// 		.body(new BaseResponseBody<>(200, "OK", costume));
	// }

	@Hidden
	@ApiDocumentResponse
	@Operation(summary = "#####코스튬 생성#####", description = "코스튬 이름, 종류, 관련 도전과제 ID에 해당하는 코스튬을 생성합니다.")
	@PostMapping
	public ResponseEntity<? extends BaseResponseBody> createCostume(
		@RequestPart(value = "info") @Valid CostumePostRequest costumePostRequest,
		@RequestPart(value = "pngFile", required = true) MultipartFile pngFile,
		@RequestPart(value = "gifFile", required = true) MultipartFile gifFile,
		HttpServletRequest request) throws IOException {

		var costumeId = costumeService.insertCostume(costumePostRequest, pngFile, gifFile);

		var location = URI.create(request.getRequestURI() + "/" + costumeId);

		var successMessage = "코스튬 정보를 성공적으로 저장하였습니다. (ID=" + costumeId + ")";

		return ResponseEntity
			.created(location)
			.body(new BaseResponseBody<>(201, "created", successMessage));
	}

	@Hidden
	@ApiDocumentResponse
	@Operation(summary = "#####코스튬 삭제#####", description = "코스튬 ID에 해당하는 코스튬을 삭제합니다.")
	@DeleteMapping("/{costumeId}")
	public ResponseEntity<? extends BaseResponseBody> deleteCostume(
		@PathVariable
		@Positive(message = "올바르지 않은 ID 양식입니다.") // 최대 상한 조건(Max)도 추가를 고려해야 함
		Long costumeId) {

		var deletedId = costumeService.deleteCostume(costumeId);

		var successMessage = "코스튬 정보를 성공적으로 삭제하였습니다. (ID=" + deletedId + ")";

		return ResponseEntity
			.ok()
			.body(new BaseResponseBody<>(200, "OK", successMessage));
	}
}
