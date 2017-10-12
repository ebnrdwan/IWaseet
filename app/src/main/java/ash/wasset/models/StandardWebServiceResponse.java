package ash.wasset.models;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by amr on 11/22/16.
 */
public class StandardWebServiceResponse {

    private  int Code;
    private String ArabicMessage;
    private String EnglishMessage;
    private  boolean Success;
    private Object Data;
    private String ServiceName;

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getArabicMessage() {
        return ArabicMessage;
    }

    public void setArabicMessage(String arabicMessage) {
        ArabicMessage = arabicMessage;
    }

    public String getEnglishMessage() {
        return EnglishMessage;
    }

    public void setEnglishMessage(String englishMessage) {
        EnglishMessage = englishMessage;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public Object getData() {
        return Data;
    }

    public void setData(Object data) {
        Data = data;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }
}
