package atest.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Message {

    private final JsonObject messageObject = new JsonObject();
    public Message(){

    }

    private static String stackTrace(Exception ex){
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
        JsonElement jsonElement = jsonParser.parse(jsonString);
        if(jsonElement.isJsonArray()){
            JsonArray array = new JsonArray();
            JsonArray source = jsonElement.getAsJsonArray();
            for (int i = 0; i < source.size(); i++) {
                array.add(source.get(i).getAsJsonObject().get("_source"));
            }
            this.add(array);
        } else {
            this.add(jsonElement.getAsJsonObject());
        }
        return this;

    }

    public Message add(JsonArray jsonArray){
        messageObject.add("_source", jsonArray);
        return this;
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

    public static Message Exception(Exception ex){
        ex.printStackTrace();
        return Message.Fail().add("exception", ex.getMessage()).add("stack", Message.stackTrace(ex));
    }

    @Override
    public String toString(){
        return this.messageObject.toString();
    }
}
