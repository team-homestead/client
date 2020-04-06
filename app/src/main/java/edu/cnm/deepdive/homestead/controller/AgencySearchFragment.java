package edu.cnm.deepdive.homestead.controller;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.homestead.R;
import edu.cnm.deepdive.homestead.model.Agency;
import edu.cnm.deepdive.homestead.view.FavoritesListAdapter;
import edu.cnm.deepdive.homestead.view.FavoritesListAdapter.OnAgencyClickListener;
import edu.cnm.deepdive.homestead.view.FavoritesListAdapter.OnFavoriteClickListener;
import edu.cnm.deepdive.homestead.viewmodel.AgencyViewModel;
import java.util.ArrayList;
import java.util.List;

public class AgencySearchFragment extends Fragment implements
    OnItemClickListener, /*OnItemLongClickListener, */ OnAgencyClickListener,
    OnFavoriteClickListener, OnQueryTextListener, OnItemSelectedListener {

  public static final String ARG_ITEM_ID = "agency_list";

  Activity activity;
  ListView agencyListView;
  List<Agency> agencies;
  FavoritesListAdapter agencyListAdapter;
  private AgencyViewModel viewModel;
  private SearchView nameFilter;
  private Spinner serviceTypeFilter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_agency_search, container, false);
    findViewsById(view);

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(this).get(AgencyViewModel.class);
    viewModel.getSearchAgencies().observe(getViewLifecycleOwner(), (agencies) -> {
      FavoritesListAdapter adapter = new FavoritesListAdapter(getContext(), agencies, this,
          this);
      agencyListView.setAdapter(adapter);
    });
    viewModel.setSearchFilter(null, null);
    viewModel.getServiceTypes().observe(getViewLifecycleOwner(), (types) -> {
        List<String> expandedTypes = new ArrayList<>(types);
        expandedTypes.add(0, "(Select Service Type)");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            getContext(), android.R.layout.simple_list_item_1, expandedTypes);
        serviceTypeFilter.setAdapter(adapter);
    });
  }


  private void findViewsById(View view) {
    agencyListView = view.findViewById(R.id.agencies_list);
    nameFilter = view.findViewById(R.id.name_filter);
    nameFilter.setOnQueryTextListener(this);
    serviceTypeFilter = view.findViewById(R.id.service_type_filter);
    serviceTypeFilter.setOnItemSelectedListener(this);
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Agency agency = (Agency) parent.getItemAtPosition(position);
    Toast.makeText(activity, agency.toString(), Toast.LENGTH_LONG).show();
  }

  /*  @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long arg3) {
        ImageButton button = (ImageButton) view.findViewById(R.id.agency_favorite_button);
        String tag = button.getTag().toString();
        if (tag.equalsIgnoreCase("grey")) {
            sharedPreference.addFavorite(activity, agencies.get(position));
            Toast.makeText(activity,
                activity.getResources().getString(R.string.add_favr), Toast.LENGTH_SHORT).show();
            button.setTag("red");
            button.setImageResource(R.drawable.ic_heart_red);
        } else {
            sharedPreference.removeFavorite(activity, agencies.get(position));
            button.setTag("grey");
            button.setImageResource(R.drawable.ic_heart_grey);
            Toast.makeText(activity,
                activity.getResources().getString(R.string.remove_favr), Toast.LENGTH_SHORT).show();
        }

        return true;
    } */

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

    @Override
    public boolean onQueryTextSubmit(String query) {
      String fragment = query.trim();
      if (fragment.length() >= 3) {
          viewModel.setSearchFilter(fragment,
              (serviceTypeFilter.getSelectedItemPosition() != 0)
                  ? (String) serviceTypeFilter.getSelectedItem() : null);
      }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String fragment = nameFilter.getQuery().toString().trim();
        String serviceType = (String) parent.getItemAtPosition(position);
      if (position > 0) {
          viewModel.setSearchFilter((fragment.length() >= 3) ? fragment : null, serviceType);
      } else {
          viewModel.setSearchFilter((fragment.length() >= 3) ? fragment : null, null);
      }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}