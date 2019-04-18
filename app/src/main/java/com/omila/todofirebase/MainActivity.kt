package com.omila.todofirebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.*
import java.io.ObjectOutputStream
import java.util.*
import kotlin.coroutines.experimental.coroutineContext

class MainActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemLongClickListener {

    companion object {
        public lateinit var itemT: EditText
        public lateinit var addBtn: Button
        public lateinit var voiceBtn: Button
        public lateinit var itemList: ListView
        public lateinit var  items: ArrayList<String>
        public  lateinit var adapter: ArrayAdapter<String>
        val FILENAME = "listinfo.dat"
        fun writeData(s: String){
            var itemEntered: String=s
            adapter.addAll(itemEntered)

            itemT.text=null

        }
    }
    /*private lateinit var itemT: EditText
    private lateinit var addBtn: Button
    private lateinit var voiceBtn: Button
    private lateinit var itemList: ListView



    protected lateinit var  items: ArrayList<String>
    public  lateinit var adapter: ArrayAdapter<String>
    val FILENAME = "listinfo.dat"*/
    private val SPEECH_REQUEST_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        itemT   =findViewById<EditText>(R.id.item_edit_text)
        addBtn=findViewById<Button>(R.id.add_button)
        voiceBtn=findViewById<Button>(R.id.voice_button)
        itemList=findViewById<ListView>(R.id.items_list)

        itemT.setBackgroundColor(49151);
        items= FileHelper.readData(this )
        adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items)
        itemList.adapter=adapter


        addBtn.setOnClickListener(this)
        voiceBtn.setOnClickListener(this)
        itemList.setOnItemLongClickListener(this);
       // itemList.setOnItemClickListener(this);
    }

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        items.removeAt(position)
        val fos = this.openFileOutput(FILENAME, Context.MODE_PRIVATE)//ovo je pomoglo da se obrisani item-i stvarno izbrisu i iz fajla, inace bi se vracali nakon restar aplikacije
        val oos = ObjectOutputStream(fos)
        oos.writeObject(items)
        oos.close()
        adapter.notifyDataSetChanged()
        Toast.makeText(this,"Item removed",Toast.LENGTH_SHORT).show()
        return true;
    }
    /*override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        addBtn.text= "Save"
        items.removeAt(position)
        adapter.notifyDataSetChanged()
        Toast.makeText(this,"Item removed",Toast.LENGTH_SHORT).show()
    }*/
    override fun onClick(v: View) {
        when(v.id){
            R.id.add_button-> {
                var itemEntered: String=itemT.text.toString()
                adapter.addAll(itemEntered)

                itemT.text=null
                FileHelper.writeData(items,this)

                Toast.makeText(this,"Item added",Toast.LENGTH_SHORT ).show()
            }
            R.id.voice_button->{
                Toast.makeText(this,"Voice Clicked",Toast.LENGTH_SHORT).show()
                displaySpeechRecognizer()
            }
        }
    }
    private fun displaySpeechRecognizer() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        }
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "sr-RS")
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE)
    }
   /* fun writeData(s: String){
        var itemEntered: String=s
        adapter.addAll(itemEntered)

        itemT.text=null
        FileHelper.writeData(items,this)
    }*/
      override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
          if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
              val spokenText: String? =
                  data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).let { results ->
                      results[0]
                  }
              // Do something with spokenText
              itemT.setText(spokenText)
          }
          super.onActivityResult(requestCode, resultCode, data)
      }
}
