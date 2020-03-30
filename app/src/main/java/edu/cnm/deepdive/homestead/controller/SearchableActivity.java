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

public class SearchableActivity extends ListActivity {

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
        "user",
        new String[] { "_agency" },
        "name like '%?%' or agencyType like '%?%'",
        new String[] { query, query, query },
        null,
        null,
        null
        );
    while (cursor.moveToNext()) {
        result.add(cursor.getString(0));
  }
  cursor.close();
  return result;
  }
}
