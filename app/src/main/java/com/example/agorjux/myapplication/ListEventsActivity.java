package com.example.agorjux.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.agorjux.moveyourbody.R;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

/**
 * Created by arthur on 10/08/15.
 */
public class ListEventsActivity extends Activity {

    boolean searchFinish = false;
    private CardContainer mCardContainer;
    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private SwipeFlingAdapterView flingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_events);

        // TODO : utiliser le gps pour recuperer la localisation et lancer la methode du WS pour chercher les evenements en fonction de la géolocalisation
        // TODO : faire une classe config avec une interface pour configurer la recherche (distance, sports, ...) => static


    }

    /*private void getCardsOld(){
        mCardContainer = (CardContainer) findViewById(R.id.layoutview);
        SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(this);
        final Context context = this.getBaseContext();
        int i;
        for(i = 1; i <= 10; i++){
            CardModel cardModel = new CardModel("Title", "Event", (Drawable)null);
            /*cardModel.setOnClickListener(new CardModel.OnClickListener() {
                @Override
                public void OnClickListener() {
                    Toast.makeText(context, "Event click", Toast.LENGTH_SHORT).show();
                }
            });

            cardModel.setOnCardDimissedListener(new CardModel.OnCardDimissedListener() {
                @Override
                public void onLike() {
                    Toast.makeText(context, "Event dislike", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onDislike() {
                    Toast.makeText(context, "Event like", Toast.LENGTH_SHORT).show();
                }
            });

            adapter.add(cardModel);
        }

        mCardContainer.setAdapter(adapter);
    }*/

    private void getCards(){
        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        al = new ArrayList<String>();
        al.add("php");
        al.add("c");
        al.add("python");
        al.add("java");

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.card_item, R.id.helloText, al);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                al.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
                al.remove(o);
                arrayAdapter.notifyDataSetChanged();
                Toast.makeText(ListEventsActivity.this, "Dislike", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object o) {
                al.remove(o);
                arrayAdapter.notifyDataSetChanged();
                Toast.makeText(ListEventsActivity.this, "Like", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
                if(i == 0){
                    // TODO Ajouter une textview vide qui aura le message quand il n'y a plus d'event
                    ListEventsActivity.this.setContentView(R.layout.activity_search_events);
                }
            }

            @Override
            public void onScroll(float v) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(v < 0 ? -v : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(v > 0 ? v : 0);
            }
        });
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int i, Object o) {

            }
        });
    }

    public void showEvents(View v){
        if(!searchFinish){
            setContentView(R.layout.activity_search_events_cards);
            getCards();
        }else{

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
