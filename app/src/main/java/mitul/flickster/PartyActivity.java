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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import mitul.flickster.model.ContactList;
import mitul.flickster.model.Flick;
import mitul.flickster.model.MovieParty;

public class PartyActivity  extends Activity implements AdapterView.OnItemSelectedListener {
    private TextView movie_title;
    private EditText date;
    private EditText time;
    private TextView rating_see;
    private Flick movieObject;
    private MovieParty movie_party;
    private Button contacts;
    final Calendar c = Calendar.getInstance();
    private static final int CONTACT_PICKER_RESULT = 1001;
    public final int PICK_CONTACTS = 100;
    private TextView people;
    private Button deleteButton;
    public static final String TAG = PartyActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);
        movieObject = (Flick) getIntent().getParcelableExtra(MovieDetailActivity.MOVIE_PARTY);
        movie_party = new MovieParty(movieObject);
        movie_party.setCurrent_rating();
        initialize();
        rating_see.setText(String.valueOf(movie_party.getCurrent_rating()));
        movie_title.setText((CharSequence)movie_party.getMovieName());
        setCurrentDateOnView();
        setCurrentTimeOnView();
        addContacts();
        addListenerToButton(contacts);
        addListenerToDeleteButton();

    }
    private void addListenerToDeleteButton() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movie_party.delete();
                ArrayList<String> contact_list = movie_party.getContacts();
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


        // ArrayList<ContentProviderOperation> ops = new ArrayList<>();

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
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,element.getMobileNumber())
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        .build());
            }

            //------------------------------------------------------ Home Numbers
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

            //------------------------------------------------------ Email
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
        rating_see = (TextView)findViewById(R.id.rate);
        contacts = (Button) findViewById(R.id.contacts);
        movie_title = (TextView) findViewById(R.id.PartyMovie);
        people = (TextView) findViewById(R.id.people);
        deleteButton = (Button) findViewById(R.id.delete);
    }
    public void addListenerToButton(Button contacts){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(i,101);
            }
        };
        contacts.setOnClickListener(listener);
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
        Toast.makeText(PartyActivity.this,
                String.valueOf(selectedItem),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int contactIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            movie_party.setContactName(cursor.getString(contactIndex));
            ArrayList<String> contact_list = movie_party.getContacts();
            Iterator<String> itr = contact_list.iterator();
            String con = "";
            while (itr.hasNext()) {
                con = con + itr.next() + " ";
            }
            people.setText(con);
        }

    }

}
