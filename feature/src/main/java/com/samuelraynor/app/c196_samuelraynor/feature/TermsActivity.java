package com.samuelraynor.app.c196_samuelraynor.feature;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TermsActivity extends AppCompatActivity {

    ListView termsList;

    ArrayList<Term> termInfo = new ArrayList<Term>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_activity_terms);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Set the ListView content
        termsList = findViewById(R.id.termsListView);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

        Term newTerm1 = null;
        Term newTerm2 = null;
        Term newTerm3 = null;
        try {
            newTerm1 = new Term("Term 1", formatter.parse("1-Jan-2018"), formatter.parse("30-Jun-2018"));
            newTerm2 = new Term("Term 2", formatter.parse("1-Jul-2018"), formatter.parse("31-Dec-2018"));
            newTerm3 = new Term("Term 3", formatter.parse("1-Jan-2019"), formatter.parse("31-Jun-2019"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        termInfo.add(newTerm1);
        termInfo.add(newTerm2);
        termInfo.add(newTerm3);
        termInfo.add(newTerm1);
        termInfo.add(newTerm2);
        termInfo.add(newTerm3);
        termInfo.add(newTerm1);
        termInfo.add(newTerm2);
        termInfo.add(newTerm3);
        termInfo.add(newTerm1);
        termInfo.add(newTerm2);
        termInfo.add(newTerm3);

        TermsAdapter termsListAdapter = new TermsAdapter(this, termInfo);

        termsList.setAdapter(termsListAdapter);

    }

    public class TermsAdapter extends ArrayAdapter<Term> {
        public TermsAdapter(Context context, ArrayList<Term> terms) {
            super(context, 0, terms);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Term term = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_term, parent, false);
            }
            TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
            TextView tvStart = (TextView) convertView.findViewById(R.id.tvStart);
            TextView tvEnd = (TextView) convertView.findViewById(R.id.tvEnd);

            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
            tvName.setText(term.getName());
            tvStart.setText("Start: ".concat(formatter.format(term.getStart())));
            tvEnd.setText("End: ".concat(formatter.format(term.getEnd())));

            // Give each entry a click listener.
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Display edit dialog
                    Snackbar.make(v, "You clicked a list item!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
            return convertView;
        }
    }

}
