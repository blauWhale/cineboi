package ch.bbcag.cineboi.helper;

public class ApiHelper {

    public  String create_API_URL(int id){
        return "https://api.themoviedb.org/3/movie/" + id + "?api_key=fa11728f6e81c5f05fb42f521fb71283";
    }

}
