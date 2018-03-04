package com.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ValidateSchema 
{
    public static void validateSchema(String strJsonResponse, String strJsonSchemaFilePath)
    {
        try
        {
            //Declared a jsonObj, rawSchema variables of type JSONObject
            JSONObject jsonObj,rawSchema;
            
            Boolean isValidationRequired = true;
            
            //Use File Path to read the JSON SCHEMA from file to a String
            String strSchema = new String(Files.readAllBytes(Paths.get(strJsonSchemaFilePath)));
            
            //Extracting the SubString after the first occurrence of '{'
            strSchema = strSchema.substring(strSchema.indexOf('{'));
            
            //We construct a JSONToken from the String
            //We construct a JSONObject from the JSONToken and assign it to rawShema object
            rawSchema = new JSONObject(new JSONTokener(strSchema));
            
            //Load JSON schema to a schema validator object
            Schema schema = SchemaLoader.load(rawSchema);
            
            //System.out.println(schema.getDescription());
            Log.info("**********************  BEGIN SCHEMA VALIDATIONS **************************");
            Log.info("The Schema Description is : " + schema.getDescription());
            
            //Checking if JSON Response is an Object or an Array of Objects
            if(strJsonResponse.trim().startsWith("["))
            {
                //System.out.println("JSON Array");
                
                //Construct a JSONArray from a source JSON text and assign it to JSONArray object
                JSONArray jsonArray = new JSONArray(strJsonResponse);
                
                //Assigning the first JSON Object from the array of Objects to JOSNObject instance
                
                jsonObj = (JSONObject) jsonArray.get(0);
            }
            else
            {
                //System.out.println("JSON Object");
                
                //Construct a JSONObject from a source JSON text string and assign it to jsonObject instance
                jsonObj =  new JSONObject(strJsonResponse);
                
                //remove any white spaces and check whether List of items
                //if its a list we fetch the first item and assign it to jsonObject instance
                //if(strJsonResponse.trim().startsWith("{\n  \"items\"")) Not working
                
                //if(strJsonResponse.trim().contains("{  \"items\": [    {")) Working
                if(strJsonResponse.trim().contains("\"items\":"))
                {
                    JSONArray jArray = jsonObj.getJSONArray("items");
                    if(jArray.length() > 0)
                        jsonObj = (JSONObject) jArray.get(0);
                    else
                    {
                        isValidationRequired = false;
                        Log.info("The response is empty. So jsonObject isn't calculated");
                    }
                }
            }
            
            //perform the shcema validation on jsonObject
            if(isValidationRequired)
                schema.validate(jsonObj);
            else
                Log.info("Since the jsonObject is null, didn't perform schema validations");
            
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(ValidationException e)
        {
            //System.out.println(e.getMessage());
            Log.info(e.getMessage());
            List<ValidationException> lstExceptions =  e.getCausingExceptions();
            for(ValidationException v : lstExceptions)
                Log.info(v.getMessage());
            
            /*e.getCausingExceptions().stream()
            .map(ValidationException::getMessage)
            .forEach(System.out::println);*/
        }
        finally
        {
            Log.info("**********************  END OF SCHEMA VALIDATIONS **************************");
        }
    }
}