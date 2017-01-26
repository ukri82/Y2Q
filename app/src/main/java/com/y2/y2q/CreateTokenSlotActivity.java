package com.y2.y2q;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.y2.y2q.misc.PermissionChecker;
import com.y2.y2q.model.QueueDetails;

public class CreateTokenSlotActivity extends AppCompatActivity implements TaskGetQueueDetails.QueueDetailsListener
{

    QueueDetails mQueueDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_token_slot);

        final Activity activity = this;
        findViewById(R.id.scan_qr_code).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(new PermissionChecker(activity).checkPermission(Manifest.permission.CAMERA))
                {
                    initiateQR();
                }

            }
        });

        initializeQueueNumberEdit();

    }

    private void initializeQueueNumberEdit()
    {
        final TaskGetQueueDetails.QueueDetailsListener listener = this;

        EditText editText = (EditText)findViewById(R.id.queue_number);
        editText.addTextChangedListener(new TextWatcher()
        {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count)
            {


            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s)
            {
                String queueId = s.toString();
                if(!queueId.isEmpty() )
                {
                    new TaskGetQueueDetails(listener, queueId).execute();
                }
            }
        });
    }

    private void initiateQR()
    {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case PermissionChecker.RequestCode:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    initiateQR();
                }
                else
                {
                    Toast.makeText(this, "Permission denied to access the camera. Please enter the Queue code above and subscribe", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null)
        {
            if(result.getContents() == null)
            {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
            else
            {
                Log.d("MainActivity", "Scanned");
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                String scannedCode = result.getContents();
                int index = scannedCode.indexOf("queueid=");
                if(index != -1)
                {
                    scannedCode = scannedCode.substring(index);
                }

                new TaskGetQueueDetails(this, scannedCode).execute();
            }
        }
        else
        {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onQueueDetails(QueueDetails queueDetails)
    {
        if(queueDetails != null)
        {
            mQueueDetails = queueDetails;

            TextView qView = (TextView) findViewById(R.id.queue_name);
            qView.setText(mQueueDetails.mName);
        }
    }
}
