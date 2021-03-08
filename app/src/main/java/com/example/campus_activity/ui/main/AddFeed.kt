package com.example.campus_activity.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.campus_activity.R

class AddFeed : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_feed)

        val feed : TextView = findViewById(R.id.new_feed_text_view)
        val spinner : Spinner = findViewById(R.id.list_club_spinner)
        val spinnertext : TextView = findViewById(R.id.spinner_text_view)

        val testClubs = resources.getStringArray(R.array.clubs)
        val adapter : ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.clubs,
            android.R.layout.simple_gallery_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                Toast.makeText(this@AddFeed,
                    " " + testClubs[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
                Toast.makeText(this@AddFeed, "Nothing Selected", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
