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
import java.util.List;

public class FavoritesListAdapter extends ArrayAdapter<Agency> {

  private Context context;
  List<Agency> agencies;
  SharedPreference sharedPreference;

  public FavoritesListAdapter(Context context, List<Agency> agencies) {
    super(context, R.layout.item_agency, agencies);
    this.context = context;
    this.agencies = agencies;
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
  public int getCount() {
    return agencies.size();
  }

  @Override
  public Agency getItem(int position) {
    return agencies.get(position);
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder = null;
    if (convertView == null) {
      LayoutInflater inflater = (LayoutInflater) context
          .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
      convertView = inflater.inflate(R.layout.item_agency, null);
      holder = new ViewHolder();
   /*   holder.agencyNameText = (TextView) convertView.findViewById(R.id.agency_name_title);
      holder.addressText =  (TextView) convertView.findViewById(R.id.agency_address_text);
      holder.phoneNumberText = (TextView) convertView.findViewById(R.id.agency_phone_number_text);
      holder.emailText = (TextView) convertView.findViewById(R.id.agency_email_text);
      holder.agencyTypeText = (TextView) convertView.findViewById(R.id.agency_type_text);
      holder.serviceTypeText = (TextView) convertView.findViewById(R.id.agency_service_type);
      holder.agencyDescriptionText = (TextView) convertView.findViewById(R.id.agency_description);
      holder.favoriteImg = (ImageButton) convertView.findViewById(R.id.agency_favorite_button); */

      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    Agency agency = (Agency) getItem(position);
 /*   holder.agencyNameText.setText(agency.getUsers());
    holder.addressText.setText(agency.getAddress());
    holder.phoneNumberText.setText(agency.getPhoneNumber());
    holder.emailText.setText(agency.getEmail());
    holder.agencyTypeText.setText(agency.getAgencyType());
    holder.serviceTypeText.setText(agency.getServices());
    holder.agencyDescriptionText.setText(agency.getDescription()); */

    if (checkFavoriteItem(agency)) {
      holder.favoriteImg.setImageResource(R.drawable.ic_heart_red);
      holder.favoriteImg.setTag("red");
    } else {
      holder.favoriteImg.setImageResource(R.drawable.ic_heart_grey);
      holder.favoriteImg.setTag("grey");
    }
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

  @Override
  public void add(Agency agency) {
    super.add(agency);
    agencies.add(agency);
    notifyDataSetChanged();
  }

  @Override
  public void remove(Agency agency) {
    super.remove(agency);
    agencies.remove(agency);
    notifyDataSetChanged();
  }

}
