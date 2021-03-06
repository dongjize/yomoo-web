package com.yomoo.yomooweb.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2017-12-19
 * @Time: 21:43
 */
public abstract class BaseModel implements Serializable {
    @SerializedName("created_at")
    protected String createdAt;

    @SerializedName("updated_at")
    protected String updatedAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
