package com.example.adminlba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

class Get{
    private EditText ItemNum;
    protected String getNum(){
        ItemNum.findViewById(R.id.Itemnum);
        String num = ItemNum.getText().toString();
        return  num;
    }
}

public class MainActivity extends AppCompatActivity {

    // creating variables for
    // EditText and buttons.
    private EditText ItemName,ItemNum,ItemPrice;
    private Button sendDatabtn,viewButton;

    private String referenceName;

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    // creating a variable for
    // our object class
    AddMenu addMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing our edittext and button
        ItemName = findViewById(R.id.Itemname);
        ItemNum = findViewById(R.id.Itemnum);
        ItemPrice = findViewById(R.id.Itemprice);

        viewButton=findViewById(R.id.viewData);

        Intent intent = getIntent();
        referenceName = intent.getStringExtra("reference_name");
        Toast.makeText(MainActivity.this, "ref"+referenceName, Toast.LENGTH_SHORT).show();

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String referenceName = coursesLV2.getItemAtPosition(position).toString();
                Intent intent = new Intent(MainActivity.this, CheckList.class);
             //   String referenceName = intent.getStringExtra("reference_name");
                //Toast.makeText(MainActivity.this, "ref "+referenceName, Toast.LENGTH_SHORT).show();
                intent.putExtra("reference_name", referenceName.trim());
                startActivity(intent);

            }
        });
        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();



        // Get the reference name from the intent extras

        // below line is used to get reference for our database.
//        databaseReference = firebaseDatabase.getReference("AddMenu");
        if(referenceName.equals("Eta"))
            referenceName="AddBooks";
        if(referenceName.equals("FoodCourt"))
            referenceName="AddMenu";
        databaseReference = firebaseDatabase.getReference(""+referenceName);
        // initializing our object
        // class variable.
        addMenu = new AddMenu();


        sendDatabtn = findViewById(R.id.idBtnSendData);


        // adding on click listener for our button.
        sendDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting text from our edittext fields.
                String name = ItemName.getText().toString();
                String num = ItemNum.getText().toString();
                String price = ItemPrice.getText().toString();

                // below line is for checking weather the
                // edittext fields are empty or not.
                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(num) && TextUtils.isEmpty(price)) {
                    // if the text fields are empty
                    // then show the below message.
                    Toast.makeText(MainActivity.this, "Please add some data.", Toast.LENGTH_SHORT).show();
                } else {
                    // else call the method to add
                    // data to our database.
                    addDatatoFirebase(name,num, price);
                }

            }
        });
    }

    private void addDatatoFirebase(String name, String phone, String price) {
        // below 3 lines of code is used to set
        // data in our object class.
        addMenu.setItemName(name);
        addMenu.setItemNum(phone);
        addMenu.setItemPrice(price);

        // we are use add value event listener method
        // which is called with database reference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                databaseReference.child(addMenu.getItemNum()).setValue(addMenu);

                // after adding this data we are showing toast message.
                Toast.makeText(MainActivity.this, "data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(MainActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
