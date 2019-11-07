package com.drake.rxbus.exmaple

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.drake.rxbus.observerEvent
import com.drake.rxbus.observerTag
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        observerEvent<Event> {
            tv_receive_event.text = "事件: $this"
        }

        observerTag("name_event") {
            tv_receive_tag.text = "标签: $this"
        }


        btn_open.setOnClickListener { startActivity(Intent(this, Main2Activity::class.java)) }
    }
}
