package com.cobby.main.common.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@EntityListeners(value = AuditingEntityListener.class) // Auditing 기능 포함
@MappedSuperclass // JPA Entity 클래스들이 이 클래스를 상속할 경우 필드들도 칼럼으로 인식하게 함.
public class CreatedTimeEntity {

	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt; // 수정 시간

	@Override
	public String toString() {
		return "UpdateTimeEntity{" +
			"createdAt=" + createdAt +
			'}';
	}
}