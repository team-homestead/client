package edu.cnm.deepdive.homestead.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.gson.Gson;
import edu.cnm.deepdive.homestead.model.Agency;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreference {

  public static final String PREFS_NAME = "Homestead";
  public static final String FAVORITES = "Favorites";

  public SharedPreference() {
    super();
  }

  public void saveFavorites(Context context, List<Agency> favorites) {
    SharedPreferences settings;
    Editor editor;

    settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    editor = settings.edit();

    Gson gson = new Gson();
    String jsonFavorites = gson.toJson(favorites);

    editor.putString(FAVORITES, jsonFavorites);

    editor.commit();
  }

  public void addFavorite(Context context, Agency agency) {
    List<Agency> favorites = getFavorites(context);

    if (favorites == null) {
      favorites = new ArrayList<Agency>();
    }
    favorites.add(agency);
    saveFavorites(context, favorites);
  }

  public void removeFavorite(Context context, Agency agency) {
    ArrayList<Agency> favorites = getFavorites(context);

    if (favorites != null) {
      favorites.remove(agency);
      saveFavorites(context, favorites);
    }
  }

  public ArrayList<Agency> getFavorites(Context context) {
    SharedPreferences settings;
    List<Agency> favorites;

    settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

    if (settings.contains(FAVORITES)) {
      String jsonFavorites = settings.getString(FAVORITES, null);
      Gson gson = new Gson();
      Agency[] favoriteItems = gson.fromJson(jsonFavorites, Agency[].class);
      favorites = Arrays.asList(favoriteItems);
      favorites = new ArrayList<Agency>(favorites);
    } else {
      return null;
    }
    return (ArrayList<Agency>) favorites;
  }
}
