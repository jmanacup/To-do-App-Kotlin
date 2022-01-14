package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.apache.commons.io.FileUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils.writeLines
import org.apache.commons.io.IOUtils.writeLines
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()

    lateinit var adapter: TaskItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{

            override fun onItemLongClicked(position: Int) {
                // remove the item from the list
                listOfTasks.removeAt(position)
                // notify the adapter
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

        loadItems()
        //look up receycler view in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        //create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //set up button and input field for user to enter tasks and add to the list
        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        //get a reference and setting on click listener
        findViewById<Button>(R.id.button).setOnClickListener{

            // Grab text
            val userInputtedTask = inputTextField.text.toString()

            //add the string to the list
            listOfTasks.add(userInputtedTask)

            //notify the adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //reset text field
            inputTextField.setText("")

            saveItems()
        }

    }

    //save the data that the user inputted
    //By write and read from a file

    //Create a method to get the data file
    fun getDataFile() : File {

        //every line represents the a specific task
        return File(filesDir, "data.txt")
    }

    // Load the items by reading every line in the data file
    fun loadItems(){
        try {
            listOfTasks =
                FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException : IOException){
            ioException.printStackTrace()
        }
    }
    // Save items by writing them into our data file
    fun saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }
}