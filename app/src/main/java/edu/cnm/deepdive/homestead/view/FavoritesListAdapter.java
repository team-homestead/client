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
import edu.cnm.deepdive.homestead.model.Agency;
import edu.cnm.deepdive.homestead.model.Service;
import java.util.List;

public class FavoritesListAdapter extends ArrayAdapter<Agency> {

  private static final String SERVICE_TYPE_DELIMITER = ", ";
  private final Context context;
  private final OnAgencyClickListener clickListener;
  private final OnFavoriteClickListener favoriteListener;

  public FavoritesListAdapter(Context context, List<Agency> agencies,
      OnAgencyClickListener clickListener,
      OnFavoriteClickListener favoriteListener) {
    super(context, R.layout.item_agency, agencies);
    this.context = context;
    this.clickListener = clickListener;
    this.favoriteListener = favoriteListener;
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
    if (agency.getAddress() != null) {
      addressText.setText(agency.getAddress());
    } else {
      addressText.setVisibility(View.GONE);
    }
    if (agency.getPhoneNumber() != null) {
      phoneNumberText.setText(agency.getPhoneNumber());
    } else {
      phoneNumberText.setVisibility(View.GONE);
    }
    if (agency.getEmail() != null) {
      emailText.setText(agency.getEmail());
    } else {
      emailText.setVisibility(View.GONE);
    }
    if (agency.getDescription() != null) {
      agencyDescriptionText.setText(agency.getDescription());
    } else {
      agencyDescriptionText.setVisibility(View.GONE);
    }
    StringBuilder builder = new StringBuilder();
    for (Service service : agency.getServices()) {
      builder.append(service.getServiceType().toString()).append(SERVICE_TYPE_DELIMITER);
    }
    serviceTypeText.setText(builder
        .substring(0, Math.max(0, builder.length() - SERVICE_TYPE_DELIMITER.length())));
    if (agency.isFavorite()) {
      favoriteImg.setImageResource(R.drawable.ic_heart_red);
    } else {
      favoriteImg.setImageResource(R.drawable.ic_heart_grey);
    }
    convertView.setOnClickListener((v) -> clickListener.onAgencyClick(position, v, agency));
    favoriteImg.setOnClickListener((v) -> favoriteListener.onFavoriteClick(position, v, agency));
    return convertView;
  }

  public interface OnAgencyClickListener {
    void onAgencyClick(int position, View view, Agency agency);
  }

  public interface OnFavoriteClickListener {
    void onFavoriteClick(int position, View view, Agency agency);
  }

}
