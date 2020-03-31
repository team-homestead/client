package edu.cnm.deepdive.homestead.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import edu.cnm.deepdive.homestead.R;
import edu.cnm.deepdive.homestead.controller.SharedPreference;
import edu.cnm.deepdive.homestead.model.Agency;
import edu.cnm.deepdive.homestead.model.Service;
import java.util.List;

public class FavoritesListAdapter extends ArrayAdapter<Agency> {

  public static final String SERVICE_TYPE_DELIMITER = ", ";
  private Context context;
  SharedPreference sharedPreference;

  public FavoritesListAdapter(Context context, List<Agency> agencies) {
    super(context, R.layout.item_agency, agencies);
    this.context = context;
    sharedPreference = new SharedPreference();
  }

  private class ViewHolder {
    TextView agencyNameText;
    TextView addressText;
    TextView phoneNumberText;
    TextView emailText;
    TextView agencyTypeText;
    TextView serviceTypeText;
    TextView agencyDescriptionText;
    ImageButton favoriteImg;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    Agency agency = getItem(position);
    if (convertView == null) {
      LayoutInflater inflater = (LayoutInflater) context
          .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
      convertView = inflater.inflate(R.layout.item_agency, null);
    }
    TextView agencyNameText = convertView.findViewById(R.id.agency_name_title);
    TextView addressText = convertView.findViewById(R.id.agency_address);
    TextView phoneNumberText = convertView.findViewById(R.id.agency_phone_number);
    TextView emailText = convertView.findViewById(R.id.agency_email);
    TextView agencyTypeText = convertView.findViewById(R.id.agency_type);
    TextView serviceTypeText = convertView.findViewById(R.id.agency_service_type);
    TextView agencyDescriptionText = convertView.findViewById(R.id.agency_description);
    ImageButton favoriteImg = convertView.findViewById(R.id.agency_favorite_button);
    agencyNameText.setText(agency.getName());
    if (agency.getAgencyType() != null) {
      agencyTypeText.setText(agency.getAgencyType().toString());
    }
    StringBuilder builder = new StringBuilder();
    for (Service service : agency.getServices()) {
      builder.append(service.getServiceType().toString()).append(SERVICE_TYPE_DELIMITER);
    }
    serviceTypeText.setText(builder
        .substring(0, Math.max(0, builder.length() - SERVICE_TYPE_DELIMITER.length())));
    if (checkFavoriteItem(agency)) {
      favoriteImg.setImageResource(R.drawable.ic_heart_red);
      favoriteImg.setTag("red");
    } else {
      favoriteImg.setImageResource(R.drawable.ic_heart_grey);
      favoriteImg.setTag("grey");
    }
    convertView.setOnClickListener();
    favoriteImg.setOnClickListener();
    return convertView;
  }

  public boolean checkFavoriteItem(Agency checkAgency) {
    boolean check = false;
    List<Agency> favorites = sharedPreference.getFavorites(context);
    if (favorites != null) {
      for (Agency agency : favorites) {
        if (agency.equals(checkAgency)) {
          check = true;
          break;
        }
      }
    }
    return check;
  }

  public interface OnAgencyClickListener {
    void onAgencyClick(int position, View view, Agency agency);
  }

  public interface OnFavoriteClickListener {
    void onFavoriteClick(int position, View view, Agency agency);
  }

}
