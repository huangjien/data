package atest.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Message {

    JsonObject messageObject = new JsonObject();
    public Message(){

    }

    public static String stackTrace(Exception ex){
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }

    public static Message OK() {
        Message message = new Message();
        return message.add("message", "OK");
    }

    public static Message Fail() {
        Message message = new Message();
        return message.add("message", "Failed");
    }

    public Message add(String jsonString){
        JsonParser jsonParser = new JsonParser();
        return this.add(jsonParser.parse(jsonString).getAsJsonObject());

    }

    public Message add(JsonObject jsonObject){
        for(String key : jsonObject.keySet()){
            messageObject.add(key, jsonObject.get(key));
        }
        return this;
    }

    public Message add(String key, String value){
        this.messageObject.addProperty(key, value);
        return this;
    }

    @Override
    public String toString(){
        return this.messageObject.toString();
    }
}
