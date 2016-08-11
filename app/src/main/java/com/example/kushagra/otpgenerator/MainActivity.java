package com.example.kushagra.otpgenerator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

     private String key = "Kushagra";
     private int mod = 100000;
     private TextView otp;
     private String hashed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        otp = (TextView)findViewById(R.id.OTP);
        createOTP();
    }

    private void createOTP()
    {

        Timer otpTimer = new Timer();
        otpTimer.scheduleAtFixedRate(
                new TimerTask() {
                    public void run() {
                        long unixTime = System.currentTimeMillis() / 60000L;
                        hashed = createHash(Long.toString(unixTime));
                        otp.post(new Runnable() {
                            @Override
                            public void run() {
                                otp.setText(hashed);
                            }
                        });
                    }
                }, 0, 1000);
    }

    private String createHash(String dataToHash)
    {
        String generatedPassword = null;
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            byte[] bytes = md.digest(dataToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(byte b:bytes)
            {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return Integer.toString(convertToDec(generatedPassword));
    }

    private long pow(int a, int b)
    {
        if (b==0)
            return 1;
        if(b==1)
            return a%mod;
        long temp = pow(a,b/2);
        temp *= temp;
        temp %= mod;
        return (temp*pow(a,b%2))%mod;


    }

    private int convertToDec(String hex)
    {
        int dec = 0;
        for(int i=hex.length()-1,j=0;i>=0;i--,j++)
        {
            char digit_ch = hex.charAt(i);
            int digit_int;
            if (digit_ch>=48 && digit_ch<=57)
                digit_int = digit_ch - 48;
            else
                digit_int = digit_ch - 97;
            dec = dec + digit_int*((int)pow(16,j));
            dec %= mod;
        }
        if (dec<=9999)
        {
            while(dec<=9999)
                dec*=10;
        }
        return dec;
    }

}
