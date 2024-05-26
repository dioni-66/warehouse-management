package com.warehousemanagement.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user_authority")
public class UserAuthorityEntity implements Serializable {
    private int userId;
    private long authorityId;

    @Id
    @Basic
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Id
    @Basic
    @Column(name = "authority_id")
    public long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(long authorityId) {
        this.authorityId = authorityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAuthorityEntity that = (UserAuthorityEntity) o;
        return userId == that.userId && authorityId == that.authorityId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, authorityId);
    }

}
