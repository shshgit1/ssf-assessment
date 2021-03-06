package tfipssf.assessment.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    
    List<String> urlkeys=new ArrayList<>();
    List<String> bookTitles=new ArrayList<>();

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
        throw new IllegalArgumentException("Error: status code %s"
        .formatted(resp.getStatusCode().toString()));

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
       
       String urlkey=KeyOfBook.substring(7);
       String urlkeylink="/book/"+urlkey;
       urlkeys.add(urlkeylink);
       bookTitles.add(titleOfBook);


       searchResultList.put(KeyOfBook,titleOfBook);


       bookrepo.save(urlkey, titleOfBook);
        }//end if
        }//end for
        return searchResultList;
       
        }//end try
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }//end catch

    } //end search method

    public List getKeyForUrl(){
        return urlkeys;
    }
    
    public List getBookTitles(){
        return bookTitles;
    }
   
    public void savetorepo(String a, String b){
        bookrepo.save(a,b);
    }

    public void retrieveFromRepo(String key){
        bookrepo.retrieve(key);
    }

    public String searchbyID(String works_id){
        final String url = UriComponentsBuilder
        .fromUriString("https://openlibrary.org/works/"+works_id+".json")
//        .queryParam("q", "description")
        .toUriString(); 

                logger.log(Level.INFO, "from id search: "+works_id);

                RequestEntity req=RequestEntity.get(url).build();
                RestTemplate temple=new RestTemplate();
                ResponseEntity<String> resp=temple.exchange(req, String.class);
        
                if (resp.getStatusCode()!=HttpStatus.OK)
                throw new IllegalArgumentException("Error: status code %s"
                .formatted(resp.getStatusCode().toString()));
        
                String body=resp.getBody();
        
                try (InputStream is=new ByteArrayInputStream(body.getBytes())){
                JsonReader reader= Json.createReader(is);
                JsonObject result=reader.readObject();
                    
          /*       JsonArray objInDocs= result.getJsonArray("description").getJsonArray(0);
                JsonObject objInDes=objInDocs.getJsonObject(0);

                String titleOfBook=objInDes.getString("title"); */
                String titleOfBook=result.getString("title");
                              
               logger.log(Level.INFO, "in id search title is "+titleOfBook);
        
               return titleOfBook;
                }
                catch(Exception e)
                {e.printStackTrace();
                    return null;
                }

    }//end searchbyID

    public String getDescription(String works_id){
        final String url = UriComponentsBuilder
        .fromUriString("https://openlibrary.org/works/"+works_id+".json")
        .queryParam("q", "description")
        .toUriString(); 

        RequestEntity req=RequestEntity.get(url).build();
        RestTemplate temple=new RestTemplate();
        ResponseEntity<String> resp=temple.exchange(req, String.class);

        if (resp.getStatusCode()!=HttpStatus.OK)
        throw new IllegalArgumentException("Error: status code %s"
        .formatted(resp.getStatusCode().toString()));

        String body=resp.getBody();

        try (InputStream is=new ByteArrayInputStream(body.getBytes())){
        JsonReader reader= Json.createReader(is);
        JsonObject result=reader.readObject();

        String descriptionOfBook=result.getString("description");
       
       logger.log(Level.INFO, "description is "+descriptionOfBook);

       return descriptionOfBook;
        }
        catch(Exception e)
        {e.printStackTrace();
            return "no description found";
        }
    }//end getdescription

    public String getExcerpt(String works_id){
        final String url = UriComponentsBuilder
        .fromUriString("https://openlibrary.org/works/"+works_id+".json")
        .queryParam("q", "excerpt")
        .toUriString(); 

        RequestEntity req=RequestEntity.get(url).build();
        RestTemplate temple=new RestTemplate();
        ResponseEntity<String> resp=temple.exchange(req, String.class);

        if (resp.getStatusCode()!=HttpStatus.OK)
        throw new IllegalArgumentException("Error: status code %s"
        .formatted(resp.getStatusCode().toString()));

        String body=resp.getBody();

        try (InputStream is=new ByteArrayInputStream(body.getBytes())){
        JsonReader reader= Json.createReader(is);
        JsonObject result=reader.readObject();

        JsonArray arrayInExcerpt= result.getJsonArray("key");
    
        JsonArray arrayinExcerpt2=arrayInExcerpt.getJsonArray(1);
        
        JsonObject a= arrayinExcerpt2.getJsonObject(1);

        String excerptOfBook=a.getString("excerpt");
        

       logger.log(Level.INFO, "excerpt is "+excerptOfBook);

       return excerptOfBook;
        }
        catch(Exception e)
        {e.printStackTrace();
            return "no excerpt found";
        }
    }//end getdescription
    
    public boolean checkifcache(String works_id){
        
        return bookrepo.checkifcache(works_id);
    }

}
