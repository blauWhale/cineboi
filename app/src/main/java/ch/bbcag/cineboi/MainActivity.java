package ch.bbcag.cineboi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import ch.bbcag.cineboi.databinding.ActivityMainBinding;
import ch.bbcag.cineboi.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        getSupportActionBar().hide();
        setTitle("Cineboi");
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baselinel_fire_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_star_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_favorite_24);
    }
}