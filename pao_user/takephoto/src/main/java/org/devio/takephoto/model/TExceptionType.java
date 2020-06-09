package org.devio.takephoto.model;

/**
 * Author: JPH
 * Date: 2016/7/26 11:01
 */
public enum TExceptionType {
    TYPE_NOT_IMAGE("The selected file is not a picture"), TYPE_WRITE_FAIL("Failed to save the selected file"),
    TYPE_URI_NULL("The Uri of the selected photo is null"), TYPE_URI_PARSE_FAIL("Failed to get file path from Uri"),
    TYPE_NO_MATCH_PICK_INTENT("Intent not matched to select image"), TYPE_NO_MATCH_CROP_INTENT("Did not match the Intent to crop the image"), TYPE_NO_CAMERA("没有相机"),
    TYPE_NO_FIND("The selected file was not found");

    String stringValue;

    TExceptionType(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}
