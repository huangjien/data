package atest.data.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class Instance {
    JsonObject basicInformation = new JsonObject();
    static JsonParser jsonParser = new JsonParser();

    public String getId() {
        return id;
    }

    private final String id;

    public Instance(String id){
        this.id = id;
        basicInformation.addProperty ("id", this.id);
    }

    public String update(String jsonString){
        JsonElement info = jsonParser.parse(jsonString);
        for (String key : info.getAsJsonObject().keySet()) {
            // you should not change id!
            if(key.equals("id"))
                continue;
            if(this.basicInformation.has(key)){
                this.basicInformation.remove(key);
            }
            this.basicInformation.add(key, info.getAsJsonObject().get(key));
        }
        return this.basicInformation.toString();
    }

    @Override
    public String toString() {
        return this.basicInformation.toString();
    }

}
