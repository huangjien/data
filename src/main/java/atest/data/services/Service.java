package atest.data.services;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


public abstract class Service {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private List<Instance> instances = Lists.newArrayList();
    public Instance get(String id){
        for (Instance instance : instances) {
            if (instance.getId().equals(id))
                return instance;
        }
        return null;
    }

    public void remove(String id){

        synchronized ( this ) {
            for (int i = 0; i < instances.size(); i++) {
                if(instances.get(i).getId().equals(id))
                    instances.remove(i);
            }
        }

    }

    public Instance register( String className, String id) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        if (instances.contains(id))
            return this.get(id);
        Class klass = Class.forName(className);
        return (Instance) klass.getConstructor(new Class[]{String.class}).newInstance(id);

    }

    public String list(){
        return gson.toJson(instances);
    }
}
