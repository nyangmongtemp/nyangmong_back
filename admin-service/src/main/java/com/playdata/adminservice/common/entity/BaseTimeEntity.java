package com.playdata.adminservice.common.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class BaseTimeEntity {

   @CreationTimestamp
   private LocalDateTime createAt;

   @UpdateTimestamp
   private LocalDateTime updateAt;

    public LocalDateTime getCreatedAt() {
        return createAt;
    }
    public void setCreatedAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updateAt;
    }
    public void setUpdatedAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
