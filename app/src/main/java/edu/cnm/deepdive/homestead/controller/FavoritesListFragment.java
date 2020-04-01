package edu.cnm.deepdive.homestead.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.homestead.R;
import edu.cnm.deepdive.homestead.model.Agency;
import edu.cnm.deepdive.homestead.view.FavoritesListAdapter;
import edu.cnm.deepdive.homestead.viewmodel.AgencyViewModel;
import java.util.List;

public class FavoritesListFragment extends Fragment
    implements FavoritesListAdapter.OnAgencyClickListener,
    FavoritesListAdapter.OnFavoriteClickListener {

  public static final String ARG_ITEM_ID = "favorite_list";

  ListView favoritesList;

  Activity activity;
  private AgencyViewModel viewModel;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_favorites_list, container, false);
    favoritesList = view.findViewById(R.id.favorites_list);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(this).get(AgencyViewModel.class);
    viewModel.getAgencies().observe(getViewLifecycleOwner(), (agencies) -> {
      FavoritesListAdapter adapter = new FavoritesListAdapter(getContext(), agencies, this,
          this);
      favoritesList.setAdapter(adapter);
    });
  }

  public void showAlert(String title, String message) {
    if (activity != null && !activity.isFinishing()) {
      AlertDialog alertDialog = new AlertDialog.Builder(activity)
          .create();
      alertDialog.setTitle(title);
      alertDialog.setMessage(message);
      alertDialog.setCancelable(false);

      alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
          new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
              //activity.finish();
              getParentFragmentManager().popBackStackImmediate();
            }
          });
      alertDialog.show();
    }
  }

  @Override
  public void onAgencyClick(int position, View view, Agency agency) {
    Log.d(getClass().getName(), "Agency clicked");
    //TODO Respond to click on Agency by displaying ?
  }

  @Override
  public void onFavoriteClick(int position, View view, Agency agency) {
    Log.d(getClass().getName(), "Favorites clicked");
    viewModel.setFavorite(agency, !agency.isFavorite());
  }

}
