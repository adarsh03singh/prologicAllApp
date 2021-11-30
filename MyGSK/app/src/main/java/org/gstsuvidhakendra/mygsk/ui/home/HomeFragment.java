package org.gstsuvidhakendra.mygsk.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import org.gstsuvidhakendra.mygsk.InnerMainScreen;
import org.gstsuvidhakendra.mygsk.R;

public class  HomeFragment extends Fragment {

   /* Long backPressedTime = Long.valueOf(0);
    Toast backToast;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                backToast = Toast.makeText(getContext(), "Press back again to leave the app.", Toast.LENGTH_SHORT);
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    backToast.cancel();
                    requireActivity().finish();
                    return;
                } else {
                    backToast.show();
                }
                backPressedTime =  System.currentTimeMillis();
            }

        });
    }*/


    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        /*final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        final String[] users = {"Select","GST","Tax","Accouting","ITR","DSC","MSME","Company Registration","Money Transfer","Travel","PAN","Loan","Insurance","Recharge","Bill Payment","Other"};
        final Spinner spin = root.findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        View choiceButton= root.findViewById(R.id.button8);
        choiceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userChoice(spin,root);
            }
        });

        //spin.setOnItemSelectedListener(this.getActivity());

        return root;
    }

    public void userChoice(Spinner view,View root){
        String userChoiceValue = view.getSelectedItem().toString();
        //Toast.makeText(this.getActivity(),userChoiceValue,Toast.LENGTH_SHORT).show();
        if(userChoiceValue=="Select"){
            Toast.makeText(this.getActivity(),"Please select valid query" ,Toast.LENGTH_SHORT).show();
        }else{
            InnerMainScreen activity = (InnerMainScreen) getActivity();
            final String myDataFromActivity = activity.getGskDataGlob();
            final String[] userGskDataArray = myDataFromActivity.split("<!>" );
            final String userGskId =userGskDataArray[0];
            final String userGskPhone = userGskDataArray[1];
            final String userGskAddress = userGskDataArray[2];

            final LinearLayout lm = root.findViewById(R.id.gskDetails);
            lm.setVisibility(View.VISIBLE);
            final TextView tv1 = root.findViewById(R.id.textView27);
            final TextView tv2 = root.findViewById(R.id.textView28);
            final TextView tv3 = root.findViewById(R.id.textView29);
            tv1.setText(userGskId);
            tv2.setText(userGskPhone);
            tv3.setText(userGskAddress);
            activity.setLeadTypeHelper(myDataFromActivity,userChoiceValue);
            //InnerMainScreen.setLeadType(this.getActivity(),myDataFromActivity,userChoiceValue);
        }
    }
}