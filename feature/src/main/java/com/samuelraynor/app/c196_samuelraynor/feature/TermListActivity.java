package com.samuelraynor.app.c196_samuelraynor.feature;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.samuelraynor.app.c196_samuelraynor.feature.database.StudentData;
import com.samuelraynor.app.c196_samuelraynor.feature.model.Term;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TermListActivity extends AppCompatActivity {

    ListView termsList;

    ArrayList<Term> termInfo = new ArrayList<>();

    TermAdapter termsListAdapter;

    StudentData studentData;

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
                addTerm(view);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Set the ListView content
        termsList = findViewById(R.id.termsListView);

        // get the terms data from the database
        studentData = new StudentData(this);


        termsListAdapter = new TermAdapter(this, termInfo);

        termsList.setAdapter(termsListAdapter);
        termsList.setOnItemClickListener(getItemOnClickListener());

        showTerms();
    }

    private AdapterView.OnItemClickListener getItemOnClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Term selectedTerm = (Term) termsList.getItemAtPosition(position);
                Intent termDetailIntent = new Intent(parent.getContext(), TermDetailActivity.class);
                termDetailIntent.putExtra("ACTION", "INFO");
                termDetailIntent.putExtra("TERM", selectedTerm.getId());
                startActivityForResult(termDetailIntent, 1);
            }
        };
    }

    public void addTerm(View view) {
        Intent addIntent = new Intent(this, TermEditActivity.class);
        addIntent.putExtra("ACTION", "NEW");
        startActivityForResult(addIntent, 2);
    }

    private void showTerms() {
        termsListAdapter.clear();
        termsListAdapter.addAll(studentData.getAllTerms());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 2) {
            //Term term = (Term) data.getSerializableExtra("TERM");
            //studentData.addTerm(term);

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        showTerms();
    }

    public class TermAdapter extends ArrayAdapter<Term> {
        public TermAdapter(Context context, ArrayList<Term> terms) {
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

            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            tvName.setText(term.getTitle());
            tvStart.setText("Start: ".concat(formatter.format(term.getStart())));
            tvEnd.setText("End: ".concat(formatter.format(term.getEnd())));

            return convertView;
        }
    }

}
