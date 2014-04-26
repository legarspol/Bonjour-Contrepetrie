package com.uliamar.bonjourcontrepetrie;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class APropos extends Activity {
    private APropos that;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apropos);
        ImageView imageDePol  = (ImageView) findViewById(R.id.pol_img);
        that = this;
        imageDePol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contre.deleteAll(Contre.class);
                Toast.makeText(that, "DDB reinitialised", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.apropos, menu);
        return true;
    }


}
