package com.drake.rxbus.exmaple

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.drake.rxbus.observerEvent
import com.drake.rxbus.sendEvent
import com.drake.rxbus.sendTag
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)



        btn_send_tag.setOnClickListener {
            Toast.makeText(this, "已发送", Toast.LENGTH_SHORT).show()
            sendTag("name_event")
        }


        btn_send_event.setOnClickListener {
            Toast.makeText(this, "已发送", Toast.LENGTH_SHORT).show()
            sendEvent(Event("金城武"))
        }

    }
}
