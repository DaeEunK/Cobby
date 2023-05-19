package com.cobby.main.stat.db.entity;

import com.cobby.main.user.db.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Stat {

	@Id
	@Column(name = "user_id", nullable = false)
	private String id;

	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false, columnDefinition = "INT UNSIGNED")
	private Long commitCnt;
	@Column(nullable = false, columnDefinition = "INT UNSIGNED")
	private Long starCnt;
	@Column(nullable = false, columnDefinition = "INT UNSIGNED")
	private Long prCnt;
	@Column(nullable = false, columnDefinition = "INT UNSIGNED")
	private Long followerCnt;
	@Column(nullable = false, columnDefinition = "INT UNSIGNED")
	private Long issueCnt;

}
