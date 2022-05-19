package com.example.prudentialfinance.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Summary {

    @SerializedName("total_count")
    @Expose
    private Integer totalCount;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return "Summary{" +
                "totalCount=" + totalCount +
                '}';
    }
}
