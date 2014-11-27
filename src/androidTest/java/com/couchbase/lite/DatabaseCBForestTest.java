package com.couchbase.lite;


import com.couchbase.lite.util.Log;
import com.couchbase.test.lite.LiteTestCaseBase;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hideki on 11/21/14.
 */
public class DatabaseCBForestTest  extends LiteTestCaseBase/*LiteTestCase*/{
    public static final String TAG = "DatabaseCBForestTest";
    protected String DEFAULT_TEST_DB = "cblite-test";
    protected String dbFilePath = null;
    protected Context context = new LiteTestContext();

    @Override
    protected void setUp() throws Exception {
        Log.v(TAG, "setUp");
        super.setUp();

        // database file path
        dbFilePath = getDBFilePath(DEFAULT_TEST_DB);

        // delete file
        File file = new File(dbFilePath);
        if(file.exists())
            file.delete();
    }

    private String getDBFilePath(String name){
        name = name.replace('/', ':');
        return context.getFilesDir() + File.separator + name + Manager.DATABASE_SUFFIX;
    }

    @Override
    protected void tearDown() throws Exception {
        Log.i(TAG, "tearDown");
        super.tearDown();
    }

    public void testHelloWorld() throws Exception{

        // Create database without Manager
        Database database = new DatabaseCBForest(dbFilePath, null);
        assertNotNull(database);
        assertNull(database.getManager());
        assertEquals(database.getName(), DEFAULT_TEST_DB);
        assertEquals(database.getPath(), dbFilePath);
        assertTrue(database.open());
        Log.i(TAG, database.getPath());

        // get the current date and time
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = GregorianCalendar.getInstance();
        String currentTimeString = dateFormatter.format(calendar.getTime());

        // create an object that contains data for a document
        Map<String, Object> docContent = new HashMap<String, Object>();
        docContent.put("message", "Hello Couchbase Lite");
        docContent.put("creationDate", currentTimeString);

        // display the data for the new document
        Log.w(TAG, "docContent=" + String.valueOf(docContent));

        // create an empty document
        Document document = database.createDocument();
        assertNotNull(document);

        // write the document to the database
        try {
            document.putProperties(docContent);
            Log.w(TAG, "Document written to database named " + database.getName() + " with ID = " + document.getId());
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Cannot write document to database", e);
        }

        // save the ID of the new document
        String docID = document.getId();
        assertNotNull(docID);
        assertNotSame("", docID);

        // retrieve the document from the database
        Document retrievedDocument = database.getDocument(docID);
        assertNotNull(retrievedDocument);

        // display the retrieved document
        Log.w(TAG, "retrievedDocument=" + String.valueOf(retrievedDocument.getProperties()));

        // delete database
        database.delete();
        // close database
        assertTrue(database.close());
    }

/*
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
    */
}
