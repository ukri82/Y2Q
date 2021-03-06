package com.y2.y2q;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.y2.utils.PermissionChecker;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScanQRFragment extends Fragment
{

    QueueDetailsView mQHandler;

    public ScanQRFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState)
    {
        super.onViewCreated(v, savedInstanceState);

        final Activity activity = this.getActivity();
        activity.findViewById(R.id.queue_qr_button).setOnClickListener(new View.OnClickListener()
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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_qr, container, false);
    }

    private void initiateQR()
    {
        IntentIntegrator integrator = new IntentIntegrator(this.getActivity());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan qr code for queue");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

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
                    Toast.makeText(this.getContext(), "Permission denied to access the camera. Please enter the Queue code above and subscribe", Toast.LENGTH_SHORT).show();
                }
        }

    }

    public void onActivityResult(IntentResult result)
    {
        if(result.getContents() == null)
        {
            if(this.getContext() != null)
            {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this.getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Log.d("MainActivity", "Scanned");

            String scannedCode = result.getContents();
            int index = scannedCode.indexOf("queueid=");
            if(index != -1)
            {
                scannedCode = scannedCode.toLowerCase(Locale.US);

                scannedCode = scannedCode.substring(index + new String("queueid=").length());

                Toast.makeText(this.getContext(), "Scan successfull. Subscribing to queue " + scannedCode + " .... Please wait", Toast.LENGTH_LONG).show();

                if(mQHandler == null)
                {
                    mQHandler = new QueueDetailsView(this.getActivity(), getView().findViewById(R.id.new_queue_card));
                }
                mQHandler.getQueueDetails(scannedCode, true);
            }
            else
            {
                Toast.makeText(this.getContext(), "Scan failed. Maybe wrong QR code. Please try entering the queue details in the other page", Toast.LENGTH_LONG).show();
            }

        }
    }

}
