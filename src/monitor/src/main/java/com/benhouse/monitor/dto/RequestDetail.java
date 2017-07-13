package com.benhouse.monitor.dto;

/**
 * Created by yunfeima on 2017/7/12.
 */
public class RequestDetail {
    private String postContent;
    private String responseContent;
    private String errorStack;

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public String getErrorStack() {
        return errorStack;
    }

    public void setErrorStack(String errorStack) {
        this.errorStack = errorStack;
    }
}
