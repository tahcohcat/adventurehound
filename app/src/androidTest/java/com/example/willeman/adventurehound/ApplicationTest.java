package com.example.willeman.adventurehound;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.couchbase.lite.Database;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.View;
import com.couchbase.lite.android.AndroidContext;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Test
    public void simpletest_DoesNothing_ReturnsTrue() {
        assertTrue(true);
    }

    @Test
    public void couchbase_ReturnView_ReturnsTrue() {

        Database database = null;

        android.content.Context context = null;
        String databaseName = "list_test01";

        try {
            Manager manager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
            database = manager.getDatabase(databaseName);
        } catch (Exception e) {
            assert(false);
        }

        View categoryView = database.getView("details");
        categoryView.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                List<String> sportDetails = (List) document.get("category:sport");
                emitter.emit("sportDetails", sportDetails);
            }
        }, "2");
    }
}