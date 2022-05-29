package kavita.myappcompany.recomendation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    int value;
    private Fragment activeFragment;
    private HomeFragment homeFragment = new HomeFragment();
    private DramaFragment dramaFragment = new DramaFragment();
    private ProfileFragment ProfileFragment = new ProfileFragment();
    private MoviesFragment moviesFragment = new MoviesFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_nav_bar);
        bottomNavigation.setVisibility(View.VISIBLE);
        bottomNavigation();
    }
    private void bottomNavigation() {
        getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment, dramaFragment).hide(dramaFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment, moviesFragment).hide(moviesFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment, ProfileFragment).hide(ProfileFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment, homeFragment).hide(homeFragment).commit();

        activeFragment = homeFragment;
        getSupportFragmentManager().beginTransaction().show(activeFragment).commit();

        if(value==2)
            bottomNavigation.setSelectedItemId(R.id.nav_myTeams);
        else if(value==3)
            bottomNavigation.setSelectedItemId(R.id.nav_profile);
        else
            bottomNavigation.setSelectedItemId(R.id.nav_home);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Fragment selectedFragment = homeFragment;
                        switch (item.getItemId()) {
                            case R.id.nav_home:
                                selectedFragment = homeFragment;
                                break;
                            case R.id.nav_myTeams:
                                selectedFragment = dramaFragment;
                                break;
                            case R.id.nav_teams:
                                selectedFragment = moviesFragment;
                                break;
                            case R.id.nav_profile:
                                selectedFragment = ProfileFragment
                                ;
                                break;
                        }
                        if (selectedFragment != activeFragment) {
                            //Bundle bundle = new Bundle();
                            //bundle.putString("idToken", Mainid);
                            //SelectedFragment.setArguments(bundle);
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .show(selectedFragment)
                                    .hide(activeFragment)
                                    .commit();
                            activeFragment = selectedFragment;
                        }
                    }
                });
                return true;
            }
        });

    }
}