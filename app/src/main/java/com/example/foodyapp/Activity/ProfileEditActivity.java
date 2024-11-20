package com.example.foodyapp.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.foodyapp.R;
import com.example.foodyapp.databinding.ActivityProfileBinding;
import com.example.foodyapp.databinding.ActivityProfileEditBinding;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

public class ProfileEditActivity extends BaseActivity {
    ActivityProfileEditBinding binding;
    private static final String TAG = "PROFILE_EDIT_TAG";
    private Uri imageUri=null;
    public String curPasswordValid, name="",curPasswordEdit="",newPassword="",profileImage="";
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        setVariable();
        loadUserInfo();
    }
    private void loadUserInfo() {

        DatabaseReference ref = database.getReference("Users");
        ref.child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String email = ""+snapshot.child("email").getValue();
                String name = ""+snapshot.child("name").getValue();
                curPasswordValid = ""+snapshot.child("password").getValue();
                profileImage = ""+snapshot.child("profileImage").getValue();
                String uid = ""+snapshot.child("uid").getValue();

                binding.emailhText.setText(email);
                binding.nameProfileText.setText(name);
                binding.fullNameEdit.setText(name);



                Glide.with(ProfileEditActivity.this)
                        .load(profileImage)
                        .placeholder(R.drawable.avatar)
                        .into(binding.profileImg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setVariable() {
        binding.btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileEditActivity.this,ProfileActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });
        binding.profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageAttachMenu();
            }
        });
        binding.profileUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private void validateData() {
        name = binding.fullNameEdit.getText().toString().trim();
        curPasswordEdit = binding.curPassEdit.getText().toString().trim();
        newPassword = binding.newPassEdit.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this,"Enter name...",Toast.LENGTH_SHORT).show();
        } else if (curPasswordEdit.isEmpty()) {
            Toast.makeText(this,"Enter current password...",Toast.LENGTH_SHORT).show();
        }else if (!curPasswordEdit.equals(curPasswordValid)) {
            Toast.makeText(this,"Current password is not valid",Toast.LENGTH_SHORT).show();
        } else {
            if (imageUri == null){
                updateProfile("");
            }else {
                uploadImage();
            }
        }

    }

    private void updateProfile( String imageUrl){
        progressDialog.setMessage("Updating user profile...");
        progressDialog.show();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name","" + name);

        if (imageUrl != null ){
            hashMap.put("profileImage",""+ imageUrl);
        }else if (newPassword!=null) {
            hashMap.put("password","" + newPassword);
        }else {
            if (!Objects.equals(profileImage, "")){
                hashMap.put("profileImage",""+ profileImage);
            }
        }

        DatabaseReference databaseReference = database.getReference("Users");
        databaseReference.child(auth.getUid())
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        binding.fullNameEdit.setText(name);
                        binding.curPassEdit.setText("");
                        binding.newPassEdit.setText("");
                        progressDialog.dismiss();
                        Toast.makeText(ProfileEditActivity.this,"Profile updated",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileEditActivity.this,"Failed to updated database due to"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void uploadImage() {
        progressDialog.setMessage("Updating profile image");
        progressDialog.show();

        String filePathAndName = "Profile Image" + auth.getUid();

        StorageReference ref = FirebaseStorage.getInstance().getReference(filePathAndName);
        ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG,"onSuccess: Profile image uploaded");
                        Log.d(TAG,"on Success: getting url of uploaded image");
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        String uploadedImageUrl = ""+uriTask.getResult();

                        Log.d(TAG,"OnSuccess: Upload image URL" +uploadedImageUrl);

                        updateProfile(uploadedImageUrl);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"onFailed: Failed to upload due to"+e.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(ProfileEditActivity.this, "Failed to upload image due to"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showImageAttachMenu() {
        PopupMenu popupMenu = new PopupMenu(this,binding.profileImg);
        popupMenu.getMenu().add(Menu.NONE,0,0,"Camera");
        popupMenu.getMenu().add(Menu.NONE,1,1,"Gallery");

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int which = menuItem.getItemId();
                if (which==0){
                    pickImageCamera();
                }else if (which==1) {
                    pickImagegallery();
                }
                return false;
            }
        });

    }

    private void pickImagegallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActitityResultLauncher.launch(intent);
    }

    private void pickImageCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Pick");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Sample Image Description");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        cameraActitityResultLauncher.launch(intent);
    }
    private final ActivityResultLauncher<Intent> cameraActitityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if(o.getResultCode()== Activity.RESULT_OK){
                        Intent data = o.getData();
                        binding.profileImg.setImageURI(imageUri);
                    }
                    else{
                        Toast.makeText(ProfileEditActivity.this,"Cancelled",Toast.LENGTH_SHORT).show();
                    }

                }
            }
    );
    private final ActivityResultLauncher<Intent> galleryActitityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if(o.getResultCode()== Activity.RESULT_OK){
                        Intent data = o.getData();
                        assert data != null;
                        imageUri = data.getData();
                        binding.profileImg.setImageURI(imageUri);
                    }
                    else{
                        Toast.makeText(ProfileEditActivity.this,"Cancelled",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
}