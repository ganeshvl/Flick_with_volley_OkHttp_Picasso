package mitul.flickster;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import mitul.flickster.db.PartyDataSource;
import mitul.flickster.db.PartyHelper;
import mitul.flickster.model.ContactList;
import mitul.flickster.model.Flick;
import mitul.flickster.model.Invitation;
import mitul.flickster.model.MovieParty;

public class PartyActivity  extends Activity implements AdapterView.OnItemSelectedListener {
    private TextView movie_title;
    private EditText date;
    private EditText time;
    private EditText party_title;
    private EditText location;
    private Flick movieObject;
    private MovieParty movie_party;
    private Button contacts;
    final Calendar c = Calendar.getInstance();
    private static final int CONTACT_PICKER_RESULT = 1001;
    public final int PICK_CONTACTS = 100;
    private TextView people;
    private Button deleteButton;
    private Button invitationButton;
    public static final String TAG = PartyActivity.class.getSimpleName();
    private PartyDataSource mPartyDataSource;
    private HashMap<String,MovieParty> party_list ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);
        Firebase.setAndroidContext(this);
        movieObject = (Flick) getIntent().getParcelableExtra(MovieDetailActivity.MOVIE_PARTY);
        movie_party = new MovieParty(movieObject);
        mPartyDataSource = new PartyDataSource(getApplicationContext());
        //movie_party.setCurrent_rating();
        initialize();
        //rating_see.setText(String.valueOf(movie_party.getCurrent_rating()));
        movie_title.setText((CharSequence) movie_party.getMovieName());
        setCurrentDateOnView();
        setCurrentTimeOnView();
        addContacts();
        addListenerToButton(contacts);
        addListenerToDeleteButton();
        addListenerToInvButton();
        HashMap<String,MovieParty> archive = pickAllmovies();
        if(archive.containsKey(movie_party.getMovieName())){
            Log.v(TAG,"==============Matching Movie======");
            Log.v(TAG, archive.get(movie_party.getMovieName()).getNamesList());
            Log.v(TAG, archive.get(movie_party.getMovieName()).getEmailList());
            if(archive.get(movie_party.getMovieName()) != null){
                updateUI(archive.get(movie_party.getMovieName()));
            }
        }
    }

    private void updateUI(MovieParty movieParty) {

        date.setText(movieParty.getDate());
        time.setText(movieParty.getTime());
        party_title.setText(movie_party.getParty_title());
        people.setText(movieParty.getEmailList());
        location.setText(movieParty.getLocation());

    }

    private HashMap<String,MovieParty> pickAllmovies() {
        party_list = new HashMap<>();
        PartyDataSource dataSource = new PartyDataSource(getApplicationContext());
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Cursor cursor = dataSource.selectAllParties();
        MovieParty movie_party;
        //cursor.moveToFirst();
        cursor.moveToLast();
        while(!cursor.isBeforeFirst()){
            //while(!cursor.isAfterLast()){
            Flick flick = new Flick();
            movie_party = new MovieParty();
            //int i = cursor.getColumnIndex(MovieHelper.COLUMN_TITLE);
            //mTitles.add(cursor.getString(i));
            Log.v(TAG, cursor.getString(cursor.getColumnIndex(PartyHelper.COLUMN_NAME)));
            movie_party.setMovieName(cursor.getString(cursor.getColumnIndex(PartyHelper.COLUMN_TITLE)));
            movie_party.setParty_title(cursor.getString(cursor.getColumnIndex(PartyHelper.COLUMN_PARTYTITLE)));
            movie_party.setContactEmail(cursor.getString(cursor.getColumnIndex(PartyHelper.COLUMN_ADDRESS)));
            movie_party.setNames(cursor.getString(cursor.getColumnIndex(PartyHelper.COLUMN_NAME)));
            movie_party.setLocation(cursor.getString(cursor.getColumnIndex(PartyHelper.COLUMN_VENUE)));
            movie_party.setDate(cursor.getString(cursor.getColumnIndex(PartyHelper.COLUMN_DATE)));
            movie_party.setTime(cursor.getString(cursor.getColumnIndex(PartyHelper.COLUMN_TIME)));
            party_list.put(movie_party.getMovie_name(),movie_party);

            Log.v(TAG,cursor.getString(cursor.getColumnIndex(PartyHelper.COLUMN_ADDRESS)));
            Log.v(TAG, cursor.getString(cursor.getColumnIndex(PartyHelper.COLUMN_DATE)));
            Log.v(TAG, cursor.getString(cursor.getColumnIndex(PartyHelper.COLUMN_TIME)));
            Log.v(TAG, cursor.getString(cursor.getColumnIndex(PartyHelper.COLUMN_VENUE)));
            Log.v(TAG, cursor.getString(cursor.getColumnIndex(PartyHelper.COLUMN_TITLE)));
            Log.v(TAG, cursor.getString(cursor.getColumnIndex(PartyHelper.COLUMN_ID)));
            Log.v(TAG, cursor.getString(cursor.getColumnIndex(PartyHelper.COLUMN_IMDB)));
            Log.v(TAG, cursor.getString(cursor.getColumnIndex(PartyHelper.COLUMN_PARTYTITLE)));

            //cursor.moveToNext();
            cursor.moveToPrevious();
        }
        dataSource.close();
        return party_list;
    }

    private void addListenerToDeleteButton() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movie_party.delete();
                ArrayList<String> contact_list = movie_party.getContactsEmail();
                Iterator<String> itr = contact_list.iterator();
                String con = "";
                while(itr.hasNext()){
                    con = con + itr.next()+ " ";
                }
                people.setText(con);
            }
        };
        deleteButton.setOnClickListener(listener);
    }

    private void addContacts() {


        ContactList mitul = new ContactList("Mitul Manish",
                "0451754624",
                "9871203289",
                "88889797",
                "mitul.manish@gmail.com",
                "pethomestay",
                "Developer");
        ContactList bijin = new ContactList("Bijin Abraham",
                "0451754628",
                "98712098289",
                "8888988797",
                "bijin.abraham@gmail.com",
                "Zendesk",
                "Business Analyst");
        ContactList Shal = new ContactList("Shalendra Sajwan",
                "0451754628",
                "98712098289",
                "8888988797",
                "shal.sajwan@gmail.com",
                "ReInteractive",
                "");
        ContactList Pal = new ContactList("Paliniappan Subramaniam",
                "0451754628",
                "98712098289",
                "8888988797",
                "pal.sub@gmail.com",
                "Podio",
                "");
        ContactList Zing = new ContactList("Zingam Xighuan",
                "0451754628",
                "98712098289",
                "8888988797",
                "zinglui@gmail.com",
                "Maxrer",
                "");
        ContactList Mr_M = new ContactList("Mitul Manish",
                "0451754624",
                "9871203289",
                "88889797",
                "mitul.manish@gmail.com",
                "pethomestay",
                "Developer");
        ContactList Mr_B = new ContactList("Barty Murgasen",
                "0451754628",
                "98712098289",
                "8888988797",
                "bijin.abraham@gmail.com",
                "Zendesk",
                "Business Analyst");
        ContactList Mr_S = new ContactList("Simonn Cowell",
                "0451754628",
                "98712098289",
                "8888988797",
                "shal.sajwan@gmail.com",
                "ReInteractive",
                "");
        ContactList Mr_P = new ContactList("Pedro Gonzales",
                "0451754628",
                "98712098289",
                "8888988797",
                "pal.sub@gmail.com",
                "Podio",
                "Coding Ninja");
        ContactList Mr_Z = new ContactList("Zero 51",
                "04517546728",
                "987120898289",
                "888894q8797",
                "zero@gmail.com",
                "Mayer",
                "Growth hacker");
        ContactList Keith = new ContactList("Keith Z",
                "678906728",
                "045190008289",
                "888894q87979",
                "keith@gmail.com",
                "XYTRT",
                "Scala");
        ContactList Chan = new ContactList("Chan",
                "6789067280",
                "0451900082089",
                "888894q879709",
                "chan@gmail.com",
                "XYT",
                "ScalaY");
        ContactList Larry = new ContactList("Larry",
                "8989",
                "60808",
                "38987",
                "larry@gmail.com",
                "TYTT",
                "UUYUYU"
                );
        ContactList Jimmy = new ContactList("Jimmy",
                "87878090787",
                "8989898-90-99",
                "08090890880808",
                "jimmy@gmail.com",
                "XXXXX",
                "YYYYYYYYY"
        );
        ContactList Sumit = new ContactList("Sumit",
                "11118787",
                "777778989",
                "080980000808",
                "sumit.sonal@gmail.com",
                "Myntra",
                "Marketing"
        );

        ArrayList<ContactList> my_contacts = new ArrayList<ContactList>();

        my_contacts.add(mitul);
        my_contacts.add(bijin);
        my_contacts.add(Shal);
        my_contacts.add(Pal);
        my_contacts.add(Zing);
        my_contacts.add(Mr_M);
        my_contacts.add(Mr_B);
        my_contacts.add(Mr_S);
        my_contacts.add(Mr_P);
        my_contacts.add(Mr_Z);
        my_contacts.add(Keith);
        my_contacts.add(Chan);
        my_contacts.add(Larry);
        my_contacts.add(Jimmy);
        my_contacts.add(Sumit);



        Iterator<ContactList> itr = my_contacts.iterator();
        while (itr.hasNext()) {
            ContactList element = itr.next();
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build());

            //------------------------------------------------------ Names
            if (element.getDisplayName() != null) {
                ops.add(ContentProviderOperation.newInsert(
                        ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(
                                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                element.getDisplayName()).build());

            }


            //------------------------------------------------------ Mobile Number

            if (element.getMobileNumber()!= null) {
                ops.add(ContentProviderOperation.
                        newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, element.getEmailID())
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        .build());
            }

            //------------------------------------------------------ Home Numbers
/*
            if (element.getHomeNumber()!= null) {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,element.getHomeNumber())
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                        .build());
            }

            //------------------------------------------------------ Work Numbers
            if (element.getWorkNumber()!= null) {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, element.getWorkNumber())
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                        .build());
            }
*/
            //------------------------------------------------------ Email
            /*
            if (element.getEmailID() != null) {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Email.DATA, element.getEmailID())
                        .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                        .build());
            }

            //------------------------------------------------------ Organization
            if (!element.getCompany().equals("") && !element.getJobTitle().equals("")) {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, element.getCompany())
                        .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                        .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, element.getJobTitle())
                        .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                        .build());
            }
            */
            // Asking the Contact provider to create a new contact
            try {
                getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(PartyActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            ops.clear();

        }
    }
    private void initialize() {
        date = (EditText) findViewById(R.id.date);
        time = (EditText) findViewById(R.id.time);
        party_title = (EditText) findViewById(R.id.party_title);
        //rating_see = (TextView)findViewById(R.id.rate);
        contacts = (Button) findViewById(R.id.contacts);
        movie_title = (TextView) findViewById(R.id.PartyMovie);
        people = (TextView) findViewById(R.id.people);
        deleteButton = (Button) findViewById(R.id.delete);
        location = (EditText) findViewById(R.id.venue);
        invitationButton = (Button) findViewById(R.id.buttonInvitation);
    }
    public void addListenerToButton(Button contacts){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(i, 101);

            }
        };
        contacts.setOnClickListener(listener);
    }
    public void addListenerToInvButton(){

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movie_party.setDate(date.getText().toString());
                movie_party.setParty_title(party_title.getText().toString());
                movie_party.setTime(time.getText().toString());
                movie_party.setLocation(location.getText().toString());
                mPartyDataSource.insertParty(movie_party);
                //mPartyDataSource.selectAllParties();
                Invitation invitation = new Invitation(movie_party.getParty_title(),movie_party.getMovieID(),
                        movie_party.getMovieName(),movie_party.getNamesList(),movie_party.getEmailList(),
                        movie_party.getDate(),movie_party.getTime(),movie_party.getLocation());

                //Firebase myFirebaseRef = new Firebase("https://resplendent-heat-626.firebaseio.com/");
                Firebase ref = new Firebase("https://resplendent-heat-626.firebaseio.com/android/saving-data/flickster");
                Firebase alanRef = ref.child("invitations").child(invitation.getParty_title());
                alanRef.setValue(invitation);
                ref.child("invitations").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
                    }

                    @Override
                    public void onCancelled(FirebaseError error) {
                    }

                });

            }
        };
        invitationButton.setOnClickListener(listener);
    }

    public void setCurrentDateOnView() {
        String dateFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat( dateFormat, Locale.US);
        date.setText(sdf.format(c.getTime()));
        movie_party.setDate(sdf.format(c.getTime()));
    }

    public void setCurrentTimeOnView() {
        String timeFormat = "hh:mm a";
        SimpleDateFormat stf = new SimpleDateFormat( timeFormat, Locale.US );
        time.setText(stf.format(c.getTime()));
        movie_party.setTime(stf.format(c.getTime()));
    }

    DatePickerDialog.OnDateSetListener my_date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet( DatePicker view, int year, int monthOfYear, int dayOfMonth ) {
            c.set( Calendar.YEAR, year );
            c.set( Calendar.MONTH, monthOfYear );
            c.set( Calendar.DAY_OF_MONTH, dayOfMonth );
            setCurrentDateOnView();
        }
    };
    public void dateOnClick(View view){
        new DatePickerDialog( PartyActivity.this, my_date,
                c.get( Calendar.YEAR ), c.get( Calendar.MONTH ), c.get( Calendar.DAY_OF_MONTH ) ).show();
    }
    TimePickerDialog.OnTimeSetListener my_time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            c.set(Calendar.HOUR_OF_DAY,hourOfDay);
            c.set(Calendar.MINUTE,minute);
            setCurrentTimeOnView();
        }
    };
    public void timeOnClick(View view){
        new TimePickerDialog(PartyActivity.this,my_time,c.get(Calendar.HOUR),c.get(Calendar.MINUTE),false).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onResume(){
        super.onResume();
        try {
            mPartyDataSource.open();
            Log.v(TAG, "---------- Database succesfully created -------------");
            //loadMovieData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onPause(){
        super.onPause();

        mPartyDataSource.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int NameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            int emailIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            Log.v(TAG,cursor.getString(NameIndex)+"----name------");
            Log.v(TAG,cursor.getString(emailIndex)+"----Email------");
            movie_party.setContactEmail(cursor.getString(emailIndex));
            movie_party.setNames(cursor.getString(NameIndex));

            Log.v(TAG, cursor.getString(emailIndex));
            Log.v(TAG,cursor.getString(NameIndex));
            ArrayList<String> contact_list = movie_party.getContactsEmail();
            ArrayList<String> names = movie_party.getContactsEmail();
            //Iterator<String> itr = contact_list.iterator();
            Iterator<String> itr = names.iterator();
            String con = "";
            while (itr.hasNext()) {
                con = con + itr.next() + " ";
            }
            people.setText(con);
        }

    }



}
