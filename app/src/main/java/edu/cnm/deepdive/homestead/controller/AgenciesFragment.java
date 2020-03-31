package edu.cnm.deepdive.homestead.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.homestead.R;
import edu.cnm.deepdive.homestead.model.Agency;
import edu.cnm.deepdive.homestead.view.FavoritesListAdapter;
import edu.cnm.deepdive.homestead.viewmodel.AgencyViewModel;
import java.util.List;
import java.util.UUID;

public class AgenciesFragment extends Fragment implements
    OnItemClickListener, OnItemLongClickListener {

    public static final String ARG_ITEM_ID = "agency_list";

    Activity activity;
    ListView agencyListView;
    List<Agency> agencies;
    FavoritesListAdapter agencyListAdapter;

    SharedPreference sharedPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        sharedPreference = new SharedPreference();
    }

    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agencies, container, false);
        findViewsById(view);
        agencyListAdapter = new FavoritesListAdapter(activity, agencies);
        agencyListView.setAdapter(agencyListAdapter);
        agencyListView.setOnItemClickListener(this);
        agencyListView.setOnItemLongClickListener(this);
        return view;
    }

    private void findViewsById(View view) {
        agencyListView = (ListView) view.findViewById(R.id.agencies_list);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Agency agency = (Agency) parent.getItemAtPosition(position);
        Toast.makeText(activity, agency.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
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
    }

    @Override
    public void onResume() {
        getActivity().setTitle(R.string.app_name);
        getActivity().getActionBar().setTitle(R.string.app_name);
        super.onResume();
    }

}