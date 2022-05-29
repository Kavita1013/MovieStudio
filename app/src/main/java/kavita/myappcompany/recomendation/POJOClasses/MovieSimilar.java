package kavita.myappcompany.recomendation.POJOClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieSimilar {

    @SerializedName("text")
    @Expose
    private String text;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MovieSimilar( String text) {

        this.text = text;
    }
}
