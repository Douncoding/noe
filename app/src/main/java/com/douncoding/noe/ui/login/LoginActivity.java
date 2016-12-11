package com.douncoding.noe.ui.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.douncoding.noe.R;
import com.douncoding.noe.model.People;
import com.douncoding.noe.ui.BaseActivity;
import com.douncoding.noe.ui.BaseContract;
import com.douncoding.noe.ui.BasePresenter;
import com.douncoding.noe.ui.home.MainActivity;
import com.douncoding.noe.util.FirebaseUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 회원가입 처리만 수행하기 때문에 구조없이 작성
 */
public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {
    public static void startActivity(Context context) {
        Intent callingIntent = new Intent(context, LoginActivity.class);
        context.startActivity(callingIntent);
    }

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;

    @Override
    public int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void initState(Bundle savedInstanceState) {

    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);
    }

    @Override
    public void initInjector() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public <P extends BasePresenter<BaseContract.View>> P getPresenter() {
        return null;
    }


    @OnClick(R.id.sign_in) public void onSignInClick() {
        (findViewById(R.id.sign_in)).setClickable(false);

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {

            }
        }
    }

    /**
     * 구글 인증 정보를 이용한 Firebase 인증곽 데이터베이스 개체 생성
     * @param acct 구글인증 정보
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        showProgressDialog("회원가입 처리 중...");

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               FirebaseUser user = task.getResult().getUser();
               String username = null;
               Uri picture = null;
               for (UserInfo profile : user.getProviderData()) {
                   username = profile.getDisplayName();
                   picture = profile.getPhotoUrl();
               }

               /**
                * 회원가입 아닌 로그인인 경우에 대한 대비
                * setValue()로 처리하는 경우 로그인시마다 소유 정보에 대한 모든데이터를 손실한다. 좀더 아름다운 방식은
                * 처음부터 로그인과 회원가입을 구분할 수 있으면 좋겠는데.. 기존의 다른앱들은 어떤방식으로 처리하는지에 대한
                * 정보가 전무하다. 추후 프로젝트에서는 이를 반영할 수 있도록 해야 한다.
                */
               DatabaseReference peopleRef = FirebaseUtil.getPeopleRef();
               Map<String, Object> updateUserData = new HashMap<>();
               updateUserData.put("picture", picture);
               updateUserData.put("username", username);
               peopleRef.child(user.getUid()).updateChildren(updateUserData, new DatabaseReference.CompletionListener() {
                   @Override
                   public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                       dismissProgressDialog();
                       if (task.isSuccessful()) {
                           MainActivity.startActivity(LoginActivity.this);
                           finish();
                       } else {
                           FirebaseCrash.report(task.getException());
                       }
                   }
               });
           } else {
               dismissProgressDialog();
               FirebaseCrash.report(task.getException());
           }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
