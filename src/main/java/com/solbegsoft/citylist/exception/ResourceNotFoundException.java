package com.solbegsoft.citylist.exception;

public class ResourceNotFoundException extends BaseException {

    private static final String RECOURSE_NOT_FOUND = "Resource not found";
    private static final String RECOURSE_BY_ID_NOT_FOUND = "Resource by ID = '%s' not found";
    private static final String RECOURSE_BY_FIELD_NOT_FOUND = "Resource by %s = '%s' not found";

    public ResourceNotFoundException() {
        super(RECOURSE_NOT_FOUND);
    }

    public ResourceNotFoundException(String id) {
        super(String.format(RECOURSE_BY_ID_NOT_FOUND, id));
    }

    public ResourceNotFoundException(String fieldName, String value) {
        super(String.format(RECOURSE_BY_FIELD_NOT_FOUND, fieldName, value));
    }
}
