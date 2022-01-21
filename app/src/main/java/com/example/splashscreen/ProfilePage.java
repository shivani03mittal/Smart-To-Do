package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;

public class ProfilePage extends AppCompatActivity {
    ImageView imageView1,imageView2;
    TextView textView1,textView2;
    FloatingActionButton floatingActionButton;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String mail;
    Bitmap bitmap;
    String newname;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        imageView1= findViewById(R.id.imageview_profile);
        imageView2= findViewById(R.id.iv_edit);
        textView1=findViewById(R.id.tvname2);
        textView2=findViewById(R.id.email2);
        floatingActionButton=findViewById(R.id.floatingpicchanger);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
        mail= firebaseUser.getUid();
        email= firebaseUser.getEmail();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder changename= new AlertDialog.Builder( ProfilePage.this);
                changename.setTitle("Display Name");

                final EditText input= new EditText(ProfilePage.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                changename.setView(input);

                changename.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        newname = input.getText().toString();
                        databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("users").child(mail).child("UserName").setValue(newname);
                        Toast.makeText(ProfilePage.this, "User"+newname, Toast.LENGTH_SHORT).show();
                    }
                });

                changename.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                changename.show();

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(mail).child("UserName");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("Username profile", snapshot.getValue(String.class));
                String username= snapshot.getValue(String.class).toString();
                Log.d("useriner check",username);
                textView1.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        textView2.setText(email);
        if(firebaseUser != null) {
            if (firebaseUser.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(firebaseUser.getPhotoUrl())
                        .into(imageView1);
            }
            else {
                imageView1.setImageResource(R.drawable.personprofile);
            }
        }
    }
    //-----------------------------------------------------------------------
    private void showDialog()
    {
        final Dialog dialog= new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.profilecam_bottomsheet_layout);

        ImageView remove= dialog.findViewById(R.id.iv_delete);
        ImageView gallery= dialog.findViewById(R.id.iv_gallery);
        ImageView camera_imageview= dialog.findViewById(R.id.iv_camera);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePic();
                dialog.dismiss();
//                Toast.makeText(ProfilePage.this,"Delete is Clicked",Toast.LENGTH_SHORT).show();
            }
        });

//        -------------------------------------------------
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opengallery();
//                imageView1.setImageResource(R.drawable.personprofile);
                dialog.dismiss();
//                Toast.makeText(ProfilePage.this,"Gallery is Clicked",Toast.LENGTH_SHORT).show();
            }
        });

//        ------------------------------------------------------

        camera_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMyCamera();
                dialog.dismiss();
//                Toast.makeText(ProfilePage.this,"camera is Clicked",Toast.LENGTH_SHORT).show();

            }
        });


//        --------------------------------------------------------

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogStyle;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    //    -------------------------------------------------------------
    void opengallery()
    {
        Intent opengallery= new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(opengallery,1000);

    }
    //------------------------------------------------------------------------
    void deletePic()
    {
        AlertDialog.Builder builder= new AlertDialog.Builder(ProfilePage.this);
        builder.setMessage("Remove profile photo?")
                .setPositiveButton("REMOVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        StorageReference storageReference= FirebaseStorage.getInstance().getReference()
                                .child("profileImages")
                                .child(uid+".jpeg");
                        storageReference.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ProfilePage.this, "Profile Picture Removed", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        Uri uri=null;
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(uri)
                                .build();

                        user.updateProfile(request)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ProfilePage.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfilePage.this, "Profile image failed...", Toast.LENGTH_SHORT).show();
                            }
                        });

//                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                firebaseUser= firebaseAuth.getCurrentUser();
                        imageView1.setImageResource(R.drawable.personprofile);
                    }
                }).setNegativeButton("CANCEL",null);
        AlertDialog alertDialog= builder.create();
        alertDialog.show();



    }

    //  ----------------------------------------------------------------------
    void startMyCamera(){
        Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 999)
        {   switch (resultCode)
        { case RESULT_OK:
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView1.setImageBitmap(bitmap);
//            savetofolder();
            handleUpload(bitmap);
        }
        }

        if(requestCode == 1000)
        {
            switch (resultCode){
                case RESULT_OK:
                    Uri imageUri = data.getData();
                    imageView1.setImageURI(imageUri);
                    handleUploadfromGal(imageUri);
            }
        }
    }
    private void handleUploadfromGal(Uri imageUri)
    {
//        ulpad image to firebase from galler
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference filereff= FirebaseStorage.getInstance().getReference()
                .child("profileImages")
                .child(uid+".jpeg");

        filereff.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ProfilePage.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                setUserProfileURL(imageUri );

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfilePage.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void handleUpload(Bitmap bitmap)
    {
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference storageReference= FirebaseStorage.getInstance().getReference()
                .child("profileImages")
                .child(uid+".jpeg");

        storageReference.putBytes(baos.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getDownloadURL(storageReference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("onFailure", e.getMessage());
                    }
                });
    }

    public void getDownloadURL(StorageReference storageReference)
    {
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
//                Log.d("onSucess: ",""+uri);
                setUserProfileURL(uri);
            }
        });
    }

    private void setUserProfileURL(Uri uri )
    {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request= new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ProfilePage.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfilePage.this, "Profile image failed...", Toast.LENGTH_SHORT).show();
            }
        });
    }
    void  savetofolder()
    {
        File path = Environment.getExternalStorageDirectory();
        File folder= new File(path.getAbsolutePath()+"/To_do/profile");
        folder.mkdir();

        File file = new File(folder, System.currentTimeMillis()+".jpg");
        OutputStream outputStream= null;
        try{
            outputStream = new FileOutputStream(file);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);

        try{
            outputStream.flush();
        }
        catch( IOException e){
            e.printStackTrace();
        }

        try{
            outputStream.close();
        }
        catch( IOException e){
            e.printStackTrace();
        }
    }

//    ==================


}