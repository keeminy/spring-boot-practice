package com.example.demo.entity;

import com.example.demo.entity.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@Table(name = "TB_USER")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

	private int userSq;

	@Id
	private String userId;

	@Column(nullable = false)
	private String userPw;

	private String userNm;

	private String userSt = "S";

	@Builder
	User(int userSq, String userId, String userPw, String userNm, String userSt) {
		this.userSq = userSq;
		this.userId = userId;
		this.userPw = userPw;
		this.userNm = userNm;
		this.userSt = userSt;
	}

	public Object map(Object object) {
		return null;
	}

}
