package com.tournament.helper.create.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.tournament.helper.R;

/**
 * Created by destevancardozoj on 3/9/18.
 */

public class AddTeamDialog extends DialogFragment {

  private AddTeamListener mListener;
  private EditText mAddTeamTitle;
  private View mAddTeamBttn;

  @NonNull
  public static AddTeamDialog newInstance() {
    AddTeamDialog dialog = new AddTeamDialog();
    return dialog;
  }

  public void launchAddTeamDialog(@NonNull FragmentManager fragmentManager,
                                         @NonNull AddTeamListener listener) {
    mListener = listener;
    show(fragmentManager, AddTeamDialog.class.getSimpleName());
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.add_team_dial, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setupViews(view);
  }

  private void setupViews(View view) {
    mAddTeamTitle = view.findViewById(R.id.add_team_title_inpt);
    mAddTeamBttn = view.findViewById(R.id.add_team_bttn);

    mAddTeamBttn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String title = mAddTeamTitle.getText().toString();
        if(TextUtils.isEmpty(title)){
          Toast.makeText(getContext(), "Set Some title to your Team", Toast.LENGTH_SHORT).show();
          return;
        }
        mListener.onAddTeam(title);
        dismissAllowingStateLoss();
      }
    });
  }

  public interface AddTeamListener{
    void onAddTeam(String title);
  }
}
