package org.chennaimetrorail.appv1.feederfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.fragment.FeaderService;
import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.jsonmodal.FeederAuto;

import java.util.List;

public class GalleryViewAuto extends Fragment {
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    Gallery gallery;
    ImageView selectedImage;
    ZoomageView zoomimg;
    AddImgAdp addImgAdp;

    SharedPreferences s_prf;
    SharedPreferences.Editor s_editor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.feeders_poupimg, container, false);
        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(), view);
        zoomimg = (ZoomageView) view.findViewById(R.id.myZoomageViewpop);
        gallery = (Gallery) view.findViewById(R.id.gallery);
        s_prf = getActivity().getSharedPreferences("feeders", 0);
        s_editor = s_prf.edit();

        Picasso.with(getActivity()).load(Constant.feederAutoList.get(0).getImageUrl()).into(zoomimg);

        addImgAdp = new AddImgAdp(getActivity(), Constant.feederAutoList);
        gallery.setAdapter(addImgAdp);


        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // show the selected Image
                Picasso.with(getActivity()).load(Constant.feederAutoList.get(position).getImageUrl()).into(zoomimg);


            }
        });
        /*Back button action*/
        LinearLayout image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);
        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s_editor.putString("message", "auto");
               s_editor.commit();

                fragmentTransaction.replace(R.id.fragment_place, new FeaderService());
                fragmentTransaction.commit();

            }
        });
        /*Title Text*/
        TextView title_txt = (TextView) view.findViewById(R.id.title_txt);
        title_txt.setText("Feeder Service Auto");

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    s_editor.putString("message", "auto");
                    s_editor.commit();

                    fragmentTransaction.replace(R.id.fragment_place, new FeaderService());
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    public class AddImgAdp extends BaseAdapter {

        List<FeederAuto> feederAutoList;
        private Context cont;

        public AddImgAdp(Context c, List<FeederAuto> feederAutoList) {
            cont = c;
            this.feederAutoList = feederAutoList;
        }

        public int getCount() {
            return feederAutoList.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imgView = new ImageView(cont);

            imgView.setLayoutParams(new Gallery.LayoutParams(150, 150));
            imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            imgView.setImageResource(R.drawable.above_shadow);
            Picasso.with(cont).load(feederAutoList.get(position).getImageUrl()).resize(200, 200).into(imgView);
            return imgView;
        }
    }


}
