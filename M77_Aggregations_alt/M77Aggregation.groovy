#! /bin/groovy
// imports for Java, Groovy and MongoDB
@Grab('org.mongodb:mongodb-driver:3.12.7')

import java.util.Arrays;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.conversions.Bson;
import java.util.concurrent.TimeUnit;
import org.bson.Document;


import com.mongodb.client.MongoClients;
import com.mongodb.ExplainVerbosity;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.model.Filters.*;
import com.mongodb.client.model.Updates.*;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import com.mongodb.util.*;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;



public static void main(String[] args) {

           /*
     * Requires the MongoDB Java Driver.
     * https://mongodb.github.io/mongo-java-driver
     */

    MongoClient mongoClient = new MongoClient(
        new MongoClientURI(
            "mongodb://127.0.0.1:27018/?readPreference=primary&appname=MongoDB%20Compass&ssl=false"
        )
    );
    MongoDatabase database = mongoClient.getDatabase("VSB_cattle");
    MongoCollection<Document> collection = database.getCollection("Basic_data_RIC");

    AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document('$project', 
        new Document("Transponder", 1L)
                .append("Animalnumber", 1L)
                .append("AnimalID", 1L)
                .append("IsoDate", 1L)
                .append("Date", 1L)
                .append("Barn", 1L)), 
        new Document('$match', 
        new Document("IsoDate", 
        new Document('$gt', 
        new java.util.Date(1676073600000L)))
                .append("Animalnumber", 
        new Document('$in', Arrays.asList("638", "652", "705", "709", "711", "717", "727", "735", "741", "745", "748", "754", "802", "804", "806", "807", "814", "815", "818", "822", "823", "824", "842", "850", "851", "852", "855", "856", "901", "903", "911", "913", "930", "934", "935", "938", "939", "941", "942", "943", "948", "6111", "6113", "6114", "6125", "6133", "6140", "6144", "6147", "6149", "6150", "6175")))), 
        new Document('$lookup', 
        new Document("from", "Liveweight_data")
                .append("let", 
        new Document("animalID", '$AnimalID')
                    .append("date", '$Date'))
                .append("pipeline", Arrays.asList(new Document('$match', 
                    new Document('$expr', 
                    new Document('$and', Arrays.asList(new Document('$eq', Arrays.asList('$AnimalID', '$$animalID')), 
                                    new Document('$eq', Arrays.asList('$Date', '$$date'))))))))
                .append("as", "LiveweightData")), 
        new Document('$lookup', 
        new Document("from", "Milking_data")
                .append("let", 
        new Document("animalID", '$AnimalID')
                    .append("date", '$Date'))
                .append("pipeline", Arrays.asList(new Document('$match', 
                    new Document('$expr', 
                    new Document('$and', Arrays.asList(new Document('$eq', Arrays.asList('$AnimalID', '$$animalID')), 
                                    new Document('$eq', Arrays.asList('$Date', '$$date'))))))))
                .append("as", "Milk")), 
        new Document('$lookup', 
        new Document("from", "Concentrate_data")
                .append("let", 
        new Document("animalID", '$AnimalID')
                    .append("date", '$Date'))
                .append("pipeline", Arrays.asList(new Document('$match', 
                    new Document('$expr', 
                    new Document('$and', Arrays.asList(new Document('$eq', Arrays.asList('$AnimalID', '$$animalID')), 
                                    new Document('$eq', Arrays.asList('$Date', '$$date'))))))))
                .append("as", "Conc")), 
        new Document('$lookup', 
        new Document("from", "Roughage_data")
                .append("let", 
        new Document("animalID", '$AnimalID')
                    .append("date", '$Date'))
                .append("pipeline", Arrays.asList(new Document('$match', 
                    new Document('$expr', 
                    new Document('$and', Arrays.asList(new Document('$eq', Arrays.asList('$AnimalID', '$$animalID')), 
                                    new Document('$eq', Arrays.asList('$Date', '$$date'))))))))
                .append("as", "Roughage")), 
        new Document('$lookup', 
        new Document("from", "Water_data")
                .append("let", 
        new Document("animalID", '$AnimalID')
                    .append("date", '$Date'))
                .append("pipeline", Arrays.asList(new Document('$match', 
                    new Document('$expr', 
                    new Document('$and', Arrays.asList(new Document('$eq', Arrays.asList('$AnimalID', '$$animalID')), 
                                    new Document('$eq', Arrays.asList('$Date', '$$date'))))))))
                .append("as", "Water")), 
        new Document('$project', 
        new Document("Transponder", 1L)
                .append("Animalnumber", 1L)
                .append("AnimalID", 1L)
                .append("IsoDate", 1L)
                .append("Date", 1L)
                .append("Barn", 1L)
                .append("Liveweight_morning", 
        new Document('$arrayElemAt', Arrays.asList('$LiveweightData.Liveweight', 0L)))
                .append("Liveweight_evening", 
        new Document('$arrayElemAt', Arrays.asList('$LiveweightData.Liveweight', 1L)))
                .append("MilkAmount_morning", 
        new Document('$arrayElemAt', Arrays.asList('$Milk.Amount', 0L)))
                .append("MilkAmount_evening", 
        new Document('$arrayElemAt', Arrays.asList('$Milk.Amount', 1L)))
                .append("ConcentrateSilo1", 
        new Document('$sum', '$Conc.Intake_Silo01_C_cow'))
                .append("ConcentrateSilo2", 
        new Document('$sum', '$Conc.Intake_Silo02_C_cow'))
                .append("ConcentrateSilo3", 
        new Document('$sum', '$Conc.Intake_Silo03_C_cow'))
                .append("ConcentrateSilo4", 
        new Document('$sum', '$Conc.Intake_Silo04_C_cow'))
                .append("Roughage", 
        new Document('$sum', '$Roughage.Intake_R_cow'))
                .append("Water", 
        new Document('$sum', '$Water.Intake_W_cow'))))); /*, 
        new Document('$out', 
        new Document("db", "Masterdata")
                .append("coll", "M77-groovy"))));*/
    MongoDatabase dbExport = mongoClient.getDatabase("Masterdata");
    MongoCollection<Document> coll = dbExport.getCollection("M77_groovy");

    //coll.insertMany(result);
    //result.each{doc-> coll.insertOne(doc)}

    result.each{object->
        //find first object based on object structure
        def filter = new BasicDBObject()
        //IsoDate IsoDate
        filter.put("IsoDate", object.IsoDate)
        //ID ID         
        filter.put("AnimalID", object.AnimalID)
        def doc=coll.find(filter).first()
        //if doc emty, insert object as document into collection
        if(doc==null){
            coll.insertOne(new Document(object))
        }else{              
            doc.remove("_id")
            //if doc exists and not equal to object, insert object
            if(doc!=object){
                coll.insertOne(new Document(object))
            }
        }
    }
}
