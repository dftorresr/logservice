
package co.edu.escuelaing.log_service;
import java.time.LocalDate;
import static spark.Spark.*;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bson.Document;




/**
 *
 * @author dtorres
 */
public class LogService {
    
    public static Map <String,String> imdb = new HashMap();    
    public static void main(String[] args) {
        
        port(getPort());
        get("/logmsg", (req, res) -> storeLogMessage(req,res));
                 
        
    }
    
       
    
    public static String storeLogMessage(Request req, Response res ){
        
        MongoClient mongo = new MongoClient("mongodbta",27017);
        // Accessing the database 
        MongoDatabase database = mongo.getDatabase("tallerdb");
        MongoCollection<Document> collection = database.getCollection("mensajesusuario");
        System.out.println("Collection mensajesusuario selected successfully");
                
        String msg = req.queryParams("msg");
        String key = LocalDate.now().toString();
        imdb.put(key,msg);
        
        Document document = new Document("Fecha",key)
                .append("Mensaje", msg);

        //Inserting document into the collection
        collection.insertOne(document);
        System.out.println("Document inserted successfully");
        Document documentsort = new Document("Fecha",-1);
        
        FindIterable<Document> iterDoc = collection.find().sort(documentsort).limit(10);
        List<String> list = new ArrayList<String>(); 
                MongoCursor<Document> cursor = iterDoc.iterator();
		while (cursor.hasNext()) {
                        list.add(cursor.next().toJson());                       
		}
        return list.toString();
       //return "Mensajes almacenados: " + msg;
                  
    }   
   
    
    public static Integer getPort(){
        if(System.getenv("PORT")!= null ){
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }
    
}
