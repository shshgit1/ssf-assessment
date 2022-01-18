package tfipssf.assessment.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import tfipssf.assessment.Repository.BookRepository;

@Service
public class BookService {
    Logger logger=Logger.getLogger("from bookservice");
    //String url_openlib="http://openlibrary.org/search.json?q=the+lord+of+the+rings";
    String url_openlib="http://openlibrary.org/search.json";
    int NumberOfResults=20;

    @Autowired BookRepository bookrepo;

public HashMap<String, String> search (String searchTerm){
 

         final String url = UriComponentsBuilder
                .fromUriString(url_openlib)
                .queryParam("q", searchTerm.trim().replace(" ","+"))
                .queryParam("limit", NumberOfResults)
                .toUriString(); 

        RequestEntity req=RequestEntity.get(url).build();
        RestTemplate temple=new RestTemplate();
        ResponseEntity<String> resp=temple.exchange(req, String.class);

        if (resp.getStatusCode()!=HttpStatus.OK)
        throw new IllegalArgumentException("eerrrror!");

        String body=resp.getBody();

        try (InputStream is=new ByteArrayInputStream(body.getBytes())){
        JsonReader reader= Json.createReader(is);
        JsonObject result=reader.readObject();
        JsonArray objInDocs= result.getJsonArray("docs");

       // List<String> searchResultList=new ArrayList<>();
            HashMap<String, String> searchResultList=new HashMap<>();
        for(int x=0;x<objInDocs.size();x++){
       
        if (objInDocs.getJsonObject(x).getBoolean("has_fulltext")==true){
            
        String titleOfBook=(String)objInDocs.getJsonObject(x).getString("title");
        String KeyOfBook=(String)objInDocs.getJsonObject(x).getString("key");

        logger.log(Level.INFO, "title is "+titleOfBook);

       // searchResultList.add(titleOfBook);
       searchResultList.put(KeyOfBook,titleOfBook);
        }//end if
        
        }//end for
       
                
        return searchResultList;
       
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }


       
    }

    public void savetorepo(String a, String b){
        bookrepo.save(a,b);
    }

    public void retrieveFromRepo(String key){
        bookrepo.retrieve(key);
    }
    

}
