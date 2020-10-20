package com.example.android_tutorial13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.AndroidException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText edtNumber;
    EditText editTextMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtNumber = findViewById(R.id.edtNumber);
        editTextMessage = findViewById(R.id.edtTextMessage);
    }

    // Call Permission Checked...
    public void callToNumber(View view) {
        if(edtNumber.getText().toString().isEmpty()) {
            edtNumber.requestFocus();
            edtNumber.setError("Please enter mobile number!");
            return;
        }
        if (isCallPermissionAllowed()) {
           call();
        }
    }
    // Permission Checked Whether Granted Or Not ....
    private boolean isCallPermissionAllowed() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission Is Granted");
                return true;
            } else {
                Log.v("TAG", "Permission is Revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else // Permission is automatically granted on sdk<23 upon installation...
        {
            Log.v("TAG", "Permission is Granted");
            return true;
        }
    }
    private void call(){
        String phonenumber = edtNumber.getText().toString();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phonenumber));
        startActivity(intent);
    }

    // Sms Permission Checked .....
    public void sendTextMsg(View view) {
         boolean result=true;
        if(editTextMessage.getText().toString().isEmpty()) {
            editTextMessage.requestFocus();
            editTextMessage.setError("Please enter message!");
            result=false;
        }
        if(edtNumber.getText().toString().isEmpty()) {
            edtNumber.requestFocus();
            edtNumber.setError("Please enter mobile number!");
            result=false;
        }
            if(!result)
                return;
            if(isSMSPermissionAllowed())
            {
             sms();
            }
    }

    private void sms(){
        String phonenumber = edtNumber.getText().toString();
        String smsText = editTextMessage.getText().toString();
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phonenumber, null, smsText, null, null);
        Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_SHORT).show();
    }

    private boolean isSMSPermissionAllowed() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission Is Granted");
                return true;
            } else {
                Log.v("TAG", "Permission is Revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 2);
                return false;
            }
        } else // Permission is automatically granted on sdk<23 upon installation...
        {
            Log.v("TAG", "Permission is Granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    sms();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
