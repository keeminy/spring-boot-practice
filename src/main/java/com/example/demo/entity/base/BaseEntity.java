package com.example.demo.entity.base;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    
    @Column(name = "created_date", nullable = false, updatable = false)
	@CreatedDate
	@ColumnDefault("CURRENT_TIMESTAMP")
	private LocalDateTime createdDate;

	@Column(name = "modified_date")
	@LastModifiedDate
	@ColumnDefault("CURRENT_TIMESTAMP")
	private LocalDateTime modifiedDate;
}
