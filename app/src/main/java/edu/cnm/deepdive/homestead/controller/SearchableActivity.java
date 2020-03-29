package edu.cnm.deepdive.homestead.controller;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import edu.cnm.deepdive.homestead.R;
import java.util.ArrayList;
import java.util.List;

/* public class SearchableActivity extends ListActivity {

  protected SQLiteDatabase database;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_searchable);

    Intent intent = getIntent();
    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
      String query = intent.getStringExtra(SearchManager.QUERY);
      doMySearch(query);
    }
  }

  public List<String> doMySearch(String query) {
    List<String> result = new ArrayList<String>();

    Cursor cursor = database.query(
        "agency",
        new String[] { "_id" },
        "name like '%?%' or type like '%?%'",
        new String[] { query, query, query },
        null,
        null,
        null
        );
    while (c.moveToNext()) {
        result.add(c.getString(0));
  }
  c.close();
  return result;
  }
}*/
