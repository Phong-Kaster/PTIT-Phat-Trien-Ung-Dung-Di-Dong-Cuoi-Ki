package com.example.prudentialfinance.Container;

import com.example.prudentialfinance.Model.Goal;
import com.example.prudentialfinance.Model.Summary;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GoalAdd {

    @SerializedName("result")
    @Expose
    private Integer result;

    @SerializedName("goal")
    @Expose
    private int goal;

    @SerializedName("method")
    @Expose
    private String method;

    @SerializedName("msg")
    @Expose
    private String msg;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "GoalAdd{" +
                "result=" + result +
                ", goal=" + goal +
                ", method='" + method + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
