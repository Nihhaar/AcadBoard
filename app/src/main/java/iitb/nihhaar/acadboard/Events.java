package iitb.nihhaar.acadboard;

/**
 * Created by Nihhaar on 11/5/2016.
 */

public class Events {
    public String name;
    public String type;
    public String description;
    public Events(String name, String type, String description){

        this.name = name;
        this.type = type;
        this.description=description;

    }

    public String getType(){
        return this.type;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }
}
