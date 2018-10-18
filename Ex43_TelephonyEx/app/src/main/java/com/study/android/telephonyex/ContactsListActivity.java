package com.study.android.telephonyex;

import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;

import static android.provider.ContactsContract.*;

public class ContactsListActivity extends AppCompatActivity {

    private static  final String TAG = "lecture";

    AddressAdapter adapter;
    ListView mContactList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);

        adapter = new AddressAdapter(this);

        mContactList = findViewById(R.id.contactsList);
        mContactList.setAdapter(adapter);
        mContactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddressItem item = (AddressItem) adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "selected : " + item.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        populateContactList();
    }

    // 주소록을 현재 선택된 계정으로 채운다.
    private void populateContactList() {
        // 연락처를 생성한다.
        Cursor cursor = getContacts();

        // 연락처를 하나씩 처리한다.
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor
                    .getColumnIndex(Contacts._ID));

            String name = cursor.getString(cursor
                    .getColumnIndex(Contacts.DISPLAY_NAME));

            // 연락처의 전화번호 데이터를 구한다.
            String telNum = retrieveContactNumber(id);
            Bitmap photo = retrieveContactPhoto(id);

            AddressItem item1 = new AddressItem(name, telNum, photo);
            adapter.addItem(item1);
        }
    }

    // 현재 선택된 계정의 전체 주소록 리스트를 구한다.
    private Cursor getContacts()
    {
        Uri uri = Contacts.CONTENT_URI;
        String[] projection = new String[] {
                Contacts._ID,
                Contacts.DISPLAY_NAME,
        };
        return managedQuery(uri, projection, null, null, null);
    }

    // 아이디가 contactId인 연락처의 전화번호만을 구한다.
    private String retrieveContactNumber(String contactId) {

        Cursor cursor1 = getContentResolver()
                .query(Data.CONTENT_URI,
                        new String[] { Data._ID, Phone.NUMBER, Phone.TYPE,
                                Phone.LABEL },
                        Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"
                                + Phone.CONTENT_ITEM_TYPE + "'",
                        new String[] { contactId }, null);;

        String telNum = "";
        if (cursor1.moveToFirst()) {
            telNum = cursor1.getString(cursor1
                    .getColumnIndex(Phone.NUMBER));
        }
//        while (cursor1.moveToNext()) {
//            telNum = cursor1.getString(cursor1
//                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//        }
        cursor1.close();

        return telNum;
    }

    private Bitmap retrieveContactPhoto(String contactId) {

        Bitmap photo = null;

        try {
            // Get photo of contactId as input stream:
            InputStream inputStream = Contacts.openContactPhotoInputStream (
                    getContentResolver(),
                    ContentUris.withAppendedId(
                            Contacts.CONTENT_URI,
                            new Long(contactId)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return photo;
    }

}
