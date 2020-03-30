package edu.cnm.deepdive.homestead.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import edu.cnm.deepdive.homestead.R;
import edu.cnm.deepdive.homestead.viewmodel.ResourcesViewModel;

public class ResourcesFragment extends Fragment {

  private ResourcesViewModel resourcesViewModel;

  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    resourcesViewModel =
        ViewModelProviders.of(this).get(ResourcesViewModel.class);
    View root = inflater.inflate(R.layout.fragment_resources, container, false);
    final TextView textView = root.findViewById(R.id.text_resources);
    resourcesViewModel.getText().observe(this, new Observer<String>() {
      @Override
      public void onChanged(@Nullable String s) {
        textView.setText(s);
      }
    });
    return root;
  }
}