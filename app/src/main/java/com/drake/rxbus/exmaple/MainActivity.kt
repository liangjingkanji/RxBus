package com.drake.rxbus.exmaple

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.drake.rxbus.observeEvent
import com.drake.rxbus.observeTag
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // 事件 + 标签
        observeEvent<Event>("refresh_event") {
            tv_receive_event.text = "事件: $event"

            Log.d("日志", "    event = $event, tag = $tag")
        }

        // 事件
        observeEvent<Event> {
            Log.d("日志", "   this = $event")
        }

        // 标签
        observeTag("refresh_event") {
            tv_receive_tag.text = "标签: $this"

            Log.d("日志", "   this = $this")
        }

        btn_open.setOnClickListener { startActivity(Intent(this, Main2Activity::class.java)) }

    }
}