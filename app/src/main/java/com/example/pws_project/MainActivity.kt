package com.example.pws_project

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    lateinit var cont_recyclerView : RecyclerView
    lateinit var cont_fab : FloatingActionButton


    val contact_list : MutableList<Contact_Class> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cont_recyclerView = findViewById(R.id.recycle_contact_list)
        cont_fab = findViewById(R.id.fab_contact)

        cont_recyclerView.layoutManager = LinearLayoutManager(this)



        //Checking for permission
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this,
                Array(1) { Manifest.permission.READ_CONTACTS },
                111
            )
        }
        else{
            getContacts()
        }


        cont_fab.setOnClickListener{
            getDialog()
        }

//

    }

    private fun getDialog() {
        val dialogView = layoutInflater.inflate(R.layout.fab_add_contact, null)
        val customDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .show()

        val btAddCancel = customDialog.findViewById<Button>(R.id.bt_add_cancel)
        btAddCancel.setOnClickListener{
            customDialog.dismiss()
        }

        val btAddContact = customDialog.findViewById<Button>(R.id.bt_add)
        val edAddName = customDialog.findViewById<EditText>(R.id.ed_addName)
        val edAddNumber = customDialog.findViewById<EditText>(R.id.ed_addNumber)
        btAddContact.setOnClickListener {

            if(!edAddName.text.toString().isEmpty()&&!edAddNumber.text.toString().isEmpty()){
                var con_intent = Intent(Intent.ACTION_INSERT)
                con_intent.setType(ContactsContract.RawContacts.CONTENT_TYPE)
                con_intent.putExtra(ContactsContract.Intents.Insert.NAME, edAddName.text.toString())
                con_intent.putExtra(
                    ContactsContract.Intents.Insert.PHONE,
                    edAddNumber.text.toString()
                )
                if(con_intent.resolveActivity(packageManager)!=null) {
                    startActivity(con_intent)
                }
                else{
                    Toast.makeText(applicationContext, "No supportable App", Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(applicationContext, "Please fill all the fields", Toast.LENGTH_LONG).show()
            }

            customDialog.dismiss()

            finish();
            startActivity(getIntent());

        }

    }

    //request again for permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            getContacts()
        }
        else{
            Toast.makeText(applicationContext, "Please Grant Permission", Toast.LENGTH_LONG).show()
        }
   }

    private fun getContacts()
    {
        //Cursor - contacts
        val contacts = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        )
        if (contacts != null) {
            while(contacts.moveToNext()){
                val conName = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val conNumber = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                val con_Object = Contact_Class()
                    con_Object.c_name = conName
                    con_Object.c_number = conNumber
                contact_list.add(con_Object)

            }
            cont_recyclerView.adapter = Contact_Adapter(contact_list, this)
            contacts.close()
        }


    }


}