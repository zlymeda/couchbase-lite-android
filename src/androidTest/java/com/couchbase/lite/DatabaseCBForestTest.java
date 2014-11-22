package com.couchbase.lite;

import com.couchbase.lite.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hideki on 11/21/14.
 */
public class DatabaseCBForestTest  extends LiteTestCase{

    // From CBL Android Tutorial
    public void testSimple() throws Exception {
        Log.i(TAG, "DatabaseCBForestTest.testSimple() - START");

        // get the current date and time
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = GregorianCalendar.getInstance();
        String currentTimeString = dateFormatter.format(calendar.getTime());

        // create an object that contains data for a document
        Map<String, Object> docContent = new HashMap<String, Object>();
        docContent.put("message", "Hello Couchbase Lite");
        docContent.put("creationDate", currentTimeString);

        // display the data for the new document
        Log.i(TAG, "docContent=" + String.valueOf(docContent));

        // create an empty document
        Document document = database.createDocument();
        assertNotNull(document);

        // write the document to the database
        try {
            document.putProperties(docContent);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Cannot write document to database", e);
        }

        // save the ID of the new document
        String docID = document.getId();
        assertNotNull(docID);

        Log.i(TAG, "docID=" + docID);

        // retrieve the document from the database
        Document retrievedDocument = database.getDocument(docID);
        assertNotNull(retrievedDocument);

        // display the retrieved document
        Log.i(TAG, "retrievedDocument=" + String.valueOf(retrievedDocument.getProperties()));

        Log.i(TAG, "DatabaseCBForestTest.testSimple() - END");
    }

}
